package uk.org.mcdonnell.fuelaccount.view.purchase;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import uk.org.mcdonnell.fuelaccount.R;
import uk.org.mcdonnell.fuelaccount.data.PurchaseDataManager;
import uk.org.mcdonnell.fuelaccount.data.schemas.PurchaseType;
import uk.org.mcdonnell.fuelaccount.data.schemas.VehicleType;
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

public class PurchaseFragment extends Fragment {
	private View view;

	private EditText date;
	private EditText vehicle;
	private EditText station;
	private EditText volume;
	private EditText price;
	private EditText total;

	private Button doneButton;
	private ListView listView;

	private PurchaseDataManager purchaseManager;
	private PurchaseAdapter purchaseAdapter;

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
						PurchaseType purchaseType = new PurchaseType();
						purchaseType.setVehicle(getVehicle().getText()
								.toString());
						purchaseType.setStation(getStation().getText()
								.toString());
						purchaseType.setDate(getDate().getText().toString());
						purchaseType
								.setVolume(getVolume().getText().toString());
						purchaseType.setPrice(getPrice().getText().toString());
						purchaseType.setTotal(getTotal().getText().toString());
						getPurchaseManager().save(getView().getContext(),
								purchaseType);
						purchaseAdapter.notifyDataSetChanged();
						getVehicle().setText(null);
						getStation().setText(null);
						getDate().setText(null);
						getVolume().setText(null);
						getPrice().setText(null);
						getTotal().setText(null);
					} catch (Exception e) {
						Log.e(this.getClass().getName(),
								"Error occurred while saving.", e);
						Toast.makeText(activity, e.getMessage(),
								Toast.LENGTH_LONG).show();
					}
				}
			}
		});

		getVehicle().addTextChangedListener(new TextWatcher() {
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

		getStation().addTextChangedListener(new TextWatcher() {
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

		getDate().addTextChangedListener(new TextWatcher() {
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
				getVehicle().setText(
						((VehicleType) arg0.getItemAtPosition(arg2))
								.getManufacturer());
				getStation()
						.setText(
								((VehicleType) arg0.getItemAtPosition(arg2))
										.getModel());
				getDate().setText(
						((VehicleType) arg0.getItemAtPosition(arg2))
								.getRegistration());
			};
		});

		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				try {
					getPurchaseManager().deletePurchase(
							getView().getContext(),
							((VehicleType) arg0.getItemAtPosition(arg2))
									.getRegistration());
					purchaseAdapter.notifyDataSetChanged();
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
			purchaseAdapter = new PurchaseAdapter(this.getView().getContext(),
					R.layout.row,
					(ArrayList<PurchaseType>) getPurchaseManager()
							.getPurchase());
			ListView list = (ListView) getView().findViewById(
					R.id.listPurchases);
			list.setAdapter(purchaseAdapter);
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

	private EditText getDate() {
		if (date == null) {
			date = (EditText) view.findViewById(R.id.purchase_date);
		}
		return date;
	}

	private EditText getVehicle() {
		if (vehicle == null) {
			vehicle = (EditText) view.findViewById(R.id.purchase_list_vehicle);
		}
		return vehicle;
	}

	private EditText getStation() {
		if (station == null) {
			station = (EditText) view.findViewById(R.id.purchase_list_station);
		}
		return station;
	}

	private EditText getVolume() {
		if (volume == null) {
			volume = (EditText) view.findViewById(R.id.purchase_volume_litre);
		}
		return volume;
	}

	private EditText getPrice() {
		if (price == null) {
			price = (EditText) view
					.findViewById(R.id.purchase_price_by_volume_pence);
		}
		return price;
	}

	private EditText getTotal() {
		if (total == null) {
			total = (EditText) view.findViewById(R.id.purchase_price_total);
		}
		return total;
	}

	private PurchaseDataManager getPurchaseManager()
			throws FileNotFoundException {
		if (purchaseManager == null) {
			purchaseManager = new PurchaseDataManager(getView(),
					Configuration.getPurchasesFile());
		}
		return purchaseManager;
	}

	private Button getDoneButton() {
		if (doneButton == null) {
			doneButton = (Button) view.findViewById(R.id.button_save_vehicle);
		}
		return doneButton;
	}

	private ListView getListView() {
		if (listView == null) {
			listView = (ListView) view.findViewById(R.id.listVehicles);
		}
		return listView;
	}

	private void enableButton() {
		if ((getVehicle().length() > 0) && (getStation().length() > 0)
				&& (getDate().length() > 0) && (getVolume().length() > 0)
				&& (getPrice().length() > 0) && (getTotal().length() > 0)) {
			getDoneButton().setEnabled(true);
		} else {
			getDoneButton().setEnabled(false);
		}
	}

}