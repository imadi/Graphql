package com.adi.graphql.components;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class Query implements GraphQLQueryResolver {

    private static List<Map<String, String>> persons = Arrays.asList(
            ImmutableMap.of("id", "1", "name", "adithya"),
            ImmutableMap.of("id", "2", "name", "vam"),
            ImmutableMap.of("id", "3", "name", "sob"),
            ImmutableMap.of("id", "4", "name", "rag"));

    public List<Map<String, String>> persons() {
        return persons;
    }




}
