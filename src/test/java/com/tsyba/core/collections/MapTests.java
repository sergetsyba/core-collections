package com.tsyba.core.collections;

import com.tsyba.core.collections.converter.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.Arrays;
import java.util.Optional;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Map")
class MapTests {
	@DisplayName("Map(Map<K, V>)")
	@Nested
	class NewMapTests {
		@DisplayName("\uD83E\uDD9C")
		@Tests({
			"when argument map is not empty, creates its copy;" +
				"[l:K, M:m, o:P, f:U]",
			"when argument map is empty, creates empty map;" +
				"[]"
		})
		void test(@StringMap Map<String, String> entries) {
			final var map = new Map<>(entries);
			assertNotSame(entries.store, map.store,
				format("new Map<>(%s)", entries));
			assertEquals(entries, map,
				format("new Map<>(%s)", entries));
		}
	}

	@DisplayName("Map(List<K>, List<V>)")
	@Nested
	class NewListsTests {
		@Tests({
			"when key and value lists have same item count, creates map;" +
				"[k, R, s, A, m]; [p, O, O, m, M];" +
				"[k:p, R:O, s:O, A:m, m:M]",
			"when keys list has repeated items, creates map keeping last occurring value;" +
				"[k, R, s, k, R]; [p, O, O, m, M];" +
				"[k:m, R:M, s:O]",
			"when keys list is shorter, creates map ignoring extra values;" +
				"[k, A, m, Q, z]; [p, O, O, m, M, u];" +
				"[k:p, A:O, m:O, Q:m, z:u]",
			"when keys list is empty, creates empty map;" +
				"[]; [p, O, O, m, M];" +
				"[]",
			"when values list is shorter, creates map ignoring extra keys;" +
				"[m, I, o, P, L, l]; [i, P, x, T];" +
				"[m:i, I:P, o:x, P:T]",
			"when values list is empty, creates empty map;" +
				"[m, I, o, P, L, l]; [];" +
				"[]",
			"when keys and values lists are empty, creates empty map;" +
				"[]; [];" +
				"[]"
		})
		void test(@StringList List<String> keys, @StringList List<String> values,
			@StringMap Map<String, String> expected) {

			final var map = new Map<>(keys, values);
			assertEquals(expected, map,
				format("new Map<>(%s, %s)", keys, values));
		}
	}

	@DisplayName("Map(Entry<K, V>...)")
	@Nested
	class NewVarargsTests {
		@Tests({
			"when all entries are not null, creates map;" +
				"[k:P, m:N, y:P, w:Q, d:D];" +
				"[k:P, m:N, y:P, w:Q, d:D]",
			"when some entries have repeated keys, creates map keeping last occurring entry;" +
				"[k:P, m:N, y:P, k:Q, y:D];" +
				"[k:Q, m:N, y:D]",
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
			"when argument array is empty, creates empty map;" +
				"[];" +
				"[]"
		})
		void test(@StringEntryArray Map.Entry<String, String>[] entries,
			@StringMap Map<String, String> expected) {

			final var map = new Map<>(entries);
			assertEquals(expected, map,
				format("new Map<>(%s)",
					Arrays.toString(entries)));
		}
	}

	@DisplayName("Map(Iterable<Entry<K, V>>)")
	@Nested
	class NewIterableTests {
		@Tests({
			"when all entries are not null, creates map;" +
				"[v:E, r:A, t:L, s:Q, f:N];" +
				"[v:E, r:A, t:L, s:Q, f:N]",
			"when some entries have repeated keys, creates map keeping last occurring entry;" +
				"[v:E, r:A, t:L, v:N, s:D];" +
				"[v:N, r:A, t:L, s:D]",
			"when some entries are null, creates map with non-null entries;" +
				"[v:E, null, null, s:Q, null];" +
				"[v:E, s:Q]",
			"when all entries are null, creates empty map;" +
				"[null, null, null, null];" +
				"[]",
			"when some entries have null keys, creates map with non-null key entries;" +
				"[null:E, r:A, t:L, null:Q, null:D];" +
				"[r:A, t:L]",
			"when all entries have null keys, creates empty map;" +
				"[null:E, null:A, null:L, null:Q];" +
				"[]",
			"when some entries have null values, creates map with non-null value entries;" +
				"[v:null, r:null, t:L, s:null, f:N];" +
				"[t:L, f:N]",
			"when all entries have null values, creates empty map;" +
				"[v:null, r:null, s:null, f:null];" +
				"[]",
			"when argument iterable is empty, creates empty map;" +
				"[];" +
				"[]"
		})
		void test(@StringEntryArray Map.Entry<String, String>[] entries,
			@StringMap Map<String, String> expected) {

			final var map = new Map<>(
				Arrays.asList(entries));

			assertEquals(expected, map,
				format("new Map<>(%s)",
					Arrays.toString(entries)));
		}
	}

