package co.tsyba.core.collections;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

final class Assertions {
	private Assertions() {
	}

	private static <T> String format(String message, T expected, T actual) {
		return String.format("%s" +
				"\n\texpected: %s" +
				"\n\tactual:   %s",
			message, expected, actual);
	}

	private static <T> String format(String message, T expected) {
		return String.format("%s" +
				"\n\texpected: %s",
			message, expected);
	}

	static <T> void assertIs(T actual, T expected) {
		assert actual == expected :
			format("Instance differs from expectation.",
				expected, actual);
	}

	static <T> void assertIsNot(T actual, T expected) {
		assert actual != expected :
			format("Instance equals expectation.",
				expected, actual);
	}

	static void assertEquals(int actual, int expected, String message) {
		assert expected == actual :
			format(message, expected, actual);
	}

	static <T> void assertEquals(T actual, T expected, String message) {
		assert expected.equals(actual) :
			format(message, expected, actual);
	}

	static <T> void assertEquals(T actual, T expected) {
		assertEquals(actual, expected,
			"Value differs from expectation.");
	}

	static <T> void assertNotEquals(T actual, T expected) {
		assert !expected.equals(actual) :
			format("Value equals expectation.",
				expected, actual);
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	static <T> void assertEquals(Optional<T> actual, T expected) {
		assert Optional.of(expected).equals(actual) :
			format("Optional value differs from expectation.",
				expected, actual);
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	static <T> void assertEmpty(Optional<T> actual) {
		final var expected = Optional.empty();
		assertEquals(actual, expected);
	}

	static <T> void assertEquals(T[] actual, T[] expected, String message) {
		assert Arrays.equals(actual, expected) :
			format(message,
				Arrays.toString(expected),
				Arrays.toString(actual));
	}

	static <T> void assertEquals(T[] actual, T[] expected) {
		assertEquals(actual, expected,
			"Array differs from expectation.");
	}

	static void assertEquals(ContiguousArrayStore actual, Object[] expected) {
		assertEquals(actual.items, expected,
			"Store differs from expectation.");
	}

	static <T> void assertEquals(List<T> actual, T[] expected) {
		assertEquals(actual.store.items, expected,
			"List differs from expectation.");
	}

	static <T> void assertEquals(MutableList<T> actual, T[] expected) {
		final var length = Math.min(actual.store.itemCount, expected.length);
		final var actual2 = Arrays.copyOf(actual.store.items, length);

		assertEquals(actual2, expected,
			"List differs from expectation.");
	}

	static <T> void assertEquals(Iterable<T> actual, T[] expected) {
		final var message = String.format("Lists are not equal." +
				"\n\texpected: %s" +
				"\n\tactual:   %s",
			Arrays.toString(expected), actual);

		final var iterator = actual.iterator();
		for (var item1 : expected) {
			assert iterator.hasNext()
				: message;

			final var item2 = iterator.next();
			assert Objects.equals(item1, item2)
				: message;
		}

		assert !iterator.hasNext()
			: message;
	}

	static void assertThrows(ThrowingRunnable runnable, Throwable expected) {
		try {
			runnable.run();
		} catch (Throwable actual) {
			assert expected.equals(actual) :
				format("Throwable differs from expectation.",
					expected, actual);

			return;
		}

		assert false :
			format("Expected throwable was not thrown.",
				expected);
	}

	static void assertThrows(ThrowingRunnable runnable, Class<? extends Throwable> expected) {
		try {
			runnable.run();
		} catch (Throwable throwable) {
			final var actual = throwable.getClass();
			assert expected.equals(actual) :
				format("Throwable differs from expectation.",
					expected, actual);

			return;
		}

		assert false :
			format("Expected throwable was not thrown.",
				expected.getName());
	}
}

interface ThrowingRunnable {
	void run() throws Exception;
}
