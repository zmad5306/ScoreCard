package us.zacharymaddox.scorecard.example.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Service1 {
	
	private Logger logger = LoggerFactory.getLogger(Service1.class);
	
	@JmsListener(destination="service1", selector="ACTION='action1'", containerFactory="myFactory")
	@Transactional
	public void action1() {
		logger.info("service1/action1 invoked");
	}
	
	@JmsListener(destination="service1", selector="ACTION='action2'", containerFactory="myFactory")
	public void action2() {
		logger.info("service1/action2 invoked");	
	}
	
	@JmsListener(destination="service1", selector="ACTION='action3'", containerFactory="myFactory")
	public void action3() {
		logger.info("service1/action3 invoked");
	}

}
