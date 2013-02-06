package uk.org.mcdonnell.fuelaccount.view.station;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import uk.org.mcdonnell.fuelaccount.R;
import uk.org.mcdonnell.fuelaccount.data.StationDataManager;
import uk.org.mcdonnell.fuelaccount.data.schemas.StationType;
import uk.org.mcdonnell.fuelaccount.util.configuration.Configuration;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class StationFragment extends Fragment {
    private View view;

    private EditText stationName;
    private EditText petrolCompany;
    private EditText postCode;
    private Button doneButton;
    private ListView listView;

    private StationDataManager stationManager;
    private StationAdapter stationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setView(inflater.inflate(R.layout.fragment_station, container, false));
        getView().setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });

        getDoneButton().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();

                if (activity != null) {
                    try {
                        StationType stationType = new StationType();
                        stationType.setStationName(getStationName().getText()
                                .toString());
                        stationType.setPetrolCompany(getPetrolCompany()
                                .getText().toString());
                        stationType.setPostCode(getPostCode().getText()
                                .toString());
                        getStationManager().save(getView().getContext(),
                                stationType);
                        stationAdapter.notifyDataSetChanged();
                        getStationName().setText(null);
                        getPetrolCompany().setText(null);
                        getPostCode().setText(null);
                    } catch (Exception e) {
                        Log.e(this.getClass().getName(),
                                "Error occurred while saving.", e);
                        Toast.makeText(activity, e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        getStationName().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                enableButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }
        });

        getPetrolCompany().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                enableButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }
        });

        getPostCode().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                enableButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }
        });

        getListView().setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(android.widget.AdapterView<?> arg0,
                    View arg1, int arg2, long arg3) {
                getStationName().setText(
                        ((StationType) arg0.getItemAtPosition(arg2))
                                .getStationName());
                getPetrolCompany().setText(
                        ((StationType) arg0.getItemAtPosition(arg2))
                                .getPetrolCompany());
                getPostCode().setText(
                        ((StationType) arg0.getItemAtPosition(arg2))
                                .getPostCode());
            };
        });

        getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    int arg2, long arg3) {
                try {
                    getStationManager().deleteStation(
                            getView().getContext(),
                            ((StationType) arg0.getItemAtPosition(arg2))
                                    .getStationName());
                    stationAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e(this.getClass().getName(),
                            "Error occurred while deleting.", e);
                    Toast.makeText(getActivity(), e.getMessage(),
                            Toast.LENGTH_LONG).show();

                    return false;
                }

                return true;
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Bind the list to the XML.
        try {
            stationAdapter = new StationAdapter(this.getView().getContext(),
                    R.layout.row, (ArrayList<StationType>) getStationManager()
                            .getStation());
            ListView list = (ListView) getView()
                    .findViewById(R.id.listStations);
            list.setAdapter(stationAdapter);
        } catch (FileNotFoundException e) {
            Log.e(this.getClass().getName(), "Error occurred while deleting.",
                    e);
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public View getView() {
        return view;
    }

    private void setView(View view) {
        this.view = view;
    }

    private EditText getStationName() {
        if (stationName == null) {
            stationName = (EditText) view.findViewById(R.id.station_name);
        }
        return stationName;
    }

    private EditText getPetrolCompany() {
        if (petrolCompany == null) {
            petrolCompany = (EditText) view
                    .findViewById(R.id.station_petrol_company);
        }
        return petrolCompany;
    }

    private EditText getPostCode() {
        if (postCode == null) {
            postCode = (EditText) view.findViewById(R.id.station_post_code);
        }
        return postCode;
    }

    private StationDataManager getStationManager() throws FileNotFoundException {
        if (stationManager == null) {
            stationManager = new StationDataManager(getView(),
                    Configuration.getStationsFile());
        }
        return stationManager;
    }

    private Button getDoneButton() {
        if (doneButton == null) {
            doneButton = (Button) view.findViewById(R.id.button_save_station);
        }
        return doneButton;
    }

    private ListView getListView() {
        if (listView == null) {
            listView = (ListView) view.findViewById(R.id.listStations);
        }
        return listView;
    }

    private void enableButton() {
        if ((getStationName().length() > 0)
                && (getPetrolCompany().length() > 0)
                && (getPostCode().length() > 0)) {
            getDoneButton().setEnabled(true);
        } else {
            getDoneButton().setEnabled(false);
        }
    }

}