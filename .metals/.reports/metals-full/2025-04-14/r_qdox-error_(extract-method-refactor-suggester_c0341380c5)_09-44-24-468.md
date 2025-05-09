error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9385.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9385.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9385.java
text:
```scala
public synchronized I@@HeaderList getHeaderList() throws Exception {

// The contents of this file are subject to the Mozilla Public License Version
// 1.1
//(the "License"); you may not use this file except in compliance with the
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.

package org.columba.mail.folder.imap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.columba.api.command.IStatusObservable;
import org.columba.core.base.ListTools;
import org.columba.core.command.CommandCancelledException;
import org.columba.core.command.CommandProcessor;
import org.columba.core.connectionstate.ConnectionStateImpl;
import org.columba.core.xml.XmlElement;
import org.columba.mail.command.MailFolderCommandReference;
import org.columba.mail.config.FolderItem;
import org.columba.mail.config.IFolderItem;
import org.columba.mail.config.ImapItem;
import org.columba.mail.folder.AbstractRemoteFolder;
import org.columba.mail.folder.IHeaderListCorruptedListener;
import org.columba.mail.folder.IMailFolder;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.folder.RootFolder;
import org.columba.mail.folder.command.ApplyFilterCommand;
import org.columba.mail.folder.headercache.BerkeleyDBHeaderList;
import org.columba.mail.folder.headercache.CachedHeaderfields;
import org.columba.mail.folder.search.DefaultSearchEngine;
import org.columba.mail.folder.search.IMAPQueryEngine;
import org.columba.mail.imap.IImapServer;
import org.columba.mail.message.ColumbaHeader;
import org.columba.mail.message.ICloseableIterator;
import org.columba.mail.message.IColumbaHeader;
import org.columba.mail.message.IHeaderList;
import org.columba.mail.message.IPersistantHeaderList;
import org.columba.mail.parser.PassiveHeaderParserInputStream;
import org.columba.mail.util.MailResourceLoader;
import org.columba.ristretto.imap.IMAPException;
import org.columba.ristretto.imap.IMAPFlags;
import org.columba.ristretto.imap.MailboxStatus;
import org.columba.ristretto.imap.SearchKey;
import org.columba.ristretto.imap.SequenceEntry;
import org.columba.ristretto.imap.SequenceSet;
import org.columba.ristretto.message.Attributes;
import org.columba.ristretto.message.Flags;
import org.columba.ristretto.message.Header;
import org.columba.ristretto.message.MimeTree;

public class IMAPFolder extends AbstractRemoteFolder {

	/** JDK 1.4+ logging framework logger, used for logging. */
	private static final Logger LOG = Logger
			.getLogger("org.columba.mail.folder.imap");

	/**
	 * 
	 */
	private IImapServer store;

	private IStatusObservable observable;
	
	/**
	 * 
	 */
	protected boolean existsOnServer = true;

	private boolean readOnly;

	private IPersistantHeaderList headerList;

	private boolean firstSync = true;

	private boolean mailboxSyncEnabled = true;

	/**
	 * @see org.columba.mail.folder.IMailbox#isReadOnly()
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	
	/**
	 * Constructor for testing purposes only!
	 * 
	 */
	public IMAPFolder(IPersistantHeaderList headerList, IImapServer server) {
		super("test","IMAPFolder","/tmp/");
		this.headerList = headerList;
		this.observable = new DummyObservable();
		
		store = server;
	}
	
	/**
	 * @see org.columba.mail.folder.FolderTreeNode#FolderTreeNode(org.columba.mail.config.FolderItem)
	 */
	public IMAPFolder(FolderItem folderItem, String path) {
		super(folderItem, path);

		DefaultSearchEngine engine = new DefaultSearchEngine(this);
		engine.setNonDefaultEngine(new IMAPQueryEngine(this));
		setSearchEngine(engine);

		headerList = new BerkeleyDBHeaderList(new File(this.getDirectoryFile(),
				"headerlist"));

		headerList
				.addHeaderListCorruptedListener(new IHeaderListCorruptedListener() {

					public void headerListCorrupted(IHeaderList headerList) {
						headerList.clear();
						getMessageFolderInfo().reset();
						fireFolderPropertyChanged();
					}
				});
	}

	/**
	 * @param type
	 */
	public IMAPFolder(String name, String type, String path) throws Exception {
		super(name, type, path);

		IFolderItem item = getConfiguration();
		item.setString("property", "accessrights", "user");
		item.setString("property", "subfolder", "true");

		headerList = new BerkeleyDBHeaderList(new File(this.getDirectoryFile(),
				"headerlist"));
	}

	/**
	 * @see org.columba.mail.folder.FolderTreeNode#removeFolder()
	 */
	public void removeFolder() throws Exception {
		try {
			if (existsOnServer) {
				String path = getImapPath();

				getServer().deleteFolder(path);
			}

			super.removeFolder();
		} catch (Exception e) {
			throw e;
		}
	}

	public void setName(String name) throws Exception {
		if (getName() == null) { // if creating new folder
			super.setName(name);
			return;
		}

		String oldPath = getImapPath();
		LOG.info("old path=" + oldPath);

		String newPath = null;

		if (getParent() instanceof IMAPFolder) {
			newPath = ((IMAPFolder) getParent()).getImapPath();
		}

		newPath += (getServer().getDelimiter() + name);
		LOG.info("new path=" + newPath);

		getServer().renameFolder(oldPath, newPath);
		super.setName(name);
	}

	/**
	 * Method getStore.
	 * 
	 * @return IMAPStore
	 */
	public IImapServer getServer() {
		if (store == null) {
			store = ((IMAPRootFolder) getRootFolder()).getServer();
		}

		return store;
	}

	/**
	 * @see org.columba.mail.folder.Folder#getHeaderList(org.columba.api.command.IWorkerStatusController)
	 */
	public IHeaderList getHeaderList() throws Exception {
		if (mailboxSyncEnabled  && ConnectionStateImpl.getInstance().isOnline() && !getServer().isSelected(this)) {
			// Trigger Synchronization
			CommandProcessor.getInstance().addOp(
					new CheckForNewMessagesCommand(
							new MailFolderCommandReference(this)));
		}

		return headerList;
	}

	protected ImapItem getImapItem() {
		return getServer().getItem();
	}

	protected void printStatusMessage(String message) {
		if (getObservable() != null) {
			getObservable().setMessage(
					getImapItem().get("host") + ": " + message);
		}
	}

	protected void setProgress(int progress) {
		if (getObservable() != null) {
			getObservable().setCurrent(progress);
		}
	}

	protected void setMaximum(int progress) {
		if (getObservable() != null) {
			getObservable().setMax(progress);
		}
	}

	/**
	 * @throws Exception
	 * @throws IOException
	 * @throws CommandCancelledException
	 * @throws IMAPException
	 */
	void synchronizeHeaderlist() throws Exception, IOException,
			CommandCancelledException, IMAPException {
		// Check if the mailbox has changed
		MailboxStatus status = getServer().getStatus(this);

		if (status.getMessages() == 0) {
			purgeHeaderList();

			syncMailboxInfo(status);

			return;
		}

		if (!firstSync
				&& status.getMessages() == this.getMessageFolderInfo()
						.getExists()) {
			return;
		}

		List localUids = new LinkedList(Arrays
				.asList(getHeaderList().getUids()));
		// Sort the uid list
		Collections.sort(localUids);

		int largestLocalUid = localUids.size() > 0 ? ((Integer) localUids
				.get(localUids.size() - 1)).intValue() : -1;

		int largestRemoteUid = getServer().getLargestRemoteUid(this);
		
		if (localUids.size() == status.getMessages()
				&& largestRemoteUid == largestLocalUid) {
			// Seems to be no change!
			if (firstSync) {
				firstSync = false;
				synchronizeFlags(status);
			} else {
				syncMailboxInfo(status);
			}

			return;
		}

		printStatusMessage(MailResourceLoader.getString("statusbar", "message",
				"sync_messages"));

		largestRemoteUid = getServer().fetchUid(
				new SequenceSet(SequenceEntry.STAR), this);
		
		if (largestRemoteUid == -1) {
			largestRemoteUid = getServer().fetchUid(
					new SequenceSet(status.getMessages()), this);
		}

		int largestLocalUidIndex = findLargestLocalUidIndex(localUids);

		int newMessages = status.getMessages() - largestLocalUidIndex;

		/*
		 * // Somehow there are new messages that // have a lower index -> out
		 * of sync if (localUids.size() - status.getMessages() + newMessages <
		 * 0) {
		 * 
		 * LOG.severe("Folder " + getName() + " is out of sync -> recreating the
		 * cache!"); purgeHeaderList(); // all messages are new newMessages =
		 * status.getMessages(); // Set the index of the largest Uid to 0 // ->
		 * ensure it works with the fetch of new // messages part below
		 * largestLocalUidIndex = 0; largestLocalUid = -1;
		 * 
		 * localUids.clear(); }
		 */
		LOG.fine("Found " + newMessages + " new Messages");

		// If we have new messages add them to the headerlist
		if (newMessages > 0) {
			printStatusMessage(MailResourceLoader.getString("statusbar",
					"message", "fetch_new_headers"));

			// Ensure sizes are correct
			getMessageFolderInfo().setExists(localUids.size());
			getMessageFolderInfo().setUnseen(
					Math.min(getMessageFolderInfo().getUnseen(), localUids
							.size()));
			getMessageFolderInfo().setRecent(0);

			List newUids = fetchNewMessages(largestLocalUidIndex);

			localUids.addAll(newUids);

			if (newUids.size() < newMessages) {
				// There are still more messages to update
				// -> issue another fetch messages command
				CommandProcessor.getInstance().addOp(
						new FetchMessagesCommand(
								new MailFolderCommandReference(this),
								newMessages, largestLocalUidIndex, newUids
										.size()));

			} else {
				fetchDone();
			}
		} else {

			// Number of deleted messages is computed from exists on imap and
			// local
			// newMessages
			findRemovedMessages(status, localUids);

			if (firstSync) {
				firstSync = false;
				synchronizeFlags(status);
			} else {
				syncMailboxInfo(status);
			}
		}

	}

	private int findLargestLocalUidIndex(List localUids) throws IOException,
			IMAPException, CommandCancelledException {
		int largestLocalUidIndex = -1;

		printStatusMessage(MailResourceLoader.getString("statusbar", "message",
				"sync_messages"));

		// Compute the number of new messages
		if (localUids.size() > 0) {
			// Find the index of the largest local Uid
			int position = localUids.size() - 1;
			while (largestLocalUidIndex == -1
					&& position >= localUids.size() - 10 && position >= 0) {
				largestLocalUidIndex = getServer().getIndex(
						(Integer) localUids.get(position--), this);
			}

			// Still not found -> do a binary search
			if (largestLocalUidIndex == -1) {
				int a, b, c;
				int index;
				a = 0;
				b = position;
				while (b > a && b - a > 1) {
					c = Math.round((b - a) * 0.5f) + a;

					index = getServer().getIndex((Integer) localUids.get(c),
							this);
					if (index == -1) {
						b = c;
					} else {
						a = c;
						largestLocalUidIndex = index;
					}
				}

				// removedLocalUids = localUids.size() - 1 - position;
			} else {
				// -2 because of the decrement in line 317
				// removedLocalUids = localUids.size() - 2 - position;
			}

			// Check if all local uids have been deleted
			if (largestLocalUidIndex == -1) {
				// all messages are new
				largestLocalUidIndex = 0;
			}

		} else {
			// all messages are new
			largestLocalUidIndex = 0;
		}
		return largestLocalUidIndex;
	}

	private void findRemovedMessages(MailboxStatus status, List localUids)
			throws Exception, IOException, IMAPException,
			CommandCancelledException {
		int largestRemoteUid = getServer().getLargestRemoteUid(this);
		
		int deletedMessages = localUids.size() - status.getMessages();

		LOG.fine("Found " + deletedMessages + " deleted Messages");

		// Find the messages that have been deleted
		if (deletedMessages > 0) {
			int found = 0;
			// First deleted all local uids that
			// are larger than the largest remote uid
			while (localUids.size() > 0
					&& found != deletedMessages
					&& ((Integer) localUids.get(localUids.size() - 1))
							.intValue() > largestRemoteUid) {
				Flags flags = getHeaderList().remove(
						localUids.get(localUids.size() - 1)).getFlags();
				fireMessageRemoved(localUids.remove(localUids.size() - 1),
						flags);

				found++;
			}

			// Search in packs beginning from newest to oldest
			// -> in most cases this should save us a lot of uid fetchings to
			// find the deleted messages

			// Pack size is min 10, max 200 else mailboxsize / 10
			int packSize = Math.min(Math.max(deletedMessages, status
					.getMessages() / 10), 200);

			int upper = status.getMessages();

			int localPointer = localUids.size() - 1;

			// Fetch Pack outer loop
			while (upper >= 1 && found != deletedMessages) {
				SequenceSet set = new SequenceSet();
				set.add(Math.max(upper - packSize + 1, 1), upper);

				// Fetch these uids and compare them to the
				// local list
				Integer[] actUids = getServer().fetchUids(set, this);

				// Compare inner loop
				for (int i = actUids.length - 1; i >= 0
						&& found != deletedMessages; i--) {

					// Find missing uids loop
					while (found != deletedMessages && localPointer >= 0
							&& !localUids.get(localPointer).equals(actUids[i])) {
						// We found the uid of a deleted message
						// -> remove it from the headerlist
						getHeaderList().remove(localUids.get(localPointer));

						found++;
						localPointer--;
					}

					// next position in the local uid list
					localPointer--;
				}

				upper = upper - packSize;
			}

			// All the other local mails are deleted
			while (found < deletedMessages && localPointer >= 0) {
				getHeaderList().remove(localUids.get(localPointer--));
				found++;
			}

			if (found != deletedMessages) {
				LOG.severe("Assertion failed : found only " + found + " of "
						+ deletedMessages);
			}

		}
	}

	private void purgeHeaderList() throws Exception {
		ICloseableIterator it = getHeaderList().keyIterator();
		while (it.hasNext()) {
			Object uid = it.next();
			fireMessageRemoved(uid, getHeaderList().get(uid).getFlags());
		}
		it.close();
		getHeaderList().clear();

		getMessageFolderInfo().reset();
		fireFolderPropertyChanged();
	}

	List fetchNewMessages(int startIndex) throws IOException, IMAPException,
			CommandCancelledException, Exception {
		IMAPFlags[] newFlags = getServer().fetchFlagsListStartFrom2(
				startIndex + 1, this);

		List newUids = new ArrayList(newFlags.length);

		// Build a list of the new uids
		for (int i = 0; i < newFlags.length; i++) {
			// Update the list of new and local uids
			newUids.add(newFlags[i].getUid());
		}

		// Fetch the headers of the new messages ...
		getServer().fetchHeaderList(getHeaderList(), newUids, this);

		// .. and set the flags
		setFlags(newFlags);

		// fire message added updates
		for (int i = 0; i < newFlags.length; i++) {
			fireMessageAdded(newFlags[i].getUid(), newFlags[i]);
		}

		return newUids;
	}

	private void syncMailboxInfo(MailboxStatus status) {
		boolean updated = false;
		if (status.getMessages() != -1
				&& messageFolderInfo.getExists() != status.getMessages()) {
			messageFolderInfo.setExists(status.getMessages());
			updated = true;
		}

		if (status.getMessages() == 0) {
			messageFolderInfo.setRecent(0);
			messageFolderInfo.setUnseen(0);
			updated = true;
		} else {

			if (status.getUnseen() != -1
					&& messageFolderInfo.getUnseen() != status.getUnseen()) {
				messageFolderInfo.setUnseen(status.getUnseen());
				updated = true;
			}
		}

		// Sanity tests
		if( messageFolderInfo.getRecent() < 0 ) {
			messageFolderInfo.setRecent(0);
			updated = true;
		}
		if( messageFolderInfo.getRecent() > messageFolderInfo.getExists() ) {
			messageFolderInfo.setRecent(messageFolderInfo.getExists());
			updated = true;
		}
		
		if( messageFolderInfo.getUnseen() < 0 ) {
			messageFolderInfo.setUnseen(0);
			updated = true;
		}
		if( messageFolderInfo.getUnseen() > messageFolderInfo.getExists() ) {
			messageFolderInfo.setUnseen(messageFolderInfo.getExists());
			updated = true;
		}

		if (updated) {
			fireFolderPropertyChanged();
		}

	}

	private void synchronizeFlags(MailboxStatus status) throws Exception,
			IOException, CommandCancelledException, IMAPException {
		printStatusMessage(MailResourceLoader.getString("statusbar", "message",
				"sync_flags"));

		MailboxStatus flagStatus = new MailboxStatus();
		flagStatus.setMessages(status.getMessages());

		// Build the remote lists of messages that are UNSEEN, FLAGGED, DELETED,
		// JUNK
		SearchKey unseenKey = new SearchKey(SearchKey.UNSEEN);
		List remoteUnseenUids = Arrays.asList(getServer().search(unseenKey,
				this));
		flagStatus.setUnseen(remoteUnseenUids.size());

		SearchKey flaggedKey = new SearchKey(SearchKey.FLAGGED);
		List remoteFlaggedUids = Arrays.asList(getServer().search(flaggedKey,
				this));

		SearchKey deletedKey = new SearchKey(SearchKey.DELETED);
		List remoteDeletedUids = Arrays.asList(getServer().search(deletedKey,
				this));

		SearchKey recentKey = new SearchKey(SearchKey.RECENT);
		List remoteRecentUids = Arrays.asList(getServer().search(recentKey,
				this));
		flagStatus.setRecent(remoteRecentUids.size());

		SearchKey junkKey = new SearchKey(SearchKey.KEYWORD, "JUNK");
		List remoteJunkUids = Arrays.asList(getServer().search(junkKey, this));

		// update the local flags and ensure that the MailboxInfo is correct
		ICloseableIterator headerIterator = getHeaderList().headerIterator();

		int unseen = 0;
		int flagged = 0;
		int recent = 0;
		int deleted = 0;
		int junk = 0;

		while (headerIterator.hasNext()) {
			IColumbaHeader header = (IColumbaHeader) headerIterator.next();
			Object uid = header.get("columba.uid");

			Flags flag = header.getFlags();
			Flags oldFlag = (Flags) flag.clone();

			int index;

			index = Collections.binarySearch(remoteUnseenUids, uid);
			flag.setSeen(index < 0);
			if (!flag.getSeen()) {
				unseen++;
			}

			index = Collections.binarySearch(remoteDeletedUids, uid);
			flag.setDeleted(index >= 0);
			if (flag.getDeleted()) {
				deleted++;
			}

			index = Collections.binarySearch(remoteFlaggedUids, uid);
			flag.setFlagged(index >= 0);
			if (flag.getFlagged()) {
				flagged++;
			}

			index = Collections.binarySearch(remoteRecentUids, uid);
			flag.setRecent(index >= 0);
			if (flag.getRecent()) {
				recent++;
			}

			index = Collections.binarySearch(remoteJunkUids, uid);
			header.getAttributes().put("columba.spam", new Boolean(index >= 0));
			if (index >= 0) {
				junk++;
			}

			if (!flag.equals(oldFlag)) {
				getHeaderList().update(uid, header);

				fireMessageFlagChanged(uid, oldFlag, 0);
			}
		}
		headerIterator.close();

		
		if (remoteJunkUids.size() != junk || remoteRecentUids.size() != recent
 remoteFlaggedUids.size() != flagged
 remoteDeletedUids.size() != deleted
 remoteUnseenUids.size() != unseen) {
			// Something is wrong
			// Sync again
			
			synchronizeHeaderlist();
			return;
		}
		
		syncMailboxInfo(flagStatus);
	}

	/**
	 * Method updateFlags.
	 * 
	 * @param flagsList
	 */
	protected void setFlags(Flags[] flagsList) throws Exception {
		for (int i = 0; i < flagsList.length; i++) {
			IMAPFlags flags = (IMAPFlags) flagsList[i];

			Integer uid = (Integer) flags.getUid();

			IColumbaHeader header = getHeaderList().get(uid);

			Flags localFlags = header.getFlags();

			localFlags.setFlags(flags.getFlags());

			// Junk flag
			header.getAttributes().put("columba.spam",
					Boolean.valueOf(flags.get(IMAPFlags.JUNK)));

			getHeaderList().update(uid, header);
		}
	}

	/**
	 * @see org.columba.mail.folder.Folder#getMimeTree(java.lang.Object,
	 *      IMAPFolder)
	 */
	public MimeTree getMimePartTree(Object uid) throws Exception {
		MimeTree tree = IMAPCache.getInstance().getMimeTree(this, uid);
		if ( tree == null ) {
			tree = getServer().getMimeTree(uid, this);
			IMAPCache.getInstance().addMimeTree(this, uid, tree);
		} 
		
		return tree;
	}

	/**
	 * Copies a set of messages from this folder to a destination folder.
	 * <p>
	 * The IMAP copy command also keeps the flags intact. So, there's no need to
	 * change these manually.
	 * 
	 * @see org.columba.mail.folder.Folder#innerCopy(org.columba.mail.folder.IMailbox,
	 *      java.lang.Object, org.columba.api.command.IWorkerStatusController)
	 */
	public void innerCopy(IMailbox destiny, Object[] uids) throws Exception {
		IMAPFolder destFolder = (IMAPFolder) destiny;
		IHeaderList srcHeaderList = getHeaderList();
		IPersistantHeaderList destHeaderList = (IPersistantHeaderList) destFolder
				.getHeaderList();

		Object[] destUids = getServer().copy(destFolder, uids, this);

		if (destUids.length < uids.length) {
			LOG
					.warning("Some messages could not be copied because they do not exist anymore!");			
		}

		// Check if maybe no message at all got copied
		// In this case we are finished here
		if( destUids.length == 0) return;
		
		// update headerlist of destination-folder
		// -> this is necessary to reflect the changes visually
		// but only do it if the target folder is still in sync!

		Integer largestDestUid = new Integer(-1);
		ICloseableIterator it = destHeaderList.keyIterator();
		while (it.hasNext()) {
			Integer uid = (Integer) it.next();
			if (largestDestUid.compareTo(uid) < 0) {
				largestDestUid = uid;
			}
		}
		it.close();

		if (((Integer) destUids[0]).intValue() == largestDestUid.intValue() + 1) {
			int j = 0;
			for (int i = 0; i < uids.length; i++) {
				IColumbaHeader destHeader = srcHeaderList.get(
						uids[i]);
				// Was this message actually copied?
				if(destHeader != null) {
					// Copy the header
					destHeader = (IColumbaHeader) destHeader.clone();
				
					destHeader.set("columba.uid", destUids[j]);
					destHeaderList.add(destHeader, destUids[j]);

					// We need IMAPFlags
					IMAPFlags flags = new IMAPFlags(destHeader.getFlags()
							.getFlags());
					flags.setUid(destUids[j]);

					destFolder.fireMessageAdded(flags.getUid(), flags);
					j++;
				}
			}
		}

	}

	/**
	 * @see org.columba.mail.folder.Folder#markMessage(java.lang.Object, int,
	 *      IMAPFolder)
	 */
	public void markMessage(Object[] uids, int variant) throws Exception {
		getServer().markMessage(uids, variant, this);

		super.markMessage(uids, variant);
	}

	/**
	 * @see org.columba.mail.folder.Folder#expungeFolder(java.lang.Object,
	 *      org.columba.api.command.IWorkerStatusController)
	 */
	public void expungeFolder() throws Exception {
		try {
			getServer().expunge(this);
			super.expungeFolder();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * @see org.columba.mail.folder.Folder#getMessageHeader(java.lang.Object,
	 *      org.columba.api.command.IWorkerStatusController)
	 * @TODO dont use deprecated method
	 */
	public IColumbaHeader getMessageHeader(Object uid) throws Exception {
		return getHeaderList().get(uid);
	}

	/**
	 * Method getImapPath.
	 * 
	 * @return String
	 */
	public String getImapPath() throws IOException, IMAPException,
			CommandCancelledException {
		StringBuffer path = new StringBuffer();
		path.append(getName());

		IMailFolder child = this;

		while (true) {
			child = (IMailFolder) child.getParent();

			if (child instanceof IMAPRootFolder) {
				break;
			}

			String n = ((IMAPFolder) child).getName();

			path.insert(0, n + getServer().getDelimiter());
		}

		return path.toString();
	}

	/**
	 * @see org.columba.mail.folder.FolderTreeNode#getDefaultProperties()
	 */
	public static XmlElement getDefaultProperties() {
		XmlElement props = new XmlElement("property");

		props.addAttribute("accessrights", "user");
		props.addAttribute("subfolder", "true");

		return props;
	}

	/**
	 * @see org.columba.mail.folder.FolderTreeNode#tryToGetLock(java.lang.Object)
	 */
	public boolean tryToGetLock(Object locker) {
		// IMAP Folders have no own lock ,but share the lock from the Root
		// to ensure that only one operation can be processed simultanous
		IMailFolder root = getRootFolder();
		if (root == null)
			throw new IllegalArgumentException("IMAPRoot is null");

		return root.tryToGetLock(locker);
	}

	/**
	 * @see org.columba.mail.folder.FolderTreeNode#releaseLock()
	 */
	public void releaseLock(Object locker) {
		IMailFolder root = getRootFolder();
		if (root == null)
			throw new IllegalArgumentException("IMAPRoot is null");

		root.releaseLock(locker);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.FolderTreeNode#addSubfolder(org.columba.mail.folder.FolderTreeNode)
	 */
	public void addSubfolder(IMailFolder child) throws Exception {
		if (child instanceof IMAPFolder) {

			getServer().createMailbox(child.getName(), this);
		}

		super.addSubfolder(child);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.Folder#getObservable()
	 */
	public IStatusObservable getObservable() {
		if( observable == null ){
			observable = ((IMAPRootFolder) getRootFolder()).getObservable();
		}
		
		return observable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.IMailbox#addMessage(java.io.InputStream)
	 */
	public Object addMessage(InputStream in, Attributes attributes, Flags flags)
			throws Exception {
		PassiveHeaderParserInputStream withHeaderInputStream = new PassiveHeaderParserInputStream(
				in);

		IMAPFlags imapFlags = new IMAPFlags(flags.getFlags());

		Integer uid = getServer()
				.append(withHeaderInputStream, imapFlags, this);

		// Since JUNK is a non-system Flag we have to set it with
		// an addtitional STORE command
		if (((Boolean) attributes.get("columba.spam")).booleanValue()) {
			imapFlags.set(IMAPFlags.JUNK, true);

			getServer().setFlags(new Object[] { uid }, imapFlags, this);
		}

		// Parser the header
		Header header = withHeaderInputStream.getHeader();

		// update the HeaderList
		IColumbaHeader cHeader = new ColumbaHeader(header,
				(Attributes) attributes.clone(), imapFlags);

		getHeaderList().add(cHeader, uid);

		fireMessageAdded(uid, cHeader.getFlags());

		return uid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.IMailbox#getHeaderFields(java.lang.Object,
	 *      java.lang.String[])
	 */
	public Header getHeaderFields(Object uid, String[] keys) throws Exception {
		// get header with UID
		IColumbaHeader header = getHeaderList().get(uid);

		if (header == null)
			return new Header();

		// cached headerfield list
		List cachedList = Arrays.asList(CachedHeaderfields
				.getDefaultHeaderfields());

		List keyList = new ArrayList(Arrays.asList(keys));
		ListTools.substract(keyList, cachedList);

		if (keyList.size() > 0) {
			return getServer().getHeaders(uid, keys, this);
		} else {
			return (Header) header.getHeader().clone();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.IMailbox#getMessageSourceStream(java.lang.Object)
	 */
	public InputStream getMessageSourceStream(Object uid) throws Exception {
		return getServer().getMessageSourceStream(uid, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.IMailbox#getMimePartSourceStream(java.lang.Object,
	 *      java.lang.Integer[])
	 */
	public InputStream getMimePartSourceStream(Object uid, Integer[] address)
			throws Exception {
		return getServer().getMimePartSourceStream(uid, address, this);
	}

	/**
	 * 
	 * 
	 * @see org.columba.mail.folder.IMailbox#getMimePartBodyStream(java.lang.Object,
	 *      java.lang.Integer[])
	 */
	public InputStream getMimePartBodyStream(Object uid, Integer[] address)
			throws Exception {
		InputStream result = IMAPCache.getInstance().getMimeBody(this, uid,
				address);
		if (result == null) {
			LOG.fine("Cache miss - fetching from server");
			result = IMAPCache.getInstance().addMimeBody(this, uid, address,
					getServer().getMimePartBodyStream(uid, address, this));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.Folder#isInboxFolder()
	 */
	public boolean isInboxFolder() {
		RootFolder root = (RootFolder) getRootFolder();

		if (root != null) {
			return root.getInboxFolder() == this;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.Folder#isTrashFolder()
	 */
	public boolean isTrashFolder() {
		RootFolder root = (RootFolder) getRootFolder();

		if (root != null) {
			return root.getTrashFolder() == this;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.IMailbox#addMessage(java.io.InputStream,
	 *      org.columba.ristretto.message.Attributes)
	 */
	public Object addMessage(InputStream in) throws Exception {
		PassiveHeaderParserInputStream withHeaderInputStream = new PassiveHeaderParserInputStream(
				in);

		Integer uid = getServer().append(withHeaderInputStream, this);

		if (getServer().isSelected(this)) {
			// update the HeaderList
			Header header = withHeaderInputStream.getHeader();
			ColumbaHeader h = new ColumbaHeader(header);
			getHeaderList().add(h, uid);

			fireMessageAdded(uid, h.getFlags());
		} else {
			if( mailboxSyncEnabled ) {			
			// Trigger Synchronization
			CommandProcessor.getInstance().addOp(
					new CheckForNewMessagesCommand(
							new MailFolderCommandReference(this)));
			}
		}

		return uid;
	}

	/**
	 * @see org.columba.mail.folder.Folder#getHeaderListStorage()
	 */
	/*
	 * public IHeaderListStorage getHeaderListStorage() { if (attributeStorage ==
	 * null) attributeStorage = new RemoteHeaderListStorage(this);
	 * 
	 * return attributeStorage; }
	 */

	/**
	 * @see org.columba.mail.folder.Folder#getSearchEngineInstance()
	 */
	public DefaultSearchEngine getSearchEngine() {
		if (searchEngine == null) {
			searchEngine = new DefaultSearchEngine(this);
			searchEngine.setNonDefaultEngine(new IMAPQueryEngine(this));
		}

		return searchEngine;
	}

	/**
	 * @see org.columba.mail.folder.IMailbox#getAllHeaderFields(java.lang.Object)
	 */
	public Header getAllHeaderFields(Object uid) throws Exception {
		return getServer().getAllHeaders(uid, this);

	}

	/**
	 * @see org.columba.mail.folder.AbstractFolder#supportsAddFolder()
	 */
	public boolean supportsAddFolder(String folder) {
		return true;
	}

	/**
	 * This is called from the UpdateFlagCommand which gets triggered from an
	 * unexpected flags updated.
	 * 
	 * @param flag
	 * @throws IOException
	 * @throws CommandCancelledException
	 * @throws IMAPException
	 */
	public void updateFlag(IMAPFlags flag) throws Exception,
			CommandCancelledException {
		if (getServer().isSelected(this)) {
			Integer uid = new Integer(getServer().fetchUid(
					new SequenceSet(flag.getIndex()), this));
			flag.setUid(uid);
			setFlags(new Flags[] { flag });
		}
	}

	/**
	 * @param readOnly
	 *            The readOnly to set.
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.AbstractMessageFolder#save()
	 */
	public void save() throws Exception {
		super.save();

		headerList.persist();
	}

	void fetchDone() throws IOException, CommandCancelledException,
			IMAPException, Exception {
		MailboxStatus status = getServer().getStatus(this);

		List localUids = new LinkedList(Arrays
				.asList(getHeaderList().getUids()));
		// Sort the uid list
		Collections.sort(localUids);

		// Number of deleted messages is computed from exists on imap and local
		// newMessages
		findRemovedMessages(status, localUids);

		if (firstSync) {
			firstSync = false;
			synchronizeFlags(status);
		} else {
			syncMailboxInfo(status);
		}

		// Apply filter if enabled
		ImapItem item = getServer().getItem();

		boolean applyFilter = item.getBooleanWithDefault(
				"automatically_apply_filter", false);

		// if "automatically apply filter" is selected & there
		// are
		// new
		// messages
		if (applyFilter) {
			CommandProcessor.getInstance()
					.addOp(
							new ApplyFilterCommand(
									new MailFolderCommandReference(this)));
		}

		// Reenable Updating the mailbox
		mailboxSyncEnabled = true;
	}

	/**
	 * @return Returns the mailboxSyncEnabled.
	 */
	public boolean isMailboxSyncEnabled() {
		return mailboxSyncEnabled;
	}

	/**
	 * @param mailboxSyncEnabled The mailboxSyncEnabled to set.
	 */
	public void setMailboxSyncEnabled(boolean mailboxSyncEnabled) {
		this.mailboxSyncEnabled = mailboxSyncEnabled;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9385.java