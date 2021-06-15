package com.ueelab.domainforward;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * @author ueelab
 */
@Controller
public class ForwardController {
    
    @Value("${resources-path}")
    private String resourcesPath;
    
    @GetMapping("/")
    public void home(HttpServletRequest request, HttpServletResponse response) throws IOException {
        URL url = new URL(request.getRequestURL().toString());
        File file = new File(resourcesPath + url.getHost() + ".png");
        if (!file.exists()) return;
        InputStream inputStream = new FileInputStream(file);
        OutputStream outputStream = response.getOutputStream();
        inputStream.transferTo(outputStream);
        response.setContentType("image/png");
    }
    
}
