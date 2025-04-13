error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7970.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7970.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7970.java
text:
```scala
final F@@aceletDocumentFactory factory = new FaceletDocumentFactory(_project);

package org.eclipse.jst.jsf.facelet.ui.internal.hover;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jst.jsf.context.resolver.structureddocument.IStructuredDocumentContextResolverFactory;
import org.eclipse.jst.jsf.context.resolver.structureddocument.IWorkspaceContextResolver;
import org.eclipse.jst.jsf.context.resolver.structureddocument.internal.ITextRegionContextResolver;
import org.eclipse.jst.jsf.context.structureddocument.IStructuredDocumentContext;
import org.eclipse.jst.jsf.context.structureddocument.IStructuredDocumentContextFactory;
import org.eclipse.jst.jsf.facelet.core.internal.cm.FaceletDocumentFactory;
import org.eclipse.jst.jsf.ui.internal.jspeditor.JSFELHover;
import org.eclipse.wst.html.ui.internal.taginfo.HTMLTagInfoHoverProcessor;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class FaceletHover implements ITextHover
{
    private IProject                  _project;
    private JSFELHover                _elHover;
    private HTMLTagInfoHoverProcessor _htmlHoverProcessor;

    public FaceletHover()
    {
        _elHover = new JSFELHover();
        _htmlHoverProcessor = new MyHTMLTagInfoHoverProcessor();
    }

    public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion)
    {
        final IStructuredDocumentContext context = IStructuredDocumentContextFactory.INSTANCE
                .getContext(textViewer, hoverRegion.getOffset());
        String info = null;
        if (isInAttributeValue(context))
        {
            info = _elHover.getHoverInfo(textViewer, hoverRegion);
        }

        if (info == null)
        {
            if (context != null)
            {
                _project = getProject(context);
            }

            info = _htmlHoverProcessor.getHoverInfo(textViewer, hoverRegion);
        }

        return info;
    }

    public IRegion getHoverRegion(ITextViewer textViewer, int offset)
    {
        IRegion region = null;
        final IStructuredDocumentContext context = IStructuredDocumentContextFactory.INSTANCE
                .getContext(textViewer, offset);

        // if we are in an attribute value, try to get a region from the
        // el hover first
        if (context != null)
        {
            if (isInAttributeValue(context))
            {
                region = _elHover.getHoverRegion(textViewer, offset);
            }
        }

        if (region == null)
        {
            if (context != null)
            {
                _project = getProject(context);
            }
            region = _htmlHoverProcessor.getHoverRegion(textViewer, offset);
        }

        return region;
    }

    private boolean isInAttributeValue(final IStructuredDocumentContext context)
    {
        final ITextRegionContextResolver resolver = IStructuredDocumentContextResolverFactory.INSTANCE
                .getTextRegionResolver(context);
        final String regionType = resolver.getRegionType();
        if (regionType != null
                && (regionType == DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE || resolver
                        .matchesRelative(new String[]
                        { DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE })))
        {
            return true;
        }

        return false;
    }

    private IProject getProject(final IStructuredDocumentContext context)
    {
        final IWorkspaceContextResolver resolver = IStructuredDocumentContextResolverFactory.INSTANCE
                .getWorkspaceContextResolver(context);

        if (resolver != null)
        {
            return resolver.getProject();
        }
        return null;
    }

    private class MyHTMLTagInfoHoverProcessor extends HTMLTagInfoHoverProcessor
    {
        @Override
        protected CMElementDeclaration getCMElementDeclaration(Node node)
        {
            if (_project != null && node.getNodeType() == Node.ELEMENT_NODE
                    && node.getPrefix() != null)
            {
                final Element element = (Element) node;
                final FaceletDocumentFactory factory = new FaceletDocumentFactory();

                final CMElementDeclaration elementDecl = factory
                        .createCMElementDeclaration(_project, element);

                if (elementDecl != null)
                {
                    return elementDecl;
                }
            }

            return super.getCMElementDeclaration(node);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7970.java