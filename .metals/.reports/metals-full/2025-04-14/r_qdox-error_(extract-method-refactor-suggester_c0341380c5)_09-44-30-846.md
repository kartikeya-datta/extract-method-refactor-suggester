error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7696.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7696.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7696.java
text:
```scala
public I@@HeaderItem getHeaderItem(String add) {

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
package org.columba.addressbook.gui.autocomplete;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.columba.addressbook.folder.AbstractFolder;
import org.columba.addressbook.gui.tree.AddressbookTreeModel;
import org.columba.addressbook.model.ContactItem;
import org.columba.addressbook.model.GroupItem;
import org.columba.addressbook.model.HeaderItem;
import org.columba.addressbook.model.IHeaderItem;
import org.columba.addressbook.model.IHeaderItemList;
import org.frapuccino.addresscombobox.ItemProvider;

public class AddressCollector implements ItemProvider, IAddressCollector {

	private	Hashtable _adds = new Hashtable();
	private static AddressCollector instance = new AddressCollector();

	private AddressCollector() {
	}
	
	public static AddressCollector getInstance() {
		return instance;
	}

	/**
	 * Add all contacts and group items to hashmap.
	 * 
	 * @param uid			selected folder uid
	 * @param includeGroup	add groups if true. No groups, otherwise.
	 */
	public void addAllContacts(int uid, boolean includeGroup) {
		IHeaderItemList list = null;

		try {
			AbstractFolder folder = (AbstractFolder) AddressbookTreeModel.getInstance()
					.getFolder(uid);
			list = folder.getHeaderItemList();
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		if (list == null)
			return;

		Iterator it = list.iterator();
		while (it.hasNext()) {
			HeaderItem headerItem = (HeaderItem) it.next();

			if (headerItem.isContact()) {
				// contacts item
				ContactItem item = (ContactItem) headerItem;

				addAddress(item.getDisplayName(), item);

				addAddress(item.getAddress(), item);
			} else {
				if (includeGroup) {
					// group item
					GroupItem item = (GroupItem) headerItem;

					addAddress(item.getDisplayName(), item);
				}
			}
		}
	}

	public void addAddress(String add, IHeaderItem item) {
		if (add != null) {
			_adds.put(add, item);
		}
	}

	public Object[] getAddresses() {
		return _adds.keySet().toArray();
	}

	public HeaderItem getHeaderItem(String add) {
		return (HeaderItem) _adds.get(add);
	}

	public void clear() {
		_adds.clear();
	}

	/**
	 * @see org.frappucino.addresscombobox.ItemProvider#getMatchingItems(java.lang.String)
	 */
	public Object[] getMatchingItems(String s) {
		Object[] items = getAddresses();

		Vector v = new Vector();
		//		 for each JComboBox item
		for (int k = 0; k < items.length; k++) {
			// to lower case
			String item = items[k].toString().toLowerCase();
			// compare if item starts with str
			if (item.startsWith(s.toLowerCase())) {
				v.add(item);
			}
		}
		return v.toArray();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7696.java