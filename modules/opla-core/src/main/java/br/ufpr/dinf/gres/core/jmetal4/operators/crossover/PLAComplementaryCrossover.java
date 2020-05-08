package br.ufpr.dinf.gres.core.jmetal4.operators.crossover;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Interface;
import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.experiments.ExperimentCommonConfigs;
import br.ufpr.dinf.gres.core.jmetal4.util.PseudoRandom;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Stream;

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

    public PLAComplementaryCrossover(Map<String, Object> parameters, ExperimentCommonConfigs configs) {
        this(parameters);
    }

    @Override
    public Object execute(Object object) throws Exception {
        Solution[] parents = (Solution[]) object;
        Solution father = parents[0];
        Solution mother = parents[1];

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
                map.put(i, Arrays.asList(father));
            } else if (fitnessFather > fitnessMother) {
                map.put(i, Arrays.asList(mother));
            } else {
                map.put(i, Arrays.asList(father, mother));
            }
            bests.add(map);
        }
    }

    private Solution selectRandom(Map<Integer, List<Solution>> solutions) {
        return solutions.entrySet().stream()
                .map(entry -> entry.getValue().get(PseudoRandom.randInt(0, solutions.entrySet().size() - 1)))
                .findFirst()
                .get();
    }

    public void doCrossover(Solution father, Solution mother) {
        List<Map<Integer, List<Solution>>> bests = new ArrayList<>();
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

        Map<Integer, List<Solution>> randonSelectected = bests.get(PseudoRandom.randInt(0, bests.size() - 1));
        mother = selectRandom(randonSelectected);

        Architecture architectureFather = (Architecture) father.getDecisionVariables()[0];
        Architecture architectureMother = (Architecture) mother.getDecisionVariables()[0];
        Architecture offSpring = architectureFather.deepClone();

        Set<Class> fatherClasses = architectureFather.getAllClasses();
        Set<Interface> fatherInterfaces = architectureFather.getAllInterfaces();

        Set<Class> motherClasses = architectureMother.getAllClasses();
        Set<Interface> motherInterfaces = architectureMother.getAllInterfaces();

        int cpClassesFather = PseudoRandom.randInt(0, fatherClasses.size() - 1);
        int cpInterfacesFather = PseudoRandom.randInt(0, fatherInterfaces.size() - 1);

        Set<Class> diffClassesFather = new HashSet<>(fatherClasses);
        Set<Interface> diffInterafacesFather = new HashSet<>(fatherInterfaces);
        diffClassesFather.removeAll(motherClasses);
        diffInterafacesFather.removeAll(motherInterfaces);

        Set<Class> offSpringClassesFather = Stream.of(fatherClasses).limit(cpClassesFather).map(HashSet::new).findFirst().orElse(new HashSet<>());
        Set<Interface> offSpringInterfacesFather = Stream.of(fatherInterfaces).limit(cpInterfacesFather).map(HashSet::new).findFirst().orElse(new HashSet<>());

        offSpringClassesFather.addAll(diffClassesFather);
        offSpringClassesFather.addAll(motherClasses);

        offSpringInterfacesFather.addAll(diffInterafacesFather);
        offSpringInterfacesFather.addAll(motherInterfaces);

        offSpring.addAllClasses(offSpringClassesFather);
        offSpring.addAllInterfaces(offSpringInterfacesFather);

        father.getDecisionVariables()[0] = offSpring;
    }

}
