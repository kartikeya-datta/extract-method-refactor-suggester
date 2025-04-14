error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9718.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9718.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9718.java
text:
```scala
c@@ontroller.getModel().setAccountItem(config.get(i));

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
package org.columba.mail.gui.composer;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBoxMenuItem;

import org.columba.mail.config.AccountItem;
import org.columba.mail.config.AccountList;
import org.columba.mail.config.MailConfig;

/**
 * @author frd
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class AccountController implements ItemListener {
	AccountView view;
	ComposerController controller;

	JCheckBoxMenuItem signMenuItem;
	JCheckBoxMenuItem encryptMenuItem;

	public AccountController(ComposerController controller) {
		this.controller = controller;
		
		view = new AccountView(this);
		
		AccountList config = MailConfig.getAccountList();

		for (int i = 0; i < config.count(); i++) {
			view.addItem(config.get(i));
			if (i == 0) {
				view.setSelectedItem(config.get(i));
				((ComposerModel)controller.getModel()).setAccountItem(config.get(i));
			}
		}

		view.addItemListener(this);
	}

	/*
	public void setSecurityMenuItems(
		JCheckBoxMenuItem signItem,
		JCheckBoxMenuItem encryptItem) {
		signMenuItem = signItem;
		encryptMenuItem = encryptItem;

		AccountItem item = (AccountItem) view.getSelectedItem();

		PGPItem pgpItem = item.getPGPItem();
		if( pgpItem.getBoolean("enabled") ) {
			signMenuItem.setEnabled(true);
			encryptMenuItem.setEnabled(true);
			
			model.setSignMessage(pgpItem.getBoolean("always_sign"));
			model.setEncryptMessage(pgpItem.getBoolean("always_encrypt"));
		}
	}
*/

	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			updateComponents(false);
			/*
			AccountItem item = (AccountItem) view.getSelectedItem();
			composerInterface.identityInfoPanel.set(item);
			
			PGPItem pgpItem = item.getPGPItem();
			signMenuItem.setEnabled(pgpItem.getBoolean("enabled"));
			signMenuItem.setSelected(pgpItem.getBoolean("always_sign"));

			encryptMenuItem.setEnabled(pgpItem.getBoolean("enabled"));
			encryptMenuItem.setSelected(pgpItem.getBoolean("always_encrypt"));
			*/
		}
	}

	public void updateComponents(boolean b) {
		if (b == true) {
			view.setSelectedItem(((ComposerModel)controller.getModel()).getAccountItem());
			
			/*
			encryptMenuItem.setSelected(model.isEncryptMessage());
			signMenuItem.setSelected(model.isSignMessage());
			*/
		} else {
			((ComposerModel)controller.getModel()).setAccountItem((AccountItem) view.getSelectedItem());
			
			/*
			model.setSignMessage(signMenuItem.isSelected());
			model.setEncryptMessage(encryptMenuItem.isSelected());
			*/
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9718.java