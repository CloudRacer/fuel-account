package uk.org.mcdonnell.fuelaccount;

import uk.org.mcdonnell.fuelaccount.view.station.StationFragment;
import uk.org.mcdonnell.fuelaccount.view.vehicle.VehicleFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class FuelAccount extends FragmentActivity {
    public final static String EXTRA_MESSAGE = "uk.org.mcdonnell.fuelaccount.MESSAGE";

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ********** FOR TESTING ONLY **********
        try {
            String FILENAME = "TEST";
            //String FILENAME = "stations.xml";
            // String string = "hello world!";

            // deleteFile(FILENAME);
            // deleteFile(uk.org.mcdonnell.fuelaccount.configuration.Configuration
            // .getStationsFile());
            // deleteFile(uk.org.mcdonnell.fuelaccount.util.configuration.Configuration
            // .getVehiclesFile());

            System.out.println("Application File Count:" + fileList().length
                    + ".");

            // FileOutputStream fos;
            if (getFileStreamPath(FILENAME).exists()) {
                System.out.println("EXISTS");
            } else {
                System.out.println("NOT EXIST");
            }
            // fos = getApplicationContext().openFileOutput(FILENAME,
            // Context.MODE_PRIVATE);
            // fos.write(string.getBytes());
            // fos.close();
            if (getFileStreamPath(FILENAME).exists()) {
                System.out.println("EXIST");
            } else {
                System.out.println("NOT EXIST");
            }

            deleteFile(FILENAME);
            // deleteFile(uk.org.mcdonnell.fuelaccount.configuration.Configuration
            // .getStationsFile());
            // deleteFile(uk.org.mcdonnell.fuelaccount.configuration.Configuration
            // .getVehiclesFile());

            System.out.println("Application File Count:" + fileList().length
                    + ".");
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "Error occurred while saving.", e);
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.menu_main_vehicles:
            showVehicle();
            return true;
        case R.id.menu_main_stations:
            showStation();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void showVehicle() {
        // Create fragment and give it an argument specifying the article it
        // should show
        VehicleFragment newFragment = new VehicleFragment();
        showFragment(newFragment);
    }

    private void showStation() {
        // Create fragment and give it an argument specifying the article it
        // should show
        StationFragment newFragment = new StationFragment();
        showFragment(newFragment);
    }

    private void showFragment(Fragment newFragment) {
        Bundle args = new Bundle();

        setCurrentFragment(newFragment);

        getCurrentFragment().setArguments(args);

        // Replace whatever is in the fragment_container view with this
        // fragment,
        // and add the transaction to the back stack so the user can
        // navigate back
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.fragment_container, getCurrentFragment());
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private Fragment getCurrentFragment() {
        return currentFragment;
    }

    private void setCurrentFragment(Fragment currentFragment) {
        if (getCurrentFragment() != null) {
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.fragment_container, getCurrentFragment());
            transaction.remove(getCurrentFragment());

            // Commit the transaction
            transaction.commit();

            getCurrentFragment().onStop();
        }

        this.currentFragment = currentFragment;
    }
}
