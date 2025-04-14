error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10568.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10568.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10568.java
text:
```scala
public P@@latformTransactionManager annotationDrivenTransactionManager() {

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

package org.springframework.transaction.annotation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.config.AdviceMode;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.CallCountingTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.BeanFactoryTransactionAttributeSourceAdvisor;

/**
 * Integration tests for the @EnableTransactionManagement annotation.
 *
 * @author Chris Beams
 * @since 3.1
 */
public class EnableTransactionManagementIntegrationTests {

	@Test
	public void repositoryIsNotTxProxy() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(Config.class);
		ctx.refresh();

		try {
			assertTxProxying(ctx);
			fail("expected exception");
		} catch (AssertionError ex) {
			assertThat(ex.getMessage(), equalTo("FooRepository is not a TX proxy"));
		}
	}

	@Test
	public void repositoryIsTxProxy_withDefaultTxManagerName() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(Config.class, DefaultTxManagerNameConfig.class);
		ctx.refresh();

		assertTxProxying(ctx);
	}

	@Test
	public void repositoryIsTxProxy_withCustomTxManagerName() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(Config.class, CustomTxManagerNameConfig.class);
		ctx.refresh();

		assertTxProxying(ctx);
	}

	@Ignore @Test // TODO SPR-8207
	public void repositoryIsTxProxy_withNonConventionalTxManagerName_fallsBackToByTypeLookup() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(Config.class, NonConventionalTxManagerNameConfig.class);
		ctx.refresh();

		assertTxProxying(ctx);
	}

	@Test
	public void repositoryIsClassBasedTxProxy() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(Config.class, ProxyTargetClassTxConfig.class);
		ctx.refresh();

		assertTxProxying(ctx);
		assertThat(AopUtils.isCglibProxy(ctx.getBean(FooRepository.class)), is(true));
	}

	@Test
	public void repositoryUsesAspectJAdviceMode() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(Config.class, AspectJTxConfig.class);
		try {
			ctx.refresh();
		} catch (Exception ex) {
			// this test is a bit fragile, but gets the job done, proving that an
			// attempt was made to look up the AJ aspect. It's due to classpath issues
			// in .integration-tests that it's not found.
			assertTrue(ex.getMessage().endsWith("AspectJTransactionManagementConfiguration.class] cannot be opened because it does not exist"));
		}
	}

	@Test
	public void implicitTxManager() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(ImplicitTxManagerConfig.class);
		ctx.refresh();

		FooRepository fooRepository = ctx.getBean(FooRepository.class);
		fooRepository.findAll();

		CallCountingTransactionManager txManager = ctx.getBean(CallCountingTransactionManager.class);
		assertThat(txManager.begun, equalTo(1));
		assertThat(txManager.commits, equalTo(1));
		assertThat(txManager.rollbacks, equalTo(0));
	}

	@Test
	public void explicitTxManager() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(ExplicitTxManagerConfig.class);
		ctx.refresh();

		FooRepository fooRepository = ctx.getBean(FooRepository.class);
		fooRepository.findAll();

		CallCountingTransactionManager txManager1 = ctx.getBean("txManager1", CallCountingTransactionManager.class);
		assertThat(txManager1.begun, equalTo(1));
		assertThat(txManager1.commits, equalTo(1));
		assertThat(txManager1.rollbacks, equalTo(0));

		CallCountingTransactionManager txManager2 = ctx.getBean("txManager2", CallCountingTransactionManager.class);
		assertThat(txManager2.begun, equalTo(0));
		assertThat(txManager2.commits, equalTo(0));
		assertThat(txManager2.rollbacks, equalTo(0));
	}


	@Configuration
	@EnableTransactionManagement
	static class ImplicitTxManagerConfig {
		@Bean
		public PlatformTransactionManager txManager() {
			return new CallCountingTransactionManager();
		}

		@Bean
		public FooRepository fooRepository() {
			return new DummyFooRepository();
		}
	}


	@Configuration
	@EnableTransactionManagement
	static class ExplicitTxManagerConfig implements TransactionManagementConfigurer {
		@Bean
		public PlatformTransactionManager txManager1() {
			return new CallCountingTransactionManager();
		}

		@Bean
		public PlatformTransactionManager txManager2() {
			return new CallCountingTransactionManager();
		}

		public PlatformTransactionManager createTransactionManager() {
			return txManager1();
		}

		@Bean
		public FooRepository fooRepository() {
			return new DummyFooRepository();
		}
	}

	private void assertTxProxying(AnnotationConfigApplicationContext ctx) {
		FooRepository repo = ctx.getBean(FooRepository.class);

		boolean isTxProxy = false;
		if (AopUtils.isAopProxy(repo)) {
			for (Advisor advisor : ((Advised)repo).getAdvisors()) {
				if (advisor instanceof BeanFactoryTransactionAttributeSourceAdvisor) {
					isTxProxy = true;
					break;
				}
			}
		}
		assertTrue("FooRepository is not a TX proxy", isTxProxy);

		// trigger a transaction
		repo.findAll();
	}


	@Configuration
	@EnableTransactionManagement
	static class DefaultTxManagerNameConfig {
		@Bean
		PlatformTransactionManager transactionManager(DataSource dataSource) {
			return new DataSourceTransactionManager(dataSource);
		}
	}


	@Configuration
	@EnableTransactionManagement
	static class CustomTxManagerNameConfig {
		@Bean
		PlatformTransactionManager txManager(DataSource dataSource) {
			return new DataSourceTransactionManager(dataSource);
		}
	}


	@Configuration
	@EnableTransactionManagement
	static class NonConventionalTxManagerNameConfig {
		@Bean
		PlatformTransactionManager txManager(DataSource dataSource) {
			return new DataSourceTransactionManager(dataSource);
		}
	}


	@Configuration
	@EnableTransactionManagement(proxyTargetClass=true)
	static class ProxyTargetClassTxConfig {
		@Bean
		PlatformTransactionManager transactionManager(DataSource dataSource) {
			return new DataSourceTransactionManager(dataSource);
		}
	}


	@Configuration
	@EnableTransactionManagement(mode=AdviceMode.ASPECTJ)
	static class AspectJTxConfig {
		@Bean
		PlatformTransactionManager transactionManager(DataSource dataSource) {
			return new DataSourceTransactionManager(dataSource);
		}
	}


	@Configuration
	static class Config {
		@Bean
		FooRepository fooRepository() {
			JdbcFooRepository repos = new JdbcFooRepository();
			repos.setDataSource(dataSource());
			return repos;
		}

		@Bean
		DataSource dataSource() {
			return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.HSQL)
				.build();
		}
	}


	interface FooRepository {
		List<Object> findAll();
	}


	@Repository
	static class JdbcFooRepository implements FooRepository {

		public void setDataSource(DataSource dataSource) {
		}

		@Transactional
		public List<Object> findAll() {
			return Collections.emptyList();
		}
	}

	@Repository
	static class DummyFooRepository implements FooRepository {

		@Transactional
		public List<Object> findAll() {
			return Collections.emptyList();
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10568.java