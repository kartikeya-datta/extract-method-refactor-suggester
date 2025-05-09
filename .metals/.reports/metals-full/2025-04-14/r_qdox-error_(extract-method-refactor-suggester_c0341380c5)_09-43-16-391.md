error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8948.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8948.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,50]

error in qdox parser
file content:
```java
offset: 50
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8948.java
text:
```scala
"org.apache.xerces.parsers.XML11DTDConfiguration",@@

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
package org.apache.xerces.dom;

import java.lang.ref.SoftReference;

import org.apache.xerces.impl.RevalidationHandler;
import org.apache.xerces.impl.dtd.XMLDTDLoader;
import org.apache.xerces.parsers.DOMParserImpl;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xml.serialize.DOMSerializerImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSParser;
import org.w3c.dom.ls.LSSerializer;

/**
 * The DOMImplementation class is description of a particular
 * implementation of the Document Object Model. As such its data is
 * static, shared by all instances of this implementation.
 * <P>
 * The DOM API requires that it be a real object rather than static
 * methods. However, there's nothing that says it can't be a singleton,
 * so that's how I've implemented it.
 * <P>
 * This particular class, along with CoreDocumentImpl, supports the DOM
 * Core and Load/Save (Experimental). Optional modules are supported by
 * the more complete DOMImplementation class along with DocumentImpl.
 * 
 * @xerces.internal
 * 
 * @version $Id$
 * @since PR-DOM-Level-1-19980818.
 */
