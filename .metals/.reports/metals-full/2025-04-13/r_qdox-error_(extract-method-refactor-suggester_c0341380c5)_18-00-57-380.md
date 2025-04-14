error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8637.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8637.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[3,1]

error in qdox parser
file content:
```java
offset: 55
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8637.java
text:
```scala
public class FailureRunView implements TestRunView {

p@@ackage junit.swingui;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;
import junit.framework.*;
import junit.runner.BaseTestRunner;


/**
 * A view presenting the test failures as a list.
 */
class FailureRunView implements TestRunView {
	JList fFailureList;
	TestRunContext fRunContext;
	
	/**
	 * Renders TestFailures in a JList
	 */
	static class FailureListCellRenderer extends DefaultListCellRenderer {
		private Icon fFailureIcon;
		private Icon fErrorIcon;
		
		FailureListCellRenderer() {
	    		super();
	    		loadIcons();
		}
	
		void loadIcons() {
			fFailureIcon= TestRunner.getIconResource(getClass(), "icons/failure.gif");
			fErrorIcon= TestRunner.getIconResource(getClass(), "icons/error.gif");		
		}
						
		public Component getListCellRendererComponent(
			JList list, Object value, int modelIndex, 
			boolean isSelected, boolean cellHasFocus) {
	
		    Component c= super.getListCellRendererComponent(list, value, modelIndex, isSelected, cellHasFocus);
			TestFailure failure= (TestFailure)value;
			String text= failure.failedTest().toString();
			String msg= failure.exceptionMessage();
			if (msg != null) 
				text+= ":" + BaseTestRunner.truncate(msg); 
	 
			if (failure.isFailure()) { 
				if (fFailureIcon != null)
		    		setIcon(fFailureIcon);
			} else {
		    	if (fErrorIcon != null)
		    		setIcon(fErrorIcon);
		    }
			setText(text);
			setToolTipText(text);
			return c;
		}
	}
	
	public FailureRunView(TestRunContext context) {
		fRunContext= context;
		fFailureList= new JList(fRunContext.getFailures());
		fFailureList.setFont(new Font("Dialog", Font.PLAIN, 12));
 
		fFailureList.setPrototypeCellValue(
			new TestFailure(new TestCase() {
				protected void runTest() {}
			}, 
			new AssertionFailedError("message"))
		);	
		fFailureList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fFailureList.setCellRenderer(new FailureListCellRenderer());
		fFailureList.setVisibleRowCount(5);

		fFailureList.addListSelectionListener(
			new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					testSelected();
				}
			}
		);
	}
	
	public Test getSelectedTest() {
		int index= fFailureList.getSelectedIndex();
		if (index == -1)
			return null;
			
		ListModel model= fFailureList.getModel();
		TestFailure failure= (TestFailure)model.getElementAt(index);
		return failure.failedTest();
	}
	
	public void activate() {
		testSelected();
	}
	
	public void addTab(JTabbedPane pane) {
		JScrollPane scrollPane= new JScrollPane(fFailureList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		Icon errorIcon= TestRunner.getIconResource(getClass(), "icons/error.gif");
		pane.addTab("Failures", errorIcon, scrollPane, "The list of failed tests");
	}
		
	public void revealFailure(Test failure) {
		fFailureList.setSelectedIndex(0);
	}
	
	public void aboutToStart(Test suite, TestResult result) {
	}

	public void runFinished(Test suite, TestResult result) {
	}

	protected void testSelected() {
		fRunContext.handleTestSelected(getSelectedTest());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8637.java