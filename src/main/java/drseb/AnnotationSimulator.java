package drseb;

import java.util.ArrayList;
import java.util.HashMap;

import drseb.exception.DiseaseNotFoundException;
import hpo.DiseaseEntry;
import hpo.DiseaseId;
import hpo.DiseaseId.DiseaseDatabase;
import hpo.HpoDataProvider;
import hpo.QueryModification;
import ontologizer.go.Ontology;
import ontologizer.go.Term;
import util.HpoHelper;

/**
 * Main entry point.
 * 
 * @author Sebastian Koehler
 *
 */
public class AnnotationSimulator {

	private Ontology ontology;
	private HashMap<DiseaseId, DiseaseEntry> annotations;
	private HpoHelper hpoHelper;
	private QueryModification queryModification;

	public AnnotationSimulator(String oboOntology, String annotationFile, String separator, int objectIdColumn, int termColumn) {
		throw new RuntimeException("not implemented so far...");
	}

	public AnnotationSimulator(String oboOntology, String annotationFile, OntologyProjectType type) {

		if (type == null) {
			throw new IllegalArgumentException(OntologyProjectType.class.toString() + " cannot be null");
		}

		if (type.equals(OntologyProjectType.HPO)) {
			HpoDataProvider hpoapi = new HpoDataProvider();
			hpoapi.setOboFile(oboOntology);
			hpoapi.setAnnotationFile(annotationFile);
			hpoapi.parseOntologyAndAssociations();

			this.hpoHelper = hpoapi.getHpoHelper();
			this.annotations = hpoapi.getDiseaseId2entry();
			if (hpoHelper == null || hpoHelper.getHpo() == null) {
				throw new RuntimeException("hpo-helper or hpo null. invalid state");
			}
			if (annotations == null || annotations.keySet().size() < 1) {
				throw new RuntimeException("annotations have not been parse properly. invalid state");
			}

		}
		else {
			throw new RuntimeException("not implemented so far...");
		}

		queryModification = new QueryModification(hpoHelper.getOrganAbnormalitySubgraph(), hpoHelper.getOrganAbnormalitySubgraphSlim());

	}

	public int getNumberObjectsAnnotated() {
		return annotations.keySet().size();
	}

	public int getNumberOfAnnotations() {
		return annotations.values().stream().mapToInt(e -> e.getAnnotations().size()).sum();
	}

	public ArrayList<ArrayList<Term>> simulatePatients(DiseaseDatabase diseaseDb, String diseaseIdent, int numberOfPatients,
			double fractionOfNoiseTerms, double chanceOfBeingMappedUp, int querySize) throws DiseaseNotFoundException {
		return simulatePatients(diseaseDb, diseaseIdent, numberOfPatients, fractionOfNoiseTerms, chanceOfBeingMappedUp, querySize, querySize);
	}

	public ArrayList<ArrayList<Term>> simulatePatients(DiseaseDatabase diseaseDb, String diseaseIdent, int numberOfPatients,
			double fractionOfNoiseTerms, double chanceOfBeingMappedUp, int lowerBoundQuerySize, int upperBoundQuerySize) throws DiseaseNotFoundException {

		DiseaseId id = new DiseaseId(diseaseDb, diseaseIdent);
		if (!annotations.containsKey(id)) {
			throw new DiseaseNotFoundException(id);
		}

		if (numberOfPatients < 1) {
			throw new IllegalArgumentException("Number of patients must be larger than 0.");
		}

		DiseaseEntry diseaseEntry = annotations.get(id);
		ArrayList<ArrayList<Term>> simulatedQueries = new ArrayList<>();
		for (int i = 0; i < numberOfPatients; i++) {
			ArrayList<Term> patientAnnotations = queryModification.generateAnnotationSet(diseaseEntry.getAnnotations(), fractionOfNoiseTerms,
					chanceOfBeingMappedUp, lowerBoundQuerySize, upperBoundQuerySize);
			simulatedQueries.add(patientAnnotations);
		}

		return simulatedQueries;
	}

}
