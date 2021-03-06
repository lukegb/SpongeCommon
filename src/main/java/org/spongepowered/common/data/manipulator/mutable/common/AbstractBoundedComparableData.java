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
package org.spongepowered.common.data.manipulator.mutable.common;

import static com.google.common.base.Preconditions.checkNotNull;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.common.data.ImmutableDataCachingUtil;
import org.spongepowered.common.util.ReflectionUtil;

import java.util.Comparator;

public abstract class AbstractBoundedComparableData<T extends Comparable<T>, M extends DataManipulator<M, I>, I extends ImmutableDataManipulator<I, M>>
    extends AbstractSingleData<T, M, I> {

    private final Class<? extends I> immutableClass;
    protected final Comparator<T> comparator;
    protected final T lowerBound;
    protected final T upperBound;

    protected AbstractBoundedComparableData(Class<M> manipulatorClass, T value, Key<? extends BaseValue<T>> usedKey, Comparator<T> comparator,
                                         Class<? extends I> immutableClass, T lowerBound, T upperBound) {
        super(manipulatorClass, value, usedKey);
        this.comparator = checkNotNull(comparator);
        this.immutableClass = checkNotNull(immutableClass);
        this.lowerBound = checkNotNull(lowerBound);
        this.upperBound = checkNotNull(upperBound);
    }

    @SuppressWarnings("unchecked")
    @Override
    public M copy() {
        return (M) ReflectionUtil.createInstance(this.getClass(), this.getValue(), this.lowerBound, this.upperBound);
    }

    @Override
    public I asImmutable() {
        return ImmutableDataCachingUtil.getManipulator(this.immutableClass, this.getValue(), this.lowerBound, this.upperBound);
    }

    @Override
    public int compareTo(M o) {
        return this.comparator.compare(o.get(this.usedKey).get(), this.getValue());
    }

    @Override
    public DataContainer toContainer() {
        return new MemoryDataContainer().set(this.usedKey.getQuery(), this.getValue());
    }
}
