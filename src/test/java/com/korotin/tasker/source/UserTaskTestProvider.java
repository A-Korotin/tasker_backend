package com.korotin.tasker.source;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

public class UserTaskTestProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        // name, description, startDate, done, expectedStatus
        return Stream.of(
                Arguments.of("Name", "Description", ZonedDateTime.now().truncatedTo(ChronoUnit.MILLIS), true, 201),
                Arguments.of("", "", ZonedDateTime.now(), true, 400),
                Arguments.of(null, "", ZonedDateTime.now(), true, 400),
                Arguments.of("Name", null, ZonedDateTime.now(), true, 400),
                Arguments.of("Name", "Description", null, true, 400),
                Arguments.of("Name", "Description", ZonedDateTime.now(), null, 400)

        );
    }
}
