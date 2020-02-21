import {Config} from "./config";

export class OptimizationDto {
  algorithm: string = "NSGAII";
  archiveSize: number;
  description: string;
  mutation: boolean;
  mutationProbability: number;
  inputArchitecture: string;
  numberRuns: number;
  populationSize: number;
  maxEvaluations: number;
  crossover: boolean;
  crossoverProbability: boolean;
  interactive: boolean = false;
  maxInteractions: number = 0;
  firstInteraction: number = 0;
  intervalInteraction: number = 0;
  clusteringAlgorithm: string = "KMEANS";
  clusteringMoment: string = "POSTERIORI";
  mutationOperators: string[] = ["featureMutation","moveMethodMutation","addClassMutation","addManagerClassMutation","moveOperationMutation"];
  patterns: string[] = [];
  objectiveFunctions: string[] = [];
  config: Config;

  constructor() {}
}
