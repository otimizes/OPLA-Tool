package jmetal4.operators.crossover;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Interface;
import jmetal4.core.Solution;
import jmetal4.experiments.ExperimentCommomConfigs;
import jmetal4.util.PseudoRandom;

/**
 * 
 * @author Fernando
 *
 */
public class PLAComplementaryCrossover extends Crossover {

	private static final Logger LOG = Logger.getLogger(PLAComplementaryCrossover.class);

	private static final long serialVersionUID = 1L;

	private int numberOfObjetive;
	private Double crossoverProbability_ = null;

	public PLAComplementaryCrossover(Map<String, Object> parameters) {
		super(parameters);
		numberOfObjetive = (int) getParameter("numberOfObjectives");
		if (parameters.get("probability") != null) {
			crossoverProbability_ = (Double) getParameter("probability");
		}

	}

	public PLAComplementaryCrossover(Map<String, Object> parameters, ExperimentCommomConfigs configs) {
		this(parameters);
	}

	@Override
	public Object execute(Object object) throws Exception {
		Solution[] parents = (Solution[]) object;
		Solution father = parents[0];
		Solution mother = parents[1];

		LOG.info("Check Probabilty " + crossoverProbability_);
		if (PseudoRandom.randDouble() < crossoverProbability_) {
			doCrossover(father, mother);
		}

		parents[0] = father;
		parents[1] = mother;
		return parents;
	}

	private void selectByFitness(Solution father, Solution mother, List<Map<Integer, List<Solution>>> bests) {
		for (int i = 0; i < numberOfObjetive; i++) {
			Map<Integer, List<Solution>> map = new HashMap<>();
			double fitnessFather = father.getObjective(i);
			double fitnessMother = mother.getObjective(i);
			if (fitnessFather < fitnessMother) {
				LOG.info("Pai com melhor fitnes");
				map.put(i, Arrays.asList(father));
			} else if (fitnessFather > fitnessMother) {
				LOG.info("Mãe com melhor fitnes");
				map.put(i, Arrays.asList(mother));
			} else {
				LOG.info("Valores de fitness iguais");
				map.put(i, Arrays.asList(father, mother));
			}
			bests.add(map);
		}

	}

	private Solution selectRandom(Map<Integer, List<Solution>> solutions) {
		return solutions.entrySet().stream()
				.map(entry -> entry.getValue().get(PseudoRandom.randInt(0, solutions.entrySet().size() -1)))
				.findFirst()
				.get();
	}

	public void doCrossover(Solution father, Solution mother) {
		LOG.info("Executando crossover");

		List<Map<Integer, List<Solution>>> bests = new ArrayList<>();

		LOG.info("Quantidade de funções objetivo selecionadas " + numberOfObjetive);
		selectByFitness(father, mother, bests);

		if (numberOfObjetive > 2 && !bests.isEmpty()) {
			int index = numberOfObjetive - 1;
			Map<Integer, List<Solution>> solutionOne = bests.get(PseudoRandom.randInt(0, index));
			Map<Integer, List<Solution>> solutionTwo = bests.get(PseudoRandom.randInt(0, index));
			father = selectRandom(solutionOne);
			mother = selectRandom(solutionTwo);
		} else {
			father = selectRandom(bests.get(0));
			mother = selectRandom(bests.get(1));
		}

		Map<Integer, List<Solution>> randonSelectected = bests.get(PseudoRandom.randInt(0, bests.size()-1));
		mother = selectRandom(randonSelectected);
		LOG.info("Selecinou individuo aleatóriamente");

		Architecture architectureFather = (Architecture) father.getDecisionVariables()[0];
		Architecture architectureMother = (Architecture) mother.getDecisionVariables()[0];
		Architecture offSpring = architectureFather.deepClone();

		Set<Class> fatherClasses = architectureFather.getAllClasses();
		Set<Interface> fatherInterfaces = architectureFather.getAllInterfaces();
		LOG.info("Pai: Clases: " + fatherClasses.size() + ", Interfaces: " + fatherInterfaces.size());
		LOG.info("Pai: Clases: Atributos: " + fatherClasses.stream().map(Class::getAllAttributes).count());
		LOG.info("Pai: Clases: Metodos: " + fatherClasses.stream().map(Class::getAllMethods).count());
		LOG.info("Pai: Interfaces: Metodos: " + fatherInterfaces.stream().map(Interface::getOperations).count());

		Set<Class> motherClasses = architectureMother.getAllClasses();
		Set<Interface> motherInterfaces = architectureMother.getAllInterfaces();
		LOG.info("Mãe: Clases: " + motherClasses.size() + ", Interfaces: " + motherInterfaces.size());
		LOG.info("Mãe: Clases: Atributos: " + motherClasses.stream().map(Class::getAllAttributes).count());
		LOG.info("Mãe: Clases: Metodos: " + motherClasses.stream().map(Class::getAllMethods).count());
		LOG.info("Mãe: Interfaces: Metodos: " + motherInterfaces.stream().map(Interface::getOperations).count());

		int cpClassesFather = PseudoRandom.randInt(0, fatherClasses.size() - 1);
		int cpInterfacesFather = PseudoRandom.randInt(0, fatherInterfaces.size() - 1);

		Set<Class> diffClassesFather = new HashSet<>(fatherClasses);
		Set<Interface> diffInterafacesFather = new HashSet<>(fatherInterfaces);
		diffClassesFather.removeAll(motherClasses);
		diffInterafacesFather.removeAll(motherInterfaces);
		LOG.info("Elementos existentes apenas no pai: Clases: " + diffClassesFather.size() + ", Interfaces: " + diffInterafacesFather.size());

		Set<Class> offSpringClassesFather = Stream.of(fatherClasses).limit(cpClassesFather).map(HashSet::new).findFirst().orElse(new HashSet<>());
		Set<Interface> offSpringInterfacesFather = Stream.of(fatherInterfaces).limit(cpInterfacesFather).map(HashSet::new).findFirst().orElse(new HashSet<>());

		offSpringClassesFather.addAll(diffClassesFather);
		offSpringClassesFather.addAll(motherClasses);

		offSpringInterfacesFather.addAll(diffInterafacesFather);
		offSpringInterfacesFather.addAll(motherInterfaces);

		offSpring.addAllClasses(offSpringClassesFather);
		offSpring.addAllInterfaces(offSpringInterfacesFather);
		LOG.info("Descendete: Clases: " + offSpring.getAllClasses().size() + ", Interfaces: " + offSpring.getAllInterfaces().size());
		LOG.info("Descendete: Clases: Atributos: " + offSpring.getAllClasses().stream().map(Class::getAllAttributes).count());
		LOG.info("Descendete: Clases: Metodos: " + offSpring.getAllClasses().stream().map(Class::getAllMethods).count());
		LOG.info("Descendete: Interfaces: Metodos: " + offSpring.getAllInterfaces().stream().map(Interface::getOperations).count());

		father.getDecisionVariables()[0] = offSpring;
	}

}
