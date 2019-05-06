package com.ht.dev.mspatient.models;

public class LoginDTO {
    String jeton;
    boolean connectOK;

    public String getJeton() {return jeton;}

    public void setJeton(String jeton) {this.jeton = jeton;}

    public boolean isConnectOK() {return connectOK;}

    public void setConnectOK(boolean connectOK) {this.connectOK = connectOK;}
    
}
