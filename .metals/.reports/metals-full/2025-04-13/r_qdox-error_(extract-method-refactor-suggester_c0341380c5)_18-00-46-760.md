error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7783.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7783.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7783.java
text:
```scala
t@@his(title, true);

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

/*
 * Toolbar.java
 *
 * Created on 29 September 2002, 21:01
 */

package org.argouml.swingext;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JToolBar;

/**
 * A toolbar class which assumes rollover effects and automatically gives tooltip
 * to any buttons created by adding an action.
 *
 * @author  Bob Tarling
 */
public class Toolbar extends JToolBar implements MouseListener {
    
    private static final Color selectedBack = new Color(153,153,153);
    private static final Color buttonBack = new Color(204,204,204);
    private static Color normalBack;

    private boolean _rollover;
    
    /** Creates a new instance of Toolbar
     */
    public Toolbar() {
        super();
        this.setFloatable(false);
        this.setRollover(true);
        this.setMargin(new Insets(0,0,0,0));
    }
    
    /** Creates a new instance of Toolbar
     * @param title The title to display in the titlebar when toolbar is floating
     */
    public Toolbar(String title) {
        this(title, false);
    }

    /** Creates a new instance of Toolbar
     * @param title The title to display in the titlebar when toolbar is floating
     * @param floatable true if the toolbar can be dragged to a floating position
     */
    public Toolbar(String title, boolean floatable) {
        super(title);
        this.setFloatable(floatable);
        this.setRollover(true);
        this.setMargin(new Insets(0,0,0,0));
    }
    
    /** Creates a new instance of Toolbar with the given orientation
     * @param orientation HORIZONTAL or VERTICAL
     */
    public Toolbar(int orientation) {
        super(orientation);
        this.setFloatable(false);
        this.setRollover(true);
        this.setMargin(new Insets(0,0,0,0));
    }

    public void setRollover(boolean rollover) {
        // TODO Check for JDK1.4 before calling super class setRollover
        //super.setRollover(rollover);
        this._rollover = rollover;
        // TODO Check for JDK1.4 before using Boolean.valueOf(rollover)
        //this.putClientProperty("JToolBar.isRollover", Boolean.valueOf(rollover));
        Boolean showRollover = Boolean.FALSE;
        if (rollover) showRollover = Boolean.TRUE;
        this.putClientProperty("JToolBar.isRollover",  showRollover);
    }
    
    public JButton add(Action action) {
        JButton button;

        if (action instanceof ButtonAction) {
            button = new JButton(action);
            String tooltip = button.getToolTipText();
            if (tooltip == null || tooltip.trim().length() == 0) {
                tooltip = button.getText();
            }
            button = super.add(action);
            button.setToolTipText(tooltip);
        } else {
            button = super.add(action);
        }
        button.setFocusPainted(false);
        button.addMouseListener(this);

        return button;
    }
    
    ////////////////////////////////////////////////////////////////
    // MouseListener implementation

    public void mouseEntered(MouseEvent me) { }
    public void mouseExited(MouseEvent me) { }
    public void mousePressed(MouseEvent me) { }
    public void mouseReleased(MouseEvent me) { }
    public void mouseClicked(MouseEvent me) {
        Object src = me.getSource();
        if (src instanceof JButton && ((JButton)src).getAction() instanceof ButtonAction) {
            JButton button = (JButton)src;
            ButtonAction action = (ButtonAction)button.getAction();
            if (action.isModal()) {
                Color currentBack = button.getBackground();
                if (currentBack.equals(selectedBack)) {
                    button.setBackground(normalBack);
                    button.setRolloverEnabled(_rollover);
                } else {
                    button.setBackground(selectedBack);
                    button.setRolloverEnabled(false);
                    normalBack = currentBack;
                }
                if (me.getClickCount() >= 2 && action.getLockMethod() == AbstractButtonAction.NONE) {
                    // FIXME Here I need to lock the button in place.
                    // The button should stay in place until it is pressed again (when
                    // it is released but not acted on) or any other key in its group
                    // is pressed.
                }
                else if (me.getClickCount() == 1) {
                }
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7783.java