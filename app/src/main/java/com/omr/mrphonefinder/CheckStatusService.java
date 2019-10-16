package com.omr.mrphonefinder;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;
import android.widget.Toast;

//servicio para check de estatus del dispositivo
public class CheckStatusService extends JobService {
    private static final String TAG = CheckStatusService.class.getSimpleName();
    private boolean jobCancelled = false;
    private static int contadorEjecucion = 1;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Job started");
        //para que se ejecuta el codigo le pasamos esta funcion que agregara otro hilo y no alente la interfaz de android
        //Toast.makeText(getApplicationContext(), "Star Job " + contadorEjecucion, Toast.LENGTH_LONG).show();
        doBackgroundWork(jobParameters);

        return true;
    }

    //servicio  que se esta ejecutando
    public void doBackgroundWork(final JobParameters params) {
        //creamos otro hilo para que ejecute la tarea
        Log.d(TAG,"LAUNCHING SERVICE");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"START SERVICIO");
                for (int i = 1; i < 15; i++) {
                    Log.d(TAG, "RUN(" + contadorEjecucion + "): " + i);
                    if (jobCancelled) {
                        return;
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Log.d(TAG, "WORK SERVICE FINESH");
                jobFinished(params, true);
                contadorEjecucion += 1;
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        //Toast.makeText(getApplicationContext(), "Stop Job", Toast.LENGTH_LONG).show();
        return true;
    }
}
