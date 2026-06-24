package com.financetracker.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseConfig {
    public FirebaseConfig(
            @Value("${app.firebase.enabled}") boolean enabled,
            @Value("${app.firebase.service-account}") String serviceAccountJson) throws Exception {
        if (enabled && FirebaseApp.getApps().isEmpty() && !serviceAccountJson.isBlank()) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(
                    new ByteArrayInputStream(serviceAccountJson.getBytes(StandardCharsets.UTF_8)));
            FirebaseApp.initializeApp(FirebaseOptions.builder().setCredentials(credentials).build());
        }
    }
}
