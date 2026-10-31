package com.tsyba.core.collections.converter;

import com.tsyba.core.collections.IndexNotInRangeException;
import com.tsyba.core.collections.IndexRange;
import com.tsyba.core.collections.IndexRangeNotInRangeException;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;

@ConvertWith(IndexRangeException.Converter.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface IndexRangeException {
	class Converter extends TypedArgumentConverter<String, Exception> {
		protected Converter() {
			super(String.class, Exception.class);
		}

		@Override
		protected Exception convert(String s) throws ArgumentConversionException {
			if (s == null) {
				return null;
			}

			final var matcher1 = indexRegex.matcher(s);
			if (matcher1.find()) {
				return matchIndexNotInRangeException(matcher1);
			}

			final var matcher2 = indexRangeRegex.matcher(s);
			if (matcher2.find()) {
				return matchIndexRangeNotInRangeException(matcher2);
			}

			throw new ArgumentConversionException(
				format("Cannot parse %s or %s from %s.",
					IndexNotInRangeException.class.getName(),
					IndexRangeNotInRangeException.class.getName(), s));
		}

		private static final Pattern indexRegex = Pattern
			.compile("(-?\\d+)" +
				"\\s*∉\\s*" +
				"\\[\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*\\)");

		private IndexNotInRangeException matchIndexNotInRangeException(Matcher matcher) {
			final var index = matcher.group(1);
			final var start = matcher.group(2);
			final var end = matcher.group(3);

			return new IndexNotInRangeException(
				Integer.parseInt(index),
				new IndexRange(
					Integer.parseInt(start),
					Integer.parseInt(end)));
		}

		private static final Pattern indexRangeRegex = Pattern
			.compile("\\[\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*\\)" +
				"\\s*⊈\\s*" +
				"\\[\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*\\)");

		private IndexRangeNotInRangeException matchIndexRangeNotInRangeException(Matcher matcher) {
			final var start1 = matcher.group(1);
			final var end1 = matcher.group(2);
			final var start2 = matcher.group(3);
			final var end2 = matcher.group(4);

			return new IndexRangeNotInRangeException(
				new IndexRange(
					Integer.parseInt(start1),
					Integer.parseInt(end1)),
				new IndexRange(
					Integer.parseInt(start2),
					Integer.parseInt(end2)));
		}
	}
}
