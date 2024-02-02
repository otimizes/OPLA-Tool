package br.otimizes.oplatool.api;

import br.otimizes.oplatool.domain.config.*;
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
import java.io.StringReader;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

@SpringBootApplication
//@EnableTransactionManagement(proxyTargetClass = true)
@EntityScan(basePackages = {
        "br.otimizes.oplatool.domain.entity",
        "br.otimizes.oplatool.domain.entity.objectivefunctions"
})
@EnableJpaRepositories(basePackages = {
        "br.otimizes.oplatool.persistence.repository"
})
@ComponentScan(basePackages = {
        "br.otimizes.oplatool.api.resource",
        "br.otimizes.oplatool.api.gateway",
        "br.otimizes.oplatool.persistence.service",
        "br.otimizes.oplatool.core.jmetal4.experiments",
        "br.otimizes.oplatool.core.persistence",
        "br.otimizes.oplatool.core.jmetal4.metrics"
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

    public static void main(String[] args) throws InterruptedException, URISyntaxException {
        try {
            if (OplaApiApplication.class.getResource("").openConnection() instanceof JarURLConnection) {
                FileConstants.BASE_RESOURCES = "BOOT-INF/classes/";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.createPathsOPLATool();
        UserHome.createDefaultOPLAPathIfDontExists();
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
