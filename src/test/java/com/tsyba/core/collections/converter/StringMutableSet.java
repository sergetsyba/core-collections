package com.tsyba.core.collections.converter;

import com.tsyba.core.collections.MutableSet;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(StringMutableSet.Converter.class)
public @interface StringMutableSet {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, MutableSet> {
		protected Converter() {
			super(String.class, MutableSet.class);
		}

		@Override
		protected MutableSet<String> convert(String s) throws ArgumentConversionException {
			final var items = new StringArray.Converter()
				.convert(s);

			return new MutableSet<>(items);
		}
	}
}
