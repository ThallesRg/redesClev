package aplication;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

public class Commutation implements Runnable{

	private List<InputPort> inputPortList = new ArrayList<>();
	private List<OutputPort> outputPortList =  new ArrayList<>();
	private Integer switchDelay;
	private SplittableRandom random;

	private boolean canTransportPackage;

	private int checkInputPortList(int currentPort) {
		boolean foundAPackage = false;

		while(!foundAPackage) {
			currentPort ++;
			InputPort port = inputPortList.get(currentPort);
			if (port.getList().size() > 0) {
				foundAPackage = true;
			}
		}

		return currentPort;
	}

	private int chooseOutputPort() {
		
		int x = random.nextInt(1, 101);
		int probability = 0;

		for(int i = 0; i <outputPortList.size(); i++){
			probability += outputPortList.get(i).getPackageFowardProbability();

			if (x <= probability) {
				return i;
			}
		}

		return 999999999;
	}
	
	private void transportPackage(int inputPort, int outputPort) {
		InputPort input = inputPortList.get(inputPort);
		OutputPort output = outputPortList.get(outputPort);

		Package pack = input.removePackage();
		if (output.getSize() > output.getList().size()) {
			output.insertPackageInList(pack);
			createLog(pack);
		} else {
			discardPackage(pack);
		}
	}

	private void discardPackage(Package pack) {
		createLog(pack);
	}

	private void createLog(Package pack) {
		
	}
	
	@Override
	public void run() {
		while(true) {
			int input = 
		}
	}

}
