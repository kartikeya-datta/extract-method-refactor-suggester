error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1013.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1013.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1013.java
text:
```scala
V@@ector<IHeaderItem> v = new Vector<IHeaderItem>();

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
package org.columba.addressbook.facade;

import java.util.List;
import java.util.Vector;

import org.columba.addressbook.folder.AbstractFolder;
import org.columba.addressbook.folder.IContactFolder;
import org.columba.addressbook.folder.IFolder;
import org.columba.addressbook.folder.StoreException;
import org.columba.addressbook.gui.tree.AddressbookTreeModel;
import org.columba.addressbook.gui.tree.util.SelectAddressbookFolderDialog;
import org.columba.addressbook.model.ContactModel;
import org.columba.addressbook.model.EmailModel;
import org.columba.addressbook.model.IContactModel;
import org.columba.addressbook.model.IHeaderItem;
import org.columba.addressbook.parser.ParserUtil;
import org.columba.core.logging.Logging;
import org.columba.ristretto.message.Address;
import org.columba.ristretto.parser.ParserException;

/**
 * Provides high-level contact management methods.
 * 
 * @author fdietz
 */
public final class ContactFacade implements IContactFacade {

	private static final java.util.logging.Logger LOG = java.util.logging.Logger
			.getLogger("org.columba.addressbook.facade"); //$NON-NLS-1$

	/**
	 * @see org.columba.addressbook.facade.IContactFacade#addContact(int,
	 *      java.lang.String)
	 */
	public void addContact(String uid, String address) throws StoreException{
		if (address == null || address.length() == 0) throw new IllegalArgumentException("address == null or empty String");
		
		if ( uid == null ) throw new IllegalArgumentException("uid == null");
		
		AbstractFolder selectedFolder = (AbstractFolder) AddressbookTreeModel
				.getInstance().getFolder(Integer.parseInt(uid));

		IContactModel card = createContactModel(address);

		try {
			if (selectedFolder.exists(card.getPreferredEmail()) == null)
				selectedFolder.add(card);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private ContactModel createContactModel(String address) {
		if (address == null || address.length() == 0) throw new IllegalArgumentException("address == null or empty String");
		
		Address adr;
		try {
			adr = Address.parse(address);
		} catch (ParserException e1) {
			if (Logging.DEBUG)
				e1.printStackTrace();
			return null;
		}

		LOG.info("address:" + address); //$NON-NLS-1$

		ContactModel card = new ContactModel();

		String fn = adr.getShortAddress();

		card.setFormattedName(fn);
		// backwards compatibility
		card.setSortString(fn);
		card
				.addEmail(new EmailModel(adr.getMailAddress(),
						EmailModel.TYPE_WORK));

		String[] result = ParserUtil.tryBreakName(fn);
		card.setGivenName(result[0]);
		card.setFamilyName(result[1]);
		card.setAdditionalNames(result[2]);
		return card;
	}

	/**
	 * @see org.columba.addressbook.facade.IContactFacade#addContactToCollectedAddresses(java.lang.String)
	 */
	public void addContactToCollectedAddresses(String address) throws StoreException{
		if (address == null || address.length() == 0) throw new IllegalArgumentException("address == null or empty String");
		
		addContact("102", address);
	}

	/**
	 * @see org.columba.addressbook.facade.IContactFacade#addContactToPersonalAddressbook(java.lang.String)
	 */
	public void addContactToPersonalAddressbook(String address) throws StoreException{
		if (address == null || address.length() == 0) throw new IllegalArgumentException("address == null or empty String");
		addContact("101", address);
	}

	/**
	 * @see org.columba.addressbook.facade.IContactFacade#addContact(int,
	 *      java.lang.String[])
	 */
	public void addContact(String uid, String[] address) throws StoreException{
		if ( uid == null ) throw new IllegalArgumentException("uid == null");
		
		if (address == null || address.length == 0) throw new IllegalArgumentException("address == null or null entry array");
		
		AddressbookTreeModel model = AddressbookTreeModel.getInstance();
		IContactFolder folder = (IContactFolder) model.getFolder(Integer.parseInt(uid));

		for (int i = 0; i < address.length; i++) {
			IContactModel card = createContactModel(address[i]);

			try {
				if (folder.exists(card.getPreferredEmail()) == null)
					folder.add(card);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see org.columba.addressbook.facade.IContactFacade#addContact(java.lang.String[])
	 */
	public void addContact(String[] address) throws StoreException{
		if (address == null || address.length == 0) throw new IllegalArgumentException("address == null or null entry array");
		
		AddressbookTreeModel model = AddressbookTreeModel.getInstance();
		SelectAddressbookFolderDialog dialog = new SelectAddressbookFolderDialog(
				model);
		if (dialog.success()) {
			IFolder folder = dialog.getSelectedFolder();
			int uid = folder.getUid();

			addContact(new Integer(uid).toString(), address);
		} else
			return;
	}

	/**
	 * @see org.columba.addressbook.facade.IContactFacade#addContact(java.lang.String)
	 */
	public void addContact(String address)throws StoreException {
		if (address == null || address.length() == 0) throw new IllegalArgumentException("address == null or empty String");
		
		AddressbookTreeModel model = AddressbookTreeModel.getInstance();
		SelectAddressbookFolderDialog dialog = new SelectAddressbookFolderDialog(
				model);
		if (dialog.success()) {
			IFolder folder = dialog.getSelectedFolder();
			int uid = folder.getUid();

			addContact(new Integer(uid).toString(), address);
		} else
			return;
	}

	/**
	 * @see org.columba.addressbook.facade.IContactFacade#getAllHeaderItems(java.lang.String)
	 */
	public List<IHeaderItem> getAllHeaderItems(String uid) throws StoreException{
		if ( uid == null ) throw new IllegalArgumentException("uid == null");
		
		Vector v = new Vector();
		AddressbookTreeModel model = AddressbookTreeModel.getInstance();
		IFolder f = model.getFolder(Integer.parseInt(uid));
		if ( f == null ) return v;
		
		IContactFolder folder = (IContactFolder) f;
		try {
			return folder.getHeaderItemList().getList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return v;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1013.java