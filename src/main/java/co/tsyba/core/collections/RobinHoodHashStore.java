package co.tsyba.core.collections;

/*
 * Created by Serge Tsyba <serge.tsyba@tsyba.com> on Dec 21, 2018.
 */
class RobinHoodHashStore<Item> {
	private Entry<Item>[] storage;
	private int capacity;
	private int itemCount;

	private final double loadFactorLimit;
	private final int probeDistanceLimit;
	private int itemCountLimit;

	RobinHoodHashStore(int capacity, double loadFactorLimit, int probeDistanceLimit) {
		if (capacity < 0) {
			throw new IllegalArgumentException("Cannot create a hash store with negative capacity: " + capacity + ".");
		}
		if (loadFactorLimit <= 0.0 || loadFactorLimit >= 1.0) {
			throw new IllegalArgumentException("Cannot create a hash store with load factor " + loadFactorLimit + ": value must be in range (0.0, 1.0).");
		}

		// note: keeping an extra slot in storage allows avoiding index boundary checks
		// during probe iterations; since probing an empty slot stops probe iteration
		// anyway, a trailing extra empty slot will thus break probe iteration
		this.storage = new Entry[capacity + probeDistanceLimit + 1];
		this.capacity = capacity;
		this.itemCount = 0;

		this.loadFactorLimit = loadFactorLimit;
		this.probeDistanceLimit = probeDistanceLimit;
		this.itemCountLimit = Math.min(capacity,
				(int) Math.round(capacity * loadFactorLimit));
	}

	public RobinHoodHashStore(int capacity, double maximumLoadFactor) {
		this(capacity, maximumLoadFactor,
				// todo: find correct probe distance limit estimate
				(int) Math.round(Math.log(capacity) / Math.log(2.0)));
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
		final var resizedStore = new RobinHoodHashStore<Item>(capacity, loadFactorLimit);

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
		this.itemCountLimit = resizedStore.itemCountLimit;
	}

	public void insert(Item item) {
		if (itemCount >= itemCountLimit) {
			// reached maximum fill factor; expand storage capacity
			resizeStorage(capacity * 2);
		}

		final var insertedEntry = new Entry<>(item);
		var entryIndex = prepareInsertionSlot(insertedEntry);

		// keep expanding storage capacity until bucket for the new entry is not full
		while (entryIndex < 0) {
			resizeStorage(capacity * 2);
			entryIndex = prepareInsertionSlot(insertedEntry);
		}

		storage[entryIndex] = insertedEntry;
		itemCount += 1;
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
			itemCount -= 1;

			return true;
		}
	}

	boolean storageEquals(Item... items) {
		if (items.length > capacity) {
			return false;
		}

		for (var index = 0; index < items.length; index += 1) {
			final var item = items[index];
			final var storedEntry = storage[index];

			if (storedEntry == null) {
				if (item != null) {
					return false;
				}
			}
			else {
				if (!item.equals(storedEntry.item)) {
					return false;
				}
			}
		}

		return true;
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
