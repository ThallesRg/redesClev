package aplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Program {
	public static void main(String[] args) throws FileNotFoundException {
		

		//Deletando todos os arquivos da pasta logs
		Arrays.stream(new File("./logs/").listFiles()).forEach(File::delete);
		Utilities.setRunning(true);

		InputPort inputPort = new InputPort("A_", 5, 100, 50);
		OutputPort outputPort = new OutputPort("B-", 5, 100, 30, 50);
		
		List<InputPort> inputList = new ArrayList<>();
		List<OutputPort> outputList = new ArrayList<>();
		
		inputList.add(inputPort);
		outputList.add(outputPort);
		
		Commutator commutator = new Commutator(inputList, outputList, 50);
		Thread threadInput = new Thread(inputPort);
		Thread threadOutput = new Thread(outputPort);
		Thread threadCommutator = new Thread(commutator);
		threadInput.start();
		threadOutput.start();
		threadCommutator.start();
		
		try {
			TimeUnit.SECONDS.sleep(10L);
			System.out.println("Parando...");
			Utilities.setRunning(false);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
		/*
		Utilities.setRunning(true);
		InputPort inputPort = new InputPort("A_", 5, 100, 50);
		Thread threadInput = new Thread(inputPort);
		threadInput.start();
		*/
	}



}
