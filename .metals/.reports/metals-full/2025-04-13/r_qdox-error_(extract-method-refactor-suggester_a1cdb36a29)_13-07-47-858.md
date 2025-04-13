error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3554.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3554.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,12]

error in qdox parser
file content:
```java
offset: 12
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3554.java
text:
```scala
@exception S@@tandardException Standard Derby error policy

/*

   Derby - Class org.apache.derby.impl.store.raw.data.RemoveFileOperation

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

package org.apache.derby.impl.store.raw.data;

import org.apache.derby.iapi.store.raw.Loggable;
import org.apache.derby.iapi.store.raw.Undoable;
import org.apache.derby.iapi.store.raw.Transaction;
import org.apache.derby.iapi.store.raw.Compensation;
import org.apache.derby.iapi.services.io.FormatIdUtil;
import org.apache.derby.iapi.services.io.StoredFormatIds;

import org.apache.derby.iapi.store.raw.xact.RawTransaction;

import org.apache.derby.iapi.error.StandardException;
import org.apache.derby.iapi.services.sanity.SanityManager;
import org.apache.derby.iapi.store.access.FileResource;
import org.apache.derby.iapi.store.raw.log.LogInstant;

import org.apache.derby.io.StorageFile;

import org.apache.derby.iapi.util.ByteArray;

import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.IOException;
import java.io.InputStream;
import org.apache.derby.iapi.services.io.LimitObjectInput;

/** 
*/

public class RemoveFileOperation implements Undoable
{
	private String name;
	private long generationId;
	private boolean removeAtOnce;

	transient private StorageFile fileToGo;

	// no-arg constructor, required by Formatable
	public RemoveFileOperation()
	{
	}

	RemoveFileOperation(String name, long generationId, boolean removeAtOnce)
	{
		this.name = name;
		this.generationId = generationId;
		this.removeAtOnce = removeAtOnce;
	}

	/*
	 * Formatable methods
	 */
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeUTF(name);
		out.writeLong(generationId);
		out.writeBoolean(removeAtOnce);
	}

	public void readExternal(ObjectInput in) 
		 throws IOException, ClassNotFoundException 
	{
		name = in.readUTF();
		generationId = in.readLong();
		removeAtOnce = in.readBoolean();
	}
	/**
		Return my format identifier.
	*/
	public int getTypeFormatId() {
		return StoredFormatIds.LOGOP_REMOVE_FILE;
	}


	/**
		Loggable methods
	*/

	/**
		the default for prepared log is always null for all the operations
		that don't have optionalData.  If an operation has optional data,
		the operation need to prepare the optional data for this method.

		Space Operation has no optional data to write out
	*/
	public ByteArray getPreparedLog()
	{
		return null;
	}

	public void releaseResource(Transaction tran)
	{
	}

	/**
		A space operation is a RAWSTORE log record
	*/
	public int group()
	{
		return Loggable.FILE_RESOURCE | Loggable.RAWSTORE ;
	}

	public void doMe(Transaction xact, LogInstant instant, 
						   LimitObjectInput in)
		 throws StandardException
	{
		if (fileToGo == null)
			return;

		BaseDataFileFactory bdff = 
			(BaseDataFileFactory) ((RawTransaction) xact).getDataFactory();
		
		bdff.fileToRemove(fileToGo, true);
	}


	/**
		@exception StandardException Standard Cloudscape error policy
	*/
	public boolean needsRedo(Transaction xact)
		 throws StandardException
	{
		if (!removeAtOnce)
			return false;

		FileResource fr = ((RawTransaction) xact).getDataFactory().getFileHandler();

		fileToGo = fr.getAsFile(name, generationId);

		if (fileToGo == null)
			return false;

        return fileToGo.exists();
	}


	public Compensation generateUndo(Transaction xact, LimitObjectInput in)
		throws StandardException, IOException {


		if (fileToGo != null) {
			BaseDataFileFactory bdff = 
				(BaseDataFileFactory) ((RawTransaction) xact).getDataFactory();
		
			bdff.fileToRemove(fileToGo, false);
		}

		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3554.java