package co.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class IndexRangeTests {
	@Nested
	@DisplayName("IndexRange(int, int)")
	class ConstructorWithBoundsTests {
		@Test
		@DisplayName("creates index range")
		void createsRange() {
			final var range = new IndexRange(5, 8);
			assert 5 == range.start;
			assert 8 == range.end;
			assert 3 == range.length;
		}

		@Test
		@DisplayName("when end is at start, creates empty range")
		void createsEmptyRangeWhenEndAtStart() {
			final var range = new IndexRange(5, 5);
			assert 5 == range.start;
			assert 5 == range.end;
			assert 0 == range.length;
		}

		@Test
		@DisplayName("when start is negative, fails")
		void failsWhenStartNegative() {
			try {
				new IndexRange(-4, 12);
			} catch (IllegalArgumentException ignored) {
				return;
			}
			assert false;
		}

		@Test
		@DisplayName("when end is before start, fails")
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
	@DisplayName("IndexRange()")
	class ConstructorTests {
		@Test
		@DisplayName("creates empty index range")
		void createsEmptyRange() {
			final var range = new IndexRange();
			assert 0 == range.start;
			assert 0 == range.end;
			assert 0 == range.length;
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
		@DisplayName("when index is within range, returns true")
		void returnsTrueWhenIndexWithinRange() {
			assert range.contains(6);
		}

		@Test
		@DisplayName("when index is before start, returns false")
		void returnsFalseWhenIndexBeforeStart() {
			assert !range.contains(1);
		}

		@Test
		@DisplayName("when index is at start, returns true")
		void returnsTrueWhenIndexAtStart() {
			assert range.contains(3);
		}

		@Test
		@DisplayName("when index is after end, returns false")
		void returnsFalseWhenIndexAfterEnd() {
			assert !range.contains(15);
		}

		@Test
		@DisplayName("when index is at end, returns false")
		void returnsFalseWhenIndexAtEnd() {
			assert !range.contains(12);
		}
	}

	@Nested
	@DisplayName(".contains(IndexRange)")
	class ContainsIndexRangeTests {
		private final IndexRange range = new IndexRange(5, 18);

		@Test
		@DisplayName("when range is within range, returns true")
		void returnsTrueWhenRangeWithinRange() {
			final var range2 = new IndexRange(7, 12);
			assert range.contains(range2);
		}

		@Test
		@DisplayName("when range is outside range, returns false")
		void returnsFalseWhenRangeOutsideRange() {
			final var range2 = new IndexRange(2, 22);
			assert !range.contains(range2);
		}

		@Test
		@DisplayName("when range starts before start, returns false")
		void returnsFalseWhenRangeStartsBeforeStarts() {
			final var range2 = new IndexRange(3, 12);
			assert !range.contains(range2);
		}

		@Test
		@DisplayName("when range starts at start, returns true")
		void returnsTrueWhenRangeStartsAtStart() {
			final var range2 = new IndexRange(5, 12);
			assert range.contains(range2);
		}

		@Test
		@DisplayName("when range ends after end, returns false")
		void returnsFalseWhenRangeEndsAfterEnd() {
			final var range2 = new IndexRange(7, 22);
			assert !range.contains(range2);
		}

		@Test
		@DisplayName("when range ends at end, returns true")
		void returnsTrueWhenRangeEndsAtEnd() {
			final var range2 = new IndexRange(7, 18);
			assert range.contains(range2);
		}
	}

	@Nested
	@DisplayName(".equals(Object)")
	class EqualsTests {
		@Test
		@DisplayName("when range equals, returns true")
		void returnsTrueWhenEqual() {
			final var range1 = new IndexRange(4, 9);
			final var range2 = new IndexRange(4, 9);

			assert range1.equals(range2);
		}

		@Test
		@DisplayName("when range start differs, returns false")
		void returnsFalseWhenRangeStartDiffers() {
			final var range1 = new IndexRange(4, 9);
			final var range2 = new IndexRange(7, 9);

			assert !range1.equals(range2);
		}

		@Test
		@DisplayName("when range end differs, returns false")
		void returnsFalseWhenRangeEndDiffers() {
			final var range1 = new IndexRange(4, 9);
			final var range2 = new IndexRange(4, 6);

			assert !range1.equals(range2);
		}
	}

	@Nested
	@DisplayName(".toString()")
	class ToStringTests {
		@Test
		@DisplayName("when index range is not empty, returns string")
		void returnsStringWhenNotEmpty() {
			final var range = new IndexRange(5, 17);
			final var string = range.toString();

			assert "[5, 17)"
				.equals(string);
		}

		@Test
		@DisplayName("when index range is empty, returns string")
		void returnsStringWhenEmpty() {
			final var range = new IndexRange(0, 0);
			final var string = range.toString();

			assert "[0, 0)"
				.equals(string);
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
		@DisplayName("when range is empty, returns empty iterator")
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
