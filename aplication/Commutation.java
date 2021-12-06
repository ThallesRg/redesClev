package aplication;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.io.BufferedWriter;

public class Commutation implements Runnable{

	private List<InputPort> inputPortList = new ArrayList<>();
	private List<OutputPort> outputPortList =  new ArrayList<>();
	private Integer switchDelay;
	private SplittableRandom random;

	private BufferedWriter logSuccess = FileHandler.createLogFile("log_sucesso_comutador");
	private BufferedWriter logNaoTratados = FileHandler.createLogFile("log_nao_tratados_comutador");

	private int checkInputPortList(int currentPort) {
		boolean foundAPackage = false;

		while(!foundAPackage) {
			currentPort ++;
			if (currentPort <= inputPortList.size()) {
				currentPort = 0;
			}
			InputPort port = inputPortList.get(currentPort);
			if (port.getList().size() > 0) {
				foundAPackage = true;
			}
		}

		return currentPort;
	}

	private int chooseOutputPort() {
		
		int randomAux = random.nextInt(1, 101);
		int probability = 0;

		for(int i = 0; i < outputPortList.size(); i++){
			probability += outputPortList.get(i).getPackageFowardProbability();

			if (randomAux <= probability) {
				return i;
			}
		}

		return 999999999;
	}
	
	private void transportPackage(int inputPort, int outputPort) {
		InputPort input = inputPortList.get(inputPort);
		OutputPort output = outputPortList.get(outputPort);

		Package pack = input.removePackage();
		pack.updateTime();
		if (output.insertPackageInList(pack)) {
			FileHandler.writeLog(logSuccess, pack.toString());
		} else {
			discardPackage(pack, output);
		}
	}

	private void discardPackage(Package pack, OutputPort port) {
		String fileName = "log-descartado-fila-de-saida-" + port.getPortID() + "-cheia";
		BufferedWriter logDiscarted = FileHandler.createLogFile(fileName);
		FileHandler.writeLog(logDiscarted, pack.toString());
	}

	private void nonTreatedPackages() {
		for(InputPort port : inputPortList) {
			for(Package pack : port.getList()) {
				FileHandler.writeLog(logNaoTratados, pack.toString());
			}
		}
	}
	
	@Override
	public void run() {
		int input = -1;
		int output;

		while(true) {
			input = checkInputPortList(input);
			output = chooseOutputPort();
			transportPackage(input, output);

			try {
				Thread.sleep(switchDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		//nonTreatedPackages();
		// Fazer isso pra cada log
		//FileHandler.closeLogFile( logNaoTratados );
	}

}
