error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/748.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/748.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/748.java
text:
```scala
t@@hrow new UnsupportedOperationException(""); //$NON-NLS-1$

package org.eclipse.jst.jsf.facelet.core.internal.cm;

import java.util.Iterator;

import org.eclipse.wst.xml.core.internal.contentmodel.CMDocument;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNamedNodeMap;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNamespace;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;

/**
 * A namespace a specific to a document, where it's tag name prefix is known.
 *
 */
class DocumentNamespaceCMAdapter implements CMNamedNodeMap, CMDocument
{
    private final String                    _prefix;
    private final NamespaceCMAdapter        _adapter;
    
    public DocumentNamespaceCMAdapter(final NamespaceCMAdapter adapter, final String prefix)
    {
        _prefix = prefix;
        _adapter = adapter;
    }
    
    public int getLength()
    {
        return _adapter.getLength();
    }

    public CMNode getNamedItem(String name)
    {
        CMNode  node = _adapter.getNamedItem(name);
        
        if (node != null)
        {
            node =  new DocumentElementCMAdapter((ElementCMAdapter) node,_prefix);
        }
        return node;
    }

    public CMNode item(int index)
    {
        CMNode  item = _adapter.item(index);
        
        if (item != null)
        {
            item = new DocumentElementCMAdapter((ElementCMAdapter) item,_prefix);
        }
        return item;
    }

    public Iterator<?> iterator()
    {
        return new WrappingIterator(_adapter.iterator());
    }

    private class WrappingIterator implements Iterator<CMNode>
    {
        private Iterator<?>   _it;
        
        public WrappingIterator(final Iterator<?> it)
        {
            _it = it;
        }
        public boolean hasNext()
        {
            return _it.hasNext();
        }

        public CMNode next()
        {
            CMNode node = (CMNode) _it.next();
            node = getNamedItem(node.getNodeName());
            return node;
        }

        public void remove()
        {
            throw new UnsupportedOperationException("");
        }
    }

    public CMNamedNodeMap getElements()
    {
        return this;
    }

    public CMNamedNodeMap getEntities()
    {
        //not changing entities
        return _adapter.getEntities();
    }

    public CMNamespace getNamespace()
    {
        return new CMNamespaceImpl(_adapter.getNamespace(), _prefix);
    }

    public String getNodeName()
    {
        // not changing node name
        return _adapter.getNodeName();
    }

    public int getNodeType()
    {
        return _adapter.getNodeType();
    }

    public Object getProperty(String propertyName)
    {
        return _adapter.getProperty(propertyName);
    }

    public boolean supports(String propertyName)
    {
        return _adapter.supports(propertyName);
    }

    private static class CMNamespaceImpl implements CMNamespace
    {
        private final CMNamespace   _proxy;
        private final String        _prefix;
        
        CMNamespaceImpl(CMNamespace proxy, final String prefix)
        {
            _proxy = proxy;
            _prefix = prefix;
        }

        public String getPrefix()
        {
            return _prefix;
        }

        public String getURI()
        {
            return _proxy.getURI();
        }

        public String getNodeName()
        {
            return _proxy.getNodeName();
        }

        public int getNodeType()
        {
            return _proxy.getNodeType();
        }

        public Object getProperty(String propertyName)
        {
            return _proxy.getProperty(propertyName);
        }

        public boolean supports(String propertyName)
        {
            return _proxy.supports(propertyName);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/748.java