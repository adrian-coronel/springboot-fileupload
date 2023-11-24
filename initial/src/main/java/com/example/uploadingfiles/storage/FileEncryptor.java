package com.example.uploadingfiles.storage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class FileEncryptor {

   public static String encryptFileName(String originalFileName){
      try {
         // Generar un UUID único
         String uuid = UUID.randomUUID().toString();

         // Aplicar SHA-256 al nombre original
         MessageDigest md = MessageDigest.getInstance("SHA-256");
         byte[] hashBytes = md.digest( // Genera el array de bytes del HASH con relacion al array de bytes del nombre original
             originalFileName.getBytes() //Obtiene el array de bytes del nombre original
         );

         // Convierte los bytes del hash a su representación hexadecimal.
         StringBuilder hashStringBuilder = new StringBuilder();
         for (byte b : hashBytes) {
            // Cada byte se convierte a dos dígitos hexadecimales.
            hashStringBuilder.append(String.format("%02x", b));
         }
         String hash = hashStringBuilder.toString();

         // Combinar UUID y hash para formar el nuevo nombre cifrado
         return uuid + "_" + hash + ".wzt";
      } catch (NoSuchAlgorithmException e) {
         throw new RuntimeException("Error al cifrar el nombre del archivo", e);
      }
   }

}
