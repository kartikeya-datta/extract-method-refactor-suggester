error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7760.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7760.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7760.java
text:
```scala
C@@lass c = bundle.loadClass(TEXT_SELECTION_CLASS);

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.actions.SimpleWildcardTester;
import org.eclipse.ui.internal.ActionExpression;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.osgi.framework.Bundle;

/**
 * Determines the enablement status given a selection. This calculation
 * is done based on the definition of the <code>enablesFor</code> attribute,
 * <code>enablement</code> element, and the <code>selection</code> element
 * found in the <code>IConfigurationElement</code> provided.
 * <p>
 * This class can be instantiated by clients. It is not intended to be extended.
 * </p>
 * 
 * @since 3.0
 * 
 * Note: The dependency on org.eclipse.jface.text for ITextSelection must be severed
 * It may be possible to do with IActionFilter
 * generic workbench registers IActionFilter for "size" property against IStructuredSelection
 * workbench text registers IActionFilter for "size" property against ITextSelection
 * code here: sel.getAdapter(IActionFilter.class)
 * As an interim solution, use reflection to access selections
 * implementing ITextSelection
 */
public final class SelectionEnabler {

    public static final int UNKNOWN = 0;

    public static final int ONE_OR_MORE = -1;

    public static final int ANY_NUMBER = -2;

    public static final int NONE_OR_ONE = -3;

    public static final int NONE = -4;

    public static final int MULTIPLE = -5;

    private List classes = new ArrayList();

    private ActionExpression enablementExpression;

    private int mode = UNKNOWN;

    /* package */ static class SelectionClass {
        public String className;

        public boolean recursive;

        public String nameFilter;
    }

    /**
     * Hard-wired id of the JFace text plug-in (not on pre-req chain).
     */
    private static final String JFACE_TEXT_PLUG_IN = "org.eclipse.jface.text"; //$NON-NLS-1$

    /**
     * Hard-wired fully qualified name of the text selection class (not on pre-req chain).
     */
    private static final String TEXT_SELECTION_CLASS = "org.eclipse.jface.text.ITextSelection"; //$NON-NLS-1$

    /**
     * Cached value of
     * <code>org.eclipse.jface.text.ITextSelection.class</code>;
     * <code>null</code> if not initialized or not present.
     */
    private static Class iTextSelectionClass = null;

    /**
     * Indicates whether the JFace text plug-in is even around.
     * Without the JFace text plug-in, text selections are moot.
     */
    private static boolean textSelectionPossible = true;

    /**
     * Returns <code>ITextSelection.class</code> or <code>null</code> if the
     * class is not available.
     * 
     * @return <code>ITextSelection.class</code> or <code>null</code> if class
     * not available
     * @since 3.0
     */
    private static Class getTextSelectionClass() {
        if (iTextSelectionClass != null) {
            // tried before and succeeded
            return iTextSelectionClass;
        }
        if (!textSelectionPossible) {
            // tried before and failed
            return null;
        }

        // JFace text plug-in is not on prereq chain of generic wb plug-in
        // hence: ITextSelection.class won't compile
        // and Class.forName("org.eclipse.jface.text.ITextSelection") won't find
        // it need to be trickier...
        Bundle bundle = Platform.getBundle(JFACE_TEXT_PLUG_IN);
        if (bundle == null || bundle.getState() == Bundle.UNINSTALLED) {
            // JFace text plug-in is not around, or has already
            // been removed, assume that it will never be around
            textSelectionPossible = false;
            return null;
        }

        // plug-in is around
        // it's not our job to activate the plug-in
        if (bundle.getState() == Bundle.INSTALLED) {
            // assume it might come alive later
            textSelectionPossible = true;
            return null;
        }

        try {
            Class c = bundle.loadClass(TEXT_SELECTION_CLASS); //$NON-NLS-1$
            // remember for next time
            iTextSelectionClass = c;
            return iTextSelectionClass;
        } catch (ClassNotFoundException e) {
            // unable to load ITextSelection - sounds pretty serious
            // treat as if JFace text plug-in were unavailable
            textSelectionPossible = false;
            return null;
        }
    }

    /**
     * Create a new instance of the receiver.
     * @param configElement
     */
    public SelectionEnabler(IConfigurationElement configElement) {
        super();
        if (configElement == null) {
            throw new IllegalArgumentException();
        }
        parseClasses(configElement);
    }

    /**
     * Check if the receiver is enabled for the given selection.
     * @param selection
     * @return <code>true</code> if the given selection matches the conditions 
     * specified in <code>IConfirgurationElement</code>.
     */
    public boolean isEnabledForSelection(ISelection selection) {
        // Optimize it.
        if (mode == UNKNOWN) {
            return false;
        }

        // Handle undefined selections.	
        if (selection == null) {
            selection = StructuredSelection.EMPTY;
        }

        // According to the dictionary, a selection is "one that
        // is selected", or "a collection of selected things".  
        // In reflection of this, we deal with one or a collection.

        // special case: structured selections
        if (selection instanceof IStructuredSelection) {
            return isEnabledFor((IStructuredSelection) selection);
        }

        // special case: text selections
        // Code should read
        // if (selection instanceof ITextSelection) {
        //    int count = ((ITextSelection) selection).getLength();
        //    return isEnabledFor(selection, count);
        // }
        // use Java reflection to avoid dependence of org.eclipse.jface.text
        // which is in an optional part of the generic workbench
        Class tselClass = getTextSelectionClass();
        if (tselClass != null && tselClass.isInstance(selection)) {
            try {
                Method m = tselClass.getDeclaredMethod(
                        "getLength", new Class[0]); //$NON-NLS-1$
                Object r = m.invoke(selection, new Object[0]);
                if (r instanceof Integer) {
                    return isEnabledFor(selection, ((Integer) r).intValue());
                }
                // should not happen - but enable if it does
                return true;
            } catch (NoSuchMethodException e) {
                // should not happen - fall through if it does
            } catch (IllegalAccessException e) {
                // should not happen - fall through if it does
            } catch (InvocationTargetException e) {
                // should not happen - fall through if it does
            }
        }

        // all other cases
        return isEnabledFor(selection);
    }

    /**
     * Compare selection count with requirements.
     */
    private boolean verifySelectionCount(int count) {
        if (count > 0 && mode == NONE)
            return false;
        if (count == 0 && mode == ONE_OR_MORE)
            return false;
        if (count > 1 && mode == NONE_OR_ONE)
            return false;
        if (count < 2 && mode == MULTIPLE)
            return false;
        if (mode > 0 && count != mode)
            return false;
        return true;
    }

    /**
     * Returns true if given structured selection matches the
     * conditions specified in the registry for
     * this action.
     */
    private boolean isEnabledFor(ISelection sel) {
        Object obj = sel;
        int count = sel.isEmpty() ? 0 : 1;

        if (verifySelectionCount(count) == false)
            return false;

        // Compare selection to enablement expression.
        if (enablementExpression != null)
            return enablementExpression.isEnabledFor(obj);

        // Compare selection to class requirements.
        if (classes.isEmpty())
            return true;
        if (obj instanceof IAdaptable) {
            IAdaptable element = (IAdaptable) obj;
            if (verifyElement(element) == false)
                return false;
        } else {
            return false;
        }

        return true;
    }

    /**
     * Returns true if given text selection matches the
     * conditions specified in the registry for this action.
     */
    private boolean isEnabledFor(ISelection sel, int count) {
        if (verifySelectionCount(count) == false)
            return false;

        // Compare selection to enablement expression.
        if (enablementExpression != null)
            return enablementExpression.isEnabledFor(sel);

        // Compare selection to class requirements.
        if (classes.isEmpty())
            return true;
        for (int i = 0; i < classes.size(); i++) {
            SelectionClass sc = (SelectionClass) classes.get(i);
            if (verifyClass(sel, sc.className))
                return true;
        }
        return false;
    }

    /**
     * Returns true if given structured selection matches the
     * conditions specified in the registry for
     * this action.
     */
    private boolean isEnabledFor(IStructuredSelection ssel) {
        int count = ssel.size();

        if (verifySelectionCount(count) == false)
            return false;

        // Compare selection to enablement expression.
        if (enablementExpression != null)
            return enablementExpression.isEnabledFor(ssel);

        // Compare selection to class requirements.
        if (classes.isEmpty())
            return true;
        for (Iterator elements = ssel.iterator(); elements.hasNext();) {
            Object obj = elements.next();
            if (obj instanceof IAdaptable) {
                IAdaptable element = (IAdaptable) obj;
                if (verifyElement(element) == false)
                    return false;
            } else {
                return false;
            }
        }

        return true;
    }

    /**
     * Parses registry element to extract mode
     * and selection elements that will be used
     * for verification.
     */
    private void parseClasses(IConfigurationElement config) {
        // Get enables for.
        String enablesFor = config
                .getAttribute(IWorkbenchRegistryConstants.ATT_ENABLES_FOR);
        if (enablesFor == null)
            enablesFor = "*"; //$NON-NLS-1$
        if (enablesFor.equals("*")) //$NON-NLS-1$
            mode = ANY_NUMBER;
        else if (enablesFor.equals("?")) //$NON-NLS-1$
            mode = NONE_OR_ONE;
        else if (enablesFor.equals("!")) //$NON-NLS-1$
            mode = NONE;
        else if (enablesFor.equals("+")) //$NON-NLS-1$
            mode = ONE_OR_MORE;
        else if (enablesFor.equals("multiple") //$NON-NLS-1$
 enablesFor.equals("2+")) //$NON-NLS-1$
            mode = MULTIPLE;
        else {
            try {
                mode = Integer.parseInt(enablesFor);
            } catch (NumberFormatException e) {
                mode = UNKNOWN;
            }
        }

        // Get enablement block.					
        IConfigurationElement[] children = config
                .getChildren(IWorkbenchRegistryConstants.TAG_ENABLEMENT);
        if (children.length > 0) {
            enablementExpression = new ActionExpression(children[0]);
            return;
        }

        // Get selection block.
        children = config.getChildren(IWorkbenchRegistryConstants.TAG_SELECTION);
        if (children.length > 0) {
            classes = new ArrayList();
            for (int i = 0; i < children.length; i++) {
                IConfigurationElement sel = children[i];
                String cname = sel.getAttribute(IWorkbenchRegistryConstants.ATT_CLASS);
                String name = sel.getAttribute(IWorkbenchRegistryConstants.ATT_NAME);
                SelectionClass sclass = new SelectionClass();
                sclass.className = cname;
                sclass.nameFilter = name;
                classes.add(sclass);
            }
        }
    }

    /**
     * Verifies if the element is an instance of a class
     * with a given class name. If direct match fails,
     * implementing interfaces will be tested,
     * then recursively all superclasses and their
     * interfaces.
     */
    private boolean verifyClass(Object element, String className) {
        Class eclass = element.getClass();
        Class clazz = eclass;
        boolean match = false;
        while (clazz != null) {
            // test the class itself
            if (clazz.getName().equals(className)) {
                match = true;
                break;
            }
            // test all the interfaces it implements
            Class[] interfaces = clazz.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                if (interfaces[i].getName().equals(className)) {
                    match = true;
                    break;
                }
            }
            if (match == true)
                break;
            // get the superclass
            clazz = clazz.getSuperclass();
        }
        return match;
    }

    /**
     * Verifies if the given element matches one of the
     * selection requirements. Element must at least pass
     * the type test, and optionally wildcard name match.
     */
    private boolean verifyElement(IAdaptable element) {
        if (classes.isEmpty())
            return true;
        for (int i = 0; i < classes.size(); i++) {
            SelectionClass sc = (SelectionClass) classes.get(i);
            if (verifyClass(element, sc.className) == false)
                continue;
            if (sc.nameFilter == null)
                return true;
            IWorkbenchAdapter de = (IWorkbenchAdapter) element
                    .getAdapter(IWorkbenchAdapter.class);
            if ((de != null)
                    && verifyNameMatch(de.getLabel(element), sc.nameFilter))
                return true;
        }
        return false;
    }

    /**
     * Verifies that the given name matches the given
     * wildcard filter. Returns true if it does.
     * @param name
     * @param filter
     * @return <code>true</code> if there is a match
     */
    public static boolean verifyNameMatch(String name, String filter) {
        return SimpleWildcardTester.testWildcardIgnoreCase(filter, name);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7760.java