error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13440.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13440.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[25,1]

error in qdox parser
file content:
```java
offset: 1209
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13440.java
text:
```scala
abstract class AttributeTransformationDescriptionBuilderImpl<T extends BaseAttributeTransformationDescriptionBuilder<?>> implements BaseAttributeTransformationDescriptionBuilder<T> {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

p@@ackage org.jboss.as.controller.transform.description;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.as.controller.AttributeDefinition;

/**
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
abstract class AttributeTransformationDescriptionBuilderImpl<T extends AttributeTransformationDescriptionBuilder> implements AttributeTransformationDescriptionBuilder<T> {

    private AttributeTransformationDescriptionBuilderRegistry registry;
    private ResourceTransformationDescriptionBuilder builder;

    AttributeTransformationDescriptionBuilderImpl(final ResourceTransformationDescriptionBuilder builder, final AttributeTransformationDescriptionBuilderRegistry registry) {
        this.builder = builder;
        this.registry = registry;
    }

    @Override
    public ResourceTransformationDescriptionBuilder end() {
        return builder;
    }

    @Override
    public T setDiscard(DiscardAttributeChecker discardChecker, AttributeDefinition...discardedAttributes) {
        AttributeDefinition[] useDefs = discardedAttributes;
        for (AttributeDefinition attribute : useDefs) {
            String attrName = getAttributeName(attribute);
            registry.setDiscardedAttribute(discardChecker, attrName);
        }
        return thisBuilder();
    }

    @Override
    public T setDiscard(DiscardAttributeChecker discardChecker, String... discardedAttributes) {
        String[] useDefs = discardedAttributes;
        for (String attrName : useDefs) {
            registry.setDiscardedAttribute(discardChecker, attrName);
        }
        return thisBuilder();
    }

    @Override
    public T addRejectCheck(final RejectAttributeChecker checker, final AttributeDefinition...rejectedAttributes){
        for (AttributeDefinition attribute : rejectedAttributes) {
            String attrName = getAttributeName(attribute);
            registry.addAttributeCheck(attrName, checker);
        }
        return thisBuilder();
    }

    @Override
    public T addRejectCheck(RejectAttributeChecker rejectChecker, String... rejectedAttributes) {
        for (String attribute : rejectedAttributes) {
            registry.addAttributeCheck(attribute, rejectChecker);
        }
        return thisBuilder();
    }

    @Override
    public T addRejectChecks(List<RejectAttributeChecker> rejectCheckers, AttributeDefinition...rejectedAttributes) {
        for (RejectAttributeChecker rejectChecker : rejectCheckers) {
            addRejectCheck(rejectChecker, rejectedAttributes);
        }
        return thisBuilder();
    }

    @Override
    public T addRejectChecks(List<RejectAttributeChecker> rejectCheckers, String... rejectedAttributes) {
        for (RejectAttributeChecker rejectChecker : rejectCheckers) {
            addRejectCheck(rejectChecker, rejectedAttributes);
        }
        return thisBuilder();
    }

    @Override
    public T addRename(AttributeDefinition attributeName, String newName) {
        registry.addRenamedAttribute(getAttributeName(attributeName), newName);
        return thisBuilder();
    }

    @Override
    public T addRename(String attributeName, String newName) {
        registry.addRenamedAttribute(attributeName, newName);
        return thisBuilder();
    }

    public T addRenames(Map<String, String> renames) {
        for (Map.Entry<String, String> rename : renames.entrySet()) {
            registry.addRenamedAttribute(rename.getKey(), rename.getValue());
        }
        return thisBuilder();
    }


    @Override
    public T setValueConverter(AttributeConverter attributeConverter, AttributeDefinition...convertedAttributes) {
        for (AttributeDefinition attribute : convertedAttributes) {
            String attrName = getAttributeName(attribute);
            registry.addAttributeConverter(attrName, attributeConverter);
        }
        return thisBuilder();
    }

    @Override
    public T setValueConverter(AttributeConverter attributeConverter, String... convertedAttributes) {
        for (String attribute : convertedAttributes) {
            registry.addAttributeConverter(attribute, attributeConverter);
        }
        return thisBuilder();
    }

    protected String getAttributeName(AttributeDefinition attr) {
        return attr.getName();
    }

    protected AttributeTransformationDescriptionBuilderRegistry getLocalRegistry() {
        return registry;
    }

    static class AttributeTransformationDescriptionBuilderRegistry {
        private final Set<String> allAttributes = new HashSet<String>();
        private final Map<String, List<RejectAttributeChecker>> attributeRestrictions = new HashMap<String, List<RejectAttributeChecker>>();
        private final Map<String, DiscardAttributeChecker> discardedAttributes = new HashMap<String, DiscardAttributeChecker>();
        private final Map<String, String> renamedAttributes = new HashMap<String, String>();
        private final Map<String, AttributeConverter> convertedAttributes = new HashMap<String, AttributeConverter>();


        void addToAllAttributes(String attributeName) {
            if (!allAttributes.contains(attributeName)) {
                allAttributes.add(attributeName);
            }
        }

        void addAttributeCheck(final String attributeName, final RejectAttributeChecker checker) {
            addToAllAttributes(attributeName);
            List<RejectAttributeChecker> checkers = attributeRestrictions.get(attributeName);
            if(checkers == null) {
                checkers = new ArrayList<RejectAttributeChecker>();
                attributeRestrictions.put(attributeName, checkers);
            }
            checkers.add(checker);
        }

        void setDiscardedAttribute(DiscardAttributeChecker discardChecker, String attributeName) {
            assert discardChecker != null : "Null discard checker";
            assert !discardedAttributes.containsKey(attributeName) : "Discard already set";
            addToAllAttributes(attributeName);
            discardedAttributes.put(attributeName, discardChecker);
        }

        void addRenamedAttribute(String attributeName, String newName) {
            assert !renamedAttributes.containsKey(attributeName) : "Rename already set";
            addToAllAttributes(attributeName);
            renamedAttributes.put(attributeName, newName);
        }

        void addAttributeConverter(String attributeName, AttributeConverter attributeConverter) {
            addToAllAttributes(attributeName);
            convertedAttributes.put(attributeName, attributeConverter);
        }

        Map<String, AttributeTransformationDescription> buildAttributes(){
            Map<String, AttributeTransformationDescription> attributes = new HashMap<String, AttributeTransformationDescription>();
            for (String name : allAttributes) {
                List<RejectAttributeChecker> checkers = attributeRestrictions.get(name);
                String newName = renamedAttributes.get(name);
                DiscardAttributeChecker discardChecker = discardedAttributes.get(name);
                attributes.put(name, new AttributeTransformationDescription(name, checkers, newName, discardChecker, convertedAttributes.get(name)));
            }
            return attributes;
        }
    }

    protected static AttributeTransformationDescriptionBuilderRegistry mergeRegistries(AttributeTransformationDescriptionBuilderRegistry one, AttributeTransformationDescriptionBuilderRegistry two) {
        final AttributeTransformationDescriptionBuilderRegistry result = new AttributeTransformationDescriptionBuilderRegistry();

        result.allAttributes.addAll(one.allAttributes);
        result.allAttributes.addAll(two.allAttributes);
        result.attributeRestrictions.putAll(one.attributeRestrictions);
        result.attributeRestrictions.putAll(two.attributeRestrictions);
        result.discardedAttributes.putAll(one.discardedAttributes);
        result.discardedAttributes.putAll(two.discardedAttributes);
        result.renamedAttributes.putAll(one.renamedAttributes);
        result.renamedAttributes.putAll(two.renamedAttributes);
        result.convertedAttributes.putAll(one.convertedAttributes);
        result.convertedAttributes.putAll(two.convertedAttributes);

        return result;
    }

    /**
     * @return this builder
     */
    protected abstract T thisBuilder();

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13440.java