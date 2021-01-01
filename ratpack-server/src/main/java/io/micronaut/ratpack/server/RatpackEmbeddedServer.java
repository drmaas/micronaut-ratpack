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

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Secondary;
import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.core.annotation.Internal;
import io.micronaut.core.util.StringUtils;
import io.micronaut.runtime.ApplicationConfiguration;
import io.micronaut.runtime.exceptions.ApplicationStartupException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.runtime.server.event.ServerShutdownEvent;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.server.RatpackServer;

import javax.annotation.Nonnull;
import javax.inject.Singleton;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implementation of the {@link EmbeddedServer} interface for Ratpack.
 *
 * @author drmaas
 * @since 1.0
 */
@Singleton
@Secondary
@Requires(classes = RatpackServer.class)
@Requires(property = RatpackServerConfiguration.ENABLED, value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
public class RatpackEmbeddedServer implements EmbeddedServer {
    private static final Logger LOG = LoggerFactory.getLogger(RatpackEmbeddedServer.class);

    private RatpackServer server;
    private ApplicationContext applicationContext;
    private ApplicationConfiguration applicationConfiguration;
    private ApplicationEventPublisher eventPublisher;

    private final AtomicBoolean running = new AtomicBoolean(false);

    @Internal
    RatpackEmbeddedServer(@Nonnull RatpackServer server,
                          @Nonnull ApplicationContext applicationContext,
                          @Nonnull ApplicationConfiguration applicationConfiguration,
                          @Nonnull ApplicationEventPublisher eventPublisher) {
        this.server = server;
        this.applicationContext = applicationContext;
        this.applicationConfiguration = applicationConfiguration;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public int getPort() {
        return server.getBindPort();
    }

    @Override
    public String getHost() {
        return server.getBindHost();
    }

    @Override
    public String getScheme() {
        return server.getScheme();
    }

    @Override
    public URL getURL() {
        try {
            return getURI().toURL();
        } catch (MalformedURLException e) {
            throw new ConfigurationException("Invalid server URL: " + e.getMessage(), e);
        }
    }

    @Override
    public URI getURI() {
        return URI.create(getScheme() + "://" + getHost() + ':' + getPort());
    }

    @Override
    public boolean isServer() {
        return true;
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public ApplicationConfiguration getApplicationConfiguration() {
        return applicationConfiguration;
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public EmbeddedServer start() {
        if (running.compareAndSet(false, true)) {
            try {
                server.start();
                eventPublisher.publishEvent(new ServerStartupEvent(this));
            } catch (Exception e) {
                throw new ApplicationStartupException("Unable to start Ratpack server: " + e.getMessage(), e);
            }
        }
        return this;
    }

    @Override
    public EmbeddedServer stop() {
        if (running.compareAndSet(true, false)) {
            try {
                eventPublisher.publishEvent(new ServerShutdownEvent(this));
                server.stop();
            } catch (Exception e) {
                LOG.error("Unable to stop Ratpack server: " + e.getMessage(), e);
            }
        }
        return this;
    }
}
