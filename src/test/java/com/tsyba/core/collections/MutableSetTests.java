package com.tsyba.core.collections;

import com.tsyba.core.collections.converter.StringArray;
import com.tsyba.core.collections.converter.StringMutableSet;
import com.tsyba.core.collections.converter.StringSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.Arrays;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class MutableSetTests {
	@DisplayName(".getDistinct()")
	@Nested
	class GetDistinctTests {
		@DisplayName("\uD83C\uDF52")
		@Tests({
			"when set is not empty, returns itself;" +
				"[w, 4, K, p, 9]",
			"when set is empty, returns itself;" +
				"[]"
		})
		void test(@StringMutableSet MutableSet<String> items) {
			final var distinct = items.getDistinct();
			assertSame(items, distinct,
				format("%s.getDistinct()", items));
		}
	}

	@DisplayName(".add(T)")
	@Nested
	class AddTests {
		@DisplayName("when argument item is not null")
		@Tests({
			"when argument item is absent, adds item;" +
				"[c, F, 8, m, Q]; k;" +
				"[c, F, 8, m, Q, k]",
			"when argument item is present, does nothing;" +
				"[y, 3, N, d, r]; N;" +
				"[y, 3, N, d, r]",
			"when set is empty, adds item;" +
				"[]; e;" +
				"[e]"
		})
		void testItemNotNull(@StringMutableSet MutableSet<String> items, String item,
			@StringMutableSet MutableSet<String> expected) {
			test(items, item, expected);
		}

		@DisplayName("when argument item is null")
		@Tests({
			"when set is not empty, does nothing;" +
				"[G, v, 7, L, b]; null;" +
				"[G, v, 7, L, b]",
			"when set is empty, does nothing;" +
				"[]; null;" +
				"[]"
		})
		void testItemNull(@StringMutableSet MutableSet<String> items, String item,
			@StringMutableSet MutableSet<String> expected) {
			test(items, item, expected);
		}

		private void test(MutableSet<String> items, String item,
			MutableSet<String> expected) {

			final var returned = items.add(item);
			assertSame(items, returned,
				format("%s.add(%s)", items, item));
			assertEquals(expected, items,
				format("%s.add(%s)", items, item));
		}
	}

	@DisplayName(".add(T...)")
	@Nested
	class AddVarargsTests {
		@DisplayName("when no argument items are null")
		@Nested
		class NoItemsNullTests {
			@DisplayName("when all argument items are distinct")
			@Tests({
				"when all argument items are absent, adds all items;" +
					"[x, 5, j, T]; [B, q, 2, v];" +
					"[x, 5, j, T, B, q, 2, v]",
				"when some argument items are present, adds absent items;" +
					"[h, D, 4, s, K]; [K, m, h, 9];" +
					"[h, D, 4, s, K, m, 9]",
				"when all argument items are present, does nothing;" +
					"[F, z, 1, R, u]; [z, F, u];" +
					"[F, z, 1, R, u]",
				"when set is empty, adds all items;" +
					"[]; [M, x, 7, e];" +
					"[M, x, 7, e]"
			})
			void testAllItemsDistinct(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when some argument items are same")
			@Tests({
				"when all argument items are absent, adds all distinct items;" +
					"[p, 9, E, c]; [d, d, W, 2];" +
					"[p, 9, E, c, d, W, 2]",
				"when some argument items are present, adds absent distinct items;" +
					"[6, t, C, f]; [f, f, k, 6];" +
					"[6, t, C, f, k]",
				"when all argument items are present, does nothing;" +
					"[Q, 2, n, J]; [n, v, v, n];" +
					"[Q, 2, n, J]",
				"when set is empty, adds all distinct items;" +
					"[]; [L, q, q, 8];" +
					"[L, q, 8]"
			})
			void testSomeItemsSame(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when all argument items are same")
			@Tests({
				"when all argument items are absent, adds distinct item;" +
					"[s, H, 0, m]; [B, B, B];" +
					"[s, H, 0, m, B]",
				"when all argument items are present, does nothing;" +
					"[r, 5, P, a]; [P, P];" +
					"[r, 5, P, a]",
				"when set is empty, adds distinct item;" +
					"[]; [g, g, g];" +
					"[x]"
			})
			void testAllItemsSame(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}
		}

		@DisplayName("when some argument items are null")
		@Nested
		class SomeItemsNullTests {
			@DisplayName("when all non-null argument items are distinct")
			@Tests({
				"when all argument items are absent, adds all non-null items;" +
					"[k, y, 4, D]; [null, V, null, 2];" +
					"[k, y, 4, D, V, 2]",
				"when some argument items are present, adds absent non-null items;" +
					"[B, e, 7, w]; [e, null, T, null, B];" +
					"[B, e, 7, w, T]",
				"when all argument items are present, does nothing;" +
					"[m, 8, C, j, N]; [null, N, null, m, 8];" +
					"[m, 8, C, j, N]",
				"when set is empty, adds all non-null items;" +
					"[]; [null, r, null, W, 9];" +
					"[r, W, 9]"
			})
			void testAllItemsDistinct(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when some non-null argument items are same")
			@Tests({
				"when all argument items are absent, adds all distinct non-null items;" +
					"[f, 3, Y, d]; [null, Z, null, Z, 9];" +
					"[f, 3, Y, d, Z, 9]",
				"when some argument items are present, adds absent distinct non-null items;" +
					"[H, o, 6, l]; [o, null, null, S, o];" +
					"[H, o, 6, l, S]",
				"when all argument items are present, does nothing;" +
					"[t, G, 0, p]; [null, G, t, null, t];" +
					"[t, G, 0, p]",
				"when set is empty, adds all distinct non-null items;" +
					"[]; [b, null, null, J, b, null];" +
					"[b, J]"
			})
			void testSomeItemsSame(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when all non-null argument items are same")
			@Tests({
				"when all argument items are absent, adds distinct non-null item;" +
					"[Q, x, 5, k]; [null, E, null, E, E];" +
					"[Q, x, 5, k, E]",
				"when all argument items are present, does nothing;" +
					"[v, 2, M, 8]; [M, M, null, null];" +
					"[v, 2, M, 8]",
				"when set is empty, adds distinct non-null item;" +
					"[]; [null, y, null, y, y];" +
					"[y]"
			})
			void testAllItemsSame(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}
		}

		@DisplayName("when all argument items are null")
		@Nested
		class AllItemsNullTests {
			@DisplayName("\uD83D\uDCA4")
			@Tests({
				"when set is not empty, does nothing;" +
					"[u, 1, Z, 5]; [null, null, null, null, null];" +
					"[u, 1, Z, 5]",
				"when set is empty, does nothing;" +
					"[]; [null, null, null];" +
					"[]"
			})
			void testAllItemsNull(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}
		}

		@DisplayName("when argument array is empty")
		@Nested
		class ArgumentArrayEmptyTests {
			@DisplayName("\uD83D\uDCEC")
			@Tests({
				"when set is not empty, does nothing;" +
					"[q, V, 3, i, L]; [];" +
					"[q, V, 3, i, L]",
				"when set is empty, does nothing;" +
					"[]; [];" +
					"[]"
			})
			void testItemsEmpty(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}
		}

		private void test(MutableSet<String> items1, String[] items2,
			MutableSet<String> expected) {

			final var returned = items1.add(items2);
			assertSame(items1, returned,
				format("%s.add(%s)", items1,
					Arrays.toString(items2)));
			assertEquals(expected, items1,
				format("%s.add(%s)", items1,
					Arrays.toString(items2)));
		}
	}

	@DisplayName(".add(Iterable<T>)")
	@Nested
	class AddIterableTests {
		@DisplayName("when no argument items are null")
		@Nested
		class NoItemsNullTests {
			@DisplayName("when all argument items are distinct")
			@Tests({
				"when all argument items are absent, adds all items;" +
					"[y, 7, R, e]; [m, K, 2, d];" +
					"[y, 7, R, e, m, K, 2, d]",
				"when some argument items are present, adds absent items;" +
					"[w, 4, N, b, s]; [s, C, w, 4];" +
					"[w, 4, N, b, s, C]",
				"when all argument items are present, does nothing;" +
					"[h, 9, F, p, k]; [p, h, k];" +
					"[h, 9, F, p, k]",
				"when set is empty, adds all items;" +
					"[]; [D, 4, i, s];" +
					"[D, 4, i, s]"
			})
			void testAllItemsDistinct(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when some argument items are same")
			@Tests({
				"when all argument items are absent, adds all distinct items;" +
					"[j, 1, V, c]; [x, x, 6, x];" +
					"[j, 1, V, c, x, 6]",
				"when some argument items are present, adds absent distinct items;" +
					"[E, 5, t, n]; [t, 5, E, t];" +
					"[E, 5, t, n]",
				"when all argument items are present, does nothing;" +
					"[q, 0, M, z]; [M, z, M, q];" +
					"[q, 0, M, z]",
				"when set is empty, adds all distinct items;" +
					"[]; [b, Y, b, 2];" +
					"[b, Y, 2]"
			})
			void testSomeItemsSame(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when all argument items are same")
			@Tests({
				"when all argument items are absent, adds distinct item;" +
					"[f, 3, P, g]; [L, L, L];" +
					"[f, 3, P, g, L]",
				"when all argument items are present, does nothing;" +
					"[u, 8, T, a]; [u, u];" +
					"[u, 8, T, a]",
				"when set is empty, adds distinct item;" +
					"[]; [n, n, n];" +
					"[o]"
			})
			void testAllItemsSame(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}
		}

		@DisplayName("when some argument items are null")
		@Nested
		class SomeItemsNullTests {
			@DisplayName("when all non-null argument items are distinct")
			@Tests({
				"when all argument items are absent, adds all non-null items;" +
					"[r, 6, H, x]; [null, c, null, 1];" +
					"[r, 6, H, x, c, 1]",
				"when some argument items are present, adds absent non-null items;" +
					"[P, 8, d, k]; [d, null, J, null, P];" +
					"[P, 8, d, k, J]",
				"when all argument items are present, does nothing;" +
					"[m, 2, X, w, 4]; [null, w, null, m, 2];" +
					"[m, 2, X, w, 4]",
				"when set is empty, adds all non-null items;" +
					"[]; [null, a, null, K, 0];" +
					"[a, K, 0]"
			})
			void testAllItemsDistinct(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when some non-null argument items are same")
			@Tests({
				"when all argument items are absent, adds all distinct non-null items;" +
					"[A, 0, f, 9]; [null, e, null, e, U];" +
					"[A, 0, f, 9, e, U]",
				"when some argument items are present, adds absent distinct non-null items;" +
					"[s, 7, i, h]; [i, null, null, R, i];" +
					"[s, 7, i, h, R]",
				"when all argument items are present, does nothing;" +
					"[V, 3, p, G]; [null, G, V, null, V];" +
					"[V, 3, p, G]",
				"when set is empty, adds all distinct non-null items;" +
					"[]; [W, null, null, j, W, null];" +
					"[W, j]"
			})
			void testSomeItemsSame(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when all non-null argument items are same")
			@Tests({
				"when all argument items are absent, adds distinct non-null item;" +
					"[l, 5, Z, b]; [null, R, null, R, R];" +
					"[l, 5, Z, b, R]",
				"when all argument items are present, does nothing;" +
					"[T, 6, y, q]; [y, y, null, null];" +
					"[T, 6, y, q]",
				"when set is empty, adds distinct non-null item;" +
					"[]; [null, u, null, u, u];" +
					"[u]"
			})
			void testAllItemsSame(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}
		}

		@DisplayName("when all argument items are null")
		@Nested
		class AllItemsNullTests {
			@DisplayName("\uD83E\uDDCA")
			@Tests({
				"when set is not empty, does nothing;" +
					"[g, 9, S, v]; [null, null, null, null, null, null, null, null];" +
					"[g, 9, S, v]",
				"when set is empty, does nothing;" +
					"[]; [null, null, null, null, null, null, null, null, null];" +
					"[]"
			})
			void testAllItemsNull(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}
		}

		@DisplayName("when argument iterable is empty")
		@Nested
		class ArgumentIterableEmptyTests {
			@DisplayName("\uD83D\uDD2D")
			@Tests({
				"when set is not empty, does nothing;" +
					"[F, 1, e, 8, N]; [];" +
					"[F, 1, e, 8, N]",
				"when set is empty, does nothing;" +
					"[]; [];" +
					"[]"
			})
			void testItemsEmpty(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}
		}

		private void test(MutableSet<String> items1, String[] items2,
			MutableSet<String> expected) {

			final var returned = items1.add(
				Arrays.asList(items2));

			assertSame(items1, returned,
				format("%s.add(%s)", items1,
					Arrays.toString(items2)));
			assertEquals(expected, items1,
				format("%s.add(%s)", items1,
					Arrays.toString(items2)));
		}
	}

	@DisplayName(".remove(T)")
	@Nested
	class RemoveTests {
		@DisplayName("when argument item is not null")
		@Tests({
			"when argument item is present, removes item;" +
				"[y, 4, B, k, 7]; k;" +
				"[y, 4, B, 7]",
			"when argument item is absent, does nothing;" +
				"[c, 0, H, p]; x;" +
				"[c, 0, H, p]",
			"when set is empty, does nothing;" +
				"[]; d;" +
				"[]"
		})
		void testItemNotNull(@StringMutableSet MutableSet<String> items, String item,
			@StringMutableSet MutableSet<String> expected) {
			test(items, item, expected);
		}

		@DisplayName("when argument item is null")
		@Tests({
			"when set is not empty, does nothing;" +
				"[n, 3, E, s, W]; null;" +
				"[n, 3, E, s, W]",
			"when set is empty, does nothing;" +
				"[]; null;" +
				"[]"
		})
		void testItemNull(@StringMutableSet MutableSet<String> items, String item,
			@StringMutableSet MutableSet<String> expected) {
			test(items, item, expected);
		}

		private void test(MutableSet<String> items, String item,
			MutableSet<String> expected) {

			final var returned = items.remove(item);
			assertSame(items, returned,
				format("%s.remove(%s)", items, item));
			assertEquals(expected, items,
				format("%s.remove(%s)", items, item));
		}
	}

	@DisplayName(".remove(T...)")
	@Nested
	class RemoveVarargsTests {
		@DisplayName("when no argument items are null")
		@Nested
		class NoItemsNullTests {
			@DisplayName("when all argument items are distinct")
			@Tests({
				"when all argument items are present, removes all items;" +
					"[z, 4, K, 9, s, M]; [K, 4, M];" +
					"[z, 9, s]",
				"when some argument items are present, removes present items;" +
					"[j, B, 2, e, 7]; [e, R, 2, j];" +
					"[B, 7]",
				"when all argument items are absent, does nothing;" +
					"[y, 5, P, d, C]; [m, 8, W];" +
					"[y, 5, P, d, C]",
				"when argument array contains all set items, removes all items;" +
					"[t, 6, z]; [z, t, H, 6];" +
					"[]",
				"when set is empty, does nothing;" +
					"[]; [k, 3, J];" +
					"[]"
			})
			void testAllItemsDistinct(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when some argument items are same")
			@Tests({
				"when all argument items are present, removes all distinct items;" +
					"[w, N, 0, f]; [N, N, f, 0];" +
					"[w]",
				"when some argument items are present, removes present distinct items;" +
					"[G, q, 4, U, b]; [q, T, q, b];" +
					"[G, 4, U]",
				"when all argument items are absent, does nothing;" +
					"[x, 2, h, V]; [c, c, c, c];" +
					"[x, 2, h, V]",
				"when argument array contains all set items, removes all items;" +
					"[r, 1, Z]; [Z, Z, r, 1, 1];" +
					"[]",
				"when set is empty, does nothing;" +
					"[]; [s, s, 4, s];" +
					"[]"
			})
			void testSomeItemsSame(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when all argument items are same")
			@Tests({
				"when all argument items are present, removes distinct item;" +
					"[o, 9, D, l]; [D, D, D];" +
					"[o, 9, l]",
				"when all argument items are absent, does nothing;" +
					"[n, 5, F]; [Q, Q, Q];" +
					"[n, 5, F]",
				"when set is empty, does nothing;" +
					"[]; [I, I];" +
					"[]"
			})
			void testAllItemsSame(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}
		}

		@DisplayName("when some argument items are null")
		@Nested
		class SomeItemsNullTests {
			@DisplayName("when all non-null argument items are distinct")
			@Tests({
				"when all argument items are present, removes all non-null items;" +
					"[u, 5, W, a, q]; [W, null, a, null];" +
					"[u, 5, q]",
				"when some argument items are present, removes present non-null items;" +
					"[l, 8, D, k]; [null, D, null, F, l];" +
					"[8, k]",
				"when all argument items are absent, does nothing;" +
					"[P, 3, x, v]; [null, j, null, 1];" +
					"[P, 3, x, v]",
				"when set is empty, does nothing;" +
					"[]; [null, A, null, 2];" +
					"[]"
			})
			void testAllItemsDistinct(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when some non-null argument items are same")
			@Tests({
				"when all argument items are present, removes all distinct non-null items;" +
					"[T, 6, i, e]; [i, null, e, i];" +
					"[T, 6]",
				"when some argument items are present, removes present distinct non-null items;" +
					"[m, 0, X, b]; [null, b, null, X, C];" +
					"[m, 0]",
				"when all argument items are absent, does nothing;" +
					"[y, 4, N, p]; [null, h, null, h];" +
					"[y, 4, N, p]",
				"when set is empty, does nothing;" +
					"[]; [V, null, V, null];" +
					"[]"
			})
			void testSomeItemsSame(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when all non-null argument items are same")
			@Tests({
				"when all argument items are present, removes distinct non-null item;" +
					"[J, 7, d, S]; [S, null, null, S];" +
					"[J, 7, d]",
				"when all argument items are absent, does nothing;" +
					"[G, 1, z]; [null, q, null, q];" +
					"[G, 1, z]",
				"when set is empty, does nothing;" +
					"[]; [null, o, o, null];" +
					"[]"
			})
			void testAllItemsSame(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}
		}

		@DisplayName("when all argument items are null")
		@Nested
		class AllItemsNullTests {
			@DisplayName("\uD83D\uDCA5")
			@Tests({
				"when set is not empty, does nothing;" +
					"[t, 9, f, E]; [null, null, null, null, null, null, null];" +
					"[t, 9, f, E]",
				"when set is empty, does nothing;" +
					"[]; [null, null, null, null, null, null, null, null, null, null];" +
					"[]"
			})
			void testAllItemsNull(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}
		}

		@DisplayName("when argument array is empty")
		@Nested
		class ArgumentArrayEmptyTests {
			@DisplayName("\uD83D\uDCA8")
			@Tests({
				"when set is not empty, does nothing;" +
					"[R, 2, c, w, 5]; [];" +
					"[R, 2, c, w, 5]",
				"when set is empty, does nothing;" +
					"[]; [];" +
					"[]"
			})
			void testItemsEmpty(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}
		}

		private void test(MutableSet<String> items1, String[] items2,
			MutableSet<String> expected) {

			final var returned = items1.remove(items2);
			assertSame(items1, returned,
				format("%s.remove(%s)", items1,
					Arrays.toString(items2)));
			assertEquals(expected, items1,
				format("%s.remove(%s)", items1,
					Arrays.toString(items2)));
		}
	}

	@DisplayName(".remove(Iterable<T>)")
	@Nested
	class RemoveIterableTests {
		@DisplayName("when no argument items are null")
		@Nested
		class NoItemsNullTests {
			@DisplayName("when all argument items are distinct")
			@Tests({
				"when all argument items are present, removes all items;" +
					"[n, 4, Y, j, 0]; [Y, 4, j];" +
					"[n, 0]",
				"when some argument items are present, removes present items;" +
					"[z, 7, F, k]; [k, s, 7, z];" +
					"[F]",
				"when all argument items are absent, does nothing;" +
					"[q, 1, B, m]; [h, 8, L];" +
					"[q, 1, B, m]",
				"when argument iterable contains all set items, removes all items;" +
					"[e, 5, T]; [T, e, G, 5];" +
					"[]",
				"when set is empty, does nothing;" +
					"[]; [a, 9, w];" +
					"[]"
			})
			void testAllItemsDistinct(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when some argument items are same")
			@Tests({
				"when all argument items are present, removes all distinct items;" +
					"[d, P, 3, x]; [P, P, x, 3];" +
					"[d]",
				"when some argument items are present, removes present distinct items;" +
					"[O, v, 6, C]; [v, U, v, C];" +
					"[O, 6]",
				"when all argument items are absent, does nothing;" +
					"[i, 2, r, N]; [g, g, g, g];" +
					"[i, 2, r, N]",
				"when argument iterable contains all set items, removes all items;" +
					"[p, 8, X]; [X, X, p, 8, 8];" +
					"[]",
				"when set is empty, does nothing;" +
					"[]; [M, M, 1, M];" +
					"[]"
			})
			void testSomeItemsSame(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when all argument items are same")
			@Tests({
				"when all argument items are present, removes distinct item;" +
					"[b, 0, W, c]; [W, W, W];" +
					"[b, 0, c]",
				"when all argument items are absent, does nothing;" +
					"[y, 3, K]; [P, P, P];" +
					"[y, 3, K]",
				"when set is empty, does nothing;" +
					"[]; [h, h];" +
					"[]"
			})
			void testAllItemsSame(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}
		}

		@DisplayName("when some argument items are null")
		@Nested
		class SomeItemsNullTests {
			@DisplayName("when all non-null argument items are distinct")
			@Tests({
				"when all argument items are present, removes all non-null items;" +
					"[s, 6, H, t, f]; [H, null, t, null];" +
					"[s, 6, f]",
				"when some argument items are present, removes present non-null items;" +
					"[o, 9, V, m]; [null, V, null, R, o];" +
					"[m]",
				"when all argument items are absent, does nothing;" +
					"[l, 2, A, p]; [null, k, null, 4];" +
					"[l, 2, A, p]",
				"when set is empty, does nothing;" +
					"[]; [null, G, null, 8];" +
					"[]"
			})
			void testAllItemsDistinct(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when some non-null argument items are same")
			@Tests({
				"when all argument items are present, removes all distinct non-null items;" +
					"[Z, 5, j, q]; [j, null, q, j];" +
					"[Z, 5]",
				"when some argument items are present, removes present distinct non-null items;" +
					"[h, 7, e, x]; [null, x, null, e, D];" +
					"[h, 7]",
				"when all argument items are absent, does nothing;" +
					"[w, 0, J, n]; [null, b, null, b];" +
					"[w, 0, J, n]",
				"when set is empty, does nothing;" +
					"[]; [E, null, E, null];" +
					"[]"
			})
			void testSomeItemsSame(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when all non-null argument items are same")
			@Tests({
				"when all argument items are present, removes distinct non-null item;" +
					"[c, 4, S, y]; [4, null, null, 4];" +
					"[c, S, y]",
				"when all argument items are absent, does nothing;" +
					"[M, 1, d]; [null, f, null, f];" +
					"[M, 1, d]",
				"when set is empty, does nothing;" +
					"[]; [null, i, i, null];" +
					"[]"
			})
			void testAllItemsSame(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}
		}

		@DisplayName("when all argument items are null")
		@Nested
		class AllItemsNullTests {
			@DisplayName("\uD83D\uDCB0")
			@Tests({
				"when set is not empty, does nothing;" +
					"[r, 6, N, u]; [null, null, null, null, null, null, null, null, null, null, null];" +
					"[r, 6, N, u]",
				"when set is empty, does nothing;" +
					"[]; [null, null];" +
					"[]"
			})
			void testAllItemsNull(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}
		}

		@DisplayName("when argument iterable is empty")
		@Nested
		class ArgumentIterableEmptyTests {
			@DisplayName("\uD83E\uDDF4")
			@Tests({
				"when set is not empty, does nothing;" +
					"[K, 3, x, b, 1]; [];" +
					"[K, 3, x, b, 1]",
				"when set is empty, does nothing;" +
					"[]; [];" +
					"[]"
			})
			void testItemsEmpty(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}
		}

		private void test(MutableSet<String> items1, String[] items2,
			MutableSet<String> expected) {

			final var returned = items1.remove(
				Arrays.asList(items2));

			assertSame(items1, returned,
				format("%s.remove(%s)", items1,
					Arrays.toString(items2)));
			assertEquals(expected, items1,
				format("%s.remove(%s)", items1,
					Arrays.toString(items2)));
		}
	}

	@DisplayName(".removeAll()")
	@Nested
	class RemoveAllTests {
		@DisplayName("\uD83E\uDDF9")
		@Tests({
			"when set is not empty, removes items;" +
				"[v, 9, z, L, 3];" +
				"[]",
			"when set is empty, does nothing;" +
				"[];" +
				"[]"
		})
		void test(@StringMutableSet MutableSet<String> items,
			@StringMutableSet MutableSet<String> expected) {

			final var returned = items.removeAll();
			assertSame(items, returned,
				format("%s.removeAll()", items));
			assertEquals(expected, items,
				format("%s.removeAll()", items));
		}
	}

	@DisplayName(".toImmutable()")
	@Nested
	class ToImmutableTests {
		@DisplayName("\uD83D\uDD12")
		@Tests({
			"when set is not empty, returns items in immutable set;" +
				"[n, 5, Y, t, 7];" +
				"[n, 5, Y, t, 7]",
			"when set is empty, returns empty immutable set;" +
				"[];" +
				"[]"
		})
		void test(@StringMutableSet MutableSet<String> items,
			@StringSet Set<String> expected) {

			final var returned = items.toImmutable();
			final var klass = returned.getClass();

			assertEquals(Set.class, klass,
				format("%s.toImmutable()", items));
			assertEquals(expected, returned,
				format("%s.toImmutable()", items));
		}
	}
}