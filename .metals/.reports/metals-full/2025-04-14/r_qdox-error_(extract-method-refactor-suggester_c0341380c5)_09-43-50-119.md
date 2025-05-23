error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6083.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6083.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6083.java
text:
```scala
I@@ColumbaHeader cHeader = new ColumbaHeader(header, (Attributes)attributes.clone(), imapFlags);

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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.columba.api.command.IStatusObservable;
import org.columba.core.base.ListTools;
import org.columba.core.command.CommandCancelledException;
import org.columba.core.command.CommandProcessor;
import org.columba.core.filter.Filter;
import org.columba.core.xml.XmlElement;
import org.columba.mail.command.MailFolderCommandReference;
import org.columba.mail.config.AccountItem;
import org.columba.mail.config.FolderItem;
import org.columba.mail.config.IFolderItem;
import org.columba.mail.config.ImapItem;
import org.columba.mail.folder.AbstractRemoteFolder;
import org.columba.mail.folder.IMailFolder;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.folder.RootFolder;
import org.columba.mail.folder.command.ApplyFilterCommand;
import org.columba.mail.folder.event.FolderEvent;
import org.columba.mail.folder.event.IFolderListener;
import org.columba.mail.folder.headercache.CachedHeaderfields;
import org.columba.mail.folder.headercache.PersistantHeaderList;
import org.columba.mail.folder.headercache.RemoteHeaderCache;
import org.columba.mail.folder.search.DefaultSearchEngine;
import org.columba.mail.folder.search.IMAPQueryEngine;
import org.columba.mail.imap.IMAPServer;
import org.columba.mail.message.ColumbaHeader;
import org.columba.mail.message.IColumbaHeader;
import org.columba.mail.message.IHeaderList;
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
	private IMAPServer store;

	/**
	 * 
	 */
	protected boolean existsOnServer = true;

	private boolean readOnly;

	private PersistantHeaderList headerList;

	private boolean lazyFlagSync = false;
	
	/**
	 * @see org.columba.mail.folder.IMailbox#isReadOnly()
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * @see org.columba.mail.folder.FolderTreeNode#FolderTreeNode(org.columba.mail.config.FolderItem)
	 */
	public IMAPFolder(FolderItem folderItem, String path) {
		super(folderItem, path);

		DefaultSearchEngine engine = new DefaultSearchEngine(this);
		engine.setNonDefaultEngine(new IMAPQueryEngine(this));
		setSearchEngine(engine);

		// setChanged(true);
		
		headerList = new PersistantHeaderList(new RemoteHeaderCache(this));
	}

	/**
	 * @param type
	 */
	public IMAPFolder(String name, String type, String path) throws Exception {
		super(name, type, path);

		IFolderItem item = getConfiguration();
		item.setString("property", "accessrights", "user");
		item.setString("property", "subfolder", "true");

		headerList = new PersistantHeaderList(new RemoteHeaderCache(this));
	
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
	public IMAPServer getServer() {
		if (store == null) {
			store = ((IMAPRootFolder) getRootFolder()).getServer();
		}

		return store;
	}

	/**
	 * @see org.columba.mail.folder.Folder#getHeaderList(org.columba.api.command.IWorkerStatusController)
	 */
	public IHeaderList getHeaderList() throws Exception{
		ensureFolderIsSynced(true);

		return headerList;
	}

	/**
	 * @param lazy TODO
	 * @throws IOException
	 * @throws IMAPException
	 * @throws CommandCancelledException
	 * @throws Exception
	 */
	public synchronized void ensureFolderIsSynced(boolean lazy) throws IOException,
			IMAPException, CommandCancelledException, Exception {
		if( !headerList.isRestored() ) {
			try {
				headerList.restore();
			} catch (IOException e) {
				// Will be fixed in the upcoming synchronization process
			}
			synchronizeHeaderlist();

			// Only do a flag sync if this folder is selected
			// -> this prevents an explicit select to this folder
			// when only check for new messages
			if( getServer().isSelected(this) ) {
				synchronizeFlags();
			} else {
				// but we have to ensure that it happens later
				lazyFlagSync = true;				
			}
		} else if (!lazy || !getServer().isSelected(this) ) {
			synchronizeHeaderlist();

			if( lazyFlagSync && getServer().isSelected(this) ) {
				// One flag sync should be enough for one session
				synchronizeFlags();
				lazyFlagSync = false;
			}
		}
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
	private void synchronizeHeaderlist() throws Exception, IOException,
			CommandCancelledException, IMAPException {
		// Check if the mailbox has changed
		MailboxStatus status = getServer().getStatus(this);

		if( status.getMessages() == 0 ) {
			headerList.clear();
			syncMailboxInfo(status);
			
			return;
		}
		
		List localUids = new LinkedList(Arrays.asList(headerList.getUids()));
		// Sort the uid list
		Collections.sort(localUids);

		int newMessages = 0;
		
		int largestLocalUid =  localUids.size() > 0 ? ((Integer)localUids.get(localUids.size()-1)).intValue() : -1;
		int largestLocalUidIndex = -1;

		int largestRemoteUid = (int)status.getUidNext() - 1;

		if( localUids.size() == status.getMessages() && largestRemoteUid == largestLocalUid) {
			// Seems to be no change!
			syncMailboxInfo(status);
			return;
		}
		
		printStatusMessage(MailResourceLoader.getString("statusbar", "message",
				"sync_messages"));

		if (status.getMessages() > 0) {
			largestRemoteUid = getServer().fetchUid(
				new SequenceSet(SequenceEntry.STAR), this);
			if (largestRemoteUid == -1) {
				largestRemoteUid = getServer().fetchUid(
						new SequenceSet(status.getMessages()), this);
			}

			printStatusMessage(MailResourceLoader.getString("statusbar",
					"message", "sync_messages"));

			// Compute the number of new messages
			if (localUids.size() > 0) {
				// Number of new messages = largestRemoteUid - largestLocalUid
				largestLocalUid = ((Integer) localUids
						.get(localUids.size() - 1)).intValue();

				// Find the index of the largest local Uid
				int position = localUids.size() - 1;
				while (largestLocalUidIndex == -1
						&& position >= localUids.size() - 10 && position > 0) {
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

						index = getServer().getIndex(
								(Integer) localUids.get(c), this);
						if (index == -1) {
							b = c;
						} else {
							a = c;
							largestLocalUidIndex = index;
						}
					}
				}

				// Check if all local uids have been deleted
				if (largestLocalUidIndex == -1) {
					newMessages = status.getMessages();

					// Set the index of the largest Uid to 0
					// -> ensure it works with the fetch of new
					// messages part below
					largestLocalUidIndex = 0;
				} else {
					newMessages = status.getMessages() - largestLocalUidIndex;
				}

			} else {
				// all messages are new
				newMessages = status.getMessages();

				// Set the index of the largest Uid to 0
				// -> ensure it works with the fetch of new
				// messages part below
				largestLocalUidIndex = 0;
			}

			// Somehow there are new messages that
			// have a lower index -> out of sync
			if (localUids.size() - status.getMessages() + newMessages < 0) {

				LOG.severe("Folder " + getName()
						+ " is out of sync -> recreating the cache!");
				headerList.clear();

				// all messages are new
				newMessages = status.getMessages();

				// Set the index of the largest Uid to 0
				// -> ensure it works with the fetch of new
				// messages part below
				largestLocalUidIndex = 0;
				largestLocalUid = -1;

				localUids.clear();
			}

			LOG.fine("Found " + newMessages + " new Messages");

			// If we have new messages add them to the headerlist
			if (newMessages > 0) {
				printStatusMessage(MailResourceLoader.getString("statusbar",
						"message", "fetch_new_headers"));

				IMAPFlags[] newFlags = getServer().fetchFlagsListStartFrom(
						largestLocalUidIndex + 1, this);

				if (newFlags.length > 0) {
					List newUids = new ArrayList(newFlags.length);

					// Build a list of the new uids
					for (int i = 0; i < newFlags.length; i++) {
						// Check if the uids match as expected
						if (((Integer) newFlags[i].getUid()).intValue() <= largestLocalUid) {
							LOG.severe("Assertion Failed : New UID is smaller");
						}

						// Update the list of new and local uids
						newUids.add(newFlags[i].getUid());
						localUids.add(newFlags[i].getUid());
					}

					// Fetch the headers of the new messages ...
					getServer().fetchHeaderList(headerList, newUids, this);

					// .. and set the flags
					setFlags(newFlags);

					// fire message added updates
					for (int i = 0; i < newFlags.length; i++) {
						fireMessageAdded(newFlags[i]);
					}

					// Apply filter on new messages if enabled
					IMAPRootFolder rootFolder = (IMAPRootFolder) getRootFolder();
					AccountItem accountItem = rootFolder.getAccountItem();
					ImapItem item = accountItem.getImapItem();

					boolean applyFilter = item.getBooleanWithDefault(
							"automatically_apply_filter", false);

					// if "automatically apply filter" is selected & there are
					// new
					// messages
					if (applyFilter) {
						CommandProcessor.getInstance().addOp(
								new ApplyFilterCommand(
										new MailFolderCommandReference(this,
												newUids.toArray())));
					}
				}
			}
		}

		// Number of deleted messages is computed from exists on imap and local
		// newMessages
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
				Flags flags = headerList.remove(localUids
						.get(localUids.size() - 1)).getFlags();
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
						headerList.remove(localUids.get(localPointer));
						
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
				headerList.remove(localUids.get(localPointer--));
				found++;
			}

			if (found != deletedMessages) {
				LOG.severe("Assertion failed : found only " + found + " of "
						+ deletedMessages);
			}

		}

		// inform listeners if MessageFolderInfo has changed
		syncMailboxInfo(status);
	}

	private void syncMailboxInfo(MailboxStatus status) {
		boolean updated  = false;
		if( status.getMessages() != -1 && messageFolderInfo.getExists() != status.getMessages() ) {
			messageFolderInfo.setExists(status.getMessages());
			updated = true;
		}
		
		if( status.getRecent() != -1 && messageFolderInfo.getRecent() != status.getRecent()) {
			messageFolderInfo.setRecent(status.getRecent());
			updated = true;
		}
		
		if( status.getUnseen() != -1 && messageFolderInfo.getUnseen() != status.getUnseen() ){
			messageFolderInfo.setUnseen(status.getUnseen());
			updated = true;
		}
		
		if( updated ) {
			fireFolderPropertyChanged();
		}
			
	}

	/**
	 * Propagates an event to all registered listeners notifying them of a
	 * message addition.
	 */
	private void fireMessageAdded(IMAPFlags flags) {
		getMessageFolderInfo().incExists();
		try {
			if (flags.getRecent()) {
				getMessageFolderInfo().incRecent();
			}
			if (!flags.getSeen()) {
				getMessageFolderInfo().incUnseen();
			}
		} catch (Exception e) {
		}
		setChanged(true);

		// update treenode
		fireFolderPropertyChanged();

		FolderEvent e = new FolderEvent(this, flags.getUid());
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) { 
			if (listeners[i] == IFolderListener.class) {
				((IFolderListener) listeners[i + 1]).messageAdded(e);
			}
		}
	}

	private void synchronizeFlags() throws Exception, IOException,
			CommandCancelledException, IMAPException {
		printStatusMessage(MailResourceLoader.getString("statusbar", "message",
				"sync_flags"));

		// Build the remote lists of messages that are UNSEEN, FLAGGED, DELETED,
		// JUNK
		SearchKey unseenKey = new SearchKey(SearchKey.UNSEEN);
		List remoteUnseenUids = Arrays.asList(getServer().search(unseenKey,
				this));

		SearchKey flaggedKey = new SearchKey(SearchKey.FLAGGED);
		List remoteFlaggedUids = Arrays.asList(getServer().search(flaggedKey,
				this));

		SearchKey deletedKey = new SearchKey(SearchKey.DELETED);
		List remoteDeletedUids = Arrays.asList(getServer().search(deletedKey,
				this));

		SearchKey junkKey = new SearchKey(SearchKey.KEYWORD, "JUNK");
		List remoteJunkUids = Arrays.asList(getServer().search(junkKey, this));

		// update the local flags and ensure that the MailboxInfo is correct
		Enumeration uids = headerList.keys();
		
		int unseen = 0;
		while (uids.hasMoreElements()) {
			Object uid = uids.nextElement();
			IColumbaHeader header = headerList.get(uid);
			Flags flag = header.getFlags();
			Flags oldFlag = (Flags) flag.clone();

			flag.setSeen(Collections.binarySearch(remoteUnseenUids, uid) < 0);
			if (!flag.getSeen()) {
				unseen++;
			}

			flag
					.setDeleted(Collections
							.binarySearch(remoteDeletedUids, uid) >= 0);

			flag
					.setFlagged(Collections
							.binarySearch(remoteFlaggedUids, uid) >= 0);

			header.getAttributes()
					.put(
							"columba.spam",
							new Boolean(Collections.binarySearch(
									remoteJunkUids, uid) >= 0));

			fireMessageFlagChanged(uid, oldFlag, 0);
		}

		// Ensure that the messageFolderInfo is up to date.
		if (messageFolderInfo.getExists() != unseen) {
			messageFolderInfo.setUnseen(unseen);

			fireFolderPropertyChanged();
		}

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

			IColumbaHeader header = headerList.get(uid);

			Flags localFlags = header.getFlags();

			localFlags.setFlags(flags.getFlags());

			// Junk flag
			header.getAttributes().put("columba.spam",
					Boolean.valueOf(flags.get(IMAPFlags.JUNK)));
		}
	}

	/**
	 * Method existsRemotely.
	 * 
	 * @param uid
	 * @param startIndex
	 * @param uidList
	 * @return boolean
	 * @throws Exception
	 */
	protected boolean existsRemotely(String uid, int startIndex, List uidList)
			throws Exception {
		for (Iterator it = uidList.iterator(); it.hasNext();) {
			String serverUID = (String) it.next();

			// for (int i = startIndex; i < uidList.size(); i++) {
			// String serverUID = (String) uidList.get(i);
			// System.out.println("server message uid: "+ serverUID );
			if (uid.equals(serverUID)) {
				// System.out.println("local uid exists remotely");
				return true;
			}
		}

		return false;
	}

	/**
	 * Method existsLocally.
	 * 
	 * @param uid
	 * @param list
	 * @return boolean
	 * @throws Exception
	 */
	protected boolean existsLocally(String uid, PersistantHeaderList list)
			throws Exception {
		for (Enumeration e = getHeaderList().keys(); e.hasMoreElements();) {
			String localUID = (String) e.nextElement();

			if (uid.equals(localUID)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @see org.columba.mail.folder.Folder#getMimeTree(java.lang.Object,
	 *      IMAPFolder)
	 */
	public MimeTree getMimePartTree(Object uid) throws Exception {
		return getServer().getMimeTree(uid, this);
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
		IHeaderList destHeaderList = destFolder.getHeaderList();
		
		Object[] destUids = getServer().copy(destFolder, uids, this);

		if (destUids.length != uids.length) {
			LOG
					.warning("Some messages could not be copied because they do not exist anymore!");
		}
		
		// update headerlist of destination-folder
		// -> this is necessary to reflect the changes visually
		int j = 0;
		for (int i = 0; i < uids.length; i++) {
			IColumbaHeader destHeader = new ColumbaHeader(srcHeaderList.get(uids[i]));
			// Was this message actually copied?

			destHeader.set("columba.uid", destUids[j]);
			destHeaderList.add(destHeader, destUids[j]);
				
			// We need IMAPFlags
			IMAPFlags flags = new IMAPFlags(destHeader.getFlags().getFlags());
			flags.setUid( destUids[j]);
			
			destFolder.fireMessageAdded( flags );
			j++;
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
		return ((IMAPRootFolder) getRootFolder()).getObservable();
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
		IColumbaHeader cHeader = new ColumbaHeader(header, attributes, imapFlags);
		header.set("columba.uid", uid);
		
		headerList.add(cHeader, uid);

		fireMessageAdded(uid);		
		
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

		// update the HeaderList
		Header header = withHeaderInputStream.getHeader();
		ColumbaHeader h = new ColumbaHeader(header);
		headerList.add(h,uid );

		fireMessageAdded(uid);
		
		return uid;
	}

	/**
	 * @see org.columba.mail.folder.Folder#getHeaderListStorage()
	 */
	/*
	public IHeaderListStorage getHeaderListStorage() {
		if (attributeStorage == null)
			attributeStorage = new RemoteHeaderListStorage(this);

		return attributeStorage;
	}*/

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
	class RemoteHeaderListStorage extends AbstractHeaderListStorage {

		protected AbstractHeaderCache headerCache;

		private IMAPFolder folder;

		public RemoteHeaderListStorage(IMAPFolder folder) {
			super();
			this.folder = folder;

		}

		public AbstractHeaderCache getHeaderCacheInstance() {
			if (headerCache == null)
				headerCache = new RemoteHeaderCache(folder);

			return headerCache;
		}

	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.AbstractMessageFolder#searchMessages(org.columba.core.filter.Filter,
	 *      java.lang.Object[])
	 */
	public Object[] searchMessages(Filter filter, Object[] uids)
			throws Exception {
		ensureFolderIsSynced(true);
		return super.searchMessages(filter, uids);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.AbstractMessageFolder#searchMessages(org.columba.core.filter.Filter)
	 */
	public Object[] searchMessages(Filter filter) throws Exception {
		ensureFolderIsSynced(true);
		return super.searchMessages(filter);
	}

	/* (non-Javadoc)
	 * @see org.columba.mail.folder.AbstractMessageFolder#save()
	 */
	public void save() throws Exception {
		super.save();
		
		headerList.persist();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6083.java