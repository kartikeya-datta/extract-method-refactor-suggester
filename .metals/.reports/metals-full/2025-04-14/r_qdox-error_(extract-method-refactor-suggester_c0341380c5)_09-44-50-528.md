error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2391.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2391.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2391.java
text:
```scala
i@@f (getView().countSelected() <= 1) {

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

package org.columba.mail.gui.attachment;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import org.columba.mail.gui.attachment.action.OpenAction;
import org.columba.mail.gui.attachment.util.IconPanel;
import org.columba.mail.gui.frame.MailFrameController;
import org.columba.mail.message.MimePart;
import org.columba.mail.message.MimePartTree;

/**
 * This class shows the attachmentlist
 *
 *
 * @version 1.0
 * @author Timo Stich
 */

public class AttachmentController {

	public JScrollPane scrollPane;

	private IconPanel attachmentPanel;

	//private int actIndex;
	//private Object actUid;

	//private TempFolder subMessageFolder;

	//private boolean inline;

	private AttachmentMenu menu;
	//private AttachmentActionListener actionListener;

	private AttachmentView view;
	private AttachmentModel model;


	private MailFrameController mailFrameController;

	public AttachmentController(MailFrameController superController) {
		super();

		this.mailFrameController = superController;

		model = new AttachmentModel();

		view = new AttachmentView(model);

		//attachmentSelectionManager = new AttachmentSelectionManager();
		mailFrameController.getSelectionManager().addSelectionHandler( new AttachmentSelectionHandler(view));

		//actionListener = new AttachmentActionListener(this);

		menu = new AttachmentMenu(superController);

		getView().setDoubleClickAction(new OpenAction(superController));

		MouseListener popupListener = new PopupListener();
		getView().addMouseListener(popupListener);

		/*
		attachmentPanel = new IconPanel();
		attachmentPanel.setDoubleClickActionCommand("OPEN");
		attachmentPanel.addActionListener(getActionListener());
		attachmentPanel.addMouseListener(popupListener);
		*/

		/*
			scrollPane = new JScrollPane(attachmentPanel);
			scrollPane.getViewport().setBackground(Color.white);
			//attachmentPanel.setPreferredSize(new Dimension(600, 50));
			 */
	}

	public MailFrameController getFrameController()	{
		return mailFrameController;
	}

	public AttachmentView getView() {
		return view;
	}

	public AttachmentModel getModel() {
		return model;
	}

	public boolean setMimePartTree(MimePartTree collection) {
		return getView().setMimePartTree(collection);
	}

	/*
	public void openWith(MimePart part, File tempFile) {
		MimeHeader header = part.getHeader();

		MimeTypeViewer viewer = new MimeTypeViewer();
		viewer.openWith(header, tempFile);

	}
	*/
	
	/*
	public void open(MimePart part, File tempFile) {
		MimeHeader header = part.getHeader();

		if (header.getContentType().toLowerCase().indexOf("message") != -1) {
			inline = true;
			openInlineMessage(part, tempFile);
		} else {
			inline = false;
			MimeTypeViewer viewer = new MimeTypeViewer();
			viewer.open(header, tempFile);
		}

	}
	*/
	
	protected void openInlineMessage(MimePart part, File tempFile) {

		/*
		subMessageFolder = new TempFolder();
		uid = null;
		
		Message subMessage = (Message) part.getContent();
		try {
			uid = subMessageFolder.add(subMessage, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		MainInterface.crossbar.operate(
			new GuiOperation(Operation.MESSAGEBODY, 4, subMessageFolder, uid));
		*/
	}

	private int countSelected() {
		return attachmentPanel.countSelected();
	}

	public int[] getSelection() {
		return attachmentPanel.getSelection();
	}

	private JPopupMenu getPopupMenu() {
		return menu;
	}

	class PopupListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				if (getView().countSelected() == 0) {
					getView().select(e.getPoint(), 0);
				}
				getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	/**
	 * Returns the mailFrameController.
	 * @return MailFrameController
	 */
	public MailFrameController getMailFrameController() {
		return mailFrameController;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2391.java