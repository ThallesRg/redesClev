package aplication;

import java.util.ArrayList;
import java.util.List;

public class OutputPort implements Runnable{

	private String portID;
	private List<Package> list = new ArrayList<>();
	private Integer size;
	private Integer packageFowardProbability;
	private Integer packageTransmittionDelay;
	private Integer retransmissionProbability;

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



	private void transmitPackage() {
		
	}

	public void createLog() {

	}
	
	@Override
	public void run() {
	}

}
