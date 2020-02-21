package br.ufpr.dinf.gres.oplaapi;

import arquitetura.io.FileUtils;
import br.ufpr.dinf.gres.oplaapi.config.ApplicationFile;
import br.ufpr.dinf.gres.oplaapi.config.ManagerApplicationConfig;
import br.ufpr.dinf.gres.oplaapi.util.Constants;
import br.ufpr.dinf.gres.oplaapi.util.UserHome;
import br.ufpr.dinf.gres.oplaapi.util.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class OplaApiApplication {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }

    public static void main(String[] args) {
        Utils.createPathsOplaTool();
        initialConfiguration();
        ManagerApplicationConfig instance = ApplicationFile.getInstance();
        try {
            instance.configureDefaultLocaleToExportModels();
            instance.updateDefaultPathToSaveModels();
            instance.updateDefaultPathToTemplateFiles();
            copyTemplates();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        SpringApplication.run(OplaApiApplication.class, args);
    }

    private static void initialConfiguration() {
        createPathOplaTool();
        configureApplicationFile();
        setPathDatabase();
        configureDb();
    }

    public static void copyTemplates() throws URISyntaxException {
        URI uriTemplatesDir = ClassLoader.getSystemResource(Constants.TEMPLATES_DIR).toURI();
        String simplesUmlPath = Constants.SIMPLES_UML_NAME;
        String simplesDiPath = Constants.SIMPLES_DI_NAME;
        String simplesNotationPath = Constants.SIMPLES_NOTATION_NAME;

        Path externalPathSimplesUml = Paths.get(UserHome.getPathToTemplates() + simplesUmlPath);
        Path externalPathSimplesDi = Paths.get(UserHome.getPathToTemplates() + simplesDiPath);
        Path externalPathSimplesNotation = Paths.get(UserHome.getPathToTemplates() + simplesNotationPath);

        FileUtils.copy(Paths.get(uriTemplatesDir.getSchemeSpecificPart()).resolve(simplesUmlPath), externalPathSimplesUml);
        FileUtils.copy(Paths.get(uriTemplatesDir.getSchemeSpecificPart()).resolve(simplesDiPath), externalPathSimplesDi);
        FileUtils.copy(Paths.get(uriTemplatesDir.getSchemeSpecificPart()).resolve(simplesNotationPath), externalPathSimplesNotation);
    }

    /**
     * Cria diretório raiz da ferramentas
     */
    private static void createPathOplaTool() {
        UserHome.createDefaultOplaPathIfDontExists();
    }

    private static void configureApplicationFile() {
        ApplicationFile.getInstance();
    }


    private static void setPathDatabase() {
        database.Database.setPathToDB(UserHome.getPathToDb());
    }

    /**
     * Somente faz uma copia do banco de dados vazio para a pasta da oplatool no
     * diretorio do usuario se o mesmo nao existir.
     *
     * @throws Exception
     */
    private static void configureDb() {
        Utils.createDataBaseIfNotExists();
    }


}
