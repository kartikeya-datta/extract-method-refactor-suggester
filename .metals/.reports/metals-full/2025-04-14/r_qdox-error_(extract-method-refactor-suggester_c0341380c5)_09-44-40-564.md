error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3455.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3455.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3455.java
text:
```scala
h@@tmlViewer = bodyPart.getHeader().contentSubtype.equalsIgnoreCase("html");

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

package org.columba.mail.gui.message;

import java.awt.Font;
import java.io.UnsupportedEncodingException;

import javax.swing.JButton;
import javax.swing.JPopupMenu;

import org.columba.core.command.Command;
import org.columba.core.config.Config;
import org.columba.main.MainInterface;
import org.columba.mail.coder.CoderRouter;
import org.columba.mail.coder.Decoder;
import org.columba.mail.command.FolderCommandReference;
import org.columba.mail.config.MailConfig;
import org.columba.mail.folder.Folder;
import org.columba.mail.folder.FolderTreeNode;
import org.columba.mail.gui.header.HeaderView;
import org.columba.mail.gui.frame.MailFrameController;
import org.columba.mail.gui.message.action.MessageActionListener;
import org.columba.mail.gui.message.action.MessageFocusListener;
import org.columba.mail.gui.message.action.MessagePopupListener;
import org.columba.mail.gui.message.command.ViewMessageCommand;
import org.columba.mail.gui.message.menu.MessageMenu;
import org.columba.mail.gui.table.MessageSelectionListener;
import org.columba.mail.message.MimePart;

/**
 * this class shows the messagebody
 */

public class MessageController implements MessageSelectionListener//implements CharsetListener
{
	private HeaderView messageHeader;

	private Folder folder;
	private Object uid;

	

	private MessageMenu menu;

	private MessageFocusListener focusListener;

	private MessagePopupListener popupListener;

	private JButton button;

	private String activeCharset;

	private MessageView view;
	private MessageActionListener actionListener;

	protected MailFrameController mailFrameController;

	//private MessageSelectionManager messageSelectionManager;
	
	public MessageController(MailFrameController mailFrameController) {
	
		this.mailFrameController = mailFrameController;

		activeCharset = "auto";

		view = new MessageView();

		//messageSelectionManager = new MessageSelectionManager();
		
		actionListener = new MessageActionListener(this);

		String[] keys = new String[4];
		keys[0] = new String("Subject");
		keys[1] = new String("From");
		keys[2] = new String("Date");
		keys[3] = new String("To");

		Font mainFont =
			new Font(
				Config.getOptionsConfig().getThemeItem().getMainFontName(),
				Font.PLAIN,
				Config.getOptionsConfig().getThemeItem().getMainFontSize());

		messageHeader = new HeaderView(keys, 2);

		menu = new MessageMenu(this);

		/*
		setLayout(new BorderLayout());
		
		//focusListener = new MessageFocusListener();
		popupListener = new MessagePopupListener(this);
		
		documentOperator = new MessageView(this);
		documentOperator.setPopupListener(popupListener);
		
		add(documentOperator, BorderLayout.CENTER);
		
		String[] keys = new String[4];
		keys[0] = new String("Subject");
		keys[1] = new String("From");
		keys[2] = new String("Date");
		keys[3] = new String("To");
		
		Font mainFont =
			new Font(
				Config.getOptionsConfig().getThemeItem().getMainFontName(),
				Font.PLAIN,
				Config.getOptionsConfig().getThemeItem().getMainFontSize());
		
		messageHeader = new HeaderView(keys, 2);
		
		add(messageHeader, BorderLayout.NORTH);
		
		actionListener = new MessageActionListener(this);
		
		menu = new MessageMenu(this);
		
		MainInterface.focusManager.registerComponent( new MessageFocusOwner(this) );
		
		button = new JButton("test - knopf");
		
		*/
	}
	
	/*
	public void setSelectionManager( SelectionManager m )
	{
		this.selectionManager = m;
		
		selectionManager.addMessageSelectionListener(this);
	}
	*/
	
	public void messageSelectionChanged( Object[] newUidList )
	{
		System.out.println("received new message-selection changed event");
		
		FolderCommandReference[] reference = (FolderCommandReference[]) MainInterface.frameController.tableController.getTableSelectionManager().getSelection();
		
		FolderTreeNode treeNode = reference[0].getFolder();
		Object[] uids = reference[0].getUids();
		
		// this is no message-viewing action,
		// but a selection of multiple messages
		if ( uids.length > 1 ) return;
		
		MainInterface.frameController.attachmentController.getAttachmentSelectionManager().setFolder(treeNode);
		MainInterface.frameController.attachmentController.getAttachmentSelectionManager().setUids(uids);
		
		MainInterface.processor.addOp(
			new ViewMessageCommand(
				mailFrameController,
				reference));
			
		
		/*
		MainInterface.crossbar.operate(
				new GuiOperation(Operation.MESSAGEBODY, 4, (Folder) selectionManager.getFolder(), newUidList[0]));
				*/
	}
	
	public MessageActionListener getActionListener() {
		return actionListener;
	}

	public MessageView getView() {

		//new MessageActionListener( view );

		return view;
	}

	

	public JPopupMenu getPopupMenu() {
		return menu.getPopupMenu();
	}

	/*
	public void update(boolean b)
	{
		if (b == true)
		{
			if (view != null)
				remove(view);
	
			add(view, BorderLayout.CENTER);
		}
	}
	*/

	public void createTextPane(boolean b, boolean htmlViewer) {

		if (b == true) {
			if (htmlViewer == true) {
				// use the html viewer

				view.enableViewer(MessageView.HTML);

				/*
				if (view != null)
					remove(view);
				
				add(view, BorderLayout.CENTER);
				*/

			} else {
				// use the advanced viewer
				/*
				view.enableViewer(MessageView.ADVANCED);
				
				if (view != null)
					remove(view);
				
				add(view, BorderLayout.CENTER);
				*/

			}
		} else {
			// use plain text viewer ( fast )

			view.enableViewer(MessageView.SIMPLE);

			/*
			if (view != null)
			remove(view);
			
			add(view, BorderLayout.CENTER);
			*/

		}

	}

	public void setViewerFont(Font font) {
		//textPane.setFont( font );
	}

	public void resetRenderer() {
		messageHeader.resetRenderer();
	}

	public Object getUid() {
		return uid;
	}

	public Folder getFolder() {
		return folder;
	}

	public void setFolder(Folder f) {
		this.folder = folder;
	}

	public void setUid(Object o) {
		this.uid = o;
	}

	public void showMessage(MimePart bodyPart) throws Exception {

		boolean htmlViewer = false;

		

		// Which Charset shall we use ?

		String charset;

		if (activeCharset.equals("auto"))
			charset = bodyPart.getHeader().getContentParameter("charset");
		else
			charset = activeCharset;

		Decoder decoder =
			CoderRouter.getDecoder(
				bodyPart.getHeader().contentTransferEncoding);

		// Shall we use the HTML-Viewer?

		htmlViewer = bodyPart.getHeader().contentSubtype.equals("html");

		// Update the MessageHeaderPane
		/*
		messageHeader.setValues(message);
		*/
		
		String decodedBody = null;

		// Decode the Text using the specified Charset				
		try {
			decodedBody = decoder.decode(bodyPart.getBody(), charset);
		} catch (UnsupportedEncodingException ex) {
			// If Charset not supported fall back to standard Charset

			try {
				decodedBody = decoder.decode(bodyPart.getBody(), null);
			} catch (UnsupportedEncodingException never) {

			}
		}

		// Show message in the MessageViewer

		insertText(decodedBody, htmlViewer);

	}

	/*
	
	public void setSecurityIndicator(int value)
	{
		messageHeader.setSecurityValue(value);
	}
	*/
	
	public void showMessageSource(String rawText)
	{
		insertText(rawText, false);
		view.setCaretPosition(0);
	}
	/*
	 * 
	public MessageActionListener getActionListener()
	{
		return actionListener;
	}
	
	public MessageFocusListener getFocusListener()
	{
		return focusListener;
	}
	
	public String getAddress()
	{
		HyperlinkTextViewer viewer =
			(HyperlinkTextViewer) view.getViewer(MessageView.ADVANCED);
	
		if (viewer != null)
			return viewer.getAddress();
		else
			return new String();
	}
	
	public String getLink()
	{
		HyperlinkTextViewer viewer =
			(HyperlinkTextViewer) view.getViewer(MessageView.ADVANCED);
	
		if (viewer != null)
			return viewer.getLink();
		else
			return new String();
	}
	*/

	protected void insertText(String s, boolean htmlView) {
		boolean advanced =
			MailConfig
				.getMainFrameOptionsConfig()
				.getWindowItem()
				.getAdvancedViewer();

		if (htmlView == true) {

			boolean b = getView().enableViewer(MessageView.HTML);
			//update(b);
			getView().setDoc(s);
		} else if (advanced == true) {

			boolean b =
				getView().enableViewer(MessageView.ADVANCED);
			//update(b);
			getView().setDoc(s);

			//getView().setHeader( button );

		} else {

			boolean b = getView().enableViewer(MessageView.SIMPLE);
			//update(b);
			getView().setDoc(s);

		}

	}
	/*	
	public void charsetChanged( CharsetEvent e ) {
		activeCharset = e.getValue();				
	}
	*/

	/*
	protected void scrollToBegin()
	{
		Runnable run = new Runnable()
		{
			public void run()
			{
				view.setCaretPosition(0);
			}
		};
		try
		{
			if (!SwingUtilities.isEventDispatchThread())
				SwingUtilities.invokeAndWait(run);
			else
				SwingUtilities.invokeLater(run);
	
		}
		catch (Exception ex)
		{
		}
	
	}
	*/

	/********************* context menu *******************************************/

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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3455.java