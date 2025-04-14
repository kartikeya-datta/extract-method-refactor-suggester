error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7266.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7266.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7266.java
text:
```scala
r@@eturn new TypeLiteral<>(type);

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

import com.google.common.collect.ImmutableList;
import org.elasticsearch.common.inject.internal.MoreTypes;
import org.elasticsearch.common.inject.util.Types;

import java.lang.reflect.*;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.elasticsearch.common.inject.internal.MoreTypes.canonicalize;

/**
 * Represents a generic type {@code T}. Java doesn't yet provide a way to
 * represent generic types, so this class does. Forces clients to create a
 * subclass of this class which enables retrieval the type information even at
 * runtime.
 * <p/>
 * <p>For example, to create a type literal for {@code List<String>}, you can
 * create an empty anonymous inner class:
 * <p/>
 * <p/>
 * {@code TypeLiteral<List<String>> list = new TypeLiteral<List<String>>() {};}
 * <p/>
 * <p>This syntax cannot be used to create type literals that have wildcard
 * parameters, such as {@code Class<?>} or {@code List<? extends CharSequence>}.
 * Such type literals must be constructed programatically, either by {@link
 * Method#getGenericReturnType extracting types from members} or by using the
 * {@link Types} factory class.
 * <p/>
 * <p>Along with modeling generic types, this class can resolve type parameters.
 * For example, to figure out what type {@code keySet()} returns on a {@code
 * Map<Integer, String>}, use this code:<pre>   {@code
 * <p/>
 *   TypeLiteral<Map<Integer, String>> mapType
 *       = new TypeLiteral<Map<Integer, String>>() {};
 *   TypeLiteral<?> keySetType
 *       = mapType.getReturnType(Map.class.getMethod("keySet"));
 *   System.out.println(keySetType); // prints "Set<Integer>"}</pre>
 *
 * @author crazybob@google.com (Bob Lee)
 * @author jessewilson@google.com (Jesse Wilson)
 */
public class TypeLiteral<T> {

    final Class<? super T> rawType;
    final Type type;
    final int hashCode;

    /**
     * Constructs a new type literal. Derives represented class from type
     * parameter.
     * <p/>
     * <p>Clients create an empty anonymous subclass. Doing so embeds the type
     * parameter in the anonymous class's type hierarchy so we can reconstitute it
     * at runtime despite erasure.
     */
    @SuppressWarnings("unchecked")
    protected TypeLiteral() {
        this.type = getSuperclassTypeParameter(getClass());
        this.rawType = (Class<? super T>) MoreTypes.getRawType(type);
        this.hashCode = MoreTypes.hashCode(type);
    }

    /**
     * Unsafe. Constructs a type literal manually.
     */
    @SuppressWarnings("unchecked")
    TypeLiteral(Type type) {
        this.type = canonicalize(checkNotNull(type, "type"));
        this.rawType = (Class<? super T>) MoreTypes.getRawType(this.type);
        this.hashCode = MoreTypes.hashCode(this.type);
    }

    /**
     * Returns the type from super class's type parameter in {@link MoreTypes#canonicalize(Type)
     * canonical form}.
     */
    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    /**
     * Gets type literal from super class's type parameter.
     */
    static TypeLiteral<?> fromSuperclassTypeParameter(Class<?> subclass) {
        return new TypeLiteral<Object>(getSuperclassTypeParameter(subclass));
    }

    /**
     * Returns the raw (non-generic) type for this type.
     *
     * @since 2.0
     */
    public final Class<? super T> getRawType() {
        return rawType;
    }

    /**
     * Gets underlying {@code Type} instance.
     */
    public final Type getType() {
        return type;
    }

    /**
     * Gets the type of this type's provider.
     */
    @SuppressWarnings("unchecked")
    final TypeLiteral<Provider<T>> providerType() {
        // This cast is safe and wouldn't generate a warning if Type had a type
        // parameter.
        return (TypeLiteral<Provider<T>>) get(Types.providerOf(getType()));
    }

    @Override
    public final int hashCode() {
        return this.hashCode;
    }

    @Override
    public final boolean equals(Object o) {
        return o instanceof TypeLiteral<?>
                && MoreTypes.equals(type, ((TypeLiteral) o).type);
    }

    @Override
    public final String toString() {
        return MoreTypes.toString(type);
    }

    /**
     * Gets type literal for the given {@code Type} instance.
     */
    public static TypeLiteral<?> get(Type type) {
        return new TypeLiteral<Object>(type);
    }

    /**
     * Gets type literal for the given {@code Class} instance.
     */
    public static <T> TypeLiteral<T> get(Class<T> type) {
        return new TypeLiteral<T>(type);
    }


    /**
     * Returns an immutable list of the resolved types.
     */
    private List<TypeLiteral<?>> resolveAll(Type[] types) {
        TypeLiteral<?>[] result = new TypeLiteral<?>[types.length];
        for (int t = 0; t < types.length; t++) {
            result[t] = resolve(types[t]);
        }
        return ImmutableList.copyOf(result);
    }

    /**
     * Resolves known type parameters in {@code toResolve} and returns the result.
     */
    TypeLiteral<?> resolve(Type toResolve) {
        return TypeLiteral.get(resolveType(toResolve));
    }

    Type resolveType(Type toResolve) {
        // this implementation is made a little more complicated in an attempt to avoid object-creation
        while (true) {
            if (toResolve instanceof TypeVariable) {
                TypeVariable original = (TypeVariable) toResolve;
                toResolve = MoreTypes.resolveTypeVariable(type, rawType, original);
                if (toResolve == original) {
                    return toResolve;
                }

            } else if (toResolve instanceof GenericArrayType) {
                GenericArrayType original = (GenericArrayType) toResolve;
                Type componentType = original.getGenericComponentType();
                Type newComponentType = resolveType(componentType);
                return componentType == newComponentType
                        ? original
                        : Types.arrayOf(newComponentType);

            } else if (toResolve instanceof ParameterizedType) {
                ParameterizedType original = (ParameterizedType) toResolve;
                Type ownerType = original.getOwnerType();
                Type newOwnerType = resolveType(ownerType);
                boolean changed = newOwnerType != ownerType;

                Type[] args = original.getActualTypeArguments();
                for (int t = 0, length = args.length; t < length; t++) {
                    Type resolvedTypeArgument = resolveType(args[t]);
                    if (resolvedTypeArgument != args[t]) {
                        if (!changed) {
                            args = args.clone();
                            changed = true;
                        }
                        args[t] = resolvedTypeArgument;
                    }
                }

                return changed
                        ? Types.newParameterizedTypeWithOwner(newOwnerType, original.getRawType(), args)
                        : original;

            } else if (toResolve instanceof WildcardType) {
                WildcardType original = (WildcardType) toResolve;
                Type[] originalLowerBound = original.getLowerBounds();
                Type[] originalUpperBound = original.getUpperBounds();

                if (originalLowerBound.length == 1) {
                    Type lowerBound = resolveType(originalLowerBound[0]);
                    if (lowerBound != originalLowerBound[0]) {
                        return Types.supertypeOf(lowerBound);
                    }
                } else if (originalUpperBound.length == 1) {
                    Type upperBound = resolveType(originalUpperBound[0]);
                    if (upperBound != originalUpperBound[0]) {
                        return Types.subtypeOf(upperBound);
                    }
                }
                return original;

            } else {
                return toResolve;
            }
        }
    }

    /**
     * Returns the generic form of {@code supertype}. For example, if this is {@code
     * ArrayList<String>}, this returns {@code Iterable<String>} given the input {@code
     * Iterable.class}.
     *
     * @param supertype a superclass of, or interface implemented by, this.
     * @since 2.0
     */
    public TypeLiteral<?> getSupertype(Class<?> supertype) {
        checkArgument(supertype.isAssignableFrom(rawType),
                "%s is not a supertype of %s", supertype, this.type);
        return resolve(MoreTypes.getGenericSupertype(type, rawType, supertype));
    }

    /**
     * Returns the resolved generic type of {@code field}.
     *
     * @param field a field defined by this or any superclass.
     * @since 2.0
     */
    public TypeLiteral<?> getFieldType(Field field) {
        checkArgument(field.getDeclaringClass().isAssignableFrom(rawType),
                "%s is not defined by a supertype of %s", field, type);
        return resolve(field.getGenericType());
    }

    /**
     * Returns the resolved generic parameter types of {@code methodOrConstructor}.
     *
     * @param methodOrConstructor a method or constructor defined by this or any supertype.
     * @since 2.0
     */
    public List<TypeLiteral<?>> getParameterTypes(Member methodOrConstructor) {
        Type[] genericParameterTypes;

        if (methodOrConstructor instanceof Method) {
            Method method = (Method) methodOrConstructor;
            checkArgument(method.getDeclaringClass().isAssignableFrom(rawType),
                    "%s is not defined by a supertype of %s", method, type);
            genericParameterTypes = method.getGenericParameterTypes();

        } else if (methodOrConstructor instanceof Constructor) {
            Constructor constructor = (Constructor) methodOrConstructor;
            checkArgument(constructor.getDeclaringClass().isAssignableFrom(rawType),
                    "%s does not construct a supertype of %s", constructor, type);
            genericParameterTypes = constructor.getGenericParameterTypes();

        } else {
            throw new IllegalArgumentException("Not a method or a constructor: " + methodOrConstructor);
        }

        return resolveAll(genericParameterTypes);
    }

    /**
     * Returns the resolved generic exception types thrown by {@code constructor}.
     *
     * @param methodOrConstructor a method or constructor defined by this or any supertype.
     * @since 2.0
     */
    public List<TypeLiteral<?>> getExceptionTypes(Member methodOrConstructor) {
        Type[] genericExceptionTypes;

        if (methodOrConstructor instanceof Method) {
            Method method = (Method) methodOrConstructor;
            checkArgument(method.getDeclaringClass().isAssignableFrom(rawType),
                    "%s is not defined by a supertype of %s", method, type);
            genericExceptionTypes = method.getGenericExceptionTypes();

        } else if (methodOrConstructor instanceof Constructor) {
            Constructor<?> constructor = (Constructor<?>) methodOrConstructor;
            checkArgument(constructor.getDeclaringClass().isAssignableFrom(rawType),
                    "%s does not construct a supertype of %s", constructor, type);
            genericExceptionTypes = constructor.getGenericExceptionTypes();

        } else {
            throw new IllegalArgumentException("Not a method or a constructor: " + methodOrConstructor);
        }

        return resolveAll(genericExceptionTypes);
    }

    /**
     * Returns the resolved generic return type of {@code method}.
     *
     * @param method a method defined by this or any supertype.
     * @since 2.0
     */
    public TypeLiteral<?> getReturnType(Method method) {
        checkArgument(method.getDeclaringClass().isAssignableFrom(rawType),
                "%s is not defined by a supertype of %s", method, type);
        return resolve(method.getGenericReturnType());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7266.java