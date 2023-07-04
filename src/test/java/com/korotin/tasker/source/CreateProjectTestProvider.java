package com.korotin.tasker.source;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class CreateProjectTestProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of("Valid", 201),
                Arguments.of("VeryVeryVeryLongValid", 201),
                Arguments.of("", 400),
                Arguments.of("   ", 400)
        );
    }
}
