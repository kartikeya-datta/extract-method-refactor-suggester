error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/796.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/796.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/796.java
text:
```scala
d@@btable.put(d.getDatabaseName(), d);

/*

   Derby - Class org.apache.derby.impl.drda.Session

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

package org.apache.derby.impl.drda;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.derby.iapi.tools.i18n.LocalizedResource;
import java.sql.SQLException;

/**
	Session stores information about the current session
	It is used so that a DRDAConnThread can work on any session.
*/
class Session
{

	// session states	   
	protected static final int INIT = 1;	// before exchange of server attributes
	protected static final int ATTEXC = 2;	// after first exchange of server attributes
	protected static final int SECACC = 3;	// after ACCSEC (Security Manager Accessed)
	protected static final int CHKSEC = 4;	// after SECCHK  (Checked Security)
	protected static final int CLOSED = 5;	// session has ended

	// session types
	protected static final int DRDA_SESSION = 1;
	protected static final int CMD_SESSION = 2;
	
	// trace name prefix and suffix
	private static final String TRACENAME_PREFIX = "Server";
	private static final String TRACENAME_SUFFIX = ".trace";

	// session information
	protected Socket clientSocket;		// session socket
	protected int connNum;				// connection number
	protected InputStream sessionInput;	// session input stream
	protected OutputStream sessionOutput;	// session output stream
	protected String traceFileName;		// trace file name for session
	protected boolean traceOn;			// whether trace is currently on for the session
	protected int state;				// the current state of the session
	protected int sessionType;			// type of session - DRDA or NetworkServerControl command
	protected String drdaID;			// DRDA ID of the session
	protected DssTrace dssTrace;		// trace object associated with the session
	protected AppRequester appRequester;	// Application requester for this session
	protected Database database;		// current database
	protected int qryinsid;				// unique identifier for each query
	protected LocalizedResource langUtil;		// localization information for command session
										// client

	private	Hashtable	dbtable;		// Table of databases accessed in this session
	private NetworkServerControlImpl nsctrl;        // NetworkServerControlImpl needed for logging
                                                        // message if tracing fails.
                                                        

	// constructor
	/**
	 * Session constructor
	 * 
	 * @param connNum		connection number
	 * @param clientSocket	communications socket for this session
	 * @param traceDirectory	location for trace files
	 * @param traceOn		whether to start tracing this connection
	 *
	 * @exception throws IOException
	 */
	Session (NetworkServerControlImpl nsctrl, int connNum, Socket clientSocket, String traceDirectory,
			boolean traceOn) throws Exception
	{
        this.nsctrl = nsctrl;
		this.connNum = connNum;
		this.clientSocket = clientSocket;
		this.traceOn = traceOn;
		if (traceOn)
			dssTrace = new DssTrace(); 
		dbtable = new Hashtable();
		initialize(traceDirectory);
	}

	/**
	 * Close session - close connection sockets and set state to closed
	 * 
	 */
	protected void close() throws SQLException
	{
		
		try {
			sessionInput.close();
			sessionOutput.close();
			clientSocket.close();
			if (dbtable != null)
				for (Enumeration e = dbtable.elements() ; e.hasMoreElements() ;) 
				{
					((Database) e.nextElement()).close();
				}
			
		}catch (IOException e) {} // ignore IOException when we are shutting down
		finally {
			state = CLOSED;
			dbtable = null;
			database = null;
		}
	}

	/**
	 * initialize a server trace for the DRDA protocol
	 * 
	 * @param traceDirectory - directory for trace file
     * @param throwException - true if we should throw an exception if
     *                         turning on tracing fails.  We do this
     *                         for NetworkServerControl API commands.
	 * @throws IOException 
	 */
	protected void initTrace(String traceDirectory, boolean throwException)  throws Exception
	{
		if (traceDirectory != null)
			traceFileName = traceDirectory + "/" + TRACENAME_PREFIX+
				connNum+ TRACENAME_SUFFIX;
		else
			traceFileName = TRACENAME_PREFIX +connNum+ TRACENAME_SUFFIX;
		
		if (dssTrace == null)
			dssTrace = new DssTrace();
        try {
            dssTrace.startComBufferTrace(traceFileName);
            traceOn = true;
        } catch (Exception e) {   
            if (throwException) {
                throw e;
            }
            // If there is an error starting tracing for the session,
            // log to the console and derby.log and do not turn tracing on.
            // let connection continue.
            nsctrl.consoleExceptionPrintTrace(e);
        }              
	}

	/**
	 * Set tracing on
	 * 
	 * @param traceDirectory 	directory for trace files
	 * @throws Exception 
	 */
	protected void setTraceOn(String traceDirectory, boolean throwException) throws Exception
	{
		if (traceOn)
			return;
		initTrace(traceDirectory, throwException);    
	}

	/**
	 * Get whether tracing is on 
	 *
	 * @return true if tracing is on false otherwise
	 */
	protected boolean isTraceOn()
	{
		if (traceOn)
			return true;
		else
			return false;
	}

	/**
	 * Get connection number
	 *
	 * @return connection number
	 */
	protected int getConnNum()
	{
		return connNum;
	}

	/**
	 * Set tracing off
	 * 
	 */
	protected void setTraceOff()
	{
		if (! traceOn)
			return;
		traceOn = false;
		if (traceFileName != null)
			dssTrace.stopComBufferTrace();
	}
	/**
	 * Add database to session table
	 */
	protected void addDatabase(Database d)
	{
		dbtable.put(d.dbName, d);
	}

	/**
	 * Get database
	 */
	protected Database getDatabase(String dbName)
	{
		return (Database)dbtable.get(dbName);
	}

	/**
	 * Get requried security checkpoint.
	 * Used to verify EXCSAT/ACCSEC/SECCHK order.
	 *
	 *  @return next required Security checkpoint or -1 if 
	 *          neither ACCSEC or SECCHK are required at this time.
	 *
	 */
	protected int getRequiredSecurityCodepoint()
	{
		switch (state)
		{
			case ATTEXC:
				// On initial exchange of attributes we require ACCSEC 
				// to access security manager
				return CodePoint.ACCSEC;
			case SECACC:
				// After security manager has been accessed successfully we
				// require SECCHK to check security
				return CodePoint.SECCHK;
			default:
				return -1;
		}	 
	}

	/**
	 * Check if a security codepoint is required
	 *
	 * @return true if ACCSEC or SECCHK are required at this time.
	 */
	protected boolean requiresSecurityCodepoint()
	{
		return (getRequiredSecurityCodepoint() != -1);
	}

	/**
	 * Set Session state
	 * 
	 */
	protected void setState(int s)
	{
		state = s;
	}
	
	/**
	 * Get session into initial state
	 *
	 * @param traceDirectory	- directory for trace files
	 */
	private void initialize(String traceDirectory)
		throws Exception
	{
		sessionInput = clientSocket.getInputStream();
		sessionOutput = clientSocket.getOutputStream();
		if (traceOn)
			initTrace(traceDirectory,false);
		state = INIT;
	}

	protected  String buildRuntimeInfo(String indent, LocalizedResource localLangUtil)
	{
		String s = "";
		s += indent +  localLangUtil.getTextMessage("DRDA_RuntimeInfoSessionNumber.I")
			+ connNum + "\n";
		if (database == null)
			return s;
		s += database.buildRuntimeInfo(indent,localLangUtil);
		s += "\n";
		return s;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/796.java