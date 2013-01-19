package uk.org.mcdonnell.fuelaccount.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import uk.org.mcdonnell.fuelaccount.util.common.ClassReflection;
import uk.org.mcdonnell.fuelaccount.util.common.Filename;
import uk.org.mcdonnell.fuelaccount.util.common.Miscellaneous;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

class XMLListManager extends uk.org.mcdonnell.fuelaccount.data.schemas.VehiclesType {

    private static final String ENCODING = "UTF-8";

    private String recordElementName = null;
    private String rootElementName = null;
    private String xmlFilename = null;

    private Context context;

    @SuppressWarnings("unused")
    private XMLListManager() {
    }

    public XMLListManager(Context context, String xmlFilename) {
        this.setContext(context);
        this.setXMLFilename(xmlFilename);
    }

    private Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    public List<Object> getRecords() {
        // Load the XML File.
        FileInputStream fileInputStream = null;
        List<Object> records = new ArrayList<Object>();
        try {
            if (getContext().getFileStreamPath(getXMLFilename()).exists()) {
                fileInputStream = getContext().openFileInput(getXMLFilename());

                Object record = null;
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
                                String className = String
                                        .format("%s.%s%s",
                                                "uk.org.mcdonnell.fuelaccount.data.schemas",
                                                Miscellaneous
                                                        .initialiseText(getRecordElementName()),
                                                "Type");
                                record = Class.forName(className).newInstance();
                            } else if (record != null) {
                                String methodName = ClassReflection
                                        .deriveSetterMethodName(xmlPullParser
                                                .getName());
                                Method method = ClassReflection.getMethod(
                                        record.getClass(), methodName);
                                if (method != null) {
                                    xmlPullParser.next();
                                    method.invoke(record,
                                            xmlPullParser.getText());
                                }

                            }

                            break;
                        case XmlPullParser.END_TAG:
                            if (xmlPullParser.getName().equalsIgnoreCase(
                                    getRecordElementName())) {
                                records.add(record);
                                record = null;
                            }

                            break;
                        }
                    }

                    eventType = xmlPullParser.next();
                }
            }
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "Error occurred while saving.", e);
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
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        }

        return records;
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
