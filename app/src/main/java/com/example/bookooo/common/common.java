package com.example.bookooo.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.bookooo.Remote.APIService;
import com.example.bookooo.Remote.RetrofitClient;
import com.example.bookooo.User;

public class common {

    public static User currentUser;



    public static String PHONE_TEXT = "userPhone";

    private static final String BASE_URL = "https://fcm.googleapis.com/";

    public static APIService getFCMService()
    {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

    public static String convertCodeToStatus(String status) {
        if(status.equals("0"))
            return "Placed";
        else if(status.equals("1"))
            return "On My Way";
        else
            return "Shipped";
    }

    public static boolean isConnectedToInternet(Context context)
    {
        ConnectivityManager connectivityManager =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager!=null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info!=null)
            {
                for (int i=0;i<info.length;i++)
                {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
    public static final String DELETE="Delete";
    public static final String USR_KEY="User";
    public static final String PWD_KEY="Password";
}
