package br.otimizes.oplatool.core.jmetal4.metrics;

import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions.*;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.common.Variable;
import br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions.*;
import br.otimizes.oplatool.core.jmetal4.problems.OPLA;
import br.otimizes.oplatool.domain.OPLAThreadScope;
import br.otimizes.oplatool.domain.config.ApplicationYamlConfig;
import br.otimizes.oplatool.domain.config.FileConstants;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ObjectiveFunctionsTest {

    @Test
    public void evaluateAGMOnPapyrus() throws Exception {
//        AV, SSC, SVC depends of variabilities and variation points
        String agm = Thread.currentThread().getContextClassLoader().getResource("agm").getFile();
        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setPathToProfile(agm + FileConstants.FILE_SEPARATOR + "smarty.profile.uml");
        applicationYamlConfig.setPathToProfileConcern(agm + FileConstants.FILE_SEPARATOR + "concerns.profile.uml");
        applicationYamlConfig.setPathToProfilePatterns(agm + FileConstants.FILE_SEPARATOR + "patterns.profile.uml");
        applicationYamlConfig.setPathToProfileRelationships(agm + FileConstants.FILE_SEPARATOR + "relationships.profile.uml");
        applicationYamlConfig.setDirectoryToExportModels("");
        applicationYamlConfig.setDirectoryToSaveModels("");
        applicationYamlConfig.setPathToTemplateModelsDirectory("");
        OPLAThreadScope.setConfig(applicationYamlConfig);
        OPLA opla = new OPLA(agm + FileConstants.FILE_SEPARATOR + "agm.uml");
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
        for (ObjectiveFunctions value : values) {
            opla.getSelectedMetrics().add(value.toString());
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
    public void evaluateAGM2onSmarty() throws Exception {
        String agm = Thread.currentThread().getContextClassLoader().getResource("PLASMarty").getFile();
        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setDirectoryToExportModels("");
        applicationYamlConfig.setDirectoryToSaveModels("");
        applicationYamlConfig.setPathToTemplateModelsDirectory("");
        OPLAThreadScope.setConfig(applicationYamlConfig);
        OPLA opla = new OPLA(agm + FileConstants.FILE_SEPARATOR + "agm2.smty");
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
    public void evaluateAGM1onSmarty() throws Exception {
        String agm = Thread.currentThread().getContextClassLoader().getResource("PLASMarty").getFile();
        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setDirectoryToExportModels("");
        applicationYamlConfig.setDirectoryToSaveModels("");
        applicationYamlConfig.setPathToTemplateModelsDirectory("");
        OPLAThreadScope.setConfig(applicationYamlConfig);
        OPLA opla = new OPLA(agm + FileConstants.FILE_SEPARATOR + "agm1.smty");
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
        applicationYamlConfig.setDirectoryToExportModels("");
        applicationYamlConfig.setDirectoryToSaveModels("");
        applicationYamlConfig.setPathToTemplateModelsDirectory("");
        OPLAThreadScope.setConfig(applicationYamlConfig);
        OPLA opla = new OPLA(agm + FileConstants.FILE_SEPARATOR + "AGMAtual.smty");
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
        for (ObjectiveFunctions value : values) {
            opla.getSelectedMetrics().add(value.toString());
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

    @Test
    public void evaluateBetAtualOnSmarty() throws Exception {
        String agm = Thread.currentThread().getContextClassLoader().getResource("PLASMarty").getFile();
        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setDirectoryToExportModels("");
        applicationYamlConfig.setDirectoryToSaveModels("");
        applicationYamlConfig.setPathToTemplateModelsDirectory("");
        OPLAThreadScope.setConfig(applicationYamlConfig);
        OPLA opla = new OPLA(agm + FileConstants.FILE_SEPARATOR + "BetAtual.smty");
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
        Double resultsACOMP = new ACOMP(architecture).getResults();
        Double resultsTV = new TV(architecture).getResults();
        Double resultsCBCS = new RCC(architecture).getResults();
        Double resultsCOE = new COE(architecture).getResults();
        Double resultsConventional = new CM(architecture).getResults();
        Double resultsDC = new DC(architecture).getResults();
        Double resultsEC = new EC(architecture).getResults();
        Double resultsElegance = new ELEG(architecture).getResults();
        Double resultsFM = new FM(architecture).getResults();
        Double resultsLCC = new LCC(architecture).getResults();
        Double resultsplaext = new EXT(architecture).getResults();
        Double resultsSSC = new SD(architecture).getResults();
        Double resultsSVC = new SV(architecture).getResults();
        Double resultsTAM = new TAM(architecture).getResults();
        Double resultswclass = new WOCSCLASS(architecture).getResults();
        Double resultswinterface = new CS(architecture).getResults();

        assertEquals(new Double(122.0), resultsACLASS);
        assertEquals(resultsACLASS, ObjectiveFunctions.ACLASS.evaluate(architecture));
        assertEquals(resultsACLASS, Double.valueOf(solution.getObjective(0)));

        assertEquals(new Double(339.0), resultsACOMP);
        assertEquals(resultsACOMP, ObjectiveFunctions.ACOMP.evaluate(architecture));
        assertEquals(resultsACOMP, Double.valueOf(solution.getObjective(1)));

        assertEquals(new Double(6.0), resultsTV);
        assertEquals(resultsTV, ObjectiveFunctions.TV.evaluate(architecture));
        assertEquals(resultsTV, Double.valueOf(solution.getObjective(2)));

        assertEquals(new Double(5.3), resultsCBCS);
        assertEquals(resultsCBCS, ObjectiveFunctions.RCC.evaluate(architecture));
        assertEquals(resultsCBCS, Double.valueOf(solution.getObjective(3)));

        assertEquals(new Double(48.0), resultsCOE);
        assertEquals(resultsCOE, ObjectiveFunctions.COE.evaluate(architecture));
        assertEquals(resultsCOE, Double.valueOf(solution.getObjective(4)));

        assertEquals(new Double(482.4208333333333), resultsConventional);
        assertEquals(resultsConventional, ObjectiveFunctions.CM.evaluate(architecture));
        assertEquals(resultsConventional, Double.valueOf(solution.getObjective(5)));

        assertEquals(new Double(688.0), resultsDC);
        assertEquals(resultsDC, ObjectiveFunctions.DC.evaluate(architecture));
        assertEquals(resultsDC, Double.valueOf(solution.getObjective(6)));

        assertEquals(new Double(200.0), resultsEC);
        assertEquals(resultsEC, ObjectiveFunctions.EC.evaluate(architecture));
        assertEquals(resultsEC, Double.valueOf(solution.getObjective(7)));

        assertEquals(new Double(0.7654241810083315), resultsElegance);
        assertEquals(resultsElegance, ObjectiveFunctions.ELEG.evaluate(architecture));
        assertEquals(resultsElegance, Double.valueOf(solution.getObjective(8)));

        assertEquals(new Double(1486.0), resultsFM);
        assertEquals(resultsFM, ObjectiveFunctions.FM.evaluate(architecture));
        assertEquals(resultsFM, Double.valueOf(solution.getObjective(9)));

        assertEquals(new Double(100.0), resultsLCC);
        assertEquals(resultsLCC, ObjectiveFunctions.LCC.evaluate(architecture));
        assertEquals(resultsLCC, Double.valueOf(solution.getObjective(10)));

        assertEquals(new Double(2.9999999105930355), resultsplaext);
        assertEquals(resultsplaext, ObjectiveFunctions.EXT.evaluate(architecture));
        assertEquals(resultsplaext, Double.valueOf(solution.getObjective(11)));

        assertEquals(new Double(1.0566037735849056), resultsSSC);
        assertEquals(resultsSSC, ObjectiveFunctions.SD.evaluate(architecture));
        assertEquals(resultsSSC, Double.valueOf(solution.getObjective(12)));

        assertEquals(new Double(0.05357142857142857), resultsSVC);
        assertEquals(resultsSVC, ObjectiveFunctions.SV.evaluate(architecture));
        assertEquals(resultsSVC, Double.valueOf(solution.getObjective(13)));

        assertEquals(new Double(5.4), resultsTAM);
        assertEquals(resultsTAM, ObjectiveFunctions.TAM.evaluate(architecture));
        assertEquals(resultsTAM, Double.valueOf(solution.getObjective(14)));

        assertEquals(new Double(0.02608695652173913), resultswclass);
        assertEquals(resultswclass, ObjectiveFunctions.WOCSCLASS.evaluate(architecture));
        assertEquals(resultswclass, Double.valueOf(solution.getObjective(15)));

        assertEquals(new Double(0.16666666666666666), resultswinterface);
        assertEquals(resultswinterface, ObjectiveFunctions.CS.evaluate(architecture));
        assertEquals(resultswinterface, Double.valueOf(solution.getObjective(16)));
    }

    @Test
    public void evaluateMM1onSmarty() throws Exception {
        String agm = Thread.currentThread().getContextClassLoader().getResource("PLASMarty").getFile();
        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setDirectoryToExportModels("");
        applicationYamlConfig.setDirectoryToSaveModels("");
        applicationYamlConfig.setPathToTemplateModelsDirectory("");
        OPLAThreadScope.setConfig(applicationYamlConfig);
        OPLA opla = new OPLA(agm + FileConstants.FILE_SEPARATOR + "mm1.smty");
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
        Double resultsACOMP = new ACOMP(architecture).getResults();
        Double resultsTV = new TV(architecture).getResults();
        Double resultsCBCS = new RCC(architecture).getResults();
        Double resultsCOE = new COE(architecture).getResults();
        Double resultsConventional = new CM(architecture).getResults();
        Double resultsDC = new DC(architecture).getResults();
        Double resultsEC = new EC(architecture).getResults();
        Double resultsElegance = new ELEG(architecture).getResults();
        Double resultsFM = new FM(architecture).getResults();
        Double resultsLCC = new LCC(architecture).getResults();
        Double resultsplaext = new EXT(architecture).getResults();
        Double resultsSSC = new SD(architecture).getResults();
        Double resultsSVC = new SV(architecture).getResults();
        Double resultsTAM = new TAM(architecture).getResults();
        Double resultswclass = new WOCSCLASS(architecture).getResults();
        Double resultswinterface = new CS(architecture).getResults();

        assertEquals(new Double(17.0), resultsACLASS);
        assertEquals(resultsACLASS, ObjectiveFunctions.ACLASS.evaluate(architecture));
        assertEquals(resultsACLASS, Double.valueOf(solution.getObjective(0)));

        assertEquals(new Double(64.0), resultsACOMP);
        assertEquals(resultsACOMP, ObjectiveFunctions.ACOMP.evaluate(architecture));
        assertEquals(resultsACOMP, Double.valueOf(solution.getObjective(1)));

        assertEquals(new Double(2.0), resultsTV);
        assertEquals(resultsTV, ObjectiveFunctions.TV.evaluate(architecture));
        assertEquals(resultsTV, Double.valueOf(solution.getObjective(2)));

        assertEquals(new Double(1.7777777777777777), resultsCBCS);
        assertEquals(resultsCBCS, ObjectiveFunctions.RCC.evaluate(architecture));
        assertEquals(resultsCBCS, Double.valueOf(solution.getObjective(3)));

        assertEquals(new Double(5.0), resultsCOE);
        assertEquals(resultsCOE, ObjectiveFunctions.COE.evaluate(architecture));
        assertEquals(resultsCOE, Double.valueOf(solution.getObjective(4)));

        assertEquals(new Double(90.03333333333333), resultsConventional);
        assertEquals(resultsConventional, ObjectiveFunctions.CM.evaluate(architecture));
        assertEquals(resultsConventional, Double.valueOf(solution.getObjective(5)));

        assertEquals(new Double(600.0), resultsDC);
        assertEquals(resultsDC, ObjectiveFunctions.DC.evaluate(architecture));
        assertEquals(resultsDC, Double.valueOf(solution.getObjective(6)));

        assertEquals(new Double(115.0), resultsEC);
        assertEquals(resultsEC, ObjectiveFunctions.EC.evaluate(architecture));
        assertEquals(resultsEC, Double.valueOf(solution.getObjective(7)));

        assertEquals(new Double(0.24084455810512997), resultsElegance);
        assertEquals(resultsElegance, ObjectiveFunctions.ELEG.evaluate(architecture));
        assertEquals(resultsElegance, Double.valueOf(solution.getObjective(8)));

        assertEquals(new Double(885.0), resultsFM);
        assertEquals(resultsFM, ObjectiveFunctions.FM.evaluate(architecture));
        assertEquals(resultsFM, Double.valueOf(solution.getObjective(9)));

        assertEquals(new Double(25.0), resultsLCC);
        assertEquals(resultsLCC, ObjectiveFunctions.LCC.evaluate(architecture));
        assertEquals(resultsLCC, Double.valueOf(solution.getObjective(10)));

        assertEquals(new Double(1000.0), resultsplaext);
        assertEquals(resultsplaext, ObjectiveFunctions.EXT.evaluate(architecture));
        assertEquals(resultsplaext, Double.valueOf(solution.getObjective(11)));

        assertEquals(new Double(1.1), resultsSSC);
        assertEquals(resultsSSC, ObjectiveFunctions.SD.evaluate(architecture));
        assertEquals(resultsSSC, Double.valueOf(solution.getObjective(12)));

        assertEquals(new Double(0.09090909090909091), resultsSVC);
        assertEquals(resultsSVC, ObjectiveFunctions.SV.evaluate(architecture));
        assertEquals(resultsSVC, Double.valueOf(solution.getObjective(13)));

        assertEquals(new Double(5.833333333333333), resultsTAM);
        assertEquals(resultsTAM, ObjectiveFunctions.TAM.evaluate(architecture));
        assertEquals(resultsTAM, Double.valueOf(solution.getObjective(14)));

        assertEquals(new Double(0.11764705882352941), resultswclass);
        assertEquals(resultswclass, ObjectiveFunctions.WOCSCLASS.evaluate(architecture));
        assertEquals(resultswclass, Double.valueOf(solution.getObjective(15)));

        assertEquals(new Double(0.4444444444444444), resultswinterface);
        assertEquals(resultswinterface, ObjectiveFunctions.CS.evaluate(architecture));
        assertEquals(resultswinterface, Double.valueOf(solution.getObjective(16)));
    }

    @Test
    public void evaluateMM2onSmarty() throws Exception {
        String agm = Thread.currentThread().getContextClassLoader().getResource("PLASMarty").getFile();
        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setDirectoryToExportModels("");
        applicationYamlConfig.setDirectoryToSaveModels("");
        applicationYamlConfig.setPathToTemplateModelsDirectory("");
        OPLAThreadScope.setConfig(applicationYamlConfig);
        OPLA opla = new OPLA(agm + FileConstants.FILE_SEPARATOR + "mm2.smty");
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
        Double resultsACOMP = new ACOMP(architecture).getResults();
        Double resultsTV = new TV(architecture).getResults();
        Double resultsCBCS = new RCC(architecture).getResults();
        Double resultsCOE = new COE(architecture).getResults();
        Double resultsConventional = new CM(architecture).getResults();
        Double resultsDC = new DC(architecture).getResults();
        Double resultsEC = new EC(architecture).getResults();
        Double resultsElegance = new ELEG(architecture).getResults();
        Double resultsFM = new FM(architecture).getResults();
        Double resultsLCC = new LCC(architecture).getResults();
        Double resultsplaext = new EXT(architecture).getResults();
        Double resultsSSC = new SD(architecture).getResults();
        Double resultsSVC = new SV(architecture).getResults();
        Double resultsTAM = new TAM(architecture).getResults();
        Double resultswclass = new WOCSCLASS(architecture).getResults();
        Double resultswinterface = new CS(architecture).getResults();

        assertEquals(new Double(14.0), resultsACLASS);
        assertEquals(resultsACLASS, ObjectiveFunctions.ACLASS.evaluate(architecture));
        assertEquals(resultsACLASS, Double.valueOf(solution.getObjective(0)));

        assertEquals(new Double(58.0), resultsACOMP);
        assertEquals(resultsACOMP, ObjectiveFunctions.ACOMP.evaluate(architecture));
        assertEquals(resultsACOMP, Double.valueOf(solution.getObjective(1)));

        assertEquals(new Double(2.0), resultsTV);
        assertEquals(resultsTV, ObjectiveFunctions.TV.evaluate(architecture));
        assertEquals(resultsTV, Double.valueOf(solution.getObjective(2)));

        assertEquals(new Double(1.9333333333333333), resultsCBCS);
        assertEquals(resultsCBCS, ObjectiveFunctions.RCC.evaluate(architecture));
        assertEquals(resultsCBCS, Double.valueOf(solution.getObjective(3)));

        assertEquals(new Double(5.0), resultsCOE);
        assertEquals(resultsCOE, ObjectiveFunctions.COE.evaluate(architecture));
        assertEquals(resultsCOE, Double.valueOf(solution.getObjective(4)));

        assertEquals(new Double(81.06666666666668), resultsConventional);
        assertEquals(resultsConventional, ObjectiveFunctions.CM.evaluate(architecture));
        assertEquals(resultsConventional, Double.valueOf(solution.getObjective(5)));

        assertEquals(new Double(625.0), resultsDC);
        assertEquals(resultsDC, ObjectiveFunctions.DC.evaluate(architecture));
        assertEquals(resultsDC, Double.valueOf(solution.getObjective(6)));

        assertEquals(new Double(160.0), resultsEC);
        assertEquals(resultsEC, ObjectiveFunctions.EC.evaluate(architecture));
        assertEquals(resultsEC, Double.valueOf(solution.getObjective(7)));

        assertEquals(new Double(0.2522316727745658), resultsElegance);
        assertEquals(resultsElegance, ObjectiveFunctions.ELEG.evaluate(architecture));
        assertEquals(resultsElegance, Double.valueOf(solution.getObjective(8)));

        assertEquals(new Double(1013.0), resultsFM);
        assertEquals(resultsFM, ObjectiveFunctions.FM.evaluate(architecture));
        assertEquals(resultsFM, Double.valueOf(solution.getObjective(9)));

        assertEquals(new Double(30.0), resultsLCC);
        assertEquals(resultsLCC, ObjectiveFunctions.LCC.evaluate(architecture));
        assertEquals(resultsLCC, Double.valueOf(solution.getObjective(10)));

        assertEquals(new Double(1000.0), resultsplaext);
        assertEquals(resultsplaext, ObjectiveFunctions.EXT.evaluate(architecture));
        assertEquals(resultsplaext, Double.valueOf(solution.getObjective(11)));

        assertEquals(new Double(1.1428571428571428), resultsSSC);
        assertEquals(resultsSSC, ObjectiveFunctions.SD.evaluate(architecture));
        assertEquals(resultsSSC, Double.valueOf(solution.getObjective(12)));

        assertEquals(new Double(0.125), resultsSVC);
        assertEquals(resultsSVC, ObjectiveFunctions.SV.evaluate(architecture));
        assertEquals(resultsSVC, Double.valueOf(solution.getObjective(13)));

        assertEquals(new Double(5.866666666666666), resultsTAM);
        assertEquals(resultsTAM, ObjectiveFunctions.TAM.evaluate(architecture));
        assertEquals(resultsTAM, Double.valueOf(solution.getObjective(14)));

        assertEquals(new Double(0.21428571428571427), resultswclass);
        assertEquals(resultswclass, ObjectiveFunctions.WOCSCLASS.evaluate(architecture));
        assertEquals(resultswclass, Double.valueOf(solution.getObjective(15)));

        assertEquals(new Double(0.6666666666666666), resultswinterface);
        assertEquals(resultswinterface, ObjectiveFunctions.CS.evaluate(architecture));
        assertEquals(resultswinterface, Double.valueOf(solution.getObjective(16)));
    }

    @Test
    public void evaluateMMAtualonSmarty() throws Exception {
        String agm = Thread.currentThread().getContextClassLoader().getResource("PLASMarty").getFile();
        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setDirectoryToExportModels("");
        applicationYamlConfig.setDirectoryToSaveModels("");
        applicationYamlConfig.setPathToTemplateModelsDirectory("");
        OPLAThreadScope.setConfig(applicationYamlConfig);
        OPLA opla = new OPLA(agm + FileConstants.FILE_SEPARATOR + "MMAtual.smty");
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
        Double resultsACOMP = new ACOMP(architecture).getResults();
        Double resultsTV = new TV(architecture).getResults();
        Double resultsCBCS = new RCC(architecture).getResults();
        Double resultsCOE = new COE(architecture).getResults();
        Double resultsConventional = new CM(architecture).getResults();
        Double resultsDC = new DC(architecture).getResults();
        Double resultsEC = new EC(architecture).getResults();
        Double resultsElegance = new ELEG(architecture).getResults();
        Double resultsFM = new FM(architecture).getResults();
        Double resultsLCC = new LCC(architecture).getResults();
        Double resultsplaext = new EXT(architecture).getResults();
        Double resultsSSC = new SD(architecture).getResults();
        Double resultsSVC = new SV(architecture).getResults();
        Double resultsTAM = new TAM(architecture).getResults();
        Double resultswclass = new WOCSCLASS(architecture).getResults();
        Double resultswinterface = new CS(architecture).getResults();

        assertEquals(new Double(14.0), resultsACLASS);
        assertEquals(resultsACLASS, ObjectiveFunctions.ACLASS.evaluate(architecture));
        assertEquals(resultsACLASS, Double.valueOf(solution.getObjective(0)));

        assertEquals(new Double(58.0), resultsACOMP);
        assertEquals(resultsACOMP, ObjectiveFunctions.ACOMP.evaluate(architecture));
        assertEquals(resultsACOMP, Double.valueOf(solution.getObjective(1)));

        assertEquals(new Double(2.0), resultsTV);
        assertEquals(resultsTV, ObjectiveFunctions.TV.evaluate(architecture));
        assertEquals(resultsTV, Double.valueOf(solution.getObjective(2)));

        assertEquals(new Double(1.9333333333333333), resultsCBCS);
        assertEquals(resultsCBCS, ObjectiveFunctions.RCC.evaluate(architecture));
        assertEquals(resultsCBCS, Double.valueOf(solution.getObjective(3)));

        assertEquals(new Double(7.0), resultsCOE);
        assertEquals(resultsCOE, ObjectiveFunctions.COE.evaluate(architecture));
        assertEquals(resultsCOE, Double.valueOf(solution.getObjective(4)));

        assertEquals(new Double(82.00952380952381), resultsConventional);
        assertEquals(resultsConventional, ObjectiveFunctions.CM.evaluate(architecture));
        assertEquals(resultsConventional, Double.valueOf(solution.getObjective(5)));

        assertEquals(new Double(680.0), resultsDC);
        assertEquals(resultsDC, ObjectiveFunctions.DC.evaluate(architecture));
        assertEquals(resultsDC, Double.valueOf(solution.getObjective(6)));

        assertEquals(new Double(225.0), resultsEC);
        assertEquals(resultsEC, ObjectiveFunctions.EC.evaluate(architecture));
        assertEquals(resultsEC, Double.valueOf(solution.getObjective(7)));

        assertEquals(new Double(0.2650066545583145), resultsElegance);
        assertEquals(resultsElegance, ObjectiveFunctions.ELEG.evaluate(architecture));
        assertEquals(resultsElegance, Double.valueOf(solution.getObjective(8)));

        assertEquals(new Double(1122.0), resultsFM);
        assertEquals(resultsFM, ObjectiveFunctions.FM.evaluate(architecture));
        assertEquals(resultsFM, Double.valueOf(solution.getObjective(9)));

        assertEquals(new Double(28.0), resultsLCC);
        assertEquals(resultsLCC, ObjectiveFunctions.LCC.evaluate(architecture));
        assertEquals(resultsLCC, Double.valueOf(solution.getObjective(10)));

        assertEquals(new Double(6.999999687075629), resultsplaext);
        assertEquals(resultsplaext, ObjectiveFunctions.EXT.evaluate(architecture));
        assertEquals(resultsplaext, Double.valueOf(solution.getObjective(11)));

        assertEquals(new Double(1.1428571428571428), resultsSSC);
        assertEquals(resultsSSC, ObjectiveFunctions.SD.evaluate(architecture));
        assertEquals(resultsSSC, Double.valueOf(solution.getObjective(12)));

        assertEquals(new Double(0.125), resultsSVC);
        assertEquals(resultsSVC, ObjectiveFunctions.SV.evaluate(architecture));
        assertEquals(resultsSVC, Double.valueOf(solution.getObjective(13)));

        assertEquals(new Double(5.866666666666666), resultsTAM);
        assertEquals(resultsTAM, ObjectiveFunctions.TAM.evaluate(architecture));
        assertEquals(resultsTAM, Double.valueOf(solution.getObjective(14)));

        assertEquals(new Double(0.14285714285714285), resultswclass);
        assertEquals(resultswclass, ObjectiveFunctions.WOCSCLASS.evaluate(architecture));
        assertEquals(resultswclass, Double.valueOf(solution.getObjective(15)));

        assertEquals(new Double(0.6666666666666666), resultswinterface);
        assertEquals(resultswinterface, ObjectiveFunctions.CS.evaluate(architecture));
        assertEquals(resultswinterface, Double.valueOf(solution.getObjective(16)));
    }
}
