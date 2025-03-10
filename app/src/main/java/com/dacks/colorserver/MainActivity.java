package com.dacks.colorserver;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    //final ColorChangerService service = new ColorChangerService();
    //YOU TUBE
    //https://www.youtube.com/watch?v=JxmozhBzzcg&t=67s
    IColorChanger colorService;
    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            colorService = IColorChanger.Stub.asInterface(service);
            Log.d("ColorChangerService.Connection", "Remote config service is connected!");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            colorService = null;
        }
    };

    private boolean serviceIsRunning;
    private boolean serviceBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> {
            if (!serviceIsRunning) {
                Intent intent = new Intent(getApplicationContext(), ColorChangerService.class);
                intent.setAction("START_SERVICE");
                //startService(intent);
                //adb shell dumpsys activity services com.dacks
                startForegroundService(intent);
                serviceIsRunning = true;
            }
        });

        Button colorButton = findViewById(R.id.colorButton);
        colorButton.setOnClickListener(v -> {
            if (!serviceBound) {
                Intent bindIntent = new Intent("com.dacks.colorserver.IColorChanger");
                bindIntent.setPackage("com.dacks.colorserver");
                bindService(bindIntent, mConnection, Context.BIND_AUTO_CREATE);
                serviceBound = true;
            }
            if (colorService != null) {
                try {
                    v.setBackgroundColor(colorService.getColor());
                }
                catch (RemoteException rex) {
                    //throw new RuntimeException(e);
                    Log.d("RemoteException", Objects.requireNonNull(rex.getMessage()));
                }
            }

        });

        Button stopButton = findViewById(R.id.stopButton);
        stopButton.setOnClickListener(v -> {
            if (serviceBound) {
                unbindService(mConnection);
                serviceBound = false;
            }
            if (serviceIsRunning) {
                Intent intent = new Intent(getApplicationContext(), ColorChangerService.class);
                intent.setAction("STOP_SERVICE");
                // startService(intent);
                stopService(intent);
                serviceIsRunning = false;
            }

        });
    }
}