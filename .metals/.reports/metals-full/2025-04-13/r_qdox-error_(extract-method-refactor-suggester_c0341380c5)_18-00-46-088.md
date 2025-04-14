error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3747.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3747.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3747.java
text:
```scala
f@@ileChooser.setDialogTitle(MailResourceLoader.getString("menu","composer","menu_message_attachFile")); //$NON-NLS-1$

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

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;

import javax.swing.JFileChooser;

import org.columba.mail.message.ComposerAttachment;
import org.columba.mail.message.MimeHeader;
import org.columba.mail.message.MimePart;
import org.columba.mail.util.MailResourceLoader;

/**
 * @author frd
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class AttachmentController implements KeyListener {
	AttachmentView view;
	ComposerController controller;

	AttachmentActionListener actionListener;
	AttachmentMenu menu;

	private JFileChooser fileChooser;

	public AttachmentController(ComposerController controller) {
		this.controller = controller;

		view = new AttachmentView(this);

		actionListener = new AttachmentActionListener(this);

		menu = new AttachmentMenu(this);

		view.addPopupListener(new PopupListener());

		fileChooser = new JFileChooser();
	}

	public ActionListener getActionListener() {
		return actionListener;
	}

	public void installListener() {
		view.installListener(this);
	}

	public void updateComponents(boolean b) {
		if (b) {
			for (int i = 0; i < controller.getModel().getAttachments().size(); i++) {
				MimePart p = (MimePart) controller.getModel().getAttachments().get(i);
				view.add( p);
			}
		} else {
			controller.getModel().getAttachments().clear();
			
			for ( int i=0; i<view.count(); i++ ) {
				MimePart mp = view.get(i);
				controller.getModel().getAttachments().add(mp);
			}
		}
	}

	public void add(MimePart part) {
		view.add(part);
		((ComposerModel)controller.getModel()).getAttachments().add(part);
	}

	public void removeSelected() {
		Object[] mp = view.getSelectedValues();
		for ( int i=0; i<mp.length; i++ ) {
			view.remove( (MimePart) mp[i] );
		}
		
	}

	public void addFileAttachment() {
		int returnValue;
		File[] files;

		fileChooser.setDialogTitle(MailResourceLoader.getString("menu","mainframe","composer_attach_file")); //$NON-NLS-1$
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(true);
		returnValue = fileChooser.showOpenDialog(view);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			files = fileChooser.getSelectedFiles();

			for (int i = 0; i < files.length; i++) {
				File file = files[i];

				FileNameMap fileNameMap = URLConnection.getFileNameMap();
				String mimetype = fileNameMap.getContentTypeFor(file.getName());
				if (mimetype == null) mimetype = "application/octet-stream"; //REALLY NEEDED?

				MimeHeader header =
					new MimeHeader(
						mimetype.substring(0, mimetype.indexOf('/')),
						mimetype.substring(mimetype.indexOf('/') + 1));

				ComposerAttachment mimePart = new ComposerAttachment(header, file);

				view.add(mimePart);
			}
		}
	}

	/******************* KeyListener ****************************/

	public void keyPressed(KeyEvent k) {
		switch (k.getKeyCode()) {
			case (KeyEvent.VK_DELETE) :
				if (view.count() > 0)
					removeSelected();
				break;
		}
	}

	public void keyReleased(KeyEvent k) {}

	public void keyTyped(KeyEvent k) {}

	/********************** MouseListener *****************************/

	class PopupListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {

				Object[] values = view.getSelectedValues();
				if (values.length == 0)
					view.fixSelection(e.getX(), e.getY());

				menu.show(e.getComponent(), e.getX(), e.getY());
			}
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3747.java