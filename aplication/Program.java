package aplication;

import java.io.FileNotFoundException;

public class Program {
	public static void main(String[] args) throws FileNotFoundException {

		FileResponse fp = FileHandler.readpecificationFile("file");
		System.out.println(fp.getSwithFabric());
		System.out.println(fp.getInputList());
		System.out.println(fp.getOutputList());
	}
}
