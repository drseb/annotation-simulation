package drseb.exception;

import hpo.DiseaseId;

public class DiseaseNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DiseaseNotFoundException(DiseaseId id) {
		super("No disease with annotations found for ID " + id.toString());
	}

}
