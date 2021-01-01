/*
 * Copyright 2017-2021 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.ratpack.server;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.netty.handler.ssl.SslContextBuilder;
import ratpack.server.ServerConfig;
import ratpack.server.ServerConfigBuilder;
import ratpack.ssl.internal.SslContexts;

import javax.inject.Singleton;
import javax.net.ssl.KeyManagerFactory;
import java.io.File;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *  Builds the Ratpack {@link ServerConfigBuilder} from the micronaut {@link RatpackServerConfiguration}.
 *
 * @author drmaas
 * @since 1.0
 */
@Factory
public class RatpackServerConfigBuilderFactory {

    private final RatpackServerConfiguration serverConfiguration;

    public RatpackServerConfigBuilderFactory(RatpackServerConfiguration serverConfiguration) {
        this.serverConfiguration = serverConfiguration;
    }

    /**
     *
     * @return the {@link ServerConfigBuilder}
     * @throws Exception if the server configuration cannot be built
     */
    @Bean
    @Singleton
    protected ServerConfigBuilder serverConfig() throws Exception {
        ServerConfigBuilder builder = ServerConfig.builder();
        if (serverConfiguration.getBaseDir() != null) {
            builder.baseDir(new File(serverConfiguration.getBaseDir()));
        }
        builder.port(serverConfiguration.getPort());
        if (serverConfiguration.getHost() != null) {
            builder.address(InetSocketAddress.createUnresolved(serverConfiguration.getHost(),
                    serverConfiguration.getPort()).getAddress());
        }
        builder.development(serverConfiguration.isDevelopment());
        builder.registerShutdownHook(serverConfiguration.isRegisterShutdownHook());
        builder.threads(serverConfiguration.getThreads());
        if (serverConfiguration.getPublicAddress() != null) {
            builder.publicAddress(URI.create(serverConfiguration.getPublicAddress()));
        }
        builder.maxContentLength(serverConfiguration.getMaxContentLength());
        builder.maxChunkSize(serverConfiguration.getMaxChunkSize());
        builder.maxInitialLineLength(serverConfiguration.getMaxInitialLineLength());
        builder.maxHeaderSize(serverConfiguration.getMaxHeaderSize());
        if (serverConfiguration.getConnectTimeout() != null) {
            builder.connectTimeoutMillis((int) serverConfiguration.getConnectTimeout().toMillis());
        }
        if (serverConfiguration.getIdleTimeout() != null) {
            builder.idleTimeout(serverConfiguration.getIdleTimeout());
        }
        if (serverConfiguration.getMaxMessagesPerRead() > 0) {
            builder.maxMessagesPerRead(serverConfiguration.getMaxMessagesPerRead());
        }
        if (serverConfiguration.getReceiveBufferSize() > 0) {
            builder.receiveBufferSize(serverConfiguration.getReceiveBufferSize());
        }
        if (serverConfiguration.getConnectQueueSize() > 0) {
            builder.connectQueueSize(serverConfiguration.getConnectQueueSize());
        }
        if (serverConfiguration.getWriteSpinCount() > 0) {
            builder.writeSpinCount(serverConfiguration.getWriteSpinCount());
        }
        if (serverConfiguration.getPortFile() != null) {
            builder.portFile(new File(serverConfiguration.getPortFile()).toPath());
        }
        if (serverConfiguration.getSslConfiguration() != null) {
            RatpackServerConfiguration.RatpackSslConfiguration ssl = serverConfiguration.getSslConfiguration();
            if (!ssl.getKeyStoreFile().isEmpty()) {
                KeyManagerFactory keyManagerFactory;
                try (InputStream is = Files.newInputStream(Paths.get(ssl.getKeyStoreFile()))) {
                    keyManagerFactory = SslContexts.keyManagerFactory(is, ssl.getKeyStorePass().toCharArray());
                }
                SslContextBuilder sslContextBuilder = SslContextBuilder.forServer(keyManagerFactory);
                if (!ssl.getTrustStoreFile().isEmpty()) {
                    try (InputStream is = Files.newInputStream(Paths.get(ssl.getTrustStoreFile()))) {
                        sslContextBuilder.trustManager(SslContexts.trustManagerFactory(is, ssl.getTrustStorePass().toCharArray()));
                    }
                }
            }
        }
        return builder;
    }
}
