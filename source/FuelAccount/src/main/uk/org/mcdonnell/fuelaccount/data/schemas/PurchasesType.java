package uk.org.mcdonnell.fuelaccount.data.schemas;

import java.util.ArrayList;
import java.util.List;

public class PurchasesType {

    protected List<PurchaseType> purchase;

    public List<PurchaseType> getPurchase() {
        if (purchase == null) {
        	purchase = new ArrayList<PurchaseType>();
        }
        return this.purchase;
    }

}
