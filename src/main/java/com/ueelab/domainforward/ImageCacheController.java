package com.ueelab.domainforward;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author ueelab
 */
@Controller
public class ImageCacheController {
    
    @Value("${resources-path}")
    private String resourcesPath;
    
    @GetMapping("/")
    public void home(HttpServletRequest request, HttpServletResponse response) throws IOException {
        URL url = new URL(request.getRequestURL().toString());
        try (InputStream inputStream = Files.newInputStream(Path.of(resourcesPath + "/" + url.getHost() + ".png"));
             OutputStream outputStream = response.getOutputStream()) {
            inputStream.transferTo(outputStream);
            response.setContentType("image/png");
        }
    }
    
}
