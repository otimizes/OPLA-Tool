package br.ufpr.dinf.gres.api.resource;

import br.ufpr.dinf.gres.domain.entity.EmailDto;
import br.ufpr.dinf.gres.api.dto.OptimizationDto;
import br.ufpr.dinf.gres.api.utils.Interaction;
import br.ufpr.dinf.gres.api.utils.Interactions;
import br.ufpr.dinf.gres.architecture.io.OPLALogs;
import br.ufpr.dinf.gres.architecture.io.OptimizationInfo;
import br.ufpr.dinf.gres.architecture.io.OptimizationInfoStatus;
import br.ufpr.dinf.gres.core.jmetal4.core.SolutionSet;
import br.ufpr.dinf.gres.core.learning.Clustering;
import br.ufpr.dinf.gres.domain.OPLAThreadScope;
import br.ufpr.dinf.gres.domain.entity.User;
import br.ufpr.dinf.gres.persistence.service.EmailService;
import br.ufpr.dinf.gres.persistence.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class InteractiveEmail {

    private final EmailService emailService;
    private final UserService userService;

    public InteractiveEmail(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    public SolutionSet run(SolutionSet solutionSet, OptimizationDto optimizationDto) {
        OPLALogs.add(new OptimizationInfo(OPLAThreadScope.mainThreadId.get(), "Your optimization is waiting for evaluation.", OptimizationInfoStatus.INTERACT));
        try {
            User userByEmail = userService.findUserByToken(OPLAThreadScope.token.get());
            emailService.send(new EmailDto(userByEmail.getLogin(), "Your optimization is waiting for evaluation.", "Your optimization is waiting for evaluation."));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Clustering clustering = new Clustering(solutionSet, optimizationDto.getClusteringAlgorithm());
            clustering.setNumClusters(solutionSet.getSolutionSet().get(0).numberOfObjectives() + 1);
            clustering.run();
            Interactions.set(OPLAThreadScope.hash.get(), new Interaction(solutionSet));
            System.out.println("Waiting interaction: " + !Interactions.get(OPLAThreadScope.hash.get()).updated);
            while (!Interactions.get(OPLAThreadScope.hash.get()).updated) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Interactions.get(OPLAThreadScope.hash.get()).solutionSet.getSolutionSet();
    }


}
