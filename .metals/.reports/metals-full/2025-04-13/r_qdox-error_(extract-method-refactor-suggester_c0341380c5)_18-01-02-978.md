error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9457.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9457.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9457.java
text:
```scala
(@@ComponentTypeInfo) elementType, null, _factory,

/*******************************************************************************
 * Copyright (c) 2008 Oracle Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cameron Bateman - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.jsf.facelet.core.internal.registry;

import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jst.jsf.common.dom.TagIdentifier;
import org.eclipse.jst.jsf.common.runtime.internal.model.component.ComponentTypeInfo;
import org.eclipse.jst.jsf.common.runtime.internal.model.decorator.ConverterTypeInfo;
import org.eclipse.jst.jsf.common.runtime.internal.model.decorator.ValidatorTypeInfo;
import org.eclipse.jst.jsf.common.runtime.internal.model.types.TypeInfo;
import org.eclipse.jst.jsf.common.runtime.internal.view.model.common.ITagElement;
import org.eclipse.jst.jsf.common.runtime.internal.view.model.common.IHandlerTagElement.TagHandlerType;
import org.eclipse.jst.jsf.core.internal.tld.TagIdentifierFactory;
import org.eclipse.jst.jsf.designtime.internal.Messages;
import org.eclipse.jst.jsf.designtime.internal.view.mapping.ViewMetadataLoader;
import org.eclipse.jst.jsf.designtime.internal.view.mapping.ViewMetadataMapper;
import org.eclipse.jst.jsf.designtime.internal.view.mapping.viewmapping.TagMapping;
import org.eclipse.jst.jsf.designtime.internal.view.mapping.viewmapping.TagToViewObjectMapping;
import org.eclipse.jst.jsf.designtime.internal.view.model.jsp.AbstractTagResolvingStrategy;
import org.eclipse.jst.jsf.designtime.internal.view.model.jsp.DefaultTagTypeInfo;
import org.eclipse.jst.jsf.facelet.core.internal.cm.FaceletDocumentFactory;
import org.eclipse.jst.jsf.facelet.core.internal.tagmodel.ComponentTag;
import org.eclipse.jst.jsf.facelet.core.internal.tagmodel.ConverterTag;
import org.eclipse.jst.jsf.facelet.core.internal.tagmodel.HandlerTag;
import org.eclipse.jst.jsf.facelet.core.internal.tagmodel.NoArchetypeFaceletTag;
import org.eclipse.jst.jsf.facelet.core.internal.tagmodel.ValidatorTag;
import org.osgi.framework.Version;

/**
 * Resolves facelet tags from JSF framework metadata.
 * 
 * @author cbateman
 *
 */
public class FaceletMetaResolvingStrategy
        extends
        AbstractTagResolvingStrategy<IFaceletTagResolvingStrategy.TLDWrapper, String>
        implements IFaceletTagResolvingStrategy
{

    /**
     * strategy id
     */
    public final static String           ID           = "org.eclipse.jst.jsf.facelet.metadata.FaceletMetaResolvingStrategy"; //$NON-NLS-1$
    /**
     * displayable nameb
     */
    public final static String           DISPLAY_NAME = Messages.DefaultJSPTagResolver_DisplayName;

    private final ViewMetadataLoader     _loader;
    private final ViewMetadataMapper     _mapper;
    private final FaceletDocumentFactory _factory;

    /**
     * @param project
     * @param factory 
     */
    public FaceletMetaResolvingStrategy(final IProject project,
            final FaceletDocumentFactory factory)
    {
        _factory = factory;
        _loader = new ViewMetadataLoader(project);
        _mapper = new ViewMetadataMapper();
    }

    @Override
    public ITagElement resolve(
            final IFaceletTagResolvingStrategy.TLDWrapper elementDecl)
    {
        // final IProjectFacetVersion version = JSFAppConfigUtils
        // .getProjectFacet(_project);
        // final String versionAsString = version.getVersionString();
        // final JSFVersion jsfVersion =
        // JSFVersion.valueOfString(versionAsString);

        final String uri = elementDecl.getUri();
        final String tagName = elementDecl.getTagDefn().getName();
        final TagIdentifier tagId = TagIdentifierFactory.createJSPTagWrapper(
                uri, tagName);
        // final DefaultTagTypeInfo defaultTagTypeInfo = new
        // DefaultTagTypeInfo();
        final TagMapping mapping = _loader.getTagToViewMapping(tagId);

        TypeInfo elementType = null;
        if (mapping != null)
        {
            elementType = findTypeInfo(mapping, "1.1", null); //$NON-NLS-1$
        }

        if (elementType instanceof ComponentTypeInfo)
        {
            return new ComponentTag(uri, tagName,
                    (ComponentTypeInfo) elementType, _factory,
                    new MetadataAttributeAdvisor(tagId, _loader));
        }
        else if (elementType instanceof ConverterTypeInfo)
        {
            return new ConverterTag(uri, tagName,
                    (ConverterTypeInfo) elementType, null, _factory,
                    new MetadataAttributeAdvisor(tagId, _loader));
        }
        else if (elementType instanceof ValidatorTypeInfo)
        {
            return new ValidatorTag(uri, tagName,
                    (ValidatorTypeInfo) elementType, null, _factory,
                    new MetadataAttributeAdvisor(tagId, _loader));
        }
        else if (elementType instanceof TagHandlerType)
        {
            return new HandlerTag(uri, tagName,
                    (TagHandlerType) elementType, null, _factory,
                    new MetadataAttributeAdvisor(
                            tagId, _loader));
        }
        else if (DefaultTagTypeInfo.isDefaultLib(tagId.getUri()))
        {
            return new NoArchetypeFaceletTag(uri, tagName, _factory, new MetadataAttributeAdvisor(tagId, _loader));
        }

        // not found
        return null;
    }

    private TypeInfo findTypeInfo(final TagMapping mapping,
            final String jsfVersion, final String libVersion)
    {
        final EList list = mapping.getVersionedTagToViewMappings();

        FIND_BY_VERSION: for (final Iterator<?> it = list.iterator(); it
                .hasNext();)
        {
            Object obj = it.next();

            if (obj instanceof TagToViewObjectMapping)
            {
                final TagToViewObjectMapping viewMapping = (TagToViewObjectMapping) obj;

                final String minJsfVersionString = viewMapping
                        .getMinJSFVersion();
                if (minJsfVersionString != null)
                {
                    try
                    {
                        final Version version = new Version(jsfVersion);
                        final Version minVersion = Version
                                .parseVersion(minJsfVersionString);

                        if (version.compareTo(minVersion) < 0)
                        {
                            // my version is less than the minimum specified
                            // by this meta-data
                            continue FIND_BY_VERSION;
                        }
                    }
                    catch (final IllegalArgumentException iae)
                    {
                        continue FIND_BY_VERSION;
                    }
                }
                final String minLibVersionString = viewMapping
                        .getMinLibraryVersion();
                if (libVersion != null && minLibVersionString != null)
                {
                    try
                    {
                        final Version version = new Version(libVersion);
                        final Version minLibVersion = Version
                                .parseVersion(minLibVersionString);

                        if (version.compareTo(minLibVersion) < 0)
                        {
                            // my lib version is less than the minimum specified
                            // by the meta-data
                            continue FIND_BY_VERSION;
                        }
                    }
                    catch (IllegalArgumentException iae)
                    {
                        continue FIND_BY_VERSION;
                    }
                }
                return _mapper.mapToFrameworkData(viewMapping.getTypeInfo());
            }
        }
        return null;
    }

    @Override
    public String getId()
    {
        return ID;
    }

    public String getDisplayName()
    {
        return DISPLAY_NAME;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9457.java