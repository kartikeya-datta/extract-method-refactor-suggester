error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4047.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4047.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4047.java
text:
```scala
t@@ypeInfo = DTComponentIntrospector.getComponent(componentType,

package org.eclipse.jst.jsf.facelet.core.internal.registry;

import org.eclipse.core.resources.IProject;
import org.eclipse.jem.internal.proxy.core.IConfigurationContributor;
import org.eclipse.jst.jsf.common.dom.TagIdentifier;
import org.eclipse.jst.jsf.common.runtime.internal.model.component.ComponentTypeInfo;
import org.eclipse.jst.jsf.common.runtime.internal.model.decorator.ConverterTypeInfo;
import org.eclipse.jst.jsf.common.runtime.internal.model.decorator.ValidatorTypeInfo;
import org.eclipse.jst.jsf.common.runtime.internal.view.model.common.ITagElement;
import org.eclipse.jst.jsf.core.internal.tld.TagIdentifierFactory;
import org.eclipse.jst.jsf.designtime.internal.view.DTComponentIntrospector;
import org.eclipse.jst.jsf.designtime.internal.view.mapping.ViewMetadataLoader;
import org.eclipse.jst.jsf.designtime.internal.view.model.jsp.AbstractTagResolvingStrategy;
import org.eclipse.jst.jsf.designtime.internal.view.model.jsp.IAttributeAdvisor;
import org.eclipse.jst.jsf.facelet.core.internal.cm.FaceletDocumentFactory;
import org.eclipse.jst.jsf.facelet.core.internal.registry.taglib.faceletTaglib.ComponentTagDefn;
import org.eclipse.jst.jsf.facelet.core.internal.registry.taglib.faceletTaglib.ConverterTagDefn;
import org.eclipse.jst.jsf.facelet.core.internal.registry.taglib.faceletTaglib.HandlerTagDefn;
import org.eclipse.jst.jsf.facelet.core.internal.registry.taglib.faceletTaglib.SourceTagDefn;
import org.eclipse.jst.jsf.facelet.core.internal.registry.taglib.faceletTaglib.TagDefn;
import org.eclipse.jst.jsf.facelet.core.internal.registry.taglib.faceletTaglib.ValidatorTagDefn;
import org.eclipse.jst.jsf.facelet.core.internal.tagmodel.ComponentTag;
import org.eclipse.jst.jsf.facelet.core.internal.tagmodel.ConverterTag;
import org.eclipse.jst.jsf.facelet.core.internal.tagmodel.FaceletTag;
import org.eclipse.jst.jsf.facelet.core.internal.tagmodel.HandlerTag;
import org.eclipse.jst.jsf.facelet.core.internal.tagmodel.NoArchetypeFaceletTag;
import org.eclipse.jst.jsf.facelet.core.internal.tagmodel.SourceTag;
import org.eclipse.jst.jsf.facelet.core.internal.tagmodel.ValidatorTag;

/*package*/class FaceletTagResolvingStrategy
        extends
        AbstractTagResolvingStrategy<IFaceletTagResolvingStrategy.TLDWrapper, String>
        implements IFaceletTagResolvingStrategy
{
    public final static String           ID = "org.eclipse.jst.jsf.facelet.core.FaceletTagResolvingStrategy"; //$NON-NLS-1$
    private final IProject               _project;
    private final FaceletDocumentFactory _factory;
    private final ViewMetadataLoader     _viewLoader;

    public FaceletTagResolvingStrategy(final IProject project,
            final FaceletDocumentFactory factory)
    {
        _project = project;
        _factory = factory;
        _viewLoader = new ViewMetadataLoader(project);
    }

    @Override
    public final String getId()
    {
        return ID;
    }

    @Override
    public final ITagElement resolve(final TLDWrapper tldWrapper)
    {
        return createFaceletTag(tldWrapper.getUri(), tldWrapper.getTagDefn());
    }

    public final String getDisplayName()
    {
        return Messages.FaceletTagResolvingStrategy_FACELET_TAG_RESOLVER_DISPLAY_NAME;
    }

    private FaceletTag createFaceletTag(final String uri, final TagDefn tagDefn)
    {
        final String tagName = tagDefn.getName();
        final TagIdentifier tagId = TagIdentifierFactory.createJSPTagWrapper(
                uri, tagName);

        final IAttributeAdvisor advisor = new MetadataAttributeAdvisor(tagId,
                _viewLoader);

        if (tagDefn instanceof ComponentTagDefn)
        {
            final ComponentTagDefn componentTagDefn = (ComponentTagDefn) tagDefn;
            final String componentType = componentTagDefn.getComponentType();
            final String componentClass = DTComponentIntrospector
                    .findComponentClass(componentType, _project);

            ComponentTypeInfo typeInfo = null;

            if (componentClass != null)
            {
                typeInfo = DTComponentIntrospector.getComponent(componentClass,
                        componentClass, _project,
                        new IConfigurationContributor[]
                        { new ELProxyContributor(_project) });
            }
            return new ComponentTag(uri, tagName, typeInfo, safeGetString(componentTagDefn.getHandlerClass()), _factory, advisor);
        }
        // render type is optional, but must have component type
        else if (tagDefn instanceof ValidatorTagDefn)
        {
            final ValidatorTagDefn validatorTagDefn = (ValidatorTagDefn) tagDefn;
            final String validatorId = validatorTagDefn.getValidatorId();

            ValidatorTypeInfo typeInfo;

            if (validatorId != null)
            {
                final String validatorClass = DTComponentIntrospector
                        .findValidatorClass(validatorId, _project);
                typeInfo = new ValidatorTypeInfo(validatorClass, validatorId);
            }
            else
            {
                typeInfo = ValidatorTypeInfo.UNKNOWN;
            }

            return new ValidatorTag(uri, tagName, typeInfo, safeGetString(validatorTagDefn.getHandlerClass()), _factory,
                    advisor);
        }
        // render type is optional, but must have converter id
        else if (tagDefn instanceof ConverterTagDefn)
        {
            final ConverterTagDefn converterTagDefn = (ConverterTagDefn) tagDefn;
            final String converterId = converterTagDefn.getConverterId();

            ConverterTypeInfo typeInfo;

            if (converterId != null)
            {
                final String converterClass = DTComponentIntrospector
                        .findConverterClass(converterId, _project);
                typeInfo = new ConverterTypeInfo(converterClass, converterId);
            }
            else
            {
                typeInfo = ConverterTypeInfo.UNKNOWN;
            }

            // for now, all converters are unknown
            return new ConverterTag(uri, tagName, typeInfo, 
                    safeGetString(converterTagDefn.getHandlerClass()), _factory, advisor);
        }
        else if (tagDefn instanceof HandlerTagDefn)
        {
            final String handlerClass = safeGetString(((HandlerTagDefn)tagDefn).getHandlerClass());
            return new HandlerTag(uri, tagName, null, handlerClass, _factory, advisor);
        }
        else if (tagDefn instanceof SourceTagDefn)
        {
            final String source = ((SourceTagDefn)tagDefn).getSource();
            return new SourceTag(uri, tagName, source, _factory, advisor);
        }

        return new NoArchetypeFaceletTag(uri, tagName, _factory, advisor);
    }
    
    private static String safeGetString(final String value)
    {
        if (value == null)
        {
            return null;
        }
        
        final String trimmed = value.trim();
        
        if ("".equals(trimmed)) //$NON-NLS-1$
        {
            return null;
        }
        
        return trimmed;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4047.java