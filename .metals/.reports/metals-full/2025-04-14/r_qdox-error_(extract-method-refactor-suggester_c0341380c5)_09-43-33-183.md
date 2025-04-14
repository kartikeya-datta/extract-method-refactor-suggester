error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3466.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3466.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[152,2]

error in qdox parser
file content:
```java
offset: 4107
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3466.java
text:
```scala
import org.w3c.dom.TypeInfo;

/*
 * Copyright 2001, 2002,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.xerces.impl.xs.opti;

import org.apache.xerces.dom3.TypeInfo;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.w3c.dom.DOMException;


/**
 * @xerces.internal  
 * 
 * @author Rahul Srivastava, Sun Microsystems Inc.
 *
 * @version $Id$
 */
public class DefaultElement extends NodeImpl 
                            implements Element {

    // default constructor
    public DefaultElement() {
    }
    
    
    public DefaultElement(String prefix, String localpart, String rawname, String uri, short nodeType) {
    	super(prefix, localpart, rawname, uri, nodeType);
    }
    
    
    //
    // org.w3c.dom.Element methods
    //
    
    // getter methods
    public String getTagName() {
    	return null;
    }


    public String getAttribute(String name) {
    	return null;
    }


    public Attr getAttributeNode(String name) {
    	return null;
    }


    public NodeList getElementsByTagName(String name) {
    	return null;
    }


    public String getAttributeNS(String namespaceURI, String localName) {
    	return null;
    }


    public Attr getAttributeNodeNS(String namespaceURI, String localName) {
    	return null;
    }


    public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
    	return null;
    }


    public boolean hasAttribute(String name) {
    	return false;
    }


    public boolean hasAttributeNS(String namespaceURI, String localName) {
    	return false;
    }
    
    public TypeInfo getSchemaTypeInfo(){
      return null;
    }
    

    // setter methods
    public void setAttribute(String name, String value) throws DOMException {
    	throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Method not supported");
    }


    public void removeAttribute(String name) throws DOMException {
    	throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Method not supported");
    }


    public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
    	throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Method not supported");
    }
    
    
    public Attr setAttributeNode(Attr newAttr) throws DOMException {
    	throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Method not supported");
    }
    
    
    public void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
    	throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Method not supported");
    }
    
    
    public void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
    	throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Method not supported");
    }
    
    
    public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
    	throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Method not supported");
    }
    
    public void setIdAttributeNode(Attr at, boolean makeId) throws DOMException{
        throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Method not supported");
    }
    public void setIdAttribute(String name, boolean makeId) throws DOMException{
        throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Method not supported");
    }
    
    public void setIdAttributeNS(String namespaceURI, String localName,
                                    boolean makeId) throws DOMException{
        throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Method not supported");
    }

}
	
 N@@o newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3466.java