package org.tohu.listener;

import org.drools.event.rule.ActivationCancelledEvent;
import org.drools.event.rule.ActivationCreatedEvent;
import org.drools.event.rule.AfterActivationFiredEvent;
import org.drools.event.rule.AgendaEventListener;
import org.drools.event.rule.AgendaGroupPoppedEvent;
import org.drools.event.rule.AgendaGroupPushedEvent;
import org.drools.event.rule.BeforeActivationFiredEvent;
import org.drools.runtime.rule.AgendaGroup;
import org.drools.runtime.rule.Activation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugAgendaEventListener implements AgendaEventListener {
	
	private final static Logger logger = LoggerFactory.getLogger(DebugAgendaEventListener.class);
	
    public void activationCreated(ActivationCreatedEvent activationcreatedevent){
    	Activation activation = activationcreatedevent.getActivation();     	   
        String debugStr = "==>[ActivationCreated(" + activation.getPropagationContext().getPropagationNumber() + 
        				  "): rule=" + activation.getRule().getName() + ";]";
    	logger.debug(debugStr);
    }

    public void activationCancelled(ActivationCancelledEvent activationcancelledevent){
    	Activation activation = activationcancelledevent.getActivation();     	   
        String debugStr = "==>[activationCancelled(" + activation.getPropagationContext().getPropagationNumber() + 
        				  "): rule=" + activation.getRule().getName() + ";]";
    	logger.debug(debugStr);    	
    }

    public void beforeActivationFired(BeforeActivationFiredEvent beforeactivationfiredevent){
    	Activation activation = beforeactivationfiredevent.getActivation();     	   
        String debugStr = "==>[beforeActivationFired(" + activation.getPropagationContext().getPropagationNumber() + 
        				  "): rule=" + activation.getRule().getName() + ";]";
    	logger.debug(debugStr);
    }

    public void afterActivationFired(AfterActivationFiredEvent afteractivationfiredevent){
    	Activation activation = afteractivationfiredevent.getActivation();     	   
        String debugStr = "==>[afterActivationFired(" + activation.getPropagationContext().getPropagationNumber() + 
        				  "): rule=" + activation.getRule().getName() + ";]";
    	logger.debug(debugStr);
    }

    public void agendaGroupPopped(AgendaGroupPoppedEvent agendagrouppoppedevent){
    	AgendaGroup agendaGroup = agendagrouppoppedevent.getAgendaGroup();     	   
        String debugStr = "<==[AgendaGroupPopped(" + agendaGroup.getName() + "]";
    	logger.debug(debugStr);
    }
    
    public void agendaGroupPushed(AgendaGroupPushedEvent agendagrouppushedevent){
    	AgendaGroup agendaGroup = agendagrouppushedevent.getAgendaGroup();
    	String debugStr = "<==[agendaGroupPushed(" + agendaGroup.getName() + "]";
    	logger.debug(debugStr);
    }
}