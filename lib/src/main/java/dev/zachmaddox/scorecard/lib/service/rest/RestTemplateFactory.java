package dev.zachmaddox.scorecard.lib.service.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class RestTemplateFactory implements FactoryBean<RestTemplate> {
	
	private final ObjectMapper mapper;

	@Override
	public RestTemplate getObject() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RestErrorHandler(mapper));
        return restTemplate;
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
