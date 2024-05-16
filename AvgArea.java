package processor;

import util.PropertyData;

public class AvgArea implements AvgCalculator {

	@Override
	public double avgItem(PropertyData property) {
		return property.getTotalLivableArea();
	}

}
