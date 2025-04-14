error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5366.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5366.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5366.java
text:
```scala
g@@etPointcut().parameterizeWith(typeVariableMap,declaringType.getWorld()),

/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 *  
 * Contributors: 
 *     PARC     initial implementation 
 * ******************************************************************/


package org.aspectj.weaver;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.aspectj.asm.AsmManager;
import org.aspectj.asm.IRelationship;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.Message;
import org.aspectj.weaver.patterns.DeclareErrorOrWarning;
import org.aspectj.weaver.patterns.PerClause;
import org.aspectj.weaver.patterns.Pointcut;


public class Checker extends ShadowMunger {

	private String msg;
	private boolean isError;

	public Checker(DeclareErrorOrWarning deow) {
		super(deow.getPointcut(), deow.getStart(), deow.getEnd(), deow.getSourceContext());
		this.msg = deow.getMessage();
		this.isError = deow.isError();
	}		
	
	private Checker(Pointcut pc, int start, int end, ISourceContext context) {
		super(pc,start,end,context);
	}

    public ShadowMunger concretize(ResolvedType fromType, World world, PerClause clause) {
        pointcut = pointcut.concretize(fromType, getDeclaringType(), 0, this);
        return this;
    }

	public void specializeOn(Shadow shadow) {
		throw new RuntimeException("illegal state");
	}

	public void implementOn(Shadow shadow) {
		throw new RuntimeException("illegal state");
	}
	
	public ShadowMunger parameterizeWith(ResolvedType declaringType,Map typeVariableMap) {
		Checker ret = new Checker(
							getPointcut().parameterizeWith(typeVariableMap),
							getStart(),
							getEnd(),
							this.sourceContext);
		ret.msg = this.msg;
		ret.isError = this.isError;
		return ret;
	}

	public boolean match(Shadow shadow, World world) {
		if (super.match(shadow, world)) {
			IMessage message = new Message(
				msg,
				shadow.toString(),
				isError ? IMessage.ERROR : IMessage.WARNING,
				shadow.getSourceLocation(),
                null,
                new ISourceLocation[]{this.getSourceLocation()},true,
				0, // id
				-1,-1); // source start/end
            
            world.getMessageHandler().handleMessage(message);
			
			if (world.getCrossReferenceHandler() != null) {
				world.getCrossReferenceHandler().addCrossReference(this.getSourceLocation(),
				  shadow.getSourceLocation(),
				  (this.isError?IRelationship.Kind.DECLARE_ERROR:IRelationship.Kind.DECLARE_WARNING),false);
			
			}
			
			if (world.getModel() != null) {
				AsmRelationshipProvider.getDefault().checkerMunger(world.getModel(), shadow, this);
			}
		}
		return false;
	}

	public int compareTo(Object other) {
		return 0;
	}
	
	public Collection getThrownExceptions() { return Collections.EMPTY_LIST; }

    /**
     * Default to true
     * FIXME Alex: ATAJ is that ok in all cases ? 
     * @return
     */
    public boolean mustCheckExceptions() { return true; }


	// XXX this perhaps ought to take account of the other fields in advice ...
    public boolean equals(Object other) {
        if (! (other instanceof Checker)) return false;
        Checker o = (Checker) other;
        return  
          o.isError == isError &&
        	 ((o.pointcut == null) ? (pointcut == null) : o.pointcut.equals(pointcut)) &&
          (AsmManager.getDefault().getHandleProvider().dependsOnLocation()
        		  ?((o.getSourceLocation()==null) ? (getSourceLocation()==null): o.getSourceLocation().equals(getSourceLocation())):true) // pr134471 - remove when handles are improved to be independent of location
        	;
    }

	private volatile int hashCode = -1;
    public int hashCode() {
    	if (hashCode == -1) {
            int result = 17;
            result = 37*result + (isError?1:0);
            result = 37*result + ((pointcut == null) ? 0 : pointcut.hashCode());
            hashCode = result;			
		}
        return hashCode;
    }

	public boolean isError() {
		return isError;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5366.java