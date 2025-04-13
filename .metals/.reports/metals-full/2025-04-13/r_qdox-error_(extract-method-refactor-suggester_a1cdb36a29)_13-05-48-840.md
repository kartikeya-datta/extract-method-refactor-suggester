error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/747.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/747.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/747.java
text:
```scala
r@@eturn _prefix + ":"+name; //$NON-NLS-1$

package org.eclipse.jst.jsf.facelet.core.internal.cm;

import java.util.Iterator;

import org.eclipse.wst.xml.core.internal.contentmodel.CMContent;
import org.eclipse.wst.xml.core.internal.contentmodel.CMDataType;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNamedNodeMap;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;

class DocumentElementCMAdapter implements CMNamedNodeMap,
        CMElementDeclaration
{
    private final  String               _prefix;
    private final  ElementCMAdapter     _adapter;
    
    public DocumentElementCMAdapter(final ElementCMAdapter adapter, final String prefix)
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
        return _adapter.getNamedItem(name);
    }

    public CMNode item(int index)
    {
        return _adapter.item(index);
    }

    @SuppressWarnings("unchecked")
    public Iterator iterator()
    {
        return _adapter.iterator();
    }

    public CMNamedNodeMap getAttributes()
    {
        return _adapter.getAttributes();
    }

    public CMContent getContent()
    {
        return _adapter.getContent();
    }

    public int getContentType()
    {
       return _adapter.getContentType();
    }

    public CMDataType getDataType()
    {
        return _adapter.getDataType();
    }

    public String getElementName()
    {
        return getPrefixedName(_adapter.getElementName());
    }

    public CMNamedNodeMap getLocalElements()
    {
        return _adapter.getLocalElements();
    }

    public int getMaxOccur()
    {
        return _adapter.getMaxOccur();
    }

    public int getMinOccur()
    {
        return _adapter.getMinOccur();
    }

    public String getNodeName()
    {
        return getPrefixedName(_adapter.getNodeName());
    }

    private String getPrefixedName(final String name)
    {
        return _prefix + ":"+name;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/747.java