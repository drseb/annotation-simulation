package drseb;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestSimulator {

	private static AnnotationSimulator simulator;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		AnnotationSimulator simulator = new AnnotationSimulator("src/test/resources/hp.obo", "src/test/resources/phenotype_annotation.tab",
				OntologyProjectType.HPO);
		TestSimulator.simulator = simulator;
		System.out.println("initialised: " + simulator);
	}

	@Test
	public void test() {
		Assert.assertEquals(simulator.getHelloWorld(), "Hello World");

		// fail("Not yet implemented");
	}

}
