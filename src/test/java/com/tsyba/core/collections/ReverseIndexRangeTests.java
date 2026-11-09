package com.tsyba.core.collections;

import com.tsyba.core.collections.converter.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Optional;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

class ReverseIndexRangeTests {
	@DisplayName(".parse(String)")
	@Nested
	class ParseTests {
		@DisplayName("when range is formatted properly")
		@Tests({
			"when range is not empty, returns parsed range;" +
				"(9, 4];" +
				"4; 9;",
			"when range is empty, returns parsed empty range;" +
				"(0, 0];" +
				"0; 0;"
		})
		void testFormattedProperly(String string, int expectedStart, int expectedEnd) {
			try {
				final var range = ReverseIndexRange.parse(string);
				final var expected = new ReverseIndexRange(expectedStart, expectedEnd);
				assertEquals(expected, range,
					format("parse(%s)", string));
			} catch (IllegalArgumentException exception) {
				fail(format("parse(%s)", string),
					exception);
			}
		}

		@DisplayName("when range is formatted improperly")
		@Tests({
			"when string does not contain range, fails;" +
				"not a range",
			"when string does not contain start and end, fails;" +
				"(,]",
			"when string does not contain start, fails;" +
				"(, 3]",
			"when start is not an integer, fails;" +
				"(x, 5]",
			"when string does not contain end, fails;" +
				"(0,]",
			"when end is not an integer, fails;" +
				"(6, y]",
			"when start is after end, fails;" +
				"(4, 9]"
		})
		void testFormattedImproperly(String string) {
			assertThrows(Exception.class,
				() -> ReverseIndexRange.parse(string),
				format("parse(%s)", string));
		}
	}

	@DisplayName(".getFirst()")
	@Nested
	class GetFirstTests {
		@DisplayName("\uD83C\uDFCC")
		@Tests({
			"when range is not empty, returns first item;" +
				"(11, 4];" +
				"10",
			"when range is empty, returns empty optional;" +
				"(0, 0];" +
				"null"
		})
		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		void test(@ReverseIntRange ReverseIndexRange range,
			@IntOptional Optional<Integer> expected) {

			final var first = range.getFirst();
			assertEquals(expected, first,
				format("%s.getFirst()", range));
		}
	}

	@DisplayName(".getLast()")
	@Nested
	class GetLastTests {
		@DisplayName("\uD83E\uDD73")
		@Tests({
			"when range is not empty, returns last item;" +
				"(14, 7];" +
				"7",
			"when range is empty, returns empty optional;" +
				"(0, 0];" +
				"null"
		})
		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		void test(@ReverseIntRange ReverseIndexRange range,
			@IntOptional Optional<Integer> expected) {

			final var last = range.getLast();
			assertEquals(expected, last,
				format("%s.getLast()", range));
		}
	}

	@DisplayName(".get(int)")
	@Nested
	class GetIntTests {
		@DisplayName("when range is not empty")
		@Tests({
			"when index is before valid index range start, fails;" +
				"(12, 5]; -2;" +
				"null; -2 ∉ [0, 7)",
			"when index is 0, returns first item;" +
				"(12, 5]; 0;" +
				"11; null",
			"when index is within valid index range, returns item at index;" +
				"(12, 5]; 4;" +
				"7; null",
			"when index is last valid index, returns last item;" +
				"(12, 5]; 6;" +
				"5; null",
			"when index is at valid index range end, fails;" +
				"(12, 5]; 7;" +
				"null; 7 ∉ [0, 7)",
			"when index is after valid index range end, fails;" +
				"(12, 5]; 11;" +
				"null; 11 ∉ [0, 7)"
		})
		void testRangeNotEmpty(@ReverseIntRange ReverseIndexRange range, int index,
			Integer expected1, @IndexRangeException Exception expected2) {
			test(range, index, expected1, expected2);
		}

		@DisplayName("when range is empty")
		@Tests({
			"when index is before valid index range start, fails;" +
				"(0, 0]; -1;" +
				"null; -1 ∉ [0, 0)",
			"when index is 0, fails;" +
				"(0, 0]; 0;" +
				"null; 0 ∉ [0, 0)",
			"when index is after valid index range end, fails;" +
				"(0, 0]; 1;" +
				"null; 1 ∉ [0, 0)"
		})
		void testRangeEmpty(@ReverseIntRange ReverseIndexRange range, int index,
			Integer expected1, @IndexRangeException Exception expected2) {
			test(range, index, expected1, expected2);
		}

