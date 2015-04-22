/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.data;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.spongepowered.api.data.DataManipulator;
import org.spongepowered.api.data.DataTransactionResult;

import java.util.Collection;
import java.util.List;

public final class DataTransactionBuilder {

    private List<DataManipulator<?>> rejected;
    private List<DataManipulator<?>> replaced;
    private DataTransactionResult.Type resultType;

    private DataTransactionBuilder() {
    }

    public static DataTransactionResult fail(final DataManipulator<?> manipulator) {
        return new DataTransactionResult() {
            @Override
            public Type getType() {
                return Type.FAILURE;
            }

            @Override
            public Optional<? extends Collection<? extends DataManipulator<?>>> getRejectedData() {
                return Optional.of(ImmutableList.of(manipulator));
            }

            @Override
            public Optional<? extends Collection<? extends DataManipulator<?>>> getReplacedData() {
                return Optional.absent();
            }
        };
    }

    public static DataTransactionBuilder builder() {
        return new DataTransactionBuilder();
    }

    public DataTransactionBuilder result(final DataTransactionResult.Type type) {
        this.resultType = checkNotNull(type);
        return this;
    }


    public DataTransactionBuilder replace(final DataManipulator<?> manipulator) {
        if (this.replaced == null) {
            this.replaced = Lists.newArrayList();
        }
        this.replaced.add(checkNotNull(manipulator));
        return this;
    }

    public DataTransactionBuilder reject(final DataManipulator<?> manipulator) {
        if (this.rejected == null) {
            this.rejected = Lists.newArrayList();
        }
        this.rejected.add(checkNotNull(manipulator));
        return this;
    }

    public DataTransactionResult build() {
        checkState(this.resultType != null);
        return new BuilderResult(this);
    }

    private class BuilderResult implements DataTransactionResult {

        private final Type type;
        private final List<DataManipulator<?>> rejected;
        private final List<DataManipulator<?>> replaced;

        BuilderResult(final DataTransactionBuilder builder) {
            this.type = builder.resultType;
            if (builder.rejected != null) {
                this.rejected = ImmutableList.copyOf(builder.rejected);
            } else {
                this.rejected = null;
            }
            if (builder.replaced != null) {
                this.replaced = ImmutableList.copyOf(builder.replaced);
            } else {
                this.replaced = null;
            }
        }

        @Override
        public Type getType() {
            return this.type;
        }

        @Override
        public Optional<? extends Collection<? extends DataManipulator<?>>> getRejectedData() {
            return Optional.fromNullable(this.rejected);
        }

        @Override
        public Optional<? extends Collection<? extends DataManipulator<?>>> getReplacedData() {
            return Optional.fromNullable(this.replaced);
        }
    }

}