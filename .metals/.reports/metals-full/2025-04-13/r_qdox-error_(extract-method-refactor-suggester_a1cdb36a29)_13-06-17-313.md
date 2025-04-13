error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4758.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4758.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4758.java
text:
```scala
p@@ublic final class GUIFactory

/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 * "Apache JMeter" must not be used to endorse or promote products
 * derived from this software without prior written permission. For
 * written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 * "Apache JMeter", nor may "Apache" appear in their name, without
 * prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.apache.jmeter.gui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JComponent;


/**
 * Provides a way to register and retrieve GUI classes and icons.
 * 
 * @author Oliver Rossmueller
 * @version $Revision$
 */
public class GUIFactory
{
    /** A Map from String to JComponent of registered GUI classes. */
    private static final Map GUI_MAP = new HashMap();
    
    /** A Map from String to ImageIcon of registered icons. */
    private static final Map ICON_MAP = new HashMap();

    /**
     * Prevent instantiation since this is a static utility class.
     */
    private GUIFactory ()
    {
    }
    
    /**
     * Get an icon which has previously been registered for this class object.
     * 
     * @param elementClass the class object which we want to get an icon for
     * 
     * @return the associated icon, or null if this class or its superclass
     *         has not been registered
     */
    public static ImageIcon getIcon(Class elementClass)
    {
        String key = elementClass.getName();
        ImageIcon icon = (ImageIcon) ICON_MAP.get(key);

        if (icon != null)
        {
            return icon;
        }
        
        if (elementClass.getSuperclass() != null)
        {
            return getIcon(elementClass.getSuperclass());
        }
        
        return null;
    }


    /**
     * Get a component instance which has previously been registered for this
     * class object.
     * 
     * @param elementClass the class object which we want to get an instance of
     * 
     * @return an instance of the class, or null if this class or its superclass
     *         has not been registered
     */
    public static JComponent getGUI(Class elementClass)
    {
        // TODO: This method doesn't appear to be used.
        String key = elementClass.getName();
        JComponent gui = (JComponent) GUI_MAP.get(key);

        if (gui != null)
        {
            return gui;
        }

        if (elementClass.getSuperclass() != null)
        {
            return getGUI(elementClass.getSuperclass());
        }

        return null;
    }


    /**
     * Register an icon so that it can later be retrieved via
     * {@link #getIcon(Class)}.  The key should match the fully-qualified
     * class name for the class used as the parameter when retrieving the
     * icon.
     * 
     * @param key   the name which can be used to retrieve this icon later
     * @param icon  the icon to store
     */
    public static void registerIcon(String key, ImageIcon icon)
    {
        ICON_MAP.put(key, icon);
    }


    /**
     * Register a GUI class so that it can later be retrieved via
     * {@link #getGUI(Class)}.  The key should match the fully-qualified
     * class name for the class used as the parameter when retrieving the
     * GUI.
     * 
     * @param key       the name which can be used to retrieve this GUI later
     * @param guiClass  the class object for the GUI component
     * 
     * @throws InstantiationException if an instance of the GUI class can not
     *    be instantiated
     * @throws IllegalAccessException if access rights do not permit an instance
     *    of the GUI class to be created
     */
    public static void registerGUI(String key, Class guiClass)
            throws InstantiationException, IllegalAccessException
    {
        // TODO: This method doesn't appear to be used.
        JMeterGUIComponent gui = (JMeterGUIComponent) guiClass.newInstance();
        JComponent component = (JComponent) gui;
        GUI_MAP.put(key, gui);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4758.java