package com.omr.mrphonefinder;

public class Commander {
    public final String cmdBloked = "#MR:BLOQUEA#";
    public short readSmsToCmd(String sms){
        try {
            if(sms.contains(cmdBloked)){
                return 1; //bloquea el celular
            }
        }catch (Exception ex){
            throw ex;
        }
        return 0; //no realices nada
    }

}
