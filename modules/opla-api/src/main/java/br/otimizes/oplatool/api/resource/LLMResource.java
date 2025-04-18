package br.otimizes.oplatool.api.resource;

import br.otimizes.oplatool.api.ChatClientEndpoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/llm")
public class LLMResource {

    @GetMapping(value = "/suggestion", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<Object> suggestion(@RequestParam("q") String q)
            throws URISyntaxException {
        final CompletableFuture<Object> responseFuture = new CompletableFuture<>();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("values.json");
        responseFuture.complete(new BufferedReader(new InputStreamReader(is))
                .lines().collect(Collectors.joining("\n")));
        return responseFuture;
    }


    @GetMapping(value = "/obj", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<Object> obj(@RequestParam("q") String q)
            throws URISyntaxException {
        final CompletableFuture<Object> responseFuture = new CompletableFuture<>();
        final ChatClientEndpoint clientEndPoint =
                new ChatClientEndpoint(new URI("ws://localhost:3000"));
        clientEndPoint.addMessageHandler(message -> {
            System.out.println("message " + message);
            if (message.contains("\"CHAT_GPT\"")) {
                clientEndPoint.userSession.close();
                responseFuture.complete(message);
            }
        });

        HashMap<Object, Object> msg = new HashMap<>();
        msg.put("content", q + " Always answer in the JSON format" +
                " {'fns': ['...'], 'suggestion': '....'}, where 'fns' contains" +
                " a list of the objective functions (uppercase acronyms) " +
                "and 'suggestion' contains the complete text you provided.");
        msg.put("from", "CLIENT");
        clientEndPoint.sendMessage(msg);

        return responseFuture;
    }
}
