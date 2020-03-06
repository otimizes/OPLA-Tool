package br.ufpr.dinf.gres.core.jmetal4.experiments;

import org.springframework.stereotype.Service;

@Service
public class PAES_OPLA_FeatMutInitializer implements AlgorithmBase<PaesConfigs> {

    private final PAES_OPLA_FeatMut paes;

    public PAES_OPLA_FeatMutInitializer(PAES_OPLA_FeatMut paes) {
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