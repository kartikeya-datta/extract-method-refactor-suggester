error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4582.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4582.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4582.java
text:
```scala
U@@mlFactory.getFactory().delete(sv);

// Copyright (c) 1996-99 The Regents of the University of California. All
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



// File: PropPanelStateVertex.java
// Classes: PropPanelStateVertex
// Original Author: oliver.heyden@gentleware.de
// 

package org.argouml.uml.ui.behavior.state_machines;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.foundation.data_types.*;

import org.argouml.application.api.*;
import org.argouml.uml.ui.*;
import org.argouml.uml.ui.foundation.core.*;
import org.argouml.model.uml.UmlFactory;
import org.argouml.uml.MMUtil;

import org.tigris.gef.util.*;

public abstract class PropPanelStateVertex extends PropPanelModelElement {

    ////////////////////////////////////////////////////////////////
    // constants
    protected static ImageIcon _stateIcon = ResourceLoader.lookupIconResource("State");
    protected static ImageIcon _actionStateIcon = ResourceLoader.lookupIconResource("ActionState");
    protected static ImageIcon _compositeStateIcon = ResourceLoader.lookupIconResource("CompositeState");
    protected static ImageIcon _simpleStateIcon = ResourceLoader.lookupIconResource("SimpleState");
    protected static ImageIcon _shallowHistoryIcon = ResourceLoader.lookupIconResource("ShallowHistory");
    protected static ImageIcon _deepHistoryIcon =ResourceLoader.lookupIconResource("DeepHistory");
    protected static ImageIcon _finalStateIcon = ResourceLoader.lookupIconResource("FinalState");
    protected static ImageIcon _initialIcon = ResourceLoader.lookupIconResource("Initial");
    protected static ImageIcon _forkIcon = ResourceLoader.lookupIconResource("Fork");
    protected static ImageIcon _joinIcon = ResourceLoader.lookupIconResource("Join");
    protected static ImageIcon _transitionIcon = ResourceLoader.lookupIconResource("Transition");

    ////////////////////////////////////////////////////////////////

    protected JScrollPane incomingScroll;
    protected JScrollPane outgoingScroll;

    ////////////////////////////////////////////////////////////////
    // contructors
    public PropPanelStateVertex(String name, int columns) {
	this(name, null, columns);
    }

    public PropPanelStateVertex(String name,ImageIcon icon, int columns) {
	super(name, icon, columns);

	Class mclass = MStateVertex.class;

	JList incomingList = new UMLList(new UMLReflectionListModel(this,"incomings",true,"getIncomings",null,null,null),true);
	incomingList.setForeground(Color.blue);
	incomingList.setVisibleRowCount(1);
	incomingList.setFont(smallFont);
        incomingScroll = new JScrollPane(incomingList);

	JList outgoingList = new UMLList(new UMLReflectionListModel(this,"outgoings",true,"getOutgoings",null,null,null),true);
	outgoingList.setForeground(Color.blue);
	outgoingList.setVisibleRowCount(1);
	outgoingList.setFont(smallFont);
        outgoingScroll = new JScrollPane(outgoingList);


	new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateUp",null);
	new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu", "button.go-back"),"navigateBackAction","isNavigateBackEnabled");
	new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
	new PropPanelButton(this,buttonPanel,_deleteIcon,localize("Delete"),"removeElement",null);
    }

    public void navigateUp() {
        Object target = getTarget();
        if(target instanceof MStateVertex) {
            MStateVertex elem = (MStateVertex) target;
            MStateVertex container = elem.getContainer();
            if(container != null) {
                navigateTo(container);
            }
        }
    }

      public MStateMachine getStateMachine() {
        MStateMachine machine = null;
        Object target = getTarget();
        if(target instanceof MState) {
            machine = ((MState) target).getStateMachine();
        }
        return machine;
    }

    public java.util.List getIncomings() {
        java.util.Collection incomings = null;
        Object target = getTarget();
        if(target instanceof MStateVertex) {
            incomings = ((MStateVertex) target).getIncomings();
        }
        return new Vector(incomings);
    }

    public java.util.List getOutgoings() {
        java.util.Collection outgoings = null;
        Object target = getTarget();
        if(target instanceof MStateVertex) {
            outgoings = ((MStateVertex) target).getOutgoings();
        }
        return new Vector(outgoings);
    }

      public void removeElement() {
	//overrides removeElement in PropPanel
        Object target = getTarget();
        if(target instanceof MStateVertex) {
            MStateVertex sv = (MStateVertex) target;

            Object newTarget = sv.getContainer();

            sv.remove();

            if(newTarget != null) {
                navigateTo(newTarget);
            }
        }
    }


} /* end class PropPanelStateVertex */

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4582.java