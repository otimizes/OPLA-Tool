package br.ufpr.dinf.gres.oplaapi;

import br.ufpr.dinf.gres.opla.entity.Execution;
import br.ufpr.dinf.gres.opla.entity.Experiment;
import br.ufpr.dinf.gres.persistence.dao.ExecutionDAO;
import br.ufpr.dinf.gres.persistence.dao.ExperimentDAO;
import br.ufpr.dinf.gres.persistence.dao.MapObjectivesDAO;
import br.ufpr.dinf.gres.persistence.dao.ObjectiveDAO;
import br.ufpr.dinf.gres.persistence.repository.ExperimentRepository;
import br.ufpr.dinf.gres.persistence.service.ExperimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/persistence")
public class PersistenceController {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(PersistenceController.class);


    private final ExperimentDAO experimentDAO;
    private final ExecutionDAO executionDAO;
    private final ObjectiveDAO objectiveDAO;
    private final MapObjectivesDAO mapObjectivesDAO;

    @Autowired
    private ExperimentRepository experimentRepository;
    @Autowired
    private ExperimentService experimentService;

    public PersistenceController() {
        this.experimentDAO = new ExperimentDAO();
        this.executionDAO = new ExecutionDAO();
        this.objectiveDAO = new ObjectiveDAO();
        this.mapObjectivesDAO = new MapObjectivesDAO();
    }

    @GetMapping("/experiments")
    public Mono<List<Experiment>> getExperiments() {
        return Mono.just(experimentDAO.findAllOrdened()).subscribeOn(Schedulers.elastic());
    }

    @GetMapping("/executions-by-experiment/{id}")
    public Mono<List<Execution>> getExecutionsByExperiment(@PathVariable Long id) {
        return Mono.just(executionDAO.findByExperimentId(id)).subscribeOn(Schedulers.elastic());
    }

    @GetMapping("/names-by-experiment/{id}")
    public Mono<List<String>> getNamesByExperiment(@PathVariable Long id) {
        Experiment byId = experimentDAO.findById(id);
        return Mono.just(mapObjectivesDAO.findNamesByExperiment(byId)).subscribeOn(Schedulers.elastic());
    }

    @GetMapping("/non-dominated-Solution-number-by-experiment/{id}")
    public Mono<Long> countNonDominatedSolution(@PathVariable Long id) {
        Experiment byId = experimentDAO.findById(id);
        return Mono.just(objectiveDAO.countNonDominatedSolution(byId)).subscribeOn(Schedulers.elastic());
    }

    @GetMapping("/name-solution/{experimentId}/{executionId")
    public Mono<List<String>> getNameSolutionsByExpExec(@PathVariable Long experimentId, @PathVariable Long executionId) {
        return Mono.just(objectiveDAO.findNameSolution(experimentId, executionId)).subscribeOn(Schedulers.elastic());
    }

    @GetMapping("/objective-values/{experimentId}/{executionId}/{solutionName}")
    public Mono<List<BigDecimal>> getNameSolutionsByExpExec(@PathVariable Long experimentId, @PathVariable Long executionId, @PathVariable String solutionName) {
        Experiment experiment = experimentDAO.findById(experimentId);
        Execution execution = executionDAO.findById(experimentId);
        return Mono.just(objectiveDAO.findObjectiveValues(experiment, execution, solutionName)).subscribeOn(Schedulers.elastic());
    }


}
