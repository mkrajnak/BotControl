package com.bot.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.bluetooth.BluetoothSocket;
import android.util.Log;



class ConnectedThread extends Thread {
   
	private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    public String message;
    public int bytes;
 
    public ConnectedThread(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
 
        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }
 
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }
 
  

    /* sending data */
    public void write(byte[] bytes) {
    	
        try {
            mmOutStream.write(bytes);
        
        } catch (IOException e) { }
    }
 
    /* teminating connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }

   


	
}
