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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class SetTests {
	@DisplayName("Set(Collection<T>)")
	@Nested
	class NewWithCollectionTests {
		@DisplayName("\uD83D\uDC96")
		@Tests({
			"when argument collection is not empty, creates set with items;" +
				"[g, R, 2, q, P, s];" +
				"[g, R, 2, q, P, s]",
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
		@DisplayName("\uD83D\uDC8C")
		@Tests({
			"when no argument items are null, creates set with all items;" +
				"[g, R, l, q, P, s, n];" +
				"[g, R, l, q, P, s, n]",
			"when some argument items are null, creates set with non-null items;" +
				"[g, null, l, null, P, null, null];" +
				"[g, l, P]",
			"when all argument items are null, creates empty set;" +
				"[null, null, null, null];" +
				"[]",
			"when argument array is empty, creates empty set;" +
				"[];" +
				"[]"
		})
		void test(@StringArray String[] items, @StringSet Set<String> expected) {
			final var set = new Set<>(items);
			assertEquals(expected, set,
				format("new Set<>(%s)",
					Arrays.toString(items)));
		}
	}

	@DisplayName("Set(Iterable<T>)")
	@Nested
	class NewWithIterable {
		@DisplayName("\uD83C\uDFCD")
		@Tests({
			"when no argument items are null, creates set with all items;" +
				"[t, y, O, f, A, s];" +
				"[t, y, O, f, A, s]",
			"when some argument items are null, creates set with non-null items;" +
				"[l, null, null, P, null, d, null];" +
				"[l, P, d]",
			"when all argument items are null, creates empty set;" +
				"[null, null];" +
				"[]",
			"when argument iterable is empty, creates empty set;" +
				"[];" +
				"[]"
		})
		void test(@StringArray String[] items, @StringSet Set<String> expected) {
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
		void testNotEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
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
		void testEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
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
		void testNotEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
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
		void testEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
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
		void testNotEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
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
		void testEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
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
		void testNotEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
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
		void testEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
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
		void testNotEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
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
		void testEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
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
		void testNotEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
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
		void testEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
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
		void testNotEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
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
		void testEmpty(@StringSet Set<String> items1, @StringSet Set<String> items2,
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
		@DisplayName("\uD83D\uDCAA")
		@Tests({
			"when no items convert to null, returns converted items;" +
				"[f, R, E, a, A, b, F, L];" +
				"[f, r, e, a, b, l]",
			"when some items convert to null, returns converted non-null items;" +
				"[f, R, a, B, 2, e, T, 4, 0];" +
				"[f, r, a, b, e, t]",
			"when all items convert to null, returns empty set;" +
				"[4, 5, 1, 0];" +
				"[]",
			"when set is empty, returns empty set;" +
				"[];" +
				"[]"
		})
		void test(@StringSet Set<String> items, @StringSet Set<String> expected) {
			final var converted = items.convert((item) -> {
				return item.matches("\\d+")
					? null
					: item.toLowerCase();
			});

			assertEquals(expected, converted,
				format("%s.convert(<to lowercase>)", items));
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

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(StringSet.Converter.class)
@interface StringSet {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, Set> {
		protected Converter() {
			super(String.class, Set.class);
		}

		@Override
		protected Set<String> convert(String s) throws ArgumentConversionException {
			final var items = new StringArray.Converter()
				.convert(s);

			return new Set<>(items);
		}
	}
}

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(StringPairSet.Converter.class)
@interface StringPairSet {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, Set> {
		protected Converter() {
			super(String.class, Set.class);
		}

		@Override
		protected Set<Pair<String, String>> convert(String s) throws ArgumentConversionException {
			return new StringSet.Converter()
				.convert(s)
				.convert((item) -> {
					final var items = item.split(":");
					return new Pair<>(items[0], items[1]);
				});
		}
	}
}
