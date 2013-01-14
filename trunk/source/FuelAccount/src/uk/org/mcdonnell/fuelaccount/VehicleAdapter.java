package uk.org.mcdonnell.fuelaccount;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import uk.org.mcdonnell.fuelaccount.schemas.VehicleType;

class VehicleAdapter extends ArrayAdapter<VehicleType> {

    private ArrayList<VehicleType> items;

    public VehicleAdapter(Context context, int textViewResourceId,
            ArrayList<VehicleType> items) {
        super(context, textViewResourceId, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) super.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.row, null);
        }
        VehicleType vehicleType = items.get(position);
        if (vehicleType != null) {
            TextView tt = (TextView) v.findViewById(R.id.toptext);
            TextView bt = (TextView) v.findViewById(R.id.bottomtext);
            if (tt != null) {
                tt.setText("Name: "
                        + String.format("%s, %s",
                                vehicleType.getManufacturer(),
                                vehicleType.getModel()));
            }
            if (bt != null) {
                bt.setText("Status: " + vehicleType.getRegistration());
            }
        }
        return v;
    }
}
