error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5369.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5369.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5369.java
text:
```scala
public A@@nnotationTypePattern parameterizeWith(Map typeVariableMap,World w) {

/* *******************************************************************
 * Copyright (c) 2004 IBM Corporation.
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 *  
 * ******************************************************************/
package org.aspectj.weaver.patterns;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.TypeVariableReference;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;

public class BindingAnnotationTypePattern extends ExactAnnotationTypePattern implements BindingPattern {

	private int formalIndex;
	
	/**
	 * @param annotationType
	 */
	public BindingAnnotationTypePattern(UnresolvedType annotationType, int index) {
		super(annotationType);
		this.formalIndex = index;
	}
		
	public BindingAnnotationTypePattern(FormalBinding binding) {
		this(binding.getType(),binding.getIndex());
	}
	
	public void resolveBinding(World world) {
	    
		if (resolved) return;
		resolved = true;
		annotationType = annotationType.resolve(world);
		ResolvedType resolvedAnnotationType = (ResolvedType) annotationType;
		if (!resolvedAnnotationType.isAnnotation()) {
			IMessage m = MessageUtil.error(
					WeaverMessages.format(WeaverMessages.REFERENCE_TO_NON_ANNOTATION_TYPE,annotationType.getName()),
					getSourceLocation());
			world.getMessageHandler().handleMessage(m);
			resolved = false;
		}
		if (annotationType.isTypeVariableReference()) return;  // we'll deal with this next check when the type var is actually bound...
        verifyRuntimeRetention(world, resolvedAnnotationType);
	}

	private void verifyRuntimeRetention(World world, ResolvedType resolvedAnnotationType) {
		if (!resolvedAnnotationType.isAnnotationWithRuntimeRetention()) { // default is class visibility
		    // default is class visibility
			IMessage m = MessageUtil.error(
					WeaverMessages.format(WeaverMessages.BINDING_NON_RUNTIME_RETENTION_ANNOTATION,annotationType.getName()),
					getSourceLocation());
			world.getMessageHandler().handleMessage(m);
			resolved = false;		    
		}
	}
	
	public AnnotationTypePattern parameterizeWith(Map typeVariableMap) {
		UnresolvedType newAnnotationType = annotationType;
		if (annotationType.isTypeVariableReference()) {
			TypeVariableReference t = (TypeVariableReference) annotationType;
			String key = t.getTypeVariable().getName();
			if (typeVariableMap.containsKey(key)) {
				newAnnotationType = (UnresolvedType) typeVariableMap.get(key);
			}
		} else if (annotationType.isParameterizedType()) {
			newAnnotationType = annotationType.parameterize(typeVariableMap);
		}
		BindingAnnotationTypePattern ret = new BindingAnnotationTypePattern(newAnnotationType,this.formalIndex);
		if (newAnnotationType instanceof ResolvedType) {
			ResolvedType rat = (ResolvedType) newAnnotationType;
			verifyRuntimeRetention(rat.getWorld(),rat);			
		}
		ret.copyLocationFrom(this);
		return ret;
	}
	
	public int getFormalIndex() {
		return formalIndex;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof BindingAnnotationTypePattern)) return false;
		BindingAnnotationTypePattern btp = (BindingAnnotationTypePattern) obj;
		return (super.equals(btp) && (btp.formalIndex == formalIndex));
	}
	
	public int hashCode() {
		return super.hashCode()*37 + formalIndex;
	}
	
	public AnnotationTypePattern remapAdviceFormals(IntMap bindings) {			
		if (!bindings.hasKey(formalIndex)) {
			return new ExactAnnotationTypePattern(annotationType);
		} else {
			int newFormalIndex = bindings.get(formalIndex);
			return new BindingAnnotationTypePattern(annotationType, newFormalIndex);
		}
	}
	private static final byte VERSION = 1; // rev if serialised form changed
	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.ExactAnnotationTypePattern#write(java.io.DataOutputStream)
	 */
	public void write(DataOutputStream s) throws IOException {
		s.writeByte(AnnotationTypePattern.BINDING);
		s.writeByte(VERSION);
		annotationType.write(s);
		s.writeShort((short)formalIndex);
		writeLocation(s);
	}	
	
	public static AnnotationTypePattern read(VersionedDataInputStream s, ISourceContext context) throws IOException {
		byte version = s.readByte();
		if (version > VERSION) {
			throw new BCException("BindingAnnotationTypePattern was written by a more recent version of AspectJ");
		}
		AnnotationTypePattern ret = new BindingAnnotationTypePattern(UnresolvedType.read(s),s.readShort());
		ret.readLocation(context,s);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5369.java