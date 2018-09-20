package us.zacharymaddox.scorecard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import us.zacharymaddox.scorecard.domain.Email;
import us.zacharymaddox.scorecard.repository.EmailRepository;

@Component
public class EmailReceiver {

	@Autowired
	private EmailRepository emailRepository;
	
    @JmsListener(destination = "mailbox", containerFactory = "myFactory")
    public void receiveMessage(Email email) {
        System.out.println("Received <" + email + ">");
        emailRepository.save(email);
        System.out.println(emailRepository.count());
    }

}
