package com.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class SequenceTests {
	@DisplayName(".getIndexRange()")
	@Nested
	class GetIndexRangeTests {
		@DisplayName("\uD83C\uDFC2")
		@Tests({
			"when sequence is not empty, returns index range;" +
				"[f, e, Q, e, m, s, d];" +
				"[0, 7)",
			"when sequence is empty, returns empty index range;" +
				"[];" +
				"[0, 0)"
		})
		void test(@TestSequence Sequence<String> items,
			@TestRange IndexRange expected) {

			final var range = items.getIndexRange();
			assertEquals(expected, range,
				format("%s.getIndexRange()", items));
		}
	}

	@DisplayName(".getFirst()")
	@Nested
	class GetFirstTests {
		@DisplayName("\uD83C\uDFC4")
		@Tests({
			"when sequence is not empty, returns first item;" +
				"[g, N, k, L, d, S];" +
				"g",
			"when sequence is empty, returns empty Optional;" +
				"[];" +
				"null"
		})
		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		void test(@TestSequence Sequence<String> items,
			@StringOptional Optional<String> expected) {

			final var item = items.getFirst();
			assertEquals(expected, item,
				format("%s.getFirst()", items));
		}
	}

	@DisplayName(".getLast()")
	@Nested
	class GetLastTests {
		@DisplayName("\uD83D\uDC2C")
		@Tests({
			"when sequence is not empty, returns last item;" +
				"[g, N, k, L, d, S];" +
				"S",
			"when sequence is empty, returns empty Optional;" +
				"[];" +
				"null"
		})
		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		void test(@TestSequence Sequence<String> items,
			@StringOptional Optional<String> expected) {

			final var item = items.getLast();
			assertEquals(expected, item,
				format("%s.getLast()", items));
		}
	}

	@DisplayName(".get(int)")
	@Nested
	class GetTests {
		@DisplayName("when sequence is not empty")
		@Tests({
			"when index is within valid range, returns item at index;" +
				"[g, B, s, E, q, s, K]; 4;" +
				"q; null",
			"when index is before valid range, fails;" +
				"[g, B, s, E, q, s, K]; -4;" +
				"null; -4 ∉ [0, 7)",
			"when index is after valid range, fails;" +
				"[g, B, s, E, q, s, K]; 12;" +
				"null; 12 ∉ [0, 7)"
		})
		void testNotEmpty(@TestSequence Sequence<String> items, int index,
			String expected1, @IndexRangeException Exception expected2) {
			test(items, index, expected1, expected2);
		}

		@DisplayName("when sequence is empty")
		@Tests({
			"when index is before valid range, fails;" +
				"[]; -1;" +
				"null; -1 ∉ [0, 0)",
			"when index is after valid range, fails;" +
				"[]; 0;" +
				"null; 0 ∉ [0, 0)"
		})
		void testEmpty(@TestSequence Sequence<String> items, int index,
			String expected1, @IndexRangeException Exception expected2) {
			test(items, index, expected1, expected2);
		}

		private void test(Sequence<String> items, int index, String expected1,
			Exception expected2) {

			try {
				final var item = items.get(index);
				assertEquals(expected1, item,
					format("%s.get(%d)", item, index));
			} catch (Exception exception) {
				if (expected2 == null) {
					throw exception;
				} else {
					assertEquals(expected2, exception,
						format("%s.get(%d)", items, index));
				}
			}
		}
	}

	@DisplayName(".guard(int)")
	@Nested
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	class GuardTests {
		@DisplayName("when sequence is not empty")
		@Tests({
			"when index is within valid range, returns index;" +
				"[g, T, s, l, I, o]; 3;" +
				"3",
			"when index is before valid range, returns empty optional;" +
				"[g, T, s, l, I, o]; -3;" +
				"null",
			"when index is after valid range, returns empty optional;" +
				"[g, T, s, l, I, o]; 13;" +
				"null",
		})
		void testNotEmpty(@TestSequence Sequence<String> items, int index,
			@IntOptional Optional<Integer> expected) {
			test(items, index, expected);
		}

		@DisplayName("when sequence is empty")
		@Tests({
			"when index is before valid range, returns empty optional;" +
				"[]; -1;" +
				"null",
			"when index is after valid range, returns empty optional;" +
				"[]; 0;" +
				"null",
		})
		void testEmpty(@TestSequence Sequence<String> items, int index,
			@IntOptional Optional<Integer> expected) {
			test(items, index, expected);
		}

		private void test(Sequence<String> items, int index,
			Optional<Integer> expected) {

			final var guarded = items.guard(index);
			assertEquals(expected, guarded,
				format("%s.guard(%d)", items, index));
		}
	}

	@DisplayName(".guard(IndexRange)")
	@Nested
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	class GuardIndexRangeTests {
		@DisplayName("when sequence is not empty")
		@Tests({
			"when index range is within valid range, returns index range;" +
				"[g, I, m, k, L, s, D, n]; [3, 5);" +
				"[3, 5)",
			"when index range starts after valid range, returns empty optional;" +
				"[g, I, m, k, L, s, D, n]; [8, 11);" +
				"null",
			"when index range ends after valid range, returns empty optional;" +
				"[g, I, m, k, L, s, D, n]; [3, 15);" +
				"null"
		})
		void testNotEmpty(@TestSequence Sequence<String> items,
			@TestRange IndexRange range,
			@TestRangeOptional Optional<IndexRange> expected) {
			test(items, range, expected);
		}

		@DisplayName("when sequence is empty")
		@Tests({
			"when index range starts after valid range, returns empty optional;" +
				"[]; [0, 2);" +
				"null"
		})
		void testEmpty(@TestSequence Sequence<String> items,
			@TestRange IndexRange range,
			@TestRangeOptional Optional<IndexRange> expected) {
			test(items, range, expected);
		}

		private void test(Sequence<String> items, IndexRange range,
			Optional<IndexRange> expected) {

			final var guarded = items.guard(range);
			assertEquals(expected, guarded,
				format("%s.guard(%s)", items, range));
		}
	}

	@DisplayName(".matchFirst(Predicate<T>)")
	@Nested
	class MatchFirstTests {
		@DisplayName("\uD83D\uDEF6")
		@Tests({
			"when some item matches, returns matching item;" +
				"[r, t, g, W, s, a];" +
				"W",
			"when several items match, returns first matching item;" +
				"[r, t, T, S, a, Q];" +
				"T",
			"when no item matches, returns empty optional;" +
				"[g, m, i, s, d, q];" +
				"null",
			"when sequence is empty, returns empty optional;" +
				"[];" +
				"null"
		})
		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		void test(@TestSequence Sequence<String> items,
			@StringOptional Optional<String> expected) {

			final var matched = items.matchFirst((item) -> {
				return item.toUpperCase()
					.equals(item);
			});

			assertEquals(expected, matched,
				format("%s.matchFirst(<is uppercase>)", items));
		}
	}

	@DisplayName(".findFirst(T)")
	@Nested
	class FindFirstTests {
		@DisplayName("\uD83C\uDF5D")
		@Tests({
			"when item is present, returns its index;" +
				"[j, M, n, K, l, O, p]; l;" +
				"4",
			"when item is present multiple times, returns its first index;" +
				"[j, M, l, K, l, O, l]; l;" +
				"2",
			"when item is absent, returns empty optional;" +
				"[j, M, n, K, l, O, p]; L;" +
				"null",
			"when sequence is empty, returns empty optional;" +
				"[]; l;" +
				"null"
		})
		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		void test(@TestSequence Sequence<String> items, String item,
			@IntOptional Optional<Integer> expected) {

			final var index = items.findFirst(item);
			assertEquals(expected, index,
				format("%s.findFirst(%s)", items, item));
		}
	}

	@DisplayName(".findFirst(Predicate<T>)")
	@Nested
	class FindFirstPredicateTests {
		@DisplayName("\uD83C\uDFDC")
		@Tests({
			"when item is present, returns its index;" +
				"[o, k, p, d, s, S, n];" +
				"5",
			"when item is present multiple times, returns its first index;" +
				"[o, k, p, D, s, S, N];" +
				"3",
			"when item is absent, returns empty optional;" +
				"[o, k, p, d, s, s, n];" +
				"null",
			"when sequence is empty, returns empty optional;" +
				"[];" +
				"null"
		})
		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		void test(@TestSequence Sequence<String> items,
			@IntOptional Optional<Integer> expected) {

			final var index = items.findFirst((item) ->
				item.toUpperCase()
					.equals(item));

			assertEquals(expected, index,
				format("%s.findFirst(<is uppercase>)", items));
		}
	}

	@DisplayName(".findFirst(Sequence<T>)")
	@Nested
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	class FindFirstSequence {
		@DisplayName("when sequence is not empty")
		@Tests({
			"when sequence is present, returns its index;" +
				"[u, Y, k, L, m, n, F, s, d]; [L, m, n];" +
				"3",
			"when sequence is present multiple times, returns its first index;" +
				"[u, L, m, n, y, F, L, m, n, g, S]; [L, m, n];" +
				"1",
			"when sequence items present in difference order, returns empty optional;" +
				"[u, Y, k, L, m, n, F, s, d]; [m, L, n];" +
				"null",
			"when sequence is absent, returns empty optional;" +
				"[u, Y, k, L, m, n, F, s, d]; [L, m, N];" +
				"null",
			"when argument sequence is empty, returns first index;" +
				"[u, Y, k, L, m, n, F, s, d]; [];" +
				"0"
		})
		void testNotEmpty(@TestSequence Sequence<String> items1,
			@TestSequence Sequence<String> items2,
			@IntOptional Optional<Integer> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when sequence is empty")
		@Tests({
			"when argument sequence is not empty, returns empty optional;" +
				"[]; [t, r, E];" +
				"null",
			"when argument sequence is empty, returns empty optional;" +
				"[]; [];" +
				"null"
		})
		void testEmpty(@TestSequence Sequence<String> items1,
			@TestSequence Sequence<String> items2,
			@IntOptional Optional<Integer> expected) {
			test(items1, items2, expected);
		}

		private void test(Sequence<String> items1, Sequence<String> items2,
			Optional<Integer> expected) {

			final var index = items1.findFirst(items2);
			assertEquals(expected, index,
				format("%s.findFirst(%s)", items1, items2));
		}
	}

	@DisplayName(".enumerate(BiPredicate<Integer, T>)")
	@Nested
	class EnumerateTests {
		@DisplayName("\uD83C\uDF26")
		@Tests({
			"when sequence is not empty, enumerates items;" +
				"[g, U, m, I, l, M];" +
				"[0:g, 1:U, 2:m, 3:I, 4:l, 5:M]",
			"when sequence is empty, does nothing;" +
				"[];" +
				"[]"
		})
		void test(@TestSequence Sequence<String> items,
			@StringMap Map<String, String> expected) {

			final var enumerated = new MutableMap<Integer, String>();
			final var returned = items.enumerate(enumerated::set);

			final var expected2 = expected.convert((key, value) ->
				new Map.Entry<>(Integer.parseInt(key), value));

			assertSame(items, returned,
				format("%s.enumerate(BiPredicate<Integer, T>)", items));
			assertEquals(expected2, enumerated,
				format("%s.enumerate(BiPredicate<Integer, T>)", items));
		}
	}
}

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(TestSequence.Converter.class)
@interface TestSequence {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, Sequence> {
		protected Converter() {
			super(String.class, Sequence.class);
		}

		@Override
		protected Sequence<String> convert(String s) throws ArgumentConversionException {
			final var items = new StringArray.Converter()
				.convert(s);

			return new Sequence<>() {
				@Override
				public Sequence<String> getPrefix(int index) {
					return null;
				}

				@Override
				public Sequence<String> getSuffix(int index) {
					return null;
				}

				@Override
				public Sequence<String> get(IndexRange range) {
					return null;
				}

				@Override
				public Sequence<String> matchAll(Predicate<String> condition) {
					return null;
				}

				@Override
				public Sequence<Integer> find(String item) {
					return null;
				}

				@Override
				public Sequence<Integer> find(Sequence<String> items) {
					return null;
				}

				@Override
				public Collection<String> getDistinct() {
					return null;
				}

				@Override
				public Sequence<String> reverse() {
					return null;
				}

				@Override
				public <R> Sequence<R> convert(Function<String, R> converter) {
					return null;
				}

				@Override
				public Iterator<String> iterator() {
					return new Iterator<>() {
						private int index = 0;

						@Override
						public boolean hasNext() {
							return index < items.length;
						}

						@Override
						public String next() {
							final var item = items[index];
							++index;

							return item;
						}
					};
				}
			};
		}
	}
}

@ConvertWith(IntOptional.Converter.class)
@Retention(RetentionPolicy.RUNTIME)
@interface IntOptional {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, Optional> {
		protected Converter() {
			super(String.class, Optional.class);
		}

		@Override
		protected Optional<Integer> convert(String s) throws ArgumentConversionException {
			return Optional.ofNullable(s)
				.map(Integer::parseInt);
		}
	}
}
