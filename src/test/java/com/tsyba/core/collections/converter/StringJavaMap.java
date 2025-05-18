package com.tsyba.core.collections.converter;

import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(StringJavaMap.Converter.class)
public @interface StringJavaMap {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, Map> {
		protected Converter() {
			super(String.class, Map.class);
		}

		@Override
		protected Map<String, String> convert(String s) throws ArgumentConversionException {
			final var entries = new StringEntryArray.Converter()
				.convert(s);

			final var map = new HashMap<String, String>();
			for (var entry : entries) {
				map.put(entry.key, entry.value);
			}

			return map;
		}
	}
}
