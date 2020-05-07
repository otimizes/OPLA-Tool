package br.ufpr.dinf.gres.core.jmetal4.experiments;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class NSGAIIOPLAFeatMutInitializer implements AlgorithmBase<NSGAIIConfig> {
    private static final Logger LOGGER = Logger.getLogger(NSGAIIOPLAFeatMutInitializer.class);

    private final NSGAIIOPLAFeatMut nsgaiiFeatMut;

    public NSGAIIOPLAFeatMutInitializer(NSGAIIOPLAFeatMut nsgaiiFeatMut) {
        this.nsgaiiFeatMut = nsgaiiFeatMut;
    }

    @Override
    public void run(NSGAIIConfig experimentCommomConfigs) {
        try {
            nsgaiiFeatMut.execute(experimentCommomConfigs);
        } catch (Exception e) {
            LOGGER.error(e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

}