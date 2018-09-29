package us.zacharymaddox.scorecard.api.service.rest;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Profile({"api"})
public class RestTemplateFactory implements FactoryBean<RestTemplate> {
	
	@Autowired
	private RestTemplateBuilder restTemplateBuilder;
	@Autowired
	private ObjectMapper mapper;

	@Override
	public RestTemplate getObject() throws Exception {
		return restTemplateBuilder
				.errorHandler(new RestErrorHandler(mapper))
				.build();
	}

	@Override
	public Class<?> getObjectType() {
		return RestTemplate.class;
	}
	
	@Override
	public boolean isSingleton() {
		return false;
	}
	

}
