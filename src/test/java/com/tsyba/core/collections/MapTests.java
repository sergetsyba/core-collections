package com.tsyba.core.collections;

import com.tsyba.core.collections.converter.StringArray;
import com.tsyba.core.collections.converter.StringOptional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

class MapTests {
	@DisplayName("Map(Map<K, V>)")
	@Tests({
		"when argument map is not empty, creates copy map;" +
			"[l:K, M:m, o:P, f:U]",
		"when argument map is empty, creates empty map;" +
			"[]"
	})
	void testNewMap(@StringMap Map<String, String> entries) {
		final var map = new Map<>(entries);
		assertNotSame(entries.store, map.store,
			format("Map<>(%s)", entries));
		assertEquals(map, entries,
			format("Map<>(%s)", entries));
	}

	@DisplayName("Map(List<K>, List<V>)")
	@Tests({
		"when key and value lists have same item count, creates map;" +
			"[k, R, s, A, m]; [p, O, O, m, M];" +
			"[k:p, R:O, s:O, A:m, m:M]",
		"when keys list has repeated items, keeps last occurring value;" +
			"[k, R, s, k, R]; [p, O, O, m, M];" +
			"[k:m, R:M, s:O]",
		"when keys list is shorter, ignores extra values;" +
			"[k, A, m]; [p, O, O, m, M];" +
			"[k:p, A:O, m:O]",
		"when keys list is empty, creates empty map;" +
			"[]; [p, O, O, m, M];" +
			"[]",
		"when values list is shorter, ignores extra keys;" +
			"[m, I, o, P, L, l]; [i, P];" +
			"[m:i, I:P]",
		"when values list is empty, creates empty map;" +
			"[m, I, o, P, L, l]; [];" +
			"[]",
		"when keys and values lists are empty, creates empty map;" +
			"[]; [];" +
			"[]"
	})
	void testNewLists(@StringList List<String> keys, @StringList List<String> values,
		@StringMap Map<String, String> expected) {

		final var map = new Map<>(keys, values);
		assertEquals(expected, map,
			format("Map<>(%s, %s)", keys, values));
	}

	@DisplayName("Map(<Map.Entry<K, V>>...)")
	@Tests({
		"when all entries are not null, creates map;" +
			"[k:P, m:N, y:P, w:Q, d:D];" +
			"[k:P, m:N, y:P, w:Q, d:D]",
		"when some entries have repeated keys, keeps last occurring entry;" +
			"[k:P, m:N, y:P, k:Q, y:D];" +
			"[k:Q, m:N, y:R]",
		"when some entries are null, creates map with non-null entries;" +
			"[k:P, null, null, w:Q, null];" +
			"[k:P, w:Q]",
		"when all entries are null, creates empty map;" +
			"[null, null, null, null];" +
			"[]",
		"when some entries have null keys, creates map with non-null key entries;" +
			"[null:P, m:N, y:P, null:Q, null:D];" +
			"[m:N, y:P]",
		"when all entries have null keys, creates empty map;" +
			"[null:P, null:N, null:P, null:Q];" +
			"[]",
		"when some entries have null values, creates map with non-null value entries;" +
			"[k:null, m:null, y:P, w:null, d:D];" +
			"[y:P, d:D]",
		"when all entries have null values, creates empty map;" +
			"[k:null, m:null, w:null, d:null];" +
			"[]",
		"when entries array is empty, creates empty map;" +
			"[];" +
			"[]"
	})
	void testNewVarargs(@StringEntryArray Map.Entry<String, String>[] entries,
		@StringMap Map<String, String> expected) {

		final var map = new Map<>(entries);
		assertEquals(expected, map,
			format("Map<>(%s)",
				Arrays.toString(entries)));
	}

