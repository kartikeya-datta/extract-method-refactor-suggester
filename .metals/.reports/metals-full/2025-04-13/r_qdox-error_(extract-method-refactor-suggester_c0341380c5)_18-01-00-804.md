error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7877.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7877.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7877.java
text:
```scala
i@@f (cat.getPriority() != null && cat.getPriority().equals(Priority.ERROR)) {

// Copyright (c) 1996-2001 The Regents of the University of California. All
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

// File: UMLListSubMenuItem.java
// Classes: UMLListSubMenuItem
// Original Author: mail@jeremybennett.com
// $$

// 2 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Created to support
// UMLModelElementListLinkModel. Based on Curt Arnold's implementation of
// UMLListMenuItem.


package org.argouml.uml.ui;

import javax.swing.event.*;
import javax.swing.*;

import org.apache.log4j.Category;
import org.apache.log4j.Priority;

import java.lang.reflect.*;
import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import java.awt.event.*;
import java.awt.*;


/**
 * <p>Extends JMenuItem to invoke a method upon selection, suitable for an
 *   entry in the "Link" sub-menu of {@link UMLModelElementListLinkModel}.</p>
 *
 * <p>The method must have the signature
 *   <code>void method(int index, MModelElement subEntry)</code>.</p>
 *
 * @author Jeremy Bennett
 */

public class UMLListSubMenuItem extends JMenuItem implements ActionListener {
    protected static Category cat = Category.getInstance(UMLListSubMenuItem.class);

    /**
     * <p>The object (typically a list model of some sort) on which the method
     *   will be invoked.</p>
     */

    private Object _actionObj;


    /**
     * <p>The index argument to be supplied to the method on
     *   selection. Typically the index into the main list on which a popup
     *   menu was invoked.</p>
     */

    private int _index;


    /**
     * <p>The model element to be supplied to the method on
     *   selection. Typically the object in a "Link" sub-menu.
     */

    private MModelElement _subEntry;


    /**
     * <p>The method to actually be invoked on selection.</p>
     */

    private Method _action;


    /**
     * <p>The parameter signature of the method on selection. Defined here as a
     *   constant for convenience.</p>
     */

    private static final Class[] _PARAMETER_SIGNATURE =
        { int.class,
          MModelElement.class };


    /**
     * <p>Creates a new sub-menu item.</p>
     *
     * <p>We invoke the superclass constructor with the given caption, then
     *   record the other parameters that will be used at invocation time. We
     *   identify the method at this time, rather than invocation time, giving
     *   earlier detection of any problems.</p>
     *
     * @param caption    Caption for menu item.
     *
     * @param actionObj  Object on which method will be invoked (typically some
     *                   sort of list).
     *
     * @param action     Name of method to be invoked on selection.
     *
     * @param index      Integer value passed as first argument to method,
     *                   typically position in main menu list.
     *
     * @param subEntry   Model element passed as second argument to method,
     *                   typically entry in the sub-menu.
     */ 

    public UMLListSubMenuItem(String caption, Object actionObj, String action,
                              int index, MModelElement subEntry) {
        super(caption);

        // Record other parameters

        _actionObj = actionObj;
        _index     = index;
        _subEntry  = subEntry;

        // Trap any problem with the method to be invoked on seleciton. If
        // there is a problem we disable the entry.

        // It would be a little more efficient to resolve the action only when
        // the popup was invoked, however this will identify bad "actions" more
        // readily

        try {
            _action = _actionObj.getClass().getMethod(action,
                                                      _PARAMETER_SIGNATURE);
        }
        catch(Exception e) {
            cat.error(this.getClass().toString() + ":" +
                               e.toString() + " while getting method " +
                               action + "(int, MModelElement)", e);
        }
        
        // Listen for anything happening on this entry

        addActionListener(this);
    }


    /**
     * <p>This method is invoked when the menu item is selected.</p>
     *
     * <p>We invoke the method previously identified on the recorded object and
     *   arguments.</p>
     *
     * <p>Specified by the {@link ActionListener} interface.</p>
     *
     * @param event  the event that triggered the action. Ignored in this
     *               implementation.
     */

    public void actionPerformed(final java.awt.event.ActionEvent event) {

        // We could get a run-time problem in the method we invoke, so trap
        // that.

        try {
             Object[] argValue = { new Integer(_index), _subEntry };
            _action.invoke(_actionObj, argValue);
        }
        catch(InvocationTargetException ex) {
            if (cat.getPriority().equals(Priority.ERROR)) {
                StringBuffer buf = new StringBuffer();
                buf.append(this.getClass().toString() +
                               ": actionPerformed(). " + 
                               ex.getTargetException().toString() +
                               " when invoking ");
                buf.append("\n");
                buf.append(_actionObj.toString() + "(" + _index + ", " +
                               _subEntry.toString() + ")");
                buf.append("\n");
                cat.error(buf.toString(), ex);
            }
           
        }
        catch(Exception e) {
            cat.error(this.getClass().toString() +
                               ": actionPerformed(). " + 
                               e.toString(), e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7877.java