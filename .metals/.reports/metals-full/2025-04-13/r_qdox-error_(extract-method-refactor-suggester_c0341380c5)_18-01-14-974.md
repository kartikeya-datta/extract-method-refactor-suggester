error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8321.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8321.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8321.java
text:
```scala
s@@etText(subject);

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

package org.columba.mail.gui.table;

import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.columba.mail.gui.table.model.MessageNode;
import org.columba.mail.message.ColumbaHeader;
import org.columba.ristretto.message.Flags;
import org.columba.ristretto.message.HeaderInterface;

public class SubjectTreeRenderer extends DefaultTreeCellRenderer {

	private Font plainFont, boldFont, underlinedFont;

	/**
	 * @param table
	 */
	public SubjectTreeRenderer() {
		super();

		boldFont = UIManager.getFont("Label.font");
		boldFont = boldFont.deriveFont(Font.BOLD);

		plainFont = UIManager.getFont("Label.font");

		underlinedFont = UIManager.getFont("Tree.font");
		underlinedFont = underlinedFont.deriveFont(Font.ITALIC);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTreeCellRendererComponent(
		JTree tree,
		Object value,
		boolean selected,
		boolean expanded,
		boolean leaf,
		int row,
		boolean hasFocus) {

		super.getTreeCellRendererComponent(
			tree,
			value,
			selected,
			expanded,
			leaf,
			row,
			hasFocus);

		MessageNode messageNode = (MessageNode) value;

		if (messageNode.getUserObject().equals("root")) {
			setText("...");
			setIcon(null);
			return this;
		}

		HeaderInterface header = messageNode.getHeader();
		if (header == null) {
			return this;
		}

		//if (getFont() != null) {
		Flags flags = ((ColumbaHeader)header).getFlags();

		if (flags != null) {
			if (!flags.getSeen()) {
				//if (!getFont().equals(boldFont))
				setFont(boldFont);
			} else if (messageNode.isHasRecentChildren()) {
				//if (!getFont().equals(underlinedFont))
				setFont(underlinedFont);
			} else //if (!getFont().equals(plainFont)) {
				setFont(plainFont);
		}

		//}

		String subject = (String) header.get("columba.subject");
		if (subject != null)
			setText("test");//subject);
		else
			setText("null");

		setIcon(null);

		/*
		return super.getTreeCellRendererComponent(
		tree,
		value,
		selected,
		expanded,
		leaf,
		row,
		hasFocus);
		*/
		return this;
	}

	public void paint(Graphics g) {

		Rectangle bounds = g.getClipBounds();
		Font font = getFont();
		FontMetrics fontMetrics = g.getFontMetrics(font);

		int textWidth = fontMetrics.stringWidth(getText());

		int iconOffset = 0;

		//int iconOffset = getHorizontalAlignment() + getIcon().getIconWidth() + 1;

		if (bounds.x == 0 && bounds.y == 0) {
			bounds.width -= iconOffset;
			String labelStr = layout(this, fontMetrics, getText(), bounds);
			setText(labelStr);
		}

		super.paint(g);
	}

	private String layout(
		JLabel label,
		FontMetrics fontMetrics,
		String text,
		Rectangle viewR) {
		Rectangle iconR = new Rectangle();
		Rectangle textR = new Rectangle();
		return SwingUtilities.layoutCompoundLabel(
			fontMetrics,
			text,
			null,
			SwingConstants.RIGHT,
			SwingConstants.RIGHT,
			SwingConstants.RIGHT,
			SwingConstants.RIGHT,
			viewR,
			iconR,
			textR,
			0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8321.java