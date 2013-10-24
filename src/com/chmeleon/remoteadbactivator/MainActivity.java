package com.chmeleon.remoteadbactivator;


import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import com.chmeleon.remoteadbactivator.ShellCommand;
import com.chmeleon.remoteadbactivator.Utils;


public class MainActivity extends Activity {

	@SuppressLint("UseValueOf")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);		
		String getPort = "getprop service.adb.tcp.port"; 
		Button btnStopADB = (Button) findViewById(R.id.btnStopADB);
		Button btnStartADB = (Button) findViewById(R.id.btnStartADB);
		TextView ipHolder = (TextView) findViewById(R.id.ipHolder);
		String IP = Utils.getIPAddress(true);
        boolean isRoot = Root.isDeviceRooted();
        TextView txtLog = (TextView) findViewById(R.id.txtLog);
		
		ipHolder.setText(IP);

        if(isRoot){

            checkPort();
            String running = ShellCommand.runRoot(getPort);
            if(running.equals("-1"))
            {
                btnStopADB.setEnabled(false);
            }
            else{
                btnStartADB.setEnabled(false);
            }
        }else{
            Toast.makeText(this, "Not rooted! You need a rooted device for this app.", Toast.LENGTH_LONG).show();
            btnStopADB.setEnabled(false);
            btnStartADB.setEnabled(false);
            txtLog.setText("You need to have ROOT access for this app!");
        }
	}
	
	public void checkPort()
	{
		TextView txtLog = (TextView) findViewById(R.id.txtLog);
		String getPort = "getprop service.adb.tcp.port";	
		String running = ShellCommand.runRoot(getPort);
		if(running.equals("-1"))
		{
			txtLog.setText("Remote ADB is not running");
		}else
		{
			txtLog.setText("Remote ADB running on port "+ running);
		}
	}
	
	public void startADB(View v)
    {
		Button btnStopAdb = (Button) findViewById(R.id.btnStopADB);
		Button btnStartAdb = (Button) findViewById(R.id.btnStartADB);
		EditText adbPort = (EditText) findViewById(R.id.adbPort);
		String setPortNumber = adbPort.getText().toString();
		String setPort = "setprop service.adb.tcp.port "+setPortNumber;
		String stopAdbd = "stop adbd";
		String startAdbd = "start adbd";
		
		if(setPortNumber.equals("")){
			Toast.makeText(this, "Please insert a port number!", Toast.LENGTH_LONG).show();
		}
		else
		{			
			ShellCommand.runRoot(setPort);
			ShellCommand.runRoot(stopAdbd);
			ShellCommand.runRoot(startAdbd);
			
	        Toast.makeText(this, "ADB remote started on port "+ setPortNumber, Toast.LENGTH_LONG).show();
	        
	        btnStopAdb.setEnabled(true);
	        btnStartAdb.setEnabled(false);
	        checkPort();
		}
    } 
	
	public void stopADB(View v)
    {		
		Button btnStopAdb = (Button) findViewById(R.id.btnStopADB);
		Button btnStartAdb = (Button) findViewById(R.id.btnStartADB);
		String setPortNumber = "-1";
		String setPort ="setprop service.adb.tcp.port "+setPortNumber;
		String stopAdbd = "stop adbd";
		String startAdbd = "start adbd";

		ShellCommand.runRoot(setPort);
		ShellCommand.runRoot(stopAdbd);
		ShellCommand.runRoot(startAdbd);
		
        Toast.makeText(this, "Adb remote stopped", Toast.LENGTH_LONG).show();
        
        btnStopAdb.setEnabled(false);
        btnStartAdb.setEnabled(true);
        checkPort();
    } 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
