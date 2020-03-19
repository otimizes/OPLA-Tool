package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.architecture.config.ApplicationYamlConfig;
import br.ufpr.dinf.gres.architecture.io.OPLAThreadScope;
import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.util.Constants;
import br.ufpr.dinf.gres.common.Variable;
import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.core.SolutionSet;
import br.ufpr.dinf.gres.core.jmetal4.experiments.Metrics;
import br.ufpr.dinf.gres.core.jmetal4.metrics.objectivefunctions.*;
import br.ufpr.dinf.gres.core.jmetal4.problems.OPLA;
import org.junit.Test;

import java.util.ArrayList;

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
        assertEquals(resultsACLASS, ObjectiveFunctionEvaluation.evaluateACLASS(architecture));
        assertEquals(resultsACLASS, Double.valueOf(solution.getObjective(0)));

        Double resultsACOMP = new ACOMP(architecture).getResults();
        assertEquals(new Double(59.0), resultsACOMP);
        assertEquals(resultsACOMP, ObjectiveFunctionEvaluation.evaluateACOMP(architecture));
        assertEquals(resultsACOMP, Double.valueOf(solution.getObjective(1)));

        Double resultsAV = new TV(architecture).getResults();
        assertEquals(new Double(0.0), resultsAV);
        assertEquals(resultsAV, ObjectiveFunctionEvaluation.evaluateTV(architecture));
        assertEquals(resultsAV, Double.valueOf(solution.getObjective(2)));

        Double resultsCBCS = new CBCS(architecture).getResults();
        assertEquals(new Double(2.0), resultsCBCS);
        assertEquals(resultsCBCS, ObjectiveFunctionEvaluation.evaluateCBCS(architecture));
        assertEquals(resultsCBCS, Double.valueOf(solution.getObjective(3)));

        Double resultsCOE = new COE(architecture).getResults();
        assertEquals(new Double(7.0), resultsCOE);
        assertEquals(resultsCOE, ObjectiveFunctionEvaluation.evaluateCOE(architecture));
        assertEquals(resultsCOE, Double.valueOf(solution.getObjective(4)));

        Double resultsConventional = new CM(architecture).getResults();
        assertEquals(new Double(95.07142857142857), resultsConventional);
        assertEquals(resultsConventional, ObjectiveFunctionEvaluation.evaluateCONVENTIONAL(architecture));
        assertEquals(resultsConventional, Double.valueOf(solution.getObjective(5)));

        Double resultsDC = new DC(architecture).getResults();
        assertEquals(new Double(430.0), resultsDC);
        assertEquals(resultsDC, ObjectiveFunctionEvaluation.evaluateDC(architecture));
        assertEquals(resultsDC, Double.valueOf(solution.getObjective(6)));

        Double resultsEC = new EC(architecture).getResults();
        assertEquals(new Double(124.0), resultsEC);
        assertEquals(resultsEC, ObjectiveFunctionEvaluation.evaluateEC(architecture));
        assertEquals(resultsEC, Double.valueOf(solution.getObjective(7)));

        Double resultsElegance = new ELEG(architecture).getResults();
        assertEquals(new Double(0.36551288350386757), resultsElegance);
        assertEquals(resultsElegance, ObjectiveFunctionEvaluation.evaluateELEG(architecture));
        assertEquals(resultsElegance, Double.valueOf(solution.getObjective(8)));

        Double resultsFM = new FM(architecture).getResults();
        assertEquals(new Double(758.0), resultsFM);
        assertEquals(resultsFM, ObjectiveFunctionEvaluation.evaluateFM(architecture));
        assertEquals(resultsFM, Double.valueOf(solution.getObjective(9)));

        Double resultsLCC = new LCC(architecture).getResults();
        assertEquals(new Double(26.0), resultsLCC);
        assertEquals(resultsLCC, ObjectiveFunctionEvaluation.evaluateLCC(architecture));
        assertEquals(resultsLCC, Double.valueOf(solution.getObjective(10)));

        Double resultsplaext = new EXT(architecture).getResults();
        assertEquals(new Double(1.4999999552965178), resultsplaext);
        assertEquals(resultsplaext, ObjectiveFunctionEvaluation.evaluateEXT(architecture));
        assertEquals(resultsplaext, Double.valueOf(solution.getObjective(11)));

        Double resultsSSC = new SD(architecture).getResults();
        assertEquals(new Double(1.0), resultsSSC);
        assertEquals(resultsSSC, ObjectiveFunctionEvaluation.evaluateSD(architecture));
        assertEquals(resultsSSC, Double.valueOf(solution.getObjective(12)));

        Double resultsSVC = new SV(architecture).getResults();
        assertEquals(new Double(0.0), resultsSVC);
        assertEquals(resultsSVC, ObjectiveFunctionEvaluation.evaluateSV(architecture));
        assertEquals(resultsSVC, Double.valueOf(solution.getObjective(13)));

        Double resultsTAM = new TAM(architecture).getResults();
        assertEquals(new Double(3.9285714285714284), resultsTAM);
        assertEquals(resultsTAM, ObjectiveFunctionEvaluation.evaluateTAM(architecture));
        assertEquals(resultsTAM, Double.valueOf(solution.getObjective(14)));

        Double resultswclass = new WOCSCLASS(architecture).getResults();
        assertEquals(new Double(0.13333333333333333), resultswclass);
        assertEquals(resultswclass, ObjectiveFunctionEvaluation.evaluateWOCSCLASS(architecture));
        assertEquals(resultswclass, Double.valueOf(solution.getObjective(15)));

        Double resultswinterface = new WOCSINTERFACE(architecture).getResults();
        assertEquals(new Double(0.2857142857142857), resultswinterface);
        assertEquals(resultswinterface, ObjectiveFunctionEvaluation.evaluateWOCSINTERFFACE(architecture));
        assertEquals(resultswinterface, Double.valueOf(solution.getObjective(16)));
    }
}
