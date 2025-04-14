error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13325.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13325.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[14,1]

error in qdox parser
file content:
```java
offset: 635
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13325.java
text:
```scala
public class JavaTypeImpl extends AbstractTypeImpl {

/*******************************************************************************
 * Copyright (c) 2005, 2007 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/

p@@ackage org.eclipse.xtend.type.impl.java;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.xtend.typesystem.AbstractTypeImpl;
import org.eclipse.xtend.typesystem.Feature;
import org.eclipse.xtend.typesystem.Type;

/**
 * @author Sven Efftinge
 * @author Arno Haase
 */
public class JavaTypeImpl extends AbstractTypeImpl implements Type {

    private final static Log log = LogFactory.getLog(JavaTypeImpl.class);

    private Class<?> clazz;

    private Set<Type> superTypes = null;

    private Feature[] features = null;

    private JavaTypeStrategy strategy = null;

    private JavaMetaModel metamodel = null;

    public JavaTypeImpl(final JavaMetaModel meta, final Class<?> clazz, final String name, final JavaTypeStrategy strategy) {
        super(meta.getTypeSystem(), name);
        this.clazz = clazz;
        metamodel = meta;
        this.strategy = strategy;
    }

    @Override
    public Feature[] getContributedFeatures() {
        if (features == null) {
            features = strategy.getFeatures(metamodel, clazz, this);
        }
        return features;
    }

    @SuppressWarnings("unchecked")
	@Override
    public Set<Type> getSuperTypes() {
        if (superTypes == null) {
            final Set<Type> result = new HashSet<Type>();
            if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Object.class)) {
                final Type beanType = metamodel.builtinAwareGetTypeForClass(clazz.getSuperclass());
                if (beanType != null) {
                    result.add(beanType);
                }
            }
            final Class[] interfaces = clazz.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                final Type beanType = metamodel.builtinAwareGetTypeForClass(interfaces[i]);
                if (beanType != null) {
                    result.add(beanType);
                }
            }
            if (result.isEmpty()) {
                result.add(metamodel.getTypeSystem().getObjectType());
            }
            superTypes = result;
        }
        return superTypes;
    }

    public boolean isInstance(final Object o) {
        return clazz.isInstance(o);
    }

    @Override
    protected boolean internalIsAssignableFrom(final Type t) {
        if (t instanceof JavaTypeImpl)
            return clazz.isAssignableFrom(((JavaTypeImpl) t).clazz);
        return false;
    }

    public Object newInstance() {
        try {
            return clazz.newInstance();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isAbstract() {
        try {
            return clazz.getConstructor(new Class[0]) == null || Modifier.isAbstract (clazz.getModifiers());
        } catch (final SecurityException e) {
            log.error(e.getMessage(), e);
        } catch (final NoSuchMethodException e) {
            log.error(e.getMessage(), e);
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13325.java