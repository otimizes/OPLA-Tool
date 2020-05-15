package br.ufpr.dinf.gres.api;

import br.ufpr.dinf.gres.domain.config.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
//@EnableTransactionManagement(proxyTargetClass = true)
@EntityScan(basePackages = {
        "br.ufpr.dinf.gres.domain.entity",
        "br.ufpr.dinf.gres.domain.entity.objectivefunctions"
})
@EnableJpaRepositories(basePackages = {
        "br.ufpr.dinf.gres.persistence.repository"
})
@ComponentScan(basePackages = {
        "br.ufpr.dinf.gres.api.resource",
        "br.ufpr.dinf.gres.api.gateway",
        "br.ufpr.dinf.gres.persistence.service",
        "br.ufpr.dinf.gres.core.jmetal4.experiments",
        "br.ufpr.dinf.gres.core.persistence",
        "br.ufpr.dinf.gres.core.jmetal4.metrics"
})
@EnableAsync
public class OplaApiApplication {

    private final Environment env;

    public OplaApiApplication(Environment env) {
        this.env = env;
    }

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
        try {
            if (OplaApiApplication.class.getResource("").openConnection() instanceof JarURLConnection) {
                FileConstants.BASE_RESOURCES = "BOOT-INF/classes/";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.createPathsOplaTool();
        UserHome.createDefaultOplaPathIfDontExists();
        Utils.createDataBaseIfNotExists();
        UserHome.copyTemplates();
        ManagerApplicationFileConfig instance = ApplicationFileConfig.getApplicationFileConfig();
        try {
            instance.configureDefaultLocaleToExportModels();
            instance.updateDefaultPathToSaveModels();
            instance.updateDefaultPathToTemplateFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SpringApplication.run(OplaApiApplication.class, args);
    }
}