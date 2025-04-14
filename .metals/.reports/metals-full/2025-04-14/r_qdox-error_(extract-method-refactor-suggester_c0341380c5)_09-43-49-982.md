error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7273.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7273.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7273.java
text:
```scala
r@@eturn new Not<>(p);

/**
 * Copyright (C) 2006 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elasticsearch.common.inject.matcher;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matcher implementations. Supports matching classes and methods.
 *
 * @author crazybob@google.com (Bob Lee)
 */
public class Matchers {
    private Matchers() {
    }

    /**
     * Returns a matcher which matches any input.
     */
    public static Matcher<Object> any() {
        return ANY;
    }

    private static final Matcher<Object> ANY = new Any();

    private static class Any extends AbstractMatcher<Object> implements Serializable {
        public boolean matches(Object o) {
            return true;
        }

        @Override
        public String toString() {
            return "any()";
        }

        public Object readResolve() {
            return any();
        }

        private static final long serialVersionUID = 0;
    }

    /**
     * Inverts the given matcher.
     */
    public static <T> Matcher<T> not(final Matcher<? super T> p) {
        return new Not<T>(p);
    }

    private static class Not<T> extends AbstractMatcher<T> implements Serializable {
        final Matcher<? super T> delegate;

        private Not(Matcher<? super T> delegate) {
            this.delegate = checkNotNull(delegate, "delegate");
        }

        public boolean matches(T t) {
            return !delegate.matches(t);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof Not
                    && ((Not) other).delegate.equals(delegate);
        }

        @Override
        public int hashCode() {
            return -delegate.hashCode();
        }

        @Override
        public String toString() {
            return "not(" + delegate + ")";
        }

        private static final long serialVersionUID = 0;
    }

    private static void checkForRuntimeRetention(
            Class<? extends Annotation> annotationType) {
        Retention retention = annotationType.getAnnotation(Retention.class);
        checkArgument(retention != null && retention.value() == RetentionPolicy.RUNTIME,
                "Annotation " + annotationType.getSimpleName() + " is missing RUNTIME retention");
    }

    /**
     * Returns a matcher which matches elements (methods, classes, etc.)
     * with a given annotation.
     */
    public static Matcher<AnnotatedElement> annotatedWith(
            final Class<? extends Annotation> annotationType) {
        return new AnnotatedWithType(annotationType);
    }

    private static class AnnotatedWithType extends AbstractMatcher<AnnotatedElement>
            implements Serializable {
        private final Class<? extends Annotation> annotationType;

        public AnnotatedWithType(Class<? extends Annotation> annotationType) {
            this.annotationType = checkNotNull(annotationType, "annotation type");
            checkForRuntimeRetention(annotationType);
        }

        public boolean matches(AnnotatedElement element) {
            return element.getAnnotation(annotationType) != null;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof AnnotatedWithType
                    && ((AnnotatedWithType) other).annotationType.equals(annotationType);
        }

        @Override
        public int hashCode() {
            return 37 * annotationType.hashCode();
        }

        @Override
        public String toString() {
            return "annotatedWith(" + annotationType.getSimpleName() + ".class)";
        }

        private static final long serialVersionUID = 0;
    }

    /**
     * Returns a matcher which matches elements (methods, classes, etc.)
     * with a given annotation.
     */
    public static Matcher<AnnotatedElement> annotatedWith(
            final Annotation annotation) {
        return new AnnotatedWith(annotation);
    }

    private static class AnnotatedWith extends AbstractMatcher<AnnotatedElement>
            implements Serializable {
        private final Annotation annotation;

        public AnnotatedWith(Annotation annotation) {
            this.annotation = checkNotNull(annotation, "annotation");
            checkForRuntimeRetention(annotation.annotationType());
        }

        public boolean matches(AnnotatedElement element) {
            Annotation fromElement = element.getAnnotation(annotation.annotationType());
            return fromElement != null && annotation.equals(fromElement);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof AnnotatedWith
                    && ((AnnotatedWith) other).annotation.equals(annotation);
        }

        @Override
        public int hashCode() {
            return 37 * annotation.hashCode();
        }

        @Override
        public String toString() {
            return "annotatedWith(" + annotation + ")";
        }

        private static final long serialVersionUID = 0;
    }

    /**
     * Returns a matcher which matches subclasses of the given type (as well as
     * the given type).
     */
    public static Matcher<Class> subclassesOf(final Class<?> superclass) {
        return new SubclassesOf(superclass);
    }

    private static class SubclassesOf extends AbstractMatcher<Class>
            implements Serializable {
        private final Class<?> superclass;

        public SubclassesOf(Class<?> superclass) {
            this.superclass = checkNotNull(superclass, "superclass");
        }

        public boolean matches(Class subclass) {
            return superclass.isAssignableFrom(subclass);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof SubclassesOf
                    && ((SubclassesOf) other).superclass.equals(superclass);
        }

        @Override
        public int hashCode() {
            return 37 * superclass.hashCode();
        }

        @Override
        public String toString() {
            return "subclassesOf(" + superclass.getSimpleName() + ".class)";
        }

        private static final long serialVersionUID = 0;
    }

    /**
     * Returns a matcher which matches objects equal to the given object.
     */
    public static Matcher<Object> only(Object value) {
        return new Only(value);
    }

    private static class Only extends AbstractMatcher<Object>
            implements Serializable {
        private final Object value;

        public Only(Object value) {
            this.value = checkNotNull(value, "value");
        }

        public boolean matches(Object other) {
            return value.equals(other);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof Only
                    && ((Only) other).value.equals(value);
        }

        @Override
        public int hashCode() {
            return 37 * value.hashCode();
        }

        @Override
        public String toString() {
            return "only(" + value + ")";
        }

        private static final long serialVersionUID = 0;
    }

    /**
     * Returns a matcher which matches only the given object.
     */
    public static Matcher<Object> identicalTo(final Object value) {
        return new IdenticalTo(value);
    }

    private static class IdenticalTo extends AbstractMatcher<Object>
            implements Serializable {
        private final Object value;

        public IdenticalTo(Object value) {
            this.value = checkNotNull(value, "value");
        }

        public boolean matches(Object other) {
            return value == other;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof IdenticalTo
                    && ((IdenticalTo) other).value == value;
        }

        @Override
        public int hashCode() {
            return 37 * System.identityHashCode(value);
        }

        @Override
        public String toString() {
            return "identicalTo(" + value + ")";
        }

        private static final long serialVersionUID = 0;
    }

    /**
     * Returns a matcher which matches classes in the given package. Packages are specific to their
     * classloader, so classes with the same package name may not have the same package at runtime.
     */
    public static Matcher<Class> inPackage(final Package targetPackage) {
        return new InPackage(targetPackage);
    }

    private static class InPackage extends AbstractMatcher<Class> implements Serializable {
        private final transient Package targetPackage;
        private final String packageName;

        public InPackage(Package targetPackage) {
            this.targetPackage = checkNotNull(targetPackage, "package");
            this.packageName = targetPackage.getName();
        }

        public boolean matches(Class c) {
            return c.getPackage().equals(targetPackage);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof InPackage
                    && ((InPackage) other).targetPackage.equals(targetPackage);
        }

        @Override
        public int hashCode() {
            return 37 * targetPackage.hashCode();
        }

        @Override
        public String toString() {
            return "inPackage(" + targetPackage.getName() + ")";
        }

        public Object readResolve() {
            return inPackage(Package.getPackage(packageName));
        }

        private static final long serialVersionUID = 0;
    }

    /**
     * Returns a matcher which matches classes in the given package and its subpackages. Unlike
     * {@link #inPackage(Package) inPackage()}, this matches classes from any classloader.
     *
     * @since 2.0
     */
    public static Matcher<Class> inSubpackage(final String targetPackageName) {
        return new InSubpackage(targetPackageName);
    }

    private static class InSubpackage extends AbstractMatcher<Class> implements Serializable {
        private final String targetPackageName;

        public InSubpackage(String targetPackageName) {
            this.targetPackageName = targetPackageName;
        }

        public boolean matches(Class c) {
            String classPackageName = c.getPackage().getName();
            return classPackageName.equals(targetPackageName)
 classPackageName.startsWith(targetPackageName + ".");
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof InSubpackage
                    && ((InSubpackage) other).targetPackageName.equals(targetPackageName);
        }

        @Override
        public int hashCode() {
            return 37 * targetPackageName.hashCode();
        }

        @Override
        public String toString() {
            return "inSubpackage(" + targetPackageName + ")";
        }

        private static final long serialVersionUID = 0;
    }

    /**
     * Returns a matcher which matches methods with matching return types.
     */
    public static Matcher<Method> returns(
            final Matcher<? super Class<?>> returnType) {
        return new Returns(returnType);
    }

    private static class Returns extends AbstractMatcher<Method> implements Serializable {
        private final Matcher<? super Class<?>> returnType;

        public Returns(Matcher<? super Class<?>> returnType) {
            this.returnType = checkNotNull(returnType, "return type matcher");
        }

        public boolean matches(Method m) {
            return returnType.matches(m.getReturnType());
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof Returns
                    && ((Returns) other).returnType.equals(returnType);
        }

        @Override
        public int hashCode() {
            return 37 * returnType.hashCode();
        }

        @Override
        public String toString() {
            return "returns(" + returnType + ")";
        }

        private static final long serialVersionUID = 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7273.java