package servicios;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtils {

    private static final String ALGORITMO = "AES";

    private static SecretKey obtenerClaveSecreta() throws Exception {
        // Hardcodear la clave secreta para fines de desarrollo
        String claveEnString = "GxjQ6KfhWvRjU1ziqmYHiw=="; // Reemplaza con tu clave generada
        return convertirStringAClave(claveEnString);
    }

    // Métodos de encriptar y desencriptar permanecen iguales
    public static String encriptar(String datos) throws Exception {
        SecretKey claveSecreta = obtenerClaveSecreta();
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        cipher.init(Cipher.ENCRYPT_MODE, claveSecreta);
        byte[] datosEncriptados = cipher.doFinal(datos.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(datosEncriptados);
    }

    public static String desencriptar(String datosEncriptados) throws Exception {
        SecretKey claveSecreta = obtenerClaveSecreta();
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        cipher.init(Cipher.DECRYPT_MODE, claveSecreta);
        byte[] datosDecodificados = Base64.getDecoder().decode(datosEncriptados);
        byte[] datosDesencriptados = cipher.doFinal(datosDecodificados);
        return new String(datosDesencriptados, "UTF-8");
    }

    // Métodos auxiliares para convertir la clave
    public static String convertirClaveAString(SecretKey claveSecreta) {
        return Base64.getEncoder().encodeToString(claveSecreta.getEncoded());
    }

    public static SecretKey convertirStringAClave(String claveEnString) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(claveEnString);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITMO);
    }
}
