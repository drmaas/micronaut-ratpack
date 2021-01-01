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
package io.micronaut.ratpack.server.internal;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import io.micronaut.context.ApplicationContext;
import ratpack.registry.RegistryBacking;
import ratpack.util.Types;

import java.util.stream.Collectors;

/**
 * Converts micronaut beans into a format that can be loaded into the ratpack registry.
 *
 * @author drmaas
 * @since 1.0
 */
public class MicronautRegistryBacking implements RegistryBacking {
    private final ApplicationContext context;

    public MicronautRegistryBacking(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public <T> Iterable<Supplier<? extends T>> provide(TypeToken<T> type) {
        return ImmutableList.copyOf(
                context.getBeansOfType(type.getRawType())
        ).reverse().stream().map(beanName ->
                (Supplier<T>) () -> Types.cast(beanName))
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MicronautRegistryBacking that = (MicronautRegistryBacking) o;

        return context.equals(that.context);
    }

    @Override
    public int hashCode() {
        return context.hashCode();
    }
}
