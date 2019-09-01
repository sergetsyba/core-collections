package co.tsyba.core.collections;

import java.util.Iterator;
import java.util.function.BiPredicate;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Dec 21, 2018.
 */
class RobinHoodHashStore<E> implements Iterable<E> {
	Entry<E>[] storage;
	int entryCount;

	private int capacity;
	private final int probeDistanceLimit;

	RobinHoodHashStore(int capacity, int probeDistanceLimit) {
		// note: keeping an extra slot in storage allows avoiding index boundary checks
		// during probe iterations; since probing an empty slot stops probe iteration
		// anyway, a trailing extra empty slot will thus break probe iteration
		this.storage = new Entry[capacity + probeDistanceLimit + 1];
		this.capacity = capacity;
		this.entryCount = 0;
		this.probeDistanceLimit = probeDistanceLimit;
	}

	public RobinHoodHashStore(int capacity, double maximumLoadFactor) {
		if (capacity < 0) {
			throw new IllegalArgumentException("Cannot create hash store with negative capacity "
					+ capacity + ".");
		}
		if (maximumLoadFactor <= 0.0 || maximumLoadFactor >= 1.0) {
			throw new IllegalArgumentException("Cannot create hash store with load factor limit "
					+ maximumLoadFactor + ": value must be in range (0.0, 1.0).");
		}

		this.probeDistanceLimit = estimateProbeDistance(maximumLoadFactor);
		this.storage = new Entry[capacity + probeDistanceLimit + 1];
		this.capacity = capacity;
	}

	/**
	 * <pre>
	 * Sources:
	 *	1. P. Celis. "Robin Hood Hashing". University of Waterloo, 1986.
	 *	Chapter 2, Theorem 2.1.
	 * </pre>
	 */
	private static int estimateProbeDistance(double maximumLoadFactor) {
		final var probeDistance = Math.log(1.0 - maximumLoadFactor)
				/ -maximumLoadFactor;

		return (int) Math.round(probeDistance);
	}

	public RobinHoodHashStore(int capacity) {
		this(capacity, 0.75);
	}

	public RobinHoodHashStore(RobinHoodHashStore<E> store) {
		this(store.capacity, store.probeDistanceLimit);
		System.arraycopy(store.storage, 0, storage, 0, store.storage.length);
		this.entryCount = store.entryCount;
	}

	private void shiftEntriesRight(int index) {
		var storedEntry = storage[index];

		for (; storedEntry != null; index += 1) {
			final var swapEntry = storage[index + 1];
			storage[index + 1] = storedEntry;
			storedEntry = swapEntry;
		}
	}

	private int prepareInsertionSlot(Entry entry) {
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
		final var resizedStore = new RobinHoodHashStore<E>(capacity, probeDistanceLimit);

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

	public void insert(E item) {
		final var insertedEntry = new Entry<>(item);

		// keep expanding storage capacity until bucket for the new entry is not full
		var entryIndex = prepareInsertionSlot(insertedEntry);
		for (; entryIndex < 0; entryIndex = prepareInsertionSlot(insertedEntry)) {
			resizeStorage(capacity * 2);
		}

		storage[entryIndex] = insertedEntry;
		entryCount += 1;
	}

	public int find(Object entry) {
		final var entryIndex = entry.hashCode() % capacity;

		for (var probeIndex = entryIndex;; probeIndex += 1) {
			final var storedEntry = storage[probeIndex];

			if (storedEntry == null) {
				// probed an empty slot, store contains no such item
				return -1;
			}
			else if (storedEntry.item.equals(entry)) {
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

	public boolean remove(Object entry) {
		var entryIndex = find(entry);
		if (entryIndex < 0) {
			return false;
		}
		else {
			// shift the remainder of the entry cluster one position to the left
			shiftEntriesLeft(entryIndex);
			entryCount -= 1;

			return true;
		}
	}

	boolean storageMatches(BiPredicate<E, E> predicate, E... items) {
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

	boolean storageIs(E... items) {
		return storageMatches((item1, item2) -> item1 == item2, items);
	}

	boolean storageEquals(E... items) {
		return storageMatches((item1, item2) -> item1.equals(item2), items);
	}

	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			private int index = 0;

			@Override
			public boolean hasNext() {
				while (index < storage.length) {
					if (storage[index] != null) {
						return true;
					}

					index += 1;
				}

				return false;
			}

			@Override
			public E next() {
				final var item = storage[index].item;
				index += 1;

				return item;
			}
		};
	}

	@Override
	public int hashCode() {
		var hashCode = 1;
		for (var entry : storage) {
			if (entry != null) {
				hashCode = 31 * hashCode + entry.hashCode;
			}
		}

		return hashCode;
	}

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof RobinHoodHashStore)) {
			return false;
		}

		// ensure this store contains same number of entries 
		// as the specififed one
		final var store = (RobinHoodHashStore) object;
		if (entryCount != store.entryCount) {
			return false;
		}

		// ensure each entry from this store is present in 
		// the specified one
		for (var entry : storage) {
			if (entry == null) {
				continue;
			}
			if (store.find(entry.item) < 0) {
				return false;
			}
		}

		return true;
	}

	class Entry<T> {
		public final T item;
		public final int hashCode;

		public Entry(T item) {
			this.item = item;
			this.hashCode = item.hashCode();
		}
	}
}
