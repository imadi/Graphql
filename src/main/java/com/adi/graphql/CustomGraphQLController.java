package com.adi.graphql;

import com.adi.graphql.components.Event;
import com.adi.graphql.components.Query;
import com.google.common.collect.ImmutableMap;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class CustomGraphQLController {

    @Autowired
    private GraphQL graphQL;

    @PostMapping(value = "/graphql", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ExecutionResult execute(@RequestBody Map<String, Object> request) {
        System.out.println("inside");
        return graphQL.execute(ExecutionInput.newExecutionInput()
                .query((String) request.get("query"))
                .operationName((String) request.get("operationName"))
                .build());
    }


    @GetMapping(value = "/graphqltest", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @ResponseBody
    public Publisher<? extends Object> get(@RequestParam String query) {
//        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
//                .query(query)
//                .build();
        ExecutionResult result = graphQL.execute(query);
        System.out.println(result);
        System.out.println("map" + result.getData().getClass());
//        return Flux.fromStream(Stream.generate(() -> {
//            return result.getData();
//        }));
        Map hm = result.getData();
        System.out.println(hm.values());
        List<Object> objectList = Arrays.asList("{id=1}", "{id=2}", "{id=3}", "{id=4}");
        Stream<Object> streamData = objectList.stream();//Stream.of("i","love");
        Stream<Object> streamData1  = Stream.of(hm.values()) ;
        Flux<Object> streamFlux = Flux.fromStream(streamData);
        Flux<Long> duration = Flux.interval(Duration.ofSeconds(1));
        return Flux.zip(streamFlux,duration).map(Tuple2::getT1);
//        return  duration.onBackpressureDrop().map(this::getList).flatMapIterable(x->x);
//        return Flux.just(items);
//        ExecutionResult result = graphQL.execute(query);
//        System.out.println(result);
//        System.out.println("map" + result.getData());
//
//        System.out.println("inside");
//        List<GraphQLError> errors = result.getErrors();
//        if (!errors.isEmpty()) {
//            return Flux.error(new Exception(errors.get(0).toString()));
//        }
//        Map<String, Object> map = new LinkedHashMap<>();
////        Flux<Event> eventFlux = Flux.fromStream(Stream.generate(() ->
////                new Event(System.currentTimeMillis(), new Date().toString())));
////        Flux<Long> duration = Flux.interval(Duration.ofSeconds(1));
////        return Flux.zip(eventFlux, duration).map(Tuple2::getT1);
//        return events();

    }

    private List<Object> getList(long interval){
        return Arrays.asList("i", "love", "reactive", "programming");
    }

    public Flux<Event> events() {
        Flux<Event> eventFlux = Flux.fromStream(Stream.generate(() ->
                new Event(System.currentTimeMillis(), new Date().toString())));
        Flux<Long> duration = Flux.interval(Duration.ofSeconds(1));
        return Flux.zip(eventFlux, duration).map(Tuple2::getT1);//.collectList().toFuture();
    }

}
