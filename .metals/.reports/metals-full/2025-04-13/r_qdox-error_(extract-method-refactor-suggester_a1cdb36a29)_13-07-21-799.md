error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6403.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6403.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6403.java
text:
```scala
T@@ypePatternList arguments = this.arguments.resolveReferences(bindings);

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
import java.lang.reflect.Modifier;

import org.apache.bcel.classfile.JavaClass;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.ResolvedPointcutDefinition;
import org.aspectj.weaver.ResolvedTypeX;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.TypeX;
import org.aspectj.weaver.ast.Test;

/**
 */

//XXX needs check that arguments contains no WildTypePatterns
public class ReferencePointcut extends Pointcut {
	public TypeX onType; 
	public TypePattern onTypeSymbolic; 
	public String name;
	public TypePatternList arguments;
	
	//public ResolvedPointcut binding;
	
	public ReferencePointcut(TypePattern onTypeSymbolic, String name, TypePatternList arguments) {
		this.onTypeSymbolic = onTypeSymbolic;
		this.name = name;
		this.arguments = arguments;
	}
	
	public ReferencePointcut(TypeX onType, String name, TypePatternList arguments) {
		this.onType = onType;
		this.name = name;
		this.arguments = arguments;
	}
	

	//??? do either of these match methods make any sense???
	public FuzzyBoolean fastMatch(ResolvedTypeX type) {
		return FuzzyBoolean.MAYBE;
	}
	
	/**
	 * Do I really match this shadow?
	 */
	public FuzzyBoolean match(Shadow shadow) {
		return FuzzyBoolean.NO;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		if (onType != null) {
			buf.append(onType);
			buf.append(".");
//			for (int i=0, len=fromType.length; i < len; i++) {
//				buf.append(fromType[i]);
//				buf.append(".");
//			}
		}
		buf.append(name);
		buf.append(arguments.toString());
		return buf.toString();
	}
	

	public void write(DataOutputStream s) throws IOException {
		//XXX ignores onType
		s.writeByte(Pointcut.REFERENCE);
		if (onType != null) {
			s.writeBoolean(true);
			onType.write(s);
		} else {
			s.writeBoolean(false);
		}
		
		s.writeUTF(name);
		arguments.write(s);
		writeLocation(s);
	}
	
	public static Pointcut read(DataInputStream s, ISourceContext context) throws IOException {
		TypeX onType = null;
		if (s.readBoolean()) {
			onType = TypeX.read(s);
		}
		ReferencePointcut ret = new ReferencePointcut(onType, s.readUTF(), 
					TypePatternList.read(s, context));
		ret.readLocation(context, s);
		return ret;
	}
	
	public void resolveBindings(IScope scope, Bindings bindings) {
		if (onTypeSymbolic != null) {
			onType = onTypeSymbolic.resolveExactType(scope, bindings);
			// in this case we've already signalled an error
			if (onType == ResolvedTypeX.MISSING) return;		
		}
		
		ResolvedTypeX searchType;
		if (onType != null) {
			searchType = scope.getWorld().resolve(onType);
		} else {
			searchType = scope.getEnclosingType();
		}
		
		
		arguments.resolveBindings(scope, bindings, true, true);
		//XXX ensure that arguments has no ..'s in it
		
		// check that I refer to a real pointcut declaration and that I match
		
		ResolvedPointcutDefinition pointcutDef = searchType.findPointcut(name);
		// if we're not a static reference, then do a lookup of outers
		if (pointcutDef == null && onType == null) {
			while (true) {
				TypeX declaringType = searchType.getDeclaringType();
				if (declaringType == null) break;
				searchType = declaringType.resolve(scope.getWorld());
				pointcutDef = searchType.findPointcut(name);
				if (pointcutDef != null) {
					// make this a static reference
					onType = searchType;
					break;
				}
			}
		}
		
		if (pointcutDef == null) {
			scope.message(IMessage.ERROR, this, "can't find referenced pointcut");
			return;
		}
		
		// check visibility
		if (!pointcutDef.isVisible(scope.getEnclosingType())) {
			scope.message(IMessage.ERROR, this, "pointcut declaration " + pointcutDef + " is not accessible");
			return;
		}
		
		if (Modifier.isAbstract(pointcutDef.getModifiers())) {
			if (onType != null) {
				scope.message(IMessage.ERROR, this, 
								"can't make static reference to abstract pointcut");
				return;
			} else if (!searchType.isAbstract()) {
				scope.message(IMessage.ERROR, this,
								"can't use abstract pointcut in concrete context");
				return;
			}
		}
		
		
		ResolvedTypeX[] parameterTypes = 
			scope.getWorld().resolve(pointcutDef.getParameterTypes());
		
		if (parameterTypes.length != arguments.size()) {
			scope.message(IMessage.ERROR, this, "incompatible number of arguments to pointcut, expected " +
						parameterTypes.length + " found " + arguments.size());
			return;
		}
		
		
		
		for (int i=0,len=arguments.size(); i < len; i++) {
			TypePattern p = arguments.get(i);
			//we are allowed to bind to pointcuts which use subtypes as this is type safe
			if (p == TypePattern.NO) {
				scope.message(IMessage.ERROR, this,
								"bad parameter to pointcut reference");
				return;
			}
			if (!p.matchesSubtypes(parameterTypes[i]) && 
				!p.getExactType().equals(TypeX.OBJECT))
			{
				scope.message(IMessage.ERROR, p, "incompatible type, expected " +
						parameterTypes[i].getName() + " found " + p);
				return;
			}
		}
	}
	
	public void postRead(ResolvedTypeX enclosingType) {
		arguments.postRead(enclosingType);
	}

	public Test findResidue(Shadow shadow, ExposedState state) {
		throw new RuntimeException("shouldn't happen");
	}


	//??? This is not thread safe, but this class is not designed for multi-threading
	private boolean concretizing = false;
	public Pointcut concretize1(ResolvedTypeX searchStart, IntMap bindings) {
		if (concretizing) {
			//Thread.currentThread().dumpStack();
			searchStart.getWorld().getMessageHandler().handleMessage(
				MessageUtil.error("circular pointcut declaration involving: " + this,
									getSourceLocation()));
			return Pointcut.makeMatchesNothing(Pointcut.CONCRETE);
		}
		
		try {
			concretizing = true;
		
			ResolvedPointcutDefinition pointcutDec;
			if (onType != null) {
				searchStart = onType.resolve(searchStart.getWorld());
				if (searchStart == ResolvedTypeX.MISSING) {
					return Pointcut.makeMatchesNothing(Pointcut.CONCRETE);
				}
			}
			pointcutDec = searchStart.findPointcut(name);
			if (pointcutDec == null) {
				searchStart.getWorld().getMessageHandler().handleMessage(
					MessageUtil.error("can't find pointcut \'" + name + "\' on " + searchStart.getName(), 
									getSourceLocation())
				);
				return Pointcut.makeMatchesNothing(Pointcut.CONCRETE);
			}
			
			if (pointcutDec.isAbstract()) {
				//Thread.currentThread().dumpStack();
				searchStart.getWorld().showMessage(IMessage.ERROR,
					pointcutDec + " is abstract", 
					getSourceLocation(), bindings.getEnclosingAdvice().getSourceLocation());
				return Pointcut.makeMatchesNothing(Pointcut.CONCRETE);
			}
					
			//System.err.println("start: " + searchStart);
			ResolvedTypeX[] parameterTypes = searchStart.getWorld().resolve(pointcutDec.getParameterTypes());
			
			arguments = arguments.resolveReferences(bindings);
			
			IntMap newBindings = new IntMap();
			for (int i=0,len=arguments.size(); i < len; i++) {
				TypePattern p = arguments.get(i);
				//we are allowed to bind to pointcuts which use subtypes as this is type safe
				if (!p.matchesSubtypes(parameterTypes[i])  && 
					!p.getExactType().equals(TypeX.OBJECT))
				{
					throw new BCException("illegal change to pointcut declaration: " + this);
				}
				
			    if (p instanceof BindingTypePattern) {
			    	newBindings.put(i, ((BindingTypePattern)p).getFormalIndex());
			    }
			}
			
			newBindings.copyContext(bindings);
			newBindings.pushEnclosingDefinition(pointcutDec);
			try {
				return pointcutDec.getPointcut().concretize1(searchStart, newBindings);
			} finally {
				newBindings.popEnclosingDefinitition();
			}
			
		} finally {
			concretizing = false;
		}
	}

    public boolean equals(Object other) { 
        if (!(other instanceof ReferencePointcut)) return false;
        ReferencePointcut o = (ReferencePointcut)other;
        return o.name.equals(name) && o.arguments.equals(arguments)
            && ((o.onType == null) ? (onType == null) : o.onType.equals(onType));
    }
    public int hashCode() {
        int result = 17;
        result = 37*result + ((onType == null) ? 0 : onType.hashCode());
        result = 37*result + arguments.hashCode();
        result = 37*result + name.hashCode();
        return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6403.java