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
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.ratpack.server.internal.MicronautRegistryBacking;
import ratpack.func.Function;
import ratpack.registry.Registry;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfigBuilder;
import ratpack.util.Exceptions;

import javax.inject.Singleton;
import java.util.List;

/**
 * Builds a singleton instance of {@link RatpackServer}.
 *
 * @author drmaas
 * @since 1.0
 */
@Factory
public class RatpackServerFactory {

    private final ApplicationContext context;
    private final ServerConfigBuilder serverConfigBuilder;
    private final List<RatpackServerCustomizer> customizers;
    private final ChainConfigurers chainConfigurers;

    public RatpackServerFactory(ApplicationContext context,
                                ServerConfigBuilder serverConfigBuilder,
                                List<RatpackServerCustomizer> customizers,
                                ChainConfigurers chainConfigurers) {
        this.context = context;
        this.serverConfigBuilder = serverConfigBuilder;
        this.customizers = customizers;
        this.chainConfigurers = chainConfigurers;
    }

    /**
     *
     * @return The {@link RatpackServer}
     * @throws Exception if the server cannot be built
     */
    @Bean
    @Singleton
    protected RatpackServer ratpackServer() throws Exception {
        for (RatpackServerCustomizer c : customizers) {
            c.getServerConfig().execute(serverConfigBuilder);
        }
        return RatpackServer.of(spec -> spec
                .serverConfig(serverConfigBuilder)
                .registry(joinedRegistry(context))
                .handlers(chainConfigurers)
        );
    }

        private Function<Registry, Registry> joinedRegistry(ApplicationContext context) {
        return baseRegistry -> {
            Registry updated = customizers.stream()
                    .map(customizer -> Exceptions.uncheck(() -> customizer.getRegistry().apply(baseRegistry)))
                    .reduce(Registry::join).orElse(Registry.empty());
            return updated.join(Registry.backedBy(new MicronautRegistryBacking(context)));
        };
    }
}
