package uk.org.mcdonnell.fuelaccount.view.station;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import uk.org.mcdonnell.fuelaccount.R;
import uk.org.mcdonnell.fuelaccount.data.schemas.StationType;

class StationAdapter extends ArrayAdapter<StationType> {

    private ArrayList<StationType> items;

    public StationAdapter(Context context, int textViewResourceId,
            ArrayList<StationType> items) {
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
        StationType stationType = items.get(position);
        if (stationType != null) {
            TextView tt = (TextView) v.findViewById(R.id.toptext);
            TextView bt = (TextView) v.findViewById(R.id.bottomtext);
            if (tt != null) {
                tt.setText(String.format("%s (%s)",
                        stationType.getTitle(),
                        stationType.getCompany()));
            }
            if (bt != null) {
                bt.setText(stationType.getPostcode());
            }
        }
        return v;
    }
}
