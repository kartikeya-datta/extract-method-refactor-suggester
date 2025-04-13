error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16017.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16017.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16017.java
text:
```scala
r@@eturn new SourceLocationImpl(lexicalClass, this.filename, line);

/* *******************************************************************
 * Copyright (c) 1999-2001 Xerox Corporation, 
 *               2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 * ******************************************************************/


package org.aspectj.runtime.reflect;

import org.aspectj.lang.*;
import org.aspectj.lang.reflect.*;

public final class Factory {    
    Class lexicalClass;
    ClassLoader lookupClassLoader;
    String filename;
    public Factory(String filename, Class lexicalClass) {
        //System.out.println("making
        this.filename = filename;
        this.lexicalClass = lexicalClass;
        lookupClassLoader = lexicalClass.getClassLoader();
    }
    
    public JoinPoint.StaticPart makeSJP(String kind, Signature sig, SourceLocation loc) {
        return new JoinPointImpl.StaticPartImpl(kind, sig, loc);
    }
    
    public JoinPoint.StaticPart makeSJP(String kind, Signature sig, int l, int c) {
        return new JoinPointImpl.StaticPartImpl(kind, sig, makeSourceLoc(l, c));
    }
    
    public JoinPoint.StaticPart makeSJP(String kind, Signature sig, int l) {
        return new JoinPointImpl.StaticPartImpl(kind, sig, makeSourceLoc(l, -1));
    }
    
    private static Object[] NO_ARGS = new Object[0];
	public static JoinPoint makeJP(JoinPoint.StaticPart staticPart, 
						Object _this, Object target)
	{
		return new JoinPointImpl(staticPart, _this, target, NO_ARGS);
	}
    
	public static JoinPoint makeJP(JoinPoint.StaticPart staticPart, 
						Object _this, Object target, Object arg0)
	{
		return new JoinPointImpl(staticPart, _this, target, new Object[] {arg0});
	}
    
	public static JoinPoint makeJP(JoinPoint.StaticPart staticPart, 
						Object _this, Object target, Object arg0, Object arg1)
	{
		return new JoinPointImpl(staticPart, _this, target, new Object[] {arg0, arg1});
	}
    
    
	public static JoinPoint makeJP(JoinPoint.StaticPart staticPart, 
						Object _this, Object target, Object[] args)
	{
		return new JoinPointImpl(staticPart, _this, target, args);
	}
    
    public MethodSignature makeMethodSig(String stringRep) {
        MethodSignatureImpl ret = new MethodSignatureImpl(stringRep);
        ret.setLookupClassLoader(lookupClassLoader);
        return ret;
    }
    
    public MethodSignature makeMethodSig(int modifiers, String name, Class declaringType, 
            Class[] parameterTypes, String[] parameterNames, Class[] exceptionTypes,
	        Class returnType) {
        MethodSignatureImpl ret = new MethodSignatureImpl(modifiers,name,declaringType,parameterTypes,parameterNames,exceptionTypes,returnType);
        ret.setLookupClassLoader(lookupClassLoader);
        return ret;   
    }

    public ConstructorSignature makeConstructorSig(String stringRep) {
        ConstructorSignatureImpl ret = new ConstructorSignatureImpl(stringRep);
        ret.setLookupClassLoader(lookupClassLoader);
        return ret;
    }
    
    public ConstructorSignature makeConstructorSig(int modifiers, Class declaringType, 
            Class[] parameterTypes, String[] parameterNames, Class[] exceptionTypes) {
        ConstructorSignatureImpl ret = new ConstructorSignatureImpl(modifiers,declaringType,parameterTypes,parameterNames,exceptionTypes);
        ret.setLookupClassLoader(lookupClassLoader);
        return ret;  	
    }

    public FieldSignature makeFieldSig(String stringRep) {
        FieldSignatureImpl ret = new FieldSignatureImpl(stringRep);
        ret.setLookupClassLoader(lookupClassLoader);
        return ret;
    }
    
    public FieldSignature makeFieldSig(int modifiers, String name, Class declaringType, 
            Class fieldType) {
        FieldSignatureImpl ret = new FieldSignatureImpl(modifiers,name,declaringType,fieldType);
        ret.setLookupClassLoader(lookupClassLoader);
        return ret;    	
    }

    public AdviceSignature makeAdviceSig(String stringRep) {
        AdviceSignatureImpl ret = new AdviceSignatureImpl(stringRep);
        ret.setLookupClassLoader(lookupClassLoader);
        return ret;
    }

    public AdviceSignature makeAdviceSig(int modifiers, String name, Class declaringType, 
            Class[] parameterTypes, String[] parameterNames, Class[] exceptionTypes,
	        Class returnType) {
        AdviceSignatureImpl ret = new AdviceSignatureImpl(modifiers,name,declaringType,parameterTypes,parameterNames,exceptionTypes,returnType);
        ret.setLookupClassLoader(lookupClassLoader);
        return ret;
    }

    public InitializerSignature makeInitializerSig(String stringRep) {
        InitializerSignatureImpl ret = new InitializerSignatureImpl(stringRep);
        ret.setLookupClassLoader(lookupClassLoader);
        return ret;
    }

    public InitializerSignature makeInitializerSig(int modifiers, Class declaringType) {
        InitializerSignatureImpl ret = new InitializerSignatureImpl(modifiers,declaringType);
        ret.setLookupClassLoader(lookupClassLoader);
        return ret;
    }
    
    public CatchClauseSignature makeCatchClauseSig(String stringRep) {
        CatchClauseSignatureImpl ret = new CatchClauseSignatureImpl(stringRep);
        ret.setLookupClassLoader(lookupClassLoader);
        return ret;
    }
    
    public CatchClauseSignature makeCatchClauseSig(Class declaringType, 
            Class parameterType, String parameterName) {
        CatchClauseSignatureImpl ret = new CatchClauseSignatureImpl(declaringType,parameterType,parameterName);
        ret.setLookupClassLoader(lookupClassLoader);
        return ret;    	
    }
    

    public SourceLocation makeSourceLoc(int line, int col)
    {
        return new SourceLocationImpl(lexicalClass, this.filename, line, col);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16017.java