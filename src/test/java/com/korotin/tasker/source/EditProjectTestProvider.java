package com.korotin.tasker.source;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class EditProjectTestProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of("Valid", "other valid", 200),
                Arguments.of("Valid", "srt", 200),
                Arguments.of("Valid", "", 400),
                Arguments.of("Valid", "    ", 400)
        );
    }
}
