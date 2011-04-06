package org.tohu.listener;

import java.io.Serializable;

public class ListenerManager implements Serializable{
	
	public static final long serialVersionUID = 11111L;
	
	public boolean isListenersRegistered() {
		return listenersRegistered;
	}

	public void setListenersRegistered(boolean listenersRegistered) {
		this.listenersRegistered = listenersRegistered;
	}

	private boolean listenersRegistered = false;
	
	public ListenerManager() {
		
	}
	
	
}