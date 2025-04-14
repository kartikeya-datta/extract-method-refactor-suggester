error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6455.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6455.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6455.java
text:
```scala
a@@ssertEquals(1, t.queryForInt("select count(*) from T_TEST"));

/*
 * Copyright 2002-2012 the original author or authors.
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

package org.springframework.jdbc.config;

import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.Assert.*;

/**
 * @author Dave Syer
 */
public class InitializeDatabaseIntegrationTests {

	private String enabled;
	private ClassPathXmlApplicationContext context;

	@Before
	public void init() {
		enabled = System.setProperty("ENABLED", "true");
	}

	@After
	public void after() {
		if (enabled != null) {
			System.setProperty("ENABLED", enabled);
		} else {
			System.clearProperty("ENABLED");
		}
		if (context != null) {
			context.close();
		}
	}

	@Test
	public void testCreateEmbeddedDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("org/springframework/jdbc/config/jdbc-initialize-config.xml");
		assertCorrectSetup(context.getBean("dataSource", DataSource.class));
	}

	@Test(expected = BadSqlGrammarException.class)
	public void testDisableCreateEmbeddedDatabase() throws Exception {
		System.setProperty("ENABLED", "false");
		context = new ClassPathXmlApplicationContext("org/springframework/jdbc/config/jdbc-initialize-config.xml");
		assertCorrectSetup(context.getBean("dataSource", DataSource.class));
	}

	@Test
	public void testIgnoreFailedDrops() throws Exception {
		context = new ClassPathXmlApplicationContext("org/springframework/jdbc/config/jdbc-initialize-fail-config.xml");
		assertCorrectSetup(context.getBean("dataSource", DataSource.class));
	}

	@Test
	public void testScriptNameWithPattern() throws Exception {
		context = new ClassPathXmlApplicationContext("org/springframework/jdbc/config/jdbc-initialize-pattern-config.xml");
		DataSource dataSource = context.getBean("dataSource", DataSource.class);
		assertCorrectSetup(dataSource);
		JdbcTemplate t = new JdbcTemplate(dataSource);
		assertEquals("Dave", t.queryForObject("select name from T_TEST", String.class));
	}

	@Test
	public void testScriptNameWithPlaceholder() throws Exception {
		context = new ClassPathXmlApplicationContext("org/springframework/jdbc/config/jdbc-initialize-placeholder-config.xml");
		DataSource dataSource = context.getBean("dataSource", DataSource.class);
		assertCorrectSetup(dataSource);
	}

	@Test
	public void testScriptNameWithExpressions() throws Exception {
		context = new ClassPathXmlApplicationContext("org/springframework/jdbc/config/jdbc-initialize-expression-config.xml");
		DataSource dataSource = context.getBean("dataSource", DataSource.class);
		assertCorrectSetup(dataSource);
	}

	@Test
	public void testCacheInitialization() throws Exception {
		context = new ClassPathXmlApplicationContext("org/springframework/jdbc/config/jdbc-initialize-cache-config.xml");
		assertCorrectSetup(context.getBean("dataSource", DataSource.class));
		CacheData cache = context.getBean(CacheData.class);
		assertEquals(1, cache.getCachedData().size());
	}

	private void assertCorrectSetup(DataSource dataSource) {
		JdbcTemplate t = new JdbcTemplate(dataSource);
		assertEquals(1, t.queryForObject("select count(*) from T_TEST", Integer.class).intValue());
	}

	public static class CacheData implements InitializingBean {

		private JdbcTemplate jdbcTemplate;
		private List<Map<String,Object>> cache;

		public void setDataSource(DataSource dataSource) {
			this.jdbcTemplate = new JdbcTemplate(dataSource);
		}

		public List<Map<String,Object>> getCachedData() {
			return cache;
		}

		@Override
		public void afterPropertiesSet() throws Exception {
			cache = jdbcTemplate.queryForList("SELECT * FROM T_TEST");
		}

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6455.java