	@DisplayName("Map(Iterable<K, V>)")
	@Tests({
		"when all entries are not null, creates map;" +
			"[k:P, m:N, y:P, w:Q, d:D];" +
			"[k:P, m:N, y:P, w:Q, d:D]",
		"when some entries have repeated keys, keeps last occurring entry;" +
			"[k:P, m:N, y:P, k:Q, y:D];" +
			"[k:Q, m:N, y:R]",
		"when some entries are null, creates map with non-null entries;" +
			"[k:P, null, null, w:Q, null];" +
			"[k:P, w:Q]",
		"when all entries are null, creates empty map;" +
			"[null, null, null, null];" +
			"[]",
		"when some entries have null keys, creates map with non-null key entries;" +
			"[null:P, m:N, y:P, null:Q, null:D];" +
			"[m:N, y:P]",
		"when all entries have null keys, creates empty map;" +
			"[null:P, null:N, null:P, null:Q];" +
			"[]",
		"when some entries have null values, creates map with non-null value entries;" +
			"[k:null, m:null, y:P, w:null, d:D];" +
			"[y:P, d:D]",
		"when all entries have null values, creates empty map;" +
			"[k:null, m:null, w:null, d:null];" +
			"[]",
		"when entries iterable is empty, creates empty map;" +
			"[];" +
			"[]"
	})
	void testNewIterable(@StringEntryArray Map.Entry<String, String>[] entries,
		@StringMap Map<String, String> expected) {

		final var map = new Map<>(
			Arrays.asList(entries));

		assertEquals(expected, map,
			format("Map<>(%s)",
				Arrays.toString(entries)));
	}

	@DisplayName("Map(java.util.Map<K, V>)")
	@Tests(value = {
		"when all entries are not null, creates map;" +
			"[g:R, s:Q, c:B, l:r];" +
			"[g:R, s:Q, c:B, l:r]",
		"when an entry has null key, creates map with non-null key entries;" +
			"[g:R, s:Q, null:B, l:r];" +
			"[g:R, s:Q, l:r]",
		"when some entries have null values, creates map with non-null value entries;" +
			"[g:null, s:Q, c:B, l:null];" +
			"[s:Q, c:B]",
		"when entries map is empty, creates empty map;" +
			"[];" +
			"[]"
	})
	void testNewJavaMap(@StringJavaMap java.util.Map<String, String> entries,
		@StringMap Map<String, String> expected) {

		final var map = new Map<>(entries);
		assertEquals(expected, map,
			format("Map<>(%s)", entries));
	}

	@DisplayName(".isEmpty()")
	@Tests(value = {
		"when map is not empty, returns false;" +
			"[j:Y, o:P, m:K];" +
			"false",
		"when map is empty, returns true;" +
			"[];" +
			"true"
	})
	void testIsEmpty(@StringMap Map<String, String> entries, boolean expected) {
		final var empty = entries.isEmpty();
		assertEquals(expected, empty,
			format("%s.isEmpty()", entries));
	}

	@DisplayName(".getCount()")
	@Tests({
		"when map is not empty, returns entry count;" +
			"[k:F, L:N, n:O, D:p];" +
			"4",
		"when map is empty, returns 0;" +
			"[];" +
			"0"
	})
	void testGetCount(@StringMap Map<String, String> entries, int expected) {
		final var count = entries.getCount();
		assertEquals(expected, count,
			format("%s.getCount()", entries));
	}

	@DisplayName(".contains(K, V)")
	@Nested
	class ContainsTests {
		@DisplayName("when map is not empty")
		@Tests({
			"when key is present and value equals, returns true;" +
				"[k:V, e:R, k:K, s:S, b:L]; s; S;" +
				"true",
			"when key is present but value differs, returns false;" +
				"[k:V, e:R, k:K, s:S, b:L]; s; O;" +
				"false",
			"when key is absent, returns false;" +
				"[k:V, e:R, k:K, s:S, b:L]; R; S;" +
				"false",
			"when key is null, returns false;" +
				"[k:V, e:R, k:K, s:S, b:L]; null; S;" +
				"false",
			"when value is null, returns false;" +
				"[k:V, e:R, k:K, s:S, b:L]; s; null;" +
				"false",
			"when key and value are null, returns false;" +
				"[k:V, e:R, k:K, s:S, b:L]; null; null;" +
				"false"
		})
		void testNotEmpty(@StringMap Map<String, String> entries, String key,
			String value, boolean expected) {
			test(entries, key, value, expected);
		}

