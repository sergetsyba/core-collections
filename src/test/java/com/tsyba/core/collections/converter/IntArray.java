package com.tsyba.core.collections.converter;

import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(IntArray.Converter.class)
public @interface IntArray {
	class Converter extends TypedArgumentConverter<String, Integer[]> {
		public Converter() {
			super(String.class, Integer[].class);
		}

		@Override
		public Integer[] convert(String s) throws ArgumentConversionException {
			if (s == null) {
				return null;
			}

			final var substring = s.substring(
				s.indexOf("[") + 1,
				s.lastIndexOf("]"));

			if (substring.isBlank()) {
				return new Integer[0];
			} else {
				final var items = substring.split("\\s*,\\s*");
				return Arrays.stream(items)
					.map(Integer::parseInt)
					.toArray(Integer[]::new);
			}
		}
	}
}