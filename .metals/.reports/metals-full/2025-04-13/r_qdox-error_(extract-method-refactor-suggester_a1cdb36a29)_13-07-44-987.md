error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4787.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4787.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[3,1]

error in qdox parser
file content:
```java
offset: 46
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4787.java
text:
```scala
public class TestSelector extends JDialog {

p@@ackage junit.swingui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import junit.runner.Sorter;
import junit.runner.TestCollector;

/**
 * A test class selector. A simple dialog to pick the name of a test suite.
 */
class TestSelector extends JDialog {
	private JButton fCancel;
	private JButton fOk;
	private JList fList;
	private JScrollPane fScrolledList;
	private JLabel fDescription;
	private String fSelectedItem;
	
	/**
	 * Renders TestFailures in a JList
	 */
	static class TestCellRenderer extends DefaultListCellRenderer {
		Icon fLeafIcon;
		Icon fSuiteIcon;
		
		public TestCellRenderer() {
			fLeafIcon= UIManager.getIcon("Tree.leafIcon");
			fSuiteIcon= UIManager.getIcon("Tree.closedIcon");
		}
		
		public Component getListCellRendererComponent(
				JList list, Object value, int modelIndex, 
				boolean isSelected, boolean cellHasFocus) {
			Component c= super.getListCellRendererComponent(list, value, modelIndex, isSelected, cellHasFocus);
			String displayString= displayString((String)value);
			
			if (displayString.startsWith("AllTests"))
				setIcon(fSuiteIcon);
			else
				setIcon(fLeafIcon);
				
			setText(displayString);
		    	return c;
		}
		
		public static String displayString(String className) {
			int typeIndex= className.lastIndexOf('.');
    			if (typeIndex < 0) 
    				return className;
    			return className.substring(typeIndex+1) + " - " + className.substring(0, typeIndex);
		}
		
		public static boolean matchesKey(String s, char ch) {
    			return ch == Character.toUpperCase(s.charAt(typeIndex(s)));
		}
		
		private static int typeIndex(String s) {
			int typeIndex= s.lastIndexOf('.');
			int i= 0;
    			if (typeIndex > 0) 
    				i= typeIndex+1;
    			return i;
		}
	}
	
