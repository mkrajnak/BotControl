package com.bot.control;


import java.util.ArrayList;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
 
public class FragmentConnect extends Fragment {
 
//	public static Fragment newInstance(Context context) {
//		FragmentConnect f = new FragmentConnect();
//
//		return f;
//	}
	private BluetoothDevice connect;
	private ConnectThread connection;
	private final int REQUEST_ENABLE_BT = 1;
	private Button get;
	private BluetoothAdapter bluetooth;
	private ListView list;
	private Communicator com;
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_connect, container,false);
        get = (Button) root.findViewById(R.id.get_devices);
        list = (ListView) root.findViewById(R.id.listView1);
        bluetooth = BluetoothAdapter.getDefaultAdapter();
        com= (Communicator) getActivity();
        
        
   	 bluetooth = BluetoothAdapter.getDefaultAdapter();
		if (bluetooth == null){
			// check if device support BT
			Toast.makeText(getActivity(), R.string.toast_bt_support, Toast.LENGTH_SHORT).show();
		}
		
		
		
		
        get.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				list.setEnabled(true);
				list.setVisibility(View.VISIBLE);
				ArrayList<String> arrayListpaired = new ArrayList<String>();
				ArrayAdapter<String> arrayAdapter =new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, arrayListpaired);
				Set<BluetoothDevice> pairedDevices = bluetooth.getBondedDevices();
				// If there are paired devices
				if (pairedDevices.size() > 0) {
				    // Loop through paired devices
				    for (BluetoothDevice device : pairedDevices) {
						// Add the name and address to an array adapter to show in a ListView
				    		arrayListpaired.add(device.getName()+"\n"+device.getAddress());
				    		list.setAdapter(arrayAdapter);
				    	  }	
				}
				
			}
		});
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				// TODO Auto-generated method stub
				String temp = (String) list.getItemAtPosition(arg2);
				String connect_address= temp.substring(temp.length()-17);
				connect = bluetooth.getRemoteDevice(connect_address);
				Toast.makeText(getActivity(), R.string.toast_connection_initiate +temp, Toast.LENGTH_SHORT).show();
				if (!(MainActivity.connectThread == null)){
					MainActivity.connectThread.cancel();
				}
				MainActivity.connectThread = new ConnectThread(connect);
				if(MainActivity.connectThread.makeConnection()){
						Toast.makeText(getActivity(),R.string.toast_connection_success, Toast.LENGTH_SHORT).show();
						com.setDeviceName(temp);		
						com.fragmentControler(1);
						Log.d("ConnectionFragment", "Connection success, calling mainActivity");
						}
				else {
						Toast.makeText(getActivity(), R.string.toast_connection_fail, Toast.LENGTH_SHORT).show();

				}
			}
		});
        
        return root;
    }
    
    
    @Override
    	public void onStart() {
    		// TODO Auto-generated method stub
    		super.onStart();
    		
    		if (!bluetooth.isEnabled()){
    			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    		}
//    		else {
//    			Toast.makeText(getActivity(), R.string.toast_bt_enabled, Toast.LENGTH_SHORT).show();
//    		}
    	}
 
}