package processor;

import util.PropertyData;

public class AvgValue implements AvgCalculator {

	@Override
	public double avgItem(PropertyData property) {
		return property.getMarketValue();
	}
	
	
}
