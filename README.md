# License: CC BY-NC 4.0
[![License: CC BY-NC 4.0](https://licensebuttons.net/l/by-nc/4.0/80x15.png)](http://creativecommons.org/licenses/by-nc/4.0/)

# annotation-simulation
Code to simulated noisy and/or imprecise ontology-associations for objects, such as HPO-annotated patients with a particular disease.

# usage

## pom
add repo
```
<repositories>
	<repository>
		<id>compbio</id>
            	<url>http://compbio.charite.de/tl_files/maven</url>
	</repository>
</repositories>
```

add dependency
```
<dependency>
	    <groupId>annotation-simulation</groupId>
	    <artifactId>annotation-simulation</artifactId>
	    <version>0.0.1-SNAPSHOT</version>
</dependency>
```



Example code to generate a thousand HPO-annotated patients with some noise and imprecision

```
// init
AnnotationSimulator simulator = new AnnotationSimulator(ontologyFile, annotationFile, OntologyProjectType.HPO);

// the disease
DiseaseDatabase db = DiseaseDatabase.OMIM;
String id = "114030";

// simulate now
ArrayList<ArrayList<Term>> patients = simulator.simulatePatients(db, id, 1000, 0.2, 0.4, 2, 10);
		
```
