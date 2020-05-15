package br.ufpr.dinf.gres.api.resource;

import br.ufpr.dinf.gres.api.dto.EmailDto;
import br.ufpr.dinf.gres.api.dto.OptimizationDto;
import br.ufpr.dinf.gres.api.utils.Interaction;
import br.ufpr.dinf.gres.api.utils.Interactions;
import br.ufpr.dinf.gres.architecture.io.OPLALogs;
import br.ufpr.dinf.gres.architecture.io.OptimizationInfo;
import br.ufpr.dinf.gres.architecture.io.OptimizationInfoStatus;
import br.ufpr.dinf.gres.core.jmetal4.core.SolutionSet;
import br.ufpr.dinf.gres.core.learning.Clustering;
import br.ufpr.dinf.gres.domain.OPLAThreadScope;
import org.springframework.stereotype.Service;

@Service
public class InteractiveEmail {

    private final EmailService emailService;

    public InteractiveEmail(EmailService emailService) {
        this.emailService = emailService;
    }

    public SolutionSet run(SolutionSet solutionSet, OptimizationDto optimizationDto) {
        OPLALogs.add(new OptimizationInfo(OPLAThreadScope.mainThreadId.get(), "Your optimization is waiting for evaluation.", OptimizationInfoStatus.INTERACT));
        try {
            emailService.send(new EmailDto(OPLAThreadScope.token.get(), "Your optimization is waiting for evaluation.", "Your optimization is waiting for evaluation."));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Clustering clustering = new Clustering(solutionSet, optimizationDto.getClusteringAlgorithm());
            clustering.setNumClusters(solutionSet.getSolutionSet().get(0).numberOfObjectives() + 1);
            clustering.run();
            Interactions.set(OPLAThreadScope.mainThreadId.get(), new Interaction(solutionSet));
            while (!Interactions.get(OPLAThreadScope.mainThreadId.get()).updated) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Interactions.get(OPLAThreadScope.mainThreadId.get()).solutionSet.getSolutionSet();
    }


}
