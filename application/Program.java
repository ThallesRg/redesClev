package application;

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* A classe Program é responsável por reunir todas as classes e seus respectivos dados gerenciando-as para execução do programa
* 
* @author Luiz Felipe Triques Moraes
* @author Renata Rona Garib
* @author Thalles Raphael Guimarães
* 
*/
public class Program {
	public static void main(String[] args) {
		
		
		//Deletando todos os arquivos da pasta logs
		Arrays.stream(new File("./logs/").listFiles()).forEach(File::delete);
		Utilities.setRunning(true);

		FileResponse fileResponse =  FileHandler.readpecificationFile(args[0]);
		List<InputPort> inputList = fileResponse.getInputPorts();
		List<OutputPort> outputList = fileResponse.getOutputPorts();
	
		List<Thread> threads = new ArrayList<Thread>();
			
		Commutator commutator = new Commutator(inputList, outputList, fileResponse.getSwitchFabric());
		
		threads.add(new Thread(commutator));
			
		for (OutputPort outputPort : outputList) {
			threads.add(new Thread(outputPort));
		}
	
		for (InputPort inputPort : inputList) {
			threads.add(new Thread(inputPort));
		}
	
		for (Thread thread : threads) {
			thread.start();
		}

		String command = "";
		Scanner sc = new Scanner(System.in);
		System.out.println("Para parar o programa digite: 'parar'");
		
		while(!command.equals("parar")) {
			command = sc.nextLine();
		}
		Utilities.setRunning(false);

		sc.close();

	}



}
