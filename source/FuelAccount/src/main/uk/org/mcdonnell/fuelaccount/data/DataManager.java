package uk.org.mcdonnell.fuelaccount.data;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import uk.org.mcdonnell.fuelaccount.data.schemas.VehicleType;
import uk.org.mcdonnell.fuelaccount.util.xml.XMLListManager;
import android.content.Context;
import android.view.View;

public class DataManager extends
        uk.org.mcdonnell.fuelaccount.data.schemas.VehiclesType {

    private String xmlFilename = null;

    private Context context;

    private XMLListManager xmlManager;

    private List<VehicleType> vehicles = null;

    public DataManager(View view, String xmlFilename) {
        this.setContext(view.getContext().getApplicationContext());
        this.setXMLFilename(xmlFilename);
    }

    private Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    public List<VehicleType> getVehicle() {
        if (vehicles == null) {
            vehicles = super.getVehicle();

            List<Object> records = getXMLManager().getRecords();
            Iterator<Object> iterator = records.iterator();
            while (iterator.hasNext()) {
                addVehicle((VehicleType) iterator.next());
            }
        }

        return vehicles;
    }

    private VehicleType getVehicle(VehicleType vehicleType) {
        return getVehicle(vehicleType.getRegistration());
    }

    private VehicleType getVehicle(String registration) {
        VehicleType vehicle = null;

        List<VehicleType> vehicles = super.getVehicle();
        if (vehicles != null && !vehicles.isEmpty()) {
            ListIterator<VehicleType> list = vehicles.listIterator();
            while (list.hasNext() && vehicle == null) {
                VehicleType entry = list.next();
                if (entry.getRegistration() != null
                        && entry.getRegistration().equalsIgnoreCase(
                                registration)) {
                    vehicle = entry;
                }
            }
        }

        return vehicle;
    }

    private void addVehicle(VehicleType vehicleType) {
        if (getVehicle(vehicleType) == null) {
            super.getVehicle().add(vehicleType);
        } else {
            getVehicle(vehicleType).setManufacturer(
                    vehicleType.getManufacturer());
            getVehicle(vehicleType).setModel(vehicleType.getModel());
        }
    }

    public VehicleType deleteVehicle(Context context, String registration)
            throws Exception {
        VehicleType vehicle = getVehicle(registration);
        if (vehicle == null) {
            throw new Exception(String.format(
                    "Cannot find detail of vehicle with registration %s.",
                    registration));
        } else {
            getVehicle().remove(vehicle);
            save(context);
        }

        return vehicle;
    }

    public void save(Context context, VehicleType vehicleType)
            throws IllegalArgumentException, IllegalStateException,
            IOException, IllegalAccessException, InvocationTargetException {
        addVehicle(vehicleType);

        save(context);
    }

    private void save(Context context) throws IllegalArgumentException,
            IllegalStateException, IOException, IllegalAccessException,
            InvocationTargetException {
        List<Object> records = new ArrayList<Object>();
        Iterator<VehicleType> iterator = super.getVehicle().iterator();
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
