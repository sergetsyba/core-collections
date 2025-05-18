package com.tsyba.core.collections;

import com.tsyba.core.collections.converter.IntList;
import com.tsyba.core.collections.converter.IntOptional;
import com.tsyba.core.collections.converter.StringList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class IndexRangeTests {
	@DisplayName("IndexRange(int, int)")
	@Nested
	class NewWithStartEndTests {
		@DisplayName("\uD83D\uDC1D")
		@Tests({
			"when start is before end, creates range;" +
				"5; 8;" +
				"[5, 8)",
			"when start is at end, creates empty range;" +
				"6; 6;" +
				"[0, 0)",
			"when start is after end, fails;" +
				"5; 2;" +
				"null",
			"when start is negative, fails;" +
				"-4; 4;" +
				"null"
		})
		void test(int start, int end, @IntRange IndexRange expected) {
			try {
				final var range = new IndexRange(start, end);
				assertEquals(expected, range,
					format("new IndexRange(%d, %d)", start, end));
			} catch (IllegalArgumentException exception) {
				if (expected != null) {
					throw exception;
				}
			}
		}
	}

	@DisplayName("IndexRange()")
	@Nested
	class NewTests {
		@DisplayName("\uD83D\uDDFD")
		@Tests({
			"creates empty index range;" +
				"[0, 0)"
		})
		void test(@IntRange IndexRange expected) {
			final var range = new IndexRange();
			assertEquals(expected, range,
				"new IndexRange()");
		}
	}

	@DisplayName(".getCount()")
	@Nested
	class GetCountTests {
		@DisplayName("\uD83C\uDFDC")
		@Tests({
			"when range is not empty, returns length;" +
				"[9, 16);" +
				"7",
			"when range is empty, returns 0;" +
				"[0, 0);" +
				"0"
		})
		void test(@IntRange IndexRange range, int expected) {
			final var count = range.getCount();
			assertEquals(expected, count,
				format("%s.isEmpty()", range));
		}
	}

	@DisplayName(".getIndexRange()")
	@Nested
	class GetIndexRangeTests {
		@DisplayName("\uD83C\uDF37")
		@Tests({
			"when index range is not empty, returns its index range;" +
				"[4, 9);" +
				"[0, 5)",
			"when index range is empty, returns empty range;" +
				"[0, 0);" +
				"[0, 0)"
		})
		void test(@IntRange IndexRange range, @IntRange IndexRange expected) {
			final var returned = range.getIndexRange();
			assertEquals(expected, returned,
				format("%s.getIndexRange()", range));
		}
	}

	@DisplayName(".contains(Integer)")
	@Nested
	class ContainsTests {
		@DisplayName("when range is not empty")
		@Tests({
			"when index is before range start, returns false;" +
				"[2, 9); 0;" +
				"false",
			"when index is at range start, returns true;" +
				"[6, 8); 6;" +
				"true",
			"when index is within range, returns true;" +
				"[4, 9); 6;" +
				"true",
			"when index is at range end, returns false;" +
				"[3, 8); 8;" +
				"false",
			"when index is after range end, returns false;" +
				"[4, 8); 12;" +
				"false",
			"when index is null, returns false;" +
				"[4, 8); null;" +
				"false"
		})
		void testNotEmpty(@IntRange IndexRange range, Integer index, boolean expected) {
			test(range, index, expected);
		}

		@DisplayName("when range is empty")
		@Tests({
			"when index is before range start, returns false;" +
				"[0, 0); -3;" +
				"false",
			"when index is at range start, returns false;" +
				"[0, 0); 0;" +
				"false",
			"when index is after range end, returns false;" +
				"[0, 0); 1;" +
				"false",
			"when index is null, returns false;" +
				"[0, 0); null;" +
				"false"
		})
		void testEmpty(@IntRange IndexRange range, Integer index, boolean expected) {
			test(range, index, expected);
		}

		private void test(IndexRange range, Integer index, boolean expected) {
			final var contains = range.contains(index);
			assertEquals(expected, contains,
				format("%s.contains(%d)", range, index));
		}
	}

	@DisplayName(".contains(IndexRange)")
	@Nested
	class ContainsIndexRange {
		@DisplayName("when range is not empty")
		@Tests({
			"when argument range starts and ends before range start, returns false;" +
				"[6, 9); [1, 4);" +
				"false",
			"when argument range starts before range start and ends within range, returns false;" +
				"[6, 9); [4, 7);" +
				"false",
			"when argument range starts at range start and ends within range, returns true;" +
				"[6, 9); [6, 8);" +
				"true",
			"when argument range coincides with range, returns true;" +
				"[6, 9); [6, 9);" +
				"true",
			"when argument range starts within range ends after range end, returns false;" +
				"[6, 9); [7, 11);" +
				"false",
			"when argument range starts and ends after range end, returns false;" +
				"[6, 9); [12, 17);" +
				"false",
			"when argument range is empty, returns true;" +
				"[6, 9); [0, 0);" +
				"true"
		})
		void testNotEmpty(@IntRange IndexRange range1, @IntRange IndexRange range2,
			boolean expected) {
			test(range1, range2, expected);
		}

		@DisplayName("when range is empty")
		@Tests({
			"when argument range is not empty, returns false;" +
				"[0, 0); [6, 9);" +
				"false",
			"when argument range is empty, returns true;" +
				"[0, 0); [0, 0);" +
				"true"
		})
		void testEmpty(@IntRange IndexRange range1, @IntRange IndexRange range2,
			boolean expected) {
			test(range1, range2, expected);
		}

		private void test(IndexRange range1, IndexRange range2, boolean expected) {
			final var contains = range1.contains(range2);
			assertEquals(expected, contains,
				format("%s.contains(%s)", range1, range2));
		}
	}

	@DisplayName(".getFirst()")
	@Nested
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	class GetFirstTests {
		@DisplayName("\uD83C\uDFC0")
		@Tests({
			"when range is not empty, returns start index;" +
				"[4, 7);" +
				"4",
			"when range is empty, returns empty optional;" +
				"[0, 0);" +
				"null"
		})
		void test(@IntRange IndexRange range, @IntOptional Optional<Integer> expected) {
			final var first = range.getFirst();
			assertEquals(expected, first,
				format("%s.getFirst()", range));
		}
	}

	@DisplayName(".getLast()")
	@Nested
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	class GetLastTests {
		@DisplayName("\uD83C\uDF81")
		@Tests({
			"when range is not empty, returns index before end;" +
				"[4, 7);" +
				"6",
			"when range is empty, returns empty optional;" +
				"[0, 0);" +
				"null"
		})
		void test(@IntRange IndexRange range, @IntOptional Optional<Integer> expected) {
			final var first = range.getLast();
			assertEquals(expected, first,
				format("%s.getLast()", range));
		}
	}


	@DisplayName(".get(Integer)")
	@Nested
	class GetAtTests {
		@DisplayName("when range is not empty")
		@Tests({
			"when index is before valid index range start, fails;" +
				"[4, 8); -4;" +
				"null; -4 ∉ [0, 4)",
			"when index is at valid index range start, returns index;" +
				"[4, 8); 0;" +
				"4; null",
			"when index is within valid index range, returns index;" +
				"[4, 8); 2;" +
				"6; null",
			"when index is at valid index range end, fails;" +
				"[4, 8); 4;" +
				"null; 4 ∉ [0, 4)",
			"when index is after valid index range end, fails;" +
				"[4, 8); 9;" +
				"null; 9 ∉ [0, 4)"
		})
		void testNotEmpty(@IntRange IndexRange range, int index, Integer expected1,
			@IndexRangeException Exception expected2) {
			test(range, index, expected1, expected2);
		}

		@DisplayName("when range is empty")
		@Tests({
			"when index is before valid index range start, fails;" +
				"[0, 0); -1;" +
				"null; -1 ∉ [0, 0)",
			"when index is at valid index range start, fails;" +
				"[0, 0); 0;" +
				"null; 0 ∉ [0, 0)",
			"when index is after valid index range end, fails;" +
				"[0, 0); 1;" +
				"null; 1 ∉ [0, 0)",
		})
		void testEmpty(@IntRange IndexRange range, int index, Integer expected1,
			@IndexRangeException Exception expected2) {
			test(range, index, expected1, expected2);
		}

		private void test(IndexRange range, int index, Integer expected1,
			Exception expected2) {

			try {
				final var value = range.get(index);
				assertEquals(expected1, value,
					format("%s.get(%d)", range, index));
			} catch (Exception exception) {
				if (expected2 == null) {
					throw exception;
				} else {
					assertEquals(expected2, exception,
						format("%s.get(%d)", range, index));
				}
			}
		}
	}

	@DisplayName(".getPrefix(int)")
	@Nested
	class GetPrefixTests {
		@DisplayName("when range is not empty")
		@Tests({
			"when index is before valid index range start, fails;" +
				"[2, 9); -1;" +
				"null; -1 ∉ [0, 7)",
			"when index is at valid index range start, returns empty range;" +
				"[2, 9); 0;" +
				"[0, 0); null",
			"when index is within valid index range, returns prefix;" +
				"[2, 9); 4;" +
				"[2, 6); null",
			"when index is at valid index range end, fails;" +
				"[2, 9); 7;" +
				"null; 7 ∉ [0, 7)",
			"when index is after valid index range end, fails;" +
				"[2, 9); 12;" +
				"null; 12 ∉ [0, 7)",
		})
		void testNotEmpty(@IntRange IndexRange range, int index,
			@IntRange IndexRange expected1, @IndexRangeException Exception expected2) {
			test(range, index, expected1, expected2);
		}

		@DisplayName("when range is empty")
		@Tests({
			"when index is before valid index range start, fails;" +
				"[0, 0); -5;" +
				"null; -5 ∉ [0, 0)",
			"when index is at valid index range start, fails;" +
				"[0, 0); 0;" +
				"null; 0 ∉ [0, 0)",
			"when index is after valid index range end, fails;" +
				"[0, 0); 1;" +
				"null; 1 ∉ [0, 0)",
		})
		void testEmpty(@IntRange IndexRange range, int index,
			@IntRange IndexRange expected1, @IndexRangeException Exception expected2) {
			test(range, index, expected1, expected2);
		}

		private void test(IndexRange range, int index, IndexRange expected1,
			Exception expected2) {

			try {
				final var prefix = range.getPrefix(index);
				assertEquals(expected1, prefix,
					format("%s.get(%d)", range, index));
			} catch (Exception exception) {
				if (expected2 == null) {
					throw exception;
				} else {
					assertEquals(expected2, exception,
						format("%s.get(%d)", range, index));
				}
			}
		}
	}

	@DisplayName(".getSuffix(int)")
	@Nested
	class GetSuffixTests {
		@DisplayName("when range is not empty")
		@Tests({
			"when index is before valid index range start, fails;" +
				"[2, 9); -1;" +
				"null; -1 ∉ [0, 7)",
			"when index is at valid index range start, returns range;" +
				"[2, 9); 0;" +
				"[2, 9); null",
			"when index is within valid index range, returns suffix;" +
				"[2, 9); 4;" +
				"[6, 9); null",
			"when index is at valid index range end, fails;" +
				"[2, 9); 7;" +
				"null; 7 ∉ [0, 7)",
			"when index is after valid index range end, fails;" +
				"[2, 9); 12;" +
				"null; 12 ∉ [0, 7)",
		})
		void testNotEmpty(@IntRange IndexRange range, int index,
			@IntRange IndexRange expected1, @IndexRangeException Exception expected2) {
			test(range, index, expected1, expected2);
		}

		@DisplayName("when range is empty")
		@Tests({
			"when index is before valid index range start, fails;" +
				"[0, 0); -5;" +
				"null; -5 ∉ [0, 0)",
			"when index is at valid index range start, fails;" +
				"[0, 0); 0;" +
				"null; 0 ∉ [0, 0)",
			"when index is after valid index range end, fails;" +
				"[0, 0); 1;" +
				"null; 1 ∉ [0, 0)",
		})
		void testEmpty(@IntRange IndexRange range, int index,
			@IntRange IndexRange expected1, @IndexRangeException Exception expected2) {
			test(range, index, expected1, expected2);
		}

		private void test(IndexRange range, int index, IndexRange expected1,
			Exception expected2) {

			try {
				final var suffix = range.getSuffix(index);
				assertEquals(expected1, suffix,
					format("%s.get(%d)", range, index));
			} catch (Exception exception) {
				if (expected2 == null) {
					throw exception;
				} else {
					assertEquals(expected2, exception,
						format("%s.get(%d)", range, index));
				}
			}
		}
	}

	@DisplayName(".get(IndexRange)")
	@Nested
	class GetAtIndexRangeTests {
		@DisplayName("when range is not empty")
		@Tests({
			"when argument range starts at valid index range start and ends within valid index range, returns range;" +
				"[6, 9); [0, 2);" +
				"[6, 8); null",
			"when argument range coincides with valid index range, returns range;" +
				"[6, 9); [0, 3);" +
				"[6, 9); null",
			"when argument range starts within valid index range and ends after valid index range end, fails;" +
				"[6, 9); [0, 7);" +
				"null; [0, 7) ⊈ [0, 3)",
			"when argument range starts and ends after valid index range end, fails;" +
				"[6, 9); [6, 9);" +
				"null; [6, 9) ⊈ [0, 3)",
			"when argument range is empty, return empty range;" +
				"[6, 9); [0, 0);" +
				"[0, 0); null"
		})
		void testNotEmpty(@IntRange IndexRange range1, @IntRange IndexRange range2,
			@IntRange IndexRange expected1, @IndexRangeException Exception expected2) {
			test(range1, range2, expected1, expected2);
		}

		@DisplayName("when range is empty")
		@Tests({
			"when argument range is not empty, fails;" +
				"[0, 0); [6, 9);" +
				"null; [6, 9) ⊈ [0, 0)",
			"when argument range is empty, returns empty range;" +
				"[0, 0); [0, 0);" +
				"[0, 0); null"
		})
		void testEmpty(@IntRange IndexRange range1, @IntRange IndexRange range2,
			@IntRange IndexRange expected1, @IndexRangeException Exception expected2) {
			test(range1, range2, expected1, expected2);
		}

		private void test(IndexRange range1, IndexRange range2,
			IndexRange expected1, Exception expected2) {

			try {
				final var subrange = range1.get(range2);
				assertEquals(expected1, subrange,
					format("%s.get(%s)", range1, range2));
			} catch (IndexRangeNotInRangeException exception) {
				if (expected2 == null) {
					throw exception;
				} else {
					assertEquals(expected2, exception,
						format("%s.get(%s)", range1, range2));
				}
			}
		}
	}

	@DisplayName(".matchAll(Predicate<Integer>)")
	@Nested
	class MatchAllTests {
		@DisplayName("\uD83D\uDC3F")
		@Tests({
			"when range is not empty, returns matching indexes;" +
				"[1, 9);" +
				"[2, 4, 6, 8]",
			"when range is empty, returns empty sequence;" +
				"[0, 0);" +
				"[]"
		})
		void test(@IntRange IndexRange range, @IntList List<Integer> expected) {
			final var matches = range.matchAll((index) -> index % 2 == 0);
			assertEquals(expected, matches,
				format("%s.matchAll(<is even>)", range));
		}
	}

	@DisplayName(".findFirst(Integer)")
	@Nested
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	class FindFirstTests {
		@DisplayName("\uD83C\uDF8E")
		@Tests({
			"when index is before range start, returns empty optional;" +
				"[2, 9); 0;" +
				"null",
			"when index is at range start, returns its index;" +
				"[6, 8); 6;" +
				"0",
			"when index is within range, returns its index;" +
				"[4, 9); 6;" +
				"2",
			"when index is at range end, returns empty optional;" +
				"[3, 8); 8;" +
				"null",
			"when index is after range end, returns empty optional;" +
				"[4, 8); 12;" +
				"null",
			"when range is empty, returns empty optional;" +
				"[0, 0); 0;" +
				"null"
		})
		void test(@IntRange IndexRange range, int item,
			@IntOptional Optional<Integer> expected) {

			final var index = range.findFirst(item);
			assertEquals(expected, index,
				format("%s.findFirst(%d)", range, item));
		}
	}

	@DisplayName(".findAll(Integer)")
	@Nested
	class FindTests {
		@DisplayName("when range is not empty")
		@Tests({
			"when index is before range start, returns empty sequence;" +
				"[4, 9); 2;" +
				"[]",
			"when index is at range start, returns its index;" +
				"[4, 9); 4;" +
				"[0]",
			"when index is withing range, returns its index;" +
				"[4, 9); 7;" +
				"[3]",
			"when index is at range end, returns empty sequence;" +
				"[4, 9); 9;" +
				"[]",
			"when index is after range end, returns empty sequence;" +
				"[4, 9); 14;" +
				"[]",
			"when index is null, returns empty sequence;" +
				"[4, 9); null;" +
				"[]"
		})
		void testNotEmpty(@IntRange IndexRange range, Integer index,
			@IntList List<Integer> expected) {
			test(range, index, expected);
		}

		@DisplayName("when range is empty")
		@Tests({
			"when index is before range start, returns empty sequence;" +
				"[0, 0); -4;" +
				"[]",
			"when index is at range start, returns empty sequence;" +
				"[0, 0); 0;" +
				"[]",
			"when index is after range end, returns empty sequence;" +
				"[0, 0); 4;" +
				"[]",
			"when index is null, returns empty sequence;" +
				"[0, 0); null;" +
				"[]"
		})
		void testEmpty(@IntRange IndexRange range, Integer index,
			@IntList List<Integer> expected) {
			test(range, index, expected);
		}

		void test(IndexRange range, Integer index, List<Integer> expected) {
			final var indexes = range.findAll(index);
			assertEquals(expected, indexes,
				format("%s.findAll(%d)", range, index));
		}
	}

	@DisplayName(".findAll(Sequence<T>)")
	@Nested
	class FindSequenceTests {
		@DisplayName("when range is not empty")
		@Tests({
			"when sequence is present, returns its index;" +
				"[3, 9); [5, 6, 7];" +
				"[2]",
			"when sequence is absent, returns empty sequence;" +
				"[3, 9); [7, 8, 9];" +
				"[]",
			"when argument sequence is larger, returns empty sequence;" +
				"[3, 5); [3, 4, 5, 6, 7];" +
				"[]",
			"when argument sequence is empty, returns all indexes;" +
				"[3, 9); [];" +
				"[0, 1, 2, 3, 4, 5]"
		})
		void testNotEmpty(@IntRange IndexRange range, @IntList List<Integer> items,
			@IntList List<Integer> expected) {
			test(range, items, expected);
		}

		@DisplayName("when range is not empty")
		@Tests({
			"when argument sequence is not empty, returns empty sequence;" +
				"[0, 0); [7, 8, 9];" +
				"[]",
			"when argument sequence is empty, returns empty sequence;" +
				"[0, 0); [];" +
				"[]"
		})
		void testEmpty(@IntRange IndexRange range, @IntList List<Integer> items,
			@IntList List<Integer> expected) {
			test(range, items, expected);
		}

		private void test(IndexRange range, Sequence<Integer> items,
			Sequence<Integer> expected) {

			final var indexes = range.findAll(items);
			assertEquals(expected, indexes,
				format("%s.findAll(%s)", range, items));
		}
	}

	@DisplayName(".getDistinct()")
	@Nested
	class GetDistinctTests {
		@DisplayName("\uD83C\uDFC0")
		@Tests({
			"returns itself;" +
				"[4, 9)"
		})
		void test(@IntRange IndexRange range) {
			final var distinct = range.getDistinct();
			assertSame(range, distinct,
				format("%s.getDistinct()", range));
		}
	}

	@DisplayName(".equals(Object)")
	@Nested
	class EqualsTests {
		@DisplayName("when range is not empty")
		@Tests({
			"when argument range equals, returns true;" +
				"[4, 8); [4, 8);" +
				"true",
			"when argument range start differs, returns false;" +
				"[4, 8); [4, 6);" +
				"false",
			"when argument range end differs, returns false;" +
				"[4, 8); [3, 8);" +
				"false",
			"when argument range is null, returns false;" +
				"[4, 8); null;" +
				"false"
		})
		void testNotEmpty(@IntRange IndexRange range1, @IntRange IndexRange range2,
			boolean expected) {
			test(range1, range2, expected);
		}

		@DisplayName("when range is empty")
		@Tests({
			"when argument range is empty, returns true;" +
				"[0, 0); [0, 0);" +
				"true",
			"when argument range is not empty, returns false;" +
				"[0, 0); [0, 6);" +
				"false",
			"when argument range in null, returns false;" +
				"[0, 0); null;" +
				"false"
		})
		void testEmpty(@IntRange IndexRange range1, @IntRange IndexRange range2,
			boolean expected) {
			test(range1, range2, expected);
		}

		private void test(IndexRange range1, IndexRange range2, boolean expected) {
			final var equals = range1.equals(range2);
			assertEquals(expected, equals,
				format("%s.equals(%s)", range1, range2));
		}
	}

	@DisplayName(".iterator(int)")
	@Nested
	class IteratorIntTests {
		@DisplayName("when range is not empty")
		@Tests({
			"when index is before valid index range start, fails;" +
				"[2, 6); -2;" +
				"null; -2 ∉ [0, 4)",
			"when index is at valid index range start, returns all items iterator;" +
				"[2, 6); 0;" +
				"[2, 3, 4, 5]; null",
			"when index is within valid index range, returns suffix iterator;" +
				"[2, 6); 2;" +
				"[4, 5]; null",
			"when index is at valid index range end, fails;" +
				"[2, 6); 6;" +
				"null; 6 ∉ [0, 4)",
			"when index is after valid index range end, fails;" +
				"[2, 6); 9;" +
				"null; 9 ∉ [0, 4)"
		})
		void testNotEmpty(@IntRange IndexRange range, int index,
			@StringList List<String> expected1,
			@IndexRangeException Exception expected2) {
			test(range, index, expected1, expected2);
		}

		@DisplayName("when range is empty")
		@Tests({
			"when index is before valid index range start, fails;" +
				"[0, 0); -1;" +
				"null; -1 ∉ [0, 0)",
			"when index is at valid index range start, fails;" +
				"[0, 0); 0;" +
				"null; 0 ∉ [0, 0)",
			"when index is after valid index range end, fails;" +
				"[0, 0); 1;" +
				"null; 1 ∉ [0, 0)"
		})
		void testEmpty(@IntRange IndexRange range, int index,
			@StringList List<String> expected1,
			@IndexRangeException Exception expected2) {
			test(range, index, expected1, expected2);
		}

		void test(IndexRange range, int index, List<String> expected1,
			Exception expected2) {

			try {
				final var iterator = range.iterator(index);
				final var iterated = new MutableList<String>();
				while (iterator.hasNext()) {
					final var next = iterator.next();
					iterated.append(Integer.toString(next));
				}

				assertEquals(expected1, iterated,
					format("%s.iterator(%d)", range, index));
			} catch (Exception exception) {
				if (expected2 == null) {
					throw exception;
				} else {
					assertEquals(expected2, exception,
						format("%s.iterator(%d)", range, index));
				}
			}
		}
	}

	@DisplayName(".iterator()")
	@Nested
	class IteratorTests {
		@DisplayName("\uD83D\uDCC1")
		@Tests({
			"when range is not empty, iterates values;" +
				"[3, 7);" +
				"[3, 4, 5, 6]",
			"when range is empty, does nothing;" +
				"[0, 0);" +
				"[]"
		})
		void test(@IntRange IndexRange range, @StringList List<String> expected) {
			final var iterator = range.iterator();
			final var iterated = new MutableList<String>();
			while (iterator.hasNext()) {
				final var next = iterator.next();
				iterated.append(Integer.toString(next));
			}

			assertEquals(expected, iterated,
				format("%s.iterator()", range));
		}
	}

	@DisplayName(".toString()")
	@Nested
	class ToStringTests {
		@DisplayName("\uD83C\uDF41")
		@Tests({
			"when range is not empty, returns formatted string;" +
				"[6, 9);" +
				"[6, 9)",
			"when range is empty, returns formatted string;" +
				"[0, 0);" +
				"[0, 0)"
		})
		void test(@IntRange IndexRange range, String expected) {
			final var string = range.toString();
			assertEquals(expected, string,
				format("%s.toString()", range));
		}
	}
}

