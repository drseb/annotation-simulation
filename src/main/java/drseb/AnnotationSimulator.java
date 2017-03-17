package drseb;

public class AnnotationSimulator {

	public AnnotationSimulator(String oboOntology, String annotationFile, String separator, int objectIdColumn, int termColumn) {

	}

	public AnnotationSimulator(String oboOntology, String annotationFile, OntologyProjectType type) {

		if (type == null) {
			throw new IllegalArgumentException(OntologyProjectType.class.toString() + " cannot be null");
		}

		if (type.equals(OntologyProjectType.HPO)) {

		}
		else {
			System.err.println("not implemented so far...");
		}

	}

}
