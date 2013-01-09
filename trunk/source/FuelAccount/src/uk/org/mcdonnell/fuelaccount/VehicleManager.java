package uk.org.mcdonnell.fuelaccount;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import uk.org.mcdonnell.fuelaccount.configuration.Configuration;
import uk.org.mcdonnell.fuelaccount.schemas.VehicleType;
import uk.org.mcdonnell.fuelaccount.schemas.VehiclesType;
import android.content.Context;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;

public class VehicleManager extends VehiclesType {

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
            vehicle = super.getVehicle();

            // Load the XML File.
            try {
                FileInputStream fileInputStream = getContext().openFileInput(
                        Configuration.getVehiclesFile());

                if (new File(Configuration.getVehiclesFile()).exists()) {
                    XmlPullParserFactory factory = XmlPullParserFactory
                            .newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xmlPullParser = factory.newPullParser();

                    xmlPullParser.setInput(fileInputStream, ENCODING);
                    int eventType = xmlPullParser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            System.out.println("Start document");
                        } else if (eventType == XmlPullParser.START_TAG) {
                            System.out.println("Start tag "
                                    + xmlPullParser.getName());
                        } else if (eventType == XmlPullParser.END_TAG) {
                            System.out.println("End tag "
                                    + xmlPullParser.getName());
                        } else if (eventType == XmlPullParser.TEXT) {
                            System.out.println("Text "
                                    + xmlPullParser.getText());
                        }
                        try {
                            eventType = xmlPullParser.next();
                        } catch (IOException e) {
                            Log.e(this.getClass().getName(),
                                    "Error occurred while saving.", e);
                            Toast.makeText(getContext(), e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    System.out.println("End document");
                }
            } catch (FileNotFoundException e) {
                Log.e(this.getClass().getName(),
                        "Error occurred while saving.", e);
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG)
                        .show();
            } catch (XmlPullParserException e) {
                Log.e(this.getClass().getName(),
                        "Error occurred while saving.", e);
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        }

        return vehicle;
    }

    public VehicleType getVehicle(String registration) {
        VehicleType vehicle = null;

        List<VehicleType> vehicles = super.getVehicle();
        if (vehicles != null && !vehicles.isEmpty()) {
            ListIterator<VehicleType> list = vehicles.listIterator();
            while (list.hasNext() && vehicle == null) {
                VehicleType entry = list.next();
                if (entry.getRegistration().equalsIgnoreCase(registration)) {
                    vehicle = entry;
                }
            }
        }

        return vehicle;
    }

    public void save(VehicleType vehicleType) throws Exception {
        VehicleType existingVehicle = getVehicle(vehicleType.getRegistration());
        if (existingVehicle == null) {
            super.getVehicle().add(vehicleType);

            save();
        }
    }

    public void save() throws IOException {
        FileOutputStream outputStream = getContext().openFileOutput(
                Configuration.getVehiclesFile(), Context.MODE_PRIVATE);

        XmlSerializer serializer = Xml.newSerializer();

        serializer.setOutput(outputStream, ENCODING);
        serializer.startDocument(null, Boolean.valueOf(true));
        serializer.setFeature(
                "http://xmlpull.org/v1/doc/features.html#indent-output", true);
        serializer.startTag(null, "vehicles");

        List<VehicleType> vehicles = super.getVehicle();
        if (vehicles != null && !vehicles.isEmpty()) {
            ListIterator<VehicleType> list = vehicles.listIterator();
            while (list.hasNext()) {
                VehicleType entry = list.next();

                serializer.startTag(null, "vehicle");
                serializer.startTag(null, "manufacturer");
                serializer.text(entry.getManufacturer());
                serializer.endTag(null, "manufacturer");
                serializer.startTag(null, "Model");
                serializer.text(entry.getModel());
                serializer.endTag(null, "Model");
                serializer.startTag(null, "registration");
                serializer.text(entry.getRegistration());
                serializer.endTag(null, "registration");
                serializer.endTag(null, "vehicle");
            }
        }

        serializer.endTag(null, "vehicles");
        serializer.endDocument();
        serializer.flush();
        outputStream.close();
    }

}
