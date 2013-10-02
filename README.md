# Remote ADB Activator

Easily control your android device over adb remotely! No more digging around in your desk drawer for your USB cable. Just connect to adb over WiFi. 

Remote ADB Activator let's you easily start and stop the remote adb on your device on your desired port.

## Getting Started

``` This app requires root access ```

To get started using ADB remotly just fill in your desired port number (default is 5555) and hit Start ADB.
ADB will now be running on the device. It will show the ip of the device on the top of the screen. 

Once you got remote adb running open up your terminal/cmd and connect adb to your ip.
You can do this with the following code assuming you run Remote adb on default port (I use 192.168.1.1 as my ip for example):

``` adb connect 192.168.1.1 ```

Now when you execute adb devices your device will be listed.

``` List of devices attached 
192.168.1.1:5555	device ```

Now you can start sending abd commands over WiFi

Special thanks to [cornedor](https://github.com/cornedor)