		private void test(ReverseIndexRange range, int index, Integer expected1,
			Exception expected2) {

			try {
				final var item = range.get(index);
				assertEquals(expected1, item,
					format("%s.get(%d)", range, index));
			} catch (IndexNotInRangeException exception) {
				assertEquals(expected2, exception,
					format("%s.get(%d)", range, index));
			}
		}
	}

	@DisplayName(".getPrefix(int)")
	@Nested
	class GetPrefixTests {
		@DisplayName("when range is not empty")
		@Tests({
			"when index is before valid index range start, fails;" +
				"(12, 5]; -3;" +
				"null; -3 ∉ [0, 7)",
			"when index is 0, returns empty prefix;" +
				"(12, 5]; 0;" +
				"(0, 0]; null",
			"when index is within valid index range, returns prefix;" +
				"(12, 5]; 4;" +
				"(12, 8]; null",
			"when index is last valid index, returns prefix;" +
				"(12, 5]; 6;" +
				"(12, 6]; null",
			"when index is at valid index range end, fails;" +
				"(12, 5]; 7;" +
				"null; 7 ∉ [0, 7)",
			"when index is after valid index range end, fails;" +
				"(12, 5]; 10;" +
				"null; 10 ∉ [0, 7)"
		})
		void testRangeNotEmpty(@ReverseIntRange ReverseIndexRange range, int index,
			@ReverseIntRange ReverseIndexRange expected1,
			@IndexRangeException Exception expected2) {
			test(range, index, expected1, expected2);
		}

		@DisplayName("when range is empty")
		@Tests({
			"when index is before valid index range start, fails;" +
				"(0, 0]; -2;" +
				"null; -2 ∉ [0, 0)",
			"when index is 0, fails;" +
				"(0, 0]; 0;" +
				"null; 0 ∉ [0, 0)",
			"when index is after valid index range end, fails;" +
				"(0, 0]; 1;" +
				"null; 1 ∉ [0, 0)"
		})
		void testRangeEmpty(@ReverseIntRange ReverseIndexRange range, int index,
			@ReverseIntRange ReverseIndexRange expected1,
			@IndexRangeException Exception expected2) {
			test(range, index, expected1, expected2);
		}

		private void test(ReverseIndexRange range, int index, IndexRange expected1,
			Exception expected2) {

			try {
				final var prefix = range.getPrefix(index);
				assertEquals(expected1, prefix,
					format("%s.getPrefix(%d)", range, index));
			} catch (IndexNotInRangeException exception) {
				assertEquals(expected2, exception,
					format("%s.getPrefix(%d)", range, index));
			}
		}
	}

	@DisplayName(".getSuffix(int)")
	@Nested
	class GetSuffixTests {
		@DisplayName("when range is not empty")
		@Tests({
			"when index is before valid index range start, fails;" +
				"(12, 5]; -1;" +
				"null; -1 ∉ [0, 7)",
			"when index is 0, returns suffix;" +
				"(12, 5]; 0;" +
				"(12, 5]; null",
			"when index is within valid index range, returns suffix;" +
				"(12, 5]; 3;" +
				"(9, 5]; null",
			"when index is last valid index, returns suffix;" +
				"(12, 5]; 6;" +
				"(6, 5]; null",
			"when index is at valid index range end, fails;" +
				"(12, 5]; 7;" +
				"null; 7 ∉ [0, 7)",
			"when index is after valid index range end, fails;" +
				"(12, 5]; 9;" +
				"null; 9 ∉ [0, 7)"
		})
		void testRangeNotEmpty(@ReverseIntRange ReverseIndexRange range, int index,
			@ReverseIntRange ReverseIndexRange expected1,
			@IndexRangeException Exception expected2) {
			test(range, index, expected1, expected2);
		}

		@DisplayName("when range is empty")
		@Tests({
			"when index is before valid index range start, fails;" +
				"(0, 0]; -1;" +
				"null; -1 ∉ [0, 0)",
			"when index is 0, fails;" +
				"(0, 0]; 0;" +
				"null; 0 ∉ [0, 0)",
			"when index is after valid index range end, fails;" +
				"(0, 0]; 2;" +
				"null; 2 ∉ [0, 0)"
		})
		void testRangeEmpty(@ReverseIntRange ReverseIndexRange range, int index,
			@ReverseIntRange ReverseIndexRange expected1,
			@IndexRangeException Exception expected2) {
			test(range, index, expected1, expected2);
		}

