package com.tsyba.core.collections;

import com.tsyba.core.collections.converter.StringArray;
import com.tsyba.core.collections.converter.StringPairSet;
import com.tsyba.core.collections.converter.StringSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.Arrays;
import java.util.HashSet;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

class SetTests {
	@DisplayName("Set(T...)")
	@Nested
	class NewVarargsTests {
		@DisplayName("when no argument items are null")
		@Tests({
			"when all argument items are distinct, creates set with all items;" +
				"[P, 4, j, O, l, a];" +
				"[P, 4, j, O, l, a]",
			"when some argument items are same, creates set with distinct items;" +
				"[e, 6, l, D, e, 6, l];" +
				"[e, 6, l, D]",
			"when all argument items are same, creates set with distinct item;" +
				"[p, p, p, p, p];" +
				"[p]"
		})
		void testNoItemsNull(@StringArray String[] items,
			@StringSet Set<String> expected) {
			test(items, expected);
		}

		@DisplayName("when some argument items are null")
		@Tests({
			"when all non-null argument items are distinct, creates set with all non-null items;" +
				"[e, Z, 6, null, null, null, F];" +
				"[e, Z, 6, F]",
			"when some non-null argument items are same, creates set with distinct non-null items;" +
				"[null, null, j, 5, j, d, null, d];" +
				"[j, 5, d]",
			"when all non-null argument items are same, creates set with distinct non-null item;" +
				"[n, n, null, n, null, null];" +
				"[n]"
		})
		void testSomeItemsNull(@StringArray String[] items,
			@StringSet Set<String> expected) {
			test(items, expected);
		}

		@DisplayName("when all argument items are null")
		@Tests({
			"creates empty set;" +
				"[null, null, null, null, null, null, null, null];" +
				"[]"
		})
		void testAllItemsNull(@StringArray String[] items,
			@StringSet Set<String> expected) {
			test(items, expected);
		}

		@DisplayName("when argument array is empty")
		@Tests({
			"creates empty set;" +
				"[];" +
				"[]"
		})
		void testItemsEmpty(@StringArray String[] items,
			@StringSet Set<String> expected) {
			test(items, expected);
		}

		private void test(String[] items, Set<String> expected) {
			final var set = new Set<>(items);
			assertEquals(expected, set,
				format("new Set<>(%s)",
					Arrays.toString(items)));
		}
	}

	@DisplayName("Set(Iterable<T>)")
	@Nested
	class NewIterableTests {
		@DisplayName("when no argument items are null")
		@Tests({
			"when all argument items are distinct, creates set with all items;" +
				"[o, W, u, g, 2, r];" +
				"[o, W, u, g, 2, r]",
			"when some argument items are same, creates set with distinct items;" +
				"[q, 5, 1, L, q, L, 5];" +
				"[q, 5, 1, L]",
			"when all argument items are same, creates set with distinct item;" +
				"[E, E, E, E, E];" +
				"[E]"
		})
		void testNoItemsNull(@StringArray String[] items,
			@StringSet Set<String> expected) {
			test(items, expected);
		}

		@DisplayName("when some argument items are null")
		@Tests({
			"when all non-null argument items are distinct, creates set with all non-null items;" +
				"[null, null, D, null, j, v, 0];" +
				"[D, j, v, 0]",
			"when some non-null argument items are same, creates set with distinct non-null items;" +
				"[s, null, S, s, S, 1, null, null];" +
				"[s, S, 1]",
			"when all non-null argument items are same, creates set with distinct non-null item;" +
				"[null, null, N, null, N, N];" +
				"[N]"
		})
		void testSomeItemsNull(@StringArray String[] items,
			@StringSet Set<String> expected) {
			test(items, expected);
		}

		@DisplayName("when all argument items are null")
		@Tests({
			"creates empty set;" +
				"[null, null, null, null, null, null, null, null, null];" +
				"[]"
		})
		void testAllItemsNull(@StringArray String[] items,
			@StringSet Set<String> expected) {
			test(items, expected);
		}

