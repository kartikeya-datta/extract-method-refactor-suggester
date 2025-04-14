error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2418.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2418.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2418.java
text:
```scala
static S@@tring PREINITIALIZATION = "preinitialization";

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


package org.aspectj.lang;

import org.aspectj.lang.reflect.SourceLocation;

/**
 * <p>Provides reflective access to both the state available at a join point and
 * static information about it.  This information is available from the body
 * of advice using the special form <code>thisJoinPoint</code>.  The primary
 * use of this reflective information is for tracing and logging applications.
 * </p>
 *
 * <pre>
 * aspect Logging {
 *     before(): within(com.bigboxco..*) && execution(public * *(..)) {
 *         System.err.println("entering: " + thisJoinPoint);
 *         System.err.println("  w/args: " + thisJoinPoint.getArgs());
 *         System.err.println("      at: " + thisJoinPoint.getSourceLocation());
 *     }
 * }
 * </pre>
 */
public interface JoinPoint {

    String toString();

    /**
     * Returns an abbreviated string representation of the join point.
     */
    String toShortString();

    /**
     * Returns an extended string representation of the join point.
     */
    String toLongString();

    /**
     * <p> Returns the currently executing object.  This will always be
     * the same object as that matched by the <code>this</code> pointcut
     * designator.  Unless you specifically need this reflective access,
     * you should use the <code>this</code> pointcut designator to
     * get at this object for better static typing and performance.</p>
     *
     * <p> Returns null when there is no currently executing object available.
     * This includes all join points that occur in a static context.</p>
     */
    Object getThis();

    /**
     * <p> Returns the target object.  This will always be
     * the same object as that matched by the <code>target</code> pointcut
     * designator.  Unless you specifically need this reflective access,
     * you should use the <code>target</code> pointcut designator to
     * get at this object for better static typing and performance.</p>
     *
     * <p> Returns null when there is no target object.</p>

     */
    Object getTarget();

    /**
     * <p>Returns the arguments at this join point.</p>
     */
    Object[] getArgs();

    /** Returns the signature at the join point.
     *
     * <code>getStaticPart().getSignature()</code> returns the same object
     */
    Signature getSignature();

    /** <p>Returns the source location corresponding to the join point.</p>
     *
     *  <p>If there is no source location available, returns null.</p>
     *
     *  <p>Returns the SourceLocation of the defining class for default constructors.</p>
     *
     *  <p> <code>getStaticPart().getSourceLocation()</code> returns the same object. </p>
     */
    SourceLocation getSourceLocation();

    /** Returns a String representing the kind of join point.  This
     *       String is guaranteed to be
     *       interned. <code>getStaticPart().getKind()</code> returns
     *       the same object.
     */
    String getKind();


    /**
     * <p>This helper object contains only the static information about a join point.
     * It is available from the <code>JoinPoint.getStaticPart()</code> method, and
     * can be accessed separately within advice using the special form
     * <code>thisJoinPointStaticPart</code>.</p>
     *
     * <p>If you are only interested in the static information about a join point,
     * you should access it through this type for the best performance.  This
     * is particularly useful for library methods that want to do serious
     * manipulations of this information, i.e.</p>
     *
     * <pre>
     * public class LoggingUtils {
     *     public static void prettyPrint(JoinPoint.StaticPart jp) {
     *         ...
     *     }
     * }
     *
     * aspect Logging {
     *     before(): ... { LoggingUtils.prettyPrint(thisJoinPointStaticPart); }
     * }
     * </pre>
     *
     * @see JoinPoint#getStaticPart()
     */
    public interface StaticPart {
        /** Returns the signature at the join point.  */
        Signature getSignature();

        /** <p>Returns the source location corresponding to the join point.</p>
        *
        *  <p>If there is no source location available, returns null.</p>
        *
        *  <p>Returns the SourceLocation of the defining class for default constructors.</p>
        */
        SourceLocation getSourceLocation();

        /** <p> Returns a String representing the kind of join point.  This String
        *       is guaranteed to be interned</p>
        */
        String getKind();

        String toString();

        /**
        * Returns an abbreviated string representation of the join point
        */
        String toShortString();

        /**
        * Returns an extended string representation of the join point
        */
        String toLongString();
    }

    public interface EnclosingStaticPart extends StaticPart {}

    /**
     * Returns an object that encapsulates the static parts of this join point.
     */
    StaticPart getStaticPart();


    /**
     * The legal return values from getKind()
     */
    static String METHOD_EXECUTION = "method-execution";
    static String METHOD_CALL = "method-call";
    static String CONSTRUCTOR_EXECUTION = "constructor-execution";
    static String CONSTRUCTOR_CALL = "constructor-call";
    static String FIELD_GET = "field-get";
    static String FIELD_SET = "field-set";
    static String STATICINITIALIZATION = "staticinitialization";
    static String PREINTIALIZATION = "preinitialization";
    static String INITIALIZATION = "initialization";
    static String EXCEPTION_HANDLER = "exception-handler";

    static String ADVICE_EXECUTION = "adviceexecution"; 

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2418.java