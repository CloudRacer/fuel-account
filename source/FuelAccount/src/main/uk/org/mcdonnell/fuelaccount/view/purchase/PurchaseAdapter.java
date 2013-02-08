package uk.org.mcdonnell.fuelaccount.view.purchase;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import uk.org.mcdonnell.fuelaccount.R;
import uk.org.mcdonnell.fuelaccount.data.schemas.PurchaseType;

class PurchaseAdapter extends ArrayAdapter<PurchaseType> {

	private ArrayList<PurchaseType> items;

	public PurchaseAdapter(Context context, int textViewResourceId,
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
		PurchaseType purchaseType = items.get(position);
		if (purchaseType != null) {
			TextView tt = (TextView) v.findViewById(R.id.toptext);
			TextView bt = (TextView) v.findViewById(R.id.bottomtext);
			if (tt != null) {
				tt.setText(purchaseType.getDate());
			}
			if (bt != null) {
				bt.setText(String.format("%s*%s=%s", purchaseType.getVolume(),
						purchaseType.getPrice(), purchaseType.getTotal()));
			}
		}
		return v;
	}
}
