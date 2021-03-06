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
package org.spongepowered.common.data.manipulator.immutable.common;

import static com.google.common.base.Preconditions.checkNotNull;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.common.data.ImmutableDataCachingUtil;
import org.spongepowered.common.data.value.immutable.ImmutableSpongeValue;
import org.spongepowered.common.util.ReflectionUtil;

public abstract class AbstractImmutableSingleEnumData<E extends Enum<E>, I extends ImmutableDataManipulator<I, M>, M extends DataManipulator<M, I>> extends
        AbstractImmutableSingleData<E, I, M> {

    private final Class<? extends M> mutableClass;

    public AbstractImmutableSingleEnumData(Class<I> immutableClass, E value, Key<? extends BaseValue<E>> usedKey, Class<? extends M> mutableClass) {
        super(immutableClass, value, usedKey);
        this.mutableClass = checkNotNull(mutableClass);
    }

    @Override
    protected ImmutableValue<?> getValueGetter() {
        return ImmutableDataCachingUtil.getWildValue(ImmutableSpongeValue.class, this.usedKey, this.value, this.value);
    }

    @Override
    public M asMutable() {
        return ReflectionUtil.createInstance(this.mutableClass, this.getValue());
    }

    @Override
    public int compareTo(I o) {
        return o.get(this.usedKey).get().ordinal() - this.value.ordinal();
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer().set(this.usedKey.getQuery(), this.value.name());
    }
}
