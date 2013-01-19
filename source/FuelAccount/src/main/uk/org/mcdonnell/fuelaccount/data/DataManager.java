package uk.org.mcdonnell.fuelaccount.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import uk.org.mcdonnell.fuelaccount.data.schemas.ObjectFactory;
import uk.org.mcdonnell.fuelaccount.data.schemas.VehicleType;
import uk.org.mcdonnell.fuelaccount.util.common.ClassReflection;
import uk.org.mcdonnell.fuelaccount.util.common.Filename;
import android.content.Context;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;

public class DataManager extends
        uk.org.mcdonnell.fuelaccount.data.schemas.VehiclesType {

    private static final String ENCODING = "UTF-8";

    private String recordElementName = null;
    private String rootElementName = null;
    private String xmlFilename = null;

    private Context context;

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

            // Load the XML File.
            FileInputStream fileInputStream = null;
            try {
                if (getContext().getFileStreamPath(getXMLFilename()).exists()) {
                    fileInputStream = getContext().openFileInput(
                            getXMLFilename());

                    VehicleType vehicleType = null;
                    XmlPullParserFactory factory = XmlPullParserFactory
                            .newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xmlPullParser = factory.newPullParser();

                    xmlPullParser.setInput(fileInputStream, ENCODING);
                    int eventType = xmlPullParser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType != XmlPullParser.START_DOCUMENT) {
                            switch (eventType) {
                            case XmlPullParser.START_TAG:
                                if (xmlPullParser.getName().equalsIgnoreCase(
                                        getRecordElementName())) {
                                    vehicleType = new ObjectFactory()
                                            .createVehicleType();
                                } else if (vehicleType != null) {
                                    String methodName = ClassReflection
                                            .deriveSetterMethodName(xmlPullParser
                                                    .getName());
                                    Method method = ClassReflection.getMethod(
                                            vehicleType.getClass(), methodName);
                                    if (method != null) {
                                        xmlPullParser.next();
                                        method.invoke(vehicleType,
                                                xmlPullParser.getText());
                                    }

                                }

                                break;
                            case XmlPullParser.END_TAG:
                                if (xmlPullParser.getName().equalsIgnoreCase(
                                        getRecordElementName())) {
                                    addVehicle(vehicleType);
                                    vehicleType = null;

                                    break;
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
            throws IllegalArgumentException, IllegalStateException,
            IOException, IllegalAccessException, InvocationTargetException {
        addVehicle(vehicleType);

        save(context);
    }

    public void save(Context context) throws IllegalArgumentException,
            IllegalStateException, IOException, IllegalAccessException,
            InvocationTargetException {
        FileOutputStream outputStream = context.openFileOutput(
                getXMLFilename(), Context.MODE_PRIVATE);

        XmlSerializer serializer = Xml.newSerializer();

        serializer.setOutput(outputStream, ENCODING);
        serializer.startDocument(null, Boolean.valueOf(true));
        serializer.setFeature(
                "http://xmlpull.org/v1/doc/features.html#indent-output", true);
        serializer.startTag(null, getRootElementName());

        List<VehicleType> vehicles = super.getVehicle();
        if (vehicles != null && !vehicles.isEmpty()) {
            ListIterator<VehicleType> list = vehicles.listIterator();
            while (list.hasNext()) {
                serializer.startTag(null, getRecordElementName());

                VehicleType entry = list.next();

                Method[] methods = entry.getClass().getMethods();

                for (int i = 0; i < methods.length; i++) {
                    Method method = methods[i];

                    if (method.getName().startsWith("get")
                            && !method.getName().equals("getClass")
                            && !method.getName().equals("getName")) {
                        String elementName = method.getName().substring(3)
                                .toLowerCase(Locale.getDefault());
                        String elementValue = (String) method.invoke(entry,
                                new Object[] {});
                        serializer.startTag(null, elementName);
                        if (elementValue != null && elementValue.length() != 0) {
                            serializer.text(elementValue);
                        }
                        serializer.endTag(null, elementName);
                    }
                }

                serializer.endTag(null, getRecordElementName());
            }
        }

        serializer.endTag(null, getRootElementName());
        serializer.endDocument();
        serializer.flush();
        outputStream.close();
    }

    private String getRecordElementName() throws FileNotFoundException {
        if (recordElementName == null) {
            if (getRootElementName().endsWith("s")) {
                recordElementName = getRootElementName().substring(0,
                        getRootElementName().length() - 1);
            }
        }

        return recordElementName;
    }

    private String getRootElementName() throws FileNotFoundException {
        if (rootElementName == null) {
            File vehiclesFile = new File(getXMLFilename());
            String filenameWithoutPathOrExtension = Filename
                    .FilenameWithoutPathOrExtension(new File(vehiclesFile
                            .getName()));

            if (!filenameWithoutPathOrExtension.endsWith("s")) {
                filenameWithoutPathOrExtension = String.format("%ss",
                        filenameWithoutPathOrExtension, "s");
            }
            rootElementName = filenameWithoutPathOrExtension;
        }

        return rootElementName;
    }

    private String getXMLFilename() {
        return xmlFilename;
    }

    private void setXMLFilename(String xmlFilename) {
        this.xmlFilename = xmlFilename;
    }

}
