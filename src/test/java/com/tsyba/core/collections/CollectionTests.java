package com.tsyba.core.collections;

import com.tsyba.core.collections.converter.StringArray;
import com.tsyba.core.collections.converter.StringOptional;
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
	@Nested
	class IsEmptyTests {
		@DisplayName("\uD83C\uDFD4")
		@Tests({
			"when collection is not empty, returns false;" +
				"[f, T, q, r];" +
				"false",
			"when collection is empty, returns true;" +
				"[];" +
				"true"
		})
		void test(@StringCollection Collection<String> items, boolean expected) {
			final var empty = items.isEmpty();
			assertEquals(expected, empty,
				format("%s.isEmpty()", items));
		}
	}

	@DisplayName(".getCount()")
	@Nested
	class GetCountTests {
		@Tests({
			"when collection is not empty, returns item count;" +
				"[b, 5, F, e];" +
				"4",
			"when collection is empty, returns 0;" +
				"[];" +
				"0"
		})
		void test(@StringCollection Collection<?> items, int expected) {
			final var count = items.getCount();
			assertEquals(expected, count,
				format("%s.getCount()", items));
		}
	}


	@DisplayName(".getMin(Comparator<T>)")
	@Nested
	class GetMinComparatorTests {
		@DisplayName("\uD83D\uDDFF")
		@Tests({
			"when collection is not empty, returns minimum;" +
				"[b, L, P, g, V, c];" +
				"g",
			"when collection is empty, returns empty optional;" +
				"[];" +
				"null"
		})
		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		void test(@StringCollection Collection<String> items,
			@StringOptional Optional<String> expected) {

			final var minimum = items.getMin(Comparator.reverseOrder());
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

		@DisplayName("when items are not comparable, fails")
		@Test
		void testNotComparable() {
			assertThrows(UnsupportedOperationException.class,
				() -> {
					new NonComparableCollection()
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
				"L",
			"when collection is empty, returns empty optional;" +
				"[];" +
				"null"
		})
		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		void test(@StringCollection Collection<String> items,
			@StringOptional Optional<String> expected) {

			final var maximum = items.getMax(Comparator.reverseOrder());
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

		@DisplayName("when items are not comparable, fails")
		@Test
		void testNotComparable() {
			assertThrows(UnsupportedOperationException.class,
				() -> {
					new NonComparableCollection()
						.getMax();
				});
		}
	}

	@DisplayName(".contains(T)")
	@Nested
	class ContainsTests {
		@DisplayName("\uD83D\uDC8A")
		@Tests({
			"when item is present, returns true;" +
				"[t, d, 5, V, A]; 5;" +
				"true",
			"when item is absent, returns false;" +
				"[t, d, 5, V, A]; 7;" +
				"false",
			"when item is null, returns false;" +
				"[t, d, 5, V, A]; null;" +
				"false",
			"when collection is empty, returns false;" +
				"[]; 5;" +
				"false"
		})
		void test(@StringCollection Collection<String> items, String item,
			boolean expected) {

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

	@DisplayName(".allMatch(Predicate<T>)")
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
			final var matches = items.allMatch((item) -> {
				return item.toLowerCase()
					.equals(item);
			});

			assertEquals(expected, matches,
				format("%s.allMatch(Predicate<T>)", items));
		}
	}

	@DisplayName(".countMatches(Predicate<T>)")
	@Nested
	class CountMatchesTests {
		@DisplayName("\uD83D\uDC16")
		@Tests({
			"when no item matches, returns 0;" +
				"[t, e, q, s, e, w];" +
				"0",
			"when some items match, returns matched item count;" +
				"[T, e, q, s, E, W];" +
				"3",
			"when all items match, returns item count;" +
				"[T, E, Q, S, E, W];" +
				"6",
			"when collection is empty, returns 0;" +
				"[];" +
				"0"
		})
		void test(@StringCollection Collection<String> items, int expected) {
			final var count = items.countMatches((item) -> {
				return item.toUpperCase()
					.equals(item);
			});

			assertEquals(expected, count,
				format("%s.countMatches(<is uppercase>)", items));
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
	@Nested
	class SortComparatorTests {
		@DisplayName("\uD83C\uDFB3")
		@Tests({
			"when collection is not empty, returns sorted list;" +
				"[k, M, s, A, 8, d, q];" +
				"[s, q, k, d, M, A, 8]",
			"when collection is empty, returns empty list;" +
				"[];" +
				"[]"
		})
		void test(@StringCollection Collection<String> items,
			@StringList List<String> expected) {

			final var sorted = items.sort(Comparator.reverseOrder());
			assertEquals(expected, sorted,
				format("%s.sort(Comparator<T>)", items));
		}
	}

	@DisplayName(".sort()")
	@Nested
	class SortTests {
		@DisplayName("when items are comparable")
		@Tests(value = {
			"when collection is not empty, returns sorted list;" +
				"[k, M, s, A, 8, d, q];" +
				"[8, A, M, d, k, q, s]",
			"when collection is empty, returns empty list;" +
				"[];" +
				"[]"
		})
		void testComparable(@StringCollection Collection<String> items,
			@StringList List<String> expected) {

			final var sorted = items.sort();
			Assertions.assertEquals(expected, sorted,
				format("%s.sort()", items));
		}

		@DisplayName("when items are not comparable, fails")
		@Test
		void testSortNotComparable() {
			Assertions.assertThrows(RuntimeException.class, () -> {
				new NonComparableCollection()
					.sort();
			}, "<non comparable items>.sort()");
		}
	}

	@DisplayName(".shuffle(Random)")
	@Nested
	class ShuffleRandomTests {
		@DisplayName("\uD83D\uDC90")
		@Tests({
			"when collection is not empty, returns items shuffled;" +
				"[r, E, V, s, x, w,O, 8]",
			"when collection is empty, returns empty collection;" +
				"[]"
		})
		void test(@StringCollection Collection<String> items) {
			final var time = System.currentTimeMillis();
			final var random = new Random(time);

			final var shuffled = items.shuffle(random);
			assertShuffled(shuffled, items,
				format("%s.shuffle()", items));
		}
	}

	@DisplayName(".shuffle()")
	@Nested
	class ShuffleTests {
		@DisplayName("\uD83C\uDF16")
		@Tests({
			"when collection is not empty, returns items shuffled;" +
				"[o, M, F, 0, K, z, v, S]",
			"when collection is empty, returns empty collection;" +
				"[]"
		})
		void test(@StringCollection Collection<String> items) {
			final var shuffled = items.shuffle();
			assertShuffled(shuffled, items,
				format("%s.shuffle()", items));
		}
	}

	static void assertShuffled(Collection<String> shuffled,
		Collection<String> unshuffled, String message) {

		final var items1 = shuffled.toArray(String[].class);
		final var items2 = unshuffled.toArray(String[].class);
		if (items1.length == 0) {
			assertEquals(0, items2.length);
		} else {
			assertFalse(Arrays.equals(items1, items2), message);

			Arrays.sort(items1);
			Arrays.sort(items2);
			assertArrayEquals(items1, items2, message);
		}
	}

	@DisplayName(".iterate(Consumer<T>)")
	@Nested
	class IterateTests {
		@DisplayName("\uD83D\uDCE5")
		@Tests({
			"when collection is not empty, iterates items;" +
				"[f, F, e, q, p, L];" +
				"[f, F, e, q, p, L]",
			"when collection is empty, does nothing;" +
				"[];" +
				"[]"
		})
		void test(@StringCollection Collection<String> items,
			@StringArray String[] expected) {

			final var iterated = new ArrayList<String>();
			items.iterate(iterated::add);

			assertArrayEquals(expected,
				iterated.toArray(new String[]{}),
				format("%s.iterate(Consumer<T>)", items));
		}
	}

	@DisplayName(".combine(R, BiFunction<R, T, R>)")
	@Nested
	class CombineTests {
		@DisplayName("\uD83D\uDC53")
		@Tests({
			"when collection is not empty, combines items with initial value;" +
				"[g, E, Q, s, a, B]; B;" +
				"BgEQsaB",
			"when collection is empty, returns initial value;" +
				"[]; A;" +
				"A"
		})
		void test(@StringCollection Collection<String> items, String initial,
			String expected) {

			final var combined = items.combine(initial,
				(combined2, item) -> {
					return combined2 + item;
				});

			assertEquals(expected, combined,
				format("%s.combine(%s, BiFunction<R, T, R>)", items, initial));
		}
	}

	@DisplayName(".join(String)")
	@Nested
	class JoinTests {
		@DisplayName("\uD83C\uDFF0")
		@Tests({
			"when collection is not empty, joins items into a string;" +
				"[g, t, W, q, P, l]; -;" +
				"g-t-W-q-P-l",
			"when collection is empty, returns empty string;" +
				"[]; -;" +
				"null"
		})
		void test(@StringCollection Collection<String> items, String separator,
			String expected) {

			if (expected == null) {
				expected = "";
			}

			final var joined = items.join(separator);
			assertEquals(expected, joined,
				format("%s.join(%s)", items, separator));
		}
	}

	@DisplayName(".toArray(Class<? extends T[]>)")
	@Nested
	class ToArrayClassTests {
		@DisplayName("\uD83C\uDFE1")
		@Tests({
			"when collection is not empty, returns items array;" +
				"[V, b, Q, r, 4, p];" +
				"[V, b, Q, r, 4, p]",
			"when collection is empty, returns empty array;" +
				"[];" +
				"[]"
		})
		void test(@StringCollection Collection<String> items,
			@StringArray String[] expected) {

			final var array = items.toArray(String[].class);
			assertArrayEquals(expected, array,
				format("%s.toArray(Class<? extends T[]>)", items));
		}
	}

	@DisplayName(".toArray()")
	@Nested
	class ToArrayTests {
		@DisplayName("\uD83E\uDD84")
		@Tests({
			"when collection is not empty, returns items array;" +
				"[T, b, 4, 0, O];" +
				"[T, b, 4, 0, O]",
			"when collection is empty, returns empty array;" +
				"[];" +
				"[]"
		})
		void test(@StringCollection Collection<String> items,
			@StringArray String[] expected) {

			final var array = items.toArray();
			assertArrayEquals(expected, array,
				format("%s.toArray()", items));
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

			return new Collection<>() {
				@Override
				public Collection<String> getDistinct() {
					throw new UnsupportedOperationException();
				}

				@Override
				public Collection<String> matchAll(Predicate<String> condition) {
					throw new UnsupportedOperationException();
				}

				@Override
				public <R> Collection<R> convert(Function<String, R> converter) {
					throw new UnsupportedOperationException();
				}

				@Override
				public Iterator<String> iterator() {
					return new ArrayIterator<>(items);
				}
			};
		}
	}
}

class NonComparableCollection implements Collection<Predicate<?>> {
	private final Predicate<?>[] items;

	public NonComparableCollection() {
		this.items = new Predicate[]{
			(item) -> ((String) item).isBlank(),
			(item) -> ((String) item).isEmpty()
		};
	}

	@Override
	public Collection<Predicate<?>> matchAll(Predicate<Predicate<?>> condition) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<Predicate<?>> getDistinct() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <R> Collection<R> convert(Function<Predicate<?>, R> converter) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Predicate<?>> iterator() {
		return new ArrayIterator<>(this.items);
	}
}

// created on Jul 7, 2019
