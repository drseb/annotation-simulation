package drseb.exception;

import hpo.ItemId;

public class DiseaseNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DiseaseNotFoundException(ItemId id) {
		super("No disease with annotations found for ID " + id.toString());
	}

}
