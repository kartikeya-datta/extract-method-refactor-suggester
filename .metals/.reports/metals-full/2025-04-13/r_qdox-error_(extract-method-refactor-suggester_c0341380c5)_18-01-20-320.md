error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9089.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9089.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[256,52]

error in qdox parser
file content:
```java
offset: 8220
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9089.java
text:
```scala
// UmlModelEventPump.getPump().removeModelEventListener(this, (MBase)_target, getEventName());

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

// $header$
package org.argouml.uml.ui;

import javax.swing.JTextField;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;

import org.apache.log4j.Category;
import org.argouml.model.uml.UmlModelEventPump;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;

/**
 * A new model for a textproperty. This model does not use reflection to reach 
 * its goal and will perform better therefore. Furthermore, it only reacts to 
 * events that are meant for this model which improves maintainability and 
 * performance. 
 * TODO: make this model independant from proppanel by designing a mechanisme
 * that can replace thirthpartyeventlistener.
 * TODO: make this model independant from GUI components to pass on targetChanged
 * and targetreasserted events by designing a mechanisme that can extend 
 * UMLChangeDispatch so UMLUserInterfaceComponents don't have to be java.awt.components
 * anymore to get targetChanged events.
 * TODO: remove the dependance on the panel to get the target.
 * @since Oct 6, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public abstract class UMLPlainTextDocument extends PlainDocument
    implements UMLUserInterfaceComponent {
        
    public static Category cat = Category.getInstance(UMLPlainTextDocument.class);
    
    /**
     * True if an event should be fired when the text of the document is changed
    */
    private boolean _firing = true;
    
    /**
     * True if an user edits the document directly (by typing in text)
    */
    private boolean _editing = false;
    
    /**
     * The target of the propertypanel that's behind this property.
    */    
    private Object _target = null;

    /**
     * The name of the property set event that will change the property this document
     * shows.
    */
    private String _eventName = null;
    
    private UMLUserInterfaceContainer _container = null;
    
    /**
     * Constructor for UMLPlainTextDocument. This takes a panel to set the
     * thirdpartyeventlistener to the given list of events to listen to.
     */
    public UMLPlainTextDocument(UMLUserInterfaceContainer cont, String eventName) {
        super();
        _container = cont;
        setEventName(eventName);
        setTarget(cont.getTarget());
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetChanged()
     */
    public void targetChanged() {
        setTarget(_container.getTarget());
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetReasserted()
     */
    public void targetReasserted() {
        setTarget(_container.getTarget());
    }

    /**
     * @see ru.novosoft.uml.MElementListener#propertySet(ru.novosoft.uml.MElementEvent)
     */
    public void propertySet(MElementEvent e) {
        handleEvent();
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleAdded(ru.novosoft.uml.MElementEvent)
     */
    public void roleAdded(MElementEvent e) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleRemoved(ru.novosoft.uml.MElementEvent)
     */
    public void roleRemoved(MElementEvent e) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#listRoleItemSet(ru.novosoft.uml.MElementEvent)
     */
    public void listRoleItemSet(MElementEvent e) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#removed(ru.novosoft.uml.MElementEvent)
     */
    public void removed(MElementEvent e) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#recovered(ru.novosoft.uml.MElementEvent)
     */
    public void recovered(MElementEvent e) {
    }

    /**
     * Returns the target.
     * @return Object
     */
    public final Object getTarget() {
        return _target;
    }

    /**
     * Sets the target.
     * @param target The target to set
     */
    public final void setTarget(Object target) {
        if (target instanceof MBase) {
            if (_target != null)
                UmlModelEventPump.getPump().removeModelEventListener(this, (MBase)_target, getEventName());
            _target = target;
            UmlModelEventPump.getPump().removeModelEventListener(this, (MBase)_target, getEventName());
            UmlModelEventPump.getPump().addModelEventListener(this, (MBase)_target, getEventName());
            handleEvent();
        }
    }

    /**
     * @see javax.swing.text.Document#insertString(int, java.lang.String, javax.swing.text.AttributeSet)
     */
    public void insertString(int offset, String str, AttributeSet a)
        throws BadLocationException {
        super.insertString(offset, str, a);
        if (isFiring()) {
            setFiring(false);
            setProperty(getText(0, getLength()));    
            setFiring(true);
        } 
        
        
             
    }

    /**
     * @see javax.swing.text.Document#remove(int, int)
     */
    public void remove(int offs, int len) throws BadLocationException {
        super.remove(offs, len);
        if (isFiring()) {
            setFiring(false);
            setProperty(getText(0, getLength()));    
            setFiring(true);
        } 
    }
    
    protected abstract void setProperty(String text);
    
    protected abstract String getProperty();
    
    private final void setFiring(boolean firing) {
        _firing = firing;
    }
    
    private final boolean isFiring() {
        return _firing;
    }
    
    private final void handleEvent() {
        try {
            setFiring(false);
            super.remove(0, getLength());
            super.insertString(0, getProperty(), null);
        } catch (BadLocationException b) {
            cat.error("A BadLocationException happened\n" +
                "The string to set was: " +
                getProperty(), b);
        }
        finally {
            setFiring(true);
        }      
    }
    
    

    /**
     * Returns the editing.
     * @return boolean
     */
    public boolean isEditing() {
        return _editing;
    }

    /**
     * Sets the editing.
     * @param editing The editing to set
     */
    public void setEditing(boolean editing) {
        _editing = editing;
    }

    /**
     * Returns the eventName.
     * @return String
     */
    public String getEventName() {
        return _eventName;
    }

    /**
     * Sets the eventName.
     * @param eventName The eventName to set
     */
    protected void setEventName(String eventName) {@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9089.java