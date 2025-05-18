package com.tsyba.core.collections.converter;

import com.tsyba.core.collections.Map;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Objects;

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(StringEntryArray.Converter.class)
public @interface StringEntryArray {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, Map.Entry[]> {
		protected Converter() {
			super(String.class, Map.Entry[].class);
		}

		@Override
		@SuppressWarnings("unchecked")
		protected Map.Entry<String, String>[] convert(String s) throws ArgumentConversionException {
			return Arrays.stream(new StringArray.Converter()
					.convert(s))
				.filter(Objects::nonNull)
				.map(this::parseEntry)
				.toArray(Map.Entry[]::new);
		}

		private Map.Entry<String, String> parseEntry(String s) {
			final var parts = s.split("\\s*:\\s*");
			final var key = "null".equals(parts[0])
				? null
				: parts[0];
			final var value = "null".equals(parts[1])
				? null
				: parts[1];

			return new Map.Entry<>(key, value);
		}
	}
}
