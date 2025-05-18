package com.tsyba.core.collections.converter;

import com.tsyba.core.collections.Set;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(StringSet.Converter.class)
public @interface StringSet {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, Set> {
		protected Converter() {
			super(String.class, Set.class);
		}

		@Override
		protected Set<String> convert(String s) throws ArgumentConversionException {
			final var items = new StringArray.Converter()
				.convert(s);

			return new Set<>(items);
		}
	}
}
