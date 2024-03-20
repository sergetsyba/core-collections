package co.tsyba.core.collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class CollectionTests {
	@DisplayName(".isEmpty()")
	@ParameterizedTest(name = "{0}")
	@CsvSource(delimiter = ';', value = {
		"when collection is not empty, returns false;" +
			"[f, T, q, r];" +
			"false",
		"when collection is empty, returns true;" +
			"[];" +
			"true"
	})
	void testIsEmpty(String name, @StringCollection Collection<String> items,
		boolean expected) {

		final var empty = items.isEmpty();
		assertEquals(expected, empty);
	}

	@DisplayName(".getCount()")
	@ParameterizedTest(name = "{0}")
	@CsvSource(delimiter = ';', value = {
		"when collection is not empty, returns item count;" +
			"[b, 5, F, e];" +
			"4",
		"when collection is empty, returns 0;" +
			"[];" +
			"0"
	})
	void testGetCount(String name, @StringCollection Collection<String> items,
		int expected) {

		final var count = items.getCount();
		assertEquals(expected, count);
	}

	@DisplayName(".getMinimum(Comparator<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(delimiter = ';', value = {
		"when collection is not empty, returns minimum;" +
			"[b, L, P, g, V, c];" +
			"L",
		"when collection is empty, returns empty optional;" +
			"[];" +
			" "
	})
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	void testGetMinimum(String name, @StringCollection Collection<String> items,
		@StringOptional Optional<String> expected) {

		final var min = items.getMinimum(Comparator.naturalOrder());
		assertEquals(expected, min);
	}

	@DisplayName(".getMaximum(Comparator<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(delimiter = ';', value = {
		"when collection is not empty, returns maximum;" +
			"[b, L, P, g, V, c];" +
			"g",
		"when collection is empty, returns empty optional;" +
			"[];" +
			" "
	})
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	void testGetMaximum(String name, @StringCollection Collection<String> items,
		@StringOptional Optional<String> expected) {

		final var min = items.getMaximum(Comparator.naturalOrder());
		assertEquals(expected, min);
	}

	@DisplayName(".contains(T)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(delimiter = ';', value = {
		"when item is present, returns true;" +
			"[t, d, 5, V, A]; 5;" +
			"true",
		"when item is absent, returns false;" +
			"[t, d, 5, V, A]; 7;" +
			"false",
		"when collection is empty, returns false;" +
			"[]; 5;" +
			"false",
	})
	void testContains(String name, @StringCollection Collection<String> items,
		String item, boolean expected) {

		final var contains = items.contains(item);
		assertEquals(expected, contains);
	}

	@DisplayName(".contains(Collection<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(delimiter = ';', value = {
		"when all items are present, returns true;" +
			"[t, d, 5, V, A]; [A, d, t];" +
			"true",
		"when some items are absent, returns false;" +
			"[O, P, q]; [P, 0, O];" +
			"false",
		"when all items are absent, returns false;" +
			"[Y, f, E, 3]; [N, R, P];" +
			"false",
		"when items are empty, returns true;" +
			"[Y, f, E, 3]; [];" +
			"true",
		"when collection is empty, returns false;" +
			"[]; [A, d, t];" +
			"false",
		"when collection and items are empty, returns true;" +
			"[]; [];" +
			"true"
	})
	void testContainsCollection(String name, @StringCollection Collection<String> items1,
		@StringCollection Collection<String> items2, boolean expected) {

		final var contains = items1.contains(items2);
		assertEquals(expected, contains);
	}

	@DisplayName(".match(Predicate<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(delimiter = ';', value = {
		"when some item matches, returns matched item;" +
			"[F, V, d, P, a, Q];" +
			"d",
		"when no item matches, returns empty optional;" +
			"[F, V, D, P, A, Q];" +
			" ",
		"when collection is empty, returns empty optional;" +
			"[];" +
			" "
	})
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	void testMatch(String name, @StringCollection Collection<String> items,
		@StringOptional Optional<String> expected) {

		final var matched = items.match((item) -> {
			return item.toLowerCase()
				.equals(item);
		});

		assertEquals(expected, matched);
	}

	@DisplayName(".noneMatches(Predicate<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(delimiter = ';', value = {
		"when no item matches, returns true;" +
			"[K, M, T, S, A];" +
			"true",
		"when some item matches, returns false;" +
			"[K, m, t, S, a];" +
			"false",
		"when each items matches, returns false;" +
			"[k, m, t, s, a];" +
			"false",
		"when collection is empty, returns true;" +
			"[];" +
			"true",
	})
	void testNoneMatches(String name, @StringCollection Collection<String> items,
		boolean expected) {

		final var noneMatches = items.noneMatches((item) -> {
			return item.toLowerCase()
				.equals(item);
		});

		assertEquals(expected, noneMatches);
	}

	@DisplayName(".anyMatches(Predicate<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(delimiter = ';', value = {
		"when no item matches, returns false;" +
			"[Y, P, D, A, S, M, S];" +
			"false",
		"when some item matches, returns true;" +
			"[Y, P, d, a, S, M, S];" +
			"true",
		"when each items matches, returns true;" +
			"[y, p, d, a, s, m, s];" +
			"true",
		"when collection is empty, returns false;" +
			"[];" +
			"false",
	})
	void testAnyMatches(String name, @StringCollection Collection<String> items,
		boolean expected) {

		final var anyMatches = items.anyMatches((item) -> {
			return item.toLowerCase()
				.equals(item);
		});

		assertEquals(expected, anyMatches);
	}

	@DisplayName(".eachMatches(Predicate<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(delimiter = ';', value = {
		"when no item matches, returns false;" +
			"[H, B, S, A];" +
			"false",
		"when some item matches, returns false;" +
			"[H, b, S, a];" +
			"false",
		"when each items match, returns true;" +
			"[h, b, s, a];" +
			"true",
		"when collection is empty, returns true;" +
			"[];" +
			"true",
	})
	void testEachMatches(String name, @StringCollection Collection<String> items,
		boolean expected) {

		final var anyMatches = items.eachMatches((item) -> {
			return item.toLowerCase()
				.equals(item);
		});

		assertEquals(expected, anyMatches);
	}

	@DisplayName(".sort(Comparator<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(delimiter = ';', value = {
		"when collection is not empty, returns sorted list;" +
			"[k, M, s, A, 8, d, q];" +
			"[s, q, k, d, M, A, 8]",
		"when collection is empty, returns empty list;" +
			"[];" +
			"[]"
	})
	void testSortComparator(String name, @StringCollection Collection<String> items,
		@StringList List<String> expected) {

		final var sorted = items.sort(Comparator.reverseOrder());
		Assertions.assertEquals(expected, sorted);
	}

	@DisplayName(".sort()")
	@ParameterizedTest(name = "{0}")
	@CsvSource(delimiter = ';', value = {
		"when collection is not empty, returns sorted list;" +
			"[k, M, s, A, 8, d, q];" +
			"[8, A, M, d, k, q, s]",
		"when collection is empty, returns empty list;" +
			"[];" +
			"[]"
	})
	void testSort(String name, @StringCollection Collection<String> items,
		@StringList List<String> expected) {

		final var sorted = items.sort();
		Assertions.assertEquals(expected, sorted);
	}

	@DisplayName(".<T not Comparable>sort()")
	@Test
	void testSortNotComparable() {
		Assertions.assertThrows(RuntimeException.class,
			() -> {
				final var collection = new AbstractPredicateCollection<>(
					String::isEmpty,
					String::isBlank
				) {
				};

				collection.sort();
			});
	}

	@DisplayName(".shuffle(Random)")
	@Nested
	class TestShuffleRandom {
		@Test
		@DisplayName("when collection is not empty, returns shuffled list")
		void testWhenNotEmpty() {
			Collection<String> items = new AbstractArrayCollection<>(
				"r", "E", "V", "s", "x", "w", "O", "8") {
			};

			final var time = System.currentTimeMillis();
			final var random = new Random(time);

			// shuffle items 10 times and ensure item order is different after
			// each shuffle
			for (var index = 0; index < 10; ++index) {
				final var shuffled = items.shuffle(random);
				assertShuffled(shuffled, items);
				items = shuffled;
			}
		}

		@DisplayName("when collection is empty, returns empty list")
		@Test
		void testWhenEmpty() {
			Collection<String> items = new AbstractArrayCollection<>() {
			};

			final var time = System.currentTimeMillis();
			final var random = new Random(time);

			final var shuffled = items.shuffle(random);
			final var items2 = new List<>(items);
			assertEquals(items2, shuffled);
		}
	}

	@DisplayName(".shuffle()")
	@Nested
	class TestShuffle {
		@Test
		@DisplayName("when collection is not empty, returns shuffled list")
		void testWhenNotEmpty() {
			Collection<String> items = new AbstractArrayCollection<>(
				"o", "M", "F", "0", "K", "z", "v", "S") {
			};

			for (var index = 0; index < 10; ++index) {
				final var shuffled = items.shuffle();
				assertShuffled(shuffled, items);

				items = shuffled;
			}
		}

		@DisplayName("when collection is empty, returns empty list")
		@Test
		void testWhenEmpty() {
			Collection<String> items = new AbstractArrayCollection<>() {
			};

			final var shuffled = items.shuffle();
			final var items2 = new List<>(items);
			assertEquals(items2, shuffled);
		}
	}

	@DisplayName(".iterate(Consumer<T>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(delimiter = ';', value = {
		"when collection is not empty, iterates items;" +
			"[f, F, e, q, p, L];" +
			"[f, F, e, q, p, L]",
		"when collection is empty, does nothing;" +
			"[];" +
			"[]"
	})
	void testIterate(String name, @StringCollection Collection<String> items,
		@StringArray String[] expected) {

		final var iterated = new ArrayList<String>();
		items.iterate(iterated::add);

		assertArrayEquals(expected,
			iterated.toArray(new String[]{
			}));
	}

	@DisplayName(".combine(R, BiFunction<R, T, R>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(delimiter = ';', value = {
		"when collection is not empty, combines items with initial value;" +
			"[g, E, Q, s, a, B]; B;" +
			"BgEQsaB",
		"when collection is empty, returns initial value;" +
			"[]; A;" +
			"A"
	})
	void testCombine(String name, @StringCollection Collection<String> items,
		String initial, String expected) {

		final var combined = items.combine(initial, (combined2, item) -> {
			return combined2 + item;
		});

		assertEquals(expected, combined);
	}

	@DisplayName(".join(String)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(delimiter = ';', value = {
		"when collection is not empty, joins items into a string;" +
			"[g, t, W, q, P, l]; -;" +
			"g-t-W-q-P-l",
		"when collection is empty, returns empty string;" +
			"[]; -;" +
			" "
	})
	void testJoin(String name, @StringCollection Collection<String> items,
		String separator, String expected) {

		if (expected == null) {
			expected = "";
		}

		final var joined = items.join(separator);
		assertEquals(expected, joined);
	}

	@DisplayName(".toArray()")
	@ParameterizedTest(name = "{0}")
	@CsvSource(delimiter = ';', value = {
		"when collection is not empty, returns items array;" +
			"[T, b, 4, 0, O];" +
			"[T, b, 4, 0, O]",
		"when collection is empty, returns empty array;" +
			"[];" +
			"[]"
	})
	void testToArray(String name, @StringCollection Collection<String> items,
		@StringArray String[] expected) {

		final var array = items.toArray();
		assertArrayEquals(expected, array);
	}

	@DisplayName(".toArray(Class<? extends T[]>)")
	@ParameterizedTest(name = "{0}")
	@CsvSource(delimiter = ';', value = {
		"when collection is not empty, returns items array;" +
			"[V, b, Q, r, 4, p];" +
			"[V, b, Q, r, 4, p]",
		"when collection is empty, returns empty array;" +
			"[];" +
			"[]"
	})
	void testToArrayClass(String name, @StringCollection Collection<String> items,
		@StringArray String[] expected) {

		final var array = items.toArray(String[].class);
		assertArrayEquals(expected, array);
	}

	static void assertShuffled
		(Collection<String> shuffled, Collection<String> unshuffled) {
		final var items1 = shuffled.toArray(String[].class);
		final var items2 = unshuffled.toArray(String[].class);
		assertArrayNotEquals(items1, items2);

		Arrays.sort(items1);
		Arrays.sort(items2);
		assertArrayEquals(items1, items2);
	}

	static <T> void assertArrayNotEquals(T[] items1, T[] items2) {
		assertFalse(Arrays.equals(items1, items2));
	}
}

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(StringArray.Converter.class)
@interface StringArray {
	class Converter extends TypedArgumentConverter<String, String[]> {
		protected Converter() {
			super(String.class, String[].class);
		}

