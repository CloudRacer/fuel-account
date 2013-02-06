package uk.org.mcdonnell.fuelaccount.data.schemas;

public class StationType {

    protected String stationName;
    protected String petrolCompany;
    protected String postCode;

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String value) {
        this.stationName = value;
    }

    public String getPetrolCompany() {
        return petrolCompany;
    }

    public void setPetrolCompany(String value) {
        this.petrolCompany = value;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String value) {
        this.postCode = value;
    }
}
