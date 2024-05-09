package co.tsyba.core.collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

public class CollectionTests {
	@DisplayName(".isEmpty()")
	@Tests({
		"when collection is not empty, returns false;" +
			"[f, T, q, r];" +
			"false",
		"when collection is empty, returns true;" +
			"[];" +
			"true"
	})
	void testIsEmpty(@StringCollection Collection<?> items, boolean expected) {
		final var empty = items.isEmpty();
		assertEquals(expected, empty,
			format("%s.isEmpty()", items));
	}

	@DisplayName(".getCount()")
	@Tests({
		"when collection is not empty, returns item count;" +
			"[b, 5, F, e];" +
			"4",
		"when collection is empty, returns 0;" +
			"[];" +
			"0"
	})
	void testGetCount(@StringCollection Collection<?> items, int expected) {
		final var count = items.getCount();
		assertEquals(expected, count,
			format("%s.getCount()", items));
	}

	@DisplayName(".getMinimum(Comparator<T>)")
	@Nested
	class GetMinComparatorTests {
		@DisplayName("\uD83D\uDDFF")
		@Tests({
			"when collection is not empty, returns minimum;" +
				"[b, L, P, g, V, c];" +
				"L",
			"when collection is empty, returns empty optional;" +
				"[];" +
				"null"
		})
		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		void testGetMin(@StringCollection Collection<String> items,
			@StringOptional Optional<String> expected) {
			final var minimum = items.getMin(Comparator.naturalOrder());
			assertEquals(expected, minimum,
				format("%s.getMinimum()", items));
		}
	}

	@DisplayName(".getMin()")
	@Nested
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	class GetMinTests {
		@DisplayName("when items are comparable")
		@Tests({
			"when collection is not empty, returns minimum;" +
				"[g, m, t, E, d, A, s];" +
				"A",
			"when collection is empty, returns empty optional;" +
				"[];" +
				"null"
		})
		void testComparable(@StringCollection Collection<String> items,
			@StringOptional Optional<String> expected) {

			final var min = items.getMin();
			assertEquals(expected, min,
				format("%s.getMin()", items));
		}

		@DisplayName("when items are not comparable")
		@Tests({
			"fails with UnsupportedOperationException"
		})
		void testNotComparable() {
			assertThrows(UnsupportedOperationException.class,
				() -> {
					new PredicateCollection<>()
						.getMin();
				});
		}
	}

	@DisplayName(".getMax(Comparator<T>)")
	@Nested
	class GetMaxComparatorTests {
		@DisplayName("\uD83D\uDDBC")
		@Tests({
			"when collection is not empty, returns maximum;" +
				"[b, L, P, g, V, c];" +
				"g",
			"when collection is empty, returns empty optional;" +
				"[];" +
				"null"
		})
		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		void test(@StringCollection Collection<String> items,
			@StringOptional Optional<String> expected) {

			final var maximum = items.getMax(Comparator.naturalOrder());
			assertEquals(expected, maximum,
				format("%s.getMaximum()", items));
		}
	}

	@DisplayName(".getMax()")
	@Nested
	class GetMaxTests {
		@DisplayName("when items are comparable")
		@Tests({
			"when collection is not empty, returns maximum;" +
				"[g, m, t, E, d, A, s];" +
				"t",
			"when collection is empty, returns empty optional;" +
				"[];" +
				"null"
		})
		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		void testComparable(@StringCollection Collection<String> items,
			@StringOptional Optional<String> expected) {

			final var max = items.getMax();
			assertEquals(expected, max,
				format("%s.getMax()", items));
		}

		@DisplayName("when items are not comparable")
		@Tests({
			"fails with UnsupportedOperationException"
		})
		void testNotComparable() {
			assertThrows(UnsupportedOperationException.class,
				() -> {
					new PredicateCollection<>()
						.getMax();
				});
		}
	}

	@DisplayName(".contains(T)")
	@Nested
	class ContainsTests {
		@DisplayName("when collection is not empty")
		@Tests({
			"when item is present, returns true;" +
				"[t, d, 5, V, A]; 5;" +
				"true",
			"when item is absent, returns false;" +
				"[t, d, 5, V, A]; 7;" +
				"false",
			"when item is null, returns false;" +
				"[t, d, 5, V, A]; null;" +
				"false"
		})
		void testNotEmpty(@StringCollection Collection<String> items, String item,
			boolean expected) {
			test(items, item, expected);
		}

