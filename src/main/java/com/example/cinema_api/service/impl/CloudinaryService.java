package com.example.cinema_api.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    @Value("${cloudinary.cloud.name}")
    private String cloudName;
    @Value("${cloudinary.api.key}")
    private String apiKey;
    @Value("${cloudinary.api.secret}")
    private String apiSecret;

    private Cloudinary cloudinary;

    // @PostConstruct hace que se ejecute automáticamente una sola vez,
    // justo después de que Spring termine de inyectar los @Value de arriba.
    // Lo necesitamos porque el objeto Cloudinary no se puede construir en el momento
    // en que se declara el campo (los @Value todavía no tienen valor en ese punto).
    @PostConstruct
    public void init() {
        Map config = new HashMap();
        config.put("cloud_name", cloudName); //a config le guardo el valor de clouName en cloud_name
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        cloudinary = new Cloudinary(config);
    }

    public String uploadImage(MultipartFile file) {
        try {
            // file.getBytes() convierte el archivo recibido en el request a un arreglo de bytes,
            // que es lo que la librería de Cloudinary necesita para subirlo.
            // ObjectUtils.asMap("resource_type", "auto") le dice a Cloudinary que detecte
            // automáticamente el tipo de archivo (imagen, video, etc.) en vez de asumir uno fijo.
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),ObjectUtils.asMap("resource_type", "auto"));
            // uploadResult es la respuesta de Cloudinary con toda la info del archivo subido
            // (id, formato, tamaño, urls, etc.). De ahí sacamos "secure_url" (la URL https)
            // para guardarla en nuestra base de datos.
            return (String)  uploadResult.get("secure_url");
        }catch (IOException e){
            // getBytes() y upload() pueden fallar por temas de red o de lectura del archivo.
            // Envolvemos el error original en una RuntimeException para no forzar a quien
            // llama a este paraa manejar una excepción checked.
            throw new RuntimeException("Failed to upload image", e);
        }
    }

}
