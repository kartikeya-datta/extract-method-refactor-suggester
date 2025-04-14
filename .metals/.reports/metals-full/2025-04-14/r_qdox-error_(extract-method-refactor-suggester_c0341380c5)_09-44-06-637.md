error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4282.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4282.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4282.java
text:
```scala
i@@f (mee.getAddedValue() != null || mee.getRemovedValue() != null || (mee.getNewValue() != null && !mee.getNewValue().equals(mee.getOldValue()))) {

// Copyright (c) 1996-2002 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.model.uml;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;

import org.apache.log4j.Category;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.NavigatorPane;
import org.argouml.ui.ProjectBrowser;

/**
 * A single listener that converts MElementEvents into Argo events. 
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 * @stereotype singleton
 */
public class UmlModelListener implements MElementListener {

    /** Log4j logging category.
     */
    Category logger = null;

    /** Singleton instance.
     */
    private static UmlModelListener SINGLETON = new UmlModelListener();

    /** Singleton instance access method.
     */
    public static UmlModelListener getInstance() {
        return SINGLETON;
    }

    /** Don't allow instantiation.
     * Create the logger.
     */
    private UmlModelListener() {
        logger = Category.getInstance("org.argouml.model.uml.listener");
    }

    /** Handle the event.
     */
    public void listRoleItemSet (MElementEvent mee) {
        logger.debug("listRoleItemSet(" + mee + ")");
	// TODO:  Do we need to model change notify here?
    }

    /** Handle the event.
     *  Provides a model change notification only if the property
     *  values differ.
     */
    public void propertySet (MElementEvent mee) {
	notifyModelChanged(mee);
    }

    /** Handle the event.
     */
    public void recovered (MElementEvent mee) {
        logger.debug("recovered(" + mee + ")");
	// TODO:  Do we need to model change notify here?
    }

    /** Handle the event.
     */
    public void removed (MElementEvent mee) {
        logger.debug("removed(" + mee + ")");
	// TODO:  Do we need to model change notify here?
	// yes since we need to update the GUI
	notifyModelChanged(mee);
    }

    /** Handle the event.
     *  Provides a model change notification.
     */
    public void roleAdded (MElementEvent mee) {
        logger.debug("roleAdded(" + mee + ")");
	notifyModelChanged(mee);
    }

    /** Handle the event.
     *  Provides a model change notification.
     */
    public void roleRemoved (MElementEvent mee) {
        logger.debug("roleRemoved(" + mee + ")");
        
	notifyModelChanged(mee);
    }

    /** Common model change notification process.
     */
    protected void notifyModelChanged(MElementEvent mee) {
	// TODO: Change the project dirty flag outside this package
	//       using an event listener.

	// TODO: post an event of some type.
	//
	// Should this be a property change event?
	//

	if (mee.getAddedValue() != null || mee.getRemovedValue() != null || !mee.getNewValue().equals(mee.getOldValue())) {
		ProjectBrowser.TheInstance.getNavigatorPane().forceUpdate();
            if (ProjectManager.getManager().getCurrentProject() != null)
		ProjectManager.getManager().getCurrentProject().setNeedsSave(true);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4282.java