package com.adi.graphql.components;

import graphql.ExecutionResult;
import graphql.execution.ExecutionContext;
import graphql.execution.ExecutionStrategy;
import graphql.execution.ExecutionStrategyParameters;
import graphql.execution.NonNullableFieldWasNullException;

import java.util.concurrent.CompletableFuture;

public class CustomES extends ExecutionStrategy {

    @Override
    public CompletableFuture<ExecutionResult> execute(ExecutionContext executionContext, ExecutionStrategyParameters executionStrategyParameters) throws NonNullableFieldWasNullException {
        return null;
    }
}
