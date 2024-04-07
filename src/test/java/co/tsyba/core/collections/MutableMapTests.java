package co.tsyba.core.collections;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

class MutableMapTests {
	@DisplayName(".get(K, V)")
	@Nested
	class TestGet {
		@DisplayName("when map is not empty")
		@Tests({
			"when key is present, does not update entry and returns stored value;" +
				"[h:E, d:Q, B:s, L:t, U:p]; L; X;" +
				"[h:E, d:Q, B:s, L:t, U:p]; t",
			"when key is absent, inserts entry and returns argument value;" +
				"[h:E, d:Q, B:s, L:t, U:p]; G; K;" +
				"[h:E, d:Q, B:s, L:t, U:p, G:X]; K",
			"when key is absent and value is null, does not insert entry and returns null;" +
				"[h:E, d:Q, B:s, L:t, U:p]; G; null;" +
				"[h:E, d:Q, B:s, L:t, U:p]; null",
			"when key is null, does not insert entry and returns argument value;" +
				"[h:E, d:Q, B:s, L:t, U:p]; null; Y;" +
				"[h:E, d:Q, B:s, L:t, U:p]; Y",
			"when key and value are null, does not insert entry and returns null;" +
				"[h:E, d:Q, B:s, L:t, U:p]; null; null;" +
				"[h:E, d:Q, B:s, L:t, U:p]; null"
		})
		void testNotEmpty(@StringMutableMap MutableMap<String, String> entries,
			String key, String value,
			@StringMap Map<String, String> expected1, String expected2) {
			test(entries, key, value, expected1, expected2);
		}

		@DisplayName("when map is empty")
		@Tests({
			"when key is not null, inserts entry and returns argument value;" +
				"[]; G; X;" +
				"[G:X]; X",
			"when key is null, does not insert entry and returns argument value;" +
				"[]; null; Y;" +
				"[]; Y",
			"when value is null, does not insert entry and returns null;" +
				"[]; G; null;" +
				"[]; null",
			"when key and value are null, does insert entry and returns null;" +
				"[]; null; null;" +
				"[]; null"
		})
		void testEmpty(@StringMutableMap MutableMap<String, String> entries,
			String key, String value,
			@StringMap Map<String, String> expected1, String expected2) {
			test(entries, key, value, expected1, expected2);
		}

		private void test(MutableMap<String, String> entries, String key, String value,
			Map<String, String> expected1, String expected2) {

			final var returned = entries.get(key, value);
			assertEquals(expected2, returned,
				format("%s.get(%s, %s)", entries, key, value));
			assertEquals(expected1, entries,
				format("%s.get(%s, %s)", entries, key, value));
		}
	}

	@DisplayName(".set(K, V)")
	@Nested
	class SetTests {
		@DisplayName("when map is not empty")
		@Tests({
			"when key is absent, inserts new entry;" +
				"[J:v, N:f, R:s]; K; O;" +
				"[J:v, N:f, R:s, K:O]",
			"when key is present, replaces stored value;" +
				"[J:v, N:f, R:s]; N; O;" +
				"[J:v, N:O, R:s]",
			"when key is null, does nothing;" +
				"[J:v, N:f, R:s]; null; O;" +
				"[J:v, N:f, R:s]",
			"when value is null, does nothing;" +
				"[J:v, N:f, R:s]; K; null;" +
				"[J:v, N:f, R:s]",
			"when key and value are null, does nothing;" +
				"[J:v, N:f, R:s]; null; null;" +
				"[J:v, N:f, R:s]"
		})
		void testNotEmpty(@StringMutableMap MutableMap<String, String> entries,
			String key, String value,
			@StringMap Map<String, String> expected) {
			test(entries, key, value, expected);
		}

		@DisplayName("when map is not empty")
		@Tests({
			"when key and value are not null, inserts new entry;" +
				"[]; K; O;" +
				"[K:O]",
			"when key is null, does nothing;" +
				"[]; null; O;" +
				"[]",
			"when value is null, does nothing;" +
				"[]; K; null;" +
				"[]",
			"when key and value are null, does nothing;" +
				"[]; null; null;" +
				"[]",
		})
		void testEmpty(@StringMutableMap MutableMap<String, String> entries,
			String key, String value,
			@StringMap Map<String, String> expected) {
			test(entries, key, value, expected);
		}

		private void test(MutableMap<String, String> entries, String key, String value,
			Map<String, String> expected) {

			final var returned = entries.set(key, value);
			assertSame(entries, returned,
				format("%s.set(%s, %s)", entries, key, value));
			assertEquals(expected, entries,
				format("%s.set(%s, %s)", entries, key, value));
		}
	}

