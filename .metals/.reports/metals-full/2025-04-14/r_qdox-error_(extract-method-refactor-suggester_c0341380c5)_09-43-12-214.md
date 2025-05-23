error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3386.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3386.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3386.java
text:
```scala
f@@ireMessageRemoved(h.getVirtualUid(), h.getFlags());

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

package org.columba.mail.folder.virtual;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JDialog;

import org.columba.api.command.IWorkerStatusController;
import org.columba.core.filter.Filter;
import org.columba.core.filter.FilterCriteria;
import org.columba.core.filter.FilterRule;
import org.columba.core.xml.XmlElement;
import org.columba.mail.config.FolderItem;
import org.columba.mail.config.IFolderItem;
import org.columba.mail.filter.MailFilterCriteria;
import org.columba.mail.folder.AbstractFolder;
import org.columba.mail.folder.AbstractMessageFolder;
import org.columba.mail.folder.FolderChildrenIterator;
import org.columba.mail.folder.FolderFactory;
import org.columba.mail.folder.IMailFolder;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.folder.event.FolderListener;
import org.columba.mail.folder.event.IFolderEvent;
import org.columba.mail.folder.headercache.CachedHeaderfields;
import org.columba.mail.folder.headercache.MemoryHeaderList;
import org.columba.mail.folder.imap.IMAPFolder;
import org.columba.mail.folder.search.DefaultSearchEngine;
import org.columba.mail.gui.config.search.SearchFrame;
import org.columba.mail.gui.frame.AbstractMailFrameController;
import org.columba.mail.gui.tree.FolderTreeModel;
import org.columba.mail.message.ColumbaHeader;
import org.columba.mail.message.ICloseableIterator;
import org.columba.mail.message.IHeaderList;
import org.columba.ristretto.message.Attributes;
import org.columba.ristretto.message.Flags;
import org.columba.ristretto.message.Header;
import org.columba.ristretto.message.MimeTree;

/**
 * Virtual folder presenting search results and saving only references to
 * messages of "real" folders.
 * <p>
 * Almost all methods don't do anything here, because the we pass all operatins
 * to the source folders. This happens on the Command and CommandReference
 * abstraction level.
 * <p>
 * 
 * @author fdietz
 *  
 */
public class VirtualFolder extends AbstractMessageFolder implements FolderListener {
	/** JDK 1.4+ logging framework logger, used for logging. */
	private static final Logger LOG = Logger
			.getLogger("org.columba.mail.folder.virtual"); //$NON-NLS-1$

	protected int nextUid;

	protected IHeaderList headerList;

	private boolean active;
	
	public VirtualFolder(FolderItem item, String path) {
		super(item, path);

		headerList = new MemoryHeaderList();

		ensureValidFilterElement();
	}

	public VirtualFolder(String name, String type, String path) {
		super(name, type, path);

		IFolderItem item = getConfiguration();
		item.setString("property", "accessrights", "user");
		item.setString("property", "subfolder", "true");
		item.setString("property", "include_subfolders", "true");
		item.setString("property", "source_uid", "101");

		headerList = new MemoryHeaderList();

		ensureValidFilterElement();
	}

	private void registerWithSource() {
		AbstractFolder folder = getSourceFolder();
		
		folder.addFolderListener(this);
		
		if( isRecursive() ) {
			FolderChildrenIterator it = new FolderChildrenIterator(folder);
		
			while( it.hasMoreChildren() ) {
				IMailFolder next = it.nextChild();
				
				if( !(next instanceof VirtualFolder) ) {
					next.addFolderListener(this);
				}
			}
		}
	}
	
	private void unregisterWithSource() {
		AbstractFolder folder = getSourceFolder();
		
		folder.removeFolderListener(this);
		
		if( isRecursive() ) {
			FolderChildrenIterator it = new FolderChildrenIterator(folder);
		
			while( it.hasMoreChildren() ) {
				IMailFolder next = it.nextChild();
				
				if( !(next instanceof VirtualFolder) ) {
					next.removeFolderListener(this);
				}
			}
		}
	}

	/**
	 * Ensures that there is at least one valid filter entry in the
	 * VFolder.
	 */
	private void ensureValidFilterElement() {		
		XmlElement filter = getConfiguration().getRoot().getElement("filter");
		
		if (filter == null) {
			filter = new XmlElement("filter");
			filter.addAttribute("description", "new filter");
			filter.addAttribute("enabled", "true");
			getConfiguration().getRoot().addElement(filter);
		} 
		
		if( filter.count() == 0 ) {
			XmlElement rules = new XmlElement("rules");
			rules.addAttribute("condition", "matchall");

			XmlElement criteria = new XmlElement("criteria");
			criteria.addAttribute("type", "Subject");
			criteria.addAttribute("headerfield", "Subject");
			criteria.addAttribute("criteria", "contains");
			criteria.addAttribute("pattern", "pattern");
			rules.addElement(criteria);
			filter.addElement(rules);
		}
	}
	

	protected Object generateNextUid() {
		return new Integer(nextUid++);
	}

	public void setNextUid(int next) {
		nextUid = next;
	}

	public JDialog showFilterDialog(AbstractMailFrameController frameController) {
		return new SearchFrame(frameController, this);
	}

	public boolean exists(Object uid) throws Exception {
		return headerList.exists(uid);
	}

	public IHeaderList getHeaderList() throws Exception {
		if( !active ) {
			activate();
		} else {
			revalidateSearch();
		}
		
		return headerList;
	}

	/**
	 * 
	 */
	private void revalidateSearch() {
		VirtualHeader h;
		
		// Analyze the Filter
		Filter filter = (Filter) getFilter().clone();
		FilterRule rule = filter.getFilterRule();
		for( int i=0;i <rule.count(); i++) {
			FilterCriteria c = rule.get(i);
			if( ! c.getTypeString().equalsIgnoreCase("flags")) {
				rule.remove(i);
				i--;
			}
		}
		
		
		// If no flags filter the seach is still valid
		if( rule.count() == 0) {
			return;
		}
		
		// redo the seach for the flags criteria		
		ICloseableIterator it = headerList.headerIterator();
		while( it.hasNext()) {
			h = (VirtualHeader) it.next();
			
			try {
				if( h.getSrcFolder().searchMessages(filter, new Object[] {h.getSrcUid()}).length == 0) {
					it.remove();

					// notify listeners
					fireMessageRemoved(h.getVirtualUid(), null);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		it.close();
		
	}

	public void addSearchToHistory() throws Exception {
		VirtualFolder searchFolder = (VirtualFolder) FolderTreeModel
				.getInstance().getFolder(106);

		// only create new subfolders if we used the default "Search Folder"
		if (!searchFolder.equals(this)) {
			return;
		}

		// (tstich) reduced to 3 because all need to be
		// search when activated on startup
		// we only want 3 subfolders
		// -> if more children exist remove them
		if (searchFolder.getChildCount() >= 3) {
			AbstractMessageFolder child = (AbstractMessageFolder) searchFolder
					.getChildAt(0);
			child.removeFolder();
		}

		// create new subfolder
		String name = "search result";
		VirtualFolder newFolder = null;

		try {
			newFolder = (VirtualFolder) FolderFactory.getInstance()
					.createChild(searchFolder, name, "VirtualFolder");
		} catch (Exception ex) {
			ex.printStackTrace();

			return;
		}

		// if creation failed
		if (newFolder == null) {
			return;
		}

		// copy all properties to the subfolder
		int uid = getConfiguration().getInteger("property", "source_uid");
		boolean includes = getConfiguration().getBoolean("property",
				"include_subfolders");

		IFolderItem newFolderItem = newFolder.getConfiguration();
		newFolderItem.setInteger("property", "source_uid", uid);
		newFolderItem.setBoolean("property", "include_subfolders", includes);

		newFolderItem.getElement("filter").removeFromParent();
		newFolderItem.getRoot().addElement(
				(XmlElement) getConfiguration().getElement("filter").clone());

		MailFilterCriteria newc = new MailFilterCriteria(new Filter(
				getConfiguration().getElement("filter")).getFilterRule().get(0));

		/*
		 * FilterCriteria c = new
		 * Filter(getConfiguration().getElement("filter")) .getFilterRule()
		 * .get( 0);
		 * 
		 * FilterCriteria newc = new
		 * Filter(getConfiguration().getElement("filter")) .getFilterRule()
		 * .get( 0); newc.setCriteria(c.getCriteriaString());
		 * newc.setHeaderItem(c.getHeaderItemString());
		 * newc.setPattern(c.getPattern()); newc.setType(c.getType());
		 */

		// lets find a good name for our new vfolder
		StringBuffer buf = new StringBuffer();

		if (newc.getTypeString().equalsIgnoreCase("flags")) {
			LOG.info("flags found"); //$NON-NLS-1$

			buf.append(newc.getTypeString());
			buf.append(" (");
			buf.append(newc.getCriteriaString());
			buf.append(" ");
			buf.append(newc.getPatternString());
			buf.append(")");
		} else if (newc.getTypeString().equalsIgnoreCase("custom headerfield")) {
			buf.append(newc.getHeaderfieldString());
			buf.append(" (");
			buf.append(newc.getCriteriaString());
			buf.append(" ");
			buf.append(newc.getPatternString());
			buf.append(")");
		} else {
			buf.append(newc.getTypeString());
			buf.append(" (");
			buf.append(newc.getCriteriaString());
			buf.append(" ");
			buf.append(newc.getPatternString());
			buf.append(")");
		}

		newFolder.setName(buf.toString());

		// update tree-view
		FolderTreeModel.getInstance().nodeStructureChanged(searchFolder);

		// update tree-node (for renaming the new folder)
		FolderTreeModel.getInstance().nodeChanged(newFolder);
	}

	protected void applySearch() throws Exception {
		AbstractMessageFolder srcFolder = getSourceFolder();

		XmlElement filter = getConfiguration().getRoot().getElement("filter");

		if (filter == null) {
			filter = new XmlElement("filter");
			filter.addAttribute("description", "new filter");
			filter.addAttribute("enabled", "true");

			XmlElement rules = new XmlElement("rules");
			rules.addAttribute("condition", "match_all");

			XmlElement criteria = new XmlElement("criteria");
			criteria.addAttribute("type", "Subject");
			criteria.addAttribute("headerfield", "Subject");
			criteria.addAttribute("criteria", "contains");
			criteria.addAttribute("pattern", "pattern");
			rules.addElement(criteria);
			filter.addElement(rules);
			getConfiguration().getRoot().addElement(filter);
		}

		Filter f = new Filter(getConfiguration().getRoot().getElement("filter"));

		applySearch(srcFolder, f);
	}

	/**
	 * @return
	 */
	AbstractMessageFolder getSourceFolder() {
		int uid = getConfiguration().getInteger("property", "source_uid");
		AbstractMessageFolder srcFolder = (AbstractMessageFolder) FolderTreeModel
				.getInstance().getFolder(uid);
		
		/*
		while( srcFolder instanceof VirtualFolder ) {
			srcFolder = ((VirtualFolder) srcFolder).getSourceFolder();
		}*/
		
		return srcFolder;
	}

	protected void applySearch(AbstractMessageFolder parent, Filter filter)
			throws Exception {
		AbstractMessageFolder folder = parent;

		Object[] resultUids = folder.searchMessages(filter);
		String[] headerfields = CachedHeaderfields.getDefaultHeaderfields();

		
		if (resultUids != null) {
			for (int i = 0; i < resultUids.length; i++) {
				ColumbaHeader header = null;

				if (folder instanceof VirtualFolder) {
					//	get source folder reference
					VirtualHeader virtualHeader = ((VirtualFolder) folder)
							.getVirtualHeader(resultUids[i]);
					AbstractMessageFolder sourceFolder = virtualHeader
							.getSrcFolder();
					Object sourceUid = virtualHeader.getSrcUid();

					Header h = sourceFolder.getHeaderFields(sourceUid,
							headerfields);
					header = new ColumbaHeader(h);
					header.setAttributes(sourceFolder.getAttributes(sourceUid));
					header.setFlags(sourceFolder.getFlags(sourceUid));
					Object uid = add((ColumbaHeader) header, sourceFolder, sourceUid);
					fireMessageAdded(uid, getFlags(uid));
				} else {
					if( !folder.exists(resultUids[i])) continue;
					
					Header h = folder.getHeaderFields(resultUids[i],
							headerfields);
					header = new ColumbaHeader(h);
					header.setAttributes(folder.getAttributes(resultUids[i]));
					header.setFlags(folder.getFlags(resultUids[i]));
					Object uid = add(header, folder, resultUids[i]);
					fireMessageAdded(uid, getFlags(uid));

				}

			}
		}
		
		if (isRecursive()) {
			for (Enumeration e = parent.children(); e.hasMoreElements();) {
				folder = (AbstractMessageFolder) e.nextElement();

				if (folder instanceof VirtualFolder) {
					continue;
				}

				applySearch(folder, filter);
			}
		}
	}
	
	private boolean isRecursive() {
		return Boolean.valueOf(
				getConfiguration().getString("property", "include_subfolders"))
				.booleanValue();
	}

	public DefaultSearchEngine getSearchEngine() {
		return null;
	}

	public Filter getFilter() {
		return new Filter(getConfiguration().getRoot().getElement("filter"));
	}

	public Object add(ColumbaHeader header, AbstractMessageFolder source, Object uid)
			throws Exception {
		Object newUid = generateNextUid();

		//VirtualMessage m = new VirtualMessage(f, uid, index);
		VirtualHeader virtualHeader = new VirtualHeader((ColumbaHeader) header,
				source, uid);
		virtualHeader.setVirtualUid(newUid);

		headerList.add(virtualHeader, newUid);
		
		return newUid;
	}

	/**
	 * @see org.columba.modules.mail.folder.Folder#markMessage(Object[], int,
	 *      IMAPFolder)
	 */
	public void markMessage(Object[] uids, int variant) throws Exception {
		List list = new ArrayList();

		// Check if all uids are still exisiting
		for ( Object uid : uids) {
			if(exists(uid)) {
				list.add(uid);
			}
		}
		if( list.size() == 0 ) return;
		
		Collections.sort(list, new Comparator() {

			public int compare(Object o1, Object o2) {
				VirtualHeader h = (VirtualHeader) headerList.get(o1);
				int oV1 = h.getSrcFolder().getUid();
				
				h = (VirtualHeader) headerList.get(o2);
				int oV2 = h.getSrcFolder().getUid();
				
				if( oV1 < oV2) {
					return -1;
				} else {
					return oV1 == oV2 ? 0 : 1;
				}
			}});
		
		List folderUids = new ArrayList(uids.length);
		Iterator it = list.iterator();

		VirtualHeader h = (VirtualHeader)headerList.get(it.next());;
		folderUids.add(h.getSrcUid());
		IMailbox srcFolder = h.getSrcFolder();  
		
		while( it.hasNext() ){			
			h = (VirtualHeader)headerList.get(it.next());
			
			if( h.getSrcFolder() == srcFolder) {
				folderUids.add(h.getSrcUid());
			} else {			
				srcFolder.markMessage(folderUids.toArray(), variant);
				
				// change to new folder
				srcFolder = h.getSrcFolder();
				folderUids.clear();
				folderUids.add(h.getSrcUid());
			}
		}
		
		srcFolder.markMessage(folderUids.toArray(), variant);
	}

	/**
	 * @see org.columba.modules.mail.folder.Folder#remove(Object)
	 */
	public void removeMessage(Object uid) throws Exception {

		// get source folder reference
		VirtualHeader h = (VirtualHeader) headerList.get(uid);
		AbstractMessageFolder sourceFolder = h.getSrcFolder();
		Object sourceUid = h.getSrcUid();

		// remove from source folder
		sourceFolder.removeMessage(sourceUid);

		// remove from virtual folder
		headerList.remove(uid);

		// notify listeners
		fireMessageRemoved(uid, getFlags(uid));	
	}

	/**
	 * @see org.columba.modules.mail.folder.Folder#getMimeTree(Object,
	 *      IMAPFolder)
	 */
	public MimeTree getMimePartTree(Object uid) throws Exception {
		if( !exists(uid)) return null;
		
		VirtualHeader h = (VirtualHeader) headerList.get(uid);
		AbstractMessageFolder sourceFolder = h.getSrcFolder();
		Object sourceUid = h.getSrcUid();

		return sourceFolder.getMimePartTree(sourceUid);
	}

	/**
	 * Get virtual header.
	 * 
	 * @param virtualUid
	 *            virtual uid
	 * @return virtual header
	 */
	public VirtualHeader getVirtualHeader(Object virtualUid) {
		return (VirtualHeader) headerList.get(virtualUid);
	}

	/**
	 * @see org.columba.modules.mail.folder.Folder#searchMessages(Filter,
	 *      Object[], IWorkerStatusController)
	 */
	public Object[] searchMessages(Filter filter, Object[] uids)
			throws Exception {
		if( !active ) {
			activate();
		} else {
			revalidateSearch();
		}

		if(uids.length == 0 ) return new Object[0];

		List list = new ArrayList(Arrays.asList(uids));

		Collections.sort(list, new Comparator() {

			public int compare(Object o1, Object o2) {
				VirtualHeader h = (VirtualHeader) headerList.get(o1);
				int oV1 = h.getSrcFolder().getUid();
				
				h = (VirtualHeader) headerList.get(o2);
				int oV2 = h.getSrcFolder().getUid();
				
				if( oV1 < oV2) {
					return -1;
				} else {
					return oV1 == oV2 ? 0 : 1;
				}
			}});
		
		List resultList = new ArrayList();
		
		List virtualHeader = new ArrayList();
		VirtualHeader h = (VirtualHeader) headerList.get(list.get(0));
		AbstractMessageFolder sourceFolder = h.getSrcFolder();
		virtualHeader.add(h);
		
		
		for (int i = 1; i < uids.length; i++) {
			h = (VirtualHeader) headerList.get(list.get(i));
			
			if( h.getSrcFolder() != sourceFolder || i == uids.length - 1){
				// Now we can search this folder since no mail from
				// this folder will come in the list
				
				Object[] srcUids = new Object[virtualHeader.size()];
				
				// Create a src uid array
				for( int j=0; j< virtualHeader.size(); j++ ) {
					srcUids[j] = ((VirtualHeader)virtualHeader.get(j)).getSrcUid();
				}
				
				// search the src folder with the src uid array
				Object[] resultUids = sourceFolder.searchMessages(filter,
						srcUids);
				
				
				// Convert the src uids back to virtual uids
				if ((resultUids != null) && (resultUids.length > 0)) {
					Object[] virtualUids = new Object[resultUids.length];
					for( int j=0;j<resultUids.length;j++) {
						virtualUids[j] = srcUidToVirtualUid(sourceFolder, resultUids[j]);
					}
					
					// Add all found virtual uids to the result
					resultList.addAll(Arrays.asList(virtualUids));
				}
				
				virtualHeader.clear();
			}
			
			// Add this header to the list for later searching
			virtualHeader.add(h);
			sourceFolder = h.getSrcFolder();
		}
		if( virtualHeader.size() > 0) {
			// Now we can search this folder since no mail from
			// this folder will come in the list
			
			Object[] srcUids = new Object[virtualHeader.size()];
			
			// Create a src uid array
			for( int j=0; j< virtualHeader.size(); j++ ) {
				srcUids[j] = ((VirtualHeader)virtualHeader.get(j)).getSrcUid();
			}
			
			// search the src folder with the src uid array
			Object[] resultUids = sourceFolder.searchMessages(filter,
					srcUids);
			
			
			// Convert the src uids back to virtual uids
			if ((resultUids != null) && (resultUids.length > 0)) {
				Object[] virtualUids = new Object[resultUids.length];
				for( int j=0;j<resultUids.length;j++) {
					virtualUids[j] = srcUidToVirtualUid(sourceFolder, resultUids[j]);
				}
				
				// Add all found virtual uids to the result
				resultList.addAll(Arrays.asList(virtualUids));
			}
			
			virtualHeader.clear();
		}
		
		
		return resultList.toArray();
	}

	public Object[] searchMessages(Filter filter) throws Exception {
		
		return searchMessages(filter, getUids());
	}

	/**
	 * @see org.columba.modules.mail.folder.FolderTreeNode#instanceNewChildNode(AdapterNode,
	 *      FolderItem)
	 */
	public String getDefaultChild() {
		return null;
	}

	public static XmlElement getDefaultProperties() {
		XmlElement props = new XmlElement("property");
		props.addAttribute("accessrights", "user");
		props.addAttribute("subfolder", "true");
		props.addAttribute("include_subfolders", "true");
		props.addAttribute("source_uid", "101");

		return props;
	}

	/*
	 * public MailFolderCommandReference getCommandReference(
	 * MailFolderCommandReference r) { MailFolderCommandReference[] newReference =
	 * null;
	 * 
	 * Object[] uids = r[0].getUids(); // if we didn't pass uid array here, use
	 * all message in this virtual // folder try { if (uids == null) { uids =
	 * getUids(); } } catch (Exception e1) { e1.printStackTrace(); }
	 * 
	 * if (uids == null) { return r; }
	 * 
	 * Hashtable list = new Hashtable();
	 * 
	 * for (int i = 0; i < uids.length; i++) { VirtualHeader virtualHeader =
	 * (VirtualHeader) headerList .get(uids[i]); AbstractMessageFolder srcFolder =
	 * virtualHeader.getSrcFolder(); Object srcUid = virtualHeader.getSrcUid();
	 * 
	 * if (list.containsKey(srcFolder)) { // bucket for this folder exists
	 * already } else { // create new bucket for this folder list.put(srcFolder,
	 * new Vector()); }
	 * 
	 * List v = (Vector) list.get(srcFolder); v.add(srcUid); }
	 * 
	 * newReference = new MailFolderCommandReference[list.size() + 2];
	 * 
	 * int i = 0;
	 * 
	 * for (Enumeration e = list.keys(); e.hasMoreElements();) {
	 * AbstractMessageFolder srcFolder = (AbstractMessageFolder)
	 * e.nextElement(); List v = (Vector) list.get(srcFolder);
	 * 
	 * newReference[i] = new MailFolderCommandReference(srcFolder);
	 * 
	 * Object[] uidArray = new Object[v.size()]; ((Vector)
	 * v).copyInto(uidArray); newReference[i].setUids(uidArray);
	 * newReference[i].setMarkVariant(r[0].getMarkVariant());
	 * newReference[i].setMessage(r[0].getMessage());
	 * newReference[i].setDestFile(r[0].getDestFile());
	 * 
	 * i++; }
	 * 
	 * if (r.length > 1) { newReference[i] = new
	 * MailFolderCommandReference((AbstractMessageFolder) r[1] .getFolder()); }
	 * else { newReference[i] = null; }
	 * 
	 * newReference[i + 1] = r[0];
	 * 
	 * return newReference; }
	 */

	/**
	 * @see org.columba.mail.folder.FolderTreeNode#tryToGetLock(java.lang.Object)
	 */
	public boolean tryToGetLock(Object locker) {
		// First try to get the lock of the virtual folder
		boolean success = super.tryToGetLock(locker);
		if( !success ) return false;
		
		// We need to get the locks of all folders		
		AbstractFolder folder = getSourceFolder();
		
		success &= folder.tryToGetLock(locker);
		
		if( success && isRecursive() ) {
			FolderChildrenIterator it = new FolderChildrenIterator(folder);

			while( success && it.hasMoreChildren() ) {
				IMailFolder next = it.nextChild();
				
				if( !(next instanceof VirtualFolder) ) {
					success &= next.tryToGetLock(locker);
				}				
			}
		}
		
		if( ! success ) {
			releaseLock(locker);
		}
		
		return success;
	}

	/**
	 * @see org.columba.mail.folder.AbstractFolder#releaseLock(java.lang.Object)
	 */
	public void releaseLock(Object locker) {
		super.releaseLock(locker);
		
		AbstractFolder folder = getSourceFolder();
		
		folder.releaseLock(locker);
		
		if( isRecursive() ) {
			FolderChildrenIterator it = new FolderChildrenIterator(folder);
		
			while( it.hasMoreChildren() ) {
				IMailFolder next = it.nextChild();
				
				if( !(next instanceof VirtualFolder) ) {
					next.releaseLock(locker);
				}
			}
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.Folder#getUids(org.columba.api.command.IWorkerStatusController)
	 */
	public Object[] getUids() throws Exception {
		if( !active ) {
			activate();
		}
		
		return headerList.getUids();
	}

	protected Object srcUidToVirtualUid(IMailFolder srcFolder, Object uid) {
		ICloseableIterator it = headerList.headerIterator();
		
		while( it.hasNext() ) {
			VirtualHeader h = (VirtualHeader) it.next();
			if( h.getSrcUid().equals(uid) && h.getSrcFolder().equals(srcFolder) ) {
				it.close();
				return h.getVirtualUid();
			}
			
		}
		it.close();
		
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.IMailbox#addMessage(java.io.InputStream)
	 */
	public Object addMessage(InputStream in) throws Exception {
		// not supported
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.IMailbox#getAttribute(java.lang.Object,
	 *      java.lang.String)
	 */
	public Object getAttribute(Object uid, String key) throws Exception {
		VirtualHeader h = (VirtualHeader) headerList.get(uid);
		AbstractMessageFolder sourceFolder = h.getSrcFolder();
		Object sourceUid = h.getSrcUid();

		return sourceFolder.getAttribute(sourceUid, key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.IMailbox#getFlags(java.lang.Object)
	 */
	public Flags getFlags(Object uid) throws Exception {

		VirtualHeader h = (VirtualHeader) headerList.get(uid);
		AbstractMessageFolder sourceFolder = h.getSrcFolder();
		
		Object sourceUid = h.getSrcUid();

		return sourceFolder.getFlags(sourceUid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.IMailbox#getHeaderFields(java.lang.Object,
	 *      java.lang.String[])
	 */
	public Header getHeaderFields(Object uid, String[] keys) throws Exception {

		VirtualHeader h = (VirtualHeader) headerList.get(uid);
		AbstractMessageFolder sourceFolder = h.getSrcFolder();
		Object sourceUid = h.getSrcUid();

		return sourceFolder.getHeaderFields(sourceUid, keys);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.IMailbox#getMessageSourceStream(java.lang.Object)
	 */
	public InputStream getMessageSourceStream(Object uid) throws Exception {

		VirtualHeader h = (VirtualHeader) headerList.get(uid);
		AbstractMessageFolder sourceFolder = h.getSrcFolder();
		Object sourceUid = h.getSrcUid();

		return sourceFolder.getMessageSourceStream(sourceUid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.IMailbox#getMimePartBodyStream(java.lang.Object,
	 *      java.lang.Integer[])
	 */
	public InputStream getMimePartBodyStream(Object uid, Integer[] address)
			throws Exception {

		VirtualHeader h = (VirtualHeader) headerList.get(uid);
		AbstractMessageFolder sourceFolder = h.getSrcFolder();
		Object sourceUid = h.getSrcUid();

		return sourceFolder.getMimePartBodyStream(sourceUid, address);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.IMailbox#getMimePartSourceStream(java.lang.Object,
	 *      java.lang.Integer[])
	 */
	public InputStream getMimePartSourceStream(Object uid, Integer[] address)
			throws Exception {

		VirtualHeader h = (VirtualHeader) headerList.get(uid);
		AbstractMessageFolder sourceFolder = h.getSrcFolder();
		Object sourceUid = h.getSrcUid();

		return sourceFolder.getMimePartSourceStream(sourceUid, address);
	}

	/**
	 * 
	 * VirtualFolder doesn't allow adding messages, in comparison to other
	 * regular mailbox folders.
	 * 
	 * @see org.columba.mail.folder.FolderTreeNode#supportsAddMessage()
	 */
	public boolean supportsAddMessage() {
		return false;
	}

	/**
	 * Virtual folders can only accept other Virtual folders as childs.
	 * 
	 * @param newFolderType
	 *            a folder to check if it is a Virtual folder.
	 * @return true if the folder is a VirtualFolder; false otherwise.
	 */
	public boolean supportsAddFolder(String newFolderType) {
		return (newFolderType.equals(getType()));
	}

	public void innerCopy(IMailbox destFolder, Object[] uids) throws Exception {
		List list = new ArrayList();

		// Check if all uids are still exisiting
		for ( Object uid : uids) {
			if(exists(uid)) {
				list.add(uid);
			}
		}
		if( list.size() == 0 ) return;

		Collections.sort(list, new Comparator() {

			public int compare(Object o1, Object o2) {
				VirtualHeader h = (VirtualHeader) headerList.get(o1);
				int oV1 = h.getSrcFolder().getUid();
				
				h = (VirtualHeader) headerList.get(o2);
				int oV2 = h.getSrcFolder().getUid();
				
				if( oV1 < oV2) {
					return -1;
				} else {
					return oV1 == oV2 ? 0 : 1;
				}
			}});
		
		List folderUids = new ArrayList(uids.length);
		Iterator it = list.iterator();

		VirtualHeader h = (VirtualHeader)headerList.get(it.next());;
		folderUids.add(h.getSrcUid());
		IMailbox srcFolder = h.getSrcFolder();  
		
		while( it.hasNext() ){			
			h = (VirtualHeader)headerList.get(it.next());
			
			if( h.getSrcFolder() == srcFolder) {
				folderUids.add(h.getSrcUid());
			} else {			
				srcFolder.innerCopy(destFolder, folderUids.toArray());
				
				// change to new folder
				srcFolder = h.getSrcFolder();
				folderUids.clear();
				folderUids.add(h.getSrcUid());
			}
		}
		
		// Copy the rest
		srcFolder.innerCopy(destFolder, folderUids.toArray());
	}

	public void setAttribute(Object uid, String key, Object value)
			throws Exception {
		// get header with UID
		/*
		 * ColumbaHeader header = (ColumbaHeader) headerList.get(uid);
		 * header.getAttributes().put(key, value);
		 */

		VirtualHeader h = (VirtualHeader) headerList.get(uid);
		h.getAttributes().put(key, value);
		AbstractMessageFolder sourceFolder = h.getSrcFolder();
		Object sourceUid = h.getSrcUid();

		sourceFolder.setAttribute(sourceUid, key, value);
	}

	public Attributes getAttributes(Object uid) throws Exception {
		VirtualHeader h = (VirtualHeader) headerList.get(uid);
		AbstractMessageFolder sourceFolder = h.getSrcFolder();
		Object sourceUid = h.getSrcUid();

		return sourceFolder.getAttributes(sourceUid);

		/*
		 * if (getHeaderList().containsKey(uid)) { return
		 * getHeaderList().get(uid).getAttributes(); } else { return null; }
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.IMailbox#addMessage(java.io.InputStream,
	 *      org.columba.ristretto.message.Attributes)
	 */
	public Object addMessage(InputStream in, Attributes attributes, Flags flags)
			throws Exception {

		// not supported

		return null;
	}


	/**
	 * @see org.columba.mail.folder.AbstractFolder#supportsMove()
	 */
	public boolean supportsMove() {
		return true;
	}

	/**
	 * @see org.columba.mail.folder.IMailbox#getAllHeaderFields(java.lang.Object)
	 */
	public Header getAllHeaderFields(Object uid) throws Exception {
		VirtualHeader h = (VirtualHeader) headerList.get(uid);
		AbstractMessageFolder sourceFolder = h.getSrcFolder();
		Object sourceUid = h.getSrcUid();

		return sourceFolder.getAllHeaderFields(sourceUid);
	}

	/**
	 * @see org.columba.mail.folder.IMailbox#expungeFolder()
	 */
	public void expungeFolder() throws Exception {
		AbstractMessageFolder srcFolder = getSourceFolder();
		
		boolean isInclude = Boolean.valueOf(
				getConfiguration().getString("property", "include_subfolders"))
				.booleanValue();

		
		if( isInclude ) {
			recursiveExpunge(srcFolder);			
		} else {
			srcFolder.expungeFolder();
		}
	}
	
	private void recursiveExpunge(AbstractMessageFolder srcFolder) throws Exception {
		AbstractMessageFolder folder;

		srcFolder.expungeFolder();
		
		for (Enumeration e = srcFolder.children(); e.hasMoreElements();) {
			folder = (AbstractMessageFolder) e.nextElement();

			if (folder instanceof VirtualFolder) {
				continue;
			}
			
			recursiveExpunge(folder);
		}
		
	}
	/* (non-Javadoc)
	 * @see org.columba.mail.folder.IMailFolder#getRootFolder()
	 */
	public IMailFolder getRootFolder() {
		return getSourceFolder().getRootFolder();		
	}

	/* (non-Javadoc)
	 * @see org.columba.mail.folder.event.IFolderListener#messageAdded(org.columba.mail.folder.event.IFolderEvent)
	 */
	public void messageAdded(IFolderEvent e) {
		//deactivate the folder
		deactivate();
		return;
		/*
		AbstractMessageFolder folder = (AbstractMessageFolder)e.getSource();
		
		try {
			Object[] resultUids = folder.searchMessages(getFilter(), new Object[] {e.getChanges()});
			
			if( resultUids.length > 0 ) {
				Header h = folder.getHeaderFields(resultUids[0],
						CachedHeaderfields.getDefaultHeaderfields());
				ColumbaHeader header = new ColumbaHeader(h);
				header.setAttributes(folder.getAttributes(resultUids[0]));
				header.setFlags(folder.getFlags(resultUids[0]));
				
				Object uid = add(header, folder, resultUids[0]);
				fireMessageAdded(uid, getFlags(uid));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		*/
	}

	/* (non-Javadoc)
	 * @see org.columba.mail.folder.event.IFolderListener#messageRemoved(org.columba.mail.folder.event.IFolderEvent)
	 */
	public void messageRemoved(IFolderEvent e) {
		Object srcUid = e.getChanges();
		
		Object vUid = srcUidToVirtualUid((IMailFolder) e.getSource(), srcUid);
		if( vUid != null ) {
			headerList.remove(vUid);

			// notify listeners
			fireMessageRemoved(vUid, null);
		}
	}
	
	protected boolean hasFlagsCriteria() {
		boolean result = false;
		
		FilterRule rule = getFilter().getFilterRule();
		
		for( int i=0; i < rule.count() && !result; i++) {
			result = rule.get(i).getTypeString().equalsIgnoreCase("FLAGS");
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see org.columba.mail.folder.event.IFolderListener#messageFlagChanged(org.columba.mail.folder.event.IFolderEvent)
	 */
	public void messageFlagChanged(IFolderEvent e) {
		Object virtualUid = srcUidToVirtualUid((IMailFolder)e.getSource(), e.getChanges());

		if( virtualUid == null && hasFlagsCriteria()) {
			
			AbstractMessageFolder folder = (AbstractMessageFolder)e.getSource();
			try {
				Object[] resultUids = folder.searchMessages(getFilter(), new Object[] {e.getChanges()});
				
				if( resultUids.length > 0 ) {
					Header h = folder.getHeaderFields(resultUids[0],
							CachedHeaderfields.getDefaultHeaderfields());
					ColumbaHeader header = new ColumbaHeader(h);
					header.setAttributes(folder.getAttributes(resultUids[0]));
					header.setFlags(folder.getFlags(resultUids[0]));
					
					Object uid = add(header, folder, resultUids[0]);
					fireMessageAdded(uid, getFlags(uid));
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} 
		
		if( virtualUid != null ) {
			// Update the Virtual Header
			
			VirtualHeader h = (VirtualHeader) headerList.get(virtualUid);
			AbstractMessageFolder folder = (AbstractMessageFolder)e.getSource();
			try {			
				h.setAttributes( folder.getAttributes( e.getChanges()));				
				h.setFlags( folder.getFlags(e.getChanges()));
			
				updateMailFolderInfo(e.getOldFlags(), e.getParameter());
			} catch (Exception e1) {
			}
		
			//fire updates
			fireMessageFlagChanged(virtualUid, e.getOldFlags(), e.getParameter());
		}
	}

	/* (non-Javadoc)
	 * @see org.columba.mail.folder.event.IFolderListener#folderPropertyChanged(org.columba.mail.folder.event.IFolderEvent)
	 */
	public void folderPropertyChanged(IFolderEvent e) {
		//don't care
	}

	/* (non-Javadoc)
	 * @see org.columba.mail.folder.event.IFolderListener#folderAdded(org.columba.mail.folder.event.IFolderEvent)
	 */
	public void folderAdded(IFolderEvent e) {
		if( isRecursive() && !(e.getChanges() instanceof VirtualFolder )) {
			AbstractMessageFolder folder = (AbstractMessageFolder) e.getChanges();
			folder.addFolderListener(this);
		}
	}

	/* (non-Javadoc)
	 * @see org.columba.mail.folder.event.IFolderListener#folderRemoved(org.columba.mail.folder.event.IFolderEvent)
	 */
	public void folderRemoved(IFolderEvent e) {
		AbstractMessageFolder folder = (AbstractMessageFolder) e.getChanges();
		folder.removeFolderListener(this);
	}

	/**
	 * 
	 */
	public void activate() throws Exception {
		if( active ) return; 
		
		LOG.fine("Activating virtual folder " + getName());
		getMessageFolderInfo().reset();
		applySearch();
		registerWithSource();		
		active = true;
	}
	
	public void deactivate() {
		active = false;
		headerList.clear();
		getMessageFolderInfo().reset();
		
		unregisterWithSource();
	}
	
	/* (non-Javadoc)
	 * @see org.columba.mail.folder.IMailFolder#removeFolder()
	 */
	public void removeFolder() throws Exception {
		if(active) {
			deactivate();
		}
		super.removeFolder();
	}

	/**
	 * @see org.columba.mail.folder.AbstractMessageFolder#getLastSelection()
	 */
	public Object getLastSelection() {
		// not supported by virtual folder
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3386.java