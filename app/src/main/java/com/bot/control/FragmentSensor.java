package com.bot.control;


import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
 
public class FragmentSensor extends Fragment {
 
    public static Fragment newInstance(Context context) {
    	FragmentSensor f = new FragmentSensor();
 
        return f;
    }
    private final String M = "M"; 
	private final String A = "A"; 
	private final String END = "E"; 
	private ToggleButton sensorController;
	private String data="0";
	private TextView receivedData;
	private Button meraj;
	Handler h = new Handler();
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_sensor, container, false);

		
        sensorController = (ToggleButton) root.findViewById(R.id.sensor);
        meraj = (Button) root.findViewById(R.id.meraj);
        sensorController.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
			if(isChecked){
				ConnectedThread sender = new ConnectedThread(MainActivity.connectThread.getMmSocket());
				sender.write(A.getBytes());
				
			}
			else{
				ConnectedThread sender = new ConnectedThread(MainActivity.connectThread.getMmSocket());
				sender.write(END.getBytes());
				
			}
				
				
			}
		});
        
        meraj.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ConnectedThread sender = new ConnectedThread(MainActivity.connectThread.getMmSocket());
				sender.write(M.getBytes());
				BluetoothSocketListener bsl = new BluetoothSocketListener();
				bsl.run();
				
				
				}
		});
        
        
        
        return root;
        
    }
    @Override
    public void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	if(MainActivity.connection_active){
    		sensorController.setEnabled(true);
    		meraj.setEnabled(true);
    	}
    	else {
    			sensorController.setEnabled(false);		
    			meraj.setEnabled(false);
    	}
    }
    
    public class BluetoothSocketListener implements Runnable{
    	
    	private String message;
    	
    	public void run() {
    	int bufferSize = 1024;
    	byte[] buffer = new byte[bufferSize];
    	try {
    		InputStream instream = MainActivity.connectThread.getMmSocket().getInputStream();
    		int bytesRead = -1;
    		message = "";
    		while (MainActivity.connectThread.getMmSocket().getInputStream().available() > 0) {
    			message= "";
    			bytesRead = instream.read(buffer);
    			if (bytesRead != 0) {
    				while ((bytesRead==bufferSize)&&(buffer[bufferSize-1] != 0)) {
    					message = message + new String(buffer, 0, bytesRead);
    					Log.d("in", message);
    					bytesRead = instream.read(buffer);
    				}
    			message = message + new String(buffer, 0, bytesRead -1);
    			Log.d("bluetooth listener", message);
    			Toast.makeText(getActivity(), "Distance  " + message +" cm", Toast.LENGTH_LONG).show();
    			MainActivity.connectThread.getMmSocket().getInputStream();
    			
    			}
    		}
    	
    	} catch (IOException e) {
    		Log.d("BLUETOOTH_COMMS", e.getMessage());
    	}
  }
    	
 }
    }
    
    
 