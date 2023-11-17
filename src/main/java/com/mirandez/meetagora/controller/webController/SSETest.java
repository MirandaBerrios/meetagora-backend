package com.mirandez.meetagora.controller.webController;

import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;


@RestController
public class SSETest {


    private final FluxProcessor<String,String> processor = DirectProcessor.<String>create().serialize();
    private final FluxSink<String> sink= processor.sink();
//    @GetMapping("/sse")
//    public Flux<ServerSentEvent<String>> streamEvents() {
//        System.err.println(" RECIVIENDO PETITUD ");
//        return Flux.interval(Duration.ofSeconds(1))
//                .map(sequence -> ServerSentEvent.<String> builder()
//                        .data("SSE - " + LocalTime.now())
//                        .build());
//    }
    @GetMapping("/sse")
    public Flux<ServerSentEvent<String>> streamEvents() {
        return processor.map(data -> ServerSentEvent.<String>builder().data(data).build());
    }

    @GetMapping(value = "/sse1")
    public Flux<ServerSentEvent<String>> streamEvents1(@RequestBody(required = false) String body) {
        System.err.println(body);
        return Flux.just(ServerSentEvent.<String>builder().data(body).build());


    }

    @GetMapping(value = "/sse-data", produces = "text/event-stream")
    public Flux<ServerSentEvent<String>> sseData() {
        return processor.map(data -> ServerSentEvent.<String>builder().data(data).build());
    }

    @PostMapping("/receive-data")
    public ResponseEntity<String> receiveData(@RequestBody String data) {
        sink.next(data);
        return ResponseEntity.ok("Información recibida exitosamente");
    }


}
