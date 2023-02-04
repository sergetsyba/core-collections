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
		@DisplayName("creates range")
		void createsRange() {
			final var range = new IndexRange(5, 8);
			assert 5 == range.start;
			assert 8 == range.end;
			assert 3 == range.length;
		}

		@Test
		@DisplayName("creates empty range when end is at start")
		void createsEmptyRangeWhenEndAtStart() {
			final var range = new IndexRange(5, 5);
			assert 5 == range.start;
			assert 5 == range.end;
			assert 0 == range.length;
		}

		@Test
		@DisplayName("fails when start is negative")
		void failsWhenStartNegative() {
			try {
				new IndexRange(-4, 12);
			} catch (IllegalArgumentException ignored) {
				return;
			}
			assert false;
		}

		@Test
		@DisplayName("fails when end is before start")
		void failsWhenEndBeforeStart() {
			try {
				new IndexRange(5, 2);
			} catch (IllegalArgumentException ignored) {
				return;
			}
			assert false;
		}
	}

	@Nested
	@DisplayName(".isEmpty()")
	class IsEmptyTests {
		@Test
		@DisplayName("when index range is empty, returns true")
		void returnsTrueWhenEmpty() {
			final var range = new IndexRange(0, 0);
			assert range.isEmpty();
		}

		@Test
		@DisplayName("when index range is note empty, returns false")
		void returnsFalseWhenNotEmpty() {
			final var range = new IndexRange(0, 10);
			assert !range.isEmpty();
		}
	}

	@Nested
	@DisplayName(".contains(int)")
	class ContainsIndexTests {
		private final IndexRange range = new IndexRange(3, 12);

		@Test
		@DisplayName("returns true when index is within range")
		void returnsTrueWhenIndexWithinRange() {
			assert range.contains(6);
		}

		@Test
		@DisplayName("returns false when index is before start")
		void returnsFalseWhenIndexBeforeStart() {
			assert !range.contains(1);
		}

		@Test
		@DisplayName("returns true when index is at start")
		void returnsTrueWhenIndexAtStart() {
			assert range.contains(3);
		}

		@Test
		@DisplayName("returns false when index is after end")
		void returnsFalseWhenIndexAfterEnd() {
			assert !range.contains(15);
		}

		@Test
		@DisplayName("returns false when index is at end")
		void returnsFalseWhenIndexAtEnd() {
			assert !range.contains(12);
		}
	}

	@Nested
	@DisplayName(".contains(IndexRange)")
	class ContainsIndexRangeTests {
		private final IndexRange range = new IndexRange(5, 18);

		@Test
		@DisplayName("returns true when range is within range")
		void returnsTrueWhenRangeWithinRange() {
			final var range2 = new IndexRange(7, 12);
			assert range.contains(range2);
		}

		@Test
		@DisplayName("returns false when range is outside range")
		void returnsFalseWhenRangeOutsideRange() {
			final var range2 = new IndexRange(2, 22);
			assert !range.contains(range2);
		}

		@Test
		@DisplayName("returns false when range starts before start")
		void returnsFalseWhenRangeStartsBeforeStarts() {
			final var range2 = new IndexRange(3, 12);
			assert !range.contains(range2);
		}

		@Test
		@DisplayName("returns true when range starts at start")
		void returnsTrueWhenRangeStartsAtStart() {
			final var range2 = new IndexRange(5, 12);
			assert range.contains(range2);
		}

		@Test
		@DisplayName("returns false when range ends after end")
		void returnsFalseWhenRangeEndsAfterEnd() {
			final var range2 = new IndexRange(7, 22);
			assert !range.contains(range2);
		}

		@Test
		@DisplayName("returns true when range ends at end")
		void returnsTrueWhenRangeEndsAtEnd() {
			final var range2 = new IndexRange(7, 18);
			assert range.contains(range2);
		}
	}

	@Nested
	@DisplayName(".equals(Object)")
	class EqualsTests {
		@Test
		@DisplayName("returns true when range equals")
		void returnsTrueWhenEqual() {
			final var range1 = new IndexRange(4, 9);
			final var range2 = new IndexRange(4, 9);

			assert range1.equals(range2);
		}

		@Test
		@DisplayName("returns false when range start differs")
		void returnsFalseWhenRangeStartDiffers() {
			final var range1 = new IndexRange(4, 9);
			final var range2 = new IndexRange(7, 9);

			assert !range1.equals(range2);
		}

		@Test
		@DisplayName("returns false when range end differs")
		void returnsFalseWhenRangeEndDiffers() {
			final var range1 = new IndexRange(4, 9);
			final var range2 = new IndexRange(4, 6);

			assert !range1.equals(range2);
		}
	}

	@Nested
	@DisplayName(".iterator()")
	class IteratorTests {
		@Test
		@DisplayName("returns iterator")
		void returnsIterator() {
			final var range = new IndexRange(3, 8);
			final var indexes = new int[range.length];
			var i = 0;

			for (var index : range) {
				indexes[i] = index;
				++i;
			}

			assert Arrays.equals(indexes, new int[]{
				3, 4, 5, 6, 7
			});
		}

		@Test
		@DisplayName("returns empty iterator when range is empty")
		void returnsEmptyIteratorWhenRangeEmpty() {
			final var range = new IndexRange(2, 2);
			final var indexes = new int[range.length];
			var i = 0;

			for (var index : range) {
				indexes[i] = index;
				++i;
			}

			assert Arrays.equals(indexes, new int[]{});
		}
	}
}
