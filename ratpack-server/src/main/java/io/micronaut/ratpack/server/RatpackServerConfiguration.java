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


import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.env.Environment;
import io.micronaut.core.io.socket.SocketUtils;
import ratpack.server.ServerConfig;

import javax.annotation.Nullable;
import java.time.Duration;

/**
 * Configuration for the Ratpack server.
 *
 * @author drmaas
 * @since 1.0
 */
@ConfigurationProperties(RatpackServerConfiguration.PREFIX)
public class RatpackServerConfiguration {

    public static final String PREFIX = "ratpack.server";
    public static final String PORT = PREFIX + ".port";
    public static final String HOST = PREFIX + ".host";
    public static final int DEFAULT_PORT = 5050;
    public static final String ENABLED = PREFIX + ".enabled";

    private boolean enabled = true;
    private String baseDir = null;
    private int port = DEFAULT_PORT;
    private String host = null;
    private boolean development = false;
    private String contextPath = "";
    private boolean registerShutdownHook = true;
    private int threads =  ServerConfig.DEFAULT_THREADS;
    private String publicAddress = null;
    private int maxContentLength = ServerConfig.DEFAULT_MAX_CONTENT_LENGTH;
    private int maxChunkSize = ServerConfig.DEFAULT_MAX_CHUNK_SIZE;
    private int maxInitialLineLength = ServerConfig.DEFAULT_MAX_INITIAL_LINE_LENGTH;
    private int maxHeaderSize = ServerConfig.DEFAULT_MAX_HEADER_SIZE;
    private Duration connectTimeout = null;
    private Duration idleTimeout = null;
    private int maxMessagesPerRead = 0;
    private int receiveBufferSize = 0;
    private int connectQueueSize = 0;
    private int writeSpinCount = 0;
    private String portFile = null;
    private RatpackSslConfiguration sslConfiguration = null;

    /**
     * Gets the enabled.
     *
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the enabled.
     *
     * @param enabled the enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Gets the baseDir.
     *
     * @return the baseDir
     */
    public String getBaseDir() {
        return baseDir;
    }

    /**
     * Sets the baseDir.
     *
     * @param baseDir the baseDir
     */
    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    /**
     * Gets the port.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets the port.
     *
     * @param port the port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Gets the host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the host.
     *
     * @param host the host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Gets the development.
     *
     * @return the development
     */
    public boolean isDevelopment() {
        return development;
    }

    /**
     * Sets the development.
     *
     * @param development the development
     */
    public void setDevelopment(boolean development) {
        this.development = development;
    }

    /**
     * Gets the contextPath.
     *
     * @return the contextPath
     */
    public String getContextPath() {
        return contextPath;
    }

    /**
     * Sets the contextPath.
     *
     * @param contextPath the contextPath
     */
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    /**
     * Gets the registerShutdownHook.
     *
     * @return the registerShutdownHook
     */
    public boolean isRegisterShutdownHook() {
        return registerShutdownHook;
    }

    /**
     * Sets the registerShutdownHook.
     *
     * @param registerShutdownHook the registerShutdownHook
     */
    public void setRegisterShutdownHook(boolean registerShutdownHook) {
        this.registerShutdownHook = registerShutdownHook;
    }

    /**
     * Gets the threads.
     *
     * @return the threads
     */
    public int getThreads() {
        return threads;
    }

    /**
     * Sets the threads.
     *
     * @param threads the threads
     */
    public void setThreads(int threads) {
        this.threads = threads;
    }

    /**
     * Gets the publicAddress.
     *
     * @return the publicAddress
     */
    public String getPublicAddress() {
        return publicAddress;
    }

    /**
     * Sets the publicAddress.
     *
     * @param publicAddress the publicAddress
     */
    public void setPublicAddress(String publicAddress) {
        this.publicAddress = publicAddress;
    }

    /**
     * Gets the maxContentLength.
     *
     * @return the maxContentLength
     */
    public int getMaxContentLength() {
        return maxContentLength;
    }

    /**
     * Sets the maxContentLength.
     *
     * @param maxContentLength the maxContentLength
     */
    public void setMaxContentLength(int maxContentLength) {
        this.maxContentLength = maxContentLength;
    }

    /**
     * Gets the maxChunkSize.
     *
     * @return the maxChunkSize
     */
    public int getMaxChunkSize() {
        return maxChunkSize;
    }

    /**
     * Sets the maxChunkSize.
     *
     * @param maxChunkSize the maxChunkSize
     */
    public void setMaxChunkSize(int maxChunkSize) {
        this.maxChunkSize = maxChunkSize;
    }

    /**
     * Gets the maxInitialLineLength.
     *
     * @return the maxInitialLineLength
     */
    public int getMaxInitialLineLength() {
        return maxInitialLineLength;
    }

    /**
     * Sets the maxInitialLineLength.
     *
     * @param maxInitialLineLength the maxInitialLineLength
     */
    public void setMaxInitialLineLength(int maxInitialLineLength) {
        this.maxInitialLineLength = maxInitialLineLength;
    }

    /**
     * Gets the maxHeaderSize.
     *
     * @return the maxHeaderSize
     */
    public int getMaxHeaderSize() {
        return maxHeaderSize;
    }

    /**
     * Sets the maxHeaderSize.
     *
     * @param maxHeaderSize the maxHeaderSize
     */
    public void setMaxHeaderSize(int maxHeaderSize) {
        this.maxHeaderSize = maxHeaderSize;
    }

