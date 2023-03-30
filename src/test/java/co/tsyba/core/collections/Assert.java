package co.tsyba.core.collections;

import java.util.Arrays;
import java.util.Optional;

final class Assert {
	private Assert() {
	}

	static void assertThrows(ThrowingRunnable runnable, Throwable expected) {
		try {
			runnable.run();
		} catch (Throwable actual) {
			assert expected.equals(actual) :
				String.format("Incorrect throwable." +
						"\n\texpected: %s" +
						"\n\tactual: %s",
					expected, actual);

			return;
		}

		assert false : String.format("Finished without throwing." +
				"\n\texpected: %s",
			expected);
	}

	static void assertThrows(ThrowingRunnable runnable, Class<? extends Throwable> expected) {
		try {
			runnable.run();
		} catch (Throwable throwable) {
			final var actual = throwable.getClass();
			assert expected.equals(actual) :
				String.format("Incorrect throwable." +
						"\n\texpected: %s" +
						"\n\tactual: %s",
					expected, actual);

			return;
		}

		assert false : String.format("Finished without throwing." +
				"\n\texpected: %s",
			expected.getName());
	}

	static <T> void assertCapacity(List<T> actual, int expected) {
		assert expected == actual.store.items.length :
			String.format("Incorrect list capacity." +
					"\n\texpected: %d" +
					"\n\tactual: %d",
				expected,
				actual.store.items.length);
	}

	static <T> void assertEquals(Optional<T> actual, T expected) {
		assert actual.isPresent() :
			String.format("Optional value differs." +
					"\n\texpected: %s" +
					"\n\tactual:   %s",
				expected, "<empty>");

		final var actual2 = actual.get();
		assert actual2.equals(expected) :
			String.format("Optional value differs." +
					"\n\texpected: %s" +
					"\n\tactual:   %s",
				expected, actual2);
	}

	static <T> void assertEmpty(Optional<T> actual) {
		if (actual.isPresent()) {
			final var actual2 = actual.get();
			assert false :
				String.format("Optional is not empty." +
						"\n\texpected: %s" +
						"\n\tactual:   %s",
					"<empty>", actual2);
		}
	}

	static <T> void assertEmpty(List<T> actual) {
		for (var item : actual.store.items) {
			assert item == null
				: String.format("List it not empty." +
					"\n\texpected: []" +
					"\n\tactual:   %s",
				actual);
		}
	}

	static <T> void assertEquals(List<T> actual, T[] expected) {
		final var message = String.format("Lists are not equal." +
				"\n\texpected: %s" +
				"\n\tactual:   %s",
			Arrays.toString(expected), actual);

		final var itemCount = actual.getCount();
		assert itemCount == expected.length
			: message;

		var index = 0;
		for (; index < expected.length; ++index) {
			assert expected[index].equals(actual.store.items[index])
				: message;
		}
		for (; index < actual.store.items.length; ++index) {
			assert actual.store.items[index] == null
				: message;
		}
	}

	static <T> void assertEqualsIgnoringOrder(List<T> actual, T[] expected) {
		final var count = actual.getCount();
		assert count == expected.length :
			String.format("Lists contains different items." +
					"\n\texpected: %s" +
					"\n\tactual: %s",
				Arrays.toString(expected), actual);

		for (var item : expected) {
			assert actual.contains(item) :
				String.format("Lists contains different items." +
						"\n\texpected: %s" +
						"\n\tactual: %s",
					Arrays.toString(expected), actual);
		}
	}

	static <T> void assertNotEquals(List<T> actual, T[] expected) {
		var index = 0;
		for (; index < expected.length; ++index) {
			if (!expected[index].equals(actual.store.items[index])) {
				return;
			}
		}
		for (; index < actual.store.items.length; ++index) {
			if (actual.store.items[index] != null) {
				return;
			}
		}

		assert false :
			String.format("Lists are equal." +
					"\n\texpected: %s" +
					"\n\tactual: %s",
				Arrays.toString(expected), actual);
	}

	static <T> void assertEquals(T actual, T expected) {
		assert expected.equals(actual) :
			String.format("Values are not equal." +
					"\n\texpected: %s" +
					"\n\tactual:   %s",
				actual, expected);
	}
}

interface ThrowingRunnable {
	void run() throws Exception;
}
