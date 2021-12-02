package aplication;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.lang.Thread;

public class InputPort implements Runnable{

	private String portID;
	private List<Package> list = new ArrayList<>();
	private Integer size;
	private Integer packageGenerationDelay;
	private Integer dropProbability;
	private Integer numberOfPackagesCreated;
	private SplittableRandom random;

	//private BufferedWriter logCreateds = FileHandler.createLogFile("log_criados");

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
	}
	
	

	public String getPortID() {
		return portID;
	}



	public void setPortID(String portID) {
		this.portID = portID;
	}



	public List<Package> getList() {
		return list;
	}
	


	public Integer getSize() {
		return size;
	}



	public void setSize(Integer size) {
		this.size = size;
	}



	public Integer getPackageGenerationDelay() {
		return packageGenerationDelay;
	}



	public void setPackageGenerationDelay(Integer packageGenerationDelay) {
		this.packageGenerationDelay = packageGenerationDelay;
	}



	public Integer getDropProbability() {
		return dropProbability;
	}



	public void setDropProbability(Integer dropProbability) {
		this.dropProbability = dropProbability;
	}



	private void createPackage() {
		numberOfPackagesCreated++;
		Package pack = new Package(portID + "" + numberOfPackagesCreated); 
		boolean isPackageDiscarded = random.nextInt(1, 101) <= dropProbability;
		if (isPackageDiscarded) {
			discardPackage(pack);
			return;
		} else if ( list.size() >= size ) {
			//log lista cheia
		}
		insertPackage(pack);
		//FileHandler.writeLog( logCreateds, "Pacote x criado" );
	}
	
	private void discardPackage(Package pack) {
		System.out.println("Descartando pacote " + pack);
	}

	private void insertPackage(Package pack) {
		list.add(pack);
	}
	
	public Package removePackage() {
		return list.remove(0);
	}
	
	@Override
	public void run() {

		while(true) {
			try {
				createPackage();
				Thread.sleep(packageGenerationDelay);
				System.out.println(list);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}

}