		private void test(ReverseIndexRange range, int index, IndexRange expected1,
			Exception expected2) {

			try {
				final var suffix = range.getSuffix(index);
				assertEquals(expected1, suffix,
					format("%s.getSuffix(%d)", range, index));
			} catch (IndexNotInRangeException exception) {
				assertEquals(expected2, exception,
					format("%s.getSuffix(%d)", range, index));
			}
		}
	}

	@DisplayName(".get(IndexRange)")
	@Nested
	class GetIndexRangeTests {
		@DisplayName("when range is not empty")
		@Tests({
			"when argument range starts at 0 and ends within valid index range, returns items in range;" +
				"(12, 5]; [0, 3);" +
				"(12, 9]; null",
			"when argument  range is within valid index range, returns items in range;" +
				"(12, 5]; [2, 5);" +
				"(10, 7]; null",
			"when argument range equals valid index range, returns items in range;" +
				"(12, 5]; [0, 7);" +
				"(12, 5]; null",
			"when argument  range starts within and ends after valid index range end, fails;" +
				"(12, 5]; [4, 9);" +
				"null; [4, 9) ⊈ [0, 7)",
			"when argument range is after valid index range, fails;" +
				"(12, 5]; [8, 10);" +
				"null; [8, 10) ⊈ [0, 7)",
			"when argument range is empty, returns empty range;" +
				"(12, 5]; [0, 0);" +
				"(0, 0]; null"
		})
		void testRangeNotEmpty(@ReverseIntRange ReverseIndexRange range,
			@IntRange IndexRange range2, @ReverseIntRange ReverseIndexRange expected1,
			@IndexRangeException Exception expected2) {
			test(range, range2, expected1, expected2);
		}

		@DisplayName("when range is empty")
		@Tests({
			"when argument range is not empty, fails;" +
				"(0, 0]; [0, 1);" +
				"null; [0, 1) ⊈ [0, 0)",
			"when argument range is empty, returns empty range;" +
				"(0, 0]; [0, 0);" +
				"(0, 0]; null"
		})
		void testRangeEmpty(@ReverseIntRange ReverseIndexRange range,
			@IntRange IndexRange range2, @ReverseIntRange ReverseIndexRange expected1,
			@IndexRangeException Exception expected2) {
			test(range, range2, expected1, expected2);
		}

		private void test(ReverseIndexRange range, IndexRange range2, IndexRange expected1,
			Exception expected2) {

			try {
				final var sub = range.get(range2);
				assertEquals(expected1, sub,
					format("%s.get(%s)", range, range2));
			} catch (IndexRangeNotInRangeException exception) {
				assertEquals(expected2, exception,
					format("%s.get(%s)", range, range2));
			}
		}
	}

	@DisplayName(".contains(int)")
	@Nested
	class ContainsTests {
		@DisplayName("when range is not empty")
		@Tests({
			"when item is after range start, returns false;" +
				"(9, 3]; 11;" +
				"false",
			"when item is at range start, returns false;" +
				"(9, 3]; 9;" +
				"false",
			"when item is within range, returns true;" +
				"(9, 3]; 6;" +
				"true",
			"when item is at range end, returns true;" +
				"(9, 3]; 3;" +
				"true",
			"when item is before range end, returns false;" +
				"(9, 3]; 2;" +
				"false",
		})
		void testRangeNotEmpty(@ReverseIntRange ReverseIndexRange range, int item,
			boolean expected) {
			test(range, item, expected);
		}

		@DisplayName("when range is empty")
		@Tests({
			"when item is after range start, returns false;" +
				"(0, 0]; 2;" +
				"false",
			"when item is at range start, returns false;" +
				"(0, 0]; 0;" +
				"false",
			"when item is before range end, returns false;" +
				"(0, 0]; -1;" +
				"false"
		})
		void testRangeEmpty(@ReverseIntRange ReverseIndexRange range, int item,
			boolean expected) {
			test(range, item, expected);
		}

		private void test(ReverseIndexRange range, int item, boolean expected) {
			final var contains = range.contains(item);
			assertEquals(expected, contains,
				format("%s.contains(%d)", range, item));
		}
	}

