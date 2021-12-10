package application;

import java.util.SplittableRandom;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.BufferedWriter;

/**
* A classe OutputPort é responsável por criar as portas de saída, as quais recebem pacotes do comutador em suas
* filas e os enviam para a "rede".
* @author Luiz Felipe Triques Moraes
* @author Renata Rona Garib
* @author Thalles Raphael Guimarães
* 
*/
public class OutputPort implements Runnable{

	/**
 	* Identificador da porta
 	*/
	private String portID;
	/**
 	* Fila de pacotes
 	*/
	private ConcurrentLinkedQueue<Package> queue = new ConcurrentLinkedQueue<Package>();
	/**
 	* Tamanha máximo da fila de pacotes
 	*/
	private Integer size;
	/**
 	* Probabilidade de receber os pacotes do comutador
 	*/
	private Integer packageFowardProbability;
	/**
 	* Tempo de transmissão de pacotes
 	*/
	private Integer packageTransmittionDelay;
	/**
 	* Probabilidade de retransmissão de pacotes
 	*/
	private Integer retransmissionProbability;
	/**
 	* Variavel randomica para a probabilidade
 	*/
	private SplittableRandom random;

	/**
 	* Escritor de arquivos do log de pacotes transmitidos com sucesso
 	*/
	private BufferedWriter logSuccess;
	/**
 	* Escritor de arquivos do log de pacotes retransmitidos
 	*/
	private BufferedWriter logRetransmitted;
	/**
 	* Escritor de arquivos do log de pacotes na fila que nao foram tratados
 	*/
	private BufferedWriter logNonTreated;
	
	public OutputPort(String portID, Integer size, Integer packageFowardProbability, Integer packageTransmittionDelay,
			Integer retransmissionProbability) {
		this.portID = portID;
		this.size = size;
		this.packageFowardProbability = packageFowardProbability;
		this.packageTransmittionDelay = packageTransmittionDelay;
		this.retransmissionProbability = retransmissionProbability;
		this.logSuccess = FileHandler.createLogFile("log-enviados-com-sucesso-" + portID);
		this.logRetransmitted = FileHandler.createLogFile("log-retransmitidos-" + portID);
		this.logNonTreated = FileHandler.createLogFile("log-nao-tratados-fila-saida-" + portID);
		this.random = new SplittableRandom();;
	}
	
	public String getPortID() {
		return portID;
	}


	public Integer getSize() {
		return size;
	}

	public Integer getPackageFowardProbability() {
		return packageFowardProbability;
	}

	public ConcurrentLinkedQueue<Package> getQueue() {
		return queue;
	}

	/**
    * Insere pacotes na fila
    * @param pack pacote
    */
	public boolean insertPackageInQueue(Package pack) {
		if (size > queue.size()) {
			queue.add(pack);
			return true;
		}
		return false;
	}

	/**
    * Transmite pacote da fila
    */
	private void transmitPackage() {
		if (queue.size() > 0) {
			Package pack = queue.poll();
			boolean isPackageTransmited = random.nextInt(1, 101) <= (100 - retransmissionProbability);
			
			if(isPackageTransmited) {
				pack.updateTime();
				FileHandler.writeLog(logSuccess, pack.toString());
			} else {
				retransmitPackage(pack);
			}
		}
	}

	/**
    * Retransmite pacote
    * @param pack pacote
    */
	private void retransmitPackage(Package pack) {
		int retransmission = 0;
		boolean isPackageTransmited = false;

		while(!isPackageTransmited) {
			retransmission ++;
			pack.updateTime();
			isPackageTransmited = random.nextInt(1, 101) <= (100 - (retransmissionProbability/Math.pow(2, retransmission)));
		}

		FileHandler.writeLog(logRetransmitted, pack.toString());
	}

	/**
    * Cria o log dos pacotes nas filas das portas de entradas não tratados
    */
	private void nonTreatedPackages() {
		for(Package pack : queue) {
			FileHandler.writeLog(logNonTreated, pack.toString());
		}
	}

	/**
    * Roda quando a thread é criada
    */
	@Override
	public void run() {

		while(Utilities.isRunning()) {
			transmitPackage();
			try {
				Thread.sleep(packageTransmittionDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		nonTreatedPackages();

		FileHandler.closeLogFile(logSuccess);
		FileHandler.closeLogFile(logNonTreated);
		FileHandler.closeLogFile(logRetransmitted);
	}

}
