package com.omr.mrphonefinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.job.JobScheduler;
import android.app.job.JobInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.ComponentName;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int ID_SERVICE = 5577;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG,"#NECESITA PERMISOS");

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.RECEIVE_SMS)) {
                Log.d(TAG,"#PERMISO OTORGADO");
                //Toast.makeText(this, "Permiso orotgdo", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECEIVE_SMS},10
                        );
                Toast.makeText(this,
                        "NO SE CONCEDIO PERMISOS, NO PODEMOS FUNCIONAR SIN ESTE",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void starJob(View v){
        ComponentName componentName = new ComponentName(getApplicationContext(), CheckStatusService.class);
        JobInfo info = null;
        //si la version de android es N
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            info = new JobInfo.Builder(ID_SERVICE, componentName)
                    .setRequiresCharging(false) //requiere que el dispositivo este cargando
                    .setRequiresDeviceIdle(false)
                    .setPersisted(true) //si se ejecutara constantemente
                    .setBackoffCriteria(6000,JobInfo.BACKOFF_POLICY_LINEAR)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY) //si esta conectado a la red
                    .build();
        }else {
            info = new JobInfo.Builder(ID_SERVICE, componentName)
                    .setRequiresCharging(false) //requiere que el dispositivo este cargando
                    .setRequiresDeviceIdle(false)
                    .setPersisted(true) //si se ejecutara constantemente
                    .setBackoffCriteria(6000,JobInfo.BACKOFF_POLICY_LINEAR)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY) //si esta conectado a la red
                    .setPeriodic(5 * 1000) //cada que tiempo se pretende ejecutar
                    .build();
        }


        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "* Job started");
        } else {
            Log.d(TAG, "* Job scheduling failed");
        }

        Log.d(TAG,"Fin btn");
    }

    public  void  stopJob(View v){
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(ID_SERVICE);
        Log.d(TAG,"stopJob");
    }

}
