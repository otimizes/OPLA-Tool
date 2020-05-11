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

- Create the persistent entity into the opla-domain > objectivefunctions.
- Every Objective Function must inherit the class ObjectiveFunctionDomain.

```java
@Entity
@Table(name = "myobj_obj")
public class MYOBJObjectiveFunction extends ObjectiveFunctionDomain {

    private static final long serialVersionUID = 1L;

    @Column(name = "value1")
    private Double value1;

    @Column(name = "value2")
    private Double value2;

    public MyObjectiveFunction(String idSolution, Execution execution, Experiment experiement) {
        super(idSolution, execution, experiement);
    }
    // GETTERS AND SETTERS
}
```

- Create the service and repository of your objective function into the opla-persistence > |repository|service| > objectivefunctions
- Create the resource inside the opla-api > resource > objectivefunctions.

- The implementation of metrics must be inside a package in opla-core > jmetal4 > metrics.
- The implementation of your objective function must be in opla-core > jmetal4 > metrics > objectivefunctions.
- The implementation class must inherit the ObjectiveFunctionImplementation and must be UPPERCASE. Read the comments in the code below.
```java
public class MYOBJ extends ObjectiveFunctionImplementation {

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

- Add your metrics into Metrics Enum and the Objective Function into the ObjectiveFunctions Enum.
```java
public enum ObjectiveFunctions implements ObjectiveFunctionsLink {
// ...
    MYOBJ {
        @Override
        public Double evaluate(Architecture architecture) {
            return new MYOBJ(architecture).getResults();
        }

        @Override
        public ACLASSObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement,
                                             Architecture arch) {
            MYOBJObjectiveFunction myobj = new MYOBJObjectiveFunction(idSolution, Execution, experiement);
            myobj.setSumClassesDepIn(Metrics.SumClassDepIn.evaluate(arch));
            myobj.setSumClassesDepOut(Metrics.SumClassDepOut.evaluate(arch));
            return aclass;
        }
    }
}
```

- Implement the tests in the core inside the MetricsTest
- ** ATTENTION ** Use the exactly name of the object function (in enum ObjectiveFunctions) as prefix of your resource, service and repository. Also use the pattern of posfix. It is used reflection to facilitate the development of new objective functions. 
-- Example: 
- MYOBJ.java -> Implementation of the objective function
- MYOBJObjectiveFunction.java -> Domain of the objective function
- MUOBJObjectiveFuntionRepository -> Repository of the objective function
- MYOBJObjectiveFunctionService -> Service of the objective function
- MYOBJObjectiveFunctionResource -> Resource of the objective function
- It will appears in the front-end, at the Objective functions section

### Implementing a new Optimization Algorithm approach
- Put your metaheuristic in jmetal4 > metaheuristics > myoptalgpackage
- Observe how was implemented the existent algorithms, following the current steps 
- Create your config in jmetal4 > experiments > base inheriting the ExperimentCommonConfigs
- Use the name of optimization algorithm config with the posfix Configs, ex: MyOptAlgConfigs
- Create your base to the Optimization Algorithm in jmetal4 > experiments > base
- Use the name of optimization algorithm base with the posfix OPLABase, ex: MyOptAlgOPLABase
- In order to link the algorithm in the api, it is necessary to create the gateway into the opla-api > gateway
- Use the name of the gateway with the posfix Gateway, ex: MyOptAlgGateway
- Add the gateway class in enum OptimizationAlgorithm, implementing the method getType
- It will appears in the front-end, at the Settings section

### Implementing a new Mutation Operator

- Create your class implementing IMutationOperator into jmetal4 > operators > mutation
- Insert the instance of it in jmetal4 > operators > FeatureMutationOperators enum.
- It will appears in the front-end, at the Mutation Operators section

### Implementing new methods in JMetal
- If you want to add new methods in SolutionSet, implement them at the core/OPLASolutionSet.java

### Implementing tests and main classes
- Every created method need to be in the tests at the repective module
- You do not need to remove the main classes, but you must to maintain them in the package named main in tests