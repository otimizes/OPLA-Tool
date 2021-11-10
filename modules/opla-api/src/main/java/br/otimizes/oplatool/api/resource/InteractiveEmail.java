package br.otimizes.oplatool.api.resource;

import br.otimizes.oplatool.api.dto.OptimizationDto;
import br.otimizes.oplatool.api.utils.Interaction;
import br.otimizes.oplatool.api.utils.Interactions;
import br.otimizes.oplatool.domain.entity.EmailDto;
import br.otimizes.oplatool.architecture.io.OPLALogs;
import br.otimizes.oplatool.architecture.io.OptimizationInfo;
import br.otimizes.oplatool.architecture.io.OptimizationInfoStatus;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.learning.Clustering;
import br.otimizes.oplatool.domain.OPLAThreadScope;
import br.otimizes.oplatool.domain.entity.User;
import br.otimizes.oplatool.persistence.service.EmailService;
import br.otimizes.oplatool.persistence.service.UserService;
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
        for (int i = 0; i < solutionSet.getSolutionSet().size(); i++) {
            // initial in 1
            solutionSet.get(i).setId(i);
        }
        // when "debugging" gives an error HERE
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
