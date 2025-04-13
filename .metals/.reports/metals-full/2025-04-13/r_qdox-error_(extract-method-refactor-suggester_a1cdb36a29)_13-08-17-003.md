error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10805.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10805.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10805.java
text:
```scala
i@@nt clsEntry, utfEntry;

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
package org.apache.openjpa.enhance;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Set;

import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.lib.log.Log;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.lib.util.Options;
import org.apache.openjpa.meta.MetaDataRepository;
import org.apache.openjpa.util.GeneralException;
import serp.bytecode.Project;
import serp.bytecode.lowlevel.ConstantPoolTable;

/**
 * Transformer that makes persistent classes implement the
 * {@link PersistenceCapable} interface at runtime.
 *
 * @author Abe White
 * @nojavadoc
 */
public class PCClassFileTransformer
    implements ClassFileTransformer {

    private static final Localizer _loc = Localizer.forPackage
        (PCClassFileTransformer.class);

    private final MetaDataRepository _repos;
    private final PCEnhancer.Flags _flags;
    private final ClassLoader _loader;
    private final Log _log;
    private final Set _names;

    /**
     * Constructor.
     *
     * @param repos metadata repository to use internally
     * @param opts enhancer configuration options
     * @param loader temporary class loader for loading intermediate classes
     */
    public PCClassFileTransformer(MetaDataRepository repos, Options opts,
        ClassLoader loader) {
        this(repos, toFlags(opts), loader, opts.removeBooleanProperty
            ("scanDevPath", "ScanDevPath", false));
    }

    /**
     * Create enhancer flags from the given options.
     */
    private static PCEnhancer.Flags toFlags(Options opts) {
        PCEnhancer.Flags flags = new PCEnhancer.Flags();
        flags.addDefaultConstructor = opts.removeBooleanProperty
            ("addDefaultConstructor", "AddDefaultConstructor",
                flags.addDefaultConstructor);
        flags.enforcePropertyRestrictions = opts.removeBooleanProperty
            ("enforcePropertyRestrictions", "EnforcePropertyRestrictions",
                flags.enforcePropertyRestrictions);
        return flags;
    }

    /**
     * Constructor.
     *
     * @param repos metadata repository to use internally
     * @param flags enhancer configuration
     * @param loader temporary class loader for loading intermediate classes
     * @param devscan whether to scan the dev classpath for persistent types
     * if none are configured
     */
    public PCClassFileTransformer(MetaDataRepository repos,
        PCEnhancer.Flags flags, ClassLoader loader, boolean devscan) {
        _repos = repos;
        _log =
            repos.getConfiguration().getLog(OpenJPAConfiguration.LOG_ENHANCE);
        _flags = flags;
        _loader = loader;

        _names = repos.getPersistentTypeNames(devscan, loader);
        if (_names == null && _log.isInfoEnabled())
            _log.info(_loc.get("runtime-enhance-pcclasses"));
    }

    public byte[] transform(ClassLoader loader, String className,
        Class redef, ProtectionDomain domain, byte[] bytes)
        throws IllegalClassFormatException {
        if (loader == _loader)
            return null;

        try {
            Boolean enhance = needsEnhance(className, redef, bytes);
            if (enhance != null && _log.isTraceEnabled())
                _log.trace(_loc.get("needs-runtime-enhance", className,
                    enhance));
            if (enhance != Boolean.TRUE)
                return null;

            PCEnhancer enhancer = new PCEnhancer(_repos.getConfiguration(),
                new Project().loadClass(new ByteArrayInputStream(bytes),
                    _loader), _repos);
            enhancer.setAddDefaultConstructor(_flags.addDefaultConstructor);
            enhancer.setEnforcePropertyRestrictions
                (_flags.enforcePropertyRestrictions);

            if (enhancer.run() == PCEnhancer.ENHANCE_NONE)
                return null;
            return enhancer.getBytecode().toByteArray();
        } catch (Throwable t) {
            _log.warn(_loc.get("cft-exception-thrown", className), t);
            if (t instanceof RuntimeException)
                throw (RuntimeException) t;
            if (t instanceof IllegalClassFormatException)
                throw (IllegalClassFormatException) t;
            throw new GeneralException(t);
        }
    }

    /**
     * Return whether the given class needs enhancement.
     */
    private Boolean needsEnhance(String clsName, Class redef, byte[] bytes) {
        if (redef != null) {
            Class[] intfs = redef.getInterfaces();
            for (int i = 0; i < intfs.length; i++)
                if (PersistenceCapable.class.getName().
                    equals(intfs[i].getName()))
                    return Boolean.valueOf(!isEnhanced(bytes));
            return null;
        }

        if (_names != null) {
            if (_names.contains(clsName.replace('/', '.')))
                return Boolean.valueOf(!isEnhanced(bytes));
            return null;
        }

        if (clsName.startsWith("java/") || clsName.startsWith("javax/"))
            return null;
        if (isEnhanced(bytes))
            return Boolean.FALSE;

        try {
            Class c = Class.forName(clsName.replace('/', '.'), false, _loader);
            if (_repos.getMetaData(c, null, false) != null)
                return Boolean.TRUE;
            return null;
        } catch (RuntimeException re) {
            throw re;
        } catch (Throwable t) {
            throw new GeneralException(t);
        }
    }

    /**
     * Analyze the bytecode to see if the given class definition implements
     * {@link PersistenceCapable}.
     */
    private static boolean isEnhanced(byte[] b) {
        ConstantPoolTable table = new ConstantPoolTable(b);
        int idx = table.getEndIndex();

        idx += 6; // skip access, cls, super
        int ifaces = table.readUnsignedShort(idx);
        int clsEntry, utfEntry, len;
        String name;
        for (int i = 0; i < ifaces; i++) {
            idx += 2;
            clsEntry = table.readUnsignedShort(idx);
            utfEntry = table.readUnsignedShort(table.get(clsEntry));
            name = table.readString(table.get(utfEntry));
            if ("openjpa/enhance/PersistenceCapable".equals(name))
                return true;
        }
        return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10805.java