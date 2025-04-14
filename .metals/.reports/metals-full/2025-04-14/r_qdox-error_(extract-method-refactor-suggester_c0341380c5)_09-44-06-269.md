error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2324.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2324.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2324.java
text:
```scala
i@@f (item.getMarker() == null)

package org.eclipse.ui.ide.examples.markers.markerSupport;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.examples.markers.MarkerExampleActivator;
import org.eclipse.ui.views.markers.MarkerField;
import org.eclipse.ui.views.markers.MarkerItem;
import org.eclipse.ui.views.markers.MarkerSupportConstants;

public class ExampleMarkerField extends MarkerField {

	public ExampleMarkerField() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getValue(MarkerItem item) {

		String message = item.getAttributeValue(IMarker.MESSAGE,
				MarkerSupportConstants.EMPTY_STRING);
		String prefix = getFixablePrefix(item);

		if (prefix == null)
			return message;

		return prefix.concat(message);
	}

	private String getFixablePrefix(MarkerItem item) {

		if (!item.isConcrete())
			return null;

		IMarker marker = item.getMarker();
		// If there is no image get the full image rather than the decorated
		// one
		if (marker != null) {
			if (IDE.getMarkerHelpRegistry().hasResolutions(marker)) {
				return "[Fixable] ";
			}

		}

		return null;

	}

	@Override
	public String getColumnHeaderText() {
		return "Alternate Description";
	}

	@Override
	public int getDefaultColumnWidth(Control control) {
		return 250;
	}

	/**
	 * Get the image for the receiver.
	 * 
	 * @param item
	 * @return Image
	 */
	private Image getImage(MarkerItem item) {
		return JFaceResources.getResources().createImageWithDefault(
				MarkerExampleActivator.imageDescriptorFromPlugin(
						MarkerExampleActivator.PLUGIN_ID,
						"$nl$/icons/eclipse.png"));
	}

	@Override
	public void update(ViewerCell cell) {
		super.update(cell);
		MarkerItem item = (MarkerItem) cell.getElement();
		cell.setImage(annotateImage(item, getImage(item)));
		cell.setFont(JFaceResources.getFontRegistry().getBold(
				JFaceResources.BANNER_FONT));
		cell.setBackground(PlatformUI.getWorkbench().getDisplay()
				.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		cell.setForeground(PlatformUI.getWorkbench().getDisplay()
				.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2324.java