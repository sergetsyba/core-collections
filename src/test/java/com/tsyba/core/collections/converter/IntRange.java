package com.tsyba.core.collections.converter;

import com.tsyba.core.collections.IndexRange;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(IntRange.Converter.class)
public @interface IntRange {
	class Converter extends TypedArgumentConverter<String, IndexRange> {
		public Converter() {
			super(String.class, IndexRange.class);
		}

		@Override
		public IndexRange convert(String s) throws ArgumentConversionException {
			return s == null
				? null
				: IndexRange.parse(s);
		}
	}
}
