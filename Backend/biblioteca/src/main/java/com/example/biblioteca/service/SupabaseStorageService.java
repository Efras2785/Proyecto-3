package com.example.biblioteca.service;

import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SupabaseStorageService {

    private final String SUPABASE_URL = "https://vrptrvtkgzkpetxzrclg.supabase.co"; 
    private final String SUPABASE_KEY = "sb_publishable_-N5zH_kZqy2Ap7okH9-Phg_V8hQieDR"; 
    private final String BUCKET_NAME = "portadas"; // Pon aquí el nombre de tu bucket

    public String uploadImage(MultipartFile file) {
        try {
            // 1. Generar un nombre único para que no se sobreescriban imágenes con el mismo nombre
            String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename().replace(" ", "_");
            
            // Esta es la ruta exacta de la API de Supabase para subir archivos
            String url = SUPABASE_URL + "/storage/v1/object/" + BUCKET_NAME + "/" + fileName;

            // 2. Preparamos las llaves y permisos
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + SUPABASE_KEY);
            headers.set("apikey", SUPABASE_KEY);
            headers.set("Content-Type", file.getContentType());

            HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);

            // 3. Disparamos la imagen hacia Supabase
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            // 4. Si todo sale bien, armamos y devolvemos el enlace público
            if (response.getStatusCode().is2xxSuccessful()) {
                return SUPABASE_URL + "/storage/v1/object/public/" + BUCKET_NAME + "/" + fileName;
            } else {
                throw new RuntimeException("Error de Supabase: " + response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Fallo al subir la imagen: " + e.getMessage());
        }
    }
}