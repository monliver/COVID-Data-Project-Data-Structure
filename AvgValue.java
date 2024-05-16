package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.PropertyData;

public class AvgValue implements AvgCalculator {

	@Override
	public double avgItem(PropertyData property) {
		return property.getMarketValue();
	}
	
	
}
