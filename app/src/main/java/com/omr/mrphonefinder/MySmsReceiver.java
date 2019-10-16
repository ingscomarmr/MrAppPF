package com.omr.mrphonefinder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class MySmsReceiver extends BroadcastReceiver {

    private static final String TAG = "MySmsReceiver";
    public static final String pdu_type = "pdus";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"#INICIAR EL RECIBER CON EL SMS QUE LLEGO");

        /* PARA OBTENER PERMISOS
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG,"#NECESITA EL PERMISO RECEIVE_SMS");

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(null,Manifest.permission.RECEIVE_SMS)) {
                Log.d(TAG,"#PERMISO OTORGADO");
                //Toast.makeText(context, "Permiso orotgdo", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(null,
                        new String[]{Manifest.permission.RECEIVE_SMS},10
                );
                Toast.makeText(context, "NO PODEMOS TRABAJAR SIN PERMISOS", Toast.LENGTH_LONG).show();
            }
        }*/


        if(intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION) && intent.getExtras() != null) {
            Log.d(TAG, "#LEER MENSAJES");
            //Toast.makeText(context, "#SMS recivido", Toast.LENGTH_LONG).show();
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs;
            String strMessage = "";
            String format = bundle.getString("format");
            // Retrieve the SMS message received.
            Object[] pdus = (Object[]) bundle.get(pdu_type);
            if (pdus != null) {
                // Check the Android version.
                boolean isVersionM = (Build.VERSION.SDK_INT >=
                        Build.VERSION_CODES.M);
                msgs = new SmsMessage[pdus.length];
                for (int i = 0; i < msgs.length; i++) {
                    // Check Android version and use appropriate createFromPdu.
                    if (isVersionM) {
                        // If Android version M or newer:
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                    } else {
                        // If Android version L or older:
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }

                    // Build the message to show.
                    String body = msgs[i].getMessageBody();
                    strMessage += "SMS DE " + msgs[i].getOriginatingAddress();
                    strMessage += " : " + body + "\n";

                    // Log and display the SMS message.

                    switch (new Commander().readSmsToCmd(body.toUpperCase())){
                        case 1:
                            Toast.makeText(context, body, Toast.LENGTH_LONG).show();
                            break;
                            default:
                                Log.d(TAG, "# procesar sms : " + strMessage);
                                break;
                    }

                }
            }
        }
    }
}
