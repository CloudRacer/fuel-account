package uk.org.mcdonnell.fuelaccount;

import java.util.ArrayList;

import uk.org.mcdonnell.fuelaccount.schemas.VehicleType;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class VehicleFragment extends Fragment {
    private View view;

    private Button doneButton;

    private EditText manufacturer;
    private EditText model;
    private EditText registration;

    private VehicleManager vehicleManager;
    private VehicleAdapter vehicleAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setView(inflater.inflate(R.layout.fragment_vehicle, container, false));
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
                        VehicleType vehicleType = new VehicleType();
                        vehicleType.setManufacturer(getManufacturer().getText()
                                .toString());
                        vehicleType.setModel(getModel().getText().toString());
                        vehicleType.setRegistration(getRegistration().getText()
                                .toString());
                        getVehicleManager().save(getView().getContext(),
                                vehicleType);
                    } catch (Exception e) {
                        Log.e(this.getClass().getName(),
                                "Error occurred while saving.", e);
                        Toast.makeText(activity, e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        getManufacturer().addTextChangedListener(new TextWatcher() {
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

        getModel().addTextChangedListener(new TextWatcher() {
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

        getRegistration().addTextChangedListener(new TextWatcher() {
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

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Bind the list to the XML.
        vehicleAdapter = new VehicleAdapter(this.getView().getContext(),
                R.layout.row, (ArrayList<VehicleType>) getVehicleManager()
                        .getVehicle());
        ListView list = (ListView) getView().findViewById(R.id.listVehicles);
        list.setAdapter(vehicleAdapter);
    }

    @Override
    public View getView() {
        return view;
    }

    private void setView(View view) {
        this.view = view;
    }

    private EditText getManufacturer() {
        if (manufacturer == null) {
            manufacturer = (EditText) view
                    .findViewById(R.id.vehicle_manufacturer);
        }
        return manufacturer;
    }

    private EditText getModel() {
        if (model == null) {
            model = (EditText) view.findViewById(R.id.vehicle_model);
        }
        return model;
    }

    private EditText getRegistration() {
        if (registration == null) {
            registration = (EditText) view
                    .findViewById(R.id.vehicle_registration);
        }
        return registration;
    }

    private VehicleManager getVehicleManager() {
        if (vehicleManager == null) {
            vehicleManager = new VehicleManager(getView());
        }
        return vehicleManager;
    }

    private Button getDoneButton() {
        if (doneButton == null) {
            doneButton = (Button) view.findViewById(R.id.button_done_vehicle);
        }
        return doneButton;
    }

    private void enableButton() {
        if ((getManufacturer().length() > 0) && (getModel().length() > 0)
                && (getRegistration().length() > 0)) {
            getDoneButton().setEnabled(true);
        } else {
            getDoneButton().setEnabled(false);
        }
    }

}