		@DisplayName("when map is empty")
		@Tests({
			"when key and value are not null, returns false;" +
				"[]; H; d;" +
				"false",
			"when key is null, returns false;" +
				"[]; null; E;" +
				"false",
			"when value is null, returns false;" +
				"[]; r; null;" +
				"false",
			"when key and value are null, returns false;" +
				"[]; null; null;" +
				"false"
		})
		void testEmpty(@StringMap Map<String, String> entries, String key,
			String value, boolean expected) {
			test(entries, key, value, expected);
		}

		private void test(Map<String, String> entries, String key, String value,
			boolean expected) {

			final var contains = entries.contains(key, value);
			assertEquals(expected, contains,
				format("%s.contains(%s, %s)", entries, key, value));
		}
	}

	@DisplayName(".contains(Map<K, V>)")
	@Nested
	class ContainsMapTests {
		@DisplayName("when map is not empty")
		@Tests({
			"when all entries are present, returns true;" +
				"[k:R, T:b, B:L, R:E]; [T:b, R:E, k:R];" +
				"true",
			"when all keys are present but some values differ, returns false;" +
				"[k:R, T:b, B:L, R:E]; [T:b, R:W, k:L];" +
				"false",
			"when all keys are present but all values differ, returns false;" +
				"[k:R, T:b, B:L, R:E]; [k:G, B:V, T:p, R:q];" +
				"false",
			"when some keys are absent, returns false;" +
				"[k:R, T:b, B:L, R:E]; [T:b, r:E, b:L];" +
				"false",
			"when all keys are absent, returns false;" +
				"[k:R, T:b, B:L, R:E]; [f:R, W:C, z:Q, j:F];" +
				"false",
			"when argument map has more entries, returns false;" +
				"[k:R, T:b, B:L, R:E]; [k:R, g:N, v:Q T:b, B:L, R:E];" +
				"false",
			"when argument map is empty, returns true;" +
				"[k:R, T:b, B:L, R:E]; [];" +
				"true"
		})
		void testNotEmpty(@StringMap Map<String, String> entries1,
			@StringMap Map<String, String> entries2, boolean expected) {
			test(entries1, entries2, expected);
		}

		@DisplayName("when map is empty")
		@Tests({
			"when argument map is not empty, returns false;" +
				"[]; [T:b, R:E, k:R];" +
				"false",
			"when argument map is empty, returns true;" +
				"[]; [];" +
				"true"
		})
		void testEmpty(@StringMap Map<String, String> entries1,
			@StringMap Map<String, String> entries2, boolean expected) {
			test(entries1, entries2, expected);
		}

		private void test(Map<String, String> entries1, Map<String, String> entries2,
			boolean expected) {

			final var contains = entries1.contains(entries2);
			assertEquals(expected, contains,
				format("%s.contains(%s)", entries1, entries2));
		}
	}

	@DisplayName(".getKeys()")
	@Tests({
		"when map is not empty, returns keys;" +
			"[T:k, E:p, V:m, L:s];" +
			"[T, E, V, L]",
		"when map is empty, returns empty set;" +
			"[];" +
			"[]"
	})
	void testGetKeys(@StringMap Map<String, String> entries,
		@StringSet Set<String> expected) {

		final var keys = entries.getKeys();
		assertEquals(expected, keys,
			format("%s.getKeys()", entries));
	}

	@DisplayName(".getValues()")
	@Tests({
		"when map is not empty, returns values;" +
			"[T:k, E:p, V:m, L:s];" +
			"[k, p, m, s]",
		"when map is empty, returns empty set;" +
			"[];" +
			"[]"
	})
	void testGetValues(@StringMap Map<String, String> entries,
		@StringList List<String> expected) {

		final var values = entries.getValues();
		assertEquals(expected.sort(), values.sort(),
			format("%s.getValues()", entries));
	}

