package com.tsyba.core.collections;

import com.tsyba.core.collections.converter.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

class ListTests {
	@DisplayName("List(Collection<T>)")
	@Nested
	class NewCollectionTests {
		@DisplayName("\uD83C\uDF4B")
		@Tests({
			"when argument collection is not empty, creates list with items;" +
				"[f, V, h, Z, C, 8];" +
				"[f, V, h, Z, C, 8]",
			"when argument collection is empty, creates empty list;" +
				"[];" +
				"[]"
		})
		void test(@StringCollection Collection<String> items,
			@StringList List<String> expected) {

			final var created = new List<>(items);
			assertEquals(items.getCount(), created.store.items.length,
				format("new List<>(%s) created with incorrect capacity", expected));
			assertEquals(expected, created,
				format("new List<>(%s)", items));
		}
	}

	@DisplayName("List(T...)")
	@Nested
	class NewVarargsTests {
		@DisplayName("\uD83D\uDE82")
		@Tests({
			"when no argument items are null, creates list with all items;" +
				"[A, k, Z, e, j];" +
				"[A, k, Z, e, j]",
			"when some argument items are null, creates list with non-null items;" +
				"[g, null, null, T, null, q];" +
				"[g, T, q]",
			"when all argument items are null, creates empty list;" +
				"[null, null, null, null];" +
				"[]",
			"when argument array is empty, creates empty list;" +
				"[];" +
				"[]"
		})
		void test(@StringArray String[] items, @StringList List<String> expected) {
			final var created = new List<>(items);
			assertEquals(expected.getCount(), created.store.items.length,
				format("new List<>(%s) created with incorrect capacity", expected));
			assertEquals(expected, created,
				format("new List<>(%s)",
					Arrays.toString(items)));
		}
	}

	@DisplayName(".getCount()")
	@Nested
	class GetCountTests {
		@DisplayName("\uD83D\uDCFA")
		@Tests({
			"when list is not empty, returns item count;" +
				"[k, Q, 4, f, j, t, n];" +
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
	class GetIntTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when index is before valid range, fails;" +
				"[v, z, r, s, d, P, V]; -4;" +
				"null; -4 ∉ [0, 7)",
			"when index is 0, returns first item;" +
				"[0, O, 6, u, S]; 0;" +
				"0; null",
			"when index is within valid range, returns item at index;" +
				"[3, v, p, e, K, f, F]; 4;" +
				"K; null",
			"when index is last valid index, returns last item;" +
				"[o, V, R, q, h, i]; 5;" +
				"i; null",
			"when index is at valid index range end, fails;" +
				"[X, k, l, r]; 4;" +
				"null; 4 ∉ [0, 4)",
			"when index is after valid range, fails;" +
				"[l, V, W, 1, E, z, I, 4]; 12;" +
				"null; 12 ∉ [0, 8)"
		})
		void testListNotEmpty(@StringList List<String> items, int index,
			String expected1, @IndexRangeException Exception expected2) {
			test(items, index, expected1, expected2);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when index is before valid range, fails;" +
				"[]; -1;" +
				"null; -1 ∉ [0, 0)",
			"when index is 0, fails;" +
				"[]; 0;" +
				"null; 0 ∉ [0, 0)",
			"when index is after valid range, fails;" +
				"[]; 1;" +
				"null; 1 ∉ [0, 0)"
		})
		void testListEmpty(@StringList List<String> items, int index,
			String expected1, @IndexRangeException Exception expected2) {
			test(items, index, expected1, expected2);
		}

