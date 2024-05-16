package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.PropertyData;

public class AvgArea implements AvgCalculator {

	@Override
	public double avgItem(PropertyData property) {
		return property.getTotalLivableArea();
	}

}
