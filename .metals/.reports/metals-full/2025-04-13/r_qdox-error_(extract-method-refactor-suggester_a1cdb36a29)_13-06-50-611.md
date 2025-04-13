error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1543.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1543.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1543.java
text:
```scala
?@@ UIManager.getColor("Tree.foreground")

/*
 * FileCellRenderer.java - renders list and tree cells for the VFS browser
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 1999 Jason Ginchereau
 * Portions copyright (C) 2001 Slava Pestov
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.gjt.sp.jedit.browser;

//{{{ Imports
import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.border.*;
import org.gjt.sp.jedit.io.VFS;
import org.gjt.sp.jedit.*;
//}}}

public class FileCellRenderer extends DefaultTreeCellRenderer
{
	public static Icon fileIcon = GUIUtilities.loadIcon("file.gif");
	public static Icon dirIcon = GUIUtilities.loadIcon("closed_folder.gif");
	public static Icon openDirIcon = GUIUtilities.loadIcon("open_folder.gif");
	public static Icon filesystemIcon = GUIUtilities.loadIcon("drive.gif");
	public static Icon loadingIcon = GUIUtilities.loadIcon("drive.gif");

	//{{{ FileCellRenderer constructor
	public FileCellRenderer()
	{
		plainFont = UIManager.getFont("Tree.font");
		boldFont = plainFont.deriveFont(Font.BOLD);
		setBorder(new EmptyBorder(1,0,1,0));
	} //}}}

	//{{{ getTreeCellRendererComponent() method
	public Component getTreeCellRendererComponent(JTree tree, Object value,
		boolean sel, boolean expanded, boolean leaf, int row,
		boolean focus)
	{
		super.getTreeCellRendererComponent(tree,value,sel,expanded,
			leaf,row,focus);

		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)value;
		Object userObject = treeNode.getUserObject();
		if(userObject instanceof VFS.DirectoryEntry)
		{
			VFS.DirectoryEntry file = (VFS.DirectoryEntry)userObject;

			underlined = (jEdit.getBuffer(file.path) != null);

			setIcon(showIcons
				? getIconForFile(file,expanded)
				: null);
			setFont(file.type == VFS.DirectoryEntry.FILE
				? plainFont : boldFont);
			setText(file.name);

			if(!sel)
			{
				Color color = file.getColor();

				setForeground(color == null
					? tree.getForeground()
					: color);
			}
		}
		else if(userObject instanceof BrowserView.LoadingPlaceholder)
		{
			setIcon(showIcons ? loadingIcon : null);
			setFont(plainFont);
			setText(jEdit.getProperty("vfs.browser.tree.loading"));
			underlined = false;
		}
		else if(userObject instanceof String)
		{
			setIcon(showIcons ? dirIcon : null);
			setFont(boldFont);
			setText((String)userObject);
			underlined = false;
		}
		else
		{
			// userObject is null?
			setIcon(null);
			setText(null);
		}

		return this;
	} //}}}

	//{{{ paintComponent() method
	public void paintComponent(Graphics g)
	{
		if(underlined)
		{
			Font font = getFont();

			FontMetrics fm = getFontMetrics(getFont());
			int x = (getIcon() == null ? 0
				: getIcon().getIconWidth()
				+ getIconTextGap());
			g.setColor(getForeground());
			g.drawLine(x,fm.getAscent() + 2,
				x + fm.stringWidth(getText()),
				fm.getAscent() + 2);
		}

		super.paintComponent(g);
	} //}}}

	//{{{ Package-private members
	boolean showIcons;

	//{{{ propertiesChanged() method
	void propertiesChanged()
	{
		showIcons = jEdit.getBooleanProperty("vfs.browser.showIcons");
	} //}}}

	//}}}

	//{{{ Private members

	//{{{ Instance members
	private Font plainFont;
	private Font boldFont;

	private boolean underlined;
	//}}}

	//{{{ getIconForFile() method
	private Icon getIconForFile(VFS.DirectoryEntry file, boolean expanded)
	{
		if(file.type == VFS.DirectoryEntry.DIRECTORY)
			return (expanded ? openDirIcon : dirIcon);
		else if(file.type == VFS.DirectoryEntry.FILESYSTEM)
			return filesystemIcon;
		else
			return fileIcon;
	} //}}}

	//}}}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1543.java