		@Override
		protected String[] convert(String s) throws ArgumentConversionException {
			final var substring = s.substring(
				s.indexOf("[") + 1,
				s.lastIndexOf("]"));

			if (substring.isBlank()) {
				return new String[0];
			} else {
				final var string = substring.split("\\s*,\\s*");

				return Arrays.stream(string)
					.filter((item) -> !item.equals("null"))
					.toArray(String[]::new);
			}
		}
	}
}

@ConvertWith(StringOptional.Converter.class)
@Retention(RetentionPolicy.RUNTIME)
@interface StringOptional {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, Optional> {
		protected Converter() {
			super(String.class, Optional.class);
		}

		@Override
		protected Optional<String> convert(String s) throws ArgumentConversionException {
			return Optional.ofNullable(s)
				.filter((s2) -> !s2.equals("null"));
		}
	}
}

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(StringCollection.Converter.class)
@interface StringCollection {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, Collection> {
		protected Converter() {
			super(String.class, Collection.class);
		}

		@Override
		protected Collection<String> convert(String s) throws ArgumentConversionException {
			final var items = new StringArray.Converter()
				.convert(s);

			return new AbstractArrayCollection<>(items) {
			};
		}
	}
}

abstract class AbstractArrayCollection<T> implements Collection<T> {
	final Object[] items;

	@SafeVarargs
	protected AbstractArrayCollection(T... items) {
		this.items = items;
	}

	@Override
	public Collection<T> filter(Predicate<T> condition) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <R> Collection<R> convert(Function<T, R> converter) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<>() {
			private int index = 0;

			@Override
			public boolean hasNext() {
				return index < items.length;
			}

			@Override
			public T next() {
				@SuppressWarnings("unchecked")
				var item = (T) items[index];
				++index;
				return item;
			}
		};
	}
}

abstract class AbstractPredicateCollection<T> implements Collection<Predicate<T>> {
	final Predicate<T>[] items;

	@SafeVarargs
	public AbstractPredicateCollection(Predicate<T>... items) {
		this.items = items;
	}

	@Override
	public Collection<Predicate<T>> filter(Predicate<Predicate<T>> condition) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <R> Collection<R> convert(Function<Predicate<T>, R> converter) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Predicate<T>> iterator() {
		throw new UnsupportedOperationException();
	}
}

// created on Jul 7, 2019
