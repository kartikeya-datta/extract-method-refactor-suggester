error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9596.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9596.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9596.java
text:
```scala
e@@vent = new CharsetEvent( this, charsetId, charsets[charsetId]);

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

package org.columba.core.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.lang.reflect.Array;
import java.util.Vector;

import javax.swing.JMenu;

import org.columba.core.gui.util.CMenu;
import org.columba.core.gui.util.CMenuItem;
import org.columba.mail.util.MailResourceLoader;

/**
 * @author -
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class CharsetManager implements ActionListener{

	private static final String[] charsets = {
		// Auto
		"auto",
		// Global # 1
		"UTF-8", "UTF-16", "ASCII",

		// West Europe # 4
		"Cp1252",
			"ISO-8859-1",
			"ISO-8859-15",
			"Cp850",
			"MacRoman",
			"ISO-8859-7",
			"MacGreek",
			"Cp1253",
			"MacIceland",
			"ISO-8859-3",

		// East Europe # 14
		"ISO-8859-4",
			"ISO-8859-13",
			"Cp1257",
			"Cp852",
			"ISO-8859-2",
			"MacCentralEurope",
			"MacCroatian",
			"Cp855",
			"ISO-8859-5",
			"KOI8_R",
			"MacCyrillic",
			"Cp1251",
			"Cp866",
			"MacUkraine",
			"MacRomania",

		// East Asian # 29
		"EUC_CN",
		"GBK",
		"GB18030",
		"Big5",
		"Big5_HKSCS",
		"EUC_TW",
		"EUC_JP",
		"SJIS",
		"EUC_KR",
		"JOHAB",
		"ISO2022KR",
		
		// West Asian # 40
		"TIS620",
		"Cp857",
		"ISO-8859-9",
		"MacTurkish",
		"Cp1254",
		"Cp1258"
		
		// # 46
	};

	private static final String[] groups = { "global", "westeurope", "easteurope", "eastasian", "seswasian"};

	private static final int[] groupOffset = { 1, 4, 14, 29, 40, 46 };

	private Vector listeners;
	
	private CharsetMenuItem selectedMenuItem;
	private int selectedId;

	public CharsetManager() {
		listeners = new Vector();
		selectedId = 0;	// Todo: Make the menu remember its last setting
	}

	public JMenu createMenu(MouseListener handler) {
		JMenu subMenu, subsubMenu;
		CharsetMenuItem menuItem;

		int groupSize = Array.getLength(groups);
		int charsetSize = Array.getLength(charsets);

		subMenu =
			new CMenu(MailResourceLoader.getString("menu","mainframe", "menu_view_charset"));


		selectedMenuItem = new CharsetMenuItem( 
				MailResourceLoader.getString("menu","mainframe", "menu_view_charset_"+charsets[0]),
				-1, 0, charsets[0]);
		
		selectedMenuItem.addMouseListener(handler);

		subMenu.add(selectedMenuItem);

		subMenu.addSeparator();


		menuItem =
			new CharsetMenuItem(
				MailResourceLoader.getString("menu","mainframe", "menu_view_charset_"+charsets[0]),
				-1, 0, charsets[0]);
		menuItem.addMouseListener(handler);
		menuItem.addActionListener( this );


		subMenu.add(menuItem);

		// Automatic Generation of Groups

		for (int i = 0; i < groupSize; i++) {
			subsubMenu =
				new CMenu(
					MailResourceLoader.getString(
						"menu","mainframe",
						"menu_view_charset_" + groups[i]));
			subMenu.add(subsubMenu);

			for (int j = groupOffset[i]; j < groupOffset[i + 1]; j++) {
				menuItem =
					new CharsetMenuItem(
						MailResourceLoader.getString(
							"menu","mainframe",
							"menu_view_charset_"+charsets[j]),
						-1,
						j, charsets[j]);
				menuItem.addMouseListener(handler);
				menuItem.addActionListener( this );
				subsubMenu.add(menuItem);
			}
		}

		return subMenu;
	}

	/**
	 * Adds a CharcterCodingListener.
	 * @param listener The listener to set
	 */
	public void addCharsetListener(CharsetListener listener) {
		if( !listeners.contains(listener) )
			listeners.add( listener );
	}
	
	public void actionPerformed( ActionEvent e ) {
		CharsetEvent event;

		int charsetId = ((CharsetMenuItem)e.getSource()).getId();

		event = new CharsetEvent( charsetId,  charsets[charsetId]);
			
		selectedMenuItem.setText( MailResourceLoader.getString(
							"menu","mainframe",
							"menu_view_charset_"+charsets[event.getId()]) );
		
		for( int i=0; i<listeners.size(); i++ ) 
			((CharsetListener)listeners.get(i)).charsetChanged(event);		
	}

}

class CharsetMenuItem extends CMenuItem{
		
	int id;
	String javaCodingName;
	
	public CharsetMenuItem( String name, int i, int id, String javaCodingName) {
		//super( name, i );
		super(name);
		
		this.id = id;
		this.javaCodingName = javaCodingName;
	}

	/**
	 * Returns the javaCodingName.
	 * @return String
	 */
	public String getJavaCodingName() {
		return javaCodingName;
	}

	/**
	 * Sets the javaCodingName.
	 * @param javaCodingName The javaCodingName to set
	 */
	public void setJavaCodingName(String javaCodingName) {
		this.javaCodingName = javaCodingName;
	}

	/**
	 * Returns the id.
	 * @return int
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * @param id The id to set
	 */
	public void setId(int id) {
		this.id = id;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9596.java