	@DisplayName(".add(Map<K, V>)")
	@Nested
	class TestAddMap {
		@DisplayName("when map is not empty")
		@Tests({
			"when all keys are absent, inserts new entries;" +
				"[h:E, c:S, a:Q, K:n]; [o:P, T:g, B:n];" +
				"[h:E, c:S, a:Q, K:n, o:P, T:g, B:n]",
			"when some keys are present, replaces their values;" +
				"[h:E, c:S, a:Q, K:n]; [a:P, T:g, h:n];" +
				"[h:n, c:S, a:P, K:n, T:g]",
			"when all keys are present, replaces their values;" +
				"[h:E, c:S, a:Q, K:n]; [c:P, h:g, a:n];" +
				"[h:g, c:P, a:n, K:n]",
			"when argument map is empty, does nothing;" +
				"[h:E, c:S, a:Q, K:n]; [];" +
				"[h:E, c:S, a:Q, K:n]"
		})
		void testNotEmpty(@StringMutableMap MutableMap<String, String> entries1,
			@StringMap Map<String, String> entries2,
			@StringMap Map<String, String> expected) {
			test(entries1, entries2, expected);
		}

		@DisplayName("when map is empty")
		@Tests({
			"when argument map is not empty, inserts all entries;" +
				"[]; [o:P, T:g, B:n];" +
				"[o:P, T:g, B:n]",
			"when argument map is empty, does nothing;" +
				"[]; [];" +
				"[]"
		})
		void testEmpty(@StringMutableMap MutableMap<String, String> entries1,
			@StringMap Map<String, String> entries2,
			@StringMap Map<String, String> expected) {
			test(entries1, entries2, expected);
		}

		private void test(MutableMap<String, String> entries1, Map<String, String> entries2,
			Map<String, String> expected) {

			final var returned = entries1.add(entries2);
			assertSame(entries1, returned,
				format("%s.add(%s)", entries1, entries2));
			assertEquals(expected, entries1,
				format("%s.add(%s)", entries1, entries2));
		}
	}

	@DisplayName(".add(Map<K, V>, TriFunction<K, V, V, V>)")
	@Nested
	class TestAddMapTriFunction {
		@DisplayName("when map is not empty")
		@Tests({
			"when all keys are absent, inserts new entries;" +
				"[k:v, E:f, s:d, m:p]; [l:F, e:e, q:P];" +
				"[k:v, E:f, s:d, m:p, l:F, e:e, q:P]",
			"when some keys are present, replaces stored values with resolved ones;" +
				"[k:v, E:F, s:d, m:p]; [s:F, E:e, q:P];" +
				"[k:v, E:F, s:F, m:p, q:P]",
			"when some keys are present and values resolve to null, does not replace stored values;" +
				"[k:v, E:f, s:d, m:p]; [s:null, E:F, q:P];" +
				"[k:v, E:F, s:d, m:p, q:P]",
			"when all keys are present, replaces stored values with resolved ones;" +
				"[k:v, E:F, s:d, m:p]; [s:F, E:W, m:Q];" +
				"[k:v, E:F, s:F, m:Q]",
			"when all keys are present and values resolve to null, does not replace stores values;" +
				"[k:v, E:f, s:d, m:p]; [s:c, E:null, m:null];" +
				"[k:v, E:f, s:c, m:p]",
			"when argument map is empty, does nothing;" +
				"[k:v, E:f, s:d, m:p]; [];" +
				"[k:v, E:f, s:d, m:p]"
		})
		void testNotEmpty(@StringMutableMap MutableMap<String, String> entries1,
			@StringMap Map<String, String> entries2,
			@StringMap Map<String, String> expected) {
			test(entries1, entries2, expected);
		}

		@DisplayName("when map is empty")
		@Tests({
			"when argument map is not empty, inserts new entries;" +
				"[]; [l:F, e:e, q:P];" +
				"[l:F, e:e, q:P]",
			"when argument map is empty, does nothing;" +
				"[]; [];" +
				"[]"
		})
		void testEmpty(@StringMutableMap MutableMap<String, String> entries1,
			@StringMap Map<String, String> entries2,
			@StringMap Map<String, String> expected) {
			test(entries1, entries2, expected);
		}

		private void test(MutableMap<String, String> entries1,
			Map<String, String> entries2, Map<String, String> expected) {

			final var returned = entries1.add(entries2, (key, value1, value2) -> {
				if (value1.toLowerCase().equals(value1)) {
					return value2.equals("null") ? null : value2;
				} else {
					return value1;
				}
			});

			assertSame(entries1, returned,
				format("%s.add(%s, func)", entries1, entries2));
			assertEquals(expected, entries1,
				format("%s.add(%s, func)", entries1, entries2));
		}
	}

