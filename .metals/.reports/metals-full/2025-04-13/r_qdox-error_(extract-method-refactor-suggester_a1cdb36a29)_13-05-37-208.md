error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7116.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7116.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7116.java
text:
```scala
C@@lass delegateClass = Class.forName("org.aspectj.weaver.reflect.Java15ReflectionBasedReferenceTypeDelegate");//,false,usingClassLoader);//ReflectionBasedReferenceTypeDelegate.class.getClassLoader());

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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.aspectj.util.LangUtil;
import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedMemberImpl;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.World;

/**
 * @author colyer
 * Creates the appropriate ReflectionBasedReferenceTypeDelegate according to
 * the VM level we are running at. Uses reflection to avoid 1.5 dependencies in
 * 1.4 and 1.3 code base.
 */
public class ReflectionBasedReferenceTypeDelegateFactory {

	public static ReflectionBasedReferenceTypeDelegate
		createDelegate(ReferenceType forReferenceType, World inWorld, ClassLoader usingClassLoader) {
		try {
			Class c = Class.forName(forReferenceType.getName(),false,usingClassLoader);
			if (LangUtil.is15VMOrGreater()) {
				ReflectionBasedReferenceTypeDelegate rbrtd = create15Delegate(forReferenceType,c,usingClassLoader,inWorld);
				if (rbrtd!=null) return rbrtd; // can be null if we didn't find the class the delegate logic loads
			}
			return new ReflectionBasedReferenceTypeDelegate(c,usingClassLoader,inWorld,forReferenceType);
		} catch (ClassNotFoundException cnfEx) {
			return null;
		}
	}
	
	private static ReflectionBasedReferenceTypeDelegate create15Delegate(ReferenceType forReferenceType, Class forClass, ClassLoader usingClassLoader, World inWorld) {
		try {
			Class delegateClass = Class.forName("org.aspectj.weaver.reflect.Java15ReflectionBasedReferenceTypeDelegate",false,usingClassLoader);//ReflectionBasedReferenceTypeDelegate.class.getClassLoader()); 
			ReflectionBasedReferenceTypeDelegate ret = (ReflectionBasedReferenceTypeDelegate) delegateClass.newInstance();
			ret.initialize(forReferenceType,forClass,usingClassLoader,inWorld);
			return ret;
		} catch (ClassNotFoundException cnfEx) {
			throw new IllegalStateException("Attempted to create Java 1.5 reflection based delegate but org.aspectj.weaver.reflect.Java15ReflectionBasedReferenceTypeDelegate was not found on classpath");
		} catch (InstantiationException insEx) {
			throw new IllegalStateException("Attempted to create Java 1.5 reflection based delegate but InstantiationException: " + insEx + " occured");
		} catch (IllegalAccessException illAccEx) {
			throw new IllegalStateException("Attempted to create Java 1.5 reflection based delegate but IllegalAccessException: " + illAccEx + " occured");
		}
	}
	
	/**
	 * convert a java.lang.reflect.Member into a resolved member in the world
	 * @param reflectMember
	 * @param inWorld
	 * @return
	 */
	public static ResolvedMember createResolvedMember(Member reflectMember, World inWorld) {
		if (reflectMember instanceof Method) {
			return createResolvedMethod((Method)reflectMember,inWorld);
		} else if (reflectMember instanceof Constructor) {
			return createResolvedConstructor((Constructor)reflectMember,inWorld);
		} else {
			return createResolvedField((Field)reflectMember,inWorld);
		}
	}
	
	public static ResolvedMember createResolvedMethod(Method aMethod, World inWorld) {
		ReflectionBasedResolvedMemberImpl ret = new ReflectionBasedResolvedMemberImpl(org.aspectj.weaver.Member.METHOD,
				toResolvedType(aMethod.getDeclaringClass(),(ReflectionWorld)inWorld),
				aMethod.getModifiers(),
				toResolvedType(aMethod.getReturnType(),(ReflectionWorld)inWorld),
				aMethod.getName(),
				toResolvedTypeArray(aMethod.getParameterTypes(),inWorld),
				toResolvedTypeArray(aMethod.getExceptionTypes(),inWorld),
				aMethod
				);
		if (inWorld instanceof ReflectionWorld) {
			ret.setAnnotationFinder(((ReflectionWorld)inWorld).getAnnotationFinder());
		}
		return ret;
	}
	
	public static ResolvedMember createResolvedAdviceMember(Method aMethod, World inWorld) {
		ReflectionBasedResolvedMemberImpl ret =
			new ReflectionBasedResolvedMemberImpl(org.aspectj.weaver.Member.ADVICE,
				toResolvedType(aMethod.getDeclaringClass(),(ReflectionWorld)inWorld),
				aMethod.getModifiers(),
				toResolvedType(aMethod.getReturnType(),(ReflectionWorld)inWorld),
				aMethod.getName(),
				toResolvedTypeArray(aMethod.getParameterTypes(),inWorld),
				toResolvedTypeArray(aMethod.getExceptionTypes(),inWorld),
				aMethod
				);
		if (inWorld instanceof ReflectionWorld) {
			ret.setAnnotationFinder(((ReflectionWorld)inWorld).getAnnotationFinder());
		}
		return ret;
	}
	
	public static ResolvedMember createStaticInitMember(Class forType, World inWorld) {
		return new ResolvedMemberImpl(org.aspectj.weaver.Member.STATIC_INITIALIZATION,
				toResolvedType(forType,(ReflectionWorld)inWorld),
				Modifier.STATIC,
				ResolvedType.VOID,
				"<clinit>",
				new UnresolvedType[0],
				new UnresolvedType[0]
				);
	}

	public static ResolvedMember createResolvedConstructor(Constructor aConstructor, World inWorld) {
		ReflectionBasedResolvedMemberImpl ret =
		   new ReflectionBasedResolvedMemberImpl(org.aspectj.weaver.Member.CONSTRUCTOR,
				toResolvedType(aConstructor.getDeclaringClass(),(ReflectionWorld)inWorld),
				aConstructor.getModifiers(),
				toResolvedType(aConstructor.getDeclaringClass(),(ReflectionWorld)inWorld),
				"init",
				toResolvedTypeArray(aConstructor.getParameterTypes(),inWorld),
				toResolvedTypeArray(aConstructor.getExceptionTypes(),inWorld),
				aConstructor
				);
		if (inWorld instanceof ReflectionWorld) {
			ret.setAnnotationFinder(((ReflectionWorld)inWorld).getAnnotationFinder());
		}
		return ret;
	}

	public static ResolvedMember createResolvedField(Field aField, World inWorld) {
		ReflectionBasedResolvedMemberImpl ret =
			new ReflectionBasedResolvedMemberImpl(org.aspectj.weaver.Member.FIELD,
				toResolvedType(aField.getDeclaringClass(),(ReflectionWorld)inWorld),
				aField.getModifiers(),
				toResolvedType(aField.getType(),(ReflectionWorld)inWorld),
				aField.getName(),
				new UnresolvedType[0],
				aField);
		if (inWorld instanceof ReflectionWorld) {
			ret.setAnnotationFinder(((ReflectionWorld)inWorld).getAnnotationFinder());
		}
		return ret;
	}
	
	public static ResolvedMember createHandlerMember(Class exceptionType, Class inType,World inWorld) {
		return new ResolvedMemberImpl(
				org.aspectj.weaver.Member.HANDLER,
				toResolvedType(inType,(ReflectionWorld)inWorld),
				Modifier.STATIC,
				"<catch>",
				"(" + inWorld.resolve(exceptionType.getName()).getSignature() + ")V");
	}
	
	public static ResolvedType resolveTypeInWorld(Class aClass, World aWorld) {
//		 classes that represent arrays return a class name that is the signature of the array type, ho-hum...
		String className = aClass.getName();
		if (aClass.isArray()) {
			return aWorld.resolve(UnresolvedType.forSignature(className));
		}
		else{
			return aWorld.resolve(className);
		} 
	}
	
	private static ResolvedType toResolvedType(Class aClass, ReflectionWorld aWorld) {
		return aWorld.resolve(aClass);
	}

	private static ResolvedType[] toResolvedTypeArray(Class[] classes, World inWorld) {
		ResolvedType[] ret = new ResolvedType[classes.length];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = ((ReflectionWorld)inWorld).resolve(classes[i]);
		}
		return ret;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7116.java