package tds.config.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import tds.common.configuration.RestTemplateConfiguration;
import tds.common.web.advice.ExceptionAdvice;

/**
 * This is the base configuration class for the config microservice
 */
@Configuration
@Import({ExceptionAdvice.class, RestTemplateConfiguration.class})
public class ConfigServiceApplicationConfiguration {
}
