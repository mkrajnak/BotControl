package com.bot.control;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
 
public class FragmentControl extends Fragment {
 
    public static Fragment newInstance(Context context) {
    	FragmentControl f = new FragmentControl();
 
        return f;
    }
    	private Button forward;
    	private Button back;
    	private Button left;
    	private Button right;
    	private Button terminate;
    	private final String VLAVO="L";
    	private final String VPRAVO="R";
    	private final String VPRED="F";
    	private final String VZAD="B";
    	private final String LIGHTS_ON="1";
    	private final String LIGHTS_OFF="0";
    	private final String BLINKER="2";
    	private boolean blinkerActivity=false;
    	public static final String END="E";
    	private Communicator com;
    	public static TextView connectedDevice;
    	private Switch svetla;
    	private ImageButton blink;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_control, container,false);
       
        forward=(Button) root.findViewById(R.id.front);
        back=(Button) root.findViewById(R.id.back);
    	left=(Button) root.findViewById(R.id.left);
    	right=(Button) root.findViewById(R.id.right);
    	terminate=(Button) root.findViewById(R.id.button2);
    	connectedDevice = (TextView) root.findViewById(R.id.textView1);
    	com = (Communicator) getActivity();
    	svetla = (Switch) root.findViewById(R.id.lights);
    	blink = (ImageButton) root.findViewById(R.id.trojuholnik);

		forward.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				ConnectedThread sender = new ConnectedThread(MainActivity.connectThread.getMmSocket());
					sender.write(VPRED.getBytes());
			/*		BluetoothSocketListener bsl = new BluetoothSocketListener();
					Thread messageListener = new Thread(bsl);
					messageListener.start();*/
					if (event.getAction() == MotionEvent.ACTION_UP){
							sender.write(END.getBytes());
						}
					return false;
			}	
		});
		
			
		
		back.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				ConnectedThread sender = new ConnectedThread(MainActivity.connectThread.getMmSocket());
				sender.write(VZAD.getBytes());
				if (event.getAction() == MotionEvent.ACTION_UP){
							sender.write(END.getBytes());
				}
				return false;
			}
		});
		right.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				ConnectedThread sender = new ConnectedThread(MainActivity.connectThread.getMmSocket());
					sender.write(VPRAVO.getBytes());
					if (event.getAction() == MotionEvent.ACTION_UP){
							sender.write(END.getBytes());
					}
				return false;
			}
		});
		left.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				ConnectedThread sender = new ConnectedThread(MainActivity.connectThread.getMmSocket());
				sender.write(VLAVO.getBytes());
				if (event.getAction() == MotionEvent.ACTION_UP ){
							sender.write(END.getBytes());
					}
				return false;
			}
		});
		terminate.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.connectThread.cancel();
				MainActivity.connection_active = false;
				com.setDeviceName(null);
				com.fragmentControler(0);
			}
		});
		
	svetla.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			if(isChecked){
				ConnectedThread sender = new ConnectedThread(MainActivity.connectThread.getMmSocket());
				sender.write(LIGHTS_ON.getBytes());
			}
			else{
				ConnectedThread sender = new ConnectedThread(MainActivity.connectThread.getMmSocket());
				sender.write(LIGHTS_OFF.getBytes());
			}
		}
	});
	
	blink.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(!(blinkerActivity)){
				ConnectedThread sender = new ConnectedThread(MainActivity.connectThread.getMmSocket());
				sender.write(BLINKER.getBytes());
				blinkerActivity= true;
				disableControls();
			}
			else{
				ConnectedThread sender = new ConnectedThread(MainActivity.connectThread.getMmSocket());
				sender.write(END.getBytes());
				blinkerActivity = false;
				enableControls();
					if (svetla.isChecked()){
						sender.write(LIGHTS_ON.getBytes());
						}
					else {
						sender.write(LIGHTS_OFF.getBytes());
					}
			}
		}
	});
			return root;
	
    }
    
	
	public static String getEND() {
		return END;
	}
 
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(!(com.getDeviceName()==null)){
			connectedDevice.setText(com.getDeviceName());
			Log.d("ControlFragmnet", "device name received");
		}
		if(connectedDevice.getText() == getResources().getString(R.string.device)){
			Log.d("ControlFragmnet", "No connection active");
			MainActivity.connection_active = false;
			Toast.makeText(getActivity(), R.string.toast_not_connected, Toast.LENGTH_SHORT).show();
			disableControls();
		}
		else{
			Log.d("ControlFragmnet", "Connection to device established");
			MainActivity.connection_active = true;
			enableControls();
		}
	}
	public void enableControls(){
		forward.setEnabled(true);
		back.setEnabled(true);
		left.setEnabled(true);
		right.setEnabled(true);
		terminate.setEnabled(true);
		svetla.setEnabled(true);
		blink.setEnabled(true);
	}
	public void disableControls(){
		forward.setEnabled(false);
		back.setEnabled(false);
		left.setEnabled(false);
		right.setEnabled(false);
		terminate.setEnabled(false);
		svetla.setEnabled(false);
		if(MainActivity.connection_active == false){
				blink.setEnabled(false);
		}
	}
}	



























