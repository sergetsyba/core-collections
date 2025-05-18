# Code style

## Tests

Currently, this project uses [JUnit 5][junit5] library for unit tests.

Every public method must have a set of unit tests.

* Group all unit tests for a class into a wrapper test class. Name this class with the
  original class's name and a `-Tests` suffix. Do not annotate it with a `@DisplayName`
  annotation.
* Group all unit tests for a public method into a nested class. Name this nested class
  with the tested method name and a `-Tests` suffix. Annotate it with a `@Nested` and
  `@DisplayName` (with the tested method's signature) annotations.
* When there are multiple overloading public methods being tested, name method parameters
  or their purpose in the corresponding `@Nested` class, separated by `-With-` infix.

<details>
<summary><em>Example</em></summary>

```java
// SomeClass.java
class SomeClass {
	public void doSomething() {
	}

	public void doSomething(int index) {
	}
}

// SomeClassTests.java
class SomeClassTests {
	@DisplayName(".soSomething()")
	@Nested
	class DoSomethingTests {
	}

	@DisplayName(".doSomething(int)")
	@Nested
	class DoSomethingWithIndexTests {

	}
}
```

</details>

Use [parameterized tests][parameterized tests] to group test cases for a method, whenever
possible.

Use provided custom `@Tests` annotation. It takes a `String` array parameter, where each
element describes a separate test case. Test description, inputs and an optional expected
result are separated by `;` within a string element.

* Describe test cases in the format
  `when <condition> and <another condition>, <expected result>`.
* Name the test method simply `test`.
* Format assertion message to show tested method signature and all parameter values. Do
  not include actual and expected results, unless assertion function itself does print it
  for some reason.

<details>
<summary><em>Example</em></summary>

```java
class SomeClassTests {
	@DisplayName(".doSomething(int)")
	class DoSomethingTests {
		@DisplayName("🎉")
		@Tests({
			"when argument is positive, does something;" +
				"12",
			"when argument is negative, does other thing;" +
				"-16"
		})
		void test(int index) {
			// ...
			assertEquals(expected, actual,
				String.format("%s.doSomething(%d)", this, index));
		}
	}
}
```

</details>

When there are multiple cases having the same condition,

* Group them into a separate test method with a `@DisplayName`, which states the common
  condition.
* Name each test method with a `test-` prefix followed by condition description.

<details>
<summary><em>Example</em></summary>

```java
class SomeClassTests {
	@DisplayName(".doSomething(List<T>)")
	@Nested
	class DoSomethingTests {
		@DisplayName("when argument list is not empty")
		@Tests({
			"when items are short, does something;" +
				"[a, b, c]",
			"when items are long, does another thing;" +
				"[aaa, bbb, ccc]",
			"when items are empty, fails;" +
				"['', '']"
		})
		void testArgumentListNotEmpty(List<T> items) {
			// ...
			assertEquals(expected, actual,
				String.format("%s.doSomething(%s)", this, items));
		}

		@DisplayName("when argument list is empty")
		@Tests({
			"does nothing;" +
				"[]"
		})
		void testArgumentListEmpty(List<T> empty) {
			// ...
			assertEquals(expected, actual,
				String.format("%s.doSomething(%s)", this, empty));
		}
	}
}
```

</details>

[junit5]: https://junit.org/junit5/

[parameterized tests]: https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests