package us.zacharymaddox.scorecard.api.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import us.zacharymaddox.scorecard.domain.Transport;

public class Service implements Serializable {
	
	private static final long serialVersionUID = -3552021332001667682L;
	@JsonProperty("service_id")
	private Long serviceId;
	private String type;
	private String name;
	private Transport transport;
	private String path;
	public Long getServiceId() {
		return serviceId;
	}
	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Transport getTransport() {
		return transport;
	}
	public void setTransport(Transport transport) {
		this.transport = transport;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

}
