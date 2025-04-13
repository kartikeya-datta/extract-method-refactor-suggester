error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9458.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9458.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9458.java
text:
```scala
(@@ComponentTypeInfo) typeInfo, null, _factory, advisor);

package org.eclipse.jst.jsf.facelet.core.internal.registry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.jst.jsf.common.dom.TagIdentifier;
import org.eclipse.jst.jsf.common.runtime.internal.model.component.ComponentTypeInfo;
import org.eclipse.jst.jsf.common.runtime.internal.model.decorator.ConverterTypeInfo;
import org.eclipse.jst.jsf.common.runtime.internal.model.decorator.ValidatorTypeInfo;
import org.eclipse.jst.jsf.common.runtime.internal.model.types.TypeInfo;
import org.eclipse.jst.jsf.common.runtime.internal.view.model.common.ITagElement;
import org.eclipse.jst.jsf.common.runtime.internal.view.model.common.IHandlerTagElement.TagHandlerType;
import org.eclipse.jst.jsf.core.JSFVersion;
import org.eclipse.jst.jsf.core.internal.tld.TagIdentifierFactory;
import org.eclipse.jst.jsf.core.jsfappconfig.JSFAppConfigUtils;
import org.eclipse.jst.jsf.designtime.internal.view.mapping.ViewMetadataLoader;
import org.eclipse.jst.jsf.designtime.internal.view.model.jsp.AbstractTagResolvingStrategy;
import org.eclipse.jst.jsf.designtime.internal.view.model.jsp.DefaultTagTypeInfo;
import org.eclipse.jst.jsf.facelet.core.internal.cm.FaceletDocumentFactory;
import org.eclipse.jst.jsf.facelet.core.internal.registry.IFaceletTagResolvingStrategy.TLDWrapper;
import org.eclipse.jst.jsf.facelet.core.internal.tagmodel.ComponentTag;
import org.eclipse.jst.jsf.facelet.core.internal.tagmodel.ConverterTag;
import org.eclipse.jst.jsf.facelet.core.internal.tagmodel.HandlerTag;
import org.eclipse.jst.jsf.facelet.core.internal.tagmodel.IFaceletTagConstants;
import org.eclipse.jst.jsf.facelet.core.internal.tagmodel.NoArchetypeFaceletTag;
import org.eclipse.jst.jsf.facelet.core.internal.tagmodel.ValidatorTag;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

/**
 * Temporary hard-coded tag resolver (to be replaced by meta-data)
 * @author cbateman
 *
 */
public class VeryTemporaryDefaultFaceletResolver extends
        AbstractTagResolvingStrategy<TLDWrapper, String> implements IFaceletTagResolvingStrategy
{
    /**
     * Tag resolver unique identifier
     */
    public final static String       ID = "org.eclipse.jst.jsf.facelet.core.VeryTemporaryDefaultFaceletResolver"; //$NON-NLS-1$
    private final DefaultTagTypeInfo _coreHtmlTypeInfo;
    private final IProject           _project;
    private final FaceletDocumentFactory    _factory;
    private final ViewMetadataLoader _viewLoader;

    /**
     * @param project
     * @param factory
     */
    public VeryTemporaryDefaultFaceletResolver(final IProject project, final FaceletDocumentFactory factory)
    {
        super();
        _factory = factory;
        _project = project;
        _coreHtmlTypeInfo = new DefaultTagTypeInfo();
        _viewLoader = new ViewMetadataLoader(project);
    }

    @Override
    public final String getId()
    {
        return ID;
    }

    @Override
    public ITagElement resolve(final TLDWrapper element)
    {
        final IProjectFacetVersion version = JSFAppConfigUtils
        .getProjectFacet(_project);
        final String versionAsString = version.getVersionString();
        final JSFVersion jsfVersion = JSFVersion.valueOfString(versionAsString);

        final String uri = element.getUri();
        final String name = element.getTagDefn().getName();
        final TagIdentifier tagId = TagIdentifierFactory.createJSPTagWrapper(
                uri, name);
        TypeInfo typeInfo = null;
        if (IFaceletTagConstants.URI_JSF_FACELETS.equals(element.getUri()))
        {
            typeInfo = getTypeInfo(tagId, jsfVersion);
        }
        else
        {
            typeInfo = _coreHtmlTypeInfo.getTypeInfo(tagId,
                    jsfVersion);
        }
        return createFromTypeInfo(tagId, typeInfo);
    }

    private ITagElement createFromTypeInfo(final TagIdentifier tagId,
            final TypeInfo typeInfo)
    {
        final MetadataAttributeAdvisor advisor =
            new MetadataAttributeAdvisor(tagId, _viewLoader);
        if (typeInfo instanceof ComponentTypeInfo)
        {
            return new ComponentTag(tagId.getUri(), tagId.getTagName(),
                    (ComponentTypeInfo) typeInfo, _factory, advisor);
        }
        else if (typeInfo instanceof ConverterTypeInfo)
        {
            return new ConverterTag(tagId.getUri(), tagId.getTagName(),
                    (ConverterTypeInfo) typeInfo, null, _factory, advisor);
        }
        else if (typeInfo instanceof ValidatorTypeInfo)
        {
            return new ValidatorTag(tagId.getUri(), tagId.getTagName(),
                    (ValidatorTypeInfo) typeInfo, null, _factory, advisor);
        }
        else if (typeInfo instanceof TagHandlerType)
        {
            return new HandlerTag(tagId.getUri(), tagId.getTagName(),
                    (TagHandlerType) typeInfo, null, _factory, advisor);
        }
        else if (DefaultTagTypeInfo.isDefaultLib(tagId.getUri()))
        {
            return new NoArchetypeFaceletTag(tagId.getUri(), tagId.getTagName(), _factory, advisor);
        }

        // not found
        return null;

    }

    public final String getDisplayName()
    {
        return "Meta-data Driven Tag Resolver"; //$NON-NLS-1$
    }

    private static final ComponentTypeInfo COMPINFO_COMPONENT = new ComponentTypeInfo(
            "facelets.ui.ComponentRef", //$NON-NLS-1$
            "com.sun.facelets.tag.ui.ComponentRef", //$NON-NLS-1$
            new String[]
                       {
                    "javax.faces.component.UIComponentBase", //$NON-NLS-1$
                    "javax.faces.component.UIComponent", "java.lang.Object", }, //$NON-NLS-1$ //$NON-NLS-2$
                    new String[]
                               { "javax.faces.component.StateHolder" }, //$NON-NLS-1$
                               "facelets", //$NON-NLS-1$
                               null);

    private static final ComponentTypeInfo COMPINFO_DEBUG     = new ComponentTypeInfo(
            "facelets.ui.Debug", //$NON-NLS-1$
            "com.sun.facelets.tag.ui.UIDebug", //$NON-NLS-1$
            new String[]
                       {
                    "javax.faces.component.UIComponentBase", //$NON-NLS-1$
                    "javax.faces.component.UIComponent", "java.lang.Object", }, //$NON-NLS-1$ //$NON-NLS-2$
                    new String[]
                               { "javax.faces.component.StateHolder" }, //$NON-NLS-1$
                               "facelets", //$NON-NLS-1$
                               null);

    private static final ComponentTypeInfo COMPINFO_REPEAT    = new ComponentTypeInfo(
            "facelets.ui.Repeat", //$NON-NLS-1$
            "com.sun.facelets.component.UIRepeat", //$NON-NLS-1$
            new String[]
                       {
                    "javax.faces.component.UIComponentBase", //$NON-NLS-1$
                    "javax.faces.component.UIComponent", "java.lang.Object", }, //$NON-NLS-1$ //$NON-NLS-2$
                    new String[]
                               {
                    "javax.faces.component.StateHolder", //$NON-NLS-1$
                               "javax.faces.component.NamingContainer"                  }, //$NON-NLS-1$
                               "facelets", //$NON-NLS-1$
                               null);

    /**
     * @param tagId
     * @param jsfVersion
     * @return a type info for the tag id in jsf version or null if none.
     */
    private TypeInfo getTypeInfo(final TagIdentifier tagId,
            final JSFVersion jsfVersion)
    {

        switch (jsfVersion)
        {
            case V1_0:
            case V1_1:
                return JSF11_ELEMENTS.get(tagId);

            case V1_2:
                return JSF12_ELEMENTS.get(tagId);

            default:
                return null;
        }
    }
    private static Map<TagIdentifier, TypeInfo> JSF11_ELEMENTS;
    private static Map<TagIdentifier, TypeInfo> JSF12_ELEMENTS;
    static
    {
        final Map<TagIdentifier, TypeInfo> elements = new HashMap<TagIdentifier, TypeInfo>();

        elements.put(IFaceletTagConstants.TAG_IDENTIFIER_COMPONENT,
                COMPINFO_COMPONENT);

        elements.put(IFaceletTagConstants.TAG_IDENTIFIER_DEBUG,
                COMPINFO_DEBUG);

        elements.put(IFaceletTagConstants.TAG_IDENTIFIER_FRAGMENT,
                COMPINFO_COMPONENT);

        elements.put(IFaceletTagConstants.TAG_IDENTIFIER_REPEAT,
                COMPINFO_REPEAT);

        JSF11_ELEMENTS = Collections.unmodifiableMap(elements);

        JSF12_ELEMENTS = Collections
        .unmodifiableMap(new HashMap<TagIdentifier, TypeInfo>(elements));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9458.java