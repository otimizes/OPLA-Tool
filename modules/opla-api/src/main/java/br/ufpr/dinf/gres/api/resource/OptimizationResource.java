package br.ufpr.dinf.gres.api.resource;

import br.ufpr.dinf.gres.api.dto.OptimizationDto;
import br.ufpr.dinf.gres.api.dto.OptimizationOptionsDTO;
import br.ufpr.dinf.gres.api.utils.Interaction;
import br.ufpr.dinf.gres.api.utils.Interactions;
import br.ufpr.dinf.gres.architecture.config.ApplicationFile;
import br.ufpr.dinf.gres.architecture.config.ApplicationYamlConfig;
import br.ufpr.dinf.gres.architecture.config.PathConfig;
import br.ufpr.dinf.gres.architecture.io.OPLALogs;
import br.ufpr.dinf.gres.architecture.io.OptimizationInfo;
import br.ufpr.dinf.gres.architecture.io.OptimizationInfoStatus;
import br.ufpr.dinf.gres.architecture.util.Constants;
import br.ufpr.dinf.gres.core.jmetal4.core.SolutionSet;
import br.ufpr.dinf.gres.domain.OPLAThreadScope;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/optimization")
@EntityScan(basePackages = {
        "br.ufpr.dinf.gres.opla.entity"
})
public class OptimizationResource {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(OptimizationResource.class);


    private final OptimizationService optimizationService;

    public OptimizationResource(OptimizationService optimizationService) {
        this.optimizationService = optimizationService;
    }

    @GetMapping(value = "/download/{token}/{hash}", produces = "application/zip")
    public void zipFiles(@PathVariable String token, @PathVariable String hash, HttpServletResponse response) throws IOException {
        PathConfig config = ApplicationFile.getInstance().getConfig();
        String url = config.getDirectoryToExportModels().toString().concat(Constants.FILE_SEPARATOR + token + Constants.FILE_SEPARATOR + hash);
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"" + hash + ".zip\"");
        ZipFiles zipFiles = new ZipFiles();
        zipFiles.zipDirectoryStream(new File(url), response.getOutputStream());
    }

    @GetMapping(value = "/download-alternative/{threadId}/{id}", produces = "application/zip")
    public void downloadAlternative(@PathVariable Long threadId, @PathVariable Integer id, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"alternatives.zip\"");
        File file = optimizationService.downloadAlternative(threadId, id);
        ZipFiles zipFiles = new ZipFiles();
        zipFiles.zipDirectoryStream(file, response.getOutputStream());
    }

    @GetMapping(value = "/download-all-alternative/{threadId}", produces = "application/zip")
    public void downloadAllAlternative(@PathVariable Long threadId, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"alternatives.zip\"");
        File file = optimizationService.downloadAllAlternative(threadId);
        ZipFiles zipFiles = new ZipFiles();
        zipFiles.zipDirectoryStream(file, response.getOutputStream());
    }


    @PostMapping(value = "/upload-pla")
    public ResponseEntity<List<String>> save(
            @RequestParam("file") List<MultipartFile> files) {

        String OUT_PATH = ApplicationFile.getInstance().getConfig().getDirectoryToExportModels().toString() + System.getProperty("file.separator") + OPLAThreadScope.token.get() + System.getProperty("file.separator");
        List<String> paths = new ArrayList<>();

        try {
            for (MultipartFile mf : files) {
                byte[] bytes = mf.getBytes();
                String s = OUT_PATH + mf.getOriginalFilename();
                paths.add(mf.getOriginalFilename());
                createPathIfNotExists(s.substring(0, s.lastIndexOf(Constants.FILE_SEPARATOR)));
                Path path = Paths.get(s);
                Files.write(path, bytes);
            }

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(paths);
    }

    private void createPathIfNotExists(String s) {
        boolean mkdirs = new File(s).mkdirs();
    }


    @GetMapping(value = "/optimization-info/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<OptimizationInfo> optimizationInfo(@PathVariable Long id) {
        return Flux.interval(Duration.ofMillis(500)).take(50).onBackpressureBuffer(50)
                .map(str -> {
                    if (OPLALogs.lastLogs.get(id) != null && !OPLALogs.lastLogs.get(id).isEmpty()) {
                        OptimizationInfo optimizationInfo = OPLALogs.lastLogs.get(id).get(0);
                        return OptimizationInfoStatus.COMPLETE.equals(optimizationInfo.status)
                                ? optimizationInfo : OPLALogs.lastLogs.get(id).remove(0);
                    }
                    return new OptimizationInfo(id, "", !(Optional.ofNullable(Interactions.interactions.get(id)).orElse(new Interaction(true)).updated) ? OptimizationInfoStatus.INTERACT : OptimizationInfoStatus.RUNNING);
                });
    }

    @GetMapping("/config")
    public Mono<ApplicationYamlConfig> config() {
        return Mono.just(ApplicationFile.getInstance().getApplicationYaml()).subscribeOn(Schedulers.elastic());
    }

    @GetMapping("/interaction/{id}")
    public Mono<Interaction> getInteraction(@PathVariable Long id) {
        return Mono.just(Interactions.interactions.get(id)).subscribeOn(Schedulers.elastic());
    }

    @PutMapping("/interaction/{id}")
    public Mono<Object> interaction(@PathVariable Long id, @RequestBody SolutionSet solutionSet) {
        Interactions.interactions.put(id, new Interaction(true, solutionSet));
        return Mono.empty().subscribeOn(Schedulers.elastic());
    }

    @GetMapping("/optimization-options")
    public OptimizationOptionsDTO optimizationAlgorithms() {
        return new OptimizationOptionsDTO();
    }

    @GetMapping("/dto")
    public Mono<OptimizationDto> dto() {
        return Mono.just(new OptimizationDto()).subscribeOn(Schedulers.elastic());
    }

    @PostMapping("/optimize")
    public Mono<OptimizationInfo> optimize(@RequestBody OptimizationDto optimizationDto) {
        switch (optimizationDto.getAlgorithm()) {
            case NSGAII:
                return executeNSGAII(optimizationDto);
            case PAES:
                return executePAES(optimizationDto);
        }
        return executeNSGAII(optimizationDto);
    }

    @PostMapping("/nsgaii")
    public Mono<OptimizationInfo> executeNSGAII(@RequestBody OptimizationDto optimizationDto) {
        return optimizationService.executeNSGAII(optimizationDto);
    }

    @PostMapping("/paes")
    public Mono<OptimizationInfo> executePAES(@RequestBody OptimizationDto optimizationDto) {
        return optimizationService.executePAES(optimizationDto);
    }


}
