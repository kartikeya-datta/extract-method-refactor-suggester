error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14788.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14788.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14788.java
text:
```scala
final I@@ConfigurationElement[] confEl = RegistryFactory.getRegistry().getConfigurationElementsFor ("org.eclipse.xtend.backend.MiddleEnd");

/*
Copyright (c) 2008 Arno Haase.
All rights reserved. This program and the accompanying materials
are made available under the terms of the Eclipse Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/epl-v10.html

Contributors:
    Arno Haase - initial API and implementation
 */
package org.eclipose.xtend.middleend.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipose.xtend.middleend.plugins.LanguageSpecificMiddleEnd;
import org.eclipose.xtend.middleend.plugins.LanguageSpecificMiddleEndFactory;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.RegistryFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


/**
 * 
 * @author Arno Haase (http://www.haase-consulting.com)
 */
public class Activator implements BundleActivator {
    private static final Log _log = LogFactory.getLog (Activator.class);
    
    private static Activator _instance = null;
    
    public static Activator getInstance () {
        return _instance;
    }

    private final List<LanguageSpecificMiddleEndFactory> _middleEndContributions = new ArrayList<LanguageSpecificMiddleEndFactory> ();
    private boolean _isInitialized = false;

    
    public List<LanguageSpecificMiddleEnd> getFreshMiddleEnds (Map<Class<?>, Object> specificParams) {
        init ();
        
        final List<LanguageSpecificMiddleEnd> result = new ArrayList<LanguageSpecificMiddleEnd>();
        
        for (LanguageSpecificMiddleEndFactory factory: _middleEndContributions) {
            try {
                result.add (factory.create (specificParams.get (factory.getClass())));
            }
            catch (IllegalArgumentException exc) {
                // this is the official way for an implementation to withdraw from the pool for this call
                _log.debug ("middle end implementation " + factory.getName() + " says it is not available: " + exc.getMessage());
            }
        }
        
        return result;
    }
    
    public void start (BundleContext context) throws Exception {
        //TODO Bernd: implement error handling and logging to be both robust and independent of Eclipse
        
        _isInitialized = false;
        _instance = this;
    }
    
    private void init () {
        if (_isInitialized)
            return;
        
        _isInitialized = true;
        _middleEndContributions.clear ();

        try {
            final IConfigurationElement[] confEl = RegistryFactory.getRegistry().getConfigurationElementsFor ("org.eclipse.xtend.middleend.MiddleEnd");

            for (IConfigurationElement curEl: confEl) {
                final Object o = curEl.createExecutableExtension ("class");
                _middleEndContributions.add ((LanguageSpecificMiddleEndFactory) o);
            }
        }
        catch (Exception exc) {
            exc.printStackTrace ();
        }
        
        Collections.sort (_middleEndContributions, new Comparator <LanguageSpecificMiddleEndFactory> () {
            public int compare (LanguageSpecificMiddleEndFactory o1, LanguageSpecificMiddleEndFactory o2) {
                return o1.getPriority() - o2.getPriority();
            }
        });
        
        _log.info ("Activating Eclipse Modeling Middle End - the following middle ends are registered:");
        for (LanguageSpecificMiddleEndFactory factory: _middleEndContributions)
            _log.info ("  " + factory.getName());
    }

    public void stop (BundleContext context) throws Exception {
        _instance = null;
        _middleEndContributions.clear();
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14788.java