	@DisplayName(".remove(K)")
	@Nested
	class RemoveTests {
		@DisplayName("when map is not empty")
		@Tests({
			"when key is present, removes its entry;" +
				"[t:E, v:s, W:a, k:G]; W;" +
				"[t:E, v:s, k:G]",
			"when key is absent, does not nothing;" +
				"[t:E, v:s, W:a, k:G]; E;" +
				"[t:E, v:s, W:a, k:G]",
			"when key is null, does nothing;" +
				"[t:E, v:s, W:a, k:G]; null;" +
				"[t:E, v:s, W:a, k:G]"
		})
		void testNotEmpty(@StringMutableMap MutableMap<String, String> entries,
			String key, @StringMap Map<String, String> expected) {
			test(entries, key, expected);
		}

		@DisplayName("when map is empty")
		@Tests({
			"when key is not null, does nothing;" +
				"[]; N;" +
				"[]",
			"when key is null, does nothing;" +
				"[]; null;" +
				"[]"
		})
		void testEmpty(@StringMutableMap MutableMap<String, String> entries,
			String key, @StringMap Map<String, String> expected) {
			test(entries, key, expected);
		}

		private void test(MutableMap<String, String> entries, String key,
			Map<String, String> expected) {

			final var returned = entries.remove(key);
			assertSame(entries, returned,
				format("%s.remove(%s)", entries, key));
			assertEquals(expected, entries,
				format("%s.remove(%s)", entries, key));
		}
	}

	@Nested
	@DisplayName(".remove(K...)")
	class RemoveVarargsTests {
		@DisplayName("when map is not empty")
		@Tests({
			"when all keys are present, removes present entries;" +
				"[h:e, Q:c, k:R, l:p, L:g, m:e]; [L, k, h, Q];" +
				"[l:p, m:e]",
			"when some keys are present, removes present entries;" +
				"[h:e, Q:c, k:R, l:p, L:g, m:e]; [o, k, n, Q];" +
				"[h:e, l:p, L:g, m:e]",
			"when all entries are absent, does nothing;" +
				"[h:e, Q:c, k:R, l:p, L:g, m:e]; [o, k, n, Q];" +
				"[h:e, Q:c, k:R, l:p, L:g, m:e]; [o, k, n, Q];" +
				"[h:e, l:p, L:g, m:e]",
			"when some keys are null, removes non-null present entries;" +
				"[h:e, Q:c, k:R, l:p, L:g, m:e]; [o, null, null, k, Q];" +
				"[h:e, l:p, L:g, m:e]",
			"when all keys are null, does nothing;" +
				"[h:e, Q:c, k:R, l:p, L:g, m:e]; [null, null, null];" +
				"[h:e, Q:c, k:R, l:p, L:g, m:e]",
			"when argument array is empty, does nothing;" +
				"[h:e, Q:c, k:R, l:p, L:g, m:e]; [];" +
				"[h:e, Q:c, k:R, l:p, L:g, m:e]"
		})
		void testNotEmpty(@StringMutableMap MutableMap<String, String> entries,
			@StringArray String[] keys, @StringMap Map<String, String> expected) {
			test(entries, keys, expected);
		}

		@DisplayName("when map is empty")
		@Tests({
			"when all keys are not null, does nothing;" +
				"[]; [j, N, K, s];" +
				"[]",
			"when some keys are null, does nothing;" +
				"[]; [j, null, null, K, s];" +
				"[]",
			"when all keys are null, does nothing;" +
				"[]; [null, null, null];" +
				"[]",
			"when argument array is empty, does nothing;" +
				"[]; [];" +
				"[]"
		})
		void testEmpty(@StringMutableMap MutableMap<String, String> entries,
			@StringArray String[] keys, @StringMap Map<String, String> expected) {
			test(entries, keys, expected);
		}

		private void test(MutableMap<String, String> entries, String[] keys,
			Map<String, String> expected) {

			final var returned = entries.remove(keys);
			assertSame(entries, returned,
				format("%s.remove(%s)", entries,
					Arrays.toString(keys)));
			assertEquals(expected, entries,
				format("%s.remove(%s)", entries,
					Arrays.toString(keys)));
		}
	}

	@DisplayName(".remove(Collection<K>)")
	@Nested
	class RemoveCollectionTests {
		@DisplayName("when map is not empty")
		@Tests({
			"when all keys are present, removes present entries;" +
				"[h:R, b:S, F:s, c:L, P:p]; [P, b, c];" +
				"[h:R, F:s]",
			"when some keys are present, removes present entries;" +
				"[h:R, b:S, F:s, c:L, P:p]; [p, B, c, h];" +
				"[b:S, F:s, P:p]",
			"when all keys are absent, does nothing;" +
				"[h:R, b:S, F:s, c:L, P:p]; [H, R, s, c];" +
				"[h:R, b:S, F:s, c:L, P:p]",
			"when argument collection is empty, does nothing;" +
				"[h:R, b:S, F:s, c:L, P:p]; [];" +
				"[h:R, b:S, F:s, c:L, P:p]"
		})
		void testNotEmpty(@StringMutableMap MutableMap<String, String> entries,
			@StringCollection Collection<String> keys,
			@StringMap Map<String, String> expected) {
			test(entries, keys, expected);
		}

