error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6875.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6875.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6875.java
text:
```scala
i@@f (this.description == null) {

/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.themes;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;

/**
 * Concrete implementation of a theme descriptor.
 *
 * @since 3.0
 */
public class ThemeDescriptor implements IThemeDescriptor {

    /* Theme */
    public static final String ATT_ID = "id";//$NON-NLS-1$

    private static final String ATT_NAME = "name";//$NON-NLS-1$	

    private Collection colors = new HashSet();

    private String description;

    private Collection fonts = new HashSet();

    private String id;

    private String name;

    private Map dataMap = new HashMap();

    /**
     * Create a new ThemeDescriptor
     * @param id
     */
    public ThemeDescriptor(String id) {
        this.id = id;
    }

    /**
     * Add a color override to this descriptor.
     * 
     * @param definition the definition to add
     */
    void add(ColorDefinition definition) {
        if (colors.contains(definition)) {
			return;
		}
        colors.add(definition);
    }

    /**
     * Add a font override to this descriptor.
     * 
     * @param definition the definition to add
     */
    void add(FontDefinition definition) {
        if (fonts.contains(definition)) {
			return;
		}
        fonts.add(definition);
    }

    /**
     * Add a data object to this descriptor.
     * 
     * @param key the key
     * @param data the data
     */
    void setData(String key, Object data) {
        if (dataMap.containsKey(key)) {
			return;
		}
            
        dataMap.put(key, data);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.themes.IThemeDescriptor#getColorOverrides()
     */
    public ColorDefinition[] getColors() {
        ColorDefinition[] defs = (ColorDefinition[]) colors
                .toArray(new ColorDefinition[colors.size()]);
        Arrays.sort(defs, IThemeRegistry.ID_COMPARATOR);
        return defs;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.themes.IThemeElementDefinition#getDescription()
     */
    public String getDescription() {
        return description;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.themes.IThemeDescriptor#getFontOverrides()
     */
    public FontDefinition[] getFonts() {
        FontDefinition[] defs = (FontDefinition[]) fonts
                .toArray(new FontDefinition[fonts.size()]);
        Arrays.sort(defs, IThemeRegistry.ID_COMPARATOR);
        return defs;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.registry.IThemeDescriptor#getID()
     */
    public String getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.registry.IThemeDescriptor#getName()
     */
    public String getName() {
        return name;
    }

    /*
     * load the name if it is not already set.
     */
    void extractName(IConfigurationElement configElement) {
        if (name == null) {
			name = configElement.getAttribute(ATT_NAME);
		}
    }

    /**
     * Set the description.
     * 
     * @param description the description
     */
    void setDescription(String description) {
        if (description == null) {
			this.description = description;
		}
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.themes.IThemeDescriptor#getData()
     */
    public Map getData() {
        return Collections.unmodifiableMap(dataMap);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6875.java