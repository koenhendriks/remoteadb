package com.chmeleon.remoteadbactivator;


import java.io.File;

public class Root {

    private static String LOG_TAG = Root.class.getName();

    public static boolean isDeviceRooted() {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
    }

    public static boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    public static boolean checkRootMethod2() {
        try {
            File file = new File("/system/app/Superuser.apk");
            return file.exists();
        } catch (Exception e){
            return false;
        }
    }

    public static boolean checkRootMethod3() {
        return new ShellCommand().executeCommand(ShellCommand.SHELL_CMD.check_su_binary)!=null;
    }
}


