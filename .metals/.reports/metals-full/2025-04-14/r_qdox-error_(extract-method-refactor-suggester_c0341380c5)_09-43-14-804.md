error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9598.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9598.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9598.java
text:
```scala
s@@uper();

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang;

import java.io.Serializable;

/**
 * <p>Operations on <code>Object</code>.</p>
 * 
 * <p>This class tries to handle <code>null</code> input gracefully.
 * An exception will generally not be thrown for a <code>null</code> input.
 * Each method documents its behaviour in more detail.</p>
 *
 * @author <a href="mailto:nissim@nksystems.com">Nissim Karpenstein</a>
 * @author <a href="mailto:janekdb@yahoo.co.uk">Janek Bogucki</a>
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @author Stephen Colebourne
 * @author Gary Gregory
 * @author Mario Winterer
 * @since 1.0
 * @version $Id$
 */
public class ObjectUtils {
    
    /**
     * <p>Singleton used as a <code>null</code> placeholder where
     * <code>null</code> has another meaning.</p>
     *
     * <p>For example, in a <code>HashMap</code> the
     * {@link java.util.HashMap#get(java.lang.Object)} method returns
     * <code>null</code> if the <code>Map</code> contains
     * <code>null</code> or if there is no matching key. The
     * <code>Null</code> placeholder can be used to distinguish between
     * these two cases.</p>
     *
     * <p>Another example is <code>Hashtable</code>, where <code>null</code>
     * cannot be stored.</p>
     *
     * <p>This instance is Serializable.</p>
     */
    public static final Null NULL = new Null();
    
    /**
     * <p><code>ObjectUtils</code> instances should NOT be constructed in
     * standard programming. Instead, the class should be used as
     * <code>ObjectUtils.defaultIfNull("a","b");</code>.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     */
    public ObjectUtils() {
      ; // empty constructor
    }

    // Defaulting
    //-----------------------------------------------------------------------
    /**
     * <p>Returns a default value if the object passed is
     * <code>null</code>.</p>
     * 
     * <pre>
     * ObjectUtils.defaultIfNull(null, null)      = null
     * ObjectUtils.defaultIfNull(null, "")        = ""
     * ObjectUtils.defaultIfNull(null, "zz")      = "zz"
     * ObjectUtils.defaultIfNull("abc", *)        = "abc"
     * ObjectUtils.defaultIfNull(Boolean.TRUE, *) = Boolean.TRUE
     * </pre>
     *
     * @param object  the <code>Object</code> to test, may be <code>null</code>
     * @param defaultValue  the default value to return, may be <code>null</code>
     * @return <code>object</code> if it is not <code>null</code>, defaultValue otherwise
     */
    public static Object defaultIfNull(Object object, Object defaultValue) {
        return object != null ? object : defaultValue;
    }

    /**
     * <p>Compares two objects for equality, where either one or both
     * objects may be <code>null</code>.</p>
     *
     * <pre>
     * ObjectUtils.equals(null, null)                  = true
     * ObjectUtils.equals(null, "")                    = false
     * ObjectUtils.equals("", null)                    = false
     * ObjectUtils.equals("", "")                      = true
     * ObjectUtils.equals(Boolean.TRUE, null)          = false
     * ObjectUtils.equals(Boolean.TRUE, "true")        = false
     * ObjectUtils.equals(Boolean.TRUE, Boolean.TRUE)  = true
     * ObjectUtils.equals(Boolean.TRUE, Boolean.FALSE) = false
     * </pre>
     *
     * @param object1  the first object, may be <code>null</code>
     * @param object2  the second object, may be <code>null</code>
     * @return <code>true</code> if the values of both objects are the same
     */
    public static boolean equals(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }
        if ((object1 == null) || (object2 == null)) {
            return false;
        }
        return object1.equals(object2);
    }

    /**
     * <p>Gets the hash code of an object returning zero when the
     * object is <code>null</code>.</p>
     *
     * <pre>
     * ObjectUtils.hashCode(null)   = 0
     * ObjectUtils.hashCode(obj)    = obj.hashCode()
     * </pre>
     *
     * @param obj  the object to obtain the hash code of, may be <code>null</code>
     * @return the hash code of the object, or zero if null
     * @since 2.1
     */
    public static int hashCode(Object obj) {
        return (obj == null) ? 0 : obj.hashCode();
    }

    // Identity ToString
    //-----------------------------------------------------------------------
    /**
     * <p>Gets the toString that would be produced by <code>Object</code>
     * if a class did not override toString itself. <code>null</code>
     * will return <code>null</code>.</p>
     *
     * <pre>
     * ObjectUtils.identityToString(null)         = null
     * ObjectUtils.identityToString("")           = "java.lang.String@1e23"
     * ObjectUtils.identityToString(Boolean.TRUE) = "java.lang.Boolean@7fa"
     * </pre>
     *
     * @param object  the object to create a toString for, may be
     *  <code>null</code>
     * @return the default toString text, or <code>null</code> if
     *  <code>null</code> passed in
     */
    public static String identityToString(Object object) {
        if (object == null) {
            return null;
        }
        return appendIdentityToString(null, object).toString();
    }

    /**
     * <p>Appends the toString that would be produced by <code>Object</code>
     * if a class did not override toString itself. <code>null</code>
     * will return <code>null</code>.</p>
     *
     * <pre>
     * ObjectUtils.appendIdentityToString(*, null)            = null
     * ObjectUtils.appendIdentityToString(null, "")           = "java.lang.String@1e23"
     * ObjectUtils.appendIdentityToString(null, Boolean.TRUE) = "java.lang.Boolean@7fa"
     * ObjectUtils.appendIdentityToString(buf, Boolean.TRUE)  = buf.append("java.lang.Boolean@7fa")
     * </pre>
     *
     * @param buffer  the buffer to append to, may be <code>null</code>
     * @param object  the object to create a toString for, may be <code>null</code>
     * @return the default toString text, or <code>null</code> if
     *  <code>null</code> passed in
     * @since 2.0
     */
    public static StringBuffer appendIdentityToString(StringBuffer buffer, Object object) {
        if (object == null) {
            return null;
        }
        if (buffer == null) {
            buffer = new StringBuffer();
        }
        return buffer
            .append(object.getClass().getName())
            .append('@')
            .append(Integer.toHexString(System.identityHashCode(object)));
    }

    // ToString
    //-----------------------------------------------------------------------
    /**
     * <p>Gets the <code>toString</code> of an <code>Object</code> returning
     * an empty string ("") if <code>null</code> input.</p>
     * 
     * <pre>
     * ObjectUtils.toString(null)         = ""
     * ObjectUtils.toString("")           = ""
     * ObjectUtils.toString("bat")        = "bat"
     * ObjectUtils.toString(Boolean.TRUE) = "true"
     * </pre>
     * 
     * @see StringUtils#defaultString(String)
     * @see String#valueOf(Object)
     * @param obj  the Object to <code>toString</code>, may be null
     * @return the passed in Object's toString, or nullStr if <code>null</code> input
     * @since 2.0
     */
    public static String toString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    /**
     * <p>Gets the <code>toString</code> of an <code>Object</code> returning
     * a specified text if <code>null</code> input.</p>
     * 
     * <pre>
     * ObjectUtils.toString(null, null)           = null
     * ObjectUtils.toString(null, "null")         = "null"
     * ObjectUtils.toString("", "null")           = ""
     * ObjectUtils.toString("bat", "null")        = "bat"
     * ObjectUtils.toString(Boolean.TRUE, "null") = "true"
     * </pre>
     * 
     * @see StringUtils#defaultString(String,String)
     * @see String#valueOf(Object)
     * @param obj  the Object to <code>toString</code>, may be null
     * @param nullStr  the String to return if <code>null</code> input, may be null
     * @return the passed in Object's toString, or nullStr if <code>null</code> input
     * @since 2.0
     */
    public static String toString(Object obj, String nullStr) {
        return obj == null ? nullStr : obj.toString();
    }

    // Null
    //-----------------------------------------------------------------------
    /**
     * <p>Class used as a null placeholder where <code>null</code>
     * has another meaning.</p>
     *
     * <p>For example, in a <code>HashMap</code> the
     * {@link java.util.HashMap#get(java.lang.Object)} method returns
     * <code>null</code> if the <code>Map</code> contains
     * <code>null</code> or if there is no matching key. The
     * <code>Null</code> placeholder can be used to distinguish between
     * these two cases.</p>
     *
     * <p>Another example is <code>Hashtable</code>, where <code>null</code>
     * cannot be stored.</p>
     */
    public static class Null implements Serializable {
        // declare serialization compatibility with Commons Lang 1.0
        private static final long serialVersionUID = 7092611880189329093L;
        
        /**
         * Restricted constructor - singleton.
         */
        Null() {
          ; // empty constructor
        }
        
        /**
         * <p>Ensure singleton.</p>
         * 
         * @return the singleton value
         */
        private Object readResolve() {
            return ObjectUtils.NULL;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9598.java