	@DisplayName(".findFirst(Integer)")
	@Nested
	class FindFirstTests {
		@DisplayName("when range is not empty")
		@Tests({
			"when item is after range start, returns empty optional;" +
				"(11, 4]; 14;" +
				"null",
			"when item is at range start, returns empty optional;" +
				"(11, 4]; 11;" +
				"null",
			"when item is first after range start, returns 0;" +
				"(11, 4]; 10;" +
				"0",
			"when item is within range, returns its index;" +
				"(11, 4]; 7;" +
				"3",
			"when item is at range end, returns its index;" +
				"(11, 4]; 4;" +
				"6",
			"when item is before range end, returns empty optional;" +
				"(11, 4]; 2;" +
				"null",
			"when item is null, returns empty optional;" +
				"(11, 4]; null;" +
				"null"
		})
		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		void testRangeNotEmpty(@ReverseIntRange ReverseIndexRange range, Integer item,
			@IntOptional Optional<Integer> expected) {

			final var index = range.findFirst(item);
			assertEquals(expected, index,
				format("%s.findFirst(%s)", range, item));
		}

		@DisplayName("when range is empty")
		@Tests({
			"when item is after range start, returns empty optional;" +
				"(0, 0]; 2;" +
				"null",
			"when item is at range start, returns empty optional;" +
				"(0, 0]; 0;" +
				"null",
			"when item is before range end, returns empty optional;" +
				"(0, 0]; -3;" +
				"null",
			"when item is null, returns empty optional;" +
				"(0, 0]; null;" +
				"null"
		})
		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		void testRangeEmpty(@ReverseIntRange ReverseIndexRange range, Integer item,
			@IntOptional Optional<Integer> expected) {

			final var index = range.findFirst(item);
			assertEquals(expected, index,
				format("%s.findFirst(%s)", range, item));
		}
	}

	@DisplayName(".findAll(Integer)")
	@Nested
	class FindAllTests {
		@DisplayName("\uD83D\uDC15")
		@Tests({
			"when item is within range, returns its reversed index;" +
				"(11, 4]; 6;" +
				"[4]",
			"when item is first index after range start, returns 0;" +
				"(11, 4]; 10;" +
				"[0]",
			"when item is before range end, returns empty sequence;" +
				"(11, 4]; 1;" +
				"[]",
			"when item is null, returns empty sequence;" +
				"(11, 4]; null;" +
				"[]"
		})
		void test(@ReverseIntRange ReverseIndexRange range, Integer item,
			@IntList List<Integer> expected) {

			final var indexes = range.findAll(item);
			assertEquals(expected, indexes,
				format("%s.findAll(%s)", range, item));
		}
	}

	@DisplayName(".iterator()")
	@Nested
	class IteratorTests {
		@DisplayName("\uD83C\uDF02")
		@Tests({
			"when range is not empty, iterates its indexes;" +
				"(10, 3];" +
				"[9, 8, 7, 6, 5, 4, 3]",
			"when range is empty, does nothing;" +
				"(0, 0];" +
				"[]"
		})
		void test(@ReverseIntRange ReverseIndexRange range, @IntArray Integer[] expected) {
			final var iterated = new ArrayList<Integer>();
			range.iterator()
				.forEachRemaining(iterated::add);

			assertArrayEquals(expected, iterated.toArray(new Integer[0]),
				format("%s.iterator()", range));
		}
	}

	@DisplayName(".iterator(int)")
	@Nested
	class IteratorIntTests {
		@DisplayName("when range is not empty")
		@Tests({
			"when index is before valid index range start, fails;" +
				"(10, 3]; -2;" +
				"null; -2 ∉ [0, 7)",
			"when index is 0, iterates items;" +
				"(10, 3]; 0;" +
				"[9, 8, 7, 6, 5, 4, 3]; null",
			"when index is within valid index range, iterates items starting at index;" +
				"(10, 3]; 3;" +
				"[6, 5, 4, 3]; null",
			"when index is last valid index, iterates last item;" +
				"(10, 3]; 6;" +
				"[3]; null",
			"when index is at valid index range end, fails;" +
				"(10, 3]; 7;" +
				"null; 7 ∉ [0, 7)",
			"when index is after valid index range end, fails;" +
				"(10, 3]; 11;" +
				"null; 11 ∉ [0, 7)"
		})
		void testRangeNotEmpty(@ReverseIntRange ReverseIndexRange range, int index,
			@IntList List<Integer> expected1,
			@IndexRangeException Exception expected2) {
			test(range, index, expected1, expected2);
		}

