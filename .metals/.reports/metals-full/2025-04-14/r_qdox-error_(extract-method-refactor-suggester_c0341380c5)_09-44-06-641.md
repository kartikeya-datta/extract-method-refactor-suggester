error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11998.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11998.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11998.java
text:
```scala
r@@eturn this.jdbcTemplate.queryForObject("SELECT COUNT(0) FROM " + tableName, Integer.class);

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

package org.springframework.test;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.core.io.support.EncodedResource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

/**
 * Subclass of AbstractTransactionalSpringContextTests that adds some convenience
 * functionality for JDBC access. Expects a {@link javax.sql.DataSource} bean
 * to be defined in the Spring application context.
 *
 * <p>This class exposes a {@link org.springframework.jdbc.core.JdbcTemplate}
 * and provides an easy way to delete from the database in a new transaction.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Thomas Risberg
 * @since 1.1.1
 * @see #setDataSource(javax.sql.DataSource)
 * @see #getJdbcTemplate()
 * @deprecated as of Spring 3.0, in favor of using the listener-based test context framework
 * ({@link org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests})
 */
@Deprecated
public abstract class AbstractTransactionalDataSourceSpringContextTests
	extends AbstractTransactionalSpringContextTests {

	protected JdbcTemplate jdbcTemplate;

	private String sqlScriptEncoding;

	/**
	 * Did this test delete any tables? If so, we forbid transaction completion,
	 * and only allow rollback.
	 */
	private boolean zappedTables;


	/**
	 * Default constructor for AbstractTransactionalDataSourceSpringContextTests.
	 */
	public AbstractTransactionalDataSourceSpringContextTests() {
	}

	/**
	 * Constructor for AbstractTransactionalDataSourceSpringContextTests with a JUnit name.
	 */
	public AbstractTransactionalDataSourceSpringContextTests(String name) {
		super(name);
	}


	/**
	 * Setter: DataSource is provided by Dependency Injection.
	 */
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * Return the JdbcTemplate that this base class manages.
	 */
	public final JdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}

	/**
	 * Specify the encoding for SQL scripts, if different from the platform encoding.
	 * @see #executeSqlScript
	 */
	public void setSqlScriptEncoding(String sqlScriptEncoding) {
		this.sqlScriptEncoding = sqlScriptEncoding;
	}


	/**
	 * Convenient method to delete all rows from these tables.
	 * Calling this method will make avoidance of rollback by calling
	 * {@code setComplete()} impossible.
	 * @see #setComplete
	 */
	protected void deleteFromTables(String[] names) {
		for (int i = 0; i < names.length; i++) {
			int rowCount = this.jdbcTemplate.update("DELETE FROM " + names[i]);
			if (logger.isInfoEnabled()) {
				logger.info("Deleted " + rowCount + " rows from table " + names[i]);
			}
		}
		this.zappedTables = true;
	}

	/**
	 * Overridden to prevent the transaction committing if a number of tables have been
	 * cleared, as a defensive measure against accidental <i>permanent</i> wiping of a database.
	 * @see org.springframework.test.AbstractTransactionalSpringContextTests#setComplete()
	 */
	@Override
	protected final void setComplete() {
		if (this.zappedTables) {
			throw new IllegalStateException("Cannot set complete after deleting tables");
		}
		super.setComplete();
	}

	/**
	 * Count the rows in the given table
	 * @param tableName table name to count rows in
	 * @return the number of rows in the table
	 */
	protected int countRowsInTable(String tableName) {
		return this.jdbcTemplate.queryForInt("SELECT COUNT(0) FROM " + tableName);
	}


	/**
	 * Execute the given SQL script. Will be rolled back by default,
	 * according to the fate of the current transaction.
	 * @param sqlResourcePath Spring resource path for the SQL script.
	 * Should normally be loaded by classpath.
	 * <p>Statements should be delimited with a semicolon.  If statements are not delimited with
	 * a semicolon then there should be one statement per line.  Statements are allowed to span
	 * lines only if they are delimited with a semicolon.
	 * <p><b>Do not use this method to execute DDL if you expect rollback.</b>
	 * @param continueOnError whether or not to continue without throwing
	 * an exception in the event of an error
	 * @throws DataAccessException if there is an error executing a statement
	 * and continueOnError was false
	 */
	protected void executeSqlScript(String sqlResourcePath, boolean continueOnError) throws DataAccessException {
		if (logger.isInfoEnabled()) {
			logger.info("Executing SQL script '" + sqlResourcePath + "'");
		}

		EncodedResource resource =
				new EncodedResource(getApplicationContext().getResource(sqlResourcePath), this.sqlScriptEncoding);
		long startTime = System.currentTimeMillis();
		List statements = new LinkedList();
		try {
			LineNumberReader lnr = new LineNumberReader(resource.getReader());
			String script = JdbcTestUtils.readScript(lnr);
			char delimiter = ';';
			if (!JdbcTestUtils.containsSqlScriptDelimiters(script, delimiter)) {
				delimiter = '\n';
			}
			JdbcTestUtils.splitSqlScript(script, delimiter, statements);
			for (Iterator itr = statements.iterator(); itr.hasNext(); ) {
				String statement = (String) itr.next();
				try {
					int rowsAffected = this.jdbcTemplate.update(statement);
					if (logger.isDebugEnabled()) {
						logger.debug(rowsAffected + " rows affected by SQL: " + statement);
					}
				}
				catch (DataAccessException ex) {
					if (continueOnError) {
						if (logger.isWarnEnabled()) {
							logger.warn("SQL: " + statement + " failed", ex);
						}
					}
					else {
						throw ex;
					}
				}
			}
			long elapsedTime = System.currentTimeMillis() - startTime;
			logger.info("Done executing SQL scriptBuilder '" + sqlResourcePath + "' in " + elapsedTime + " ms");
		}
		catch (IOException ex) {
			throw new DataAccessResourceFailureException("Failed to open SQL script '" + sqlResourcePath + "'", ex);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11998.java