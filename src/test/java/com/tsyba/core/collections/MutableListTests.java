package com.tsyba.core.collections;

import com.tsyba.core.collections.converter.IndexRangeException;
import com.tsyba.core.collections.converter.StringArray;
import com.tsyba.core.collections.converter.StringList;
import com.tsyba.core.collections.converter.StringMutableList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.Arrays;

import static com.tsyba.core.collections.MutableList.minimumCapacity;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

class MutableListTests {
	@DisplayName("MutableList(Collection<T>)")
	@Nested
	class NewCollectionTests {
		@DisplayName("\uD83E\uDD12")
		@Tests({
			"when argument list is not empty, creates list with items;" +
				"[p, g, F, P, F];" +
				"[p, g, F, P, F]",
			"when argument list is empty, creates empty list;" +
				"[];" +
				"[]"
		})
		void test(@StringList List<String> items,
			@StringMutableList MutableList<String> expected) {

			final var created = new MutableList<>(items);
			assertEquals(minimumCapacity, created.store.items.length,
				format("new MutableList<>(%s) created with incorrect capacity", items));
			assertEquals(expected, created,
				format("new MutableList<>(%s)", items));
		}
	}

	@DisplayName("MutableList(T...)")
	@Nested
	class NewVarargsTests {
		@DisplayName("\uD83D\uDC57")
		@Tests({
			"when no argument items are null, creates list with all items;" +
				"[b, d, Q, P, G];" +
				"[b, d, Q, P, G]",
			"when some argument items are null, creates list with non-null items;" +
				"[null, B, null, null, G, null];" +
				"[B, G]",
			"when all argument items are null, creates empty list;" +
				"[null, null, null, null];" +
				"[]",
			"when argument array is empty, creates empty list;" +
				"[];" +
				"[]"
		})
		void test(@StringArray String[] items,
			@StringMutableList MutableList<String> expected) {

			final var created = new MutableList<>(items);
			assertEquals(minimumCapacity, created.store.items.length,
				format("new MutableList<>(%s) created with incorrect capacity",
					Arrays.toString(items)));
			assertEquals(expected, created,
				format("new MutableList<>(%s)", Arrays.toString(items)));
		}
	}

	@DisplayName(".set(int, T)")
	@Nested
	class SetTests {
		@DisplayName("when list is not empty")
		@Nested
		class NotEmptyListTests {
			@DisplayName("when argument item is not null")
			@Tests({
				"when index is before valid range, fails;" +
					"[m, f, E, s, a]; -2; O;" +
					"null; -2 ∉ [0, 5)",
				"when index is 0, replaces first item;" +
					"[g, R, s, a, W]; 0; P;" +
					"[P, R, s, a, W]; null",
				"when index is within valid range, replaces item at index;" +
					"[g, E, x, s]; 2; O;" +
					"[g, E, O, s]; null",
				"when index is last valid index, replaces last item;" +
					"[y, T, r, s, a, A]; 5; g;" +
					"[y, T, r, s, a, g]; null",
				"when index is at valid range end, fails;" +
					"[n, G, s, 8]; 4; T;" +
					"null; 4 ∉ [0, 4)",
				"when index is after valid range, fails;" +
					"[m, M, s, n, a]; 7; B;" +
					"null; 7 ∉ [0, 5)"
			})
			void testItemNotNull(@StringMutableList MutableList<String> items,
				int index, String item,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items, index, item, expected1, expected2);
			}

