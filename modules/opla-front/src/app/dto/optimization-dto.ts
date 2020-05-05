import {Config} from "./config";

export class OptimizationDto {
  algorithm: string = "NSGAII";
  archiveSize: number;
  description: string;
  mutation: boolean;
  mutationProbability: number;
  inputArchitecture: string;
  numberRuns: number;
  papyrus: boolean = false;
  populationSize: number;
  maxEvaluations: number;
  crossover: boolean;
  crossoverProbability: boolean;
  interactive: boolean = false;
  maxInteractions: number = 3;
  firstInteraction: number = 3;
  intervalInteraction: number = 3 ;
  clusteringAlgorithm: string = "KMEANS";
  clusteringMoment: string = "POSTERIORI";
  mutationOperators: string[] = ["featureMutation","moveMethodMutation","addClassMutation","addManagerClassMutation","moveOperationMutation"];
  patterns: string[] = [];
  objectiveFunctions: string[] = [];
  config: Config = new Config();
  scopeSelection: string;

  constructor() {}
}
