package drseb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import drseb.exception.DiseaseNotFoundException;
import hpo.HpoDataProvider;
import hpo.Item;
import hpo.ItemId;
import hpo.ItemId.ItemDatabase;
import hpo.QueryModification;
import ontologizer.go.Ontology;
import ontologizer.go.Term;
import sonumina.math.graph.SlimDirectedGraphView;
import util.HpoHelper;

/**
 * Main entry point.
 * 
 * @author Sebastian Koehler
 *
 */
public class AnnotationSimulator {

	private Ontology ontology;
	private SlimDirectedGraphView<Term> ontologySlim;

	private HashMap<ItemId, Item> annotations;
	private QueryModification queryModification;

	public AnnotationSimulator(String oboOntology, String annotationFile, String separator, int objectIdColumn, int termColumn) {
		throw new RuntimeException("not implemented so far...");
	}

	public AnnotationSimulator(String oboOntology, String annotationFile, OntologyProjectType type, long seedForRandomGenerator) {
		setupOntologyData(oboOntology, annotationFile, type);
		queryModification = new QueryModification(ontology, ontologySlim, seedForRandomGenerator);
	}

	public AnnotationSimulator(String oboOntology, String annotationFile, OntologyProjectType type) {
		setupOntologyData(oboOntology, annotationFile, type);
		queryModification = new QueryModification(ontology, ontologySlim);

	}

	private void setupOntologyData(String oboOntology, String annotationFile, OntologyProjectType type) {
		if (type == null) {
			throw new IllegalArgumentException(OntologyProjectType.class.toString() + " cannot be null");
		}

		if (type.equals(OntologyProjectType.HPO)) {
			HpoDataProvider hpoapi = new HpoDataProvider();
			hpoapi.setOboFile(oboOntology);
			hpoapi.setAnnotationFile(annotationFile);
			hpoapi.parseOntologyAndAssociations();

			HpoHelper hpoHelper = hpoapi.getHpoHelper();
			this.annotations = hpoapi.getDiseaseId2entry();
			if (hpoHelper == null || hpoHelper.getHpo() == null) {
				throw new RuntimeException("hpo-helper or hpo null. invalid state");
			}
			if (annotations == null || annotations.keySet().size() < 1) {
				throw new RuntimeException("annotations have not been parse properly. invalid state");
			}

			this.ontology = hpoHelper.getOrganAbnormalitySubgraph();
			this.ontologySlim = hpoHelper.getOrganAbnormalitySubgraphSlim();

		} else {
			throw new RuntimeException("not implemented so far...");
		}
	}

	public Set<ItemId> getAllObjectsAnnotated() {
		return annotations.keySet();
	}

	public List<ItemId> getRandomSetObjectsAnnotated(int numObjectsToReturn) {

		ArrayList<ItemId> allObjectsAnnotated = new ArrayList<ItemId>(getAllObjectsAnnotated());

		if (numObjectsToReturn > allObjectsAnnotated.size())
			numObjectsToReturn = allObjectsAnnotated.size();

		Collections.shuffle(allObjectsAnnotated, queryModification.getRandomNumberGenerator());

		List<ItemId> sublist = allObjectsAnnotated.subList(0, numObjectsToReturn - 1);
		return sublist;

	}

	public HashMap<ItemId, Item> getAnnotations() {
		return annotations;
	}

	public Ontology getOntology() {
		return ontology;
	}

	public int getNumberObjectsAnnotated() {
		return annotations.keySet().size();
	}

	public int getNumberOfAnnotations() {
		return annotations.values().stream().mapToInt(e -> e.getAnnotations().size()).sum();
	}

	public List<List<Term>> simulatePatients(ItemDatabase diseaseDb, String diseaseIdent, int numberOfPatients, double fractionOfNoiseTerms,
			double chanceOfBeingMappedUp, int querySize) throws DiseaseNotFoundException {
		return simulatePatients(diseaseDb, diseaseIdent, numberOfPatients, fractionOfNoiseTerms, chanceOfBeingMappedUp, querySize, querySize);
	}

	public List<List<Term>> simulatePatients(ItemId diseaseId, int numberOfPatients, double fractionOfNoiseTerms, double chanceOfBeingMappedUp,
			int lowerBoundQuerySize, int upperBoundQuerySize) throws DiseaseNotFoundException {
		return simulatePatients(diseaseId.getDiseaseDb(), diseaseId.getDiseaseIdInDb(), numberOfPatients, fractionOfNoiseTerms, chanceOfBeingMappedUp,
				lowerBoundQuerySize, upperBoundQuerySize);
	}

	public List<List<Term>> simulatePatients(ItemDatabase diseaseDb, String diseaseIdent, int numberOfPatients, double fractionOfNoiseTerms,
			double chanceOfBeingMappedUp, int lowerBoundQuerySize, int upperBoundQuerySize) throws DiseaseNotFoundException {

		ItemId id = new ItemId(diseaseDb, diseaseIdent);
		if (!annotations.containsKey(id)) {
			throw new DiseaseNotFoundException(id);
		}

		if (numberOfPatients < 1) {
			throw new IllegalArgumentException("Number of patients must be larger than 0.");
		}

		Item diseaseEntry = annotations.get(id);
		List<List<Term>> simulatedQueries = new ArrayList<>();
		for (int i = 0; i < numberOfPatients; i++) {
			List<Term> patientAnnotations = queryModification.generateAnnotationSet(diseaseEntry.getAnnotations(), fractionOfNoiseTerms,
					chanceOfBeingMappedUp, lowerBoundQuerySize, upperBoundQuerySize);
			simulatedQueries.add(patientAnnotations);
		}

		return simulatedQueries;
	}

}
