package br.ufpr.dinf.gres.oplaapi;

import arquitetura.config.ApplicationFile;
import arquitetura.config.ApplicationYamlConfig;
import arquitetura.config.PathConfig;
import arquitetura.util.Constants;
import br.ufpr.dinf.gres.oplaapi.dto.OptimizationDto;
import br.ufpr.dinf.gres.oplaapi.dto.OptimizationInfo;
import br.ufpr.dinf.gres.oplaapi.dto.OptimizationInfoStatus;
import br.ufpr.dinf.gres.oplaapi.dto.OptimizationOptionsDTO;
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

@RestController
@RequestMapping("/api/optimization")
@EntityScan(basePackages = {
        "br.ufpr.dinf.gres.opla.entity"
})
public class OptimizationController {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(OptimizationController.class);


    private final OptimizationService optimizationService;

    public OptimizationController(OptimizationService optimizationService) {
        this.optimizationService = optimizationService;
    }

    @GetMapping(value = "/download/{id}", produces = "application/zip")
    public void zipFiles(@PathVariable String id, HttpServletResponse response) throws IOException {
        PathConfig config = ApplicationFile.getInstance().getConfig();
        String url = config.getDirectoryToExportModels().toString().concat(Constants.FILE_SEPARATOR + id);
        //setting headers
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"" + id + ".zip\"");
        ZipFiles zipFiles = new ZipFiles();
        zipFiles.zipDirectoryStream(new File(url), response.getOutputStream());
    }


    @PostMapping(value = "/upload-pla")
    public ResponseEntity<List<String>> save(
            @RequestParam("file") List<MultipartFile> files) {

        String OUT_PATH = ApplicationFile.getInstance().getConfig().getDirectoryToExportModels().toString() + "/";
        List<String> paths = new ArrayList<>();

        try {
            for (MultipartFile mf : files) {
                byte[] bytes = mf.getBytes();
                String s = OUT_PATH + mf.getOriginalFilename();
                paths.add(s);
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
                    if (OptimizationService.lastLogs.get(id) != null && !OptimizationService.lastLogs.get(id).isEmpty()) {
                        OptimizationInfo optimizationInfo = OptimizationService.lastLogs.get(id).get(0);
                        return OptimizationInfoStatus.COMPLETE.equals(optimizationInfo.status)
                                ? optimizationInfo : OptimizationService.lastLogs.get(id).remove(0);
                    }
                    return new OptimizationInfo(id, "", OptimizationInfoStatus.RUNNING);
                });
    }

    @GetMapping("/config")
    public Mono<ApplicationYamlConfig> config() {
        return Mono.just(ApplicationFile.getInstance().getApplicationYaml()).subscribeOn(Schedulers.elastic());
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
