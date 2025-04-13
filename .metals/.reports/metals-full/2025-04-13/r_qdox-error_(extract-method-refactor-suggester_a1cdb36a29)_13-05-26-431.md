error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2983.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2983.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2983.java
text:
```scala
i@@f (e.getMessage() == null) {

/*

 Derby - Class org.apache.derbyTesting.system.nstest.init.Initializer

 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 */

package org.apache.derbyTesting.system.nstest.init;

import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;

import org.apache.derbyTesting.system.nstest.NsTest;
import org.apache.derbyTesting.system.nstest.utils.DbUtil;

/**
 * Initializer: Main Class that populates the tables needed for the test
 */
public class Initializer {

	private String thread_id;

	private DbUtil dbutil;

	public Initializer(String name) {
		this.thread_id = name;
		dbutil = new DbUtil(this.thread_id);
	}

	// This starts the acutal inserts
	public void startInserts() {

		Connection conn = null;
		int insertsRemaining = NsTest.MAX_INITIAL_ROWS;

		// The JDBC driver should have been loaded by nstest.java at this
		// point, we just need
		// to get a connection to the database
		try {

			System.out.println(thread_id
					+ " is getting a connection to the database...");

			if (NsTest.embeddedMode) {
				conn = DriverManager.getConnection(NsTest.embedDbURL,
						NsTest.prop);
			} else {
				if(NsTest.driver_type.equalsIgnoreCase("DerbyClient")) {
					System.out.println("-->Using derby client url");
					conn = DriverManager.getConnection(NsTest.clientDbURL,
							NsTest.prop);
				}
			}
		} catch (Exception e) {
			System.out.println("FAIL: " + thread_id
					+ " could not get the database connection");
			printException("getting database connection in startInserts()", e);
		}

		// add one to the statistics of client side connections made per jvm
		NsTest.addStats(NsTest.CONNECTIONS_MADE, 1);
		System.out.println("Connection number: " + NsTest.numConnections);

		// set autocommit to false to keep transaction control in your hand
		if (NsTest.AUTO_COMMIT_OFF) {
			try {

				conn.setAutoCommit(false);
			} catch (Exception e) {
				System.out.println("FAIL: " + thread_id
						+ "'s setAutoCommit() failed:");
				printException("setAutoCommit() in Initializer", e);
			}
		}

		while (insertsRemaining-- >= 0) {
			try {
				int numInserts = dbutil.add_one_row(conn, thread_id);
				System.out.println("Intializer.java: exited add_one_row: "
						+ numInserts + " rows");
			} catch (Exception e) {
				System.out.println(" FAIL: " + thread_id
						+ " unexpected exception:");
				printException("add_one_row() in Initializer", e);
				break;
			}
		}// end of while(insertsRemaning-- > 0)

		// commit the huge bulk Insert!
		if (NsTest.AUTO_COMMIT_OFF) {
			try {
				conn.commit();
			} catch (Exception e) {
				System.out
						.println("FAIL: " + thread_id + "'s commit() failed:");
				printException("commit in Initializer", e);
			}
		}

	}// end of startInserts()

	// ** This method abstracts exception message printing for all exception
	// messages. You may want to change
	// ****it if more detailed exception messages are desired.
	// ***Method is synchronized so that the output file will contain sensible
	// stack traces that are not
	// ****mixed but rather one exception printed at a time
	public synchronized void printException(String where, Exception e) {
		if (e instanceof SQLException) {
			SQLException se = (SQLException) e;

			if (se.getSQLState().equals("40001"))
				System.out.println("deadlocked detected");
			if (se.getSQLState().equals("40XL1"))
				System.out.println(" lock timeout exception");
			if (se.getSQLState().equals("23500"))
				System.out.println(" duplicate key violation");
			if (se.getNextException() != null) {
				String m = se.getNextException().getSQLState();
				System.out.println(se.getNextException().getMessage()
						+ " SQLSTATE: " + m);
			}
		}
		if (e.getMessage().equals(null)) {
			e.printStackTrace(System.out);
		}
		System.out.println("During - " + where
				+ ", the exception thrown was : " + e.getMessage());
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2983.java