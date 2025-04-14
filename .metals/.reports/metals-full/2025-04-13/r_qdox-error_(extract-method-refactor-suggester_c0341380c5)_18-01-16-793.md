error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4932.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4932.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4932.java
text:
```scala
S@@tring[] pds = Services.getImplementors(ProductDerivation.class, cl);

/*
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.openjpa.lib.conf;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.MissingResourceException;

import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.lib.util.Services;

/**
 * Utilities for running product derivations.
 *
 * @author Abe White
 * @nojavadoc
 */
public class ProductDerivations {

    private static final ProductDerivation[] _derivations;
    private static final String[] _prefixes;
    static {
        ClassLoader cl = ProductDerivation.class.getClassLoader();
        String pds = Services.getImplementors(ProductDerivation.class, cl);
        List derivations = new ArrayList(pds.length);
        for (int i = 0; i < pds.length; i++) {
            try {
                Class cls = Class.forName(pds[i], true, cl);
                derivations.add(cls.newInstance());
            } catch (Throwable t) {
                Localizer loc = Localizer.forPackage(ProductDerivations.class);
                System.err.println(loc.get("bad-product-derivation", pds[i], 
                    t));
            }
        }

        // must be at least one product derivation to define metadata factories,
        // etc. 
        if (derivations.isEmpty()) {
            Localizer loc = Localizer.forPackage(ProductDerivations.class);
            throw new MissingResourceException(loc.get("no-product-derivations",
                ProductDerivation.class.getName()).getMessage(),
                ProductDerivations.class.getName(), "derivations");
        }

        Collections.sort(derivations, new ProductDerivationComparator());
        _derivations = (ProductDerivation[]) derivations.toArray
            (new ProductDerivation[derivations.size()]);

        List prefixes = new ArrayList(2);
        for (int i = 0; i < _derivations.length; i++) {
            if (_derivations[i].getConfigurationPrefix() != null
                && !"openjpa".equals(_derivations[i].getConfigurationPrefix()))
                prefixes.add(_derivations[i].getConfigurationPrefix());
        }
        _prefixes = new String[1 + prefixes.size()];
        _prefixes[0] = "openjpa";
        for (int i = 0; i < prefixes.size(); i++)
            _prefixes[i + 1] = (String) prefixes.get(i);
    }

    /**
     * Return the recognized prefixes for configuration properties.
     */
    public static String[] getConfigurationPrefixes() {
        return _prefixes;
    }

    /**
     * Apply {@link ProductDerivation#beforeConfigurationConstruct} callbacks
     * to the the given instance. Exceptions are swallowed.
     */
    public static void beforeConfigurationConstruct(ConfigurationProvider cp) {
        for (int i = 0; i < _derivations.length; i++) {
            try {
                _derivations[i].beforeConfigurationConstruct(cp);
            } catch (Exception e) {
                // can't log; no configuration yet
                e.printStackTrace();
            }
        }
    }

    /**
     * Apply {@link ProductDerivation#beforeConfigurationLoad} callbacks
     * to the the given instance. Exceptions are swallowed.
     */
    public static void beforeConfigurationLoad(Configuration conf) {
        for (int i = 0; i < _derivations.length; i++) {
            try {
                _derivations[i].beforeConfigurationLoad(conf);
            } catch (Exception e) {
                // logging not configured yet
                e.printStackTrace();
            }
        }
    }

    /**
     * Apply {@link ProductDerivation#afterSpecificationSet} callbacks
     * to the the given instance. Exceptions are swallowed.
     */
    public static void afterSpecificationSet(Configuration conf) {
        for (int i = 0; i < _derivations.length; i++) {
            try {
                _derivations[i].afterSpecificationSet(conf);
            } catch (Exception e) {
                // logging not configured yet
                e.printStackTrace();
            }
        }
    }

    /**
     * Load the given given resource, or return false if it is not a resource
     * this provider understands. The given class loader may be null.
     *
     * @param anchor optional named anchor within a multiple-configuration
     * resource
     */
    public static ConfigurationProvider load(String resource, String anchor, 
        ClassLoader loader) {
        if (resource == null || resource.length() == 0)
            return null;
        if (loader == null)
            loader = Thread.currentThread().getContextClassLoader();
        ConfigurationProvider provider = null;
        StringBuffer errs = null;
        // most specific to least
        for (int i = _derivations.length - 1; i >= 0; i--) {
            try {
                provider = _derivations[i].load(resource, anchor, loader);
                if (provider != null)
                    return provider;
            } catch (Throwable t) {
                errs = (errs == null) ? new StringBuffer() : errs.append("\n");
                errs.append(_derivations[i].getClass().getName() + ":" + t);
            }
        }
        reportErrors(errs, resource);
        throw new MissingResourceException(resource, 
            ProductDerivations.class.getName(), resource);
    }

    /**
     * Load given file, or return false if it is not a file this provider
     * understands.
     *
     * @param anchor optional named anchor within a multiple-configuration file
     */
    public static ConfigurationProvider load(File file, String anchor, 
        ClassLoader loader) {
        if (file == null)
            return null;
        if (loader == null)
            loader = Thread.currentThread().getContextClassLoader();
        ConfigurationProvider provider = null;
        StringBuffer errs = null;
        // most specific to least
        for (int i = _derivations.length - 1; i >= 0; i--) {
            try {
                provider = _derivations[i].load(file, anchor);
                if (provider != null)
                    return provider;
            } catch (Throwable t) {
                errs = (errs == null) ? new StringBuffer() : errs.append("\n");
                errs.append(_derivations[i].getClass().getName() + ":" + t);
            }
        }
        reportErrors(errs, file.getAbsolutePath());
        throw new MissingResourceException(file.getAbsolutePath(), 
            ProductDerivations.class.getName(), file.getAbsolutePath());
    }
   
    /**
     * Return a {@link ConfigurationProvider} that has parsed system defaults.
     */
    public static ConfigurationProvider loadDefaults(ClassLoader loader) {
        return load(loader, false);
    }

    /**
     * Return a {@link ConfigurationProvider} that has parsed system globals.
     */
    public static ConfigurationProvider loadGlobals(ClassLoader loader) {
        return load(loader, true);
    }
            
    /**
     * Load a built-in resource location.
     */
    private static ConfigurationProvider load(ClassLoader loader, 
       boolean globals) {
        if (loader == null)
            loader = Thread.currentThread().getContextClassLoader();
        
        ConfigurationProvider provider = null;
        StringBuffer errs = null;
        String type = (globals) ? "globals" : "defaults";
        // most specific to least
        for (int i = _derivations.length - 1; i >= 0; i--) {
            try {
                provider = (globals) ? _derivations[i].loadGlobals(loader) 
                    : _derivations[i].loadDefaults(loader);
                if (provider != null)
                   return provider;
            } catch (Throwable t) {
                errs = (errs == null) ? new StringBuffer() : errs.append("\n");
                errs.append(_derivations[i].getClass().getName() + ":" + t);
            }
        }
        reportErrors(errs, type);
        return null;
    }
 
    /**
     * Thrown proper exception for given errors.
     */
    private static void reportErrors(StringBuffer errs, String resource) {
        if (errs == null)
            return;
        throw new MissingResourceException(errs.toString(), 
            ProductDerivations.class.getName(), resource);
    }

    /**
     * Compare {@link ProductDerivation}s.
     */
    private static class ProductDerivationComparator
        implements Comparator {

        public int compare(Object o1, Object o2) {
            int type1 = ((ProductDerivation) o1).getType();
            int type2 = ((ProductDerivation) o2).getType();
            if (type1 != type2)
                return type1 - type2;

            // arbitrary but consistent order
            return o1.getClass().getName().compareTo(o2.getClass().
                getName());
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4932.java