error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5183.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5183.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5183.java
text:
```scala
public b@@oolean hasAnnotation(TypeX ofType) {

/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     PARC     initial implementation 
 * ******************************************************************/


package org.aspectj.weaver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;

import org.aspectj.bridge.ISourceLocation;

/**
 * This is the declared member, i.e. it will always correspond to an
 * actual method/... declaration
 */
public class ResolvedMember extends Member implements IHasPosition, AnnotatedElement {
    
    public String[] parameterNames = null;
    protected TypeX[] checkedExceptions = TypeX.NONE;
    
    
    // these three fields hold the source location of this member
	protected int start, end;
	protected ISourceContext sourceContext = null;
    
    //XXX deprecate this in favor of the constructor below
	public ResolvedMember(
		Kind kind,
		TypeX declaringType,
		int modifiers,
		TypeX returnType,
		String name,
		TypeX[] parameterTypes)
	{
		super(kind, declaringType, modifiers, returnType, name, parameterTypes);
	}

    
    
	public ResolvedMember(
		Kind kind,
		TypeX declaringType,
		int modifiers,
		TypeX returnType,
		String name,
		TypeX[] parameterTypes,
		TypeX[] checkedExceptions) 
	{
		super(kind, declaringType, modifiers, returnType, name, parameterTypes);
		this.checkedExceptions = checkedExceptions;
	}
    
	public ResolvedMember(
		Kind kind,
		TypeX declaringType,
		int modifiers,
		String name,
		String signature) 
	{
		super(kind, declaringType, modifiers, name, signature);
	}    

    public static final ResolvedMember[] NONE = new ResolvedMember[0];

	// ----

    public final int getModifiers(World world) {
        return modifiers;
    }
    public final int getModifiers() {
        return modifiers;
    }

	// ----
	

    public final TypeX[] getExceptions(World world) {
        return getExceptions();
    }
    
    public TypeX[] getExceptions() {
        return checkedExceptions;
    }
    
    public ShadowMunger getAssociatedShadowMunger() {
		return null;
    }
    
    // ??? true or false?
    public boolean isAjSynthetic() {
    	return true;
    }

    public boolean hasAnnotation(ResolvedTypeX ofType) {
        // The ctors don't allow annotations to be specified ... yet - but
        // that doesn't mean it is an error to call this method.
        // Normally the weaver will be working with subtypes of 
        // this type - BcelField/BcelMethod
    	return false;
    }
    
    public ResolvedTypeX[] getAnnotationTypes() {
    	// The ctors don't allow annotations to be specified ... yet - but
    	// that doesn't mean it is an error to call this method.
    	// Normally the weaver will be working with subtypes of
    	// this type - BcelField/BcelMethod
    	return null;
    }
    
    public boolean isBridgeMethod() {
    	return (modifiers & Constants.ACC_BRIDGE)!=0;
    }
    
    public boolean isVarargsMethod() {
    	return (modifiers & Constants.ACC_VARARGS)!=0;
    }
    
	public boolean isSynthetic() {
		return false;
	}
    
    public void write(DataOutputStream s) throws IOException {
    	getKind().write(s);
    	getDeclaringType().write(s);
    	s.writeInt(modifiers);
    	s.writeUTF(getName());
    	s.writeUTF(getSignature());
		TypeX.writeArray(getExceptions(), s);

		s.writeInt(getStart());
		s.writeInt(getEnd());

    }

    public static void writeArray(ResolvedMember[] members, DataOutputStream s) throws IOException {
		s.writeInt(members.length);
		for (int i = 0, len = members.length; i < len; i++) {
			members[i].write(s);
		}
    }

    
    public static ResolvedMember readResolvedMember(DataInputStream s, ISourceContext sourceContext) throws IOException {
    	ResolvedMember m = new ResolvedMember(Kind.read(s), TypeX.read(s), s.readInt(), s.readUTF(), s.readUTF());
		m.checkedExceptions = TypeX.readArray(s);
		m.start = s.readInt();
		m.end = s.readInt();
		m.sourceContext = sourceContext;
		return m;
    }
    
    public static ResolvedMember[] readResolvedMemberArray(DataInputStream s, ISourceContext context) throws IOException {
    	int len = s.readInt();
		ResolvedMember[] members = new ResolvedMember[len];
		for (int i=0; i < len; i++) {
			members[i] = ResolvedMember.readResolvedMember(s, context);
		}
		return members;
    }
    
    
    
	public ResolvedMember resolve(World world) {
		return this;
	}
	
	public ISourceContext getSourceContext(World world) {
		return getDeclaringType().resolve(world).getSourceContext();
	}

	public final String[] getParameterNames() {
		return parameterNames;
	}
	public final String[] getParameterNames(World world) {
		return getParameterNames();
	}
	
	public AjAttribute.EffectiveSignatureAttribute getEffectiveSignature() {
		return null;
	}
	
    public ISourceLocation getSourceLocation() {
    	//System.out.println("get context: " + this + " is " + sourceContext);
    	if (sourceContext == null) {
    		//System.err.println("no context: " + this);
    		return null;
    	}
    	return sourceContext.makeSourceLocation(this);
    }
    
	public int getEnd() {
		return end;
	}

	public ISourceContext getSourceContext() {
		return sourceContext;
	}

	public int getStart() {
		return start;
	}

	public void setPosition(int sourceStart, int sourceEnd) {
		this.start = sourceStart;
		this.end = sourceEnd;
	}

	public void setSourceContext(ISourceContext sourceContext) {
		this.sourceContext = sourceContext;
	}
	
	public boolean isAbstract() {
		return Modifier.isAbstract(modifiers);
	}

	public boolean isPublic() {
		return Modifier.isPublic(modifiers);
	}

	public boolean isVisible(ResolvedTypeX fromType) {
		World world = fromType.getWorld();
		return ResolvedTypeX.isVisible(getModifiers(), getDeclaringType().resolve(world),
					fromType);
	}
	public void setCheckedExceptions(TypeX[] checkedExceptions) {
		this.checkedExceptions = checkedExceptions;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5183.java