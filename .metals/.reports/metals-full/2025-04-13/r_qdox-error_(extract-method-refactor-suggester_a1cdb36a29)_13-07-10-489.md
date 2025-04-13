error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1329.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1329.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1329.java
text:
```scala
T@@his utility class provides static methods for managing user authorization in a Derby database.

/*

   Derby - Class org.apache.derby.database.UserUtility

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

package org.apache.derby.database;
import org.apache.derby.iapi.db.PropertyInfo;
import org.apache.derby.iapi.store.access.TransactionController;
import org.apache.derby.iapi.util.IdUtil;
import org.apache.derby.iapi.error.StandardException;
import org.apache.derby.iapi.sql.conn.LanguageConnectionContext;
import org.apache.derby.iapi.sql.conn.ConnectionUtil;
import org.apache.derby.iapi.reference.SQLState;
import org.apache.derby.iapi.reference.Property;

import java.sql.SQLException;
import org.apache.derby.iapi.error.PublicAPI;

/**
  This utility class provides static methods for managing user authorization in a Cloudscape database.
  
   <p>This class can only be used within an SQL-J statement, a Java procedure or a server side Java method.
   <p>This class can be accessed using the class alias <code> USERUTILITY </code> in SQL-J statements.
  */
public abstract class UserUtility
{
	/** Enumeration value for read access permission ("READ_ACCESS_PERMISSION"). */
	public final static String READ_ACCESS_PERMISSION = "READ_ACCESS_PERMISSION";
	/** Enumeration value for full access permission ("FULL_ACCESS_PERMISSION"). */
	public final static String FULL_ACCESS_PERMISSION = "FULL_ACCESS_PERMISSION";

	/** Prevent users from creating UserUtility Objects. */
	private UserUtility() {}

	/**
	  Add a user's authorization permission to the database.

	  <P>
 	  Only users with FULL_ACCESS_PERMISSION may use this.
	  
	  @param userName the user's name. A valid possibly delimited
	  SQL identifier.
	  @param permission READ_ACCESS_PERMISSION or FULL_ACCESS_PERMISSION.
	  @exception SQLException thrown if this fails.
	  */
	public static final void add(String userName, String permission)
		 throws SQLException
	{
		String pv;
		TransactionController tc = ConnectionUtil.getCurrentLCC().getTransactionExecute();
		try {
		normalizeIdParam("userName",userName); //Validate
		if (permission==null)
			throw StandardException.newException(SQLState.UU_INVALID_PARAMETER, "permission","null");			
		if (permission.equals(READ_ACCESS_PERMISSION))
		{
			pv = (String)tc.getProperty(Property.READ_ONLY_ACCESS_USERS_PROPERTY);
			pv = IdUtil.appendId(userName,pv);
			PropertyInfo.setDatabaseProperty(Property.READ_ONLY_ACCESS_USERS_PROPERTY,pv);
		}
		else if (permission.equals(FULL_ACCESS_PERMISSION))
		{
			pv = (String)tc.getProperty(Property.FULL_ACCESS_USERS_PROPERTY);
			pv = IdUtil.appendId(userName,pv);
			PropertyInfo.setDatabaseProperty(Property.FULL_ACCESS_USERS_PROPERTY,pv);
		}
		else
			throw StandardException.newException(SQLState.UU_UNKNOWN_PERMISSION, permission);
		} catch (StandardException se) {
			throw PublicAPI.wrapStandardException(se);
		}
	}

	/**
	  Set the authorization permission for a user in the database.

	  <P>
	  Only users with FULL_ACCESS_PERMISSION may use this.

	  @param userName the user's name. A valid possibly delimited
	  SQL identifier.
	  @param permission READ_ACCESS_PERMISSION or FULL_ACCESS_PERMISSION.
	  @exception SQLException thrown if this fails.
	  */
	public static final void set(String userName, String permission)
		 throws SQLException
	{
		drop(userName);
		add(userName,permission);
	}

	/**
	  Drop a user's authorization permission from the database.

	  <P>
	  Only users with FULL_ACCESS_PERMISSION may use this.

	  @param userName the user's name. A valid possibly delimited
	  SQL identifier.

	  @exception SQLException thrown if this fails or the user
	  being dropped does not exist.
	  */
	public static final void drop(String userName) throws
	SQLException
	{
		TransactionController tc = ConnectionUtil.getCurrentLCC().getTransactionExecute();

		try {
		String userId = normalizeIdParam("userName",userName); 

		String access = getPermission(userName);
		if (access != null && access.equals(READ_ACCESS_PERMISSION))
		{
			String pv = (String)tc.getProperty(Property.READ_ONLY_ACCESS_USERS_PROPERTY);
			String newList = IdUtil.deleteId(userId,pv);
			PropertyInfo.setDatabaseProperty(Property.READ_ONLY_ACCESS_USERS_PROPERTY,newList);
		}
		else if (access != null && access.equals(FULL_ACCESS_PERMISSION))
		{
			String pv = (String)tc.getProperty(Property.FULL_ACCESS_USERS_PROPERTY);
			String newList = IdUtil.deleteId(userId,pv);
			PropertyInfo.setDatabaseProperty(Property.FULL_ACCESS_USERS_PROPERTY,newList);
		}
		else
		{
			throw StandardException.newException(SQLState.UU_UNKNOWN_USER, userName);
		}
		} catch (StandardException se) {
			throw PublicAPI.wrapStandardException(se);
		}
	}

	/**
	  Return a user's authorization permission in a database.

	  <P>
	  Users with FULL_ACCESS_PERMISSION or READ_ACCESS_PERMISSION
	  may use this.
	  
	  @param userName the user's name. A valid possibly delimited
	  SQL identifier.
	  @return FULL_ACCESS_PERMISSION if the user is in "derby.database.fullAccessUsers",
	          READ_ACCESS_PERMISSION if the user is in "derby.database.readOnlyAccessUsers",
			  or null if the user is not in either list.
	  @exception SQLException thrown if this fails.
	  */
	public static final String getPermission(String userName)
         throws SQLException
	{
		TransactionController tc = ConnectionUtil.getCurrentLCC().getTransactionExecute();

		try {

		String pv = (String)
			tc.getProperty(Property.READ_ONLY_ACCESS_USERS_PROPERTY);
		String userId = normalizeIdParam("userName",userName); 
		if (IdUtil.idOnList(userId,pv)) return READ_ACCESS_PERMISSION;
		pv = (String)tc.getProperty(Property.FULL_ACCESS_USERS_PROPERTY);
		if (IdUtil.idOnList(userId,pv)) return FULL_ACCESS_PERMISSION;
		return null;
		} catch (StandardException se) {
			throw PublicAPI.wrapStandardException(se);
		}
	}

	private static String normalizeIdParam(String pName, String pValue)
		 throws StandardException
	{
		if (pValue==null)
			throw StandardException.newException(SQLState.UU_INVALID_PARAMETER, pName,"null");
			
		try {
			return IdUtil.parseId(pValue);
		}
		catch (StandardException se) {
			throw StandardException.newException(SQLState.UU_INVALID_PARAMETER, se, pName,pValue);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1329.java