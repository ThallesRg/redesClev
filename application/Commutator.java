package application;

import java.util.List;
import java.util.SplittableRandom;
import java.io.BufferedWriter;

/**
* A classe Comutator é responsável por criar a entidade Comutador de pacotes, a qual retira pacotes das filas de entrada das portas
* e os coloca nas filas das portas de saida
* 
* @author Luiz Felipe Triques Moraes
* @author Renata Rona Garib
* @author Thalles Raphael Guimarães
* 
*/
public class Commutator implements Runnable{

	/**
 	* Lista de todas as portas de entrada
 	*/
	private List<InputPort> inputPortList;
	/**
 	* Lista de todas as portas de saida
 	*/
	private List<OutputPort> outputPortList;
	/**
 	* Tempo de demora para a comutação de um pacote
 	*/
	private Integer switchDelay;
	/**
 	* Variavel randomica para probabilidade
 	*/
	private SplittableRandom random;

	/**
 	* Escritor de arquivos do log de pacotes comutados com sucesso
 	*/
	private BufferedWriter logSuccess;
	/**
 	* Escritor de arquivos do log de pacotes não tratados
 	*/
	private BufferedWriter logNaoTratados; 
	

	public Commutator(List<InputPort> inputPortList, List<OutputPort> outputPortList, Integer switchDelay) {
		this.inputPortList = inputPortList;
		this.outputPortList = outputPortList;
		this.switchDelay = switchDelay;
		this.random = new SplittableRandom();
		this.logSuccess = FileHandler.createLogFile("log_sucesso_comutador");
		this.logNaoTratados = FileHandler.createLogFile("log_nao_tratados_comutador");
	}

	/**
    * Checa todas as filas das portas de entrada até achar alguma com um pacote
    * @param currentPort posição da porta atual
	* @return posição da porta que contem o pacote
    */
	private int checkInputPortList(int currentPort) {
		boolean foundAPackage = false;

		while (!foundAPackage && Utilities.isRunning()) {
			currentPort++;
			if (currentPort >= inputPortList.size()) {
				currentPort = 0;
			}
			InputPort port = inputPortList.get(currentPort);
			if (!port.getQueue().isEmpty()) {
				foundAPackage = true;
			}
		}

		return currentPort;
	}

	/**
    * Escolhe uma porta de saída levando em consideração a sua probabilidade
	* @return posição da porta de saída escolhida
    */
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

	/**
    * Tira o pacote da fila da porta de entrada e o coloca na fila da porta de saída
    * @param inputPort posição da porta de entrada
    * @param outputPort posição da porta de saída
    */
	private void transportPackage(int inputPort, int outputPort) {

		if (Utilities.isRunning()) {
			InputPort input = inputPortList.get(inputPort);
			OutputPort output = outputPortList.get(outputPort);
			
			Package pack = input.removePackage();
			pack.updateTime();
			if (output.insertPackageInQueue(pack)) {
				FileHandler.writeLog(logSuccess, pack.toString());
			} else {
				discardPackage(pack, output);
			}
		}
		
	}

	/**
    * Discarta o pacote quando a fila da porta de saída escolhida está cheia
    * @param pack pacote a ser descartado
    * @param port porta de saída
    */	
	private void discardPackage(Package pack, OutputPort port) {
		String fileName = "log-descartado-fila-de-saida-" + port.getPortID() + "-cheia";
		BufferedWriter logDiscarted = FileHandler.createLogFile(fileName);
		FileHandler.writeLog(logDiscarted, pack.toString());
		FileHandler.closeLogFile(logDiscarted);
	}

	/**
    * Cria o log dos pacotes nas filas das portas de entradas não tratados
    */
	private void nonTreatedPackages() {
		for (InputPort port : inputPortList) {
			for (Package pack : port.getQueue()) {
				FileHandler.writeLog(logNaoTratados, pack.toString());
			}
		}
	}

	/**
    * Roda quando a thread é criada
    */
	@Override
	public void run() {
		int input = -1;
		int output;

		while (Utilities.isRunning()) {
			input = checkInputPortList(input);
			output = chooseOutputPort();
			transportPackage(input, output);

			try {
				Thread.sleep(switchDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		nonTreatedPackages();

		FileHandler.closeLogFile(logSuccess);
		FileHandler.closeLogFile(logNaoTratados);
	}

}
