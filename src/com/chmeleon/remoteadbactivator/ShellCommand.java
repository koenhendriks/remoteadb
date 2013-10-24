package com.chmeleon.remoteadbactivator;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 * @author corne, tassadar, chmeleon
 *
 */
public class ShellCommand {	
	
	/**
	* runRootCommand function from multiRom
	* https://github.com/Tasssadar/MultiROMMgr/blob/master/src/com/tassadar/multirommgr/MultiROMMgrActivity.java#L397
	* 
	* @author Tassadar
	*/
	 public static String runRoot(String command) {
	        Process proc = null;
	        OutputStreamWriter osw = null;
	        StringBuilder sbstdOut = new StringBuilder();

	        //Log.e(TAG, command);

	        try {

	            proc = Runtime.getRuntime().exec("su"); //Added -c for SuperSu not displaying the code on screen - Chmeleon
	            osw = new OutputStreamWriter(proc.getOutputStream());
	            osw.write(command);
	            osw.flush();
	            osw.close();
	        } catch (IOException ex) {
	            ex.printStackTrace();
	            return null;
	        } finally {
	            if (osw != null) {
	                try {
	                    osw.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                    return null;
	                }
	            }
	        }
	        try {
	            if (proc != null)
	                proc.waitFor();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	            return null;
	        }
	        
	        int read;
	        try {
	            while((read = proc.getInputStream().read()) != -1)
	                sbstdOut.append((char)read);
	            while((read = proc.getErrorStream().read()) != -1)
	                sbstdOut.append((char)read);
	        } catch (IOException e) { }
	        
	        String res = sbstdOut.toString();

	        // What the hell?!
	        res = res.replace("FIX ME! implement ttyname() bionic/libc/bionic/stubs.c:360", "");

	        if(res.equals("\n") || res.split("\n").length == 1)
	            res = res.replaceAll("\n", "");
	        proc.destroy();
	        return res;
	    }   
	 
	
	/**
	* run function from PerformanceTool
	* https://github.com/cornedor/performance-tool/blob/master/src/info/corne/performancetool/ShellCommand.java
	* 
	* @author Corne
	*/
	public static String run(String cmd[])
	{
		String res = "";
		try
		{
			Process process = new ProcessBuilder(cmd).start();
			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            try {
                process.waitFor();
            } catch (InterruptedException ex) {
                Logger.getLogger(ShellCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
            String line;
            while ((line = stderr.readLine()) != null) {
                res = res + "\n" + line;
            }
            while ((line = stdout.readLine()) != null) {
                res = res + "\n" + line;
                while ((line = stderr.readLine()) != null) {
                    res = res + "\n" + line;
                }
            }
		}
		catch(IOException e)
		{
			return "Error: " + e.getMessage();
		}
		return res;
	}
    private static String LOG_TAG = "LOGTAGROOT";

    public static enum SHELL_CMD {
        check_su_binary(new String[] { "/system/xbin/which", "su" });

        String[] command;

        SHELL_CMD(String[] command) {
            this.command = command;
        }
    }

    public ArrayList<String> executeCommand(SHELL_CMD shellCmd) {
        String line = null;
        ArrayList<String> fullResponse = new ArrayList<String>();
        Process localProcess = null;
        try {
            localProcess = Runtime.getRuntime().exec(shellCmd.command);
        } catch (Exception e) {
            return null;
        }
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                localProcess.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(
                localProcess.getInputStream()));
        try {
            while ((line = in.readLine()) != null) {
                Log.d(LOG_TAG, "--> Line received: " + line);
                fullResponse.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(LOG_TAG, "--> Full response was: " + fullResponse);
        return fullResponse;
    }
}