@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(IntRange.Converter.class)
@interface IntRange {
	class Converter extends TypedArgumentConverter<String, IndexRange> {
		protected Converter() {
			super(String.class, IndexRange.class);
		}

		@Override
		protected IndexRange convert(String s) throws ArgumentConversionException {
			if (s == null) {
				return null;
			}

			final var length = s.length();
			final var bounds = s.substring(1, length - 1)
				.split("\\s*,\\s*");

			return new IndexRange(
				Integer.parseInt(bounds[0]),
				Integer.parseInt(bounds[1]));
		}
	}
}

@ConvertWith(IntRangeOptional.Converter.class)
@Retention(RetentionPolicy.RUNTIME)
@interface IntRangeOptional {
	@SuppressWarnings("rawtypes")
	class Converter extends TypedArgumentConverter<String, Optional> {
		protected Converter() {
			super(String.class, Optional.class);
		}

		@Override
		protected Optional<IndexRange> convert(String s) throws ArgumentConversionException {
			final var converter = new IntRange.Converter();
			return Optional.ofNullable(s)
				.map(converter::convert);
		}
	}
}

@ConvertWith(IndexRangeException.Converter.class)
@Retention(RetentionPolicy.RUNTIME)
@interface IndexRangeException {
	class Converter extends TypedArgumentConverter<String, Exception> {
		protected Converter() {
			super(String.class, Exception.class);
		}

