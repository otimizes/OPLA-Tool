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
}
