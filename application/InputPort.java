package application;

import java.util.SplittableRandom;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.lang.Thread;
import java.io.BufferedWriter;

/**
* A classe InputPort é responsável por criar as portas de entrada, as quais criam pacotes e os inserem em suas
* fila.
* 
* @author Luiz Felipe Triques Moraes
* @author Renata Rona Garib
* @author Thalles Raphael Guimarães
* 
*/
public class InputPort implements Runnable{

	/**
 	* Identificador da porta
 	*/
	private String portID;
	/**
 	* Fila de pacotes
 	*/
	private ConcurrentLinkedQueue<Package> list = new ConcurrentLinkedQueue<Package>();
	/**
 	* Tamanha máximo da fila de pacotes
 	*/
	private Integer size;
	/**
 	* Tempo de criação de pacotes
 	*/
	private Integer packageGenerationDelay;
	/**
 	* Probabilidade de descartar pacotes
 	*/
	private Integer dropProbability;
	/**
 	* Numero total de pacotes criados
 	*/
	private Integer numberOfPackagesCreated;
	/**
 	* Variavel randomica para a probabilidade
 	*/
	private SplittableRandom random;

	/**
 	* Escritor de arquivos do log de pacotes criados com sucesso
 	*/
	private BufferedWriter logCreated;
	/**
 	* Escritor de arquivos do log de pacotes descartos
 	*/
	private BufferedWriter logDiscarted; 
	/**
 	* Escritor de arquivos do log de pacotes descartos pela fila estar cheia
 	*/
	private BufferedWriter logQueue; 

	public InputPort(String portID, Integer size, Integer packageGenerationDelay,
			Integer dropProbability) {
		this.portID = portID;
		this.size = size;
		this.packageGenerationDelay = packageGenerationDelay;
		this.dropProbability = dropProbability;
		this.numberOfPackagesCreated = 0;
		this.random = new SplittableRandom();
		this.logCreated = FileHandler.createLogFile("log-criados-com-sucesso-" + portID);
		this.logDiscarted = FileHandler.createLogFile("log-descartados-" + portID);
		this.logQueue = FileHandler.createLogFile("log-descartados-fila-longa-" + portID);
	}
	

	public String getPortID() {
		return portID;
	}


	public ConcurrentLinkedQueue<Package> getList() {
		return list;
	}
	

	public Integer getSize() {
		return size;
	}

	/**
    * Cria pacotes, caso possivel os insere na fila, caso contrario os descarta
    */
	private void createPackage() {
		numberOfPackagesCreated++;
		Package pack = new Package(portID + "" + numberOfPackagesCreated); 
		boolean isPackageDiscarded = random.nextInt(1, 101) <= dropProbability;
		if (isPackageDiscarded) {
			FileHandler.writeLog(logDiscarted, pack.toString());
			return;
		} else if (list.size() >= size) {
			FileHandler.writeLog(logQueue, pack.toString());
			return;
		}
		insertPackage(pack);
		FileHandler.writeLog(logCreated, pack.toString());
	}

	/**
    * Insere pacotes na fila
    * @param pack pacote
    */
	private void insertPackage(Package pack) {
		list.add(pack);
	}

	/**
    * Remove pacotes da fila
    */
	public Package removePackage() {
		return list.poll();
	}
	
	/**
    * Roda quando a thread é criada
    */
	@Override
	public void run() {

		while(Utilities.isRunning()) {
			try {
				createPackage();
				Thread.sleep(packageGenerationDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		FileHandler.closeLogFile(logCreated);
		FileHandler.closeLogFile(logDiscarted);
		FileHandler.closeLogFile(logQueue);
	}

}
