error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6268.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6268.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6268.java
text:
```scala
r@@eturn Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

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

package org.springframework.jdbc.datasource.embedded;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.util.Assert;

/**
 * Creates a {@link EmbeddedDatabase} instance. Callers are guaranteed that the returned database has been fully
 * initialized and populated.
 *
 * <p>Can be configured:<br>
 * Call {@link #setDatabaseName(String)} to change the name of the database.<br>
 * Call {@link #setDatabaseType(EmbeddedDatabaseType)} to set the database type if you wish to use one of the supported types.<br>
 * Call {@link #setDatabaseConfigurer(EmbeddedDatabaseConfigurer)} to configure support for your own embedded database type.<br>
 * Call {@link #setDatabasePopulator(DatabasePopulator)} to change the algorithm used to populate the database.<br>
 * Call {@link #setDataSourceFactory(DataSourceFactory)} to change the type of DataSource used to connect to the database.<br>
 * Call {@link #getDatabase()} to get the {@link EmbeddedDatabase} instance.<br>
 *
 * @author Keith Donald
 * @author Juergen Hoeller
 * @since 3.0
 */
public class EmbeddedDatabaseFactory {

	private static Log logger = LogFactory.getLog(EmbeddedDatabaseFactory.class);

	private String databaseName = "testdb";

	private DataSourceFactory dataSourceFactory = new SimpleDriverDataSourceFactory();

	private EmbeddedDatabaseConfigurer databaseConfigurer;

	private DatabasePopulator databasePopulator;

	private DataSource dataSource;


	/**
	 * Set the name of the database. Defaults to "testdb".
	 * @param databaseName name of the test database
	 */
	public void setDatabaseName(String databaseName) {
		Assert.notNull(databaseName, "Database name is required");
		this.databaseName = databaseName;
	}

	/**
	 * Set the type of embedded database to use. Call this when you wish to configure
	 * one of the pre-supported types. Defaults to HSQL.
	 * @param type the test database type
	 */
	public void setDatabaseType(EmbeddedDatabaseType type) {
		this.databaseConfigurer = EmbeddedDatabaseConfigurerFactory.getConfigurer(type);
	}

	/**
	 * Set the strategy that will be used to configure the embedded database instance.
	 * Call this when you wish to use an embedded database type not already supported.
	 * @param configurer the embedded database configurer
	 */
	public void setDatabaseConfigurer(EmbeddedDatabaseConfigurer configurer) {
		Assert.notNull(configurer, "EmbeddedDatabaseConfigurer is required");
		this.databaseConfigurer = configurer;
	}

	/**
	 * Set the strategy that will be used to populate the embedded database. Defaults to null.
	 * @param populator the database populator
	 */
	public void setDatabasePopulator(DatabasePopulator populator) {
		Assert.notNull(populator, "DatabasePopulator is required");
		this.databasePopulator = populator;
	}

	/**
	 * Set the factory to use to create the DataSource instance that connects to the embedded database.
	 * Defaults to {@link SimpleDriverDataSourceFactory}.
	 * @param dataSourceFactory the data source factory
	 */
	public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
		Assert.notNull(dataSourceFactory, "DataSourceFactory is required");
		this.dataSourceFactory = dataSourceFactory;
	}

	/**
	 * Factory method that returns the embedded database instance.
	 */
	public EmbeddedDatabase getDatabase() {
		if (this.dataSource == null) {
			initDatabase();
		}
		return new EmbeddedDataSourceProxy(this.dataSource);
	}


	/**
	 * Hook to initialize the embedded database. Subclasses may call to force initialization. After calling this method,
	 * {@link #getDataSource()} returns the DataSource providing connectivity to the db.
	 */
	protected void initDatabase() {
		// Create the embedded database source first
		if (logger.isInfoEnabled()) {
			logger.info("Creating embedded database '" + this.databaseName + "'");
		}
		if (this.databaseConfigurer == null) {
			this.databaseConfigurer = EmbeddedDatabaseConfigurerFactory.getConfigurer(EmbeddedDatabaseType.HSQL);
		}
		this.databaseConfigurer.configureConnectionProperties(
				this.dataSourceFactory.getConnectionProperties(), this.databaseName);
		this.dataSource = this.dataSourceFactory.getDataSource();

		// Now populate the database
		if (this.databasePopulator != null) {
			try {
				populateDatabase();
			}
			catch (RuntimeException ex) {
				// failed to populate, so leave it as not initialized
				shutdownDatabase();
				throw ex;
			}
		}
	}

	private void populateDatabase() {
		try {
			Connection connection = this.dataSource.getConnection();
			try {
				this.databasePopulator.populate(connection);
			}
			finally {
				try {
					connection.close();
				}
				catch (SQLException ex) {
					// ignore
				}
			}
		}
		catch (Exception ex) {
			throw new DataAccessResourceFailureException("Failed to populate database", ex);
		}
	}

	/**
	 * Hook that gets the DataSource that provides the connectivity to the embedded database.
	 * <p>Returns null if the DataSource has not been initialized or the database has been shut down.
	 * Subclasses may call to access the datasource instance directly.
	 */
	protected DataSource getDataSource() {
		return this.dataSource;
	}

	/**
	 * Hook to shutdown the embedded database. Subclasses may call to force shutdown.
	 * After calling, {@link #getDataSource()} returns null. Does nothing if no embedded database has been initialized.
	 */
	protected void shutdownDatabase() {
		if (this.dataSource != null) {
			this.databaseConfigurer.shutdown(this.dataSource, this.databaseName);
			this.dataSource = null;
		}
	}


	private class EmbeddedDataSourceProxy implements EmbeddedDatabase {

		private final DataSource dataSource;

		public EmbeddedDataSourceProxy(DataSource dataSource) {
			this.dataSource = dataSource;
		}

		public Connection getConnection() throws SQLException {
			return this.dataSource.getConnection();
		}

		public Connection getConnection(String username, String password) throws SQLException {
			return this.dataSource.getConnection(username, password);
		}

		public PrintWriter getLogWriter() throws SQLException {
			return this.dataSource.getLogWriter();
		}

		public void setLogWriter(PrintWriter out) throws SQLException {
			this.dataSource.setLogWriter(out);
		}

		public int getLoginTimeout() throws SQLException {
			return this.dataSource.getLoginTimeout();
		}

		public void setLoginTimeout(int seconds) throws SQLException {
			this.dataSource.setLoginTimeout(seconds);
		}

		public <T> T unwrap(Class<T> iface) throws SQLException {
			return this.dataSource.unwrap(iface);
		}

		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			return this.dataSource.isWrapperFor(iface);
		}

		public Logger getParentLogger() {
			return Logger.getGlobal();
		}

		public void shutdown() {
			shutdownDatabase();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6268.java