	@DisplayName("Map(java.util.Map<K, V>)")
	@Nested
	class NewJavaMapTests {
		@Tests({
			"when all entries are not null, creates map;" +
				"[g:R, s:Q, c:B, l:r];" +
				"[g:R, s:Q, c:B, l:r]",
			"when some entries have null keys, creates map with non-null key entries;" +
				"[g:R, s:Q, null:B, l:r];" +
				"[g:R, s:Q, l:r]",
			"when some entries have null values, creates map with non-null value entries;" +
				"[g:null, s:Q, c:B, l:null];" +
				"[s:Q, c:B]",
			"when argument map is empty, creates empty map;" +
				"[];" +
				"[]"
		})
		void test(@StringJavaMap java.util.Map<String, String> entries,
			@StringMap Map<String, String> expected) {

			final var map = new Map<>(entries);
			assertEquals(expected, map,
				format("new Map<>(%s)", entries));
		}
	}

	@DisplayName(".isEmpty()")
	@Nested
	class IsEmptyTests {
		@DisplayName("\uD83C\uDF35")
		@Tests({
			"when map is not empty, returns false;" +
				"[j:Y, o:P, m:K, u:F];" +
				"false",
			"when map is empty, returns true;" +
				"[];" +
				"true"
		})
		void test(@StringMap Map<String, String> entries, boolean expected) {
			final var empty = entries.isEmpty();
			assertEquals(expected, empty,
				format("%s.isEmpty()", entries));
		}
	}

	@DisplayName(".getCount()")
	@Nested
	class GetCountTests {
		@DisplayName("\uD83E\uDDEE")
		@Tests({
			"when map is not empty, returns entry count;" +
				"[k:F, L:N, n:O, D:p];" +
				"4",
			"when map is empty, returns 0;" +
				"[];" +
				"0"
		})
		void test(@StringMap Map<String, String> entries, int expected) {
			final var count = entries.getCount();
			assertEquals(expected, count,
				format("%s.getCount()", entries));
		}
	}

	@DisplayName(".containsKey(K)")
	@Nested
	class ContainsKeyTests {
		@DisplayName("when map is not empty")
		@Tests({
			"when key is present, returns true;" +
				"[C:w, n:J, b:T, p:H, q:D]; q;" +
				"true",
			"when key is absent, returns false;" +
				"[C:w, n:J, b:T, p:H, q:D]; Q;" +
				"false",
			"when key is null, returns false;" +
				"[C:w, n:J, b:T, p:H, q:D]; null;" +
				"false"
		})
		void testMapNotEmpty(@StringMap Map<String, String> entries, String key,
			boolean expected) {
			test(entries, key, expected);
		}

		@DisplayName("when map is empty")
		@Tests({
			"when key is not null, returns false;" +
				"[]; Z;" +
				"false",
			"when key is null, returns false;" +
				"[]; null;" +
				"false"
		})
		void testMapEmpty(@StringMap Map<String, String> entries, String key,
			boolean expected) {
			test(entries, key, expected);
		}

