package aplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class Program {
	public static void main(String[] args) throws FileNotFoundException {
		
		//Deletando todos os arquivos da pasta logs
		Arrays.stream(new File("./logs/").listFiles()).forEach(File::delete);

		
		

	}
}
