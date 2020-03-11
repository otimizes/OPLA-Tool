package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.architecture.builders.ArchitectureBuilder;
import br.ufpr.dinf.gres.architecture.config.ApplicationYamlConfig;
import br.ufpr.dinf.gres.architecture.io.OPLAThreadScope;
import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.architecture.util.Constants;
import br.ufpr.dinf.gres.core.jmetal4.metrics.classical.*;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.RelationalCohesion;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static org.junit.Assert.*;

public class MetricsTest {

    @Test
    public void evaluateAGM() throws ClassNotFoundException {
        String agm = Thread.currentThread().getContextClassLoader().getResource("agm").getFile();
        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setPathToProfile(agm + Constants.FILE_SEPARATOR + "smarty.profile.uml");
        applicationYamlConfig.setPathToProfileConcern(agm + Constants.FILE_SEPARATOR + "concerns.profile.uml");
        applicationYamlConfig.setPathToProfilePatterns(agm + Constants.FILE_SEPARATOR + "patterns.profile.uml");
        applicationYamlConfig.setPathToProfileRelationships(agm + Constants.FILE_SEPARATOR + "relationships.profile.uml");
        OPLAThreadScope.setConfig(applicationYamlConfig);

        List<String> xmis = Arrays.asList(
                agm + Constants.FILE_SEPARATOR + "agm.uml"
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

        Architecture architecture = arrayList.get(0);

        Double resultsCOE = new COE(architecture).getResults();
        assertEquals( new Double(33.0), resultsCOE);
        Double resultsLCC = new LCC(architecture).getResults();
        assertEquals( new Double(26.0), resultsLCC);
        Double resultsHRelationalCohesion = new RelationalCohesion(architecture).getResults();
        assertEquals( new Double(7.0), resultsHRelationalCohesion);
        Double resultsFM = new MSI(architecture).getResults();
        assertEquals( new Double(758.0), resultsFM);
        Double resultsACLASS = new ACLASS(architecture).getResults();
        assertEquals( new Double(30.0), resultsACLASS);
        Double resultsACOMP = new ACOMP(architecture).getResults();
        assertEquals( new Double(59.0), resultsACOMP);
        Double resultsTAM = new TAM(architecture).getResults();
        assertEquals( new Double(3.9285714285714284), resultsTAM);
        Double resultsEC = new EC(architecture).getResults();
        assertEquals( new Double(124.0), resultsEC);
        Double resultsDC = new DC(architecture).getResults();
        assertEquals( new Double(430.0), resultsDC);


        System.out.println("");
    }
}
