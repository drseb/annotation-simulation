package drseb;

import java.util.HashMap;

import hpo.DiseaseEntry;
import hpo.DiseaseId;
import hpo.HpoDataProvider;
import ontologizer.go.Ontology;

/**
 * Main entry point.
 * 
 * @author Sebastian Koehler
 *
 */
public class AnnotationSimulator {

	private Ontology ontology;
	private HashMap<DiseaseId, DiseaseEntry> annotations;

	public AnnotationSimulator(String oboOntology, String annotationFile, String separator, int objectIdColumn, int termColumn) {
		System.err.println("not implemented so far...");
	}

	public AnnotationSimulator(String oboOntology, String annotationFile, OntologyProjectType type) {

		if (type == null) {
			throw new IllegalArgumentException(OntologyProjectType.class.toString() + " cannot be null");
		}

		if (type.equals(OntologyProjectType.HPO)) {
			HpoDataProvider hpoapi = new HpoDataProvider();
			hpoapi.setOboFile(oboOntology);
			hpoapi.setAnnotationFile(annotationFile);
			hpoapi.parseOntoAndAssociations();

			this.ontology = hpoapi.getHpo();
			this.annotations = hpoapi.getDiseaseId2entry();
		}
		else {
			System.err.println("not implemented so far...");
		}

	}

	public String getHelloWorld() {
		return "Hello World";
	}

}
