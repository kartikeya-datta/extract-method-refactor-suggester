error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9377.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9377.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9377.java
text:
```scala
d@@efaultFont = new FontData [] {StringConverter.asFontData(definition.getValue(), PreferenceConverter.FONTDATA_DEFAULT_DEFAULT)};

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
package org.eclipse.ui.internal.presentation;

import java.util.Arrays;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.GradientData;
import org.eclipse.jface.resource.GradientRegistry;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;


/**
 * @since 3.0
 */
public final class PresentationRegistryPopulator {

    public static void populateRegistry(FontRegistry registry, FontDefinition [] definitions, IPreferenceStore store) {
		// sort the definitions by dependant ordering so that we process 
		// ancestors before children.		
		FontDefinition [] copyOfDefinitions = new FontDefinition[definitions.length];
		System.arraycopy(definitions, 0, copyOfDefinitions, 0, definitions.length);
		Arrays.sort(copyOfDefinitions, FontDefinition.HIERARCHY_COMPARATOR);

		for (int i = 0; i < copyOfDefinitions.length; i++) {
			FontDefinition definition = copyOfDefinitions[i];
			installFont(definition, registry, store);
		}
    }
    
    
    /**
     * @param definition
     * @param registry
     * @param store
     */
    private static void installFont(FontDefinition definition, FontRegistry registry, IPreferenceStore store) {
		String id = definition.getId();
		FontData [] prefFont = store != null ? PreferenceConverter.getFontDataArray(store, id) : null;
		FontData [] defaultFont = null;
		if (definition.getValue() != null)
		    defaultFont = new FontData [] {StringConverter.asFontData(definition.getValue(), null)};
		else if (definition.getDefaultsTo() != null)
		    defaultFont = registry.getFontData(definition.getDefaultsTo());
		else
		    defaultFont = PreferenceConverter.FONTDATA_ARRAY_DEFAULT_DEFAULT;
		    
		
		if (prefFont == null || prefFont == PreferenceConverter.FONTDATA_ARRAY_DEFAULT_DEFAULT) {
		    prefFont = defaultFont;
		}
		
		if (defaultFont != null && store != null) {
			PreferenceConverter.setDefault(
					store, 
					id, 
					defaultFont);
		}

		
		if (prefFont != null) {		    
			registry.put(id, prefFont);
		}
    }


    public static void populateRegistry(GradientRegistry registry, GradientDefinition [] definitions, IPreferenceStore store) {		
		for (int i = 0; i < definitions.length; i++) {
			installGradient(definitions[i], registry, store);
		}        
    }
        
    
    /**
     * @param definition
     * @param registry
     * @param store
     */
    private static void installGradient(GradientDefinition definition, GradientRegistry registry, IPreferenceStore store) {
		String id = definition.getId();
		GradientData prefGradient = store != null ? PreferenceConverter.getGradient(store, id) : null;
		
		String[] values = definition.getValues();
        RGB [] rgbs = new RGB[values.length];
		for (int i = 0; i < rgbs.length; i++) {
            rgbs[i] = StringConverter.asRGB(values[i], null);
        }
        
        GradientData defaultGradient = new GradientData(rgbs, definition.getPercentages(), definition.getDirection());		
		
		if (prefGradient == null || prefGradient == PreferenceConverter.GRADIENT_DEFAULT_DEFAULT) {
		    prefGradient = defaultGradient;
		}
		
		if (defaultGradient != null && store != null) {
			PreferenceConverter.setDefault(
					store, 
					id, 
					defaultGradient);
		}
		
		if (prefGradient != null) {		    
			registry.put(id, prefGradient);
		}
    }

    public static void populateRegistry(ColorRegistry registry, ColorDefinition [] definitions, IPreferenceStore store) {
		// sort the definitions by dependant ordering so that we process 
		// ancestors before children.		
		ColorDefinition [] copyOfDefinitions = new ColorDefinition[definitions.length];
		System.arraycopy(definitions, 0, copyOfDefinitions, 0, definitions.length);
		Arrays.sort(copyOfDefinitions, ColorDefinition.HIERARCHY_COMPARATOR);

		for (int i = 0; i < copyOfDefinitions.length; i++) {
			ColorDefinition definition = copyOfDefinitions[i];
			installColor(definition, registry, store);
		}        
    }
    
	/**
	 * Installs the given color in the color registry.
	 * 
	 * @param definition
	 *            the color definition
	 * @param registry
	 *            the color registry
	 * @param store
	 *            the preference store from which to set and obtain color data
	 */
	private static void installColor(
		ColorDefinition definition,
		ColorRegistry registry,
		IPreferenceStore store) {
				
		String id = definition.getId();
		RGB prefColor = store != null ? PreferenceConverter.getColor(store, id) : null;
		RGB defaultColor = null;
		if (definition.getValue() != null)
		    defaultColor = StringConverter.asRGB(definition.getValue(), null);
		else 
		    defaultColor = registry.getRGB(definition.getDefaultsTo());
		
		if (prefColor == null || prefColor == PreferenceConverter.COLOR_DEFAULT_DEFAULT) {
		    prefColor = defaultColor;
		}
		
		if (defaultColor != null && store != null) {
			PreferenceConverter.setDefault(
					store, 
					id, 
					defaultColor);
		}

		
		if (prefColor != null) {		    
			registry.put(id, prefColor);
		}
	}

    
    /**
     * Not intended to be instantiated.
     */
    private PresentationRegistryPopulator() {
        // no-op
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9377.java