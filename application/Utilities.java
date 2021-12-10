package application;

/**
* A classe Utilities possui uma variável que será compartilhada entre outras classes para gerenciar as Threads
* 
* @author Luiz Felipe Triques Moraes
* @author Renata Rona Garib
* @author Thalles Raphael Guimarães
* 
*/
public class Utilities {
    private static boolean isRunning = false;

	public static boolean isRunning() {
		return isRunning;
	}

	public static void setRunning(boolean isRunning) {
		Utilities.isRunning = isRunning;
	}
    

}