package co.tsyba.core.collections;

import java.util.Arrays;

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

	static <T> void assertEquals(List<T> actual, T[] expected) {
		var index = 0;
		for (; index < expected.length; ++index) {
			assert expected[index].equals(actual.store.items[index]) :
				String.format("Lists are not equal." +
						"\n\texpected: %s" +
						"\n\tactual: %s",
					Arrays.toString(expected), actual);
		}
		for (; index < actual.store.items.length; ++index) {
			assert actual.store.items[index] == null :
				String.format("Lists are not equal." +
						"\n\texpected: %s" +
						"\n\tactual: %s",
					Arrays.toString(expected), actual);
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
}

interface ThrowingRunnable {
	void run() throws Exception;
}
