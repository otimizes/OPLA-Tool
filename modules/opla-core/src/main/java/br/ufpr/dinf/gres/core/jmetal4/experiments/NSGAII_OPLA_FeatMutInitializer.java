package br.ufpr.dinf.gres.core.jmetal4.experiments;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class NSGAII_OPLA_FeatMutInitializer implements AlgorithmBase<NSGAIIConfig> {
    private static final Logger LOGGER = Logger.getLogger(NSGAII_OPLA_FeatMutInitializer.class);

    private final NSGAII_OPLA_FeatMut nsgaiiFeatMut;

    public NSGAII_OPLA_FeatMutInitializer(NSGAII_OPLA_FeatMut nsgaiiFeatMut) {
        this.nsgaiiFeatMut = nsgaiiFeatMut;
    }

    @Override
    public void run(NSGAIIConfig experimentCommomConfigs) {
        try {
            LOGGER.info("execute()");
            nsgaiiFeatMut.execute(experimentCommomConfigs);
            LOGGER.info("Finished");
        } catch (Exception e) {
            LOGGER.error(e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

}