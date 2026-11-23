package com.tsyba.core.collections.converter;

import com.tsyba.core.collections.IndexRange;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Optional;

@ConvertWith(IntRangeOptional.Converter.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntRangeOptional {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, Optional> {
		public Converter() {
			super(String.class, Optional.class);
		}

		@Override
		public Optional<IndexRange> convert(String s) throws ArgumentConversionException {
			final var converter = new IntRange.Converter();
			return Optional.ofNullable(s)
				.map(converter::convert);
		}
	}
}
