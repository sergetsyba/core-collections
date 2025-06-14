package com.tsyba.core.collections;

import com.tsyba.core.collections.converter.StringArray;
import com.tsyba.core.collections.converter.StringCollection;
import com.tsyba.core.collections.converter.StringMutableSet;
import com.tsyba.core.collections.converter.StringSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.Arrays;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class MutableSetTests {
	@DisplayName(".add(T)")
	@Nested
	class AddTests {
		@DisplayName("when argument item is not null")
		@Tests({
			"when argument item is absent, adds argument item;" +
				"[g, e, Q, s, A, m]; P;" +
				"[g, e, Q, s, A, m, P]",
			"when argument item is present, does nothing;" +
				"[g, e, Q, s, A, m]; Q;" +
				"[g, e, Q, s, A, m]",
			"when set is empty, adds argument item;" +
				"[]; V;" +
				"[V]",
		})
		void testItemNotNull(@StringMutableSet MutableSet<String> items, String item,
			@StringMutableSet MutableSet<String> expected) {
			test(items, item, expected);
		}

		@DisplayName("when argument item is null")
		@Tests({
			"when set is not empty, does nothing;" +
				"[b, M, L, s, P]; null;" +
				"[b, M, L, s, P]",
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
	class AddWithVarargsTests {
		@DisplayName("when no argument items are null")
		@Nested
		class NoItemsNullTests {
			@DisplayName("when all argument items are distinct")
			@Tests({
				"when all argument items are absent, adds all argument items;" +
					"[e, P, z, q, r, s]; [k, O, v, w];" +
					"[e, P, z, q, r, s, k, O, v, w]",
				"when some argument items are present, adds absent argument items;" +
					"[t, Y, v, b]; [Y, c, t, d];" +
					"[t, Y, v, b, c, d]",
				"when all argument items are present, does nothing;" +
					"[x, K, m, P, w]; [K, m, P];" +
					"[x, K, m, P, w]",
				"when set is empty, adds all argument items;" +
					"[]; [M, I, O, P];" +
					"[M, I, O, P]",
			})
			void testAllItemsDistinct(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when some argument items are same")
			@Tests({
				"when all argument items are absent, adds all distinct argument items;" +
					"[e, P, z, q, r, s]; [k, k, O, k, v, k];" +
					"[e, P, z, q, r, s, k, O, v]",
				"when some argument items are present, adds absent distinct argument items;" +
					"[d, F, s, t]; [f, d, S, t, d, d, d, f];" +
					"[d, F, s, t, f, S]",
				"when all argument items are present, does nothing;" +
					"[m, g, v, h, I]; [g, m, I, g, g, m];" +
					"[m, g, v, h, I]",
				"when set is empty, adds all distinct argument items;" +
					"[]; [y, a, Y, b, y, C, y, y];" +
					"[y, a, Y, b, C]"
			})
			void testSomeItemsSame(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when all argument items are same")
			@Tests({
				"when all argument items are absent, adds distinct absent argument item;" +
					"[r, l, f, H]; [g, g, g];" +
					"[r, l, f, H, g]",
				"when all argument items are present, does nothing;" +
					"[u, I, o, C, q, r, s]; [C, C];" +
					"[u, I, o, C, q, r, s]",
				"when set is empty, adds distinct absent argument item;" +
					"[]; [D, D, D, D, D];" +
					"[D]"
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
				"when all argument items are absent, adds all non-null argument items;" +
					"[v, N, g]; [null, f, R, null, null];" +
					"[v, N, g, f, R]",
				"when some argument items are present, adds absent non-null argument items;" +
					"[v, N, g]; [G, null, N, null, v];" +
					"[v, N, g, G]",
				"when all argument items are present, does nothing;" +
					"[v, N, g]; [N, null, g, null, v, null, null];" +
					"[v, N, g]",
				"when set is empty, adds all non-null argument items;" +
					"[]; [null, j, null, null, E];" +
					"[j, E]"
			})
			void testAllItemsDistinct(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when some non-null argument items are same")
			@Tests({
				"when all argument items are absent, adds all distinct non-null argument items;" +
					"[f, t, P, l, Q]; [p, null, null, p, F, null, F];" +
					"[f, t, P, l, Q, p, F]",
				"when some argument items are present, adds absent distinct non-null argument items;" +
					"[f, t, P, l, Q]; [null, t, T, null, T, null, f];" +
					"[f, t, P, l, Q, T]",
				"when all argument items are present, does nothing;" +
					"[f, t, P, l, Q]; [null, null, Q, Q, null, Q, l, null];" +
					"[f, t, P, l, Q]",
				"when set is empty, adds all distinct non-null argument items;" +
					"[]; [b, null, null, j, null, j, n, n, null];" +
					"[b, j, n]"
			})
			void testSomeItemsSame(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}
		}

		@DisplayName("when all argument items are null")
		@Nested
		class AllItemsNullTests {
			@DisplayName("\uD83D\uDC62")
			@Tests({
				"when set is not empty, does nothing;" +
					"[t, R, s, b]; [null, null, null];" +
					"[t, R, s, b]",
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

		@DisplayName("when argument array is empty")
		@Nested
		class ArgumentArrayEmptyTests {
			@DisplayName("\uD83C\uDFB4")
			@Tests({
				"when set is not empty, does nothing;" +
					"[t, R, s, b]; [];" +
					"[t, R, s, b]",
				"when set is empty, does nothing;" +
					"[]; [];" +
					"[]"
			})
			void testArgumentArrayEmpty(@StringMutableSet MutableSet<String> items1,
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

	@DisplayName(".add(Collection<T>)")
	@Nested
	class AddWithCollectionTests {
		@DisplayName("when all argument items are distinct")
		@Tests({
			"when all argument items are absent, adds all argument items;" +
				"[g, R, s, k, b]; [v, l];" +
				"[g, R, s, k, b, v, l]",
			"when some argument items are present, adds absent argument items;" +
				"[v, f, s, W, a]; [v, s, r, E];" +
				"[v, f, s, W, a, r, E]",
			"when all argument items are present, does nothing;" +
				"[f, R, u, I]; [I, u, f];" +
				"[f, R, u, I]",
			"when set is empty, adds all argument items;" +
				"[]; [b, K, O, p, l];" +
				"[b, K, O, p, l]"
		})
		void testAllItemsDistinct(@StringMutableSet MutableSet<String> items1,
			@StringCollection Collection<String> items2,
			@StringMutableSet MutableSet<String> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when some argument items are same")
		@Tests({
			"when all argument items are absent, adds all distinct argument items;" +
				"[b, F, r, s]; [h, K, l, K, h, h];" +
				"[b, F, r, s, h, K, l]",
			"when some argument items are present, adds absent distinct argument items;" +
				"[f, R, s]; [R, k, L, R, s, R];" +
				"[f, R, s, k, L]",
			"when all argument items are present, does nothing;" +
				"[r, S, v, b]; [b, S, S, b, S];" +
				"[r, S, v, b]",
			"when set is empty, adds all distinct argument items;" +
				"[]; [R, s, a, s, a, s];" +
				"[R, s, a]"
		})
		void testSomeItemsSame(@StringMutableSet MutableSet<String> items1,
			@StringCollection Collection<String> items2,
			@StringMutableSet MutableSet<String> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when all argument items are same")
		@Tests({
			"when all argument items are absent, adds single distinct argument item;" +
				"[r, E, a, b, k]; [o, o, o, o];" +
				"[r, E, a, b, k, o]; [o]",
			"when all argument items are present, does nothing;" +
				"[g, H, t, u]; [H, H];" +
				"[g, H, t, u]",
			"when set is empty, adds single distinct argument item;" +
				"[]; [k, k, k];" +
				"[k]"
		})
		void testAllItemsSame(@StringMutableSet MutableSet<String> items1,
			@StringCollection Collection<String> items2,
			@StringMutableSet MutableSet<String> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when argument collection is empty")
		@Tests({
			"when set is not empty, does nothing;" +
				"[b, N, K, O, p]; [];" +
				"[b, N, K, O, p]",
			"when set is empty, does nothing;" +
				"[]; [];" +
				"[]"
		})
		void testArgumentCollectionEmpty(@StringMutableSet MutableSet<String> items1,
			@StringCollection Collection<String> items2,
			@StringMutableSet MutableSet<String> expected) {
			test(items1, items2, expected);
		}

		private void test(MutableSet<String> items1, Collection<String> items2,
			MutableSet<String> expected) {

			final var returned = items1.add(items2);
			assertSame(items1, returned,
				format("%s.add(%s)", items1, items2));
			assertEquals(expected, items1,
				format("%s.add(%s)", items1, items2));
		}
	}

	@DisplayName(".add(Iterable<T>)")
	@Nested
	class AddWithIterableTests {
		@DisplayName("when no argument items are null")
		@Nested
		class NoItemsNullTests {
			@DisplayName("when all argument items are distinct")
			@Tests({
				"when all argument items are absent, adds all argument items;" +
					"[x, K, m, P, w]; [N, q, r];" +
					"[x, K, m, P, w, N, q, r]",
				"when some argument items are present, adds absent argument items;" +
					"[t, Y]; [Y, c, t, d, v, e];" +
					"[t, Y, c, d, v, e]",
				"when all argument items are present, does nothing;" +
					"[s, W, l, M, p, k]; [M, s, W, l];" +
					"[s, W, l, M, p, k]",
				"when set is empty, adds all argument items;" +
					"[]; [f, R, t, X, q, z];" +
					"[f, R, t, X, q, z]"
			})
			void testAllItemsDistinct(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when some argument items are same")
			@Tests({
				"when all argument items are absent, adds all distinct argument items;" +
					"[b, G, v, h, J]; [g, m, I, g, g, m];" +
					"[b, G, v, h, J, g, m, I]",
				"when some argument items are present, adds absent distinct argument items;" +
					"[d, F]; [f, d, S, t, d, d, d, f];" +
					"[d, F, f, S, t]",
				"when all argument items are present, does nothing;" +
					"[e, P, z, q, r, s]; [P, z, z, P, P, r];" +
					"[e, P, z, q, r, s]",
				"when set is empty, adds all distinct argument items;" +
					"[]; [y, a, Y, b, y, C, y, y];" +
					"[y, a, Y, b, C]"
			})
			void testSomeItemsSame(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when all argument items are same")
			@Tests({
				"when all argument items are absent, adds single distinct argument item;" +
					"[u, I, o, p, q, r, s]; [C, C];" +
					"[u, I, o, p, q, r, s, C]",
				"when all argument items are present, does nothing;" +
					"[g, H]; [g, g, g];" +
					"[g, H]",
				"when set is empty, adds single distinct argument item;" +
					"[]; [D, D, D, D, D];" +
					"[D]"
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
				"when all argument items are absent, adds all non-null argument items;" +
					"[j, M]; [null, R, s, null, null, t];" +
					"[j, M, R, s, t]",
				"when some argument items are present, adds absent non-null argument items;" +
					"[l, N, q, r, s, t]; [r, null, N, null, L, null, s];" +
					"[l, N, q, r, s, t, L]",
				"when all argument items are present, does nothing;" +
					"[m, O]; [O, null, m, null, null, null];" +
					"[m, O]",
				"when set is empty, adds all non-null argument items;" +
					"[]; [null, T, null, null, u, V, null];" +
					"[T, u, V]"
			})
			void testAllItemsDistinct(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when some non-null argument items are same")
			@Tests({
				"when all argument items are absent, adds all distinct non-null argument items;" +
					"[n, P, s, w, x]; [W, null, null, W, X, null, X, y];" +
					"[n, P, s, w, x, W, X, y]",
				"when some argument items are present, adds absent distinct non-null argument items;" +
					"[o, Q]; [null, Q, v, null, v, null, o, w, v];" +
					"[o, Q, v, w]",
				"when all argument items are present, does nothing;" +
					"[p, R, u, v, w, x, y]; [null, null, R, R, null, R, p, null, u];" +
					"[p, R, u, v, w, x, y]",
				"when set is empty, adds all distinct non-null argument items;" +
					"[]; [x, null, null, y, null, y, Z, Z, null, a];" +
					"[x, y, Z, a]"
			})
			void testSomeItemsSame(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}
		}

		@DisplayName("when all argument items are null")
		@Nested
		class AllItemsNullTests {
			@DisplayName("\uD83D\uDC62")
			@Tests({
				"when set is not empty, does nothing;" +
					"[q, S, v, w, x, y, z]; [null, null, null, null];" +
					"[q, S, v, w, x, y, z]",
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

		@DisplayName("when argument iterable is empty")
		@Nested
		class ArgumentIterableEmptyTests {
			@DisplayName("\uD83C\uDF54")
			@Tests({
				"when set is not empty, does nothing;" +
					"[h, R, s, b]; [];" +
					"[h, R, s, b]",
				"when set is empty, does nothing;" +
					"[]; [];" +
					"[]"
			})
			void testArgumentIterableEmpty(@StringMutableSet MutableSet<String> items1,
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
			"when argument item is present, removes argument item;" +
				"[g, R, s, B, n]; R;" +
				"[g, s, B, n]",
			"when argument item is absent, does nothing;" +
				"[g, R, s, B, n]; O;" +
				"[g, R, s, B, n]",
			"when set is empty, does nothing;" +
				"[]; R;" +
				"[]"
		})
		void testItemNotNull(@StringMutableSet MutableSet<String> items, String item,
			@StringMutableSet MutableSet<String> expected) {
			test(items, item, expected);
		}

		@DisplayName("when argument item is null")
		@Tests({
			"when set is not empty, does nothing;" +
				"[g, M, n, I]; null;" +
				"[g, M, n, I]",
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
	class RemoveWithVarargsTests {
		@DisplayName("when no argument items are null")
		@Nested
		class NoItemsNullTests {
			@DisplayName("when all argument items are distinct")
			@Tests({
				"when all argument items are present, removes all argument items;" +
					"[h, F, D, n, v, s]; [n, F, s, D];" +
					"[h, v]",
				"when some argument items are present, removes present argument items;" +
					"[h, K, m, d, S]; [r, E, K, S, h];" +
					"[m, d]",
				"when all argument items are absent, does nothing;" +
					"[b, R, s, k]; [P, o, L, d];" +
					"[b, R, s, k]",
				"when argument array contains all items, removes all items;" +
					"[b, e, s, F, g]; [s, g, F, e, f, W, b];" +
					"[]",
				"when set is empty, does nothing;" +
					"[]; [r, E, s, a, q];" +
					"[]"
			})
			void testAllItemsDistinct(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when some argument items are same")
			@Tests({
				"when all argument items are present, removes all distinct argument items;" +
					"[b, F, s]; [F, b, b, b, F];" +
					"[s]",
				"when some argument items are present, removes distinct present argument items;" +
					"[t, R, l, K, s]; [y, U, y, y, t, l, l, K, l];" +
					"[R, s]",
				"when all argument items are absent, does nothing;" +
					"[e, a, s, t]; [u, I, k, v];" +
					"[e, a, s, t]",
				"when argument array contains all items, removes all items;" +
					"[f, R]; [R, h, m, R, f, f, R, m];" +
					"[]",
				"when set is empty, does nothing;" +
					"[]; [b, n, b, h, h, b];" +
					"[]"
			})
			void testSomeItemsSame(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when all argument items are same")
			@Tests({
				"when all argument items are present, removes single distinct argument item;" +
					"[r, f, q, w]; [r, r, r];" +
					"[f, q, w]",
				"when all argument items are absent, does nothing;" +
					"[r, f, q, w]; [R, R, R, R];" +
					"[r, f, q, w]",
				"when set is empty, does nothing;" +
					"[]; [r, r, r];" +
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
				"when all argument items are present, removes all non-null present argument items;" +
					"[b, E, s, f]; [s, null, E, null, null];" +
					"[b, f]",
				"when some argument items are present, removes non-null present  argument items;" +
					"[f, h, G, l]; [b, O, null, h, null, e, q, null];" +
					"[f, G, l]",
				"when all argument items are absent, does nothing;" +
					"[b, r]; [null, f, null, E, null];" +
					"[b, r]",
				"when set is empty, does nothing;" +
					"[]; [null, t, null, O, d];" +
					"[]"
			})
			void testAllItemsDistinct(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when some non-null argument items are same")
			@Tests({
				"when all argument items are present, removes all non-null distinct argument items;" +
					"[b, I, j, D]; [null, b, null, j, null, null];" +
					"[I, D]",
				"when some argument items are present, removes non-null distinct present argument items;" +
					"[b, F, d, a]; [null, r, I, null, F, b, null];" +
					"[d, a]",
				"when all argument items are absent, does nothing;" +
					"[u, I, l, k]; [null, G, m, n, null];" +
					"[u, I, l, k]",
				"when set is empty, does nothing;" +
					"[]; [b, null, e];" +
					"[]"
			})
			void testSomeItemsSame(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when all non-null argument items are same")
			@Tests({
				"when all argument items are present, removes single non-null distinct argument item;" +
					"[b, D, s, f]; [f, null, null, null, f];" +
					"[b, D, s]",
				"when all argument items are absent, does nothing;" +
					"[b, n, m]; [null, V, V, null];" +
					"[b, n, m]",
				"when set is empty, does nothing;" +
					"[]; [T, null, T, T];" +
					"[]"
			})
			void testAllItemsSame(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}
		}

		@DisplayName("when all arguments items are null")
		@Nested
		class AllItemsNullTests {
			@DisplayName("\uD83D\uDCA6")
			@Tests({
				"when set is not empty, does nothing;" +
					"[n, J, k, f, d]; [null, null, null];" +
					"[n, J, k, f, d]",
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

		@DisplayName("when argument array is empty")
		@Nested
		class ArgumentArrayEmptyTests {
			@DisplayName("\uD83D\uDCA6")
			@Tests({
				"when set is not empty, does nothing;" +
					"[b, N, m, J]; [];" +
					"[b, N, m, J]",
				"when set is empty, does nothing;" +
					"[]; [null, null];" +
					"[]"
			})
			void testArgumentArrayEmpty(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}
		}

		private void test(MutableSet<String> items1, String[] items2,
			MutableSet<String> expected) {

			final var returned = items1.remove(items2);
			assertSame(items1, returned, format("%s.remove(%s)",
				items1, Arrays.toString(items2)));
			assertEquals(expected, returned, format("%s.remove(%s)",
				items1, Arrays.toString(items2)));
		}
	}

	@DisplayName(".remove(Collection<T>)")
	@Nested
	class RemoveWithCollectionTests {
		@DisplayName("when all argument items are distinct")
		@Tests({
			"when all argument items are present, removes all argument items;" +
				"[x, K, m, P, w]; [K, m, P];" +
				"[x, w]",
			"when some argument items are present, removes present argument items;" +
				"[t, Y, v, b]; [Y, c, t, d];" +
				"[v, b]",
			"when all argument items are absent, does nothing;" +
				"[s, W, l, M, p, k]; [q, r, n];" +
				"[s, W, l, M, p, k]",
			"when set is empty, does nothing;" +
				"[]; [f, R, t];" +
				"[]"
		})
		void testAllItemsDistinct(@StringMutableSet MutableSet<String> items1,
			@StringCollection Collection<String> items2,
			@StringMutableSet MutableSet<String> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when some argument items are same")
		@Tests({
			"when all argument items are present, removes all distinct argument items;" +
				"[m, g, v, h, I]; [g, m, I, g, g, m];" +
				"[v, h]",
			"when some argument items are present, removes present distinct argument items;" +
				"[d, F, s, t]; [f, d, S, t, d, d, d, f];" +
				"[F, s]",
			"when set is empty, does nothing;" +
				"[]; [y, a, Y, b, y, C, y, y];" +
				"[]"
		})
		void testSomeItemsSame(@StringMutableSet MutableSet<String> items1,
			@StringCollection Collection<String> items2,
			@StringMutableSet MutableSet<String> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when all argument items are same")
		@Tests({
			"when all argument items are present, removes single distinct argument item;" +
				"[u, I, o, C, q, r, s]; [C, C];" +
				"[u, I, o, q, r, s]",
			"when all argument items are absent, does nothing;" +
				"[r, l, f, H]; [g, g, g];" +
				"[r, l, f, H]",
			"when set is empty, does nothing;" +
				"[]; [D, D, D, D, D];" +
				"[]"
		})
		void testAllItemsSame(@StringMutableSet MutableSet<String> items1,
			@StringCollection Collection<String> items2,
			@StringMutableSet MutableSet<String> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when argument collection is empty")
		@Tests({
			"when set is not empty, does nothing;" +
				"[o, R, g, T]; [];" +
				"[o, R, g, T]",
			"when set is empty, does nothing;" +
				"[]; [];" +
				"[]",
		})
		void testArgumentCollectionEmpty(@StringMutableSet MutableSet<String> items1,
			@StringCollection Collection<String> items2,
			@StringMutableSet MutableSet<String> expected) {
			test(items1, items2, expected);
		}

		private void test(MutableSet<String> items1, Collection<String> items2,
			MutableSet<String> expected) {

			final var returned = items1.remove(items2);
			assertSame(items1, returned,
				format("%s.remove(%s)", items1, items2));
			assertEquals(expected, items1,
				format("%s.remove(%s)", items1, items2));
		}
	}

	@DisplayName(".remove(Iterable<T>)")
	@Nested
	class RemoveWithIterableTests {
		@DisplayName("when no argument items are null")
		@Nested
		class NoItemsNullTests {
			@DisplayName("when all argument items are distinct")
			@Tests({
				"when all argument items are present, removes all argument items;" +
					"[m, n, J, K, f]; [m, J];" +
					"[n, K, f]",
				"when some argument items are present, removes present argument items;" +
					"[n, M, m, L, I, o, p]; [U, L, N, p];" +
					"[n, M, m, I, o]",
				"when all argument items are absent, does nothing;" +
					"[e, v, b]; [U, P, O, N];" +
					"[e, v, b]",
				"when set is empty, does nothing;" +
					"[]; [M, I, O, P];" +
					"[]"
			})
			void testAllItemsDistinct(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when some argument items are same")
			@Tests({
				"when all argument items are present, removes all distinct argument items;" +
					"[m, g, v, h, I]; [g, m, I, g, g, m];" +
					"[v, h]",
				"when some argument items are present, removes present distinct argument items;" +
					"[d, F, s, t]; [f, d, S, t, d, d, d, f];" +
					"[F, s]",
				"when all argument items are absent, does nothing;" +
					"[e, P, z, q, r, s]; [k, k, O, k, v, k];" +
					"[e, P, z, q, r, s]",
				"when set is empty, does nothing;" +
					"[]; [y, a, Y, b, y, C, y, y];" +
					"[]"
			})
			void testSomeItemsSame(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when all argument items are same")
			@Tests({
				"when all argument items are present, removes single distinct argument item;" +
					"[u, I, o, C, q, r, s]; [C, C];" +
					"[u, I, o, q, r, s]",
				"when all argument items are absent, does nothing;" +
					"[r, l, f, H]; [g, g, g];" +
					"[r, l, f, H]",
				"when set is empty, does nothing;" +
					"[]; [D, D, D, D, D];" +
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
				"when all argument items are present, removes all non-null argument items;" +
					"[j, N, k, L, I]; [N, null, L, null, I];" +
					"[j, k]",
				"when some argument items are present, removes present non-null argument items;" +
					"[g, H, m, L, p]; [null, L, G, null, p];" +
					"[g, H, m]",
				"when all argument items are absent, does nothing;" +
					"[q, R, s, t, u]; [V, null, W, null, X];" +
					"[q, R, s, t, u]",
				"when set is empty, does nothing;" +
					"[]; [M, null, I, null, O];" +
					"[]"
			})
			void testAllItemsDistinct(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when some argument items are same")
			@Tests({
				"when all argument items are present, removes all non-null distinct argument items;" +
					"[m, g, v, h, I]; [g, null, m, I, g, null, m];" +
					"[v, h]",
				"when some argument items are present, removes present non-null distinct argument items;" +
					"[d, F, s, t]; [null, d, S, t, t, t, d, null, f];" +
					"[F, s]",
				"when all argument items are absent, does nothing;" +
					"[e, P, z, q, r, s]; [k, null, k, O, k, null, v, O, O];" +
					"[e, P, z, q, r, s]",
				"when set is empty, does nothing;" +
					"[]; [y, null, Y, b, y, null, C, Y, Y];" +
					"[]"
			})
			void testSomeItemsSame(@StringMutableSet MutableSet<String> items1,
				@StringArray String[] items2,
				@StringMutableSet MutableSet<String> expected) {
				test(items1, items2, expected);
			}

			@DisplayName("when all argument items are same")
			@Tests({
				"when all argument items are present, removes single non-null distinct argument item;" +
					"[u, I, o, C, q, r, s]; [C, null, C];" +
					"[u, I, o, q, r, s]",
				"when all argument items are absent, does nothing;" +
					"[r, l, f, H]; [g, null, g, g];" +
					"[r, l, f, H]",
				"when set is empty, does nothing;" +
					"[]; [D, null, D, D, null];" +
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
			@DisplayName("\uD83D\uDECF")
			@Tests({
				"when set is not empty, does nothing;" +
					"[b, V, c]; [null, null];" +
					"[b, V, c]",
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
			@DisplayName("\uD83D\uDCB4")
			@Tests({
				"when set is not empty, does nothing;" +
					"[r, E, v, K, O]; [];" +
					"[r, E, v, K, O]",
				"when set is empty, does nothing;" +
					"[]; [];" +
					"[]"
			})
			void testArgumentIterableEmpty(@StringMutableSet MutableSet<String> items1,
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
		@Tests({
			"when set is not empty, removes all items;" +
				"[t, K, L, s, B];" +
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
		@Tests({
			"when set is not empty, returns all items in immutable set;" +
				"[g, R, e, A, s];" +
				"[g, R, e, A, s]",
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
