error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16063.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16063.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16063.java
text:
```scala
t@@his.col = column;

package org.apache.jmeter.gui.util;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

/**
 * @author mstover
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class TextAreaTableCellEditor implements TableCellEditor,FocusListener {
	JScrollPane pane;
	JTextArea editor;
	String value = "";
	LinkedList listeners = new LinkedList();
	int row,col;
	
	public Component getTableCellEditorComponent(JTable table,
                                             Object value,
                                             boolean isSelected,
                                             int row,
                                             int column)
    {
    	editor = new JTextArea(value.toString());
    	editor.addFocusListener(this);
    	editor.setEnabled(true);
    	editor.setRows(editor.getRows());
    	editor.revalidate();
    	pane = new JScrollPane(editor,JScrollPane.VERTICAL_SCROLLBAR_NEVER,
    			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    	pane.validate();
    	this.row = row;
    	this.col = col;
    	return pane;
    }
    
    public int getColumn()
    {
    	return col;
    }
    
    public int getRow()
    {
    	return row;
    }
    
    public void focusLost(FocusEvent fe)
    {
    	stopCellEditing();
    }
    
    public void focusGained(FocusEvent fe)
    {
    }
    
    public TextAreaTableCellEditor()
    {
    	editor = new JTextArea();
    	editor.setRows(3);
    }
    
    public Component getComponent()
    {
    	return editor;
    }
    
    public Object getCellEditorValue()
    {
    	return editor.getText();
    }
    
    public void cancelCellEditing()
    {
    	Iterator iter = ((List)listeners.clone()).iterator();
    	while(iter.hasNext())
    	{
    		((CellEditorListener)iter.next()).editingCanceled(new ChangeEvent(this));
    	}
    }
    
    public boolean stopCellEditing()
    {
    	Iterator iter = ((List)listeners.clone()).iterator();
    	while(iter.hasNext())
    	{
    		((CellEditorListener)iter.next()).editingStopped(new ChangeEvent(this));
    	}
    	return true;
    }
    
    public void addCellEditorListener(CellEditorListener lis)
    {
    	listeners.add(lis);
    }
    
    public boolean isCellEditable(EventObject anEvent)
    {
    	if (anEvent instanceof MouseEvent)
		{
			if (((MouseEvent)anEvent).getClickCount() > 0)
			{
				return true;
			}
		}
		else if(anEvent instanceof FocusEvent)
		{
			if(((FocusEvent)anEvent).getID() == FocusEvent.FOCUS_GAINED)
			{
				return true;
			}
		}
		return true;
    }
    
    public void removeCellEditorListener(CellEditorListener lis)
    {
    	listeners.remove(lis);
    }
    
    public boolean shouldSelectCell(EventObject eo)
    {
    	return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16063.java