error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10103.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10103.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,24]

error in qdox parser
file content:
```java
offset: 24
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10103.java
text:
```scala
protected static final S@@tring DEFAULT_CLIENT = "ecf.generic.client";

package org.eclipse.ecf.example.collab.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.ecf.core.ContainerDescription;
import org.eclipse.ecf.core.ContainerFactory;
import org.eclipse.ecf.example.collab.ClientPlugin;
import org.eclipse.ecf.example.collab.ClientPluginConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class ConnectionDialog extends TitleAreaDialog {
	protected static final String CLASSNAME = JoinGroupWizardPage.class.getName();
	protected static final String ISSERVER_PROP_NAME = CLASSNAME+".isServer";
	protected static final String DEFAULT_CLIENT = "org.eclipse.ecf.provider.generic.Client";
	
	public ConnectionDialog(Shell parentShell) {
		super(parentShell);
	}

	protected Control createDialogArea(Composite parent) {
		Composite main = new Composite((Composite) super.createDialogArea(parent), SWT.NONE);
		main.setLayout(new GridLayout());
		main.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label providerLabel = new Label(main, SWT.NONE);
		providerLabel.setText("Connection Protocol");

		Composite providerComp = new Composite(main, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		providerComp.setLayout(layout);
		providerComp.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		TableViewer viewer = new TableViewer(providerComp, SWT.BORDER
 SWT.FULL_SELECTION);
		viewer.setContentProvider(new ECFProviderContentProvider());
		viewer.setLabelProvider(new ECFProviderLabelProvider());		
		viewer.addSelectionChangedListener(new ProviderSelector());
		
		Table table = viewer.getTable();
		GridData gData = new GridData(GridData.FILL_VERTICAL);
		gData.widthHint = 150;
		table.setLayoutData(gData);
/*		table.setHeaderVisible(true);
		TableColumn tc = new TableColumn(table, SWT.NONE);
		tc.setText("Name");
		tc = new TableColumn(table, SWT.NONE);
		tc.setText("Classname");*/

		viewer.setInput(ContainerFactory.getDefault().getDescriptions());
		
		Composite paramComp = new Composite(providerComp, SWT.NONE);
		paramComp.setLayout(new GridLayout());
		paramComp.setLayoutData(new GridData(GridData.FILL_BOTH));

		this.setTitle("ECF Connection");
		this.setMessage("Please choose a provider and supply connection parameters.");

		return parent;
	}

	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	private class ECFProviderContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {		
			List rawDescriptions = (List) inputElement;
			List elements = new ArrayList();
			
			for(Iterator i=rawDescriptions.iterator(); i.hasNext(); ) {
	            final ContainerDescription desc = (ContainerDescription) i.next();
	            Map props = desc.getProperties();
	            String isServer = (String) props.get(ISSERVER_PROP_NAME);
	            if (isServer == null || !isServer.equalsIgnoreCase("true")) {
	                elements.add(desc);
	            }
	        }
			
			return elements.toArray();
		}

		public void dispose() {			
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}		
	}
	
	private class ECFProviderLabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {			
			if (columnIndex == 0) {
				//TODO: If the container description contains an image for the provider, display it here.
				return ClientPlugin.getDefault().getImageRegistry().get(ClientPluginConstants.DECORATION_DEFAULT_PROVIDER);
			}
			
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			ContainerDescription desc = (ContainerDescription) element;
			switch(columnIndex) {
			case 0:
				return desc.getDescription();
			case 1:
				return desc.getName();
			}
			
			return "";
		}

		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
		}
		
	}	
	
	private class ProviderSelector implements ISelectionChangedListener {

		public void selectionChanged(SelectionChangedEvent event) {
			StructuredSelection selection = (StructuredSelection) event.getSelection();
			ContainerDescription desc = (ContainerDescription) selection.getFirstElement();
			
			//desc.
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10103.java