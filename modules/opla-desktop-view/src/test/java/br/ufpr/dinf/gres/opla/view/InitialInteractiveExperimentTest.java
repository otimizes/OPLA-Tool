package br.ufpr.dinf.gres.opla.view;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import arquitetura.representation.Package;
import br.ufpr.dinf.gres.loglog.LogLog;
import br.ufpr.dinf.gres.opla.view.util.Utils;
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
import java.util.List;
import java.util.stream.Collectors;

public class InitialInteractiveExperimentTest {


    public static final Logger LOGGER = Logger.getLogger(InitialInteractiveExperimentTest.class);

//    @Test
    public void savePositionsUML() throws Exception {

        List<String> xmis = Arrays.asList(
                "/home/wmfsystem/oplatool/plas/agm/agm.uml"
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

        SolutionSet solutionSet = new SolutionSet();
        Solution solution = new Solution();


        for (Architecture architecture : arrayList) {
            System.out.println();
            architecture.save(architecture, "agm2", "2");
            System.out.println(architecture.getName());
            Utils.executePapyrus("/home/wmfsystem/App/eclipse/eclipse", "/home/wmfsystem/oplatool/output/agm.di");
        }
    }

    @Test
    public void savePositionsUML2() throws Exception {

        List<String> xmis = Arrays.asList(
                "/home/wmfsystem/oplatool/plas/agm/agm.uml"
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

        SolutionSet solutionSet = new SolutionSet();
        Solution solution = new Solution();


        for (Architecture architecture : arrayList) {
            System.out.println();
            architecture.save(architecture, "agm2", "2");
            System.out.println(architecture.getName());
            Utils.executePapyrus("/home/wmfsystem/App/eclipse/eclipse", "/home/wmfsystem/oplatool/output/agm2agm2.di");
        }
    }
}