		@DisplayName("when map is empty")
		@Tests({
			"when argument collection is not empty, does nothing;" +
				"[]; [J, f, e, O, l];" +
				"[]",
			"when argument collection is empty, does nothing;" +
				"[]; [];" +
				"[]"
		})
		void testEmpty(@StringMutableMap MutableMap<String, String> entries,
			@StringCollection Collection<String> keys,
			@StringMap Map<String, String> expected) {
			test(entries, keys, expected);
		}

		void test(MutableMap<String, String> entries, Collection<String> keys,
			Map<String, String> expected) {

			final var returned = entries.remove(keys);
			assertSame(entries, returned,
				format("%s.remove(%s)", entries, keys));
			assertEquals(expected, entries,
				format("%s.remove(%s)", entries, keys));
		}
	}

	@DisplayName(".clear()")
	@Tests({
		"when map is not empty, removes all entries;" +
			"[h:R, f:W, k:N, l:S];" +
			"[]",
		"when map is empty, does nothing;" +
			"[];" +
			"[]"
	})
	void testClear(@StringMutableMap MutableMap<String, String> entries,
		@StringMap Map<String, String> expected) {

		final var returned = entries.clear();
		assertSame(entries, returned,
			format("%s.clear()", entries));
		assertEquals(expected, entries,
			format("%s.clear()", entries));
	}

	@DisplayName(".iterate(BiConsumer<K, V>)")
	@Tests({
		"returns itself;" +
			"[t:E, g:A, v:K, V:r]"
	})
	void testIterate(@StringMutableMap MutableMap<String, String> entries) {
		@SuppressWarnings("ResultOfMethodCallIgnored")
		final var returned = entries.iterate((key, value) -> key.toLowerCase());

		assertSame(returned, entries,
			format("%s.iterate(BiConsumer<K, V>)", entries));
	}

	@DisplayName(".filter(BiPredicate<K, V>)")
	@Tests({
		"returns a mutable map;" +
			"[f:E, s:q, c:S, x:l]"
	})
	void testFilter(@StringMutableMap MutableMap<String, String> entries) {
		final var filtered = entries.filter((key, value) -> value.isBlank());
		final var klass = filtered.getClass();

		assertEquals(MutableMap.class, klass,
			format("%s.filter(BiPredicate<K, V>)", entries));
		assertNotSame(entries, filtered,
			format("%s.filter(BiPredicate<K, V>)", entries));
	}

	@DisplayName(".convert(BiFunction<K, V, Entry<K, V>>)")
	@Tests({
		"returns a mutable map;" +
			"[g:E, r:t, k:c, s:Q]"
	})
	void testConvert(@StringMutableMap MutableMap<String, String> entries) {
		final var converted = entries.convert(Map.Entry::new);
		final var klass = converted.getClass();

		assertEquals(MutableMap.class, klass,
			format("%s.convert(BiFunction<K, V, Entry<K, V>>)", entries));
		assertNotSame(entries, converted,
			format("%s.convert(BiFunction<K, V, Entry<K, V>>)", entries));
	}

	@DisplayName(".toImmutable()")
	@Tests({
		"when map is not empty, returns entries in Map;" +
			"[g:M, t:Q, c:W, p:K, L:s];" +
			"[g:M, t:Q, c:W, p:K, L:s]",
		"when map is empty, returns empty Map;" +
			"[];" +
			"[]"
	})
	void testToImmutable(@StringMutableMap MutableMap<String, String> entries,
		@StringMap Map<String, String> expected) {

		final var immutable = entries.toImmutable();
		final var klass = immutable.getClass();

		assertEquals(klass, Map.class,
			format("%s.toImmutable()", entries));
		assertEquals(expected, entries,
			format("%s.toImmutable()", entries));
	}
}

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(StringMutableMap.Converter.class)
@interface StringMutableMap {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, MutableMap> {
		protected Converter() {
			super(String.class, MutableMap.class);
		}

		@Override
		protected MutableMap<String, String> convert(String s) throws ArgumentConversionException {
			final var entries = new StringEntryArray.Converter()
				.convert(s);

			return new MutableMap<>(entries);
		}
	}
}

// created on Sep 1, 2019
