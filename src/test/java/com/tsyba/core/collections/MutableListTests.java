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
	@DisplayName("MutableList(T...)")
	@Nested
	class NewWithVarargsTests {
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
				format("new MutableList<>(%s) created with incorrect capacity", expected));
			assertEquals(expected, created,
				format("new MutableList<>(%s)", Arrays.toString(items)));
		}
	}

	@DisplayName("MutableList(List<T>)")
	@Nested
	class NewWithListTests {
		@DisplayName("\uD83E\uDD12")
		@Tests({
			"when argument list is not empty, creates its copy;" +
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
				format("new MutableList<>(%s) created with incorrect capacity", expected));
			assertEquals(expected, created,
				format("new MutableList<>(%s)", items));
		}
	}

	@DisplayName(".set(int, T)")
	@Nested
	class SetTests {
		@DisplayName("when list is not empty")
		@Nested
		class NotEmptyListTests {
			@DisplayName("when index is within valid range")
			@Tests({
				"when argument item is not null, replaces item at index;" +
					"[g, E, x, s]; 2; O;" +
					"[g, E, O, s]; null",
				"when argument item is null, does nothing;" +
					"[g, E, x, s]; 2; null;" +
					"[g, E, x, s]; null"
			})
			void testIndexWithinValidRange(@StringMutableList MutableList<String> items,
				int index, String item,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items, index, item, expected1, expected2);
			}


			@DisplayName("when index is before valid range")
			@Tests({
				"when argument item is not null, fails;" +
					"[m, f, E, s, a]; -2; O;" +
					"null; -2 ∉ [0, 5)",
				"when argument item is null, fails;" +
					"[i, F, E, s, a, g]; -6; null;" +
					"null; -6 ∉ [0, 6)"
			})
			void testIndexBeforeValidRange(@StringMutableList MutableList<String> items,
				int index, String item,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items, index, item, expected1, expected2);
			}

			@DisplayName("when index is after valid range")
			@Tests({
				"when argument item is not null, fails;" +
					"[m, M, s, n, a]; 7; null;" +
					"null; 7 ∉ [0, 5)",
				"when argument item is null, fails;" +
					"[k, f]; 3; null;" +
					"null; 3 ∉ [0, 2)"
			})
			void testIndexAfterValidRange(@StringMutableList MutableList<String> items,
				int index, String item,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items, index, item, expected1, expected2);
			}
		}

		@DisplayName("when list is empty")
		@Nested
		class EmptyListTests {
			@DisplayName("when index is before valid range")
			@Tests({
				"when argument item is not null, fails;" +
					"[]; -2; I;" +
					"null; -2 ∉ [0, 0)",
				"when argument item is null, fails;" +
					"[]; -1; null;" +
					"null; -1 ∉ [0, 0)"
			})
			void testIndexBeforeValidRange(@StringMutableList MutableList<String> items,
				int index, String item,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items, index, item, expected1, expected2);
			}

			@DisplayName("when index is after valid range")
			@Tests({
				"when argument item is not null, fails;" +
					"[]; 0; s;" +
					"null; 0 ∉ [0, 0)",
				"when argument item is null, fails;" +
					"[]; 3; null;" +
					"null; 3 ∉ [0, 0)"
			})
			void testIndexAfterValidRange(@StringMutableList MutableList<String> items,
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
				assertEquals(items, expected1,
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
				"[v, B, f]; g;" +
				"[g, v, B, f]",
			"when argument item is null, does nothing;" +
				"[v, B, f]; null;" +
				"[v, B, f]"
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
	class PrependWithVarargsTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when no argument items are null, prepends all items;" +
				"[h, V, d]; [n, M];" +
				"[n, M, h, V, d]",
			"when some argument items are null, prepends non-null items;" +
				"[g, H]; [null, n, null, null, O, null];" +
				"[n, O, g, H]",
			"when all argument items are null, does nothing;" +
				"[f, E, q]; [null, null, null];" +
				"[f, E, q]",
			"when argument array is empty, does nothing;" +
				"[b, R, O]; [];" +
				"[b, R, O]"
		})
		void testListNotEmpty(@StringMutableList MutableList<String> items1,
			@StringArray String[] items2,
			@StringMutableList MutableList<String> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when no argument items are null, prepends all items;" +
				"[]; [n, M];" +
				"[n, M]",
			"when some argument items are null, prepends non-null items;" +
				"[]; [null, n, null, null, O, null];" +
				"[n, O]",
			"when all argument items are null, does nothing;" +
				"[]; [null, null, null];" +
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
	class PrependWithListTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when argument list is not empty, prepends all items;" +
				"[G, f]; [F, F, a];" +
				"[F, F, a, G, f]",
			"when argument list is empty, does nothing;" +
				"[G, f]; [];" +
				"[G, f]"
		})
		void testListNotEmpty(@StringMutableList MutableList<String> items1,
			@StringList List<String> items2,
			@StringMutableList MutableList<String> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when argument list is not empty, prepends all items;" +
				"[]; [V, Q, P];" +
				"[V, Q, P]",
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
				"[v, b, f]; g;" +
				"[v, b, f, g]",
			"when argument item is null, does nothing;" +
				"[v, b, f]; null;" +
				"[v, b, f]"
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
	class AppendWithVarargsTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when no argument are null, appends all items;" +
				"[h, V, d]; [n, M];" +
				"[h, V, d, n, M]",
			"when some argument items are null, appends non-null items;" +
				"[g, H]; [null, n, null, null, O, null];" +
				"[g, H, n, O]",
			"when all argument items are null, does nothing;" +
				"[f, E, Q]; [null, null, null];" +
				"[f, E, Q]",
			"when argument array is empty, does nnothing;" +
				"[b, R, O]; [];" +
				"[b, R, O]"
		})
		void testListNotEmpty(@StringMutableList MutableList<String> items1,
			@StringArray String[] items2,
			@StringMutableList MutableList<String> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when no argument are null, appends all items;" +
				"[]; [n, M];" +
				"[n, M]",
			"when some argument items are null, appends non-null items;" +
				"[]; [null, n, null, null, O, null];" +
				"[n, O]",
			"when all argument items are null, does nothing;" +
				"[]; [null, null, null];" +
				"[]",
			"when argument array is empty, does nnothing;" +
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
	class AppendWithListTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when argument list is not empty, appends all items;" +
				"[G, f]; [F, F, a];" +
				"[G, f, F, F, a]",
			"when argument list is empty, does nothing;" +
				"[G, f]; [];" +
				"[G, f]"
		})
		void testListNotEmpty(@StringMutableList MutableList<String> items1,
			@StringList List<String> items2,
			@StringMutableList MutableList<String> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when argument list is not empty, appends all items;" +
				"[]; [F, F, a];" +
				"[F, F, a]",
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
			@DisplayName("when index is before valid range")
			@Tests({
				"when argument item is not null, fails;" +
					"[r, e, q, P, L, a]; -4; u;" +
					"null; -4 ∉ [0, 6)",
				"when argument item is null, fails;" +
					"[r, P]; -2; null;" +
					"null; -2 ∉ [0, 2)",
			})
			void testIndexBeforeValidRange(@StringMutableList MutableList<String> items,
				int index, String item, @StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items, index, item, expected1, expected2);
			}

			@DisplayName("when index is within valid range")
			@Tests({
				"when argument item is not null, inserts item;" +
					"[g, I, K, s, A]; 2; u;" +
					"[g, I, u, K, s, A]; null",
				"when argument item is null, does nothing;" +
					"[g, I, K, s, A]; 2; null;" +
					"[g, I, K, s, A]; null"
			})
			void testIndexWithinValidRange(@StringMutableList MutableList<String> items,
				int index, String item, @StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items, index, item, expected1, expected2);
			}

			@DisplayName("when index is after valid range")
			@Tests({
				"when argument item is not null, fails;" +
					"[e, f, Q, s, O, l]; 8; u;" +
					"null; 8 ∉ [0, 6)",
				"when argument item is null, fails;" +
					"[j, s, Q, s]; 10; null;" +
					"null; 10 ∉ [0, 4)"
			})
			void testIndexAfterValidRange(@StringMutableList MutableList<String> items,
				int index, String item, @StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items, index, item, expected1, expected2);
			}
		}

		@DisplayName("when list is empty")
		@Nested
		class EmptyListTests {
			@DisplayName("when index is before valid range")
			@Tests({
				"when argument item is not null, fails;" +
					"[]; -1; u;" +
					"null; -1 ∉ [0, 0)",
				"when argument item is null, fails;" +
					"[]; -3; null;" +
					"null; -3 ∉ [0, 0)",
			})
			void testIndexBeforeValidRange(@StringMutableList MutableList<String> items,
				int index, String item, @StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items, index, item, expected1, expected2);
			}

			@DisplayName("when index is after valid range")
			@Tests({
				"when argument item is not null, fails;" +
					"[]; 2; u;" +
					"null; 2 ∉ [0, 0)",
				"when argument item is null, fails;" +
					"[]; 0; null;" +
					"null; 0 ∉ [0, 0)"
			})
			void testIndexAfterValidRange(@StringMutableList MutableList<String> items,
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
	class InsertWithVarargsTests {
		@DisplayName("when list is not empty")
		@Nested
		class NotEmptyListTests {
			@DisplayName("when index is before valid range")
			@Tests({
				"when no argument items are null, fails;" +
					"[g, U, s, A]; -5; [b, E];" +
					"null; -5 ∉ [0, 4)",
				"when some argument items are null, fails;" +
					"[b, R, s]; -2; [null, b, null, s, W];" +
					"null; -2 ∉ [0, 3)",
				"when all argument items are null, fails;" +
					"[m, I]; -1; [null, null, null, null];" +
					"null; -1 ∉ [0, 2)",
				"when argument array is empty, fails;" +
					"[m, E, s, a, Q]; -4; [];" +
					"null; -4 ∉ [0, 5)",
			})
			void testIndexBeforeValidRange(@StringMutableList MutableList<String> items1,
				int index, @StringArray String[] items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, index, items2, expected1, expected2);
			}

			@DisplayName("when index is within valid range")
			@Tests({
				"when no argument items are null, inserts all items;" +
					"[v, F, G, h]; 2; [F, F];" +
					"[v, F, F, F, G, h]; null",
				"when some argument items are null, inserts non-null items;" +
					"[b, a, s, R]; 2; [F, null, null, D];" +
					"[b, a, F, D, s, R]; null",
				"when all argument items are null, does nothing;" +
					"[b, a, s, R]; 2; [null, null, null];" +
					"[b, a, s, R]; null",
				"when index is 0, prepends items;" +
					"[b, a, s, R]; 0; [F, F];" +
					"[F, F, b, a, s, R]; null",
				"when argument array is empty, does nothing;" +
					"[r, E, a, s]; 2; [];" +
					"[r, E, a, s]; null"
			})
			void testIndexWithinValidRange(@StringMutableList MutableList<String> items1,
				int index, @StringArray String[] items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, index, items2, expected1, expected2);
			}

			@DisplayName("when index is after valid range")
			@Tests({
				"when no argument items are null, fails;" +
					"[m, e, q, A, s]; 5; [o, s, a];" +
					"null; 5 ∉ [0, 5)",
				"when some argument items are null, fails;" +
					"[o, E]; 6; [null, null, w, Q];" +
					"null; 6 ∉ [0, 2)",
				"when all argument items are null, fails;" +
					"[i, s, a, Q]; 12; [null, null];" +
					"null; 12 ∉ [0, 4)",
				"when argument array is empty, fails;" +
					"[o, g, R, e, s, N]; 9; [];" +
					"null; 9 ∉ [0, 6)",
			})
			void testIndexAfterValidRange(@StringMutableList MutableList<String> items1,
				int index, @StringArray String[] items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, index, items2, expected1, expected2);
			}
		}

		@DisplayName("when list is empty")
		@Nested
		class EmptyListTests {
			@DisplayName("when index is before valid range")
			@Tests({
				"when no argument items are null, fails;" +
					"[]; -1; [F, D];" +
					"null; -1 ∉ [0, 0)",
				"when some argument items are null, fails;" +
					"[]; -3; [null, s, d, null];" +
					"null; -3 ∉ [0, 0)",
				"when all argument items are null, fails;" +
					"[]; -4; [null, null, null];" +
					"null; -4 ∉ [0, 0)",
				"when argument array is empty, fails;" +
					"[]; -7; [];" +
					"null; -7 ∉ [0, 0)",
			})
			void testIndexBeforeValidRange(@StringMutableList MutableList<String> items1,
				int index, @StringArray String[] items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, index, items2, expected1, expected2);
			}

			@DisplayName("when index is after valid range")
			@Tests({
				"when no argument items are null, fails;" +
					"[]; 2; [F, D];" +
					"null; 2 ∉ [0, 0)",
				"when some argument items are null, fails;" +
					"[]; 0; [F, null, null, e];" +
					"null; 0 ∉ [0, 0)",
				"when all argument items are null, fails;" +
					"[]; 6; [null, null, null];" +
					"null; 6 ∉ [0, 0)",
				"when argument array is empty, fails;" +
					"[]; 0; [];" +
					"null; 0 ∉ [0, 0)",
			})
			void testIndexAfterValidRange(@StringMutableList MutableList<String> items1,
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
	class InsertWithListTests {
		@DisplayName("when list is not empty")
		@Nested
		class NotEmptyListTests {
			@DisplayName("when index is before valid range")
			@Tests({
				"when argument list is not empty, fails;" +
					"[v, F, G, h]; -2; [F, F];" +
					"null; -2 ∉ [0, 4)",
				"when argument list is empty, fails;" +
					"[v, F, G, h]; -1; [];" +
					"null; -1 ∉ [0, 4)"
			})
			void testIndexBeforeValidRange(@StringMutableList MutableList<String> items1,
				int index, @StringMutableList MutableList<String> items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, index, items2, expected1, expected2);
			}

			@DisplayName("when index is within valid range")
			@Tests({
				"when argument list is not empty, inserts items;" +
					"[v, F, G, h]; 2; [F, F];" +
					"[v, F, F, F, G, h]; null",
				"when index is 0, prepends items;" +
					"[v, F, G, h]; 0; [F, F];" +
					"[F, F, v, F, G, h]; null",
				"when argument list is empty, does nothing;" +
					"[v, F, G, h]; 1; [];" +
					"[v, F, G, h]; null"
			})
			void testIndexWithinValidRange(@StringMutableList MutableList<String> items1,
				int index, @StringMutableList MutableList<String> items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, index, items2, expected1, expected2);
			}

			@DisplayName("when index is after valid range")
			@Tests({
				"when argument list is not empty, fails;" +
					"[v, F, G, h]; 9; [F, F];" +
					"null; 9 ∉ [0, 4)",
				"when argument list is empty, fails;" +
					"[v, F, G, h]; 5; [];" +
					"null; 5 ∉ [0, 4)"
			})
			void testIndexAfterValidRange(@StringMutableList MutableList<String> items1,
				int index, @StringMutableList MutableList<String> items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, index, items2, expected1, expected2);
			}
		}

		@DisplayName("when list is empty")
		@Nested
		class EmptyListTests {
			@DisplayName("when index is before valid range")
			@Tests({
				"when argument list is not empty, fails;" +
					"[]; -2; [F, F];" +
					"null; -2 ∉ [0, 0)",
				"when argument list is empty, fails;" +
					"[]; 0; [];" +
					"null; 0 ∉ [0, 0)"
			})
			void testIndexBeforeValidRange(@StringMutableList MutableList<String> items1,
				int index, @StringMutableList MutableList<String> items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, index, items2, expected1, expected2);
			}

			@DisplayName("when index is after valid range")
			@Tests({
				"when argument list is not empty, fails;" +
					"[]; 9; [F, F];" +
					"null; 9 ∉ [0, 0)",
				"when argument list is empty, fails;" +
					"[]; 0; [];" +
					"null; 0 ∉ [0, 0)"
			})
			void testIndexAfterValidRange(@StringMutableList MutableList<String> items1,
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
	class ReplaceWithVarargsTests {
		@DisplayName("when list is not empty")
		@Nested
		class NotEmptyListTests {
			@DisplayName("when index range is within valid range")
			@Tests({
				"when no argument items are null, replaces items in range with all items;" +
					"[f, e, q, R, w]; [2, 4); [a, T, f, s];" +
					"[f, e, a, T, f, s, w]; null",
				"when some argument items are null, replaces items in range with non-null items;" +
					"[f, e, q, R, w]; [2, 5); [null, b, null, null, n];" +
					"[f, e, b, n]; null",
				"when all argument items are null, removes items in range;" +
					"[f, e, q, R, w]; [1, 3); [null, null];" +
					"[f, R, w]; null",
				"when argument array is empty, removes items in range;" +
					"[f, e, q, R, w]; [2, 3); [];" +
					"[f, e, R, w]; null"
			})
			void testIndexRangeWithinValidRange(@StringMutableList MutableList<String> items1,
				@IntRange IndexRange range, @StringArray String[] items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, range, items2, expected1, expected2);
			}

			@DisplayName("when index range starts within and ends after valid range")
			@Tests({
				"when no argument items are null, fails;" +
					"[g, s, W, s, a]; [2, 8); [f, L, o];" +
					"null; [2, 8) ⊈ [0, 5)",
				"when some argument items are null, fails;" +
					"[g, s, W, a, O]; [0, 10); [n, null, S, s, null];" +
					"null; [0, 10) ⊈ [0, 5)",
				"when all argument items are null, fails;" +
					"[m, b, s]; [1, 4); [null, null, null];" +
					"null; [1, 4) ⊈ [0, 3)",
				"when argument array is empty, fails;" +
					"[h, f, s, w, W]; [2, 7); [];" +
					"null; [2, 7) ⊈ [0, 5)"
			})
			void testIndexRangeEndsAfterValidRange(@StringMutableList MutableList<String> items1,
				@IntRange IndexRange range, @StringArray String[] items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, range, items2, expected1, expected2);
			}

			@DisplayName("when index range starts and ends after valid range")
			@Tests({
				"when no argument items are null, fails;" +
					"[r, E, s]; [5, 9); [d, s, a];" +
					"null; [5, 9) ⊈ [0, 3)",
				"when some argument items are null, fails;" +
					"[g, v, s, w]; [7, 8); [q, null, t];" +
					"null; [7, 8) ⊈ [0, 4)",
				"when all argument items are null, fails;" +
					"[f, g]; [4, 8); [g, s, w, l, P];" +
					"null; [4, 8) ⊈ [0, 2)",
				"when argument array is empty, fails;" +
					"[e, E, v, s, w]; [5, 7); [];" +
					"null; [5, 7) ⊈ [0, 5)"
			})
			void testIndexRangeStartsAfterValidRange(@StringMutableList MutableList<String> items1,
				@IntRange IndexRange range, @StringArray String[] items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, range, items2, expected1, expected2);
			}

			@DisplayName("when index range is empty")
			@Tests({
				"when no argument items are null, prepends all items;" +
					"[f, e, q, R, w]; [0, 0); [a, T, f, s];" +
					"[a, T, f, s, f, e, q, R, w]; null",
				"when some argument items are null, prepends non-null items;" +
					"[f, e, q, R, w]; [0, 0); [null, b, null, null, n];" +
					"[b, n, f, e, q, R, w]; null",
				"when all argument items are null, does nothing;" +
					"[f, e, q, R, w]; [0, 0); [null, null];" +
					"[f, e, q, R, w]; null",
				"when argument array is empty, does nothing;" +
					"[f, e, q, R, w]; [0, 0); [];" +
					"[f, e, q, R, w]; null"
			})
			void testIndexRangeEmpty(@StringMutableList MutableList<String> items1,
				@IntRange IndexRange range, @StringArray String[] items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, range, items2, expected1, expected2);
			}
		}

		@DisplayName("when list is empty")
		@Nested
		class EmptyListTests {
			@DisplayName("when index range is not empty")
			@Tests({
				"when no argument items are null, fails;" +
					"[]; [0, 3); [a, T, f, s];" +
					"null; [0, 3) ⊈ [0, 0)",
				"when some argument items are null, fails;" +
					"[]; [1, 2); [null, b, null, null, n];" +
					"null; [1, 2) ⊈ [0, 0)",
				"when all argument items are null, fails;" +
					"[]; [0, 1); [null, null];" +
					"null; [0, 1) ⊈ [0, 0)",
				"when argument array is empty, fails;" +
					"[]; [2, 5); [];" +
					"null; [2, 5) ⊈ [0, 0)"
			})
			void testIndexRangeNotEmpty(@StringMutableList MutableList<String> items1,
				@IntRange IndexRange range, @StringArray String[] items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, range, items2, expected1, expected2);
			}

			@DisplayName("when index range is empty")
			@Tests({
				"when no argument items are null, prepends all items;" +
					"[]; [0, 0); [a, T, f, s];" +
					"[a, T, f, s]; null",
				"when some argument items are null, prepends non-null items;" +
					"[]; [0, 0); [null, b, null, null, n];" +
					"[b, n]; null",
				"when all argument items are null, does nothing;" +
					"[]; [0, 0); [null, null];" +
					"[]; null",
				"when argument array is empty, does nothing;" +
					"[]; [0, 0); [];" +
					"[]; null"
			})
			void testIndexRangeEmpty(@StringMutableList MutableList<String> items1,
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
				assertEquals(items1, returned,
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
	class ReplaceWithListTests {
		@DisplayName("when list is not empty")
		@Nested
		class NotEmptyListTests {
			@DisplayName("when index range is within valid range")
			@Tests({
				"when argument list is not empty, replaces items in range with all items;" +
					"[h, d, w, a, S, a, b]; [3, 6); [g, d, s, e];" +
					"[h, d, w, g, d, s, e, b]; null",
				"when argument list is empty, removes items in range;" +
					"[n, F, g, s, S, O]; [2, 5); [];" +
					"[n, F, O]; null"
			})
			void testIndexRangeWithinValidRange(@StringMutableList MutableList<String> items1,
				@IntRange IndexRange range, @StringList List<String> items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, range, items2, expected1, expected2);
			}

			@DisplayName("when index range starts within, but ends after valid range")
			@Tests({
				"when argument list is not empty, fails;" +
					"[g, f, s, W, s]; [3, 6); [g, f];" +
					"null; [3, 6) ⊈ [0, 5)",
				"when argument list is empty, fails;" +
					"[f, d, w, s]; [1, 9); [];" +
					"null; [1, 9) ⊈ [0, 4)"
			})
			void testIndexRangeEndsAfterValidRange(@StringMutableList MutableList<String> items1,
				@IntRange IndexRange range, @StringList List<String> items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, range, items2, expected1, expected2);
			}

			@DisplayName("when index range starts and ends after valid range")
			@Tests({
				"when argument list is not empty, fails;" +
					"[s, f, e]; [3, 6); [p, O, k, J];" +
					"null; [3, 6) ⊈ [0, 3)",
				"when argument list is empty, fails;" +
					"[d, s, a, w]; [6, 8); [];" +
					"null; [6, 8) ⊈ [0, 4)"
			})
			void testIndexRangeStartsAfterValidRange(@StringMutableList MutableList<String> items1,
				@IntRange IndexRange range, @StringList List<String> items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, range, items2, expected1, expected2);
			}

			@DisplayName("when index range is empty")
			@Tests({
				"when argument list is not empty, prepends items;" +
					"[s, f, e]; [0, 0); [p, O, k, J];" +
					"[p, O, k, J, s, f, e]; null",
				"when argument list is empty, does nothing;" +
					"[d, s, a, w]; [0, 0); [];" +
					"[d, s, a,w ]; null"
			})
			void testIndexRangeEmpty(@StringMutableList MutableList<String> items1,
				@IntRange IndexRange range, @StringList List<String> items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, range, items2, expected1, expected2);
			}
		}

		@DisplayName("when list is empty")
		@Nested
		class EmptyListTests {
			@DisplayName("when index range is not empty")
			@Tests({
				"when argument list is not empty, fails;" +
					"[]; [0, 1); [f, a, S, o];" +
					"null; [0, 1) ⊈ [0, 0)",
				"when argument list is empty, fails;" +
					"[]; [1, 2); [];" +
					"null; [1, 2) ⊈ [0, 0)"
			})
			void testIndexRangeNotEmpty(@StringMutableList MutableList<String> items1,
				@IntRange IndexRange range, @StringList List<String> items2,
				@StringMutableList MutableList<String> expected1,
				@IndexRangeException Exception expected2) {
				test(items1, range, items2, expected1, expected2);
			}

			@DisplayName("when index range is empty")
			@Tests({
				"when argument list is not empty, prepends items;" +
					"[]; [0, 0); [f, a, S, o];" +
					"[f, a, S, o]; null",
				"when argument list is empty, does nothing;" +
					"[]; [0, 0); [];" +
					"[]; null"
			})
			void testIndexRangeEmpty(@StringMutableList MutableList<String> items1,
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
				assertEquals(items1, returned,
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
				"[g, r, s, A]"
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
		@DisplayName("when list is not empty")
		@Tests({
			"when index is within valid range, removes item at index;" +
				"[g, e, A, s, a, T]; 2;" +
				"[g, e, s, a, T]; null",
			"when list has 1 item and index is 0, empties list;" +
				"[g]; 0;" +
				"[]; null",
			"when index is before valid range, fails;" +
				"[g, e, A, s, a, T]; -2;" +
				"null; -2 ∉ [0, 6)",
			"when index is after valid range, fails;" +
				"[g, e, A, s, a, T]; 7;" +
				"null; 7 ∉ [0, 6)"
		})
		void testListNotEmpty(@StringMutableList MutableList<String> items, int index,
			@StringMutableList MutableList<String> expected1,
			@IndexRangeException Exception expected2) {
			test(items, index, expected1, expected2);
		}

		@DisplayName("when list is empty")
		@Tests({
			"when index is before valid range, fails;" +
				"[]; -2;" +
				"null; -2 ∉ [0, 0)",
			"when index is after valid range, fails;" +
				"[]; 0;" +
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
				assertEquals(exception, expected2,
					format("%s.remove(%d)", items, index));
			}
		}
	}

	@DisplayName(".remove(IndexRange)")
	@Nested
	class RemoveWithIndexRangeTests {
		@DisplayName("when list is not empty")
		@Tests({
			"when index range is within valid range, removes items in range;" +
				"[g, d, e, a, s, a, w, A, s]; [3, 7);" +
				"[g, d, e, A, s]; null",
			"when index range is equal to valid range, empties list;" +
				"[g, e, S, d, A, F, S]; [0, 7);" +
				"[]; null",
			"when index range starts within, but ends after valid range, fails;" +
				"[v, R, d, s, S, s]; [3, 8);" +
				"null; [3, 8) ⊈ [0, 6)",
			"when index range starts and ends after valid range, fails;" +
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
			"when list is not empty, returns all items in immutable list;" +
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
