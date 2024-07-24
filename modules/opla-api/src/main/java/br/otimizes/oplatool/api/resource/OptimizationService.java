package br.otimizes.oplatool.api.resource;

import br.otimizes.oplatool.api.dto.OptimizationDto;
import br.otimizes.oplatool.api.gateway.IGateway;
import br.otimizes.oplatool.api.utils.Interaction;
import br.otimizes.oplatool.api.utils.Interactions;
import br.otimizes.oplatool.architecture.builders.ArchitectureBuilderSMarty;
import br.otimizes.oplatool.architecture.io.OPLALogs;
import br.otimizes.oplatool.architecture.io.OptimizationInfo;
import br.otimizes.oplatool.architecture.io.OptimizationInfoStatus;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.domain.OPLAThreadScope;
import br.otimizes.oplatool.domain.config.ApplicationFileConfigThreadScope;
import br.otimizes.oplatool.domain.config.FileConstants;
import br.otimizes.oplatool.persistence.service.OPLACommand;
import br.ufpr.dinf.gres.loglog.LogLog;
import br.ufpr.dinf.gres.loglog.Logger;
import com.rits.cloning.Cloner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;

@Service
public class OptimizationService {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(OptimizationService.class);
    private static final LogLog VIEW_LOGGER = Logger.getLogger();

    private final InteractiveEmail interactiveEmail;
    private final ApplicationContext context;

    public OptimizationService(InteractiveEmail interactiveEmail, ApplicationContext context) {
        this.interactiveEmail = interactiveEmail;
        this.context = context;
    }

    public Mono<OptimizationInfo> execute(OptimizationDto optimizationDto) {
        String token = OPLAThreadScope.token.get();
        Thread thread = new Thread(() -> {
            configureThreadScope(optimizationDto, token);
            IGateway bean = context.getBean(optimizationDto.getAlgorithm().getType());
            bean.execute(optimizationDto);
        });
        thread.setName(OPLAThreadScope.token.get() + FileConstants.FILE_SEPARATOR + thread.getId());
        thread.start();
        OptimizationInfo optimizationInfo = new OptimizationInfo(OPLAThreadScope.token.get() + FileConstants.FILE_SEPARATOR + thread.getId(), "", OptimizationInfoStatus.RUNNING);
        optimizationInfo.threadId = thread.getId();
        optimizationInfo.currentGeneration = 0;
        return Mono.just(optimizationInfo).subscribeOn(Schedulers.elastic());
    }


    File downloadAlternative(String token, String hash, Integer id) {
        SolutionSet solutionSet = Interactions.get(token, hash).solutionSet;
        Solution solution = solutionSet.get(id);

        OptimizationInfo first = OPLALogs.getFirst(token, hash);
        SolutionSet solutionSet1 = new SolutionSet();
        solutionSet1.setCapacity(1);
        solutionSet1.add(solution);

        String dirOnAnalysis = ApplicationFileConfigThreadScope.getDirectoryToExportModels() + OPLAThreadScope.token.get() + FileConstants.FILE_SEPARATOR + hash + FileConstants.FILE_SEPARATOR + "interaction/";
        deleteDirectory(new File(dirOnAnalysis));
        new File(dirOnAnalysis).mkdirs();

        String plaName = "interaction-" + first.currentGeneration + ".solution-" + id + ".smty";
        new SolutionSet(solutionSet1).saveVariablesToFile(OPLAThreadScope.token.get() + FileConstants.FILE_SEPARATOR + hash + FileConstants.FILE_SEPARATOR + "interaction/" + plaName);
        return new File(dirOnAnalysis);
    }

    void openAlternative(String token, String hash, Integer id) throws InterruptedException, SAXException,
            ParserConfigurationException, XPathExpressionException, IOException {
        File file = downloadAlternative(token, hash, id);
        File[] files = file.listFiles();
        File fileToOpen = files[0];
        String pathSmarty = ApplicationFileConfigThreadScope.getPathSmarty();
        Process process = OPLACommand.executeJar(pathSmarty, fileToOpen.getAbsolutePath());
        process.waitFor();
        setInteraction(token, hash, id, fileToOpen);
    }

    public Architecture setInteraction(String token, String hash, Integer solutionId, File file) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
        Architecture architecture = new ArchitectureBuilderSMarty().create(file);
        Interaction interaction = Interactions.get(token, hash);
        Cloner cloner = new Cloner();
        Solution solution = cloner.shallowClone(interaction.solutionSet.get(solutionId));
        solution.setDecisionVariables(new Architecture[]{architecture});
        interaction.solutionSet.getSolutions().set(solutionId, solution);
        return architecture;
    }

    private boolean deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDirectory(children[i]);
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public File downloadAllAlternative(String token, String hash) {
        SolutionSet solutionSet = Interactions.get(token, hash).solutionSet;
        String plaNameOnAnalyses = "Interaction_" + token + "_" + hash + "_" + "_" + solutionSet.get(0).getAlternativeArchitecture().getName();
        String dirOnAnalyses = ApplicationFileConfigThreadScope.getDirectoryToExportModels() + OPLAThreadScope.token.get() + FileConstants.FILE_SEPARATOR + "interaction/";
        boolean delete = deleteDirectory(new File(dirOnAnalyses));
        boolean create = new File(dirOnAnalyses).mkdir();
        new SolutionSet(solutionSet).saveVariablesToFile(OPLAThreadScope.token.get() + FileConstants.FILE_SEPARATOR + "interaction/" + plaNameOnAnalyses);
        File file = new File(dirOnAnalyses);
        return file;
    }

    private void configureThreadScope(OptimizationDto optimizationDto, String token) {
        OPLAThreadScope.token.set(token);
        OPLAThreadScope.mainThreadId.set(Thread.currentThread().getId());
        OPLAThreadScope.userDir.set(optimizationDto.getConfig().getDirectoryToExportModels() + OPLAThreadScope.token.get() + FileConstants.FILE_SEPARATOR);
        OPLAThreadScope.pla.set(OPLAThreadScope.userDir.get() + optimizationDto.getInputArchitecture());
        optimizationDto.getConfig().setPathToProfile(optimizationDto.getConfig().getDirectoryToExportModels() + OPLAThreadScope.token.get() + FileConstants.FILE_SEPARATOR + optimizationDto.getConfig().getPathToProfile());
        optimizationDto.getConfig().setPathToProfileRelationships(optimizationDto.getConfig().getDirectoryToExportModels() + OPLAThreadScope.token.get() + FileConstants.FILE_SEPARATOR + optimizationDto.getConfig().getPathToProfileRelationships());
        optimizationDto.getConfig().setPathToProfilePatterns(optimizationDto.getConfig().getDirectoryToExportModels() + OPLAThreadScope.token.get() + FileConstants.FILE_SEPARATOR + optimizationDto.getConfig().getPathToProfilePatterns());
        optimizationDto.getConfig().setPathToProfileConcern(optimizationDto.getConfig().getDirectoryToExportModels() + OPLAThreadScope.token.get() + FileConstants.FILE_SEPARATOR + optimizationDto.getConfig().getPathToProfileConcern());
        OPLAThreadScope.setConfig(optimizationDto.getConfig());
        optimizationDto.setInputArchitecture(OPLAThreadScope.pla.get());
        optimizationDto.setInteractiveFunction(solutionSet -> interactiveEmail.run(solutionSet, optimizationDto));
    }

}
