error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2625.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2625.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2625.java
text:
```scala
p@@lace.getNodeName()+')');

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.xerces.util;

import java.util.Hashtable;

import org.apache.xerces.dom.AttrImpl;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.impl.xs.opti.ElementImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSException;

/**
 * Some useful utility methods.
 * This class was modified in Xerces2 with a view to abstracting as
 * much as possible away from the representation of the underlying
 * parsed structure (i.e., the DOM).  This was done so that, if Xerces
 * ever adopts an in-memory representation more efficient than the DOM
 * (such as a DTM), we should easily be able to convert our schema
 * parsing to utilize it.
 *
 * @version $Id$
 */
public class DOMUtil {
    
    //
    // Constructors
    //
    
    /** This class cannot be instantiated. */
    protected DOMUtil() {}
    
    //
    // Public static methods
    //
    
    /**
     * Copies the source tree into the specified place in a destination
     * tree. The source node and its children are appended as children
     * of the destination node.
     * <p>
     * <em>Note:</em> This is an iterative implementation.
     */
    public static void copyInto(Node src, Node dest) throws DOMException {
        
        // get node factory
        Document factory = dest.getOwnerDocument();
        boolean domimpl = factory instanceof DocumentImpl;
        
        // placement variables
        Node start  = src;
        Node parent = src;
        Node place  = src;
        
        // traverse source tree
        while (place != null) {
            
            // copy this node
            Node node = null;
            int  type = place.getNodeType();
            switch (type) {
            case Node.CDATA_SECTION_NODE: {
                node = factory.createCDATASection(place.getNodeValue());
                break;
            }
            case Node.COMMENT_NODE: {
                node = factory.createComment(place.getNodeValue());
                break;
            }
            case Node.ELEMENT_NODE: {
                Element element = factory.createElement(place.getNodeName());
                node = element;
                NamedNodeMap attrs  = place.getAttributes();
                int attrCount = attrs.getLength();
                for (int i = 0; i < attrCount; i++) {
                    Attr attr = (Attr)attrs.item(i);
                    String attrName = attr.getNodeName();
                    String attrValue = attr.getNodeValue();
                    element.setAttribute(attrName, attrValue);
                    if (domimpl && !attr.getSpecified()) {
                        ((AttrImpl)element.getAttributeNode(attrName)).setSpecified(false);
                    }
                }
                break;
            }
            case Node.ENTITY_REFERENCE_NODE: {
                node = factory.createEntityReference(place.getNodeName());
                break;
            }
            case Node.PROCESSING_INSTRUCTION_NODE: {
                node = factory.createProcessingInstruction(place.getNodeName(),
                        place.getNodeValue());
                break;
            }
            case Node.TEXT_NODE: {
                node = factory.createTextNode(place.getNodeValue());
                break;
            }
            default: {
                throw new IllegalArgumentException("can't copy node type, "+
                        type+" ("+
                        node.getNodeName()+')');
            }
            }
            dest.appendChild(node);
            
            // iterate over children
            if (place.hasChildNodes()) {
                parent = place;
                place  = place.getFirstChild();
                dest   = node;
            }
            
            // advance
            else {
                place = place.getNextSibling();
                while (place == null && parent != start) {
                    place  = parent.getNextSibling();
                    parent = parent.getParentNode();
                    dest   = dest.getParentNode();
                }
            }
            
        }
        
    } // copyInto(Node,Node)
    
    /** Finds and returns the first child element node. */
    public static Element getFirstChildElement(Node parent) {
        
        // search for node
        Node child = parent.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                return (Element)child;
            }
            child = child.getNextSibling();
        }
        
        // not found
        return null;
        
    } // getFirstChildElement(Node):Element
    
    /** Finds and returns the first visible child element node. */
    public static Element getFirstVisibleChildElement(Node parent) {
        
        // search for node
        Node child = parent.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE &&
                    !isHidden(child)) {
                return (Element)child;
            }
            child = child.getNextSibling();
        }
        
        // not found
        return null;
        
    } // getFirstChildElement(Node):Element
    
    /** Finds and returns the first visible child element node. */
    public static Element getFirstVisibleChildElement(Node parent, Hashtable hiddenNodes) {
        
        // search for node
        Node child = parent.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE &&
                    !isHidden(child, hiddenNodes)) {
                return (Element)child;
            }
            child = child.getNextSibling();
        }
        
        // not found
        return null;
        
    } // getFirstChildElement(Node):Element
    
    /** Finds and returns the last child element node. 
     *  Overload previous method for non-Xerces node impl.
     */
    public static Element getLastChildElement(Node parent) {
        
        // search for node
        Node child = parent.getLastChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                return (Element)child;
            }
            child = child.getPreviousSibling();
        }
        
        // not found
        return null;
        
    } // getLastChildElement(Node):Element
    
    /** Finds and returns the last visible child element node. */
    public static Element getLastVisibleChildElement(Node parent) {
        
        // search for node
        Node child = parent.getLastChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE &&
                    !isHidden(child)) {
                return (Element)child;
            }
            child = child.getPreviousSibling();
        }
        
        // not found
        return null;
        
    } // getLastChildElement(Node):Element
    
    /** Finds and returns the last visible child element node. 
     *  Overload previous method for non-Xerces node impl
     */
    public static Element getLastVisibleChildElement(Node parent, Hashtable hiddenNodes) {
        
        // search for node
        Node child = parent.getLastChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE &&
                    !isHidden(child, hiddenNodes)) {
                return (Element)child;
            }
            child = child.getPreviousSibling();
        }
        
        // not found
        return null;
        
    } // getLastChildElement(Node):Element
    /** Finds and returns the next sibling element node. */
    public static Element getNextSiblingElement(Node node) {
        
        // search for node
        Node sibling = node.getNextSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                return (Element)sibling;
            }
            sibling = sibling.getNextSibling();
        }
        
        // not found
        return null;
        
    } // getNextSiblingElement(Node):Element
    
    // get next visible (un-hidden) node.
    public static Element getNextVisibleSiblingElement(Node node) {
        
        // search for node
        Node sibling = node.getNextSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE &&
                    !isHidden(sibling)) {
                return (Element)sibling;
            }
            sibling = sibling.getNextSibling();
        }
        
        // not found
        return null;
        
    } // getNextSiblingdElement(Node):Element
    
    // get next visible (un-hidden) node, overload previous method for non Xerces node impl
    public static Element getNextVisibleSiblingElement(Node node, Hashtable hiddenNodes) {
        
        // search for node
        Node sibling = node.getNextSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE &&
                    !isHidden(sibling, hiddenNodes)) {
                return (Element)sibling;
            }
            sibling = sibling.getNextSibling();
        }
        
        // not found
        return null;
        
    } // getNextSiblingdElement(Node):Element
    
    // set this Node as being hidden
    public static void setHidden(Node node) {
        if (node instanceof org.apache.xerces.impl.xs.opti.NodeImpl)
            ((org.apache.xerces.impl.xs.opti.NodeImpl)node).setReadOnly(true, false);
        else if (node instanceof org.apache.xerces.dom.NodeImpl)
            ((org.apache.xerces.dom.NodeImpl)node).setReadOnly(true, false);
    } // setHidden(node):void

    // set this Node as being hidden, overloaded method
    public static void setHidden(Node node, Hashtable hiddenNodes) {
        if (node instanceof org.apache.xerces.impl.xs.opti.NodeImpl) {
            ((org.apache.xerces.impl.xs.opti.NodeImpl)node).setReadOnly(true, false);
        }
        else {
        	hiddenNodes.put(node, "");
        }
    } // setHidden(node):void
    
    // set this Node as being visible
    public static void setVisible(Node node) {
        if (node instanceof org.apache.xerces.impl.xs.opti.NodeImpl)
            ((org.apache.xerces.impl.xs.opti.NodeImpl)node).setReadOnly(false, false);
        else if (node instanceof org.apache.xerces.dom.NodeImpl)
            ((org.apache.xerces.dom.NodeImpl)node).setReadOnly(false, false);
    } // setVisible(node):void   

    // set this Node as being visible, overloaded method
    public static void setVisible(Node node, Hashtable hiddenNodes) {
        if (node instanceof org.apache.xerces.impl.xs.opti.NodeImpl) {
            ((org.apache.xerces.impl.xs.opti.NodeImpl)node).setReadOnly(false, false);   
        }
        else {
            hiddenNodes.remove(node);
        }
    } // setVisible(node):void
    
    // is this node hidden?
    public static boolean isHidden(Node node) {
        if (node instanceof org.apache.xerces.impl.xs.opti.NodeImpl)
            return ((org.apache.xerces.impl.xs.opti.NodeImpl)node).getReadOnly();
        else if (node instanceof org.apache.xerces.dom.NodeImpl)
            return ((org.apache.xerces.dom.NodeImpl)node).getReadOnly();
        return false;
    } // isHidden(Node):boolean

    // is this node hidden? overloaded method
    public static boolean isHidden(Node node, Hashtable hiddenNodes) {
        if (node instanceof org.apache.xerces.impl.xs.opti.NodeImpl) {
            return ((org.apache.xerces.impl.xs.opti.NodeImpl)node).getReadOnly();
        }
        else {
            return hiddenNodes.containsKey(node);
        }
    } // isHidden(Node):boolean
    
    /** Finds and returns the first child node with the given name. */
    public static Element getFirstChildElement(Node parent, String elemName) {
        
        // search for node
        Node child = parent.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if (child.getNodeName().equals(elemName)) {
                    return (Element)child;
                }
            }
            child = child.getNextSibling();
        }
        
        // not found
        return null;
        
    } // getFirstChildElement(Node,String):Element
    
    /** Finds and returns the last child node with the given name. */
    public static Element getLastChildElement(Node parent, String elemName) {
        
        // search for node
        Node child = parent.getLastChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if (child.getNodeName().equals(elemName)) {
                    return (Element)child;
                }
            }
            child = child.getPreviousSibling();
        }
        
        // not found
        return null;
        
    } // getLastChildElement(Node,String):Element
    
    /** Finds and returns the next sibling node with the given name. */
    public static Element getNextSiblingElement(Node node, String elemName) {
        
        // search for node
        Node sibling = node.getNextSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                if (sibling.getNodeName().equals(elemName)) {
                    return (Element)sibling;
                }
            }
            sibling = sibling.getNextSibling();
        }
        
        // not found
        return null;
        
    } // getNextSiblingdElement(Node,String):Element
    
    /** Finds and returns the first child node with the given qualified name. */
    public static Element getFirstChildElementNS(Node parent,
            String uri, String localpart) {
        
        // search for node
        Node child = parent.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                String childURI = child.getNamespaceURI();
                if (childURI != null && childURI.equals(uri) &&
                        child.getLocalName().equals(localpart)) {
                    return (Element)child;
                }
            }
            child = child.getNextSibling();
        }
        
        // not found
        return null;
        
    } // getFirstChildElementNS(Node,String,String):Element
    
    /** Finds and returns the last child node with the given qualified name. */
    public static Element getLastChildElementNS(Node parent,
            String uri, String localpart) {
        
        // search for node
        Node child = parent.getLastChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                String childURI = child.getNamespaceURI();
                if (childURI != null && childURI.equals(uri) &&
                        child.getLocalName().equals(localpart)) {
                    return (Element)child;
                }
            }
            child = child.getPreviousSibling();
        }
        
        // not found
        return null;
        
    } // getLastChildElementNS(Node,String,String):Element
    
    /** Finds and returns the next sibling node with the given qualified name. */
    public static Element getNextSiblingElementNS(Node node,
            String uri, String localpart) {
        
        // search for node
        Node sibling = node.getNextSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                String siblingURI = sibling.getNamespaceURI();
                if (siblingURI != null && siblingURI.equals(uri) &&
                        sibling.getLocalName().equals(localpart)) {
                    return (Element)sibling;
                }
            }
            sibling = sibling.getNextSibling();
        }
        
        // not found
        return null;
        
    } // getNextSiblingdElementNS(Node,String,String):Element
    
    /** Finds and returns the first child node with the given name. */
    public static Element getFirstChildElement(Node parent, String elemNames[]) {
        
        // search for node
        Node child = parent.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                for (int i = 0; i < elemNames.length; i++) {
                    if (child.getNodeName().equals(elemNames[i])) {
                        return (Element)child;
                    }
                }
            }
            child = child.getNextSibling();
        }
        
        // not found
        return null;
        
    } // getFirstChildElement(Node,String[]):Element
    
    /** Finds and returns the last child node with the given name. */
    public static Element getLastChildElement(Node parent, String elemNames[]) {
        
        // search for node
        Node child = parent.getLastChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                for (int i = 0; i < elemNames.length; i++) {
                    if (child.getNodeName().equals(elemNames[i])) {
                        return (Element)child;
                    }
                }
            }
            child = child.getPreviousSibling();
        }
        
        // not found
        return null;
        
    } // getLastChildElement(Node,String[]):Element
    
    /** Finds and returns the next sibling node with the given name. */
    public static Element getNextSiblingElement(Node node, String elemNames[]) {
        
        // search for node
        Node sibling = node.getNextSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                for (int i = 0; i < elemNames.length; i++) {
                    if (sibling.getNodeName().equals(elemNames[i])) {
                        return (Element)sibling;
                    }
                }
            }
            sibling = sibling.getNextSibling();
        }
        
        // not found
        return null;
        
    } // getNextSiblingdElement(Node,String[]):Element
    
    /** Finds and returns the first child node with the given qualified name. */
    public static Element getFirstChildElementNS(Node parent,
            String[][] elemNames) {
        
        // search for node
        Node child = parent.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                for (int i = 0; i < elemNames.length; i++) {
                    String uri = child.getNamespaceURI();
                    if (uri != null && uri.equals(elemNames[i][0]) &&
                            child.getLocalName().equals(elemNames[i][1])) {
                        return (Element)child;
                    }
                }
            }
            child = child.getNextSibling();
        }
        
        // not found
        return null;
        
    } // getFirstChildElementNS(Node,String[][]):Element
    
    /** Finds and returns the last child node with the given qualified name. */
    public static Element getLastChildElementNS(Node parent,
            String[][] elemNames) {
        
        // search for node
        Node child = parent.getLastChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                for (int i = 0; i < elemNames.length; i++) {
                    String uri = child.getNamespaceURI();
                    if (uri != null && uri.equals(elemNames[i][0]) &&
                            child.getLocalName().equals(elemNames[i][1])) {
                        return (Element)child;
                    }
                }
            }
            child = child.getPreviousSibling();
        }
        
        // not found
        return null;
        
    } // getLastChildElementNS(Node,String[][]):Element
    
    /** Finds and returns the next sibling node with the given qualified name. */
    public static Element getNextSiblingElementNS(Node node,
            String[][] elemNames) {
        
        // search for node
        Node sibling = node.getNextSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                for (int i = 0; i < elemNames.length; i++) {
                    String uri = sibling.getNamespaceURI();
                    if (uri != null && uri.equals(elemNames[i][0]) &&
                            sibling.getLocalName().equals(elemNames[i][1])) {
                        return (Element)sibling;
                    }
                }
            }
            sibling = sibling.getNextSibling();
        }
        
        // not found
        return null;
        
    } // getNextSiblingdElementNS(Node,String[][]):Element
    
    /**
     * Finds and returns the first child node with the given name and
     * attribute name, value pair.
     */
    public static Element getFirstChildElement(Node   parent,
            String elemName,
            String attrName,
            String attrValue) {
        
        // search for node
        Node child = parent.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)child;
                if (element.getNodeName().equals(elemName) &&
                        element.getAttribute(attrName).equals(attrValue)) {
                    return element;
                }
            }
            child = child.getNextSibling();
        }
        
        // not found
        return null;
        
    } // getFirstChildElement(Node,String,String,String):Element
    
    /**
     * Finds and returns the last child node with the given name and
     * attribute name, value pair.
     */
    public static Element getLastChildElement(Node   parent,
            String elemName,
            String attrName,
            String attrValue) {
        
        // search for node
        Node child = parent.getLastChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)child;
                if (element.getNodeName().equals(elemName) &&
                        element.getAttribute(attrName).equals(attrValue)) {
                    return element;
                }
            }
            child = child.getPreviousSibling();
        }
        
        // not found
        return null;
        
    } // getLastChildElement(Node,String,String,String):Element
    
    /**
     * Finds and returns the next sibling node with the given name and
     * attribute name, value pair. Since only elements have attributes,
     * the node returned will be of type Node.ELEMENT_NODE.
     */
    public static Element getNextSiblingElement(Node   node,
            String elemName,
            String attrName,
            String attrValue) {
        
        // search for node
        Node sibling = node.getNextSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)sibling;
                if (element.getNodeName().equals(elemName) &&
                        element.getAttribute(attrName).equals(attrValue)) {
                    return element;
                }
            }
            sibling = sibling.getNextSibling();
        }
        
        // not found
        return null;
        
    } // getNextSiblingElement(Node,String,String,String):Element
    
    /**
     * Returns the concatenated child text of the specified node.
     * This method only looks at the immediate children of type
     * <code>Node.TEXT_NODE</code> or the children of any child
     * node that is of type <code>Node.CDATA_SECTION_NODE</code>
     * for the concatenation.
     *
     * @param node The node to look at.
     */
    public static String getChildText(Node node) {
        
        // is there anything to do?
        if (node == null) {
            return null;
        }
        
        // concatenate children text
        StringBuffer str = new StringBuffer();
        Node child = node.getFirstChild();
        while (child != null) {
            short type = child.getNodeType();
            if (type == Node.TEXT_NODE) {
                str.append(child.getNodeValue());
            }
            else if (type == Node.CDATA_SECTION_NODE) {
                str.append(getChildText(child));
            }
            child = child.getNextSibling();
        }
        
        // return text value
        return str.toString();
        
    } // getChildText(Node):String
    
    // return the name of this element
    public static String getName(Node node) {
        return node.getNodeName();
    } // getLocalName(Element):  String
    
    /** returns local name of this element if not null, otherwise
     returns the name of the node
     */
    public static String getLocalName(Node node) {
        String name = node.getLocalName();
        return (name!=null)? name:node.getNodeName();
    } // getLocalName(Element):  String
    
    public static Element getParent(Element elem) {
        Node parent = elem.getParentNode();
        if (parent instanceof Element)
            return (Element)parent;
        return null;
    } // getParent(Element):Element
    
    // get the Document of which this Node is a part
    public static Document getDocument(Node node) {
        return node.getOwnerDocument();
    } // getDocument(Node):Document
    
    // return this Document's root node
    public static Element getRoot(Document doc) {
        return doc.getDocumentElement();
    } // getRoot(Document(:  Element
    
    // some methods for handling attributes:
    
    // return the right attribute node
    public static Attr getAttr(Element elem, String name) {
        return elem.getAttributeNode(name);
    } // getAttr(Element, String):Attr
    
    // return the right attribute node
    public static Attr getAttrNS(Element elem, String nsUri,
            String localName) {
        return elem.getAttributeNodeNS(nsUri, localName);
    } // getAttrNS(Element, String):Attr
    
    // get all the attributes for an Element
    public static Attr[] getAttrs(Element elem) {
        NamedNodeMap attrMap = elem.getAttributes();
        Attr [] attrArray = new Attr[attrMap.getLength()];
        for (int i=0; i<attrMap.getLength(); i++)
            attrArray[i] = (Attr)attrMap.item(i);
        return attrArray;
    } // getAttrs(Element):  Attr[]
    
    // get attribute's value
    public static String getValue(Attr attribute) {
        return attribute.getValue();
    } // getValue(Attr):String
    
    // It is noteworthy that, because of the way the DOM specs
    // work, the next two methods return the empty string (not
    // null!) when the attribute with the specified name does not
    // exist on an element.  Beware!
    
    // return the value of the attribute of the given element
    // with the given name
    public static String getAttrValue(Element elem, String name) {
        return elem.getAttribute(name);
    } // getAttr(Element, String):Attr
    
    // return the value of the attribute of the given element
    // with the given name
    public static String getAttrValueNS(Element elem, String nsUri,
            String localName) {
        return elem.getAttributeNS(nsUri, localName);
    } // getAttrValueNS(Element, String):Attr
    
    // return the prefix
    public static String getPrefix(Node node) {
        return node.getPrefix();
    }
    
    // return the namespace URI
    public static String getNamespaceURI(Node node) {
        return node.getNamespaceURI();
    }
    
    // return annotation
    public static String getAnnotation(Node node) {
        if (node instanceof ElementImpl) {
            return ((ElementImpl)node).getAnnotation();
        }
        return null;
    }
    
    // return synthetic annotation
    public static String getSyntheticAnnotation(Node node) {
        if (node instanceof ElementImpl) {
            return ((ElementImpl)node).getSyntheticAnnotation();
        }
        return null;
    }
    
    /**
     * Creates a DOMException. On J2SE 1.4 and above the cause for the exception will be set.
     */
    public static DOMException createDOMException(short code, Throwable cause) {
        DOMException de = new DOMException(code, cause != null ? cause.getMessage() : null);
        if (cause != null && ThrowableMethods.fgThrowableMethodsAvailable) {
            try {
                ThrowableMethods.fgThrowableInitCauseMethod.invoke(de, new Object [] {cause});
            }
            // Something went wrong. There's not much we can do about it.
            catch (Exception e) {}
        }
        return de;
    }
    
    /**
     * Creates an LSException. On J2SE 1.4 and above the cause for the exception will be set.
     */
    public static LSException createLSException(short code, Throwable cause) {
        LSException lse = new LSException(code, cause != null ? cause.getMessage() : null);
        if (cause != null && ThrowableMethods.fgThrowableMethodsAvailable) {
            try {
                ThrowableMethods.fgThrowableInitCauseMethod.invoke(lse, new Object [] {cause});
            }
            // Something went wrong. There's not much we can do about it.
            catch (Exception e) {}
        }
        return lse;
    }
    
    /**
     * Holder of methods from java.lang.Throwable.
     */
    static class ThrowableMethods {
        
        // Method: java.lang.Throwable.initCause(java.lang.Throwable)
        private static java.lang.reflect.Method fgThrowableInitCauseMethod = null;
        
        // Flag indicating whether or not Throwable methods available.
        private static boolean fgThrowableMethodsAvailable = false;
        
        private ThrowableMethods() {}
        
        // Attempt to get methods for java.lang.Throwable on class initialization.
        static {
            try {
                fgThrowableInitCauseMethod = Throwable.class.getMethod("initCause", new Class [] {Throwable.class});
                fgThrowableMethodsAvailable = true;
            }
            // ClassNotFoundException, NoSuchMethodException or SecurityException
            // Whatever the case, we cannot use java.lang.Throwable.initCause(java.lang.Throwable).
            catch (Exception exc) {
                fgThrowableInitCauseMethod = null;
                fgThrowableMethodsAvailable = false;
            }
        }
    }
    
} // class DOMUtil
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2625.java