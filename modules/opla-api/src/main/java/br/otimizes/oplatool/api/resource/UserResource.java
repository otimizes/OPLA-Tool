package br.otimizes.oplatool.api.resource;

import br.otimizes.oplatool.api.base.BaseResource;
import br.otimizes.oplatool.domain.entity.LoginDto;
import br.otimizes.oplatool.domain.entity.LoginResultDto;
import br.otimizes.oplatool.domain.entity.User;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
public class UserResource extends BaseResource<User> {

    public UserResource(BaseService<User> service) {
        super(service);
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<LoginResultDto>> sigin(@RequestBody LoginDto loginDto) {
        return asyncMono(ResponseEntity.ok(((UserService) service).sigin(loginDto)));
    }
    @PostMapping(value = "/forgot", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<LoginResultDto>> forgot(@RequestBody LoginDto loginDto) {
        return asyncMono(ResponseEntity.ok(((UserService) service).forgot(loginDto)));
    }
}