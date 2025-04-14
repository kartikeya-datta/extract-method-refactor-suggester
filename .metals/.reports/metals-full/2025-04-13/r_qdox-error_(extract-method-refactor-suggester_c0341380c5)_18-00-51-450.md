error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9578.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9578.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9578.java
text:
```scala
private T@@ype analyzeOperationCall (ExecutionContext ctx, @SuppressWarnings("unused") OperationCall expr) {

/*
Copyright (c) 2008 Arno Haase.
All rights reserved. This program and the accompanying materials
are made available under the terms of the Eclipse Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/epl-v10.html

Contributors:
    Arno Haase - initial API and implementation
 */
package org.eclipse.xtend.middleend.old;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.internal.xtend.expression.ast.BooleanLiteral;
import org.eclipse.internal.xtend.expression.ast.BooleanOperation;
import org.eclipse.internal.xtend.expression.ast.Case;
import org.eclipse.internal.xtend.expression.ast.ChainExpression;
import org.eclipse.internal.xtend.expression.ast.CollectionExpression;
import org.eclipse.internal.xtend.expression.ast.ConstructorCallExpression;
import org.eclipse.internal.xtend.expression.ast.Expression;
import org.eclipse.internal.xtend.expression.ast.FeatureCall;
import org.eclipse.internal.xtend.expression.ast.GlobalVarExpression;
import org.eclipse.internal.xtend.expression.ast.IfExpression;
import org.eclipse.internal.xtend.expression.ast.IntegerLiteral;
import org.eclipse.internal.xtend.expression.ast.LetExpression;
import org.eclipse.internal.xtend.expression.ast.ListLiteral;
import org.eclipse.internal.xtend.expression.ast.NullLiteral;
import org.eclipse.internal.xtend.expression.ast.OperationCall;
import org.eclipse.internal.xtend.expression.ast.RealLiteral;
import org.eclipse.internal.xtend.expression.ast.StringLiteral;
import org.eclipse.internal.xtend.expression.ast.SwitchExpression;
import org.eclipse.internal.xtend.expression.ast.TypeSelectExpression;
import org.eclipse.internal.xtend.expression.parser.SyntaxConstants;
import org.eclipse.internal.xtend.xtend.ast.CreateExtensionStatement;
import org.eclipse.internal.xtend.xtend.ast.ExpressionExtensionStatement;
import org.eclipse.internal.xtend.xtend.ast.Extension;
import org.eclipse.internal.xtend.xtend.ast.JavaExtensionStatement;
import org.eclipse.xtend.expression.ExecutionContext;
import org.eclipse.xtend.expression.Variable;
import org.eclipse.xtend.typesystem.ParameterizedType;
import org.eclipse.xtend.typesystem.Property;
import org.eclipse.xtend.typesystem.StaticProperty;
import org.eclipse.xtend.typesystem.Type;


/**
 * This class is a visitor that serves as a replacement for the "analyze"
 *  methods of the Expression classes. While the "analyze" methods perform
 *  strict static checking, this class does a "best effort": It finds
 *  the best statically predictable type, based on the assumption that
 *  the code is dynamically correct.<br>
 *  
 * An example can illustrate this. Let "x" be a collection with statically
 *  unknown inner type. Then the two approaches yield different results for
 *  "x.myProperty". The analyze methods in the expressions return "null"
 *  because they can not statically ensure the type correctness of the 
 *  expression. This class on the other hand returns the type "Collection[Object]".
 * 
 * @author Arno Haase (http://www.haase-consulting.com)
 */
public class OldTypeAnalyzer {
    public Type analyze (ExecutionContext ctx, Extension ext, Type[] paramTypes) {
        if (ext instanceof JavaExtensionStatement)
            return analyzeJavaExtension (ctx, (JavaExtensionStatement) ext);
        if (ext instanceof ExpressionExtensionStatement)
            return analyzeExpressionExtension (ctx, (ExpressionExtensionStatement) ext, paramTypes);
        if (ext instanceof CreateExtensionStatement)
            return ctx.getTypeForName (((CreateExtensionStatement) ext).getReturnTypeIdentifier().getValue());;
        
        throw new IllegalArgumentException ("unknown extension type " + ext.getClass().getName());
    }
    
    private Type analyzeExpressionExtension (ExecutionContext ctx, ExpressionExtensionStatement ext, Type[] paramTypes) {
        ctx = ctx.cloneWithoutVariables();
        
        for (int i=0; i<ext.getParameterNames().size(); i++) {
            final String name = ext.getParameterNames().get(i);
            final Type value = paramTypes[i];
            
            ctx = ctx.cloneWithVariable (new Variable (name, value));
        }
        
        return analyze (ctx, ext.getExpression());
    }
    
    private Type analyzeJavaExtension (ExecutionContext ctx, JavaExtensionStatement ext) {
        if (ext.getReturnTypeIdentifier() != null) 
            return ctx.getTypeForName (ext.getReturnTypeIdentifier().getValue());
        
        return ctx.getObjectType(); // Typesystem provides no support for evaluating the return type of the Method...
    }
    
    
    public Type analyze (ExecutionContext ctx, Expression expr) {
        if (expr instanceof BooleanLiteral)
            return ctx.getBooleanType();
        if (expr instanceof IntegerLiteral)
            return ctx.getIntegerType();
        if (expr instanceof NullLiteral)
            return ctx.getVoidType();
        if (expr instanceof RealLiteral)
            return ctx.getRealType();
        if (expr instanceof StringLiteral)
            return ctx.getStringType();
        if (expr instanceof ListLiteral)
            return analyzeListLiteral (ctx, (ListLiteral) expr);

        if (expr instanceof OperationCall)
            return analyzeOperationCall (ctx, (OperationCall) expr);
        if (expr instanceof CollectionExpression)
            return analyzeCollectionExpression (ctx, (CollectionExpression) expr);
        if (expr instanceof TypeSelectExpression)
            return analyzeTypeSelect (ctx, (TypeSelectExpression) expr);
        
        // This case must come *after* OperationCall etc. because of implementation inheritance in the Xtend AST!
        if (expr instanceof FeatureCall)
            return analyzeFeatureCall (ctx, (FeatureCall) expr);
        
        if (expr instanceof BooleanOperation)
            return ctx.getBooleanType();
        
        if (expr instanceof GlobalVarExpression)
            return analyzeGlobalVar (ctx, (GlobalVarExpression) expr);
        if (expr instanceof LetExpression)
            return analyzeLet (ctx, (LetExpression) expr);
        if (expr instanceof ChainExpression)
            return analyzeChain (ctx, (ChainExpression) expr);

        if (expr instanceof ConstructorCallExpression)
            return analyzeConstructorCall (ctx, (ConstructorCallExpression) expr);
        
        if (expr instanceof IfExpression)
            return analyzeIf (ctx, (IfExpression) expr);
        if (expr instanceof SwitchExpression)
            return analyzeSwitch (ctx, (SwitchExpression) expr);
        
        throw new IllegalArgumentException ("unknown expression kind " + expr.getClass().getName());
    }

    private Type analyzeListLiteral (ExecutionContext ctx, ListLiteral expr) {
        Type innerType = null;
        
        for (Expression ele: expr.getElements()) {
            if (innerType == null)
                innerType = analyze (ctx, ele);
            else 
                innerType = getCommonSupertype (innerType, analyze (ctx, ele));
        }
        
        if (innerType == null)
            innerType = ctx.getObjectType();
        
        return ctx.getListType (innerType);
    }

    private Type analyzeOperationCall (ExecutionContext ctx, OperationCall expr) {
        return ctx.getObjectType(); 
        
        // it would require extreme effort to find matching extensions etc. because we do not know
        //  the parameter types but their *super*types, so ObjectType is a safe assumption here.
    }
    
    
    private Type analyzeCollectionExpression (ExecutionContext ctx, CollectionExpression expr) {
        if (Arrays.asList (SyntaxConstants.COLLECT, SyntaxConstants.SELECT, SyntaxConstants.REJECT, SyntaxConstants.SORT_BY).contains (expr.getName().getValue()))
            return analyze (ctx, expr.getTarget());

        if (expr.getName().getValue().equals(SyntaxConstants.SELECTFIRST))
            return ((ParameterizedType) analyze (ctx, expr.getTarget())).getInnerType();
 
        if (Arrays.asList (SyntaxConstants.EXISTS, SyntaxConstants.NOT_EXISTS, SyntaxConstants.FOR_ALL).contains (expr.getName().getValue()))
            return ctx.getBooleanType();

        throw new IllegalArgumentException ("unknown collection operation " + expr.getName().getValue());
    }
    
    private Type analyzeTypeSelect (ExecutionContext ctx, TypeSelectExpression expr) {
        final Type innerType = ctx.getTypeForName (expr.getTypeName ());
        return ctx.getCollectionType(innerType);
    }

    private Type analyzeFeatureCall (ExecutionContext ctx, FeatureCall expr) {
        Type targetType = null;
        if (expr.getTarget() == null) {
            // enum literal
            final StaticProperty staticProp = expr.getEnumLiteral(ctx);
            if (staticProp != null)
                return staticProp.getReturnType();

            // variable
            Variable var = ctx.getVariable (expr.getName().getValue());
            if (var != null)
                return (Type) var.getValue();

            // implicit variable 'this'
            var = ctx.getVariable (ExecutionContext.IMPLICIT_VARIABLE);
            if (var != null) 
                targetType = (Type) var.getValue();

        } 
        else 
            targetType = analyze (ctx, expr.getTarget());

        // simple property
        if (targetType != null) {
            Property p = targetType.getProperty (expr.getName().getValue());
            if (p != null)
                return p.getReturnType();

            if (targetType instanceof ParameterizedType) {
                final Type innerType = ((ParameterizedType) targetType).getInnerType ();
                p = innerType.getProperty (expr.getName().getValue());
                if (p != null) {
                    Type rt = p.getReturnType();
                    
                    if (rt instanceof ParameterizedType) 
                        rt = ((ParameterizedType) rt).getInnerType();

                    return ctx.getListType(rt);
                }
            }
            
            return ctx.getObjectType();
        }
        
        // type literal
        if (expr.getTarget() == null) {
            final Type type = ctx.getTypeForName (expr.getName().getValue());
            if (type != null)
                return ctx.getTypeType();
        }

        return ctx.getObjectType();
    }

    private Type analyzeGlobalVar (ExecutionContext ctx, @SuppressWarnings("unused") GlobalVarExpression expr) {
        return ctx.getObjectType();
    }
    
    private Type analyzeLet (ExecutionContext ctx, LetExpression expr) {
        final Type t = analyze (ctx, expr.getVarExpression());
        ctx = ctx.cloneWithVariable (new Variable (expr.getName(), t));
        return analyze (ctx, expr.getTargetExpression());
    }

    private Type analyzeChain (ExecutionContext ctx, ChainExpression expr) {
        return analyze (ctx, expr.getNext());
    }

    private Type analyzeConstructorCall (ExecutionContext ctx, ConstructorCallExpression expr) {
        return ctx.getTypeForName (expr.getTypeName());
    }
    
    private Type analyzeIf (ExecutionContext ctx, IfExpression expr) {
        if (expr.getElsePart() == null)
            return analyze (ctx, expr.getThenPart());
        else
            return getCommonSupertype (analyze (ctx, expr.getThenPart()), analyze (ctx, expr.getElsePart()));
    }
    
    private Type analyzeSwitch (ExecutionContext ctx, SwitchExpression expr) {
        Type result = analyze (ctx, expr.getDefaultExpr());
        for (Case curCase: expr.getCases())
            result = getCommonSupertype (result, analyze (ctx, curCase.getThenPart()));
        
        return result;
    }
    
    private Type getCommonSupertype (Type t1, Type t2) {
        if (t1.isAssignableFrom (t2))
            return t1;
        if (t2.isAssignableFrom (t1))
            return t2;
        
        final Set<Type> commonSupertypes = new HashSet<Type>();
        for (Type parent1: t1.getSuperTypes())
            for (Type parent2: t2.getSuperTypes())
                commonSupertypes.add (getCommonSupertype (parent1, parent2));

        // this is an arbitrary way to disambiguate in the case of several matches / multiple inheritance
        final Iterator<Type> iter = commonSupertypes.iterator();
        Type result = iter.next();
        while (iter.hasNext()) {
            final Type candidate = iter.next();
            if (candidate.isAssignableFrom (result))
                result = candidate;
        }
        
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9578.java