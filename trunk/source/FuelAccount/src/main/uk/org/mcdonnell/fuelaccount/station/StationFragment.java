package uk.org.mcdonnell.fuelaccount.station;

import uk.org.mcdonnell.fuelaccount.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class StationFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_station, container,
                false);
        view.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });

        Button button = (Button) view.findViewById(R.id.button_save_station);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();

                if (activity != null) {
                    Toast.makeText(activity, "Button clicked!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
}
