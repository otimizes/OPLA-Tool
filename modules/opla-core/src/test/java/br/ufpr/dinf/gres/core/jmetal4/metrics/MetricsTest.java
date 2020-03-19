package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.architecture.builders.ArchitectureBuilder;
import br.ufpr.dinf.gres.architecture.config.ApplicationYamlConfig;
import br.ufpr.dinf.gres.architecture.io.OPLAThreadScope;
import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.architecture.util.Constants;
import br.ufpr.dinf.gres.common.Variable;
import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.core.SolutionSet;
import br.ufpr.dinf.gres.core.jmetal4.experiments.Metrics;
import br.ufpr.dinf.gres.core.jmetal4.metrics.all.*;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.RelationalCohesion;
import br.ufpr.dinf.gres.core.jmetal4.problems.OPLA;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class MetricsTest {

    //    TODO Verificar com Thelma métricas zeradas
    @Test
    public void evaluateAGM() throws Exception {
//        AV, SSC, SVC depends of variabilities and variation points
//        String agm = Thread.currentThread().getContextClassLoader().getResource("agm").getFile();
        String agm = "/home/wmfsystem/workspace/asdasd/plas/newagm";
        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setPathToProfile(agm + Constants.FILE_SEPARATOR + "resources/smarty.profile.uml");
        applicationYamlConfig.setPathToProfileConcern(agm + Constants.FILE_SEPARATOR + "resources/concerns.profile.uml");
        applicationYamlConfig.setPathToProfilePatterns(agm + Constants.FILE_SEPARATOR + "resources/patterns.profile.uml");
        applicationYamlConfig.setPathToProfileRelationships(agm + Constants.FILE_SEPARATOR + "resources/relationships.profile.uml");
        applicationYamlConfig.setDirectoryToExportModels("/home/wmfsystem/oplatool/output/");
        applicationYamlConfig.setDirectoryToSaveModels("/home/wmfsystem/oplatool/temp/");
        applicationYamlConfig.setPathToTemplateModelsDirectory("/home/wmfsystem/oplatool/templates/");
        OPLAThreadScope.setConfig(applicationYamlConfig);
        OPLA opla = new OPLA(agm + Constants.FILE_SEPARATOR + "agm.uml");
        Architecture architecture = opla.getArchitecture_();

        Metrics[] values = Metrics.values();
        SolutionSet solutionSet = new SolutionSet();
        Solution solution = new Solution(values.length);
        solution.setProblem(opla);
        solution.setDecisionVariables(new Variable[]{architecture});
        solutionSet.setCapacity(1);
        solutionSet.add(solution);

        opla.setSelectedMetrics(new ArrayList<>());
        solution.setNumberOfObjectives(values.length);
        for (int i = 0; i < values.length; i++) {
            opla.getSelectedMetrics().add(values[i].toString());
        }

        opla.evaluate(solution);

        Double resultsACLASS = new ACLASS(architecture).getResults();
        assertEquals(new Double(30.0), resultsACLASS);
        assertEquals(resultsACLASS, MetricsEvaluation.evaluateACLASS(architecture));
        assertEquals(resultsACLASS, Double.valueOf(solution.getObjective(0)));

        Double resultsACOMP = new ACOMP(architecture).getResults();
        assertEquals(new Double(59.0), resultsACOMP);
        assertEquals(resultsACOMP, MetricsEvaluation.evaluateACOMP(architecture));
        assertEquals(resultsACOMP, Double.valueOf(solution.getObjective(1)));

        Double resultsAV = new AV(architecture).getResults();
        assertEquals(new Double(0.0), resultsAV);
        assertEquals(resultsAV, MetricsEvaluation.evaluateAV(architecture));
        assertEquals(resultsAV, Double.valueOf(solution.getObjective(2)));

        Double resultsCBCS = new CBCS(architecture).getResults();
        assertEquals(new Double(2.0), resultsCBCS);
        assertEquals(resultsCBCS, MetricsEvaluation.evaluateCBCS(architecture));
        assertEquals(resultsCBCS, Double.valueOf(solution.getObjective(3)));

        Double resultsCOE = new COE(architecture).getResults();
        assertEquals(new Double(7.0), resultsCOE);
        assertEquals(resultsCOE, MetricsEvaluation.evaluateCOE(architecture));
        assertEquals(resultsCOE, Double.valueOf(solution.getObjective(4)));

        Double resultsConventional = new Conventional(architecture).getResults();
        assertEquals(new Double(95.07142857142857), resultsConventional);
        assertEquals(resultsConventional, MetricsEvaluation.evaluateCONVENTIONAL(architecture));
        assertEquals(resultsConventional, Double.valueOf(solution.getObjective(5)));

        Double resultsDC = new DC(architecture).getResults();
        assertEquals(new Double(430.0), resultsDC);
        assertEquals(resultsDC, MetricsEvaluation.evaluateDC(architecture));
        assertEquals(resultsDC, Double.valueOf(solution.getObjective(6)));

        Double resultsEC = new EC(architecture).getResults();
        assertEquals(new Double(124.0), resultsEC);
        assertEquals(resultsEC, MetricsEvaluation.evaluateEC(architecture));
        assertEquals(resultsEC, Double.valueOf(solution.getObjective(7)));

        Double resultsElegance = new Elegance(architecture).getResults();
        assertEquals(new Double(0.36551288350386757), resultsElegance);
        assertEquals(resultsElegance, MetricsEvaluation.evaluateELEGANCE(architecture));
        assertEquals(resultsElegance, Double.valueOf(solution.getObjective(8)));

        Double resultsFM = new FM(architecture).getResults();
        assertEquals(new Double(758.0), resultsFM);
        assertEquals(resultsFM, MetricsEvaluation.evaluateFM(architecture));
        assertEquals(resultsFM, Double.valueOf(solution.getObjective(9)));

        Double resultsLCC = new LCC(architecture).getResults();
        assertEquals(new Double(26.0), resultsLCC);
        assertEquals(resultsLCC, MetricsEvaluation.evaluateLCC(architecture));
        assertEquals(resultsLCC, Double.valueOf(solution.getObjective(10)));

        Double resultsMSIDesignOutset = new MSIDesignOutset(architecture).getResults();
        assertEquals(new Double(758.0), resultsMSIDesignOutset);
        assertEquals(resultsMSIDesignOutset, MetricsEvaluation.evaluateMSIDESIGNOUTSET(architecture));
        assertEquals(resultsMSIDesignOutset, Double.valueOf(solution.getObjective(11)));

        Double resultsplaext = new PLAExtensibility(architecture).getResults();
        assertEquals(new Double(1.4999999552965178), resultsplaext);
        assertEquals(resultsplaext, MetricsEvaluation.evaluatePLAEXTENSIBILITY(architecture));
        assertEquals(resultsplaext, Double.valueOf(solution.getObjective(12)));

        Double resultsSSC = new SSC(architecture).getResults();
        assertEquals(new Double(1.0), resultsSSC);
        assertEquals(resultsSSC, MetricsEvaluation.evaluateSSC(architecture));
        assertEquals(resultsSSC, Double.valueOf(solution.getObjective(13)));

        Double resultsSVC = new SVC(architecture).getResults();
        assertEquals(new Double(0.0), resultsSVC);
        assertEquals(resultsSVC, MetricsEvaluation.evaluateSVC(architecture));
        assertEquals(resultsSVC, Double.valueOf(solution.getObjective(14)));

        Double resultsTAM = new TAM(architecture).getResults();
        assertEquals(new Double(3.9285714285714284), resultsTAM);
        assertEquals(resultsTAM, MetricsEvaluation.evaluateTAM(architecture));
        assertEquals(resultsTAM, Double.valueOf(solution.getObjective(15)));

        Double resultswclass = new WOCSClass(architecture).getResults();
        assertEquals(new Double(0.13333333333333333), resultswclass);
        assertEquals(resultswclass, MetricsEvaluation.evaluateWOCSCLASS(architecture));
        assertEquals(resultswclass, Double.valueOf(solution.getObjective(16)));

        Double resultswinterface = new WOCSInterface(architecture).getResults();
        assertEquals(new Double(0.2857142857142857), resultswinterface);
        assertEquals(resultswinterface, MetricsEvaluation.evaluateWOCSINTERFFACE(architecture));
        assertEquals(resultswinterface, Double.valueOf(solution.getObjective(17)));
    }
}
