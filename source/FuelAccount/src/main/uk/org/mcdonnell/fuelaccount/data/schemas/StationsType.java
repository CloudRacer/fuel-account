package uk.org.mcdonnell.fuelaccount.data.schemas;

import java.util.ArrayList;
import java.util.List;

public class StationsType {

	protected List<StationType> station;

	public List<StationType> getStation() {
		if (station == null) {
			station = new ArrayList<StationType>();
		}
		return this.station;
	}

}
