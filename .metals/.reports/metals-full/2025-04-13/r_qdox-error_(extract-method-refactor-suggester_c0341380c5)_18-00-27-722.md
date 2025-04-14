error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5449.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5449.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5449.java
text:
```scala
i@@f (targetType.equals(world.getCoreType(TypeX.OBJECT))) {

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


package org.aspectj.weaver.patterns;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.aspectj.bridge.IMessage;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ResolvedTypeX;
import org.aspectj.weaver.TypeX;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;

public class DeclareParents extends Declare {
	private TypePattern child;
	private TypePatternList parents;
	

	public DeclareParents(TypePattern child, List parents) {
		this(child, new TypePatternList(parents));
	}
	
	private DeclareParents(TypePattern child, TypePatternList parents) {
		this.child = child;
		this.parents = parents;
	}
	
	public boolean match(ResolvedTypeX typeX) {
		if (!child.matchesStatically(typeX)) return false;
		if (typeX.getWorld().getLint().typeNotExposedToWeaver.isEnabled() &&
				!typeX.isExposedToWeaver())
		{
			typeX.getWorld().getLint().typeNotExposedToWeaver.signal(typeX.getName(), getSourceLocation());
		}
		
		return true;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("declare parents: ");
		buf.append(child);
		buf.append(" extends ");  //extends and implements are treated equivalently
		buf.append(parents);
		buf.append(";");
		return buf.toString();
	}
	
	public boolean equals(Object other) { 
		if (!(other instanceof DeclareParents)) return false;
		DeclareParents o = (DeclareParents)other;
		return o.child.equals(child) && o.parents.equals(parents);
	}
    
    //??? cache this 
    public int hashCode() {
    	int result = 23;
        result = 37*result + child.hashCode();
        result = 37*result + parents.hashCode();
    	return result;
    }


	public void write(DataOutputStream s) throws IOException {
		s.writeByte(Declare.PARENTS);
		child.write(s);
		parents.write(s);
		writeLocation(s);
	}

	public static Declare read(DataInputStream s, ISourceContext context) throws IOException {
		Declare ret = new DeclareParents(TypePattern.read(s, context), TypePatternList.read(s, context));
		ret.readLocation(context, s);
		return ret;
	}
	
    public void resolve(IScope scope) {
    	child = child.resolveBindings(scope, Bindings.NONE, false, false);
    	parents = parents.resolveBindings(scope, Bindings.NONE, false, true); 
//    	for (int i=0; i < parents.size(); i++) {
//    		parents.get(i).assertExactType(scope.getMessageHandler());
//		}
    }

	public TypePatternList getParents() {
		return parents;
	}

	public TypePattern getChild() {
		return child;
	}
	
	public boolean isAdviceLike() {
		return false;
	}
	
	private ResolvedTypeX maybeGetNewParent(ResolvedTypeX targetType, TypePattern typePattern, World world) {
		if (typePattern == TypePattern.NO) return null;  // already had an error here
		TypeX iType = typePattern.getExactType();
		ResolvedTypeX parentType = iType.resolve(world);
		
		if (targetType.equals(world.resolve(TypeX.OBJECT))) {
			world.showMessage(IMessage.ERROR, 
					WeaverMessages.format(WeaverMessages.DECP_OBJECT),
			        this.getSourceLocation(), null);
			return null;
		}
			
		if (parentType.isAssignableFrom(targetType)) return null;  // already a parent
					
		if (targetType.isAssignableFrom(parentType)) {
			world.showMessage(IMessage.ERROR,
					WeaverMessages.format(WeaverMessages.CANT_EXTEND_SELF,targetType.getName()),
					this.getSourceLocation(), null
			);
			return null;
		}
					
		if (parentType.isClass()) {
			if (targetType.isInterface()) {
				world.showMessage(IMessage.ERROR, 
						WeaverMessages.format(WeaverMessages.INTERFACE_CANT_EXTEND_CLASS),
						this.getSourceLocation(), null
				);
				return null;
				// how to handle xcutting errors???
			}
			
			if (!targetType.getSuperclass().isAssignableFrom(parentType)) {
				world.showMessage(IMessage.ERROR,
						WeaverMessages.format(WeaverMessages.DECP_HIERARCHY_ERROR,
								iType.getName(),
								targetType.getSuperclass().getName()), 
						this.getSourceLocation(), null
				);
				return null;
			} else {
				return parentType;
			}				
		} else {
			return parentType;
		}
	}
	

	public List/*<ResolvedTypeX>*/ findMatchingNewParents(ResolvedTypeX onType) {
		if (!match(onType)) return Collections.EMPTY_LIST;
		
		List ret = new ArrayList();
		for (int i=0; i < parents.size(); i++) {
			ResolvedTypeX t = maybeGetNewParent(onType, parents.get(i), onType.getWorld());
			if (t != null) ret.add(t);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5449.java