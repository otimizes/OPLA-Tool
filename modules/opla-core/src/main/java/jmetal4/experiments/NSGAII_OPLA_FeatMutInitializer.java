package jmetal4.experiments;

import org.apache.log4j.Logger;

public class NSGAII_OPLA_FeatMutInitializer implements AlgorithmBase {
	private static final Logger LOGGER = Logger.getLogger(NSGAII_OPLA_FeatMutInitializer.class);

	private NSGAIIConfig config;

	public NSGAII_OPLA_FeatMutInitializer(NSGAIIConfig config) {
		this.config = config;
	}

	@Override
	public void run() {
		NSGAII_OPLA_FeatMut nsgaiiFeatMut = new NSGAII_OPLA_FeatMut();
		nsgaiiFeatMut.setConfigs(this.config);
		try {

			LOGGER.info("execute()");
			nsgaiiFeatMut.execute();
			LOGGER.info("Finished");
		} catch (Exception e) {
			LOGGER.error(e);
			throw new RuntimeException(e.getMessage());

		}
	}

}