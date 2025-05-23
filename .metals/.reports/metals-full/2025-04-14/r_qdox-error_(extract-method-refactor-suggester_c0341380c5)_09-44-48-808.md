error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1829.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1829.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1829.java
text:
```scala
c@@ = ' ';

/*
 * Copyright 2002-2010 the original author or authors.
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

package org.springframework.jdbc.datasource.init;

import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.StringUtils;

/**
 * Populates a database from SQL scripts defined in external resources.
 *
 * <p>Call {@link #addScript(Resource)} to add a SQL script location.<br>
 * Call {@link #setSqlScriptEncoding(String)} to set the encoding for all added scripts.<br>
 *
 * @author Keith Donald
 * @author Dave Syer
 * @author Juergen Hoeller
 * @since 3.0
 */
public class ResourceDatabasePopulator implements DatabasePopulator {

	private static String DEFAULT_COMMENT_PREFIX = "--";

	private static final Log logger = LogFactory.getLog(ResourceDatabasePopulator.class);


	private List<Resource> scripts = new ArrayList<Resource>();

	private String sqlScriptEncoding;

	private String commentPrefix = DEFAULT_COMMENT_PREFIX;

	private boolean continueOnError = false;

	private boolean ignoreFailedDrops = false;


	/**
	 * Add a script to execute to populate the database.
	 * @param script the path to a SQL script
	 */
	public void addScript(Resource script) {
		this.scripts.add(script);
	}

	/**
	 * Set the scripts to execute to populate the database.
	 * @param scripts the scripts to execute
	 */
	public void setScripts(Resource[] scripts) {
		this.scripts = Arrays.asList(scripts);
	}

	/**
	 * Specify the encoding for SQL scripts, if different from the platform encoding.
	 * Note setting this property has no effect on added scripts that are already
	 * {@link EncodedResource encoded resources}.
	 * @see #addScript(Resource)
	 */
	public void setSqlScriptEncoding(String sqlScriptEncoding) {
		this.sqlScriptEncoding = sqlScriptEncoding;
	}

	/**
	 * Set the line prefix that identifies comments in the SQL script.
	 * Default is "--".
	 */
	public void setCommentPrefix(String commentPrefix) {
		this.commentPrefix = commentPrefix;
	}

	/**
	 * Flag to indicate that all failures in SQL should be logged but not cause a failure.
	 * Defaults to false.
	 */
	public void setContinueOnError(boolean continueOnError) {
		this.continueOnError = continueOnError;
	}

	/**
	 * Flag to indicate that a failed SQL <code>DROP</code> statement can be ignored.  
	 * <p>This is useful for non-embedded databases whose SQL dialect does not support an
	 * <code>IF EXISTS</code> clause in a <code>DROP</code>. The default is false so that if the
	 * populator runs accidentally, it will fail fast when the script starts with a <code>DROP</code>.
	 */
	public void setIgnoreFailedDrops(boolean ignoreFailedDrops) {
		this.ignoreFailedDrops = ignoreFailedDrops;
	}


	public void populate(Connection connection) throws SQLException {
		for (Resource script : this.scripts) {
			executeSqlScript(connection, applyEncodingIfNecessary(script), this.continueOnError, this.ignoreFailedDrops);
		}
	}

	private EncodedResource applyEncodingIfNecessary(Resource script) {
		if (script instanceof EncodedResource) {
			return (EncodedResource) script;
		}
		else {
			return new EncodedResource(script, this.sqlScriptEncoding);
		}
	}

	/**
	 * Execute the given SQL script. <p>The script will normally be loaded by classpath. There should be one statement
	 * per line. Any semicolons will be removed. <b>Do not use this method to execute DDL if you expect rollback.</b>
	 * @param connection the JDBC Connection with which to perform JDBC operations
	 * @param resource the resource (potentially associated with a specific encoding) to load the SQL script from.
	 * @param continueOnError whether or not to continue without throwing an exception in the event of an error.
	 * @param ignoreFailedDrops whether of not to continue in thw event of specifically an error on a <code>DROP</code>.
	 */
	private void executeSqlScript(Connection connection, EncodedResource resource, boolean continueOnError, boolean ignoreFailedDrops)
			throws SQLException {

		if (logger.isInfoEnabled()) {
			logger.info("Executing SQL script from " + resource);
		}
		long startTime = System.currentTimeMillis();
		List<String> statements = new LinkedList<String>();
		String script;
		try {
			script = readScript(resource);
		}
		catch (IOException ex) {
			throw new CannotReadScriptException(resource, ex);
		}
		char delimiter = ';';
		if (!containsSqlScriptDelimiters(script, delimiter)) {
			delimiter = '\n';
		}
		splitSqlScript(script, delimiter, statements);
		int lineNumber = 0;
		Statement stmt = connection.createStatement();
		try {
			for (String statement : statements) {
				lineNumber++;
				try {
					int rowsAffected = stmt.executeUpdate(statement);
					if (logger.isDebugEnabled()) {
						logger.debug(rowsAffected + " rows affected by SQL: " + statement);
					}
				}
				catch (SQLException ex) {
					boolean dropStatement = statement.trim().toLowerCase().startsWith("drop");
					if (continueOnError || (dropStatement && ignoreFailedDrops)) {
						if (logger.isDebugEnabled()) {
							logger.debug("Line " + lineNumber + " statement failed: " + statement, ex);
						}
					}
					else {
						throw ex;
					}
				}
			}
		}
		finally {
			try {
				stmt.close();
			}
			catch (Throwable ex) {
				logger.debug("Could not close JDBC Statement", ex);
			}
		}
		long elapsedTime = System.currentTimeMillis() - startTime;
		if (logger.isInfoEnabled()) {
			logger.info("Done executing SQL script from " + resource + " in " + elapsedTime + " ms.");
		}
	}

	/**
	 * Read a script from the given resource and build a String containing the lines.
	 * @param resource the resource to be read
	 * @return <code>String</code> containing the script lines
	 * @throws IOException in case of I/O errors
	 */
	private String readScript(EncodedResource resource) throws IOException {
		LineNumberReader lnr = new LineNumberReader(resource.getReader());
		String currentStatement = lnr.readLine();
		StringBuilder scriptBuilder = new StringBuilder();
		while (currentStatement != null) {
			if (StringUtils.hasText(currentStatement) &&
					(this.commentPrefix != null && !currentStatement.startsWith(this.commentPrefix))) {
				if (scriptBuilder.length() > 0) {
					scriptBuilder.append('\n');
				}
				scriptBuilder.append(currentStatement);
			}
			currentStatement = lnr.readLine();
		}
		return scriptBuilder.toString();
	}

	/**
	 * Does the provided SQL script contain the specified delimiter?
	 * @param script the SQL script
	 * @param delim character delimiting each statement - typically a ';' character
	 */
	private boolean containsSqlScriptDelimiters(String script, char delim) {
		boolean inLiteral = false;
		char[] content = script.toCharArray();
		for (int i = 0; i < script.length(); i++) {
			if (content[i] == '\'') {
				inLiteral = !inLiteral;
			}
			if (content[i] == delim && !inLiteral) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Split an SQL script into separate statements delimited with the provided delimiter character.
	 * Each individual statement will be added to the provided <code>List</code>.
	 * @param script the SQL script
	 * @param delim character delimiting each statement (typically a ';' character)
	 * @param statements the List that will contain the individual statements
	 */
	private void splitSqlScript(String script, char delim, List<String> statements) {
		StringBuilder sb = new StringBuilder();
		boolean inLiteral = false;
		char[] content = script.toCharArray();
		for (int i = 0; i < script.length(); i++) {
			char c = content[i];
			if (c == '\'') {
				inLiteral = !inLiteral;
			}
			if (!inLiteral) {
				if (c == delim) {
					if (sb.length() > 0) {
						statements.add(sb.toString());
						sb = new StringBuilder();
					}
					continue;
				}
				else if (c == '\n' || c == '\t') {
					continue;
				}
			}
			sb.append(c);
		}
		if (StringUtils.hasText(sb)) {
			statements.add(sb.toString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1829.java