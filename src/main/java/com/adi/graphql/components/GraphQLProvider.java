package com.adi.graphql.components;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.coxautodev.graphql.tools.SchemaParserOptions;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.execution.SubscriptionExecutionStrategy;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@Component
public class GraphQLProvider {

    @Autowired
    private GraphQLDataFetchers graphQLDataFetchers;

    private GraphQL graphQL;

    @Autowired
    private Subscription subscription;

    @Autowired
    private Query query;

    @Autowired
    private EventQR eventQR;

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    @PostConstruct
    public void init() throws IOException {
        URL url = Resources.getResource("schema.graphqls");
        String sdl = Resources.toString(url, Charsets.UTF_8);
        GraphQLSchema graphQLSchema = buildSchema();//buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema)
                .subscriptionExecutionStrategy(new SubscriptionExecutionStrategy()).build();
    }

    private GraphQLSchema buildSchema() {
        List<GraphQLResolver<? extends Object>> resolvers = ImmutableList.of(query, subscription, eventQR);
        SchemaParserOptions options = SchemaParserOptions.newOptions()
                .genericWrappers(new SchemaParserOptions.GenericWrapper(Flux.class, 0))
                .build();
        return com.coxautodev.graphql.tools.SchemaParser.newParser()
                .file("schema.graphqls")
                .resolvers(resolvers)
                .options(options)
                .build()
                .makeExecutableSchema();
    }

    private GraphQLSchema buildSchema(String sdl) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher())
                        .dataFetcher("allBooks", graphQLDataFetchers.getAllBooksDataFetcher()))
                .type(newTypeWiring("Book")
                        .dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher())
                        .dataFetcher("pageCount", graphQLDataFetchers.getPageCountDataFetcher()))
                .build();
    }


}