		@Override
		protected Exception convert(String s) throws ArgumentConversionException {
			if (s == null) {
				return null;
			}

			final var matcher1 = indexRegex.matcher(s);
			if (matcher1.find()) {
				return matchIndexNotInRangeException(matcher1);
			}

			final var matcher2 = indexRangeRegex.matcher(s);
			if (matcher2.find()) {
				return matchIndexRangeNotInRangeException(matcher2);
			}

			throw new ArgumentConversionException(
				format("Cannot parse %s or %s from %s.",
					IndexNotInRangeException.class.getName(),
					IndexRangeNotInRangeException.class.getName(), s));
		}

		private static final Pattern indexRegex = Pattern
			.compile("(-?\\d+)" +
				"\\s*∉\\s*" +
				"\\[\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*\\)");

		private IndexNotInRangeException matchIndexNotInRangeException(Matcher matcher) {
			final var index = matcher.group(1);
			final var start = matcher.group(2);
			final var end = matcher.group(3);

			return new IndexNotInRangeException(
				Integer.parseInt(index),
				new IndexRange(
					Integer.parseInt(start),
					Integer.parseInt(end)));
		}

		private static final Pattern indexRangeRegex = Pattern
			.compile("\\[\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*\\)" +
				"\\s*⊈\\s*" +
				"\\[\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*\\)");

		private IndexRangeNotInRangeException matchIndexRangeNotInRangeException(Matcher matcher) {
			final var start1 = matcher.group(1);
			final var end1 = matcher.group(2);
			final var start2 = matcher.group(3);
			final var end2 = matcher.group(4);

			return new IndexRangeNotInRangeException(
				new IndexRange(
					Integer.parseInt(start1),
					Integer.parseInt(end1)),
				new IndexRange(
					Integer.parseInt(start2),
					Integer.parseInt(end2)));
		}
	}
}