package uk.org.mcdonnell.fuelaccount.view.station;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import uk.org.mcdonnell.fuelaccount.R;
import uk.org.mcdonnell.fuelaccount.data.schemas.PurchaseType;

class StationAdapter extends ArrayAdapter<PurchaseType> {

    private ArrayList<PurchaseType> items;

    public StationAdapter(Context context, int textViewResourceId,
            ArrayList<PurchaseType> items) {
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
        PurchaseType stationType = items.get(position);
        if (stationType != null) {
            TextView tt = (TextView) v.findViewById(R.id.toptext);
            TextView bt = (TextView) v.findViewById(R.id.bottomtext);
            if (tt != null) {
                tt.setText(String.format("%s (%s)",
                        stationType.getVehicle(),
                        stationType.getStation()));
            }
            if (bt != null) {
                bt.setText(stationType.getVolume());
            }
        }
        return v;
    }
}