			@DisplayName("when argument item is null")
			@Tests({
				"when index is before valid range, fails;" +
					"[i, F, E, s, a, g]; -6; null;" +
					"null; -6 ∉ [0, 6)",
				"when index is 0, does nothing;" +
					"[g, R, s, 4]; 0; null;" +
					"[g, R, s, 4]; null",
				"when index is within valid range, does nothing;" +
					"[g, E, x, s]; 2; null;" +
					"[g, E, x, s]; null",
				"when index is last valid index, does nothing;" +
					"[g, r, A, s, A]; 4; null;" +
					"[g, r, A, s, A]; null",
				"when index is at valid range end, fails;" +
					"[g, V, 9, u]; 4; null;" +
					"null; 4 ∉ [0, 4)",
				"when index is after valid range, fails;" +
					"[k, f, Q, 2]; 4; null;" +
					"null; 4 ∉ [0, 4)"
			})
			void testItemNull(@StringMutableList MutableList<String> items,
				int index, String item,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items, index, item, expected1, expected2);
			}
		}

		@DisplayName("when list is empty")
		@Nested
		class EmptyListTests {
			@DisplayName("when argument item is not null")
			@Tests({
				"when index is before valid range, fails;" +
					"[]; -2; O;" +
					"null; -2 ∉ [0, 0)",
				"when index is 0, fails;" +
					"[]; 0; P;" +
					"null; 0 ∉ [0, 0)",
				"when index is after valid range, fails;" +
					"[]; 1; f;" +
					"null; 1 ∉ [0, 0)"
			})
			void testItemNotNull(@StringMutableList MutableList<String> items,
				int index, String item,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items, index, item, expected1, expected2);
			}

			@DisplayName("when argument item is null")
			@Tests({
				"when index is before valid range, fails;" +
					"[]; -1; null;" +
					"null; -1 ∉ [0, 0)",
				"when index is 0, fails;" +
					"[]; 0; null;" +
					"null; 0 ∉ [0, 0)",
				"when index is after valid range, fails;" +
					"[]; 4; null;" +
					"null; 4 ∉ [0, 0)"
			})
			void testItemNull(@StringMutableList MutableList<String> items,
				int index, String item,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items, index, item, expected1, expected2);
			}
		}

		private void test(MutableList<String> items, int index, String item,
			MutableList<String> expected1, Exception expected2) {

			try {
				final var returned = items.set(index, item);
				assertSame(items, returned,
					format("%s.set(%d, %s)", items, index, item));
				assertEquals(expected1, items,
					format("%s.set(%d, %s)", items, index, item));
			} catch (Exception exception) {
				assertNull(expected1);
				assertEquals(expected2, exception,
					format("%s.set(%d, %s)", items, index, item));
			}
		}
	}

	@DisplayName(".prepend(T)")
	@Nested
	class PrependTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when argument item is not null, prepends item;" +
				"[v, B, f, 6]; g;" +
				"[g, v, B, f, 6]",
			"when argument item is null, does nothing;" +
				"[v, B, f, 6]; null;" +
				"[v, B, f, 6]"
		})
		void testListNotEmpty(@StringMutableList MutableList<String> items, String item,
			@StringMutableList MutableList<String> expected) {
			test(items, item, expected);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when argument item is not null, prepends item;" +
				"[]; g;" +
				"[g]",
			"when argument item is null, does nothing;" +
				"[]; null;" +
				"[]"
		})
		void testListEmpty(@StringMutableList MutableList<String> items, String item,
			@StringMutableList MutableList<String> expected) {
			test(items, item, expected);
		}

		private void test(MutableList<String> items, String item,
			MutableList<String> expected) {

			final var returned = items.prepend(item);
			assertSame(items, returned,
				format("%s.prepend(%s)", items, item));
			assertEquals(expected, items,
				format("%s.prepend(%s)", items, item));
		}
	}

	@DisplayName(".prepend(T...)")
	@Nested
	class PrependVarargsTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when no argument items are null, prepends all items;" +
				"[h, V, d, 9]; [n, M, k, 6];" +
				"[n, M, k, 6, h, V, d, 9]",
			"when some argument items are null, prepends non-null items;" +
				"[g, H, p, 4]; [null, n, null, O, null, 7];" +
				"[n, O, 7, g, H, p, 4]",
			"when all argument items are null, does nothing;" +
				"[f, E, q, 2]; [null, null, null, null];" +
				"[f, E, q, 2]",
			"when argument array is empty, does nothing;" +
				"[b, R, O, 5]; [];" +
				"[b, R, O, 5]"
		})
		void testListNotEmpty(@StringMutableList MutableList<String> items1,
			@StringArray String[] items2,
			@StringMutableList MutableList<String> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when no argument items are null, prepends all items;" +
				"[]; [n, M, k, 6];" +
				"[n, M, k, 6]",
			"when some argument items are null, prepends non-null items;" +
				"[]; [null, n, null, O, null, 7];" +
				"[n, O, 7]",
			"when all argument items are null, does nothing;" +
				"[]; [null, null, null, null];" +
				"[]",
			"when argument array is empty, does nothing;" +
				"[]; [];" +
				"[]"
		})
		void testListEmpty(@StringMutableList MutableList<String> items1,
			@StringArray String[] items2,
			@StringMutableList MutableList<String> expected) {
			test(items1, items2, expected);
		}

		private void test(MutableList<String> items1, String[] items2,
			MutableList<String> expected) {

			final var returned = items1.prepend(items2);
			assertSame(items1, returned,
				format("%s.prepend(%s)", items1,
					Arrays.toString(items2)));
			assertEquals(expected, items1,
				format("%s.prepend(%s)", items1,
					Arrays.toString(items2)));
		}
	}

	@DisplayName(".prepend(List<T>)")
	@Nested
	class PrependListTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when argument list is not empty, prepends items;" +
				"[G, f, w, 8]; [F, y, a, 3];" +
				"[F, y, a, 3, G, f, w, 8]",
			"when argument list is empty, does nothing;" +
				"[G, f, w, 8]; [];" +
				"[G, f, w, 8]"
		})
		void testListNotEmpty(@StringMutableList MutableList<String> items1,
			@StringList List<String> items2,
			@StringMutableList MutableList<String> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when argument list is not empty, prepends items;" +
				"[]; [V, Q, P, 1];" +
				"[V, Q, P, 1]",
			"when argument list is empty, does nothing;" +
				"[]; [];" +
				"[]"
		})
		void testListEmpty(@StringMutableList MutableList<String> items1,
			@StringList List<String> items2,
			@StringMutableList MutableList<String> expected) {
			test(items1, items2, expected);
		}

		private void test(MutableList<String> items1, List<String> items2,
			MutableList<String> expected) {

			final var returned = items1.prepend(items2);
			assertSame(items1, returned,
				format("%s.prepend(%s)", items1, items2));
			assertEquals(expected, items1,
				format("%s.prepend(%s)", items1, items2));
		}
	}

	@DisplayName(".append(T)")
	@Nested
	class AppendTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when argument item is not null, appends item;" +
				"[v, b, f, 6]; g;" +
				"[v, b, f, 6, g]",
			"when argument item is null, does nothing;" +
				"[v, b, f, 6]; null;" +
				"[v, b, f, 6]"
		})
		void testListNotEmpty(@StringMutableList MutableList<String> items, String item,
			@StringMutableList MutableList<String> expected) {
			test(items, item, expected);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when argument item is not null, appends item;" +
				"[]; g;" +
				"[g]",
			"when argument item is null, does nothing;" +
				"[]; null;" +
				"[]"
		})
		void testListEmpty(@StringMutableList MutableList<String> items, String item,
			@StringMutableList MutableList<String> expected) {
			test(items, item, expected);
		}

		private void test(MutableList<String> items, String item,
			MutableList<String> expected) {

			final var returned = items.append(item);
			assertSame(items, returned,
				format("%s.append(%s)", items, item));
			assertEquals(expected, items,
				format("%s.append(%s)", items, item));
		}
	}

	@DisplayName(".append(T...)")
	@Nested
	class AppendVarargsTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when no argument items are null, appends all items;" +
				"[h, V, d, 3]; [n, M, k, 6];" +
				"[h, V, d, 3, n, M, k, 6]",
			"when some argument items are null, appends non-null items;" +
				"[g, H, p, 4]; [null, n, null, O, null, 7];" +
				"[g, H, p, 4, n, O, 7]",
			"when all argument items are null, does nothing;" +
				"[f, E, q, 2]; [null, null, null, null];" +
				"[f, E, q, 2]",
			"when argument array is empty, does nothing;" +
				"[b, R, O, 5]; [];" +
				"[b, R, O, 5]"
		})
		void testListNotEmpty(@StringMutableList MutableList<String> items1,
			@StringArray String[] items2,
			@StringMutableList MutableList<String> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when no argument items are null, appends all items;" +
				"[]; [n, M, k, 6];" +
				"[n, M, k, 6]",
			"when some argument items are null, appends non-null items;" +
				"[]; [null, n, null, O, null, 7];" +
				"[n, O, 7]",
			"when all argument items are null, does nothing;" +
				"[]; [null, null, null, null];" +
				"[]",
			"when argument array is empty, does nothing;" +
				"[]; [];" +
				"[]"
		})
		void testListEmpty(@StringMutableList MutableList<String> items1,
			@StringArray String[] items2,
			@StringMutableList MutableList<String> expected) {
			test(items1, items2, expected);
		}

		private void test(MutableList<String> items1, String[] items2,
			MutableList<String> expected) {

			final var returned = items1.append(items2);
			assertSame(items1, returned,
				format("%s.append(%s)", items1,
					Arrays.toString(items2)));
			assertEquals(expected, items1,
				format("%s.append(%s)", items1,
					Arrays.toString(items2)));
		}
	}

	@DisplayName(".append(List<T>)")
	@Nested
	class AppendListTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when argument list is not empty, appends items;" +
				"[G, f, w, 8]; [F, y, a, 3];" +
				"[G, f, w, 8, F, y, a, 3]",
			"when argument list is empty, does nothing;" +
				"[G, f, w, 8]; [];" +
				"[G, f, w, 8]"
		})
		void testListNotEmpty(@StringMutableList MutableList<String> items1,
			@StringList List<String> items2,
			@StringMutableList MutableList<String> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when argument list is not empty, appends items;" +
				"[]; [F, y, a, 3];" +
				"[F, y, a, 3]",
			"when argument list is empty, does nothing;" +
				"[]; [];" +
				"[]"
		})
		void testListEmpty(@StringMutableList MutableList<String> items1,
			@StringList List<String> items2,
			@StringMutableList MutableList<String> expected) {
			test(items1, items2, expected);
		}

		private void test(MutableList<String> items1, List<String> items2,
			MutableList<String> expected) {

			final var returned = items1.append(items2);
			assertSame(items1, returned,
				format("%s.append(%s)", items1, items2));
			assertEquals(expected, items1,
				format("%s.append(%s)", items1, items2));
		}
	}

	@DisplayName(".insert(int, T)")
	@Nested
	class InsertTests {
		@DisplayName("when list is not empty")
		@Nested
		class NotEmptyListTests {
			@DisplayName("when argument item is not null")
			@Tests({
				"when index is before valid range, fails;" +
					"[r, e, q, P, L, a]; -4; u;" +
					"null; -4 ∉ [0, 6)",
				"when index is 0, prepends item;" +
					"[g, R, s, 6]; 0; L;" +
					"[L, g, R, s, 6]; null",
				"when index is within valid range, inserts item at index;" +
					"[g, I, K, s, A]; 2; u;" +
					"[g, I, u, K, s, A]; null",
				"when index is last valid index, inserts item at index;" +
					"[g, f, s, J, H]; 4; K;" +
					"[g, f, s, J, K, H]; null",
				"when index is at valid range end, fails;" +
					"[b, G, h, n]; 4; N;" +
					"null; 4 ∉ [0, 4)",
				"when index is after valid range end, fails;" +
					"[e, f, Q, s, O, l]; 8; u;" +
					"null; 8 ∉ [0, 6)"
			})
			void testItemNotNull(@StringMutableList MutableList<String> items,
				int index, String item, @StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items, index, item, expected1, expected2);
			}

			@DisplayName("when argument item is null")
			@Tests({
				"when index is before valid range, fails;" +
					"[r, P, y, 3]; -2; null;" +
					"null; -2 ∉ [0, 4)",
				"when index is 0, does nothing;" +
					"[p, K, T, s]; 0; null;" +
					"[p, K, T, s]; null",
				"when index is within valid range, does nothing;" +
					"[g, I, K, s, A]; 2; null;" +
					"[g, I, K, s, A]; null",
				"when index is last valid index, does nothing;" +
					"[n, M, l, L, L]; 4; null;" +
					"[n, M, l, L, L]; null",
				"when index is at valid range end, fails;" +
					"[b, F, 7, c]; 4; null;" +
					"null; 4 ∉ [0, 4)",
				"when index is after valid range end, fails;" +
					"[j, s, Q, s]; 10; null;" +
					"null; 10 ∉ [0, 4)"
			})
			void testItemNull(@StringMutableList MutableList<String> items,
				int index, String item, @StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items, index, item, expected1, expected2);
			}
		}

		@DisplayName("when list is empty")
		@Nested
		class EmptyListTests {
			@DisplayName("when argument item is not null")
			@Tests({
				"when index is before valid range, fails;" +
					"[]; -4; u;" +
					"null; -4 ∉ [0, 0)",
				"when index is 0, fails;" +
					"[]; 0; L;" +
					"null; 0 ∉ [0, 0)",
				"when index is after valid range end, fails;" +
					"[]; 8; u;" +
					"null; 8 ∉ [0, 0)"
			})
			void testItemNotNull(@StringMutableList MutableList<String> items,
				int index, String item, @StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items, index, item, expected1, expected2);
			}

			@DisplayName("when argument item is null")
			@Tests({
				"when index is before valid range, fails;" +
					"[]; -2; null;" +
					"null; -2 ∉ [0, 0)",
				"when index is 0, fails;" +
					"[]; 0; null;" +
					"null; 0 ∉ [0, 0)",
				"when index is after valid range end, fails;" +
					"[]; 10; null;" +
					"null; 10 ∉ [0, 0)"
			})
			void testItemNull(@StringMutableList MutableList<String> items,
				int index, String item, @StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items, index, item, expected1, expected2);
			}
		}

		private void test(MutableList<String> items, int index, String item,
			MutableList<String> expected1, Exception expected2) {

			try {
				final var returned = items.insert(index, item);
				assertSame(items, returned,
					format("%s.insert(%d, %s)", items, index, item));
				assertEquals(expected1, items,
					format("%s.insert(%d, %s)", items, index, item));
			} catch (IndexNotInRangeException exception) {
				assertNull(expected1);
				assertEquals(expected2, exception,
					format("%s.insert(%d, %s)", items, index, item));
			}
		}
	}

	@DisplayName(".insert(int, T...)")
	@Nested
	class InsertVarargsTests {
		@DisplayName("when list is not empty")
		@Nested
		class NotEmptyListTests {
			@DisplayName("when no argument items are null")
			@Tests({
				"when index is before valid range, fails;" +
					"[g, U, s, A, 5]; -5; [b, E, 9, k];" +
					"null; -5 ∉ [0, 5)",
				"when index is 0, prepends all items;" +
					"[b, a, s, R, 7]; 0; [F, y, 2, i];" +
					"[F, y, 2, i, b, a, s, R, 7]; null",
				"when index is within valid range, inserts items at index;" +
					"[v, F, G, h, 6]; 2; [K, q, 3, d];" +
					"[v, F, K, q, 3, d, G, h, 6]; null",
				"when index is last valid index, inserts items at index;" +
					"[g, R, t, y, K]; 4; [o, 5, c, m];" +
					"[g, R, t, y, o, 5, c, m, K]; null",
				"when index is at valid range end, fails;" +
					"[m, e, q, A, 8]; 5; [o, s, 4, a];" +
					"null; 5 ∉ [0, 5)",
				"when index is after valid range, fails;" +
					"[g, R, K, u, 2]; 9; [o, b, s, 6];" +
					"null; 9 ∉ [0, 5)"
			})
			void testNoItemsNull(@StringMutableList MutableList<String> items1,
				int index, @StringArray String[] items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, index, items2, expected1, expected2);
			}

			@DisplayName("when some argument items are null")
			@Tests({
				"when index is before valid range, fails;" +
					"[b, R, s, 4, N]; -2; [null, b, null, s, W, 6];" +
					"null; -2 ∉ [0, 5)",
				"when index is 0, prepends non-null items;" +
					"[g, R, l, K, 5]; 0; [k, null, Q, null, null];" +
					"[k, Q, g, R, l, K, 5]; null",
				"when index is within valid range, inserts non-null items at index;" +
					"[b, a, s, R, 7]; 2; [F, null, null, D, 1];" +
					"[b, a, F, D, 1, s, R, 7]; null",
				"when index is last valid index, inserts non-null items at index;" +
					"[b, N, m, 6, w]; 4; [k, null, O, g];" +
					"[b, N, m, 6, k, O, g, w]; null",
				"when index is at valid range end, fails;" +
					"[o, P, u, 9]; 4; [null, K, null, 3];" +
					"null; 4 ∉ [0, 4)",
				"when index is after valid range, fails;" +
					"[o, E, c, 1]; 6; [null, null, w, Q];" +
					"null; 6 ∉ [0, 4)"
			})
			void testSomeItemsNull(@StringMutableList MutableList<String> items1,
				int index, @StringArray String[] items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, index, items2, expected1, expected2);
			}

			@DisplayName("when all argument items are null")
			@Tests({
				"when index is before valid range, fails;" +
					"[m, I, s, 3]; -1; [null, null, null, null];" +
					"null; -1 ∉ [0, 4)",
				"when index is 0, does nothing;" +
					"[r, E, t, g]; 0; [null, null, null];" +
					"[r, E, t, g]; null",
				"when index is within valid range, does nothing;" +
					"[b, a, s, R]; 2; [null, null, null];" +
					"[b, a, s, R]; null",
				"when index is last valid index, does nothing;" +
					"[n, m, 4, W]; 3; [null, null];" +
					"[n, m, 4, W]; null",
				"when index is at valid range end, fails;" +
					"[m, U, s, F, 9]; 5; [null, null, null, null];" +
					"null; 5 ∉ [0, 5)",
				"when index is after valid range, fails;" +
					"[i, s, a, Q]; 12; [null, null, null];" +
					"null; 12 ∉ [0, 4)"
			})
			void testAllItemsNull(@StringMutableList MutableList<String> items1,
				int index, @StringArray String[] items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, index, items2, expected1, expected2);
			}

			@DisplayName("when argument array is empty")
			@Tests({
				"when index is before valid range, fails;" +
					"[m, E, s, a, Q]; -4; [];" +
					"null; -4 ∉ [0, 5)",
				"when index is 0, does nothing;" +
					"[j, N, m, 7, o]; 0; [];" +
					"[j, N, m, 7, o]; null",
				"when index is within valid range, does nothing;" +
					"[r, E, a, 3, s]; 2; [];" +
					"[r, E, a, 3, s]; null",
				"when index is last valid index, does nothing;" +
					"[n, B, e, 9, r]; 4; [];" +
					"[n, B, e, 9, r]; null",
				"when index is at valid range end, fails;" +
					"[b, f, K, 6]; 4; [];" +
					"null; 4 ∉ [0, 4)",
				"when index is after valid range, fails;" +
					"[o, g, R, e, 5, N]; 9; [];" +
					"null; 9 ∉ [0, 6)"
			})
			void testItemsEmpty(@StringMutableList MutableList<String> items1,
				int index, @StringArray String[] items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, index, items2, expected1, expected2);
			}
		}

		@DisplayName("when list is empty")
		@Nested
		class EmptyListTests {
			@DisplayName("when no argument items are null")
			@Tests({
				"when index is before valid range, fails;" +
					"[]; -5; [b, E, 9, k];" +
					"null; -5 ∉ [0, 0)",
				"when index is 0, fails;" +
					"[]; 0; [F, y, 2, i];" +
					"null; 0 ∉ [0, 0)",
				"when index is after valid range, fails;" +
					"[]; 5; [o, g, b, s];" +
					"null; 5 ∉ [0, 0)"
			})
			void testNoItemsNull(@StringMutableList MutableList<String> items1,
				int index, @StringArray String[] items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, index, items2, expected1, expected2);
			}

			@DisplayName("when some argument items are null")
			@Tests({
				"when index is before valid range, fails;" +
					"[]; -4; [null, b, null, s, W, 6];" +
					"null; -4 ∉ [0, 0)",
				"when index is 0, fails;" +
					"[]; 0; [k, null, Q, null, null];" +
					"null; 0 ∉ [0, 0)",
				"when index is after valid range, fails;" +
					"[]; 6; [null, null, w, Q, 8];" +
					"null; 6 ∉ [0, 0)"
			})
			void testSomeItemsNull(@StringMutableList MutableList<String> items1,
				int index, @StringArray String[] items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, index, items2, expected1, expected2);
			}

			@DisplayName("when all argument items are null")
			@Tests({
				"when index is before valid range, fails;" +
					"[]; -1; [null, null, null, null];" +
					"null; -1 ∉ [0, 0)",
				"when index is 0, fails;" +
					"[]; 0; [null, null, null];" +
					"null; 0 ∉ [0, 0)",
				"when index is after valid range, fails;" +
					"[]; 12; [null, null, null];" +
					"null; 12 ∉ [0, 0)"
			})
			void testAllItemsNull(@StringMutableList MutableList<String> items1,
				int index, @StringArray String[] items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, index, items2, expected1, expected2);
			}

			@DisplayName("when argument array is empty")
			@Tests({
				"when index is before valid range, fails;" +
					"[]; -4; [];" +
					"null; -4 ∉ [0, 0)",
				"when index is 0, fails;" +
					"[]; 0; [];" +
					"null; 0 ∉ [0, 0)",
				"when index is after valid range, fails;" +
					"[]; 9; [];" +
					"null; 9 ∉ [0, 0)"
			})
			void testItemsEmpty(@StringMutableList MutableList<String> items1,
				int index, @StringArray String[] items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, index, items2, expected1, expected2);
			}
		}

		private void test(MutableList<String> items1, int index, String[] items2,
			MutableList<String> expected1, Exception expected2) {

			try {
				final var returned = items1.insert(index, items2);
				assertSame(items1, returned,
					format("%s.insert(%d, %s)", items1, index,
						Arrays.toString(items2)));
				assertEquals(expected1, items1,
					format("%s.insert(%d, %s)", items1, index,
						Arrays.toString(items2)));
			} catch (IndexNotInRangeException exception) {
				assertNull(expected1);
				assertEquals(expected2, exception,
					format("%s.insert(%d, %s)", items1, index,
						Arrays.toString(items2)));
			}
		}
	}

	@DisplayName(".insert(int, List<T>)")
	@Nested
	class InsertListTests {
		@DisplayName("when list is not empty")
		@Nested
		class NotEmptyListTests {
			@DisplayName("when argument list is not empty")
			@Tests({
				"when index is before valid range, fails;" +
					"[v, F, G, 6, h]; -2; [7, y, Q, b];" +
					"null; -2 ∉ [0, 5)",
				"when index is 0, prepends items;" +
					"[v, K, 3, d, R]; 0; [g, N, 2, W];" +
					"[g, N, 2, W, v, K, 3, d, R]; null",
				"when index is within valid range, inserts items at index;" +
					"[v, Q, 8, m, t]; 2; [p, 5, z, D];" +
					"[v, Q, p, 5, z, D, 8, m, t]; null",
				"when index is last valid index, inserts items at index;" +
					"[g, y, 4, f, n]; 4; [o, E, 7, c];" +
					"[g, y, 4, f, o, E, 7, c, n]; null",
				"when index is at valid range end, fails;" +
					"[g, e, s, 9, a]; 5; [1, u, K, I];" +
					"null; 5 ∉ [0, 5)",
				"when index is after valid range, fails;" +
					"[v, F, 2, G, h]; 9; [q, y, 8, b];" +
					"null; 9 ∉ [0, 5)"
			})
			void testItemsNotEmpty(@StringMutableList MutableList<String> items1,
				int index, @StringMutableList MutableList<String> items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, index, items2, expected1, expected2);
			}

			@DisplayName("when argument list is empty")
			@Tests({
				"when index is before valid range, fails;" +
					"[v, F, 4, G, h]; -1; [];" +
					"null; -1 ∉ [0, 5)",
				"when index is 0, does nothing;" +
					"[m, 7, J, k, P]; 0; [];" +
					"[m, 7, J, k, P]; null",
				"when index is within valid range, does nothing;" +
					"[v, 3, F, G, h]; 1; [];" +
					"[v, 3, F, G, h]; null",
				"when index is last valid index, does nothing;" +
					"[b, g, 9, r, 2]; 4; [];" +
					"[b, g, 9, r, 2]; null",
				"when index is at valid range end, fails;" +
					"[b, R, 6, o]; 4; [];" +
					"null; 4 ∉ [0, 4)",
				"when index is after valid range, fails;" +
					"[v, Q, F, 1, h]; 5; [];" +
					"null; 5 ∉ [0, 5)"
			})
			void testItemsEmpty(@StringMutableList MutableList<String> items1,
				int index, @StringMutableList MutableList<String> items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, index, items2, expected1, expected2);
			}
		}

		@DisplayName("when list is empty")
		@Nested
		class EmptyListTests {
			@DisplayName("when argument list is not empty")
			@Tests({
				"when index is before valid range, fails;" +
					"[]; -2; [7, y, Q, b];" +
					"null; -2 ∉ [0, 0)",
				"when index is 0, fails;" +
					"[]; 0; [g, 4, R, s, Q];" +
					"null; 0 ∉ [0, 0)",
				"when index is after valid range, fails;" +
					"[]; 9; [F, y, 8, 1];" +
					"null; 9 ∉ [0, 0)"
			})
			void testItemsNotEmpty(@StringMutableList MutableList<String> items1,
				int index, @StringMutableList MutableList<String> items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, index, items2, expected1, expected2);
			}

			@DisplayName("when argument list is empty")
			@Tests({
				"when index is before valid range, fails;" +
					"[]; -1; [];" +
					"null; -1 ∉ [0, 0)",
				"when index is 0, fails;" +
					"[]; 0; [];" +
					"null; 0 ∉ [0, 0)",
				"when index is after valid range, fails;" +
					"[]; 3; [];" +
					"null; 3 ∉ [0, 0)"
			})
			void testItemsEmpty(@StringMutableList MutableList<String> items1,
				int index, @StringMutableList MutableList<String> items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, index, items2, expected1, expected2);
			}
		}

		private void test(MutableList<String> items1, int index,
			MutableList<String> items2, MutableList<String> expected1,
			Exception expected2) {

			try {
				final var returned = items1.insert(index, items2);
				assertSame(items1, returned,
					format("%s.insert(%d, %s)", items1, index, items2));
				assertEquals(expected1, items1,
					format("%s.insert(%d, %s)", items1, index, items2));
			} catch (IndexNotInRangeException exception) {
				assertNull(expected1);
				assertEquals(expected2, exception,
					format("%s.insert(%d, %s)", items1, index, items2));
			}
		}
	}

	@DisplayName(".replace(IndexRange, T...)")
	@Nested
	class ReplaceVarargsTests {
		@DisplayName("when list is not empty")
		@Nested
		class NotEmptyListTests {
			@DisplayName("when no argument items are null")
			@Tests({
				"when index range starts at 0 and ends within valid range, replaces items in range with items;" +
					"[g, f, R, u, 4, O, l]; [0, 4); [8, q, y];" +
					"[8, q, y, O, l]; null",
				"when index range is within valid range, replaces items in range with items;" +
					"[f, e, q, R, 6, w, Z]; [2, 4); [a, T, 3, s, m];" +
					"[f, e, a, T, 3, s, m, 6, w, Z]; null",
				"when index range starts within valid range and ends at valid range end, replaces items in range with items;" +
					"[b, M, N, s, 2, l, Q]; [3, 7); [g, T, 9, s];" +
					"[b, M, N, g, T, 9, s]; null",
				"when index range equals valid range, replaces items with items;" +
					"[g, R, K, 1, I]; [0, 5); [m, N, J, 7];" +
					"[m, N, J, 7]; null",
				"when index range starts within valid range and ends after valid range end, fails;" +
					"[g, s, W, 5, a]; [2, 8); [f, L, 3, o];" +
					"null; [2, 8) ⊈ [0, 5)",
				"when index range is after valid range end, fails;" +
					"[r, E, s, 9, d]; [6, 9); [w, 4, b, n];" +
					"null; [6, 9) ⊈ [0, 5)",
				"when index range is empty, prepends items;" +
					"[f, e, q, R, 6, w]; [0, 0); [a, T, 4, s, u];" +
					"[a, T, 4, s, u, f, e, q, R, 6, w]; null"
			})
			void testNoItemsNull(@StringMutableList MutableList<String> items1,
				@IntRange IndexRange range, @StringArray String[] items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, range, items2, expected1, expected2);
			}

			@DisplayName("when some argument items are null")
			@Tests({
				"when index range starts at 0 and ends within valid range, replaces items in range with non-null items;" +
					"[b, G, k, J, 6, o, P]; [0, 5); [K, null, Y, U, null, 3];" +
					"[K, Y, U, 3, P]; null",
				"when index range is within valid range, replaces items in range with non-null items;" +
					"[f, e, q, R, 5, w, H]; [2, 4); [null, b, null, null, n, 7];" +
					"[f, e, b, n, 7, 5, w, H]; null",
				"when index range starts within valid range and ends at valid range end, replaces items in range with non-null items;" +
					"[b, G, s, e, W, 1, l]; [3, 7); [K, m, null, null, y];" +
					"[b, G, s, K, m, y]; null",
				"when index range equals valid range, replaces items with non-null items;" +
					"[r, E, 4, z, Q]; [0, 5); [null, null, J, null, 8];" +
					"[J, 8]; null",
				"when index range starts within valid range and ends after valid range end, fails;" +
					"[g, s, W, 3, a, O]; [2, 10); [n, null, S, s, null];" +
					"null; [2, 10) ⊈ [0, 6)",
				"when index range is after valid range end, fails;" +
					"[g, v, s, 8, w]; [7, 8); [q, null, 5, t];" +
					"null; [7, 8) ⊈ [0, 5)",
				"when index range is empty, prepends non-null items;" +
					"[f, e, q, R, 2, w]; [0, 0); [null, b, null, null, n];" +
					"[b, n, f, e, q, R, 2, w]; null"
			})
			void testSomeItemsNull(@StringMutableList MutableList<String> items1,
				@IntRange IndexRange range, @StringArray String[] items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, range, items2, expected1, expected2);
			}

			@DisplayName("when all argument items are null")
			@Tests({
				"when index range starts at 0 and ends within valid range, removes items in range;" +
					"[t, R, s, K, 5, n, 9]; [0, 4); [null, null, null];" +
					"[n, 9]; null",
				"when index range is within valid range, removes items in range;" +
					"[f, e, q, R, 7, w, 3]; [1, 3); [null, null, null];" +
					"[f, R, 7, w, 3]; null",
				"when index range starts within valid range and ends at valid range end, removes items in range;" +
					"[b, n, M, 4, d, F, e]; [3, 7); [null, null, null];" +
					"[b, n, M]; null",
				"when index range equals valid range, removes all items;" +
					"[n, T, g, 6, J, k]; [0, 6); [null, null, null, null];" +
					"[]; null",
				"when index range starts within valid range and ends after valid range end, fails;" +
					"[m, b, 8, s, 1]; [1, 4); [null, null, null];" +
					"null; [1, 4) ⊈ [0, 5)",
				"when index range is after valid range end, fails;" +
					"[f, g, 9, y]; [4, 8); [q, s, 3, w, l, P];" +
					"null; [4, 8) ⊈ [0, 4)",
				"when index range is empty, does nothing;" +
					"[f, e, q, R, 6, w]; [0, 0); [null, null, null];" +
					"[f, e, q, R, 6, w]; null"
			})
			void testAllItemsNull(@StringMutableList MutableList<String> items1,
				@IntRange IndexRange range, @StringArray String[] items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, range, items2, expected1, expected2);
			}

			@DisplayName("when argument array is empty")
			@Tests({
				"when index range starts at 0 and ends within valid range, removes items in range;" +
					"[g, R, s, 7, a]; [0, 1); [];" +
					"[R, s, 7, a]; null",
				"when index range is within valid range, removes items in range;" +
					"[f, e, q, R, 5, w]; [2, 3); [];" +
					"[f, e, R, 5, w]; null",
				"when index range starts within valid range and ends at valid range end, removes items in range;" +
					"[g, T, s, 9, v, N]; [3, 6); [];" +
					"[g, T, s]; null",
				"when index range equals valid range, removes all items;" +
					"[t, R, s, b, 8, n, O, h]; [0, 8); [];" +
					"[]; null",
				"when index range starts within valid range and ends after valid range end, fails;" +
					"[h, f, s, 6, w, W]; [2, 7); [];" +
					"null; [2, 7) ⊈ [0, 6)",
				"when index range is after valid range end, fails;" +
					"[e, E, v, 3, s, w]; [5, 7); [];" +
					"null; [5, 7) ⊈ [0, 6)",
				"when index range is empty, does nothing;" +
					"[f, e, q, R, 6, w]; [0, 0); [];" +
					"[f, e, q, R, 6, w]; null"
			})
			void testItemsEmpty(@StringMutableList MutableList<String> items1,
				@IntRange IndexRange range, @StringArray String[] items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, range, items2, expected1, expected2);
			}
		}

		@DisplayName("when list is empty")
		@Nested
		class EmptyListTests {
			@DisplayName("when no argument items are null")
			@Tests({
				"when index range is not empty, fails;" +
					"[]; [0, 3); [a, T, 4, f, s];" +
					"null; [0, 3) ⊈ [0, 0)",
				"when index range is empty, prepends items;" +
					"[]; [0, 0); [a, T, 4, f, s];" +
					"[a, T, 4, f, s]; null"
			})
			void testNoItemsNull(@StringMutableList MutableList<String> items1,
				@IntRange IndexRange range, @StringArray String[] items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, range, items2, expected1, expected2);
			}

			@DisplayName("when some argument items are null")
			@Tests({
				"when index range is not empty, fails;" +
					"[]; [1, 2); [null, b, null, null, n, 5];" +
					"null; [1, 2) ⊈ [0, 0)",
				"when index range is empty, prepends non-null items;" +
					"[]; [0, 0); [null, b, null, null, n, 5];" +
					"[b, n, 5]; null"
			})
			void testSomeItemsNull(@StringMutableList MutableList<String> items1,
				@IntRange IndexRange range, @StringArray String[] items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, range, items2, expected1, expected2);
			}

			@DisplayName("when all argument items are null")
			@Tests({
				"when index range is not empty, fails;" +
					"[]; [0, 1); [null, null, null];" +
					"null; [0, 1) ⊈ [0, 0)",
				"when index range is empty, does nothing;" +
					"[]; [0, 0); [null, null, null];" +
					"[]; null"
			})
			void testAllItemsNull(@StringMutableList MutableList<String> items1,
				@IntRange IndexRange range, @StringArray String[] items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, range, items2, expected1, expected2);
			}

			@DisplayName("when argument array is empty")
			@Tests({
				"when index range is not empty, fails;" +
					"[]; [2, 5); [];" +
					"null; [2, 5) ⊈ [0, 0)",
				"when index range is empty, does nothing;" +
					"[]; [0, 0); [];" +
					"[]; null"
			})
			void testItemsEmpty(@StringMutableList MutableList<String> items1,
				@IntRange IndexRange range, @StringArray String[] items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, range, items2, expected1, expected2);
			}
		}

		private void test(MutableList<String> items1, IndexRange range,
			String[] items2, MutableList<String> expected1, Exception expected2) {

			try {
				final var returned = items1.replace(range, items2);
				assertSame(items1, returned,
					format("%s.replace(%s, %s)", items1, range,
						Arrays.toString(items2)));
				assertEquals(expected1, items1,
					format("%s.replace(%s, %s)", items1, range,
						Arrays.toString(items2)));
			} catch (Exception exception) {
				assertNull(expected1);
				assertEquals(expected2, exception,
					format("%s.replace(%s, %s)", items1, range,
						Arrays.toString(items2)));
			}
		}
	}

	@DisplayName(".replace(IndexRange, List<T>)")
	@Nested
	class ReplaceListTests {
		@DisplayName("when list is not empty")
		@Nested
		class NotEmptyListTests {
			@DisplayName("when argument list is not empty")
			@Tests({
				"when index range starts at 0 and ends within valid range, replaces items in range;" +
					"[g, t, U, 5, U, j, f]; [0, 3); [v, 7, F, K];" +
					"[v, 7, F, K, U, j, f]; null",
				"when index range is within valid range, replaces items in range;" +
					"[h, d, w, 1, a, S, 9, b]; [3, 6); [g, N, 2, s, e];" +
					"[h, d, w, g, N, 2, s, e, b]; null",
				"when index range equals valid range, replaces all items;" +
					"[g, R, e, 4, Y]; [0, 5); [A, 8, S];" +
					"[A, 8, S]; null",
				"when index range starts within and ends at valid range end, replaces items in range;" +
					"[n, B, D, 6, d, g]; [4, 6); [t, R, E, s, 3];" +
					"[n, B, D, 6, t, R, E, s, 3]; null",
				"when index range starts within and ends after valid range end, fails;" +
					"[g, f, s, W, 5, s]; [3, 6); [q, y, 8, f];" +
					"null; [3, 6) ⊈ [0, 6)",
				"when index range starts and ends after valid range end, fails;" +
					"[s, f, e, 1]; [3, 6); [p, O, 7, k, J];" +
					"null; [3, 6) ⊈ [0, 4)",
				"when index range is empty, prepends items;" +
					"[s, f, e, 9]; [0, 0); [p, O, k, 4, J];" +
					"[p, O, k, 4, J, s, f, e, 9]; null"
			})
			void testItemsNotEmpty(@StringMutableList MutableList<String> items1,
				@IntRange IndexRange range, @StringList List<String> items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, range, items2, expected1, expected2);
			}

			@DisplayName("when argument list is empty")
			@Tests({
				"when index range starts at 0 and ends within valid range, removes items in range;" +
					"[F, D, t, 6, r]; [0, 2); [];" +
					"[t, 6, r]; null",
				"when index range is within valid range, removes items in range;" +
					"[n, F, g, s, 7, S, O]; [2, 5); [];" +
					"[n, F, S, O]; null",
				"when index range equals valid range, empties list;" +
					"[y, T, i, 4, o, P, f]; [0, 7); [];" +
					"[]; null",
				"when index range starts within and ends at valid range end, removes items in range;" +
					"[h, T, d, F, 3, b, s]; [5, 7); [];" +
					"[h, T, d, F, 3, b]; null",
				"when index range starts within and ends after valid range end, fails;" +
					"[f, d, w, 8, s]; [1, 9); [];" +
					"null; [1, 9) ⊈ [0, 5)",
				"when index range starts and ends after valid range end, fails;" +
					"[d, s, a, 1, w]; [6, 8); [];" +
					"null; [6, 8) ⊈ [0, 5)",
				"when index range is empty, does nothing;" +
					"[d, s, a, w, 4]; [0, 0); [];" +
					"[d, s, a, w, 4]; null"
			})
			void testItemsEmpty(@StringMutableList MutableList<String> items1,
				@IntRange IndexRange range, @StringList List<String> items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, range, items2, expected1, expected2);
			}
		}

		@DisplayName("when list is empty")
		@Nested
		class EmptyListTests {
			@DisplayName("when argument list is not empty")
			@Tests({
				"when index range is not empty, fails;" +
					"[]; [0, 1); [f, a, 7, S, o];" +
					"null; [0, 1) ⊈ [0, 0)",
				"when index range is empty, prepends items;" +
					"[]; [0, 0); [f, a, 7, S, o];" +
					"[f, a, 7, S, o]; null",
			})
			void testItemsNotEmpty(@StringMutableList MutableList<String> items1,
				@IntRange IndexRange range, @StringList List<String> items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, range, items2, expected1, expected2);
			}

			@DisplayName("when argument list is empty")
			@Tests({
				"when index range is not empty, fails;" +
					"[]; [1, 2); [];" +
					"null; [1, 2) ⊈ [0, 0)",
				"when index range is empty, does nothing;" +
					"[]; [0, 0); [];" +
					"[]; null"
			})
			void testItemsEmpty(@StringMutableList MutableList<String> items1,
				@IntRange IndexRange range, @StringList List<String> items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, range, items2, expected1, expected2);
			}
		}

		private void test(MutableList<String> items1, IndexRange range,
			List<String> items2, MutableList<String> expected1, Exception expected2) {

			try {
				final var returned = items1.replace(range, items2);
				assertSame(items1, returned,
					format("%s.replace(%s, %s)", items1, range, items2));
				assertEquals(expected1, items1,
					format("%s.replace(%s, %s)", items1, range, items2));
			} catch (Exception exception) {
				assertNull(expected1);
				assertEquals(expected2, exception,
					format("%s.replace(%s, %s)", items1, range, items2));
			}
		}
	}

	@DisplayName(".removeFirst()")
	@Nested
	class RemoveFirstTests {
		@DisplayName("\uD83D\uDD0A")
		@Tests({
			"when list has more than 1 item, removes first item;" +
				"[g, t, s, E, a];" +
				"[t, s, E, a]",
			"when list has 1 item, empties list;" +
				"[g];" +
				"[]",
			"when list is empty, does nothing;" +
				"[];" +
				"[]"
		})
		void test(@StringMutableList MutableList<String> items,
			@StringMutableList MutableList<String> expected) {

			final var returned = items.removeFirst();
			assertSame(items, returned,
				format("%s.removeFirst()", items));
			assertEquals(expected, items,
				format("%s.removeFirst()", items));
		}
	}

	@DisplayName(".removeLast()")
	@Nested
	class RemoveLastTests {
		@DisplayName("\uD83D\uDDD1")
		@Tests({
			"when list has more than 1 item, removes last item;" +
				"[g, r, s, A, s];" +
				"[g, r, s, A]",
			"when list has 1 item, empties list;" +
				"[A];" +
				"[]",
			"when list is empty, does nothing;" +
				"[];" +
				"[]"
		})
		void test(@StringMutableList MutableList<String> items,
			@StringMutableList MutableList<String> expected) {

			final var returned = items.removeLast();
			assertSame(items, returned,
				format("%s.removeLast()", items));
			assertEquals(expected, items,
				format("%s.removeLast()", items));
		}
	}

	@DisplayName(".remove(int)")
	@Nested
	class RemoveTests {
		@DisplayName("when list has more than 1 item")
		@Tests({
			"when index is before valid range, fails;" +
				"[g, e, A, s, a, T]; -2;" +
				"null; -2 ∉ [0, 6)",
			"when index is 0, removes first item;" +
				"[n, G, s, a]; 0;" +
				"[G, s, a]; null",
			"when index is within valid range, removes item at index;" +
				"[g, e, A, s, a, T]; 2;" +
				"[g, e, s, a, T]; null",
			"when index is last valid index, removes last item;" +
				"[h, S, d, a]; 3;" +
				"[h, S, d]; null",
			"when index is at valid range end, fails;" +
				"[q, W, 8, c, K, 2]; 6;" +
				"null; 6 ∉ [0, 6)",
			"when index is after valid range, fails;" +
				"[g, e, A, s, a, T]; 7;" +
				"null; 7 ∉ [0, 6)"
		})
		void testListNotEmpty(@StringMutableList MutableList<String> items, int index,
			@StringMutableList MutableList<String> expected1,
			@IndexRangeException Exception expected2) {
			test(items, index, expected1, expected2);
		}

		@DisplayName("when list has 1 item")
		@Tests({
			"when index is before valid range, fails;" +
				"[A]; -2;" +
				"null; -2 ∉ [0, 1)",
			"when index is 0, empties list;" +
				"[g]; 0;" +
				"[]; null",
			"when index is 1, fails;" +
				"[g]; 1;" +
				"null; 1 ∉ [0, 1)",
			"when index is after valid range, fails;" +
				"[q]; 3;" +
				"null; 3 ∉ [0, 1)"
		})
		void testListHasOneItem(@StringMutableList MutableList<String> items, int index,
			@StringMutableList MutableList<String> expected1,
			@IndexRangeException Exception expected2) {
			test(items, index, expected1, expected2);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when index is before valid range, fails;" +
				"[]; -2;" +
				"null; -2 ∉ [0, 0)",
			"when index is 0, fails;" +
				"[]; 0;" +
				"null; 0 ∉ [0, 0)",
			"when index is after valid range, fails;" +
				"[]; 7;" +
				"null; 7 ∉ [0, 0)"
		})
		void testListEmpty(@StringMutableList MutableList<String> items, int index,
			@StringMutableList MutableList<String> expected1,
			@IndexRangeException Exception expected2) {
			test(items, index, expected1, expected2);
		}

		private void test(MutableList<String> items, int index,
			MutableList<String> expected1, Exception expected2) {

			try {
				final var returned = items.remove(index);
				assertSame(items, returned,
					format("%s.remove(%d)", items, index));
				assertEquals(expected1, items,
					format("%s.remove(%d)", items, index));
			} catch (Exception exception) {
				assertNull(expected1);
				assertEquals(expected2, exception,
					format("%s.remove(%d)", items, index));
			}
		}
	}

	@DisplayName(".remove(IndexRange)")
	@Nested
	class RemoveIndexRangeTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when index range starts at 0 and ends within valid range, removes items in range;" +
				"[b, F, e, a, s]; [0, 2);" +
				"[e, a, s]; null",
			"when index range is within valid range, removes items in range;" +
				"[g, d, e, a, s, a, w, A, s]; [3, 7);" +
				"[g, d, e, A, s]; null",
			"when index range starts within and ends at valid range end, removes items in range;" +
				"[k, V, f, s, S, S]; [3, 6);" +
				"[k, V, f]; null",
			"when index range equals valid range, empties list;" +
				"[g, e, S, d, A, F, S]; [0, 7);" +
				"[]; null",
			"when index range starts within valid range and ends after valid range end, fails;" +
				"[v, R, d, s, S, s]; [3, 8);" +
				"null; [3, 8) ⊈ [0, 6)",
			"when index range starts and ends after valid range end, fails;" +
				"[g, R, T, W, s]; [8, 9);" +
				"null; [8, 9) ⊈ [0, 5)",
			"when index range is empty, does nothing;" +
				"[g, E, R, R, s]; [0, 0);" +
				"[g, E, R, R, s]; null"
		})
		void testListNotEmpty(@StringMutableList MutableList<String> items,
			@IntRange IndexRange range,
			@StringMutableList MutableList<String> expected1,
			@IndexRangeException Exception expected2) {
			test(items, range, expected1, expected2);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when index range is not empty, fails;" +
				"[]; [0, 1);" +
				"null; [0, 1) ⊈ [0, 0)",
			"when index range is empty, does nothing;" +
				"[]; [0, 0);" +
				"[]; null",
		})
		void testListEmpty(@StringMutableList MutableList<String> items,
			@IntRange IndexRange range,
			@StringMutableList MutableList<String> expected1,
			@IndexRangeException Exception expected2) {
			test(items, range, expected1, expected2);
		}

		private void test(MutableList<String> items, IndexRange range,
			MutableList<String> expected1, Exception expected2) {

			try {
				final var returned = items.remove(range);
				assertSame(items, returned,
					format("%s.remove(%s)", items, range));
				assertEquals(expected1, items,
					format("%s.remove(%s)", items, range));
			} catch (Exception exception) {
				assertNull(expected1);
				assertEquals(expected2, exception,
					format("%s.remove(%s)", items, range));
			}
		}
	}

	@DisplayName(".clear()")
	@Nested
	class ClearTests {
		@DisplayName("\uD83D\uDDDE")
		@Tests({
			"when list is not empty, empties list;" +
				"[g, R, S, s];" +
				"[]",
			"when list is empty, does nothing;" +
				"[];" +
				"[]"
		})
		void test(@StringMutableList MutableList<String> items,
			@StringMutableList MutableList<String> expected) {

			final var returned = items.clear();
			assertSame(items, returned,
				format("%s.clear()", items));
			assertEquals(expected, items,
				format("%s.clear()", items));
		}
	}

	@DisplayName(".toImmutable()")
	@Nested
	class ToImmutableTests {
		@DisplayName("\uD83D\uDE93")
		@Tests({
			"when list is not empty, returns items in immutable list;" +
				"[v, m, K, l];" +
				"[v, m, K, l]",
			"when list is empty, returns empty immutable list;" +
				"[];" +
				"[]"
		})
		void test(@StringMutableList MutableList<String> items,
			@StringList List<String> expected) {

			final var returned = items.toImmutable();
			final var klass = returned.getClass();

			assertEquals(List.class, klass,
				format("%s.toImmutable()", items));
			assertEquals(expected, returned,
				format("%s.toImmutable()", items));
		}
	}
}

// created on Jul 25, 2019
