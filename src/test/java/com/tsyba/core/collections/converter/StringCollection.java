package com.tsyba.core.collections.converter;

import com.tsyba.core.collections.Collection;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(StringCollection.Converter.class)
public @interface StringCollection {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, Collection> {
		protected Converter() {
			super(String.class, Collection.class);
		}

		@Override
		protected Collection<String> convert(String s) throws ArgumentConversionException {
			final var items = new StringArray.Converter()
				.convert(s);

			return new Collection<>() {
				@Override
				public Collection<String> getDistinct() {
					throw new UnsupportedOperationException();
				}

				@Override
				public Collection<String> matchAll(Predicate<String> condition) {
					throw new UnsupportedOperationException();
				}

				@Override
				public <R> Collection<R> convert(Function<String, R> converter) {
					throw new UnsupportedOperationException();
				}

				@Override
				public Iterator<String> iterator() {
					return Arrays.asList(items)
						.iterator();
				}
			};
		}
	}
}
