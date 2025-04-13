error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2151.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2151.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[13,1]

error in qdox parser
file content:
```java
offset: 597
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2151.java
text:
```scala
public final class ActionHandler extends AbstractHandler {

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
p@@ackage org.eclipse.ui.commands;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.actions.RetargetAction;

/**
 * This class adapts instances of <code>IAction</code> to
 * <code>IHandler</code>.
 * 
 * @since 3.0
 */
public class ActionHandler extends AbstractHandler {

    private final static String ATTRIBUTE_CHECKED = "checked"; //$NON-NLS-1$

    private final static String ATTRIBUTE_ENABLED = "enabled"; //$NON-NLS-1$

    private final static String ATTRIBUTE_HANDLED = "handled"; //$NON-NLS-1$

    private final static String ATTRIBUTE_ID = "id"; //$NON-NLS-1$

    private final static String ATTRIBUTE_STYLE = "style"; //$NON-NLS-1$

    private IAction action;

    private Set definedAttributeNames;

    /**
     * Creates a new instance of this class given an instance of
     * <code>IAction</code>.
     * 
     * @param action
     *            the action. Must not be <code>null</code>.
     */
    public ActionHandler(IAction action) {
        super();
        if (action == null) throw new NullPointerException();

        this.action = action;
        definedAttributeNames = new HashSet();
        definedAttributeNames.add(ATTRIBUTE_CHECKED);
        definedAttributeNames.add(ATTRIBUTE_ENABLED);
        definedAttributeNames.add(ATTRIBUTE_HANDLED);
        definedAttributeNames.add(ATTRIBUTE_ID);
        definedAttributeNames.add(ATTRIBUTE_STYLE);
        definedAttributeNames = Collections
                .unmodifiableSet(definedAttributeNames);
    }

    public void execute(Object parameter) throws ExecutionException {
        if ((action.getStyle() == IAction.AS_CHECK_BOX)
 (action.getStyle() == IAction.AS_RADIO_BUTTON)) {
            action.setChecked(!action.isChecked());
        }

        try {
            if (parameter instanceof Event)
                action.runWithEvent((Event) parameter);
            else
                action.run();
        } catch (Exception e) {
            throw new ExecutionException(e);
        }
    }

    public Object getAttributeValue(String attributeName)
            throws NoSuchAttributeException {
        if (!definedAttributeNames.contains(attributeName))
            throw new NoSuchAttributeException();
        else if (ATTRIBUTE_CHECKED.equals(attributeName))
            return action.isChecked() ? Boolean.TRUE : Boolean.FALSE;
        else if (ATTRIBUTE_ENABLED.equals(attributeName))
            return action.isEnabled() ? Boolean.TRUE : Boolean.FALSE;
        else if (ATTRIBUTE_HANDLED.equals(attributeName)) {
            if (action instanceof RetargetAction) {
                RetargetAction retargetAction = (RetargetAction) action;
                return (retargetAction.getActionHandler() != null) ? Boolean.TRUE
                        : Boolean.FALSE;
            } else
                return Boolean.TRUE;
        } else if (ATTRIBUTE_ID.equals(attributeName))
            return action.getId();
        else if (ATTRIBUTE_STYLE.equals(attributeName))
            return new Integer(action.getStyle());
        else
            throw new NoSuchAttributeException();
    }

    public Set getDefinedAttributeNames() {
        return definedAttributeNames;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2151.java