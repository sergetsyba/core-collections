package co.tsyba.core.collections;


import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.DefaultArgumentConverter;
import org.junit.platform.commons.util.AnnotationUtils;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.copyOfRange;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@TestTemplate
@ExtendWith(TestsExtension.class)
@interface Tests {
	String[] value() default {};
}

class TestsExtension implements TestTemplateInvocationContextProvider {
	@Override
	public boolean supportsTestTemplate(ExtensionContext context) {
		final var method = context.getTestMethod();
		return AnnotationUtils.isAnnotated(method, Tests.class);
	}

	@Override
	public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
		final var method = context.getRequiredTestMethod();
		final var tests = AnnotationUtils
			.findAnnotation(method, Tests.class)
			.map(Tests::value)
			.orElseGet(() -> new String[0]);

		return Stream.of(tests)
			.map(TestsInvocationContext::new);
	}
}

class TestsInvocationContext implements TestTemplateInvocationContext {
	private final String[] values;

	public TestsInvocationContext(String values) {
		this.values = values.split("\\s*;\\s*");
	}

	@Override
	public String getDisplayName(int invocationIndex) {
		return values[0];
	}

	@Override
	public List<Extension> getAdditionalExtensions() {
		return List.of(
			new TestsParameterResolver(
				copyOfRange(values, 1, values.length)));
	}
}

class TestsParameterResolver implements ParameterResolver {
	private final String[] values;

	TestsParameterResolver(String[] values) {
		this.values = values;
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return true;
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		final var parameter = parameterContext.getParameter();
		final var index = parameterContext.getIndex();
		final var value = "null".equals(values[index])
			? null
			: values[index];

		return getConverter(parameter)
			.convert(value, parameterContext);
	}

	private ArgumentConverter getConverter(Parameter parameter) {
		return AnnotationUtils.findAnnotation(parameter, ConvertWith.class)
			.map(ConvertWith::value)
			.map((klass) -> (ArgumentConverter) ReflectionUtils.newInstance(klass))
			.orElse(DefaultArgumentConverter.INSTANCE);
	}
}