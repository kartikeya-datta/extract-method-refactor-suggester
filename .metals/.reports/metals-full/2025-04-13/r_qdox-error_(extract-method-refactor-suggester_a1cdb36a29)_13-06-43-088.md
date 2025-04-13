error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1428.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1428.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1428.java
text:
```scala
t@@hrow PatchMessages.MESSAGES.patchTypesDontMatch();

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

package org.jboss.as.patching.metadata;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import org.jboss.as.patching.PatchMessages;
import org.jboss.as.patching.PatchingException;
import org.jboss.as.patching.metadata.Patch.PatchType;
import org.jboss.as.patching.metadata.impl.IdentityImpl;
import org.jboss.as.patching.metadata.impl.PatchElementProviderImpl;

/**
 * @author Emanuel Muckenhuber
 */
public class PatchBuilder extends ModificationBuilderTarget<PatchBuilder> implements Builder, PatchMetadataResolver {

    protected String patchId;
    private String description;
    private Identity identity;
    private PatchType patchType;
    private final List<ContentModification> modifications = new ArrayList<ContentModification>();
    private final List<PatchElementHolder> elements = new ArrayList<PatchElementHolder>();

    public static PatchBuilder create() {
        return new PatchBuilder();
    }

    protected PatchBuilder() {
    }

    public PatchBuilder setPatchId(String patchId) {
        if (!Patch.PATCH_NAME_PATTERN.matcher(patchId).matches()) {
            throw PatchMessages.MESSAGES.illegalPatchName(patchId);
        }
        this.patchId = patchId;
        return this;
    }

    public PatchBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public PatchIdentityBuilder upgradeIdentity(final String name, final String version, final String resultingVersion) {
        final PatchIdentityBuilder builder = new PatchIdentityBuilder(name, version, PatchType.CUMULATIVE, this);
        final IdentityImpl identity = builder.getIdentity();
        identity.setResultingVersion(resultingVersion);
        this.identity = identity;
        this.patchType = PatchType.CUMULATIVE;
        return builder;
    }

    public PatchIdentityBuilder oneOffPatchIdentity(final String name, final String version) {
        final PatchIdentityBuilder builder = new PatchIdentityBuilder(name, version, PatchType.ONE_OFF, this);
        final IdentityImpl identity = builder.getIdentity();
        this.identity = identity;
        this.patchType = PatchType.ONE_OFF;
        return builder;
    }

    @Override
    protected PatchBuilder internalAddModification(ContentModification modification) {
        this.modifications.add(modification);
        return this;
    }

    public PatchElementBuilder upgradeElement(final String patchId, final String layerName, final boolean addOn) {
        if (!Patch.PATCH_NAME_PATTERN.matcher(patchId).matches()) {
            throw PatchMessages.MESSAGES.illegalPatchName(patchId);
        }
        final PatchElementBuilder builder = new PatchElementBuilder(patchId, layerName, addOn, this);
        builder.upgrade();
        elements.add(builder);
        return builder;
    }

    public PatchElementBuilder oneOffPatchElement(final String patchId, final String layerName, final boolean addOn) {
        if (!Patch.PATCH_NAME_PATTERN.matcher(patchId).matches()) {
            throw PatchMessages.MESSAGES.illegalPatchName(patchId);
        }
        final PatchElementBuilder builder = new PatchElementBuilder(patchId, layerName, addOn, this);
        builder.oneOffPatch();
        elements.add(builder);
        return builder;
    }

    public PatchElementBuilder addElement(final String patchId, final String layerName, final boolean addOn) {
        if (!Patch.PATCH_NAME_PATTERN.matcher(patchId).matches()) {
            throw PatchMessages.MESSAGES.illegalPatchName(patchId);
        }
        final PatchElementBuilder builder = new PatchElementBuilder(patchId, layerName, addOn, this);
        //builder.cumulativePatch();
        elements.add(builder);
        return builder;
    }

    public PatchBuilder addElement(final PatchElement element) {
        this.elements.add(new PatchElementHolder() {
            @Override
            public PatchElement createElement(PatchType patchType) {
                final PatchType type = element.getProvider().getPatchType();
                if (type == null) {
                    if (patchType == PatchType.CUMULATIVE) {
                        ((PatchElementProviderImpl)element.getProvider()).upgrade();
                    } else {
                        ((PatchElementProviderImpl)element.getProvider()).oneOffPatch();
                    }
                } else if (patchType != PatchBuilder.this.patchType) {
                    throw new RuntimeException("patch types don't match");
                }
                return element;
            }
        });
        return this;
    }

    public List<ContentModification> getModifications() {
        return modifications;
    }

    @Override
    public Patch resolvePatch(String name, String version) throws PatchingException {
        return build();
    }

    @Override
    public Patch build() {
        assert notNull(identity);
        assert notNull(patchId);

        // Create the elements
        final List<PatchElement> elements = new ArrayList<PatchElement>();
        for (final PatchElementHolder holder : this.elements) {
            elements.add(holder.createElement(patchType));
        }

        return new PatchImpl(patchId, description, identity, unmodifiableList(elements), unmodifiableList(modifications));
    }

    @Override
    protected PatchBuilder returnThis() {
        return this;
    }

    static boolean notNull(Object o) {
        return o != null;
    }

    protected interface PatchElementHolder {

        PatchElement createElement(final PatchType type);

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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1428.java