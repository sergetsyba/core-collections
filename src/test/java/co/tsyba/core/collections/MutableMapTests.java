package co.tsyba.core.collections;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MutableMapTests {
	@Nested
	@DisplayName(".get(K, V)")
	class GetTests {
		@Nested
		@DisplayName("when map is not empty")
		class NotEmptyMapTests {
			private final Set<Map.Entry<String, Integer>> proto = new Set<>(
				new Map.Entry<>("F", 5),
				new Map.Entry<>("R", 3),
				new Map.Entry<>("g", 9));

			@Test
			@DisplayName("when key is present, does not store and returns stored value")
			void returnsStoredValueWhenKeyPresent() {
				final var entries = new MutableMap<>(proto);
				final var value = entries.get("R", 0);

				assert 3 == value;
				assert new Set<>(entries.store)
					.equals(proto);
			}

			@Test
			@DisplayName("when key is absent, stores and returns argument value")
			void returnsArgValueWhenKeyAbsent() {
				final var entries = new MutableMap<>(proto);
				final var value = entries.get("r", 0);

				final var expected = new MutableSet<>(proto)
					.add(new Map.Entry<>("r", 0));

				assert 0 == value;
				assert new Set<>(entries.store)
					.equals(expected);
			}

			@Test
			@DisplayName("when key is present and argument value is null, does not store and returns stored value")
			void returnsStoredValueWhenKeyPresentArgValueNull() {
				final var entries = new MutableMap<>(proto);
				final var value = entries.get("F", null);

				assert 5 == value;
				assert new Set<>(entries.store)
					.equals(proto);
			}

			@Test
			@DisplayName("when key is absent and argument value is null, does not store and returns null")
			void returnsNullWhenKeyAbsentArgValueNull() {
				final var entries = new MutableMap<>(proto);
				final var value = entries.get("X", null);

				assert null == value;
				assert new Set<>(entries.store)
					.equals(proto);
			}

			@Test
			@DisplayName("when key is null, does not store but returns argument value")
			void returnsArgValueWhenKeyNull() {
				final var entries = new MutableMap<>(proto);
				final var value = entries.get(null, 9);

				assert 9 == value;
				assert new Set<>(entries.store)
					.equals(proto);
			}

			@Test
			@DisplayName("when key and argument value are null, does not store and returns null")
			void returnsNullWhenKeyArgValueNull() {
				final var entries = new MutableMap<>(proto);
				final var value = entries.get(null, null);

				assert null == value;
				assert entries.store
					.equals(proto);
			}
		}

		@Nested
		@DisplayName("when map is empty")
		class EmptyMapTests {
			private final Set<Map.Entry<String, Integer>> proto = new Set<>();

			@Test
			@DisplayName("stores and returns argument value")
			void returnsArgValue() {
				final var entries = new MutableMap<>(proto);
				final var value = entries.get("F", 3);

				final var expected = new MutableSet<>(proto)
					.add(new Map.Entry<>("F", 3));

				assert 3 == value;
				assert new Set<>(entries.store)
					.equals(expected);
			}

			@Test
			@DisplayName("when key is null, does not store but returns argument value")
			void returnsArgValueWhenKeyNull() {
				final var entries = new MutableMap<>(proto);
				final var value = entries.get(null, 9);

				assert 9 == value;
				assert entries.store
					.equals(proto);
			}

			@Test
			@DisplayName("when argument value is null, does not store and returns null")
			void returnsNullWhenArgValueNull() {
				final var entries = new MutableMap<>(proto);
				final var value = entries.get("F", null);

				assert null == value;
				assert entries.store
					.equals(proto);
			}

			@Test
			@DisplayName("when key and argument value are null, does not store and returns null")
			void returnsNullWhenKeyArgValueNull() {
				final var entries = new MutableMap<>(proto);
				final var value = entries.get(null, null);

				assert null == value;
				assert entries.store
					.equals(proto);
			}
		}
	}

	@Nested
	@DisplayName(".set(K, V)")
	class SetTests {
		private final Set<Map.Entry<String, Integer>> proto = new Set<>(
			new Map.Entry<>("G", 7),
			new Map.Entry<>("x", 0));

		@Test
		@DisplayName("when key is absent, inserts new entry")
		void insertsEntryWhenKeyAbsent() {
			final var entries = new MutableMap<>(proto);
			final var returned = entries.set("B", 4);

			final var expected = new MutableSet<>(proto)
				.add(new Map.Entry<>("B", 4));

			assert entries == returned;
			assert new Set<>(entries.store)
				.equals(expected);
		}

		@Test
		@DisplayName("when key already present, replaces stored value")
		void replacesStoredValueWhenKeyPresent() {
			final var entries = new MutableMap<>(proto);
			final var returned = entries.set("x", 4);

			final var expected = new Set<>(
				new Map.Entry<>("G", 7),
				new Map.Entry<>("x", 4));

			assert entries == returned;
			assert new Set<>(entries.store)
				.equals(expected);
		}

		@Test
		@DisplayName("when key is null, does nothing")
		void doesNothingWhenKeyNull() {
			final var entries = new MutableMap<>(proto);
			final var returned = entries.set(null, 4);

			assert entries == returned;
			assert entries.store
				.equals(proto);
		}

		@Test
		@DisplayName("when value is null, does nothing")
		void doesNothingWhenValueNull() {
			final var entries = new MutableMap<>(proto);
			final var returned = entries.set("H", null);

			assert entries == returned;
			assert entries.store
				.equals(proto);
		}
	}

	@Nested
	@DisplayName(".add(Map<K, V>)")
	class AddMapTests {
		@Nested
		@DisplayName("when map is not empty")
		class NotEmptyMapTests {
			private final Set<Map.Entry<String, Integer>> proto = new Set<>(
				new Map.Entry<>("B", 0),
				new Map.Entry<>("J", 9),
				new Map.Entry<>("s", 4));

			@Test
			@DisplayName("when all keys are absent, adds entries")
			void addsEntriesWhenAllKeysAbsent() {
				final var entries1 = new MutableMap<>(proto);
				final var entries2 = new Map<>(
					new Map.Entry<>("N", 9),
					new Map.Entry<>("p", 1));

				final var returned = entries1.add(entries2);

				final var expected = new Set<>(
					new Map.Entry<>("B", 0),
					new Map.Entry<>("J", 9),
					new Map.Entry<>("s", 4),
					new Map.Entry<>("N", 9),
					new Map.Entry<>("p", 1));

				assert returned == entries1;
				assert new Set<>(entries1.store)
					.equals(expected);
			}

			@Test
			@DisplayName("when some keys are present, replaces their values")
			void replacesValuesWhenSomeKeysPresent() {
				final var entries1 = new MutableMap<>(proto);
				final var entries2 = new Map<>(
					new Map.Entry<>("B", 9),
					new Map.Entry<>("s", 1));

				final var returned = entries1.add(entries2);

				final var expected = new Set<>(
					new Map.Entry<>("B", 9),
					new Map.Entry<>("J", 9),
					new Map.Entry<>("s", 1));

				assert returned == entries1;
				assert new Set<>(entries1.store)
					.equals(expected);
			}

			@Test
			@DisplayName("when argument map is empty, does nothing")
			void doesNothingWhenArgMapEmpty() {
				final var entries1 = new MutableMap<>(proto);
				final var entries2 = new Map<String, Integer>();

				final var returned = entries1.add(entries2);

				assert returned == entries1;
				assert new Set<>(entries1.store)
					.equals(proto);
			}
		}

		@Nested
		@DisplayName("when map is empty")
		class EmptyMapTests {
			private final Set<Map.Entry<String, Integer>> proto = new Set<>();

			@Test
			@DisplayName("adds entries")
			void addsEntries() {
				final var entries1 = new MutableMap<>(proto);
				final var entries2 = new Map<>(
					new Map.Entry<>("K", 9),
					new Map.Entry<>("m", 8));

				final var returned = entries1.add(entries2);

				final var expected = new Set<>(
					new Map.Entry<>("K", 9),
					new Map.Entry<>("m", 8));

				assert returned == entries1;
				assert new Set<>(entries1.store)
					.equals(expected);
			}

			@Test
			@DisplayName("when argument map is empty, does nothing")
			void doesNothingWhenArgMapEmpty() {
				final var entries1 = new MutableMap<>(proto);
				final var entries2 = new Map<String, Integer>();

				final var returned = entries1.add(entries2);

				assert returned == entries1;
				assert new Set<>(entries1.store)
					.isEmpty();
			}
		}
	}

	@Nested
	@DisplayName(".add(Map<K, V>, BiFunction<V, V, V>)")
	class AddMapBiFunctionTests {
		@Nested
		@DisplayName("when map is not empty")
		class NotEmptyMapTests {
			private final Set<Map.Entry<String, Integer>> proto = new Set<>(
				new Map.Entry<>("V", 2),
				new Map.Entry<>("G", 1),
				new Map.Entry<>("e", 9),
				new Map.Entry<>("I", 7));

			@Test
			@DisplayName("when all keys are absent, adds entries")
			void addsEntriesWhenAllKeysAbsent() {
				final var entries1 = new MutableMap<>(proto);
				final var entries2 = new Map<>(
					new Map.Entry<>("g", 3),
					new Map.Entry<>("K", 4));

				final var keys = new MutableSet<String>();
				final var returned = entries1.add(entries2,
					(key, value1, value2) -> {
						keys.add(key);
						return value2;
					});

				final var expected = new Set<>(
					new Map.Entry<>("V", 2),
					new Map.Entry<>("G", 1),
					new Map.Entry<>("e", 9),
					new Map.Entry<>("I", 7),
					new Map.Entry<>("g", 3),
					new Map.Entry<>("K", 4));

				assert returned == entries1;
				assert keys.isEmpty();

				assert new Set<>(entries1.store)
					.equals(expected);
			}

			@Test
			@DisplayName("when some keys are present, resolves their values")
			void resolvesValuesWhenSomeKeysPresent() {
				final var entries1 = new MutableMap<>(proto);
				final var entries2 = new Map<>(
					new Map.Entry<>("G", 3),
					new Map.Entry<>("K", 4),
					new Map.Entry<>("I", 4));

				final var keys = new MutableSet<String>();
				final var returned = entries1.add(entries2,
					(key, value1, value2) -> {
						keys.add(key);
						return value2 + 2;
					});

				final var expected = new Set<>(
					new Map.Entry<>("V", 2),
					new Map.Entry<>("G", 5),
					new Map.Entry<>("e", 9),
					new Map.Entry<>("I", 6),
					new Map.Entry<>("K", 4));

				assert returned == entries1;
				assert new Set<>("G", "I")
					.equals(keys);

				assert new Set<>(entries1.store)
					.equals(expected);
			}

			@Test
			@DisplayName("when resolver returns null, preserves stored value")
			void preservesValueWhenResolverReturnsNull() {
				final var entries1 = new MutableMap<>(proto);
				final var entries2 = new Map<>(
					new Map.Entry<>("G", 3),
					new Map.Entry<>("K", 4),
					new Map.Entry<>("I", 4),
					new Map.Entry<>("e", 7));

				final var keys = new MutableSet<String>();
				final var returned = entries1.add(entries2,
					(key, value1, value2) -> {
						keys.add(key);
						return value2 > 3 ? null : value2;
					});

				final var expected = new Set<>(
					new Map.Entry<>("V", 2),
					new Map.Entry<>("G", 3),
					new Map.Entry<>("e", 9),
					new Map.Entry<>("I", 7),
					new Map.Entry<>("K", 4));

				assert returned == entries1;
				assert new Set<>("G", "I", "e")
					.equals(keys);

				assert new Set<>(entries1.store)
					.equals(expected);
			}

			@Test
			@DisplayName("when argument map is empty, does nothing")
			void doesNothingWhenArgMapEmpty() {
				final var entries1 = new MutableMap<>(proto);
				final var entries2 = new Map<String, Integer>();

				final var keys = new MutableSet<String>();
				final var returned = entries1.add(entries2,
					(key, value1, value2) -> {
						keys.add(key);
						return value2;
					});

				assert returned == entries1;
				assert keys.isEmpty();

				assert new Set<>(entries1.store)
					.equals(proto);
			}
		}

		@Nested
		@DisplayName("when map is empty")
		class EmptyMapTests {
			private final Set<Map.Entry<String, Integer>> proto = new Set<>();

			@Test
			@DisplayName("adds all entries")
			void addsAllEntries() {
				final var entries1 = new MutableMap<>(proto);
				final var entries2 = new Map<>(
					new Map.Entry<>("G", 3),
					new Map.Entry<>("e", 7));

				final var keys = new MutableSet<String>();
				final var returned = entries1.add(entries2,
					(key, value1, value2) -> {
						keys.add(key);
						return value2;
					});

				final var expected = new Set<>(
					new Map.Entry<>("G", 3),
					new Map.Entry<>("e", 7));

				assert returned == entries1;
				assert keys.isEmpty();

				assert new Set<>(entries1.store)
					.equals(expected);
			}

			@Test
			@DisplayName("when argument map is empty, does nothing")
			void doesNothingWhenArgMapEmpty() {
				final var entries1 = new MutableMap<>(proto);
				final var entries2 = new Map<String, Integer>();

				final var keys = new MutableSet<String>();
				final var returned = entries1.add(entries2,
					(key, value1, value2) -> {
						keys.add(key);
						return value2;
					});

				assert returned == entries1;
				assert keys.isEmpty();

				assert new Set<>(entries1.store)
					.equals(proto);
			}
		}
	}

	@Nested
	@DisplayName(".remove(K)")
	class RemoveTests {
		@Nested
		@DisplayName("when map is not empty")
		class NotEmptyMapTests {
			private final Set<Map.Entry<String, Integer>> proto = new Set<>(
				new Map.Entry<>("P", 5),
				new Map.Entry<>("l", 4),
				new Map.Entry<>("M", 3),
				new Map.Entry<>("F", 5));

			@Test
			@DisplayName("when key is present, removes its entry")
			void removesEntryWhenKeyPresent() {
				final var entries = new MutableMap<>(proto);
				final var returned = entries.remove("M");

				final var expected = new Set<>(
					new Map.Entry<>("P", 5),
					new Map.Entry<>("l", 4),
					new Map.Entry<>("F", 5));

				assert returned == entries;
				assert new Set<>(entries.store)
					.equals(expected);
			}

			@Test
			@DisplayName("when key is absent, does nothing")
			void doesNothingWhenKeyAbsent() {
				final var entries = new MutableMap<>(proto);
				final var returned = entries.remove("m");

				assert returned == entries;
				assert entries.store
					.equals(proto);
			}

			@Test
			@DisplayName("when key is null, does nothing")
			void doesNothingWhenKeyNull() {
				final var entries = new MutableMap<>(proto);
				final var returned = entries.remove((String) null);

				assert returned == entries;
				assert new Set<>(entries.store)
					.equals(proto);
			}
		}

		@Nested
		@DisplayName("when map is empty")
		class EmptyMapTests {
			private final Set<Map.Entry<String, Integer>> proto = new Set<>();

			@Test
			@DisplayName("does nothing")
			void doesNothing() {
				final var entries = new MutableMap<>(proto);
				final var returned = entries.remove("b");

				assert returned == entries;
				assert new Set<>(entries.store)
					.equals(proto);
			}

			@Test
			@DisplayName("when key is null, does nothing")
			void doesNothingWhenKeyNull() {
				final var entries = new MutableMap<String, Integer>();
				final var returned = entries.remove((String) null);

				assert returned == entries;
				assert new Set<>(entries.store)
					.equals(proto);
			}
		}
	}

	@SuppressWarnings("ConstantConditions")
	@Nested
	@DisplayName(".remove(K...)")
	class RemoveVarargsTests {
		@Nested
		@DisplayName("when map is not empty")
		class NotEmptyMapTests {
			private final Set<Map.Entry<String, Integer>> proto = new Set<>(
				new Map.Entry<>("P", 5),
				new Map.Entry<>("l", 4),
				new Map.Entry<>("M", 3),
				new Map.Entry<>("F", 5));

			@Test
			@DisplayName("when all keys are present, removes their entries")
			void removesEntriesWhenAllKeysPresent() {
				final var entries = new MutableMap<>(proto);
				final var returned = entries.remove("M", "P", "F");

				final var expected = new Set<>(
					new Map.Entry<>("l", 4));

				assert returned == entries;
				assert new Set<>(entries.store)
					.equals(expected);
			}

			@Test
			@DisplayName("when some keys are present, removes their entries")
			void removesEntriesWhenSomeKeysPresent() {
				final var entries = new MutableMap<>(proto);
				final var returned = entries.remove("M", "p", "F", "L");

				final var expected = new Set<>(
					new Map.Entry<>("P", 5),
					new Map.Entry<>("l", 4));

				assert returned == entries;
				assert new Set<>(entries.store)
					.equals(expected);
			}

			@Test
			@DisplayName("when no keys are present, does nothing")
			void doesNothingWhenNoKeysPresent() {
				final var entries = new MutableMap<>(proto);
				final var returned = entries.remove("V", "b", "L", "w");

				assert returned == entries;
				assert new Set<>(entries.store)
					.equals(proto);
			}

			@Test
			@DisplayName("when some keys are null, ignores them")
			void ignoresNullsWhenSomeKeysNull() {
				final var entries = new MutableMap<>(proto);
				final var returned = entries.remove("m", null, "P", null, null, "F");

				final var expected = new Set<>(
					new Map.Entry<>("l", 4),
					new Map.Entry<>("M", 3));

				assert returned == entries;
				assert new Set<>(entries.store)
					.equals(expected);
			}
		}

		@Nested
		@DisplayName("when map is empty")
		class EmptyMapTests {
			private final Set<Map.Entry<String, Integer>> proto = new Set<>();

			@Test
			@DisplayName("does nothing")
			void doesNothing() {
				final var entries = new MutableMap<>(proto);
				final var returned = entries.remove("b", "F", "w");

				assert returned == entries;
				assert new Set<>(entries.store)
					.equals(proto);
			}

			@Test
			@DisplayName("when some keys are null, does nothing")
			void doesNothingWhenSomeKeysNull() {
				final var entries = new MutableMap<>(proto);
				final var returned = entries.remove(null, "V", null, null, "Q");

				assert returned == entries;
				assert new Set<>(entries.store)
					.equals(proto);
			}
		}
	}

	@Nested
	@DisplayName(".remove(Collection<K>)")
	class RemoveCollectionTests {
		@Nested
		@DisplayName("when map is not empty")
		class NotEmptyMapTests {
			private final Set<Map.Entry<String, Integer>> proto = new Set<>(
				new Map.Entry<>("P", 5),
				new Map.Entry<>("l", 3),
				new Map.Entry<>("M", 4),
				new Map.Entry<>("F", 5));

			@Test
			@DisplayName("when all keys are present, removes entries")
			void removesEntriesWhenAllKeysPresent() {
				final var entries = new MutableMap<>(proto);
				final var returned = entries.remove(
					new Set<>("M", "P", "F"));

				final var expected = new Set<>(
					new Map.Entry<>("l", 4));

				assert returned == entries;
				assert new Set<>(entries.store)
					.equals(expected);
			}

			@Test
			@DisplayName("when some keys are present, removes entries")
			void removesEntriesWhenSomeKeysPresent() {
				final var entries = new MutableMap<>(proto);
				final var returned = entries.remove(
					new Set<>("M", "p", "F", "L"));

				final var expected = new Set<>(
					new Map.Entry<>("P", 5),
					new Map.Entry<>("l", 4));

				assert returned == entries;
				assert new Set<>(entries.store)
					.equals(expected);
			}

			@Test
			@DisplayName("when no keys are present, does nothing")
			void doesNothingWhenNoKeysPresent() {
				final var entries = new MutableMap<>(proto);
				final var returned = entries.remove(
					new Set<>("V", "b", "L", "w"));

				assert returned == entries;
				assert new Set<>(entries.store)
					.equals(proto);
			}
		}

		@Nested
		@DisplayName("when map is empty")
		class EmptyMapTests {
			private final Set<Map.Entry<String, Integer>> proto = new Set<>();

			@Test
			@DisplayName("does nothing")
			void doesNothing() {
				final var entries = new MutableMap<>(proto);
				final var returned = entries.remove(
					new Set<>("b", "F", "w"));

				assert returned == entries;
				assert new Set<>(entries.store)
					.equals(proto);
			}
		}
	}

	@Nested
	@DisplayName(".clear()")
	class ClearTests {
		@Test
		@DisplayName("when map is not empty, removes all entries")
		void removesAllEntriesWhenNotEmpty() {
			final var entries = new MutableMap<>(
				new Map.Entry<>("v", 5),
				new Map.Entry<>("v", 4),
				new Map.Entry<>("v", 2),
				new Map.Entry<>("v", 7));

			final var returned = entries.clear();

			assert returned == entries;
			assert new Set<>(entries.store)
				.isEmpty();
		}

		@Test
		@DisplayName("when map is empty, does nothing")
		void doesNothingWhenEmpty() {
			final var entries = new MutableMap<>();
			final var returned = entries.clear();

			assert returned == entries;
			assert new Set<>(entries.store)
				.isEmpty();
		}
	}

	@Nested
	@DisplayName(".toImmutable()")
	class ToImmutableTests {
		@Test
		@DisplayName("when map is not empty, returns immutable copy")
		void returnsMapWhenNotEmpty() {
			final var proto = new Set<>(
				new Map.Entry<>("G", 0),
				new Map.Entry<>("x", 3),
				new Map.Entry<>("q", 1));

			final var entries1 = new MutableMap<>(proto);
			final var entries2 = entries1.toImmutable();

			assert Map.class == entries2.getClass();
			assert new Set<>(entries2.store)
				.equals(proto);
		}

		@Test
		@DisplayName("when map is empty, returns empty immutable copy")
		void returnsEmptyMapWhenEmpty() {
			final var entries1 = new MutableMap<String, Integer>();
			final var entries2 = entries1.toImmutable();

			assert Map.class == entries2.getClass();
			assert new Set<>(entries2.store)
				.isEmpty();
		}
	}
}

// created on Sep 1, 2019
