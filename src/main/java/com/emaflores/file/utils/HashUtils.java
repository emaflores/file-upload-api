package com.emaflores.file.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {

    /**
     * Método que calcula el hash de un array de bytes utilizando un algoritmo especificado
     * @param bytes Array de bytes que se desea calcular su hash
     * @param algorithm Algoritmo de hash que se desea utilizar (por ejemplo "SHA-256")
     * @return El hash en formato hexadecimal
     * @throws NoSuchAlgorithmException Si no se encuentra el algoritmo de hash especificado
     */
    public static String hash(byte[] bytes, String algorithm) throws NoSuchAlgorithmException {
        // Se obtiene una instancia de MessageDigest con el algoritmo especificado
        MessageDigest md = MessageDigest.getInstance(algorithm);
        // Se actualiza el digest con los bytes de entrada
        md.update(bytes);
        // Se calcula el hash final
        byte[] digest = md.digest();
        // Se convierte el hash a formato hexadecimal
        return bytesToHex(digest);
    }

    /**
     * Método auxiliar que convierte un array de bytes en una cadena hexadecimal
     * @param bytes Array de bytes que se desea convertir a formato hexadecimal
     * @return La cadena hexadecimal
     */
    private static String bytesToHex(byte[] bytes) {
        // Se crea un StringBuilder para ir construyendo la cadena hexadecimal
        StringBuilder sb = new StringBuilder();
        // Se recorre el array de bytes
        for (byte b : bytes) {
            // Se convierte cada byte a su representación hexadecimal y se agrega al StringBuilder
            sb.append(String.format("%02x", b));
        }
        // Se devuelve la cadena hexadecimal resultante
        return sb.toString();
    }
}