	@DisplayName(".get(K)")
	@Nested
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	class GetTests {
		@DisplayName("when map is not empty")
		@Tests({
			"when key is present, returns value;" +
				"[J:n, R:s, O:n, v:Y, p:L]; O;" +
				"n",
			"when key is absent, returns empty optional;" +
				"[J:n, R:s, O:n, v:Y, p:L]; o;" +
				"null",
			"when key is null, returns empty optional;" +
				"[J:n, R:s, O:n, v:Y, p:L]; null;" +
				"null"
		})
		void testNotEmpty(@StringMap Map<String, String> entries, String key,
			@StringOptional Optional<String> expected) {
			test(entries, key, expected);
		}

		@DisplayName("when map is empty")
		@Tests({
			"when key is not null, returns empty optional;" +
				"[]; K;" +
				"null",
			"when key is null, returns empty optional;" +
				"[]; null;" +
				"null"
		})
		void testEmpty(@StringMap Map<String, String> entries, String key,
			@StringOptional Optional<String> expected) {
			test(entries, key, expected);
		}

		private void test(Map<String, String> entries, String key,
			Optional<String> expected) {

			final var value = entries.get(key);
			assertEquals(expected, value,
				format("%s.get(%s)", entries, key));
		}
	}

	@DisplayName(".get(K...)")
	@Nested
	class GetVarargsTests {
		@DisplayName("when map is not empty")
		@Tests({
			"when all keys are present, returns entries;" +
				"[J:k, M:M, n:d, E:P, H:s]; [J, E, H];" +
				"[J:k, E:P, H:s]",
			"when some keys are present, returns present entries;" +
				"[J:k, M:M, n:d, E:P, H:s]; [j, K, M, n, O];" +
				"[M:M, n:d]",
			"when all keys are absent, returns empty map;" +
				"[J:k, M:M, n:d, E:P, H:s]; [l, U, o, P];" +
				"[]",
			"when some keys are null, returns present entries for non-null keys;" +
				"[J:k, M:M, n:d, E:P, H:s]; [n, null, null, E];" +
				"[n:d, E:P]",
			"when all keys are null, returns empty map;" +
				"[J:k, M:M, n:d, E:P, H:s]; [null, null, null];" +
				"[]",
			"when argument array is empty, returns empty map;" +
				"[J:k, M:M, n:d, E:P, H:s]; [];" +
				"[]"
		})
		void testNotEmpty(@StringMap Map<String, String> entries,
			@StringArray String[] keys, @StringMap Map<String, String> expected) {
			test(entries, keys, expected);
		}

		@DisplayName("when map is empty")
		@Tests({
			"when argument array is not empty, returns empty map;" +
				"[]; [J, E, H];" +
				"[]",
			"when argument array is empty, returns empty map;" +
				"[]; [];" +
				"[]"
		})
		void testEmpty(@StringMap Map<String, String> entries,
			@StringArray String[] keys, @StringMap Map<String, String> expected) {
			test(entries, keys, expected);
		}

		private void test(Map<String, String> entries1, String[] keys,
			Map<String, String> expected) {

			final var entries2 = entries1.get(keys);
			assertEquals(expected, entries2,
				format("%s.get(%s)", entries1,
					Arrays.toString(keys)));
		}
	}

	@DisplayName(".get(Collection<T>)")
	@Nested
	class GetCollectionTests {
		@DisplayName("when map is not empty")
		@Tests({
			"when all keys are present, returns present entries;" +
				"[U:R, d:E, g:A, a:K, V:x]; [d, U, V, g];" +
				"[d:E, U:R, V:x, g:A]",
			"when some keys are present, returns present entries;" +
				"[U:R, d:E, g:A, a:K, V:x]; [D, U, I, c, V];" +
				"[U:R, V:x]",
			"when all keys are absent, returns empty map;" +
				"[U:R, d:E, g:A, a:K, V:x]; [T, e, Q, p];" +
				"[]",
			"when argument collection is empty, returns empty map;" +
				"[U:R, d:E, g:A, a:K, V:x]; [];" +
				"[]"
		})
		void testNotEmpty(@StringMap Map<String, String> entries,
			@StringCollection Collection<String> keys,
			@StringMap Map<String, String> expected) {
			test(entries, keys, expected);
		}

