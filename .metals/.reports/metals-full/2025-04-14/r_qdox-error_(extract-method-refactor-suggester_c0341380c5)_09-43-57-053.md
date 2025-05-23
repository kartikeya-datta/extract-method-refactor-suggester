error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/757.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/757.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/757.java
text:
```scala
private synchronized v@@oid ensureFolderIsSynced() throws IOException, IMAPException, CommandCancelledException, Exception {

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

import org.columba.core.command.CommandCancelledException;
import org.columba.core.command.CommandProcessor;
import org.columba.core.command.StatusObservable;
import org.columba.core.filter.Filter;
import org.columba.core.xml.XmlElement;
import org.columba.mail.command.MailFolderCommandReference;
import org.columba.mail.config.AccountItem;
import org.columba.mail.config.FolderItem;
import org.columba.mail.config.IFolderItem;
import org.columba.mail.config.ImapItem;
import org.columba.mail.folder.AbstractFolder;
import org.columba.mail.folder.AbstractHeaderListStorage;
import org.columba.mail.folder.IHeaderListStorage;
import org.columba.mail.folder.IMailFolder;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.folder.AbstractRemoteFolder;
import org.columba.mail.folder.RootFolder;
import org.columba.mail.folder.command.ApplyFilterCommand;
import org.columba.mail.folder.headercache.AbstractHeaderCache;
import org.columba.mail.folder.headercache.CachedHeaderfields;
import org.columba.mail.folder.headercache.RemoteHeaderCache;
import org.columba.mail.folder.search.DefaultSearchEngine;
import org.columba.mail.folder.search.IMAPQueryEngine;
import org.columba.mail.imap.IMAPServer;
import org.columba.mail.message.ColumbaHeader;
import org.columba.mail.message.ColumbaMessage;
import org.columba.mail.message.HeaderList;
import org.columba.mail.message.IColumbaHeader;
import org.columba.mail.message.IColumbaMessage;
import org.columba.mail.message.IHeaderList;
import org.columba.mail.parser.PassiveHeaderParserInputStream;
import org.columba.mail.util.MailResourceLoader;
import org.columba.ristretto.imap.IMAPException;
import org.columba.ristretto.imap.IMAPFlags;
import org.columba.ristretto.imap.MailboxStatus;
import org.columba.ristretto.imap.SearchKey;
import org.columba.ristretto.imap.SequenceSet;
import org.columba.ristretto.message.Attributes;
import org.columba.ristretto.message.Flags;
import org.columba.ristretto.message.Header;
import org.columba.ristretto.message.MailboxInfo;
import org.columba.ristretto.message.MimePart;
import org.columba.ristretto.message.MimeTree;

public class IMAPFolder extends AbstractRemoteFolder {

	/** JDK 1.4+ logging framework logger, used for logging. */
	private static final Logger LOG = Logger
			.getLogger("org.columba.mail.folder.imap");

	//private boolean select=false;
	//private boolean fetch=false;
	//private StringBuffer cache;
	private Object aktMessageUid;

	/**
	 *  
	 */
	private IColumbaMessage aktMessage;

	/**
	 *  
	 */
	private boolean mailcheck = false;

	/**
	 *  
	 */
	private IMAPServer store;

	/**
	 *  
	 */
	protected IHeaderList headerList;

	/**
	 *  
	 */
	protected boolean existsOnServer = true;

	/**
	 *  
	 */
	private IHeaderListStorage attributeStorage;

	private boolean readOnly;

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

		//setChanged(true);
	}

	/**
	 * @param type
	 */
	public IMAPFolder(String name, String type, String path) throws Exception {
		super(name, type, path);

		IFolderItem item = getConfiguration();
		item.setString("property", "accessrights", "user");
		item.setString("property", "subfolder", "true");
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
	 * @see org.columba.mail.folder.Folder#getHeaderList(org.columba.core.command.WorkerStatusController)
	 */
	public IHeaderList getHeaderList() throws Exception {
		ensureFolderIsSynced();

		return headerList;
	}

	/**
	 * @throws IOException
	 * @throws IMAPException
	 * @throws CommandCancelledException
	 * @throws Exception
	 */
	private void ensureFolderIsSynced() throws IOException, IMAPException, CommandCancelledException, Exception {
		if (headerList == null || !getServer().isSelected(this)) {
			synchronizeHeaderlist();
			synchronizeFlags();
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
	public void synchronizeHeaderlist() throws Exception, IOException,
			CommandCancelledException, IMAPException {
		headerList = super.getHeaderList();

		// Check if the mailbox has changed
		MailboxStatus status = getServer().getStatus(this);

		List localUids = extractUids(headerList);
		// Sort the uid list
		Collections.sort(localUids);

		int newMessages = 0;
		int largestLocalUid = -1;
		int largestLocalUidIndex = -1;

		int largestRemoteUid = -1;

		printStatusMessage(MailResourceLoader.getString("statusbar", "message",
				"sync_messages"));

		if (status.getMessages() > 0) {
			largestRemoteUid = getServer().fetchUids(
					new SequenceSet(status.getMessages()), this)[0].intValue();

			printStatusMessage(MailResourceLoader.getString("statusbar",
					"message", "sync_messages"));

			// Compute the number of new messages
			if (localUids.size() > 0) {
				// Number of new messages = largestRemoteUid - largestLocalUid
				largestLocalUid = ((Integer) localUids
						.get(localUids.size() - 1)).intValue();

				// Find the index of the largest local Uid
				int position = localUids.size() - 1;
				while (largestLocalUidIndex == -1 && position >= localUids.size() - 10) {
					largestLocalUidIndex = getServer().getIndex(
							(Integer) localUids.get(position--), this);
				}

				//Still not found -> do a binary search
				if (largestLocalUidIndex == -1) {
					int a, b, c;
					int index;
					a = 0;
					b = position;
					while (b - a > 1) {
						c = (b - a) >> 1 + a;

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
					//    messages part below
					largestLocalUidIndex = 0;
				} else {
					newMessages = status.getMessages() - largestLocalUidIndex;
				}

			} else {
				// all messages are new
				newMessages = status.getMessages();

				// Set the index of the largest Uid to 0
				// -> ensure it works with the fetch of new
				//    messages part below
				largestLocalUidIndex = 0;
			}

			// Somehow there are new messages that
			// have a lower index -> out of sync
			if (localUids.size() - status.getMessages() + newMessages < 0) {

				LOG.severe("Folder " + getName()
						+ " is out of sync -> recreating the cache!");
				getHeaderListStorage().reset();

				headerList = super.getHeaderList();

				// all messages are new
				newMessages = status.getMessages();

				// Set the index of the largest Uid to 0
				// -> ensure it works with the fetch of new
				//    messages part below
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
					fireMessageAdded(newFlags[i].getUid());
				}

				// Apply filter on new messages if enabled
				IMAPRootFolder rootFolder = (IMAPRootFolder) getRootFolder();
				AccountItem accountItem = rootFolder.getAccountItem();
				ImapItem item = accountItem.getImapItem();

				boolean applyFilter = item.getBooleanWithDefault(
						"automatically_apply_filter", false);

				// if "automatically apply filter" is selected & there are new
				// messages
				if (applyFilter) {
					CommandProcessor.getInstance().addOp(
							new ApplyFilterCommand(
									new MailFolderCommandReference(this,
											newUids.toArray())));
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
				Flags flags = ((ColumbaHeader) headerList.remove(localUids
						.get(localUids.size() - 1))).getFlags();
				fireMessageRemoved(localUids.remove(localUids.size() - 1),
						flags);

				found++;
			}

			// Search in packs beginning from newest to oldest
			// -> in most cases this should save us a lot of uid fetchings to
			//    find the deleted messages

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
		if (deletedMessages > 0 || newMessages > 0) {
			fireFolderPropertyChanged();
		}
	}

	public void synchronizeFlags() throws Exception, IOException,
			CommandCancelledException, IMAPException {
		headerList = super.getHeaderList();

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
		MailboxInfo testInfo = new MailboxInfo();
		while (uids.hasMoreElements()) {
			Object uid = uids.nextElement();
			IColumbaHeader header = headerList.get(uid);
			Flags flag = header.getFlags();
			Flags oldFlag = (Flags)flag.clone();
			
			testInfo.incExists();

			flag.setSeen(Collections.binarySearch(remoteUnseenUids, uid) < 0);
			if (!flag.getSeen()) {
				testInfo.incUnseen();
			}

			flag
					.setDeleted(Collections
							.binarySearch(remoteDeletedUids, uid) >= 0);

			flag
					.setFlagged(Collections
							.binarySearch(remoteFlaggedUids, uid) >= 0);

			header.getAttributes().put("columba.spam", new Boolean(Collections.binarySearch(
					remoteJunkUids, uid) >= 0));

			fireMessageFlagChanged(uid, oldFlag, 0);
		}

		// Ensure that the messageFolderInfo is up to date.
		if (!messageFolderInfo.equals(testInfo)) {
			messageFolderInfo.setExists(testInfo.getExists());
			messageFolderInfo.setUnseen(testInfo.getUnseen());
			messageFolderInfo.setRecent(testInfo.getRecent());

			fireFolderPropertyChanged();
		}

	}

	/**
	 * Method updateFlags.
	 * 
	 * @param flagsList
	 */
	protected void setFlags(Flags[] flagsList) {
		for (int i = 0; i < flagsList.length; i++) {
			IMAPFlags flags = (IMAPFlags) flagsList[i];

			Integer uid = (Integer) flags.getUid();

			ColumbaHeader header = (ColumbaHeader) headerList.get(uid);

			Flags localFlags = header.getFlags();

			localFlags.setFlags(flags.getFlags());

			//Junk flag
			header.getAttributes().put("columba.spam",
					Boolean.valueOf(flags.get(IMAPFlags.JUNK)));
		}
	}

	/**
	 * Method save.
	 */
	public void save() throws Exception {
		//  make sure that messagefolderinfo(total/unread/recent count)
		// is saved in tree.xml file
		saveMessageFolderInfo();

		// only save header-cache if folder data changed
		if (hasChanged()) {
			getHeaderListStorage().save();
			setChanged(false);
		}
	}

	/**
	 * @param headerList
	 * @return
	 */
	private List extractUids(IHeaderList headerList) {
		LinkedList headerUids = new LinkedList();
		Enumeration keys = headerList.keys();

		while (keys.hasMoreElements()) {
			headerUids.add(keys.nextElement());
		}
		return headerUids;
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
			//System.out.println("server message uid: "+ serverUID );
			if (uid.equals(serverUID)) {
				//System.out.println("local uid exists remotely");
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
	protected boolean existsLocally(String uid, HeaderList list)
			throws Exception {
		for (Enumeration e = headerList.keys(); e.hasMoreElements();) {
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
	 * @see org.columba.mail.folder.Folder#getMimePart(java.lang.Object,
	 *      java.lang.Integer, org.columba.core.command.WorkerStatusController)
	 * @TODO dont use deprecated method
	 */
	public MimePart getMimePart(Object uid, Integer[] address) throws Exception {
		return null;
	}

	/**
	 * Copies a set of messages from this folder to a destination folder.
	 * <p>
	 * The IMAP copy command also keeps the flags intact. So, there's no need to
	 * change these manually.
	 * 
	 * @see org.columba.mail.folder.Folder#innerCopy(org.columba.mail.folder.IMailbox,
	 *      java.lang.Object, org.columba.core.command.WorkerStatusController)
	 */
	public void innerCopy(IMailbox destiny, Object[] uids) throws Exception {
		IMAPFolder destFolder = (IMAPFolder) destiny;

		Object[] destUids = getServer().copy(destFolder, uids, this);

		if (destUids.length != uids.length) {
			LOG
					.warning("Some messages could not be copied because they do not exist anymore!");
		}

		// update headerlist of destination-folder
		// -> this is necessary to reflect the changes visually
		int j = 0;
		for (int i = 0; i < uids.length; i++) {
			ColumbaHeader header = (ColumbaHeader) getHeaderList().get(uids[i]);
			// Was this message actually copied?

			if (header != null) {
				destFolder.getHeaderListStorage().addMessage(destUids[j],
						header.getHeader(), header.getAttributes(),
						header.getFlags());
				destFolder.fireMessageAdded(destUids[j]);
				j++;
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
	 *      org.columba.core.command.WorkerStatusController)
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
	 *      org.columba.core.command.WorkerStatusController)
	 * @TODO dont use deprecated method
	 */
	public IColumbaHeader getMessageHeader(Object uid) throws Exception {
		if (headerList == null) {
			getHeaderList();
		}

		return (ColumbaHeader) headerList.get(uid);
	}

	/**
	 * Method getMessage.
	 * 
	 * @param uid
	 * @param worker
	 * @return AbstractMessage
	 * @throws Exception
	 */
	public IColumbaMessage getMessage(Object uid) throws Exception {
		return new ColumbaMessage((ColumbaHeader) headerList.get((String) uid));
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

		AbstractFolder child = this;

		while (true) {
			child = (AbstractFolder) child.getParent();

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

		if (root != null) {
			return root.tryToGetLock(locker);
		} else {
			return false;
		}
	}

	/**
	 * @see org.columba.mail.folder.FolderTreeNode#releaseLock()
	 */
	public void releaseLock(Object locker) {
		IMailFolder root = getRootFolder();

		if (root != null) {
			root.releaseLock(locker);
		}
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
	public StatusObservable getObservable() {
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

		fireMessageAdded(uid);

		// update the HeaderList
		getHeaderListStorage().addMessage(uid, header, attributes, imapFlags);

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
		ColumbaHeader header = (ColumbaHeader) getHeaderList().get(uid);

		Header result = new Header();

		// if only one headerfield wasn't found in cache
		// -> call IMAPStore.getHeader() to fetch the
		// -> missing headerfields
		boolean parsingNeeded = false;

		// cached headerfield list
		List list = Arrays.asList(CachedHeaderfields.getDefaultHeaderfields());

		for (int i = 0; i < keys.length; i++) {
			if (header.get(keys[i]) != null) {
				// headerfield found
				result.set(keys[i], header.get(keys[i]));
			} else {
				// check if this headerfield is in the cache
				// -> if its not a cached headerfield, we need to fetch it
				if (!list.contains(keys[i])) {
					parsingNeeded = true;
				}
			}
		}

		if (parsingNeeded) {
			return getServer().getHeaders(uid, keys, this);
		} else {
			return result;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.IMailbox#getMimePartBodyStream(java.lang.Object,
	 *      java.lang.Integer[])
	 */
	public InputStream getMimePartBodyStream(Object uid, Integer[] address)
			throws Exception {
		return getServer().getMimePartBodyStream(uid, address, this);
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

		fireMessageAdded(uid);

		// update the HeaderList
		Header header = withHeaderInputStream.getHeader();
		ColumbaHeader h = new ColumbaHeader(header);
		getHeaderListStorage().addMessage(uid, header, h.getAttributes(),
				h.getFlags());

		return uid;
	}

	/**
	 * @see org.columba.mail.folder.Folder#getHeaderListStorage()
	 */
	public IHeaderListStorage getHeaderListStorage() {
		if (attributeStorage == null)
			attributeStorage = new RemoteHeaderListStorage(this);

		return attributeStorage;
	}

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
	public void updateFlag(IMAPFlags flag) throws IOException, IMAPException,
			CommandCancelledException {
		if (getServer().isSelected(this)) {
			Integer[] uids = getServer().fetchUids(
					new SequenceSet(flag.getIndex()), this);
			if (uids.length == 1) {
				flag.setUid(uids[0]);
				setFlags(new Flags[] { flag });
			}
		}
	}

	/**
	 * @param readOnly
	 *            The readOnly to set.
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	class RemoteHeaderListStorage extends AbstractHeaderListStorage {

		protected AbstractHeaderCache headerCache;

		private IMAPFolder folder;

		/**
		 *  
		 */
		public RemoteHeaderListStorage(IMAPFolder folder) {
			super();
			this.folder = folder;

		}

		/**
		 * @see org.columba.mail.folder.AbstractHeaderListStorage#getHeaderCacheInstance()
		 */
		public AbstractHeaderCache getHeaderCacheInstance() {
			if (headerCache == null)
				headerCache = new RemoteHeaderCache(folder);

			return headerCache;
		}

	}
	/* (non-Javadoc)
	 * @see org.columba.mail.folder.AbstractMessageFolder#searchMessages(org.columba.core.filter.Filter, java.lang.Object[])
	 */
	public Object[] searchMessages(Filter filter, Object[] uids)
			throws Exception {
		ensureFolderIsSynced();		
		return super.searchMessages(filter, uids);
	}
	/* (non-Javadoc)
	 * @see org.columba.mail.folder.AbstractMessageFolder#searchMessages(org.columba.core.filter.Filter)
	 */
	public Object[] searchMessages(Filter filter) throws Exception {
		ensureFolderIsSynced();
		return super.searchMessages(filter);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/757.java