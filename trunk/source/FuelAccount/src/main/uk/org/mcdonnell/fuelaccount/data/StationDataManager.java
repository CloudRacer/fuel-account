package uk.org.mcdonnell.fuelaccount.data;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import uk.org.mcdonnell.fuelaccount.data.schemas.StationType;
import uk.org.mcdonnell.fuelaccount.util.xml.XMLListManager;
import android.content.Context;
import android.view.View;

public class StationDataManager extends
        uk.org.mcdonnell.fuelaccount.data.schemas.StationsType {

    private String xmlFilename = null;

    private Context context;

    private XMLListManager xmlManager;

    private List<StationType> stations = null;

    public StationDataManager(View view, String xmlFilename) {
        this.setContext(view.getContext().getApplicationContext());
        this.setXMLFilename(xmlFilename);
    }

    private Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    public List<StationType> getStation() {
        if (stations == null) {
            stations = super.getStation();

            List<Object> records = getXMLManager().getRecords();
            Iterator<Object> iterator = records.iterator();
            while (iterator.hasNext()) {
                addStation((StationType) iterator.next());
            }
        }

        return stations;
    }

    private StationType getStation(StationType stationType) {
        return getStation(stationType.getTitle());
    }

    private StationType getStation(String stationName) {
        StationType station = null;

        List<StationType> stations = super.getStation();
        if (stations != null && !stations.isEmpty()) {
            ListIterator<StationType> list = stations.listIterator();
            while (list.hasNext() && station == null) {
                StationType entry = list.next();
                if (entry.getTitle() != null
                        && entry.getTitle().equalsIgnoreCase(stationName)) {
                    station = entry;
                }
            }
        }

        return station;
    }

    private void addStation(StationType stationType) {
        if (getStation(stationType) == null) {
            super.getStation().add(stationType);
        } else {
            getStation(stationType).setCompany(
                    stationType.getCompany());
            getStation(stationType).setPostcode(stationType.getPostcode());
        }
    }

    public StationType deleteStation(Context context, String stationName)
            throws Exception {
        StationType station = getStation(stationName);
        if (station == null) {
            throw new Exception(String.format(
                    "Cannot find detail of station with the name %s.",
                    stationName));
        } else {
            getStation().remove(station);
            save(context);
        }

        return station;
    }

    public void save(Context context, StationType stationType)
            throws IllegalArgumentException, IllegalStateException,
            IOException, IllegalAccessException, InvocationTargetException {
        addStation(stationType);

        save(context);
    }

    private void save(Context context) throws IllegalArgumentException,
            IllegalStateException, IOException, IllegalAccessException,
            InvocationTargetException {
        List<Object> records = new ArrayList<Object>();
        Iterator<StationType> iterator = super.getStation().iterator();
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
