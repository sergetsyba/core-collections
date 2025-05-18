package com.tsyba.core.collections.converter;

import com.tsyba.core.collections.MutableMap;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(StringMutableMap.Converter.class)
public @interface StringMutableMap {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, MutableMap> {
		protected Converter() {
			super(String.class, MutableMap.class);
		}

		@Override
		protected MutableMap<String, String> convert(String s) throws ArgumentConversionException {
			final var entries = new StringEntryArray.Converter()
				.convert(s);

			return new MutableMap<>(entries);
		}
	}
}