		@DisplayName("when map is empty")
		@Tests({
			"when argument collection is not empty, returns empty map;" +
				"[]; [d, U, V, g];" +
				"[]",
			"when argument collection is empty, returns empty map;" +
				"[]; [];" +
				"[]"
		})
		void testEmpty(@StringMap Map<String, String> entries,
			@StringCollection Collection<String> keys,
			@StringMap Map<String, String> expected) {
			test(entries, keys, expected);
		}

		private void test(Map<String, String> entries1, Collection<String> keys,
			Map<String, String> expected) {

			final var entries2 = entries1.get(keys);
			assertEquals(expected, entries2,
				format("%s.get(%s)", entries1, keys));
		}
	}

	@DisplayName(".anyMatches(BiPredicate<K, V>)")
	@Nested
	class AnyMatchesTests {
		@DisplayName("\uD83D\uDC82")
		@Tests({
			"when all entries match, returns true;" +
				"[T:f, E:a, W:q, P:d];" +
				"true",
			"when some entries match, returns true;" +
				"[T:F, E:a, W:Q, P:d];" +
				"true",
			"when no entries match, returns false;" +
				"[T:F, E:A, W:Q, P:D];" +
				"false",
			"when map is empty, returns false;" +
				"[];" +
				"false"
		})
		void test(@StringMap Map<String, String> entries, boolean expected) {
			final var matches = entries.anyMatches((key, value) -> {
				return value.toLowerCase()
					.equals(value);
			});

			assertEquals(expected, matches,
				format("%s.anyMatches(BiPredicate<K, V>)", entries));
		}
	}

	@DisplayName(".noneMatches(BiPredicate<K, V>)")
	@Nested
	class NoneMatchesTests {
		@DisplayName("\uD83D\uDC60")
		@Tests({
			"when all entries match, returns false;" +
				"[T:f, E:a, W:q, P:d];" +
				"false",
			"when some entries match, returns false;" +
				"[T:F, E:a, W:Q, P:d];" +
				"false",
			"when no entries match, returns true;" +
				"[T:F, E:A, W:Q, P:D];" +
				"true",
			"when map is empty, returns true;" +
				"[];" +
				"true"
		})
		void test(@StringMap Map<String, String> entries, boolean expected) {
			final var matches = entries.noneMatches((key, value) -> {
				return value.toLowerCase()
					.equals(value);
			});

			assertEquals(expected, matches,
				format("%s.noneMatches(BiPredicate<K, V>)", entries));
		}
	}

	@DisplayName(".allMatch(BiPredicate<K, V>)")
	@Nested
	class AllMatchTests {
		@DisplayName("\uD83C\uDFE3")
		@Tests({
			"when all entries match, returns true;" +
				"[T:f, E:a, W:q, P:d];" +
				"true",
			"when some entries match, returns false;" +
				"[T:F, E:a, W:Q, P:d];" +
				"false",
			"when no entries match, returns false;" +
				"[T:F, E:A, W:Q, P:D];" +
				"false",
			"when map is empty, returns true;" +
				"[];" +
				"true"
		})
		void test(@StringMap Map<String, String> entries, boolean expected) {
			final var matches = entries.allMatch((key, value) -> {
				return value.toLowerCase()
					.equals(value);
			});

			assertEquals(expected, matches,
				format("%s.allMatch(BiPredicate<K, V>)", entries));
		}
	}

	@DisplayName(".matchAny(BiPredicate<K, V>)")
	@Nested
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	class TestMatchAny {
		@DisplayName("\uD83C\uDFDE")
		@Tests({
			"when some entry matches, returns its value;" +
				"[T:R, F:D, Q:p, g:c, E:W];" +
				"p",
			"when no entry matches, returns empty optional;" +
				"[T:R, F:D, Q:P, g:C, E:W];" +
				"null",
			"when map is empty, returns empty optional;" +
				"[];" +
				"null"
		})
		void test(@StringMap Map<String, String> entries,
			@StringOptional Optional<String> expected) {
			final var matches = entries.matchAny((key, value) -> {
				return value.toLowerCase()
					.equals(value);
			});

			assertEquals(expected, matches,
				format("%s.matchAny(BiPredicate<K, V>)", entries));
		}
	}

