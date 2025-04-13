error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6907.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6907.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6907.java
text:
```scala
t@@hrow new OperationFailedException(new ModelNode().set(parameterName + " may not be null.")); //TODO i18n

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.jboss.as.controller.operations.validation;

import java.util.EnumSet;

import org.jboss.as.controller.OperationFailedException;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * Validates that the given parameter is of the correct type.
 * <p>
 * Note on strict type matching:
 * </p>
 * <p>
 * The constructor takes a parameter {@code strictType}. If {@code strictType} is {@code false}, nodes being validated do not
 * need to precisely match the type(s) passed to the constructor; rather a limited set of value conversions
 * will be attempted, and if the node value can be converted, the node is considered to match the required type.
 * The conversions are:
 * <ul>
 * <li>For BIG_DECIMAL, BIG_INTEGER, DOUBLE, INT, LONG and PROPERTY, the related ModelNode.asXXX() method is invoked; if
 * no exception is thrown the type is considered to match.</li>
 * <li>For BOOLEAN, if the node is of type BOOLEAN it is considered to match. If it is of type STRING with a value
 * ignoring case of "true" or "false" it is considered to match.</li>
 * <li>For OBJECT, if the node is of type OBJECT or PROPERTY it is considered to match. If it is of type LIST and each element
 * in the list is of type PROPERTY it is considered to match.</li>
 * <li>For STRING, if the node is of type STRING, BIG_DECIMAL, BIG_INTEGER, DOUBLE, INT or LONG it is considered to match.</li>
 * </ul>
 * For all other types, an exact match is required.
 * </p>
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class ModelTypeValidator implements ParameterValidator {
    protected final EnumSet<ModelType> validTypes;
    protected final boolean nullable;
    protected final boolean strictType;

    /**
     * Same as {@code ModelTypeValidator(type, false, false, false)}.
     *
     * @param type the valid type. Cannot be {@code null}
     */
    public ModelTypeValidator(final ModelType type) {
        this(false, false, false, type);
    }

    /**
     * Same as {@code ModelTypeValidator(type, nullable, false, false)}.
     *
     * @param type the valid type. Cannot be {@code null}
     * @param nullable whether {@link ModelType#UNDEFINED} is allowed
     */
    public ModelTypeValidator(final ModelType type, final boolean nullable) {
        this(nullable, false, false, type);
    }

    /**
     * Same as {@code ModelTypeValidator(type, nullable, allowExpressions, false)}.
     *
     * @param type the valid type. Cannot be {@code null}
     * @param nullable whether {@link ModelType#UNDEFINED} is allowed
     * @param allowExpressions whether {@link ModelType#EXPRESSION} is allowed
     */
    public ModelTypeValidator(final ModelType type, final boolean nullable, final boolean allowExpressions) {
        this(nullable, allowExpressions, false, type);
    }

    /**
     * Creates a ModelTypeValidator that allows the given type.
     *
     * @param type the valid type. Cannot be {@code null}
     * @param nullable whether {@link ModelType#UNDEFINED} is allowed
     * @param allowExpressions whether {@link ModelType#EXPRESSION} is allowed
     * @param strictType {@code true} if the type of a node must precisely match {@code type}; {@code false} if the value
     *              conversions described in the class javadoc can be performed to check for compatible types
     */
    public ModelTypeValidator(final ModelType type, final boolean nullable, final boolean allowExpressions, final boolean strictType) {
        this(nullable, allowExpressions, strictType, type);
    }

    /**
     * Creates a ModelTypeValidator that allows potentially more than one type.
     *
     * @param nullable whether {@link ModelType#UNDEFINED} is allowed
     * @param allowExpressions whether {@link ModelType#EXPRESSION} is allowed
     * @param strictType {@code true} if the type of a node must precisely match {@code type}; {@code false} if the value
     *              conversions described in the class javadoc can be performed to check for compatible types
     * @param firstValidType a valid type. Cannot be {@code null}
     * @param otherValidTypes additional valid types. May be {@code null}
     */
    public ModelTypeValidator(final boolean nullable, final boolean allowExpressions, final boolean strictType, ModelType firstValidType, ModelType... otherValidTypes) {
        this.validTypes = EnumSet.of(firstValidType, otherValidTypes);
        this.nullable = nullable;
        if (allowExpressions) {
            this.validTypes.add(ModelType.EXPRESSION);
        }
        this.strictType = strictType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateParameter(String parameterName, ModelNode value) throws OperationFailedException {
        if (!value.isDefined()) {
            if (!nullable)
                throw new OperationFailedException(new ModelNode().set(parameterName + " may not be null ")); //TODO i18n
        } else  {
            boolean matched = false;
            if (strictType) {
                matched = validTypes.contains(value.getType());
            } else {
                for (ModelType validType : validTypes) {
                    if (matches(value, validType)) {
                        matched = true;
                        break;
                    }
                }
            }
            if  (!matched)
                throw new OperationFailedException(new ModelNode().set("Wrong type for " + parameterName + ". Expected " + validTypes.toString() + " but was " + value.getType())); //TODO i18n
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateResolvedParameter(String parameterName, ModelNode value) throws OperationFailedException {
        validateParameter(parameterName, value.resolve());
    }

    private boolean matches(ModelNode value, ModelType validType) {
        try {
            switch (validType) {
            case BIG_DECIMAL: {
                value.asBigDecimal();
                return true;
            }
            case BIG_INTEGER: {
                value.asBigInteger();
                return true;
            }
            case DOUBLE: {
                value.asDouble();
                return true;
            }
            case INT: {
                value.asInt();
                return true;
            }
            case LONG: {
                value.asLong();
                return true;
            }
            case PROPERTY: {
                value.asProperty();
                return true;
            }
            case BOOLEAN: {
                // Allow some type conversions, not others.
                switch (value.getType()) {
                case STRING: {
                    String s = value.asString();
                    return "false".equalsIgnoreCase(s) || "true".equalsIgnoreCase(s);
                }
                case BOOLEAN:
                //case INT:
                    return true;
                }
                return false;
            }
            case OBJECT: {
                // We accept OBJECT, PROPERTY or LIST where all elements are PROPERTY
                switch (value.getType()) {
                    case PROPERTY:
                    case OBJECT:
                        return true;
                    case LIST: {
                        for (ModelNode node : value.asList()) {
                            if (node.getType() != ModelType.PROPERTY) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
                return false;
            }
            case STRING: {
                // Allow some type conversions, not others.
                switch (value.getType()) {
                    case BIG_DECIMAL:
                    case BIG_INTEGER:
                    case BOOLEAN:
                    case DOUBLE:
                    case INT:
                    case LONG:
                    case STRING:
                        return true;
                }
                return false;
            }
            case BYTES:
                // we could handle STRING but IMO if people want to allow STRING to byte[] conversion
                // they should use a different validator class
            case LIST:
                // we could handle OBJECT but IMO if people want to allow OBJECT to LIST conversion
                // they should use a different validator class
            case EXPRESSION:
            case TYPE:
            case UNDEFINED:
            default:
                return validType == value.getType();
            }
        }
        catch (RuntimeException e) {
            return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6907.java