error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7914.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7914.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7914.java
text:
```scala
r@@eturn new URI(baseURI).toString();

/*
 * Copyright 1999-2002,2004 The Apache Software Foundation.
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

package org.apache.xerces.dom;

import org.apache.xerces.util.URI;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.Notation;

/**
 * Notations are how the Document Type Description (DTD) records hints
 * about the format of an XML "unparsed entity" -- in other words,
 * non-XML data bound to this document type, which some applications
 * may wish to consult when manipulating the document. A Notation
 * represents a name-value pair, with its nodeName being set to the
 * declared name of the notation.
 * <P>
 * Notations are also used to formally declare the "targets" of
 * Processing Instructions.
 * <P>
 * Note that the Notation's data is non-DOM information; the DOM only
 * records what and where it is.
 * <P>
 * See the XML 1.0 spec, sections 4.7 and 2.6, for more info.
 * <P>
 * Level 1 of the DOM does not support editing Notation contents.
 *
 * @version $Id$
 * @since  PR-DOM-Level-1-19980818.
 */
public class NotationImpl 
    extends NodeImpl 
    implements Notation {

    //
    // Constants
    //

    /** Serialization version. */
    static final long serialVersionUID = -764632195890658402L;
    
    //
    // Data
    //

    /** Notation name. */
    protected String name;

    /** Public identifier. */
    protected String publicId;

    /** System identifier. */
    protected String systemId;

    /** Base URI*/
    protected String baseURI;

    //
    // Constructors
    //

    /** Factory constructor. */
    public NotationImpl(CoreDocumentImpl ownerDoc, String name) {
    	super(ownerDoc);
        this.name = name;
    }
    
    //
    // Node methods
    //

    /** 
     * A short integer indicating what type of node this is. The named
     * constants for this value are defined in the org.w3c.dom.Node interface.
     */
    public short getNodeType() {
        return Node.NOTATION_NODE;
    }

    /**
     * Returns the notation name
     */
    public String getNodeName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return name;
    }

    //
    // Notation methods
    //

    /**
     * The Public Identifier for this Notation. If no public identifier
     * was specified, this will be null.  
     */
    public String getPublicId() {

        if (needsSyncData()) {
            synchronizeData();
        }
    	return publicId;

    } // getPublicId():String

    /**
     * The System Identifier for this Notation. If no system identifier
     * was specified, this will be null.  
     */
    public String getSystemId() {

        if (needsSyncData()) {
            synchronizeData();
        }
    	return systemId;

    } // getSystemId():String

    //
    // Public methods
    //

    /** 
     * NON-DOM: The Public Identifier for this Notation. If no public
     * identifier was specified, this will be null.  
     */
    public void setPublicId(String id) {

    	if (isReadOnly()) {
    		throw new DOMException(
    		DOMException.NO_MODIFICATION_ALLOWED_ERR,
                DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null));
        }
        if (needsSyncData()) {
            synchronizeData();
        }
        publicId = id;

    } // setPublicId(String)

    /** 
     * NON-DOM: The System Identifier for this Notation. If no system
     * identifier was specified, this will be null.  
     */
    public void setSystemId(String id) {

    	if(isReadOnly()) {
    		throw new DOMException(
    		DOMException.NO_MODIFICATION_ALLOWED_ERR,
                DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null));
        }
        if (needsSyncData()) {
            synchronizeData();
        }
    	systemId = id;

    } // setSystemId(String)
    

    /**
     * Returns the absolute base URI of this node or null if the implementation
     * wasn't able to obtain an absolute URI. Note: If the URI is malformed, a
     * null is returned.
     * 
     * @return The absolute base URI of this node or null.
     * @since DOM Level 3
     */
    public String getBaseURI() {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (baseURI != null && baseURI.length() != 0 ) {// attribute value is always empty string
            try {
                baseURI = new URI(baseURI).toString();
            }
            catch (org.apache.xerces.util.URI.MalformedURIException e){
                // REVISIT: what should happen in this case?
                return null;
            }
        }
        return baseURI;
    }

    /** NON-DOM: set base uri*/
    public void setBaseURI(String uri){
        if (needsSyncData()) {
            synchronizeData();
        }
        baseURI = uri;
    }

} // class NotationImpl
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7914.java