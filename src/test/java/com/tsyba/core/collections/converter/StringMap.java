package com.tsyba.core.collections.converter;

import com.tsyba.core.collections.Map;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(StringMap.Converter.class)
public @interface StringMap {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, Map> {
		protected Converter() {
			super(String.class, Map.class);
		}

		@Override
		protected Map<String, String> convert(String s) throws ArgumentConversionException {
			final var entries = new StringEntryArray.Converter()
				.convert(s);

			return new Map<>(entries);
		}
	}
}
