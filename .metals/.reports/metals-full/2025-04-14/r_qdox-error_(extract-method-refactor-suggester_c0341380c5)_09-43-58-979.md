error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1885.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1885.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1885.java
text:
```scala
t@@his.xid = null;

/*

   Derby - Class org.apache.derby.impl.drda.DRDAXAProtocol.java

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
import org.apache.derby.iapi.services.sanity.SanityManager;
import javax.transaction.xa.*;

/**
 * This class translates DRDA XA protocol from an application requester to XA
 * calls for Derby and then translates the results from Derby to DRDA
 * for return to the application requester.
 * This class requires the use of javax.transaction.xa classes from j2ee,
 * so is separated from DRDAConnThread, because of the additional
 * library requirements
 */

class DRDAXAProtocol {

	private DRDAConnThread connThread;
	private DDMReader reader;
	private DDMWriter writer;
    /** Holds the Xid of the global transaction associated with
      * the corresponding DRDAConnThread (and connection itself). */
    private Xid xid;


	DRDAXAProtocol(DRDAConnThread connThread)
	{
		this.connThread = connThread;
		reader = connThread.getReader();
		writer = connThread.getWriter();
        xid = null;
	}



	/**
	 * Parse SYNCCTL - Parse SYNCCTL command for XAMGR lvl 7
	 *
	 */
	protected void parseSYNCCTL() throws DRDAProtocolException
	{
		
		reader.markCollection();
		
		int codePoint = reader.getCodePoint(CodePoint.SYNCTYPE);
		int syncType = parseSYNCTYPE();
		
		int xaflags = 0;
		boolean readXAFlags = false;
		Xid xid = null;

		codePoint = reader.getCodePoint();
		while (codePoint != -1)
		{
			switch(codePoint)
			{
				case CodePoint.XID:
					xid = parseXID();
					break;					
				case CodePoint.XAFLAGS:
					xaflags = parseXAFlags();					
					readXAFlags =true;
					break;
				case CodePoint.TIMEOUT:
					// optional/ignorable.
					reader.skipBytes();
					break;
				case CodePoint.RLSCONV:
					connThread.codePointNotSupported(codePoint);	  
				default:
					connThread.invalidCodePoint(codePoint);
			}

			codePoint = reader.getCodePoint();
		}


		{
			connThread.trace("syncType = " + syncTypeToString(syncType));
			connThread.trace("xid = " + xid);
			connThread.trace("xaflags =" + xaflagsToString(xaflags));
		}

		if (syncType != CodePoint.SYNCTYPE_INDOUBT)
		{
			if (xid == null)
				connThread.missingCodePoint(CodePoint.XID);
			
			// All but Recover and forget require xaFlags
			if (syncType != CodePoint.SYNCTYPE_REQ_FORGET && 
				! readXAFlags)
				if (SanityManager.DEBUG)
					connThread.missingCodePoint(CodePoint.XAFLAGS);
		}

		switch (syncType)
		{  
			case CodePoint.SYNCTYPE_NEW_UOW:
				// new unit of work for XA
				// formatId -1 is just a local connection
				startXATransaction(xid,xaflags);
				break;
			case CodePoint.SYNCTYPE_END_UOW:
				// End unit of work
				endXA(xid,xaflags);
				break;
			case CodePoint.SYNCTYPE_PREPARE:
				prepareXATransaction(xid);
				// Prepare to commit 
				break;
			case CodePoint.SYNCTYPE_MIGRATE:
				// migrate to resync server sync type
				connThread.codePointNotSupported(codePoint);				
				break;			
			case CodePoint.SYNCTYPE_REQ_COMMIT:
				// request to commit sync type
				commitTransaction(xid,xaflags);
				break;
			case CodePoint.SYNCTYPE_COMMITTED:
				// commit  sync type
				commitTransaction(xid, xaflags);
				break;
			case CodePoint.SYNCTYPE_REQ_FORGET:
				// request to forget sync type
				forgetXATransaction(xid);
				break;
			case CodePoint.SYNCTYPE_ROLLBACK:
				//rollback sync type
				rollbackTransaction(xid, true);
				break;
			case CodePoint.SYNCTYPE_INDOUBT:
				//recover sync type
				if (readXAFlags)
					recoverXA(xaflags);
				else
					recoverXA();
				break;
			default:
				connThread.invalidCodePoint(codePoint);
 		}

	}

	/** 
	 * parse SYNCTYPE for XAMGR lvl 7
	 * return synctype value 
	 *   CodePoint.SYNCTYPE_NEW_UOW -> XAResource.start()
	 *   CodePoint.SYNCTYPE_END_UOW -> XAResource.end()
	 *   CodePoint.SYNCTYPE_PREPARE -> XAResource.prepare()
	 *   CodePoint.SYNCTYPE_MIGRATE -> not supported  //SYNCPT MGR LEVEL 5
	 *   CodePoint.SYNCTYPE_REQ_COMMIT -> not supported //SYNCPT MGR LEVEL 5
	 *   CodePoint.SYNCTYPE_COMMITTED -> XAResource.commit()  
	 *                                   or local commit for null XID
	 *   CodePoint.SYNCTYPE_REQ_LOG ->  not supported
	 *   CodePoint.SYNCTYPE_REQ_FORGET -> XAResource.forget()
	 *   CodePoint.SYNCTYPE_ROLLBACK -> XAResource.rollback()
	 *   CodePoint.SYNCTYPE_MIGRATED -> not supported
	 *   CodePoint.SYNCTYPE_INDOUBT   -> XAResource.recover();
	 * 
	 */	 
	protected int  parseSYNCTYPE() throws DRDAProtocolException
	{
		return reader.readUnsignedByte();
		
	}
	

	/** Parse XID
	 *  formatId -1 translates into a null XID and a local transaction
	 */
	private  Xid parseXID () throws DRDAProtocolException
	{
		int formatId = reader.readNetworkInt();
		byte[] gtrid = null;
		byte[] bqual = null;
		if (formatId != -1)
		{
			int gtridLen = reader.readNetworkInt();
			int bqualLen = reader.readNetworkInt();
			
			gtrid = reader.readBytes(gtridLen);
			bqual = reader.readBytes(bqualLen);
		}
		return new DRDAXid(formatId, gtrid, bqual);
	}


	/** 
	 *  parse XIDSHR
	 *
	 * @return XIDSHR value
	 * @throws DRDAProtocolException
	 */
	private int parseXIDSHR() throws DRDAProtocolException
	{
		return reader.readUnsignedByte();
	}

	/** 
	 *  parse XAFlags 
	 *
	 * @return XAFlags value
	 * @throws DRDAProtocolException
	 */
	private int parseXAFlags() throws DRDAProtocolException
	{
		return reader.readNetworkInt();
	}


	/**
	 *  Start the xa transaction. Send SYNCRRD response
	 * 
	 *  @param xid - XID (formatId = -1 for local transaction)
	 *  @param xaflags - xaflags
	 *  @throws DRDAProtocolException
	 */
	private void startXATransaction(Xid xid, int xaflags) throws DRDAProtocolException
	{
		XAResource xaResource = getXAResource();
		int xaRetVal = xaResource.XA_OK;

		try {
			if (xid.getFormatId() != -1)
				xaResource.start(xid,xaflags);
            this.xid = xid;
		} catch (XAException xe)
		{
			xaRetVal = processXAException(xe);
		}
		writeSYNCCRD(CodePoint.SYNCTYPE_NEW_UOW, 
					 xaRetVal, null);
		
	}
	

	/**
	 *  Commit  the xa transaction. Send SYNCCRD response
	 * 
	 *  @param xid - XID (formatId = -1 for local transaction)
	 *  @param xaflags - xaflags
	 *  @throws DRDAProtocolException
	 */
	private void commitTransaction(Xid xid, int xaflags) throws DRDAProtocolException
	{
		boolean local  = ( xid.getFormatId() == -1);
		if (local)
			commitLocalTransaction();
		else
			commitXATransaction(xid, xaflags);
	}

	/**
	 *  Commit local transaction. Send SYNCCRD response.
	 * 
	 *  @throws DRDAProtocolException
	 */	   
	private void commitLocalTransaction() throws DRDAProtocolException
	{
		int xaRetVal = XAResource.XA_OK;
		try {
			connThread.getDatabase().commit();
		}
		catch  (Exception e)
		{
			xaRetVal = XAException.XAER_RMFAIL;
			if (SanityManager.DEBUG)
			{
				connThread.getServer().consoleExceptionPrint(e);
			}

		}
		writeSYNCCRD(CodePoint.SYNCTYPE_COMMITTED, 
					 xaRetVal, null);

	}

	
	/**
	 *  Commit  the xa transaction. Send SYNCCRD response.
	 * 
	 *  @param xid - XID 
	 *  @param xaflags - xaflags
	 *  @throws DRDAProtocolException
	 */
	private void commitXATransaction(Xid xid, int xaflags) throws DRDAProtocolException
	{
		XAResource xaResource = getXAResource();
		int xaRetVal = xaResource.XA_OK;
		// check this
		boolean isOnePhase = (xaflags & XAResource.TMONEPHASE) != 0;
		try {
			xaResource.commit(xid, isOnePhase);
			if (SanityManager.DEBUG)
				connThread.trace("committed XA transaction: xaRetVal=" + xaRetVal);

		} catch (XAException xe)
		{
			xaRetVal = processXAException(xe);
		}
		writeSYNCCRD(CodePoint.SYNCTYPE_COMMITTED, 
					 xaRetVal, null);
		
	}

	/**
	 * Rollback transaction. Optionally send SYNCCRD response.
	 * @param xid  Xid for rollback for global transaction.
	 *             If xid formatid is -1 it represents a local transaction
     * @param sendSYNCCRD Indicates whether the function should
     *                    send a SYNCCRD response
	 */
	private void rollbackTransaction(Xid xid, boolean sendSYNCCRD) throws DRDAProtocolException
	{
		boolean local  = ( xid.getFormatId() == -1);
		if (local)
			rollbackLocalTransaction(sendSYNCCRD);
		else
			rollbackXATransaction(xid, sendSYNCCRD);
	}
	
	/**
	 * Rollback a local transaction. Optionally send SYNCCRD response.
	 *
     * @param sendSYNCCRD Indicates whether the function should
     *                    send a SYNCCRD response
	 * @throws DRDAProtocolException
	 */
	private void rollbackLocalTransaction(boolean sendSYNCCRD) throws DRDAProtocolException
	{
		int xaRetVal = XAResource.XA_OK;
		try {
			connThread.getDatabase().rollback();
		}
		catch  (Exception e)
		{
			xaRetVal = XAException.XAER_RMFAIL;
			if (SanityManager.DEBUG)
			{
				connThread.getServer().consoleExceptionPrint(e);
			}
			
		}
        if (sendSYNCCRD) {
            writeSYNCCRD(CodePoint.SYNCTYPE_COMMITTED,
                         xaRetVal, null);
        }
	}

	/**
	 *  Rollback the xa transaction. Optionally send SYNCCRD response.
	 * 
	 *  @param xid - XID 
     *  @param sendSYNCCRD Indicates whether the function should
     *                     send a SYNCCRD response
	 *  @throws DRDAProtocolException
	 */
	private void rollbackXATransaction(Xid xid, boolean sendSYNCCRD) throws DRDAProtocolException
	{
		XAResource xaResource = getXAResource();
		int xaRetVal = xaResource.XA_OK;

		try {
			xaResource.rollback(xid);
			if (SanityManager.DEBUG)
			{
				connThread.trace("rollback  XA transaction: xaRetVal=" + xaRetVal); 
			}
		} catch (XAException xe)
		{
			xaRetVal = processXAException(xe);
		}
        if (sendSYNCCRD) {
            writeSYNCCRD(CodePoint.SYNCTYPE_ROLLBACK,
                         xaRetVal, null);
        }
	}

	/**
	 *  End  the xa transaction. Send SYNCRRD response
	 * 
	 *  @param xid - XID 
	 *  @param xaflags - xaflags
	 *  @throws DRDAProtocolException
	 */
	private void endXA(Xid xid, int xaflags) throws DRDAProtocolException
	{
		XAResource xaResource = getXAResource();		
		int xaRetVal = xaResource.XA_OK;

		try {
			xaResource.end(xid,xaflags);
            xid = null;
			if (SanityManager.DEBUG)
			{
				connThread.trace("ended XA transaction. xid =  " + xid +
							   " xaflags =" + xaflags + 
								 "xaRetVal=" + xaRetVal); 
			}
		} catch (XAException xe)
		{
			xaRetVal = processXAException(xe);
		}
		writeSYNCCRD(CodePoint.SYNCTYPE_END_UOW,
					 xaRetVal, null);		
	}


	/**
	 *  Prepare the xa transaction. Send SYNCCRD response.
	 * 
	 *  @param xid - XID 
	 *  @throws DRDAProtocolException
	 */
	private void prepareXATransaction(Xid xid) throws DRDAProtocolException
	{
		XAResource xaResource = getXAResource();
		int xaRetVal = xaResource.XA_OK;

		try {
			xaRetVal = xaResource.prepare(xid);
			if (SanityManager.DEBUG)
			{
				connThread.trace("prepared xa transaction: xaRetVal=" +
								xaRetVal); 
			}
		} catch (XAException xe)
		{			
			xaRetVal = processXAException(xe);
		}
		writeSYNCCRD(CodePoint.SYNCTYPE_PREPARE,
					 xaRetVal, null);		
	}

	/**
	 *  Forget the xa transaction. Send SYNCCRD response.
	 * 
	 *  @param xid - XID 
	 *  @throws DRDAProtocolException
	 */
	private void forgetXATransaction(Xid xid) throws DRDAProtocolException
	{
		XAResource xaResource = getXAResource();
		int xaRetVal = xaResource.XA_OK;

		try {
			xaResource.forget(xid);
			if (SanityManager.DEBUG)
			{
				connThread.trace("forgot xa transaction: xaRetVal=" + xaRetVal);
			}
		} catch (XAException xe)
		{
			xaRetVal = processXAException(xe);
		}
		writeSYNCCRD(CodePoint.SYNCTYPE_REQ_FORGET,
					 xaRetVal, null);		
	}

	// JCC doesn't send xaflags but always wants TMSTARTRSCAN.  
	//So default to that if we got no xaflags
	private void recoverXA() throws DRDAProtocolException
	{
		recoverXA(XAResource.TMSTARTRSCAN);
	}

	/**
	 * Call recover. Send SYNCCRD response with indoubt list
	 * 
	 *  @throws DRDAProtocolException
	 */
	private void recoverXA(int xaflags) throws DRDAProtocolException
	{
		XAResource xaResource = getXAResource();
		int xaRetVal = xaResource.XA_OK;
		Xid[] indoubtXids = null;
		try {
			indoubtXids = xaResource.recover(xaflags);
		} catch (XAException xe)
		{
			xaRetVal = processXAException(xe);
		}
		writeSYNCCRD(CodePoint.SYNCTYPE_INDOUBT,
					 xaRetVal, indoubtXids);		
	}

	/** Write SYNCCRD (SYNCCTL response)
	 * @param synctype - XA Command to send response for see  parseSYNCTYPE
	 * @param xaRetVal - return value from XA command
	 * @param xids - list of xids to return for recover. 
	 *               null for other commands
	 * @throws DRDAProtocolException
	 */
	private void writeSYNCCRD (int synctype, int xaRetVal, Xid[] xids) throws DRDAProtocolException
	{
		writer.createDssReply();
		writer.startDdm(CodePoint.SYNCCRD);
		writer.startDdm(CodePoint.XARETVAL);
		writer.writeInt(xaRetVal);
		writer.endDdm();
		if (xids != null)
			writePRPHRCLST(xids);
		writer.endDdmAndDss();
	}

	/** write PRPHRCLST (indoubt list)
	 * 
	 * @param xids - list of indoubt xa transactions obtained from recover
	 * @throws DRDAProtocolException
	 */
	private void writePRPHRCLST(Xid[] xids) throws DRDAProtocolException
	{
		int xidcnt = (xids == null ? 0 : xids.length);
		writer.startDdm(CodePoint.PRPHRCLST);
		writer.writeScalar2Bytes(CodePoint.XIDCNT, xidcnt);
		for (int i = 0; i < xidcnt; i++)
			writeXID(xids[i]);
		writer.endDdm();
	}

	/** write XID
	 * 
	 * @param xid - XID to write
	 * @throws DRDAProtocolException
	 */
	
	private void writeXID(Xid xid) throws DRDAProtocolException
	{
		writer.startDdm(CodePoint.XID);
		int formatId = xid.getFormatId();
		byte[] gtrid = xid.getGlobalTransactionId();
		byte[] bqual = xid.getBranchQualifier();
		
		writer.writeInt(formatId);
		writer.writeInt(gtrid.length);
		writer.writeInt(bqual.length);
		writer.writeBytes(gtrid);
		writer.writeBytes(bqual);
		writer.endDdm();
	}
	

	/** get XAResource for the connection
	 *
	 * @return XAResource
	 */
	private XAResource getXAResource()
	{
		return ((XADatabase) connThread.getDatabase()).getXAResource();
		
	}
	
	/** printable syncType for debug output
	 * @param syncType
	 * @return - sync type meaning
	 */
	private String syncTypeToString(int syncType)
	{
		switch (syncType)
		{  
			case CodePoint.SYNCTYPE_NEW_UOW:
				return "SYNCTYPE_NEW_UOW";
				
			case CodePoint.SYNCTYPE_END_UOW:
				return "SYNCTYPE_END_UOW";
				
			case CodePoint.SYNCTYPE_PREPARE:
				return "SYNCTYPE_PREPARE";
				
			case CodePoint.SYNCTYPE_MIGRATE:
				return "SYNCTYPE_MIGRATE";
							
			case CodePoint.SYNCTYPE_REQ_COMMIT:
				return "SYNCTYPE_REQ_COMMIT";
				
			case CodePoint.SYNCTYPE_COMMITTED:
				return "SYNCTYPE_COMMITTED";
				
			case CodePoint.SYNCTYPE_REQ_FORGET:
				return "SYNCTYPE_FORGET";
				
			case CodePoint.SYNCTYPE_ROLLBACK:
				return "SYNCTYPE_ROLLBACK";
				
			case CodePoint.SYNCTYPE_REQ_LOG:
				return "SYNCTYPE_REQ_LOG";
				
			case   CodePoint.SYNCTYPE_MIGRATED:
				return "SYNCTYPE_MIGRATED";
				
			case CodePoint.SYNCTYPE_INDOUBT:
				return "SYNCTYPE_INDOUBT";
				
			default:
				return "UNKNOWN SYNCTYPE";
 		}
	}

	/** 
	 * printable xaflags
	 * @param xaflags
	 * @return printable xaflags for debug output
	 */
	private String xaflagsToString(int xaflags)
	{
		switch (xaflags)
		{
			case XAResource.TMENDRSCAN :
				return "XAResource.TMENDRSCAN";
				
			case XAResource.TMFAIL:
				return "XAResource.TMFAIL";
				
			case XAResource.TMNOFLAGS:
				return "XAResource.TMNOFLAGS";
				
			case XAResource.TMJOIN:
				return "XAResource.TMJOIN";
				
			case XAResource.TMONEPHASE:
				return "XAResource.TMONEPHASE";
				
			case XAResource.TMRESUME:
				return "XAResource.TMRESUME";
				
			case XAResource.TMSTARTRSCAN:
				return "XAResource.TMSTARTRSCAN";
				
			case XAResource.TMSUCCESS:
				return "XAResource.TMSUCCESS";
				
			case XAResource.TMSUSPEND:
				return "XAResource.TMSUSPEND";
				
			default:
				return "UNRECOGNIZED flags:" + xaflags;
				
		}
	}

	/** 
	 * return xa exception errorCode.
	 * print to console for debug output.
	 * @param xe - XA Exception
	 */
	private int processXAException(XAException xe)
	{
		int xaRetVal = xe.errorCode;
		if (SanityManager.DEBUG)
		{
			connThread.getServer().consoleExceptionPrint(xe);
		}
		return xaRetVal;
	}

    /**
     * This function rollbacks the current global transaction associated
     * with the XAResource or a local transaction. The function should
     * be called only in exceptional cases - like client socket
     * is closed. */
    void rollbackCurrentTransaction()
    {
        if (xid != null) {
            boolean local  = ( xid.getFormatId() == -1);
            try {
                // if the transaction is not local disassociate the transaction from
                // the connection first because the rollback can not be performed
                // on a transaction associated with the XAResource
                try {
                    if (!local) {
                        XAResource xaResource = getXAResource();
                        // this will throw the XAException (because TMFAIL
                        // will throw an exception)
                        xaResource.end(xid, XAResource.TMFAIL);
                    }
                } catch (XAException e) {
                    // do not print out the exception generally thrown
                    // when TMFAIL flag is present
                    if (e.errorCode < XAException.XA_RBBASE
 e.errorCode > XAException.XA_RBEND) {
                        connThread.getServer().consoleExceptionPrint(e);
                    }
                }
                rollbackTransaction(xid, false);
            } catch  (DRDAProtocolException e) {
                // because we do not dump any DRDA stuff to the socket
                // the exception can not be thrown in this case
                // However, we will dump the exception to the console
                connThread.getServer().consoleExceptionPrint(e);
            }
            xid = null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1885.java