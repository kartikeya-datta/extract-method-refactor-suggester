error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3909.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3909.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3909.java
text:
```scala
i@@f (((TabBehaviour)Tweaklets.get(TabBehaviour.KEY)).autoPinOnDirty()) {

/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.tweaklets.TabBehaviour;
import org.eclipse.ui.internal.tweaklets.Tweaklets;

/**
 * Action to toggle the pin state of an editor. If an editor is
 * pinned, then it is not reused.
 */
public class PinEditorAction extends ActiveEditorAction {
    private IPropertyListener propListener = new IPropertyListener() {
        public void propertyChanged(Object source, int propId) {
            if (propId == WorkbenchPartReference.INTERNAL_PROPERTY_PINNED) {
                WorkbenchPartReference ref = (WorkbenchPartReference)source;
                setChecked(ref.isPinned());
            } else if (propId == IWorkbenchPartConstants.PROP_DIRTY) {
        		if (((TabBehaviour)Tweaklets.get(TabBehaviour.class)).autoPinOnDirty()) {
					WorkbenchPartReference ref = (WorkbenchPartReference) source;
					if (ref.isDirty()) {
						ref.setPinned(true);
					}
				}
            }
        }
    };

    /**
     * Creates a PinEditorAction.
     */
    public PinEditorAction(IWorkbenchWindow window) {
        super(WorkbenchMessages.PinEditorAction_text, window); 
        setActionDefinitionId("org.eclipse.ui.window.pinEditor"); //$NON-NLS-1$
        setToolTipText(WorkbenchMessages.PinEditorAction_toolTip); 
        setId("org.eclipse.ui.internal.PinEditorAction"); //$NON-NLS-1$
        // @issue need help constant for this?
        //	WorkbenchHelp.setHelp(this, new Object[] {IHelpContextIds.SAVE_ACTION});
        setImageDescriptor(WorkbenchImages
                .getImageDescriptor(IWorkbenchGraphicConstants.IMG_ETOOL_PIN_EDITOR));
        setDisabledImageDescriptor(WorkbenchImages
                .getImageDescriptor(IWorkbenchGraphicConstants.IMG_ETOOL_PIN_EDITOR_DISABLED));
    }

    /* (non-Javadoc)
     * Method declared on IAction.
     */
    public void run() {
        if (getWorkbenchWindow() == null) {
            // action has been dispose
            return;
        }
        IEditorPart editor = getActiveEditor();
        if (editor != null) {
            WorkbenchPartReference ref = getReference(editor);
            ref.setPinned(isChecked());
        }
    }

    private WorkbenchPartReference getReference(IEditorPart editor) {
        return (WorkbenchPartReference)((EditorSite)editor.getSite()).getPartReference();
    }
    
    /* (non-Javadoc)
     * Method declared on ActiveEditorAction.
     */
    protected void updateState() {
        if (getWorkbenchWindow() == null || getActivePage() == null) {
            setChecked(false);
            setEnabled(false);
            return;
        }

        IEditorPart editor = getActiveEditor();
        boolean enabled = (editor != null);
        setEnabled(enabled);
        if (enabled) {
            setChecked(getReference(editor).isPinned());
        } else {
            setChecked(false);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.ActiveEditorAction#editorActivated(org.eclipse.ui.IEditorPart)
     */
    protected void editorActivated(IEditorPart part) {
        super.editorActivated(part);
        if (part != null) {
            getReference(part).addInternalPropertyListener(propListener);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.ActiveEditorAction#editorDeactivated(org.eclipse.ui.IEditorPart)
     */
    protected void editorDeactivated(IEditorPart part) {
        super.editorDeactivated(part);
        if (part != null) {
            getReference(part).removeInternalPropertyListener(propListener);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.actions.ActionFactory.IWorkbenchAction#dispose()
     */
    public void dispose() {
        // deactivate current editor now before super dispose because active editor will be null after call
        editorDeactivated(getActiveEditor());
        super.dispose();
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3909.java