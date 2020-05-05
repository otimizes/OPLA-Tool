package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.architecture.config.ApplicationYamlConfig;
import br.ufpr.dinf.gres.architecture.io.OPLAConfigThreadScope;
import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.util.Constants;
import br.ufpr.dinf.gres.common.Variable;
import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.core.SolutionSet;
import br.ufpr.dinf.gres.core.jmetal4.metrics.objectivefunctions.*;
import br.ufpr.dinf.gres.core.jmetal4.problems.OPLA;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class MetricsTest {

    @Test
    public void evaluateAGMOnPapyrus() throws Exception {
//        AV, SSC, SVC depends of variabilities and variation points
        String agm = Thread.currentThread().getContextClassLoader().getResource("agm").getFile();
        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setPathToProfile(agm + Constants.FILE_SEPARATOR + "smarty.profile.uml");
        applicationYamlConfig.setPathToProfileConcern(agm + Constants.FILE_SEPARATOR + "concerns.profile.uml");
        applicationYamlConfig.setPathToProfilePatterns(agm + Constants.FILE_SEPARATOR + "patterns.profile.uml");
        applicationYamlConfig.setPathToProfileRelationships(agm + Constants.FILE_SEPARATOR + "relationships.profile.uml");
        applicationYamlConfig.setDirectoryToExportModels("/home/wmfsystem/oplatool/output/");
        applicationYamlConfig.setDirectoryToSaveModels("/home/wmfsystem/oplatool/temp/");
        applicationYamlConfig.setPathToTemplateModelsDirectory("/home/wmfsystem/oplatool/templates/");
        OPLAConfigThreadScope.setConfig(applicationYamlConfig);
        OPLA opla = new OPLA(agm + Constants.FILE_SEPARATOR + "agm.uml");
        Architecture architecture = opla.getArchitecture_();

        ObjectiveFunctions[] values = ObjectiveFunctions.values();
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
        assertEquals(resultsACLASS, ObjectiveFunctions.ACLASS.evaluate(architecture));
        assertEquals(resultsACLASS, Double.valueOf(solution.getObjective(0)));

        Double resultsACOMP = new ACOMP(architecture).getResults();
        assertEquals(new Double(59.0), resultsACOMP);
        assertEquals(resultsACOMP, ObjectiveFunctions.ACOMP.evaluate(architecture));
        assertEquals(resultsACOMP, Double.valueOf(solution.getObjective(1)));

        Double resultsTV = new TV(architecture).getResults();
        assertEquals(new Double(0.0), resultsTV);
        assertEquals(resultsTV, ObjectiveFunctions.TV.evaluate(architecture));
        assertEquals(resultsTV, Double.valueOf(solution.getObjective(2)));

        Double resultsCBCS = new RCC(architecture).getResults();
        assertEquals(new Double(2.0), resultsCBCS);
        assertEquals(resultsCBCS, ObjectiveFunctions.RCC.evaluate(architecture));
        assertEquals(resultsCBCS, Double.valueOf(solution.getObjective(3)));

        Double resultsCOE = new COE(architecture).getResults();
        assertEquals(new Double(7.0), resultsCOE);
        assertEquals(resultsCOE, ObjectiveFunctions.COE.evaluate(architecture));
        assertEquals(resultsCOE, Double.valueOf(solution.getObjective(4)));

        Double resultsConventional = new CM(architecture).getResults();
        assertEquals(new Double(95.07142857142857), resultsConventional);
        assertEquals(resultsConventional, ObjectiveFunctions.CM.evaluate(architecture));
        assertEquals(resultsConventional, Double.valueOf(solution.getObjective(5)));

        Double resultsDC = new DC(architecture).getResults();
        assertEquals(new Double(430.0), resultsDC);
        assertEquals(resultsDC, ObjectiveFunctions.DC.evaluate(architecture));
        assertEquals(resultsDC, Double.valueOf(solution.getObjective(6)));

        Double resultsEC = new EC(architecture).getResults();
        assertEquals(new Double(124.0), resultsEC);
        assertEquals(resultsEC, ObjectiveFunctions.EC.evaluate(architecture));
        assertEquals(resultsEC, Double.valueOf(solution.getObjective(7)));

        Double resultsElegance = new ELEG(architecture).getResults();
        assertEquals(new Double(0.36551288350386757), resultsElegance);
        assertEquals(resultsElegance, ObjectiveFunctions.ELEG.evaluate(architecture));
        assertEquals(resultsElegance, Double.valueOf(solution.getObjective(8)));

        Double resultsFM = new FM(architecture).getResults();
        assertEquals(new Double(758.0), resultsFM);
        assertEquals(resultsFM, ObjectiveFunctions.FM.evaluate(architecture));
        assertEquals(resultsFM, Double.valueOf(solution.getObjective(9)));

        Double resultsLCC = new LCC(architecture).getResults();
        assertEquals(new Double(26.0), resultsLCC);
        assertEquals(resultsLCC, ObjectiveFunctions.LCC.evaluate(architecture));
        assertEquals(resultsLCC, Double.valueOf(solution.getObjective(10)));

        Double resultsplaext = new EXT(architecture).getResults();
        assertEquals(new Double(1.4999999552965178), resultsplaext);
        assertEquals(resultsplaext, ObjectiveFunctions.EXT.evaluate(architecture));
        assertEquals(resultsplaext, Double.valueOf(solution.getObjective(11)));

        Double resultsSSC = new SD(architecture).getResults();
        assertEquals(new Double(1.0), resultsSSC);
        assertEquals(resultsSSC, ObjectiveFunctions.SD.evaluate(architecture));
        assertEquals(resultsSSC, Double.valueOf(solution.getObjective(12)));

        Double resultsSVC = new SV(architecture).getResults();
        assertEquals(new Double(0.0), resultsSVC);
        assertEquals(resultsSVC, ObjectiveFunctions.SV.evaluate(architecture));
        assertEquals(resultsSVC, Double.valueOf(solution.getObjective(13)));

        Double resultsTAM = new TAM(architecture).getResults();
        assertEquals(new Double(3.9285714285714284), resultsTAM);
        assertEquals(resultsTAM, ObjectiveFunctions.TAM.evaluate(architecture));
        assertEquals(resultsTAM, Double.valueOf(solution.getObjective(14)));

        Double resultswclass = new WOCSCLASS(architecture).getResults();
        assertEquals(new Double(0.13333333333333333), resultswclass);
        assertEquals(resultswclass, ObjectiveFunctions.WOCSCLASS.evaluate(architecture));
        assertEquals(resultswclass, Double.valueOf(solution.getObjective(15)));

        Double resultswinterface = new CS(architecture).getResults();
        assertEquals(new Double(0.2857142857142857), resultswinterface);
        assertEquals(resultswinterface, ObjectiveFunctions.CS.evaluate(architecture));
        assertEquals(resultswinterface, Double.valueOf(solution.getObjective(16)));
    }

    @Test
    public void evaluateAGM2OnSmarty() throws Exception {
        String agm = Thread.currentThread().getContextClassLoader().getResource("PLASMarty").getFile();
        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setDirectoryToExportModels("/home/wmfsystem/oplatool/output/");
        applicationYamlConfig.setDirectoryToSaveModels("/home/wmfsystem/oplatool/temp/");
        applicationYamlConfig.setPathToTemplateModelsDirectory("/home/wmfsystem/oplatool/templates/");
        OPLAConfigThreadScope.setConfig(applicationYamlConfig);
        OPLA opla = new OPLA(agm + Constants.FILE_SEPARATOR + "agm2.smty");
        Architecture architecture = opla.getArchitecture_();

        ObjectiveFunctions[] values = ObjectiveFunctions.values();
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
        assertEquals(resultsACLASS, ObjectiveFunctions.ACLASS.evaluate(architecture));
        assertEquals(resultsACLASS, Double.valueOf(solution.getObjective(0)));

        Double resultsACOMP = new ACOMP(architecture).getResults();
        assertEquals(new Double(59.0), resultsACOMP);
        assertEquals(resultsACOMP, ObjectiveFunctions.ACOMP.evaluate(architecture));
        assertEquals(resultsACOMP, Double.valueOf(solution.getObjective(1)));

        Double resultsTV = new TV(architecture).getResults();
        assertEquals(new Double(4.0), resultsTV);
        assertEquals(resultsTV, ObjectiveFunctions.TV.evaluate(architecture));
        assertEquals(resultsTV, Double.valueOf(solution.getObjective(2)));

        Double resultsCBCS = new RCC(architecture).getResults();
        assertEquals(new Double(2.0), resultsCBCS);
        assertEquals(resultsCBCS, ObjectiveFunctions.RCC.evaluate(architecture));
        assertEquals(resultsCBCS, Double.valueOf(solution.getObjective(3)));

        Double resultsCOE = new COE(architecture).getResults();
        assertEquals(new Double(7.0), resultsCOE);
        assertEquals(resultsCOE, ObjectiveFunctions.COE.evaluate(architecture));
        assertEquals(resultsCOE, Double.valueOf(solution.getObjective(4)));

        Double resultsConventional = new CM(architecture).getResults();
        assertEquals(new Double(95.21428571428571), resultsConventional);
        assertEquals(resultsConventional, ObjectiveFunctions.CM.evaluate(architecture));
        assertEquals(resultsConventional, Double.valueOf(solution.getObjective(5)));

        Double resultsDC = new DC(architecture).getResults();
        assertEquals(new Double(551.0), resultsDC);
        assertEquals(resultsDC, ObjectiveFunctions.DC.evaluate(architecture));
        assertEquals(resultsDC, Double.valueOf(solution.getObjective(6)));

        Double resultsEC = new EC(architecture).getResults();
        assertEquals(new Double(181.0), resultsEC);
        assertEquals(resultsEC, ObjectiveFunctions.EC.evaluate(architecture));
        assertEquals(resultsEC, Double.valueOf(solution.getObjective(7)));

        Double resultsElegance = new ELEG(architecture).getResults();
        assertEquals(new Double(0.38540333706981755), resultsElegance);
        assertEquals(resultsElegance, ObjectiveFunctions.ELEG.evaluate(architecture));
        assertEquals(resultsElegance, Double.valueOf(solution.getObjective(8)));

        Double resultsFM = new FM(architecture).getResults();
        assertEquals(new Double(1040.0), resultsFM);
        assertEquals(resultsFM, ObjectiveFunctions.FM.evaluate(architecture));
        assertEquals(resultsFM, Double.valueOf(solution.getObjective(9)));

        Double resultsLCC = new LCC(architecture).getResults();
        assertEquals(new Double(35.0), resultsLCC);
        assertEquals(resultsLCC, ObjectiveFunctions.LCC.evaluate(architecture));
        assertEquals(resultsLCC, Double.valueOf(solution.getObjective(10)));

        Double resultsplaext = new EXT(architecture).getResults();
        assertEquals(new Double(1000.0), resultsplaext);
        assertEquals(resultsplaext, ObjectiveFunctions.EXT.evaluate(architecture));
        assertEquals(resultsplaext, Double.valueOf(solution.getObjective(11)));

        Double resultsSSC = new SD(architecture).getResults();
        assertEquals(new Double(1.2857142857142856), resultsSSC);
        assertEquals(resultsSSC, ObjectiveFunctions.SD.evaluate(architecture));
        assertEquals(resultsSSC, Double.valueOf(solution.getObjective(12)));

        Double resultsSVC = new SV(architecture).getResults();
        assertEquals(new Double(0.2222222222222222), resultsSVC);
        assertEquals(resultsSVC, ObjectiveFunctions.SV.evaluate(architecture));
        assertEquals(resultsSVC, Double.valueOf(solution.getObjective(13)));

        Double resultsTAM = new TAM(architecture).getResults();
        assertEquals(new Double(4.071428571428571), resultsTAM);
        assertEquals(resultsTAM, ObjectiveFunctions.TAM.evaluate(architecture));
        assertEquals(resultsTAM, Double.valueOf(solution.getObjective(14)));

        Double resultswclass = new WOCSCLASS(architecture).getResults();
        assertEquals(new Double(0.13333333333333333), resultswclass);
        assertEquals(resultswclass, ObjectiveFunctions.WOCSCLASS.evaluate(architecture));
        assertEquals(resultswclass, Double.valueOf(solution.getObjective(15)));

        Double resultswinterface = new CS(architecture).getResults();
        assertEquals(new Double(0.2857142857142857), resultswinterface);
        assertEquals(resultswinterface, ObjectiveFunctions.CS.evaluate(architecture));
        assertEquals(resultswinterface, Double.valueOf(solution.getObjective(16)));
    }

    @Test
    public void evaluateAGM1OnSmarty() throws Exception {
        String agm = Thread.currentThread().getContextClassLoader().getResource("PLASMarty").getFile();
        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setDirectoryToExportModels("/home/wmfsystem/oplatool/output/");
        applicationYamlConfig.setDirectoryToSaveModels("/home/wmfsystem/oplatool/temp/");
        applicationYamlConfig.setPathToTemplateModelsDirectory("/home/wmfsystem/oplatool/templates/");
        OPLAConfigThreadScope.setConfig(applicationYamlConfig);
        OPLA opla = new OPLA(agm + Constants.FILE_SEPARATOR + "agm1.smty");
        Architecture architecture = opla.getArchitecture_();

        ObjectiveFunctions[] values = ObjectiveFunctions.values();
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
        assertEquals(new Double(29.0), resultsACLASS);
        assertEquals(resultsACLASS, ObjectiveFunctions.ACLASS.evaluate(architecture));
        assertEquals(resultsACLASS, Double.valueOf(solution.getObjective(0)));

        Double resultsACOMP = new ACOMP(architecture).getResults();
        assertEquals(new Double(59.0), resultsACOMP);
        assertEquals(resultsACOMP, ObjectiveFunctions.ACOMP.evaluate(architecture));
        assertEquals(resultsACOMP, Double.valueOf(solution.getObjective(1)));

        Double resultsTV = new TV(architecture).getResults();
        assertEquals(new Double(4.0), resultsTV);
        assertEquals(resultsTV, ObjectiveFunctions.TV.evaluate(architecture));
        assertEquals(resultsTV, Double.valueOf(solution.getObjective(2)));

        Double resultsCBCS = new RCC(architecture).getResults();
        assertEquals(new Double(2.0), resultsCBCS);
        assertEquals(resultsCBCS, ObjectiveFunctions.RCC.evaluate(architecture));
        assertEquals(resultsCBCS, Double.valueOf(solution.getObjective(3)));

        Double resultsCOE = new COE(architecture).getResults();
        assertEquals(new Double(7.0), resultsCOE);
        assertEquals(resultsCOE, ObjectiveFunctions.COE.evaluate(architecture));
        assertEquals(resultsCOE, Double.valueOf(solution.getObjective(4)));

        Double resultsConventional = new CM(architecture).getResults();
        assertEquals(new Double(93.92857142857142), resultsConventional);
        assertEquals(resultsConventional, ObjectiveFunctions.CM.evaluate(architecture));
        assertEquals(resultsConventional, Double.valueOf(solution.getObjective(5)));

        Double resultsDC = new DC(architecture).getResults();
        assertEquals(new Double(481.0), resultsDC);
        assertEquals(resultsDC, ObjectiveFunctions.DC.evaluate(architecture));
        assertEquals(resultsDC, Double.valueOf(solution.getObjective(6)));

        Double resultsEC = new EC(architecture).getResults();
        assertEquals(new Double(141.0), resultsEC);
        assertEquals(resultsEC, ObjectiveFunctions.EC.evaluate(architecture));
        assertEquals(resultsEC, Double.valueOf(solution.getObjective(7)));

        Double resultsElegance = new ELEG(architecture).getResults();
        assertEquals(new Double(0.3336789849490488), resultsElegance);
        assertEquals(resultsElegance, ObjectiveFunctions.ELEG.evaluate(architecture));
        assertEquals(resultsElegance, Double.valueOf(solution.getObjective(8)));

        Double resultsFM = new FM(architecture).getResults();
        assertEquals(new Double(914.0), resultsFM);
        assertEquals(resultsFM, ObjectiveFunctions.FM.evaluate(architecture));
        assertEquals(resultsFM, Double.valueOf(solution.getObjective(9)));

        Double resultsLCC = new LCC(architecture).getResults();
        assertEquals(new Double(33.0), resultsLCC);
        assertEquals(resultsLCC, ObjectiveFunctions.LCC.evaluate(architecture));
        assertEquals(resultsLCC, Double.valueOf(solution.getObjective(10)));

        Double resultsplaext = new EXT(architecture).getResults();
        assertEquals(new Double(1000.0), resultsplaext);
        assertEquals(resultsplaext, ObjectiveFunctions.EXT.evaluate(architecture));
        assertEquals(resultsplaext, Double.valueOf(solution.getObjective(11)));

        Double resultsSSC = new SD(architecture).getResults();
        assertEquals(new Double(1.2857142857142856), resultsSSC);
        assertEquals(resultsSSC, ObjectiveFunctions.SD.evaluate(architecture));
        assertEquals(resultsSSC, Double.valueOf(solution.getObjective(12)));

        Double resultsSVC = new SV(architecture).getResults();
        assertEquals(new Double(0.2222222222222222), resultsSVC);
        assertEquals(resultsSVC, ObjectiveFunctions.SV.evaluate(architecture));
        assertEquals(resultsSVC, Double.valueOf(solution.getObjective(13)));

        Double resultsTAM = new TAM(architecture).getResults();
        assertEquals(new Double(3.7857142857142856), resultsTAM);
        assertEquals(resultsTAM, ObjectiveFunctions.TAM.evaluate(architecture));
        assertEquals(resultsTAM, Double.valueOf(solution.getObjective(14)));

        Double resultswclass = new WOCSCLASS(architecture).getResults();
        assertEquals(new Double(0.13793103448275862), resultswclass);
        assertEquals(resultswclass, ObjectiveFunctions.WOCSCLASS.evaluate(architecture));
        assertEquals(resultswclass, Double.valueOf(solution.getObjective(15)));

        Double resultswinterface = new CS(architecture).getResults();
        assertEquals(new Double(0.2857142857142857), resultswinterface);
        assertEquals(resultswinterface, ObjectiveFunctions.CS.evaluate(architecture));
        assertEquals(resultswinterface, Double.valueOf(solution.getObjective(16)));
    }

    @Test
    public void evaluateAGMAtualOnSmarty() throws Exception {
        String agm = Thread.currentThread().getContextClassLoader().getResource("PLASMarty").getFile();
        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setDirectoryToExportModels("/home/wmfsystem/oplatool/output/");
        applicationYamlConfig.setDirectoryToSaveModels("/home/wmfsystem/oplatool/temp/");
        applicationYamlConfig.setPathToTemplateModelsDirectory("/home/wmfsystem/oplatool/templates/");
        OPLAConfigThreadScope.setConfig(applicationYamlConfig);
        OPLA opla = new OPLA(agm + Constants.FILE_SEPARATOR + "AGMAtual.smty");
        Architecture architecture = opla.getArchitecture_();

        ObjectiveFunctions[] values = ObjectiveFunctions.values();
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
        assertEquals(resultsACLASS, ObjectiveFunctions.ACLASS.evaluate(architecture));
        assertEquals(resultsACLASS, Double.valueOf(solution.getObjective(0)));

        Double resultsACOMP = new ACOMP(architecture).getResults();
        assertEquals(new Double(59.0), resultsACOMP);
        assertEquals(resultsACOMP, ObjectiveFunctions.ACOMP.evaluate(architecture));
        assertEquals(resultsACOMP, Double.valueOf(solution.getObjective(1)));

        Double resultsTV = new TV(architecture).getResults();
        assertEquals(new Double(4.0), resultsTV);
        assertEquals(resultsTV, ObjectiveFunctions.TV.evaluate(architecture));
        assertEquals(resultsTV, Double.valueOf(solution.getObjective(2)));

        Double resultsCBCS = new RCC(architecture).getResults();
        assertEquals(new Double(2.0), resultsCBCS);
        assertEquals(resultsCBCS, ObjectiveFunctions.RCC.evaluate(architecture));
        assertEquals(resultsCBCS, Double.valueOf(solution.getObjective(3)));

        Double resultsCOE = new COE(architecture).getResults();
        assertEquals(new Double(7.0), resultsCOE);
        assertEquals(resultsCOE, ObjectiveFunctions.COE.evaluate(architecture));
        assertEquals(resultsCOE, Double.valueOf(solution.getObjective(4)));

        Double resultsConventional = new CM(architecture).getResults();
        assertEquals(new Double(95.07142857142857), resultsConventional);
        assertEquals(resultsConventional, ObjectiveFunctions.CM.evaluate(architecture));
        assertEquals(resultsConventional, Double.valueOf(solution.getObjective(5)));

        Double resultsDC = new DC(architecture).getResults();
        assertEquals(new Double(430.0), resultsDC);
        assertEquals(resultsDC, ObjectiveFunctions.DC.evaluate(architecture));
        assertEquals(resultsDC, Double.valueOf(solution.getObjective(6)));

        Double resultsEC = new EC(architecture).getResults();
        assertEquals(new Double(124.0), resultsEC);
        assertEquals(resultsEC, ObjectiveFunctions.EC.evaluate(architecture));
        assertEquals(resultsEC, Double.valueOf(solution.getObjective(7)));

        Double resultsElegance = new ELEG(architecture).getResults();
        assertEquals(new Double(0.36551288350386757), resultsElegance);
        assertEquals(resultsElegance, ObjectiveFunctions.ELEG.evaluate(architecture));
        assertEquals(resultsElegance, Double.valueOf(solution.getObjective(8)));

        Double resultsFM = new FM(architecture).getResults();
        assertEquals(new Double(758.0), resultsFM);
        assertEquals(resultsFM, ObjectiveFunctions.FM.evaluate(architecture));
        assertEquals(resultsFM, Double.valueOf(solution.getObjective(9)));

        Double resultsLCC = new LCC(architecture).getResults();
        assertEquals(new Double(26.0), resultsLCC);
        assertEquals(resultsLCC, ObjectiveFunctions.LCC.evaluate(architecture));
        assertEquals(resultsLCC, Double.valueOf(solution.getObjective(10)));

        Double resultsplaext = new EXT(architecture).getResults();
        assertEquals(new Double(1.4999999552965178), resultsplaext);
        assertEquals(resultsplaext, ObjectiveFunctions.EXT.evaluate(architecture));
        assertEquals(resultsplaext, Double.valueOf(solution.getObjective(11)));

        Double resultsSSC = new SD(architecture).getResults();
        assertEquals(new Double(1.2857142857142856), resultsSSC);
        assertEquals(resultsSSC, ObjectiveFunctions.SD.evaluate(architecture));
        assertEquals(resultsSSC, Double.valueOf(solution.getObjective(12)));

        Double resultsSVC = new SV(architecture).getResults();
        assertEquals(new Double(0.2222222222222222), resultsSVC);
        assertEquals(resultsSVC, ObjectiveFunctions.SV.evaluate(architecture));
        assertEquals(resultsSVC, Double.valueOf(solution.getObjective(13)));

        Double resultsTAM = new TAM(architecture).getResults();
        assertEquals(new Double(3.9285714285714284), resultsTAM);
        assertEquals(resultsTAM, ObjectiveFunctions.TAM.evaluate(architecture));
        assertEquals(resultsTAM, Double.valueOf(solution.getObjective(14)));

        Double resultswclass = new WOCSCLASS(architecture).getResults();
        assertEquals(new Double(0.13333333333333333), resultswclass);
        assertEquals(resultswclass, ObjectiveFunctions.WOCSCLASS.evaluate(architecture));
        assertEquals(resultswclass, Double.valueOf(solution.getObjective(15)));

        Double resultswinterface = new CS(architecture).getResults();
        assertEquals(new Double(0.2857142857142857), resultswinterface);
        assertEquals(resultswinterface, ObjectiveFunctions.CS.evaluate(architecture));
        assertEquals(resultswinterface, Double.valueOf(solution.getObjective(16)));
    }
}
