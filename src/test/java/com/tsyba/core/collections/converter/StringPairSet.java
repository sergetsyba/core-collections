package com.tsyba.core.collections.converter;

import com.tsyba.core.collections.Pair;
import com.tsyba.core.collections.Set;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(StringPairSet.Converter.class)
public @interface StringPairSet {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, Set> {
		protected Converter() {
			super(String.class, Set.class);
		}

		@Override
		protected Set<Pair<String, String>> convert(String s) throws ArgumentConversionException {
			return new StringSet.Converter()
				.convert(s)
				.convert((item) -> {
					final var items = item.split(":");
					return new Pair<>(items[0], items[1]);
				});
		}
	}
}
