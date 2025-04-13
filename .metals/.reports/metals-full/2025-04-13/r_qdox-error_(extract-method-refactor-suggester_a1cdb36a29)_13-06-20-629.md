error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12279.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12279.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12279.java
text:
```scala
B@@rowserManager.getDefault().getEditorManager().showSourceLine(loc, true);

/* *******************************************************************
 * Copyright (c) 1999-2001 Xerox Corporation, 
 *               2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 * ******************************************************************/


package org.aspectj.tools.ajbrowser;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.aspectj.ajde.Ajde;
import org.aspectj.ajde.TaskListManager;
import org.aspectj.ajde.ui.swing.CompilerMessage;
import org.aspectj.ajde.ui.swing.CompilerMessagesCellRenderer;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.Message;
import org.aspectj.bridge.IMessage.Kind;

/**
 * Used to display a list of compiler messages that can be clicked in order
 * to seek to their corresponding sourceline.
 *
 * @author Mik Kersten
 */
public class CompilerMessagesPanel extends JPanel implements TaskListManager {
    private JScrollPane jScrollPane1 = new JScrollPane();
    //private JScrollPane jScrollPane2 = new JScrollPane();
    private JList list = new JList();
    private DefaultListModel listModel = new DefaultListModel();
    private BorderLayout borderLayout1 = new BorderLayout();
    private boolean hasWarning = false;

    public CompilerMessagesPanel() {
        try {
            jbInit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        list.setModel(listModel);

        MouseListener mouseListener = new MouseAdapter() {
             public void mouseClicked(MouseEvent e) {
                 if (e.getClickCount() >= 1) {
                     int index = list.locationToIndex(e.getPoint());
                     if (listModel.getSize() >= index && index != -1) {
                     	CompilerMessage cm = (CompilerMessage)listModel.getElementAt(index);
                        if ((null != cm) && (null != cm.message)) {
                            displayMessage(cm.message);
                        }
                     }
                  }
             }
        };
        list.addMouseListener(mouseListener);
        list.setCellRenderer(new CompilerMessagesCellRenderer());
    } 
    
    public void addSourcelineTask(
        String message,
        ISourceLocation sourceLocation,
        Kind kind) {
            addSourcelineTask(new Message(message, kind, null, sourceLocation));
    }

    /**
     * called when user double-clicks on a message.
     */
    protected void displayMessage(IMessage message) {
        ISourceLocation loc = message.getISourceLocation();
        Ajde.getDefault().getEditorManager().showSourceLine(loc, true);
        // show dialog with stack trace if thrown
        Throwable thrown = message.getThrown();
        if (null != thrown) { 
            Ajde.getDefault().getErrorHandler().handleError(message.getMessage(), thrown);
        }
    }

    public void addSourcelineTask(IMessage message) {   
        listModel.addElement(new CompilerMessage(message));
        if (!hasWarning && IMessage.WARNING.isSameOrLessThan(message.getKind())) {
            hasWarning = true;
        }
        BrowserManager.getDefault().showMessages();
    }

    public void addProjectTask(String message, IMessage.Kind kind) {
        IMessage m = new Message(message, kind, null, null);
		listModel.addElement(new CompilerMessage(m));
        if (!hasWarning && IMessage.WARNING.isSameOrLessThan(kind)) {
            hasWarning = true;
        }
		BrowserManager.getDefault().showMessages();
	}

    public boolean hasWarning() {
        return hasWarning;
    }
    
    public void clearTasks() {
        listModel.clear();
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        this.add(jScrollPane1, BorderLayout.CENTER);
        jScrollPane1.getViewport().add(list, null);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12279.java