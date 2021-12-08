package aplication;

import java.util.ArrayList;
import java.util.List;

public class FileResponse {

    private String switchFabric;
	private List<InputPort> inputPorts = new ArrayList<>();
    private List<OutputPort> outputPorts = new ArrayList<>();
	
    public int getSwitchFabric() {
		return Integer.parseInt(switchFabric);
	}
	public List<InputPort> getInputPorts() {
		return inputPorts;
	}
	public List<OutputPort> getOutputPorts() {
		return outputPorts;
	}

	public void setSwitchFabric(String switchFabric){
		this.switchFabric = switchFabric;
	}

	public void addInputPort(String portID, String size, String packageGenerationDelay, String dropProbability){
		int dropProbabilityInt = Integer.parseInt(dropProbability);
		checkProbability(dropProbabilityInt);
		inputPorts.add(new InputPort(portID, Integer.parseInt(size), Integer.parseInt(packageGenerationDelay), dropProbabilityInt));
	}

	public void addOutputPort(String portID, String size, String packageFowardProbabily, String packageTransmissionDelay, String retransmissionProbabily){	
		int packageFowardProbabilyInt = Integer.parseInt(packageFowardProbabily);
		int retransmissionProbabilyInt = Integer.parseInt(retransmissionProbabily);
		checkProbability(packageFowardProbabilyInt, retransmissionProbabilyInt);
		outputPorts.add(new OutputPort(portID, Integer.parseInt(size), packageFowardProbabilyInt, Integer.parseInt(packageTransmissionDelay), retransmissionProbabilyInt));
	}
	
	private void checkProbability(int... probabilities) {
		for (int probability : probabilities) {
			if (probability < 0 || probability > 100) {
				throw new IllegalArgumentException("Número " + probability + " não é uma probabilidade.");
			}
		}
	}
	
	public void checkSumFowardProbability() {
		int sum=0;
		for (OutputPort outputPort : getOutputPorts()){
			sum += outputPort.getPackageFowardProbability();
		}
		
		if (sum != 100){
			throw new ArithmeticException("Soma das probabilidades de repasse das portas de saída não da 100.");
		}
	}
	
}
