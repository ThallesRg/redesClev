package aplication;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.io.BufferedWriter;

public class Commutator extends Thread {

	private List<InputPort> inputPortList;
	private List<OutputPort> outputPortList;
	private Integer switchDelay;
	private SplittableRandom random;

	private BufferedWriter logSuccess;
	private BufferedWriter logNaoTratados; 

	
	
	public Commutator(List<InputPort> inputPortList, List<OutputPort> outputPortList, Integer switchDelay) {
		this.inputPortList = inputPortList;
		this.outputPortList = outputPortList;
		this.switchDelay = switchDelay;
		this.random = new SplittableRandom();
		this.logSuccess = FileHandler.createLogFile("log_sucesso_comutador");
		this.logNaoTratados = FileHandler.createLogFile("log_nao_tratados_comutador");
	}

	private int checkInputPortList(int currentPort) {
		boolean foundAPackage = false;

		while (!foundAPackage && Utilities.isRunning()) {
			currentPort++;
			if (currentPort >= inputPortList.size()) {
				currentPort = 0;
			}
			InputPort port = inputPortList.get(currentPort);
			if (!port.getList().isEmpty()) {
				foundAPackage = true;
			}
		}

		return currentPort;
	}

	private int chooseOutputPort() {


		int randomAux = random.nextInt(1, 101);
		int probability = 0;
		for (int i = 0; i < outputPortList.size(); i++) {
			probability += outputPortList.get(i).getPackageFowardProbability();

			if (randomAux <= probability) {
				return i;
			}
		}

		return 999999999;
	}

	private void transportPackage(int inputPort, int outputPort) {

		if (Utilities.isRunning()) {
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
		
	}

	private void discardPackage(Package pack, OutputPort port) {
		String fileName = "log-descartado-fila-de-saida-" + port.getPortID() + "-cheia";
		BufferedWriter logDiscarted = FileHandler.createLogFile(fileName);
		FileHandler.writeLog(logDiscarted, pack.toString());
		FileHandler.closeLogFile(logDiscarted);
	}

	private void nonTreatedPackages() {
		for (InputPort port : inputPortList) {
			for (Package pack : port.getList()) {
				FileHandler.writeLog(logNaoTratados, pack.toString());
			}
		}
	}

	@Override
	public void run() {
		int input = -1;
		int output;
		System.out.println("Rodando Buffer...");


		while (Utilities.isRunning()) {
			input = checkInputPortList(input);
			System.out.println("Input: " + input);
			output = chooseOutputPort();
			System.out.println("Output: " + output);
			transportPackage(input, output);

			try {
				Thread.sleep(switchDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		

		nonTreatedPackages();
		// Fazer isso pra cada log

		FileHandler.closeLogFile(logSuccess);
		FileHandler.closeLogFile(logNaoTratados);
	
		System.out.println("Parou Commutator");
	}

}