		private void test(List<String> items, int index, String expected1,
			Exception expected2) {

			try {
				final var item = items.get(index);
				assertEquals(expected1, item,
					format("%s.get(%d)", items, index));
			} catch (IndexNotInRangeException exception) {
				assertEquals(expected2, exception,
					format("%s.get(%d)", items, index));
			}
		}
	}

	@DisplayName(".getPrefix(int)")
	@Nested
	class GetPrefixTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when index is before valid range, fails;" +
				"[z, X, N, j, m, q, T, y]; -4;" +
				"null; -4 ∉ [0, 8)",
			"when index is 0, returns empty list;" +
				"[d, n, M, 6, h, X, p, k]; 0;" +
				"[]; null",
			"when index is within valid range, returns prefix;" +
				"[k, N, Y, Z, z, e, 3, 1]; 4;" +
				"[k, N, Y, Z]; null",
			"when index is last valid index, returns prefix;" +
				"[7, i, I, 0, g, h, C, v]; 7;" +
				"[7, i, I, 0, g, h, C]; null",
			"when index is at valid index range end, fails;" +
				"[6, f, V, S]; 4;" +
				"null; 4 ∉ [0, 4)",
			"when index is after valid range, fails;" +
				"[y, Y, S, c, f, 2, u]; 11;" +
				"null; 11 ∉ [0, 7)"
		})
		void testListNotEmpty(@StringList List<String> items, int index,
			@StringList List<String> expected1,
			@IndexRangeException Exception expected2) {
			test(items, index, expected1, expected2);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when index is before valid range, fails;" +
				"[]; -1;" +
				"null; -1 ∉ [0, 0)",
			"when index is 0, fails;" +
				"[]; 0;" +
				"null; 0 ∉ [0, 0)",
			"when index is after valid range, fails;" +
				"[]; 1;" +
				"null; 1 ∉ [0, 0)"
		})
		void testListEmpty(@StringList List<String> items, int index,
			@StringList List<String> expected1,
			@IndexRangeException Exception expected2) {
			test(items, index, expected1, expected2);
		}

		private void test(List<String> items, int index, List<String> expected1,
			Exception expected2) {

			try {
				final var prefix = items.getPrefix(index);
				assertEquals(expected1, prefix,
					format("%s.getPrefix(%d)", items, index));
			} catch (IndexNotInRangeException exception) {
				assertEquals(expected2, exception,
					format("%s.getPrefix(%d)", items, index));
			}
		}
	}

	@DisplayName(".getSuffix(int)")
	@Nested
	class GetSuffixTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when index is before valid range, fails;" +
				"[7, 1, f, 4, N, y, T, s]; -4;" +
				"null; -4 ∉ [0, 8)",
			"when index is 0, returns all items;" +
				"[4, N, M, b, U, f, V, w]; 0;" +
				"[4, N, M, b, U, f, V, w]; null",
			"when index is within valid range, returns suffix;" +
				"[n, k, o, p, d, X, r, D]; 4;" +
				"[d, X, r, D]; null",
			"when index is last valid index, returns suffix with last item;" +
				"[2, I, L, P, o, b, 1, A]; 7;" +
				"[A]; null",
			"when index is at valid index range end, fails;" +
				"[i, v, t, s]; 4;" +
				"null; 4 ∉ [0, 4)",
			"when index is after valid range, fails;" +
				"[X, n, S, z, 6, R, L]; 11;" +
				"null; 11 ∉ [0, 7)"
		})
		void testListNotEmpty(@StringList List<String> items, int index,
			@StringList List<String> expected1,
			@IndexRangeException Exception expected2) {
			test(items, index, expected1, expected2);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when index is before valid range, fails;" +
				"[]; -1;" +
				"null; -1 ∉ [0, 0)",
			"when index is 0, fails;" +
				"[]; 0;" +
				"null; 0 ∉ [0, 0)",
			"when index is after valid range, fails;" +
				"[]; 1;" +
				"null; 1 ∉ [0, 0)"
		})
		void testListEmpty(@StringList List<String> items, int index,
			@StringList List<String> expected1,
			@IndexRangeException Exception expected2) {
			test(items, index, expected1, expected2);
		}

		private void test(List<String> items, int index, List<String> expected1,
			Exception expected2) {

			try {
				final var suffix = items.getSuffix(index);
				assertEquals(expected1, suffix,
					format("%s.getSuffix(%d)", items, index));
			} catch (IndexNotInRangeException exception) {
				assertEquals(expected2, exception,
					format("%s.getSuffix(%d)", items, index));
			}
		}
	}

	@DisplayName(".get(IndexRange)")
	@Nested
	class GetIndexRangeTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when index range starts at 0 and ends within valid range, returns items in range;" +
				"[n, H, g, l, y, 6, C, R]; [0, 4);" +
				"[n, H, g, l]; null",
			"when index range is within valid range, returns items in range;" +
				"[y, 3, Z, 8, x, h, s, n]; [2, 6);" +
				"[Z, 8, x, h]; null",
			"when index range starts within valid range and ends at valid range end, returns items in range;" +
				"[E, r, a, N, w, d, G, C]; [6, 8);" +
				"[G, C]; null",
			"when index range equals valid range, returns all items;" +
				"[h, K, U, s, b, M, R, L]; [0, 8);" +
				"[h, K, U, s, b, M, R, L]; null",
			"when index range starts within valid range and ends after valid range end, fails;" +
				"[h, U, p, m, Q, 6, L, 4]; [6, 9);" +
				"null; [6, 9) ⊈ [0, 8)",
			"when index range starts and ends after valid range end, fails;" +
				"[X, r, 4, G, H, j, l, b]; [12, 21);" +
				"null; [12, 21) ⊈ [0, 8)",
			"when index range is empty, returns empty list;" +
				"[T, o, E, 2, Y, W, 8, A]; [0, 0);" +
				"[]; null"
		})
		void testListNotEmpty(@StringList List<String> items, @IntRange IndexRange range,
			@StringList List<String> expected1,
			@IndexRangeException Exception expected2) {
			test(items, range, expected1, expected2);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when index range is not empty, fails;" +
				"[]; [0, 1);" +
				"null; [0, 1) ⊈ [0, 0)",
			"when index range is empty, returns empty list;" +
				"[]; [0, 0);" +
				"[]; null"
		})
		void testListEmpty(@StringList List<String> items, @IntRange IndexRange range,
			@StringList List<String> expected1,
			@IndexRangeException Exception expected2) {
			test(items, range, expected1, expected2);
		}

		private void test(List<String> items, IndexRange range, List<String> expected1,
			Exception expected2) {

			try {
				final var returned = items.get(range);
				assertEquals(expected1, returned,
					format("%s.get(%s)", items, range));
			} catch (IndexRangeNotInRangeException exception) {
				assertEquals(expected2, exception,
					format("%s.get(%s)", items, range));
			}
		}
	}

	@DisplayName(".matchAll(Predicate<T>)")
	@Nested
	class MatchAllTests {
		@DisplayName("\uD83D\uDD31")
		@Tests({
			"when some items match, returns matching items;" +
				"[g, R, k, W, m, T];" +
				"[R, W, T]",
			"when all items match, returns matching items;" +
				"[F, B, N, U, Z, Y];" +
				"[F, B, N, U, Z, Y]",
			"when no items match, returns empty list;" +
				"[p, x, u, n, f, b];" +
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
	class FindAllTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when argument item is present multiple times, returns its indexes;" +
				"[c, M, e, M, k, w, M, p]; M;" +
				"[1, 3, 6]",
			"when argument item is present once, returns its index;" +
				"[1, t, s, u, P, U, D]; P;" +
				"[4]",
			"when argument item is absent, returns empty list;" +
				"[7, i, c, e, h, G, H]; x;" +
				"[]",
			"when argument item is null, returns empty list;" +
				"[8, a, b, h, X, s, g]; null;" +
				"[]"
		})
		void testListNotEmpty(@StringList List<String> items, String item,
			@IntList List<Integer> expected) {
			test(items, item, expected);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when argument item is not null, returns empty list;" +
				"[]; R;" +
				"[]",
			"when argument item is null, returns empty list;" +
				"[]; null;" +
				"[]"
		})
		void testListEmpty(@StringList List<String> items, String item,
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
	class FindAllSequenceTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when argument sequence is present multiple times, returns its indexes;" +
				"[v, E, L, m, G, o, E, L, y, E, L, z]; [E, L];" +
				"[1, 6, 9]",
			"when argument sequence is present once, returns its index;" +
				"[k, W, r, T, q, m, W, r, z]; [W, r, T];" +
				"[1]",
			"when argument sequence is absent, returns empty sequence;" +
				"[b, E, k, r, T, x, d, m]; [r, T, k];" +
				"[]",
			"when argument sequence is longer, returns empty sequence;" +
				"[m, N, o, b]; [m, N, o, b, P];" +
				"[]",
			"when argument sequence is empty, returns all indexes;" +
				"[Y, o, k, 2, R, e, T, s, W, n]; [];" +
				"[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]"
		})
		void testListNotEmpty(@StringList List<String> items1,
			@StringList List<String> items2, @IntList List<Integer> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when argument sequence is not empty, returns empty sequence;" +
				"[]; [t, r, E, k];" +
				"[]",
			"when argument sequence is empty, returns empty sequence;" +
				"[]; [];" +
				"[]"
		})
		void testListEmpty(@StringList List<String> items1,
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
			"when all items are distinct, returns all items;" +
				"[t, m, j, E, d, S, s];" +
				"[E, S, d, j, m, s, t]",
			"when some items are same, returns distinct items;" +
				"[t, e, r, t, t, r, R, r];" +
				"[R, e, r, t]",
			"when all items are same, returns distinct item;" +
				"[g, g, g, g, g, g, g];" +
				"[g]",
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
			"when list is not empty, returns items in reverse order;" +
				"[x, 9, 3, Z, 0, K, h];" +
				"[h, K, 0, Z, 3, 9, x]",
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

	@DisplayName(".iterate(Consumer<T>)")
	@Nested
	class IterateTests {
		@DisplayName("\uD83D\uDED2")
		@Tests({
			"when list is not empty, iterates items;" +
				"[q, 9, b, C, W, 3];" +
				"[q, 9, b, C, W, 3]",
			"when list is empty, does nothing;" +
				"[];" +
				"[]"
		})
		void test(@StringList List<String> items, @StringArray String[] expected) {
			final var iterated = new ArrayList<String>();
			final var returned = items.iterate(iterated::add);

			assertSame(items, returned,
				format("%s.iterate(Consumer<T>)", items));
			assertArrayEquals(expected,
				iterated.toArray(new String[]{}),
				format("%s.iterate(Consumer<T>)", items));
		}
	}

	@DisplayName(".enumerate(BiConsumer<Integer, T>)")
	@Nested
	class EnumerateTests {
		@DisplayName("\uD83D\uDCDA")
		@Tests({
			"when list is not empty, enumerates items;" +
				"[B, h, G, y, p, V];" +
				"[0:B, 1:h, 2:G, 3:y, 4:p, 5:V]",
			"when list is empty, does nothing;" +
				"[];" +
				"[]"
		})
		void test(@StringList List<String> items,
			@StringMap Map<String, String> expected) {

			final var enumerated = new HashMap<Integer, String>();
			final var returned = items.enumerate(enumerated::put);

			final var expected2 = new HashMap<Integer, String>();
			expected.iterate((key, value) ->
				expected2.put(Integer.parseInt(key), value));

			assertSame(items, returned,
				format("%s.enumerate(BiConsumer<Integer, T>)", items));
			assertEquals(expected2, enumerated,
				format("%s.enumerate(BiConsumer<Integer, T>)", items));
		}
	}

	@DisplayName(".convert(Function<T, R>)")
	@Nested
	class ConvertTests {
		@DisplayName("\uD83C\uDF5A")
		@Tests({
			"when no items convert to null, returns all converted items;" +
				"[v, n, b, g, x, p];" +
				"[V, N, B, G, X, P]",
			"when some items convert to null, returns non-null converted items;" +
				"[V, n, B, g, x, P];" +
				"[N, G, X]",
			"when all items convert to null, returns empty list;" +
				"[C, H, O, L, W, R];" +
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
			"when list is not empty, returns items in java.util.List;" +
				"[A, g, 9, 2, C, R, H];" +
				"[A, g, 9, 2, C, R, H]",
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

	@DisplayName(".iterator()")
	@Nested
	class IteratorTests {
		@DisplayName("\uD83E\uDDF2")
		@Tests({
			"when list is not empty, iterates items;" +
				"[H, P, F, l, R, 5];" +
				"[H, P, F, l, R, 5]",
			"when list is empty, does nothing;" +
				"[];" +
				"[]"
		})
		void test(@StringList List<String> items, @StringArray String[] expected) {
			final var iterated = new ArrayList<String>();
			items.iterator()
				.forEachRemaining(iterated::add);

			assertArrayEquals(expected,
				iterated.toArray(new String[]{}),
				format("%s.iterator()", items));
		}
	}

	@DisplayName(".iterator(int)")
	@Nested
	class IteratorIntTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when index is before valid range, fails;" +
				"[r, 2, z, A, E]; -2;" +
				"null; -2 ∉ [0, 5)",
			"when index is 0, iterates all items;" +
				"[C, u, 3, t, R, m]; 0;" +
				"[C, u, 3, t, R, m]; null",
			"when index is within valid range, iterates remaining items;" +
				"[D, q, 1, 8, v, T]; 2;" +
				"[1, 8, v, T]; null",
			"when index is last valid index, iterates last item;" +
				"[r, h, N, l, d]; 4;" +
				"[d]; null",
			"when index is at valid index range end, fails;" +
				"[p, L, O, N, l]; 5;" +
				"null; 5 ∉ [0, 5)",
			"when index is after valid range, fails;" +
				"[t, z, s, V, E, 1]; 12;" +
				"null; 12 ∉ [0, 6)"
		})
		void testListNotEmpty(@StringList List<String> items, int index,
			@StringArray String[] expected1, @IndexRangeException Exception expected2) {
			test(items, index, expected1, expected2);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when index is before valid range, fails;" +
				"[]; -1;" +
				"null; -1 ∉ [0, 0)",
			"when index is 0, fails;" +
				"[]; 0;" +
				"null; 0 ∉ [0, 0)",
			"when index is after valid range, fails;" +
				"[]; 3;" +
				"null; 3 ∉ [0, 0)"
		})
		void testListEmpty(@StringList List<String> items, int index,
			@StringArray String[] expected1, @IndexRangeException Exception expected2) {
			test(items, index, expected1, expected2);
		}

		private void test(List<String> items, int index, String[] expected1,
			Exception expected2) {

			try {
				final var iterated = new ArrayList<String>();
				final var iterator = items.iterator(index);
				iterator.forEachRemaining(iterated::add);

				assertArrayEquals(expected1,
					iterated.toArray(new String[]{}),
					format("%s.iterator(%d)", items, index));
			} catch (IndexNotInRangeException exception) {
				assertEquals(expected2, exception,
					format("%s.iterator(%d)", items, index));
			}
		}
	}

	@DisplayName(".toArray()")
	@Nested
	class ToArrayTests {
		@DisplayName("\uD83C\uDF68")
		@Tests({
			"when list is not empty, returns items in array;" +
				"[n, L, 8, F, S, 6, o];" +
				"[n, L, 8, F, S, 6, o]",
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

	@DisplayName(".toArray(Class<? extends T[]>)")
	@Nested
	class ToArrayClassTests {
		@DisplayName("\uD83E\uDDFA")
		@Tests({
			"when list is not empty, returns items in array;" +
				"[j, w, u, T, F, 5, M];" +
				"[j, w, u, T, F, 5, M]",
			"when list is empty, returns empty array;" +
				"[];" +
				"[]"
		})
		void test(@StringList List<String> items, @StringArray String[] expected) {
			final var array = items.toArray(String[].class);
			assertArrayEquals(expected, array,
				format("%s.toArray(Class<? extends T[]>)", items));
		}
	}

	@DisplayName(".equals(Object)")
	@Nested
	class EqualsTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when items equal argument list items, returns true;" +
				"[G, M, v, H, y, Z]; [G, M, v, H, y, Z];" +
				"true",
			"when some items differ from argument list items, returns false;" +
				"[n, Y, h, U, g, 5]; [n, Y, h, U, G, 5];" +
				"false",
			"when items equal argument list items but in different order, returns false;" +
				"[V, 3, 2, E, Q, n]; [Q, V, 2, 3, E, n];" +
				"false",
			"when no items equal argument list items, returns false;" +
				"[b, g, K, 8, o, u]; [3, O, l, p, N, 4];" +
				"false",
			"when list is shorter than argument list, returns false;" +
				"[j, D, r, B]; [j, D, r, B, p, R];" +
				"false",
			"when list is longer than argument list, returns false;" +
				"[H, P, Z, W, s, g]; [H, P, Z, W];" +
				"false",
			"when argument list is empty, returns false;" +
				"[P, 9, b, U, 7, q]; [];" +
				"false",
			"when argument list is null, returns false;" +
				"[6, X, I, 2]; null;" +
				"false"
		})
		void testListNotEmpty(@StringList List<String> items1,
			@StringList List<String> items2, boolean expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when argument list is not empty, returns false;" +
				"[]; [f, 8, m, Q, s, 3];" +
				"false",
			"when argument list is empty, returns true;" +
				"[]; [];" +
				"true",
			"when argument list is null, returns false;" +
				"[]; null;" +
				"false"
		})
		void testListEmpty(@StringList List<String> items1,
			@StringList List<String> items2, boolean expected) {
			test(items1, items2, expected);
		}

		private void test(List<String> items1, List<String> items2, boolean expected) {
			final var equals = items1.equals(items2);
			assertEquals(expected, equals,
				format("%s.equals(%s)", items1, items2));
		}
	}

	@DisplayName(".hashCode()")
	@Nested
	class HashCodeTests {
		@DisplayName("\uD83C\uDF69")
		@Tests({
			"when lists are equal, returns equal hash codes;" +
				"[q, W, 6, r, T, v]; [q, W, 6, r, T, v];" +
				"true",
			"when lists differ, returns different hash codes;" +
				"[Z, n, 4, K, s]; [Z, n, 4, K, s, m, Q];" +
				"false",
			"when one list is empty, returns different hash codes;" +
				"[]; [E, p, 7, c, a];" +
				"false",
			"when lists are empty, returns equal hash codes;" +
				"[]; [];" +
				"true"
		})
		void test(@StringList List<String> items1, @StringList List<String> items2,
			boolean expected) {

			if (expected) {
				assertEquals(items1.hashCode(), items2.hashCode(),
					format("%s.hashCode(), %s.hashCode()", items1, items2));
			} else {
				assertNotEquals(items1.hashCode(), items2.hashCode(),
					format("%s.hashCode(), %s.hashCode()", items1, items2));
			}
		}
	}

	@DisplayName(".toString()")
	@Nested
	class ToStringTests {
		@DisplayName("\uD83D\uDDA5")
		@Tests({
			"when list is not empty, returns string representation;" +
				"[a, e, 6, Z, 8, J, 5];" +
				"[a, e, 6, Z, 8, J, 5]",
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

// created on Jul 10, 2019
