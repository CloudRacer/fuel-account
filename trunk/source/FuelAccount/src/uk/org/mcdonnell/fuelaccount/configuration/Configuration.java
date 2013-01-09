package uk.org.mcdonnell.fuelaccount.configuration;

import java.io.FileNotFoundException;

import android.annotation.SuppressLint;

public class Configuration {

    private enum FileNames {
        VEHICLES, STATIONS
    }

    @SuppressLint("DefaultLocale")
    public static String getVehiclesFile() throws FileNotFoundException {
        return FileNames.VEHICLES.toString().toLowerCase();
    }

    @SuppressLint("DefaultLocale")
    public static String getStationsFile() {
        return FileNames.STATIONS.toString().toLowerCase();
    }

}