		@DisplayName("when range is empty")
		@Tests({
			"when index is before valid index range start, fails;" +
				"(0, 0]; -1;" +
				"null; -1 ∉ [0, 0)",
			"when index is 0, fails;" +
				"(0, 0]; 0;" +
				"null; 0 ∉ [0, 0)",
			"when index is after valid index range end, fails;" +
				"(0, 0]; 2;" +
				"null; 2 ∉ [0, 0)"
		})
		void testRangeEmpty(@ReverseIntRange ReverseIndexRange range, int index,
			@IntList List<Integer> expected1,
			@IndexRangeException Exception expected2) {
			test(range, index, expected1, expected2);
		}

		private void test(ReverseIndexRange range, int index, List<Integer> expected1,
			Exception expected2) {

			try {
				final var iterated = new MutableList<Integer>();
				range.iterator(index)
					.forEachRemaining(iterated::append);

				assertEquals(expected1, iterated,
					format("%s.iterator(%d)", range, index));
			} catch (IndexNotInRangeException exception) {
				assertEquals(expected2, exception,
					format("%s.iterator(%d)", range, index));
			}
		}
	}

	@DisplayName(".reverse()")
	@Nested
	class ReverseTests {
		@DisplayName("\uD83C\uDF19")
		@Tests({
			"when range is not empty, returns forward range;" +
				"(12, 5];" +
				"[5, 12)",
			"when range is empty, returns empty forward range;" +
				"(0, 0];" +
				"[0, 0)"
		})
		void test(@ReverseIntRange ReverseIndexRange range, @IntRange IndexRange expected) {
			final var reversed = range.reverse();
			assertEquals(expected, reversed,
				format("%s.reverse()", range));
		}
	}

	@DisplayName(".equals(Object)")
	@Nested
	class EqualsTests {
		@DisplayName("when range is not empty")
		@Tests({
			"when argument range equals, returns true;" +
				"(12, 5]; (12, 5];" +
				"true",
			"when argument range start differs, returns false;" +
				"(12, 5]; (12, 6];" +
				"false",
			"when argument range end differs, returns false;" +
				"(12, 5]; (11, 5];" +
				"false",
			"when argument range is null, returns false;" +
				"(12, 5]; null;" +
				"false"
		})
		void testRangeNotEmpty(@ReverseIntRange ReverseIndexRange range1,
			@ReverseIntRange ReverseIndexRange range2,
			boolean expected) {
			test(range1, range2, expected);
		}

		@DisplayName("when range is empty")
		@Tests({
			"when argument range is empty, returns true;" +
				"(0, 0]; (0, 0];" +
				"true",
			"when argument range is not empty, returns false;" +
				"(0, 0]; (5, 1];" +
				"false",
			"when argument range is null, returns false;" +
				"(0, 0]; null;" +
				"false"
		})
		void testRangeEmpty(@ReverseIntRange ReverseIndexRange range1,
			@ReverseIntRange ReverseIndexRange range2,
			boolean expected) {
			test(range1, range2, expected);
		}

		private void test(ReverseIndexRange range, IndexRange range2, boolean expected) {
			final var equals = range.equals(range2);
			assertEquals(expected, equals,
				format("%s.equals(%s)", range, range2));
		}
	}

	@DisplayName(".toString()")
	@Nested
	class ToStringTests {
		@DisplayName("\uD83C\uDF14")
		@Tests({
			"when range is not empty, returns string representation;" +
				"(12, 5];" +
				"(12, 5]",
			"when range is empty, returns (0, 0];" +
				"(0, 0];" +
				"(0, 0]"
		})
		void test(@ReverseIntRange ReverseIndexRange range, String expected) {
			final var string = range.toString();
			assertEquals(expected, string,
				format("%s.toString()", range));
		}
	}
}

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(ReverseIntRange.Converter.class)
@interface ReverseIntRange {
	class Converter extends TypedArgumentConverter<String, ReverseIndexRange> {
		protected Converter() {
			super(String.class, ReverseIndexRange.class);
		}

		@Override
		protected ReverseIndexRange convert(String string) throws ArgumentConversionException {
			return string == null
				? null
				: ReverseIndexRange.parse(string);
		}
	}
}
