package com.tsyba.core.collections.converter;

import com.tsyba.core.collections.Map;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static java.lang.String.format;

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(StringMapEntry.Converter.class)
public @interface StringMapEntry {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, Map.Entry> {
		protected Converter() {
			super(String.class, Map.Entry.class);
		}

		@Override
		protected Map.Entry<String, String> convert(String s) throws ArgumentConversionException {
			if (s == null || s.equals("null")) {
				return null;
			}

			final var parts = s.split("\\s*:\\s*", 2);
			if (parts.length != 2) {
				throw new ArgumentConversionException(
					format("Invalid Map.Entry formatted expression: %s.", s));
			}

			final var key = parts[0].equals("null")
				? null
				: parts[0];
			final var value = parts[1].equals("null")
				? null
				: parts[1];

			return new Map.Entry<>(key, value);
		}
	}
}
