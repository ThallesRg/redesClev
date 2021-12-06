package aplication;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.io.BufferedWriter;

public class OutputPort implements Runnable{

	private String portID;
	private List<Package> list = new ArrayList<>();
	private Integer size;
	private Integer packageFowardProbability;
	private Integer packageTransmittionDelay;
	private Integer retransmissionProbability;
	private SplittableRandom random;

	private BufferedWriter logSuccess = FileHandler.createLogFile("log-enviados-com-sucesso-" + portID);
	private BufferedWriter logRetransmitted = FileHandler.createLogFile("log-retransmitidos-" + portID);
	private BufferedWriter logNonTreated = FileHandler.createLogFile("log-nao-tratados-fila-saida-" + portID);

	public OutputPort(String portID, Integer size, Integer packageFowardProbability, Integer packageTransmittionDelay,
			Integer retransmissionProbability) {
		this.portID = portID;
		this.size = size;
		this.packageFowardProbability = packageFowardProbability;
		this.packageTransmittionDelay = packageTransmittionDelay;
		this.retransmissionProbability = retransmissionProbability;
	}
	
	public String getPortID() {
		return portID;
	}



	public void setPortID(String portID) {
		this.portID = portID;
	}



	public Integer getSize() {
		return size;
	}



	public void setSize(Integer size) {
		this.size = size;
	}



	public Integer getPackageFowardProbability() {
		return packageFowardProbability;
	}



	public void setPackageFowardProbability(Integer packageFowardProbability) {
		this.packageFowardProbability = packageFowardProbability;
	}



	public Integer getPackageTransmittionDelay() {
		return packageTransmittionDelay;
	}



	public void setPackageTransmittionDelay(Integer packageTransmittionDelay) {
		this.packageTransmittionDelay = packageTransmittionDelay;
	}



	public Integer getRetransmissionProbability() {
		return retransmissionProbability;
	}



	public void setRetransmissionProbability(Integer retransmissionProbability) {
		this.retransmissionProbability = retransmissionProbability;
	}



	public List<Package> getList() {
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
			Package pack = list.remove(0);
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
	public void run() {

		while(true) {
			try {
				transmitPackage();
				Thread.sleep(packageTransmittionDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//nonTreatedPackages();
	}

}
