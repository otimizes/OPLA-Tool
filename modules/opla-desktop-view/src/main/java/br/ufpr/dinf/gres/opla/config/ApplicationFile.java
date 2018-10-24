package br.ufpr.dinf.gres.opla.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

/**
 * @author elf
 */
public class ApplicationFile {

	private static final Logger LOGGER = Logger.getLogger(ApplicationFile.class);

	private static ManagerApplicationConfig instance = null;

	protected ApplicationFile() {
	}

	public static ManagerApplicationConfig getInstance() {
		if (instance == null) {
			instance = new ManagerApplicationConfig();
		}
		return instance;
	}

	/**
	 * Default path is config/application.yaml If you need chance it, before call
	 * this method set the new path with <code>setNewPathToConfigurationFile</code>
	 * <br/>
	 * <br/>
	 * IMPORTANT: Only call this method once.
	 *
	 * @throws FileNotFoundException
	 */
	public static void load() {
		try {
			Yaml yaml = new Yaml();
			Path pathToNewConfigurations = instance.getConfig().getPathToNewConfigurations();
			if (StringUtils.isNotBlank(pathToNewConfigurations.toString())) {
				PathConfig dir = yaml.loadAs(new FileInputStream(pathToNewConfigurations.toFile()), PathConfig.class);
				LOGGER.info("New Path" + dir);
			} else {
				InputStream inputStream = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("config/application.yaml");
				YamlFile dir = yaml.loadAs(inputStream, YamlFile.class);
				LOGGER.info("Default Path " + dir);
			}
		} catch (FileNotFoundException e) {
			LOGGER.error("I can't read the configuration file: application.yaml");
		}
	}

	class YamlFile {
		
		String directoryToSaveModels;

		String directoryToExportModels;

		String pathToProfile;

		String pathToProfileConcern;

		String pathToTemplateModelsDirectory;

		String pathToProfileRelationships;

		String pathToProfilePatterns;
	}
}
