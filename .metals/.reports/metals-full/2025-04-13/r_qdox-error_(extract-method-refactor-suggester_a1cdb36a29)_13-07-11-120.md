error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10619.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10619.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10619.java
text:
```scala
A@@ssert.notNull(populator, "The DatabasePopulator is required");

/*
 * Copyright 2002-2009 the original author or authors.
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
package org.springframework.jdbc.datasource.embedded;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

/**
 * Returns a {@link EmbeddedDatabase} instance pre-populated with test data.
 * When the database is returned, callers are guaranteed that the database schema and test data will have already been loaded.
 * 
 * Can be configured.
 * Call {@link #setDatabaseName(String)} to change the name of the  database.
 * Call {@link #setDatabaseType(EmbeddedDatabaseType)} to set the database type if you wish to use one of the supported types.
 * Call {@link #setDatabaseConfigurer(EmbeddedDatabaseConfigurer)} to set a configuration strategy for your own embedded database type.
 * Call {@link #setDatabasePopulator(DatabasePopulator)} to change the algorithm used to populate the database.
 * Call {@link #setDataSourceFactory(DataSourceFactory)} to change the type of DataSource used to connect to the database.
 * Call {@link #getDatabase()} to get the {@link EmbeddedDatabase} instance.
 * @author Keith Donald
 */
public class EmbeddedDatabaseFactory {

	private static Log logger = LogFactory.getLog(EmbeddedDatabaseFactory.class);

	private String databaseName;

	private DataSourceFactory dataSourceFactory;
	
	private EmbeddedDatabaseConfigurer databaseConfigurer;

	private DatabasePopulator databasePopulator;

	private EmbeddedDatabase database;
	
	/**
	 * Creates a default {@link EmbeddedDatabaseFactory}.
	 * Calling {@link #getDatabase()} will create a embedded HSQL database of name 'testdb'.
	 */
	public EmbeddedDatabaseFactory() {
		setDatabaseName("testdb");
		setDatabaseType(EmbeddedDatabaseType.HSQL);
		setDataSourceFactory(new SimpleDriverDataSourceFactory());
	}
	
	/**
	 * Sets the name of the test database.
	 * Defaults to 'testdb'.
	 * @param name of the test database
	 */
	public void setDatabaseName(String name) {
		Assert.notNull(name, "The testDatabaseName is required");
		databaseName = name;
	}
	
	/**
	 * Sets the type of embedded database to use.
	 * Call this when you wish to configure one of the pre-supported types.
	 * Defaults to HSQL.
	 * @param type the test database type
	 */
	public void setDatabaseType(EmbeddedDatabaseType type) {
		setDatabaseConfigurer(EmbeddedDatabaseConfigurerFactory.getConfigurer(type));
	}

	/**
	 * Sets the strategy that will be used to configure the embedded database instance.
	 * Call this when you wish to use an embedded database type not already supported.
	 * @param configurer the embedded database configurer
	 */
	public void setDatabaseConfigurer(EmbeddedDatabaseConfigurer configurer) {
		this.databaseConfigurer = configurer;
	}
	
	/**
	 * Sets the strategy that will be used to populate the embedded database.
	 * Defaults to null.
	 * @param populator the database populator 
	 */
	public void setDatabasePopulator(DatabasePopulator populator) {
		Assert.notNull(populator, "The TestDatabasePopulator is required");
		databasePopulator = populator;
	}

	/**
	 * Sets the factory to use to create the DataSource instance that connects to the embedded database.
	 * Defaults to {@link SimpleDriverDataSourceFactory}.
	 * @param dataSourceFactory the data source factory
	 */
	public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
		Assert.notNull(dataSourceFactory, "The DataSourceFactory is required");
		this.dataSourceFactory = dataSourceFactory;
	}

	// other public methods

	/**
	 * Factory method that returns the embedded database instance.
	 */
	public EmbeddedDatabase getDatabase() {
		if (database == null) {
			initDatabase();
		}
		return database;
	}
	
	// internal helper methods

	private void initDatabase() {
		// create the embedded database source first
		database = new EmbeddedDataSourceProxy(createDataSource());
		if (logger.isInfoEnabled()) {
			logger.info("Created embedded database '" + databaseName + "'");
		}
		if (databasePopulator != null) {
			// now populate the database
			populateDatabase();
		}
	}

	private DataSource createDataSource() {
		databaseConfigurer.configureConnectionProperties(dataSourceFactory.getConnectionProperties(), databaseName);
		return dataSourceFactory.getDataSource();
	}

	private void populateDatabase() {
		TransactionTemplate template = new TransactionTemplate(new DataSourceTransactionManager(database));
		template.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				databasePopulator.populate(new JdbcTemplate(database));
			}
		});
	}
	
	class EmbeddedDataSourceProxy implements EmbeddedDatabase {
		private DataSource dataSource;

		public EmbeddedDataSourceProxy(DataSource dataSource) {
			this.dataSource = dataSource;
		}

		public Connection getConnection() throws SQLException {
			return dataSource.getConnection();
		}

		public Connection getConnection(String username, String password)
				throws SQLException {
			return dataSource.getConnection(username, password);
		}

		public int getLoginTimeout() throws SQLException {
			return dataSource.getLoginTimeout();
		}

		public PrintWriter getLogWriter() throws SQLException {
			return dataSource.getLogWriter();
		}

		public void setLoginTimeout(int seconds) throws SQLException {
			dataSource.setLoginTimeout(seconds);
		}

		public void setLogWriter(PrintWriter out) throws SQLException {
			dataSource.setLogWriter(out);
		}

		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			return dataSource.isWrapperFor(iface);
		}

		public <T> T unwrap(Class<T> iface) throws SQLException {
			return dataSource.unwrap(iface);
		}
		
		public void shutdown() {
			shutdownDatabase();
		}

	}
	
	private void shutdownDatabase() {
		if (database != null) {
			databaseConfigurer.shutdown(database);
			database = null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10619.java