public class CoreDOMImplementationImpl
	implements DOMImplementation, DOMImplementationLS {
    
    //
    // Data
    //

    // validator pools
    private static final int SIZE = 2;
    
    private SoftReference schemaValidators[] = new SoftReference[SIZE];
    private SoftReference xml10DTDValidators[] = new SoftReference[SIZE];
    private SoftReference xml11DTDValidators[] = new SoftReference[SIZE];
    
    private int freeSchemaValidatorIndex = -1;
    private int freeXML10DTDValidatorIndex = -1;
    private int freeXML11DTDValidatorIndex = -1;
    
    private int schemaValidatorsCurrentSize = SIZE;
    private int xml10DTDValidatorsCurrentSize = SIZE;
    private int xml11DTDValidatorsCurrentSize = SIZE;
    
    private SoftReference xml10DTDLoaders[] = new SoftReference[SIZE];
    private SoftReference xml11DTDLoaders[] = new SoftReference[SIZE];
    
    private int freeXML10DTDLoaderIndex = -1;
    private int freeXML11DTDLoaderIndex = -1;
    
    private int xml10DTDLoaderCurrentSize = SIZE;
    private int xml11DTDLoaderCurrentSize = SIZE;

    // Document and doctype counter.  Used to assign order to documents and
    // doctypes without owners, on an demand basis.   Used for
    // compareDocumentPosition
    private int docAndDoctypeCounter = 0;
    
	// static
	/** Dom implementation singleton. */
	static final CoreDOMImplementationImpl singleton = new CoreDOMImplementationImpl();
	
	//
	// Public methods
	//
	/** NON-DOM: Obtain and return the single shared object */
	public static DOMImplementation getDOMImplementation() {
		return singleton;
	}
	//
	// DOMImplementation methods
	//
	/**
	 * Test if the DOM implementation supports a specific "feature" --
	 * currently meaning language and level thereof.
	 *
	 * @param feature The package name of the feature to test.
	 * In Level 1, supported values are "HTML" and "XML" (case-insensitive).
	 * At this writing, org.apache.xerces.dom supports only XML.
	 *
	 * @param version The version number of the feature being tested.
	 * This is interpreted as "Version of the DOM API supported for the
	 * specified Feature", and in Level 1 should be "1.0"
	 *
	 * @return true iff this implementation is compatible with the specified
	 * feature and version.
	 */
	public boolean hasFeature(String feature, String version) {
	    
	    boolean anyVersion = version == null || version.length() == 0;
	    
	    // check if Xalan implementation is around and if yes report true for supporting
	    // XPath API
	    // if a plus sign "+" is prepended to any feature name, implementations 
	    // are considered in which the specified feature may not be directly 
	    // castable DOMImplementation.getFeature(feature, version). Without a 
	    // plus, only features whose interfaces are directly castable are considered.
	    if ((feature.equalsIgnoreCase("+XPath"))       
	        && (anyVersion || version.equals("3.0"))) {
	        try {
	            Class xpathClass = ObjectFactory.findProviderClass(
	                "org.apache.xpath.domapi.XPathEvaluatorImpl",
	                ObjectFactory.findClassLoader(), true);
                
                // Check if the DOM XPath implementation implements
                // the interface org.w3c.dom.XPathEvaluator
                Class interfaces[] = xpathClass.getInterfaces();
                for (int i = 0; i < interfaces.length; i++) {
                    if (interfaces[i].getName().equals(
                        "org.w3c.dom.xpath.XPathEvaluator")) {
                        return true;
                    }
                }
	        } catch (Exception e) {
	            return false;
	        }
	        return true;
	    }
	    if (feature.startsWith("+")) {
	        feature = feature.substring(1);
	    }
	    return (
	        feature.equalsIgnoreCase("Core")
	            && (anyVersion
 version.equals("1.0")
 version.equals("2.0")
 version.equals("3.0")))
 (feature.equalsIgnoreCase("XML")
	            && (anyVersion
 version.equals("1.0")
 version.equals("2.0")
 version.equals("3.0")))
 (feature.equalsIgnoreCase("XMLVersion")
	            && (anyVersion
 version.equals("1.0")
 version.equals("1.1")))
 (feature.equalsIgnoreCase("LS")
	            && (anyVersion 
 version.equals("3.0")))
 (feature.equalsIgnoreCase("ElementTraversal")
	            && (anyVersion
 version.equals("1.0")));
	} // hasFeature(String,String):boolean


	/**
	 * Introduced in DOM Level 2. <p>
	 *
	 * Creates an empty DocumentType node.
	 *
	 * @param qualifiedName The qualified name of the document type to be created.
	 * @param publicID The document type public identifier.
	 * @param systemID The document type system identifier.
	 * @since WD-DOM-Level-2-19990923
	 */
	public DocumentType createDocumentType( String qualifiedName,
                                    String publicID, String systemID) {
		// REVISIT: this might allow creation of invalid name for DOCTYPE
		//          xmlns prefix.
		//          also there is no way for a user to turn off error checking.
		checkQName(qualifiedName);
		return new DocumentTypeImpl(null, qualifiedName, publicID, systemID);
	}

    final void checkQName(String qname){
        int index = qname.indexOf(':');
        int lastIndex = qname.lastIndexOf(':');
        int length = qname.length();

        // it is an error for NCName to have more than one ':'
        // check if it is valid QName [Namespace in XML production 6]
        if (index == 0 || index == length - 1 || lastIndex != index) {
            String msg =
                DOMMessageFormatter.formatMessage(
                    DOMMessageFormatter.DOM_DOMAIN,
                    "NAMESPACE_ERR",
                    null);
            throw new DOMException(DOMException.NAMESPACE_ERR, msg);
        }
        int start = 0;
        // Namespace in XML production [6]
        if (index > 0) {
            // check that prefix is NCName
            if (!XMLChar.isNCNameStart(qname.charAt(start))) {
                String msg =
                    DOMMessageFormatter.formatMessage(
                        DOMMessageFormatter.DOM_DOMAIN,
                        "INVALID_CHARACTER_ERR",
                        null);
                throw new DOMException(DOMException.INVALID_CHARACTER_ERR, msg);
            }
            for (int i = 1; i < index; i++) {
                if (!XMLChar.isNCName(qname.charAt(i))) {
                    String msg =
                        DOMMessageFormatter.formatMessage(
                            DOMMessageFormatter.DOM_DOMAIN,
                            "INVALID_CHARACTER_ERR",
                            null);
                    throw new DOMException(
                        DOMException.INVALID_CHARACTER_ERR,
                        msg);
                }
            }
            start = index + 1;
        }

        // check local part
        if (!XMLChar.isNCNameStart(qname.charAt(start))) {
            // REVISIT: add qname parameter to the message
            String msg =
                DOMMessageFormatter.formatMessage(
                    DOMMessageFormatter.DOM_DOMAIN,
                    "INVALID_CHARACTER_ERR",
                    null);
            throw new DOMException(DOMException.INVALID_CHARACTER_ERR, msg);
        }
        for (int i = start + 1; i < length; i++) {
            if (!XMLChar.isNCName(qname.charAt(i))) {
                String msg =
                    DOMMessageFormatter.formatMessage(
                        DOMMessageFormatter.DOM_DOMAIN,
                        "INVALID_CHARACTER_ERR",
                        null);
                throw new DOMException(DOMException.INVALID_CHARACTER_ERR, msg);
            }
        }
    }


	/**
	 * Introduced in DOM Level 2. <p>
	 *
	 * Creates an XML Document object of the specified type with its document
	 * element.
	 *
	 * @param namespaceURI     The namespace URI of the document
	 *                         element to create, or null.
	 * @param qualifiedName    The qualified name of the document
	 *                         element to create.
	 * @param doctype          The type of document to be created or null.<p>
	 *
	 *                         When doctype is not null, its
	 *                         Node.ownerDocument attribute is set to
	 *                         the document being created.
	 * @return Document        A new Document object.
	 * @throws DOMException    WRONG_DOCUMENT_ERR: Raised if doctype has
	 *                         already been used with a different document.
	 * @since WD-DOM-Level-2-19990923
	 */
	public Document createDocument(
		String namespaceURI,
		String qualifiedName,
		DocumentType doctype)
		throws DOMException {
		if (doctype != null && doctype.getOwnerDocument() != null) {
			String msg =
				DOMMessageFormatter.formatMessage(
					DOMMessageFormatter.DOM_DOMAIN,
					"WRONG_DOCUMENT_ERR",
					null);
			throw new DOMException(DOMException.WRONG_DOCUMENT_ERR, msg);
		}
		CoreDocumentImpl doc = createDocument(doctype);
		// If namespaceURI and qualifiedName are null return a Document with no document element.
		if (qualifiedName != null || namespaceURI != null) {
		    Element e = doc.createElementNS(namespaceURI, qualifiedName);
		    doc.appendChild(e);
		}
		return doc;
	}
	
	protected CoreDocumentImpl createDocument(DocumentType doctype) {
	    return new CoreDocumentImpl(doctype);
	}

	/**
	 * DOM Level 3 WD - Experimental.
	 */
	public Object getFeature(String feature, String version) {
	    if (singleton.hasFeature(feature, version)) {
	        if ((feature.equalsIgnoreCase("+XPath"))) {
	            try {
	                Class xpathClass = ObjectFactory.findProviderClass(
	                    "org.apache.xpath.domapi.XPathEvaluatorImpl",
	                    ObjectFactory.findClassLoader(), true);
	                
	                // Check if the DOM XPath implementation implements
	                // the interface org.w3c.dom.XPathEvaluator
	                Class interfaces[] = xpathClass.getInterfaces();
	                for (int i = 0; i < interfaces.length; i++) {
	                    if (interfaces[i].getName().equals(
	                        "org.w3c.dom.xpath.XPathEvaluator")) {
	                        return xpathClass.newInstance();
	                    }
	                }
	            } catch (Exception e) {
	                return null;
	            }
	        } else {
	            return singleton;
	        }
	    }
	    return null;
	}

	// DOM L3 LS

	/**
	 * DOM Level 3 LS CR - Experimental.
     * Create a new <code>LSParser</code>. The newly constructed parser may
     * then be configured by means of its <code>DOMConfiguration</code>
     * object, and used to parse documents by means of its <code>parse</code>
     *  method.
     * @param mode  The <code>mode</code> argument is either
     *   <code>MODE_SYNCHRONOUS</code> or <code>MODE_ASYNCHRONOUS</code>, if
     *   <code>mode</code> is <code>MODE_SYNCHRONOUS</code> then the
     *   <code>LSParser</code> that is created will operate in synchronous
     *   mode, if it's <code>MODE_ASYNCHRONOUS</code> then the
     *   <code>LSParser</code> that is created will operate in asynchronous
     *   mode.
     * @param schemaType  An absolute URI representing the type of the schema
     *   language used during the load of a <code>Document</code> using the
     *   newly created <code>LSParser</code>. Note that no lexical checking
     *   is done on the absolute URI. In order to create a
     *   <code>LSParser</code> for any kind of schema types (i.e. the
     *   LSParser will be free to use any schema found), use the value
     *   <code>null</code>.
     * <p ><b>Note:</b>    For W3C XML Schema [<a href='http://www.w3.org/TR/2001/REC-xmlschema-1-20010502/'>XML Schema Part 1</a>]
     *   , applications must use the value
     *   <code>"http://www.w3.org/2001/XMLSchema"</code>. For XML DTD [<a href='http://www.w3.org/TR/2000/REC-xml-20001006'>XML 1.0</a>],
     *   applications must use the value
     *   <code>"http://www.w3.org/TR/REC-xml"</code>. Other Schema languages
     *   are outside the scope of the W3C and therefore should recommend an
     *   absolute URI in order to use this method.
     * @return  The newly created <code>LSParser</code> object. This
     *   <code>LSParser</code> is either synchronous or asynchronous
     *   depending on the value of the <code>mode</code> argument.
     * <p ><b>Note:</b>    By default, the newly created <code>LSParser</code>
     *    does not contain a <code>DOMErrorHandler</code>, i.e. the value of
     *   the "<a href='http://www.w3.org/TR/2003/WD-DOM-Level-3-Core-20030609/core.html#parameter-error-handler'>
     *   error-handler</a>" configuration parameter is <code>null</code>. However, implementations
     *   may provide a default error handler at creation time. In that case,
     *   the initial value of the <code>"error-handler"</code> configuration
     *   parameter on the new created <code>LSParser</code> contains a
     *   reference to the default error handler.
     * @exception DOMException
     *    NOT_SUPPORTED_ERR: Raised if the requested mode or schema type is
     *   not supported.
	 */
    public LSParser createLSParser(short mode, String schemaType)
		throws DOMException {
		if (mode != DOMImplementationLS.MODE_SYNCHRONOUS || (schemaType !=null &&
		   !"http://www.w3.org/2001/XMLSchema".equals(schemaType) &&
			!"http://www.w3.org/TR/REC-xml".equals(schemaType))) {
			String msg =
				DOMMessageFormatter.formatMessage(
					DOMMessageFormatter.DOM_DOMAIN,
					"NOT_SUPPORTED_ERR",
					null);
			throw new DOMException(DOMException.NOT_SUPPORTED_ERR, msg);
		}
		if (schemaType != null
			&& schemaType.equals("http://www.w3.org/TR/REC-xml")) {
			return new DOMParserImpl(
				"org.apache.xerces.parsers.DTDConfiguration",
				schemaType);
		}
		else {
			// create default parser configuration validating against XMLSchemas
			return new DOMParserImpl(
				"org.apache.xerces.parsers.XIncludeAwareParserConfiguration",
				schemaType);
		}
	}

    /**
     * DOM Level 3 LS CR - Experimental.
     * Create a new <code>LSSerializer</code> object.
     * @return The newly created <code>LSSerializer</code> object.
     * <p ><b>Note:</b>    By default, the newly created
     * <code>LSSerializer</code> has no <code>DOMErrorHandler</code>,
     * i.e. the value of the <code>"error-handler"</code> configuration
     * parameter is <code>null</code>. However, implementations may
     * provide a default error handler at creation time. In that case, the
     * initial value of the <code>"error-handler"</code> configuration
     * parameter on the new created <code>LSSerializer</code> contains a
     * reference to the default error handler.
     */
    public LSSerializer createLSSerializer() {
        try {
            Class serializerClass = ObjectFactory.findProviderClass(
                "org.apache.xml.serializer.dom3.LSSerializerImpl",
                ObjectFactory.findClassLoader(), true);
            return (LSSerializer) serializerClass.newInstance();
        }
        catch (Exception e) {}
        // Fall back to Xerces' deprecated serializer if 
        // the Xalan based serializer is unavailable.
        return new DOMSerializerImpl();
    }
    
	/**
	 * DOM Level 3 LS CR - Experimental.
	 * Create a new empty input source.
	 * @return  The newly created input object.
	 */
	public LSInput createLSInput() {
		return new DOMInputImpl();
	}

	//
	// Protected methods
	//
	/** NON-DOM: retrieve validator. */
	synchronized RevalidationHandler getValidator(String schemaType, String xmlVersion) {
        if (schemaType == XMLGrammarDescription.XML_SCHEMA) {
            // create new validator - we should not attempt
            // to restrict the number of validation handlers being
            // requested
            while (freeSchemaValidatorIndex >= 0) {
                // return first available validator
                SoftReference ref = schemaValidators[freeSchemaValidatorIndex];
                RevalidationHandlerHolder holder = (RevalidationHandlerHolder) ref.get();
                if (holder != null && holder.handler != null) {
                    RevalidationHandler val = holder.handler;
                    holder.handler = null;
                    --freeSchemaValidatorIndex;
                    return val;
                }
                schemaValidators[freeSchemaValidatorIndex--] = null;
            }
            return (RevalidationHandler) (ObjectFactory
                    .newInstance(
                        "org.apache.xerces.impl.xs.XMLSchemaValidator",
                        ObjectFactory.findClassLoader(),
                        true));
        }
        else if(schemaType == XMLGrammarDescription.XML_DTD) {
            // return an instance of XML11DTDValidator
            if ("1.1".equals(xmlVersion)) {
                while (freeXML11DTDValidatorIndex >= 0) {
                    // return first available validator
                    SoftReference ref = xml11DTDValidators[freeXML11DTDValidatorIndex];
                    RevalidationHandlerHolder holder = (RevalidationHandlerHolder) ref.get();
                    if (holder != null && holder.handler != null) {
                        RevalidationHandler val = holder.handler;
                        holder.handler = null;
                        --freeXML11DTDValidatorIndex;
                        return val;
                    }
                    xml11DTDValidators[freeXML11DTDValidatorIndex--] = null;
                }
                return (RevalidationHandler) (ObjectFactory
                        .newInstance(
                                "org.apache.xerces.impl.dtd.XML11DTDValidator",
                                ObjectFactory.findClassLoader(),
                                true));
            }
            // return an instance of XMLDTDValidator
            else {
                while (freeXML10DTDValidatorIndex >= 0) {
                    // return first available validator
                    SoftReference ref = xml10DTDValidators[freeXML10DTDValidatorIndex];
                    RevalidationHandlerHolder holder = (RevalidationHandlerHolder) ref.get();
                    if (holder != null && holder.handler != null) {
                        RevalidationHandler val = holder.handler;
                        holder.handler = null;
                        --freeXML10DTDValidatorIndex;
                        return val;
                    }
                    xml10DTDValidators[freeXML10DTDValidatorIndex--] = null;
                }
                return (RevalidationHandler) (ObjectFactory
                        .newInstance(
                            "org.apache.xerces.impl.dtd.XMLDTDValidator",
                            ObjectFactory.findClassLoader(),
                            true));
            }
        }
        return null;
	}

	/** NON-DOM: release validator */
	synchronized void releaseValidator(String schemaType, String xmlVersion,
	        RevalidationHandler validator) {
	    if (schemaType == XMLGrammarDescription.XML_SCHEMA) {
	        ++freeSchemaValidatorIndex;
	        if (schemaValidators.length == freeSchemaValidatorIndex) {
	            // resize size of the validators
	            schemaValidatorsCurrentSize += SIZE;
	            SoftReference newarray[] =  new SoftReference[schemaValidatorsCurrentSize];
	            System.arraycopy(schemaValidators, 0, newarray, 0, schemaValidators.length);
	            schemaValidators = newarray;
	        }
	        SoftReference ref = schemaValidators[freeSchemaValidatorIndex];
	        if (ref != null) {
	            RevalidationHandlerHolder holder = (RevalidationHandlerHolder) ref.get();
	            if (holder != null) {
	                holder.handler = validator;
	                return;
	            }
	        }
	        schemaValidators[freeSchemaValidatorIndex] = new SoftReference(new RevalidationHandlerHolder(validator));
	    }
	    else if (schemaType == XMLGrammarDescription.XML_DTD) {
	        // release an instance of XML11DTDValidator
	        if ("1.1".equals(xmlVersion)) {
	            ++freeXML11DTDValidatorIndex;
	            if (xml11DTDValidators.length == freeXML11DTDValidatorIndex) {
	                // resize size of the validators
	                xml11DTDValidatorsCurrentSize += SIZE;
	                SoftReference [] newarray = new SoftReference[xml11DTDValidatorsCurrentSize];
	                System.arraycopy(xml11DTDValidators, 0, newarray, 0, xml11DTDValidators.length);
	                xml11DTDValidators = newarray;
	            }
	            SoftReference ref = xml11DTDValidators[freeXML11DTDValidatorIndex];
	            if (ref != null) {
	                RevalidationHandlerHolder holder = (RevalidationHandlerHolder) ref.get();
	                if (holder != null) {
	                    holder.handler = validator;
	                    return;
	                }
	            }
	            xml11DTDValidators[freeXML11DTDValidatorIndex] = new SoftReference(new RevalidationHandlerHolder(validator));
	        }
	        // release an instance of XMLDTDValidator
	        else {
	            ++freeXML10DTDValidatorIndex;
	            if (xml10DTDValidators.length == freeXML10DTDValidatorIndex) {
	                // resize size of the validators
	                xml10DTDValidatorsCurrentSize += SIZE;
	                SoftReference [] newarray = new SoftReference[xml10DTDValidatorsCurrentSize];
	                System.arraycopy(xml10DTDValidators, 0, newarray, 0, xml10DTDValidators.length);
	                xml10DTDValidators = newarray;
	            }
	            SoftReference ref = xml10DTDValidators[freeXML10DTDValidatorIndex];
	            if (ref != null) {
	                RevalidationHandlerHolder holder = (RevalidationHandlerHolder) ref.get();
	                if (holder != null) {
	                    holder.handler = validator;
	                    return;
	                }
	            }
	            xml10DTDValidators[freeXML10DTDValidatorIndex] = new SoftReference(new RevalidationHandlerHolder(validator));
	        }
	    }
	}
    
    /** NON-DOM: retrieve DTD loader */
    synchronized final XMLDTDLoader getDTDLoader(String xmlVersion) {
        // return an instance of XML11DTDProcessor
        if ("1.1".equals(xmlVersion)) {
            while (freeXML11DTDLoaderIndex >= 0) {
                // return first available DTD loader
                SoftReference ref = xml11DTDLoaders[freeXML11DTDLoaderIndex];
                XMLDTDLoaderHolder holder = (XMLDTDLoaderHolder) ref.get();
                if (holder != null && holder.loader != null) {
                    XMLDTDLoader val = holder.loader;
                    holder.loader = null;
                    --freeXML11DTDLoaderIndex;
                    return val;
                }
                xml11DTDLoaders[freeXML11DTDLoaderIndex--] = null;
            }
            return (XMLDTDLoader) (ObjectFactory
                    .newInstance(
                        "org.apache.xerces.impl.dtd.XML11DTDProcessor",
                        ObjectFactory.findClassLoader(),
                        true));
        }
        // return an instance of XMLDTDLoader
        else {
            while (freeXML10DTDLoaderIndex >= 0) {
                // return first available DTD loader
                SoftReference ref = xml10DTDLoaders[freeXML10DTDLoaderIndex];
                XMLDTDLoaderHolder holder = (XMLDTDLoaderHolder) ref.get();
                if (holder != null && holder.loader != null) {
                    XMLDTDLoader val = holder.loader;
                    holder.loader = null;
                    --freeXML10DTDLoaderIndex;
                    return val;
                }
                xml10DTDLoaders[freeXML10DTDLoaderIndex--] = null;
            }
            return new XMLDTDLoader();
        }
    }
    
    /** NON-DOM: release DTD loader */
    synchronized final void releaseDTDLoader(String xmlVersion, XMLDTDLoader loader) {
        // release an instance of XMLDTDLoader
        if ("1.1".equals(xmlVersion)) {
            ++freeXML11DTDLoaderIndex;
            if (xml11DTDLoaders.length == freeXML11DTDLoaderIndex) {
                // resize size of the DTD loaders
                xml11DTDLoaderCurrentSize += SIZE;
                SoftReference [] newarray = new SoftReference[xml11DTDLoaderCurrentSize];
                System.arraycopy(xml11DTDLoaders, 0, newarray, 0, xml11DTDLoaders.length);
                xml11DTDLoaders = newarray;
            }
            SoftReference ref = xml11DTDLoaders[freeXML11DTDLoaderIndex];
            if (ref != null) {
                XMLDTDLoaderHolder holder = (XMLDTDLoaderHolder) ref.get();
                if (holder != null) {
                    holder.loader = loader;
                    return;
                }
            }
            xml11DTDLoaders[freeXML11DTDLoaderIndex] = new SoftReference(new XMLDTDLoaderHolder(loader));
        }
        // release an instance of XMLDTDLoader
        else {
            ++freeXML10DTDLoaderIndex;
            if (xml10DTDLoaders.length == freeXML10DTDLoaderIndex) {
                // resize size of the DTD loaders
                xml10DTDLoaderCurrentSize += SIZE;
                SoftReference [] newarray = new SoftReference[xml10DTDLoaderCurrentSize];
                System.arraycopy(xml10DTDLoaders, 0, newarray, 0, xml10DTDLoaders.length);
                xml10DTDLoaders = newarray;
            }
            SoftReference ref = xml10DTDLoaders[freeXML10DTDLoaderIndex];
            if (ref != null) {
                XMLDTDLoaderHolder holder = (XMLDTDLoaderHolder) ref.get();
                if (holder != null) {
                    holder.loader = loader;
                    return;
                }
            }
            xml10DTDLoaders[freeXML10DTDLoaderIndex] = new SoftReference(new XMLDTDLoaderHolder(loader));
        }
    }
    
	/** NON-DOM:  increment document/doctype counter */
	protected synchronized int assignDocumentNumber() {
	    return ++docAndDoctypeCounter;
	}
    
	/** NON-DOM:  increment document/doctype counter */
	protected synchronized int assignDocTypeNumber() {
	    return ++docAndDoctypeCounter;
	}
	
	/**
     * DOM Level 3 LS CR - Experimental.
	 *
	 * Create a new empty output destination object where
	 * <code>LSOutput.characterStream</code>,
	 * <code>LSOutput.byteStream</code>, <code>LSOutput.systemId</code>,
	 * <code>LSOutput.encoding</code> are null.
	 * @return  The newly created output object.
	 */
	public LSOutput createLSOutput() {
	    return new DOMOutputImpl();
	}
    
    /**
     * A holder for RevalidationHandlers. This allows us to reuse
     * SoftReferences which haven't yet been cleared by the garbage
     * collector.
     */
    static final class RevalidationHandlerHolder {
        RevalidationHandlerHolder(RevalidationHandler handler) {
            this.handler = handler;
        }
        RevalidationHandler handler;
    }
    
    /**
     * A holder for XMLDTDLoaders. This allows us to reuse SoftReferences 
     * which haven't yet been cleared by the garbage collector.
     */
    static final class XMLDTDLoaderHolder {
        XMLDTDLoaderHolder(XMLDTDLoader loader) {
            this.loader = loader;
        }
        XMLDTDLoader loader;
    }

} // class DOMImplementationImpl
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8948.java