package com.tsyba.core.collections;

import com.tsyba.core.collections.converter.StringArray;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

public class MutableSetTests {
	@DisplayName(".getDistinct()")
	@Nested
	class GetDistinctTests {
		@DisplayName("")
		@Tests({
			"returns a copy of itself;" +
				"[g, t, w, s, A, q]"
		})
		void test(@StringMutableSet MutableSet<String> items) {
			final var distinct = items.getDistinct();
			assertEquals(items, distinct,
				format("%s.getDistinct()", items));
			assertNotSame(items, distinct,
				format("%s.getDistinct()", items));
		}
	}

	@DisplayName(".unite(Set<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"returns a mutable set;" +
			"[f, r, q, e, a]; [t, w, e, s]"
	}, delimiter = ';')
	void testUnite(String name, @StringMutableSet MutableSet<String> items1,
		@StringMutableSet MutableSet<String> items2) {

		final var returned = items1.unite(items2);
		final var klass = returned.getClass();
		assertEquals(MutableSet.class, klass);
	}

	@DisplayName(".intersect(Set<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"returns a mutable set;" +
			"[f, r, q, e, a]; [t, w, e, s]"
	}, delimiter = ';')
	void testIntersect(String name, @StringMutableSet MutableSet<String> items1,
		@StringMutableSet MutableSet<String> items2) {

		final var returned = items1.intersect(items2);
		final var klass = returned.getClass();
		assertEquals(MutableSet.class, klass);
	}

	@DisplayName(".subtract(Set<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"returns a mutable set;" +
			"[f, r, q, e, a]; [t, w, e, s]"
	}, delimiter = ';')
	void testSubtract(String name, @StringMutableSet MutableSet<String> items1,
		@StringMutableSet MutableSet<String> items2) {

		final var returned = items1.subtract(items2);
		final var klass = returned.getClass();
		assertEquals(MutableSet.class, klass);
	}

	@DisplayName(".symmetricSubtract(Set<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"returns a mutable set;" +
			"[f, r, q, e, a]; [t, w, e, s]"
	}, delimiter = ';')
	void testSymmetricSubtract(String name, @StringMutableSet MutableSet<String> items1,
		@StringMutableSet MutableSet<String> items2) {

		final var returned = items1.symmetricSubtract(items2);
		final var klass = returned.getClass();
		assertEquals(MutableSet.class, klass);
	}

	@DisplayName(".multiply(Set<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"returns a mutable set;" +
			"[f, r, q, e, a]; [t, w, e, s]"
	}, delimiter = ';')
	void testMultiply(String name, @StringMutableSet MutableSet<String> items1,
		@StringMutableSet MutableSet<String> items2) {

		final var returned = items1.multiply(items2);
		final var klass = returned.getClass();
		assertEquals(MutableSet.class, klass);
	}

	@DisplayName(".add(T)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when item is absent, adds item;" +
			"[g, e, Q, s, A, m]; P;" +
			"[g, e, Q, s, A, m, P]",
		"when item is present, does not add item;" +
			"[g, e, Q, s, A, m]; Q;" +
			"[g, e, Q, s, A, m]",
		"when item is null, does nothing;" +
			"[b, M, L, s, P]; null;" +
			"[b, M, L, s, P]",
		"when set is empty and item is not null, adds item;" +
			"[]; V;" +
			"[V]",
		"when set is empty and item is null, does not add item;" +
			"[]; null;" +
			"[]"
	}, delimiter = ';', nullValues = "null")
	void testAdd(String name, @StringMutableSet MutableSet<String> items,
		String item, @StringSet Set<String> expected) {

		final var returned = items.add(item);
		assertSame(items, returned);
		assertEquals(expected, items);
	}

	@DisplayName(".add(T...)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when all items are absent, adds items;" +
			"[b, L, s, W]; [e, Q, o];" +
			"[b, L, s, W, e, Q, o]",
		"when some items are present, adds absent items;" +
			"[b, L, s, W]; [b, E, s, W, l];" +
			"[b, L, s, W, E, l]",
		"when all items are present, does not add items;" +
			"[b, L, s, W]; [b, E, s, W, l];" +
			"[b, L, s, W, E, l]",
		"when some items are null, adds non-null items;" +
			"[b, L, s, W]; [null, R, null, null, K];" +
			"[b, L, s, W, R, K]",
		"when all items are null, does nothing;" +
			"[b, L, s, W]; [null, null];" +
			"[b, L, s, W]",
		"when items are empty, does nothing;" +
			"[b, L, s, W]; [];" +
			"[b, L, s, W]",
		"when set is empty and all items are not null, adds items;" +
			"[]; [g, O, p, R];" +
			"[g, O, p, R]",
		"when set is empty and some items are null, adds non-null items;" +
			"[]; [b, null, P, k, null];" +
			"[b, P, k]",
		"when set is empty and all items are null, does nothing;" +
			"[]; [null, null, null, null];" +
			"[]",
		"when set and items are empty, does nothing;" +
			"[]; [];" +
			"[]"
	}, delimiter = ';', nullValues = "null")
	void testAddVarargs(String name, @StringMutableSet MutableSet<String> items1,
		@StringArray String[] items2, @StringSet Set<String> expected) {

		final var returned = items1.add(items2);
		assertSame(items1, returned);
		assertEquals(expected, items1);
	}

	@DisplayName(".add(Collection<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when all items are absent, adds items;" +
			"[j, I, w, 9, 7]; [l, k, O];" +
			"[j, I, w, 9, 7, l, k, O]",
		"when some items are present, adds absent items;" +
			"[j, I, w, 9, 7]; [l, 9, I];" +
			"[j, I, w, 9, 7, l]",
		"when all items are present, does not add items;" +
			"[j, I, w, 9, 7]; [j, I, w, 9, 7];" +
			"[j, I, w, 9, 7]",
		"when items are empty, does nothing;" +
			"[k, m, H, u, 7, h]; [];" +
			"[k, m, H, u, 7, h]",
		"when set is empty, adds items;" +
			"[]; [j, I, w, 9, 7];" +
			"[j, I, w, 9, 7]",
		"when set and items are empty, does nothing;" +
			"[]; [];" +
			"[]"
	}, delimiter = ';')
	void testAddCollection(String name, @StringMutableSet MutableSet<String> items1,
		@StringCollection Collection<String> items2, @StringSet Set<String> expected) {

		final var returned = items1.add(items2);
		assertSame(items1, returned);
		assertEquals(expected, items1);
	}

	@DisplayName(".add(Iterable<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when all items are absent, adds items;" +
			"[b, L, s, W]; [e, Q, o];" +
			"[b, L, s, W, e, Q, o]",
		"when some items are present, adds absent items;" +
			"[b, L, s, W]; [b, E, s, W, l];" +
			"[b, L, s, W, E, l]",
		"when all items are present, does not add items;" +
			"[b, L, s, W]; [b, E, s, W, l];" +
			"[b, L, s, W, E, l]",
		"when some items are null, adds non-null items;" +
			"[b, L, s, W]; [null, R, null, null, K];" +
			"[b, L, s, W, R, K]",
		"when all items are null, does nothing;" +
			"[b, L, s, W]; [null, null];" +
			"[b, L, s, W]",
		"when items are empty, does nothing;" +
			"[b, L, s, W]; [];" +
			"[b, L, s, W]",
		"when set is empty and all items are not null, adds items;" +
			"[]; [g, O, p, R];" +
			"[g, O, p, R]",
		"when set is empty and some items are null, adds non-null items;" +
			"[]; [b, null, P, k, null];" +
			"[b, P, k]",
		"when set is empty and all items are null, does nothing;" +
			"[]; [null, null, null, null];" +
			"[]",
		"when set and items are empty, does nothing;" +
			"[]; [];" +
			"[]"
	}, delimiter = ';', nullValues = "null")
	void testAddIterable(String name, @StringMutableSet MutableSet<String> items1,
		@StringArray String[] items2, @StringSet Set<String> expected) {

		final var items3 = Arrays.asList(items2);
		final var returned = items1.add(items3);

		assertSame(items1, returned);
		assertEquals(expected, items1);
	}

	@DisplayName(".remove(T)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when item is present, removes item;" +
			"[g, O, e, T, d, f, l]; T;" +
			"[g, O, e, d, f, l]",
		"when item is present and is the only item, removes item;" +
			"[g]; g;" +
			"[]",
		"when item is absent, does not remove item;" +
			"[g, O, e, T, d, f, l]; t;" +
			"[g, O, e, T, d, f, l]",
		"when item is null, does nothing;" +
			"[g, O, e, T, d, f, l]; null;" +
			"[g, O, e, T, d, f, l]",
		"when set is empty and item is not null, does nothing;" +
			"[]; T;" +
			"[]",
		"when set is empty and item is null, does nothing;" +
			"[]; null;" +
			"[]"
	}, delimiter = ';', nullValues = "null")
	void testRemove(String name, @StringMutableSet MutableSet<String> items,
		String item, @StringSet Set<String> expected) {

		final var returned = items.remove(item);
		assertSame(items, returned);
		assertEquals(expected, items);
	}

	@DisplayName(".remove(T...)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when all items are present, removes items;" +
			"[n, M, m, L, I, o, p]; [M, L, I];" +
			"[n, m, o, p]",
		"when all items are present and are the only items, removes all items;" +
			"[n, M, m, L, I, o, p]; [n, M, m, L, I, o, p];" +
			"[]",
		"when some items are present, removes present items;" +
			"[n, M, m, L, I, o, p]; [U, L, N, p];" +
			"[n, M, m, I, o]",
		"when all items are absent, does nothing;" +
			"[n, M, m, L, I, o, p]; [U, P, O, N];" +
			"[n, M, m, L, I, o, p]",
		"when some items are null, removes non-null items;" +
			"[n, M, m, L, I, o, p]; [n, null, M, null, null, p];" +
			"[m, L, I, o]",
		"when all items are null, does nothing;" +
			"[n, M, m, L, I, o, p]; [null, null];" +
			"[n, M, m, L, I, o, p]",
		"when items are empty, does nothing;" +
			"[n, M, m, L, I, o, p]; [];" +
			"[n, M, m, L, I, o, p]",
		"when set is empty and all items are not null, does nothing;" +
			"[]; [M, I, O, P];" +
			"[]",
		"when set is empty and some items are null, does nothing;" +
			"[]; [null, null, P, null];" +
			"[]",
		"when set is empty and all items are null, does nothing;" +
			"[]; [null, null];" +
			"[]",
		"when set and items are empty, does nothing;" +
			"[]; [];" +
			"[]"
	}, delimiter = ';', nullValues = "null")
	void testRemoveVarargs(String name, @StringMutableSet MutableSet<String> items1,
		@StringArray String[] items2, @StringSet Set<String> expected) {

		final var returned = items1.remove(items2);
		assertSame(items1, returned);
		assertEquals(expected, items1);
	}

	@DisplayName(".remove(Collection<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when all items are present, removes items;" +
			"[n, M, m, L, I, o, p]; [M, L, I];" +
			"[n, m, o, p]",
		"when all items are present and are the only items, removes all items;" +
			"[n, M, m, L, I, o, p]; [n, M, m, L, I, o, p];" +
			"[]",
		"when some items are present, removes present items;" +
			"[n, M, m, L, I, o, p]; [U, L, N, p];" +
			"[n, M, m, I, o]",
		"when all items are absent, does nothing;" +
			"[n, M, m, L, I, o, p]; [U, P, O, N];" +
			"[n, M, m, L, I, o, p]",
		"when items are empty, does nothing;" +
			"[n, M, m, L, I, o, p]; [];" +
			"[n, M, m, L, I, o, p]",
		"when set is empty, does nothing;" +
			"[]; [M, I, O, P];" +
			"[]",
		"when set and items are empty, does nothing;" +
			"[]; [];" +
			"[]"
	}, delimiter = ';')
	void testRemoveCollection(String name, @StringMutableSet MutableSet<String> items1,
		@StringCollection Collection<String> items2, @StringSet Set<String> expected) {

		final var returned = items1.remove(items2);
		assertSame(items1, returned);
		assertEquals(expected, items1);
	}

	@DisplayName(".remove(Iterable<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when all items are present, removes items;" +
			"[n, M, m, L, I, o, p]; [M, L, I];" +
			"[n, m, o, p]",
		"when all items are present and are the only items, removes all items;" +
			"[n, M, m, L, I, o, p]; [n, M, m, L, I, o, p];" +
			"[]",
		"when some items are present, removes present items;" +
			"[n, M, m, L, I, o, p]; [U, L, N, p];" +
			"[n, M, m, I, o]",
		"when all items are absent, does nothing;" +
			"[n, M, m, L, I, o, p]; [U, P, O, N];" +
			"[n, M, m, L, I, o, p]",
		"when some items are null, removes non-null items;" +
			"[n, M, m, L, I, o, p]; [n, null, M, null, null, p];" +
			"[m, L, I, o]",
		"when all items are null, does nothing;" +
			"[n, M, m, L, I, o, p]; [null, null];" +
			"[n, M, m, L, I, o, p]",
		"when items are empty, does nothing;" +
			"[n, M, m, L, I, o, p]; [];" +
			"[n, M, m, L, I, o, p]",
		"when set is empty and all items are not null, does nothing;" +
			"[]; [M, I, O, P];" +
			"[]",
		"when set is empty and some items are null, does nothing;" +
			"[]; [null, null, P, null];" +
			"[]",
		"when set is empty and all items are null, does nothing;" +
			"[]; [null, null];" +
			"[]",
		"when set and items are empty, does nothing;" +
			"[]; [];" +
			"[]"
	}, delimiter = ';', nullValues = "null")
	void testRemoveIterable(String name, @StringMutableSet MutableSet<String> items1,
		@StringArray String[] items2, @StringSet Set<String> expected) {

		final var items3 = Arrays.asList(items2);
		final var returned = items1.remove(items3);

		assertSame(items1, returned);
		assertEquals(expected, items1);
	}

	@DisplayName(".removeAll()")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when set is not empty, removes all items;" +
			"[t, K, L, s, B]",
		"when set is empty, does nothing;" +
			"[]"
	}, delimiter = ';')
	void testRemoveAll(String name, @StringMutableSet MutableSet<String> items) {
		final var returned = items.removeAll();
		final var expected = new Set<String>();

		assertSame(items, returned);
		assertEquals(expected, items);
	}

	@DisplayName(".matchAll(Predicate<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"returns a mutable set;" +
			"[f, r, q, e, a]"
	}, delimiter = ';')
	void testFilter(String name, @StringMutableSet MutableSet<String> items) {
		final var returned = items.matchAll(String::isBlank);
		final var klass = returned.getClass();
		assertEquals(MutableSet.class, klass);
	}

	@DisplayName(".iterate(Consumer<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"returns itself;" +
			"[f, r, q, e, a]"
	}, delimiter = ';')
	void testIterate(String name, @StringMutableSet MutableSet<String> items) {
		@SuppressWarnings("ResultOfMethodCallIgnored")
		final var returned = items.iterate(String::toLowerCase);
		assertSame(items, returned);
	}

	@DisplayName(".convert(Function<T, R>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"returns a mutable set;" +
			"[f, r, q, e, a]"
	}, delimiter = ';')
	void testConvert(String name, @StringMutableSet MutableSet<String> items) {
		final var returned = items.convert(String::toLowerCase);
		final var klass = returned.getClass();
		assertEquals(MutableSet.class, klass);
	}

	@DisplayName(".toImmutable()")
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = {
		"when set is not empty, returns items in co.tsyba.core.Set;" +
			"[g, R, e, A, s]",
		"when set is empty, returns empty co.tsyba.core.Set;" +
			"[]"
	}, delimiter = ';')
	void testToImmutable(String name, @StringMutableSet MutableSet<String> items) {
		final var immutable = items.toImmutable();
		final var klass = immutable.getClass();

		assertEquals(Set.class, klass);
		assertEquals(items, immutable);
	}
}

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(StringMutableSet.Converter.class)
@interface StringMutableSet {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, MutableSet> {
		protected Converter() {
			super(String.class, MutableSet.class);
		}

		@Override
		protected MutableSet<String> convert(String s) throws ArgumentConversionException {
			final var items = new StringArray.Converter()
				.convert(s);

			return new MutableSet<>(items);
		}
	}
}