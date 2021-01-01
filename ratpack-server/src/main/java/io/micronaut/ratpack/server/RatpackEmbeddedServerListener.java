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

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.core.annotation.Internal;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.inject.Singleton;

/**
 * Application event listener that will startup the {@link RatpackEmbeddedServer} as a secondary server
 * on a different port allowing the Micronaut HTTP server and the GRPC server to run side by side.
 *
 * @author drmaas
 * @since 1.0
 */
@Internal
@Singleton
@Requires(beans = RatpackEmbeddedServer.class)
public class RatpackEmbeddedServerListener  implements ApplicationEventListener<ServerStartupEvent>, AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(RatpackEmbeddedServerListener.class);

    private final BeanContext beanContext;
    private RatpackEmbeddedServer ratpackServer;

    /**
     * Default constructor.
     * @param beanContext The bean context
     */
    public RatpackEmbeddedServerListener(BeanContext beanContext) {
        this.beanContext = beanContext;
    }

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
        final EmbeddedServer server = event.getSource();
        if (!(server instanceof RatpackEmbeddedServer)) {
            this.ratpackServer = beanContext.getBean(RatpackEmbeddedServer.class);
            ratpackServer.start();
            if (LOG.isInfoEnabled()) {
                LOG.info("Ratpack started on port {}", ratpackServer.getPort());
            }
        }
    }

    @Override
    @PreDestroy
    public void close() {
        if (ratpackServer != null && ratpackServer.isRunning()) {
            ratpackServer.stop();
        }
    }
}
