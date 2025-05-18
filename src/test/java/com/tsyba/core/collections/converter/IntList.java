package com.tsyba.core.collections.converter;

import com.tsyba.core.collections.List;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(IntList.Converter.class)
public @interface IntList {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, List> {
		protected Converter() {
			super(String.class, List.class);
		}

		@Override
		protected List<Integer> convert(String s) throws ArgumentConversionException {
			if (s == null) {
				return null;
			} else {
				final var converter = new StringArray.Converter();
				final var items2 = Arrays.stream(converter.convert(s))
					.map(Integer::parseInt)
					.toArray(Integer[]::new);

				return new List<>(items2);
			}
		}
	}
}
