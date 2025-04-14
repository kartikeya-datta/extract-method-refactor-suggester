error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3292.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3292.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3292.java
text:
```scala
I@@mageLoader.getSmallImageIcon("stock_search-16.png") ); //$NON-NLS-1$

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

package org.columba.mail.gui.composer;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;

import org.columba.core.gui.util.CMenu;
import org.columba.core.gui.util.CMenuItem;
import org.columba.core.gui.util.ImageLoader;
import org.columba.mail.util.MailResourceLoader;

/**
 * @author frd
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ComposerMenu extends JMenuBar{

	public ComposerMenu( ComposerInterface composerInterface )
	{
		JMenu fileMenu =
			new JMenu(MailResourceLoader.getString("menu","mainframe", "menu_file")); //$NON-NLS-1$ //$NON-NLS-2$
		fileMenu.setMnemonic(KeyEvent.VK_F);

		CMenuItem newMenuItem;
		newMenuItem =
			new CMenuItem(composerInterface.composerActionListener.newAction);
		fileMenu.add(newMenuItem);

		fileMenu.addSeparator();

		CMenuItem saveAsMenuItem;
		saveAsMenuItem =
			new CMenuItem(
				composerInterface.composerActionListener.saveAsAction);
		fileMenu.add(saveAsMenuItem);

		CMenuItem saveDraftMenuItem;
		saveDraftMenuItem =
			new CMenuItem(
				composerInterface.composerActionListener.saveDraftAction);
		fileMenu.add(saveDraftMenuItem);

		CMenuItem saveTemplateMenuItem;
		saveTemplateMenuItem =
			new CMenuItem(
				composerInterface.composerActionListener.saveTemplateAction);
		fileMenu.add(saveTemplateMenuItem);

		fileMenu.addSeparator();

		CMenuItem sendMenuItem;
		sendMenuItem =
			new CMenuItem(composerInterface.composerActionListener.sendAction);
		fileMenu.add(sendMenuItem);

		CMenuItem sendLaterMenuItem;
		sendLaterMenuItem =
			new CMenuItem(
				composerInterface.composerActionListener.sendLaterAction);
		fileMenu.add(sendLaterMenuItem);

		fileMenu.addSeparator();

		CMenuItem printMenuItem = new CMenuItem(MailResourceLoader.getString("menu","mainframe","menu_message_print")); //$NON-NLS-1$
		printMenuItem.setAccelerator(
			KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		printMenuItem.setMnemonic(KeyEvent.VK_P);
		printMenuItem.setEnabled(false);

		fileMenu.add(printMenuItem);

		fileMenu.addSeparator();

		CMenuItem exitMenuItem;
		exitMenuItem =
			new CMenuItem(composerInterface.composerActionListener.exitAction);
		fileMenu.add(exitMenuItem);

		JMenu editMenu =
			new JMenu(MailResourceLoader.getString("menu","mainframe","menu_edit")); //$NON-NLS-1$ //$NON-NLS-2$
		editMenu.setMnemonic(KeyEvent.VK_E);

		CMenuItem undoMenuItem;
		undoMenuItem =
			new CMenuItem(composerInterface.composerActionListener.undoAction);
		editMenu.add(undoMenuItem);

		CMenuItem redoMenuItem;
		redoMenuItem =
			new CMenuItem(composerInterface.composerActionListener.redoAction);
		editMenu.add(redoMenuItem);

		editMenu.addSeparator();

		CMenuItem cutMenuItem;
		cutMenuItem =
			new CMenuItem(composerInterface.composerActionListener.cutAction);
		editMenu.add(cutMenuItem);

		CMenuItem copyMenuItem;
		copyMenuItem =
			new CMenuItem(composerInterface.composerActionListener.copyAction);
		editMenu.add(copyMenuItem);

		CMenuItem pasteMenuItem;
		pasteMenuItem =
			new CMenuItem(composerInterface.composerActionListener.pasteAction);
		editMenu.add(pasteMenuItem);

		CMenuItem deleteMenuItem;
		deleteMenuItem =
			new CMenuItem(
				composerInterface.composerActionListener.deleteAction);
		editMenu.add(deleteMenuItem);

		editMenu.addSeparator();

		CMenuItem selectAllMenuItem;
		selectAllMenuItem =
			new CMenuItem(
				composerInterface.composerActionListener.selectAllAction);
		editMenu.add(selectAllMenuItem);

		editMenu.addSeparator();

		CMenuItem menuItem =
			new CMenuItem(
				MailResourceLoader.getString("menu","mainframe","menu_edit_find"), //$NON-NLS-1$ //$NON-NLS-2$
				ImageLoader.getSmallImageIcon("stock_search-16.png_8") ); //$NON-NLS-1$
		menuItem.setAccelerator(
			KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
		menuItem.setMnemonic(KeyEvent.VK_F);

		menuItem.setEnabled(false);
		editMenu.add(menuItem);

		menuItem =
			new CMenuItem(
				MailResourceLoader.getString("menu","composer","menu_edit_findagain")); //$NON-NLS-1$ //$NON-NLS-2$

		menuItem.setMnemonic(KeyEvent.VK_G);
		menuItem.setAccelerator(
			KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
		menuItem.setEnabled(false);
		editMenu.add(menuItem);

		editMenu.addSeparator();


        // 09/16/02 ALP
        // Added as part of external editor support
		menuItem =
			new CMenuItem(
                MailResourceLoader.getString("menu","composer","menu_edit_extern_edit"));

// 		menuItem.setMnemonic(KeyEvent.VK_G);
// 		menuItem.setAccelerator(
// 			KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
		menuItem.setEnabled(true);
		menuItem.setActionCommand("EXTERNEDIT");
// 		menuItem.addActionListener(
// 			MainInterface.mainFrame.getActionListener() );
		menuItem.addActionListener(composerInterface.composerActionListener.externEditAction);

		editMenu.add(menuItem);

		editMenu.addSeparator();
        // 09/16/02 ALP
        // End addition

		menuItem =
			new CMenuItem(
				MailResourceLoader.getString("menu","mainframe","menu_preferences_general")); //$NON-NLS-1$ //$NON-NLS-2$
		//ImageLoader.getImageIcon("", "Preferences16" ) );

		menuItem.setActionCommand("GENERAL_OPTIONS"); //$NON-NLS-1$
		menuItem.addActionListener(
		composerInterface.composerActionListener );
		editMenu.add(menuItem);

		JMenu viewMenu = new JMenu(MailResourceLoader.getString("menu","mainframe","menu_view")); //$NON-NLS-1$
		viewMenu.setMnemonic(KeyEvent.VK_V);

		CMenu subMenu = new CMenu(MailResourceLoader.getString("menu","mainframe","menu_view_show_hide")); //$NON-NLS-1$

		JCheckBoxMenuItem cbMenuItem = new JCheckBoxMenuItem(MailResourceLoader.getString("menu","mainframe","menu_view_showtoolbar")); //$NON-NLS-1$
		cbMenuItem.addActionListener(composerInterface.composerActionListener);
		cbMenuItem.setActionCommand("VIEW_TOOLBAR"); //$NON-NLS-1$
		cbMenuItem.setEnabled(false);
		if (composerInterface.viewItem.getBoolean("toolbars","show_main") )
			cbMenuItem.setSelected(true);
		subMenu.add(cbMenuItem);

		cbMenuItem = new JCheckBoxMenuItem(MailResourceLoader.getString("menu","composer","menu_account_info_panel")); //$NON-NLS-1$
		cbMenuItem.addActionListener(composerInterface.composerActionListener);
		cbMenuItem.setActionCommand("VIEW_ACCOUNTINFO"); //$NON-NLS-1$
		cbMenuItem.setEnabled(false);
		if (composerInterface.viewItem.getBoolean("toolbars","show_folderinfo") )
			cbMenuItem.setSelected(true);
		subMenu.add(cbMenuItem);

		viewMenu.add(subMenu);

		viewMenu.addSeparator();

		cbMenuItem = new JCheckBoxMenuItem(MailResourceLoader.getString("menu","composer","menu_addressbook_panel")); //$NON-NLS-1$
		cbMenuItem.setActionCommand("VIEW_ADDRESSBOOK"); //$NON-NLS-1$
		cbMenuItem.addActionListener(composerInterface.composerActionListener);
		if (composerInterface.viewItem.getBoolean("addressbook","enabled") )
			cbMenuItem.setSelected(true);
		viewMenu.add(cbMenuItem);

		JMenu messageMenu =
			new JMenu(MailResourceLoader.getString("menu","mainframe","menu_message")); //$NON-NLS-1$ //$NON-NLS-2$
		messageMenu.setMnemonic(KeyEvent.VK_M);

		CMenuItem addressbookMessageMenuItem;
		addressbookMessageMenuItem =
			new CMenuItem(
				composerInterface.composerActionListener.addressbookAction);
		messageMenu.add(addressbookMessageMenuItem);

		CMenuItem attachFileMenuItem;
		attachFileMenuItem =
			new CMenuItem(
				composerInterface.composerActionListener.attachFileAction);
		messageMenu.add(attachFileMenuItem);

		CMenuItem spellCheckMenuItem;
		spellCheckMenuItem =
			new CMenuItem(
				composerInterface.composerActionListener.spellCheckAction);
		messageMenu.add(spellCheckMenuItem);
		
		messageMenu.addSeparator();
		
		messageMenu.add( composerInterface.charsetManager.createMenu(null) );

		JMenu securityMenu = new JMenu(MailResourceLoader.getString("menu","composer","menu_security")); //$NON-NLS-1$

		JCheckBoxMenuItem signMenuItem =
			new JCheckBoxMenuItem(
				composerInterface.composerActionListener.signAction);
		
		signMenuItem.setSelected(false);
		signMenuItem.setEnabled(false);
		securityMenu.add(signMenuItem);

		JCheckBoxMenuItem encryptMenuItem =
			new JCheckBoxMenuItem(
				composerInterface.composerActionListener.encryptAction);
		encryptMenuItem.setSelected(false);
		encryptMenuItem.setEnabled(false);
		securityMenu.add(encryptMenuItem);

		composerInterface.accountController.setSecurityMenuItems(signMenuItem, encryptMenuItem);
		
		add(fileMenu);
		add(editMenu);
		add(viewMenu);
		add(messageMenu);
		add(securityMenu);

		
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3292.java