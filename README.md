# OPLA-Tool

![OtimizesUEM](https://raw.githubusercontent.com/SBSE-UEM/OPLA-Tool/master/logo-grupo-pesquisa.png)

## Description

This project was created from the project 
```sh
https://github.com/SBSE-UEM/OPLA-Tool-Spyke
```

## Requirements
Before to compile the code, you need to install the following softwares on your PC:
- Java Development Kit (Version >= 6)
- Git - http://git-scm.com
- Maven - http://maven.apache.org (Version >= 3.5)

## How to Build
This section show the step-by-step that you should follow to build the OPLA-Tool. 

- Create a directory to build OPLA-Tool:
```sh
mkdir opla-tool
```
- Access the folder:
```sh
cd opla-tool
```
- Download all projects:
```sh
git clone https://github.com/SBSE-UEM/OPLA-Tool.git
```
- Compile
```sh
mvn clean install Obs: If it is the first run, execute **mvn clean** first to install local dependencies
```
- Open OPLA-Tool:
```sh
java -jar modules/opla-gui/target/opla-gui-1.0.0-SNAPSHOT-jar-with-dependencies
```

## How to open the PLAs
```sh
Download and Install the Eclipse Papyrus Luna RS2: https://www.eclipse.org/papyrus/download.html
Import the PLAs: https://www.youtube.com/watch?v=9mmPUagHjM8
```


## How to import into eclipse IDE
```sh
mvn eclipse:clean
```

Import into eclipse IDE using Maven Project Type

```html
File > Import > Maven > Exists Maven Project > Select the directory created for build OPLA-Tool
```
## How to contribute to this project

Make Fork this project and create a Pull Request with your changes.
https://github.com/SBSE-UEM/OPLA-Tool/blob/master/Contributing.pdf

### Implementing a new Objective Function

- Create the persistent entity into the opla-domain > metric.
- Every Objective Function must inherit the class BaseMetric.

```java
@Entity
@Table(name = "myobj_metrics")
public class MyObjMetric extends BaseMetric {

    private static final long serialVersionUID = 1L;

    @Column(name = "value1")
    private Double value1;

    @Column(name = "value2")
    private Double value2;

    public AcompMetric(String idSolution, Execution execution, Experiment experiement) {
        super(idSolution, execution, experiement);
    }
    // GETTERS AND SETTERS
}
```

- Create the service and repository of your objective function into the opla-persistence
- Create the resource inside the opla-api.

- Add the metric into Metrics Enum 
```java
public enum Metrics {
    // another,
    MYOBJ;
}
```

- The implementation of metrics and the objective function must be in opla-core > jmetal4 > metrics.
- The class must inherit the BaseMetricResults. Read the comments in the code below.
```java
public class MYOBJ extends BaseMetricResults {

    public MYOBJ(Architecture architecture) {
        super(architecture);
        //Code as Example...
        double aclassFitness = 0.0;
        ClassDependencyIn CDepIN = new ClassDependencyIn(architecture);
        ClassDependencyOut CDepOUT = new ClassDependencyOut(architecture);
        aclassFitness = CDepIN.getResults() + CDepOUT.getResults();
        //Always set the results and access using the getResults();
        this.setResults(aclassFitness);
    }
}
```

- Add the instance your obj. function inside the class MetricsEvaluation as a static method that returns a Double value. 
- Also add 
```java
public class MetricsEvaluation {
    public static List<Fitness> evaluate(List<String> selectedMetrics, Solution solution) {
             // ... inside the for, add you method call
                 switch (metric) {
                     case MYOBJ:
                        fitnesses.add(new Fitness(MetricsEvaluation.evaluateMYOBJ((Architecture) solution.getDecisionVariables()[0])));
                 }
            //..
    }

    //...
    public static Double evaluateMYOBJ(Architecture architecture) {
        return new MYOBJ(architecture).getResults();
    }
    //...
}
```

- Add the list of metrics that contains your persistent objective 
```java
public class AllMetrics {
    
    private List<MyObjMetric> myObj = new ArrayList<>();
    // GETTERS AND SETTERS
}
```

- Add the link of the core with the persistence in class opla-core > persistence > Persistence
```java
@Service
public class Persistence {

    private final MyObjService myObjService;
    // constructor and another methods
    
    public void save(AllMetrics allMetrics, List<String> list) {
    // ...
            if (list.contains(Metrics.MYOBJ))
                myObjService.saveAll(allMetrics.getMyObj());
    }
}
```
- Implement the tests in the core inside the MetricsTest
```java
@Service
public class Persistence {

    private final MyObjService myObjService;
    // constructor and another methods
    
    public void save(AllMetrics allMetrics, List<String> list) {
    // ...
            if (list.contains(Metrics.MYOBJ))
                myObjService.saveAll(allMetrics.getMyObj());
    }
}
```