error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3403.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3403.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3403.java
text:
```scala
r@@eturn annotationType.getAnnotation(BindingAnnotation.class) != null;

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

package org.elasticsearch.common.inject;

import org.elasticsearch.common.inject.internal.Annotations;
import org.elasticsearch.common.inject.internal.MoreTypes;
import org.elasticsearch.common.inject.internal.ToStringBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Binding key consisting of an injection type and an optional annotation.
 * Matches the type and annotation at a point of injection.
 * <p/>
 * <p>For example, {@code Key.get(Service.class, Transactional.class)} will
 * match:
 * <p/>
 * <pre>
 *   {@literal @}Inject
 *   public void setService({@literal @}Transactional Service service) {
 *     ...
 *   }
 * </pre>
 * <p/>
 * <p>{@code Key} supports generic types via subclassing just like {@link
 * TypeLiteral}.
 * <p/>
 * <p>Keys do not differentiate between primitive types (int, char, etc.) and
 * their correpsonding wrapper types (Integer, Character, etc.). Primitive
 * types will be replaced with their wrapper types when keys are created.
 *
 * @author crazybob@google.com (Bob Lee)
 */
public class Key<T> {

    private final AnnotationStrategy annotationStrategy;

    private final TypeLiteral<T> typeLiteral;
    private final int hashCode;

    /**
     * Constructs a new key. Derives the type from this class's type parameter.
     * <p/>
     * <p>Clients create an empty anonymous subclass. Doing so embeds the type
     * parameter in the anonymous class's type hierarchy so we can reconstitute it
     * at runtime despite erasure.
     * <p/>
     * <p>Example usage for a binding of type {@code Foo} annotated with
     * {@code @Bar}:
     * <p/>
     * <p>{@code new Key<Foo>(Bar.class) {}}.
     */
    @SuppressWarnings("unchecked")
    protected Key(Class<? extends Annotation> annotationType) {
        this.annotationStrategy = strategyFor(annotationType);
        this.typeLiteral = (TypeLiteral<T>) TypeLiteral.fromSuperclassTypeParameter(getClass());
        this.hashCode = computeHashCode();
    }

    /**
     * Constructs a new key. Derives the type from this class's type parameter.
     * <p/>
     * <p>Clients create an empty anonymous subclass. Doing so embeds the type
     * parameter in the anonymous class's type hierarchy so we can reconstitute it
     * at runtime despite erasure.
     * <p/>
     * <p>Example usage for a binding of type {@code Foo} annotated with
     * {@code @Bar}:
     * <p/>
     * <p>{@code new Key<Foo>(new Bar()) {}}.
     */
    @SuppressWarnings("unchecked")
    protected Key(Annotation annotation) {
        // no usages, not test-covered
        this.annotationStrategy = strategyFor(annotation);
        this.typeLiteral = (TypeLiteral<T>) TypeLiteral.fromSuperclassTypeParameter(getClass());
        this.hashCode = computeHashCode();
    }

    /**
     * Constructs a new key. Derives the type from this class's type parameter.
     * <p/>
     * <p>Clients create an empty anonymous subclass. Doing so embeds the type
     * parameter in the anonymous class's type hierarchy so we can reconstitute it
     * at runtime despite erasure.
     * <p/>
     * <p>Example usage for a binding of type {@code Foo}:
     * <p/>
     * <p>{@code new Key<Foo>() {}}.
     */
    @SuppressWarnings("unchecked")
    protected Key() {
        this.annotationStrategy = NullAnnotationStrategy.INSTANCE;
        this.typeLiteral = (TypeLiteral<T>) TypeLiteral.fromSuperclassTypeParameter(getClass());
        this.hashCode = computeHashCode();
    }

    /**
     * Unsafe. Constructs a key from a manually specified type.
     */
    @SuppressWarnings("unchecked")
    private Key(Type type, AnnotationStrategy annotationStrategy) {
        this.annotationStrategy = annotationStrategy;
        this.typeLiteral = MoreTypes.makeKeySafe((TypeLiteral<T>) TypeLiteral.get(type));
        this.hashCode = computeHashCode();
    }

    /**
     * Constructs a key from a manually specified type.
     */
    private Key(TypeLiteral<T> typeLiteral, AnnotationStrategy annotationStrategy) {
        this.annotationStrategy = annotationStrategy;
        this.typeLiteral = MoreTypes.makeKeySafe(typeLiteral);
        this.hashCode = computeHashCode();
    }

    private int computeHashCode() {
        return typeLiteral.hashCode() * 31 + annotationStrategy.hashCode();
    }

    /**
     * Gets the key type.
     */
    public final TypeLiteral<T> getTypeLiteral() {
        return typeLiteral;
    }

    /**
     * Gets the annotation type.
     */
    public final Class<? extends Annotation> getAnnotationType() {
        return annotationStrategy.getAnnotationType();
    }

    /**
     * Gets the annotation.
     */
    public final Annotation getAnnotation() {
        return annotationStrategy.getAnnotation();
    }

    boolean hasAnnotationType() {
        return annotationStrategy.getAnnotationType() != null;
    }

    String getAnnotationName() {
        Annotation annotation = annotationStrategy.getAnnotation();
        if (annotation != null) {
            return annotation.toString();
        }

        // not test-covered
        return annotationStrategy.getAnnotationType().toString();
    }

    Class<? super T> getRawType() {
        return typeLiteral.getRawType();
    }

    /**
     * Gets the key of this key's provider.
     */
    Key<Provider<T>> providerKey() {
        return ofType(typeLiteral.providerType());
    }

    @Override
    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Key<?>)) {
            return false;
        }
        Key<?> other = (Key<?>) o;
        return annotationStrategy.equals(other.annotationStrategy)
                && typeLiteral.equals(other.typeLiteral);
    }

    @Override
    public final int hashCode() {
        return this.hashCode;
    }

    @Override
    public final String toString() {
        return new ToStringBuilder(Key.class)
                .add("type", typeLiteral)
                .add("annotation", annotationStrategy)
                .toString();
    }

    /**
     * Gets a key for an injection type and an annotation strategy.
     */
    static <T> Key<T> get(Class<T> type,
                          AnnotationStrategy annotationStrategy) {
        return new Key<T>(type, annotationStrategy);
    }

    /**
     * Gets a key for an injection type.
     */
    public static <T> Key<T> get(Class<T> type) {
        return new Key<T>(type, NullAnnotationStrategy.INSTANCE);
    }

    /**
     * Gets a key for an injection type and an annotation type.
     */
    public static <T> Key<T> get(Class<T> type,
                                 Class<? extends Annotation> annotationType) {
        return new Key<T>(type, strategyFor(annotationType));
    }

    /**
     * Gets a key for an injection type and an annotation.
     */
    public static <T> Key<T> get(Class<T> type, Annotation annotation) {
        return new Key<T>(type, strategyFor(annotation));
    }

    /**
     * Gets a key for an injection type.
     */
    public static Key<?> get(Type type) {
        return new Key<Object>(type, NullAnnotationStrategy.INSTANCE);
    }

    /**
     * Gets a key for an injection type and an annotation type.
     */
    public static Key<?> get(Type type,
                             Class<? extends Annotation> annotationType) {
        return new Key<Object>(type, strategyFor(annotationType));
    }

    /**
     * Gets a key for an injection type and an annotation.
     */
    public static Key<?> get(Type type, Annotation annotation) {
        return new Key<Object>(type, strategyFor(annotation));
    }

    /**
     * Gets a key for an injection type.
     */
    public static <T> Key<T> get(TypeLiteral<T> typeLiteral) {
        return new Key<T>(typeLiteral, NullAnnotationStrategy.INSTANCE);
    }

    /**
     * Gets a key for an injection type and an annotation type.
     */
    public static <T> Key<T> get(TypeLiteral<T> typeLiteral,
                                 Class<? extends Annotation> annotationType) {
        return new Key<T>(typeLiteral, strategyFor(annotationType));
    }

    /**
     * Gets a key for an injection type and an annotation.
     */
    public static <T> Key<T> get(TypeLiteral<T> typeLiteral,
                                 Annotation annotation) {
        return new Key<T>(typeLiteral, strategyFor(annotation));
    }

    /**
     * Returns a new key of the specified type with the same annotation as this
     * key.
     */
    <T> Key<T> ofType(Class<T> type) {
        return new Key<T>(type, annotationStrategy);
    }

    /**
     * Returns a new key of the specified type with the same annotation as this
     * key.
     */
    Key<?> ofType(Type type) {
        return new Key<Object>(type, annotationStrategy);
    }

    /**
     * Returns a new key of the specified type with the same annotation as this
     * key.
     */
    <T> Key<T> ofType(TypeLiteral<T> type) {
        return new Key<T>(type, annotationStrategy);
    }

    /**
     * Returns true if this key has annotation attributes.
     */
    boolean hasAttributes() {
        return annotationStrategy.hasAttributes();
    }

    /**
     * Returns this key without annotation attributes, i.e. with only the
     * annotation type.
     */
    Key<T> withoutAttributes() {
        return new Key<T>(typeLiteral, annotationStrategy.withoutAttributes());
    }

    interface AnnotationStrategy {
        Annotation getAnnotation();

        Class<? extends Annotation> getAnnotationType();

        boolean hasAttributes();

        AnnotationStrategy withoutAttributes();
    }

    /**
     * Returns {@code true} if the given annotation type has no attributes.
     */
    static boolean isMarker(Class<? extends Annotation> annotationType) {
        return annotationType.getDeclaredMethods().length == 0;
    }

    /**
     * Gets the strategy for an annotation.
     */
    static AnnotationStrategy strategyFor(Annotation annotation) {
        checkNotNull(annotation, "annotation");
        Class<? extends Annotation> annotationType = annotation.annotationType();
        ensureRetainedAtRuntime(annotationType);
        ensureIsBindingAnnotation(annotationType);

        if (annotationType.getDeclaredMethods().length == 0) {
            return new AnnotationTypeStrategy(annotationType, annotation);
        }

        return new AnnotationInstanceStrategy(annotation);
    }

    /**
     * Gets the strategy for an annotation type.
     */
    static AnnotationStrategy strategyFor(Class<? extends Annotation> annotationType) {
        checkNotNull(annotationType, "annotation type");
        ensureRetainedAtRuntime(annotationType);
        ensureIsBindingAnnotation(annotationType);
        return new AnnotationTypeStrategy(annotationType, null);
    }

    private static void ensureRetainedAtRuntime(
            Class<? extends Annotation> annotationType) {
        checkArgument(Annotations.isRetainedAtRuntime(annotationType),
                "%s is not retained at runtime. Please annotate it with @Retention(RUNTIME).",
                annotationType.getName());
    }

    private static void ensureIsBindingAnnotation(
            Class<? extends Annotation> annotationType) {
        checkArgument(isBindingAnnotation(annotationType),
                "%s is not a binding annotation. Please annotate it with @BindingAnnotation.",
                annotationType.getName());
    }

    static enum NullAnnotationStrategy implements AnnotationStrategy {
        INSTANCE;

        public boolean hasAttributes() {
            return false;
        }

        public AnnotationStrategy withoutAttributes() {
            throw new UnsupportedOperationException("Key already has no attributes.");
        }

        public Annotation getAnnotation() {
            return null;
        }

        public Class<? extends Annotation> getAnnotationType() {
            return null;
        }

        @Override
        public String toString() {
            return "[none]";
        }
    }

    // this class not test-covered
    static class AnnotationInstanceStrategy implements AnnotationStrategy {

        final Annotation annotation;

        AnnotationInstanceStrategy(Annotation annotation) {
            this.annotation = checkNotNull(annotation, "annotation");
        }

        public boolean hasAttributes() {
            return true;
        }

        public AnnotationStrategy withoutAttributes() {
            return new AnnotationTypeStrategy(getAnnotationType(), annotation);
        }

        public Annotation getAnnotation() {
            return annotation;
        }

        public Class<? extends Annotation> getAnnotationType() {
            return annotation.annotationType();
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof AnnotationInstanceStrategy)) {
                return false;
            }

            AnnotationInstanceStrategy other = (AnnotationInstanceStrategy) o;
            return annotation.equals(other.annotation);
        }

        @Override
        public int hashCode() {
            return annotation.hashCode();
        }

        @Override
        public String toString() {
            return annotation.toString();
        }
    }

    static class AnnotationTypeStrategy implements AnnotationStrategy {

        final Class<? extends Annotation> annotationType;

        // Keep the instance around if we have it so the client can request it.
        final Annotation annotation;

        AnnotationTypeStrategy(Class<? extends Annotation> annotationType,
                               Annotation annotation) {
            this.annotationType = checkNotNull(annotationType, "annotation type");
            this.annotation = annotation;
        }

        public boolean hasAttributes() {
            return false;
        }

        public AnnotationStrategy withoutAttributes() {
            throw new UnsupportedOperationException("Key already has no attributes.");
        }

        public Annotation getAnnotation() {
            return annotation;
        }

        public Class<? extends Annotation> getAnnotationType() {
            return annotationType;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof AnnotationTypeStrategy)) {
                return false;
            }

            AnnotationTypeStrategy other = (AnnotationTypeStrategy) o;
            return annotationType.equals(other.annotationType);
        }

        @Override
        public int hashCode() {
            return annotationType.hashCode();
        }

        @Override
        public String toString() {
            return "@" + annotationType.getName();
        }
    }

    static boolean isBindingAnnotation(Annotation annotation) {
        return isBindingAnnotation(annotation.annotationType());
    }

    static boolean isBindingAnnotation(
            Class<? extends Annotation> annotationType) {
        return annotationType.isAnnotationPresent(BindingAnnotation.class);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3403.java