error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/778.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/778.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/778.java
text:
```scala
<@@LI> JDBC 3.0 - Java 2 - JDK 1.4, J2SE 5.0

/*

   Derby - Class org.apache.derby.jdbc.EmbeddedXADataSource

   Copyright 2001, 2004 The Apache Software Foundation or its licensors, as applicable.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package org.apache.derby.jdbc;

import org.apache.derby.iapi.reference.MessageId;
import org.apache.derby.iapi.services.i18n.MessageService;

import org.apache.derby.iapi.services.monitor.Monitor;
import org.apache.derby.iapi.jdbc.ResourceAdapter;
import org.apache.derby.iapi.db.Database;

import org.apache.derby.iapi.reference.Property;

import org.apache.derby.iapi.error.ExceptionSeverity;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Driver;


/** -- jdbc 2.0. extension -- */
import javax.sql.XADataSource;
import javax.sql.XAConnection;


/**

	EmbeddedXADataSource is Derby's XADataSource implementation.
	

	<P>An XADataSource is a factory for XAConnection objects.  It represents a
	RM in a DTP environment.  An object that implements the XADataSource
	interface is typically registered with a JNDI service provider.   	
	<P>
	EmbeddedXADataSource automatically supports the correct JDBC specification version
	for the Java Virtual Machine's environment.
	<UL>
	<LI> JDBC 3.0 - Java 2 - JDK 1.4
	<LI> JDBC 2.0 - Java 2 - JDK 1.2,1.3
	</UL>

	<P>EmbeddedXADataSource object only works on a local database.  There is no
	client/server support.  An EmbeddedXADataSource object must live in the same jvm as
	the database. 

	<P>EmbeddedXADataSource is serializable and referenceable.

	<P>See EmbeddedDataSource for DataSource properties.

 */
public class EmbeddedXADataSource extends EmbeddedDataSource implements
				javax.sql.XADataSource
{

	private static final long serialVersionUID = -5715798975598379738L;

	// link to the database
	transient private ResourceAdapter ra;
  
	/**
	  no-arg constructor
	*/
	public EmbeddedXADataSource() 
	{
		super();
	}


	/*
	 * XADataSource methods 
	 */


	/**
	 * Attempt to establish a database connection.
	 *
	 * @return  a Connection to the database
	 * @exception SQLException if a database-access error occurs.
	 */
	public final XAConnection getXAConnection() throws SQLException
	{
		if (ra == null || !ra.isActive())
			setupResourceAdapter(null, null, false);

		return new EmbedXAConnection(this, ra, getUser(), getPassword(), false);
	}

	/**
	 * Attempt to establish a database connection with the given user
	 * name and password.
	 *
	 * @param user the database user on whose behalf the Connection is being made
	 * @param password the user's password
	 * @return  a Connection to the database
	 * @exception SQLException if a database-access error occurs.
	 */
	public final XAConnection getXAConnection(String user, String password)
		 throws SQLException 
	{
		if (ra == null || !ra.isActive())
			setupResourceAdapter(user, password, true);

		return new EmbedXAConnection(this, ra, user, password, true);
	}
	
	/*
	 * private method
	 */

	void update() {
		ra = null;
		super.update();
	}

	private void setupResourceAdapter(String user, String password, boolean requestPassword) throws SQLException
	{
		synchronized(this)
		{
			if (ra == null || !ra.isActive())
			{
				// If it is inactive, it is useless.
				ra = null;

				String dbName = getDatabaseName();
				if (dbName != null) {

					// see if database already booted, if it is, then don't make a
					// connection. 
					Database database = null;

					// if monitor is never setup by any ModuleControl, getMonitor
					// returns null and no cloudscape database has been booted. 
					if (Monitor.getMonitor() != null)
						database = (Database)
							Monitor.findService(Property.DATABASE_MODULE, dbName);

					if (database == null)
					{
						// If database is not found, try connecting to it.  This
						// boots and/or creates the database.  If database cannot
						// be found, this throws SQLException.
						if (requestPassword)
							getConnection(user, password).close();
						else
							getConnection().close();

						// now try to find it again
						database = (Database)
							Monitor.findService(Property.DATABASE_MODULE, dbName); 
					}

					if (database != null)
						ra = (ResourceAdapter) database.getResourceAdapter();
				}

				if (ra == null)
					throw new SQLException(MessageService.getTextMessage(MessageId.CORE_DATABASE_NOT_AVAILABLE),
										   "08006",
										   ExceptionSeverity.DATABASE_SEVERITY);


				// If database is already up, we need to set up driver
				// seperately. 
				findDriver();

				if (driver == null)
					throw new SQLException(MessageService.getTextMessage(MessageId.CORE_DRIVER_NOT_AVAILABLE),
										   "08006",
										   ExceptionSeverity.DATABASE_SEVERITY);

			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/778.java