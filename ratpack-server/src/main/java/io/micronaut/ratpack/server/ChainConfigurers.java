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

import io.micronaut.core.order.Ordered;
import ratpack.func.Action;
import ratpack.handling.Chain;
import ratpack.handling.Handler;
import ratpack.handling.Handlers;
import ratpack.server.ServerConfig;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that installs user {@link Chain} instances.
 *
 * @author drmaas
 * @since 1.0
 */
@Singleton
public class ChainConfigurers implements Action<Chain> {

    private final RatpackServerConfiguration configuration;
    private final List<Action<Chain>> delegates;
    private final List<Handler> handlers;
    private final List<RatpackServerCustomizer> customizers;

    public ChainConfigurers(RatpackServerConfiguration configuration,
                            List<Action<Chain>> delegates,
                            List<Handler> handlers,
                            List<RatpackServerCustomizer> customizers) {
        this.configuration = configuration;
        this.delegates = delegates;
        this.handlers = handlers;
        this.customizers = customizers;
    }

    @Override
    public void execute(Chain chain) throws Exception {
        chain.prefix(configuration.getContextPath(), chain1 -> {
            List<Action<Chain>> delegates = new ArrayList<>(this.delegates);
            for (RatpackServerCustomizer customizer : customizers) {
                delegates.addAll(customizer.getHandlers());
            }
            if (handlers.size() == 1 || delegates.isEmpty()) {
                delegates.add(singleHandlerAction());
            }
            delegates.add(staticResourcesAction(chain.getServerConfig()));
            delegates.sort((c1, c2) -> {
                int c1Order = 0;
                int c2Order = 0;
                if (c1 instanceof Ordered) {
                    c1Order = ((Ordered) c1).getOrder();
                }
                if (c2 instanceof Ordered) {
                    c2Order = ((Ordered) c2).getOrder();
                }
                return c1Order - c2Order;
            });
            for (Action<Chain> delegate : delegates) {
                if (!(delegate instanceof ChainConfigurers)) {
                    delegate.execute(chain);
                }
            }
        });
    }

    private Action<Chain> staticResourcesAction(final ServerConfig config) {
        return chain -> chain
                .all(Handlers.files(config, f -> f.dir("static").indexFiles("index.html")))
                .all(Handlers.files(config, f -> f.dir("public").indexFiles("index.html")));
    }

    private Action<Chain> singleHandlerAction() {
        return chain -> {
            if (handlers.size() == 1) {
                chain.get(handlers.get(0));
            }
        };
    }
}
