package aplication;

import java.util.SplittableRandom;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.BufferedWriter;

public class OutputPort implements Runnable{

	private String portID;
	//private List<Package> list = new ArrayList<>();
	private ConcurrentLinkedQueue<Package> list = new ConcurrentLinkedQueue<Package>();
	private Integer size;
	private Integer packageFowardProbability;
	private Integer packageTransmittionDelay;
	private Integer retransmissionProbability;
	private SplittableRandom random;

	private BufferedWriter logSuccess;
	private BufferedWriter logRetransmitted;
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

	public ConcurrentLinkedQueue<Package> getList() {
		return list;
	}

	//mudar
	public boolean insertPackageInList(Package pack) {
		if (size > list.size()) {
			list.add(pack);
			return true;
		}
		return false;
	}

	private void transmitPackage() {
		if (list.size() > 0) {
			Package pack = list.poll();
			boolean isPackageTransmited = random.nextInt(1, 101) <= (100 - retransmissionProbability);
			
			if(isPackageTransmited) {
				FileHandler.writeLog(logSuccess, pack.toString());
			} else {
				retransmitPackage(pack);
			}
		}
	}

	private void retransmitPackage(Package pack) {
		int retransmission = 0;
		boolean isPackageTransmited = false;

		while(!isPackageTransmited) {
			retransmission ++;
			isPackageTransmited = random.nextInt(1, 101) <= (100 - (retransmissionProbability/Math.pow(2, retransmission)));
		}

		FileHandler.writeLog(logRetransmitted, pack.toString());
	}

	private void nonTreatedPackages() {
		for(Package pack : list) {
			FileHandler.writeLog(logNonTreated, pack.toString());
		}
	}

	
	@Override
	public String toString() {
		return "OutputPort [portID=" + portID + ", list=" + list + ", size=" + size + ", packageFowardProbability="
				+ packageFowardProbability + ", packageTransmittionDelay=" + packageTransmittionDelay
				+ ", retransmissionProbability=" + retransmissionProbability + "]";
	}

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

		FileHandler.closeLogFile(logSuccess);
		FileHandler.closeLogFile(logNonTreated);
		FileHandler.closeLogFile(logRetransmitted);
		System.out.println("Parando Output");
	}

}
