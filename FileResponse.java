package aplication;

import java.util.ArrayList;
import java.util.List;

public class FileResponse {

    private Integer swithFabric;
    private List<List<String>> inputList = new ArrayList<>();
    private List<List<String>> outputList = new ArrayList<>();
	
    public List<List<String>> getInputList() {
		return inputList;
	}
	
	public Integer getSwithFabric() {
		return swithFabric;
	}
	public List<List<String>> getOutputList() {
		return outputList;
	}
	
	public void setSwithFabric(Integer swithFabric) {
		this.swithFabric = swithFabric;
	}

	public void addInputList(List<String> stringList) {
		inputList.add(stringList);
	}
	
	public void addOutputList(List<String> stringList) {
		outputList.add(stringList);
	}
}