	@DisplayName(".iterate(BiConsumer<K, V>)")
	@Tests({
		"when map is not empty, iterates entries;" +
			"[T:e, g:O, p:S, s:P];" +
			"[Te, gO, pS, sP]",
		"when map is empty, does nothing;" +
			"[];" +
			"[]"
	})
	void testIterate(@StringMap Map<String, String> entries,
		@StringSet Set<String> expected) {

		final var iterated = new MutableSet<String>();
		final var returned = entries.iterate((key, value) -> {
			iterated.add(key + value);
		});

		assertSame(entries, returned);
		assertEquals(expected, iterated,
			format("%s.iterate(BiConsumer<K, V>)", entries));
	}

	@DisplayName(".filter(BiPredicate<K, V>)")
	@Nested
	class FilterTests {
		@DisplayName("\uD83C\uDF69")
		@Tests({
			"when all entries match, returns all entries;" +
				"[r:R, e:E, p:P, f:F];" +
				"[r:R, e:E, p:P, f:F]",
			"when some entries match, returns matched entries;" +
				"[r:R, e:w, p:K, f:F];" +
				"[r:R, f:F]",
			"when no entries match, returns empty map;" +
				"[r:Q, e:c, p:L, f:D];" +
				"[]",
			"when map is empty, returns empty map;" +
				"[];" +
				"[]"
		})
		void test(@StringMap Map<String, String> entries,
			@StringMap Map<String, String> expected) {
			final var filtered = entries.filter((key, value) -> {
				return value.toLowerCase()
					.equals(key);
			});

			assertEquals(expected, filtered,
				format("%s.filter(BiPredicate<K, V>)", entries));
		}
	}

	@DisplayName(".convert(BiFunction<K, V, Entry<L, W>>)")
	@Nested
	class ConvertTests {
		@DisplayName("\uD83D\uDCF9")
		@Tests({
			"when all entries convert to not null, returns converted entries;" +
				"[R:f, S:q, T:k, C:b, O:p];" +
				"[r:F, s:Q, t:L, c:B, o:P]",
			"when some entries convert to null, returns non-null converted entries;" +
				"[r:F, S:q, t:K, C:b, O:p];" +
				"[s:Q, c:B, o:P]",
			"when all entries convert to null, returns empty map;" +
				"[r:F, s:Q, t:K, c:B, o:P];" +
				"[]",
			"when some entry keys convert to null, returns non-null key converted entries;" +
				"[r:f, S:q, t:k, C:b, o:p];" +
				"[s:Q, c:B]",
			"when all entry keys convert to null, returns empty map;" +
				"[r:f, s:q, t:k, c:b, o:p];" +
				"[]",
			"when some entry values convert to null, returns non-null converted value entries;" +
				"[R:F, S:q, T:K, C:B, O:p];" +
				"[s:Q, o:P]",
			"when all entry values convert to null, returns empty map;" +
				"[R:F, S:Q, T:K, C:B, O:P];" +
				"[]",
			"when map is empty, returns empty map;" +
				"[];" +
				"[]"
		})
		void test(@StringMap Map<String, String> entries,
			@StringMap Map<String, String> expected) {

			final var converted = entries.convert((key1, value1) -> {
				var key2 = key1.toLowerCase();
				if (key1.equals(key2)) {
					key2 = null;
				}
				var value2 = value1.toUpperCase();
				if (value1.equals(value2)) {
					value2 = null;
				}

				return key2 == null && value2 == null
					? null
					: new Map.Entry<>(key2, value2);
			});

			assertEquals(expected, converted,
				format("%s.convert(BiFunction<K, V, Entry<L, W>>)", entries));
		}
	}

