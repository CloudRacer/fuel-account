package uk.org.mcdonnell.fuelaccount.data.schemas;

public class StationType {

    protected String title;
    protected String company;
    protected String postcode;

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String value) {
        this.company = value;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String value) {
        this.postcode = value;
    }
}
