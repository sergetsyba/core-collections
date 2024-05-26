package com.tsyba.core.collections.converter;

import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Optional;

@ConvertWith(IntOptional.Converter.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntOptional {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, Optional> {
		protected Converter() {
			super(String.class, Optional.class);
		}

		@Override
		protected Optional<Integer> convert(String s) throws ArgumentConversionException {
			return Optional.ofNullable(s)
				.map(Integer::parseInt);
		}
	}
}
