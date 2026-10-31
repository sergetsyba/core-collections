package com.tsyba.core.collections.converter;

import com.tsyba.core.collections.MutableList;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(StringMutableList.Converter.class)
public @interface StringMutableList {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, MutableList> {
		protected Converter() {
			super(String.class, MutableList.class);
		}

		@Override
		protected MutableList<String> convert(String s) throws ArgumentConversionException {
			if (s == null) {
				return null;
			} else {
				final var items = new StringArray.Converter()
					.convert(s);

				return new MutableList<>(items);
			}
		}
	}
}
