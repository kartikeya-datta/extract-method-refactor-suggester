error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9621.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9621.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9621.java
text:
```scala
f@@Lines.addElement(st.nextToken());

package junit.swingui;

import java.awt.Component;
import java.awt.Font;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import junit.framework.TestFailure;
import junit.runner.BaseTestRunner;
import junit.runner.FailureDetailView;

/**
 * A view that shows a stack trace of a failure
 */
public class DefaultFailureDetailView implements FailureDetailView {
	JList fList;  
	
	/**
	 * A ListModel representing the scanned failure stack trace.
	 */
	static class StackTraceListModel extends AbstractListModel {
		private Vector fLines= new Vector(20);
		
		public Object getElementAt(int index) {
			return fLines.elementAt(index);
		}

		public int getSize() {
			return fLines.size();
		}
		
		public void setTrace(String trace) {
			scan(trace);
			fireContentsChanged(this, 0, fLines.size());
		}
		
		public void clear() {
			fLines.removeAllElements();
			fireContentsChanged(this, 0, fLines.size());
		}
		
		private void scan(String trace) {
			fLines.removeAllElements();
     		StringTokenizer st= new StringTokenizer(trace, "\n\r", false);
	    	while (st.hasMoreTokens()) 
 				fLines.add(st.nextToken());
		}
	}
	
	/**
	 * Renderer for stack entries
	 */
	static class StackEntryRenderer extends DefaultListCellRenderer {
						
		public Component getListCellRendererComponent(
				JList list, Object value, int modelIndex, 
				boolean isSelected, boolean cellHasFocus) {
			String text= ((String)value).replace('\t', ' ');
		    Component c= super.getListCellRendererComponent(list, text, modelIndex, isSelected, cellHasFocus);
			setText(text);
			setToolTipText(text);
			return c;
		}
	}
	
	/**
	 * Returns the component used to present the trace
	 */
	public Component getComponent() {
		if (fList == null) {
			fList= new JList(new StackTraceListModel());
			fList.setFont(new Font("Dialog", Font.PLAIN, 12));
			fList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			fList.setVisibleRowCount(5);
			fList.setCellRenderer(new StackEntryRenderer());
		}
		return fList;
	}
	
	/**
	 * Shows a TestFailure
	 */
	public void showFailure(TestFailure failure) {
		getModel().setTrace(BaseTestRunner.getFilteredTrace(failure.trace()));
	}
	/**
	 * Clears the output.
	 */
	public void clear() {
		getModel().clear();
	}
	
	private StackTraceListModel getModel() {
		return 	(StackTraceListModel)fList.getModel();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9621.java