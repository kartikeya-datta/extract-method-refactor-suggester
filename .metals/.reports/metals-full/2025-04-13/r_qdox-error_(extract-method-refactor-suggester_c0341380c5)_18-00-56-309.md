error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13320.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13320.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[14,1]

error in qdox parser
file content:
```java
offset: 637
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13320.java
text:
```scala
public class ListTypeImpl extends CollectionTypeImpl {

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

p@@ackage org.eclipse.internal.xtend.type.baseimpl.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.internal.xtend.type.baseimpl.OperationImpl;
import org.eclipse.xtend.expression.TypeSystem;
import org.eclipse.xtend.typesystem.Feature;
import org.eclipse.xtend.typesystem.ParameterizedType;
import org.eclipse.xtend.typesystem.Type;

/**
 * @author Sven Efftinge (http://www.efftinge.de)
 * @author Arno Haase
 */
public class ListTypeImpl extends CollectionTypeImpl implements Type {

	public ListTypeImpl(final Type innerType, final TypeSystem ts, final String name) {
		super(innerType, ts, name);
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean isInstance(final Object o) {
		return o instanceof List;
	}

	@Override
	public Object newInstance() {
		return new ArrayList<Object>();
	}

	@Override
	public ParameterizedType cloneWithInnerType(final Type innerType) {
		return (ParameterizedType) getTypeSystem().getListType(innerType);
	}

	@Override
	public Feature[] getContributedFeatures() {
		return new Feature[] { new OperationImpl(this, "get", getInnerType(), new Type[] { getTypeSystem().getIntegerType() }) {
			@Override
			public Object evaluateInternal(final Object target, final Object[] params) {
				return ((List<?>) target).get(((Number) params[0]).intValue());
			}
		}, new OperationImpl(this, "indexOf", getTypeSystem().getIntegerType(), new Type[] { getTypeSystem().getObjectType() }) {
			@Override
			public Object evaluateInternal(final Object target, final Object[] params) {
				return new Long(((List<?>) target).indexOf(params[0]));
			}
		}, new OperationImpl(this, "first", getInnerType(), new Type[0]) {
			@Override
			@SuppressWarnings("unchecked")
			public Object evaluateInternal(final Object target, final Object[] params) {
				if (target instanceof List) {
					List<?> l = (List<?>) target;
					if (l.size() > 0)
						return l.get(0);
				}
				return null;
			}
		}, new OperationImpl(this, "reverse", getTypeSystem().getCollectionType(getInnerType()), new Type[0]) {
			@Override
			@SuppressWarnings("unchecked")
			public Object evaluateInternal(final Object target, final Object[] params) {
				if (target instanceof List) {
					List<?> l = new ArrayList ((List<?>) target);
					Collections.reverse(l);
					return l;
				}
				return null;
			}
		}, new OperationImpl(this, "last", getInnerType(), new Type[0]) {
			@Override
			@SuppressWarnings("unchecked")
			public Object evaluateInternal(final Object target, final Object[] params) {
				if (target instanceof List) {
					List<?> l = (List<?>) target;
					if (l.size() > 0)
						return l.get(l.size() - 1);
				}
				return null;
			}
		}, new OperationImpl(this, "withoutFirst", this, new Type[0]) {
			@SuppressWarnings("unchecked")
			@Override
			public Object evaluateInternal(final Object target, final Object[] params) {
				if (target instanceof List) {
					List l = (List) target;
					List r = new ArrayList();
					for (int i = 1; i < l.size(); i++) {
						r.add(l.get(i));
					}
					return r;
				}
				return null;
			}
		}, new OperationImpl(this, "withoutLast", this, new Type[0]) {
			@SuppressWarnings("unchecked")
			@Override
			public Object evaluateInternal(final Object target, final Object[] params) {
				if (target instanceof List) {
					List l = (List) target;
					List r = new ArrayList();
					for (int i = 0; i < l.size() - 1; i++) {
						r.add(l.get(i));
					}
					return r;
				}
				return null;
			}
		}

		};
	}

	@Override
	public Set<Type> getSuperTypes() {
		return Collections.singleton(getTypeSystem().getCollectionType(getInnerType()));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13320.java