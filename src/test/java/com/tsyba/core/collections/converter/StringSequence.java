package com.tsyba.core.collections.converter;

import com.tsyba.core.collections.Collection;
import com.tsyba.core.collections.IndexRange;
import com.tsyba.core.collections.Sequence;
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
@ConvertWith(StringSequence.Converter.class)
public @interface StringSequence {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, Sequence> {
		protected Converter() {
			super(String.class, Sequence.class);
		}

		@Override
		protected Sequence<String> convert(String s) throws ArgumentConversionException {
			final var items = new StringArray.Converter()
				.convert(s);

			return new Sequence<>() {
				@Override
				public Sequence<String> getPrefix(int index) {
					throw new UnsupportedOperationException();
				}

				@Override
				public Sequence<String> getSuffix(int index) {
					throw new UnsupportedOperationException();
				}

				@Override
				public Sequence<String> get(IndexRange range) {
					throw new UnsupportedOperationException();
				}

				@Override
				public Sequence<String> matchAll(Predicate<String> condition) {
					throw new UnsupportedOperationException();
				}

				@Override
				public Sequence<Integer> findAll(String item) {
					throw new UnsupportedOperationException();
				}

				@Override
				public Sequence<Integer> findAll(Sequence<String> items) {
					throw new UnsupportedOperationException();
				}

				@Override
				public Collection<String> getDistinct() {
					throw new UnsupportedOperationException();
				}

				@Override
				public Sequence<String> reverse() {
					throw new UnsupportedOperationException();
				}

				@Override
				public <R> Sequence<R> convert(Function<String, R> converter) {
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
