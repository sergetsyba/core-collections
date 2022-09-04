package co.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class IndexRangeTests {
	@Nested
	@DisplayName("new IndexRange(int, int)")
	class IndexRangeConstructorTests {
		@Test
		@DisplayName("creates index range")
		void createsIndexRange() {
			final var range = new IndexRange(4, 9);
			assert range.start == 4;
			assert range.end == 9;
			assert range.length == 6;
		}

		@Test
		@DisplayName("fails when start index is negative")
		void failsWhenStartIndexIsNegative() {
			try {
				new IndexRange(-2, 4);
			} catch (IllegalArgumentException ignored) {
				return;
			}
			assert false;
		}

		@Test
		@DisplayName("fails when end index is negative")
		void failsWhenEndIndexIsNegative() {
			try {
				new IndexRange(2, -4);
			} catch (IllegalArgumentException ignored) {
				return;
			}
			assert false;
		}

		@Test
		@DisplayName("fails when start index is equal end index")
		void failsWhenStartIndexEqualsEndIndex() {
			try {
				new IndexRange(3, 3);
			} catch (IllegalArgumentException ignored) {
				return;
			}
			assert false;
		}

		@Test
		@DisplayName("fails when start index is larger than end index")
		void failsWhenStartIndexAfterEndIndex() {
			try {
				new IndexRange(3, 2);
			} catch (IllegalArgumentException ignored) {
				return;
			}
			assert false;
		}
	}

	@Nested
	@DisplayName(".contains(int)")
	class ContainsIndexTests {
		private final IndexRange range = new IndexRange(3, 12);

		@Test
		@DisplayName("returns false when index before range start")
		void returnsFalseWhenIndexBeforeRange() {
			assert !range.contains(2);
		}

		@Test
		@DisplayName("returns true when index at range start")
		void returnsTrueWhenIndexAtRangeStart() {
			assert range.contains(3);
		}

		@Test
		@DisplayName("returns true when index in range")
		void returnsTrueWhenIndexInRange() {
			assert range.contains(5);
		}

		@Test
		@DisplayName("returns true when index at range end")
		void returnsTrueWhenIndexAtRangeEnd() {
			assert range.contains(11);
		}

		@Test
		@DisplayName("returns false when index after range end")
		void returnsFalseWhenIndexAfterRange() {
			assert !range.contains(12);
		}
	}

	@Nested
	@DisplayName(".contains(IndexRange)")
	class ContainsIndexRangeTests {
		private final IndexRange range = new IndexRange(5, 18);

		@Test
		@DisplayName("returns false when the specified range before range")
		void returnsFalseWhenRangeBeforeRange() {
			final var range2 = new IndexRange(0, 3);
			assert !range.contains(range2);
		}

		@Test
		@DisplayName("returns false when the specified range start before range start")
		void returnsFalseWhenRangeStartBeforeRangeStart() {
			final var range2 = new IndexRange(0, 7);
			assert !range.contains(range2);
		}

		@Test
		@DisplayName("returns true when the specified range start at range start")
		void returnsTrueWhenRangeStartAtRangeStart() {
			final var range2 = new IndexRange(5, 12);
			assert range.contains(range2);
		}

		@Test
		@DisplayName("returns true when the specified range within range")
		void returnsTrueWhenRangeWithinRange() {
			final var range2 = new IndexRange(7, 12);
			assert range.contains(range2);
		}

		@Test
		@DisplayName("returns true when the specified range equals range")
		void returnsTrueWhenRangeEqualsRange() {
			final var range2 = new IndexRange(5, 18);
			assert range.contains(range2);
		}

		@Test
		@DisplayName("returns true when the specified range at range end")
		void returnsTrueWhenRangeAtRangeEnd() {
			final var range2 = new IndexRange(14, 18);
			assert range.contains(range2);
		}

		@Test
		@DisplayName("returns false when the specified range end after range end")
		void returnsFalseWhenRangeEndAfterRangeEnd() {
			final var range2 = new IndexRange(7, 19);
			assert !range.contains(range2);
		}

		@Test
		@DisplayName("returns false when the specified range after range")
		void returnsFalseWhenRangeAfterRange() {
			final var range2 = new IndexRange(19, 22);
			assert !range.contains(range2);
		}
	}

	@Nested
	@DisplayName(".iterator()")
	class IteratorTests {
		private final IndexRange range = new IndexRange(3, 8);

		@Test
		@DisplayName("returns iterator")
		void returnsIterator() {
			final var indexes = new int[range.length];
			var i = 0;

			for (var index : range) {
				indexes[i] = index;
				++i;
			}

			final var expected = new int[]{3, 4, 5, 6, 7, 8};
			assert Arrays.equals(indexes, expected);
		}
	}
}
