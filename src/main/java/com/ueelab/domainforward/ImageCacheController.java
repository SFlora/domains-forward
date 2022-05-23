package com.ueelab.domainforward;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;

/**
 * @author ueelab
 */
@Controller
public class ImageCacheController {
    
    @Value("${resources-path}")
    private String resourcesPath;
    
    @GetMapping("/")
    public Mono<Void> getPicture(ServerHttpRequest request, ServerHttpResponse response) {
        String[] hostSplit = request.getURI().getHost().split("\\.");
        String domain = hostSplit[hostSplit.length - 2] + '.' + hostSplit[hostSplit.length - 1];
        Path path = Path.of(resourcesPath + "/" + domain + ".png");
        Flux<DataBuffer> flux = DataBufferUtils.read(path, new DefaultDataBufferFactory(), StreamUtils.BUFFER_SIZE);
        return response.writeWith(flux).doOnNext(unused -> response.getHeaders().setContentType(MediaType.IMAGE_PNG));
    }
    
}
