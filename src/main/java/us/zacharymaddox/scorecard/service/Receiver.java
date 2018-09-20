package us.zacharymaddox.scorecard.service;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import us.zacharymaddox.scorecard.domain.Email;

@Component
public class Receiver {

    @JmsListener(destination = "mailbox", containerFactory = "myFactory")
    public void receiveMessage(Email email) {
        System.out.println("Received <" + email + ">");
    }

}
