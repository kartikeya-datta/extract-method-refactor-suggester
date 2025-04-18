error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9960.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9960.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9960.java
text:
```scala
n@@ew Color(248, 248, 248));

// The contents of this file are subject to the Mozilla Public License Version
// 1.1
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
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.mail.gui.table.plugins;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import org.columba.api.plugin.IExtensionInterface;
import org.columba.mail.gui.message.viewer.HeaderSeparatorBorder;
import org.columba.mail.gui.table.model.MessageNode;
import org.columba.mail.message.ColumbaHeader;
import org.columba.mail.message.IColumbaHeader;
import org.columba.ristretto.message.Flags;

/**
 * MultiLine renderer uses a two-row JPanel to display as much information as
 * possible in a very narrow JTable column.
 * <p>
 * Horrible experimental hack - you have been warned!
 * 
 * @author fdietz
 */
public class MultiLineRenderer extends JPanel implements TableCellRenderer,
		IExtensionInterface {

	private static final java.util.logging.Logger LOG = java.util.logging.Logger
			.getLogger("org.columba.mail.gui.table.plugins");

	private Font plainFont;

	private Font boldFont;

	private Font underlinedFont;

	protected static Border outterBorder = new EmptyBorder(2, 1, 2, 2);

	protected static Border lineBorder = new HeaderSeparatorBorder(
			Color.LIGHT_GRAY);

	protected static Border noFocusBorder = BorderFactory.createCompoundBorder(
			lineBorder, outterBorder);

	// We need a place to store the color the JLabel should be returned
	// to after its foreground and background colors have been set
	// to the selection background color.
	// These ivars will be made protected when their names are finalized.
	private Color unselectedForeground;

	private Color unselectedBackground;

	private JLabel subjectLabel;

	private AttachmentRenderer attachmentRenderer;

	private StatusRenderer statusRenderer;

	private FromRenderer fromRenderer;

	private DateRenderer dateRenderer;

	public MultiLineRenderer() {
		boldFont = UIManager.getFont("Tree.font");
		boldFont = boldFont.deriveFont(Font.BOLD);

		plainFont = UIManager.getFont("Tree.font");

		underlinedFont = UIManager.getFont("Tree.font");
		underlinedFont = underlinedFont.deriveFont(Font.ITALIC);

		unselectedForeground = UIManager.getColor("Table.foreground");
		unselectedBackground = UIManager.getColor("Table.background");

		setOpaque(true);
		setBorder(noFocusBorder);

		setLayout(new BorderLayout());

		statusRenderer = new StatusRenderer();
		fromRenderer = new FromRenderer();
		dateRenderer = new DateRenderer();
		attachmentRenderer = new AttachmentRenderer();

		subjectLabel = new JLabel();
		subjectLabel.setForeground(Color.darkGray);

		JPanel p3 = new JPanel();
		p3.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
		p3.setOpaque(false);
		p3.setLayout(new BorderLayout());
		add(p3, BorderLayout.WEST);
		p3.add(statusRenderer, BorderLayout.NORTH);

		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new BorderLayout());
		add(p, BorderLayout.CENTER);

		JPanel p2 = new JPanel();
		p2.setOpaque(false);
		p2.setLayout(new BorderLayout());
		// p2.setBorder(BorderFactory.createEmptyBorder(0,0,2,0));
		p.add(p2, BorderLayout.NORTH);

		p2.add(fromRenderer, BorderLayout.CENTER);
		p2.add(dateRenderer, BorderLayout.EAST);

		JPanel p4 = new JPanel();
		p4.setOpaque(false);
		p4.setLayout(new BorderLayout());
		p.add(p4, BorderLayout.CENTER);

		p4.add(subjectLabel, BorderLayout.CENTER);
		p4.add(attachmentRenderer, BorderLayout.EAST);

	}

	/**
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
	 *      java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		if (isSelected) {
			super.setForeground(table.getSelectionForeground());
			super.setBackground(table.getSelectionBackground());
		} else {
			super
					.setForeground((unselectedForeground != null) ? unselectedForeground
							: table.getForeground());
			super
					.setBackground((unselectedBackground != null) ? unselectedBackground
							: table.getBackground());
		}

		setFont(table.getFont());

		if (hasFocus) {
			// setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
			if (table.isCellEditable(row, column)) {
				super.setForeground(UIManager
						.getColor("Table.focusCellForeground"));
				super.setBackground(UIManager
						.getColor("Table.focusCellBackground"));
			}
		} else {
			// setBorder(noFocusBorder);
		}

		setBorder(noFocusBorder);

		statusRenderer.getTableCellRendererComponent(table, value, isSelected,
				hasFocus, row, column);
		fromRenderer.getTableCellRendererComponent(table, value, isSelected,
				hasFocus, row, column);
		dateRenderer.getTableCellRendererComponent(table, value, isSelected,
				hasFocus, row, column);
		attachmentRenderer.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);

		// TreePath path = tree.getPathForRow(row);
		MessageNode messageNode = (MessageNode) value;

		IColumbaHeader header = messageNode.getHeader();

		if (header == null) {
			LOG.info("header is null"); //$NON-NLS-1$

			return this;
		}

		Flags flags = ((ColumbaHeader) header).getFlags();

		if (flags != null) {
			// mark as bold if message is unseen
			if (!flags.getSeen()) {
				if (!getFont().equals(boldFont)) {
					setFont(boldFont);
				}
			} else if (messageNode.isHasRecentChildren()) {
				if (!getFont().equals(underlinedFont)) {
					setFont(underlinedFont);
				}
			} else if (!getFont().equals(plainFont)) {
				setFont(plainFont);
			}
		}

		Color msgColor = (Color) header.get("columba.color");

		if (isSelected)
			setBackground(UIManager.getColor("Table.selectionBackground"));
		else
			setBackground(table.getBackground());

		if (msgColor != null) {
			if (isSelected)
				setForeground(UIManager.getColor("Table.selectionForeground"));
			else {
				if (msgColor.equals(Color.BLACK) == false)
					setForeground(msgColor);
				else
					setForeground(table.getForeground());

			}
		}

		String subject = (String) header.get("columba.subject");
		if (isSelected)
			subjectLabel.setForeground(UIManager
					.getColor("Table.selectionForeground"));
		else
			subjectLabel.setForeground(Color.DARK_GRAY);

		subjectLabel.setText(subject);

		return this;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9960.java