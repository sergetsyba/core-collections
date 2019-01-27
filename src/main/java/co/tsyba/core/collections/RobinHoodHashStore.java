package co.tsyba.core.collections;

import java.util.function.BiPredicate;

/*
 * Created by Serge Tsyba <serge.tsyba@tsyba.com> on Dec 21, 2018.
 */
class RobinHoodHashStore<Item> {
	private Entry<Item>[] storage;
	private int capacity;
	private final int probeDistanceLimit;

	RobinHoodHashStore(int capacity, int probeDistanceLimit) {
		// note: keeping an extra slot in storage allows avoiding index boundary checks
		// during probe iterations; since probing an empty slot stops probe iteration
		// anyway, a trailing extra empty slot will thus break probe iteration
		this.storage = new Entry[capacity + probeDistanceLimit + 1];
		this.capacity = capacity;
		this.probeDistanceLimit = probeDistanceLimit;
	}

	public RobinHoodHashStore(int capacity, double loadFactorLimit) {
		if (capacity < 0) {
			throw new IllegalArgumentException("Cannot create a hash store with negative capacity: " + capacity + ".");
		}
		if (loadFactorLimit <= 0.0 || loadFactorLimit >= 1.0) {
			throw new IllegalArgumentException("Cannot create a hash store with load factor limit " + loadFactorLimit
					+ ": value must be in range (0.0, 1.0).");
		}

		// Source:
		// P. Celis. "Robin Hood Hashing". University of Waterloo, 1986. Chapter 2,
		// Theorem 2.1.
		final var estimatedProbeDistanceLimit = Math.log(1.0 - loadFactorLimit) / -loadFactorLimit;
		this.probeDistanceLimit = (int) Math.round(estimatedProbeDistanceLimit);

		this.storage = new Entry[capacity + probeDistanceLimit + 1];
		this.capacity = capacity;
	}

	public RobinHoodHashStore(int capacity) {
		this(capacity, 0.75);
	}

	private void shiftEntriesRight(int index) {
		var storedEntry = storage[index];

		for (; storedEntry != null; index += 1) {
			final var swapEntry = storage[index + 1];
			storage[index + 1] = storedEntry;
			storedEntry = swapEntry;
		}
	}

	public int prepareInsertionSlot(Entry entry) {
		final var entryIndex = entry.hashCode % capacity;
		final var probeIndexLimit = entryIndex + probeDistanceLimit;

		for (var probeIndex = entryIndex; probeIndex < probeIndexLimit; probeIndex += 1) {
			final var storedEntry = storage[probeIndex];

			if (storedEntry == null) {
				// probed an empty slot; place the new entry in it
				return probeIndex;
			}
			else if (storedEntry.item.equals(entry.item)) {
				// probed an equal entry; replace it with the new entry
				return probeIndex;
			}
			else if (storedEntry.hashCode % capacity > entryIndex) {
				// probed an entry with bucket index higher than that of the new entry
				// (i.e. an entry with lower probe distance, than that of the new entry);
				// shift the remainder of the entry cluster one position to the right
				// and place the entry into the freed slot
				shiftEntriesRight(probeIndex);
				return probeIndex;
			}
		}

		return -1;
	}

	private void resizeStorage(int capacity) {
		final var resizedStore = new RobinHoodHashStore<Item>(capacity, probeDistanceLimit);

		for (var storedEntry : storage) {
			if (storedEntry != null) {
				final var entryIndex = resizedStore.prepareInsertionSlot(storedEntry);
				if (entryIndex < 0) {
					// cannot find insertion index for a stored entry in resized store;
					// expand capacity of resized store and re-insert all entries into
					// expanded store
					resizeStorage(capacity * 2);
					return;
				}
				else {
					resizedStore.storage[entryIndex] = storedEntry;
				}
			}
		}

		this.storage = resizedStore.storage;
		this.capacity = resizedStore.capacity;
	}

	public void insert(Item item) {
		final var insertedEntry = new Entry<>(item);

		// keep expanding storage capacity until bucket for the new entry is not full
		var entryIndex = prepareInsertionSlot(insertedEntry);
		for (; entryIndex < 0; entryIndex = prepareInsertionSlot(insertedEntry)) {
			resizeStorage(capacity * 2);
		}

		storage[entryIndex] = insertedEntry;
	}

	public int find(Item item) {
		final var entry = new Entry<>(item);
		final var entryIndex = entry.hashCode % capacity;

		for (var probeIndex = entryIndex;; probeIndex += 1) {
			final var storedEntry = storage[probeIndex];

			if (storedEntry == null) {
				// probed an empty slot, store contains no such item
				return -1;
			}
			else if (storedEntry.item.equals(entry.item)) {
				// found the equal item
				return probeIndex;
			}
			else if (storedEntry.hashCode % capacity > entryIndex) {
				// probed an entry with bucket index higher than that of the new entry;
				// store contains no such item
				return -1;
			}
		}
	}

	private void shiftEntriesLeft(int index) {
		for (; storage[index] != null; index += 1) {
			storage[index] = storage[index + 1];
		}
	}

	public boolean remove(Item item) {
		var entryIndex = find(item);
		if (entryIndex < 0) {
			return false;
		}
		else {
			// shift the remainder of the entry cluster one position to the left
			shiftEntriesLeft(entryIndex);
			return true;
		}
	}

	boolean storageMatches(BiPredicate<Item, Item> predicate, Item... items) {
		if (items.length > capacity) {
			return false;
		}

		for (var index = 0; index < items.length; ++index) {
			final var storedEntry = storage[index];
			final var item = items[index];

			if (storedEntry == null) {
				if (item != null) {
					return false;
				}
			}
			else {
				if (!predicate.test(storedEntry.item, item)) {
					return false;
				}
			}
		}

		return true;
	}

	boolean storageIs(Item... items) {
		return storageMatches((item1, item2) -> item1 == item2, items);
	}

	boolean storageEquals(Item... items) {
		return storageMatches((item1, item2) -> item1.equals(item2), items);
	}

	private class Entry<Item> {
		public final Item item;
		public final int hashCode;

		public Entry(Item item) {
			this.item = item;
			this.hashCode = item.hashCode();
		}
	}
}
