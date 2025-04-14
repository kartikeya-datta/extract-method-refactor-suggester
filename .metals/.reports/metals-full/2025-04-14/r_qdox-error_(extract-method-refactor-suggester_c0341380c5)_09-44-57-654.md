error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8650.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8650.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8650.java
text:
```scala
i@@f (onType.getWeaverState() == null) {

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.aspectj.util.TypeSafeEnum;

/** This is an abstraction over method/field introduction.  It might not have the chops
 * to handle other inter-type declarations.  This is the thing that is used on the 
 * eclipse side and serialized into a ConcreteTypeMunger.
 */
public abstract class ResolvedTypeMunger {
	protected Kind kind;
	protected ResolvedMember signature;
	
	private Set /* resolvedMembers */ superMethodsCalled = Collections.EMPTY_SET;

	public ResolvedTypeMunger(Kind kind, ResolvedMember signature) {
		this.kind = kind;
		this.signature = signature;
	}

	// ----

    // fromType is guaranteed to be a non-abstract aspect
    public ConcreteTypeMunger concretize(World world, ResolvedTypeX aspectType) {
		ConcreteTypeMunger munger = world.concreteTypeMunger(this, aspectType);
        return munger;
    }
    
    
    public boolean matches(ResolvedTypeX matchType, ResolvedTypeX aspectType) {
    	ResolvedTypeX onType = matchType.getWorld().resolve(signature.getDeclaringType());
    	//System.err.println("matching: " + this + " to " + matchType + " onType = " + onType);
   		if (matchType.equals(onType)) { 
   			if (!onType.isExposedToWeaver()) {
   				if (onType.getWeaverState() != null) {
	   				if (matchType.getWorld().getLint().typeNotExposedToWeaver.isEnabled()) {
	   					matchType.getWorld().getLint().typeNotExposedToWeaver.signal(
	   						matchType.getName(), signature.getSourceLocation());
	   				}
   				}
   			}
   			return true;
   		}
   		//System.err.println("NO MATCH DIRECT");
   		
    	if (onType.isInterface()) {
    		return matchType.isTopmostImplementor(onType);
    	} else {
    		return false;
    	}
    }

	// ----

	public String toString() {
		return "ResolvedTypeMunger(" + getKind() + ", " + getSignature() +")";
		//.superMethodsCalled + ")";
	}

	// ----

	public static ResolvedTypeMunger read(DataInputStream s, ISourceContext context) throws IOException {
		Kind kind = Kind.read(s);
		if (kind == Field) {
			return NewFieldTypeMunger.readField(s, context);
		} else if (kind == Method) {
			return NewMethodTypeMunger.readMethod(s, context);

		} else if (kind == Constructor) {
			return NewConstructorTypeMunger.readConstructor(s, context);
		} else {
			throw new RuntimeException("unimplemented");
		}
	}

	protected static Set readSuperMethodsCalled(DataInputStream s) throws IOException {
		Set ret = new HashSet();
		int n = s.readInt();
		for (int i=0; i < n; i++) {
			ret.add(ResolvedMember.readResolvedMember(s, null));
		}
		return ret;
	}
	
	protected void writeSuperMethodsCalled(DataOutputStream s) throws IOException {
		if (superMethodsCalled == null) {
			s.writeInt(0);
			return;
		}
		
		List ret = new ArrayList(superMethodsCalled);
		Collections.sort(ret);
		int n = ret.size();
		s.writeInt(n);
		for (Iterator i = ret.iterator(); i.hasNext(); ) {
			ResolvedMember m = (ResolvedMember)i.next();
			m.write(s);
		}
	}

	
	public abstract void write(DataOutputStream s) throws IOException;

	public Kind getKind() {
		return kind;
	}

	
	
	public static class Kind extends TypeSafeEnum {
		/* private */ Kind(String name, int key) {
			super(name, key);
		}
		
	    public static Kind read(DataInputStream s) throws IOException {
	        int key = s.readByte();
	        switch(key) {
	            case 1: return Field;
	            case 2: return Method;
	            case 5: return Constructor;
	        }
	        throw new BCException("bad kind: " + key);
	    }
	}
	
	// ---- fields
	
	public static final Kind Field = new Kind("Field", 1);
	public static final Kind Method = new Kind("Method", 2);
	public static final Kind Constructor = new Kind("Constructor", 5);
	
	// not serialized, only created during concretization of aspects
	public static final Kind PerObjectInterface = new Kind("PerObjectInterface", 3);
	public static final Kind PrivilegedAccess = new Kind("PrivilegedAccess", 4);
	
	public static final Kind Parent = new Kind("Parent", 6);

	public static final String SUPER_DISPATCH_NAME = "superDispatch";


	public void setSuperMethodsCalled(Set c) {
		this.superMethodsCalled = c;
	}

	public Set getSuperMethodsCalled() {
		return superMethodsCalled;
	}
	

	public ResolvedMember getSignature() {
		return signature;
	}
	
	// ---- 

	public ResolvedMember getMatchingSyntheticMember(Member member, ResolvedTypeX aspectType) {
		if ((getSignature() != null) && getSignature().isPublic() && member.equals(getSignature())) { 
			return getSignature();
		}
			
		return null;
	}

	public boolean changesPublicSignature() {
		return kind == Field || kind == Method || kind == Constructor;
	}
	
	public boolean needsAccessToTopmostImplementor() {
		if (kind == Field) {
			return true;
		} else if (kind == Method) {
			return !signature.isAbstract();
		} else {
			return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8650.java