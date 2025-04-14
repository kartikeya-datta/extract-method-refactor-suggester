error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7497.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7497.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7497.java
text:
```scala
P@@latformUI.getWorkbench().getHelpSystem().setHelp(comp, IContextHelpIds.XPATH_DIALOG);

/*******************************************************************************
 * Copyright (c) 2008 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.core.utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.wst.xml.security.core.XmlSecurityPlugin;
import org.w3c.dom.Document;


/**
 * <p>Provides a dialog window (<code>ElementListSelectionDialog</code>) with the XPath
 * expressions of all the elements of the selected XML document. Used on the first pages of the
 * Digital Signature Wizard and the Encryption Wizard.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class XpathDialog extends ElementListSelectionDialog {
  /** The XML document. */
  private IFile xmlDocument;

  /**
   * First constructor for the XPath dialog with two parameters.
   *
   * @param shell The parent shell
   * @param labelProvider The ILabelProvider
   */
  public XpathDialog(Shell shell, ILabelProvider labelProvider) {
    super(shell, labelProvider);
  }

  /**
   * Second constructor for the XPath dialog with four parameters.
   *
   * @param shell The parent shell
   * @param labelProvider ILabelProvider
   * @param file The XML document
   * @param message The message to display in the dialog window
   */
  public XpathDialog(Shell shell, ILabelProvider labelProvider, IFile file, String message) {
    super(shell, labelProvider);
    setTitle(Messages.xpathSelectionTitle);
    setMessage(message);
    xmlDocument = file;
    setElements(createContent());
    open();
  }

  /**
   * Adds the context help to the dialog.
   *
   * @param parent The parent composite
   * @return The manipulated parent composite
   */
  protected Control createDialogArea(Composite parent) {
    Control comp = super.createDialogArea(parent);
    PlatformUI.getWorkbench().getHelpSystem().setHelp(comp, "org.eclipse.wst.xml.security.doc.xpath");
    return comp;
  }

  /**
   * Collects all the nodes from the XML document.
   *
   * @return Object array with all the nodes
   */
  private Object[] createContent() {
    try {
      Document doc = Utils.parse(xmlDocument);
      return Utils.getCompleteXpath(doc);
    } catch (Exception e) {
      XmlSecurityPlugin plugin = XmlSecurityPlugin.getDefault();
      IStatus status = new Status(IStatus.ERROR, plugin.getBundle().getSymbolicName(), 0,
          Messages.xpathSelectionError, e);
      plugin.getLog().log(status);
      return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7497.java