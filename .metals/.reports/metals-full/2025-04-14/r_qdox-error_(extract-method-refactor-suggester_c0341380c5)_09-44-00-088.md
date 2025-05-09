error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3472.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3472.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3472.java
text:
```scala
public v@@oid setDatabaseEncrypted(boolean flushLog)

/*

   Derby - Class org.apache.derby.impl.store.raw.log.ReadOnly

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

package org.apache.derby.impl.store.raw.log;

import org.apache.derby.iapi.reference.SQLState;

import org.apache.derby.iapi.services.monitor.ModuleControl;
import org.apache.derby.iapi.services.monitor.ModuleSupportable;
import org.apache.derby.iapi.services.sanity.SanityManager;
import org.apache.derby.iapi.services.io.Formatable;

import org.apache.derby.iapi.services.property.PersistentSet;
import org.apache.derby.iapi.store.raw.Compensation;
import org.apache.derby.iapi.store.raw.Loggable;
import org.apache.derby.iapi.store.raw.RawStoreFactory;
import org.apache.derby.iapi.store.raw.ScanHandle;
import org.apache.derby.iapi.store.raw.log.LogFactory;
import org.apache.derby.iapi.store.raw.log.LogInstant;
import org.apache.derby.iapi.store.raw.log.Logger;
import org.apache.derby.iapi.store.raw.log.LogScan;

import org.apache.derby.iapi.store.raw.data.DataFactory;
import org.apache.derby.iapi.store.raw.xact.TransactionFactory;
import org.apache.derby.iapi.store.raw.xact.RawTransaction;
import org.apache.derby.iapi.store.raw.xact.TransactionId;

import org.apache.derby.iapi.error.StandardException;

import org.apache.derby.io.StorageFile;
import org.apache.derby.iapi.store.access.DatabaseInstant;
import org.apache.derby.catalog.UUID;

import java.util.Properties;
import java.io.File;

/**
	A read-only version of the log factory.
	It doesn't do anything, it doesn't check that
	the database needs recovery or not.
	<P>
	It doesn't handle undo.  No recovery.

	<P>Multithreading considerations:<BR>
	This class must be MT-safe.
*/

public class ReadOnly implements LogFactory, ModuleSupportable {

	private String logArchiveDirectory = null;

	/* 
	** Methods of Log Factory
	*/

	public Logger getLogger() {
		return null;
	}

	/**
	  MT - not needed, no work is done
	  @exception StandardException Cloudscape Standard Error Policy
	*/
	public void recover(RawStoreFactory rawStoreFactory,
						DataFactory dataFactory,
						TransactionFactory transactionFactory)
		 throws StandardException
	{
		if (transactionFactory != null)
			transactionFactory.useTransactionTable((Formatable)null);
	}

	/**
	  MT - not needed, no work is done
	*/
	public boolean checkpoint(RawStoreFactory rawStoreFactory,
							  DataFactory dataFactory,
							  TransactionFactory transactionFactory,
							  boolean wait)
	{
		return true;
	}

	public StandardException markCorrupt(StandardException originalError) {
		return originalError;
	}

	public void flush(LogInstant where) throws StandardException {
	}

	/*
	** Methods of ModuleControl
	*/

	public boolean canSupport(Properties startParams) {

		String runtimeLogAttributes = startParams.getProperty(LogFactory.RUNTIME_ATTRIBUTES);
		if (runtimeLogAttributes == null)
			return false;

		return runtimeLogAttributes.equals(LogFactory.RT_READONLY);
	}

	/*
	 * truncation point support (not supported)
	 */

	public LogInstant setTruncationLWM(UUID name,
									   LogInstant instant,
									   RawStoreFactory rawStoreFactory, 
									  TransactionFactory transFactory)
		 throws StandardException
	{
		if (SanityManager.DEBUG)
			SanityManager.THROWASSERT("functionality not implemented");

        throw StandardException.newException(
                SQLState.STORE_FEATURE_NOT_IMPLEMENTED);

	}

	/**
	  @exception StandardException functionality not implmented
	*/
	public void setTruncationLWM(UUID name, LogInstant instant) throws StandardException
	{
		if (SanityManager.DEBUG)
			SanityManager.THROWASSERT("functionality not implemented");

        throw StandardException.newException(
                SQLState.STORE_FEATURE_NOT_IMPLEMENTED);
	}


	/**
	  @exception StandardException functionality not implmented
	*/
	public void removeTruncationLWM(UUID name,
							 RawStoreFactory rawStoreFactory, 
							 TransactionFactory transFactory)
		 throws StandardException
	{
		if (SanityManager.DEBUG)
			SanityManager.THROWASSERT("functionality not implemented");

        throw StandardException.newException(
                SQLState.STORE_FEATURE_NOT_IMPLEMENTED);
	}


	/**
	  @exception StandardException functionality not implmented
	*/
	public LogInstant getTruncationLWM(UUID name) throws StandardException
	{
		if (SanityManager.DEBUG)
			SanityManager.THROWASSERT("functionality not implemented");

        throw StandardException.newException(
                SQLState.STORE_FEATURE_NOT_IMPLEMENTED);
	}

	/**
	  @exception StandardException functionality not implmented
	*/
	public void removeTruncationLWM(UUID name) throws StandardException
	{
		if (SanityManager.DEBUG)
			SanityManager.THROWASSERT("functionality not implemented");

        throw StandardException.newException(
                SQLState.STORE_FEATURE_NOT_IMPLEMENTED);
	}

	/**
	  @exception StandardException functionality not implmented
	*/
	public ScanHandle openFlushedScan(DatabaseInstant i, int groupsIWant)
		 throws StandardException
	{
		if (SanityManager.DEBUG)
			SanityManager.THROWASSERT("functionality not implemented");

        throw StandardException.newException(
                SQLState.STORE_FEATURE_NOT_IMPLEMENTED);
	}

	/**
	  @exception StandardException functionality not implmented
	*/
	public LogScan openForwardsScan(LogInstant startAt,LogInstant stopAt)
		 throws StandardException
	{
		if (SanityManager.DEBUG)
			SanityManager.THROWASSERT("functionality not implemented");

        throw StandardException.newException(
                SQLState.STORE_FEATURE_NOT_IMPLEMENTED);
	}

	/**
	  */
    public LogInstant getFirstUnflushedInstant()
	{
		if (SanityManager.DEBUG)
			SanityManager.THROWASSERT("functionality not implemented");

		return null;
	}

	/**
	  @exception StandardException functionality not implmented
	  */
	public LogScan openForwardsFlushedScan(LogInstant startAt)
		 throws StandardException
	{
		if (SanityManager.DEBUG)
			SanityManager.THROWASSERT("functionality not implemented");

        throw StandardException.newException(
                SQLState.STORE_FEATURE_NOT_IMPLEMENTED);
	}

	/**
	 * Backup restore - stop sending log record to the log stream
	 * @exception StandardException Standard Cloudscape error policy
	 */
	public void freezePersistentStore() throws StandardException
	{
		// read only, do nothing
	}

	/**
	 * Backup restore - start sending log record to the log stream
	 * @exception StandardException Standard Cloudscape error policy
	 */
	public void unfreezePersistentStore() throws StandardException
	{
		// read only, do nothing
	}

	/**
	 * Backup restore - is the log being archived to some directory?
	 * if RawStore.LOG_ARCHIVAL_DIRECTORY is set to some value, that means the
	 * log is meant to be archived.  Else, log not archived.
	 */
	public boolean logArchived()
	{
		return (logArchiveDirectory != null);
	}

	/**
		Get JBMS properties relavent to the log factory
	 */
	public void getLogFactoryProperties(PersistentSet set) 
	{
		// do nothing
	}
	
	public StorageFile getLogDirectory()
	{
		return null;
	}

	public String getCanonicalLogPath()
	{
		return null;
	}

	
	//roll-forward recovery support routines
	//Nothing to be done for read only databases
	public void enableLogArchiveMode()
	{
		//do nothing
	}

	public void disableLogArchiveMode()
	{
		//do nothing
	}

	//this function is suppose to delete all the logs 
	//before this call that are not active logs.
	public void deleteOnlineArchivedLogFiles()
	{
		//do nothing
	}


	//Is the transaction in rollforward recovery
	public boolean inRFR()
	{
		return false;
	}

	/**	
		perform a  checkpoint during rollforward recovery
	*/
	public void checkpointInRFR(LogInstant cinstant, long redoLWM, 
								DataFactory df) throws StandardException
	{
		//do nothing
	}

		
	/*
	 * There are no log files to backup for  read  only databases, nothing to be
     * done here. 
     * @param toDir - location where the log files should be copied to.
     * @exception StandardException Standard Derby error policy
	*/
	public void startLogBackup(File toDir) throws StandardException
	{
		// nothing to do for read only databases.
	}

	
	/* 
     * There are no log files to backup for read only databases, 
     * nothing to be done here. 
     *
     * @param toDir - location where the log files should be copied to.
     * @exception StandardException Standard Derby error policy
	*/
	public void endLogBackup(File toDir) throws StandardException
	{
		// nothing to do for read only databases.
	}

	
	/*
     * Log backup is not started for for read only databases, no work to do
     * here.
	 **/
	public void abortLogBackup()
	{
		// nothing to do for read only databases.
	}

    /*
     * Set that the database is encrypted. Read-only database can not 
     * be reencrypted, nothing to do in this case. 
     */
    public void setDatabaseEncrypted()
    {
        // nothing to do for a read-only database.
    }


    /*
     * set up a new log file to start writing 
     * the log records into the new log file 
     * after this call.
     *
     * <P>MT - synchronization provided by caller - RawStore boot,
     * This method is called while re-encrypting the database 
     * at databse boot time. 
     *
     * Read-only database can not be reencrypted, 
     * nothing to do in this case. 
     */
    public void startNewLogFile() throws StandardException 
    {
        // nothing to do for a read-only database. 
    }

    /*
     * find if the checkpoint is in the last log file. 
     *
     * <P>MT - synchronization provided by caller - RawStore boot,
     * This method is called only if a crash occured while 
     * re-encrypting the database at boot time. 

     * Read-only database can not be re-encrypted, 
     * nothing to do in this case. 
     */
    public boolean isCheckpointInLastLogFile() 
        throws StandardException 
    {
        // nothing to do for a read-only database. 
        return false;
    }
    
    /*
     * delete the log file after the checkpoint. 
     *
     * <P>MT - synchronization provided by caller - RawStore boot,
     * This method is called only if a crash occured while 
     * re-encrypting the database at boot time. 
     *
     * Read-only database can not be re-encrypted, 
     * nothing to do in this case. 
     */
    public void deleteLogFileAfterCheckpointLogFile() 
        throws StandardException 
    {
        // nothing to do for a read-only database. 
    }



    /**
     *  Check to see if a database has been upgraded to the required
     *  level in order to use a store feature.
     *
     * This method is generally used to prevent writes to 
     * data/log file by a particular store feature until the 
     * database is upgraded to the required version. 
     * In read-only database writes are not allowed, so nothing to do
     * for this method in this implementation of the log factory.
     *
     * @param requiredMajorVersion  required database Engine major version
     * @param requiredMinorVersion  required database Engine minor version
     * @param feature Non-null to throw an exception, null to return the 
     *                state of the version match.
     *
     * @exception  StandardException 
     *             not implemented exception is thrown
     */
	public boolean checkVersion(int requiredMajorVersion, 
                                int requiredMinorVersion, 
                                String feature) 
        throws StandardException
    {
        // nothing to do for read only databases; 
        throw StandardException.newException(
                  SQLState.STORE_FEATURE_NOT_IMPLEMENTED);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3472.java