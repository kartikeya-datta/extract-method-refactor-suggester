error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7699.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7699.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7699.java
text:
```scala
S@@tring extAttr = model.getExtendedAttribute(column);

/*
 * FileCellRenderer.java - renders table cells for the VFS browser
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 1999 Jason Ginchereau
 * Portions copyright (C) 2001, 2003 Slava Pestov
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
import java.awt.font.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import org.gjt.sp.jedit.io.VFS;
import org.gjt.sp.jedit.io.VFSFile;
import org.gjt.sp.jedit.*;
//}}}

public class FileCellRenderer extends DefaultTableCellRenderer
{
	public static Icon fileIcon = GUIUtilities.loadIcon("File.png");
	public static Icon openFileIcon = GUIUtilities.loadIcon("OpenFile.png");
	public static Icon dirIcon = GUIUtilities.loadIcon("Folder.png");
	public static Icon openDirIcon = GUIUtilities.loadIcon("OpenFolder.png");
	public static Icon filesystemIcon = GUIUtilities.loadIcon("DriveSmall.png");
	public static Icon loadingIcon = GUIUtilities.loadIcon("ReloadSmall.png");

	//{{{ FileCellRenderer constructor
	public FileCellRenderer()
	{
		plainFont = UIManager.getFont("Tree.font");
		if(plainFont == null)
			plainFont = jEdit.getFontProperty("metal.secondary.font");
		boldFont = plainFont.deriveFont(Font.BOLD);
	} //}}}

	//{{{ getTableCellRendererComponent() method
	public Component getTableCellRendererComponent(JTable table,
		Object value, boolean isSelected, boolean hasFocus, 
		int row, int column)
	{
		super.getTableCellRendererComponent(table,value,isSelected,
			hasFocus,row,column);

		if(value instanceof VFSDirectoryEntryTableModel.Entry)
		{
			VFSDirectoryEntryTableModel.Entry entry =
				(VFSDirectoryEntryTableModel.Entry)value;
			VFSFile file = entry.dirEntry;

			setFont(file.getType() == VFSFile.FILE
				? plainFont : boldFont);

			this.isSelected = isSelected;
			this.file = file;

			if(column == 0)
			{
				// while its broken to have a null
				// symlinkPath, some older plugins
				// might...
				String path;
				if(file.getSymlinkPath() == null)
					path = file.getPath();
				else
					path = file.getSymlinkPath();
				openBuffer = (jEdit._getBuffer(path) != null);

				setIcon(showIcons
					? getIconForFile(file,entry.expanded,
					openBuffer) : null);
				setText(file.getName());

				int state;
				if(file.getType() == VFSFile.FILE)
					state = ExpansionToggleBorder.STATE_NONE;
				else if(entry.expanded)
					state = ExpansionToggleBorder.STATE_EXPANDED;
				else
					state = ExpansionToggleBorder.STATE_COLLAPSED;

				setBorder(new ExpansionToggleBorder(
					state,entry.level));
			}
			else
			{
				VFSDirectoryEntryTableModel model = (VFSDirectoryEntryTableModel)table.getModel();
				String extAttr = model.getExtendedAttribute(column - 1);

				openBuffer = false;
				setIcon(null);
				setText(file.getExtendedAttribute(extAttr));
				setBorder(new EmptyBorder(1,1,1,1));
			}
		}

		return this;
	} //}}}

	//{{{ paintComponent() method
	public void paintComponent(Graphics g)
	{
		if(!isSelected)
		{
			Color color = file.getColor();

			setForeground(color == null
				? UIManager.getColor("Tree.foreground")
				: color);
		}

		super.paintComponent(g);

		if(openBuffer)
		{
			Font font = getFont();

			FontMetrics fm = getFontMetrics(font);
			int x, y;
			if(getIcon() == null)
			{
				x = 0;
				y = fm.getAscent() + 2;
			}
			else
			{
				x = getIcon().getIconWidth() + getIconTextGap();
				y = Math.max(fm.getAscent() + 2,16);
			}

			Insets border = getBorder().getBorderInsets(this);
			x += border.left;

			g.setColor(getForeground());
			g.drawLine(x,y,x + fm.stringWidth(getText()),y);
		}
	} //}}}

	//{{{ getIconForFile() method
	/**
	 * @since jEdit 4.3pre2
	 */
	public static Icon getIconForFile(VFSFile file,
		boolean expanded)
	{
		return getIconForFile(file,expanded,
			jEdit._getBuffer(file.getSymlinkPath()) != null);
	} //}}}

	//{{{ getIconForFile() method
	public static Icon getIconForFile(VFSFile file,
		boolean expanded, boolean openBuffer)
	{
		if(file.getType() == VFSFile.DIRECTORY)
			return (expanded ? openDirIcon : dirIcon);
		else if(file.getType() == VFSFile.FILESYSTEM)
			return filesystemIcon;
		else if(openBuffer)
			return openFileIcon;
		else
			return fileIcon;
	} //}}}

	//{{{ Package-private members
	Font plainFont;
	Font boldFont;
	boolean showIcons;

	//{{{ propertiesChanged() method
	void propertiesChanged()
	{
		showIcons = jEdit.getBooleanProperty("vfs.browser.showIcons");
	} //}}}

	//{{{ getEntryWidth() method
	int getEntryWidth(VFSDirectoryEntryTableModel.Entry entry,
		Font font, FontRenderContext fontRenderContext)
	{
		String name = entry.dirEntry.getName();
		int width = (int)font.getStringBounds(name,fontRenderContext)
			.getWidth();
		width += ExpansionToggleBorder.ICON_WIDTH
			+ entry.level * ExpansionToggleBorder.LEVEL_WIDTH
			+ 3;
		if(showIcons)
		{
			width += fileIcon.getIconWidth();
			width += getIconTextGap();
		}
		return width;
	} //}}}

	//}}}

	//{{{ Private members
	private boolean openBuffer;
	private boolean isSelected;
	private VFSFile file;
	//}}}

	//{{{ ExpansionToggleBorder class
	static class ExpansionToggleBorder implements Border
	{
		static final Icon COLLAPSED_ICON;
		static final Icon EXPANDED_ICON;
		static final int ICON_WIDTH;

		static final int LEVEL_WIDTH = 15;

		static final int STATE_NONE = 0;
		static final int STATE_COLLAPSED = 1;
		static final int STATE_EXPANDED = 2;

		//{{{ ExpansionToggleBorder constructor
		public ExpansionToggleBorder(int state, int level)
		{
			this.state = state;
			this.level = level;
		} //}}}

		//{{{ paintBorder() method
		public void paintBorder(Component c, Graphics g,
			int x, int y, int width, int height)
		{
			switch(state)
			{
			case STATE_COLLAPSED:
				COLLAPSED_ICON.paintIcon(c,g,
					x + level * LEVEL_WIDTH + 2,
					y + (height - COLLAPSED_ICON.getIconHeight()) / 2);
				break;
			case STATE_EXPANDED:
				EXPANDED_ICON.paintIcon(c,g,
					x + level * LEVEL_WIDTH + 2,
					y + 2 + (height - EXPANDED_ICON.getIconHeight()) / 2);
				break;
			}
		} //}}}

		//{{{ getBorderInsets() method
		public Insets getBorderInsets(Component c)
		{
			return new Insets(1,level * LEVEL_WIDTH
				+ ICON_WIDTH + 4,1,1);
		} //}}}

		//{{{ isBorderOpaque() method
		public boolean isBorderOpaque()
		{
			return false;
		} //}}}

		//{{{ isExpansionToggle() method
		public static boolean isExpansionToggle(int level, int x)
		{
			return (x >= level * LEVEL_WIDTH)
				&& (x <= level * LEVEL_WIDTH + ICON_WIDTH);
		} //}}}

		//{{{ Private members
		private int state;
		private int level;

		static
		{
			COLLAPSED_ICON = GUIUtilities.loadIcon("arrow1.png");
			EXPANDED_ICON = GUIUtilities.loadIcon("arrow2.png");
			ICON_WIDTH = Math.max(COLLAPSED_ICON.getIconWidth(),
				EXPANDED_ICON.getIconWidth());
		} //}}}
	} //}}}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7699.java