		@DisplayName("when argument iterable is empty")
		@Tests({
			"creates empty set;" +
				"[];" +
				"[]"
		})
		void testItemsEmpty(@StringArray String[] items,
			@StringSet Set<String> expected) {
			test(items, expected);
		}

		private void test(String[] items, Set<String> expected) {
			final var set = new Set<>(
				Arrays.asList(items));

			assertEquals(expected, set,
				format("new Set<>(%s)",
					Arrays.toString(items)));
		}
	}

	@DisplayName(".getCount()")
	@Nested
	class GetCountTests {
		@DisplayName("\uD83C\uDFAF")
		@Tests({
			"when set is not empty, returns item count;" +
				"[f, s, V, t, A, L, W];" +
				"7",
			"when set is empty, returns 0;" +
				"[];" +
				"0"
		})
		void test(@StringSet Set<String> items, int expected) {
			final var count = items.getCount();
			assertEquals(expected, count,
				format("%s.getCount()", items));
		}
	}

	@DisplayName(".getDistinct()")
	@Nested
	class GetDistinctTests {
		@DisplayName("\uD83C\uDF40")
		@Tests({
			"when set is not empty, returns itself;" +
				"[l, 5, s, x, 2, p]",
			"when set is empty, returns itself;" +
				"[]"
		})
		void test(@StringSet Set<String> items) {
			final var distinct = items.getDistinct();
			assertSame(items, distinct,
				format("%s.getDistinct()", items));
		}
	}

	@DisplayName(".isDisjoint(Set<T>)")
	@Nested
	class IsDisjointTests {
		@DisplayName("when set is not empty")
		@Tests({
			"when set and argument set are disjoint, returns true;" +
				"[9, F, V, s, P, Y]; [t, 8, q, h];" +
				"true",
			"when set and argument set intersect, returns false;" +
				"[l, p, v, 1, c, G]; [y, H, v, j, G];" +
				"false",
			"when set and argument set are equal, returns false;" +
				"[d, z, c, I, x]; [I, x, d, z, c];" +
				"false",
			"when argument set is empty, returns true;" +
				"[X, i, 7, g, e, h]; [];" +
				"true"
		})
		void testSetNotEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
			boolean expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when set is empty")
		@Tests({
			"when argument set is not empty, returns true;" +
				"[]; [O, k, P, i];" +
				"true",
			"when argument set is empty, returns true;" +
				"[]; [];" +
				"true"
		})
		void testSetEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
			boolean expected) {
			test(items1, items2, expected);
		}

