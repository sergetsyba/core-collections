package com.tsyba.core.collections;

import com.tsyba.core.collections.converter.StringArray;
import com.tsyba.core.collections.converter.StringList;
import com.tsyba.core.collections.converter.StringPairSet;
import com.tsyba.core.collections.converter.StringSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.Arrays;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class SetTests {
	@DisplayName("Set(Collection<T>)")
	@Nested
	class NewWithCollectionTests {
		@DisplayName("\uD83D\uDC96")
		@Tests({
			"when all argument items are distinct, creates set with all argument items;" +
				"[g, R, 2, q, P, s];" +
				"[g, R, 2, q, P, s]",
			"when some argument items are same, creates set with distinct argument items;" +
				"[g, R, R, f, S, g, R];" +
				"[g, R, f, S]",
			"when all argument items are same, creates set with single distinct argument item;" +
				"[b, b, b, b, b];" +
				"[b]",
			"when argument collection is empty, creates empty set;" +
				"[];" +
				"[]"
		})
		void test(@StringList List<String> items, @StringSet Set<String> expected) {
			final var set = new Set<>(items);
			assertEquals(expected, set,
				format("new Set<>(%s)", items));
		}
	}

	@DisplayName("Set(T...)")
	@Nested
	class NewWithVarargs {
		@DisplayName("when no argument items are null")
		@Tests({
			"when all argument items are distinct, creates set with all argument items;" +
				"[g, R, l, q, P, s, n];" +
				"[g, R, l, q, P, s, n]",
			"when some argument items are same, creates set with distinct argument items;" +
				"[g, R, s, g, s, R, R];" +
				"[g, R, s]",
			"when all argument items are same, creates set with single distinct argument item;" +
				"[g, g, g];" +
				"[g]"
		})
		void testNoItemsNull(@StringArray String[] items,
			@StringSet Set<String> expected) {
			test(items, expected);
		}

		@DisplayName("when some argument items are null")
		@Tests({
			"when all argument items are distinct, creates set with all non-null argument items;" +
				"[g, null, l, null, P, null, null];" +
				"[g, l, P]",
			"when some argument items are same, creates set with non-null distinct argument items;" +
				"[n, null, null, m, null, m, O, null, O];" +
				"[n, m, O]",
			"when all argument items are same, creates set with single non-null distinct argument item;" +
				"[t, null, t, null, null, t];" +
				"[t]",
		})
		void testSomeItemsNull(@StringArray String[] items,
			@StringSet Set<String> expected) {
			test(items, expected);
		}

		@DisplayName("when all argument items are null")
		@Tests({
			"creates empty set;" +
				"[null, null, null, null];" +
				"[]",
		})
		void testAllItemsNull(@StringArray String[] items,
			@StringSet Set<String> expected) {
			test(items, expected);
		}

		@DisplayName("when argument array is empty")
		@Tests({
			"creates empty set;" +
				"[];" +
				"[]",
		})
		void testArgumentArrayEmpty(@StringArray String[] items,
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
	class NewWithIterable {
		@DisplayName("when no argument items are null")
		@Tests({
			"when all argument items are distinct, creates set with all argument items;" +
				"[b, R, s, k];" +
				"[b, R, s, k]",
			"when some argument items are same, creates set with distinct argument items;" +
				"[p, k, H, f, f, k, p, k];" +
				"[p, k, H, f]",
			"when all argument items are same, creates set with single distinct argument item;" +
				"[l, l, l, l, l];" +
				"[l]"
		})
		void testNoItemsNull(@StringArray String[] items,
			@StringSet Set<String> expected) {
			test(items, expected);
		}

		@DisplayName("when some argument items are null")
		@Tests({
			"when all argument items are distinct, creates set with all non-null argument items;" +
				"[k, null, null, f, e, null, h, j];" +
				"[k, f, e, h, j]",
			"when some argument items are same, creates set with non-null distinct argument items;" +
				"[null, null, R, R, j, R, null, j, O];" +
				"[R, j, O]",
			"when all argument items are same, creates set with single non-null distinct argument item;" +
				"[v, null, v, null, null];" +
				"[v]",
		})
		void testSomeItemsNull(@StringArray String[] items,
			@StringSet Set<String> expected) {
			test(items, expected);
		}

		@DisplayName("when all argument items are null")
		@Tests({
			"creates empty set;" +
				"[null, null];" +
				"[]",
		})
		void testAllItemsNull(@StringArray String[] items,
			@StringSet Set<String> expected) {
			test(items, expected);
		}

		@DisplayName("when argument iterable is empty")
		@Tests({
			"creates empty set;" +
				"[];" +
				"[]",
		})
		void testArgumentIterableEmpty(@StringArray String[] items,
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
		@DisplayName("\uD83C\uDFA9")
		@Tests({
			"when set is not empty, returns item count;" +
				"[g, b, L, f, s, O, k];" +
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
		@DisplayName("\uD83D\uDCD7")
		@Tests({
			"when set is not empty, returns itself;" +
				"[g, t, w, s, A, q]",
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
				"[t, E, q, v, b, 0]; [e, g, 7, T];" +
				"true",
			"when set and argument set intersect, returns false;" +
				"[t, E, q, v, b, 0]; [P, z, E, q];" +
				"false",
			"when set and argument set are equal, returns false;" +
				"[t, E, q, v, b, 0]; [t, E, q, v, b, 0];" +
				"false",
			"when argument set is empty, returns true;" +
				"[t, E, q, v, b, 0]; [];" +
				"true"
		})
		void testSetNotEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
			boolean expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when set is empty")
		@Tests({
			"when argument set is not empty, returns true;" +
				"[]; [P, z, E, q];" +
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

	@DisplayName(".unite(Set<T>)")
	@Nested
	class UniteTests {
		@DisplayName("when set is not empty")
		@Tests({
			"when set and argument set are disjoint, returns union set;" +
				"[t, E, q, v, b, 0]; [e, g, 7, T];" +
				"[t, E, q, v, b, 0, e, g, 7, T]",
			"when set and argument set intersect, returns union set;" +
				"[t, E, q, v, b, 0]; [P, z, E, q];" +
				"[t, E, q, v, b, 0, P, z]",
			"when set and argument set are equal, returns union set;" +
				"[t, E, q, v, b, 0]; [t, E, q, v, b, 0];" +
				"[t, E, q, v, b, 0]",
			"when argument set is empty, returns union set;" +
				"[t, E, q, v, b, 0]; [];" +
				"[t, E, q, v, b, 0]"
		})
		void testSetNotEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
			@StringSet Set<String> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when set is empty")
		@Tests({
			"when argument set is not empty, returns union set;" +
				"[]; [P, z, E, q];" +
				"[P, z, E, q]",
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

	@DisplayName(".intersects(Set<T>)")
	@Nested
	class IntersectsTests {
		@DisplayName("when set is not empty")
		@Tests({
			"when set and argument set are disjoint, returns false;" +
				"[t, E, q, v, b, 0]; [e, g, 7, T];" +
				"false",
			"when set and argument set intersect, returns true;" +
				"[t, E, q, v, b, 0]; [P, z, E, q];" +
				"true",
			"when set and argument set are equal, returns true;" +
				"[t, E, q, v, b, 0]; [t, E, q, v, b, 0];" +
				"true",
			"when argument set is empty, returns false;" +
				"[t, E, q, v, b, 0]; [];" +
				"false"
		})
		void testSetNotEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
			boolean expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when set is empty")
		@Tests({
			"when argument set is not empty, returns false;" +
				"[]; [P, z, E, q];" +
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

	@DisplayName(".intersect(Set<T>)")
	@Nested
	class IntersectTests {
		@DisplayName("when set is not empty")
		@Tests({
			"when set and argument set are disjoint, returns empty set;" +
				"[t, E, q, v, b, 0]; [e, g, 7, T];" +
				"[]",
			"when set and argument set intersect, returns intersection set;" +
				"[t, E, q, v, b, 0]; [P, z, E, q];" +
				"[E, q]",
			"when set and argument set are equal, returns intersection set;" +
				"[t, E, q, v, b, 0]; [t, E, q, v, b, 0];" +
				"[t, E, q, v, b, 0]",
			"when argument set is empty, returns empty set;" +
				"[t, E, q, v, b, 0]; [];" +
				"[]"
		})
		void testSetNotEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
			@StringSet Set<String> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when set is empty")
		@Tests({
			"when argument set is not empty, returns empty set;" +
				"[]; [P, z, E, q];" +
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
				"[t, E, q, v, b, 0]; [e, g, 7, T];" +
				"[t, E, q, v, b, 0]",
			"when set and argument set intersect, returns difference set;" +
				"[t, E, q, v, b, 0]; [P, z, E, q];" +
				"[t, v, b, 0]",
			"when set and argument set are equal, returns empty set;" +
				"[t, E, q, v, b, 0]; [t, E, q, v, b, 0];" +
				"[]",
			"when argument set is empty, returns difference set;" +
				"[t, E, q, v, b, 0]; [];" +
				"[t, E, q, v, b, 0]"
		})
		void testSetNotEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
			@StringSet Set<String> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when set is empty")
		@Tests({
			"when argument set is not empty, returns empty set;" +
				"[]; [P, z, E, q];" +
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
				"[t, E, q, v, b, 0]; [e, g, 7, T];" +
				"[t, E, q, v, b, 0, e, g, 7, T]",
			"when set and argument set intersect, returns symmetric difference set;" +
				"[t, E, q, v, b, 0]; [P, z, E, q];" +
				"[t, v, b, 0, P, z]",
			"when set and argument set are equal, returns empty set;" +
				"[t, E, q, v, b, 0]; [t, E, q, v, b, 0];" +
				"[]",
			"when argument set is empty, returns symmetric difference set;" +
				"[t, E, q, v, b, 0]; [];" +
				"[t, E, q, v, b, 0]"
		})
		void testSetNotEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
			@StringSet Set<String> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when set is empty")
		@Tests({
			"when argument set is not empty, returns symmetric difference set;" +
				"[]; [P, z, E, q];" +
				"[P, z, E, q]",
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
				"[t, E, q]; [e, g];" +
				"[t:e, t:g, E:e, E:g, q:e, q:g]",
			"when set and argument set intersect, returns product set;" +
				"[t, E, q]; [q, E];" +
				"[t:q, t:E, E:q, E:E, q:q, q:E]",
			"when set and argument set are equal, returns product set;" +
				"[t, E, q]; [t, E, q];" +
				"[t:t, t:E, t:q, E:t, E:E, E:q, q:t, q:E, q:q]",
			"when argument set is empty, returns empty set;" +
				"[t, E, q, v, b, 0]; [];" +
				"[]"
		})
		void testSetNotEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
			@StringPairSet Set<Pair<String, String>> expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when set is empty")
		@Tests({
			"when argument set is not empty, returns empty set;" +
				"[]; [P, z, E, q];" +
				"[]",
			"when argument set is empty, returns empty set;" +
				"[]; [];" +
				"[]"
		})
		void testSetEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
			@StringPairSet Set<Pair<String, String>> expected) {
			test(items1, items2, expected);
		}

		private void test(Set<String> items1, Set<String> items2, Set<Pair<String, String>> expected) {
			final var product = items1.multiply(items2);
			assertEquals(expected, product,
				format("%s.multiply(%s)", items1, items2));
		}
	}

	@DisplayName(".matchAll(Predicate<T>)")
	@Nested
	class MatchAllTests {
		@DisplayName("\uD83C\uDF8E")
		@Tests({
			"when all items match, returns matched items;" +
				"[r, o, e, q, s, a, b, p];" +
				"[r, o, e, q, s, a, b, p]",
			"when some items match, returns matched items;" +
				"[r, O, e, Q, s, A, B, P];" +
				"[r, e, s]",
			"when no items match, returns empty set;" +
				"[R, W, D, L, P];" +
				"[]",
			"when set is empty, returns empty items;" +
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

	@DisplayName(".convert(Function<T, R>)")
	@Nested
	class ConvertTests {
		@DisplayName("when no items convert to null")
		@Tests({
			"when all converted items are distinct, returns all converted items;" +
				"[a, g, d, s, w];" +
				"[a, g, d, s, w]",
			"when some converted items are same, returns distinct converted items;" +
				"[a, u, s, f, i, b];" +
				"[a, s, f, b]",
			"when all converted items are same, returns single distinct converted item;" +
				"[e, i, o, u];" +
				"[a]",
			"when set is empty, returns empty set;" +
				"[];" +
				"[]"
		})
		void testNoItemsConvertNull(@StringSet Set<String> items,
			@StringSet Set<String> expected) {
			test(items, expected);
		}

		@DisplayName("when some items convert to null")
		@Tests({
			"when all converted items are distinct, returns all non-null converted items;" +
				"[E, g, a, B, O, s];" +
				"[g, a, s]",
			"when some converted items are same, returns non-null distinct converted items;" +
				"[F, D, a, u, g, V, o];" +
				"[a, g]",
			"when all converted items are same, returns single non-null distinct converted item;" +
				"[a, e, G, B, i, K, o];" +
				"[a]"
		})
		void testSomeItemsConvertNull(@StringSet Set<String> items,
			@StringSet Set<String> expected) {
			test(items, expected);
		}

		@DisplayName("when all items convert to null")
		@Tests({
			"returns empty set;" +
				"[R, W, D, O];" +
				"[]"
		})
		void testAllItemsConvertNull(@StringSet Set<String> items,
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
				format("%s.convert(Predicate<>)", items));
		}
	}

	@DisplayName(".bridge()")
	@Nested
	class BridgeTests {
		@DisplayName("\uD83D\uDC5C")
		@Tests({
			"when set is not empty, returns items in java.util.Set;" +
				"[t, E, a, Q, p, L];" +
				"[t, E, a, Q, p, L]",
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
