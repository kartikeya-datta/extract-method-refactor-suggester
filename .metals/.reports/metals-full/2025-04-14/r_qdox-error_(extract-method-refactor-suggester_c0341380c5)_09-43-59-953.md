error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1230.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1230.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1230.java
text:
```scala
r@@eadHeader(getEmbryonicPage(dis));

/*

   Derby - Class org.apache.derby.impl.store.raw.data.InputStreamContainer

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

import org.apache.derby.impl.store.raw.data.FileContainer;

import org.apache.derby.iapi.store.raw.ContainerKey;

import org.apache.derby.iapi.error.StandardException;
import org.apache.derby.iapi.store.raw.log.LogInstant;
import org.apache.derby.iapi.services.sanity.SanityManager;
import org.apache.derby.impl.store.raw.data.BaseDataFileFactory;

import org.apache.derby.iapi.services.io.InputStreamUtil;
import org.apache.derby.iapi.reference.SQLState;

import org.apache.derby.io.StorageFile;

import java.io.InputStream;
import java.io.IOException;
import java.io.DataInputStream;

/**
	A class that uses a ZipEntry to be a single container file,
	but read-only.

*/

final class InputStreamContainer extends FileContainer  {

    private StorageFile containerPath;
    
	/*
	 * Constructors
	 */

	InputStreamContainer(BaseDataFileFactory factory) {
		super(factory);
		canUpdate = false;
	}

	final boolean openContainer(ContainerKey newIdentity) throws StandardException {
		DataInputStream dis = null;
		try
        {
			InputStream is = null;
            containerPath = dataFactory.getContainerPath(newIdentity, false);
            try
            {
                is = containerPath.getInputStream();
            }
            catch (IOException ioe)
            {
                // Maybe it's been stubbified.
                containerPath = dataFactory.getContainerPath(newIdentity, true);
                try
                {
                    is = getInputStream();
                }
                catch (IOException ioe2)
                {
                    containerPath = null;
                    return false;
                }
            }

			dis = new DataInputStream(is);
			
			// FileData has to be positioned just at the beginning 
			// of the first allocation page. And it is because we
			// just opened the stream and the first allocation page
			// is located at the beginning of the file.
			readHeader(dis);

			return true;

        } catch (IOException ioe) {
            throw StandardException.
                newException(SQLState.FILE_CONTAINER_EXCEPTION, ioe, this);
        } finally {
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException ioe) {}
			}
		}
	} // end of openContainer

	void closeContainer()
    {
		containerPath = null;
	}

	/**
		Write out the header information for this container. If an i/o exception
		occurs then ...

		@see org.apache.derby.iapi.services.cache.Cacheable#clean
		@exception StandardException Standard Derby error policy
	*/
	public final void clean(boolean forRemove) throws StandardException {

		// Nothing to do since we are inherently read-only.

	}

	/**
		Preallocate page.  
	*/
	protected final int preAllocate(long lastPreallocPagenum, int preAllocSize) {

		// Nothing to do since we are inherently read-only.
		return 0;
	}

	protected void truncatePages(long lastValidPagenum)
    {
		// Nothing to do since we are inherently read-only.
		return;
    }
    

	/*
	** Container creation, opening, and closing
	*/

	/**
		Create a new container, all references to identity must be through the
		passed in identity, this object will no identity until after this method returns.
	*/
	void createContainer(ContainerKey newIdentity) throws StandardException {
		// RESOLVE - probably should throw an error ...
	}



	/**
		Remove the container.
	*/
	protected final void removeContainer(LogInstant instant, boolean leaveStub) throws StandardException 
	{
	// RESOVE
	}

	/*
	** Methods used solely by StoredPage
	*/

	/**
		Read a page into the supplied array.

		<BR> MT - thread safe
	*/
	protected final void readPage(long pageNumber, byte[] pageData) 
		 throws IOException, StandardException
	{

		if (SanityManager.DEBUG) {
			SanityManager.ASSERT(!getCommittedDropState());
		}

		long pageOffset = pageNumber * pageSize;

		readPositionedPage(pageOffset, pageData);

		if (dataFactory.databaseEncrypted() && 
			pageNumber != FIRST_ALLOC_PAGE_NUMBER)
		{
			decryptPage(pageData, pageSize);
		}
	}

	/**
		Read the page at the positioned offset.
		This default implementation, opens the stream and skips to the offset
		and then reads the data into pageData.
	*/
	protected void readPositionedPage(long pageOffset, byte[] pageData) throws IOException {


		InputStream is = null;
		try {
			// no need to synchronize as each caller gets a new stream
			is = getInputStream();

			InputStreamUtil.skipBytes(is, pageOffset);

			InputStreamUtil.readFully(is, pageData, 0, pageSize);

			is.close();
			is = null;
		} finally {
			if (is != null) {
				try {is.close();} catch (IOException ioe) {}
			}
		}
	}

	/**
		Write a page from the supplied array.

		<BR> MT - thread safe
	*/
	protected final void writePage(long pageNumber, byte[] pageData, boolean syncPage)
		throws IOException, StandardException {
	}

	protected final void flushAll() {
	}

	/**
		Get an input stream positioned at the beginning of the file
	*/
	protected InputStream getInputStream() throws IOException
    {
        return containerPath.getInputStream();
    }

		
	/**
     * Backup the container.
     * There is no support to backup this type of containers. It may not be any
     * real use for users because users can simply  make copies of the read only 
     * database in Zip files easily using OS utilities.
     * 
     * @exception StandardException Standard Derby error policy 
     */
	protected void backupContainer(BaseContainerHandle handle,	String backupLocation)
	    throws StandardException
	{
        throw StandardException.newException(
                SQLState.STORE_FEATURE_NOT_IMPLEMENTED);
	}


    /**
     * Encrypt the container.  There is no support to encrypt 
     * this type of containers. 
     * 
     * @exception StandardException Standard Derby error policy 
     */
	protected void encryptContainer(BaseContainerHandle handle, 
                                    String newFilePath)
	    throws StandardException
	{
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1230.java