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
package org.spongepowered.common.data.manipulator.immutable.block;

import static org.spongepowered.common.data.util.ComparatorUtil.intComparator;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableLayeredData;
import org.spongepowered.api.data.manipulator.mutable.block.LayeredData;
import org.spongepowered.api.data.value.immutable.ImmutableBoundedValue;
import org.spongepowered.common.data.ImmutableDataCachingUtil;
import org.spongepowered.common.data.manipulator.immutable.common.AbstractImmutableBoundedComparableData;
import org.spongepowered.common.data.manipulator.mutable.block.SpongeLayeredData;
import org.spongepowered.common.data.value.immutable.ImmutableSpongeBoundedValue;

public class ImmutableSpongeLayeredData extends AbstractImmutableBoundedComparableData<Integer, ImmutableLayeredData, LayeredData> implements ImmutableLayeredData {

    public ImmutableSpongeLayeredData(int value, int lowerBound, int upperBound) {
        super(ImmutableLayeredData.class, value, Keys.LAYER, intComparator(), SpongeLayeredData.class, lowerBound, upperBound);
    }

    @Override
    public ImmutableBoundedValue<Integer> layer() {
        return ImmutableDataCachingUtil.getValue(ImmutableSpongeBoundedValue.class, Keys.LAYER, 0, this.value,
                                                 this.comparator, this.lowerBound, this.upperBound);
    }
}
