package uk.org.mcdonnell.fuelaccount.data.schemas;

public class PurchaseType {

	protected String date;
	protected String vehile;
	protected String station;
	protected String volume;
	protected String price;
	protected String total;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getVehicle() {
		return vehile;
	}

	public void setVehicle(String value) {
		this.vehile = value;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String value) {
		this.station = value;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String value) {
		this.volume = value;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String value) {
		this.price = value;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String value) {
		this.total = value;
	}
}
