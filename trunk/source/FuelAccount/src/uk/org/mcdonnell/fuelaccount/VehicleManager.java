package uk.org.mcdonnell.fuelaccount;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import uk.org.mcdonnell.fuelaccount.configuration.Configuration;
import uk.org.mcdonnell.fuelaccount.schemas.ObjectFactory;
import uk.org.mcdonnell.fuelaccount.schemas.VehicleType;
import uk.org.mcdonnell.fuelaccount.schemas.VehiclesType;
import android.content.Context;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;

public class VehicleManager extends VehiclesType {

    private static final String ROOT_ELEMENT = "vehicles";
    private static final String RECORD_ELEMENT = "vehicle";
    private static final String REGISTRATION_ELEMENT = "registration";
    private static final String MODEL_ELEMENT = "model";
    private static final String MANUFACTURER_ELEMENT = "manufacturer";
    private static final String ENCODING = "UTF-8";

    private Context context;

    private List<VehicleType> vehicles = null;

    public VehicleManager(View view) {
        this.setContext(view.getContext().getApplicationContext());
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

            // Load the XML File.
            FileInputStream fileInputStream = null;
            try {
                if (getContext().getFileStreamPath(
                        Configuration.getVehiclesFile()).exists()) {
                    fileInputStream = getContext().openFileInput(
                            Configuration.getVehiclesFile());

                    VehicleType vehicleType = null;
                    XmlPullParserFactory factory = XmlPullParserFactory
                            .newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xmlPullParser = factory.newPullParser();

                    xmlPullParser.setInput(fileInputStream, ENCODING);
                    int eventType = xmlPullParser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType != XmlPullParser.START_DOCUMENT) {
                            if (eventType == XmlPullParser.START_TAG) {
                                if (xmlPullParser.getName().equalsIgnoreCase(
                                        RECORD_ELEMENT)) {
                                    vehicleType = new ObjectFactory()
                                            .createVehicleType();
                                } else if (xmlPullParser.getName()
                                        .equalsIgnoreCase(MANUFACTURER_ELEMENT)) {
                                    // Go to the element content.
                                    eventType = xmlPullParser.next();
                                    vehicleType.setManufacturer(xmlPullParser
                                            .getText());
                                    // Go to the closing tag.
                                    eventType = xmlPullParser.next();
                                } else if (xmlPullParser.getName()
                                        .equalsIgnoreCase(MODEL_ELEMENT)) {
                                    // Go to the element content.
                                    eventType = xmlPullParser.next();
                                    vehicleType.setModel(xmlPullParser
                                            .getText());
                                    // Go to the closing tag.
                                    eventType = xmlPullParser.next();
                                } else if (xmlPullParser.getName()
                                        .equalsIgnoreCase(REGISTRATION_ELEMENT)) {
                                    // Go to the element content.
                                    eventType = xmlPullParser.next();
                                    vehicleType.setRegistration(xmlPullParser
                                            .getText());
                                    // Go to the closing tag.
                                    eventType = xmlPullParser.next();
                                }
                            } else if (eventType == XmlPullParser.END_TAG) {
                                if (xmlPullParser.getName().equalsIgnoreCase(
                                        RECORD_ELEMENT)) {
                                    addVehicle(vehicleType);
                                    vehicleType = null;
                                }
                            }
                        }

                        eventType = xmlPullParser.next();
                    }
                }
            } catch (Exception e) {
                Log.e(this.getClass().getName(),
                        "Error occurred while saving.", e);
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG)
                        .show();

            } finally {
                try {
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                } catch (IOException e) {
                    Log.e(this.getClass().getName(),
                            "Error occurred while saving.", e);
                    Toast.makeText(getContext(), e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }

        return vehicles;
    }

    public VehicleType getVehicle(VehicleType vehicleType) {
        return getVehicle(vehicleType.getRegistration());
    }

    public VehicleType getVehicle(String registration) {
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
            throws IllegalArgumentException, IllegalStateException, IOException {
        addVehicle(vehicleType);

        save(context);
    }

    public void save(Context context) throws IllegalArgumentException,
            IllegalStateException, IOException {
        FileOutputStream outputStream = context.openFileOutput(
                Configuration.getVehiclesFile(), Context.MODE_PRIVATE);

        XmlSerializer serializer = Xml.newSerializer();

        serializer.setOutput(outputStream, ENCODING);
        serializer.startDocument(null, Boolean.valueOf(true));
        serializer.setFeature(
                "http://xmlpull.org/v1/doc/features.html#indent-output", true);
        serializer.startTag(null, ROOT_ELEMENT);

        List<VehicleType> vehicles = super.getVehicle();
        if (vehicles != null && !vehicles.isEmpty()) {
            ListIterator<VehicleType> list = vehicles.listIterator();
            while (list.hasNext()) {
                VehicleType entry = list.next();

                if (entry.getRegistration() != null
                        && entry.getRegistration().toString().length() != 0) {
                    serializer.startTag(null, RECORD_ELEMENT);
                    serializer.startTag(null, MANUFACTURER_ELEMENT);
                    if (entry.getManufacturer() != null
                            && entry.getManufacturer().toString().length() != 0) {
                        serializer.text(entry.getManufacturer());
                    }
                    serializer.endTag(null, MANUFACTURER_ELEMENT);
                    serializer.startTag(null, "Model");
                    if (entry.getModel() != null
                            && entry.getModel().toString().length() != 0) {
                        serializer.text(entry.getModel());
                    }
                    serializer.endTag(null, "Model");
                    serializer.startTag(null, REGISTRATION_ELEMENT);
                    serializer.text(entry.getRegistration());
                    serializer.endTag(null, REGISTRATION_ELEMENT);
                    serializer.endTag(null, RECORD_ELEMENT);
                }
            }
        }

        serializer.endTag(null, ROOT_ELEMENT);
        serializer.endDocument();
        serializer.flush();
        outputStream.close();
    }
}
