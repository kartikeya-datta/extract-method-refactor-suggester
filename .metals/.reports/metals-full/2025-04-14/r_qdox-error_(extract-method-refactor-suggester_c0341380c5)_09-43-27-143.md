error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/784.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/784.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/784.java
text:
```scala
i@@f (searchStart.isMissing()) {

/*******************************************************************************
 * Copyright (c) 2005 Contributors.
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *   Alexandre Vasseur         initial implementation
 *******************************************************************************/
package org.aspectj.weaver.patterns;

import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.ResolvedPointcutDefinition;
import org.aspectj.weaver.ResolvedType;

/**
 * A visitor that turns a pointcut into a type pattern equivalent for a perthis or pertarget matching:
 * - pertarget(target(Foo)) => Foo+ (this one is a special case..)
 * - pertarget(execution(* Foo.do()) => Foo
 * - perthis(call(* Foo.do()) => *
 * - perthis(!call(* Foo.do()) => * (see how the ! has been absorbed here..)
 *
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class PerThisOrTargetPointcutVisitor extends IdentityPointcutVisitor {

    /** A maybe marker */
    private final static TypePattern MAYBE = new TypePatternMayBe();

    private final boolean m_isTarget;
    private final ResolvedType m_fromAspectType;

    public PerThisOrTargetPointcutVisitor(boolean isTarget, ResolvedType fromAspectType) {
        m_isTarget = isTarget;
        m_fromAspectType = fromAspectType;
    }

    public TypePattern getPerTypePointcut(Pointcut perClausePointcut) {
        return (TypePattern) perClausePointcut.accept(this, perClausePointcut);
    }

    //-- visitor methods, all is like Identity visitor except when it comes to transform pointcuts

    public Object visit(WithinPointcut node, Object data) {
        if (m_isTarget) {
            //pertarget(.. &&  within(Foo)) => true
            //pertarget(.. && !within(Foo)) => true as well !
            return MAYBE;
        } else {
            return node.getTypePattern();
        }
    }

    public Object visit(WithincodePointcut node, Object data) {
        if (m_isTarget) {
            //pertarget(.. &&  withincode(* Foo.do())) => true
            //pertarget(.. && !withincode(* Foo.do())) => true as well !
            return MAYBE;
        } else {
            return node.getSignature().getDeclaringType();
        }
    }

    public Object visit(WithinAnnotationPointcut node, Object data) {
        if (m_isTarget) {
            return MAYBE;
        } else {
            return node.getAnnotationTypePattern();
        }
    }

    public Object visit(WithinCodeAnnotationPointcut node, Object data) {
        if (m_isTarget) {
            return MAYBE;
        } else {
            return MAYBE;//FIXME AV - can we optimize ? perthis(@withincode(Foo)) = hasmethod(..)
        }
    }

    public Object visit(KindedPointcut node, Object data) {
        if (node.getKind().equals(Shadow.AdviceExecution)) {
            return MAYBE;//TODO AV - can we do better ?
        } else if (node.getKind().equals(Shadow.ConstructorExecution)
 node.getKind().equals(Shadow.Initialization)
 node.getKind().equals(Shadow.MethodExecution)
 node.getKind().equals(Shadow.PreInitialization)
 node.getKind().equals(Shadow.StaticInitialization)) {
            return node.getSignature().getDeclaringType();
        } else if (node.getKind().equals(Shadow.ConstructorCall)
 node.getKind().equals(Shadow.FieldGet)
 node.getKind().equals(Shadow.FieldSet)
 node.getKind().equals(Shadow.MethodCall)) {
            if (m_isTarget) {
                return node.getSignature().getDeclaringType();
            } else {
                return MAYBE;
            }
        } else if (node.getKind().equals(Shadow.ExceptionHandler)) {
            return MAYBE;
        } else {
            throw new ParserException("Undetermined - should not happen: " + node.getKind().getSimpleName(), null);
        }
    }

    public Object visit(AndPointcut node, Object data) {
        return new AndTypePattern(getPerTypePointcut(node.left), getPerTypePointcut(node.right));
    }

    public Object visit(OrPointcut node, Object data) {
        return new OrTypePattern(getPerTypePointcut(node.left), getPerTypePointcut(node.right));
    }

    public Object visit(NotPointcut node, Object data) {
//        TypePattern negated = getPerTypePointcut(node.getNegatedPointcut());
//        if (MAYBE.equals(negated)) {
//            return MAYBE;
//        }
//        return new NotTypePattern(negated);
    	    // AMC - the only safe thing to return here is maybe...
    		// see for example pr114054
    	return MAYBE;
    }

    public Object visit(ThisOrTargetAnnotationPointcut node, Object data) {
        if (m_isTarget && !node.isThis()) {
            return node.getAnnotationTypePattern();
        } else if (!m_isTarget && node.isThis()) {
            return node.getAnnotationTypePattern();
        } else {
            // perthis(@target(Foo))
            return MAYBE;
        }
    }

    public Object visit(ThisOrTargetPointcut node, Object data) {
        if ((m_isTarget && !node.isThis())
 (!m_isTarget && node.isThis())) {
        	String pointcutString = node.getType().toString();
        	// see pr115788 "<nothing>" means there was a problem resolving types - that will be reported so dont blow up
        	// the parser here..
        	if (pointcutString.equals("<nothing>")) {
        		return new NoTypePattern();
        	}
            //pertarget(target(Foo)) => Foo+ for type pattern matching
            //perthis(this(Foo)) => Foo+ for type pattern matching
            // TODO AV - we do like a deep copy by parsing it again.. quite dirty, would need a clean deep copy
            TypePattern copy = new PatternParser(pointcutString.replace('$', '.')).parseTypePattern();
            // TODO AV - see dirty replace from $ to . here as inner classes are with $ instead (#108488)
            copy.includeSubtypes = true;
            return copy;
        } else {
            // perthis(target(Foo)) => maybe
            return MAYBE;
        }
    }

    public Object visit(ReferencePointcut node, Object data) {
        // && pc_ref()
        // we know there is no support for binding in perClause: perthis(pc_ref(java.lang.String))
        // TODO AV - may need some work for generics..

        ResolvedPointcutDefinition pointcutDec;
        ResolvedType searchStart = m_fromAspectType;
        if (node.onType != null) {
            searchStart = node.onType.resolve(m_fromAspectType.getWorld());
            if (searchStart == ResolvedType.MISSING) {
                return MAYBE;// this should not happen since concretize will fails but just in case..
            }
        }
        pointcutDec = searchStart.findPointcut(node.name);

        return getPerTypePointcut(pointcutDec.getPointcut());
    }

    public Object visit(IfPointcut node, Object data) {
        return TypePattern.ANY;
    }

    public Object visit(HandlerPointcut node, Object data) {
        // quiet unexpected since a KindedPointcut but do as if...
        return MAYBE;
    }

    public Object visit(CflowPointcut node, Object data) {
        return MAYBE;
    }

    public Object visit(ConcreteCflowPointcut node, Object data) {
        return MAYBE;
    }

    public Object visit(ArgsPointcut node, Object data) {
        return MAYBE;
    }

    public Object visit(ArgsAnnotationPointcut node, Object data) {
        return MAYBE;
    }

    public Object visit(AnnotationPointcut node, Object data) {
        return MAYBE;
    }

    public Object visit(Pointcut.MatchesNothingPointcut node, Object data) {
        // a small hack since the usual MatchNothing has its toString = "<nothing>" which is not parseable back
        // while I use back parsing for check purpose.
        return new NoTypePattern() {
            public String toString() {
                return "false";
            }
        };
    }

    /**
     * A MayBe type pattern that acts as ANY except that !MAYBE = MAYBE
     *
     * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
     */
    private static class TypePatternMayBe extends AnyTypePattern {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/784.java