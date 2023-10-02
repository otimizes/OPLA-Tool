//package br.otimizes.oplatool.core.jmetal4.metaheuristics.nsgaIII;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class NSGAIII<S extends DoubleSolution> implements Algorithm<List<S>> {
//
//    private Problem<S> problem;
//
//    private int maxIterations;
//
//    private int populationSize;
//
//    private int divisions;
//
//    private double eta;
//
//    private CrossoverOperator<S> crossoverOperator;
//
//    private MutationOperator<S> mutationOperator;
//
//    private SelectionOperator<List<S>, S> selectionOperator;
//
//    private SolutionListEvaluator<S> evaluator;
//
//    private NonDominatedSolutionListArchive<S> archive;
//
//    public NSGAIII(Problem<S> problem, int maxIterations, int populationSize, int divisions, double eta,
//                   CrossoverOperator<S> crossoverOperator, MutationOperator<S> mutationOperator,
//                   SelectionOperator<List<S>, S> selectionOperator, SolutionListEvaluator<S> evaluator) {
//        this.problem = problem;
//        this.maxIterations = maxIterations;
//        this.populationSize = populationSize;
//        this.divisions = divisions;
//        this.eta = eta;
//        this.crossoverOperator = crossoverOperator;
//        this.mutationOperator = mutationOperator;
//        this.selectionOperator = selectionOperator;
//        this.evaluator = evaluator;
//        this.archive = new NonDominatedSolutionListArchive<S>(populationSize, new RankingAndCrowdingDistanceComparator<S>());
//    }
//
//    @Override
//    public void run() {
//        List<S> population = new ArrayList<>(populationSize);
//        List<S> offspringPopulation = new ArrayList<>(populationSize);
//        List<S> jointPopulation = new ArrayList<>(2 * populationSize);
//        List<S> matingPopulation = new ArrayList<>(2 * populationSize);
//        List<S> union = new ArrayList<>(2 * populationSize);
//
//        population = createInitialPopulation();
//        population = evaluatePopulation(population);
//
//        int iterations = 0;
//        while (iterations < maxIterations) {
//            offspringPopulation.clear();
//            for (int i = 0; i < populationSize; i++) {
//                List<S> parents = selectionOperator.execute(population);
//                S offspring = crossoverOperator.execute(parents.get(0), parents.get(1));
//                mutationOperator.execute(offspring);
//                offspringPopulation.add(offspring);
//            }
//
//            offspringPopulation = evaluatePopulation(offspringPopulation);
//
//            jointPopulation.clear();
//            jointPopulation.addAll(population);
//            jointPopulation.addAll(offspringPopulation);
//
//            NonDominatedSolutionListArchive<S> archive1 = new NonDominatedSolutionListArchive<>(populationSize, new RankingAndCrowdingDistanceComparator<S>());
//
//            SolutionListUtils.computeDominance(jointPopulation);
//            for (S solution : jointPopulation) {
//                archive1.add(solution);
//            }
//
//            List<S> population1 = new ArrayList<>(populationSize);
//            for (int i = 0; i < populationSize; i++) {
//                population1.add(
//                        archive1.get(i));
//            }
//
//            archive1.computeCrowdingDistances();
//
//            population = population1;
//            archive = archive1;
//
//            iterations++;
//        }
//    }
//
//    @Override
//    public List<S> getResult() {
//        return archive.getSolutionList();
//    }
//
//    @Override
//    public String getName() {
//        return "NSGAIII";
//    }
//
//    @Override
//    public String getDescription() {
//        return "NSGAIII algorithm";
//    }
//
//    private List<S> createInitialPopulation() {
//        List<S> population = new ArrayList<>(populationSize);
//        for (int i = 0; i < populationSize; i++) {
//            S solution = problem.createSolution();
//            population.add(solution);
//        }
//        return population;
//    }
//
//    private List<S> evaluatePopulation(List<S> population) {
//        population = evaluator.evaluate(population, problem);
//        return population;
//    }
//
//    public static void main(String[] args) throws JMetalException {
//        Problem<DoubleSolution> problem = new DTLZ2(10, 2);
//        int maxIterations = 250;
//        int populationSize = 100;
//        int divisions = 12;
//        double eta = 0.0075;
//        CrossoverOperator<DoubleSolution> crossoverOperator = new SBXCrossover(0.9, 20.0);
//        MutationOperator<DoubleSolution> mutationOperator = new PolynomialMutation(1.0 / problem.getNumberOfVariables(), 20.0);
//        SelectionOperator<List<DoubleSolution>, DoubleSolution> selectionOperator = new BinaryTournamentSelection<DoubleSolution>();
//        SolutionListEvaluator<DoubleSolution> evaluator = new SequentialSolutionListEvaluator<DoubleSolution>();
//
//        Algorithm<List<DoubleSolution>> algorithm = new NSGAIII<>(problem, maxIterations, populationSize, divisions, eta,
//                crossoverOperator, mutationOperator, selectionOperator, evaluator);
//
//        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
//
//        List<DoubleSolution> population = algorithm.getResult();
//
//        long computingTime = algorithmRunner.getComputingTime();
//
//        JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
//    }
