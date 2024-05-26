package com.tsyba.core.collections;

import com.tsyba.core.collections.converter.StringArray;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListTests {
	@DisplayName("List(T...)")
	@Nested
	class NewWithVarargsTests {
		@DisplayName("\uD83D\uDE82")
		@Tests({
			"when no items are null, creates list;" +
				"[d, O, s, R, s];" +
				"[d, O, s, R, s];",
			"when some items are null, creates list with non-null items;" +
				"[d, null, null, s, null, s];" +
				"[d, s, s]",
			"when all items are null, creates empty list;" +
				"[null, null, null, null];" +
				"[]",
			"when argument array is empty, creates empty list;" +
				"[];" +
				"[]"
		})
		void test(@StringArray String[] items, @StringList List<String> expected) {
			final var created = new List<>(items);
			assertEquals(expected, created,
				format("new List<>(%s)",
					Arrays.toString(items)));
		}
	}

	@DisplayName("List(Collection<T>)")
	@Nested
	class NewWithCollectionTests {
		@DisplayName("\uD83C\uDF4B")
		@Tests({
			"when argument collection is not empty, creates list;" +
				"[h, m, I, k, m, m];" +
				"[h, m, I, k, m, m]",
			"when argument collection is empty, creates empty list;" +
				"[];" +
				"[]"
		})
		void test(@StringCollection Collection<String> items,
			@StringList List<String> expected) {

			final var created = new List<>(items);
			assertEquals(expected, created,
				format("new List<>(%s)", items));
		}
	}

	@DisplayName(".getCount()")
	@Nested
	class GetCountTests {
		@DisplayName("\uD83D\uDCFA")
		@Tests({
			"when list is not empty, returns item count;" +
				"[h, M, m, M, M, d, j];" +
				"7",
			"when list is empty, returns 0;" +
				"[];" +
				"0"
		})
		void test(@StringList List<String> items, int expected) {
			final var count = items.getCount();
			assertEquals(expected, count,
				format("%s.getCount()", items));
		}
	}

	@DisplayName(".get(int)")
	@Nested
	class GetAtIndexTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when index is before valid range start, fails;" +
				"[h, s, e, q, s, A]; -1;" +
				"null",
			"when index is at valid range start, returns first item;" +
				"[h, s, e, q, s, A]; 0;" +
				"h",
			"when index is within valid range, returns item at index;" +
				"[h, s, e, q, s, A]; 4;" +
				"s",
			"when index is one before valid range end, returns last item;" +
				"[h, s, e, q, s, A]; 5;" +
				"A",
			"when index is at valid range end, fails;" +
				"[h, s, e, q, s, A]; 6;" +
				"null",
			"when index is after valid range end, fails;" +
				"[h, s, e, q, s, A]; 8;" +
				"null"
		})
		void testNotEmpty(@StringList List<String> items, int index, String expected) {
			test(items, index, expected);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when index is before valid range start, fails;" +
				"[]; -1;" +
				"null",
			"when index is at valid range start, fails;" +
				"[]; 0;" +
				"null",
			"when index is after valid range end, fails;" +
				"[]; 1;" +
				"null"
		})
		void testEmpty(@StringList List<String> items, int index, String expected) {
			test(items, index, expected);
		}

		private void test(List<String> items, int index, String expected) {
			try {
				final var item = items.get(index);
				assertEquals(expected, item,
					format("%s.get(%d)", items, index));
			} catch (IndexNotInRangeException exception) {
				if (expected == null) {
					final var expected2 = new IndexNotInRangeException(index,
						items.getIndexRange());

					assertEquals(expected2, exception,
						format("%s.get(%d)", items, index));
				} else {
					throw exception;
				}
			}
		}
	}

	@DisplayName(".getPrefix(int)")
	@Nested
	class GetPrefixTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when index is before valid range start, fails;" +
				"[g, r, e, Q, e, s, A, c]; -4;" +
				"null",
			"when index is at valid range start, returns empty list;" +
				"[g, r, e, Q, e, s, A, c]; 0;" +
				"[]",
			"when index is within valid range, returns prefix;" +
				"[g, r, e, Q, e, s, A, c]; 4;" +
				"[g, r, e, Q]",
			"when index is one before valid range end, returns prefix;" +
				"[g, r, e, Q, e, s, A, c]; 7;" +
				"[g, r, e, Q, e, s, A]",
			"when index is at valid range end, fails;" +
				"[g, r, e, Q, e, s, A, c]; 8;" +
				"null",
			"when index is after valid range end, fails;" +
				"[g, r, e, Q, e, s, A, c]; 11;" +
				"null"
		})
		void testNotEmpty(@StringList List<String> items, int index,
			@StringList List<String> expected) {
			test(items, index, expected);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when index is before valid range start, fails;" +
				"[]; -1;" +
				"null",
			"when index is at valid range start, fails;" +
				"[]; 0;" +
				"null",
			"when index is after valid range end, fails;" +
				"[]; 1;" +
				"null"
		})
		void testEmpty(@StringList List<String> items, int index,
			@StringList List<String> expected) {
			test(items, index, expected);
		}

		private void test(List<String> items, int index, List<String> expected) {
			try {
				final var prefix = items.getPrefix(index);
				assertEquals(expected, prefix,
					format("%s.getPrefix(%d)", items, index));
			} catch (IndexNotInRangeException exception) {
				if (expected == null) {
					final var expected2 = new IndexNotInRangeException(index,
						items.getIndexRange());

					assertEquals(expected2, exception,
						format("%s.getPrefix(%d)", items, index));
				} else {
					throw exception;
				}
			}
		}
	}

	@DisplayName(".getSuffix(int)")
	@Nested
	class GetSuffixTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when index is before valid range start, fails;" +
				"[g, r, e, Q, e, s, A, c]; -4;" +
				"null",
			"when index is at valid range start, returns all items;" +
				"[g, r, e, Q, e, s, A, c]; 0;" +
				"[g, r, e, Q, e, s, A, c]",
			"when index is within valid range, returns suffix;" +
				"[g, r, e, Q, e, s, A, c]; 4;" +
				"[e, s, A, c]",
			"when index is one before valid range end, returns suffix;" +
				"[g, r, e, Q, e, s, A, c]; 7;" +
				"[c]",
			"when index is at valid range end, fails;" +
				"[g, r, e, Q, e, s, A, c]; 8;" +
				"null",
			"when index is after valid range end, fails;" +
				"[g, r, e, Q, e, s, A, c]; 11;" +
				"null"
		})
		void testNotEmpty(@StringList List<String> items, int index,
			@StringList List<String> expected) {
			test(items, index, expected);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when index is before valid range start, fails;" +
				"[]; -1;" +
				"null",
			"when index is at valid range start, fails;" +
				"[]; 0;" +
				"null",
			"when index is after valid range end, fails;" +
				"[]; 1;" +
				"null"
		})
		void testEmpty(@StringList List<String> items, int index,
			@StringList List<String> expected) {
			test(items, index, expected);
		}

		private void test(List<String> items, int index, List<String> expected) {
			try {
				final var prefix = items.getSuffix(index);
				assertEquals(expected, prefix,
					format("%s.getSuffix(%d)", items, index));
			} catch (IndexNotInRangeException exception) {
				if (expected == null) {
					final var expected2 = new IndexNotInRangeException(index,
						items.getIndexRange());

					assertEquals(expected2, exception,
						format("%s.getSuffix(%d)", items, index));
				} else {
					throw exception;
				}
			}
		}
	}

	@DisplayName(".get(IndexRange)")
	@Nested
	class GetAtIndexRangeTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when range starts at valid range start, returns items in range;" +
				"[f, y, R, e, l, k, g, S]; [0, 4);" +
				"[f, y, R, e]",
			"when range is within valid range, returns items in range;" +
				"[f, y, R, e, l, k, g, S]; [2, 6);" +
				"[R, e, l, k]",
			"when range ends at valid range end, returns items in range;" +
				"[f, y, R, e, l, k, g, S]; [6, 8);" +
				"[g, S]",
			"when range equals valid range, returns all items;" +
				"[f, y, R, e, l, k, g, S]; [0, 8);" +
				"[f, y, R, e, l, k, g, S];",
			"when range ends after valid range end, fails;" +
				"[f, y, R, e, l, k, g, S]; [6, 9);" +
				"null",
			"when range is empty, returns empty list;" +
				"[f, y, R, e, l, k, g, S]; [0, 0);" +
				"[]"
		})
		void testNotEmpty(@StringList List<String> items, @IntRange IndexRange range,
			@StringList List<String> expected) {
			test(items, range, expected);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when range is not empty, fails;" +
				"[]; [0, 1);" +
				"null",
			"when range is empty, returns empty list;" +
				"[]; [0, 0);" +
				"[]"
		})
		void testEmpty(@StringList List<String> items, @IntRange IndexRange range,
			@StringList List<String> expected) {
			test(items, range, expected);
		}

		private void test(List<String> items, IndexRange range, List<String> expected) {
			try {
				final var returned = items.get(range);
				assertEquals(expected, returned,
					format("%s.get(%s)", items, range));
			} catch (Exception exception) {
				if (expected == null) {
					final var expected2 = new IndexRangeNotInRangeException(range,
						items.getIndexRange());

					assertEquals(expected2, exception,
						format("%s.get(%s)", items, range));
				} else {
					throw exception;
				}
			}
		}
	}

	@DisplayName(".matchAll(Predicate<T>)")
	@Nested
	class MatchAllTests {
		@DisplayName("\uD83D\uDD31")
		@Tests({
			"when some items match, returns matched items;" +
				"[g, R, W, s, A, s, W];" +
				"[R, W, A, W]",
			"when all items match, returns matched items;" +
				"[R, W, A, X, K, S];" +
				"[R, W, A, X, K, S]",
			"when no items match, returns empty list;" +
				"[g, m, k, o, l, m];" +
				"[]",
			"when list is empty, returns empty list;" +
				"[];" +
				"[]"
		})
		void test(@StringList List<String> items, @StringList List<String> expected) {
			final var matched = items.matchAll((item) -> {
				return item.toUpperCase()
					.equals(item);
			});

			assertEquals(expected, matched,
				format("%s.matchAll(<is uppercase>)", items));
		}
	}

	@DisplayName(".findAll(T)")
	@Nested
	class FindTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when item is present multiple times, returns its indexes;" +
				"[g, R, s, R, s, a, v, R, f]; R;" +
				"[1, 3, 7]",
			"when item is present once, returns its index;" +
				"[g, R, s, R, s, a, v, R, f]; a;" +
				"[5]",
			"when item is absent, returns empty list;" +
				"[g, R, s, R, s, a, v, R, f]; r;" +
				"[]",
			"when item is null, returns empty list;" +
				"[g, R, s, R, s, a, v, R, f]; null;" +
				"[]"
		})
		void testNotEmpty(@StringList List<String> items, String item,
			@IntList List<Integer> expected) {
			test(items, item, expected);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when item is not null, returns empty list;" +
				"[]; R;" +
				"[]",
			"when item is null, returns empty list;" +
				"[]; null;" +
				"[]"
		})
		void testEmpty(@StringList List<String> items, String item,
			@IntList List<Integer> expected) {
			test(items, item, expected);
		}

		private void test(List<String> items, String item, List<Integer> expected) {
			final var indexes = items.findAll(item);
			assertEquals(expected, indexes,
				format("%s.findAll(%s)", items, item));
		}
	}

	@DisplayName(".findAll(Sequence<T>)")
	@Nested
	class FindSequenceTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when sequence is present multiple times, returns its indexes;" +
				"[r, e, Q, s, c, d, e, Q, q, e, Q, f]; [e, Q];" +
				"[1, 6, 9]",
			"when sequence is present once, returns its index;" +
				"[r, e, Q, s, c, d, e, Q, q, e, Q, f]; [e, Q, q];" +
				"[6]",
			"when sequence is absent, returns empty sequence;" +
				"[r, e, Q, s, c, d, e, Q, q, e, Q, f]; [E, Q];" +
				"[]",
			"when argument sequence is larger, returns empty sequence;" +
				"[r, e, Q]; [r, e, Q, s, c];" +
				"[]",
			"when argument sequence is empty, returns all indexes;" +
				"[r, e, Q, s, c, d, e, Q, q, e, Q, f]; [];" +
				"[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]"
		})
		void testNotEmpty(@StringList List<String> items1,
			@StringList List<String> items2, @IntList List<Integer> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when argument sequence is not empty, returns empty sequence;" +
				"[]; [e, Q];" +
				"[]",
			"when argument sequence is empty, returns empty sequence;" +
				"[]; [];" +
				"[]"
		})
		void testEmpty(@StringList List<String> items1,
			@StringList List<String> items2, @IntList List<Integer> expected) {
			test(items1, items2, expected);
		}

		private void test(List<String> items1, List<String> items2,
			List<Integer> expected) {

			final var indexes = items1.findAll(items2);
			assertEquals(expected, indexes,
				format("%s.findAll(%s)", items1, items2));
		}
	}

	@DisplayName(".getDistinct()")
	@Nested
	class GetDistinctTests {
		@DisplayName("\uD83C\uDF9F")
		@Tests({
			"when some items are duplicate, returns distinct items;" +
				"[t, e, r, t, t, r, R, r];" +
				"[r, R, t, e]",
			"when all items are distinct, returns distinct items;" +
				"[t, m, j, E, d, S, s];" +
				"[j, d, s, t, m, E, S]",
			"when list is empty, returns empty list;" +
				"[];" +
				"[]"
		})
		void test(@StringList List<String> items, @StringList List<String> expected) {
			final var distinct = items.getDistinct();
			assertEquals(expected, distinct,
				format("%s.getDistinct()", items));
		}
	}

	@DisplayName(".reverse()")
	@Nested
	class ReverseTests {
		@DisplayName("\uD83D\uDC58")
		@Tests({
			"when list is not empty, returns its items in reverse order;" +
				"[t, g, s, W, s, a, Q];" +
				"[Q, a, s, W, s, g, t]",
			"when list is empty, returns empty list;" +
				"[];" +
				"[]"
		})
		void test(@StringList List<String> items, @StringList List<String> expected) {
			final var reversed = items.reverse();
			assertEquals(expected, reversed,
				format("%s.reverse()", items));
		}
	}

	@DisplayName(".convert(Function<T, R>)")
	@Nested
	class ConvertTests {
		@DisplayName("\uD83C\uDF5A")
		@Tests({
			"when no items convert to null, returns converted items;" +
				"[g, s, s, a, b, w];" +
				"[G, S, S, A, B, W]",
			"when some items convert to null, returns non-null converted items;" +
				"[g, S, s, A, b, W];" +
				"[G, S, B]",
			"when all items convert to null, returns empty list;" +
				"[G, S, S, A, B, W];" +
				"[]",
			"when list is empty, returns empty list;" +
				"[];" +
				"[]"
		})
		void test(@StringList List<String> items, @StringList List<String> expected) {
			final var converted = items.convert((item) -> {
				final var uppercase = item.toUpperCase();
				return item.equals(uppercase)
					? null
					: uppercase;
			});

			assertEquals(expected, converted,
				format("%s.convert(<to uppercase>)", items));
		}
	}

	@DisplayName(".bridge()")
	@Nested
	class BridgeTests {
		@DisplayName("\uD83C\uDF0D")
		@Tests({
			"when list is not empty, returns java.util.List;" +
				"[t, b, s, W, q, a, W];" +
				"[t, b, s, W, q, a, W]",
			"when list is empty, returns empty java.util.List;" +
				"[];" +
				"[]"
		})
		void test(@StringList List<String> items, @StringArray String[] expected) {
			final var bridged = items.bridge();
			assertEquals(Arrays.asList(expected), bridged,
				format("%s.bridge()", items));
		}
	}

	@DisplayName(".toArray()")
	@Nested
	class ToArrayTests {
		@DisplayName("\uD83C\uDF68")
		@Tests({
			"when list its not empty, returns items array;" +
				"[g, b, d, s, s, Q, g];" +
				"[g, b, d, s, s, Q, g]",
			"when list is empty, returns empty array;" +
				"[];" +
				"[]"
		})
		void test(@StringList List<String> items, @StringArray Object[] expected) {
			final var array = items.toArray();
			assertArrayEquals(expected, array,
				format("%s.toArray()", items));
		}
	}

	@DisplayName(".toArray(Class<? extends T[]>)")
	@Nested
	class ToArrayClassTests {
		@DisplayName("\uD83C\uDF68")
		@Tests({
			"when list its not empty, returns items array;" +
				"[h, j, M, f, f, h, Q];" +
				"[h, j, M, f, f, h, Q]",
			"when list is empty, returns empty array;" +
				"[];" +
				"[]"
		})
		void test(@StringList List<String> items, @StringArray String[] expected) {
			final var array = items.toArray();
			assertArrayEquals(expected, array,
				format("%s.toArray()", items));
		}
	}

	@DisplayName(".toString()")
	@Nested
	class ToStringTests {
		@DisplayName("\uD83D\uDDA5")
		@Tests({
			"when list is not empty, returns string representation;" +
				"[g, d, s, a, s, Q, s];" +
				"[g, d, s, a, s, Q, s]",
			"when list is empty, returns [];" +
				"[];" +
				"[]"
		})
		void test(@StringList List<String> items, String expected) {
			final var string = items.toString();
			assertEquals(expected, string,
				format("%s.toString()", items));
		}
	}
}

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(StringList.Converter.class)
@interface StringList {
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

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(IntList.Converter.class)
@interface IntList {
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

// created on Jul 10, 2019