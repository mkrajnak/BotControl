package com.bot.control;



import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentSettings extends Fragment{
	
	private SeekBar speedController;
	private final int maxSpeed= 115;
	private final int maxDelay= 999;
	private final String setter = "S";
	private final String delayFlag="3";
	private SeekBar delayController;
	private TextView speed;
	private TextView delay;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_settings, container, false);
		speed = (TextView) view.findViewById(R.id.speed);
		delay=	(TextView) view.findViewById(R.id.delay);
		
		speedController = (SeekBar) view.findViewById(R.id.speed_controll);
		speedController.setMax(maxSpeed);
		
		delayController = (SeekBar) view.findViewById(R.id.delay_controll);
		delayController.setMax(maxDelay);
		
		speedController.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int changed=0;
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				changed +=140;
				speed.setText("Speed (" +changed +")");
				ConnectedThread speed = new ConnectedThread(MainActivity.connectThread.getMmSocket());
				speed.write(setter.getBytes());
				speed.write((String.valueOf(changed)).getBytes());
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				changed = progress;
			}
		});
		
		delayController.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int changed=0;
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				delay.setText("Light delay (" +changed+")");
				ConnectedThread delay = new ConnectedThread(MainActivity.connectThread.getMmSocket());
				delay.write(delayFlag.getBytes());
				delay.write((String.valueOf(changed)).getBytes());
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				changed = progress;
			}
		});
		return view;
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getActivity());
		Editor edit = sh.edit();
		int speedProgress = speedController.getProgress();
		int delayProgress = delayController.getProgress();
		edit.putInt("speedProgress", speedProgress).commit();
		edit.putInt("delayProgress", delayProgress).commit();
		
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getActivity());
		int speedProgress = sh.getInt("speedProgress", 0);
		int delayProgress = sh.getInt("delayProgress", 0);
		speedController.setProgress(speedProgress);
		delayController.setProgress(delayProgress);
		delay.setText("Light delay (" +delayProgress+")");
		speed.setText("Speed (" +speedProgress+")");
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
			if(MainActivity.connection_active){
				speedController.setEnabled(true);
				delayController.setEnabled(true);
			}
			else{
				speedController.setEnabled(false);
				delayController.setEnabled(false);
				Log.d("Bluetooth","ROAR");
				}
		}
}


