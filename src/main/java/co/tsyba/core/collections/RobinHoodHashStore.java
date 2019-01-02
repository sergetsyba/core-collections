package co.tsyba.core.collections;

/*
 * Created by Serge Tsyba <serge.tsyba@tsyba.com> on Dec 21, 2018.
 */
class RobinHoodHashStore<Item> {
	private Entry<Item>[] storage;
	private int capacity;
	private int itemCount;

	private final double maximumLoadFactor;
	private final int maximumItemCount;

	public RobinHoodHashStore(int capacity, double maximumLoadFactor) {
		// note: keeping an extra slot in storage allows avoiding index boundary checks
		// during probe iterations; since probing an empty slot stops probe iteration
		// anyway, a trailing extra empty slot will thus break probe iteration
		this.storage = new Entry[capacity + 1];
		this.capacity = capacity;

		this.maximumLoadFactor = maximumLoadFactor;
		this.maximumItemCount = (int) Math.floor(capacity * maximumLoadFactor);
	}

	public RobinHoodHashStore(int capacity) {
		this(capacity, 0.75);
	}

	private void expandCapacity() {
		final var expandedStore = new RobinHoodHashStore<Item>(capacity * 2, maximumLoadFactor);
		for (Entry<Item> entry : storage) {
			if (entry != null) {
				expandedStore.unsafeAdd(entry);
			}
		}

		storage = expandedStore.storage;
		capacity = expandedStore.capacity;
		itemCount = expandedStore.itemCount;
	}

	private void unsafeAdd(Entry entry) {
		final var bucketIndex = entry.hashCode % capacity;

		var index = bucketIndex;
		for (;; index += 1) {
			final var storedEntry = storage[index];

			if (storedEntry == null) {
				// probed an empty slot, place the new entry in it
				storage[index] = entry;
				return;
			}
			else if (storedEntry.item.equals(entry.item)) {
				// probed an equal entry, replace it with the new entry
				storage[index] = entry;
				return;
			}
			else if (storedEntry.hashCode % capacity > bucketIndex) {
				// probed an entry with bucket index higher than that of the new entry
				// (i.e. probed an entry with lower probe distance, than that of
				// the new entry)
				break;
			}
		}

		// shift the remainder of the entry cluster one position to the right and place
		// the new entry into the freed slot
		for (; entry != null; index += 1) {
			final var swapEntry = storage[index];
			storage[index] = entry;
			entry = swapEntry;
		}
	}

	public void add(Item item) {
		// when the last slot in starage is filled, adding another item, which hashes
		// to the last slot, is no longer possible; expand storage and re-fill exntries
		// until the last slot is empty
		// todo: consider putting probe distance limit
		while (storage[capacity - 1] != null
				|| itemCount >= maximumItemCount) {

			expandCapacity();
		}

		final var entry = new Entry<>(item);
		unsafeAdd(entry);
		itemCount += 1;
	}

	public int find(Item item) {
		final var entry = new Entry<>(item);
		final var bucketIndex = entry.hashCode % capacity;

		for (var index = bucketIndex;; index += 1) {
			final var storedEntry = storage[index];

			if (storedEntry == null) {
				// probed an empty slot, store contains no such item
				return -1;
			}
			else if (storedEntry.item.equals(entry.item)) {
				// found the equal item
				return index;
			}
			else if (storedEntry.hashCode % capacity > bucketIndex) {
				// probed an entry with bucket index higher than that of the new entry;
				// store contains no such item
				return -1;
			}
		}
	}

	public boolean remove(Item item) {
		var index = find(item);
		if (index < 0) {
			return false;
		}
		else {
			// shift the remainder of the entry cluster one position to the left
			for (; storage[index] != null; index += 1) {
				storage[index] = storage[index + 1];
			}

			itemCount -= 1;
			return true;
		}
	}

	boolean storageEquals(Item... items) {
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
