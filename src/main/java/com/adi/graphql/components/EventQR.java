package com.adi.graphql.components;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Component
public class EventQR implements GraphQLQueryResolver {

    public List<Event> events(){
        Flux<Event> eventFlux = Flux.fromStream(Stream.generate(() ->
                new Event(System.currentTimeMillis(), new Date().toString())));
        Flux<Long> duration = Flux.interval(Duration.ofSeconds(1));
        return Arrays.asList(new Event(System.currentTimeMillis(), new Date().toString()));
    }
}
