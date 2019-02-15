package learning;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import arquitetura.representation.Package;
import br.ufpr.dinf.gres.loglog.LogLog;
import jmetal4.core.Solution;
import jmetal4.core.SolutionSet;
import jmetal4.core.Variable;
import jmetal4.encodings.solutionType.ArchitectureSolutionType;
import jmetal4.experiments.NSGAIIConfig;
import jmetal4.problems.OPLA;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class InitialInteractiveTest {


    public static final Logger LOGGER = Logger.getLogger(InitialInteractiveTest.class);

    //    @Test
    public void countElements() throws Exception {

        List<String> xmis = Arrays.asList(
                "/home/wmfsystem/workspace/plas/agm/agm.uml",
                "/home/wmfsystem/workspace/plas/vinicius/2/sem_interacao/VAR_All_agm-6168413134.uml",
                "/home/wmfsystem/workspace/plas/vinicius/2/sem_interacao/VAR_All_agm-7845213531.uml",
                "/home/wmfsystem/workspace/plas/vinicius/2/sem_interacao/VAR_All_agm-9181131839.uml",
                "/home/wmfsystem/workspace/plas/vinicius/2/sem_interacao/VAR_All_agm-9184713578.uml",
                "/home/wmfsystem/workspace/plas/vinicius/2/experimento_choma/VAR_All_agm-1983211558.uml",
                "/home/wmfsystem/workspace/plas/vinicius/2/experimento_choma/VAR_All_agm-2139224476.uml",
                "/home/wmfsystem/workspace/plas/vinicius/2/experimento_choma/VAR_All_agm-4399228137.uml",
                "/home/wmfsystem/workspace/plas/vinicius/2/experimento_choma/VAR_All_agm-5399919845.uml",
                "/home/wmfsystem/workspace/plas/vinicius/2/experimento_choma/VAR_All_agm-6886372127.uml",
                "/home/wmfsystem/workspace/plas/vinicius/2/experimento_choma/VAR_All_agm-7117618645.uml",
                "/home/wmfsystem/workspace/plas/vinicius/2/experimento_choma/VAR_All_agm-7125411918.uml"
        );


        ArchitectureBuilder architectureBuilder = new ArchitectureBuilder();
        List<Architecture> arrayList = xmis.stream().map(x -> {
            try {
                return architectureBuilder.create(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());


        for (Architecture architecture : arrayList) {
            System.out.println();
            System.out.println(architecture.getName());
            for (Package allPackage : architecture.getAllPackages()) {
                System.out.println(allPackage.getName() + ": " + allPackage.getAllConcerns().toString());
            }
        }
    }


//    @Test
    public void pfTrue() throws IOException {

        List<double[]> pfs = Arrays.asList(
                new double[]{23.0, 719.0, 27.0},
                new double[]{28.0, 752.0, 26.0},
                new double[]{16.0, 694.0, 29.0},
                new double[]{12.0, 676.0, 30.0},
                new double[]{24.0, 718.0, 27.0},
                new double[]{23.0, 712.0, 28.0},
                new double[]{16.0, 692.0, 29.0},
                new double[]{12.0, 674.0, 30.0},
                new double[]{34.0, 793.0, 25.0},
                new double[]{28.0, 747.0, 26.0},
                new double[]{18.0, 716.0, 28.0}
        );


        List<Solution> collect = pfs.stream().map(pf -> {
            Solution solution = new Solution();
            solution.createObjective(3);
            solution.setObjective(0, pf[0]);
            solution.setObjective(1, pf[1]);
            solution.setObjective(2, pf[2]);
            solution.setNumberOfObjectives(3);
            try {
                solution.setType(new ArchitectureSolutionType(new OPLA()));
                Variable[] variables = new Variable[solution.numberOfObjectives()];
                variables[0] = new Architecture("a");
                variables[1] = new Architecture("b");
                variables[2] = new Architecture("c");
                solution.setDecisionVariables(variables);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            ((OPLA) solution.getType().problem_).setSelectedMetrics(Arrays.asList("featureDriven", "aclass", "coe"));
            return solution;
        }).collect(Collectors.toList());

        SolutionSet solutionSet = new SolutionSet();
        solutionSet.setSolutionSet(collect);

//        ClusteringExperimentTest.rCommand(solutionSet);

        OPLA opla = new OPLA();

        NSGAIIConfig nsgaiiConfig = new NSGAIIConfig();
        nsgaiiConfig.setLogger(new LogLog());

        opla.setConfigs(nsgaiiConfig);
        SolutionSet dominadas = opla.removeDominadas(solutionSet);
        dominadas = opla.removeRepetidas(dominadas);
        dominadas.getSolutionSet().forEach(s -> System.out.println(s.getObjective(0) + " " + s.getObjective(1) + " " + s.getObjective(2)));
        System.out.println("----->");

    }

/*

x = c(23.0, 28.0, 16.0, 12.0, 24.0, 23.0, 16.0, 12.0, 34.0, 28.0, 18.0)
y = c(719.0, 752.0, 694.0, 676.0, 718.0, 712.0, 692.0, 674.0, 793.0, 747.0, 716.0)
z = c(27.0, 26.0, 29.0, 30.0, 27.0, 28.0, 29.0, 30.0, 25.0, 26.0, 28.0)
colors = c("blue","red","red","red","blue","blue","blue","blue","blue","blue","blue")
s3d = scatterplot3d(x, y, z, type = "p", angle = 10, pch = 1, main = "Non Dominated (Blue) x Dominated (Red)", zlab="ACLASS", ylab="FM", xlab="COE", color=colors)

 */

    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date.getTime());
    }
}



