error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1499.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1499.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 664
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1499.java
text:
```scala
public class StandardEnvironmentTests {

/*
 * Copyright 2002-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

p@@ackage org.springframework.core.env;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.hasItem;
import static org.junit.matchers.JUnitMatchers.hasItems;
import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;
import static org.springframework.core.env.AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME;
import static org.springframework.core.env.AbstractEnvironment.RESERVED_DEFAULT_PROFILE_NAME;

import java.lang.reflect.Field;
import java.security.AccessControlException;
import java.security.Permission;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;
import org.springframework.mock.env.MockPropertySource;

/**
 * Unit tests for {@link StandardEnvironment}.
 *
 * @author Chris Beams
 */
public class EnvironmentTests {

	private static final String ALLOWED_PROPERTY_NAME = "theanswer";
	private static final String ALLOWED_PROPERTY_VALUE = "42";

	private static final String DISALLOWED_PROPERTY_NAME = "verboten";
	private static final String DISALLOWED_PROPERTY_VALUE = "secret";

	private static final String STRING_PROPERTY_NAME = "stringPropName";
	private static final String STRING_PROPERTY_VALUE = "stringPropValue";
	private static final Object NON_STRING_PROPERTY_NAME = new Object();
	private static final Object NON_STRING_PROPERTY_VALUE = new Object();

	private ConfigurableEnvironment environment = new StandardEnvironment();

	@Test
	public void propertySourceOrder() {
		ConfigurableEnvironment env = new StandardEnvironment();
		MutablePropertySources sources = env.getPropertySources();
		assertThat(sources.precedenceOf(PropertySource.named(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME)), equalTo(0));
		assertThat(sources.precedenceOf(PropertySource.named(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME)), equalTo(1));
		assertThat(sources.size(), is(2));
	}

	@Test
	public void activeProfilesIsEmptyByDefault() {
		assertThat(environment.getActiveProfiles().length, is(0));
	}

	@Test
	public void defaultProfilesContainsDefaultProfileByDefault() {
		assertThat(environment.getDefaultProfiles().length, is(1));
		assertThat(environment.getDefaultProfiles()[0], equalTo("default"));
	}

	@Test
	public void setActiveProfiles() {
		environment.setActiveProfiles("local", "embedded");
		String[] activeProfiles = environment.getActiveProfiles();
		assertThat(Arrays.asList(activeProfiles), hasItems("local", "embedded"));
		assertThat(activeProfiles.length, is(2));
	}

	@Test(expected=IllegalArgumentException.class)
	public void setActiveProfiles_withNullProfileArray() {
		environment.setActiveProfiles((String[])null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void setActiveProfiles_withNullProfile() {
		environment.setActiveProfiles((String)null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void setActiveProfiles_withEmptyProfile() {
		environment.setActiveProfiles("");
	}

	@Test(expected=IllegalArgumentException.class)
	public void setDefaultProfiles_withNullProfileArray() {
		environment.setDefaultProfiles((String[])null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void setDefaultProfiles_withNullProfile() {
		environment.setDefaultProfiles((String)null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void setDefaultProfiles_withEmptyProfile() {
		environment.setDefaultProfiles("");
	}

	@Test
	public void addActiveProfile() {
		assertThat(environment.getActiveProfiles().length, is(0));
		environment.setActiveProfiles("local", "embedded");
		assertThat(Arrays.asList(environment.getActiveProfiles()), hasItems("local", "embedded"));
		assertThat(environment.getActiveProfiles().length, is(2));
		environment.addActiveProfile("p1");
		assertThat(Arrays.asList(environment.getActiveProfiles()), hasItems("p1"));
		assertThat(environment.getActiveProfiles().length, is(3));
		environment.addActiveProfile("p2");
		environment.addActiveProfile("p3");
		assertThat(Arrays.asList(environment.getActiveProfiles()), hasItems("p2", "p3"));
		assertThat(environment.getActiveProfiles().length, is(5));
	}

	@Test
	public void reservedDefaultProfile() {
		assertThat(environment.getDefaultProfiles(), equalTo(new String[]{RESERVED_DEFAULT_PROFILE_NAME}));
		System.setProperty(DEFAULT_PROFILES_PROPERTY_NAME, "d0");
		assertThat(environment.getDefaultProfiles(), equalTo(new String[]{"d0"}));
		environment.setDefaultProfiles("d1", "d2");
		assertThat(environment.getDefaultProfiles(), equalTo(new String[]{"d1","d2"}));
		System.getProperties().remove(DEFAULT_PROFILES_PROPERTY_NAME);
	}

	@Test
	public void getActiveProfiles_systemPropertiesEmpty() {
		assertThat(environment.getActiveProfiles().length, is(0));
		System.setProperty(ACTIVE_PROFILES_PROPERTY_NAME, "");
		assertThat(environment.getActiveProfiles().length, is(0));
		System.getProperties().remove(ACTIVE_PROFILES_PROPERTY_NAME);
	}

	@Test
	public void getActiveProfiles_fromSystemProperties() {
		System.setProperty(ACTIVE_PROFILES_PROPERTY_NAME, "foo");
		assertThat(Arrays.asList(environment.getActiveProfiles()), hasItem("foo"));
		System.getProperties().remove(ACTIVE_PROFILES_PROPERTY_NAME);
	}

	@Test
	public void getActiveProfiles_fromSystemProperties_withMultipleProfiles() {
		System.setProperty(ACTIVE_PROFILES_PROPERTY_NAME, "foo,bar");
		assertThat(Arrays.asList(environment.getActiveProfiles()), hasItems("foo", "bar"));
		System.getProperties().remove(ACTIVE_PROFILES_PROPERTY_NAME);
	}

	@Test
	public void getActiveProfiles_fromSystemProperties_withMulitpleProfiles_withWhitespace() {
		System.setProperty(ACTIVE_PROFILES_PROPERTY_NAME, " bar , baz "); // notice whitespace
		assertThat(Arrays.asList(environment.getActiveProfiles()), hasItems("bar", "baz"));
		System.getProperties().remove(ACTIVE_PROFILES_PROPERTY_NAME);
	}

	@Test
	public void getDefaultProfiles() {
		assertThat(environment.getDefaultProfiles(), equalTo(new String[] {RESERVED_DEFAULT_PROFILE_NAME}));
		environment.getPropertySources().addFirst(new MockPropertySource().withProperty(DEFAULT_PROFILES_PROPERTY_NAME, "pd1"));
		assertThat(environment.getDefaultProfiles().length, is(1));
		assertThat(Arrays.asList(environment.getDefaultProfiles()), hasItem("pd1"));
	}

	@Test
	public void setDefaultProfiles() {
		environment.setDefaultProfiles();
		assertThat(environment.getDefaultProfiles().length, is(0));
		environment.setDefaultProfiles("pd1");
		assertThat(Arrays.asList(environment.getDefaultProfiles()), hasItem("pd1"));
		environment.setDefaultProfiles("pd2", "pd3");
		assertThat(Arrays.asList(environment.getDefaultProfiles()), not(hasItem("pd1")));
		assertThat(Arrays.asList(environment.getDefaultProfiles()), hasItems("pd2", "pd3"));
	}

	@Test(expected=IllegalArgumentException.class)
	public void acceptsProfiles_withEmptyArgumentList() {
		environment.acceptsProfiles();
	}

	@Test(expected=IllegalArgumentException.class)
	public void acceptsProfiles_withNullArgumentList() {
		environment.acceptsProfiles((String[])null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void acceptsProfiles_withNullArgument() {
		environment.acceptsProfiles((String)null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void acceptsProfiles_withEmptyArgument() {
		environment.acceptsProfiles("");
	}


	@Test
	public void acceptsProfiles_activeProfileSetProgrammatically() {
		assertThat(environment.acceptsProfiles("p1", "p2"), is(false));
		environment.setActiveProfiles("p1");
		assertThat(environment.acceptsProfiles("p1", "p2"), is(true));
		environment.setActiveProfiles("p2");
		assertThat(environment.acceptsProfiles("p1", "p2"), is(true));
		environment.setActiveProfiles("p1", "p2");
		assertThat(environment.acceptsProfiles("p1", "p2"), is(true));
	}

	@Test
	public void acceptsProfiles_activeProfileSetViaProperty() {
		assertThat(environment.acceptsProfiles("p1"), is(false));
		environment.getPropertySources().addFirst(new MockPropertySource().withProperty(ACTIVE_PROFILES_PROPERTY_NAME, "p1"));
		assertThat(environment.acceptsProfiles("p1"), is(true));
	}

	@Test
	public void acceptsProfiles_defaultProfile() {
		assertThat(environment.acceptsProfiles("pd"), is(false));
		environment.setDefaultProfiles("pd");
		assertThat(environment.acceptsProfiles("pd"), is(true));
		environment.setActiveProfiles("p1");
		assertThat(environment.acceptsProfiles("pd"), is(false));
		assertThat(environment.acceptsProfiles("p1"), is(true));
	}

	@Test
	public void environmentSubclass_withCustomProfileValidation() {
		ConfigurableEnvironment env = new AbstractEnvironment() {
			@Override
			protected void validateProfile(String profile) {
				super.validateProfile(profile);
				if (profile.contains("-")) {
					throw new IllegalArgumentException(
							"Invalid profile [" + profile + "]: must not contain dash character");
				}
			}
		};

		env.addActiveProfile("validProfile"); // succeeds

		try {
			env.addActiveProfile("invalid-profile");
			fail("expected validation exception");
		} catch (IllegalArgumentException ex) {
			assertThat(ex.getMessage(),
					equalTo("Invalid profile [invalid-profile]: must not contain dash character"));
		}
	}

	@Test
	public void getSystemProperties_withAndWithoutSecurityManager() {
		System.setProperty(ALLOWED_PROPERTY_NAME, ALLOWED_PROPERTY_VALUE);
		System.setProperty(DISALLOWED_PROPERTY_NAME, DISALLOWED_PROPERTY_VALUE);
		System.getProperties().put(STRING_PROPERTY_NAME, NON_STRING_PROPERTY_VALUE);
		System.getProperties().put(NON_STRING_PROPERTY_NAME, STRING_PROPERTY_VALUE);

		{
			Map<?, ?> systemProperties = environment.getSystemProperties();
			assertThat(systemProperties, notNullValue());
			assertSame(systemProperties, System.getProperties());
			assertThat(systemProperties.get(ALLOWED_PROPERTY_NAME), equalTo((Object)ALLOWED_PROPERTY_VALUE));
			assertThat(systemProperties.get(DISALLOWED_PROPERTY_NAME), equalTo((Object)DISALLOWED_PROPERTY_VALUE));

			// non-string keys and values work fine... until the security manager is introduced below
			assertThat(systemProperties.get(STRING_PROPERTY_NAME), equalTo(NON_STRING_PROPERTY_VALUE));
			assertThat(systemProperties.get(NON_STRING_PROPERTY_NAME), equalTo((Object)STRING_PROPERTY_VALUE));
		}

		SecurityManager oldSecurityManager = System.getSecurityManager();
		SecurityManager securityManager = new SecurityManager() {
			@Override
			public void checkPropertiesAccess() {
				// see http://download.oracle.com/javase/1.5.0/docs/api/java/lang/System.html#getProperties()
				throw new AccessControlException("Accessing the system properties is disallowed");
			}
			@Override
			public void checkPropertyAccess(String key) {
				// see http://download.oracle.com/javase/1.5.0/docs/api/java/lang/System.html#getProperty(java.lang.String)
				if (DISALLOWED_PROPERTY_NAME.equals(key)) {
					throw new AccessControlException(
							format("Accessing the system property [%s] is disallowed", DISALLOWED_PROPERTY_NAME));
				}
			}
			@Override
			public void checkPermission(Permission perm) {
				// allow everything else
			}
		};
		System.setSecurityManager(securityManager);

		{
			Map<?, ?> systemProperties = environment.getSystemProperties();
			assertThat(systemProperties, notNullValue());
			assertThat(systemProperties, instanceOf(ReadOnlySystemAttributesMap.class));
			assertThat((String)systemProperties.get(ALLOWED_PROPERTY_NAME), equalTo(ALLOWED_PROPERTY_VALUE));
			assertThat(systemProperties.get(DISALLOWED_PROPERTY_NAME), equalTo(null));

			// nothing we can do here in terms of warning the user that there was
			// actually a (non-string) value available. By this point, we only
			// have access to calling System.getProperty(), which itself returns null
			// if the value is non-string.  So we're stuck with returning a potentially
			// misleading null.
			assertThat(systemProperties.get(STRING_PROPERTY_NAME), nullValue());

			// in the case of a non-string *key*, however, we can do better.  Alert
			// the user that under these very special conditions (non-object key +
			// SecurityManager that disallows access to system properties), they
			// cannot do what they're attempting.
			try {
				systemProperties.get(NON_STRING_PROPERTY_NAME);
				fail("Expected IllegalArgumentException when searching with non-string key against ReadOnlySystemAttributesMap");
			} catch (IllegalArgumentException ex) {
				// expected
			}
		}

		System.setSecurityManager(oldSecurityManager);
		System.clearProperty(ALLOWED_PROPERTY_NAME);
		System.clearProperty(DISALLOWED_PROPERTY_NAME);
		System.getProperties().remove(STRING_PROPERTY_NAME);
		System.getProperties().remove(NON_STRING_PROPERTY_NAME);
	}

	@Test
	public void getSystemEnvironment_withAndWithoutSecurityManager() {
		getModifiableSystemEnvironment().put(ALLOWED_PROPERTY_NAME, ALLOWED_PROPERTY_VALUE);
		getModifiableSystemEnvironment().put(DISALLOWED_PROPERTY_NAME, DISALLOWED_PROPERTY_VALUE);

		{
			Map<String, Object> systemEnvironment = environment.getSystemEnvironment();
			assertThat(systemEnvironment, notNullValue());
			assertSame(systemEnvironment, System.getenv());
		}

		SecurityManager oldSecurityManager = System.getSecurityManager();
		SecurityManager securityManager = new SecurityManager() {
			@Override
			public void checkPermission(Permission perm) {
				//see http://download.oracle.com/javase/1.5.0/docs/api/java/lang/System.html#getenv()
				if ("getenv.*".equals(perm.getName())) {
					throw new AccessControlException("Accessing the system environment is disallowed");
				}
				//see http://download.oracle.com/javase/1.5.0/docs/api/java/lang/System.html#getenv(java.lang.String)
				if (("getenv."+DISALLOWED_PROPERTY_NAME).equals(perm.getName())) {
					throw new AccessControlException(
							format("Accessing the system environment variable [%s] is disallowed", DISALLOWED_PROPERTY_NAME));
				}
			}
		};
		System.setSecurityManager(securityManager);

		{
			Map<String, Object> systemEnvironment = environment.getSystemEnvironment();
			assertThat(systemEnvironment, notNullValue());
			assertThat(systemEnvironment, instanceOf(ReadOnlySystemAttributesMap.class));
			assertThat(systemEnvironment.get(ALLOWED_PROPERTY_NAME), equalTo((Object)ALLOWED_PROPERTY_VALUE));
			assertThat(systemEnvironment.get(DISALLOWED_PROPERTY_NAME), nullValue());
		}

		System.setSecurityManager(oldSecurityManager);
		getModifiableSystemEnvironment().remove(ALLOWED_PROPERTY_NAME);
		getModifiableSystemEnvironment().remove(DISALLOWED_PROPERTY_NAME);
	}

	@SuppressWarnings("unchecked")
	private static Map<String, String> getModifiableSystemEnvironment() {
		// for os x / linux
		Class<?>[] classes = Collections.class.getDeclaredClasses();
		Map<String, String> env = System.getenv();
		for (Class<?> cl : classes) {
			if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
				try {
					Field field = cl.getDeclaredField("m");
					field.setAccessible(true);
					Object obj = field.get(env);
					if (obj != null && obj.getClass().getName().equals("java.lang.ProcessEnvironment$StringEnvironment")) {
						return (Map<String, String>) obj;
					}
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		}

		// for windows
		Class<?> processEnvironmentClass;
		try {
			processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		try {
			Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
			theCaseInsensitiveEnvironmentField.setAccessible(true);
			Object obj = theCaseInsensitiveEnvironmentField.get(null);
			return (Map<String, String>) obj;
		} catch (NoSuchFieldException e) {
			// do nothing
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		try {
			Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
			theEnvironmentField.setAccessible(true);
			Object obj = theEnvironmentField.get(null);
			return (Map<String, String>) obj;
		} catch (NoSuchFieldException e) {
			// do nothing
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		throw new IllegalStateException();
	}
}
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:48)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:97)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:489)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7(Indexer.scala:361)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7$adapted(Indexer.scala:356)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1306)
	scala.collection.parallel.ParIterableLike$Foreach.leaf(ParIterableLike.scala:938)
	scala.collection.parallel.Task.$anonfun$tryLeaf$1(Tasks.scala:52)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.util.control.Breaks$$anon$1.catchBreak(Breaks.scala:97)
	scala.collection.parallel.Task.tryLeaf(Tasks.scala:55)
	scala.collection.parallel.Task.tryLeaf$(Tasks.scala:49)
	scala.collection.parallel.ParIterableLike$Foreach.tryLeaf(ParIterableLike.scala:935)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1499.java