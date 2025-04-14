error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4460.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4460.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4460.java
text:
```scala
S@@tringBuilder backwards = new StringBuilder();

/*
 * Copyright 2004-2008 the original author or authors.
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
package org.springframework.expression.spel;

import java.awt.Color;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.expression.spel.standard.StandardEvaluationContext;
import org.springframework.expression.spel.testresources.Fruit;
import org.springframework.expression.spel.testresources.Inventor;
import org.springframework.expression.spel.testresources.Person;
import org.springframework.expression.spel.testresources.PlaceOfBirth;

/**
 * Builds an evaluation context for test expressions. Features of the test evaluation context are:
 * <ul>
 * <li>The root context object is an Inventor instance {@link Inventor}
 * </ul>
 */
public class TestScenarioCreator {

	public static StandardEvaluationContext getTestEvaluationContext() {
		StandardEvaluationContext testContext = new StandardEvaluationContext();
		setupRootContextObject(testContext);
		populateContextMap(testContext);
		createTestClassloader(testContext);
		populateVariables(testContext);
		populateFunctions(testContext);
		return testContext;
	}

	/**
	 * Register some Java reflect methods as well known functions that can be called from an expression.
	 * @param testContext the test evaluation context
	 */
	private static void populateFunctions(StandardEvaluationContext testContext) {
		try {
			testContext.registerFunction("isEven", TestScenarioCreator.class.getDeclaredMethod("isEven",
					new Class[] { Integer.TYPE }));
			testContext.registerFunction("reverseInt", TestScenarioCreator.class.getDeclaredMethod("reverseInt",
					new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE }));
			testContext.registerFunction("reverseString", TestScenarioCreator.class.getDeclaredMethod("reverseString",
					new Class[] { String.class }));
			testContext.registerFunction("varargsFunctionReverseStringsAndMerge", TestScenarioCreator.class
					.getDeclaredMethod("varargsFunctionReverseStringsAndMerge", new Class[] { String[].class }));
			testContext.registerFunction("varargsFunctionReverseStringsAndMerge2", TestScenarioCreator.class
					.getDeclaredMethod("varargsFunctionReverseStringsAndMerge2", new Class[] { Integer.TYPE,
							String[].class }));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Register some variables that can be referenced from the tests
	 * @param testContext the test evaluation context
	 */
	private static void populateVariables(StandardEvaluationContext testContext) {
		testContext.setVariable("answer", 42);
	}

	/**
	 * Include a testcode jar on the default context classpath so that tests can lookup entries in it.
	 * @param testContext the test evaluation context
	 */
	private static void createTestClassloader(StandardEvaluationContext testContext) {
		try {
			ClassLoader cl = new URLClassLoader(new URL[] { new File("target/test-classes/testcode.jar").toURI()
					.toURL() }, Thread.currentThread().getContextClassLoader());
			testContext.setClassLoader(cl);
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		}
	}

	/**
	 * Create the root context object, an Inventor instance. Non-qualified property and method references will be
	 * resolved against this context object.
	 *
	 * @param testContext the evaluation context in which to set the root object
	 */
	private static void setupRootContextObject(StandardEvaluationContext testContext) {
		GregorianCalendar c = new GregorianCalendar();
		c.set(1856, 7, 9);
		Inventor tesla = new Inventor("Nikola Tesla", c.getTime(), "Serbian");
		tesla.setPlaceOfBirth(new PlaceOfBirth("SmilJan"));
		tesla.setInventions(new String[] { "Telephone repeater", "Rotating magnetic field principle",
				"Polyphase alternating-current system", "Induction motor", "Alternating-current power transmission",
				"Tesla coil transformer", "Wireless communication", "Radio", "Fluorescent lights" });
		testContext.setRootObject(tesla);
	}

	/**
	 * Create a context configuration that tests can reference into using the
	 * @() language construct. at(context:objectName) will index a particular object within a particular context. The
	 * 'root' context will be used for references where no context is specified, eg.
	 * @(orange).
	 *
	 * @param testContext the evaluation context in which to register the new references
	 */
	private static void populateContextMap(StandardEvaluationContext testContext) {
		Map<String, Map<String, Object>> contextToReferencesMap = new HashMap<String, Map<String, Object>>();

		Person andy, christian, julie, stefanie, rob, rod, adrian;
		andy = new Person("Andy");
		christian = new Person("Christian");
		julie = new Person("Julie");
		stefanie = new Person("Stefanie");
		rod = new Person("Rod");
		rob = new Person("Rob");
		adrian = new Person("Adrian");

		Map<String, Object> people = new HashMap<String, Object>();
		people.put("Andy", andy);
		people.put("Christian", christian);
		people.put("Julie", julie);
		people.put("Stefanie", stefanie);

		Map<String, Object> colors = new HashMap<String, Object>();
		colors.put("red", Color.red);
		colors.put("orange", Color.orange);
		colors.put("yellow", Color.yellow);
		colors.put("green", Color.red);
		colors.put("blue", Color.orange);
		contextToReferencesMap.put("colors", colors);

		Map<String, Object> fruits = new HashMap<String, Object>();
		fruits.put("orange", new Fruit("Orange", Color.orange, "orange"));
		fruits.put("apple", new Fruit("Apple", Color.green, "green"));
		fruits.put("banana", new Fruit("Banana", Color.yellow, "yellow"));

		Map<String, Object> root = new HashMap<String, Object>();
		root.put("orange", new Fruit("Orange", Color.orange, "orange"));
		root.put("apple", new Fruit("Apple", Color.green, "green"));
		root.put("banana", new Fruit("Banana", Color.yellow, "yellow"));
		root.put("red", Color.red);
		root.put("orange", Color.orange);
		root.put("yellow", Color.yellow);
		root.put("green", Color.red);
		root.put("blue", Color.orange);
		root.put("Andy", andy);
		root.put("Christian", christian);
		root.put("Julie", julie);
		root.put("Stefanie", stefanie);
		root.put("Adrian", adrian);
		root.put("Rob", rob);
		root.put("Rod", rod);

		contextToReferencesMap.put("people", people);
		contextToReferencesMap.put("fruits", fruits);
		contextToReferencesMap.put("root", root); // used if no context
		// specified

		contextToReferencesMap.put("a.b.c", fruits);
		java.util.Set<String> contextKeys = contextToReferencesMap.keySet();
		for (String contextName : contextKeys) {
			Map<String, Object> elements = contextToReferencesMap.get(contextName);
			Set<String> objectNames = elements.keySet();
			for (String objectName : objectNames) {
				testContext.addReference(contextName, objectName, elements.get(objectName));
			}
		}
	}

	// These methods are registered in the test context and therefore accessible through function calls
	// in test expressions

	public static String isEven(int i) {
		if ((i % 2) == 0)
			return "y";
		return "n";
	}

	public static int[] reverseInt(int i, int j, int k) {
		return new int[] { k, j, i };
	}

	public static String reverseString(String input) {
		StringBuffer backwards = new StringBuffer();
		for (int i = 0; i < input.length(); i++) {
			backwards.append(input.charAt(input.length() - 1 - i));
		}
		return backwards.toString();
	}

	public static String varargsFunctionReverseStringsAndMerge(String... strings) {
		StringBuilder sb = new StringBuilder();
		if (strings != null) {
			for (int i = strings.length - 1; i >= 0; i--) {
				sb.append(strings[i]);
			}
		}
		return sb.toString();
	}

	public static String varargsFunctionReverseStringsAndMerge2(int j, String... strings) {
		StringBuilder sb = new StringBuilder();
		sb.append(j);
		if (strings != null) {
			for (int i = strings.length - 1; i >= 0; i--) {
				sb.append(strings[i]);
			}
		}
		return sb.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4460.java