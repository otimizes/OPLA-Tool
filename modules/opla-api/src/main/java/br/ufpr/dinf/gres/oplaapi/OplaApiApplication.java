package br.ufpr.dinf.gres.oplaapi;

import br.ufpr.dinf.gres.oplaapi.config.ApplicationFile;
import br.ufpr.dinf.gres.oplaapi.util.UserHome;
import br.ufpr.dinf.gres.oplaapi.util.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
        OplaApiApplication.createPathOplaTool();
        OplaApiApplication.configureApplicationFile();
        OplaApiApplication.setPathDatabase();
        OplaApiApplication.configureDb();
        SpringApplication.run(OplaApiApplication.class, args);
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
