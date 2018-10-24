package br.ufpr.dinf.gres.opla.view.util;

import java.nio.file.FileSystems;

/**
 * @author Fernando
 */
public class Constants {

    public static final String USER_HOME = System.getProperty("user.home");
    public static final String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();

    public static final String APPLICATION_YAML_NAME = "application.yaml";
    public static final String CONFIG_PATH = "config";
    public static final String LOCAL_YAML_PATH = Constants.CONFIG_PATH + Constants.FILE_SEPARATOR + Constants.APPLICATION_YAML_NAME;
    

    public static final String PATH_EMPTY_DB = "emptyDB";
    public static final String EMPTY_DB_NAME = "oplatool.db";

    public static final String PROFILES_DIR = "profiles";
    public static final String TEMPLATES_DIR = "templates";
    public static final String TEMP_DIR = "temp";
    public static final String OUTPUT_DIR = "output";
    public static final String BINS_DIR = "bins";
    public static final String HYPERVOLUME_DIR = "hv";
    public static final String DB_DIR = "db";

    public static final String PROFILE_SMART_NAME = "smarty.profile.uml";
    public static final String PROFILE_CONCERNS_NAME = "concerns.profile.uml";
    public static final String PROFILE_PATTERN_NAME = "patterns.profile.uml";
    public static final String PROFILE_RELATIONSHIP_NAME = "relationships.profile.uml";

    public static final String SIMPLES_UML_NAME = "simples.uml";
    public static final String SIMPLES_DI_NAME = "simples.di";
    public static final String SIMPLES_NOTATION_NAME = "simples.notation";

}