    /**
     * Gets the connectTimeout.
     *
     * @return the connectTimeout
     */
    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * Sets the connectTimeout.
     *
     * @param connectTimeout the connectTimeout
     */
    public void setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * Gets the idleTimeout.
     *
     * @return the idleTimeout
     */
    public Duration getIdleTimeout() {
        return idleTimeout;
    }

    /**
     * Sets the idleTimeout.
     *
     * @param idleTimeout the idleTimeout
     */
    public void setIdleTimeout(Duration idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    /**
     * Gets the maxMessagesPerRead.
     *
     * @return the maxMessagesPerRead
     */
    public int getMaxMessagesPerRead() {
        return maxMessagesPerRead;
    }

    /**
     * Sets the maxMessagesPerRead.
     *
     * @param maxMessagesPerRead the maxMessagesPerRead
     */
    public void setMaxMessagesPerRead(int maxMessagesPerRead) {
        this.maxMessagesPerRead = maxMessagesPerRead;
    }

    /**
     * Gets the receiveBufferSize.
     *
     * @return the receiveBufferSize
     */
    public int getReceiveBufferSize() {
        return receiveBufferSize;
    }

    /**
     * Sets the receiveBufferSize.
     *
     * @param receiveBufferSize the receiveBufferSize
     */
    public void setReceiveBufferSize(int receiveBufferSize) {
        this.receiveBufferSize = receiveBufferSize;
    }

    /**
     * Gets the connectQueueSize.
     *
     * @return the connectQueueSize
     */
    public int getConnectQueueSize() {
        return connectQueueSize;
    }

    /**
     * Sets the connectQueueSize.
     *
     * @param connectQueueSize the connectQueueSize
     */
    public void setConnectQueueSize(int connectQueueSize) {
        this.connectQueueSize = connectQueueSize;
    }

    /**
     * Gets the writeSpinCount.
     *
     * @return the writeSpinCount
     */
    public int getWriteSpinCount() {
        return writeSpinCount;
    }

    /**
     * Sets the writeSpinCount.
     *
     * @param writeSpinCount the writeSpinCount
     */
    public void setWriteSpinCount(int writeSpinCount) {
        this.writeSpinCount = writeSpinCount;
    }

    /**
     * Gets the portFile.
     *
     * @return the portFile
     */
    public String getPortFile() {
        return portFile;
    }

    /**
     * Sets the portFile.
     *
     * @param portFile the portFile
     */
    public void setPortFile(String portFile) {
        this.portFile = portFile;
    }

    /**
     * Gets the sslConfiguration.
     *
     * @return the sslConfiguration
     */
    public RatpackSslConfiguration getSslConfiguration() {
        return sslConfiguration;
    }

    /**
     * Sets the sslConfiguration.
     *
     * @param sslConfiguration the sslConfiguration
     */
    public void setSslConfiguration(RatpackSslConfiguration sslConfiguration) {
        this.sslConfiguration = sslConfiguration;
    }

    /**
     * Default constructor.
     *
     * @param environment      The environment
     * @param host       The server host
     * @param port       The server port
     */
    public RatpackServerConfiguration(
            Environment environment,
            @Property(name = HOST) @Nullable String host,
            @Property(name = PORT) @Nullable Integer port) {
        this.port = port != null ? port :
                environment.getActiveNames().contains(Environment.TEST) ? SocketUtils.findAvailableTcpPort() : DEFAULT_PORT;
        this.host = host;
    }



    /**
     * The SSL configuration.
     */
    @ConfigurationProperties(RatpackServerConfiguration.PREFIX + ".ssl")
    public static class RatpackSslConfiguration {
        private String keyStoreFile = null;
        private String keyStorePass = null;
        private String trustStoreFile = null;
        private String trustStorePass = null;

        /**
         * Gets the keyStoreFile.
         *
         * @return the keyStoreFile
         */
        public String getKeyStoreFile() {
            return keyStoreFile;
        }

        /**
         * Sets the keyStoreFile.
         *
         * @param keyStoreFile the keyStoreFile
         */
        public void setKeyStoreFile(String keyStoreFile) {
            this.keyStoreFile = keyStoreFile;
        }

        /**
         * Gets the keyStorePass.
         *
         * @return the keyStorePass
         */
        public String getKeyStorePass() {
            return keyStorePass;
        }

        /**
         * Sets the keyStorePass.
         *
         * @param keyStorePass the keyStorePass
         */
        public void setKeyStorePass(String keyStorePass) {
            this.keyStorePass = keyStorePass;
        }

        /**
         * Gets the trustStoreFile.
         *
         * @return the trustStoreFile
         */
        public String getTrustStoreFile() {
            return trustStoreFile;
        }

        /**
         * Sets the trustStoreFile.
         *
         * @param trustStoreFile the trustStoreFile
         */
        public void setTrustStoreFile(String trustStoreFile) {
            this.trustStoreFile = trustStoreFile;
        }

        /**
         * Gets the trustStorePass.
         *
         * @return the trustStorePass
         */
        public String getTrustStorePass() {
            return trustStorePass;
        }

        /**
         * Sets the trustStorePass.
         *
         * @param trustStorePass the trustStorePass
         */
        public void setTrustStorePass(String trustStorePass) {
            this.trustStorePass = trustStorePass;
        }
    }

}
