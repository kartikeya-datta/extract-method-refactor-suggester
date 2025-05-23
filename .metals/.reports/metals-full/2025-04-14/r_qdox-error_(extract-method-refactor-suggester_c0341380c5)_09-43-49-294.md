error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2925.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2925.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2925.java
text:
```scala
T@@hread.sleep(INTERRUPT_RETRY_SLEEP);

/*

   Derby - Class org.apache.derby.impl.store.raw.data.RAFContainer

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

import org.apache.derby.iapi.reference.SQLState;
import org.apache.derby.impl.store.raw.data.BaseContainer;
import org.apache.derby.impl.store.raw.data.BaseContainerHandle;
import org.apache.derby.impl.store.raw.data.BasePage;

import org.apache.derby.iapi.services.cache.Cacheable;
import org.apache.derby.iapi.services.context.ContextService;
import org.apache.derby.iapi.services.monitor.Monitor;
import org.apache.derby.iapi.services.diag.Performance;
import org.apache.derby.iapi.services.sanity.SanityManager;
import org.apache.derby.iapi.services.io.FormatIdUtil;

import org.apache.derby.iapi.util.InterruptStatus;
import org.apache.derby.iapi.util.InterruptDetectedException;

import org.apache.derby.iapi.error.StandardException;

import org.apache.derby.iapi.store.raw.ContainerHandle;
import org.apache.derby.iapi.store.raw.ContainerKey;
import org.apache.derby.iapi.store.raw.Loggable;
import org.apache.derby.iapi.store.raw.log.LogInstant;
import org.apache.derby.iapi.store.raw.xact.RawTransaction;

import org.apache.derby.io.StorageFactory;
import org.apache.derby.io.WritableStorageFactory;
import org.apache.derby.io.StorageFile;
import org.apache.derby.io.StorageRandomAccessFile;
import org.apache.derby.iapi.services.io.FileUtil;
import java.util.Vector;

import java.io.DataInput;
import java.io.IOException;
import java.io.File;
import java.io.RandomAccessFile;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.security.PrivilegedActionException;

/**
	RAFContainer (short for RandomAccessFileContainer) is a concrete subclass of FileContainer
	for FileContainers which are implemented on java.io.RandomAccessFile.
*/

class RAFContainer extends FileContainer implements PrivilegedExceptionAction
{

	/*
	 * Immutable fields
	 */
	protected StorageRandomAccessFile fileData;

	/* 
	** Mutable fields, only valid when the identity is valid.
	*/
	protected boolean			needsSync;

    /* privileged actions */
    private int actionCode;
    private static final int GET_FILE_NAME_ACTION = 1;
    private static final int CREATE_CONTAINER_ACTION = 2;
    private static final int REMOVE_FILE_ACTION = 3;
    private static final int OPEN_CONTAINER_ACTION = 4;
    private static final int STUBBIFY_ACTION = 5;
	private static final int BACKUP_CONTAINER_ACTION = 6;
    private static final int GET_RANDOM_ACCESS_FILE_ACTION = 7;
    private ContainerKey actionIdentity;

    /**
     * Identity of this container. Make it visible to RAFContainer4, which may
     * need to reopen the container after interrupts due to a NIO channel being
     * closed by the interrupt.
     */
    protected ContainerKey currentIdentity;
    private boolean reopen;

    private boolean actionStub;
    private boolean actionErrorOK;
    private boolean actionTryAlternatePath;
    private StorageFile actionFile;
    private LogInstant actionInstant;
	private String actionBackupLocation;
	private BaseContainerHandle actionContainerHandle;

	private boolean inBackup = false;
	private boolean inRemove = false;
        private String fileName;


	/*
	 * Constructors
	 */

	RAFContainer(BaseDataFileFactory factory) {
		super(factory);
	}

	/*
	** Methods overriding super-class
	*/

	synchronized public boolean isDirty() {
		return super.isDirty() || needsSync;
	}

	/*
	** Methods of Cacheable
	*/

	/**
		Set container's identity
		@exception StandardException Standard Derby error policy
	*/
	public Cacheable setIdentity(Object key) throws StandardException {

		ContainerKey newIdentity = (ContainerKey) key;

		// if this is an open for a temp container then return an object of that type
		if (newIdentity.getSegmentId() == ContainerHandle.TEMPORARY_SEGMENT) {

			TempRAFContainer tmpContainer = new TempRAFContainer(dataFactory);
			return tmpContainer.setIdent(newIdentity);
		}

		return setIdent(newIdentity);
	}

	/**
		@exception StandardException Standard Derby error policy
	 */
	public Cacheable createIdentity(Object key, Object createParameter) throws StandardException {

		ContainerKey newIdentity = (ContainerKey) key;

		if (newIdentity.getSegmentId() == ContainerHandle.TEMPORARY_SEGMENT) {
			TempRAFContainer tmpContainer = new TempRAFContainer(dataFactory);
			return tmpContainer.createIdent(newIdentity, createParameter);
		}

		return createIdent(newIdentity, createParameter);
	}


	/*
	** Container creation, opening, and closing
	*/

	/**
		Remove the container

		@exception StandardException Standard Derby error policy
	*/
	protected void removeContainer(LogInstant instant, boolean leaveStub)
		 throws StandardException
	{

		try {
			synchronized(this)
			{
				inRemove = true;
				// wait until the thread that is doing the backup stops 
				// before proceeding with the remove.
				while(inBackup)
				{
					try	{
						wait();
					}
					catch (InterruptedException ie)
					{
                        InterruptStatus.setInterrupted();
					}	
				}
			}

		// discard all of my pages in the cache
		pageCache.discard(identity);
		stubbify(instant);
		}finally
		{	
			synchronized(this) {
				inRemove = false;
				notifyAll();
			}
		}

		// RESOLVE: leaveStub false
	}

	void closeContainer() {

		if (fileData != null) {
			try {
				fileData.close();
			} catch (IOException ioe) {
			} finally {

				fileData = null;
			}
		}
	}


	/*
	** Methods used solely by StoredPage
	*/

	/**
		Read a page into the supplied array.

		<BR> MT - thread safe
		@exception IOException exception reading page
		@exception StandardException Standard Derby error policy
	*/
	protected void readPage(long pageNumber, byte[] pageData)
		 throws IOException, StandardException
	{
		if (SanityManager.DEBUG) {
			SanityManager.ASSERT(!getCommittedDropState());
		}

		long pageOffset = pageNumber * pageSize;

		synchronized (this) {

			fileData.seek(pageOffset);

			fileData.readFully(pageData, 0, pageSize);
		}

		if (dataFactory.databaseEncrypted() &&
			pageNumber != FIRST_ALLOC_PAGE_NUMBER)
		{
			decryptPage(pageData, pageSize);
		}
	}

	/**
		Write a page from the supplied array.

		<BR> MT - thread safe

		@exception StandardException Standard Derby error policy
		@exception IOException IO error accessing page
	*/
	protected void writePage(long pageNumber, byte[] pageData, boolean syncPage)
		 throws IOException, StandardException
	{
		synchronized(this)
		{

			if (getCommittedDropState())
            {
                // committed and dropped, do nothing.
                // This file container may only be a stub

				return;
            }

            ///////////////////////////////////////////////////
            //
            // RESOLVE: right now, no logical -> physical mapping.
            // We can calculate the offset.  In the future, we may need to
            // look at the allocation page or the in memory translation table
            // to figure out where the page should go
            //
            /////////////////////////////////////////////////

			long pageOffset = pageNumber * pageSize;

            byte [] encryptionBuf = null; 
            if (dataFactory.databaseEncrypted() 
                && pageNumber != FIRST_ALLOC_PAGE_NUMBER)
            {
                // We cannot encrypt the page in place because pageData is
                // still being accessed as clear text.  The encryption
                // buffer is shared by all who access this container and can
                // only be used within the synchronized block.

                encryptionBuf = getEncryptionBuffer();
            }

            byte[] dataToWrite = 
                updatePageArray(pageNumber, pageData, encryptionBuf, false);

			try
			{
				fileData.seek(pageOffset);

				/**
					On EPOC (www.symbian.com) a seek beyond the end of
					a file just moves the file pointer to the end of the file.

				*/
				if (fileData.getFilePointer() != pageOffset)
					padFile(fileData, pageOffset);

				dataFactory.writeInProgress();
				try
				{
					fileData.write(dataToWrite, 0, pageSize);
				}
				finally
				{
					dataFactory.writeFinished();
				}
			}
			catch (IOException ioe)
			{
				// On some platforms, if we seek beyond the end of file, or try
				// to write beyond the end of file (not appending to it, but
				// skipping some bytes), it will give IOException.
				// Try writing zeros from the current end of file to pageOffset
				// and see if we can then do the seek/write.  The difference
				// between pageOffset and current end of file is almost always
				// going to be the multiple of pageSize

				if (!padFile(fileData, pageOffset))
					throw ioe;	// not writing beyond EOF, rethrow exception

				if (SanityManager.DEBUG)
                {
					SanityManager.ASSERT(
                        fileData.length() >= pageOffset,
                        "failed to blank filled missing pages");
                }

				fileData.seek(pageOffset);
				dataFactory.writeInProgress();
				try
				{
					fileData.write(dataToWrite, 0, pageSize);
				}
				finally
				{
					dataFactory.writeFinished();
				}
			}

			if (syncPage)
			{
				dataFactory.writeInProgress();
				try
				{
                    if (!dataFactory.dataNotSyncedAtAllocation)
                        fileData.sync();
				}
				finally
				{
					dataFactory.writeFinished();
				}
			}
			else
            {
				needsSync = true;
            }
		}

	}

    /**
     * Update the page array with container header if the page is a first alloc
     * page and encrypt the page data if the database is encrypted.  
     * @param pageNumber the page number of the page
     * @param pageData  byte array that has the actual page data.
     * @param encryptionBuf buffer that is used to store encryted version of the
     * page.
     * @return byte array of the the page data as it should be on the disk.
     */
    protected byte[] updatePageArray(long pageNumber, 
                                   byte[] pageData, 
                                   byte[] encryptionBuf, 
                                   boolean encryptWithNewEngine) 
        throws StandardException, IOException
    {
        if (pageNumber == FIRST_ALLOC_PAGE_NUMBER)
        {
            // write header into the alloc page array regardless of dirty
            // bit because the alloc page have zero'ed out the borrowed
            // space
            writeHeader(getIdentity(), pageData);

            if (SanityManager.DEBUG) 
            {
                if (FormatIdUtil.readFormatIdInteger(pageData) != AllocPage.FORMAT_NUMBER)
                    SanityManager.THROWASSERT(
                            "expect " +
                            AllocPage.FORMAT_NUMBER +
                            "got " +
                            FormatIdUtil.readFormatIdInteger(pageData));
            }

            return pageData;

        } 
        else 
        {
            if (dataFactory.databaseEncrypted() || encryptWithNewEngine) 
            {
                return encryptPage(pageData, 
                                   pageSize, 
                                   encryptionBuf, 
                                   encryptWithNewEngine);
            } 
            else
            {
                return pageData;
            }
        }
    }


	/**
		Pad the file upto the passed in page offset.
		Returns true if the file needed padding.
	*/

	private boolean padFile(StorageRandomAccessFile file, long pageOffset)
		throws IOException, StandardException {

		long currentEOF = file.length();
		if (currentEOF >= pageOffset)
			return false;

		// all objects in java are by definition initialized
		byte zero[] = new byte[pageSize];

		file.seek(currentEOF);

		while(currentEOF < pageOffset)
		{
			dataFactory.writeInProgress();
			try
			{
				long len = pageOffset - currentEOF;
				if (len > pageSize)
					len = pageSize;

				file.write(zero, 0, (int) len);
			}
			finally
			{
				dataFactory.writeFinished();
			}
			currentEOF += pageSize;
		}

		return true;
	}

    /**
     * Clean the container.
     * <p>
     * Write out the container header and sync all dirty pages of this
     * container to disk before returning.
     * <p>
     * checkpoint calls this interface through callbacks by telling
     * the cache manager to clean all containers in the open container
     * cache.  This sync of the file happens as part of writing and then
     * syncing the container header in writeRAFHeader().
     * <p>
     *
     * @param forRemove Is clean called because container is being removed?
     *
	 * @exception  StandardException  Standard exception policy.
     **/
	public void clean(boolean forRemove) throws StandardException
	{
		boolean waited = false;

        // If interrupt recovery is in progress (NIO), we must expect to
        // release our monitor on "this" and to retry writeRAFHeader, so be
        // prepared to retry.
        boolean success = false;
        int maxTries = MAX_INTERRUPT_RETRIES; // ca 60s = (120 * 0.5s)

        while (!success) {
            success = true;

            synchronized (this) {

                // committed and dropped, do nothing.
                // This file container has already been stubbified
                if (getCommittedDropState()) {
                    clearDirty();
                    return;
                }

                // The container is about to change, need to wait till it is
                // really changed.  We are in the predirty state only for the
                // duration where the log record that changed the container has
                // been sent to the log and before the change actually
                // happened.
                while(preDirty == true)
                {
                    waited = true;
                    try
                    {
                        wait();
                    }
                    catch (InterruptedException ie)
                    {
                        InterruptStatus.setInterrupted();
                    }
                }

                if (waited)
                {
                    // someone else may have stubbified this while we waited
                    if (getCommittedDropState())
                    {
                        clearDirty();
                        return;
                    }
                }


                if (forRemove) {

                    //              removeFile()
                    //              clearDirty();

                } else if (isDirty()) {

                    try {

                        // Cannot get the alloc page and write it out because
                        // in order to do so, the alloc page will need to find
                        // this container object.  But this container object is
                        // in the middle of being cleaned and may not be
                        // 'found' and we will hang.
                        //
                        // Instead, just clobber the container info, which is
                        // checksum'ed seperately from the alloc page
                        //
                        writeRAFHeader(
                            getIdentity(),
                            fileData,
                            false,  // don't create, container exists
                            true);  // syncfile

                        clearDirty();

                    } catch (InterruptDetectedException e) {
                        if (--maxTries > 0) {
                            success = false;

                            // Wait a bit so recovery can take place before
                            // we re-grab monitor on "this" (which recovery
                            // needs) and retry writeRAFHeader.
                            try {
                                Thread.sleep(500); // 0.5s
                            } catch (InterruptedException ee) {
                                // This thread received an interrupt as
                                // well, make a note.
                                InterruptStatus.setInterrupted();
                            }

                            continue; // retry write of RAFHeader
                        } else {
                            // We have tried for a minute, not sure what's
                            // going on, so to be on safe side we can't
                            // continue
                            throw StandardException.newException(
                                SQLState.FILE_IO_INTERRUPTED, e);
                        }
                    } catch (IOException ioe) {

                        throw dataFactory.markCorrupt(
                            StandardException.newException(
                                SQLState.FILE_CONTAINER_EXCEPTION, ioe,
                                getIdentity() != null ?
                                getIdentity().toString() : "unknown",
                                "clean", fileName));
                    }
                }
            }
        }
	}

	private void clearDirty() {
		isDirty = false;
		needsSync = false;
	}


	/**
		Preallocate some pages if need be
	*/
	protected int preAllocate(long lastPreallocPagenum, 
							  int preAllocSize)
	{  
	
		/* we had a condition here , that looks at the file size before
		 * preallocation to handle the optimization cases like , we 
		 * preallocated the space and then crashed, as we don;t log the 
		 * preallocated length, we don't have updated value until AlocExtent
		 * page get flushed to the disk. only way to find out that the pages
		 * we want already exist  is to look at the file length.
		 * Althought it was nice thing to do, we had bug no: 3813 from
		 * customer , who for some unexplainable reasons he gets lots of
		 * junk at the end of the file. As junk is not initialized with
		 * format-ID , we get into recovery problem.
		 * To avoid such unforseen conditions, removed the file size check 
		 * condition , as it is better not to fail in recovery than 
		 * losing some special case performance improvement.
		 */
  
		int n = doPreAllocatePages(lastPreallocPagenum, preAllocSize); 

		if (n > 0)				// sync the file
		{
			synchronized(this)
			{
				boolean inwrite = false;
				try
				{
					dataFactory.writeInProgress();
					inwrite = true;

                    if (!dataFactory.dataNotSyncedAtAllocation)
                        fileData.sync();
  				}
				catch (IOException ioe)
				{
					// The disk may have run out of space. 
					// Don't error out in pre-allocation since the user may not
					// actually need this page.
					n = 0;
				}
				catch (StandardException se)
				{
					// some problem calling writeInProgress
					n = 0;
				}
				finally
				{
					if (inwrite)
						dataFactory.writeFinished();
				}
			}
		}

		return n;
	}

    /**
     * Truncate pages of a container.
     * <p>
     * Truncate all pages from lastValidPagenum+1 through the end of the file.
     * <p>
     *
     * @param lastValidPagenum  The page number of the last valid page of the
     *                          file.  All pages after this one are truncated.
     *
	 * @exception  StandardException  Standard exception policy.
     **/
	protected void truncatePages(
    long lastValidPagenum)
        throws StandardException
	{  


        synchronized(this)
        {
            boolean inwrite = false;
            try
            {
                dataFactory.writeInProgress();
                inwrite = true;

                fileData.setLength((lastValidPagenum + 1) * pageSize);
            }
            catch (IOException ioe)
            {
                // The disk may have run out of space. 
                // Don't error out in un-allocation since application can
                // still function even if allocation fails.
            }
            catch (StandardException se)
            {
                // some problem calling writeInProgress
            }
            finally
            {
                if (inwrite)
                    dataFactory.writeFinished();
            }
        }

		return;
	}


	/**
		Write the header of a random access file and sync it
		@param create if true, the container is being created
				if false, the container already exist
		@param syncFile if true, sync the file
	*/
    private void writeRAFHeader(
    Object                  identity,
    StorageRandomAccessFile file, 
    boolean                 create, 
								boolean syncFile) 
		 throws IOException, StandardException
	{
		byte[] epage;
		if (create)
		{
			// the file doesn't exist yet, get an embryonic page buffer

            // Allocating AllocPage.MAX_BORROWED_SPACE bytes for the
            // embryonic page should be enough, but we want to leave
            // the end of the file at a page boundary. This is to work
            // around bugs in the EPOC jvm where a seek beyond the end
            // of a file does not throw an exception but just moves
            // the offset to the end of the file. This only occurs
            // when the second page is written after the header has
            // been written, ending up with the page at the incorrect
            // offset.

            epage = new byte[pageSize];
		}
		else
		{
			epage = getEmbryonicPage(file, FIRST_ALLOC_PAGE_OFFSET);
		}

		// need to check for frozen state

        writeHeader(identity, file, create, epage);

		if (syncFile)
		{
			dataFactory.writeInProgress();
			try
			{
                if (!dataFactory.dataNotSyncedAtCheckpoint)
                   file.sync();

			}
			finally
			{
				dataFactory.writeFinished();
			}
		}
	}

	/**
		flush the cache to ensure all of my pages are written to disk

		@exception StandardException Standard Derby error policy
	*/
	protected void flushAll() throws StandardException {

		pageCache.clean(identity);

		// now clean myself which will sync all my pages.
		clean(false);
	}


	 synchronized StorageFile getFileName(ContainerKey identity, boolean stub,
											 boolean errorOK, boolean tryAlternatePath)
		 throws StandardException
	 {
         // RESOLVE - READ ONLY

         actionCode = GET_FILE_NAME_ACTION;
         actionIdentity = identity;
         actionStub = stub;
         actionErrorOK = errorOK;
         actionTryAlternatePath = tryAlternatePath;
         try
         {
             return (StorageFile) AccessController.doPrivileged( this);
         }
         catch( PrivilegedActionException pae){ throw (StandardException) pae.getException();}
         finally{ actionIdentity = null; }
	 }

    protected StorageFile privGetFileName(ContainerKey identity, boolean stub,
                                    boolean errorOK, boolean tryAlternatePath)
        throws StandardException
    {
        StorageFile container = dataFactory.getContainerPath( identity, stub);

        // retry with small case 'c' and 'd'
        // bug fix for track 3444
        if (!container.exists() && tryAlternatePath)
        {
            container = dataFactory.getAlternateContainerPath( identity, stub);
        }

        if (!container.exists()) {

            StorageFile directory = container.getParentDir();

            if (!directory.exists())
            {
                // make sure only 1 thread can create a segment at one time
                synchronized(dataFactory)
                {
                    if (!directory.exists())
                    {
                        if (!directory.mkdirs())
                        {
                            if (errorOK)
                            {
                                return null;
                            }
                            else
                            {
                                throw StandardException.newException(
                                    SQLState.FILE_CANNOT_CREATE_SEGMENT,
                                    directory);
                            }
                        }
                    }
                }
            }
        }

        return container;
    } // end of privGetFileName


	synchronized void createContainer(ContainerKey newIdentity)
        throws StandardException
    {

		if (SanityManager.DEBUG) {
			if ((spareSpace < 0) || (spareSpace > 100))
				SanityManager.THROWASSERT("invalid spare space " + spareSpace);
		}

        actionCode = CREATE_CONTAINER_ACTION;
        actionIdentity = newIdentity;
        try
        {
            AccessController.doPrivileged( this);
            currentIdentity = newIdentity;
        }
        catch( PrivilegedActionException pae){ throw (StandardException) pae.getException();}
        finally{ actionIdentity = null; }
    } // end of createContainer

	synchronized boolean removeFile(StorageFile file)
        throws SecurityException, StandardException
    {
        actionCode = REMOVE_FILE_ACTION;
        actionFile = file;
        try
        {
            return AccessController.doPrivileged( this) != null;
        }
        catch( PrivilegedActionException pae){ throw (StandardException) pae.getException();}
        finally{ actionFile = null; }
    } // end of removeFile

    private boolean privRemoveFile(StorageFile file)
        throws StandardException
    {
		closeContainer();

		dataFactory.writeInProgress();
		try
		{
            if (file.exists())
                return file.delete();
		}
		finally
		{
			dataFactory.writeFinished();
		}

		return true;
    } // end of privRemoveFile

    protected ContainerKey idAPriori = null;

    synchronized boolean openContainer(ContainerKey newIdentity)
            throws StandardException {
        return openContainerMinion(newIdentity, false);
    }

    synchronized boolean reopenContainer(ContainerKey newIdentity)
            throws StandardException {
        return openContainerMinion(newIdentity, true);
    }

    private boolean openContainerMinion(
        ContainerKey newIdentity,
        boolean doReopen) throws StandardException
    {
        actionCode = OPEN_CONTAINER_ACTION;
        reopen = doReopen;
        actionIdentity = newIdentity;
        boolean success = false;
        idAPriori = currentIdentity;

        try
        {
            currentIdentity = newIdentity;
            // NIO: We need to set currentIdentity before we try to open, in
            // case we need its value to perform a recovery in the case of an
            // interrupt during readEmbryonicPage as part of
            // OPEN_CONTAINER_ACTION.  Note that this gives a recursive call to
            // openContainer.
            //
            // If we don't succeed in opening, we reset currentIdentity to its
            // a priori value.

            success = AccessController.doPrivileged(this) != null;
            idAPriori = currentIdentity;
            return success;
        }
        catch( PrivilegedActionException pae) { 
            closeContainer();
            throw (StandardException) pae.getException();
        }
        catch (RuntimeException e) {
            closeContainer();
            throw e;
        }
        finally
        {
            if (!success) {
                currentIdentity = idAPriori;
            }

            actionIdentity = null; 
        }
    }

	private synchronized void stubbify(LogInstant instant)
        throws StandardException
	{
         // update header, synchronized this in case the cache is cleaning
         // this container at the same time.  Make sure the clean and
         // stubbify is mutually exclusive.
         setDroppedState(true);
         setCommittedDropState(true);

		 // The whole container should be shrunk into a 'stub'.
		 // If the file system supports truncation, we can just truncate the
		 // file after the header.  Since it doesn't, we need to write out a
		 // seperate file (the stub), then reset fileData to point to that,
		 // then remove the current file.
		 //
		 // There may still be dirty pages that belongs to this file which are
		 // still in the page cache.  They need not really
		 // be written since they don't really exist anymore
		 //
		 // there are 3 pieces of information on disk :
		 // 1) the log operation that caused this file to be stubbified
		 // 2) the stub
		 // 3) the file
		 //
		 // The order of event, as far as persisent store is concerned, is
		 // A) stub shows up
		 // B) the file disappear
		 // C) the log operation got flushed
		 // (B and C may swap order)
		 //
		 // If neither A or B happens (we crashed before the sync call),
		 // then nothing happened.
		 //
		 // if A happened but B and C did not, then when we recover, we will not
		 // know the file has been stubbified.  Hopefully, it will be stubbified
		 // again if the post-commit queue manager is alerted to the fact.
		 //
		 // if A and B happened but C did not, then the file is stubbified but
		 // there is no log record to indicate that.  This is undesirable but
		 // still safe because the only time we stubbify is on a post commit
		 // operation, i.e., either a create container has rolled back or a
		 // dropped container has committed.  We end up having a a container
		 // stub which behaves the same as a dropped container - only that all
		 // the redo work is unnecessary because we 'know' it will
		 // eventually be dropped and committed.
		 //
		 // If A and C happened and not B, then during redo, this stubbify
		 // routine will be called again and the file will be deleted again
		 //
		 // The reason why A has to be sync'ed out is that we don't want B to
		 // happen but A did not and the system crashed.  Then we are left
		 // with neither the file nor the stub and maybe even no log record.
		 // Then the system is not recoverable.

		actionIdentity = (ContainerKey)getIdentity();
        actionInstant = instant;
        actionCode = STUBBIFY_ACTION;
        try
        {
            AccessController.doPrivileged( this);
        }
        catch( PrivilegedActionException pae){ throw (StandardException) pae.getException();}
        finally
        {
            actionIdentity = null;
            actionInstant = null;
        }
    }




		
    /**
     * Backup the  container.
     * 
     * @param handle the container handle.
     * @param backupLocation location of the backup container. 
     * @exception StandardException Standard Derby error policy 
     */
	protected void backupContainer(BaseContainerHandle handle,	String backupLocation)
	    throws StandardException 
	{
		actionContainerHandle = handle;
        actionBackupLocation = backupLocation;
        actionCode = BACKUP_CONTAINER_ACTION;
        try
        {
            AccessController.doPrivileged(this);
        }
        catch( PrivilegedActionException pae){ throw (StandardException) pae.getException();}
        finally
        {
            actionContainerHandle = null;
            actionBackupLocation = null;
        }
	}


    /**
     * Backup the  container.
     *
     * The container is written to the backup by reading  the pages
     * through the page cache, and then writing into the backup container.
     * If the container is dropped(commitetd drop), only container stub is
     * copied to the  backup using simple file copy. 
     * 
     * MT - 
     * At any given time only one backup thread is allowed, but when backup in 
     * progress DML/DDL operations can run in parallel. Pages are latched while 
     * writing them to the backup to avoid copying partial changes to the pages.
     * Online backup does not acquire any user level locks , so users can drop
     * tables when backup is in progress. So it is possible that Container 
     * Removal request can come in when container backup is in progress.  
	 * This case is handled by using the synchronization on this object monitor 
	 * and using inRemove and inBackup flags. Conatiner removal checks if backup
     * is in progress and wait for the backup to yield to continue the removal. 
     * Basic idea is to give preference to remove by stopping the backup of the 
     * container temporarily,  when the remove container is requested by another 
     * thread. Generally, it takes more  time to backup a regular container than 
     * the stub becuase  stub is just one page. After each page copy, a check is
     * made to find  if a remove is requested and if it is then backup of the 
     * container is aborted and the backup thread puts itself into the wait state until
	 * remove  request thread notifies that the remove is complete. When 
	 * remove request compeletes stub is copied into the backup.
	 * 
     * Compress is blocked when backup is in progesss, so truncation of the
     * container can not happen when backup is in progess. No need to
     * synchronize backup of the container with truncation. 
     * 
     * 
     * @param handle the container handle.
     * @param backupLocation location of the backup container. 
     * @exception StandardException Derby Standard error policy
     *
     */
    private void privBackupContainer(BaseContainerHandle handle,	
                                     String backupLocation)
        throws StandardException 
    {
        boolean backupCompleted = false;
        File backupFile = null;
        RandomAccessFile backupRaf = null;
        boolean isStub = false;
        BasePage page = null; 

        while(!backupCompleted) {
            try {

                synchronized (this) {
                    // wait if some one is removing the 
                    // container because of a drop.
                    while (inRemove)
                    {
                        try	{
                            wait();
                        }
                        catch (InterruptedException ie)
                        {
                            InterruptStatus.setInterrupted();
                        }	
                    }

                    if (getCommittedDropState())
                        isStub = true;
                    inBackup = true;
                }
			
                // create container at the backup location.
                if (isStub) {
                    // get the stub ( it is a committted drop table container )
                    StorageFile file = privGetFileName((ContainerKey)getIdentity(), 
                                                       true, false, true);
                    backupFile = new File(backupLocation, file.getName());

					// directly copy the stub to the backup 
					if(!FileUtil.copyFile(dataFactory.getStorageFactory(), 
                                          file, backupFile))
                    {
                        throw StandardException.newException(
                                              SQLState.RAWSTORE_ERROR_COPYING_FILE,
                                              file, backupFile);
                    }
                }else {
                    // regular container file 
                    long lastPageNumber= getLastPageNumber(handle);
                    if (lastPageNumber == ContainerHandle.INVALID_PAGE_NUMBER) {
                        // last page number is invalid if there are no pages in
                        // the container yet. No need to backup this container, 
                        // this container creation is yet to complete.The reason
                        // backup is getting called on such a container is 
                        // because container handle appears in the cache after 
                        // the file is created on the disk but before it's 
                        // first page is allocated. 
                        return;
                    }

                    StorageFile file = 
                        privGetFileName(
                            (ContainerKey)getIdentity(), false, false, true);

                    backupFile = new File(backupLocation , file.getName());
                    backupRaf  = new RandomAccessFile(backupFile,  "rw");

                    byte[] encryptionBuf = null;
                    if (dataFactory.databaseEncrypted()) {
                        // Backup uses seperate encryption buffer to encrypt the
                        // page instead of encryption buffer used by the regular
                        // conatiner writes. Otherwise writes to the backup 
                        // has to be synchronized with regualar database writes
                        // because backup can run in parallel to container
                        // writes.
                        encryptionBuf = new byte[pageSize];
                    }

                    // copy all the pages of the container from the database 
                    // to the backup location by reading through the page cache.
                    for (long pageNumber = FIRST_ALLOC_PAGE_NUMBER; 
                         pageNumber <= lastPageNumber; pageNumber++) {
                        page = getLatchedPage(handle, pageNumber);
                        
                        // update the page array before writing to the disk 
                        // with container header and encrypt it if the database 
                        // is encrypted. 
                        
                        byte[] dataToWrite = 
                            updatePageArray(
                                pageNumber, 
                                                             page.getPageArray(), 
                                encryptionBuf, 
                                false);
                        backupRaf.write(dataToWrite, 0, pageSize);

                        // unlatch releases page from cache, see 
                        // StoredPage.releaseExclusive()
                        page.unlatch();
                        page = null;

                        // check if some one wants to commit drop the table while
                        // conatiner is being written to the backup. If so,
                        // abort  the backup and restart it once the drop 
                        // is complete.

						synchronized (this)
						{
							if (inRemove) {
								break; 
							}
						}
					}
				}	

                // sync and close the backup conatiner. Incase of a stub, 
                // it is already synced and closed while doing the copy.
                if(!isStub) {
                    backupRaf.getFD().sync();
                    backupRaf.close();
                    backupRaf = null;
                }
                
                // backup of the conatiner is complete. 
                backupCompleted = true;

            }catch (IOException ioe) {
                throw StandardException.newException(
                                                SQLState.BACKUP_FILE_IO_ERROR, 
                                                ioe, 
                                                backupFile);
            } finally {
                synchronized (this) {
                    inBackup = false;
                    notifyAll();
                }

                if (page != null) {
                    page.unlatch();
                    page = null;
                }

                // if backup of container is not complete, close the file
                // handles and  remove the container file from the backup 
                // if it exists
                if (!backupCompleted && backupFile != null) 
                {
                    if (backupRaf != null) 
                    {
						try {
                            backupRaf.close();
                            backupRaf = null;
                        } catch (IOException ioe){
                            throw StandardException.newException(
                                            SQLState.BACKUP_FILE_IO_ERROR, 
                                            ioe, 
                                            backupFile);
                        }
                    }

                    if(backupFile.exists()) 
                    {
                        if (!backupFile.delete())
                            throw StandardException.newException(
                                                SQLState.UNABLE_TO_DELETE_FILE, 
                                                backupFile);
                    }
                } 
            }
        }
    }




    /**
     * Create encrypted version of the  container with the 
     * user specified encryption properties. 
     * 
     * Read all the pages of the container from the original container 
     * through the page cache, encrypt each page data with new encryption 
     * mechanism and  write to the specified container file.
     *
     * @param handle the container handle.
     * @param newFilePath file to store the new encrypted version of 
     *                    the container
     * @exception StandardException Derby Standard error policy
     *
     */
    protected void encryptContainer(BaseContainerHandle handle, 
                                    String newFilePath)	
        throws StandardException 
    {
        BasePage page = null; 
        StorageFile newFile = 
            dataFactory.getStorageFactory().newStorageFile(newFilePath);
        StorageRandomAccessFile newRaf = null;
        try {
            long lastPageNumber= getLastPageNumber(handle);
 
            newRaf = privGetRandomAccessFile(newFile);

            byte[] encryptionBuf = null;
            encryptionBuf = new byte[pageSize];

            // copy all the pages from the current container to the 
            // new container file after encryting the pages. 
            for (long pageNumber = FIRST_ALLOC_PAGE_NUMBER; 
                 pageNumber <= lastPageNumber; pageNumber++) 
            {

                page = getLatchedPage(handle, pageNumber);
                        
                // update the page array before writing to the disk 
                // with container header and encrypt it.
                        
                byte[] dataToWrite = updatePageArray(pageNumber, 
                                                     page.getPageArray(), 
                                                     encryptionBuf, 
                                                     true);
                newRaf.write(dataToWrite, 0, pageSize);

                // unlatch releases page from cache.
                page.unlatch();
                page = null;
            }

            // sync the new version of the container.
            newRaf.sync();
            newRaf.close();
            newRaf = null;
            
        }catch (IOException ioe) {
            throw StandardException.newException(
                                    SQLState.FILE_CONTAINER_EXCEPTION, ioe,
                                    getIdentity() != null ?
                                        getIdentity().toString() : "unknown",
                                    "encrypt", newFilePath);
        } finally {

            if (page != null) {
                page.unlatch();
                page = null;
            }
            
            if (newRaf != null) {
                try {
                    newRaf.close();
                }catch (IOException ioe) 
                {
                    newRaf = null;
                    throw StandardException.newException(
                                    SQLState.FILE_CONTAINER_EXCEPTION, ioe,
                                    getIdentity() != null ?
                                        getIdentity().toString() : "unknown",
                                    "encrypt-close", newFilePath);
                    
                }
            }
        }
    }


    synchronized StorageRandomAccessFile privGetRandomAccessFile(StorageFile file)
        throws SecurityException, StandardException
    {
        actionCode = GET_RANDOM_ACCESS_FILE_ACTION;
        actionFile = file;
        try
        {
            return (StorageRandomAccessFile)AccessController.doPrivileged(this);
        }
        catch( PrivilegedActionException pae){ 
            throw (StandardException) pae.getException();
        }
        finally{ actionFile = null; }
    }



     // PrivilegedExceptionAction method
    public Object run() throws StandardException, IOException
     {
         switch( actionCode)
         {
         case GET_FILE_NAME_ACTION:
             return privGetFileName( actionIdentity, actionStub, actionErrorOK, actionTryAlternatePath);

         case CREATE_CONTAINER_ACTION:
         {
             StorageFile file = privGetFileName( actionIdentity, false, false, false);

             try {
                 if (file.exists()) {
                     // note I'm left in the no-identity state as fillInIdentity()
                     // hasn't been called.
                     throw StandardException.newException( SQLState.FILE_EXISTS, file);
                 }
             } catch (SecurityException se) {
                 throw StandardException.newException( SQLState.FILE_CREATE, se, file);
             }

             try {

                 // OK not to force WAL here, in fact, this operation preceeds the
                 // creation of the log record to ensure sufficient space.

                 dataFactory.writeInProgress();
                 try
                 {
                     fileData = file.getRandomAccessFile( "rw");
                 }
                 finally
                 {
                     dataFactory.writeFinished();
                 }

                 // This container format specifies that the first page is an
                 // allocation page and the container information is stored 
                 // within it.  The allocation page needs to be somewhat 
                 // formatted because if the system crashed after the create 
                 // container log operation is written, it needs to be well 
                 // formed enough to get the container information back out of
                 // it.
                 //
                 // Don't try to go thru the page cache here because the 
                 // container object cannot be found in the container cache at
                 // this point yet.  However, if we use the page cache to store
                 // the first allocation page, then in order to write itself 
                 // out, it needs to ask the container to do so, which is going
                 // to create a deadlock.  The allocation page cannot write 
                 // itself out without going thru the container because it 
                 // doesn't know where its offset is.  Here we effectively 
                 // hardwire page 0 at offset 0 of the container file to be 
                 // the first allocation page.

                 // create an embryonic page - if this is not a temporary 
                 // container, synchronously write out the file header.
                 writeRAFHeader(
                     actionIdentity, fileData, true, 
                     (actionIdentity.getSegmentId() != 
                          ContainerHandle.TEMPORARY_SEGMENT));

             } catch (SecurityException se) {

                 // only thrown by the RandomeAccessFile constructor,
                 // so the file won't exist
                 throw StandardException.newException( SQLState.FILE_CREATE, se, file);

             } catch (IOException ioe) {

                 boolean fileDeleted;
                 try {
                     fileDeleted = privRemoveFile(file);
                 } catch (SecurityException se) {
                     throw StandardException.newException( SQLState.FILE_CREATE_NO_CLEANUP, ioe, file, se.toString());
                 }

                 if (!fileDeleted) {
                     throw StandardException.newException( SQLState.FILE_CREATE_NO_CLEANUP, ioe, file, ioe.toString());
                 }

                 throw StandardException.newException( SQLState.FILE_CREATE, ioe, file);
             }

             canUpdate = true;
             return null;
         } // end of case CREATE_CONTAINER_ACTION

         case REMOVE_FILE_ACTION:
             return privRemoveFile( actionFile) ? this : null;

         case OPEN_CONTAINER_ACTION:
         {
             boolean isStub = false;	// is this a stub?

             StorageFile file = privGetFileName( actionIdentity, false, true, true);
             if (file == null)
                 return null;

             try {
                 if (!file.exists()) {

                     // file does not exist, may be it has been stubbified
                     file = privGetFileName( actionIdentity, true, true, true);
                     if (!file.exists())
                         return null;
                     isStub = true;
                 }
             } catch (SecurityException se) {
                 throw StandardException.newException(
                     SQLState.DATA_UNEXPECTED_EXCEPTION, se);
             }

             canUpdate = false;
             try {
                 if (!dataFactory.isReadOnly() && file.canWrite())
                     canUpdate = true;
             } catch (SecurityException se) {
                 // just means we can't write to it.
             }
             fileName = file.toString();

             try {

                 fileData = file.getRandomAccessFile(canUpdate ? "rw" : "r");

                 if (!reopen) {
                     // under reopen: can give race condition or if we
                     // synchronize access, deadlock, so skip, we know
                     // what's there anyway.
                     readHeader(getEmbryonicPage(fileData,
                                                 FIRST_ALLOC_PAGE_OFFSET));
                 }


                 if (SanityManager.DEBUG)
                 {
                     if (isStub)
                         SanityManager.ASSERT(getDroppedState() && getCommittedDropState(),
                                              "a stub failed to set drop state");
                 }

             } catch (IOException ioe) {

                 if (isStub)
                 {
                     throw dataFactory.
                         markCorrupt(StandardException.
                                     newException(SQLState.
                                             FILE_CONTAINER_EXCEPTION, ioe,
                                             getIdentity() != null ?
                                                 getIdentity().toString() :
                                                 "unknown",
                                             "read", fileName));
                 }

                 // maybe it is being stubbified... try that
                 StorageFile stub = 
                     privGetFileName(actionIdentity, true, true, true);

                 if (stub.exists())
                 {
                     try
                     {
                         boolean delete_status = privRemoveFile(file);
                         if (SanityManager.DEBUG)
                         {
                             if (!delete_status)
                             {
                                 SanityManager.THROWASSERT(
                                     "delete of file (" + file + ") failed.");
                             }
                         }

                         fileData = 
                             stub.getRandomAccessFile(canUpdate ? "rw" : "r");

                         readHeader(getEmbryonicPage(fileData,
                                                     FIRST_ALLOC_PAGE_OFFSET));
                     }
                     catch (IOException ioe2)
                     {
                         throw dataFactory.
                             markCorrupt(StandardException.
                                         newException(SQLState.
                                                      FILE_CONTAINER_EXCEPTION,
                                             ioe2,
                                             getIdentity() != null ?
                                                 getIdentity().toString() :
                                                 "unknown",
                                             "delete-stub", fileName));
                     }

                     // RESOLVE: this is a temporary hack

                 }
                 else
                     throw dataFactory.
                         markCorrupt(StandardException.
                                     newException(SQLState.
                                                  FILE_CONTAINER_EXCEPTION, ioe,
                                             getIdentity() != null ?
                                                 getIdentity().toString() :
                                                 "unknown",
                                             "read", fileName));
             }

             return this;
         } // end of case OPEN_CONTAINER_ACTION

         case STUBBIFY_ACTION:
         {
             StorageFile file = privGetFileName( actionIdentity, false, false, true);
             StorageFile stub = privGetFileName( actionIdentity, true, false, false);

             StorageRandomAccessFile stubData = null;

             try
             {
                 // !!!!!
                 // bumpContainerVersion();
                 //
                 // do NOT bump the container version.  We WANT the stubbify
                 // operation to get redone every time.  This is because this
                 // operation first writes out the stub and then remove the
                 // container file.  If we bump the version, then the stub will
                 // contain the new version.  And if the system crashes right then,
                 // then we will skip the whole operation during redo even though
                 // the container file may not have been removed.  Since we don't
                 // want to have the remove happen before the stub is written, we
                 // cannot sync it and therefore cannot be sure the remove
                 // happened before the system crashed.

                 if (!stub.exists())
                 {
                     // write the header to the stub
                     stubData = stub.getRandomAccessFile( "rw");

                     writeRAFHeader(
                        actionIdentity,
                        stubData,
                                    true, /* create */
                                    true); /* sync */

                     stubData.close();
                     stubData = null;
                 }


                 // Force WAL and check for database corruption before removing file.
                 // This is one operation where the container is changed on disk
                 // directly without going thru the container cache, which otherwise
                 // would have force WAL.  Take care of it here.
                 dataFactory.flush(actionInstant);

                 // try to remove the container file
                 // fileDate is not null only if we are redoing a removeContainer
                 // (stubbify) operation.  Then fileData acutally is opened against
                 // the stub and the original container file does not exist.
                 // Then we need to close it here because this method is called by
                 // cache.remove and nobody will be able to see fileData after this.
                 privRemoveFile(file);

             }
             catch (SecurityException se)
             {
                 throw StandardException.
                     newException(SQLState.FILE_CANNOT_REMOVE_FILE, se, file, 
                                  se.toString());
             }
             catch (IOException ioe)
             {
                 // exception thrown while in creating the stub.  Remove the
                 // (half-baked) stub
                 try
                 {
                     if (stubData != null)
                     {
                         stubData.close();
                         stub.delete();
                         stubData = null;
                     }

                     if (fileData != null)
                     {
                         fileData.close();
                         fileData = null;
                     }
                 }
                 catch (IOException ioe2)
                 {
                     throw StandardException.newException(
                         SQLState.FILE_CANNOT_REMOVE_FILE, ioe2, file, ioe.toString());
                 }
                 catch (SecurityException se)
                 {
                     throw StandardException.newException(
                         SQLState.FILE_CANNOT_REMOVE_FILE, se, file, stub);
                 }
             }
	
             //let the data factory know about this the stub file;It
             // could  remove when next checkpoint occurs if it's not necessary for recovery
             dataFactory.stubFileToRemoveAfterCheckPoint(stub,actionInstant, getIdentity());
             return null;
         } // end of case STUBBIFY_ACTION
		 
		 case BACKUP_CONTAINER_ACTION: {
			 privBackupContainer(actionContainerHandle, actionBackupLocation);
			 return null;
		 } // end of case BACKUP_CONTAINER_ACTION

         case GET_RANDOM_ACCESS_FILE_ACTION: {
             return actionFile.getRandomAccessFile("rw");
		 } // end of case BACKUP_CONTAINER_ACTION

		 
		 } // end of switch
         return null;

     } // end of run
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2925.java