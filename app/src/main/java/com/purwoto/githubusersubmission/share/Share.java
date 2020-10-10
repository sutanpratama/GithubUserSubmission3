package com.purwoto.githubusersubmission.share;

import android.content.Context;
import android.content.pm.PackageManager;

public class Share {

    public static boolean isAppAvailable(Context context,String appName){
        PackageManager packageManager = context.getPackageManager();
        try{
            packageManager.getPackageInfo(appName, 0);
            return true;
        }catch(PackageManager.NameNotFoundException e){
            return false;
        }
    }

}
