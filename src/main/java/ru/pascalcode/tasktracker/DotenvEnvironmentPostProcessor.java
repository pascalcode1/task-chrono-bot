package ru.pascalcode.tasktracker;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.Map;
import java.util.stream.Collectors;

public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String PROPERTY_SOURCE_NAME = "dotenv";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Dotenv dotenv = Dotenv.configure()
                              .ignoreIfMissing()
                              .load();
        Map<String, Object> dotenvMap = dotenv.entries()
                                              .stream()
                                              .collect(Collectors.toMap(DotenvEntry::getKey, DotenvEntry::getValue));
        PropertySource<?> dotenvPropertySource = new MapPropertySource(PROPERTY_SOURCE_NAME, dotenvMap);
        environment.getPropertySources()
                   .addFirst(dotenvPropertySource);
    }
}
