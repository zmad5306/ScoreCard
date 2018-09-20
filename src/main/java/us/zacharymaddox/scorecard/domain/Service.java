package us.zacharymaddox.scorecard.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

public class Service extends BaseDomain implements Serializable {
	
	private static final long serialVersionUID = 4081098177527594840L;
	
	@Id
	private String serviceId;
	private String name;
	private Transport transport;
	private String path;
	
	public Service() {
		super("service");
	}
	
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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