		private void test(Set<String> items1, Set<String> items2, boolean expected) {
			final var disjoint = items1.isDisjoint(items2);
			assertEquals(expected, disjoint,
				format("%s.isDisjoint(%s)", items1, items2));
		}
	}

	@DisplayName(".intersects(Set<T>)")
	@Nested
	class IntersectsTests {
		@DisplayName("when set is not empty")
		@Tests({
			"when set and argument set are disjoint, returns false;" +
				"[F, J, o, 2, z, D]; [Z, u, 4, m];" +
				"false",
			"when set and argument set intersect, returns true;" +
				"[N, K, L, n, V, G]; [2, b, 6, N, L];" +
				"true",
			"when set and argument set are equal, returns true;" +
				"[i, f, W, 3, P]; [3, W, f, i, P];" +
				"true",
			"when argument set is empty, returns false;" +
				"[H, 0, p, s, o, c]; [];" +
				"false"
		})
		void testSetNotEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
			boolean expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when set is empty")
		@Tests({
			"when argument set is not empty, returns false;" +
				"[]; [E, N, D, J];" +
				"false",
			"when argument set is empty, returns false;" +
				"[]; [];" +
				"false"
		})
		void testSetEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
			boolean expected) {
			test(items1, items2, expected);
		}

		private void test(Set<String> items1, Set<String> items2, boolean expected) {
			final var intersects = items1.intersects(items2);
			assertEquals(expected, intersects,
				format("%s.intersects(%s)", items1, items2));
		}
	}

	@DisplayName(".unite(Set<T>)")
	@Nested
	class UniteTests {
		@DisplayName("when set is not empty")
		@Tests({
			"when set and argument set are disjoint, returns union set;" +
				"[x, W, 9, N, T, p]; [k, 4, X, D];" +
				"[x, W, 9, N, T, p, k, 4, X, D]",
			"when set and argument set intersect, returns union set;" +
				"[9, R, M, K, v, 1]; [K, 6, F, R, X];" +
				"[9, R, M, K, v, 1, 6, F, X]",
			"when set and argument set are equal, returns union set;" +
				"[a, 2, m, d, r]; [m, 2, d, r, a];" +
				"[a, 2, m, d, r]",
			"when argument set is empty, returns union set;" +
				"[f, G, R, W, q, z]; [];" +
				"[f, G, R, W, q, z]"
		})
		void testSetNotEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
			@StringSet Set<String> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when set is empty")
		@Tests({
			"when argument set is not empty, returns union set;" +
				"[]; [U, Z, 6, x];" +
				"[U, Z, 6, x]",
			"when argument set is empty, returns empty set;" +
				"[]; [];" +
				"[]"
		})
		void testSetEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
			@StringSet Set<String> expected) {
			test(items1, items2, expected);
		}

		private void test(Set<String> items1, Set<String> items2, Set<String> expected) {
			final var union = items1.unite(items2);
			assertEquals(expected, union,
				format("%s.unite(%s)", items1, items2));
		}
	}

	@DisplayName(".intersect(Set<T>)")
	@Nested
	class IntersectTests {
		@DisplayName("when set is not empty")
		@Tests({
			"when set and argument set are disjoint, returns empty set;" +
				"[0, T, W, d, 2, j]; [u, y, N, 7];" +
				"[]",
			"when set and argument set intersect, returns intersection set;" +
				"[M, 0, F, 3, 7, 2]; [3, s, R, E, 0];" +
				"[0, 3]",
			"when set and argument set are equal, returns intersection set;" +
				"[p, T, L, 0, h]; [L, h, 0, p, T];" +
				"[p, T, L, 0, h]",
			"when argument set is empty, returns empty set;" +
				"[K, a, V, P, c, D]; [];" +
				"[]"
		})
		void testSetNotEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
			@StringSet Set<String> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when set is empty")
		@Tests({
			"when argument set is not empty, returns empty set;" +
				"[]; [S, H, k, J];" +
				"[]",
			"when argument set is empty, returns empty set;" +
				"[]; [];" +
				"[]"
		})
		void testSetEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
			@StringSet Set<String> expected) {
			test(items1, items2, expected);
		}

		private void test(Set<String> items1, Set<String> items2, Set<String> expected) {
			final var intersection = items1.intersect(items2);
			assertEquals(expected, intersection,
				format("%s.intersect(%s)", items1, items2));
		}
	}

	@DisplayName(".subtract(Set<T>)")
	@Nested
	class SubtractTests {
		@DisplayName("when set is not empty")
		@Tests({
			"when set and argument set are disjoint, returns difference set;" +
				"[C, B, S, 9, x, Z]; [Q, V, f, e];" +
				"[C, B, S, 9, x, Z]",
			"when set and argument set intersect, returns difference set;" +
				"[I, A, 3, p, k, U]; [3, z, 2, I, 4];" +
				"[A, p, k, U]",
			"when set and argument set are equal, returns empty set;" +
				"[6, A, P, g, C]; [C, A, g, P, 6];" +
				"[]",
			"when argument set is empty, returns difference set;" +
				"[m, b, o, 7, 9, I]; [];" +
				"[m, b, o, 7, 9, I]"
		})
		void testSetNotEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
			@StringSet Set<String> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when set is empty")
		@Tests({
			"when argument set is not empty, returns empty set;" +
				"[]; [Z, x, t, 8];" +
				"[]",
			"when argument set is empty, returns empty set;" +
				"[]; [];" +
				"[]"
		})
		void testSetEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
			@StringSet Set<String> expected) {
			test(items1, items2, expected);
		}

		private void test(Set<String> items1, Set<String> items2, Set<String> expected) {
			final var difference = items1.subtract(items2);
			assertEquals(expected, difference,
				format("%s.subtract(%s)", items1, items2));
		}
	}

	@DisplayName(".symmetricSubtract(Set<T>)")
	@Nested
	class SymmetricSubtractTests {
		@DisplayName("when set is not empty")
		@Tests({
			"when set and argument set are disjoint, returns symmetric difference set;" +
				"[n, K, 6, X, 3, 8]; [i, T, q, J];" +
				"[n, K, 6, X, 3, 8, i, T, q, J]",
			"when set and argument set intersect, returns symmetric difference set;" +
				"[F, D, d, u, 0, 3]; [f, A, u, 1, 3];" +
				"[F, D, d, 0, f, A, 1]",
			"when set and argument set are equal, returns empty set;" +
				"[y, f, 8, l, 2]; [l, f, y, 2, 8];" +
				"[]",
			"when argument set is empty, returns symmetric difference set;" +
				"[v, D, R, m, G, X]; [];" +
				"[v, D, R, m, G, X]"
		})
		void testSetNotEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
			@StringSet Set<String> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when set is empty")
		@Tests({
			"when argument set is not empty, returns symmetric difference set;" +
				"[]; [t, D, 9, b];" +
				"[t, D, 9, b]",
			"when argument set is empty, returns empty set;" +
				"[]; [];" +
				"[]"
		})
		void testSetEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
			@StringSet Set<String> expected) {
			test(items1, items2, expected);
		}

		private void test(Set<String> items1, Set<String> items2, Set<String> expected) {
			final var difference = items1.symmetricSubtract(items2);
			assertEquals(expected, difference,
				format("%s.symmetricSubtract(%s)", items1, items2));
		}
	}

	@DisplayName(".multiply(Set<T>)")
	@Nested
	class MultiplyTests {
		@DisplayName("when set is not empty")
		@Tests({
			"when set and argument set are disjoint, returns product set;" +
				"[T, N, g, A]; [Q, d, P, q];" +
				"[T:Q, T:d, T:P, T:q, N:Q, N:d, N:P, N:q, g:Q, g:d, g:P, g:q, A:Q, A:d, A:P, A:q]",
			"when set and argument set intersect, returns product set;" +
				"[D, l, i, 6]; [8, 9, D, l];" +
				"[D:8, D:9, D:D, D:l, l:8, l:9, l:D, l:l, i:8, i:9, i:D, i:l, 6:8, 6:9, 6:D, 6:l]",
			"when set and argument set are equal, returns product set;" +
				"[G, j, X, K]; [X, G, j, K];" +
				"[G:G, G:j, G:X, G:K, j:G, j:j, j:X, j:K, X:G, X:j, X:X, X:K, K:G, K:j, K:X, K:K]",
			"when argument set is empty, returns empty set;" +
				"[F, c, 0, u]; [];" +
				"[]"
		})
		void testSetNotEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
			@StringPairSet Set<Pair<String, String>> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when set is empty")
		@Tests({
			"when argument set is not empty, returns empty set;" +
				"[]; [n, H, j, b];" +
				"[]",
			"when argument set is empty, returns empty set;" +
				"[]; [];" +
				"[]"
		})
		void testSetEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
			@StringPairSet Set<Pair<String, String>> expected) {
			test(items1, items2, expected);
		}

		private void test(Set<String> items1, Set<String> items2,
			Set<Pair<String, String>> expected) {

			final var product = items1.multiply(items2);
			assertEquals(expected, product,
				format("%s.multiply(%s)", items1, items2));
		}
	}

	@DisplayName(".matchAll(Predicate<T>)")
	@Nested
	class MatchAllTests {
		@DisplayName("\uD83C\uDF3C")
		@Tests({
			"when some items match, returns matching items;" +
				"[c, b, t, K, a, M, F];" +
				"[c, b, t, a]",
			"when all items match, returns matching items;" +
				"[p, b, v, c, q, k];" +
				"[p, b, v, c, q, k]",
			"when no items match, returns empty set;" +
				"[C, R, G, Q, Y];" +
				"[]",
			"when set is empty, returns empty set;" +
				"[];" +
				"[]"
		})
		void test(@StringSet Set<String> items, @StringSet Set<String> expected) {
			final var matched = items.matchAll((item) -> {
				return item.toLowerCase()
					.equals(item);
			});

			assertEquals(expected, matched,
				format("%s.matchAll(<is lowercase>)", items));
		}
	}

	@DisplayName(".iterate(Consumer<T>)")
	@Nested
	class IterateTests {
		@DisplayName("\uD83D\uDC0C")
		@Tests({
			"when set is not empty, iterates items;" +
				"[A, J, g, r, i, C];" +
				"[A, J, g, r, i, C]",
			"when set is empty, does nothing;" +
				"[];" +
				"[]"
		})
		void test(@StringSet Set<String> items, @StringArray String[] expected) {
			final var iterated = new HashSet<String>();
			final var returned = items.iterate(iterated::add);

			assertSame(items, returned,
				format("%s.iterate(Consumer<T>)", items));
			assertEquals(java.util.Set.of(expected), iterated,
				format("%s.iterate(Consumer<T>)", items));
		}
	}

	@DisplayName(".convert(Function<T, R>)")
	@Nested
	class ConvertTests {
		@DisplayName("when no items convert to null")
		@Tests({
			"when all converted items are distinct, returns all converted items;" +
				"[g, p, z, h, n, y];" +
				"[g, p, z, h, n, y]",
			"when some converted items are same, returns distinct converted items;" +
				"[i, f, n, e, o, k];" +
				"[a, f, n, k]",
			"when all converted items are same, returns distinct converted item;" +
				"[a, e, u, o];" +
				"[a]"
		})
		void testNoItemsConvertNull(@StringSet Set<String> items,
			@StringSet Set<String> expected) {
			test(items, expected);
		}

		@DisplayName("when some items convert to null")
		@Tests({
			"when all non-null converted items are distinct, returns all non-null converted items;" +
				"[q, R, N, n, W, d];" +
				"[q, n, d]",
			"when some non-null converted items are same, returns distinct non-null converted items;" +
				"[B, i, I, f, g, e, L];" +
				"[a, f, g]",
			"when all non-null converted items are same, returns distinct non-null converted item;" +
				"[u, X, a, V, o, P];" +
				"[a]"
		})
		void testSomeItemsConvertNull(@StringSet Set<String> items,
			@StringSet Set<String> expected) {
			test(items, expected);
		}

		@DisplayName("when all items convert to null")
		@Tests({
			"returns empty set;" +
				"[G, W, U, X, I];" +
				"[]"
		})
		void testAllItemsConvertNull(@StringSet Set<String> items,
			@StringSet Set<String> expected) {
			test(items, expected);
		}

		@DisplayName("when set is empty")
		@Tests({
			"returns empty set;" +
				"[];" +
				"[]"
		})
		void testSetEmpty(@StringSet Set<String> items,
			@StringSet Set<String> expected) {
			test(items, expected);
		}

		private void test(Set<String> items, Set<String> expected) {
			final var converted = items.convert((item) -> {
				// all lowercase vowels convert to a
				final var isVowel = new Set<>("a", "e", "o", "u", "i")
					.contains(item);

				if (isVowel) {
					return "a";
				}

				// all uppercase letters convert to null
				final var isUpperCase = item.toUpperCase()
					.equals(item);

				if (isUpperCase) {
					return null;
				}

				return item;
			});

			assertEquals(expected, converted,
				format("%s.convert(<vowels to a, uppercase to null>)", items));
		}
	}

	@DisplayName(".bridge()")
	@Nested
	class BridgeTests {
		@DisplayName("\uD83C\uDF09")
		@Tests({
			"when set is not empty, returns items in java.util.Set;" +
				"[i, k, F, Q, a, f];" +
				"[i, k, F, Q, a, f]",
			"when set is empty, returns empty java.util.Set;" +
				"[];" +
				"[]"
		})
		void test(@StringSet Set<String> items, @StringArray String[] expected) {
			final var expected2 = java.util.Set.of(expected);
			final var set = items.bridge();
			assertEquals(expected2, set,
				format("%s.bridge()", items));
		}
	}

	@DisplayName(".iterator()")
	@Nested
	class IteratorTests {
		@DisplayName("\uD83C\uDFAA")
		@Tests({
			"when set is not empty, iterates items;" +
				"[S, 1, U, g, R, H];" +
				"[S, 1, U, g, R, H]",
			"when set is empty, does nothing;" +
				"[];" +
				"[]"
		})
		void test(@StringSet Set<String> items, @StringArray String[] expected) {
			final var iterated = new HashSet<String>();
			items.iterator()
				.forEachRemaining(iterated::add);

			assertEquals(java.util.Set.of(expected), iterated,
				format("%s.iterator()", items));
		}
	}

	@DisplayName(".equals(Object)")
	@Nested
	class EqualsTests {
		@DisplayName("when set is not empty")
		@Tests({
			"when items equal argument items, returns true;" +
				"[F, i, 0, s, C, v]; [F, i, 0, s, C, v];" +
				"true",
			"when some items differ from argument items, returns false;" +
				"[k, j, 5, x, W, v]; [k, j, a, x, b, v];" +
				"false",
			"when items equal argument items but in different order, returns true;" +
				"[K, j, v, c, l, t]; [j, t, l, K, c, v];" +
				"true",
			"when no items equal argument items, returns false;" +
				"[r, T, i, M, w, 1]; [s, N, c, L, C, F];" +
				"false",
			"when set is smaller than argument set, returns false;" +
				"[T, y, B, G]; [T, y, B, G, v, w, c];" +
				"false",
			"when set is larger than argument set, returns false;" +
				"[G, l, P, t, b, O, W]; [P, O, l, W];" +
				"false",
			"when argument set is empty, returns false;" +
				"[E, R, O, p, 1]; [];" +
				"false",
			"when argument set is null, returns false;" +
				"[j, x, Z, N]; null;" +
				"false"
		})
		void testSetNotEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
			boolean expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when set is empty")
		@Tests({
			"when argument set is not empty, returns false;" +
				"[]; [P, j, e, q, T];" +
				"false",
			"when argument set is empty, returns true;" +
				"[]; [];" +
				"true",
			"when argument set is null, returns false;" +
				"[]; null;" +
				"false"
		})
		void testSetEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
			boolean expected) {
			test(items1, items2, expected);
		}

		private void test(Set<String> items1, Set<String> items2, boolean expected) {
			final var equals = items1.equals(items2);
			assertEquals(expected, equals,
				format("%s.equals(%s)", items1, items2));
		}
	}

	@DisplayName(".hashCode()")
	@Nested
	class HashCodeTests {
		@DisplayName("\uD83C\uDF6F")
		@Tests({
			"when sets are equal, returns equal hash codes;" +
				"[b, q, w, T, e, h]; [b, q, w, T, e, h];" +
				"true",
			"when sets differ, returns different hash codes;" +
				"[H, N, n, K, B]; [H, N, n, K, B, q, v];" +
				"false",
			"when one set is empty, returns different hash codes;" +
				"[]; [x, R, o, d, k];" +
				"false",
			"when sets are empty, returns equal hash codes;" +
				"[]; [];" +
				"true"
		})
		void test(@StringSet Set<String> items1, @StringSet Set<String> items2,
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
		@DisplayName("\uD83C\uDF6D")
		@Tests({
			"when set is not empty, returns string representation;" +
				"[N, g, m, R, e, q];" +
				"{N, g, m, R, e, q}",
			"when set is empty, returns {};" +
				"[];" +
				"{}"
		})
		void test(@StringSet Set<String> items, String expected) {
			final var string = items.toString();
			assertEquals(expected, string,
				format("%s.toString()", items));
		}
	}
}
