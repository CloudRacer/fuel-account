package uk.org.mcdonnell.fuelaccount.data;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import uk.org.mcdonnell.fuelaccount.data.schemas.PurchaseType;
import uk.org.mcdonnell.fuelaccount.util.xml.XMLListManager;
import android.content.Context;
import android.view.View;

public class PurchaseDataManager extends
		uk.org.mcdonnell.fuelaccount.data.schemas.PurchasesType {

	private String xmlFilename = null;

	private Context context;

	private XMLListManager xmlManager;

	private List<PurchaseType> purchases = null;

	public PurchaseDataManager(View view, String xmlFilename) {
		this.setContext(view.getContext().getApplicationContext());
		this.setXMLFilename(xmlFilename);
	}

	private Context getContext() {
		return context;
	}

	private void setContext(Context context) {
		this.context = context;
	}

	public List<PurchaseType> getPurchase() {
		if (purchases == null) {
			purchases = super.getPurchase();

			List<Object> records = getXMLManager().getRecords();
			Iterator<Object> iterator = records.iterator();
			while (iterator.hasNext()) {
				addPurchase((PurchaseType) iterator.next());
			}
		}

		return purchases;
	}

	private PurchaseType getPurchase(PurchaseType purchaseType) {
		return getPurchase(purchaseType.getDate());
	}

	private PurchaseType getPurchase(String date) {
		PurchaseType purchase = null;

		List<PurchaseType> purchases = super.getPurchase();
		if (purchases != null && !purchases.isEmpty()) {
			ListIterator<PurchaseType> list = purchases.listIterator();
			while (list.hasNext() && purchase == null) {
				PurchaseType entry = list.next();
				if (entry.getVehicle() != null && entry.getDate().equals(date)) {
					purchase = entry;
				}
			}
		}

		return purchase;
	}

	private void addPurchase(PurchaseType stationType) {
		if (getPurchase(stationType) == null) {
			super.getPurchase().add(stationType);
		} else {
			getPurchase(stationType).setVehicle(stationType.getVehicle());
			getPurchase(stationType).setStation(stationType.getStation());
			getPurchase(stationType).setDate(stationType.getDate());
			getPurchase(stationType).setVolume(stationType.getVolume());
			getPurchase(stationType).setPrice(stationType.getPrice());
			getPurchase(stationType).setTotal(stationType.getTotal());
		}
	}

	public PurchaseType deletePurchase(Context context, String date)
			throws Exception {
		PurchaseType station = getPurchase(date);
		if (station == null) {
			throw new Exception(String.format(
					"Cannot find detail of purchase on %s.", date.toString()));
		} else {
			getPurchase().remove(station);
			save(context);
		}

		return station;
	}

	public void save(Context context, PurchaseType stationType)
			throws IllegalArgumentException, IllegalStateException,
			IOException, IllegalAccessException, InvocationTargetException {
		addPurchase(stationType);

		save(context);
	}

	private void save(Context context) throws IllegalArgumentException,
			IllegalStateException, IOException, IllegalAccessException,
			InvocationTargetException {
		List<Object> records = new ArrayList<Object>();
		Iterator<PurchaseType> iterator = super.getPurchase().iterator();
		while (iterator.hasNext()) {
			records.add((Object) iterator.next());
		}

		getXMLManager().save(records);
	}

	private String getXMLFilename() {
		return xmlFilename;
	}

	private void setXMLFilename(String xmlFilename) {
		this.xmlFilename = xmlFilename;
	}

	private XMLListManager getXMLManager() {
		if (xmlManager == null) {
			xmlManager = new XMLListManager(getContext(), getXMLFilename());
		}
		return xmlManager;
	}

}
