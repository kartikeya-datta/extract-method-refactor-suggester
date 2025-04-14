error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1783.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1783.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1783.java
text:
```scala
i@@nt currentUpdate = new Long(event.getPosition()).intValue();

// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.swingext;

import javax.swing.JFrame;
import javax.swing.ProgressMonitor;
import javax.swing.UIManager;

import org.argouml.i18n.Translator;
import org.argouml.persistence.ProgressEvent;
import org.argouml.persistence.ProgressListener;

/**
 * Manages a ProgressMonitor dialog
 * 
 * @author andrea_nironi@tigris.org
 */
public class ProgressMonitorWindow implements ProgressListener {
    
    private ProgressMonitor pbar;
    
    /**
     * initializes a ProgressMonitor
     * 
     * @param parent	the Component to be set as parent
     * @param title     the (internationalized) title of the ProgressMonitor
     */
    public ProgressMonitorWindow(JFrame parent, String title) {
        this.pbar = new ProgressMonitor(parent, 
                title,
                null, 0, 100);
        this.pbar.setMillisToDecideToPopup(250);       
        this.pbar.setMillisToPopup(500);
        parent.repaint();
        progress(5);
        
    }
    
    /**
     * Report a progress to the ProgressMonitor window
     * @param event 	the ProgressEvent, containing the current position
     * 					of the monitor
     */
    public void progress(final ProgressEvent event) {
    	if (this.pbar != null) {
            int currentUpdate = Long.valueOf(event.getPosition()).intValue();
            pbar.setProgress(currentUpdate);
            Object[] args = new Object[]{String.valueOf(currentUpdate)};
            pbar.setNote(Translator.localize("dialog.progress.note", args));
    	}
    }
    
    /**
     * Report a progress to the ProgressMonitor window
     * @param progress     the progress to be set
     */
    public void progress(final int progress) {
        if (this.pbar != null) {
            pbar.setProgress(progress);
            Object[] args = new Object[]{String.valueOf(progress)};
            pbar.setNote(Translator.localize("dialog.progress.note", args));
        }
    }
    
    /**
     * Checks if the ProgressMonitor has been canceled or not
     * 
     * @return true if the user has pressed "cancel"
     */
    public boolean isCanceled() {
        return this.pbar != null && this.pbar.isCanceled();
    }

    /**
     * Close the ProgressMonitor dialog, releasing its resources
     */
    public void close() {
        this.pbar.close();
        this.pbar = null;
    }
    
    // these settings are needed to make the ProgressMonitor pop up early
    static {
        UIManager.put("ProgressBar.repaintInterval", new Integer(150));
        UIManager.put("ProgressBar.cycleTime", new Integer(1050));        
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1783.java