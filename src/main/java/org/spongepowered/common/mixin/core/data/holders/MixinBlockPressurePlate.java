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
package org.spongepowered.common.mixin.core.data.holders;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.spongepowered.common.data.DataTransactionBuilder.successNoData;
import static org.spongepowered.common.data.DataTransactionBuilder.successReplaceData;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.DataManipulator;
import org.spongepowered.api.data.DataPriority;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.manipulator.block.PoweredData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.common.data.manipulator.block.SpongePoweredData;
import org.spongepowered.common.interfaces.block.IMixinPoweredHolder;

import java.util.Collection;

@Mixin(BlockPressurePlate.class)
public abstract class MixinBlockPressurePlate extends BlockBasePressurePlate implements IMixinPoweredHolder {

    protected MixinBlockPressurePlate(Material materialIn) {
        super(materialIn);
    }

    @Override
    public Collection<DataManipulator<?>> getManipulators(World world, BlockPos blockPos) {
        return getManipulators(world.getBlockState(blockPos));
    }

    @Override
    public ImmutableList<DataManipulator<?>> getManipulators(IBlockState blockState) {
        PoweredData data = getPoweredData(blockState);
        return data == null ? ImmutableList.<DataManipulator<?>>of() : ImmutableList.<DataManipulator<?>>of(data);
    }

    @Override
    public PoweredData getPoweredData(IBlockState blockState) {
        final boolean powered = (Boolean) blockState.getValue(BlockPressurePlate.POWERED);
        return powered ? new SpongePoweredData() : null;
    }

    @Override
    public DataTransactionResult setPoweredData(PoweredData poweredData, World world, BlockPos blockPos, DataPriority priority) {
        final PoweredData data = getPoweredData(world.getBlockState(blockPos));
        switch (checkNotNull(priority)) {
            case DATA_MANIPULATOR:
            case POST_MERGE:
                IBlockState blockState = world.getBlockState(blockPos);
                world.setBlockState(blockPos, blockState.withProperty(BlockPressurePlate.POWERED, poweredData != null), 3);
                return successReplaceData(data);
            default:
                return successNoData();
        }
    }

    @Override
    public BlockState resetPoweredData(BlockState blockState) {
        return (BlockState) ((IBlockState) blockState).withProperty(BlockPressurePlate.POWERED, false);
    }
}
