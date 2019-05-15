package com.adi.graphql.controllers;


import graphql.GraphQLError;
import graphql.spring.web.reactive.GraphQLInvocation;
import graphql.spring.web.reactive.GraphQLInvocationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
public class CustomGraphQLController {

    @Autowired
    private GraphQLInvocation graphQLInvocation;

    @GetMapping(value = "/customGraphQL", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Object get(@RequestParam String query, ServerWebExchange exchange) {
        GraphQLInvocationData invocationData = new GraphQLInvocationData(query, null, null);
        return graphQLInvocation.invoke(invocationData, exchange).flatMapMany(executionResult -> {
            List<GraphQLError> errors = executionResult.getErrors();
            if (!errors.isEmpty()) {
                return Flux.error(new ResponseStatusException(INTERNAL_SERVER_ERROR, errors.get(0).toString()));
            }
            return Flux.from(executionResult.getData());
        });
    }

}
