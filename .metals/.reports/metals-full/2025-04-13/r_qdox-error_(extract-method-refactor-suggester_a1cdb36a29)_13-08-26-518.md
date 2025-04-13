error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2377.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2377.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2377.java
text:
```scala
i@@f (dbname.length() == 0 || (encryptDB && encryptpassword == null)) {

/*

   Derby - Class org.apache.derby.jdbc.Driver20

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

package org.apache.derby.jdbc;

import org.apache.derby.iapi.reference.Attribute;
import org.apache.derby.iapi.reference.MessageId;
import org.apache.derby.iapi.reference.Property;
import org.apache.derby.iapi.reference.SQLState;

import org.apache.derby.impl.jdbc.EmbedConnection;

import org.apache.derby.iapi.services.sanity.SanityManager;
import org.apache.derby.iapi.error.StandardException;
import org.apache.derby.iapi.sql.ResultSet;
import org.apache.derby.iapi.jdbc.BrokeredConnection;
import org.apache.derby.iapi.jdbc.BrokeredConnectionControl;
import org.apache.derby.iapi.services.i18n.MessageService;
import org.apache.derby.iapi.services.monitor.Monitor;
import org.apache.derby.iapi.services.io.FormatableProperties;
import org.apache.derby.iapi.security.SecurityUtil;

import org.apache.derby.impl.jdbc.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;

import java.security.Permission;
import java.security.AccessControlException;

import java.util.Properties;

/**
	This class extends the local JDBC driver in order to determine at JBMS
	boot-up if the JVM that runs us does support JDBC 2.0. If it is the case
	then we will load the appropriate class(es) that have JDBC 2.0 new public
	methods and sql types.
*/

public abstract class Driver20 extends InternalDriver implements Driver {

	private static final String[] BOOLEAN_CHOICES = {"false", "true"};

	private Class  antiGCDriverManager;

	/*
	**	Methods from ModuleControl
	*/

	public void boot(boolean create, Properties properties) throws StandardException {

		super.boot(create, properties);

		// Register with the driver manager
		AutoloadedDriver.registerDriverModule( this );

		// hold onto the driver manager to avoid its being garbage collected.
		// make sure the class is loaded by using .class
		antiGCDriverManager = java.sql.DriverManager.class;
	}

	public void stop() {

		super.stop();

		AutoloadedDriver.unregisterDriverModule();
	}
  
	public org.apache.derby.impl.jdbc.EmbedResultSet 
	newEmbedResultSet(EmbedConnection conn, ResultSet results, boolean forMetaData, org.apache.derby.impl.jdbc.EmbedStatement statement, boolean isAtomic)
		throws SQLException
	{
		return new EmbedResultSet20(conn, results, forMetaData, statement,
								 isAtomic); 
	}
	public abstract BrokeredConnection newBrokeredConnection(BrokeredConnectionControl control);
    /**
     * <p>The getPropertyInfo method is intended to allow a generic GUI tool to 
     * discover what properties it should prompt a human for in order to get 
     * enough information to connect to a database.  Note that depending on
     * the values the human has supplied so far, additional values may become
     * necessary, so it may be necessary to iterate though several calls
     * to getPropertyInfo.
     *
     * @param url The URL of the database to connect to.
     * @param info A proposed list of tag/value pairs that will be sent on
     *          connect open.
     * @return An array of DriverPropertyInfo objects describing possible
     *          properties.  This array may be an empty array if no properties
     *          are required.
     * @exception SQLException if a database-access error occurs.
     */
	public  DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {

		// RESOLVE other properties should be added into this method in the future ... 

        if (info != null) {
			if (Boolean.valueOf(info.getProperty(Attribute.SHUTDOWN_ATTR)).booleanValue()) {
	
				// no other options possible when shutdown is set to be true
				return new DriverPropertyInfo[0];
			}
		}

		// at this point we have databaseName, 

		String dbname = InternalDriver.getDatabaseName(url, info);

		// convert the ;name=value attributes in the URL into
		// properties.
		FormatableProperties finfo = getAttributes(url, info);
		info = null; // ensure we don't use this reference directly again.
		boolean encryptDB = Boolean.valueOf(finfo.getProperty(Attribute.DATA_ENCRYPTION)).booleanValue();		
		String encryptpassword = finfo.getProperty(Attribute.BOOT_PASSWORD);

		if (dbname.length() == 0 || (encryptDB = true && encryptpassword == null)) {

			// with no database name we can have shutdown or a database name

			// In future, if any new attribute info needs to be included in this
			// method, it just has to be added to either string or boolean or secret array
			// depending on whether it accepts string or boolean or secret(ie passwords) value. 

			String[][] connStringAttributes = {
				{Attribute.DBNAME_ATTR, MessageId.CONN_DATABASE_IDENTITY},
				{Attribute.CRYPTO_PROVIDER, MessageId.CONN_CRYPTO_PROVIDER},
				{Attribute.CRYPTO_ALGORITHM, MessageId.CONN_CRYPTO_ALGORITHM},
				{Attribute.CRYPTO_KEY_LENGTH, MessageId.CONN_CRYPTO_KEY_LENGTH},
				{Attribute.CRYPTO_EXTERNAL_KEY, MessageId.CONN_CRYPTO_EXTERNAL_KEY},
				{Attribute.TERRITORY, MessageId.CONN_LOCALE},
				{Attribute.COLLATION, MessageId.CONN_COLLATION},
				{Attribute.USERNAME_ATTR, MessageId.CONN_USERNAME_ATTR},
				{Attribute.LOG_DEVICE, MessageId.CONN_LOG_DEVICE},
				{Attribute.ROLL_FORWARD_RECOVERY_FROM, MessageId.CONN_ROLL_FORWARD_RECOVERY_FROM},
				{Attribute.CREATE_FROM, MessageId.CONN_CREATE_FROM},
				{Attribute.RESTORE_FROM, MessageId.CONN_RESTORE_FROM},
			};

			String[][] connBooleanAttributes = {
				{Attribute.SHUTDOWN_ATTR, MessageId.CONN_SHUT_DOWN_CLOUDSCAPE},
                {Attribute.DEREGISTER_ATTR, MessageId.CONN_DEREGISTER_AUTOLOADEDDRIVER},
				{Attribute.CREATE_ATTR, MessageId.CONN_CREATE_DATABASE},
				{Attribute.DATA_ENCRYPTION, MessageId.CONN_DATA_ENCRYPTION},
				{Attribute.UPGRADE_ATTR, MessageId.CONN_UPGRADE_DATABASE},
				};

			String[][] connStringSecretAttributes = {
				{Attribute.BOOT_PASSWORD, MessageId.CONN_BOOT_PASSWORD},
				{Attribute.PASSWORD_ATTR, MessageId.CONN_PASSWORD_ATTR},
				};

			
			DriverPropertyInfo[] optionsNoDB = new 	DriverPropertyInfo[connStringAttributes.length+
																	  connBooleanAttributes.length+
			                                                          connStringSecretAttributes.length];
			
			int attrIndex = 0;
			for( int i = 0; i < connStringAttributes.length; i++, attrIndex++ )
			{
				optionsNoDB[attrIndex] = new DriverPropertyInfo(connStringAttributes[i][0], 
									  finfo.getProperty(connStringAttributes[i][0]));
				optionsNoDB[attrIndex].description = MessageService.getTextMessage(connStringAttributes[i][1]);
			}

			optionsNoDB[0].choices = Monitor.getMonitor().getServiceList(Property.DATABASE_MODULE);
			// since database name is not stored in FormatableProperties, we
			// assign here explicitly
			optionsNoDB[0].value = dbname;

			for( int i = 0; i < connStringSecretAttributes.length; i++, attrIndex++ )
			{
				optionsNoDB[attrIndex] = new DriverPropertyInfo(connStringSecretAttributes[i][0], 
									  (finfo.getProperty(connStringSecretAttributes[i][0]) == null? "" : "****"));
				optionsNoDB[attrIndex].description = MessageService.getTextMessage(connStringSecretAttributes[i][1]);
			}

			for( int i = 0; i < connBooleanAttributes.length; i++, attrIndex++ )
			{
				optionsNoDB[attrIndex] = new DriverPropertyInfo(connBooleanAttributes[i][0], 
           		    Boolean.valueOf(finfo == null? "" : finfo.getProperty(connBooleanAttributes[i][0])).toString());
				optionsNoDB[attrIndex].description = MessageService.getTextMessage(connBooleanAttributes[i][1]);
				optionsNoDB[attrIndex].choices = BOOLEAN_CHOICES;				
			}

			return optionsNoDB;
		}

		return new DriverPropertyInfo[0];
	}

    /**
     * Checks for System Privileges.
     *
     * @param user The user to be checked for having the permission
     * @param perm The permission to be checked
     * @throws AccessControlException if permissions are missing
     * @throws Exception if the privileges check fails for some other reason
     */
    public void checkSystemPrivileges(String user,
                                      Permission perm)
        throws Exception {
        SecurityUtil.checkUserHasPermission(user, perm);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2377.java