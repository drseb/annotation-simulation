package drseb;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestSimulator {

	private static AnnotationSimulator simulator;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("setUpBeforeClass");
		AnnotationSimulator simulator = new AnnotationSimulator("src/test/resources/hp.obo", "src/test/resources/phenotype_annotation.tab.gz",
				OntologyProjectType.HPO);
		TestSimulator.simulator = simulator;
	}

	@Test
	public void testNumberDiseases() {
		Assert.assertTrue("Number of disease must be higher than 9000", simulator.getNumberObjectsAnnotated() > 9000);
	}

	@Test
	public void testNumberAnnotations() {
		Assert.assertTrue("Number of annotation must be higher than 120,000", simulator.getNumberOfAnnotations() > 120000);
	}

}
