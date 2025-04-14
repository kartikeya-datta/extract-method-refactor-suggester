error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3456.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3456.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3456.java
text:
```scala
e@@xtends Dependent

/*

   Derby - Class org.apache.derby.iapi.sql.PreparedStatement

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to you under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package org.apache.derby.iapi.sql;

import org.apache.derby.iapi.error.StandardException;

import org.apache.derby.iapi.sql.conn.LanguageConnectionContext;

import org.apache.derby.iapi.sql.depend.Dependent;
import org.apache.derby.iapi.sql.depend.Provider;

import org.apache.derby.iapi.types.DataTypeDescriptor;
import java.sql.Timestamp;
import java.sql.SQLWarning;

/**
 * The PreparedStatement interface provides methods to execute prepared
 * statements, store them, and get metadata about them.
 *
 */
public interface PreparedStatement
	extends Dependent, Provider
{

	/**
	 * Checks whether this PreparedStatement is up to date.
	 * A PreparedStatement can become out of date if any of several
	 * things happen:
	 *
	 *	A schema used by the statement is dropped
	 *	A table used by the statement is dropped
	 *	A table used by the statement, or a column in such a table,
	 *		is altered in one of several ways: a column is dropped,
	 *		a privilege is dropped, a constraint is added or
	 *		dropped, an index is dropped.
	 *	A view used by the statement is dropped.
	 *
	 * In general, anything that happened since the plan was generated
	 * that might cause the plan to fail, or to generate incorrect results,
	 * will cause this method to return FALSE.
	 *
	 * @return	TRUE if the PreparedStatement is up to date,
	 *		FALSE if it is not up to date
	 */
	boolean	upToDate() throws StandardException;

	/**
	 * Re-prepare the statement if it is not up to date or,
	 * if requested, simply not optimal.
	 * If there are open cursors using this prepared statement,
	 * then we will not be able to recompile the statement.
	 *
	 * @param lcc			The LanguageConnectionContext.
	 *
	 * @exception StandardException thrown if unable to perform
	 */
	void rePrepare(LanguageConnectionContext lcc) 
		throws StandardException;

	/**
	 * PreparedStatements are re-entrant - that is, more than one
	 * execution can be active at a time for a single prepared statement.
	 * An Activation contains all the local state information to
	 * execute a prepared statement (as opposed to the constant
	 * information, such as literal values and code). Each Activation
	 * class contains the code specific to the prepared statement
	 * represented by an instance of this class (PreparedStatement).
	 *
	 * @param lcc			The LanguageConnectionContext.
	 * @return	The new activation.
	 *
	 * @exception StandardException		Thrown on failure
	 */
	Activation	getActivation(LanguageConnectionContext lcc, boolean scrollable) throws StandardException;

	/**
	 * Execute the PreparedStatement and return results.
	 *<p>
	 * There is no executeQuery() or
	 * executeUpdate(); a method is provided in
	 * ResultSet to tell whether to expect rows to be returned.
	 *
	 * @param activation The activation containing all the local state
	 *		to execute the plan.
 	 * @param rollbackParentContext True if 1) the statement context is
	 *  NOT a top-level context, AND 2) in the event of a statement-level
	 *	 exception, the parent context needs to be rolled back, too.
     * @param timeoutMillis timeout value in milliseconds.
	 *
	 * @return	A ResultSet for a statement. A ResultSet represents
	 *		the results returned from the statement, if any.
	 *		Will return NULL if the plan for the PreparedStatement
	 *		has aged out of cache, or the plan is out of date.
	 *
	 * @exception StandardException		Thrown on failure
	 */
    ResultSet execute(Activation activation,
                      boolean rollbackParentContext,
                      long timeoutMillis)
        throws StandardException;

	/**
		Simple form of execute(). Creates a new single use activation and executes it,
		but also passes rollbackParentContext parameter (see above).
	*/
    ResultSet execute(LanguageConnectionContext lcc,
                      boolean rollbackParentContext,
                      long timeoutMillis)
        throws StandardException;

	/**
	 * Get the ResultDescription for the statement.  The ResultDescription
	 * describes what the results look like: what are the rows and columns?
	 * <p>
	 * This is available here and on the ResultSet so that users can
	 * see the shape of the result before they execute.
	 *
	 * @return	A ResultDescription describing the results.
	 *
	 */
	ResultDescription	getResultDescription();

	/**
	 * Return true if the query node for this statement references SESSION schema tables.
	 *
	 * @return	true if references SESSION schema tables, else false
	 */
	boolean referencesSessionSchema();

	/**
	 * Get an array of DataTypeDescriptors describing the types of the
	 * parameters of this PreparedStatement. The Nth element of the array
	 * describes the Nth parameter.
	 *
	 * @return		An array of DataTypeDescriptors telling the
	 *			type, length, precision, scale, etc. of each
	 *			parameter of this PreparedStatement.
	 */
	DataTypeDescriptor[]	getParameterTypes();

	/**
	 *	Return the SQL string that this statement is for.
	 *
	 *	@return the SQL string this statement is for.
	 */
	String getSource();

	/**
	 *	Return the SPS Name for this statement.
	 *
	 *	@return the SPS Name for this statement
	 */
	String getSPSName();

	/**
	 * Get the total compile time for the associated query in milliseconds.
	 * Compile time can be divided into parse, bind, optimize and generate times.
	 * 
	 * @return long		The total compile time for the associated query in milliseconds.
	 */
	public long getCompileTimeInMillis();

	/**
	 * Get the parse time for the associated query in milliseconds.
	 * 
	 * @return long		The parse time for the associated query in milliseconds.
	 */
	public long getParseTimeInMillis();

	/**
	 * Get the bind time for the associated query in milliseconds.
	 * 
	 * @return long		The bind time for the associated query in milliseconds.
	 */
	public long getBindTimeInMillis();

	/**
	 * Get the optimize time for the associated query in milliseconds.
	 * 
	 * @return long		The optimize time for the associated query in milliseconds.
	 */
	public long getOptimizeTimeInMillis();

	/**
	 * Get the generate time for the associated query in milliseconds.
	 * 
	 * @return long		The generate time for the associated query in milliseconds.
	 */
	public long getGenerateTimeInMillis();

	/**
	 * Get the timestamp for the beginning of compilation
	 *
	 * @return Timestamp	The timestamp for the beginning of compilation.
	 */
	public Timestamp getBeginCompileTimestamp();

	/**
	 * Get the timestamp for the end of compilation
	 *
	 * @return Timestamp	The timestamp for the end of compilation.
	 */
	public Timestamp getEndCompileTimestamp();

	/**
	 * Returns whether or not this Statement requires should
	 * behave atomically -- i.e. whether a user is permitted
	 * to do a commit/rollback during the execution of this
	 * statement.
	 *
	 * @return boolean	Whether or not this Statement is atomic
	 */
	boolean isAtomic();

	/**
		Return any compile time warnings. Null if no warnings exist.
	*/
	public SQLWarning getCompileTimeWarnings();

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3456.java