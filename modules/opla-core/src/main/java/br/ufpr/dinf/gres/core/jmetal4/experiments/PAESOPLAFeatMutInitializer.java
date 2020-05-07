package br.ufpr.dinf.gres.core.jmetal4.experiments;

import org.springframework.stereotype.Service;

@Service
public class PAESOPLAFeatMutInitializer implements AlgorithmBase<PaesConfigs> {

    private final PAESOPLAFeatMut paes;

    public PAESOPLAFeatMutInitializer(PAESOPLAFeatMut paes) {
        this.paes = paes;
    }

    @Override
    public void run(PaesConfigs config) {
        try {
            paes.execute(config);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}