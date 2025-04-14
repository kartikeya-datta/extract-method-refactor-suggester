error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10005.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10005.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10005.java
text:
```scala
private C@@ollection values = new ArrayList();

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.decorators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.IWorkbenchConstants;
import org.eclipse.ui.internal.registry.RegistryReader;

/**
 * The DecoratorRegistryReader is the class that reads the
 * decorator descriptions from the registry
 */

public class DecoratorRegistryReader extends RegistryReader {

    //The registry values are the ones read from the registry
    private Collection values = new ArrayList();;

    private Collection ids = new HashSet();

    private static final String ATT_ID = "id"; //$NON-NLS-1$    

    private static final String P_TRUE = "true"; //$NON-NLS-1$


    private static final String ATT_LIGHTWEIGHT = "lightweight"; //$NON-NLS-1$


    /**
     * Constructor for DecoratorRegistryReader.
     */
    public DecoratorRegistryReader() {
        super();
    }

    /*
     * @see RegistryReader#readElement(IConfigurationElement)
     */
    public boolean readElement(IConfigurationElement element) {

        //String name = element.getAttribute(ATT_LABEL);

        String id = element.getAttribute(ATT_ID);
        if (ids.contains(id)) {
            logDuplicateId(element);
            return false;
        }
        ids.add(id);

        boolean noClass = element.getAttribute(DecoratorDefinition.ATT_CLASS) == null;

        DecoratorDefinition desc = null;
        //Lightweight or Full? It is lightweight if it is declared lightweight or if there is no class
        if (P_TRUE.equals(element.getAttribute(ATT_LIGHTWEIGHT)) || noClass) {

            String iconPath = element.getAttribute(LightweightDecoratorDefinition.ATT_ICON);

            if (noClass && iconPath == null) {
                logMissingElement(element, LightweightDecoratorDefinition.ATT_ICON);
                return false;
            }

            desc = new LightweightDecoratorDefinition(id, element);
        } else {
            desc = new FullDecoratorDefinition(id, element);
        }
        
        if (desc.getEnablement() == null) {
            logMissingElement(element, DecoratorDefinition.CHILD_ENABLEMENT);
        	return false;
        }
        values.add(desc);

        return true;

    }

    /**
     * Read the decorator extensions within a registry and set 
     * up the registry values.
     */
    Collection readRegistry(IExtensionRegistry in) {
        values.clear();
        ids.clear();
        readRegistry(in, PlatformUI.PLUGIN_ID,
                IWorkbenchConstants.PL_DECORATORS);
        return values;
    }

    public Collection getValues() {
        return values;
    }

    /**
     * Logs a registry error when the configuration element is unknown.
     */
    protected void logDuplicateId(IConfigurationElement element) {
        logError(element, "Duplicate id found: " + element.getAttribute(ATT_ID));//$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10005.java