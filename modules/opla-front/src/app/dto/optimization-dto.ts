import {Config} from "./config";

export class OptimizationDto {
  algorithm: string = "NSGAII";
  archiveSize: number;
  description: string;
  mutation: boolean;
  mutationProbability: number;
  inputArchitecture: string;
  numberRuns: number;
  architectureBuilder: any;
  papyrus: boolean = false;
  populationSize: number;
  maxEvaluations: number;
  crossover: boolean;
  crossoverProbability: boolean;
  interactive: boolean = false;
  maxInteractions: number;
  firstInteraction: number;
  intervalInteraction: number;
  clusteringAlgorithm: string = "KMEANS";
  clusteringMoment: string = "POSTERIORI";
  crossoverOperators: string[] = [];
  mutationOperators: string[] = ["FEATURE_DRIVEN_OPERATOR", "MOVE_METHOD_MUTATION", "MOVE_ATTRIBUTE_MUTATION", "MOVE_OPERATION_MUTATION", "ADD_CLASS_MUTATION", "ADD_MANAGER_CLASS_MUTATION"];
  patterns: string[] = [];
  objectiveFunctions: string[] = [];
  config: Config = new Config();
  scopeSelection: string;

  constructor() {
  }
}
