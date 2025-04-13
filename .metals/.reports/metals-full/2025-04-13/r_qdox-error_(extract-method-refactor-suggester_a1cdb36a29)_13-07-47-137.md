error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2041.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2041.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2041.java
text:
```scala
public O@@bject accept(PatternNodeVisitor visitor, Object data) {

/* *******************************************************************
 * Copyright (c) 2004 IBM Corporation.
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * ******************************************************************/
package org.aspectj.weaver.patterns;

import java.io.DataOutputStream;
import java.io.IOException;

import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.AnnotatedElement;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ResolvedTypeX;
import org.aspectj.weaver.TypeX;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;

/**
 * Matches an annotation of a given type
 */
public class ExactAnnotationTypePattern extends AnnotationTypePattern {

	protected TypeX annotationType;
	protected String formalName;
	protected boolean resolved = false;
	private boolean bindingPattern = false;
	
	/**
	 * 
	 */
	public ExactAnnotationTypePattern(TypeX annotationType) {
		this.annotationType = annotationType;
		this.resolved = (annotationType instanceof ResolvedTypeX);
	}

	protected ExactAnnotationTypePattern(String formalName) {
		this.formalName = formalName;
		this.resolved = false;
		this.bindingPattern = true;
		// will be turned into BindingAnnotationTypePattern during resolution
	}

    public TypeX getAnnotationType() {
        return annotationType;
    }

	public FuzzyBoolean fastMatches(AnnotatedElement annotated) {
		if (annotated.hasAnnotation(annotationType)) {
			return FuzzyBoolean.YES;
		} else {
			// could be inherited, but we don't know that until we are 
			// resolved, and we're not yet...
			return FuzzyBoolean.MAYBE;
		}
	}
	
	public FuzzyBoolean matches(AnnotatedElement annotated) {
		boolean checkSupers = false;
		if (annotationType.hasAnnotation(TypeX.AT_INHERITED)) {
			if (annotated instanceof ResolvedTypeX) {
				checkSupers = true;
			}
		}
		
		if (annotated.hasAnnotation(annotationType)) {
			return FuzzyBoolean.YES;
		} else if (checkSupers) {
			ResolvedTypeX toMatchAgainst = ((ResolvedTypeX) annotated).getSuperclass();
			while (toMatchAgainst != null) {
				if (toMatchAgainst.hasAnnotation(annotationType)) return FuzzyBoolean.YES;
				toMatchAgainst = toMatchAgainst.getSuperclass();
			}
		} 
		return FuzzyBoolean.NO;
	}
	
	// this version should be called for @this, @target, @args
	public FuzzyBoolean matchesRuntimeType(AnnotatedElement annotated) {
		if (annotationType.hasAnnotation(TypeX.AT_INHERITED)) {
			// a static match is good enough
			if (matches(annotated).alwaysTrue()) {
				return FuzzyBoolean.YES;
			} 
		}
		// a subtype could match at runtime
		return FuzzyBoolean.MAYBE;
	}

	
	public void resolve(World world) {
		if (!resolved) annotationType = annotationType.resolve(world);
		resolved = true;
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.AnnotationTypePattern#resolveBindings(org.aspectj.weaver.patterns.IScope, org.aspectj.weaver.patterns.Bindings, boolean)
	 */
	public AnnotationTypePattern resolveBindings(IScope scope,
			Bindings bindings, boolean allowBinding) {
		if (resolved) return this;
		resolved = true;
		String simpleName = maybeGetSimpleName();
		if (simpleName != null) {
			FormalBinding formalBinding = scope.lookupFormal(simpleName);
			if (formalBinding != null) {
				if (bindings == null) {
					scope.message(IMessage.ERROR, this, "negation doesn't allow binding");
					return this;
				}
				if (!allowBinding) {
					scope.message(IMessage.ERROR, this, 
						"name binding only allowed in @pcds, args, this, and target");
					return this;
				}
				formalName = simpleName;
				bindingPattern = true;
				verifyIsAnnotationType(formalBinding.getType(),scope);
				BindingAnnotationTypePattern binding = new BindingAnnotationTypePattern(formalBinding);
				binding.copyLocationFrom(this);
				bindings.register(binding, scope);
				binding.resolveBinding(scope.getWorld());
				
				return binding;
			} 
		}

		// Non binding case
		String cleanname = annotationType.getName();
		annotationType = scope.getWorld().resolve(annotationType,true);
		
		// We may not have found it if it is in a package, lets look it up...
		if (annotationType == ResolvedTypeX.MISSING) {
			TypeX type = null;
			while ((type = scope.lookupType(cleanname,this)) == ResolvedTypeX.MISSING) {
				int lastDot = cleanname.lastIndexOf('.');
				if (lastDot == -1) break;
				cleanname = cleanname.substring(0,lastDot)+"$"+cleanname.substring(lastDot+1);
			}
			annotationType = scope.getWorld().resolve(type,true);
		}
		
		verifyIsAnnotationType(annotationType,scope);
		return this;
	}
	
	private String maybeGetSimpleName() {
		if (formalName != null) return formalName;
		String ret = annotationType.getName();
		return (ret.indexOf('.') == -1) ? ret : null;
	}
	
	/**
	 * @param scope
	 */
	private void verifyIsAnnotationType(TypeX type,IScope scope) {
		if (!type.isAnnotation(scope.getWorld())) {
			IMessage m = MessageUtil.error(
					WeaverMessages.format(WeaverMessages.REFERENCE_TO_NON_ANNOTATION_TYPE,type.getName()),
					getSourceLocation());
			scope.getWorld().getMessageHandler().handleMessage(m);
			resolved = false;
		}
	}

	private static byte VERSION = 1; // rev if serialisation form changes
	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.PatternNode#write(java.io.DataOutputStream)
	 */
	public void write(DataOutputStream s) throws IOException {
		s.writeByte(AnnotationTypePattern.EXACT);
		s.writeByte(VERSION);
		s.writeBoolean(bindingPattern);
		if (bindingPattern) {
			s.writeUTF(formalName);
		} else {
			annotationType.write(s);
		}
		writeLocation(s);
	}

	public static AnnotationTypePattern read(VersionedDataInputStream s,ISourceContext context) throws IOException {
		AnnotationTypePattern ret;
		byte version = s.readByte();
		if (version > VERSION) {
			throw new BCException("ExactAnnotationTypePattern was written by a newer version of AspectJ");
		}
		boolean isBindingPattern = s.readBoolean();
		if (isBindingPattern) {
			ret = new ExactAnnotationTypePattern(s.readUTF());
		} else {
			ret = new ExactAnnotationTypePattern(TypeX.read(s));			
		}
		ret.readLocation(context,s);
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof ExactAnnotationTypePattern)) return false;
		ExactAnnotationTypePattern other = (ExactAnnotationTypePattern) obj;
		return (other.annotationType.equals(annotationType));
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return annotationType.hashCode();
	}
	
	public String toString() {
	    if (!resolved && formalName != null) return formalName;
		String ret = "@" + annotationType.toString();
		if (formalName != null) ret = ret + " " + formalName;
		return ret;
	}

    public Object accept(PointcutVisitor visitor, Object data) {
        return visitor.visit(this, data);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2041.java