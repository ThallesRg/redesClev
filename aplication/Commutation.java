package aplication;

import java.util.ArrayList;
import java.util.List;

public class Commutation implements Runnable{

	private List<InputPort> inputPortList = new ArrayList<>();
	private List<OutputPort> outputPortList =  new ArrayList<>();
	private Integer switchDelay;
	
	private boolean canTransportPackage;

	private void checkInputPortList() {

	}

	public void chooseOutputPort() {
		
	}
	
	synchronized public void transportPackage() {
		/*
		canTransportPortPackage = false;

		// ...

		try {
			wait(switchDelay);
		} catch(InterruptedException ex) {
			// ...
		}
		canTransportPortPackage = true;
		*/
	}

	private void discardPackage() {

	}

	private void createLog() {
		
	}
	
	@Override
	public void run() {
		while(canTransportPackage) {
			checkInputPortList();
		}
	}

}
