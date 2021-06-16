package com.ueelab.domainforward;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ueelab
 */
@Controller
@EnableScheduling
public class ImageCacheController {
    
    @Value("${resources-path}")
    private String resourcesPath;
    
    private Map<String, byte[]> imageByteMap = Collections.emptyMap();
    
    private static final byte[] EMPTY_BYTES = new byte[0];
    
    @GetMapping("/")
    public void home(HttpServletRequest request, HttpServletResponse response) throws IOException {
        URL url = new URL(request.getRequestURL().toString());
        byte[] imageBytes = imageByteMap.get(url.getHost() + ".png");
        if (imageBytes == null) {
            return;
        }
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
             OutputStream outputStream = response.getOutputStream()) {
            inputStream.transferTo(outputStream);
            response.setContentType("image/png");
        }
    }
    
    @Scheduled(fixedDelay = 1000 * 60)
    public void sync() throws IOException {
        this.imageByteMap = Files.list(Path.of(resourcesPath))
                .collect(Collectors.toMap(path -> path.getFileName().toString(), path -> {
                    try (InputStream inputStream = Files.newInputStream(path)) {
                        return inputStream.readAllBytes();
                    } catch (IOException e) {
                        return EMPTY_BYTES;
                    }
                }));
    }
    
}
