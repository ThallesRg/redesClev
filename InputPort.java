package aplication;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.lang.Thread;
import java.io.BufferedWriter;

public class InputPort implements Runnable{

	private String portID;
	//private List<Package> list = new ArrayList<>();
	private ConcurrentLinkedQueue<Package> list = new ConcurrentLinkedQueue<Package>();
	private Integer size;
	private Integer packageGenerationDelay;
	private Integer dropProbability;
	private Integer numberOfPackagesCreated;
	private SplittableRandom random;

	
	private BufferedWriter logCreated;
	private BufferedWriter logDiscarted; 
	private BufferedWriter logQueue; 

	
	/*
	Um pacote virtual gerado pela porta de entrada será inserido na fila de entrada associada àquela porta. Porém, caso a fila de entrada esteja cheia, o
	pacote gerado deve ser descartado. Assim, cada porta de entrada deve gerar e manter três diferentes
	registros (logs): i) registro dos pacotes criados com sucesso, isto é, pacote foi gerado e não
	descartado na simulação da verificação de erro; ii) registro dos pacotes descartados por meio da
	simulação de um erro de transmissão; iii) registro dos pacotes descartados em razão da fila de
	entrada estar cheia.

	*/

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

	private void insertPackage(Package pack) {
		list.add(pack);
	}
	
	public Package removePackage() {
		return list.poll();
	}
	
	
	
	@Override
	public String toString() {
		return "InputPort [portID=" + portID + ", list=" + list + ", size=" + size + "]";
	}


	@Override
	public void run() {

		while(Utilities.isRunning()) {
			try {
				createPackage();
				Thread.sleep(packageGenerationDelay);
				System.out.println(list);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		FileHandler.closeLogFile(logCreated);
		FileHandler.closeLogFile(logDiscarted);
		FileHandler.closeLogFile(logQueue);
	}

}
