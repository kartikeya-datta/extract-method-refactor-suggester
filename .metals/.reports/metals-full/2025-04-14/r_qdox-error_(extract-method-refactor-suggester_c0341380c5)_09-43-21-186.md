error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4770.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4770.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4770.java
text:
```scala
L@@ist as = BcelAttributes.readAjAttributes(getDeclaringType().getClassName(),attrs, getSourceContext(world),world.getMessageHandler());

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


package org.aspectj.weaver.bcel;

import java.util.Iterator;
import java.util.List;

import org.aspectj.apache.bcel.classfile.Attribute;
import org.aspectj.apache.bcel.classfile.Field;
import org.aspectj.apache.bcel.classfile.Synthetic;
import org.aspectj.apache.bcel.classfile.annotation.Annotation;
import org.aspectj.weaver.AjAttribute;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedTypeX;
import org.aspectj.weaver.TypeX;
import org.aspectj.weaver.World;

final class BcelField extends ResolvedMember {

	private Field field;
	private boolean isAjSynthetic;
	private boolean isSynthetic = false;
	private ResolvedTypeX[] resolvedAnnotations;
	private World world;

	BcelField(BcelObjectType declaringType, Field field) {
		super(
			FIELD, 
			declaringType.getResolvedTypeX(),
			field.getAccessFlags(),
			field.getName(), 
			field.getSignature());
		this.field = field;
		this.world = declaringType.getResolvedTypeX().getWorld();
		unpackAttributes(world);
		checkedExceptions = TypeX.NONE;
	}

	// ----
	
	private void unpackAttributes(World world) {
		Attribute[] attrs = field.getAttributes();
		List as = BcelAttributes.readAjAttributes(attrs, getSourceContext(world),world.getMessageHandler());
		for (Iterator iter = as.iterator(); iter.hasNext();) {
			AjAttribute a = (AjAttribute) iter.next();
			if (a instanceof AjAttribute.AjSynthetic) {
				isAjSynthetic = true;
			} else {
				throw new BCException("weird field attribute " + a);
			}
		}
		isAjSynthetic = false;
		
		
		for (int i = attrs.length - 1; i >= 0; i--) {
			if (attrs[i] instanceof Synthetic) isSynthetic = true;
		}
	}
	
	

	public boolean isAjSynthetic() {
		return isAjSynthetic; // || getName().startsWith(NameMangler.PREFIX);
	}
	
	public boolean isSynthetic() {
		return isSynthetic;
	}
	
	public boolean hasAnnotation(TypeX ofType) {
		Annotation[] anns = field.getAnnotations();
		for (int i = 0; i < anns.length; i++) {
			Annotation annotation = anns[i];
			if (annotation.getTypeName().equals(ofType.getName())) return true;
		}
		return false;
	}
	
	public ResolvedTypeX[] getAnnotationTypes() {
	 	if (resolvedAnnotations == null) {
	 		Annotation[] annotations = field.getAnnotations();
			resolvedAnnotations = new ResolvedTypeX[annotations.length];
			for (int i = 0; i < annotations.length; i++) {
				Annotation annotation = annotations[i];
				ResolvedTypeX rtx = world.resolve(TypeX.forName(annotation.getTypeName()));
				resolvedAnnotations[i] = rtx;
			}
	 	}
	 	return resolvedAnnotations;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4770.java