		private void test(Map<String, String> entries, String key, boolean expected) {
			final var contains = entries.containsKey(key);
			assertEquals(expected, contains,
				format("%s.containsKey(%s)", entries, key));
		}
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
		void testMapNotEmpty(@StringMap Map<String, String> entries, String key,
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
		void testMapEmpty(@StringMap Map<String, String> entries, String key,
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

	@DisplayName(".contains(Entry<K, V>)")
	@Nested
	class ContainsEntryTests {
		@DisplayName("when map is not empty")
		@Tests({
			"when entry is present, returns true;" +
				"[q:A, z:E, d:R, m:L]; d:R;" +
				"true",
			"when entry key is present but value differs, returns false;" +
				"[q:A, z:E, d:R, m:L]; d:X;" +
				"false",
			"when entry key is absent, returns false;" +
				"[q:A, z:E, d:R, m:L]; o:R;" +
				"false",
			"when entry key is null, returns false;" +
				"[q:A, z:E, d:R, m:L]; null:R;" +
				"false",
			"when entry is null, returns false;" +
				"[q:A, z:E, d:R, m:L]; null;" +
				"false"
		})
		void testMapNotEmpty(@StringMap Map<String, String> entries,
			@StringMapEntry Map.Entry<String, String> entry, boolean expected) {
			test(entries, entry, expected);
		}

		@DisplayName("when map is empty")
		@Tests({
			"when entry is not null, returns false;" +
				"[]; H:d;" +
				"false",
			"when entry key is null, returns false;" +
				"[]; null:R;" +
				"false",
			"when entry is null, returns false;" +
				"[]; null;" +
				"false"
		})
		void testMapEmpty(@StringMap Map<String, String> entries,
			@StringMapEntry Map.Entry<String, String> entry, boolean expected) {
			test(entries, entry, expected);
		}

		private void test(Map<String, String> entries, Map.Entry<String, String> entry,
			boolean expected) {

			final var contains = entries.contains(entry);
			assertEquals(expected, contains,
				format("%s.contains(%s)", entries, entry));
		}
	}

	@DisplayName(".contains(Collection<Entry<K, V>>)")
	@Nested
	class ContainsMapTests {
		@DisplayName("when map is not empty")
		@Tests({
			"when all entries are present, returns true;" +
				"[k:R, T:b, B:L, R:E]; [T:b, R:E, k:R, B:L];" +
				"true",
			"when all keys are present but some values differ, returns false;" +
				"[k:R, T:b, B:L, R:E]; [T:b, R:W, k:L, B:M];" +
				"false",
			"when all keys are present but all values differ, returns false;" +
				"[k:R, T:b, B:L, R:E]; [k:G, B:V, T:p, R:q];" +
				"false",
			"when some keys are absent, returns false;" +
				"[k:R, T:b, B:L, R:E]; [T:b, r:E, b:L, q:N];" +
				"false",
			"when all keys are absent, returns false;" +
				"[k:R, T:b, B:L, R:E]; [f:R, W:C, z:Q, j:F];" +
				"false",
			"when argument map has more entries, returns false;" +
				"[k:R, T:b, B:L, R:E]; [k:R, g:N, v:Q, T:b, B:L, R:E];" +
				"false",
			"when argument map is empty, returns true;" +
				"[k:R, T:b, B:L, R:E]; [];" +
				"true"
		})
		void testMapNotEmpty(@StringMap Map<String, String> entries1,
			@StringMap Map<String, String> entries2, boolean expected) {
			test(entries1, entries2, expected);
		}

		@DisplayName("when map is empty")
		@Tests({
			"when argument map is not empty, returns false;" +
				"[]; [q:N, v:U, t:B, y:J];" +
				"false",
			"when argument map is empty, returns true;" +
				"[]; [];" +
				"true"
		})
		void testMapEmpty(@StringMap Map<String, String> entries1,
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
	@Nested
	class GetKeysTests {
		@DisplayName("\uD83D\uDD11")
		@Tests({
			"when map is not empty, returns keys;" +
				"[T:k, E:p, V:m, L:s];" +
				"[T, E, V, L]",
			"when map is empty, returns empty set;" +
				"[];" +
				"[]"
		})
		void test(@StringMap Map<String, String> entries,
			@StringSet Set<String> expected) {

			final var keys = entries.getKeys();
			assertEquals(expected, keys,
				format("%s.getKeys()", entries));
		}
	}

	@DisplayName(".getValues()")
	@Nested
	class GetValuesTests {
		@DisplayName("\uD83D\uDCDC")
		@Tests({
			"when map is not empty, returns values;" +
				"[W:x, n:b, L:m, y:s];" +
				"[x, b, m, s]",
			"when map is empty, returns empty list;" +
				"[];" +
				"[]"
		})
		void test(@StringMap Map<String, String> entries,
			@StringList List<String> expected) {

			final var values = entries.getValues();
			assertEquals(expected.sort(), values.sort(),
				format("%s.getValues()", entries));
		}
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
		void testMapNotEmpty(@StringMap Map<String, String> entries, String key,
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
		void testMapEmpty(@StringMap Map<String, String> entries, String key,
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
				"[J:k, M:M, n:d, E:P, H:s]; [J, E, H, M];" +
				"[J:k, E:P, H:s, M:M]",
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
				"[J:k, M:M, n:d, E:P, H:s]; [null, null, null, null];" +
				"[]",
			"when argument array is empty, returns empty map;" +
				"[J:k, M:M, n:d, E:P, H:s]; [];" +
				"[]"
		})
		void testMapNotEmpty(@StringMap Map<String, String> entries,
			@StringArray String[] keys, @StringMap Map<String, String> expected) {
			test(entries, keys, expected);
		}

		@DisplayName("when map is empty")
		@Tests({
			"when argument array is not empty, returns empty map;" +
				"[]; [J, E, H, M];" +
				"[]",
			"when argument array is empty, returns empty map;" +
				"[]; [];" +
				"[]"
		})
		void testMapEmpty(@StringMap Map<String, String> entries,
			@StringArray String[] keys, @StringMap Map<String, String> expected) {
			test(entries, keys, expected);
		}

		private void test(Map<String, String> entries, String[] keys,
			Map<String, String> expected) {

			final var entries2 = entries.get(keys);
			assertEquals(expected, entries2,
				format("%s.get(%s)", entries,
					Arrays.toString(keys)));
		}
	}

	@DisplayName(".get(Collection<K>)")
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
		void testMapNotEmpty(@StringMap Map<String, String> entries,
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
		void testMapEmpty(@StringMap Map<String, String> entries,
			@StringCollection Collection<String> keys,
			@StringMap Map<String, String> expected) {
			test(entries, keys, expected);
		}

		private void test(Map<String, String> entries, Collection<String> keys,
			Map<String, String> expected) {

			final var entries2 = entries.get(keys);
			assertEquals(expected, entries2,
				format("%s.get(%s)", entries, keys));
		}
	}

	@DisplayName(".matchAll(Predicate<Entry<K, V>>)")
	@Nested
	class MatchAllTests {
		@DisplayName("\uD83C\uDF3C")
		@Tests({
			"when some entries match, returns matching entries;" +
				"[T:R, E:a, w:Q, p:d, O:C];" +
				"[E:a, p:d]",
			"when all entries match, returns matching entries;" +
				"[T:f, E:a, w:q, p:d];" +
				"[T:f, E:a, w:q, p:d]",
			"when no entries match, returns empty map;" +
				"[T:F, E:A, w:Q, p:D];" +
				"[]",
			"when map is empty, returns empty map;" +
				"[];" +
				"[]"
		})
		void test(@StringMap Map<String, String> entries,
			@StringMap Map<String, String> expected) {

			final var matched = entries.matchAll((entry) -> {
				return entry.value.toLowerCase()
					.equals(entry.value);
			});

			assertEquals(expected, matched,
				format("%s.matchAll(<value is lowercase>)", entries));
		}
	}

	@DisplayName(".getDistinct()")
	@Nested
	class GetDistinctTests {
		@DisplayName("\uD83C\uDF40")
		@Tests({
			"when map is not empty, returns itself;" +
				"[l:R, 5:x, s:t, 2:p]",
			"when map is empty, returns itself;" +
				"[]"
		})
		void test(@StringMap Map<String, String> entries) {
			final var distinct = entries.getDistinct();
			assertSame(entries, distinct,
				format("%s.getDistinct()", entries));
		}
	}

	@DisplayName(".convert(Function<Entry<K, V>, R>)")
	@Nested
	class ConvertFunctionTests {
		@DisplayName("when no entries convert to null")
		@Tests({
			"when all converted items are distinct, returns all converted items;" +
				"[g:p, w:i, n:v, c:a];" +
				"[p, i, v, a]",
			"when some converted items are same, returns all converted items;" +
				"[g:p, w:i, n:i, c:a];" +
				"[p, i, i, a]",
			"when all converted items are same, returns all converted items;" +
				"[g:p, w:p, n:p, c:p];" +
				"[p, p, p, p]"
		})
		void testNoEntriesConvertNull(@StringMap Map<String, String> entries,
			@StringList List<String> expected) {
			test(entries, expected);
		}

		@DisplayName("when some entries convert to null")
		@Tests({
			"when all non-null converted items are distinct, returns all non-null converted items;" +
				"[G:p, w:i, n:v, c:a];" +
				"[i, v, a]",
			"when some non-null converted items are same, returns all non-null converted items;" +
				"[G:p, w:i, n:i, c:a];" +
				"[i, i, a]",
			"when all non-null converted items are same, returns all non-null converted items;" +
				"[G:p, w:i, n:i, c:i];" +
				"[i, i, i]"
		})
		void testSomeEntriesConvertNull(@StringMap Map<String, String> entries,
			@StringList List<String> expected) {
			test(entries, expected);
		}

		@DisplayName("when all entries convert to null")
		@Tests({
			"returns empty collection;" +
				"[G:P, w:I, n:V, c:A];" +
				"[]"
		})
		void testAllEntriesConvertNull(@StringMap Map<String, String> entries,
			@StringList List<String> expected) {
			test(entries, expected);
		}

		@DisplayName("when map is empty")
		@Tests({
			"returns empty collection;" +
				"[];" +
				"[]"
		})
		void testMapEmpty(@StringMap Map<String, String> entries,
			@StringList List<String> expected) {
			test(entries, expected);
		}

		private void test(Map<String, String> entries, List<String> expected) {
			final var converted = entries.convert((entry) -> {
				final var value = entry.value;
				if (value.toUpperCase()
					.equals(value)) {

					return null;
				}

				return value;
			});

			assertEquals(expected, converted,
				format("%s.convert(<uppercase values to null>)", entries));
		}
	}

	@DisplayName(".join(String, String)")
	@Nested
	class JoinTests {
		@DisplayName("\uD83C\uDF80")
		@Tests({
			"when map is not empty, returns joined string;" +
				"[R:g, G:h, j:W, o:P]; :; ,;" +
				"R:g,j:W,G:h,o:P",
			"when map is empty, returns empty string;" +
				"[]; :; ,;" +
				"null"
		})
		void test(@StringMap Map<String, String> entries, String separator1,
			String separator2, String expected) {

			if (expected == null) {
				expected = "";
			}

			final var joined = entries.join(separator1, separator2);
			assertEquals(expected, joined,
				format("%s.join(%s, %s)", entries, separator1, separator2));
		}
	}

	@DisplayName(".bridge()")
	@Nested
	class BridgeTests {
		@DisplayName("\uD83C\uDF09")
		@Tests({
			"when map is not empty, returns entries in java.util.Map;" +
				"[g:e, w:Q, s:P, v:M, d:S];" +
				"[g:e, w:Q, s:P, v:M, d:S]",
			"when map is empty, returns empty java.util.Map;" +
				"[];" +
				"[]"
		})
		void test(@StringMap Map<String, String> entries,
			@StringJavaMap java.util.Map<String, String> expected) {

			final var bridged = entries.bridge();
			assertEquals(expected, bridged,
				format("%s.bridge()", entries));
		}
	}

	@DisplayName(".toString()")
	@Nested
	class ToStringTests {
		@DisplayName("\uD83C\uDF81")
		@Tests({
			"when map is not empty, returns string representation;" +
				"[T:R, e:q, O:l, j:c];" +
				"{T:R, e:q, j:c, O:l}",
			"when map is empty, returns {};" +
				"[];" +
				"{}"
		})
		void test(@StringMap Map<String, String> entries, String expected) {
			final var string = entries.toString();
			assertEquals(expected, string,
				format("%s.toString()", entries));
		}
	}
}