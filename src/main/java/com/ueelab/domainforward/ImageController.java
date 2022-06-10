package com.ueelab.domainforward;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;

/**
 * @author ueelab
 */
@Controller
public class ImageController {
    
    @Value("${resources-path}")
    private String resourcesPath;
    
    @GetMapping("")
    public Mono<Void> getImage(ServerWebExchange exchange) {
        String[] hostSplit = exchange.getRequest().getURI().getHost().split("\\.");
        String domain = hostSplit[hostSplit.length - 2] + '.' + hostSplit[hostSplit.length - 1];
        Path path = Path.of(resourcesPath + "/" + domain + ".png");
        Flux<DataBuffer> dataBufferFlux = DataBufferUtils.read(path, new DefaultDataBufferFactory(), StreamUtils.BUFFER_SIZE);
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.IMAGE_PNG);
        return response.writeWith(dataBufferFlux);
    }
    
}
