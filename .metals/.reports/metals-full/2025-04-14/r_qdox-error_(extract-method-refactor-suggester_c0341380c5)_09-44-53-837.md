error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15536.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15536.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15536.java
text:
```scala
(@@(AttrNSImpl)newAttr).name= (prefix!=null)?(prefix+":"+localName):localName;

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

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import org.apache.xerces.util.URI;

/**
 * Elements represent most of the "markup" and structure of the
 * document.  They contain both the data for the element itself
 * (element name and attributes), and any contained nodes, including
 * document text (as children).
 * <P>
 * Elements may have Attributes associated with them; the API for this is
 * defined in Node, but the function is implemented here. In general, XML
 * applications should retrive Attributes as Nodes, since they may contain
 * entity references and hence be a fairly complex sub-tree. HTML users will
 * be dealing with simple string values, and convenience methods are provided
 * to work in terms of Strings.
 * <P>
 * ElementImpl does not support Namespaces. ElementNSImpl, which inherits from
 * it, does.
 * @see ElementNSImpl
 *
 * @author Arnaud  Le Hors, IBM
 * @author Joe Kesselman, IBM
 * @author Andy Clark, IBM
 * @author Ralf Pfeiffer, IBM
 * @version $Id$
 * @since  PR-DOM-Level-1-19980818.
 */
public class ElementImpl
    extends ParentNode
    implements Element {

    //
    // Constants
    //

    /** Serialization version. */
    static final long serialVersionUID = 3717253516652722278L;
    //
    // Data
    //

    /** Element name. */
    protected String name;

    /** Attributes. */
    protected AttributeMap attributes;

    //
    // Constructors
    //

    /** Factory constructor. */
    public ElementImpl(CoreDocumentImpl ownerDoc, String name) {
    	super(ownerDoc);
        this.name = name;
        needsSyncData(true);    // synchronizeData will initialize attributes
    }

    // for ElementNSImpl
    protected ElementImpl() {}
    
    // Support for DOM Level 3 renameNode method.
    // Note: This only deals with part of the pb. CoreDocumentImpl
    // does all the work.
    void rename(String name) {
        if (needsSyncData()) {
            synchronizeData();
        }
	    this.name = name;
        reconcileDefaultAttributes();
    }

    //
    // Node methods
    //

    
    /**
     * A short integer indicating what type of node this is. The named
     * constants for this value are defined in the org.w3c.dom.Node interface.
     */
    public short getNodeType() {
        return Node.ELEMENT_NODE;
    }

    /**
     * Returns the element name
     */
    public String getNodeName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return name;
    }

    /**
     * Retrieve all the Attributes as a set. Note that this API is inherited
     * from Node rather than specified on Element; in fact only Elements will
     * ever have Attributes, but they want to allow folks to "blindly" operate
     * on the tree as a set of Nodes.
     */
    public NamedNodeMap getAttributes() {

        if (needsSyncData()) {
            synchronizeData();
        }
        if (attributes == null) {
            attributes = new AttributeMap(this, null);
        }
        return attributes;

    } // getAttributes():NamedNodeMap

    /**
     * Return a duplicate copy of this Element. Note that its children
     * will not be copied unless the "deep" flag is true, but Attributes
     * are <i>always</i> replicated.
     *
     * @see org.w3c.dom.Node#cloneNode(boolean)
     */
    public Node cloneNode(boolean deep) {

    	ElementImpl newnode = (ElementImpl) super.cloneNode(deep);
    	// Replicate NamedNodeMap rather than sharing it.
        if (attributes != null) {
            newnode.attributes = (AttributeMap) attributes.cloneMap(newnode);
        }
    	return newnode;

    } // cloneNode(boolean):Node

   /**
     * DOM Level 3 WD - Experimental.
     * Retrieve baseURI
     */
    public String getBaseURI() {

        if (needsSyncData()) {
            synchronizeData();
        }
        String baseURI = this.ownerNode.getBaseURI();
        if (attributes != null) {
            Attr attrNode = (Attr)attributes.getNamedItem("xml:base");
            if (attrNode != null) {
                String uri =  attrNode.getNodeValue();
                if (uri.length() != 0 ) {// attribute value is always empty string
                    try {
                       uri = new URI(baseURI, uri).toString();  
                    } 
                    catch (org.apache.xerces.util.URI.MalformedURIException e){
                        // REVISIT: what should happen in this case?
                        return null;
                    }
                    return uri;
                }
            }
        }
        return baseURI;
    }



    /**
     * NON-DOM
     * set the ownerDocument of this node, its children, and its attributes
     */
    void setOwnerDocument(CoreDocumentImpl doc) {
	super.setOwnerDocument(doc);
        if (attributes != null) {
            attributes.setOwnerDocument(doc);
        }
    }

    //
    // Element methods
    //

    /**
     * Look up a single Attribute by name. Returns the Attribute's
     * string value, or an empty string (NOT null!) to indicate that the
     * name did not map to a currently defined attribute.
     * <p>
     * Note: Attributes may contain complex node trees. This method
     * returns the "flattened" string obtained from Attribute.getValue().
     * If you need the structure information, see getAttributeNode().
     */
    public String getAttribute(String name) {

        if (needsSyncData()) {
            synchronizeData();
        }
        if (attributes == null) {
            return "";
        }
        Attr attr = (Attr)(attributes.getNamedItem(name));
        return (attr == null) ? "" : attr.getValue();

    } // getAttribute(String):String


    /**
     * Look up a single Attribute by name. Returns the Attribute Node,
     * so its complete child tree is available. This could be important in
     * XML, where the string rendering may not be sufficient information.
     * <p>
     * If no matching attribute is available, returns null.
     */
    public Attr getAttributeNode(String name) {

        if (needsSyncData()) {
            synchronizeData();
        }
        if (attributes == null) {
            return null;
        }
        return (Attr)attributes.getNamedItem(name);

    } // getAttributeNode(String):Attr
    

    /**
     * Returns a NodeList of all descendent nodes (children,
     * grandchildren, and so on) which are Elements and which have the
     * specified tag name.
     * <p>
     * Note: NodeList is a "live" view of the DOM. Its contents will
     * change as the DOM changes, and alterations made to the NodeList
     * will be reflected in the DOM.
     *
     * @param tagname The type of element to gather. To obtain a list of
     * all elements no matter what their names, use the wild-card tag
     * name "*".
     *
     * @see DeepNodeListImpl
     */
    public NodeList getElementsByTagName(String tagname) {
    	return new DeepNodeListImpl(this,tagname);
    }

    /**
     * Returns the name of the Element. Note that Element.nodeName() is
     * defined to also return the tag name.
     * <p>
     * This is case-preserving in XML. HTML should uppercasify it on the
     * way in.
     */
    public String getTagName() {
        if (needsSyncData()) {
            synchronizeData();
        }
    	return name;
    }

    /**
     * In "normal form" (as read from a source file), there will never be two
     * Text children in succession. But DOM users may create successive Text
     * nodes in the course of manipulating the document. Normalize walks the
     * sub-tree and merges adjacent Texts, as if the DOM had been written out
     * and read back in again. This simplifies implementation of higher-level
     * functions that may want to assume that the document is in standard form.
     * <p>
     * To normalize a Document, normalize its top-level Element child.
     * <p>
     * As of PR-DOM-Level-1-19980818, CDATA -- despite being a subclass of
     * Text -- is considered "markup" and will _not_ be merged either with
     * normal Text or with other CDATASections.
     */
    public void normalize() {
        // No need to normalize if already normalized.
        if (isNormalized()) {
            return;
        }
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        ChildNode kid, next;
        for (kid = firstChild; kid != null; kid = next) {
            next = kid.nextSibling;

            // If kid is a text node, we need to check for one of two
            // conditions:
            //   1) There is an adjacent text node
            //   2) There is no adjacent text node, but kid is
            //      an empty text node.
            if ( kid.getNodeType() == Node.TEXT_NODE )
            {
                // If an adjacent text node, merge it with kid
                if ( next!=null && next.getNodeType() == Node.TEXT_NODE )
                {
                    ((Text)kid).appendData(next.getNodeValue());
                    removeChild( next );
                    next = kid; // Don't advance; there might be another.
                }
                else
                {
                    // If kid is empty, remove it
                    if ( kid.getNodeValue().length()==0 )
                        removeChild( kid );
                }
            }

            // Otherwise it might be an Element, which is handled recursively
            else if (kid.getNodeType() == Node.ELEMENT_NODE) {
                kid.normalize();
            }
        }

        // We must also normalize all of the attributes
        if ( attributes!=null )
        {
            for( int i=0; i<attributes.getLength(); ++i )
            {
                Node attr = attributes.item(i);
                attr.normalize();
            }
        }

    	// changed() will have occurred when the removeChild() was done,
    	// so does not have to be reissued.

        isNormalized(true);
    } // normalize()

    /**
     * Remove the named attribute from this Element. If the removed
     * Attribute has a default value, it is immediately replaced thereby.
     * <P>
     * The default logic is actually implemented in NamedNodeMapImpl.
     * PR-DOM-Level-1-19980818 doesn't fully address the DTD, so some
     * of this behavior is likely to change in future versions. ?????
     * <P>
     * Note that this call "succeeds" even if no attribute by this name
     * existed -- unlike removeAttributeNode, which will throw a not-found
     * exception in that case.
     *	
     * @throws DOMException(NO_MODIFICATION_ALLOWED_ERR) if the node is
     * readonly.
     */
    public void removeAttribute(String name) {

    	if (ownerDocument.errorChecking && isReadOnly()) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
            throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, msg);
        }
    		
        if (needsSyncData()) {
            synchronizeData();
        }

        if (attributes == null) {
            return;
        }

        attributes.safeRemoveNamedItem(name);

    } // removeAttribute(String)

  
    /**
     * Remove the specified attribute/value pair. If the removed
     * Attribute has a default value, it is immediately replaced.
     * <p>
     * NOTE: Specifically removes THIS NODE -- not the node with this
     * name, nor the node with these contents. If the specific Attribute
     * object passed in is not stored in this Element, we throw a
     * DOMException.  If you really want to remove an attribute by name,
     * use removeAttribute().
     *
     * @return the Attribute object that was removed.
     * @throws DOMException(NOT_FOUND_ERR) if oldattr is not an attribute of
     * this Element.
     * @throws DOMException(NO_MODIFICATION_ALLOWED_ERR) if the node is
     * readonly.
     */
    public Attr removeAttributeNode(Attr oldAttr)
        throws DOMException {

    	if (ownerDocument.errorChecking && isReadOnly()) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
            throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, msg);
        }
    		
        if (needsSyncData()) {
            synchronizeData();
        }

        if (attributes == null) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null);
            throw new DOMException(DOMException.NOT_FOUND_ERR, msg);
        }
        return (Attr) attributes.removeItem(oldAttr, true);

    } // removeAttributeNode(Attr):Attr

   
    /**
     * Add a new name/value pair, or replace the value of the existing
     * attribute having that name.
     *
     * Note: this method supports only the simplest kind of Attribute,
     * one whose value is a string contained in a single Text node.
     * If you want to assert a more complex value (which XML permits,
     * though HTML doesn't), see setAttributeNode().
     *
     * The attribute is created with specified=true, meaning it's an
     * explicit value rather than inherited from the DTD as a default.
     * Again, setAttributeNode can be used to achieve other results.
     *
     * @throws DOMException(INVALID_NAME_ERR) if the name is not acceptable.
     * (Attribute factory will do that test for us.)
     *
     * @throws DOMException(NO_MODIFICATION_ALLOWED_ERR) if the node is
     * readonly.
     */
    public void setAttribute(String name, String value) {

    	if (ownerDocument.errorChecking && isReadOnly()) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
            throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, msg);
        }

        if (needsSyncData()) {
            synchronizeData();
        }

    	Attr newAttr = getAttributeNode(name);
        if (newAttr == null) {
            newAttr = getOwnerDocument().createAttribute(name);

            if (attributes == null) {
                attributes = new AttributeMap(this, null);
            }

	    newAttr.setNodeValue(value);
            attributes.setNamedItem(newAttr);
        }
	else {
	    newAttr.setNodeValue(value);
	}

    } // setAttribute(String,String)
 
    /**
     * Add a new attribute/value pair, or replace the value of the
     * existing attribute with that name.
     * <P>
     * This method allows you to add an Attribute that has already been
     * constructed, and hence avoids the limitations of the simple
     * setAttribute() call. It can handle attribute values that have
     * arbitrarily complex tree structure -- in particular, those which
     * had entity references mixed into their text.
     *
     * @throws DOMException(INUSE_ATTRIBUTE_ERR) if the Attribute object
     * has already been assigned to another Element.
     */
    public Attr setAttributeNode(Attr newAttr)
        throws DOMException
        {

        if (needsSyncData()) {
            synchronizeData();
        }

    	if (ownerDocument.errorChecking) {
            if (isReadOnly()) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
                throw new DOMException(
                                     DOMException.NO_MODIFICATION_ALLOWED_ERR, 
                                     msg);
            }
    	
            if (newAttr.getOwnerDocument() != ownerDocument) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null);
    		    throw new DOMException(DOMException.WRONG_DOCUMENT_ERR, msg);
            }
        }

        if (attributes == null) {
            attributes = new AttributeMap(this, null);
        }
    	// This will throw INUSE if necessary
    	return (Attr) attributes.setNamedItem(newAttr);

    } // setAttributeNode(Attr):Attr
    
    //
    // DOM2: Namespace methods
    //

    /**
     * Introduced in DOM Level 2. <p>
     *
     * Retrieves an attribute value by local name and namespace URI. 
     *
     * @param namespaceURI
     *                      The namespace URI of the attribute to
     *                      retrieve.
     * @param localName     The local name of the attribute to retrieve.
     * @return String       The Attr value as a string, or empty string
     *                      if that attribute
     *                      does not have a specified or default value.
     * @since WD-DOM-Level-2-19990923
     */
    public String getAttributeNS(String namespaceURI, String localName) {

        if (needsSyncData()) {
            synchronizeData();
        }

        if (attributes == null) {
            return "";
        }

        Attr attr = (Attr)(attributes.getNamedItemNS(namespaceURI, localName));
        return (attr == null) ? "" : attr.getValue();

    } // getAttributeNS(String,String):String
    
    /**
     * Introduced in DOM Level 2. <p>
     *
     *  Adds a new attribute.
     *  If the given namespaceURI is null or an empty string and the
     *  qualifiedName has a prefix that is "xml", the new attribute is bound to
     *  the predefined namespace "http://www.w3.org/XML/1998/namespace"
     *  [Namespaces].  If an attribute with the same local name and namespace
     *  URI is already present on the element, its prefix is changed to be the
     *  prefix part of the qualifiedName, and its value is changed to be the
     *  value parameter. This value is a simple string, it is not parsed as it
     *  is being set. So any markup (such as syntax to be recognized as an
     *  entity reference) is treated as literal text, and needs to be
     *  appropriately escaped by the implementation when it is written out. In
     *  order to assign an attribute value that contains entity references, the
     *  user must create an Attr node plus any Text and EntityReference nodes,
     *  build the appropriate subtree, and use setAttributeNodeNS or
     *  setAttributeNode to assign it as the value of an attribute.
     *
     * @param namespaceURI      The namespace URI of the attribute to create
     *                          or alter. 
     * @param qualifiedName     The qualified name of the attribute to create or
     *                          alter.
     * @param value             The value to set in string form.
     * @throws                  INVALID_CHARACTER_ERR: Raised if the specified
     *                          name contains an invalid character.
     *
     * @throws                  NO_MODIFICATION_ALLOWED_ERR: Raised if this
     *                          node is readonly.
     *
     * @throws                  NAMESPACE_ERR: Raised if the qualifiedName
     *                          has a prefix that is "xml" and the namespaceURI
     *                          is neither null nor an empty string nor
     *                          "http://www.w3.org/XML/1998/namespace", or if
     *                          the qualifiedName has a prefix that is "xmlns"
     *                          but the namespaceURI is neither null nor an
     *                          empty string, or if if the qualifiedName has a
     *                          prefix different from "xml" and "xmlns" and the
     *                          namespaceURI is null or an empty string.
     * @since WD-DOM-Level-2-19990923
     */
     public void setAttributeNS(String namespaceURI,String qualifiedName,
		                          String value) {
		if (ownerDocument.errorChecking && isReadOnly()) {
			String msg =
				DOMMessageFormatter.formatMessage(
					DOMMessageFormatter.DOM_DOMAIN,
					"NO_MODIFICATION_ALLOWED_ERR",
					null);
			throw new DOMException(
				DOMException.NO_MODIFICATION_ALLOWED_ERR,
				msg);
		}
		if (needsSyncData()) {
			synchronizeData();
		}
		int index = qualifiedName.indexOf(':');
		String prefix, localName;
		if (index < 0) {
			prefix = null;
			localName = qualifiedName;
		}
		else {
			prefix = qualifiedName.substring(0, index);
			localName = qualifiedName.substring(index + 1);
		}
		Attr newAttr = getAttributeNodeNS(namespaceURI, localName);
		if (newAttr == null) {
            // REVISIT: this is not efficient, we are creating twice the same
            //          strings for prefix and localName.
			newAttr = getOwnerDocument().createAttributeNS(
					namespaceURI,
					qualifiedName);
			if (attributes == null) {
				attributes = new AttributeMap(this, null);
			}
			newAttr.setNodeValue(value);
			attributes.setNamedItemNS(newAttr);
		}
		else {
            // change prefix and value
            ((AttrNSImpl)newAttr).name= prefix+":"+localName;
			newAttr.setNodeValue(value);
		}

    } // setAttributeNS(String,String,String)
    
    /**
     * Introduced in DOM Level 2. <p>
     *
     * Removes an attribute by local name and namespace URI. If the removed
     * attribute has a default value it is immediately replaced.
     * The replacing attribute has the same namespace URI and local name, 
     * as well as the original prefix.<p>
     *
     * @param namespaceURI  The namespace URI of the attribute to remove.
     *                      
     * @param localName     The local name of the attribute to remove.
     * @throws                  NO_MODIFICATION_ALLOWED_ERR: Raised if this
     *                          node is readonly.
     * @since WD-DOM-Level-2-19990923
     */
    public void removeAttributeNS(String namespaceURI, String localName) {

    	if (ownerDocument.errorChecking && isReadOnly()) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
            throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, msg);
        }
    		
        if (needsSyncData()) {
            synchronizeData();
        }

        if (attributes == null) {
            return;
        }

        attributes.safeRemoveNamedItemNS(namespaceURI, localName);

    } // removeAttributeNS(String,String)
    
    /**
     * Retrieves an Attr node by local name and namespace URI. 
     *
     * @param namespaceURI  The namespace URI of the attribute to
     *                      retrieve. 
     * @param localName     The local name of the attribute to retrieve.
     * @return Attr         The Attr node with the specified attribute 
     *                      local name and namespace
     *                      URI or null if there is no such attribute.
     * @since WD-DOM-Level-2-19990923
     */
    public Attr getAttributeNodeNS(String namespaceURI, String localName){

        if (needsSyncData()) {
            synchronizeData();
        }
        if (attributes == null) {
            return null;
        }
        return (Attr)attributes.getNamedItemNS(namespaceURI, localName);

    } // getAttributeNodeNS(String,String):Attr
 
    /**
     * Introduced in DOM Level 2. <p>
     *
     * Adds a new attribute. If an attribute with that local name and 
     * namespace URI is already present in the element, it is replaced 
     * by the new one.
     *
     * @param Attr      The Attr node to add to the attribute list. When 
     *                  the Node has no namespaceURI, this method behaves 
     *                  like setAttributeNode.
     * @return Attr     If the newAttr attribute replaces an existing attribute
     *                  with the same local name and namespace URI, the *
     *                  previously existing Attr node is returned, otherwise
     *                  null is returned.
     * @throws          WRONG_DOCUMENT_ERR: Raised if newAttr
     *                  was created from a different document than the one that
     *                  created the element.
     *
     * @throws          NO_MODIFICATION_ALLOWED_ERR: Raised if
     *                  this node is readonly.
     *
     * @throws          INUSE_ATTRIBUTE_ERR: Raised if newAttr is
     *                  already an attribute of another Element object. The
     *                  DOM user must explicitly clone Attr nodes to re-use
     *                  them in other elements.
     * @since WD-DOM-Level-2-19990923
     */
    public Attr setAttributeNodeNS(Attr newAttr)
        throws DOMException
        {

        if (needsSyncData()) {
            synchronizeData();
        }
        if (ownerDocument.errorChecking) {
            if (isReadOnly()) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
    		    throw new DOMException(
                                     DOMException.NO_MODIFICATION_ALLOWED_ERR, 
                                     msg);
            }
            if (newAttr.getOwnerDocument() != ownerDocument) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null);
                throw new DOMException(DOMException.WRONG_DOCUMENT_ERR, msg);
            }
        }

        if (attributes == null) {
            attributes = new AttributeMap(this, null);
        }
    	// This will throw INUSE if necessary
    	return (Attr) attributes.setNamedItemNS(newAttr);

    } // setAttributeNodeNS(Attr):Attr

    /**
      * NON-DOM: sets attribute node for this element
      */
    protected int setXercesAttributeNode (Attr attr){

        if (attributes == null) {
            attributes = new AttributeMap(this, null);
        }
        return attributes.addItem(attr);

    }
    
    /**
     * Introduced in DOM Level 2.
     */
    public boolean hasAttributes() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return (attributes != null && attributes.getLength() != 0);
    }

    /**
     * Introduced in DOM Level 2.
     */
    public boolean hasAttribute(String name) {
        return getAttributeNode(name) != null;
    }

    /**
     * Introduced in DOM Level 2.
     */
    public boolean hasAttributeNS(String namespaceURI, String localName) {
        return getAttributeNodeNS(namespaceURI, localName) != null;
    }

    /**
     * Introduced in DOM Level 2. <p>
     *
     * Returns a NodeList of all the Elements with a given local name and
     * namespace URI in the order in which they would be encountered in a
     * preorder traversal of the Document tree, starting from this node.
     *
     * @param namespaceURI The namespace URI of the elements to match
     *                     on. The special value "*" matches all
     *                     namespaces. When it is null or an empty
     *                     string, this method behaves like
     *                     getElementsByTagName.
     * @param localName    The local name of the elements to match on.
     *                     The special value "*" matches all local names.
     * @return NodeList    A new NodeList object containing all the matched
     *                     Elements.
     * @since WD-DOM-Level-2-19990923
     */
    public NodeList getElementsByTagNameNS(String namespaceURI,
                                           String localName) {
    	return new DeepNodeListImpl(this, namespaceURI, localName);
    }

    /**
     * DOM Level 3 WD- Experimental.
     * Override inherited behavior from NodeImpl and ParentNode to check on
     * attributes
     */
    public boolean isEqualNode(Node arg) {
        if (!super.isEqualNode(arg)) {
            return false;
        }
        boolean hasAttrs = hasAttributes();
        if (hasAttrs != ((Element) arg).hasAttributes()) {
            return false;
        }
        if (hasAttrs) {
            NamedNodeMap map1 = getAttributes();
            NamedNodeMap map2 = ((Element) arg).getAttributes();
            int len = map1.getLength();
            if (len != map2.getLength()) {
                return false;
            }
            for (int i = 0; i < len; i++) {
                Node n1 = map1.item(i);
                if (n1.getLocalName() == null) { // DOM Level 1 Node
                    Node n2 = map2.getNamedItem(n1.getNodeName());
                    if (n2 == null || !((NodeImpl) n1).isEqualNode(n2)) {
                        return false;
                    }
                }
                else {
                    Node n2 = map2.getNamedItemNS(n1.getNamespaceURI(),
                                                  n1.getLocalName());
                    if (n2 == null || !((NodeImpl) n1).isEqualNode(n2)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //
    // Public methods
    //

    /**
     * NON-DOM: Subclassed to flip the attributes' readonly switch as well.
     * @see NodeImpl#setReadOnly
     */
    public void setReadOnly(boolean readOnly, boolean deep) {
    	super.setReadOnly(readOnly,deep);
        if (attributes != null) {
            attributes.setReadOnly(readOnly,true);
        }
    }

    /**
     * NON-DOM: register the given attribute node as an ID attribute
     */
    public void setIdAttributeNode(Attr at) {
        if (needsSyncData()) {
            synchronizeData();
        }
    	if (ownerDocument.errorChecking) {
            if (isReadOnly()) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
                throw new DOMException(
                                     DOMException.NO_MODIFICATION_ALLOWED_ERR, 
                                     msg);
            }
    	
            if (at.getOwnerElement() != this) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null);
                throw new DOMException(DOMException.NOT_FOUND_ERR, msg);
            }
        }
        ((AttrImpl) at).isIdAttribute(true);
        ownerDocument.putIdentifier(at.getValue(), this);
    }


    //
    // Protected methods
    //

    /** Synchronizes the data (name and value) for fast nodes. */
    protected void synchronizeData() {

        // no need to sync in the future
        needsSyncData(false);

        // we don't want to generate any event for this so turn them off
        boolean orig = ownerDocument.getMutationEvents();
        ownerDocument.setMutationEvents(false);

        // attributes
        setupDefaultAttributes();

        // set mutation events flag back to its original value
        ownerDocument.setMutationEvents(orig);

    } // synchronizeData()

    // support for DOM Level 3 renameNode method
    // @param el The element from which to take the attributes
    void moveSpecifiedAttributes(ElementImpl el) {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (el.hasAttributes()) {
            if (attributes == null) {
                attributes = new AttributeMap(this, null);
            }
            attributes.moveSpecifiedAttributes(el.attributes);
        }
    }

    /** Setup the default attributes. */
    protected void setupDefaultAttributes() {
        NamedNodeMapImpl defaults = getDefaultAttributes();
        if (defaults != null) {
            attributes = new AttributeMap(this, defaults);
        }
    }

    /** Reconcile default attributes. */
    protected void reconcileDefaultAttributes() {
        if (attributes != null) {
            NamedNodeMapImpl defaults = getDefaultAttributes();
            attributes.reconcileDefaults(defaults);
        }
    }

    /** Get the default attributes. */
    protected NamedNodeMapImpl getDefaultAttributes() {

    	DocumentTypeImpl doctype =
            (DocumentTypeImpl) ownerDocument.getDoctype();
    	if (doctype == null) {
            return null;
        }
        ElementDefinitionImpl eldef =
            (ElementDefinitionImpl)doctype.getElements()
                                               .getNamedItem(getNodeName());
        if (eldef == null) {
            return null;
        }
        return (NamedNodeMapImpl) eldef.getAttributes();

    } // getDefaultAttributes()

} // class ElementImpl
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15536.java