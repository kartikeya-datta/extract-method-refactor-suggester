error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6822.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6822.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,4]

error in qdox parser
file content:
```java
offset: 4
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6822.java
text:
```scala
 (@@namespaceURI!=null && namespaceURI.equals(NamespaceContext.XMLNS_URI)

/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 1999-2002 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Xerces" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation and was
 * originally based on software copyright (c) 1999, International
 * Business Machines, Inc., http://www.apache.org.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.apache.xerces.dom;

import org.w3c.dom.DOMException;
import org.apache.xerces.impl.dv.xs.XSSimpleTypeDecl;
import org.apache.xerces.xni.NamespaceContext;

/**
 * AttrNSImpl inherits from AttrImpl and adds namespace support. 
 * <P>
 * The qualified name is the node name, and we store localName which is also
 * used in all queries. On the other hand we recompute the prefix when
 * necessary.
 * @author Arnaud  Le Hors, IBM
 * @author Andy Clark, IBM
 * @author Ralf Pfeiffer, IBM
 * @version $Id$
 */
public class AttrNSImpl
    extends AttrImpl {

    //
    // Constants
    //

    /** Serialization version. */
    static final long serialVersionUID = -781906615369795414L;
    static final String xmlnsURI = "http://www.w3.org/2000/xmlns/";
    static final String xmlURI = "http://www.w3.org/XML/1998/namespace";

    //
    // Data
    //

    /** DOM2: Namespace URI. */
    protected String namespaceURI;
  
    /** DOM2: localName. */
    protected String localName;

    /*
     * Default constructor
     */
    public AttrNSImpl(){}
        
   /**
     * DOM2: Constructor for Namespace implementation.
     */
    protected AttrNSImpl(CoreDocumentImpl ownerDocument, 
                         String namespaceURI, 
                         String qualifiedName) {

        super(ownerDocument, qualifiedName);
        setName(namespaceURI, qualifiedName);
    }

    private void setName(String namespaceURI, String qname){
        
        String prefix;
		// DOM Level 3: namespace URI is never empty string.
        this.namespaceURI = namespaceURI;
        if (namespaceURI !=null) {
  	        this.namespaceURI = (namespaceURI.length() == 0)? null
                                 : namespaceURI;

		}
		int colon1 = qname.indexOf(':');
		int colon2 = qname.lastIndexOf(':');
		ownerDocument().checkNamespaceWF(qname, colon1, colon2);
		if (colon1 < 0) {
			// there is no prefix
			localName = qname;
			ownerDocument().checkQName(null, localName);
			if (ownerDocument().errorChecking) {
				if (qname.equals("xmlns")
					&& (namespaceURI == null
 !namespaceURI.equals(NamespaceContext.XMLNS_URI))
 (namespaceURI.equals(NamespaceContext.XMLNS_URI)
						&& !qname.equals("xmlns"))) {
					String msg =
						DOMMessageFormatter.formatMessage(
							DOMMessageFormatter.DOM_DOMAIN,
							"NAMESPACE_ERR",
							null);
					throw new DOMException(DOMException.NAMESPACE_ERR, msg);
				}
			}
		}
		else {
			prefix = qname.substring(0, colon1);
			localName = qname.substring(colon2+1);
			ownerDocument().checkQName(prefix, localName);
            ownerDocument().checkDOMNSErr(prefix, namespaceURI);
        }
    } 

    // when local name is known
    public AttrNSImpl(CoreDocumentImpl ownerDocument, 
                         String namespaceURI, 
                         String qualifiedName,
                         String localName) {
        super(ownerDocument, qualifiedName);
        
        this.localName = localName;
        this.namespaceURI = namespaceURI;
    }
    
    // for DeferredAttrImpl
    protected AttrNSImpl(CoreDocumentImpl ownerDocument, 
                         String value) {
        super(ownerDocument, value);
    }

    // Support for DOM Level 3 renameNode method.
    // Note: This only deals with part of the pb. It is expected to be
    // called after the Attr has been detached for one thing.
    // CoreDocumentImpl does all the work.
    void rename(String namespaceURI, String qualifiedName) {
        if (needsSyncData()) {
            synchronizeData();
        }
	this.name = qualifiedName;
        setName(namespaceURI, qualifiedName);
    }

    /**
     * NON-DOM: resets this node and sets specified values for the node
     * 
     * @param ownerDocument
     * @param namespaceURI
     * @param qualifiedName
     * @param localName
     */
    public void setValues (CoreDocumentImpl ownerDocument, 
                         String namespaceURI, 
                         String qualifiedName,
                         String localName){

        super.textNode = null;
        super.flags = 0;
        isSpecified(true);
        hasStringValue(true);
        super.setOwnerDocument(ownerDocument);
        this.localName = localName;
        this.namespaceURI = namespaceURI;
        super.name = qualifiedName;
        super.value = null;
    }

    //
    // DOM2: Namespace methods
    //

    /** 
     * Introduced in DOM Level 2. <p>
     *
     * The namespace URI of this node, or null if it is unspecified.<p>
     *
     * This is not a computed value that is the result of a namespace lookup
     * based on an examination of the namespace declarations in scope. It is
     * merely the namespace URI given at creation time.<p>
     *
     * For nodes created with a DOM Level 1 method, such as createElement
     * from the Document interface, this is null.     
     * @since WD-DOM-Level-2-19990923
     */
    public String getNamespaceURI()
    {
        if (needsSyncData()) {
            synchronizeData();
        }
        // REVIST: This code could/should be done at a lower-level, such that
        // the namespaceURI is set properly upon creation. However, there still
        // seems to be some DOM spec interpretation grey-area.
        return namespaceURI;
    }
    
    /** 
     * Introduced in DOM Level 2. <p>
     *
     * The namespace prefix of this node, or null if it is unspecified. <p>
     *
     * For nodes created with a DOM Level 1 method, such as createElement
     * from the Document interface, this is null. <p>
     *
     * @since WD-DOM-Level-2-19990923
     */
    public String getPrefix()
    {
        if (needsSyncData()) {
            synchronizeData();
        }
        int index = name.indexOf(':');
        return index < 0 ? null : name.substring(0, index); 
    }
    
    /**
     * Introduced in DOM Level 2. <p>
     * 
     * Note that setting this attribute changes the nodeName attribute, which
     * holds the qualified name, as well as the tagName and name attributes of
     * the Element and Attr interfaces, when applicable.<p>
     * 
     * @param prefix The namespace prefix of this node, or null(empty string) if it is unspecified.
     *
     * @exception INVALID_CHARACTER_ERR
     *                   Raised if the specified
     *                   prefix contains an invalid character.
     * @exception DOMException
     * @since WD-DOM-Level-2-19990923
     */
    public void setPrefix(String prefix)
        throws DOMException
    {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (ownerDocument().errorChecking) {
            if (isReadOnly()) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
                throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, msg);
            }
            if (prefix != null && prefix.length() != 0) {

                if (!CoreDocumentImpl.isXMLName(prefix)) {
                    String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_CHARACTER_ERR", null);
                    throw new DOMException(DOMException.INVALID_CHARACTER_ERR, msg);
                }
                if (namespaceURI == null || prefix.indexOf(':') >=0) {
                    String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NAMESPACE_ERR", null);
                    throw new DOMException(DOMException.NAMESPACE_ERR, msg);
               
                }
               if (prefix.equals("xmlns")) {
                    if (!namespaceURI.equals(xmlnsURI)){
                        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NAMESPACE_ERR", null);
                        throw new DOMException(DOMException.NAMESPACE_ERR, msg);
                    }
                } else if (prefix.equals("xml")) {
                    if (!namespaceURI.equals(xmlURI)) {
                        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NAMESPACE_ERR", null);
                        throw new DOMException(DOMException.NAMESPACE_ERR, msg);
                    }
                }else if (name.equals("xmlns")) {
                    String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NAMESPACE_ERR", null);
                    throw new DOMException(DOMException.NAMESPACE_ERR, msg);
                }
            } 
        }

        // update node name with new qualifiedName
        if (prefix !=null && prefix.length() != 0) {
            name = prefix + ":" + localName;
        }
        else {
            name = localName;
        }
    }
                                        
    /** 
     * Introduced in DOM Level 2. <p>
     *
     * Returns the local part of the qualified name of this node.
     * @since WD-DOM-Level-2-19990923
     */
    public String getLocalName()
    {
        if (needsSyncData()) {
            synchronizeData();
        }
        return localName;
    }
    
    
    /**
     * @see org.apache.xerces.dom3.TypeInfo#getTypeName()
     */
    public String getTypeName() {
        if (type !=null){
            if (type instanceof XSSimpleTypeDecl){
                return ((XSSimpleTypeDecl)type).getName();
            }
            return (String)type;
        }
        return null;
    }

    /**
     * @see org.apache.xerces.dom3.TypeInfo#getTypeNamespace()
     */
    public String getTypeNamespace() {
        if (type !=null){
            if (type instanceof XSSimpleTypeDecl){
                return ((XSSimpleTypeDecl)type).getNamespace();
            }
        }
        return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6822.java