package com.dacks.colorserver;

import android.graphics.Color;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.Random;

public class ColorChanger extends IColorChanger.Stub{
    @Override
    public int getColor() throws RemoteException {
        Random rnd = new Random();
        final int color = Color.argb(255, rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
        String TAG = "ColorChanger";
        Log.d(TAG, "server.getColor: " + color);
        return color;
    }
}
