package co.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SetTests {
	@DisplayName("Set(Collection<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when items are not empty, creates set;" +
			"[g, R, 2, q, P, s];" +
			"[g, R, 2, q, P, s]",
		"when items are empty, creates empty set;" +
			"[];" +
			"[]"
	}, delimiter = ';')
	void testNewCollection(String name, @StringArray String[] items,
		@StringSet Set<String> expected) {

		final var set = new Set<>(
			new List<>(items));

		assertEquals(expected, set);
	}

	@DisplayName("Set(T...)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when all items are not null, creates set;" +
			"[g, R, l, q, P, s, n];" +
			"[g, R, l, q, P, s, n]",
		"when some items are null, creates set with non-null items;" +
			"[g, null, l, null, P, null, null];" +
			"[g, l, P]",
		"when all items are null, creates empty set;" +
			"[null, null, null, null];" +
			"[]",
		"when items are empty, creates empty set;" +
			"[];" +
			"[]"
	}, delimiter = ';')
	void testNewVarargs(String name, @StringArray String[] items,
		@StringSet Set<String> expected) {

		final var set = new Set<>(items);
		assertEquals(expected, set);
	}

	@DisplayName("Set(Iterable<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when all items are not null, creates set;" +
			"[t, y, O, f, A, s];" +
			"[t, y, O, f, A, s]",
		"when some items are null, creates set with non-null items;" +
			"[l, null, null, P, null, d, null];" +
			"[l, P, d]",
		"when all items are null, creates empty set;" +
			"[null, null];" +
			"[]",
		"when items are empty, creates empty set;" +
			"[];" +
			"[]"
	}, delimiter = ';')
	void testNewIterable(String name, @StringArray String[] items,
		@StringSet Set<String> expected) {

		final var set = new Set<>(
			Arrays.asList(items));

		assertEquals(expected, set);
	}

	@DisplayName(".getCount()")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when set is not empty, returns item count;" +
			"[g, b, L, f, s, O, k];" +
			"7",
		"when set is empty, returns 0;" +
			"[];" +
			"0"
	}, delimiter = ';')
	void testGetCount(String name, @StringSet Set<String> items, int expected) {
		final var count = items.getCount();
		assertEquals(expected, count);
	}

	@DisplayName(".contains(T)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when item is present, returns true;" +
			"[b, K, s, L, m, s, D]; m;" +
			"true",
		"when item is absent, returns false;" +
			"[b, K, s, L, m, s, D]; M;" +
			"false",
		"when item is null, returns false;" +
			"[b, K, s, L, m, s, D]; ;" +
			"false",
		"when set is empty, returns false;" +
			"[]; m;" +
			"false",
	}, delimiter = ';')
	void testContains(String name, @StringSet Set<String> items, String item,
		boolean expected) {

		final var contains = items.contains(item);
		assertEquals(expected, contains);
	}

	@DisplayName(".isDisjoint(Set<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when sets are disjoint, returns true;" +
			"[t, E, q, v, b, 0]; [e, g, 7, T];" +
			"true",
		"when sets intersect, returns false;" +
			"[t, E, q, v, b, 0]; [P, z, E, q];" +
			"false",
		"when sets are equal, returns false;" +
			"[t, E, q, v, b, 0]; [t, E, q, v, b, 0];" +
			"false",
		"when argument set is empty, returns true;" +
			"[t, E, q, v, b, 0]; [];" +
			"true",
		"when set is empty, returns true;" +
			"[]; [P, z, E, q];" +
			"true",
		"when both set and argument set are empty, returns true;" +
			"[]; [];" +
			"true"
	}, delimiter = ';')
	void testIsDisjoint(String name, @StringSet Set<String> items1,
		@StringSet Set<String> items2, boolean expected) {

		final var disjoint = items1.isDisjoint(items2);
		assertEquals(expected, disjoint);
	}

	@DisplayName(".unite(Set<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when sets are disjoint, returns union set;" +
			"[t, E, q, v, b, 0]; [e, g, 7, T];" +
			"[t, E, q, v, b, 0, e, g, 7, T]",
		"when sets intersect, returns union set;" +
			"[t, E, q, v, b, 0]; [P, z, E, q];" +
			"[t, E, q, v, b, 0, P, z]",
		"when sets are equal, returns union set;" +
			"[t, E, q, v, b, 0]; [t, E, q, v, b, 0];" +
			"[t, E, q, v, b, 0]",
		"when argument set is empty, returns union set;" +
			"[t, E, q, v, b, 0]; [];" +
			"[t, E, q, v, b, 0]",
		"when set is empty, returns union set;" +
			"[]; [P, z, E, q];" +
			"[P, z, E, q]",
		"when both set and argument set are empty, returns empty set;" +
			"[]; [];" +
			"[]"
	}, delimiter = ';')
	void testUnite(String name, @StringSet Set<String> items1,
		@StringSet Set<String> items2, @StringSet Set<String> expected) {

		final var union = items1.unite(items2);
		assertEquals(expected, union);
	}

	@DisplayName(".intersects(Set<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when sets are disjoint, returns false;" +
			"[t, E, q, v, b, 0]; [e, g, 7, T];" +
			"false",
		"when sets intersect, returns true;" +
			"[t, E, q, v, b, 0]; [P, z, E, q];" +
			"true",
		"when sets are equal, returns true;" +
			"[t, E, q, v, b, 0]; [t, E, q, v, b, 0];" +
			"true",
		"when argument set is empty, returns false;" +
			"[t, E, q, v, b, 0]; [];" +
			"false",
		"when set is empty, returns false;" +
			"[]; [P, z, E, q];" +
			"false",
		"when both set and argument set are empty, returns false;" +
			"[]; [];" +
			"false"
	}, delimiter = ';')
	void testIntersects(String name, @StringSet Set<String> items1,
		@StringSet Set<String> items2, boolean expected) {

		final var intersects = items1.intersects(items2);
		assertEquals(expected, intersects);
	}

	@DisplayName(".intersect(Set<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when sets are disjoint, returns empty set;" +
			"[t, E, q, v, b, 0]; [e, g, 7, T];" +
			"[]",
		"when sets intersect, returns intersection set;" +
			"[t, E, q, v, b, 0]; [P, z, E, q];" +
			"[E, q]",
		"when sets are equal, returns intersection set;" +
			"[t, E, q, v, b, 0]; [t, E, q, v, b, 0];" +
			"[t, E, q, v, b, 0]",
		"when argument set is empty, returns empty set;" +
			"[t, E, q, v, b, 0]; [];" +
			"[]",
		"when set is empty, returns empty set;" +
			"[]; [P, z, E, q];" +
			"[]",
		"when both set and argument set are empty, returns empty set;" +
			"[]; [];" +
			"[]"
	}, delimiter = ';')
	void testIntersect(String name, @StringSet Set<String> items1,
		@StringSet Set<String> items2, @StringSet Set<String> expected) {

		final var intersection = items1.intersect(items2);
		assertEquals(expected, intersection);
	}

	@DisplayName(".subtract(Set<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when sets are disjoint, returns difference set;" +
			"[t, E, q, v, b, 0]; [e, g, 7, T];" +
			"[t, E, q, v, b, 0]",
		"when sets intersect, returns difference set;" +
			"[t, E, q, v, b, 0]; [P, z, E, q];" +
			"[t, v, b, 0]",
		"when sets are equal, returns empty set;" +
			"[t, E, q, v, b, 0]; [t, E, q, v, b, 0];" +
			"[]",
		"when argument set is empty, returns difference set;" +
			"[t, E, q, v, b, 0]; [];" +
			"[t, E, q, v, b, 0]",
		"when set is empty, returns empty set;" +
			"[]; [P, z, E, q];" +
			"[]",
		"when both set and argument set are empty, returns empty set;" +
			"[]; [];" +
			"[]"
	}, delimiter = ';')
	void testSubtract(String name, @StringSet Set<String> items1,
		@StringSet Set<String> items2, @StringSet Set<String> expected) {

		final var difference = items1.subtract(items2);
		assertEquals(expected, difference);
	}

	@DisplayName(".symmetricSubtract(Set<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when sets are disjoint, returns symmetric difference set;" +
			"[t, E, q, v, b, 0]; [e, g, 7, T];" +
			"[t, E, q, v, b, 0, e, g, 7, T]",
		"when sets intersect, returns symmetric difference set;" +
			"[t, E, q, v, b, 0]; [P, z, E, q];" +
			"[t, v, b, 0, P, z]",
		"when sets are equal, returns empty set;" +
			"[t, E, q, v, b, 0]; [t, E, q, v, b, 0];" +
			"[]",
		"when argument set is empty, returns symmetric difference set;" +
			"[t, E, q, v, b, 0]; [];" +
			"[t, E, q, v, b, 0]",
		"when set is empty, returns symmetric difference set;" +
			"[]; [P, z, E, q];" +
			"[P, z, E, q]",
		"when both set and argument set are empty, returns empty set;" +
			"[]; [];" +
			"[]"
	}, delimiter = ';')
	void testSymmetricSubtract(String name, @StringSet Set<String> items1,
		@StringSet Set<String> items2, @StringSet Set<String> expected) {

		final var difference = items1.symmetricSubtract(items2);
		assertEquals(expected, difference);
	}

	@DisplayName(".multiply(Set<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when sets are disjoint, returns product set;" +
			"[t, E, q]; [e, g];" +
			"[t:e, t:g, E:e, E:g, q:e, q:g]",
		"when sets intersect, returns product set;" +
			"[t, E, q]; [q, E];" +
			"[t:q, t:E, E:q, E:E, q:q, q:E]",
		"when sets are equal, returns product set;" +
			"[t, E, q]; [t, E, q];" +
			"[t:t, t:E, t:q, E:t, E:E, E:q, q:t, q:E, q:q]",
		"when argument set is empty, returns empty set;" +
			"[t, E, q, v, b, 0]; [];" +
			"[]",
		"when set is empty, returns empty set;" +
			"[]; [P, z, E, q];" +
			"[]",
		"when both set and argument set are empty, returns empty set;" +
			"[]; [];" +
			"[]"
	}, delimiter = ';')
	void testMultiply(String name, @StringSet Set<String> items1,
		@StringSet Set<String> items2,
		@StringPairSet Set<Pair<String, String>> expected) {

		final var product = items1.multiply(items2);
		assertEquals(expected, product);
	}

	@DisplayName(".filter(Predicate<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when some items match, returns matched items;" +
			"[r, O, e, Q, s, A, B, P];" +
			"[r, e, s]",
		"when no items match, returns empty set;" +
			"[R, W, D, L, P];" +
			"[]",
		"when set is empty, returns empty items;" +
			"[];" +
			"[]"
	}, delimiter = ';')
	void testFilter(String name, @StringSet Set<String> items,
		@StringSet Set<String> expected) {

		final var filtered = items.filter((item) -> {
			return item.toLowerCase()
				.equals(item);
		});

		assertEquals(expected, filtered);
	}

	@DisplayName(".convert(Function<T, R>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when set is not empty, returns converted items;" +
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
	}, delimiter = ';')
	void testConvert(String name, @StringSet Set<String> items,
		@StringSet Set<String> expected) {

		final var converted = items.convert((item) -> {
			return item.matches("\\d+")
				? null
				: item.toLowerCase();
		});

		assertEquals(expected, converted);
	}

	@DisplayName(".bridge()")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when set is not empty, returns items in java.util.Set;" +
			"[t, E, a, Q, p, L];" +
			"[t, E, a, Q, p, L]",
		"when set is empty, returns empty java.util.Set;" +
			"[];" +
			"[]"
	}, delimiter = ';')
	void testBridge(String name, @StringSet Set<String> items,
		@StringArray String[] expected) {

		final var expected2 = java.util.Set.of(expected);
		final var set = items.bridge();
		assertEquals(expected2, set);
	}

	@DisplayName(".toString()")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when set is not empty, converts to string;" +
			"[N, g, m, R, e, q];" +
			"[N, g, m, R, e, q]",
		"when set is empty, converts to [];" +
			"[];" +
			"[]"
	}, delimiter = ';')
	void testToString(String name, @StringSet Set<String> items, String expected) {
		final var string = items.toString();
		assertEquals(expected, string);
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
