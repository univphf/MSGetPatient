package com.ht.dev.mspatient.models;

/**
 * Mai 2017
 * @author tondeur-h
 */
public class Patient {
private String IPP;
private String NOM;
private String PRENOM;
private String DDN;
private String TEL;
private String EMAIL;


    
    public String getTEL() {
        return TEL;
    }

    public void setTEL(String TEL) {
        this.TEL = TEL;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }


    public String getNOM() {
        return NOM;
    }

    public void setNOM(String NOM) {
        this.NOM = NOM;
    }

    public String getPRENOM() {
        return PRENOM;
    }

    public void setPRENOM(String PRENOM) {
        this.PRENOM = PRENOM;
    }

    public String getDDN() {
        return DDN;
    }

    public void setDDN(String DDN) {
        this.DDN = DDN;
    }

    public String getIPP() {
        return IPP;
    }

    public void setIPP(String IPP) {
        this.IPP = IPP;
    }


   /*******************************************************
     * Convertir la date dans le bon format de requetage...
     * @param ddn
     * @return 
     *******************************************************/
    private String convert_ddn(String ddn) {
        return ddn.substring(0, 4)+ddn.substring(4, 6)+ddn.substring(6, 8);
    }

    
    public Patient() {
        //appeler la couche metier 
    }
    
}
