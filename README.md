# OPLA-Tool

![OPLA-Tool](https://raw.githubusercontent.com/otimizes/OPLA-Tool/master/modules/opla-front/src/assets/logo.png)

## Description

This project was created to automate the Multi-Objective Optimization Approach for PLA Design (MOA4PLA).

## Requirements
Before to compile the code, you need to install the following softwares on your PC:
- Java - [Java Development Kit (Version >= 6)](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)
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
git clone https://github.com/otimizes/OPLA-Tool.git
```
- Compile
```sh
mvn clean install Obs: If it is the first run, execute **mvn clean** first to install local dependencies
```
- Open OPLA-Tool:
```sh
java -jar modules/opla-api/target/opla-api-1.0.0-SNAPSHOT.jar
```

## How to use the tool
[\<Click here to see how using the tool\>](https://github.com/otimizes/OPLA-Tool/blob/master/USAGE.md)

## How to open the PLAs

- Download and Install the [Eclipse Papyrus Luna RS2](https://www.eclipse.org/papyrus/download.html) and [Import the PLAs](https://www.youtube.com/watch?v=9mmPUagHjM8)



## How to import into eclipse IDE
```sh
mvn eclipse:clean
```

Import into eclipse IDE using Maven Project Type

```html
File > Import > Maven > Exists Maven Project > Select the directory created for build OPLA-Tool
```
## How to contribute to this project

Make Fork this project and create a Pull Request with your changes 

[\<Click here to see how to contribute\>](https://github.com/otimizes/OPLA-Tool/blob/master/CONTRIBUTING.md).

## Documentation

[\<Click here to access the backend documentation\>](https://otimizes.github.io/OPLA-Tool/docs/index.html)

[\<Click here to access the frontend documentation\>](https://otimizes.github.io/OPLA-Tool/docs/front/index.html)

### Implementing a new Objective Function 

- [\<Click here to see the implemented Objective Functions\>](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/core/jmetal4/metrics/ObjectiveFunctions.html)
- Create the persistent entity into the [opla-domain > objectivefunctions](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/domain/entity/objectivefunctions/package-summary.html).
- Every Objective Function must inherit the class [ObjectiveFunctionDomain](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/domain/entity/objectivefunctions/ObjectiveFunctionDomain.html).

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

- Create the service and repository of your objective function into the [opla-persistence > service > objectivefunctions](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/persistence/service/objectivefunctions/package-summary.html) and [opla-persistence > repository > objectivefunctions](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/persistence/repository/objectivefunctions/package-summary.html)
- Create the resource inside the [opla-api > resource > objectivefunctions](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/api/resource/objectivefunctions/package-summary.html).

- The implementation of metrics must be inside a package in [opla-core > jmetal4 > metrics](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/core/jmetal4/metrics/package-summary.html).
- The implementation of your objective function must be in [opla-core > jmetal4 > metrics > objectivefunctions](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/core/jmetal4/metrics/objectivefunctions/package-summary.html).
- The implementation class must inherit the [ObjectiveFunctionImplementation](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/core/jmetal4/metrics/ObjectiveFunctionImplementation.html) and must be UPPERCASE. Read the comments in the code below.
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

- Add your metrics into Metrics Enum and the Objective Function into the [ObjectiveFunctions](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/core/jmetal4/metrics/ObjectiveFunctions.html) Enum.
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
- ** ATTENTION ** Use the exactly name of the object function (in enum [ObjectiveFunctions](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/core/jmetal4/metrics/ObjectiveFunctions.html)) as prefix of your resource, service and repository. Also use the pattern of posfix. It is used reflection to facilitate the development of new objective functions. 
-- Example: 
- MYOBJ.java -> Implementation of the objective function
- MYOBJObjectiveFunction.java -> Domain of the objective function
- MUOBJObjectiveFuntionRepository -> Repository of the objective function
- MYOBJObjectiveFunctionService -> Service of the objective function
- MYOBJObjectiveFunctionResource -> Resource of the objective function
- It will appears in the front-end, at the Objective functions section

### Implementing a new Optimization Algorithm
- [\<Click here to see the implemented Optimization algorithms\>](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/api/gateway/OptimizationAlgorithms.html)
- Put your metaheuristic in jmetal4 > metaheuristics > youralgorithmpackage
- Observe how was implemented the existent algorithms, following the current steps 
- Create your config in [jmetal4 > experiments > base](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/core/jmetal4/experiments/base/package-summary.html) inheriting the ExperimentCommonConfigs
- Use the name of optimization algorithm config with the posfix Configs, ex: MyOptAlgConfigs
- Create your base to the Optimization Algorithm in [jmetal4 > experiments > base](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/core/jmetal4/experiments/base/package-summary.html)
- Use the name of optimization algorithm base with the posfix OPLABase, ex: MyOptAlgOPLABase
- In order to link the algorithm in the api, it is necessary to create the gateway into the [opla-api > gateway](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/api/gateway/package-frame.html)
- Use the name of the gateway with the posfix Gateway, ex: MyOptAlgGateway
- Add the gateway class in enum [OptimizationAlgorithms](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/api/gateway/OptimizationAlgorithms.html), implementing the method getType
- It will appears in the front-end, at the Settings section

### Implementing a new Mutation Operator
- [\<Click here to see the implemented Mutation Operators\>](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/core/jmetal4/operators/MutationOperators.html)
- Create your class implementing [IMutationOperator](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/core/jmetal4/operators/mutation/IMutationOperator.html) into [jmetal4 > operators > mutation](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/core/jmetal4/operators/mutation/package-summary.html)
- Insert the instance of it in [jmetal4 > operators > MutationOperators](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/core/jmetal4/operators/MutationOperators.html) enum.
- It will appears in the front-end, at the Mutation Operators section

### Implementing a new Crossover Operator
- [\<Click here to see the implemented Crossover Operators\>](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/core/jmetal4/operators/CrossoverOperators.html)
- Create your class implementing [IMutationOperator](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/core/jmetal4/operators/mutation/IMutationOperator.html) into [jmetal4 > operators > mutation](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/core/jmetal4/operators/mutation/package-summary.html)
- Insert the instance of it in [jmetal4 > operators > CrossoverOperators](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/core/jmetal4/operators/CrossoverOperators.html) enum.
- It will appears in the front-end, at the Mutation Operators section

### Implementing new methods in JMetal
- If you want to add new methods in [SolutionSet](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/core/jmetal4/core/SolutionSet.html), implement them at the [OPLASolutionSet](https://otimizes.github.io/OPLA-Tool/docs/br/otimizes/oplatool/core/jmetal4/core/OPLASolutionSet.html)

### Implementing tests and main classes
- Every created method need to be in the tests at the respective module
- You do not need to remove the main classes, but you must to maintain them in the package named main in tests