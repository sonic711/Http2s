package com.example.http2s.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;



@Configuration
@Slf4j
public class ClientConfiguration {

    // 檔案注入證書 1
    @Bean
    @Qualifier("clientBob")
    @ConfigurationProperties("rest.ssl.client.bob")
    public ClientKeyStoreInfo clientBob() {
        return new ClientKeyStoreInfo();
    }

    // 檔案注入證書 2
    @Bean(name = "SSL")
    public RestTemplate myRestTemplate(RestTemplateBuilder builder) {
        log.debug("TEST RESTTMEPLATE");
        ClientKeyStoreInfo client = clientBob();

        HttpComponentsClientHttpRequestFactory customRequestFactory = new HttpComponentsClientHttpRequestFactory();
        try {
            //SSL Context 是 JDK 中的东西
            final SSLContext sslContext = new SSLContextBuilder()
                    //.setKeyStoreType("jks")
                    .setKeyStoreType("pkcs12") // 说明 keystore 的组织格式
                    .loadKeyMaterial(client.getKeyStoreFile().getURL(),
                            client.getKeyStorePwd().toCharArray(),
                            client.getPrivateKeyPwd().toCharArray())
                    .loadTrustMaterial(new TrustSelfSignedStrategy()) // loadTrustMaterial 需要我们提供一个根据对方提供的证书，判断对方是否可信的处理器。此处我们简单地无论如何都返回 true 。
                    .build();

            CloseableHttpClient httpClient = HttpClients.custom()//
                    .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()//
                            .setSSLSocketFactory(SSLConnectionSocketFactoryBuilder.create()//
                                    .setSslContext(sslContext)//
                                    .setHostnameVerifier(NoopHostnameVerifier.INSTANCE).build())
                            .build())
                    .build();
            customRequestFactory.setHttpClient(httpClient);
        } catch (Exception e) {
            log.debug("TrustAllStrategy RestTemplate register failed!");
        }
        return builder.requestFactory(() -> customRequestFactory).build();
    }

}