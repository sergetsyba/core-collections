package com.tsyba.core.collections.converter;

import com.tsyba.core.collections.List;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(StringList.Converter.class)
public @interface StringList {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, List> {
		protected Converter() {
			super(String.class, List.class);
		}

		@Override
		protected List<String> convert(String s) throws ArgumentConversionException {
			if (s == null) {
				return null;
			} else {
				final var items = new StringArray.Converter()
					.convert(s);

				return new List<>(items);
			}
		}
	}
}
