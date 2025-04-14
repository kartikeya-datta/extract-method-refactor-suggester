error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7985.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7985.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7985.java
text:
```scala
C@@lass clazz = SWT.class;

/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.themes;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Useful color utilities.
 * 
 * @since 3.0
 */
public final class ColorUtils {

    /**
     * @param value the SWT constant <code>String</code>.
     * @return the value of the SWT constant, or <code>SWT.COLOR_BLACK</code> if it could 
     * not be determined.
     */
    private static RGB process(String value) {
        try {
            Class clazz = SWT.class; //$NON-NLS-1$
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if (field.getType() == Integer.TYPE
                        && Modifier.isStatic(field.getModifiers())
                        && Modifier.isPublic(field.getModifiers())
                        && Modifier.isFinal(field.getModifiers())) {
                    if (value.equals(field.getName())) {
                        return getSystemColor(field.getInt(null));
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            // no op - shouldnt happen.  We check for static before calling getInt(null)
        } catch (IllegalAccessException e) {
            // no op - shouldnt happen.  We check for public before calling getInt(null)
        }
        return getSystemColor(SWT.COLOR_BLACK);
    }

    /**
     * Blend the two color values returning a value that is halfway between them.
     * 
     * @param val1 the first value
     * @param val2 the second value
     * @return the blended color
     */
    public static RGB blend(RGB val1, RGB val2) {
        int red = blend(val1.red, val2.red);
        int green = blend(val1.green, val2.green);
        int blue = blend(val1.blue, val2.blue);
        return new RGB(red, green, blue);
    }

    /**
     * Blend the two color values returning a value that is halfway between them.
     * 
     * @param temp1 the first value
     * @param temp2 the second value
     * @return the blended int value
     */
    private static int blend(int temp1, int temp2) {
        return (Math.abs(temp1 - temp2) / 2) + Math.min(temp1, temp2);
    }

    /**
     * @param colorId the system color identifier.
     * @return the RGB value of the supplied system color.
     */
    private static RGB getSystemColor(int colorId) {
        return Display.getCurrent().getSystemColor(colorId).getRGB();
    }

    /**
     * Get the RGB value for a given color.
     * 
     * @param rawValue the raw value, either an RGB triple or an SWT constant.
     * @return Returns the RGB value.
     */
    public static RGB getColorValue(String rawValue) {
        if (rawValue == null)
            return null;

        rawValue = rawValue.trim();

        if (!isDirectValue(rawValue)) {
            return process(rawValue);
        }

        return StringConverter.asRGB(rawValue);
    }

    /**
     * Get the RGB values for a given color array.
     * 
     * @param rawValues the raw values, either RGB triple or an SWT constant.
     * @return Returns the RGB values.
     */
    public static RGB[] getColorValues(String[] rawValues) {
        RGB[] values = new RGB[rawValues.length];
        for (int i = 0; i < rawValues.length; i++) {
            values[i] = getColorValue(rawValues[i]);
        }
        return values;
    }

    /** 
     * @return whether the value returned by <code>getValue()</code> is already 
     * in RGB form. 
     */
    private static boolean isDirectValue(String rawValue) {
    	return rawValue.indexOf(',') >= 0;
    }

    /**
     * Not intended to be instantiated.
     */
    private ColorUtils() {
        //no-op
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7985.java