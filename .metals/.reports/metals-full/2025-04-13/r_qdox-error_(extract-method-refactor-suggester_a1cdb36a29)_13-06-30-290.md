error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8606.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8606.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8606.java
text:
```scala
.@@getString("org.columba.core.i18n.dialog", "error", "no_browser"), "Error",

//The contents of this file are subject to the Mozilla Public License Version 1.1
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
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003. 
//
//All Rights Reserved.

package org.columba.core.gui.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.columba.core.util.GlobalResourceLoader;
import org.jdesktop.jdic.desktop.Desktop;
import org.jdesktop.jdic.desktop.DesktopException;

public class URLController implements ActionListener {
    private String address;
    private URL link;

    //TODO (@author fdietz): i18n
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

    //TODO (@author fdietz): i18n
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

    /**
     * Composer message for recipient
     * 
     * @param address	email address of recipient
     */
    public void compose(String address) {
    	//IServiceManager.getInstance().createService("");
    	
       // TODO: implement this
    }

    /**
     * Add contact to addressbook.
     * 
     * @param address		new email address
     */
    public void contact(String address) {
    	// TODO: implement this
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
    	try {
			Desktop.browse(url);
		} catch (DesktopException e) {
			JOptionPane.showMessageDialog(null, GlobalResourceLoader
					.getString("dialog", "error", "no_browser"), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
    }

    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        if (action.equals("COMPOSE")) {
            compose(getAddress());
        } else if (action.equals("CONTACT")) {
            contact(getAddress());
        } else if (action.equals("OPEN")) {
            open(getLink());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8606.java