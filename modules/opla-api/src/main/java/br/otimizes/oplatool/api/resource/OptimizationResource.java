package br.otimizes.oplatool.api.resource;

import br.otimizes.oplatool.api.dto.Infos;
import br.otimizes.oplatool.api.dto.OptimizationDto;
import br.otimizes.oplatool.api.dto.OptimizationOptionsDTO;
import br.otimizes.oplatool.api.utils.Interaction;
import br.otimizes.oplatool.api.utils.Interactions;
import br.otimizes.oplatool.architecture.io.OPLALogs;
import br.otimizes.oplatool.architecture.io.OptimizationInfo;
import br.otimizes.oplatool.architecture.io.OptimizationInfoStatus;
import br.otimizes.oplatool.domain.OPLAThreadScope;
import br.otimizes.oplatool.domain.config.ApplicationFileConfig;
import br.otimizes.oplatool.domain.config.ApplicationYamlConfig;
import br.otimizes.oplatool.domain.config.FileConstants;
import br.otimizes.oplatool.persistence.service.ZipFiles;
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
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/optimization")
public class OptimizationResource {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(OptimizationResource.class);

    private final OptimizationService optimizationService;

    public OptimizationResource(OptimizationService optimizationService) {
        this.optimizationService = optimizationService;
    }

    @GetMapping(value = "/download/{token}/{hash}", produces = "application/zip")
    public void zipFiles(@PathVariable String token, @PathVariable String hash, HttpServletResponse response) throws IOException {
        String url = ApplicationFileConfig.getInstance().getDirectoryToExportModels().concat(FileConstants.FILE_SEPARATOR + token + FileConstants.FILE_SEPARATOR + hash);
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"" + hash + ".zip\"");
        ZipFiles zipFiles = new ZipFiles();
        zipFiles.zipDirectoryStream(new File(url), response.getOutputStream());
    }

    @GetMapping(value = "/download-alternative/{token}/{hash}/{id}", produces = "application/zip")
    public void downloadAlternative(@PathVariable String token, @PathVariable String hash, @PathVariable Integer id, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"alternatives.zip\"");
        File file = optimizationService.downloadAlternative(token, hash, id);
        ZipFiles zipFiles = new ZipFiles();
        zipFiles.zipDirectoryStream(file, response.getOutputStream());
    }

    @GetMapping(value = "/open-alternative/{token}/{hash}/{id}", produces = "application/zip")
    public void openAlternative(@PathVariable String token, @PathVariable String hash, @PathVariable Integer id) throws IOException {
        optimizationService.openAlternative(token, hash, id);
    }

    @GetMapping(value = "/download-all-alternative/{token}/{hash}", produces = "application/zip")
    public void downloadAllAlternative(@PathVariable String token, @PathVariable String hash, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"alternatives.zip\"");
        File file = optimizationService.downloadAllAlternative(token, hash);
        ZipFiles zipFiles = new ZipFiles();
        zipFiles.zipDirectoryStream(file, response.getOutputStream());
    }


    @PostMapping(value = "/upload-pla")
    public ResponseEntity<List<String>> save(
            @RequestParam("file") List<MultipartFile> files) {

        String OUT_PATH = ApplicationFileConfig.getInstance().getDirectoryToExportModels() + FileConstants.FILE_SEPARATOR + OPLAThreadScope.token.get() + FileConstants.FILE_SEPARATOR;
        List<String> paths = new ArrayList<>();

        try {
            for (MultipartFile mf : files) {
                byte[] bytes = mf.getBytes();
                String s = OUT_PATH + mf.getOriginalFilename();
                paths.add(mf.getOriginalFilename());
                createPathIfNotExists(s.substring(0, s.lastIndexOf(FileConstants.FILE_SEPARATOR)));
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


    @GetMapping(value = "/optimization-infos")
    public Mono<Infos> optimizationInfos() {
        for (Map.Entry<String, List<OptimizationInfo>> stringListEntry : OPLALogs.lastLogs.entrySet()) {
            if (OptimizationInfoStatus.COMPLETE.equals(stringListEntry.getValue().get(0).status)) {
                OPLALogs.lastLogs.remove(stringListEntry.getKey());
            }
        }
        List<Map.Entry<String, List<OptimizationInfo>>> collect = OPLALogs.lastLogs.entrySet().stream()
                .filter(optimizationInfos -> optimizationInfos.getKey().startsWith(OPLAThreadScope.hash.get().split(FileConstants.FILE_SEPARATOR)[0])).collect(Collectors.toList());
        return Mono.just(new Infos(collect)).subscribeOn(Schedulers.elastic());
    }

    @PostMapping(value = "/kill-optimization-process/{token}/{hash}")
    public void killOptimizationProcess(@PathVariable String token, @PathVariable String hash) {
        Set<Thread> threads = Thread.getAllStackTraces().keySet();
        for (Thread thread : threads) {
            if ((token + FileConstants.FILE_SEPARATOR + hash).startsWith(thread.getName())) {
                System.out.println("Thread Finished: " + thread.getName() + " - " + token + FileConstants.FILE_SEPARATOR + hash);
                thread.stop();
            }
        }
        OPLALogs.remove(token, hash);
    }

    @GetMapping(value = "/optimization-info/{token}/{hash}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<OptimizationInfo> optimizationInfo(@PathVariable String token, @PathVariable String hash) {
        return Flux.interval(Duration.ofMillis(500)).take(50).onBackpressureBuffer(50)
                .map(str -> {
                    if (OPLALogs.get(token, hash) != null && !OPLALogs.get(token, hash).isEmpty()) {
                        OptimizationInfo optimizationInfo = OPLALogs.get(token, hash).get(0);
                        return OptimizationInfoStatus.COMPLETE.equals(optimizationInfo.status)
                                ? clear(token, hash, optimizationInfo) : OPLALogs.get(token, hash).remove(0);
                    }
                    return new OptimizationInfo(token + FileConstants.FILE_SEPARATOR + hash, "", Interactions.interactions.size() > 0 &&
                            !(Optional.of(Interactions.get(token, hash)).orElse(new Interaction(true)).updated)
                            ? OptimizationInfoStatus.INTERACT : OptimizationInfoStatus.RUNNING);
                });
    }

    private OptimizationInfo clear(String token, String hash, OptimizationInfo optimizationInfo) {
        OPLALogs.remove(token, hash);
        return optimizationInfo;
    }

    @GetMapping("/config")
    public Mono<ApplicationYamlConfig> config() {
        return Mono.just(ApplicationFileConfig.getInstance()).subscribeOn(Schedulers.elastic());
    }

    @GetMapping("/interaction/{token}/{hash}")
    public Mono<Interaction> getInteraction(@PathVariable String token, @PathVariable String hash) {
        return Mono.just(Interactions.get(token, hash)).subscribeOn(Schedulers.elastic());
    }

    @PostMapping("/interaction/{token}/{hash}")
    public Mono<Object> postInteraction(@PathVariable String token, @PathVariable String hash, @RequestBody Interaction interaction) {
        Interactions.update(token, hash, interaction.solutionSet.getSolutionSet());
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
        return optimizationService.execute(optimizationDto);
    }


}
