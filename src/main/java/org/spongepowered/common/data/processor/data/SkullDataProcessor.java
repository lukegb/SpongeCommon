package org.spongepowered.common.data.processor.data;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.spongepowered.common.data.util.DataUtil.getData;

import com.google.common.base.Optional;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySkull;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.ImmutableSkullData;
import org.spongepowered.api.data.manipulator.mutable.SkullData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.service.persistence.InvalidDataException;
import org.spongepowered.common.data.DataProcessor;
import org.spongepowered.common.data.manipulator.mutable.SpongeSkullData;
import org.spongepowered.common.data.processor.common.SkullUtils;

public class SkullDataProcessor implements DataProcessor<SkullData, ImmutableSkullData> {

    @Override
    public boolean supports(DataHolder dataHolder) {
        return SkullUtils.supportsObject(dataHolder);
    }

    @Override
    public Optional<SkullData> from(DataHolder dataHolder) {
        if (dataHolder instanceof TileEntitySkull) {
            return Optional.<SkullData>of(new SpongeSkullData(SkullUtils.getSkullType((TileEntitySkull) dataHolder)));
        } else if (SkullUtils.isValidItemStack(dataHolder)) {
            return Optional.<SkullData>of(new SpongeSkullData(SkullUtils.getSkullType((ItemStack) dataHolder)));
        }
        return Optional.absent();
    }

    @Override
    public Optional<SkullData> fill(DataHolder dataHolder, SkullData manipulator) {
        if (this.supports(dataHolder)) {
            if (dataHolder instanceof TileEntitySkull) {
                manipulator.set(Keys.SKULL_TYPE, SkullUtils.getSkullType((TileEntitySkull) dataHolder));
            } else {
                manipulator.set(Keys.SKULL_TYPE, SkullUtils.getSkullType((ItemStack) dataHolder));
            }
            return Optional.of(manipulator);
        }
        return Optional.absent();
    }

    @Override
    public Optional<SkullData> fill(DataHolder dataHolder, SkullData manipulator, MergeFunction overlap) {
        if (this.supports(dataHolder)) {
            SkullData merged = overlap.merge(checkNotNull(manipulator.copy()), this.from(dataHolder).get());
            return Optional.of(manipulator.set(Keys.SKULL_TYPE, merged.type().get()));
        }
        return Optional.absent();
    }

    @Override
    public Optional<SkullData> fill(DataContainer container, SkullData skullData) {
        return Optional.of(skullData.set(Keys.SKULL_TYPE, getData(container, Keys.SKULL_TYPE)));
    }

    @Override
    public Optional<ImmutableSkullData> fillImmutable(DataHolder dataHolder, ImmutableSkullData immutable) {
        return
    }

    @Override
    public Optional<ImmutableSkullData> fillImmutable(DataHolder dataHolder, ImmutableSkullData immutable, MergeFunction overlap) {
        return null;
    }

    @Override
    public Optional<ImmutableSkullData> fillImmutable(DataContainer container, ImmutableSkullData immutableManipulator) {
        return null;
    }

    @Override
    public DataTransactionResult set(DataHolder dataHolder, SkullData manipulator) {
        return null;
    }

    @Override
    public DataTransactionResult set(DataHolder dataHolder, SkullData manipulator, MergeFunction function) {
        return null;
    }

    @Override
    public Optional<ImmutableSkullData> with(Key<? extends BaseValue<?>> key, Object value, ImmutableSkullData immutable) {
        return null;
    }

    @Override
    public DataTransactionResult remove(DataHolder dataHolder) {
        return null;
    }

    @Override
    public SkullData create() {
        return null;
    }

    @Override
    public ImmutableSkullData createImmutable() {
        return null;
    }

    @Override
    public Optional<SkullData> createFrom(DataHolder dataHolder) {
        return this.from(dataHolder);
    }

    @Override
    public Optional<SkullData> build(DataView container) throws InvalidDataException {
        return null;
    }
}
