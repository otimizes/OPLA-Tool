# Changelog
## [1.0.0] - 2018-11-29
### Added
- Clustering Algorithm KMeans and Dbscan
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
     
