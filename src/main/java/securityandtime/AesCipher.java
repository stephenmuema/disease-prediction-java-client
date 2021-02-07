package securityandtime;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public class AesCipher {

    private static final String SECRET_KEY_1 = "ssdkF$HUy2A#D%kdssdkF$HUy2A#D%kd";
    private static final String SECRET_KEY_2 = "weJiSEvR5yAC5ftBweJiSEvR5yAC5ftB";
    private static String CIPHER_NAME = "AES/CBC/PKCS5PADDING";
    private static int CIPHER_KEY_LEN = 32; //256 bits
    private IvParameterSpec ivParameterSpec;
    private SecretKeySpec secretKeySpec;
    private Cipher cipher;

    /**
     * Encrypt data using AES Cipher (CBC) using 256 bit key
     * key  - key to use should be 32 bytes long (256 bits)
     * iv   - initialization vector
     * data - data to encrypt
     * return encryptedData data in base64 encoding with iv attached at end after a :
     */


    public AesCipher() throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException {
        ivParameterSpec = new IvParameterSpec(SECRET_KEY_1.getBytes(StandardCharsets.UTF_8));
        secretKeySpec = new SecretKeySpec(SECRET_KEY_2.getBytes(StandardCharsets.UTF_8), "AES");
        cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    }

    public static String encrypt(String key, String iv, String data) {

        try {
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKey = new SecretKeySpec(fixKey(key).getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance(AesCipher.CIPHER_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

            byte[] encryptedData = cipher.doFinal((data.getBytes()));

            String encryptedDataInBase64 = Base64.getEncoder().encodeToString(encryptedData);
            String ivInBase64 = Base64.getEncoder().encodeToString(iv.getBytes(StandardCharsets.UTF_8));

            return encryptedDataInBase64 + ":" + ivInBase64;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static String fixKey(String key) {

        if (key.length() < AesCipher.CIPHER_KEY_LEN) {
            int numPad = AesCipher.CIPHER_KEY_LEN - key.length();

            StringBuilder keyBuilder = new StringBuilder(key);
            for (int i = 0; i < numPad; i++) {
                keyBuilder.append("0"); //0 pad to len 16 bytes
            }
            key = keyBuilder.toString();

            return key;

        }

        if (key.length() > AesCipher.CIPHER_KEY_LEN) {
            return key.substring(0, CIPHER_KEY_LEN); //truncate to 16 bytes
        }

        return key;
    }

    /**
     * Decrypt data using AES Cipher (CBC) with 128 bit key
     * <p>
     * key  - key to use should be 16 bytes long (128 bits)
     * data - encrypted data with iv at the end separate by :
     * decrypted data string
     */

    public static String decrypt(String key, String data) {

        byte[] original = new byte[0];
        try {
            String[] parts = data.split(":");

            IvParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode(parts[1]));
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance(AesCipher.CIPHER_NAME);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

            byte[] decodedEncryptedData = Base64.getDecoder().decode(parts[0]);

            original = cipher.doFinal(decodedEncryptedData);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new String(original);
    }


}