	protected class DoubleClickListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
	    		if (e.getClickCount() == 2) {
	    			okSelected();
	    		}
	      }
	}
	
	protected class KeySelectListener extends KeyAdapter {
		public void keyTyped(KeyEvent e) {
			keySelectTestClass(e.getKeyChar());
		}
	}

	public TestSelector(Frame parent, TestCollector testCollector) {
		super(parent, true);
		setSize(350, 300);
		setResizable(false);
		// setLocationRelativeTo only exists in 1.4
		try {
			setLocationRelativeTo(parent);
		} catch (NoSuchMethodError e) {
			centerWindow(this);
		}
		setTitle("Test Selector");
		
		Vector list= null;
		try {
			parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			list= createTestList(testCollector);
		} finally {
			parent.setCursor(Cursor.getDefaultCursor());
		}
		fList= new JList(list);
		fList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fList.setCellRenderer(new TestCellRenderer());
		fScrolledList= new JScrollPane(fList);

		fCancel= new JButton("Cancel");
		fDescription= new JLabel("Select the Test class:");
		fOk= new JButton("OK");
		fOk.setEnabled(false);
		getRootPane().setDefaultButton(fOk);
		
		defineLayout();
		addListeners();
	}

	public static void centerWindow(Component c) {
		Dimension paneSize= c.getSize();
		Dimension screenSize= c.getToolkit().getScreenSize();
		c.setLocation((screenSize.width-paneSize.width)/2, (screenSize.height-paneSize.height)/2);
	}
	
	private void addListeners() {
		fCancel.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			}
		);
		
		fOk.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					okSelected();
				}
			}
		);

		fList.addMouseListener(new DoubleClickListener());
		fList.addKeyListener(new KeySelectListener());
		fList.addListSelectionListener(
			new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					checkEnableOK(e);
				}
			}
		);

		addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					dispose();
				}
			}
		);
	}
	
	private void defineLayout() {
		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints labelConstraints = new GridBagConstraints();
		labelConstraints.gridx= 0; labelConstraints.gridy= 0;
		labelConstraints.gridwidth= 1; labelConstraints.gridheight= 1;
		labelConstraints.fill= GridBagConstraints.BOTH;
		labelConstraints.anchor= GridBagConstraints.WEST;
		labelConstraints.weightx= 1.0;
		labelConstraints.weighty= 0.0;
		labelConstraints.insets= new Insets(8, 8, 0, 8);
		getContentPane().add(fDescription, labelConstraints);

		GridBagConstraints listConstraints = new GridBagConstraints();
		listConstraints.gridx= 0; listConstraints.gridy= 1;
		listConstraints.gridwidth= 4; listConstraints.gridheight= 1;
		listConstraints.fill= GridBagConstraints.BOTH;
		listConstraints.anchor= GridBagConstraints.CENTER;
		listConstraints.weightx= 1.0;
		listConstraints.weighty= 1.0;
		listConstraints.insets= new Insets(8, 8, 8, 8);
		getContentPane().add(fScrolledList, listConstraints);
		
		GridBagConstraints okConstraints= new GridBagConstraints();
		okConstraints.gridx= 2; okConstraints.gridy= 2;
		okConstraints.gridwidth= 1; okConstraints.gridheight= 1;
		okConstraints.anchor= java.awt.GridBagConstraints.EAST;
		okConstraints.insets= new Insets(0, 8, 8, 8);
		getContentPane().add(fOk, okConstraints);


		GridBagConstraints cancelConstraints = new GridBagConstraints();
		cancelConstraints.gridx= 3; cancelConstraints.gridy= 2;
		cancelConstraints.gridwidth= 1; cancelConstraints.gridheight= 1;
		cancelConstraints.anchor= java.awt.GridBagConstraints.EAST;
		cancelConstraints.insets= new Insets(0, 8, 8, 8);
		getContentPane().add(fCancel, cancelConstraints);
	}
	
	public void checkEnableOK(ListSelectionEvent e) {
		fOk.setEnabled(fList.getSelectedIndex() != -1);
	}
	
	public void okSelected() {
		fSelectedItem= (String)fList.getSelectedValue();
		dispose();
	}
	
	public boolean isEmpty() {
		return fList.getModel().getSize() == 0;
	}
	
	public void keySelectTestClass(char ch) {
		ListModel model= fList.getModel();
		if (!Character.isJavaIdentifierStart(ch))
			return;
		for (int i= 0; i < model.getSize(); i++) {
			String s= (String)model.getElementAt(i);
			if (TestCellRenderer.matchesKey(s, Character.toUpperCase(ch))) {
				fList.setSelectedIndex(i);
				fList.ensureIndexIsVisible(i);
				return;
			}
		}
		Toolkit.getDefaultToolkit().beep();
	}
	
	public String getSelectedItem() {
		return fSelectedItem;
	}

	private Vector createTestList(TestCollector collector) {
    		Enumeration each= collector.collectTests();
    		Vector v= new Vector(200);
    		Vector displayVector= new Vector(v.size());
    		while(each.hasMoreElements()) {
    			String s= (String)each.nextElement();
    			v.addElement(s);
    			displayVector.addElement(TestCellRenderer.displayString(s));
    		}
    		if (v.size() > 0)
    			Sorter.sortStrings(displayVector, 0, displayVector.size()-1, new ParallelSwapper(v));
    		return v;
	}
	
	private class ParallelSwapper implements Sorter.Swapper {
		Vector fOther;
		
		ParallelSwapper(Vector other) {
			fOther= other;
		}
		public void swap(Vector values, int left, int right) {
			Object tmp= values.elementAt(left); 
			values.setElementAt(values.elementAt(right), left); 
			values.setElementAt(tmp, right);
			Object tmp2= fOther.elementAt(left);
			fOther.setElementAt(fOther.elementAt(right), left);
			fOther.setElementAt(tmp2, right);
		}			
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4787.java