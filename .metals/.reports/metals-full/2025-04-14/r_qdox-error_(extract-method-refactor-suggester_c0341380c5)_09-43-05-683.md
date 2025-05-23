error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/356.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/356.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/356.java
text:
```scala
(@@fmd.getName()), (Class[]) null);

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
package org.apache.openjpa.meta;

import java.lang.reflect.Method;
import java.io.ByteArrayInputStream;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.WeakHashMap;


import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.enhance.PCEnhancer;
import org.apache.openjpa.util.InternalException;
import org.apache.openjpa.lib.util.Localizer;
import serp.bytecode.BCClass;
import serp.bytecode.BCClassLoader;
import serp.bytecode.BCField;
import serp.bytecode.BCMethod;
import serp.bytecode.Code;
import serp.bytecode.Constants;
import serp.bytecode.Project;


/**
 * Creates implementations of managed interfaces.  Will throw exceptions
 * on unknown properties.
 *
 * @author Steve Kim
 */
class InterfaceImplGenerator {
    private static final Localizer _loc = Localizer.forPackage
        (InterfaceImplGenerator.class);
    private static final String POSTFIX = "openjpaimpl";

    private final MetaDataRepository _repos;
    private final Map _impls = new WeakHashMap();
    private final Project _project = new Project();
    private final BCClassLoader _loader = new BCClassLoader(_project);

    // distinct project / loader for enhanced version of class
    private final Project _enhProject = new Project();
    private final BCClassLoader _enhLoader = new BCClassLoader(_enhProject);

    /**
     * Constructor.  Supply repository.
     */
    public InterfaceImplGenerator(MetaDataRepository repos) {
        _repos = repos;
    }

    /**
     * Create a concrete implementation of the given type, possibly
     * returning a cached version of the class.
     */
    public synchronized Class createImpl(ClassMetaData meta) {
        Class iface = meta.getDescribedType();

        // check cache.
        Class impl = (Class) _impls.get(iface);
        if (impl != null)
            return impl;

        BCClass bc = _project.loadClass(getClassName(meta));
        bc.declareInterface(iface);
        ClassMetaData sup = meta.getPCSuperclassMetaData();
        if (sup != null)
            bc.setSuperclass(sup.getInterfaceImpl());

        FieldMetaData[] fields = meta.getDeclaredFields();
        Set methods = new HashSet();
        for (int i = 0; i < fields.length; i++) 
            addField(bc, iface, fields[i], methods);
        invalidateNonBeanMethods(bc, iface, methods);

        // first load the base class as the enhancer requires the class
        // to be available
        try {
            meta.setInterfaceImpl(Class.forName(bc.getName(), true, _loader));
        } catch (Throwable t) {
            throw new InternalException(_loc.get("interface-load"), t).
                setFatal(true);
        }
        // copy the BCClass into the enhancer project.
        bc = _enhProject.loadClass(new ByteArrayInputStream(bc.toByteArray()), 
            _loader);
        PCEnhancer enhancer = new PCEnhancer(_repos.getConfiguration(), bc, 
            meta);

        int result = enhancer.run();
        if (result != PCEnhancer.ENHANCE_PC)
            throw new InternalException(_loc.get("interface-badenhance", 
                iface)).setFatal(true);
        try{
            // load the class for real.
            impl = Class.forName(bc.getName(), true, _enhLoader);
        } catch (Throwable t) {
            throw new InternalException(_loc.get("interface-load2"), t).
                setFatal(true);
        }
        // cache the generated impl.
        _impls.put(iface, impl);
        return impl;
    }

    /**
     * Add bean getters and setters, also recording seen methods
     * into the given set.
     */
    private void addField (BCClass bc, Class iface, FieldMetaData fmd, 
        Set methods) {
        String name = fmd.getName();
        Class type = fmd.getDeclaredType();
        BCField field = bc.declareField(name, type);
        field.setAccessFlags(Constants.ACCESS_PRIVATE);

        // getter
        name = StringUtils.capitalize(name);
        String prefix = isGetter(iface, fmd) ? "get" : "is";
        BCMethod meth = bc.declareMethod(prefix + name, type, null);
        meth.makePublic();
        Code code = meth.getCode(true);
        code.aload().setThis();
        code.getfield().setField(field);
        code.xreturn().setType(type);
        code.calculateMaxStack();
        code.calculateMaxLocals();
        methods.add(getMethodSafe(iface, meth.getName(), null));

        // setter
        meth = bc.declareMethod("set" + name, void.class, new Class[]{type});
        meth.makePublic();
        code = meth.getCode(true);
        code.aload().setThis();
        code.xload().setParam(0).setType(type);
        code.putfield().setField(field);
        code.vreturn();
        code.calculateMaxStack();
        code.calculateMaxLocals();
        methods.add(getMethodSafe(iface, meth.getName(), type));
    }

    /**
     * Invalidate methods on the interface which are not managed.
     */
    private void invalidateNonBeanMethods(BCClass bc, Class iface, 
        Set methods) {
        Method[] meths = iface.getDeclaredMethods();
        BCMethod meth;
        Code code;
        Class type = _repos.getMetaDataFactory().getDefaults().
            getUnimplementedExceptionType();
        for (int i = 0; i < meths.length; i++) {
            if (methods.contains(meths[i]))
                continue;
            meth = bc.declareMethod(meths[i].getName(), 
                meths[i].getReturnType(), meths[i].getParameterTypes());
            meth.makePublic();
            code = meth.getCode(true);
            code.anew().setType(type);
            code.dup();
            code.invokespecial().setMethod(type, "<init>", void.class, null);
            code.athrow();
            code.calculateMaxLocals();
            code.calculateMaxStack();
        }
    }

    /**
     * Return a unique class name.
     */
    protected final String getClassName(ClassMetaData meta) {
        Class iface = meta.getDescribedType();
        return iface.getName() + "$" + System.identityHashCode(iface) + POSTFIX;
    }

    /**
     * Convenience method to return the given method / arg.
     */
    private static Method getMethodSafe(Class iface, String name, Class arg) {
        try {
            return iface.getDeclaredMethod(name, arg == null ? null :
                new Class[]{arg});
        } catch (NoSuchMethodException e) {
            throw new InternalException (_loc.get ("interface-mismatch", name));
        }
    }

    private static boolean isGetter(Class iface, FieldMetaData fmd) {
        if (fmd.getType() != boolean.class && fmd.getType() != Boolean.class)
            return true;
        try {
            Method meth = iface.getDeclaredMethod("is" + StringUtils.capitalize
                (fmd.getName()), null);
            return meth == null;
        } catch (NoSuchMethodException e) {}
        return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/356.java