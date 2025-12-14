package dev.zachmaddox.scorecard.api.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public GroupedOpenApi scoreCardApi() {
		return GroupedOpenApi.builder()
				.group("scorecard")
				.pathsToMatch("/api/**")
				.build();
	}

}
