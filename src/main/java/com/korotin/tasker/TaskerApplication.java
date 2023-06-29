package com.korotin.tasker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main utility class. Starts the Tasker application
 */
@SpringBootApplication
public class TaskerApplication {

    public static void main(final String[] args) {
        SpringApplication.run(TaskerApplication.class, args);
    }

}
