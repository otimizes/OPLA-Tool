package br.ufpr.dinf.gres.api.base;

import br.ufpr.dinf.gres.api.dto.ResultList;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.validation.Valid;
import java.util.List;

@RestController
@Scope("prototype")
public class BaseResource<T> {
    public final BaseService<T> service;

    public BaseResource(BaseService<T> service) {
        this.service = service;
    }

    public <T> Mono<T> asyncMono(T callable) {
        return Mono.just(callable).publishOn(Schedulers.elastic());
    }

    public <T> Flux<T> asyncFlux(Iterable<T> callable) {
        return Flux.fromIterable(callable).publishOn(Schedulers.elastic());
    }

    @Transactional
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        T entity = service.getOne(id);
        service.delete(entity);
        return asyncMono(ResponseEntity.noContent().build());
    }

    @Transactional
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<ResponseEntity<T>> getOne(@PathVariable Long id) {
        T entity = service.getOne(id);
        return asyncMono(ResponseEntity.ok(entity));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<T>> save(@RequestBody @Valid T model) {
        return asyncMono(ResponseEntity.ok(service.save(model)));
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<T>> update(@RequestBody @Valid T model) {
        return asyncMono(ResponseEntity.ok(service.save(model)));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<ResultList<T>>> findAll() {
        return asyncMono(ResponseEntity.ok(new ResultList<>(service.findAll())));
    }

    @GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<ResultList<T>>> count() {
        return asyncMono(ResponseEntity.ok(new ResultList<>(service.count())));
    }

    @Transactional
    @GetMapping(value = "/by-experiment/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<ResponseEntity<ResultList<T>>> findByExperiment(@PathVariable Long id) {
        List<T> byExperiment = service.findByExperiment(id);
        return asyncMono(ResponseEntity.ok(new ResultList<>(byExperiment)));
    }


}
