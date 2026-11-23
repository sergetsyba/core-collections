# Code style guide

## Unit tests

### Structure

* Group unit tests for each tested type into a separate suite.
    * Name test suite class after the type it tests with `Tests` suffix.
    * Use tested type name for test suite description.
* Group unit tests for each method being tested into a separate `@Nested` suite.
    * Name test suite class after the method it tests with `Tests` suffix.
    * When there are overloaded variants of the method being tested, include argument type
      in the test suite class name.
    * Use tested method signature for test suite description.
        * Prefix method test descriptions with `.`, except for constructors.
* Group test cases into parametrized tests by common test condition.
    * Name test methods after the common condition with a `test...` prefix.
        * Omit `a`/`an`/`the` articles and auxiliary verbs from test method name.
    * When there is no common test condition name test method simply `test(...)`.
* When there are multiple test conditions either split them into separate test methods
  or regroup into a `@Nested` suite.
    * Name nested test suite class after common test condition with `Tests` suffix.
    * Omit articles and auxiliary verbs from class name, just like test method name.
* Use package access level for all unit test classes and methods.
* Order test suites in the same order corresponding methods are declared.

<details>
<summary>Example</summary>
<p>

```java
public class Collection<T> {
	public boolean isEmpty() {
		// ...
	}

	public boolean contains(Collection<T> items) {
		// ...
	}
}
```

```java

@DisplayName("Collection<T>")
class CollectionTests {
	@Nested
	@DisplayName(".isEmpty()")
	class IsEmptyTests {
		void test() {
			// ...
		}
	}

	@Nested
	@DisplayName(".contains(Collection<T>)")
	class ContainsCollectionTests {
		void testCollectionNotEmpty() {
		}

		void testCollectionEmpty() {
		}
	}
}
```

</p>
</details>

### Descriptions

* Describe test cases according to their input conditions and expected results.
    * List test conditions first, starting with `when ...`.
    * List expected result after test conditions, separated by a comma.
* Describe nested test suites and groups of parametrized tests with their common
  condition.
    * Write common condition, starting with `when ...`.
* Use natural grammar and sentence structure overall.
    * Refer to collection elements as items, map elements as entries.
    * Use `argument ...` to refer to a method argument when a test description needs to
      distinguish between method argument and object being tested.
* Write test descriptions in lower-case, unless referencing class names or methods.

### Arguments

* Use collections with at least 4 elements, unless a test requires a specific element
  count.
* Do not repeat collection elements between test cases, unless a test suite requires it.


* Prefer provided convenience `@Tests` annotation for parametrized tests.
    * Each test case is defined as a single string.
    * Test arguments are separated by semicolons.
    * First argument is a test case description.

<details>
<summary>Example</summary>
<p>

```java
public class Collection<T> {
	public boolean isEmpty() {
		// ...
	}

	public boolean contains(Collection<T> items) {
		// ...
	}
}
```

```java

@DisplayName("Collection<T>")
class CollectionTests {
	@Nested
	@DisplayName(".isEmpty()")
	class IsEmptyTests {
		@DisplayName("🍅")
		@Tests({
			"when collection is not empty, returns false;" +
				"[r, T, s, e, G]; false",
			"when collection is empty, returns true;" +
				"[]; true"
		})
		void test(@StringCollection Collection<String> items, boolean expected) {
			// ...
		}
	}

	@Nested
	@DisplayName(".contains(Collection<T>)")
	class ContainsCollectionTests {
		@DisplayName("when collection is not empty")
		@Tests({
			"when all argument items are present, returns true;" +
				"[r, D, v, s]; [v, s, r];" +
				"true",
			"when some argument items are absent, returns false;" +
				"[v, d, A, f, K, l]; [f, A, x, G, o];" +
				"false",
			"..."
		})
		void testCollectionNotEmpty(@StringCollection Collection<String> items1,
			@StringCollection Collection<String> items2, boolean expected) {
			// ...
		}

		@DisplayName("when collection is empty")
		@Tests({
			"when argument collection is not empty, returns false;" +
				"[]; [f, N, m, j, T];" +
				"false",
			"when argument collection is empty, returns true;" +
				"[]; [];" +
				"true",
		})
		void testCollectionEmpty(@StringCollection Collection<String> items1,
			@StringCollection Collection<String> items2, boolean expected) {
			// ...
		}
	}
}
```

</p>
</details>
