package aplication;

import java.io.FileNotFoundException;

public class Program {
	public static void main(String[] args) throws FileNotFoundException {
		InputPort ip = new InputPort("A:", 20, 1000, 0);
		Thread thread = new Thread(ip);
		thread.start();
	}
}
