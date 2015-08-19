package org.spongepowered.common.data;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.spongepowered.api.data.DataTransactionBuilder;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.ValueContainer;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.common.data.value.mutable.SpongeValue;

public abstract class AbstractValueProcessor<E> implements ValueProcessor<E, Value<E>> {

    @Override
    public Optional<Value<E>> getApiValueFromContainer(ValueContainer<?> container) {
        Optional<E> actualValue = this.getValueFromContainer(container);
        if (actualValue.isPresent()) {
            return Optional.<Value<E>>of(new SpongeValue<E>(this.getKey(), actualValue.get()));
        } else {
            return Optional.absent();
        }
    }

    @Override
    public DataTransactionResult transform(ValueContainer<?> container, Function<E, E> function) {
        if (this.supports(container)) {
            return offerToStore(container, Preconditions.checkNotNull(function.apply(getValueFromContainer(container).orNull())));
        }
        return DataTransactionBuilder.failNoData();
    }

    @Override
    public DataTransactionResult offerToStore(ValueContainer<?> container, BaseValue<E> value) {
        return offerToStore(container, value.get());
    }
}
