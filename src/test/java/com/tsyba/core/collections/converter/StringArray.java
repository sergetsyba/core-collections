package com.tsyba.core.collections.converter;

import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(StringArray.Converter.class)
public @interface StringArray {
	class Converter extends TypedArgumentConverter<String, String[]> {
		public Converter() {
			super(String.class, String[].class);
		}

		@Override
		public String[] convert(String s) throws ArgumentConversionException {
			if (s == null) {
				return null;
			}

			final var substring = s.substring(
				s.indexOf("[") + 1,
				s.lastIndexOf("]"));

			if (substring.isBlank()) {
				return new String[0];
			} else {
				final var string = substring.split("\\s*,\\s*");

				return Arrays.stream(string)
					.filter((item) -> !item.equals("null"))
					.toArray(String[]::new);
			}
		}
	}
}