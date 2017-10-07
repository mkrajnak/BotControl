package com.bot.control;



import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnDrawListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements Communicator {
	
	public static String received = "0";
	public static boolean connection_active = false;
	private final String[] data ={"Connect","Control","Sensor","Settings","About"};
	private final String[] fragments ={
			"com.bot.control.FragmentConnect",
			"com.bot.control.FragmentControl",
			"com.bot.control.FragmentSensor",
			"com.bot.control.FragmentSettings",
			"com.bot.control.FragmentAbout"};
	private int [] icons = {R.drawable.connect,
							R.drawable.control,
							R.drawable.sensor,
							R.drawable.settings,
							R.drawable.about};
	public static ConnectThread connectThread;
	private String deviceName = null;
	private ActionBarDrawerToggle iconSwitcher;
	private DrawerLayout drawer;
	private ListView navList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_main);
		
		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActionBar().getThemedContext(), android.R.layout.simple_list_item_1, data);
		 
		 drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
		 navList = (ListView) findViewById(R.id.drawer);
		 CustomAdapter adapter = new CustomAdapter(this, data, icons);
		 navList.setAdapter(adapter);
			
		 navList.setOnItemClickListener(new OnItemClickListener(){ // changing fragment adter chooding from menu in drawer
		         @Override
		         public void onItemClick(AdapterView<?> parent, View view, final int pos,long id){
		        	 
		                 drawer.setDrawerListener( new DrawerLayout.SimpleDrawerListener(){
		                         @Override
		                         public void onDrawerClosed(View drawerView){
		                                 super.onDrawerClosed(drawerView);
		                                 Log.d("drawerOpener", "Drawer is opened");
		                                 FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
		                                 tx.replace(R.id.main, Fragment.instantiate(MainActivity.this, fragments[pos]));
		                                 tx.commit();
		                         }
		                       
		                 });
		                 drawer.closeDrawer(navList);
		                 
		              
		                 
		         }
		 });
		 FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
		 tx.replace(R.id.main,Fragment.instantiate(MainActivity.this, fragments[0]));
		 tx.commit();
		 
		 getActionBar().setDisplayHomeAsUpEnabled(true);
		 getActionBar().setHomeButtonEnabled(true);
		 
	
		 iconSwitcher = new ActionBarDrawerToggle(this, drawer, R.drawable.launcher, R.string.open_drawer, R.string.close_drawer){ // NavDrawer will appear after taping app menu icon 
	            public void onDrawerClosed(View view) {
	                getActionBar().setTitle(getTitle());
	                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
	            }

	            public void onDrawerOpened(View drawerView) {
	                getActionBar().setTitle(getTitle());
	                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
	            }
	        };
		 drawer.setDrawerListener(iconSwitcher);
		 
		
		 drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() { // draver bug fix
			 @Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				// TODO Auto-generated method stub
				super.onDrawerSlide(drawerView, slideOffset);
				if(MainActivity.connection_active){
					ConnectedThread canceler = new ConnectedThread(connectThread.getMmSocket());
					canceler.write(FragmentControl.END.getBytes());
					Log.d("DRAWER", "ENDING control sequence");
				}
				else {
					Log.d("DRAWER", "Slide without ending sequence");
				}
			}
		});
		 
	}
	
	class CustomAdapter extends ArrayAdapter<String> { // Custom adapter for navigation drawer with icons
	    int size = 1;
	    Context context;
	    int[] images;
	    String[] titleArray;

	    CustomAdapter(Context c, String[] titles, int imgs[]) {
	        super(c, R.layout.custom_menu, R.id.item, titles);
	        this.context = c;
	        this.images = imgs;
	        this.titleArray = titles;
	    }
	    public View getView(int position, View convertView, ViewGroup parent) {
	        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        View row = inflater.inflate(R.layout.custom_menu, parent, false);
	        ImageView myImage = (ImageView) row.findViewById(R.id.icon);
	        myImage.setImageResource(images[position]);
	        TextView myTitle = (TextView) row.findViewById(R.id.item);
	        myTitle.setText(titleArray[position]);
	        return row;
	    }

	}
	
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.main, menu);
	        return super.onCreateOptionsMenu(menu);
	    }
	 
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	         // The action bar home/up action should open or close the drawer.
	         // ActionBarDrawerToggle will take care of this.
	        if (iconSwitcher.onOptionsItemSelected(item)) {
	            return true;
	        }
	        return super.onOptionsItemSelected(item);
	       
	    }

	@Override
	public void fragmentControler(int fragment) { // methods used for communication between fragments, passing name and mac address of devices from connect fragment to control fragment
		// TODO Auto-generated method stub
		FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.main, Fragment.instantiate(MainActivity.this, fragments[fragment]));
        tx.commit();
        Log.d("Main", "Fragment exchange success");
	}

	@Override
	public String getDeviceName() {
		// TODO Auto-generated method stub
		return deviceName;
	}

	@Override
	public void setDeviceName(String device) {
		// TODO Auto-generated method stub
		this.deviceName=device;
	}


}






























	
