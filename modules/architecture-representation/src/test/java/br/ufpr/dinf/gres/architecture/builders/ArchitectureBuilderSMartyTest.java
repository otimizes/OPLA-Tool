package br.ufpr.dinf.gres.architecture.builders;

import br.ufpr.dinf.gres.architecture.config.ApplicationYamlConfig;
import br.ufpr.dinf.gres.architecture.io.OPLAConfigThreadScope;
import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.util.Constants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArchitectureBuilderSMartyTest {

    @Test
    public void buildAGM1OnSmarty() throws Exception {
        String agm = Thread.currentThread().getContextClassLoader().getResource("PLASMarty").getFile();
        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setDirectoryToExportModels("/home/wmfsystem/oplatool/output/");
        applicationYamlConfig.setDirectoryToSaveModels("/home/wmfsystem/oplatool/temp/");
        applicationYamlConfig.setPathToTemplateModelsDirectory("/home/wmfsystem/oplatool/templates/");
        OPLAConfigThreadScope.setConfig(applicationYamlConfig);
        String xmiFilePath = agm + Constants.FILE_SEPARATOR + "agm1.smty";
        Architecture architecture = new ArchitectureBuilderSMarty().create(xmiFilePath);

        // Exemplo
        assertEquals(9, architecture.getAllPackages().size());

        System.out.println("Implemente seus testes aqui.");
    }

}
