package com.ht.dev.mspatient.util;

/*********************
 * @author tondeur-h
 * @version mai 2015
 ********************/

import java.security.*;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***************************************************************************
 * Cette classe propose des méthodes permettant de crypter et décrypter des
 * messages avec l'algorithme Blowfish. La méthode main peut être appelée via
 * java -jar CHDemat.Blowfish %loginARNUM% %mdpARNUM% Cette appel génére un
 * fichier CHVDemat.key comtenant le login/mdp crypté pouvant être relu par
 * l'application CHVDemat.
 ***************************************************************************/
public class Blowfish {

    //logger 
    private static final Logger log = LoggerFactory.getLogger(Blowfish.class);

    
    public final static int KEY_SIZE = 128;  //taille de la clé de cryptage
    private Key secretKey;
    
    /*****************************************
     * constructeur par défaut met en place le
     * logger Fichier pour ecrire des
     * fichiers trace.
     ****************************************/
    public Blowfish() {}

    
    /************************************************
     * lire la clé secrete dans un objet de type Key
     *
     * @return Key
     ************************************************/
    public Key getSecretKey() {
        return secretKey;
    }

    /**************************************************************************
     * Retourne toutes les informations de la clé sous forme d'un tableau de
     * bytes. Elle peut ainsi être stockée puis reconstruite ultérieurement en
     * utilisant la méthode setSecretKey(byte[] keyData)
     *
     * @return byte
     **************************************************************************/
    public byte[] getSecretKeyInBytes() {
        return secretKey.getEncoded();
    }

    /******************************************************************************
     * Méthode qui permet la reconstruction de la clé secréte dans l'objet en
     * cours.
     *
     * @param secretKey Key
     *******************************************************************************/
    private void setSecretKey(Key secretKey) {
        this.secretKey = secretKey;
    }

    /**************************************************************************
     * Permet de reconstruire la clé secrète à partir de ses données, stockées
     * dans un tableau de bytes.
     *
     * @param keyData byte[]
     **************************************************************************/
    public void setSecretKey(byte[] keyData) {
        secretKey = new SecretKeySpec(keyData, "Blowfish");
    }

    /***************************************
     * Génére la clé secréte a partir d'une
     * instance KeyGenerator
     ***************************************/
    public void generateKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("Blowfish");
            keyGen.init(KEY_SIZE);
            secretKey = keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
             log.error("SecretKey..."+ e.getMessage());
        }
    }

    /**********************************************
     * Crypte la chaine de byte passé en parametre
     *
     * @param plaintext byte[]
     * @return byte[]
     **********************************************/
    private byte[] crypt(byte[] plaintext) {
        try {
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(plaintext);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            log.error("Erreur de cryptage Blowfish..."+e.getMessage());
        }
        return null;
    }

    /**********************************************
     * Crypte la chaine de caractéres passé en parametre
     *
     * @param plaintext String
     * @return byte[]
     **********************************************/
    private byte[] cryptToByte(String plaintext) {
        return crypt(plaintext.getBytes());
    }

      /**********************************************
     * Crypte la chaine de caractéres passé en parametre
     *
     * @param plaintext String
     * @return String
     **********************************************/
    public String cryptToStringB64(String plaintext) {
        return Base64.getEncoder().encodeToString(crypt(plaintext.getBytes()));
    }
    
    
     /**********************************************
     * Crypte la chaine de caractéres passé en parametre
     *
     * @param plaintext String
     * @return String
     **********************************************/
    public String cryptToStringB64(byte[] plaintext) {
        return Base64.getEncoder().encodeToString(crypt(plaintext));
    }
    
    /**********************************************
     * deCrypte la chaine de byte passé en parametre
     *
     * @param ciphertext []
     * @return byte[]
     **********************************************/
    private byte[] decryptInBytes(byte[] ciphertext) {
        try {
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(ciphertext);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            log.error("Blowfish decryptage in Bytes..."+e.getMessage());
        }
        return null;
    }

    /**********************************************
     * Crypte la chaine de caractéres passé en parametre
     *
     * @param ciphertextB64
     * @return byte[]
     **********************************************/
    public String decryptInString(String ciphertextB64) {
        return new String(decryptInBytes(Base64.getDecoder().decode(ciphertextB64)));
    }

} //fin de la classe