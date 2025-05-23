error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8684.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8684.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8684.java
text:
```scala
private R@@eflectionWorld() {

/* *******************************************************************
 * Copyright (c) 2005 Contributors.
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://eclipse.org/legal/epl-v10.html 
 *  
 * Contributors: 
 *   Adrian Colyer			Initial implementation
 * ******************************************************************/
package org.aspectj.weaver.reflect;

import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.util.LangUtil;
import org.aspectj.weaver.Advice;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.ConcreteTypeMunger;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ReferenceTypeDelegate;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.ResolvedTypeMunger;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.World;
import org.aspectj.weaver.AjAttribute.AdviceAttribute;
import org.aspectj.weaver.patterns.Pointcut;
import org.aspectj.weaver.patterns.PerClause.Kind;

/**
 * A ReflectionWorld is used solely for purposes of type resolution based on 
 * the runtime classpath (java.lang.reflect). It does not support weaving operations
 * (creation of mungers etc..).
 *
 */
public class ReflectionWorld extends World {

	private ClassLoader classLoader;
	private AnnotationFinder annotationFinder;
	
	public ReflectionWorld() {
		super();
		this.setMessageHandler(new ExceptionBasedMessageHandler());
		setBehaveInJava5Way(LangUtil.is15VMOrGreater());
		this.classLoader = ReflectionWorld.class.getClassLoader();
		if (LangUtil.is15VMOrGreater()) {
			initializeAnnotationFinder(this.classLoader);
		}
	}
	
	public ReflectionWorld(ClassLoader aClassLoader) {
		super();
		this.setMessageHandler(new ExceptionBasedMessageHandler());
		setBehaveInJava5Way(LangUtil.is15VMOrGreater());
		this.classLoader = aClassLoader;
		if (LangUtil.is15VMOrGreater()) {
			initializeAnnotationFinder(this.classLoader);
		}
	}

	private void initializeAnnotationFinder(ClassLoader loader) {
		try {
			Class java15AnnotationFinder = Class.forName("org.aspectj.weaver.reflect.Java15AnnotationFinder");
			this.annotationFinder = (AnnotationFinder) java15AnnotationFinder.newInstance();
			this.annotationFinder.setClassLoader(loader);
			this.annotationFinder.setWorld(this);
		} catch(ClassNotFoundException ex) {
			// must be on 1.4 or earlier
		} catch(IllegalAccessException ex) {
			// not so good
			throw new BCException("AspectJ internal error",ex);
		} catch(InstantiationException ex) {
			throw new BCException("AspectJ internal error",ex);
		}
	}
	
	public ClassLoader getClassLoader() {
		return this.classLoader;
	}
	
	public AnnotationFinder getAnnotationFinder() {
		return this.annotationFinder;
	}
	
	public ResolvedType resolve(Class aClass) {
		// classes that represent arrays return a class name that is the signature of the array type, ho-hum...
		String className = aClass.getName();
		if (aClass.isArray()) {
			return resolve(UnresolvedType.forSignature(className));
		}
		else{
			return resolve(className);
		} 
	}
	
	/* (non-Javadoc)
	 * @see org.aspectj.weaver.World#resolveDelegate(org.aspectj.weaver.ReferenceType)
	 */
	protected ReferenceTypeDelegate resolveDelegate(ReferenceType ty) {
		return ReflectionBasedReferenceTypeDelegateFactory.createDelegate(ty, this, this.classLoader);
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.World#createAdviceMunger(org.aspectj.weaver.AjAttribute.AdviceAttribute, org.aspectj.weaver.patterns.Pointcut, org.aspectj.weaver.Member)
	 */
	public Advice createAdviceMunger(AdviceAttribute attribute,
			Pointcut pointcut, Member signature) {
		throw new UnsupportedOperationException("Cannot create advice munger in ReflectionWorld");
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.World#makeCflowStackFieldAdder(org.aspectj.weaver.ResolvedMember)
	 */
	public ConcreteTypeMunger makeCflowStackFieldAdder(ResolvedMember cflowField) {
		throw new UnsupportedOperationException("Cannot create cflow stack in ReflectionWorld");
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.World#makeCflowCounterFieldAdder(org.aspectj.weaver.ResolvedMember)
	 */
	public ConcreteTypeMunger makeCflowCounterFieldAdder(
			ResolvedMember cflowField) {
		throw new UnsupportedOperationException("Cannot create cflow counter in ReflectionWorld");
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.World#makePerClauseAspect(org.aspectj.weaver.ResolvedType, org.aspectj.weaver.patterns.PerClause.Kind)
	 */
	public ConcreteTypeMunger makePerClauseAspect(ResolvedType aspect, Kind kind) {
		throw new UnsupportedOperationException("Cannot create per clause in ReflectionWorld");
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.World#concreteTypeMunger(org.aspectj.weaver.ResolvedTypeMunger, org.aspectj.weaver.ResolvedType)
	 */
	public ConcreteTypeMunger concreteTypeMunger(ResolvedTypeMunger munger,
			ResolvedType aspectType) {
		throw new UnsupportedOperationException("Cannot create type munger in ReflectionWorld");
	}
	
	public static class ReflectionWorldException extends RuntimeException {

		private static final long serialVersionUID = -3432261918302793005L;

		public ReflectionWorldException(String message) {
			super(message);
		}
	}
	
	private static class ExceptionBasedMessageHandler implements IMessageHandler {

		public boolean handleMessage(IMessage message) throws AbortException {
			throw new ReflectionWorldException(message.toString());
		}

		public boolean isIgnoring(org.aspectj.bridge.IMessage.Kind kind) {
			if (kind == IMessage.INFO) {
				return true;
			} else {
				return false;
			}
		}

		public void dontIgnore(org.aspectj.bridge.IMessage.Kind kind) {
			// empty
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8684.java