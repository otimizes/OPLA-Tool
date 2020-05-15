package br.ufpr.dinf.gres.api.resource;

import br.ufpr.dinf.gres.api.dto.OptimizationDto;
import br.ufpr.dinf.gres.api.gateway.IGateway;
import br.ufpr.dinf.gres.api.utils.Interactions;
import br.ufpr.dinf.gres.api.utils.OpenPLA;
import br.ufpr.dinf.gres.architecture.io.OptimizationInfo;
import br.ufpr.dinf.gres.architecture.io.OptimizationInfoStatus;
import br.ufpr.dinf.gres.core.jmetal4.core.OPLASolutionSet;
import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.core.SolutionSet;
import br.ufpr.dinf.gres.domain.OPLAThreadScope;
import br.ufpr.dinf.gres.domain.config.ApplicationFileConfigThreadScope;
import br.ufpr.dinf.gres.loglog.LogLog;
import br.ufpr.dinf.gres.loglog.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;

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
        thread.start();
        return Mono.just(new OptimizationInfo(thread.getId(), "", OptimizationInfoStatus.RUNNING)).subscribeOn(Schedulers.elastic());
    }


    File downloadAlternative(Long threadId, Integer id) {
        SolutionSet solutionSet = Interactions.get(threadId).solutionSet.getSolutionSet();
        Solution solution = solutionSet.get(id);
        String plaNameOnAnalyses = "Interaction_" + threadId + "_" + id + "_" + solution.getAlternativeArchitecture().getName();
        String dirOnAnalyses = ApplicationFileConfigThreadScope.getDirectoryToExportModels() + OPLAThreadScope.token.get() + System.getProperty("file.separator") + "interaction/";
        boolean delete = deleteDirectory(new File(dirOnAnalyses));
        boolean create = new File(dirOnAnalyses).mkdir();
        SolutionSet solutionSet1 = new SolutionSet();
        solutionSet1.setCapacity(1);
        solutionSet1.add(solution);
        new OPLASolutionSet(solutionSet1).saveVariablesToFile(OPLAThreadScope.token.get() + System.getProperty("file.separator") + "interaction/" + plaNameOnAnalyses);
        File file = new File(dirOnAnalyses);
        return file;
    }

    void openAlternative(Long threadId, Integer id) {
        File file = downloadAlternative(threadId, id);
        File[] files = file.listFiles();
        File fileToOpen = files[0];
        String pathSmarty = ApplicationFileConfigThreadScope.getPathSmarty();
        OpenPLA.executeJar(pathSmarty, fileToOpen.getAbsolutePath());
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

    public File downloadAllAlternative(Long threadId) {
        SolutionSet solutionSet = Interactions.get(threadId).solutionSet.getSolutionSet();
        String plaNameOnAnalyses = "Interaction_" + threadId + "_" + "_" + solutionSet.get(0).getAlternativeArchitecture().getName();
        String dirOnAnalyses = ApplicationFileConfigThreadScope.getDirectoryToExportModels() + OPLAThreadScope.token.get() + System.getProperty("file.separator") + "interaction/";
        boolean delete = deleteDirectory(new File(dirOnAnalyses));
        boolean create = new File(dirOnAnalyses).mkdir();
        new OPLASolutionSet(solutionSet).saveVariablesToFile(OPLAThreadScope.token.get() + System.getProperty("file.separator") + "interaction/" + plaNameOnAnalyses);
        File file = new File(dirOnAnalyses);
        return file;
    }

    private void configureThreadScope(OptimizationDto optimizationDto, String token) {
        OPLAThreadScope.token.set(token);
        OPLAThreadScope.mainThreadId.set(Thread.currentThread().getId());
        OPLAThreadScope.userDir.set(optimizationDto.getConfig().getDirectoryToExportModels() + OPLAThreadScope.token.get() + System.getProperty("file.separator"));
        OPLAThreadScope.pla.set(OPLAThreadScope.userDir.get() + optimizationDto.getInputArchitecture());
        optimizationDto.getConfig().setPathToProfile(optimizationDto.getConfig().getDirectoryToExportModels() + OPLAThreadScope.token.get() + System.getProperty("file.separator") + optimizationDto.getConfig().getPathToProfile());
        optimizationDto.getConfig().setPathToProfileRelationships(optimizationDto.getConfig().getDirectoryToExportModels() + OPLAThreadScope.token.get() + System.getProperty("file.separator") + optimizationDto.getConfig().getPathToProfileRelationships());
        optimizationDto.getConfig().setPathToProfilePatterns(optimizationDto.getConfig().getDirectoryToExportModels() + OPLAThreadScope.token.get() + System.getProperty("file.separator") + optimizationDto.getConfig().getPathToProfilePatterns());
        optimizationDto.getConfig().setPathToProfileConcern(optimizationDto.getConfig().getDirectoryToExportModels() + OPLAThreadScope.token.get() + System.getProperty("file.separator") + optimizationDto.getConfig().getPathToProfileConcern());
        OPLAThreadScope.setConfig(optimizationDto.getConfig());
        optimizationDto.setInputArchitecture(OPLAThreadScope.pla.get());
        optimizationDto.setInteractiveFunction(solutionSet -> interactiveEmail.run(solutionSet, optimizationDto));
    }


}
