error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7882.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7882.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7882.java
text:
```scala
i@@f (prevSibling != null && next != null && prevSibling.getNodeType() == Node.TEXT_NODE &&

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


import java.util.Vector;

import org.apache.xerces.dom3.DOMError;
import org.apache.xerces.dom3.DOMErrorHandler;
import org.apache.xerces.impl.Constants;
import org.apache.xerces.impl.RevalidationHandler;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.xs.XSTypeDefinition;
import org.apache.xerces.impl.xs.util.SimpleLocator;
import org.apache.xerces.util.AugmentationsImpl;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLDocumentSource;
import org.apache.xerces.xs.AttributePSVI;
import org.apache.xerces.xs.ElementPSVI;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.ProcessingInstruction;
import org.apache.xerces.util.XML11Char;
import org.apache.xerces.util.XMLChar;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Comment;
/**
 * This class adds implementation for normalizeDocument method.
 * It acts as if the document was going through a save and load cycle, putting
 * the document in a "normal" form. The actual result depends on the features being set
 * and governing what operations actually take place. See setNormalizationFeature for details.
 * Noticeably this method normalizes Text nodes, makes the document "namespace wellformed",
 * according to the algorithm described below in pseudo code, by adding missing namespace
 * declaration attributes and adding or changing namespace prefixes, updates the replacement
 * tree of EntityReference nodes, normalizes attribute values, etc.
 * Mutation events, when supported, are generated to reflect the changes occuring on the
 * document.
 * See Namespace normalization for details on how namespace declaration attributes and prefixes
 * are normalized.
 * 
 * NOTE: There is an initial support for DOM revalidation with XML Schema as a grammar.
 * The tree might not be validated correctly if entityReferences, CDATA sections are
 * present in the tree. The PSVI information is not exposed, normalized data (including element
 * default content is not available).
 *
 * NOTE: the implementation is experimental and methods, functionality
 *       can be modified or removed in the future.
 * 
 * @author Elena Litani, IBM
 * @author Neeraj Bajaj, Sun Microsystems, inc.
 * @version $Id$
 */
public class DOMNormalizer implements XMLDocumentHandler {

    //
    // constants
    //
    /** Debug normalize document*/
    protected final static boolean DEBUG_ND = false;
    /** Debug namespace fix up algorithm*/
    protected final static boolean DEBUG = false;
    /** Debug document handler events */
    protected final static boolean DEBUG_EVENTS = false;

    /** prefix added by namespace fixup algorithm should follow a pattern "NS" + index*/
    protected final static String PREFIX = "NS";

    //
    // Data
    //
    protected DOMConfigurationImpl fConfiguration = null;
    protected CoreDocumentImpl fDocument = null;
    protected final XMLAttributesProxy fAttrProxy = new XMLAttributesProxy();
    protected final QName fQName = new QName();

    /** Validation handler represents validator instance. */
    protected RevalidationHandler fValidationHandler;

    /** symbol table */
    protected SymbolTable fSymbolTable;
    /** error handler. may be null. */
    protected DOMErrorHandler fErrorHandler;
    
    /**
     * Cached {@link DOMError} impl.
     * The same object is re-used to report multiple errors.
     */
    private final DOMErrorImpl fError = new DOMErrorImpl();
    
    // Validation against namespace aware grammar
    protected boolean fNamespaceValidation = false;

    // Update PSVI information in the tree
    protected boolean fPSVI = false;

    /** The namespace context of this document: stores namespaces in scope */
    protected final NamespaceContext fNamespaceContext = new NamespaceSupport();

    /** Stores all namespace bindings on the current element */
    protected final NamespaceContext fLocalNSBinder = new NamespaceSupport();

    /** list of attributes */
    protected final Vector fAttributeList = new Vector(5,10);


    /** DOM Locator -  for namespace fixup algorithm */
    protected final DOMLocatorImpl fLocator = new DOMLocatorImpl();

    /** for setting the PSVI */
    protected Node fCurrentNode = null;
    private QName fAttrQName = new QName();
    
    // attribute value normalization
    final XMLString fNormalizedValue = new XMLString(new char[16], 0, 0);
    
    /**
     * If the user stops the process, this exception will be thrown.
     */
    public static final RuntimeException abort = new RuntimeException();
    
    // Constructor
    // 

    public DOMNormalizer(){}



    /**
     * Normalizes document.
     * Note: reset() must be called before this method.
     */
	protected void normalizeDocument(CoreDocumentImpl document, DOMConfigurationImpl config) {

		fDocument = document;
		fConfiguration = config;

		// intialize and reset DOMNormalizer component
		// 
		fSymbolTable = (SymbolTable) fConfiguration.getProperty(DOMConfigurationImpl.SYMBOL_TABLE);
		// reset namespace context
		fNamespaceContext.reset();
		fNamespaceContext.declarePrefix(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);

		if ((fConfiguration.features & DOMConfigurationImpl.VALIDATE) != 0) {
			fValidationHandler =
				CoreDOMImplementationImpl.singleton.getValidator(XMLGrammarDescription.XML_SCHEMA);
			fConfiguration.setFeature(DOMConfigurationImpl.XERCES_VALIDATION, true);
			fConfiguration.setFeature(DOMConfigurationImpl.SCHEMA, true);
			// report fatal error on DOM Level 1 nodes
			fNamespaceValidation = true;
            
			// check if we need to fill in PSVI
            fPSVI = ((fConfiguration.features & DOMConfigurationImpl.PSVI) !=0)?true:false;
            
            // reset ID table           
            fDocument.clearIdentifiers();
            
            // reset schema validator
			((XMLComponent) fValidationHandler).reset(fConfiguration);
		}

		fErrorHandler = (DOMErrorHandler) fConfiguration.getParameter(Constants.DOM_ERROR_HANDLER);
		if (fValidationHandler != null) {
			fValidationHandler.setDocumentHandler(this);
			fValidationHandler.startDocument(
                    new SimpleLocator(fDocument.fDocumentURI, fDocument.fDocumentURI,
						-1, -1 ), fDocument.encoding, fNamespaceContext, null);

		}
		try {
			Node kid, next;
			for (kid = fDocument.getFirstChild(); kid != null; kid = next) {
				next = kid.getNextSibling();
				kid = normalizeNode(kid);
				if (kid != null) { // don't advance
					next = kid;
				}
			}

			// release resources
			if (fValidationHandler != null) {
				fValidationHandler.endDocument(null);
				CoreDOMImplementationImpl.singleton.releaseValidator(
					XMLGrammarDescription.XML_SCHEMA, fValidationHandler);
				fValidationHandler = null;
			}
		}
		catch (RuntimeException e) {
            if( e==abort )
                return; // processing aborted by the user
            throw e;    // otherwise re-throw.
		}

	}


    /**
     * 
     * This method acts as if the document was going through a save
     * and load cycle, putting the document in a "normal" form. The actual result
     * depends on the features being set and governing what operations actually
     * take place. See setNormalizationFeature for details. Noticeably this method
     * normalizes Text nodes, makes the document "namespace wellformed",
     * according to the algorithm described below in pseudo code, by adding missing
     * namespace declaration attributes and adding or changing namespace prefixes, updates
     * the replacement tree of EntityReference nodes,normalizes attribute values, etc.
     * 
     * @param node   Modified node or null. If node is returned, we need
     *               to normalize again starting on the node returned.
     * @return  the normalized Node
     */
    protected Node normalizeNode (Node node){

        int type = node.getNodeType();
        boolean wellformed;

        switch (type) {
        case Node.DOCUMENT_TYPE_NODE: {
                if (DEBUG_ND) {
                    System.out.println("==>normalizeNode:{doctype}");
                }
                //REVISIT: well-formness encoding info
                break;
            }

        case Node.ELEMENT_NODE: {  
                if (DEBUG_ND) {
                    System.out.println("==>normalizeNode:{element} "+node.getNodeName());
                }
                
                //do the name check only when version of the document was changed &
                //application has set the value of well-formed features to true
                if ( ((fConfiguration.features & DOMConfigurationImpl.WELLFORMED) != 0) && 
                    fDocument.isXMLVersionChanged()){
                    if(fNamespaceValidation){
                        wellformed = CoreDocumentImpl.isValidQName(node.getPrefix() , node.getLocalName(),  fDocument.isXML11Version()) ;
                    }
                    else{
                        wellformed = CoreDocumentImpl.isXMLName(node.getNodeName() , fDocument.isXML11Version());
                    }
                    if (!wellformed){
				            String msg = DOMMessageFormatter.formatMessage(
				                DOMMessageFormatter.DOM_DOMAIN, 
				                "wf-invalid-character-in-node-name", 
				                new Object[]{"Element", node.getNodeName()});
                            reportDOMError(fErrorHandler, fError, fLocator, msg, DOMError.SEVERITY_ERROR, node, 
                                "wf-invalid-character-in-node-name");                       
                    }
                }
                // push namespace context
                fNamespaceContext.pushContext();
                fLocalNSBinder.reset();

                ElementImpl elem = (ElementImpl)node;
                if (elem.needsSyncChildren()) {
                    elem.synchronizeChildren();
                }
                AttributeMap attributes = (elem.hasAttributes()) ? (AttributeMap) elem.getAttributes() : null; 

                // fix namespaces and remove default attributes
                if ((fConfiguration.features & DOMConfigurationImpl.NAMESPACES) !=0) {
                    // fix namespaces
                    // normalize attribute values
                    // remove default attributes
                    namespaceFixUp(elem, attributes);
                } else {
                    if ( attributes!=null ) {
                        for ( int i=0; i<attributes.getLength(); ++i ) {
                            Attr attr = (Attr)attributes.item(i);
                            //removeDefault(attr, attributes);
                            attr.normalize();
                            if ( ((fConfiguration.features & DOMConfigurationImpl.WELLFORMED) != 0)){
                                    isAttrValueWF(fErrorHandler, fError, fLocator, attributes, (AttrImpl)attr, attr.getValue(), fDocument.isXML11Version());
                                if (fDocument.isXMLVersionChanged()){                                   
                                    wellformed=CoreDocumentImpl.isXMLName(node.getNodeName() , fDocument.isXML11Version());
                                    if (!wellformed){
				                            String msg = DOMMessageFormatter.formatMessage(
				                              DOMMessageFormatter.DOM_DOMAIN, 
				                              "wf-invalid-character-in-node-name", 
				                               new Object[]{"Attr",node.getNodeName()});
				                            reportDOMError(fErrorHandler, fError, fLocator, msg, DOMError.SEVERITY_ERROR, 
				                                node, "wf-invalid-character-in-node-name");
                                    }
                                }           
                            }
                        }                                                        
                    }
                }
                
                
                if (fValidationHandler != null) {
                    // REVISIT: possible solutions to discard default content are:
                    //         either we pass some flag to XML Schema validator
                    //         or rely on the PSVI information.
                    fAttrProxy.setAttributes(attributes, fDocument, elem);
                    updateQName(elem, fQName); // updates global qname
                    // set error node in the dom error wrapper
                    // so if error occurs we can report an error node
                    fConfiguration.fErrorHandlerWrapper.fCurrentNode = node;
                    fCurrentNode = node;
                    // call re-validation handler
                    fValidationHandler.startElement(fQName, fAttrProxy, null);
                }

                // normalize children
                Node kid, next;
                for (kid = elem.getFirstChild(); kid != null; kid = next) {
                    next = kid.getNextSibling();
                    kid = normalizeNode(kid);
                    if (kid !=null) {
                        next = kid;  // don't advance
                    }
                } 
                if (DEBUG_ND) {
                    // normalized subtree
                    System.out.println("***The children of {"+node.getNodeName()+"} are normalized");
                    for (kid = elem.getFirstChild(); kid != null; kid = next) {
                        next = kid.getNextSibling();
                        System.out.println(kid.getNodeName() +"["+kid.getNodeValue()+"]");
                    }

                }


                if (fValidationHandler != null) {
                    updateQName(elem, fQName); // updates global qname
                    //
                    // set error node in the dom error wrapper
                    // so if error occurs we can report an error node
                    fConfiguration.fErrorHandlerWrapper.fCurrentNode = node;
                    fCurrentNode = node;
                    fValidationHandler.endElement(fQName, null);
                }

                // pop namespace context
                fNamespaceContext.popContext();

                break;
            }

        case Node.COMMENT_NODE: {  
                if (DEBUG_ND) {
                    System.out.println("==>normalizeNode:{comments}");
                }

                if ((fConfiguration.features & DOMConfigurationImpl.COMMENTS) == 0) {
                    Node prevSibling = node.getPreviousSibling();
                    Node parent = node.getParentNode();
                    // remove the comment node
                    parent.removeChild(node);
                    if (prevSibling != null && prevSibling.getNodeType() == Node.TEXT_NODE) {
                        Node nextSibling = prevSibling.getNextSibling();
                        if (nextSibling != null && nextSibling.getNodeType() == Node.TEXT_NODE) {
                            ((TextImpl)nextSibling).insertData(0, prevSibling.getNodeValue());
                            parent.removeChild(prevSibling);
                            return nextSibling;
                        }
                    }
                }//if comment node need not be removed
                else {
                    if ( ((fConfiguration.features & DOMConfigurationImpl.WELLFORMED) != 0)){
                        String commentdata = ((Comment)node).getData();
                        // check comments for invalid xml chracter as per the version
                        // of the document                            
                        isCommentWF(fErrorHandler, fError, fLocator, commentdata, fDocument.isXML11Version());                        
                    }
                }//end-else if comment node is not to be removed.
				break;
            }
        case Node.ENTITY_REFERENCE_NODE: { 
                if (DEBUG_ND) {
                    System.out.println("==>normalizeNode:{entityRef} "+node.getNodeName());
                }

                if ((fConfiguration.features & DOMConfigurationImpl.ENTITIES) == 0) {
                    Node prevSibling = node.getPreviousSibling();
                    Node parent = node.getParentNode();
                    ((EntityReferenceImpl)node).setReadOnly(false, true);
                    expandEntityRef (parent, node);
                    parent.removeChild(node);
                    Node next = (prevSibling != null)?prevSibling.getNextSibling():parent.getFirstChild();
                    // The list of children #text -> &ent;
                    // and entity has a first child as a text
                    // we should not advance
                    if (prevSibling !=null && prevSibling.getNodeType() == Node.TEXT_NODE && 
                        next.getNodeType() == Node.TEXT_NODE) {
                        return prevSibling;  // Don't advance                          
                    }
                    return next;
                } else {
                    if ( ((fConfiguration.features & DOMConfigurationImpl.WELLFORMED) != 0) && 
                        fDocument.isXMLVersionChanged()){
                            CoreDocumentImpl.isXMLName(node.getNodeName() , fDocument.isXML11Version());                    
                    }
                    // REVISIT: traverse entity reference and send appropriate calls to the validator
                    // (no normalization should be performed for the children).
                }
                break;
            }

        case Node.CDATA_SECTION_NODE: {
                if (DEBUG_ND) {
                    System.out.println("==>normalizeNode:{cdata}");
                }
                
                if ((fConfiguration.features & DOMConfigurationImpl.CDATA) == 0) {
                    // convert CDATA to TEXT nodes
                    Node prevSibling = node.getPreviousSibling();
                    if (prevSibling != null && prevSibling.getNodeType() == Node.TEXT_NODE){
                        ((Text)prevSibling).appendData(node.getNodeValue());
                        node.getParentNode().removeChild(node);
                        return prevSibling; //don't advance                        
                    }
                    else {
                        Text text = fDocument.createTextNode(node.getNodeValue());
                        Node parent = node.getParentNode();
                        node = parent.replaceChild(text, node);
                        return text;  //don't advance
                        
                    }
                }

                // send characters call for CDATA
                if (fValidationHandler != null) {
                    // set error node in the dom error wrapper
                    // so if error occurs we can report an error node
                    fConfiguration.fErrorHandlerWrapper.fCurrentNode = node;
                    fCurrentNode = node;
                    fValidationHandler.startCDATA(null);
                    fValidationHandler.characterData(node.getNodeValue(), null);
                    fValidationHandler.endCDATA(null);
                }
                String value = node.getNodeValue();
                
                if ((fConfiguration.features & DOMConfigurationImpl.SPLITCDATA) != 0) {
                    int index;
                    Node parent = node.getParentNode();
                    
                    isXMLCharWF(fErrorHandler, fError, fLocator, node.getNodeValue(), fDocument.isXML11Version());
                    while ( (index=value.indexOf("]]>")) >= 0 ) {
                        node.setNodeValue(value.substring(0, index+2));
                        value = value.substring(index +2);
                        
                        Node firstSplitNode = node;
                        Node newChild = fDocument.createCDATASection(value);
                        parent.insertBefore(newChild, node.getNextSibling());
                        node = newChild;                      
                        // issue warning
                        String msg = DOMMessageFormatter.formatMessage(
                            DOMMessageFormatter.DOM_DOMAIN, 
                            "cdata-sections-splitted", 
                             null);
                        reportDOMError(fErrorHandler, fError, fLocator, msg, DOMError.SEVERITY_WARNING, 
                            firstSplitNode, "cdata-sections-splitted");
                    }

                }
                else {
                    // check well-formness
                    isCDataWF(fErrorHandler, fError, fLocator, value, fDocument.isXML11Version());
                }
                break;
            }

        case Node.TEXT_NODE: { 
                if (DEBUG_ND) {
                    System.out.println("==>normalizeNode(text):{"+node.getNodeValue()+"}");
                }
                // If node is a text node, we need to check for one of two
                // conditions:
                //   1) There is an adjacent text node
                //   2) There is no adjacent text node, but node is
                //      an empty text node.
                Node next = node.getNextSibling();
                // If an adjacent text node, merge it with this node
                if ( next!=null && next.getNodeType() == Node.TEXT_NODE ) {
                    ((Text)node).appendData(next.getNodeValue());
                    node.getParentNode().removeChild( next );
                    // We don't need to check well-formness here since we are not yet
                    // done with this node.
                    
                    return node; // Don't advance;                   
                } else if (node.getNodeValue().length()==0) {
                    // If kid is empty, remove it
                    node.getParentNode().removeChild( node );
                } else {                    
                    // validator.characters() call and well-formness
                    // Don't send characters or check well-formness in the following cases:
                    // 1. entities is false, next child is entity reference: expand tree first
                    // 2. comments is false, and next child is comment
                    // 3. cdata is false, and next child is cdata
                  
                    short nextType = (next != null)?next.getNodeType():-1;
                    if (nextType == -1 || !(((fConfiguration.features & DOMConfigurationImpl.ENTITIES) == 0 &&
                           nextType == Node.ENTITY_NODE) ||
                          ((fConfiguration.features & DOMConfigurationImpl.COMMENTS) == 0 &&
                           nextType == Node.COMMENT_NODE) ||
                          ((fConfiguration.features & DOMConfigurationImpl.CDATA) == 0) &&
                          nextType == Node.CDATA_SECTION_NODE)) {
                              if ( ((fConfiguration.features & DOMConfigurationImpl.WELLFORMED) != 0) ){
                                  isXMLCharWF(fErrorHandler, fError, fLocator, node.getNodeValue(), fDocument.isXML11Version());
                              }                              
                              if (fValidationHandler != null) {
                                     fConfiguration.fErrorHandlerWrapper.fCurrentNode = node;
                                     fCurrentNode = node;
                                     fValidationHandler.characterData(node.getNodeValue(), null);
                                     if (DEBUG_ND) {
                                         System.out.println("=====>characterData(),"+nextType);

                                     }
                              }                             
                    }
                    else {
                            if (DEBUG_ND) {
                                System.out.println("=====>don't send characters(),"+nextType);

                            }
                    }
                }
                break;
            }        
        case Node.PROCESSING_INSTRUCTION_NODE: {
            
            //do the well-formed valid PI target name , data check when application has set the value of well-formed feature to true
            if((fConfiguration.features & DOMConfigurationImpl.WELLFORMED) != 0 ){
                ProcessingInstruction pinode = (ProcessingInstruction)node ;

                String target = pinode.getTarget();
                //1.check PI target name
                if(fDocument.isXML11Version()){
                    wellformed = XML11Char.isXML11ValidName(target);
                }
                else{                
                    wellformed = XMLChar.isValidName(target);
                }

				if (!wellformed) {
				    String msg = DOMMessageFormatter.formatMessage(
				        DOMMessageFormatter.DOM_DOMAIN, 
				        "wf-invalid-character-in-node-name", 
				        new Object[]{"Element", node.getNodeName()});
                    reportDOMError(fErrorHandler, fError, fLocator, msg, DOMError.SEVERITY_ERROR, null,  
                        "wf-invalid-character-in-node-name");
                }        
                                
                //2. check PI data
                //processing isntruction data may have certain characters
                //which may not be valid XML character               
                isXMLCharWF(fErrorHandler, fError, fLocator, pinode.getData(), fDocument.isXML11Version());
            }
        }//end case Node.PROCESSING_INSTRUCTION_NODE
        
        }//end of switch
        return null;
    }//normalizeNode

    protected final void expandEntityRef (Node parent, Node reference){
        Node kid, next;
        for (kid = reference.getFirstChild(); kid != null; kid = next) {
            next = kid.getNextSibling();
            parent.insertBefore(kid, reference);
        }
    }

    // fix namespaces
    // normalize attribute values
    // remove default attributes
    // check attribute names if the version of the document changed.
    
    protected final void namespaceFixUp (ElementImpl element, AttributeMap attributes){
        if (DEBUG) {
            System.out.println("[ns-fixup] element:" +element.getNodeName()+
                               " uri: "+element.getNamespaceURI());
        }

        // ------------------------------------
        // pick up local namespace declarations
        // <xsl:stylesheet xmlns:xsl="http://xslt">
        //   <!-- add the following via DOM 
        //          body is bound to http://xslt
        //    -->
        //   <xsl:body xmlns:xsl="http://bound"/>
        //
        // ------------------------------------

        String value, name, uri, prefix;
        if (attributes != null) {

            // Record all valid local declarations
            for (int k=0; k < attributes.getLength(); k++) {
                Attr attr = (Attr)attributes.getItem(k);
               
                //do the name check only when version of the document was changed &
                //application has set the value of well-formed features to true
                if ( ((fConfiguration.features & DOMConfigurationImpl.WELLFORMED) != 0) && 
                    fDocument.isXMLVersionChanged()){
                    //checkQName does checking based on the version of the document
                    fDocument.checkQName(attr.getPrefix() , attr.getLocalName()) ;
                }
                
                uri = attr.getNamespaceURI();
                if (uri != null && uri.equals(NamespaceContext.XMLNS_URI)) {
                    // namespace attribute
                    value = attr.getNodeValue();
                    if (value == null) {
                        value=XMLSymbols.EMPTY_STRING;
                    }

                    // Check for invalid namespace declaration:
                    if (value.equals(NamespaceContext.XMLNS_URI)) {
                    	//A null value for locale is passed to formatMessage, 
                    	//which means that the default locale will be used
                        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.XML_DOMAIN,"CantBindXMLNS",null );
                        reportDOMError(fErrorHandler, fError, fLocator, msg, DOMError.SEVERITY_ERROR, attr, "CantBindXMLNS");
                    } else {
                        // XML 1.0 Attribute value normalization
                        // value = normalizeAttributeValue(value, attr);
                        prefix = attr.getPrefix();
                        prefix = (prefix == null || 
                                  prefix.length() == 0) ? XMLSymbols.EMPTY_STRING :fSymbolTable.addSymbol(prefix);
                        String localpart = fSymbolTable.addSymbol( attr.getLocalName());
                        if (prefix == XMLSymbols.PREFIX_XMLNS) { //xmlns:prefix

                            value = fSymbolTable.addSymbol(value);
                            if (value.length() != 0) {
                                fNamespaceContext.declarePrefix(localpart, value);
                            } else {
                                // REVISIT: issue error on invalid declarations
                                //          xmlns:foo = ""

                            }
                            //removeDefault (attr, attributes);
                            continue;
                        } else { // (localpart == fXmlnsSymbol && prefix == fEmptySymbol)  -- xmlns
                            // empty prefix is always bound ("" or some string)
                            value = fSymbolTable.addSymbol(value);
                            fNamespaceContext.declarePrefix(XMLSymbols.EMPTY_STRING, value);
                            //removeDefault (attr, attributes);
                            continue;
                        }
                    }  // end-else: valid declaration
                } // end-if: namespace attribute
            }
        }



        // ---------------------------------------------------------
        // Fix up namespaces for element: per DOM L3 
        // Need to consider the following cases:
        //
        // case 1: <xsl:stylesheet xmlns:xsl="http://xsl">
        // We create another element body bound to the "http://xsl" namespace
        // as well as namespace attribute rebounding xsl to another namespace.
        // <xsl:body xmlns:xsl="http://another">
        // Need to make sure that the new namespace decl value is changed to 
        // "http://xsl"
        //
        // ---------------------------------------------------------
        // check if prefix/namespace is correct for current element
        // ---------------------------------------------------------

        uri = element.getNamespaceURI();
        prefix = element.getPrefix();
        if (uri != null) {  // Element has a namespace
            uri = fSymbolTable.addSymbol(uri);
            prefix = (prefix == null || 
                      prefix.length() == 0) ? XMLSymbols.EMPTY_STRING :fSymbolTable.addSymbol(prefix);
            if (fNamespaceContext.getURI(prefix) == uri) {
                // The xmlns:prefix=namespace or xmlns="default" was declared at parent.
                // The binder always stores mapping of empty prefix to "".
            } else {
                // the prefix is either undeclared 
                // or
                // conflict: the prefix is bound to another URI
                addNamespaceDecl(prefix, uri, element);
                fLocalNSBinder.declarePrefix(prefix, uri);
                fNamespaceContext.declarePrefix(prefix, uri);
            }
        } else { // Element has no namespace
            if (element.getLocalName() == null) {
                //  Error: DOM Level 1 node!
                if (fNamespaceValidation) {
                    String msg = DOMMessageFormatter.formatMessage(
                        DOMMessageFormatter.DOM_DOMAIN, "NullLocalElementName", 
                        new Object[]{element.getNodeName()});
                    reportDOMError(fErrorHandler, fError, fLocator, msg, DOMError.SEVERITY_FATAL_ERROR, element, 
                        "NullLocalElementName");
                } else {
                    String msg = DOMMessageFormatter.formatMessage(
                        DOMMessageFormatter.DOM_DOMAIN, "NullLocalElementName", 
                        new Object[]{element.getNodeName()});
                    reportDOMError(fErrorHandler, fError, fLocator, msg, DOMError.SEVERITY_ERROR, element, 
                        "NullLocalElementName");
                }
            } else { // uri=null and no colon (DOM L2 node)
                uri = fNamespaceContext.getURI(XMLSymbols.EMPTY_STRING);
                if (uri !=null && uri.length() > 0) {
                    // undeclare default namespace declaration (before that element
                    // bound to non-zero length uir), but adding xmlns="" decl                    
                    addNamespaceDecl (XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING, element);
                    fLocalNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
                    fNamespaceContext.declarePrefix(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
                }
            }
        }

        // -----------------------------------------
        // Fix up namespaces for attributes: per DOM L3 
        // check if prefix/namespace is correct the attributes
        // -----------------------------------------
        if (attributes != null) {

            // clone content of the attributes
            attributes.cloneMap(fAttributeList);
            for (int i = 0; i < fAttributeList.size(); i++) {
                Attr attr = (Attr) fAttributeList.elementAt(i);

                if (DEBUG) {
                    System.out.println("==>[ns-fixup] process attribute: "+attr.getNodeName());
                }
                // normalize attribute value
                attr.normalize();                
                value = attr.getValue();
                name = attr.getNodeName();                
                uri = attr.getNamespaceURI();

                // make sure that value is never null.
                if (value == null) {
                    value=XMLSymbols.EMPTY_STRING;
                }

                if (uri != null) {  // attribute has namespace !=null
                    prefix = attr.getPrefix();
                    prefix = (prefix == null || 
                              prefix.length() == 0) ? XMLSymbols.EMPTY_STRING :fSymbolTable.addSymbol(prefix);
                    /*String localpart =*/ fSymbolTable.addSymbol( attr.getLocalName());

                    // ---------------------------------------
                    // skip namespace declarations 
                    // ---------------------------------------
                    // REVISIT: can we assume that "uri" is from some symbol
                    // table, and compare by reference? -SG
                    if (uri != null && uri.equals(NamespaceContext.XMLNS_URI)) {
                        continue;
                    }
                    //---------------------------------------
                    // check if value of the attribute is namespace well-formed
                    //---------------------------------------
                    if ( ((fConfiguration.features & DOMConfigurationImpl.WELLFORMED) != 0)){
                            isAttrValueWF(fErrorHandler, fError, fLocator, attributes, (AttrImpl)attr, attr.getValue(), fDocument.isXML11Version());
                            if (fDocument.isXMLVersionChanged()){                                   
                                boolean wellformed=CoreDocumentImpl.isXMLName(attr.getNodeName() , fDocument.isXML11Version());
                                if (!wellformed){
				                        String msg = DOMMessageFormatter.formatMessage(
				                            DOMMessageFormatter.DOM_DOMAIN, 
				                            "wf-invalid-character-in-node-name", 
				                            new Object[]{"Attribute", attr.getNodeName()});
                                        reportDOMError(fErrorHandler, fError, fLocator, msg, DOMError.SEVERITY_ERROR, attr, 
                                            "wf-invalid-character-in-node-name");  
                                }
                        }
                    }

                    // ---------------------------------------
                    // remove default attributes
                    // ---------------------------------------
                    /* 
                    if (removeDefault(attr, attributes)) {
                        continue;
                    }
                    */
                    // XML 1.0 Attribute value normalization
                    //value = normalizeAttributeValue(value, attr);
                    
                    // reset id-attributes
                    ((AttrImpl)attr).setIdAttribute(false);


                    uri = fSymbolTable.addSymbol(uri);

                    // find if for this prefix a URI was already declared
                    String declaredURI =  fNamespaceContext.getURI(prefix);

                    if (prefix == XMLSymbols.EMPTY_STRING || declaredURI != uri) {
                        // attribute has no prefix (default namespace decl does not apply to attributes) 
                        // OR
                        // attribute prefix is not declared
                        // OR
                        // conflict: attribute has a prefix that conficlicts with a binding
                        //           already active in scope

                        name  = attr.getNodeName();
                        // Find if any prefix for attributes namespace URI is available
                        // in the scope
                        String declaredPrefix = fNamespaceContext.getPrefix(uri);
                        if (declaredPrefix !=null && declaredPrefix !=XMLSymbols.EMPTY_STRING) {

                            // use the prefix that was found (declared previously for this URI
                            prefix = declaredPrefix;
                        } else {
                            if (prefix != XMLSymbols.EMPTY_STRING && fLocalNSBinder.getURI(prefix) == null) {
                                // the current prefix is not null and it has no in scope declaration

                                // use this prefix
                            } else {

                                // find a prefix following the pattern "NS" +index (starting at 1)
                                // make sure this prefix is not declared in the current scope.
                                int counter = 1;
                                prefix = fSymbolTable.addSymbol(PREFIX +counter++);
                                while (fLocalNSBinder.getURI(prefix)!=null) {
                                    prefix = fSymbolTable.addSymbol(PREFIX +counter++);
                                }

                            }
                            // add declaration for the new prefix
                            addNamespaceDecl(prefix, uri, element);
                            value = fSymbolTable.addSymbol(value);
                            fLocalNSBinder.declarePrefix(prefix, value);
                            fNamespaceContext.declarePrefix(prefix, uri);
                        }

                        // change prefix for this attribute
                        attr.setPrefix(prefix);
                    }
                } else { // attribute uri == null

                    // XML 1.0 Attribute value normalization
                    //value = normalizeAttributeValue(value, attr);
                    
                    // reset id-attributes
                    ((AttrImpl)attr).setIdAttribute(false);

                    if (attr.getLocalName() == null) {
                        // It is an error if document has DOM L1 nodes.
                        if (fNamespaceValidation) {
                            String msg = DOMMessageFormatter.formatMessage(
                                DOMMessageFormatter.DOM_DOMAIN, 
                                "NullLocalAttrName", new Object[]{attr.getNodeName()});
                            reportDOMError(fErrorHandler, fError, fLocator, msg, DOMError.SEVERITY_FATAL_ERROR, attr, 
                                "NullLocalAttrName");
                        } else {
                            String msg = DOMMessageFormatter.formatMessage(
                                DOMMessageFormatter.DOM_DOMAIN, 
                                "NullLocalAttrName", new Object[]{attr.getNodeName()});
                            reportDOMError(fErrorHandler, fError, fLocator, msg, DOMError.SEVERITY_ERROR, attr, 
                                "NullLocalAttrName");
                        }
                    } else {
                        // uri=null and no colon
                        // no fix up is needed: default namespace decl does not 

                        // ---------------------------------------
                        // remove default attributes
                        // ---------------------------------------
                        // removeDefault(attr, attributes);
                    }
                }
            }
        } // end loop for attributes
    }
    
    /**
     * Adds a namespace attribute or replaces the value of existing namespace
     * attribute with the given prefix and value for URI.
     * In case prefix is empty will add/update default namespace declaration.
     * 
     * @param prefix
     * @param uri
     * @exception IOException
     */

    protected final void addNamespaceDecl(String prefix, String uri, ElementImpl element){
        if (DEBUG) {
            System.out.println("[ns-fixup] addNamespaceDecl ["+prefix+"]");
        }
        if (prefix == XMLSymbols.EMPTY_STRING) {
            if (DEBUG) {
                System.out.println("=>add xmlns=\""+uri+"\" declaration");
            }
            element.setAttributeNS(NamespaceContext.XMLNS_URI, XMLSymbols.PREFIX_XMLNS, uri);             
        } else {
            if (DEBUG) {
                System.out.println("=>add xmlns:"+prefix+"=\""+uri+"\" declaration");
            }
            element.setAttributeNS(NamespaceContext.XMLNS_URI, "xmlns:"+prefix, uri); 
        }
    }
    
    
    //
    // Methods for well-formness checking
    //
    
    
    /**
     * Check if CDATA section is well-formed
     * @param datavalue
     * @param isXML11Version = true if XML 1.1
     */
    public static final void isCDataWF(DOMErrorHandler errorHandler, DOMErrorImpl error, DOMLocatorImpl locator, 
            String datavalue, boolean isXML11Version)
    {
        if(datavalue == null || (datavalue.length() == 0) ) return ;
                
        char [] dataarray = datavalue.toCharArray(); 
        int datalength = dataarray.length ;

        //version of the document is XML 1.1
        if(isXML11Version){                    
            //we need to check all chracters as per production rules of XML11
            int i = 0 ;
            while(i < datalength){     
                char c = dataarray[i++];                                      
                if(XML11Char.isXML11Invalid(c)){
                    String msg =
                        DOMMessageFormatter.formatMessage(
                            DOMMessageFormatter.XML_DOMAIN,
                            "InvalidCharInCDSect",
                            new Object[] { Integer.toString(c, 16)});
                    reportDOMError(
                        errorHandler,
                        error,
                        locator,
                        msg,
                        DOMError.SEVERITY_ERROR,
                        null,
                        "wf-invalid-character");
                }
                else if (c==']'){
                    int count = i;
                    if (count<datalength && dataarray[count]==']'){
                        while (++count <datalength && dataarray[count]==']'){
                            // do nothing
                        }
                        if (count <datalength && dataarray[count]=='>'){
                            //CDEndInContent
		  					String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.XML_DOMAIN,
							    "CDEndInContent", null);
							reportDOMError(errorHandler, error, locator,msg, DOMError.SEVERITY_ERROR, null, "wf-invalid-character");
                        }
                    }
                    
                }
            }
        }//version of the document is XML 1.0
        else{                    
            //we need to check all chracters as per production rules of XML 1.0
            int i = 0 ;
            while(i < datalength){   
                char c = dataarray[i++];                         
                if( XMLChar.isInvalid(c) ){
                	//Note:  The key InvalidCharInCDSect from XMLMessages.properties
                	//is being used to obtain the message and DOM error type
                	//"wf-invalid-character" is used.  Also per DOM it is error but 
                	//as per XML spec. it is fatal error
					String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.XML_DOMAIN, "InvalidCharInCDSect", new Object[]{Integer.toString(c, 16)});
					reportDOMError(errorHandler, error, locator, msg, DOMError.SEVERITY_ERROR, null, 
					    "wf-invalid-character");
                }
                else if (c==']'){
                    int count = i;
                    if (count<datalength && dataarray[count]==']'){
                        while (++count <datalength && dataarray[count]==']'){
                            // do nothing
                        }
                        if (count <datalength && dataarray[count]=='>'){
		  					String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.XML_DOMAIN,"CDEndInContent", null);
							reportDOMError(errorHandler, error, locator,  msg, DOMError.SEVERITY_ERROR, null, "wf-invalid-character");
                        }
                    }
                    
                }
            }            
        }//end-else fDocument.isXMLVersion()
        
    }//isCDataWF
     
    /**
     * NON-DOM: check for valid XML characters as per the XML version
     * @param datavalue
     * @param isXML11Version = true if XML 1.1
     */
    public static final void isXMLCharWF(DOMErrorHandler errorHandler, DOMErrorImpl error, DOMLocatorImpl locator, 
                String datavalue, boolean isXML11Version)
    {
        if(datavalue == null || (datavalue.length() == 0) ) return ;      
        char [] dataarray = datavalue.toCharArray(); 
        int datalength = dataarray.length ;

        //version of the document is XML 1.1
        if(isXML11Version){                    
            //we need to check all chracters as per production rules of XML11
            int i = 0 ;
            while(i < datalength){                            
                if(XML11Char.isXML11Invalid(dataarray[i++])){
					String msg = DOMMessageFormatter.formatMessage(
                        DOMMessageFormatter.DOM_DOMAIN, "InvalidXMLCharInDOM", 
                        new Object[]{Integer.toString(dataarray[i-1], 16)});
					reportDOMError(errorHandler, error, locator, msg, DOMError.SEVERITY_ERROR, null, 
					    "wf-invalid-character");
                };
            }
        }//version of the document is XML 1.0
        else{                    
            //we need to check all chracters as per production rules of XML 1.0
            int i = 0 ;
            while(i < datalength){                            
                if( XMLChar.isInvalid(dataarray[i++]) ){
					String msg = DOMMessageFormatter.formatMessage(
                        DOMMessageFormatter.DOM_DOMAIN, "InvalidXMLCharInDOM", 
                        new Object[]{Integer.toString(dataarray[i-1], 16)});
					reportDOMError(errorHandler, error, locator, msg, DOMError.SEVERITY_ERROR, null, 
					    "wf-invalid-character");
                };
            }            
        }//end-else fDocument.isXMLVersion()
        
    }//isXMLCharWF
    
    /**
     * NON-DOM: check if value of the comment is well-formed
     * @param datavalue
     * @param isXML11Version = true if XML 1.1
     */
    public static final void isCommentWF(DOMErrorHandler errorHandler, DOMErrorImpl error, DOMLocatorImpl locator, 
                                    String datavalue, boolean isXML11Version)
    {
        if(datavalue == null || (datavalue.length() == 0) ) return ;
                
        char [] dataarray = datavalue.toCharArray(); 
        int datalength = dataarray.length ;

        //version of the document is XML 1.1
        if(isXML11Version){                    
            //we need to check all chracters as per production rules of XML11
            int i = 0 ;
            while(i < datalength){   
                char c = dataarray[i++];
                                         
                if(XML11Char.isXML11Invalid(c)){
  					String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.XML_DOMAIN, 
					    "InvalidCharInComment", 
					    new Object [] {Integer.toString(dataarray[i-1], 16)});
					reportDOMError(errorHandler, error, locator, msg, DOMError.SEVERITY_ERROR, null, "wf-invalid-character");
                }
                else if (c == '-' && i<datalength && dataarray[i]=='-'){
  					String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.XML_DOMAIN,
					    "DashDashInComment", null);
					// invalid: '--' in comment                   
					reportDOMError(errorHandler, error, locator, msg, DOMError.SEVERITY_ERROR, null, "wf-invalid-character");
                }
            }
        }//version of the document is XML 1.0
        else{                    
            //we need to check all chracters as per production rules of XML 1.0
            int i = 0 ;
            while(i < datalength){ 
                char c = dataarray[i++];                           
                if( XMLChar.isInvalid(c) ){
  					String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.XML_DOMAIN,
					    "InvalidCharInComment", new Object [] {Integer.toString(dataarray[i-1], 16)});
					reportDOMError(errorHandler, error, locator, msg, DOMError.SEVERITY_ERROR, null, "wf-invalid-character");
                }  
                else if (c == '-' && i<datalength && dataarray[i]=='-'){
  					String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.XML_DOMAIN,
					    "DashDashInComment", null);
					// invalid: '--' in comment                   
					reportDOMError(errorHandler, error, locator, msg, DOMError.SEVERITY_ERROR, null, "wf-invalid-character");
                }                                      
            }
                        
        }//end-else fDocument.isXMLVersion()
        
    }//isCommentWF
    
    /** NON-DOM: check if attribute value is well-formed
     * @param attributes
     * @param a
     * @param value
     */
    public static final void isAttrValueWF(DOMErrorHandler errorHandler, DOMErrorImpl error, 
            DOMLocatorImpl locator, AttributeMap attributes, AttrImpl a, String value, boolean xml11Version) {
        if (a.hasStringValue()) {
            isXMLCharWF(errorHandler, error, locator, value, xml11Version);
        } else {
        	NodeList children = a.getChildNodes(); 
            //check each child node of the attribute's value
            for (int j = 0; j < children.getLength(); j++) {
                Node child = children.item(j);
                //If the attribute's child is an entity refernce
                if (child.getNodeType() == Node.ENTITY_REFERENCE_NODE) {
                    Document owner = a.getOwnerDocument();
                    Entity ent = null;
                    //search for the entity in the docType
                    //of the attribute's ownerDocument
                    if (owner != null) {
                        DocumentType docType = owner.getDoctype();
                        if (docType != null) {
                            NamedNodeMap entities = docType.getEntities();
                            ent = (Entity) entities.getNamedItemNS(
                                    "*",
                                    child.getNodeName());
                        }
                    }
                    //If the entity was not found issue a fatal error
                    if (ent == null) {
                        String msg = DOMMessageFormatter.formatMessage(
                            DOMMessageFormatter.DOM_DOMAIN, "UndeclaredEntRefInAttrValue", 
                            new Object[]{a.getNodeName()});
                        reportDOMError(errorHandler, error, locator, msg, DOMError.SEVERITY_ERROR, 
                            null, "UndeclaredEntRefInAttrValue");
                    }
                }
                else {
                    // Text node
                    isXMLCharWF(errorHandler, error, locator, child.getNodeValue(), xml11Version);
                }
            }
        }
    }



    /**
     * Reports a DOM error to the user handler.
     * 
     * If the error is fatal, the processing will be always aborted.
     */
    public static final void reportDOMError(DOMErrorHandler errorHandler, DOMErrorImpl error, DOMLocatorImpl locator, 
                        String message, short severity, Node node, String type ){
        if( errorHandler!=null ) {
            error.reset();
            error.fMessage = message;
            error.fSeverity = severity;
            error.fLocator = locator;
            error.fType = type;
            error.fRelatedData = node;
            locator.fRelatedNode = node;
    
            if(!errorHandler.handleError(error))
                throw abort;
        }
        if( severity==DOMError.SEVERITY_FATAL_ERROR )
            throw abort;
    }

    protected final void updateQName (Node node, QName qname){

        String prefix    = node.getPrefix();
        String namespace = node.getNamespaceURI();
        String localName = node.getLocalName();
        // REVISIT: the symbols are added too often: start/endElement
        //          and in the namespaceFixup. Should reduce number of calls to symbol table.
        qname.prefix = (prefix!=null && prefix.length()!=0)?fSymbolTable.addSymbol(prefix):null;
        qname.localpart = (localName != null)?fSymbolTable.addSymbol(localName):null;
        qname.rawname = fSymbolTable.addSymbol(node.getNodeName()); 
        qname.uri =  (namespace != null)?fSymbolTable.addSymbol(namespace):null;
    }
    


	/* REVISIT: remove this method if DOM does not change spec.
	 * Performs partial XML 1.0 attribute value normalization and replaces
     * attribute value if the value is changed after the normalization.
     * DOM defines that normalizeDocument acts as if the document was going 
     * through a save and load cycle, given that serializer will not escape
     * any '\n' or '\r' characters on load those will be normalized.
     * Thus during normalize document we need to do the following:
     * - perform "2.11 End-of-Line Handling"
     * - replace #xD, #xA, #x9 with #x20 (white space).
     * Note: This alg. won't attempt to resolve entity references or character entity 
     * references, since '&' will be escaped during serialization and during loading
     * this won't be recognized as entity reference, i.e. attribute value "&foo;" will 
     * be serialized as "&amp;foo;" and thus after loading will be "&foo;" again.
	 * @param value current attribute value
	 * @param attr current attribute
	 * @return String the value (could be original if normalization did not change 
     * the string)
	 */
    final String normalizeAttributeValue(String value, Attr attr) {
        if (!attr.getSpecified()){
            // specified attributes should already have a normalized form
            // since those were added by validator
            return value;
        }
        int end = value.length();
        // ensure capacity 
        if (fNormalizedValue.ch.length < end) {
            fNormalizedValue.ch = new char[end];
        }
        fNormalizedValue.length = 0;
        boolean normalized = false;
        for (int i = 0; i < end; i++) {
            char c = value.charAt(i);
            if (c==0x0009 || c==0x000A) {
               fNormalizedValue.ch[fNormalizedValue.length++] = ' '; 
               normalized = true;
            }
            else if(c==0x000D){
               normalized = true;
               fNormalizedValue.ch[fNormalizedValue.length++] = ' ';
               int next = i+1;
               if (next < end && value.charAt(next)==0x000A) i=next; // skip following xA
            }
            else {
                fNormalizedValue.ch[fNormalizedValue.length++] = c;
            }
        }
        if (normalized){
           value = fNormalizedValue.toString();
           attr.setValue(value);
        }
        return value;
    }
  
    protected final class XMLAttributesProxy 
    implements XMLAttributes {
        protected AttributeMap fAttributes;
        protected CoreDocumentImpl fDocument;
        protected ElementImpl fElement;

        protected final Vector fAugmentations = new Vector(5);


        public void setAttributes(AttributeMap attributes, CoreDocumentImpl doc, ElementImpl elem) {
            fDocument = doc;
            fAttributes = attributes;
            fElement = elem;
            if (attributes != null) {
                int length = attributes.getLength();

                fAugmentations.setSize(length);
                // REVISIT: this implementation does not store any value in augmentations
                //          and basically not keeping augs in parallel to attributes map
                //          untill all attributes are added (default attributes)
                for (int i = 0; i < length; i++) {
                    fAugmentations.setElementAt(new AugmentationsImpl(), i);
                }
            } else {
                fAugmentations.setSize(0);
            }
        }


		/**
         * This method adds default declarations
		 * @see org.apache.xerces.xni.XMLAttributes#addAttribute(QName, String, String)
		 */
		public int addAttribute(QName qname, String attrType, String attrValue) {
 			int index = fElement.getXercesAttribute(qname.uri, qname.localpart);
			// add defaults to the tree
			if (index < 0) {
                // the default attribute was removed by a user and needed to 
                // be added back
				AttrImpl attr = (AttrImpl)
					((CoreDocumentImpl) fElement.getOwnerDocument()).createAttributeNS(
						qname.uri,
						qname.rawname,
						qname.localpart);
                // REVISIT: the following should also update ID table
				index = fElement.setXercesAttributeNode(attr);
				attr.setNodeValue(attrValue);
				fAugmentations.insertElementAt(new AugmentationsImpl(), index);
                attr.setSpecified(false);
			}            
			else {
                // default attribute is in the tree
                // we don't need to do anything since prefix was already fixed
                // at the namespace fixup time and value must be same value, otherwise
                // attribute will be treated as specified and we will never reach 
                // this method.
                
            }
            return index;
		}


        public void removeAllAttributes(){
            // REVISIT: implement
        }


        public void removeAttributeAt(int attrIndex){
            // REVISIT: implement
        }


        public int getLength(){
            return(fAttributes != null)?fAttributes.getLength():0;
        }


        public int getIndex(String qName){        
            // REVISIT: implement
            return -1;
        }

        public int getIndex(String uri, String localPart){
            // REVISIT: implement
            return -1;
        }

        public void setName(int attrIndex, QName attrName){
            // REVISIT: implement
        }

        public void getName(int attrIndex, QName attrName){
            if (fAttributes !=null) {
                updateQName((Node)fAttributes.getItem(attrIndex), attrName);
            }
        }

        public String getPrefix(int index){
            // REVISIT: implement
            return null;
        }


        public String getURI(int index){
            // REVISIT: implement
            return null;
        }


        public String getLocalName(int index){
            // REVISIT: implement
            return null;
        }


        public String getQName(int index){
            // REVISIT: implement
            return null;
        }


        public void setType(int attrIndex, String attrType){
            // REVISIT: implement
        }


        public String getType(int index){
            return "CDATA";
        }


        public String getType(String qName){
            return "CDATA";
        }


        public String getType(String uri, String localName){
            return "CDATA";
        }


        public void setValue(int attrIndex, String attrValue){
            // REVISIT: is this desired behaviour? 
            // The values are updated in the case datatype-normalization is turned on
            // in this case we need to make sure that specified attributes stay specified
            
            if (fAttributes != null){
                AttrImpl attr = (AttrImpl)fAttributes.getItem(attrIndex);
                boolean specified = attr.getSpecified();
                attr.setValue(attrValue);
                attr.setSpecified(specified);
            
            }
        }


        public String getValue(int index){
            return (fAttributes !=null)?fAttributes.item(index).getNodeValue():"";

        }


        public String getValue(String qName){
            // REVISIT: implement
            return null;
        }


        public String getValue(String uri, String localName){ 
            if (fAttributes != null) {
                Node node =  fAttributes.getNamedItemNS(uri, localName);
                return(node != null)? node.getNodeValue():null;
            }
            return null;
        }


        public void setNonNormalizedValue(int attrIndex, String attrValue){
            // REVISIT: implement

        }


        public String getNonNormalizedValue(int attrIndex){
            // REVISIT: implement
            return null;
        }


        public void setSpecified(int attrIndex, boolean specified){
            AttrImpl attr = (AttrImpl)fAttributes.getItem(attrIndex);
            attr.setSpecified(specified);
        }

        public boolean isSpecified(int attrIndex){
            return((Attr)fAttributes.getItem(attrIndex)).getSpecified();
        }

        public Augmentations getAugmentations (int attributeIndex){
            return(Augmentations)fAugmentations.elementAt(attributeIndex);
        }

        public Augmentations getAugmentations (String uri, String localPart){ 
            // REVISIT: implement
            return null;
        }

        public Augmentations getAugmentations(String qName){
            // REVISIT: implement
            return null;
        }

        /**
         * Sets the augmentations of the attribute at the specified index.
         * 
         * @param attrIndex The attribute index.
         * @param augs      The augmentations.
         */
        public void setAugmentations(int attrIndex, Augmentations augs) {
            fAugmentations.setElementAt(augs, attrIndex);
        }
    }

    // 
    // XMLDocumentHandler methods
    //

    /**
     * The start of the document.
     * 
     * @param locator  The document locator, or null if the document
     *                 location cannot be reported during the parsing
     *                 of this document. However, it is <em>strongly</em>
     *                 recommended that a locator be supplied that can
     *                 at least report the system identifier of the
     *                 document.
     * @param encoding The auto-detected IANA encoding name of the entity
     *                 stream. This value will be null in those situations
     *                 where the entity encoding is not auto-detected (e.g.
     *                 internal entities or a document entity that is
     *                 parsed from a java.io.Reader).
     * @param namespaceContext
     *                 The namespace context in effect at the
     *                 start of this document.
     *                 This object represents the current context.
     *                 Implementors of this class are responsible
     *                 for copying the namespace bindings from the
     *                 the current context (and its parent contexts)
     *                 if that information is important.
     *                 
     * @param augs     Additional information that may include infoset augmentations
     * @exception XNIException
     *                   Thrown by handler to signal an error.
     */
    public void startDocument(XMLLocator locator, String encoding, 
                              NamespaceContext namespaceContext,
                              Augmentations augs) 
        throws XNIException{
    }

    /**
     * Notifies of the presence of an XMLDecl line in the document. If
     * present, this method will be called immediately following the
     * startDocument call.
     * 
     * @param version    The XML version.
     * @param encoding   The IANA encoding name of the document, or null if
     *                   not specified.
     * @param standalone The standalone value, or null if not specified.
     * @param augs       Additional information that may include infoset augmentations
     *                   
     * @exception XNIException
     *                   Thrown by handler to signal an error.
     */
    public void xmlDecl(String version, String encoding, String standalone, Augmentations augs)
        throws XNIException{
    }

    /**
     * Notifies of the presence of the DOCTYPE line in the document.
     * 
     * @param rootElement
     *                 The name of the root element.
     * @param publicId The public identifier if an external DTD or null
     *                 if the external DTD is specified using SYSTEM.
     * @param systemId The system identifier if an external DTD, null
     *                 otherwise.
     * @param augs     Additional information that may include infoset augmentations
     *                 
     * @exception XNIException
     *                   Thrown by handler to signal an error.
     */
    public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs)
        throws XNIException{
    }

    /**
     * A comment.
     * 
     * @param text   The text in the comment.
     * @param augs   Additional information that may include infoset augmentations
     *               
     * @exception XNIException
     *                   Thrown by application to signal an error.
     */
    public void comment(XMLString text, Augmentations augs) throws XNIException{
    }

    /**
     * A processing instruction. Processing instructions consist of a
     * target name and, optionally, text data. The data is only meaningful
     * to the application.
     * <p>
     * Typically, a processing instruction's data will contain a series
     * of pseudo-attributes. These pseudo-attributes follow the form of
     * element attributes but are <strong>not</strong> parsed or presented
     * to the application as anything other than text. The application is
     * responsible for parsing the data.
     * 
     * @param target The target.
     * @param data   The data or null if none specified.
     * @param augs   Additional information that may include infoset augmentations
     *               
     * @exception XNIException
     *                   Thrown by handler to signal an error.
     */
    public void processingInstruction(String target, XMLString data, Augmentations augs)
        throws XNIException{
    }

    /**
     * The start of an element.
     * 
     * @param element    The name of the element.
     * @param attributes The element attributes.
     * @param augs       Additional information that may include infoset augmentations
     *                   
     * @exception XNIException
     *                   Thrown by handler to signal an error.
     */
	public void startElement(QName element, XMLAttributes attributes, Augmentations augs)
		throws XNIException {
		Element currentElement = (Element) fCurrentNode;
		int attrCount = attributes.getLength();
        if (DEBUG_EVENTS) {
            System.out.println("==>startElement: " +element+
            " attrs.length="+attrCount);
        }

		for (int i = 0; i < attrCount; i++) {
			attributes.getName(i, fAttrQName);
			Attr attr = null;

			attr = currentElement.getAttributeNodeNS(fAttrQName.uri, fAttrQName.localpart);
            AttributePSVI attrPSVI =
				(AttributePSVI) attributes.getAugmentations(i).getItem(Constants.ATTRIBUTE_PSVI);

			if (attrPSVI != null) {
                //REVISIT: instead we should be using augmentations:
                // to set/retrieve Id attributes
                XSTypeDefinition decl = attrPSVI.getMemberTypeDefinition();
                boolean id = false;
                if (decl != null){
                    id = ((XSSimpleType)decl).isIDType();
                } else{
                    decl = attrPSVI.getTypeDefinition();
                    if (decl !=null){
                       id = ((XSSimpleType)decl).isIDType(); 
                    }
                }
                if (id){
                    ((ElementImpl)currentElement).setIdAttributeNode(attr, true);
                }
                
				if (fPSVI) {
					((PSVIAttrNSImpl) attr).setPSVI(attrPSVI);
				}
				if ((fConfiguration.features & DOMConfigurationImpl.DTNORMALIZATION) != 0) {
					// datatype-normalization
					// NOTE: The specified value MUST be set after we set
					//       the node value because that turns the "specified"
					//       flag to "true" which may overwrite a "false"
					//       value from the attribute list.
					boolean specified = attr.getSpecified();
					attr.setValue(attrPSVI.getSchemaNormalizedValue());
					if (!specified) {
						((AttrImpl) attr).setSpecified(specified);
					}
				}
			}
		}
	}


    /**
     * An empty element.
     * 
     * @param element    The name of the element.
     * @param attributes The element attributes.
     * @param augs       Additional information that may include infoset augmentations
     *                   
     * @exception XNIException
     *                   Thrown by handler to signal an error.
     */
	public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs)
		throws XNIException {
        if (DEBUG_EVENTS) {
            System.out.println("==>emptyElement: " +element);
        }

		startElement(element, attributes, augs);
        endElement(element, augs);
	}

    /**
     * This method notifies the start of a general entity.
     * <p>
     * <strong>Note:</strong> This method is not called for entity references
     * appearing as part of attribute values.
     * 
     * @param name     The name of the general entity.
     * @param identifier The resource identifier.
     * @param encoding The auto-detected IANA encoding name of the entity
     *                 stream. This value will be null in those situations
     *                 where the entity encoding is not auto-detected (e.g.
     *                 internal entities or a document entity that is
     *                 parsed from a java.io.Reader).
     * @param augs     Additional information that may include infoset augmentations
     *                 
     * @exception XNIException Thrown by handler to signal an error.
     */
    public void startGeneralEntity(String name, 
                                   XMLResourceIdentifier identifier,
                                   String encoding,
                                   Augmentations augs) throws XNIException{
    }

    /**
     * Notifies of the presence of a TextDecl line in an entity. If present,
     * this method will be called immediately following the startEntity call.
     * <p>
     * <strong>Note:</strong> This method will never be called for the
     * document entity; it is only called for external general entities
     * referenced in document content.
     * <p>
     * <strong>Note:</strong> This method is not called for entity references
     * appearing as part of attribute values.
     * 
     * @param version  The XML version, or null if not specified.
     * @param encoding The IANA encoding name of the entity.
     * @param augs     Additional information that may include infoset augmentations
     *                 
     * @exception XNIException
     *                   Thrown by handler to signal an error.
     */
    public void textDecl(String version, String encoding, Augmentations augs) throws XNIException{
    }

    /**
     * This method notifies the end of a general entity.
     * <p>
     * <strong>Note:</strong> This method is not called for entity references
     * appearing as part of attribute values.
     * 
     * @param name   The name of the entity.
     * @param augs   Additional information that may include infoset augmentations
     *               
     * @exception XNIException
     *                   Thrown by handler to signal an error.
     */
    public void endGeneralEntity(String name, Augmentations augs) throws XNIException{
    }

    /**
     * Character content.
     * 
     * @param text   The content.
     * @param augs   Additional information that may include infoset augmentations
     *               
     * @exception XNIException
     *                   Thrown by handler to signal an error.
     */
    public void characters(XMLString text, Augmentations augs) throws XNIException{
    }

    /**
     * Ignorable whitespace. For this method to be called, the document
     * source must have some way of determining that the text containing
     * only whitespace characters should be considered ignorable. For
     * example, the validator can determine if a length of whitespace
     * characters in the document are ignorable based on the element
     * content model.
     * 
     * @param text   The ignorable whitespace.
     * @param augs   Additional information that may include infoset augmentations
     *               
     * @exception XNIException
     *                   Thrown by handler to signal an error.
     */
    public void ignorableWhitespace(XMLString text, Augmentations augs) throws XNIException{
    }

    /**
     * The end of an element.
     * 
     * @param element The name of the element.
     * @param augs    Additional information that may include infoset augmentations
     *                
     * @exception XNIException
     *                   Thrown by handler to signal an error.
     */
	public void endElement(QName element, Augmentations augs) throws XNIException {
		if (DEBUG_EVENTS) {
			System.out.println("==>endElement: " + element);
		}

		ElementPSVI elementPSVI = (ElementPSVI) augs.getItem(Constants.ELEMENT_PSVI);
		if (elementPSVI != null) {
			ElementImpl elementNode = (ElementImpl) fCurrentNode;
			if (fPSVI) {
				((PSVIElementNSImpl) fCurrentNode).setPSVI(elementPSVI);
			}
			// include element default content (if one is available)
			String normalizedValue = elementPSVI.getSchemaNormalizedValue();
			if ((fConfiguration.features & DOMConfigurationImpl.DTNORMALIZATION) != 0) {
                if (normalizedValue !=null)
				    elementNode.setTextContent(normalizedValue);
			}
			else {
				// NOTE: this is a hack: it is possible that DOM had an empty element
				// and validator sent default value using characters(), which we don't 
				// implement. Thus, here we attempt to add the default value.
				String text = elementNode.getTextContent();
				if (text.length() == 0) {
					// default content could be provided
                    if (normalizedValue !=null)
                        elementNode.setTextContent(normalizedValue);
				}
			}
		}
	}


    /**
     * The start of a CDATA section.
     * 
     * @param augs   Additional information that may include infoset augmentations
     *               
     * @exception XNIException
     *                   Thrown by handler to signal an error.
     */
    public void startCDATA(Augmentations augs) throws XNIException{
    }

    /**
     * The end of a CDATA section.
     * 
     * @param augs   Additional information that may include infoset augmentations
     *               
     * @exception XNIException
     *                   Thrown by handler to signal an error.
     */
    public void endCDATA(Augmentations augs) throws XNIException{
    }

    /**
     * The end of the document.
     * 
     * @param augs   Additional information that may include infoset augmentations
     *               
     * @exception XNIException
     *                   Thrown by handler to signal an error.
     */
    public void endDocument(Augmentations augs) throws XNIException{
    }


    /** Sets the document source. */
    public void setDocumentSource(XMLDocumentSource source){
    }


    /** Returns the document source. */
    public XMLDocumentSource getDocumentSource(){
        return null;
    }

    
}  // DOMNormalizer class
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7882.java