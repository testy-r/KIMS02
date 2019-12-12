package util;

import java.util.List;
	
public class DataSet {
		
	public List<Double> values;
	public double[] targets;
	public String type;
		
	public DataSet(List<Double> values, double[] targets, String type) {
		this.values = values;
		this.targets = targets;
		this.type = type;
	}
}
