package net.skeeks.webtesting;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class BrowserTestTemplate implements TestTemplateInvocationContextProvider {

	@Override
	public boolean supportsTestTemplate(ExtensionContext context) {
		return true;
	}

	@Override
	public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
		List<TestTemplateInvocationContext> contexts = new ArrayList<TestTemplateInvocationContext>();
		contexts.add(browserInvocationContext(FirefoxDriver.class));
		System.out.println("Method " + context.getTestMethod().get().getName());
		if (context.getTestMethod().get().getAnnotationsByType(NoChrome.class).length == 0) {
			contexts.add(browserInvocationContext(ChromeDriver.class));
		}
		return contexts.stream();
	}

	private TestTemplateInvocationContext browserInvocationContext(Class<? extends WebDriver> parameter) {
		return new TestTemplateInvocationContext() {
			@Override
			public String getDisplayName(int invocationIndex) {
				return parameter.getSimpleName();
			}

			@Override
			public List<Extension> getAdditionalExtensions() {
				return Arrays.asList(new ParameterResolver() {
					@Override
					public boolean supportsParameter(ParameterContext parameterContext,
							ExtensionContext extensionContext) {
						return true;
					}

					@Override
					public Object resolveParameter(ParameterContext parameterContext,
							ExtensionContext extensionContext) {
						try {
							return parameter.getDeclaredConstructor().newInstance();
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				}, new InvocationInterceptor() {
					@Override
					public void interceptTestTemplateMethod(Invocation<Void> invocation,
							ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext)
							throws Throwable {
						System.out.println("before test template invocation");

						WebDriver driver = null;
						if (!invocationContext.getArguments().isEmpty()) {
							Object arg1 = invocationContext.getArguments().get(0);
							if (arg1 instanceof WebDriver) {
								driver = (WebDriver) arg1;
							}
						}

						try {
							invocation.proceed();
							if (driver != null) {
								System.out.println("Terminating driver");
								driver.quit();
							}
						} catch (Exception e) {
							if (driver != null) {
								System.out.println("Terminating driver");
								driver.quit();
							}
							throw e;
						}
						System.out.println("after test template invocation");
					}
				});
			}
		};
	}
}