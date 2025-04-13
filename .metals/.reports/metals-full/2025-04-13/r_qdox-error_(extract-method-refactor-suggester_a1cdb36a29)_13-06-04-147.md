error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13066.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13066.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13066.java
text:
```scala
e@@qualTo(CommandLinePropertySource.COMMAND_LINE_PROPERTY_SOURCE_NAME));

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

package org.springframework.core.env;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

/**
 * Unit tests for {@link SimpleCommandLinePropertySource}.
 *
 * @author Chris Beams
 * @since 3.1
 */
public class SimpleCommandLinePropertySourceTests {

	@Test
	public void withDefaultName() {
		PropertySource<?> ps = new SimpleCommandLinePropertySource();
		assertThat(ps.getName(),
				equalTo(CommandLinePropertySource.DEFAULT_COMMAND_LINE_PROPERTY_SOURCE_NAME));
	}

	@Test
	public void withCustomName() {
		PropertySource<?> ps = new SimpleCommandLinePropertySource("ps1", new String[0]);
		assertThat(ps.getName(), equalTo("ps1"));
	}

	@Test
	public void withNoArgs() {
		PropertySource<?> ps = new SimpleCommandLinePropertySource();
		assertThat(ps.containsProperty("foo"), is(false));
		assertThat(ps.getProperty("foo"), nullValue());
	}

	@Test
	public void withOptionArgsOnly() {
		CommandLinePropertySource<?> ps =
			new SimpleCommandLinePropertySource("--o1=v1", "--o2");
		assertThat(ps.containsProperty("o1"), is(true));
		assertThat(ps.containsProperty("o2"), is(true));
		assertThat(ps.containsProperty("o3"), is(false));
		assertThat(ps.getProperty("o1"), equalTo("v1"));
		assertThat(ps.getProperty("o2"), equalTo(""));
		assertThat(ps.getProperty("o3"), nullValue());
	}

	@Test
	public void withDefaultNonOptionArgsNameAndNoNonOptionArgsPresent() {
		PropertySource<?> ps = new SimpleCommandLinePropertySource("--o1=v1", "--o2");

		assertThat(ps.containsProperty("nonOptionArgs"), is(false));
		assertThat(ps.containsProperty("o1"), is(true));
		assertThat(ps.containsProperty("o2"), is(true));

		assertThat(ps.containsProperty("nonOptionArgs"), is(false));
		assertThat(ps.getProperty("nonOptionArgs"), nullValue());
	}

	@Test
	public void withDefaultNonOptionArgsNameAndNonOptionArgsPresent() {
		CommandLinePropertySource<?> ps =
			new SimpleCommandLinePropertySource("--o1=v1", "noa1", "--o2", "noa2");

		assertThat(ps.containsProperty("nonOptionArgs"), is(true));
		assertThat(ps.containsProperty("o1"), is(true));
		assertThat(ps.containsProperty("o2"), is(true));

		String nonOptionArgs = ps.getProperty("nonOptionArgs");
		assertThat(nonOptionArgs, equalTo("noa1,noa2"));
	}

	@Test
	public void withCustomNonOptionArgsNameAndNoNonOptionArgsPresent() {
		CommandLinePropertySource<?> ps =
			new SimpleCommandLinePropertySource("--o1=v1", "noa1", "--o2", "noa2");
		ps.setNonOptionArgsPropertyName("NOA");

		assertThat(ps.containsProperty("nonOptionArgs"), is(false));
		assertThat(ps.containsProperty("NOA"), is(true));
		assertThat(ps.containsProperty("o1"), is(true));
		assertThat(ps.containsProperty("o2"), is(true));
		String nonOptionArgs = ps.getProperty("NOA");
		assertThat(nonOptionArgs, equalTo("noa1,noa2"));
	}

	@Test
	public void covertNonOptionArgsToStringArrayAndList() {
		CommandLinePropertySource<?> ps =
			new SimpleCommandLinePropertySource("--o1=v1", "noa1", "--o2", "noa2");
		StandardEnvironment env = new StandardEnvironment();
		env.getPropertySources().addFirst(ps);

		String nonOptionArgs = env.getProperty("nonOptionArgs");
		assertThat(nonOptionArgs, equalTo("noa1,noa2"));

		String[] nonOptionArgsArray = env.getProperty("nonOptionArgs", String[].class);
		assertThat(nonOptionArgsArray[0], equalTo("noa1"));
		assertThat(nonOptionArgsArray[1], equalTo("noa2"));

		@SuppressWarnings("unchecked")
		List<String> nonOptionArgsList = env.getProperty("nonOptionArgs", List.class);
		assertThat(nonOptionArgsList.get(0), equalTo("noa1"));
		assertThat(nonOptionArgsList.get(1), equalTo("noa2"));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13066.java