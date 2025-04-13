error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15680.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15680.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15680.java
text:
```scala
i@@nAspect.crosscuttingMembers.addTypeMunger(new BcelAccessForInlineMunger(inAspect));

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

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Set;

import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.AjcMemberMaker;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ResolvedTypeX;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.bcel.BcelAccessForInlineMunger;
import org.aspectj.weaver.ast.Expr;
import org.aspectj.weaver.ast.Literal;
import org.aspectj.weaver.ast.Test;
import org.aspectj.weaver.ataspectj.Ajc5MemberMaker;

public class PerSingleton extends PerClause {
	public PerSingleton() {
	}

	public Set couldMatchKinds() {
		return Shadow.ALL_SHADOW_KINDS;
	}

	public FuzzyBoolean fastMatch(FastMatchInfo type) {
		return FuzzyBoolean.YES;
	}
	
    protected FuzzyBoolean matchInternal(Shadow shadow) {
        return FuzzyBoolean.YES;
    }

    public void resolveBindings(IScope scope, Bindings bindings) {
    	// this method intentionally left blank
    }

    public Test findResidueInternal(Shadow shadow, ExposedState state) {
        // TODO: the commented code is for slow Aspects.aspectOf() style - keep or remove
        //
        //    	Expr myInstance =
        //    		Expr.makeCallExpr(AjcMemberMaker.perSingletonAspectOfMethod(inAspect),
        //    							Expr.NONE, inAspect);
        //
        //    	state.setAspectInstance(myInstance);
        //
        //    	// we have no test
        //    	// a NoAspectBoundException will be thrown if we need an instance of this
        //    	// aspect before we are bound
        //        return Literal.TRUE;
//        if (!Ajc5MemberMaker.isSlowAspect(inAspect)) {
            Expr myInstance =
                Expr.makeCallExpr(AjcMemberMaker.perSingletonAspectOfMethod(inAspect),
                                    Expr.NONE, inAspect);

            state.setAspectInstance(myInstance);

            // we have no test
            // a NoAspectBoundException will be thrown if we need an instance of this
            // aspect before we are bound
            return Literal.TRUE;
//        } else {
//            CallExpr callAspectOf =Expr.makeCallExpr(
//                    Ajc5MemberMaker.perSingletonAspectOfMethod(inAspect),
//                    new Expr[]{
//                        Expr.makeStringConstantExpr(inAspect.getName(), inAspect),
//                        //FieldGet is using ResolvedType and I don't need that here
//                        new FieldGetOn(Member.ajClassField, shadow.getEnclosingType())
//                    },
//                    inAspect
//            );
//            Expr castedCallAspectOf = new CastExpr(callAspectOf, inAspect.getName());
//            state.setAspectInstance(castedCallAspectOf);
//            return Literal.TRUE;
//        }
    }

	public PerClause concretize(ResolvedTypeX inAspect) {
		PerSingleton ret = new PerSingleton();

        ret.copyLocationFrom(this);

		ret.inAspect = inAspect;

        //ATAJ: add a munger to add the aspectOf(..) to the @AJ aspects
        if (!inAspect.isAbstract() && Ajc5MemberMaker.isAnnotationStyleAspect(inAspect)) {
            //TODO will those change be ok if we add a serializable aspect ?
            // dig: "can't be Serializable/Cloneable unless -XserializableAspects"
            inAspect.crosscuttingMembers.addLateTypeMunger(
                    inAspect.getWorld().makePerClauseAspect(inAspect, getKind())
            );
        }

        //ATAJ inline around advice support
        if (Ajc5MemberMaker.isAnnotationStyleAspect(inAspect)) {
            inAspect.crosscuttingMembers.addLateTypeMunger(new BcelAccessForInlineMunger(inAspect));
        }

        return ret;
	}

    public void write(DataOutputStream s) throws IOException {
    	SINGLETON.write(s);
    	writeLocation(s);
    }
    
	public static PerClause readPerClause(VersionedDataInputStream s, ISourceContext context) throws IOException {
		PerSingleton ret = new PerSingleton();
		ret.readLocation(context, s);
		return ret;
	}
	
	
	public PerClause.Kind getKind() {
		return SINGLETON;
	}
	
	public String toString() {
		return "persingleton(" + inAspect + ")";
	}
	
	public String toDeclarationString() {
		return "";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15680.java