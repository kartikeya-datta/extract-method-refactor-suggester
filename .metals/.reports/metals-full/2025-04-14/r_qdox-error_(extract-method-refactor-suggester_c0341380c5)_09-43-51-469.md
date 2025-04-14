error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5637.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5637.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5637.java
text:
```scala
C@@omposerController controller = new ComposerController();

// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Library General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

package org.columba.mail.gui.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.columba.addressbook.folder.ContactCard;
import org.columba.addressbook.gui.tree.util.SelectAddressbookFolderDialog;
import org.columba.mail.gui.composer.ComposerController;
import org.columba.mail.gui.mimetype.MimeTypeViewer;
import org.columba.main.MainInterface;

public class URLController implements ActionListener {

	private String address;
	private URL link;

	public JPopupMenu createContactMenu(String contact) {
		JPopupMenu popup = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem("Add Contact to Addressbook");
		menuItem.addActionListener(this);
		menuItem.setActionCommand("CONTACT");
		popup.add(menuItem);
		menuItem = new JMenuItem("Compose Message for " + contact);
		menuItem.setActionCommand("COMPOSE");
		menuItem.addActionListener(this);
		popup.add(menuItem);

		return popup;
	}

	public JPopupMenu createLinkMenu() {
		JPopupMenu popup = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem("Open");
		menuItem.addActionListener(this);
		menuItem.setActionCommand("OPEN");
		popup.add(menuItem);
		menuItem = new JMenuItem("Open with...");
		menuItem.setActionCommand("OPEN_WITH");
		menuItem.addActionListener(this);
		popup.add(menuItem);
		popup.addSeparator();
		menuItem = new JMenuItem("Open with internal browser");
		menuItem.setActionCommand("OPEN_WITHINTERNALBROWSER");
		menuItem.addActionListener(this);
		popup.add(menuItem);

		return popup;
	}

	public void setAddress(String s) {
		this.address = s;
	}

	public String getAddress() {
		return address;
	}

	public URL getLink() {
		return link;
	}

	public void setLink(URL u) {
		this.link = u;
	}

	public void compose(String address) {
		ComposerController controller = new ComposerController(null);

		controller.getModel().setTo(address);

		controller.showComposerWindow();
	}

	public void contact(String address) {
		SelectAddressbookFolderDialog dialog =
			MainInterface
				.addressbookInterface
				.tree
				.getSelectAddressbookFolderDialog();

		org.columba.addressbook.folder.Folder selectedFolder =
			dialog.getSelectedFolder();

		if (selectedFolder == null)
			return;

		try {

			ContactCard card = new ContactCard();
			card.set("displayname", address);
			card.set("email", "internet", address);

			selectedFolder.add(card);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public JPopupMenu createMenu(URL url) {
		if (url.getProtocol().equalsIgnoreCase("mailto")) {
			// found email address

			setAddress(url.getFile());
			JPopupMenu menu = createContactMenu(url.getFile());
			return menu;

		} else {

			setLink(url);
			JPopupMenu menu = createLinkMenu();
			return menu;
		}
	}

	public void open(URL url) {
		MimeTypeViewer viewer = new MimeTypeViewer();
		viewer.openURL(url);
	}

	public void openWith(URL url) {
		MimeTypeViewer viewer = new MimeTypeViewer();
		viewer.openWithURL(url);
	}

	/*
	public void openWithBrowser(URL url) {
		MimeTypeViewer viewer = new MimeTypeViewer();
		viewer.openWithBrowserURL(url);
	}
	*/
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("COMPOSE")) {
			compose(getAddress());
		} else if (action.equals("CONTACT")) {
			contact(getAddress());
		} else if (action.equals("OPEN")) {
			open(getLink());
		} else if (action.equals("OPEN_WITH")) {
			openWith(getLink());
		} else if (action.equals("OPEN_WITHINTERNALBROWSER")) {
			//openWithBrowser(getLink());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5637.java