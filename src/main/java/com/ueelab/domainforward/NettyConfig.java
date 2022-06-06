package com.ueelab.domainforward;

import lombok.SneakyThrows;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import javax.annotation.PostConstruct;
import java.net.URI;

@Configuration
public class NettyConfig {
    
    @Bean
    public ErrorWebExceptionHandler exceptionHandler() {
        return (exchange, ex) -> {
            exchange.getResponse().setStatusCode(HttpStatus.OK);
            return exchange.getResponse().setComplete();
        };
    }
    
    @PostConstruct
    public void startRedirectServer() {
        new NettyReactiveWebServerFactory(80).getWebServer((request, response) -> {
            URI uri = this.newUri(request.getURI());
            response.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
            response.getHeaders().setLocation(uri);
            return response.setComplete();
        }).start();
    }
    
    @SneakyThrows
    private URI newUri(URI uri) {
        return new URI("https",
                uri.getUserInfo(),
                uri.getHost(),
                443,
                uri.getPath(),
                uri.getQuery(),
                uri.getFragment());
    }
    
}
