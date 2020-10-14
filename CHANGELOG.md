# Changelog

**Add your changes here
- Removed front Logs in portuguese
- Position of the logout button changed
- Added new mvn repo, on github

## [2.1.0] - 2020-09-15
- Added ASP implementation from https://github.com/tmadrigar/OPLA-Tool/

## [2.0.2] - 2020-09-09
- Allowed the selection of many crossover operators at the same time (using pseudo random)

## [2.0.1] - 2020-08-20
- Full compatibility with windows (cast double MathUtils and removed cast Node)
- Full compatibility with new java versions
- Added escaped file separator in file constants

## [2.0.0] - 2020-07-09
In this version, you are able to access the http://localhost:8080 and use the web interface to optimize PLAs.
It were fixed a lot of bugs and problems of the oldest version. Now, there is a documentation and MD files to assist 
you in the contribution of this project. Currently, the default modeling tool is the SMartyModeling present in the 
root path of this project. The packages were renamed to br.otimizes.oplatool.


### Report
- Added new croosover/mutation operators and new objective functions
- The packages were renamed to br.otimizes.oplatool
- Web interface in angular 8 (view usage.md tutorial)
- Every use have a login and the output directories are created according the user's token
- Clean code modules
- Java Comments
- Removed duplicated packages and classes
- Removed duplicated jmetal
- Implemented Chomma memetic algorithms
- Implements new objective functions from another authors 
- Add comment (named as description) to Interface, Class and Package (in SMartyModeling);
- Add mutex relationship that SMartyModeling has to OPLA-Tool;
- Correction in usage and abstraction relationship;
- New PLA file with Comment (description) in its structure and small change in package structure in .smty file;
- Change of encoding and decoding because of comments and mutex;
- in class MutationUtils Line 235 variabilities.clear(); commented. This code will delete variabilities of architecture smarty;
- modified remove interface and removeOnlyElement to force remove interface by id if cannot remove by hash
- modified function moveInterface to not add duplicated interface and remove interface from origin by id if fail to remove by hash
- modified function addExternalInterface from Package to not add duplicated interface
- created function findInterfaceByID
- NSGAII - save base hypervolume before start optimization
- OPLASolutionSet: add function to save link between output PLA name and fitness
- Comments in the code
- created class to save string to file and create temp and log directory
- GenerateArchitectureSMarty divided in many class
- Architecture divided in many class to implement method for Architecture
- Change Operation from Interface to Method (variable opetations and methods that has operation in name)
- Created ArchitecturBuilderSMarty for encoding a PLA ".smty" to Architecture
- The selection and call of "ArchitecturBuilder" or "ArchitecturBuilderSMarty" is in the "/core/../problem/OPLA".
- for the selection of encoding use the input PLA format (if .smty, use encoding from SMarty else, encoding actual from Papyrus).
- When the PLA is ".smty", set a variable isSMarty and toSMarty (from /architecture-representation/../representation/Architecture) to true (in ArchitecturBuilderSMarty)
- if isSMarty is true, for valiability utilize an list inside each architecture (each architecture has a list for valiability for) and not use the flyweight. the change of a valiability in a architecture will not affect other solutions.
- the variable toSMarty is used for decoding. If it is set true, decode the output to .smty (even if the origin PLA is from Papyrus)
- the decoding is called by /architecture-representation/../representation/Architecture in method save (use toSMarty to verify type for decoding) and save2(force save to .smty).
- Created GenerateArchitectureSMarty for encoding a PLA ".smty" to Architecture.
- created decoding "save2" to force save in SMarty format if need.
- create openTempArch() in Architecture to open an architecture in execiution time, saving the solution in output/TEMP/ and openning in SMarty Modeling, if SMartyModeling.jar is in the same directory. When use this function, stop the execution of OPLA-Tool while SMartyModeling is open.
- create recursive function for subpackages.
- created class TypeSMarty because SMarty Modeling use a list of all types (predefined ou new) in its structure.
- created many variable in the architecture to save info for the smarty that Papyrus not need.
- created other variable in other class from representation/ to save info like posX, posY, sizeX, sizeY, id if not exists and other variable that has in smarty and not in the version of OPLA-Tool
- created a variable isMandatory in element (set as true for default). in decoding use the valiability to find if the element is optional or not (when the input is from Papyrus).
- created directory PLASMarty to save PLA in SMarty format.
- the PLA with name has Atual is a PLA that is converted directy the PLA from Papyrus using encoding and decoding from OPLA-Tool to save in smty format.
- the agm1, agm2, mm1 and mm2 is get using the base the Image of the original PLA from Thelma and changed with Thelma supervision. (this changes inclued the addition of missing elements, stereotype and creation of constructor instead of an attribute, addition of stereotype to constructor method).
- the pla that has converted to smty is agm, mm and bet.
- the SMartyModeling.jar is in the project.
- in the decoding, resize and reorder all elements to not overlap other elements.
- in the decoding created new id for elements or relationship if not exists.
- in interactive, when create the list, save temporary architecture to /output/TEMP/ to be open in SMartyModeling if the selected tool is SMartyModeling instead of Papyrus.
- created relationship requires;
- the decoding has a log to save if some problem occur. The log only be saved if a problem occur. For each architecture is created an individual file that will be saved in output/TEMP/.
- the erro log inclued error like deletion of an relationship because some elements of relationship not exists in architecture.
- in NSGAII, before the creation of population, created a function to save the original fitness of solution (if need to compara after ou not find the original fitness in documents) in output/TEMP/.
- in SolutionSet, when call the function of architecture to save the solution, is called other function to save the link of fitness and output file name. This link is saved in output/TEMP/.
- **** you can use a Papyrus PLA for input and save in smarty format output if set the variable toSMarty in Architecture to true.
- **** Some PLA cannot be decoded to SMarty because of restriction of elements in SMartyModeling or OPLA-Tool.
- **** you can open an solution in execution time if call openTempArch() from Architecture.
- **** the error in the decoding can be found in output/TEMP/
- **** the name and value of the selected fitness can be found in output/TEMP/.
- the jar from SMartyModeling can be found in:
- https://github.com/leandroflores/demo_SMartyModeling_tool
- https://github.com/leandrofloress/demo_SMartyModeling_tool
- Feature driven mutation fix
- Max interactions
- Interval of interaction
- First Interaction
- Clustering Algorithm KMeans and DBSCAN
- Interactive package
- Graphics, tables, hypervolume corrected in Result and Experiment Tabs
- Internal Bloxpot
- Maven Dependencies and configurations updated
- Starting Clustering Module
- Papyrus initial integration
- JUnit initial tests
- Jar generate Adjusts
- Objective Functions implementation from João Version
- Command Line experiment 
    -  Example: <b>java -jar</b> opla-desktop-view-1.0.0-SNAPSHOT-jar-with-dependencies.jar 
            <b>algorithm</b>=NSGAII <br>
            <b>description</b>=teste <br>
            <b>mutation</b>=true <br>
            <b>mutationProbability</b>=0.9<br> 
            <b>inputArchitecture</b>=/home/wmfsystem/workspace/PLA/files/agm/agm.uml<br>
            <b>numberRuns</b>=2<br>
            <b>maxEvaluations</b>=10<br>
            <b>populationSize</b>=5<br>
            <b>crossover</b>=false<br>
            <b>crossoverProbability</b>=0.0<br>
            <b>clusteringMoment</b>=NONE<br>
            <b>clusteringAlgorithm</b>=KMEANS<br>
            <b>mutationOperators</b>=featureMutation,moveMethodMutation,addClassMutation,addManagerClassMutation,moveOperationMutation<br>
            <b>objectiveFunctions</b>=featureDriven,aclass,coe<br>
    
    
     java -jar opla-desktop-view-1.0.0-SNAPSHOT-jar-with-dependencies.jar algorithm=NSGAII objectiveFunctions=featureDriven,aclass,coe mutationOperators=featureMutation,moveMethodMutation,addClassMutation,addManagerClassMutation,moveOperationMutation description=teste mutation=true mutationProbability=0.9 inputArchitecture=/home/wmfsystem/workspace/PLA/files/agm/agm.uml numberRuns=2 maxEvaluations=5 populationSize=2 crossover=false crossoverProbability=0.0 clusteringMoment=NONE clusteringAlgorithm=KMEANS
     