	@DisplayName(".combine(TriFunction<R, K, V, R>)")
	@Tests({
		"when map is not empty, returns combined value;" +
			"[R:e, g:K, d:E, l:L]; O;" +
			"OdElLRegK",
		"when map is empty, returns initial value;" +
			"[]; O;" +
			"O"
	})
	void testCombine(@StringMap Map<String, String> entries, String initial,
		String expected) {

		final var combined = entries.combine(initial,
			(result, key, value) -> {
				return result + key + value;
			});

		assertEquals(expected, combined,
			format("%s.combine(TriFunction<R, K, V, R>)", entries));
	}

	@DisplayName(".join(String, String)")
	@Tests({
		"when map is not empty, returns joined string;" +
			"[R:g, G:h, j:W, o:P]; :; ,;" +
			"R:g,j:W,G:h,o:P",
		"when map is empty, returns empty string;" +
			"[]; :; ,;" +
			"null"
	})
	void testJoin(@StringMap Map<String, String> entries, String separator1,
		String separator2, String expected) {

		if (expected == null) {
			expected = "";
		}

		final var joined = entries.join(separator1, separator2);
		assertEquals(expected, joined,
			format("%s.join(%s, %s)", entries, separator1, separator2));
	}

	@DisplayName(".bridge()")
	@Tests({
		"when map is not empty, returns entries in java.util.Map;" +
			"[g:e, w:Q, s:P, v:M, d:S];" +
			"[g:e, w:Q, s:P, v:M, d:S]",
		"when map is empty, returns empty java.util.Map;" +
			"[];" +
			"[]"
	})
	void testBridge(@StringMap Map<String, String> entries,
		@StringJavaMap java.util.Map<String, String> expected) {

		final var bridged = entries.bridge();
		assertEquals(expected, bridged,
			format("%s.bridge()", entries));
	}

	@DisplayName(".toString()")
	@Tests({
		"when map is not empty, returns string;" +
			"[T:R, e:q, O:l, j:c];" +
			"{T:R, e:q, j:c, O:l}",
		"when map is empty, returns {};" +
			"[];" +
			"{}"
	})
	void testToString(@StringMap Map<String, String> entries, String expected) {
		final var string = entries.toString();
		assertEquals(expected, string,
			format("%s.toString()", entries));
	}
}

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(StringMap.Converter.class)
@interface StringMap {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, Map> {
		protected Converter() {
			super(String.class, Map.class);
		}

		@Override
		protected Map<String, String> convert(String s) throws ArgumentConversionException {
			final var entries = new StringEntryArray.Converter()
				.convert(s);

			return new Map<>(entries);
		}
	}
}

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(StringEntryArray.Converter.class)
@interface StringEntryArray {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, Map.Entry[]> {
		protected Converter() {
			super(String.class, Map.Entry[].class);
		}

		@Override
		@SuppressWarnings("unchecked")
		protected Map.Entry<String, String>[] convert(String s) throws ArgumentConversionException {
			return Arrays.stream(new StringArray.Converter()
					.convert(s))
				.filter(Objects::nonNull)
				.map(this::parseEntry)
				.toArray(Map.Entry[]::new);
		}

		private Map.Entry<String, String> parseEntry(String s) {
			final var parts = s.split("\\s*:\\s*");
			final var key = "null".equals(parts[0])
				? null
				: parts[0];
			final var value = "null".equals(parts[1])
				? null
				: parts[1];

			return new Map.Entry<>(key, value);
		}
	}
}

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(StringJavaMap.Converter.class)
@interface StringJavaMap {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, java.util.Map> {
		protected Converter() {
			super(String.class, java.util.Map.class);
		}

		@Override
		protected java.util.Map<String, String> convert(String s) throws ArgumentConversionException {
			final var entries = new StringEntryArray.Converter()
				.convert(s);

			final var map = new HashMap<String, String>();
			for (var entry : entries) {
				map.put(entry.key, entry.value);
			}

			return map;
		}
	}
}