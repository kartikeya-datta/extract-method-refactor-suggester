error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16272.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16272.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16272.java
text:
```scala
f@@NamespaceSupport.reset();

/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 2000-2002 The Apache Software Foundation.  All rights
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

package org.apache.xerces.impl;

import java.util.Enumeration;

import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.msg.XMLMessageFormatter;

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
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentFilter;

/**
 * This class performs namespace binding on the startElement and endElement
 * method calls and passes all other methods through to the registered
 * document handler. This class can be configured to only pass the
 * start and end prefix mappings (start/endPrefixMapping).
 * <p>
 * This component requires the following features and properties from the
 * component manager that uses it:
 * <ul>
 *  <li>http://xml.org/sax/features/namespaces</li>
 *  <li>http://apache.org/xml/properties/internal/symbol-table</li>
 *  <li>http://apache.org/xml/properties/internal/error-reporter</li>
 * </ul>
 *
 * @author Andy Clark, IBM
 *
 * @version $Id$
 */
public class XMLNamespaceBinder
    implements XMLComponent, XMLDocumentFilter {

    //
    // Constants
    //

    // feature identifiers

    /** Feature identifier: namespaces. */
    protected static final String NAMESPACES =
        Constants.SAX_FEATURE_PREFIX + Constants.NAMESPACES_FEATURE;

    // property identifiers

    /** Property identifier: symbol table. */
    protected static final String SYMBOL_TABLE =
        Constants.XERCES_PROPERTY_PREFIX + Constants.SYMBOL_TABLE_PROPERTY;

    /** Property identifier: error reporter. */
    protected static final String ERROR_REPORTER =
        Constants.XERCES_PROPERTY_PREFIX + Constants.ERROR_REPORTER_PROPERTY;

    // recognized features and properties

    /** Recognized features. */
    private static final String[] RECOGNIZED_FEATURES = {
        NAMESPACES,
    };

    /** Recognized properties. */
    private static final String[] RECOGNIZED_PROPERTIES = {
        SYMBOL_TABLE,
        ERROR_REPORTER,
    };

    //
    // Data
    //

    // features

    /** Namespaces. */
    protected boolean fNamespaces;

    // properties

    /** Symbol table. */
    protected SymbolTable fSymbolTable;

    /** Error reporter. */
    protected XMLErrorReporter fErrorReporter;

    // handlers

    /** Document handler. */
    protected XMLDocumentHandler fDocumentHandler;

    // namespaces

    /** Namespace support. */
    protected NamespaceSupport fNamespaceSupport = new NamespaceSupport();

    // settings

    /** Only pass start and end prefix mapping events. */
    protected boolean fOnlyPassPrefixMappingEvents;

    // shared context

    /** Namespace context. */
    private NamespaceContext fNamespaceContext;

    // temp vars

    /** Attribute QName. */
    private QName fAttributeQName = new QName();

    //
    // Constructors
    //

    /** Default constructor. */
    public XMLNamespaceBinder() {
        this(null);
    } // <init>()

    /**
     * Constructs a namespace binder that shares the specified namespace
     * context during each parse.
     *
     * @param namespaceContext The shared context.
     */
    public XMLNamespaceBinder(NamespaceContext namespaceContext) {
        fNamespaceContext = namespaceContext;
    } // <init>(NamespaceContext)


    //
    // Public methods
    //

    /** Returns the current namespace context. */
    public NamespaceContext getNamespaceContext() {
        return fNamespaceSupport;
    } // getNamespaceContext():NamespaceContext

    // settings

    /**
     * Sets whether the namespace binder only passes the prefix mapping
     * events to the registered document handler or passes all document
     * events.
     *
     * @param onlyPassPrefixMappingEvents True to pass only the prefix
     *                                    mapping events; false to pass
     *                                    all events.
     */
    public void setOnlyPassPrefixMappingEvents(boolean onlyPassPrefixMappingEvents) {
        fOnlyPassPrefixMappingEvents = onlyPassPrefixMappingEvents;
    } // setOnlyPassPrefixMappingEvents(boolean)

    /**
     * Returns true if the namespace binder only passes the prefix mapping
     * events to the registered document handler; false if the namespace
     * binder passes all document events.
     */
    public boolean getOnlyPassPrefixMappingEvents() {
        return fOnlyPassPrefixMappingEvents;
    } // getOnlyPassPrefixMappingEvents():boolean

    //
    // XMLComponent methods
    //

    /**
     * Resets the component. The component can query the component manager
     * about any features and properties that affect the operation of the
     * component.
     *
     * @param componentManager The component manager.
     *
     * @throws SAXException Thrown by component on initialization error.
     *                      For example, if a feature or property is
     *                      required for the operation of the component, the
     *                      component manager may throw a
     *                      SAXNotRecognizedException or a
     *                      SAXNotSupportedException.
     */
    public void reset(XMLComponentManager componentManager)
        throws XNIException {

        // features
        try {
            fNamespaces = componentManager.getFeature(NAMESPACES);
        }
        catch (XMLConfigurationException e) {
            fNamespaces = true;
        }

        // Xerces properties
        fSymbolTable = (SymbolTable)componentManager.getProperty(SYMBOL_TABLE);
        fErrorReporter = (XMLErrorReporter)componentManager.getProperty(ERROR_REPORTER);

        // initialize vars
        fNamespaceSupport.reset(fSymbolTable);

        // use shared context
        NamespaceContext context = fNamespaceContext;
        while (context != null) {
            int count = context.getDeclaredPrefixCount();
            for (int i = 0; i < count; i++) {
                String prefix = context.getDeclaredPrefixAt(i);
                if (fNamespaceSupport.getURI(prefix) == null) {
                    String uri = context.getURI(prefix);
                    fNamespaceSupport.declarePrefix(prefix, uri);
                }
            }
            context = context.getParentContext();
        }

    } // reset(XMLComponentManager)

    /**
     * Returns a list of feature identifiers that are recognized by
     * this component. This method may return null if no features
     * are recognized by this component.
     */
    public String[] getRecognizedFeatures() {
        return (String[])(RECOGNIZED_FEATURES.clone());
    } // getRecognizedFeatures():String[]

    /**
     * Sets the state of a feature. This method is called by the component
     * manager any time after reset when a feature changes state.
     * <p>
     * <strong>Note:</strong> Components should silently ignore features
     * that do not affect the operation of the component.
     *
     * @param featureId The feature identifier.
     * @param state     The state of the feature.
     *
     * @throws SAXNotRecognizedException The component should not throw
     *                                   this exception.
     * @throws SAXNotSupportedException The component should not throw
     *                                  this exception.
     */
    public void setFeature(String featureId, boolean state)
        throws XMLConfigurationException {
    } // setFeature(String,boolean)

    /**
     * Returns a list of property identifiers that are recognized by
     * this component. This method may return null if no properties
     * are recognized by this component.
     */
    public String[] getRecognizedProperties() {
        return (String[])(RECOGNIZED_PROPERTIES.clone());
    } // getRecognizedProperties():String[]

    /**
     * Sets the value of a property during parsing.
     *
     * @param propertyId
     * @param value
     */
    public void setProperty(String propertyId, Object value)
        throws XMLConfigurationException {

        // Xerces properties
        if (propertyId.startsWith(Constants.XERCES_PROPERTY_PREFIX)) {
            String property =
               propertyId.substring(Constants.XERCES_PROPERTY_PREFIX.length());
            if (property.equals(Constants.SYMBOL_TABLE_PROPERTY)) {
                fSymbolTable = (SymbolTable)value;
            }
            else if (property.equals(Constants.ERROR_REPORTER_PROPERTY)) {
                fErrorReporter = (XMLErrorReporter)value;
            }
            return;
        }

    } // setProperty(String,Object)

    //
    // XMLDocumentSource methods
    //

    /**
     * Sets the document handler to receive information about the document.
     *
     * @param documentHandler The document handler.
     */
    public void setDocumentHandler(XMLDocumentHandler documentHandler) {
        fDocumentHandler = documentHandler;
    } // setDocumentHandler(XMLDocumentHandler)

    //
    // XMLDocumentHandler methods
    //

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
                                   String encoding, Augmentations augs) 
        throws XNIException {
        if (fDocumentHandler != null && !fOnlyPassPrefixMappingEvents) {
            fDocumentHandler.startGeneralEntity(name, identifier, encoding, augs);
        }
    } // startEntity(String,String,String,String,String)

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
     * @throws XNIException Thrown by handler to signal an error.
     */
    public void textDecl(String version, String encoding, Augmentations augs)
        throws XNIException {
        if (fDocumentHandler != null && !fOnlyPassPrefixMappingEvents) {
            fDocumentHandler.textDecl(version, encoding, augs);
        }
    } // textDecl(String,String)

    /**
     * The start of the document.
     *
     * @param locator  The system identifier of the entity if the entity
     *                 is external, null otherwise.
     * @param encoding The auto-detected IANA encoding name of the entity
     *                 stream. This value will be null in those situations
     *                 where the entity encoding is not auto-detected (e.g.
     *                 internal entities or a document entity that is
     *                 parsed from a java.io.Reader).
     * @param augs     Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
    public void startDocument(XMLLocator locator, String encoding, Augmentations augs)
        throws XNIException {
        if (fDocumentHandler != null && !fOnlyPassPrefixMappingEvents) {
            fDocumentHandler.startDocument(locator, encoding, augs);
        }
    } // startDocument(XMLLocator,String)

    /**
     * Notifies of the presence of an XMLDecl line in the document. If
     * present, this method will be called immediately following the
     * startDocument call.
     *
     * @param version    The XML version.
     * @param encoding   The IANA encoding name of the document, or null if
     *                   not specified.
     * @param standalone The standalone value, or null if not specified.
     * @param augs     Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
    public void xmlDecl(String version, String encoding, String standalone, Augmentations augs)
        throws XNIException {
        if (fDocumentHandler != null && !fOnlyPassPrefixMappingEvents) {
            fDocumentHandler.xmlDecl(version, encoding, standalone, augs);
        }
    } // xmlDecl(String,String,String)

    /**
     * Notifies of the presence of the DOCTYPE line in the document.
     *
     * @param rootElement The name of the root element.
     * @param publicId    The public identifier if an external DTD or null
     *                    if the external DTD is specified using SYSTEM.
     * @param systemId    The system identifier if an external DTD, null
     *                    otherwise.
     * @param augs     Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
    public void doctypeDecl(String rootElement,
                            String publicId, String systemId, Augmentations augs)
        throws XNIException {
        if (fDocumentHandler != null && !fOnlyPassPrefixMappingEvents) {
            fDocumentHandler.doctypeDecl(rootElement, publicId, systemId, augs);
        }
    } // doctypeDecl(String,String,String)

    /**
     * A comment.
     *
     * @param text The text in the comment.
     * @param augs     Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by application to signal an error.
     */
    public void comment(XMLString text, Augmentations augs) throws XNIException {
        if (fDocumentHandler != null && !fOnlyPassPrefixMappingEvents) {
            fDocumentHandler.comment(text, augs);
        }
    } // comment(XMLString)

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
     * @param augs     Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
    public void processingInstruction(String target, XMLString data, Augmentations augs)
        throws XNIException {
        if (fDocumentHandler != null && !fOnlyPassPrefixMappingEvents) {
            fDocumentHandler.processingInstruction(target, data, augs);
        }
    } // processingInstruction(String,XMLString)

    /**
     * The start of a namespace prefix mapping. This method will only be
     * called when namespace processing is enabled.
     *
     * @param prefix The namespace prefix.
     * @param uri    The URI bound to the prefix.
     * @param augs     Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
    public void startPrefixMapping(String prefix, String uri, Augmentations augs)
        throws XNIException {

        // REVISIT: Should prefix mapping from previous stage in
        //          the pipeline affect the namespaces?

        // call handlers
        if (fDocumentHandler != null) {
            fDocumentHandler.startPrefixMapping(prefix, uri, augs);
        }

    } // startPrefixMapping(String,String)

    /**
     * Binds the namespaces. This method will handle calling the
     * document handler to start the prefix mappings.
     * <p>
     * <strong>Note:</strong> This method makes use of the
     * fAttributeQName variable. Any contents of the variable will
     * be destroyed. Caller should copy the values out of this
     * temporary variable before calling this method.
     *
     * @param element    The name of the element.
     * @param attributes The element attributes.
     * @param augs     Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
    public void startElement(QName element, XMLAttributes attributes, Augmentations augs)
        throws XNIException {

        if (fNamespaces) {
            handleStartElement(element, attributes, augs, false);
        }
        else if (fDocumentHandler != null) {
            fDocumentHandler.startElement(element, attributes, augs);
        }


    } // startElement(QName,XMLAttributes)

    /**
     * An empty element.
     *
     * @param element    The name of the element.
     * @param attributes The element attributes.
     * @param augs     Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
    public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs)
        throws XNIException {

        if (fNamespaces) {
            handleStartElement(element, attributes, augs, true);
            handleEndElement(element, augs, true);
        }
        else if (fDocumentHandler != null) {
            fDocumentHandler.emptyElement(element, attributes, augs);
        }

    } // emptyElement(QName,XMLAttributes)

    /**
     * Character content.
     *
     * @param text The content.
     * @param augs     Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
    public void characters(XMLString text, Augmentations augs) throws XNIException {
        if (fDocumentHandler != null && !fOnlyPassPrefixMappingEvents) {
            fDocumentHandler.characters(text, augs);
        }
    } // characters(XMLString)

    /**
     * Ignorable whitespace. For this method to be called, the document
     * source must have some way of determining that the text containing
     * only whitespace characters should be considered ignorable. For
     * example, the validator can determine if a length of whitespace
     * characters in the document are ignorable based on the element
     * content model.
     *
     * @param text The ignorable whitespace.
     * @param augs     Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
    public void ignorableWhitespace(XMLString text, Augmentations augs) throws XNIException {
        if (fDocumentHandler != null && !fOnlyPassPrefixMappingEvents) {
            fDocumentHandler.ignorableWhitespace(text, augs);
        }
    } // ignorableWhitespace(XMLString)

    /**
     * The end of an element.
     *
     * @param element The name of the element.
     * @param augs     Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
    public void endElement(QName element, Augmentations augs) throws XNIException {

        if (fNamespaces) {
            handleEndElement(element, augs, false);
        }
        else if (fDocumentHandler != null) {
            fDocumentHandler.endElement(element, augs);
        }

    } // endElement(QName)

    /**
     * The end of a namespace prefix mapping. This method will only be
     * called when namespace processing is enabled.
     *
     * @param prefix The namespace prefix.
     * @param augs     Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
    public void endPrefixMapping(String prefix, Augmentations augs) throws XNIException {

        // REVISIT: Should prefix mapping from previous stage in
        //          the pipeline affect the namespaces?

        // call handlers
        if (fDocumentHandler != null) {
            fDocumentHandler.endPrefixMapping(prefix, augs);
        }

    } // endPrefixMapping(String)

    /**
     * The start of a CDATA section.
     * @param augs     Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
    public void startCDATA(Augmentations augs) throws XNIException {
        if (fDocumentHandler != null && !fOnlyPassPrefixMappingEvents) {
            fDocumentHandler.startCDATA(augs);
        }
    } // startCDATA()

    /**
     * The end of a CDATA section.
     * @param augs     Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
    public void endCDATA(Augmentations augs) throws XNIException {
        if (fDocumentHandler != null && !fOnlyPassPrefixMappingEvents) {
            fDocumentHandler.endCDATA(augs);
        }
    } // endCDATA()

    /**
     * The end of the document.
     * @param augs     Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
    public void endDocument(Augmentations augs) throws XNIException {
        if (fDocumentHandler != null && !fOnlyPassPrefixMappingEvents) {
            fDocumentHandler.endDocument(augs);
        }
    } // endDocument()

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
    public void endGeneralEntity(String name, Augmentations augs) throws XNIException {
        if (fDocumentHandler != null && !fOnlyPassPrefixMappingEvents) {
            fDocumentHandler.endGeneralEntity(name, augs);
        }
    } // endEntity(String)

    //
    // Protected methods
    //

    /** Handles start element. */
    protected void handleStartElement(QName element, XMLAttributes attributes,
                                      Augmentations augs,
                                      boolean isEmpty) throws XNIException {

        // add new namespace context
        fNamespaceSupport.pushContext();

        if (element.prefix == XMLSymbols.PREFIX_XMLNS) {
            fErrorReporter.reportError(XMLMessageFormatter.XMLNS_DOMAIN,
                                       "ElementXMLNSPrefix",
                                       new Object[]{element.rawname},
                                       XMLErrorReporter.SEVERITY_FATAL_ERROR);
        }
        
        // search for new namespace bindings
        int length = attributes.getLength();
        for (int i = 0; i < length; i++) {
            String localpart = attributes.getLocalName(i);
            String prefix = attributes.getPrefix(i);
            // when it's of form xmlns="..." or xmlns:prefix="...",
            // it's a namespace declaration. but prefix:xmlns="..." isn't.
            if (prefix == XMLSymbols.PREFIX_XMLNS ||
                prefix == XMLSymbols.EMPTY_STRING && localpart == XMLSymbols.PREFIX_XMLNS) {

                // get the internalized value of this attribute
                String uri = fSymbolTable.addSymbol(attributes.getValue(i));

                // 1. "xmlns" can't be bound to any namespace
                if (prefix == XMLSymbols.PREFIX_XMLNS && localpart == XMLSymbols.PREFIX_XMLNS) {
                    fErrorReporter.reportError(XMLMessageFormatter.XMLNS_DOMAIN,
                                               "CantBindXMLNS",
                                               new Object[]{attributes.getQName(i)},
                                               XMLErrorReporter.SEVERITY_FATAL_ERROR);
                }
                
                // 2. the namespace for "xmlns" can't be bound to any prefix
                if (uri == NamespaceContext.XMLNS_URI) {
                    fErrorReporter.reportError(XMLMessageFormatter.XMLNS_DOMAIN,
                                               "CantBindXMLNS",
                                               new Object[]{attributes.getQName(i)},
                                               XMLErrorReporter.SEVERITY_FATAL_ERROR);
                }
                
                // 3. "xml" can't be bound to any other namespace than it's own
                if (localpart == XMLSymbols.PREFIX_XML) {
                    if (uri != NamespaceContext.XML_URI) {
                        fErrorReporter.reportError(XMLMessageFormatter.XMLNS_DOMAIN,
                                                   "CantBindXML",
                                                   new Object[]{attributes.getQName(i)},
                                                   XMLErrorReporter.SEVERITY_FATAL_ERROR);
                    }
                }
                // 4. the namespace for "xml" can't be bound to any other prefix
                else {
                    if (uri ==NamespaceContext.XML_URI) {
                        fErrorReporter.reportError(XMLMessageFormatter.XMLNS_DOMAIN,
                                                   "CantBindXML",
                                                   new Object[]{attributes.getQName(i)},
                                                   XMLErrorReporter.SEVERITY_FATAL_ERROR);
                    }
                }

                prefix = localpart != XMLSymbols.PREFIX_XMLNS ? localpart : XMLSymbols.EMPTY_STRING;

                // http://www.w3.org/TR/1999/REC-xml-names-19990114/#dt-prefix
                // We should only report an error if there is a prefix,
                // that is, the local part is not "xmlns". -SG
                if (uri == XMLSymbols.EMPTY_STRING && localpart != XMLSymbols.PREFIX_XMLNS) {
                    fErrorReporter.reportError(XMLMessageFormatter.XMLNS_DOMAIN,
                                               "EmptyPrefixedAttName",
                                               new Object[]{attributes.getQName(i)},
                                               XMLErrorReporter.SEVERITY_FATAL_ERROR);
                    continue;
                }

                // declare prefix in context
                fNamespaceSupport.declarePrefix(prefix, uri.length() != 0 ? uri : null);

                // call handler
                if (fDocumentHandler != null) {
                    fDocumentHandler.startPrefixMapping(prefix, uri, augs);
                }
            }
        }

        // bind the element
        String prefix = element.prefix != null
                      ? element.prefix : XMLSymbols.EMPTY_STRING;
        element.uri = fNamespaceSupport.getURI(prefix);
        if (element.prefix == null && element.uri != null) {
            element.prefix = XMLSymbols.EMPTY_STRING;
        }
        if (element.prefix != null && element.uri == null) {
            fErrorReporter.reportError(XMLMessageFormatter.XMLNS_DOMAIN,
                                       "ElementPrefixUnbound",
                                       new Object[]{element.prefix, element.rawname},
                                       XMLErrorReporter.SEVERITY_FATAL_ERROR);
        }

        // bind the attributes
        for (int i = 0; i < length; i++) {
            attributes.getName(i, fAttributeQName);
            String aprefix = fAttributeQName.prefix != null
                           ? fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
            String arawname = fAttributeQName.rawname;
            if (arawname == XMLSymbols.PREFIX_XMLNS) {
                fAttributeQName.uri = fNamespaceSupport.getURI(XMLSymbols.PREFIX_XMLNS);
                attributes.setName(i, fAttributeQName);
            }
            else if (aprefix != XMLSymbols.EMPTY_STRING) {
                fAttributeQName.uri = fNamespaceSupport.getURI(aprefix);
                if (fAttributeQName.uri == null) {
                    fErrorReporter.reportError(XMLMessageFormatter.XMLNS_DOMAIN,
                                               "AttributePrefixUnbound",
                                               new Object[]{aprefix, arawname},
                                               XMLErrorReporter.SEVERITY_FATAL_ERROR);
                }
                attributes.setName(i, fAttributeQName);
            }
        }

        // verify that duplicate attributes don't exist
        // Example: <foo xmlns:a='NS' xmlns:b='NS' a:attr='v1' b:attr='v2'/>
        int attrCount = attributes.getLength();
        for (int i = 0; i < attrCount - 1; i++) {
            String alocalpart = attributes.getLocalName(i);
            String auri = attributes.getURI(i);
            for (int j = i + 1; j < attrCount; j++) {
                String blocalpart = attributes.getLocalName(j);
                String buri = attributes.getURI(j);
                if (alocalpart == blocalpart && auri == buri) {
                    fErrorReporter.reportError(XMLMessageFormatter.XMLNS_DOMAIN,
                                               "AttributeNSNotUnique",
                                               new Object[]{element.rawname,alocalpart, auri},
                                               XMLErrorReporter.SEVERITY_FATAL_ERROR);
                }
            }
        }

        // call handler
        if (fDocumentHandler != null && !fOnlyPassPrefixMappingEvents) {
            if (isEmpty) {
                fDocumentHandler.emptyElement(element, attributes, augs);
            }
            else {
                fDocumentHandler.startElement(element, attributes, augs);
            }
        }


    } // handleStartElement(QName,XMLAttributes,boolean)

    /** Handles end element. */
    protected void handleEndElement(QName element, Augmentations augs, boolean isEmpty)
        throws XNIException {

        // bind element
        String eprefix = element.prefix != null ? element.prefix : XMLSymbols.EMPTY_STRING;
        element.uri = fNamespaceSupport.getURI(eprefix);
        if (element.uri != null) {
            element.prefix = eprefix;
        }

        // call handlers
        if (fDocumentHandler != null && !fOnlyPassPrefixMappingEvents) {
            if (!isEmpty) {
                fDocumentHandler.endElement(element, augs);
            }
        }

        // end prefix mappings
        if (fDocumentHandler != null) {
            int count = fNamespaceSupport.getDeclaredPrefixCount();
            for (int i = count - 1; i >= 0; i--) {
                String prefix = fNamespaceSupport.getDeclaredPrefixAt(i);
                fDocumentHandler.endPrefixMapping(prefix, augs);
            }
        }

        // pop context
        fNamespaceSupport.popContext();

    } // handleEndElement(QName,boolean)

} // class XMLNamespaceBinder
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16272.java