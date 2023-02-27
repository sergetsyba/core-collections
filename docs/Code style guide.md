# Code style guide

## Unit tests

* **Use package access level for all unit test classes and methods.**
* **Create a separate class with unit tests for each public class.**
	* Name unit test classes after the class they test with a `Tests` suffix.

	<details>
	<summary>Example</summary>
	<p>

	```java
	// Collection.java
	public class Collection {
		// ...
	}
	```

	```java
	// CollectionTests.java
	class CollectionTests {
		// ...
	}
	```
	</p>
	</details>

* **Create a separate inner class with unit tests for each public method.**
	* Name the inner class after the method it tests with a `Tests` prefix.
		* When there are multiple overloaded methods with the same name, **include
			argument type in the inner class name** as well.
	* Use signature of the method being tested as test description, prefixed with a dot,
		but, excluding return type and argument names of the method.

	<details>
	<summary>Example</summary>
	<p>
	
	```java
	class Collection<T> {
		public boolean contains(T item) {
			// ...
		}
		
		@SafeVarargs
		public boolean contains(T... items) {
			// ...
		}
		
		public boolean contains(Collection<T> items) {
			// ...
		}
				
		// ...
	}
	```
	
	```java
	class CollectionTests {
		@Nested
		@DisplayName(".contains(T)")
		class ContainsTests {
			// ...
		}
		
		@Nested
		@DisplayName(".contains(T...)")
		class ContainsVarargsTests {
			// ...
		}
		
		@Nested
		@DisplayName(".contains(Collection<T>)")
		class ContainsCollectionTests {
			// ...
		}
	}
	```
	</p>
	</details>

* **Name unit test functions according to the expected result and input conditions of a
	method being tested.**
	* State expected result first.
	* Include conditions after the `When` infix.
	* Omit `a`/`an`/`the` articles and auxiliary verbs.

	<details>
	<summary>Example</summary>
	<p>

	```java
	public class Map<K, V> {
		public boolean isEmpty() {
			// ...
		}
	}
	```

	```java
	class MapTests {
		@Nested
		@DisplayName(".isEmpty()")
		class IsEmptyTests {
			@Test
			void returnsTrueWhenMapEmpty() {
				// ...
			}
			
			@Test
			void returnsFalseWhenMapNotEmpty() {
				// ...
			}
		}
	}
	```
	</p>
	</details>

* **Describe unit tests according to input conditions and expected result of a method
	being tested.**
	* List test conditions first, starting with `when ... `.
	* List expected result after test conditions, separated by a comma.
	* Use natural grammar and sentence structure overall.
	* Use lower-case, unless referencing class names or methods.
	
	<details>
	<summary>Example</summary>
	<p>

	```java
	public class Map<K, V> {
		public boolean isEmpty() {
			// ...
		}
	}
	```

	```java
	class MapTests {
		@Nested
		@DisplayName(".isEmpty()")
		class IsEmptyTests {
			@Test
			@DisplayName("when map is empty, returns true")
			void returnsTrueWhenMapEmpty() {
				// ...
			}
			
			@Test
			@DisplayName("when map is not empty, returns false")
			void returnsFalseWhenMapNotEmpty() {
				// ...
			}
		}
	}
	```
	</p>
	</details>

* **Group unit tests with common input conditions into an inner test class.**
	* Name the inner class according to the common input conditions, with a `Tests`
		suffix.
	* Provide a description, stating input conditions, starting with `when ...`.
	* Omit the common input conditions from the subsequent unit test function names and
		descriptions.

	<details>
	<summary>Example</summary>
	<p>

	```java
	public class Map<K, V> {
		public boolean contains(T item) {
			// ...
		}
	}
	```

	```java
	class MapTests {
		@Nested
		@DisplayName(".contains(T)")
		class ContainsTests {
			@Nested
			@DisplayName("when map is not empty")
			class NotEmptyMapTests {
				@Test
				@DisplayName("when item is present, returns true")
				void returnsTrueWhenItemPresent() {
					// ...
				}
				
				@Test
				@DisplayName("when item is absent, returns false")
				void returnsFalseWhenItemAbsent() {
					// ...
				}
			}
			
			@Test
			@DisplayName("when map is empty, returns false")
			void returnsFalseWhenEmpty() {
				// ...
			}
		}
	}
	```
	</p>
	</details>

* When a method of a tested class takes an argument of the same class, **use `Arg` to
	refer to the argument in unit test function names and `... argument ...` in their
	descriptions**.
	* Refer to the primary instance of the tested class without any additional moniker.

	<details>
	<summary>Example</summary>
	<p>

	```java
	public class Set<T> {
		public boolean contains(Set<T> items) {
			// ...
		}
	}
	```

	```java
	class SetTests {
		@Nested
		@DisplayName(".contains(Set<T>)")
		class ContainsTests {
			@Nested
			@DisplayName("when set is not empty")
			class NotEmptySetTests {
				@Test
				@DisplayName("when all items of argument set are present, returns true")
				void returnsTrueWhenAllArgSetItemsPresent() {
					// ...
				}
				
				@Test
				@DisplayName("when some items of argument set are absent, returns false")
				void returnsFalseWhenSomeArgSetItemsAbsent() {
					// ...
				}
				
				@Test
				@DisplayName("when argument set is empty, returns true")
				void returnsTrueWhenArgSetEmpty() {
					// ...
				}
			}
			
			// ...
		}
	}
	```
	</p>
	</details>