		@DisplayName("when collection is empty")
		@Tests({
			"when collection is empty, returns false;" +
				"[]; 5;" +
				"false",
		})
		void testEmpty(@StringCollection Collection<String> items, String item,
			boolean expected) {
			test(items, item, expected);
		}

		private void test(Collection<String> items, String item, boolean expected) {
			final var contains = items.contains(item);
			assertEquals(expected, contains,
				format("%s.contains(%s)", items, item));
		}
	}

	@DisplayName(".contains(Collection<T>)")
	@Nested
	class ContainsCollectionTests {
		@DisplayName("when collection is not empty")
		@Tests({
			"when all items are present, returns true;" +
				"[t, d, 5, V, A]; [A, d, t];" +
				"true",
			"when some items are absent, returns false;" +
				"[O, P, q]; [P, 0, O];" +
				"false",
			"when all items are absent, returns false;" +
				"[Y, f, E, 3]; [N, R, P];" +
				"false",
			"when argument collection is empty, returns true;" +
				"[Y, f, E, 3]; [];" +
				"true"
		})
		void testNotEmpty(@StringCollection Collection<String> items1,
			@StringCollection Collection<String> items2, boolean expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when collection is empty")
		@Tests({
			"when argument collection is not empty, returns false;" +
				"[]; [A, d, t];" +
				"false",
			"when argument collection is empty, returns true;" +
				"[]; [];" +
				"true"
		})
		void testEmpty(@StringCollection Collection<String> items1,
			@StringCollection Collection<String> items2, boolean expected) {
			test(items1, items2, expected);
		}

		private void test(Collection<String> items1, Collection<String> items2, boolean expected) {
			final var contains = items1.contains(items2);
			assertEquals(expected, contains,
				format("%s.contains(%s)", items1, items2));
		}
	}

	@DisplayName(".noneMatches(Predicate<T>)")
	@Nested
	class NoneMatchesTests {
		@DisplayName("\uD83D\uDD2C")
		@Tests({
			"when no item matches, returns true;" +
				"[K, M, T, S, A];" +
				"true",
			"when some item matches, returns false;" +
				"[K, m, t, S, a];" +
				"false",
			"when all items match, returns false;" +
				"[k, m, t, s, a];" +
				"false",
			"when collection is empty, returns true;" +
				"[];" +
				"true"
		})
		void test(@StringCollection Collection<String> items, boolean expected) {
			final var matches = items.noneMatches((item) -> {
				return item.toLowerCase()
					.equals(item);
			});

			assertEquals(expected, matches,
				format("%s.noneMatches(Predicate<T>)", items));
		}
	}

	@DisplayName(".anyMatches(Predicate<T>)")
	@Nested
	class AnyMatchesTests {
		@DisplayName("\uD83D\uDC20")
		@Tests({
			"when no item matches, returns false;" +
				"[Y, P, D, A, S, M, S];" +
				"false",
			"when some item matches, returns true;" +
				"[Y, P, d, a, S, M, S];" +
				"true",
			"when all items match, returns true;" +
				"[y, p, d, a, s, m, s];" +
				"true",
			"when collection is empty, returns false;" +
				"[];" +
				"false",
		})
		void test(@StringCollection Collection<String> items, boolean expected) {
			final var matches = items.anyMatches((item) -> {
				return item.toLowerCase()
					.equals(item);
			});

			assertEquals(expected, matches,
				format("%s.anyMatches(Predicate<T>)", items));
		}
	}

	@DisplayName(".eachMatches(Predicate<T>)")
	@Nested
	class EachMatchesTests {
		@DisplayName("\uD83D\uDD2A")
		@Tests({
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
		void test(@StringCollection Collection<String> items, boolean expected) {
			final var matches = items.eachMatches((item) -> {
				return item.toLowerCase()
					.equals(item);
			});

			assertEquals(expected, matches,
				format("%s.eachMatches(Predicate<T>)", items));
		}
	}

	@DisplayName(".matchAny(Predicate<T>)")
	@Nested
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	class MatchAnyTests {
		@DisplayName("\uD83C\uDF2D")
		@Tests({
			"when some item matches, returns matched item;" +
				"[F, V, d, P, a, Q];" +
				"d",
			"when no item matches, returns empty optional;" +
				"[F, V, D, P, A, Q];" +
				"null",
			"when collection is empty, returns empty optional;" +
				"[];" +
				"null"
		})
		void test(@StringCollection Collection<String> items,
			@StringOptional Optional<String> expected) {

			final var match = items.matchAny((item) -> {
				return item.toLowerCase()
					.equals(item);
			});

			assertEquals(expected, match,
				format("%s.match(Predicate<T>)", items));
		}
	}

	@DisplayName(".sort(Comparator<T>)")
	@Tests({
		"when collection is not empty, returns sorted list;" +
			"[k, M, s, A, 8, d, q];" +
			"[s, q, k, d, M, A, 8]",
		"when collection is empty, returns empty list;" +
			"[];" +
			"[]"
	})
	void testSortComparator(@StringCollection Collection<String> items,
		@StringList List<String> expected) {

		final var sorted = items.sort(Comparator.reverseOrder());
		assertEquals(expected, sorted,
			format("%s.sort(Comparator<T>)", items));
	}

	@DisplayName(".sort()")
	@Tests(value = {
		"when collection is not empty, returns sorted list;" +
			"[k, M, s, A, 8, d, q];" +
			"[8, A, M, d, k, q, s]",
		"when collection is empty, returns empty list;" +
			"[];" +
			"[]"
	})
	void testSort(@StringCollection Collection<String> items,
		@StringList List<String> expected) {

		final var sorted = items.sort();
		Assertions.assertEquals(expected, sorted,
			format("%s.sort()", items));
	}

	@DisplayName(".sort(), where T not Comparable")
	@Test
	void testSortNotComparable() {
		Assertions.assertThrows(RuntimeException.class, () -> {
			final var collection = new PredicateCollection<>(
				String::isEmpty,
				String::isBlank);

			collection.sort();
		}, "<non comparable items>.sort()");
	}

	@DisplayName(".shuffle(Random)")
	@Nested
	class TestShuffleRandom {
		@Test
		@DisplayName("when collection is not empty, returns shuffled list")
		void testWhenNotEmpty() {
			Collection<String> items = new ArrayCollection<>(
				"r", "E", "V", "s", "x", "w", "O", "8");

			final var time = System.currentTimeMillis();
			final var random = new Random(time);

			// shuffle items 10 times and ensure item order is different after
			// each shuffle
			for (var index = 0; index < 10; ++index) {
				final Collection<String> shuffled = items.shuffle(random);
				assertShuffled(shuffled, items,
					format("%s.shuffle(Random)", items));

				items = shuffled;
			}
		}

		@DisplayName("when collection is empty, returns empty list")
		@Test
		void testWhenEmpty() {
			final var items = new ArrayCollection<>();
			final var time = System.currentTimeMillis();
			final var random = new Random(time);

			final var shuffled = items.shuffle(random);
			final var items2 = new List<>(items);

			assertEquals(items2, shuffled,
				format("%s.shuffle(Random)", items));
		}
	}

	@DisplayName(".shuffle()")
	@Nested
	class TestShuffle {
		@Test
		@DisplayName("when collection is not empty, returns shuffled list")
		void testWhenNotEmpty() {
			Collection<String> items = new ArrayCollection<>(
				"o", "M", "F", "0", "K", "z", "v", "S");

			for (var index = 0; index < 10; ++index) {
				final var shuffled = items.shuffle();
				assertShuffled(shuffled, items,
					format("%s.shuffle()", items));

				items = shuffled;
			}
		}

		@DisplayName("when collection is empty, returns empty list")
		@Test
		void testWhenEmpty() {
			final var items = new ArrayCollection<>();

			final var shuffled = items.shuffle();
			final var items2 = new List<>(items);

			assertEquals(items2, shuffled,
				format("%s.shuffle()", items));
		}
	}

	@DisplayName(".iterate(Consumer<T>)")
	@Tests({
		"when collection is not empty, iterates items;" +
			"[f, F, e, q, p, L];" +
			"[f, F, e, q, p, L]",
		"when collection is empty, does nothing;" +
			"[];" +
			"[]"
	})
	void testIterate(@StringCollection Collection<String> items,
		@StringArray String[] expected) {

		final var iterated = new ArrayList<String>();
		items.iterate(iterated::add);

		assertArrayEquals(expected,
			iterated.toArray(new String[]{}),
			format("%s.iterate(Consumer<T>)", items));
	}

	@DisplayName(".combine(R, BiFunction<R, T, R>)")
	@Tests({
		"when collection is not empty, combines items with initial value;" +
			"[g, E, Q, s, a, B]; B;" +
			"BgEQsaB",
		"when collection is empty, returns initial value;" +
			"[]; A;" +
			"A"
	})
	void testCombine(@StringCollection Collection<String> items, String initial,
		String expected) {

		final var combined = items.combine(initial,
			(combined2, item) -> {
				return combined2 + item;
			});

		assertEquals(expected, combined,
			format("%s.combine(%s, BiFunction<R, T, R>)", items, initial));
	}

	@DisplayName(".join(String)")
	@Tests({
		"when collection is not empty, joins items into a string;" +
			"[g, t, W, q, P, l]; -;" +
			"g-t-W-q-P-l",
		"when collection is empty, returns empty string;" +
			"[]; -;" +
			"null"
	})
	void testJoin(@StringCollection Collection<String> items, String separator,
		String expected) {

		if (expected == null) {
			expected = "";
		}

		final var joined = items.join(separator);
		assertEquals(expected, joined,
			format("%s.join(%s)", items, separator));
	}

	@DisplayName(".toArray()")
	@Tests({
		"when collection is not empty, returns items array;" +
			"[T, b, 4, 0, O];" +
			"[T, b, 4, 0, O]",
		"when collection is empty, returns empty array;" +
			"[];" +
			"[]"
	})
	void testToArray(@StringCollection Collection<String> items,
		@StringArray String[] expected) {

		final var array = items.toArray();
		assertArrayEquals(expected, array,
			format("%s.toArray()", items));
	}

	@DisplayName(".toArray(Class<? extends T[]>)")
	@Tests({
		"when collection is not empty, returns items array;" +
			"[V, b, Q, r, 4, p];" +
			"[V, b, Q, r, 4, p]",
		"when collection is empty, returns empty array;" +
			"[];" +
			"[]"
	})
	void testToArrayClass(@StringCollection Collection<String> items,
		@StringArray String[] expected) {

		final var array = items.toArray(String[].class);
		assertArrayEquals(expected, array,
			format("%s.toArray(Class<? extends T[]>)", items));
	}

	static void assertShuffled
		(Collection<String> shuffled, Collection<String> unshuffled, String message) {
		final var items1 = shuffled.toArray(String[].class);
		final var items2 = unshuffled.toArray(String[].class);
		assertArrayNotEquals(items1, items2);

		Arrays.sort(items1);
		Arrays.sort(items2);
		assertArrayEquals(items1, items2, message);
	}

	static <T> void assertArrayNotEquals(T[] items1, T[] items2) {
		assertFalse(Arrays.equals(items1, items2));
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

			return new ArrayCollection<>(items) {
			};
		}
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
			if (s == null) {
				return null;
			}

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
			return Optional.ofNullable(s);
		}
	}
}

class ArrayCollection<T> implements Collection<T> {
	final Object[] items;

	@SafeVarargs
	protected ArrayCollection(T... items) {
		this.items = items;
	}

	@Override
	public Collection<T> getDistinct() {
		throw new UnsupportedOperationException();
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

	@Override
	public String toString() {
		return "[" + join(", ") + "]";
	}
}

class PredicateCollection<T> implements Collection<Predicate<T>> {
	final Predicate<T>[] items;

	@SafeVarargs
	public PredicateCollection(Predicate<T>... items) {
		this.items = items;
	}

	@Override
	public Collection<Predicate<T>> getDistinct() {
		throw new UnsupportedOperationException();
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
