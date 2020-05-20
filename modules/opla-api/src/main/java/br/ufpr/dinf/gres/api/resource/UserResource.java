package br.ufpr.dinf.gres.api.resource;

import br.ufpr.dinf.gres.api.base.BaseResource;
import br.ufpr.dinf.gres.domain.entity.LoginDto;
import br.ufpr.dinf.gres.domain.entity.LoginResultDto;
import br.ufpr.dinf.gres.domain.entity.User;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.service.UserService;
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