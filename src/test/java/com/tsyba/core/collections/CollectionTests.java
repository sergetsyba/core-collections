package com.tsyba.core.collections;

import com.tsyba.core.collections.converter.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

class CollectionTests {
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
			"when collection is not empty, returns minimum item according to comparator;" +
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
				format("%s.getMin(Comparator<T>)", items));
		}

		@DisplayName("when items are not comparable, returns minimum item according to comparator")
		@Test
		void testItemsNotComparable() {
			final var items = new NonComparableCollection(
				new Object(),
				new Object(),
				new Object());

			final var first = items.iterator()
				.next();

			final var minimum = items.getMin((item1, item2) -> 0);
			assertSame(first, minimum.get(),
				format("%s.getMin(Comparator<T>)", items));
		}
	}

	@DisplayName(".getMin()")
	@Nested
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	class GetMinTests {
		@DisplayName("when items are comparable")
		@Tests({
			"when collection is not empty, returns minimum item;" +
				"[g, m, t, E, d, A, s];" +
				"A",
			"when collection is empty, returns empty optional;" +
				"[];" +
				"null"
		})
		void testItemsComparable(@StringCollection Collection<String> items,
			@StringOptional Optional<String> expected) {

			final var min = items.getMin();
			assertEquals(expected, min,
				format("%s.getMin()", items));
		}

		@DisplayName("when items are not comparable, fails")
		@Test
		void testItemsNotComparable() {
			final var items = new NonComparableCollection(
				new Object(),
				new Object(),
				new Object());

			assertThrows(RuntimeException.class,
				() -> {
					items.getMin();
				}, format("%s.getMin()", items));
		}
	}

	@DisplayName(".getMax(Comparator<T>)")
	@Nested
	class GetMaxComparatorTests {
		@DisplayName("\uD83D\uDDBC")
		@Tests({
			"when collection is not empty, returns maximum item according to comparator;" +
				"[n, e, v, R, t, m, D];" +
				"D",
			"when collection is empty, returns empty optional;" +
				"[];" +
				"null"
		})
		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		void test(@StringCollection Collection<String> items,
			@StringOptional Optional<String> expected) {

			final var maximum = items.getMax(Comparator.reverseOrder());
			assertEquals(expected, maximum,
				format("%s.getMax(Comparator<T>)", items));
		}

		@DisplayName("when items are not comparable, returns maximum item according to comparator")
		@Test
		void testItemsNotComparable() {
			final var items = new NonComparableCollection(
				new Object(),
				new Object(),
				new Object());

			final var first = items.iterator()
				.next();

			final var maximum = items.getMax((item1, item2) -> 0);
			assertSame(first, maximum.get(),
				format("%s.getMax(Comparator<T>)", items));
		}
	}

	@DisplayName(".getMax()")
	@Nested
	class GetMaxTests {
		@DisplayName("when items are comparable")
		@Tests({
			"when collection is not empty, returns maximum item;" +
				"[h, W, o, k, C, z];" +
				"z",
			"when collection is empty, returns empty optional;" +
				"[];" +
				"null"
		})
		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		void testItemsComparable(@StringCollection Collection<String> items,
			@StringOptional Optional<String> expected) {

			final var max = items.getMax();
			assertEquals(expected, max,
				format("%s.getMax()", items));
		}

		@DisplayName("when items are not comparable, fails")
		@Test
		void testItemsNotComparable() {
			final var items = new NonComparableCollection(
				new Object(),
				new Object(),
				new Object());

			assertThrows(RuntimeException.class,
				() -> {
					items.getMax();
				}, format("%s.getMax()", items));
		}
	}

	@DisplayName(".contains(T)")
	@Nested
	class ContainsTests {
		@DisplayName("\uD83D\uDC8A")
		@Tests({
			"when argument item is present, returns true;" +
				"[h, Y, 3, w, R]; 3;" +
				"true",
			"when argument item is absent, returns false;" +
				"[k, 7, Z, n, B]; q;" +
				"false",
			"when argument item is null, returns false;" +
				"[x, E, 9, s, M]; null;" +
				"false",
			"when collection is empty, returns false;" +
				"[]; 4;" +
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
			"when all argument items are present, returns true;" +
				"[x, R, 7, m, K]; [m, 7, x, R];" +
				"true",
			"when some argument items are absent, returns false;" +
				"[w, G, 4, r]; [G, 0, w, Q];" +
				"false",
			"when all argument items are absent, returns false;" +
				"[B, v, 6, T]; [N, c, E, 5];" +
				"false",
			"when argument collection is empty, returns true;" +
				"[L, 8, y, D]; [];" +
				"true"
		})
		void testCollectionNotEmpty(@StringCollection Collection<String> items1,
			@StringCollection Collection<String> items2, boolean expected) {
			test(items1, items2, expected);
		}

		@DisplayName("when collection is empty")
		@Tests({
			"when argument collection is not empty, returns false;" +
				"[]; [p, 2, W, n];" +
				"false",
			"when argument collection is empty, returns true;" +
				"[]; [];" +
				"true"
		})
		void testCollectionEmpty(@StringCollection Collection<String> items1,
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
				"[R, X, B, E, M];" +
				"true",
			"when some item matches, returns false;" +
				"[Q, w, Z, k, F];" +
				"false",
			"when all items match, returns false;" +
				"[h, y, c, s, p];" +
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
				"[Q, Z, E, T, U];" +
				"false",
			"when some item matches, returns true;" +
				"[G, o, R, n, W];" +
				"true",
			"when all items match, returns true;" +
				"[m, x, f, k, r];" +
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
	class AllMatchTests {
		@DisplayName("\uD83D\uDD2A")
		@Tests({
			"when no item matches, returns false;" +
				"[C, N, W, Y];" +
				"false",
			"when some item matches, returns false;" +
				"[J, q, V, r];" +
				"false",
			"when all items match, returns true;" +
				"[u, b, z, h];" +
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
				"[n, u, y, c, f, p];" +
				"0",
			"when some items match, returns matched item count;" +
				"[R, e, O, b, W, g];" +
				"3",
			"when all items match, returns item count;" +
				"[D, K, X, M, J, H];" +
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
				"[K, M, y, R, w, B];" +
				"y",
			"when no item matches, returns empty optional;" +
				"[K, T, G, Z, X, C];" +
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
				format("%s.matchAny(Predicate<T>)", items));
		}
	}

	@DisplayName(".sort(Comparator<T>)")
	@Nested
	class SortComparatorTests {
		@DisplayName("\uD83C\uDFB3")
		@Tests({
			"when collection is not empty, returns items ordered according to comparator;" +
				"[k, M, s, A, 8, d, q];" +
				"[s, q, k, d, M, A, 8]",
			"when collection has duplicate items, returns items ordered according to comparator;" +
				"[b, a, b, A];" +
				"[b, b, a, A]",
			"when collection is empty, returns empty copy;" +
				"[];" +
				"[]"
		})
		void test(@StringCollection Collection<String> items,
			@StringList List<String> expected) {

			final var sorted = items.sort(Comparator.reverseOrder());
			assertEquals(expected, sorted,
				format("%s.sort(Comparator<T>)", items));
		}

		@DisplayName("when items are not comparable, returns items ordered according to comparator")
		@Test
		void testItemsNotComparable() {
			final var items = new NonComparableCollection(
				new Object(),
				new Object(),
				new Object());

			final var sorted = items.sort((item1, item2) -> 0);
			final var iterator = items.iterator();

			for (var item : sorted) {
				assertSame(iterator.next(), item,
					format("%s.sort(Comparator<T>)", items));
			}
			assertFalse(iterator.hasNext(),
				format("%s.sort(Comparator<T>)", items));
		}
	}

	@DisplayName(".sort()")
	@Nested
	class SortTests {
		@DisplayName("when items are comparable")
		@Tests({
			"when collection is not empty, returns items ordered naturally;" +
				"[n, W, e, 9, k, R];" +
				"[9, R, W, e, k, n]",
			"when collection has duplicate items, returns items ordered naturally;" +
				"[d, p, d, m];" +
				"[d, d, m, p]",
			"when collection is empty, returns empty copy;" +
				"[];" +
				"[]"
		})
		void testItemsComparable(@StringCollection Collection<String> items,
			@StringList List<String> expected) {

			final var sorted = items.sort();
			assertEquals(expected, sorted,
				format("%s.sort()", items));
			assertNotSame(items, sorted,
				format("%s.sort()", items));
		}

		@DisplayName("when items are not comparable, fails")
		@Test
		void testItemsNotComparable() {
			final var items = new NonComparableCollection(
				new Object(),
				new Object(),
				new Object());

			assertThrows(RuntimeException.class, () -> {
				items.sort();
			}, format("%s.sort()", items));
		}
	}

	@DisplayName(".shuffle()")
	@Nested
	class ShuffleTests {
		@DisplayName("\uD83C\uDFAE")
		@Tests({
			"when collection is not empty, returns shuffled copy;" +
				"[t, R, 4, e, W, 9, A]",
			"when collection has duplicate items, returns shuffled copy;" +
				"[h, M, h, 6, s, K]",
			"when collection has single item, returns its copy;" +
				"[b]",
			"when collection is empty, returns empty copy;" +
				"[]"
		})
		void test(@StringCollection Collection<String> items) {
			final var shuffled = assertShuffles(items,
				Collection::shuffle,
				format("%s.shuffle()", items));

			assertNotSame(items, shuffled,
				format("%s.shuffle()", items));
		}
	}

	@DisplayName(".shuffle(Random)")
	@Nested
	class ShuffleRandomTests {
		@DisplayName("\uD83C\uDFB2")
		@Tests({
			"when collection is not empty, returns shuffled copy;" +
				"[c, 8, U, r, F, 2, k]",
			"when collection has duplicate items, returns shuffled copy;" +
				"[n, E, n, 3, q, D]",
			"when collection has single item, returns its copy;" +
				"[Y]",
			"when collection is empty, returns empty copy;" +
				"[]"
		})
		void test(@StringCollection Collection<String> items) {
			final var random = new Random();
			final var shuffled = assertShuffles(items,
				items2 -> items2.shuffle(random),
				format("%s.shuffle(Random)", items));

			assertNotSame(items, shuffled,
				format("%s.shuffle(Random)", items));
		}
	}

	private static Collection<String> assertShuffles(Collection<String> items,
		Function<Collection<String>, Collection<String>> shuffle, String message) {

		final var random = new Random();
		for (var attempt = 0; attempt < 100; ++attempt) {
			final var shuffled = shuffle.apply(items);
			final var isShuffled = isArrayShuffled(
				items.toArray(String[].class),
				shuffled.toArray(String[].class));

			if (isShuffled) {
				return shuffled;
			} else {
				try {
					// wait for the next millisecond with a random pause, so the next
					// shuffle has a different time seed
					final var time = System.currentTimeMillis();
					do {
						Thread.sleep(random.nextInt(100));
					} while (System.currentTimeMillis() == time);
				} catch (InterruptedException exception) {
					fail(message);
				}
			}
		}

		return fail(message);
	}

	private static boolean isArrayShuffled(String[] shuffled, String[] original) {
		final var equals = Arrays.equals(shuffled, original);
		if (shuffled.length < 2) {
			return equals;
		} else {
			// copy arrays
			shuffled = Arrays.copyOf(shuffled, shuffled.length);
			original = Arrays.copyOf(original, original.length);
			// sort arrays
			Arrays.sort(shuffled);
			Arrays.sort(original);
			// verify arrays are not equal before sort, but are equal after
			return !equals
				&& Arrays.equals(shuffled, original);
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

			final var items2 = new ArrayList<String>();
			final var returned = items.iterate(items2::add);

			assertSame(items, returned,
				format("%s.iterate(Consumer<T>) returns itself", items));
			assertArrayEquals(expected,
				items2.toArray(new String[]{}),
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
			"when collection has single item, returns item string;" +
				"[h]; -;" +
				"h",
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

// created on Jul 7, 2019
