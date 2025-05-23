error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9718.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9718.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9718.java
text:
```scala
public final Q@@Name name = new QName();

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

import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;

/**
 * The XMLAttributesImpl class is an implementation of the XMLAttributes
 * interface which defines a collection of attributes for an element. 
 * In the parser, the document source would scan the entire start element 
 * and collect the attributes. The attributes are communicated to the
 * document handler in the startElement method.
 * <p>
 * The attributes are read-write so that subsequent stages in the document
 * pipeline can modify the values or change the attributes that are
 * propogated to the next stage.
 *
 * @see org.apache.xerces.xni.XMLDocumentHandler#startElement
 *
 * @author Andy Clark, IBM 
 * @author Elena Litani, IBM
 * @author Michael Glavassevich, IBM
 *
 * @version $Id$
 */
public class XMLAttributesImpl
    implements XMLAttributes {

    //
    // Constants
    //
    
    /** Default table size. */
    protected static final int TABLE_SIZE = 101;
    
    /** 
     * Threshold at which an instance is treated
     * as a large attribute list.
     */
    protected static final int SIZE_LIMIT = 20;
    
    //
    // Data
    //

    // features

    /** Namespaces. */
    protected boolean fNamespaces = true;

    // data

    /** 
     * Usage count for the attribute table view. 
     * Incremented each time all attributes are removed
     * when the attribute table view is in use.
     */
    protected int fLargeCount = 1;
    
    /** Attribute count. */
    protected int fLength;

    /** Attribute information. */
    protected Attribute[] fAttributes = new Attribute[4];
    
    /** 
     * Hashtable of attribute information. 
     * Provides an alternate view of the attribute specification. 
     */
    protected Attribute[] fAttributeTableView;
    
    /**
     * Tracks whether each chain in the hash table is stale
     * with respect to the current state of this object.
     * A chain is stale if its state is not the same as the number
     * of times the attribute table view has been used.
     */
    protected int[] fAttributeTableViewChainState;
    
    /**
     * Actual number of buckets in the table view.
     */
    protected int fTableViewBuckets;
    
    /**
     * Indicates whether the table view contains consistent data.
     */
    protected boolean fIsTableViewConsistent;

    //
    // Constructors
    //

    /** Default constructor. */
    public XMLAttributesImpl() {
        this(TABLE_SIZE);
    }
    
    /**
     * @param tableSize initial size of table view
     */
    public XMLAttributesImpl(int tableSize) {
        fTableViewBuckets = tableSize;
        for (int i = 0; i < fAttributes.length; i++) {
            fAttributes[i] = new Attribute();
        }
    } // <init>()

    //
    // Public methods
    //

    /** 
     * Sets whether namespace processing is being performed. This state
     * is needed to return the correct value from the getLocalName method.
     *
     * @param namespaces True if namespace processing is turned on.
     *
     * @see #getLocalName
     */
    public void setNamespaces(boolean namespaces) {
        fNamespaces = namespaces;
    } // setNamespaces(boolean)

    //
    // XMLAttributes methods
    //

    /**
     * Adds an attribute. The attribute's non-normalized value of the
     * attribute will have the same value as the attribute value until
     * set using the <code>setNonNormalizedValue</code> method. Also,
     * the added attribute will be marked as specified in the XML instance
     * document unless set otherwise using the <code>setSpecified</code>
     * method.
     * <p>
     * <strong>Note:</strong> If an attribute of the same name already
     * exists, the old values for the attribute are replaced by the new
     * values.
     * 
     * @param name  The attribute name.
     * @param type  The attribute type. The type name is determined by
     *                  the type specified for this attribute in the DTD.
     *                  For example: "CDATA", "ID", "NMTOKEN", etc. However,
     *                  attributes of type enumeration will have the type
     *                  value specified as the pipe ('|') separated list of
     *                  the enumeration values prefixed by an open 
     *                  parenthesis and suffixed by a close parenthesis.
     *                  For example: "(true|false)".
     * @param value The attribute value.
     * 
     * @return Returns the attribute index.
     *
     * @see #setNonNormalizedValue
     * @see #setSpecified
     */
    public int addAttribute(QName name, String type, String value) {

        int index;
        if (fLength < SIZE_LIMIT) {
            index = name.uri != null && !name.uri.equals("") 
                ? getIndexFast(name.uri, name.localpart)
                : getIndexFast(name.rawname);

            if (index == -1) {
                index = fLength;
                if (fLength++ == fAttributes.length) {
                    Attribute[] attributes = new Attribute[fAttributes.length + 4];
                    System.arraycopy(fAttributes, 0, attributes, 0, fAttributes.length);
                    for (int i = fAttributes.length; i < attributes.length; i++) {
                        attributes[i] = new Attribute();
                    }
                    fAttributes = attributes;
                }
            }
        }
        else if (name.uri == null || 
            name.uri.length() == 0 || 
            (index = getIndexFast(name.uri, name.localpart)) == -1) {
            
            /**
             * If attributes were removed from the list after the table
             * becomes in use this isn't reflected in the table view. It's
             * assumed that once a user starts removing attributes they're 
             * not likely to add more. We only make the view consistent if
             * the user of this class adds attributes, removes them, and
             * then adds more.
             */
            if (!fIsTableViewConsistent || fLength == SIZE_LIMIT) {
                prepareAndPopulateTableView();
                fIsTableViewConsistent = true;
            }

            int bucket = getTableViewBucket(name.rawname); 
		
            // The chain is stale. 
            // This must be a unique attribute.
            if (fAttributeTableViewChainState[bucket] != fLargeCount) {
                index = fLength;
                if (fLength++ == fAttributes.length) {
                    Attribute[] attributes = new Attribute[fAttributes.length << 1];
                    System.arraycopy(fAttributes, 0, attributes, 0, fAttributes.length);
                    for (int i = fAttributes.length; i < attributes.length; i++) {
                        attributes[i] = new Attribute();
                    }
                    fAttributes = attributes;
                }
			
                // Update table view.
                fAttributeTableViewChainState[bucket] = fLargeCount;
                fAttributes[index].next = null;
                fAttributeTableView[bucket] = fAttributes[index];
            }
            // This chain is active. 
            // We need to check if any of the attributes has the same rawname.
            else {
                // Search the table.
                Attribute found = fAttributeTableView[bucket];
                while (found != null) {
                    if (found.name.rawname == name.rawname) {
                        break;
                    }
                    found = found.next;
                }
                // This attribute is unique.
                if (found == null) {
                    index = fLength;
                    if (fLength++ == fAttributes.length) {
                        Attribute[] attributes = new Attribute[fAttributes.length << 1];
                        System.arraycopy(fAttributes, 0, attributes, 0, fAttributes.length);
                        for (int i = fAttributes.length; i < attributes.length; i++) {
                            attributes[i] = new Attribute();
                        }
                        fAttributes = attributes;
                    }
                
                    // Update table view
                    fAttributes[index].next = fAttributeTableView[bucket];
                    fAttributeTableView[bucket] = fAttributes[index];
                }
                // Duplicate. We still need to find the index.
                else {
                    index = getIndexFast(name.rawname);
                }
            }
        }          
        
        // set values
        Attribute attribute = fAttributes[index];
        attribute.name.setValues(name);
        attribute.type = type;
        attribute.value = value;
        attribute.nonNormalizedValue = value;
        attribute.specified = false;
            
        // clear augmentations
        attribute.augs.removeAllItems();

        return index;

    } // addAttribute(QName,String,XMLString)

    /** 
     * Removes all of the attributes. This method will also remove all
     * entities associated to the attributes.
     */
    public void removeAllAttributes() {
        fLength = 0;
    } // removeAllAttributes()

    /**
     * Removes the attribute at the specified index.
     * <p>
     * <strong>Note:</strong> This operation changes the indexes of all
     * attributes following the attribute at the specified index.
     * 
     * @param attrIndex The attribute index.
     */
    public void removeAttributeAt(int attrIndex) {
        fIsTableViewConsistent = false;
        if (attrIndex < fLength - 1) {
            Attribute removedAttr = fAttributes[attrIndex];
            System.arraycopy(fAttributes, attrIndex + 1,
                fAttributes, attrIndex, fLength - attrIndex - 1);
            // Make the discarded Attribute object available for re-use
            // by tucking it after the Attributes that are still in use
            fAttributes[fLength-1] = removedAttr;
        }
        fLength--;
    } // removeAttributeAt(int)

    /**
     * Sets the name of the attribute at the specified index.
     * 
     * @param attrIndex The attribute index.
     * @param attrName  The new attribute name.
     */
    public void setName(int attrIndex, QName attrName) {
        fAttributes[attrIndex].name.setValues(attrName);
    } // setName(int,QName)

    /**
     * Sets the fields in the given QName structure with the values
     * of the attribute name at the specified index.
     * 
     * @param attrIndex The attribute index.
     * @param attrName  The attribute name structure to fill in.
     */
    public void getName(int attrIndex, QName attrName) {
        attrName.setValues(fAttributes[attrIndex].name);
    } // getName(int,QName)

    /**
     * Sets the type of the attribute at the specified index.
     * 
     * @param attrIndex The attribute index.
     * @param attrType  The attribute type. The type name is determined by
     *                  the type specified for this attribute in the DTD.
     *                  For example: "CDATA", "ID", "NMTOKEN", etc. However,
     *                  attributes of type enumeration will have the type
     *                  value specified as the pipe ('|') separated list of
     *                  the enumeration values prefixed by an open 
     *                  parenthesis and suffixed by a close parenthesis.
     *                  For example: "(true|false)".
     */
    public void setType(int attrIndex, String attrType) {
        fAttributes[attrIndex].type = attrType;
    } // setType(int,String)

    /**
     * Sets the value of the attribute at the specified index. This
     * method will overwrite the non-normalized value of the attribute.
     * 
     * @param attrIndex The attribute index.
     * @param attrValue The new attribute value.
     *
     * @see #setNonNormalizedValue
     */
    public void setValue(int attrIndex, String attrValue) {
        Attribute attribute = fAttributes[attrIndex];
        attribute.value = attrValue;
        attribute.nonNormalizedValue = attrValue;
    } // setValue(int,String)

    /**
     * Sets the non-normalized value of the attribute at the specified
     * index.
     *
     * @param attrIndex The attribute index.
     * @param attrValue The new non-normalized attribute value.
     */
    public void setNonNormalizedValue(int attrIndex, String attrValue) {
        if (attrValue == null) {
            attrValue = fAttributes[attrIndex].value;
        }
        fAttributes[attrIndex].nonNormalizedValue = attrValue;
    } // setNonNormalizedValue(int,String)

    /**
     * Returns the non-normalized value of the attribute at the specified
     * index. If no non-normalized value is set, this method will return
     * the same value as the <code>getValue(int)</code> method.
     *
     * @param attrIndex The attribute index.
     */
    public String getNonNormalizedValue(int attrIndex) {
        String value = fAttributes[attrIndex].nonNormalizedValue;
        return value;
    } // getNonNormalizedValue(int):String

    /**
     * Sets whether an attribute is specified in the instance document
     * or not.
     *
     * @param attrIndex The attribute index.
     * @param specified True if the attribute is specified in the instance
     *                  document.
     */
    public void setSpecified(int attrIndex, boolean specified) {
        fAttributes[attrIndex].specified = specified;
    } // setSpecified(int,boolean)

    /**
     * Returns true if the attribute is specified in the instance document.
     *
     * @param attrIndex The attribute index.
     */
    public boolean isSpecified(int attrIndex) {
        return fAttributes[attrIndex].specified;
    } // isSpecified(int):boolean

    //
    // AttributeList and Attributes methods
    //

    /**
     * Return the number of attributes in the list.
     *
     * <p>Once you know the number of attributes, you can iterate
     * through the list.</p>
     *
     * @return The number of attributes in the list.
     */
    public int getLength() {
        return fLength;
    } // getLength():int

    /**
     * Look up an attribute's type by index.
     *
     * <p>The attribute type is one of the strings "CDATA", "ID",
     * "IDREF", "IDREFS", "NMTOKEN", "NMTOKENS", "ENTITY", "ENTITIES",
     * or "NOTATION" (always in upper case).</p>
     *
     * <p>If the parser has not read a declaration for the attribute,
     * or if the parser does not report attribute types, then it must
     * return the value "CDATA" as stated in the XML 1.0 Recommentation
     * (clause 3.3.3, "Attribute-Value Normalization").</p>
     *
     * <p>For an enumerated attribute that is not a notation, the
     * parser will report the type as "NMTOKEN".</p>
     *
     * @param index The attribute index (zero-based).
     * @return The attribute's type as a string, or null if the
     *         index is out of range.
     * @see #getLength
     */
    public String getType(int index) {
        if (index < 0 || index >= fLength) {
            return null;
        }
        return getReportableType(fAttributes[index].type);
    } // getType(int):String

    /**
     * Look up an attribute's type by XML 1.0 qualified name.
     *
     * <p>See {@link #getType(int) getType(int)} for a description
     * of the possible types.</p>
     *
     * @param qname The XML 1.0 qualified name.
     * @return The attribute type as a string, or null if the
     *         attribute is not in the list or if qualified names
     *         are not available.
     */
    public String getType(String qname) {
        int index = getIndex(qname);
        return index != -1 ? getReportableType(fAttributes[index].type) : null;
    } // getType(String):String

    /**
     * Look up an attribute's value by index.
     *
     * <p>If the attribute value is a list of tokens (IDREFS,
     * ENTITIES, or NMTOKENS), the tokens will be concatenated
     * into a single string with each token separated by a
     * single space.</p>
     *
     * @param index The attribute index (zero-based).
     * @return The attribute's value as a string, or null if the
     *         index is out of range.
     * @see #getLength
     */
    public String getValue(int index) {
        if (index < 0 || index >= fLength) {
            return null;
        }
        return fAttributes[index].value;
    } // getValue(int):String

    /**
     * Look up an attribute's value by XML 1.0 qualified name.
     *
     * <p>See {@link #getValue(int) getValue(int)} for a description
     * of the possible values.</p>
     *
     * @param qname The XML 1.0 qualified name.
     * @return The attribute value as a string, or null if the
     *         attribute is not in the list or if qualified names
     *         are not available.
     */
    public String getValue(String qname) {
        int index = getIndex(qname);
        return index != -1 ? fAttributes[index].value : null;
    } // getValue(String):String

    //
    // AttributeList methods
    //

    /**
     * Return the name of an attribute in this list (by position).
     *
     * <p>The names must be unique: the SAX parser shall not include the
     * same attribute twice.  Attributes without values (those declared
     * #IMPLIED without a value specified in the start tag) will be
     * omitted from the list.</p>
     *
     * <p>If the attribute name has a namespace prefix, the prefix
     * will still be attached.</p>
     *
     * @param index The index of the attribute in the list (starting at 0).
     * @return The name of the indexed attribute, or null
     *         if the index is out of range.
     * @see #getLength 
     */
    public String getName(int index) {
        if (index < 0 || index >= fLength) {
            return null;
        }
        return fAttributes[index].name.rawname;
    } // getName(int):String

    //
    // Attributes methods
    //

    /**
     * Look up the index of an attribute by XML 1.0 qualified name.
     *
     * @param qName The qualified (prefixed) name.
     * @return The index of the attribute, or -1 if it does not
     *         appear in the list.
     */
    public int getIndex(String qName) {
        for (int i = 0; i < fLength; i++) {
            Attribute attribute = fAttributes[i];
            if (attribute.name.rawname != null &&
                attribute.name.rawname.equals(qName)) {
                return i;
            }
        }
        return -1;
    } // getIndex(String):int
    
    /**
     * Look up the index of an attribute by Namespace name.
     *
     * @param uri The Namespace URI, or null if
     *        the name has no Namespace URI.
     * @param localPart The attribute's local name.
     * @return The index of the attribute, or -1 if it does not
     *         appear in the list.
     */
    public int getIndex(String uri, String localPart) {
        for (int i = 0; i < fLength; i++) {
            Attribute attribute = fAttributes[i];
            if (attribute.name.localpart != null &&
                attribute.name.localpart.equals(localPart) &&
                ((uri==attribute.name.uri) ||
                (uri!=null && attribute.name.uri!=null && attribute.name.uri.equals(uri))))
            {
                return i;
            }
        }
        return -1;
    } // getIndex(String,String):int

    /**
     * Look up an attribute's local name by index.
     *
     * @param index The attribute index (zero-based).
     * @return The local name, or the empty string if Namespace
     *         processing is not being performed, or null
     *         if the index is out of range.
     * @see #getLength
     */
    public String getLocalName(int index) {
        if (!fNamespaces) {
            return "";
        }
        if (index < 0 || index >= fLength) {
            return null;
        }
        return fAttributes[index].name.localpart;
    } // getLocalName(int):String

    /**
     * Look up an attribute's XML 1.0 qualified name by index.
     *
     * @param index The attribute index (zero-based).
     * @return The XML 1.0 qualified name, or the empty string
     *         if none is available, or null if the index
     *         is out of range.
     * @see #getLength
     */
    public String getQName(int index) {
        if (index < 0 || index >= fLength) {
            return null;
        }
        String rawname = fAttributes[index].name.rawname;
        return rawname != null ? rawname : "";
    } // getQName(int):String

    /**
     * Look up an attribute's type by Namespace name.
     *
     * <p>See {@link #getType(int) getType(int)} for a description
     * of the possible types.</p>
     *
     * @param uri The Namespace URI, or null if the
     *        name has no Namespace URI.
     * @param localName The local name of the attribute.
     * @return The attribute type as a string, or null if the
     *         attribute is not in the list or if Namespace
     *         processing is not being performed.
     */
    public String getType(String uri, String localName) {
        if (!fNamespaces) {
            return null;
        }
        int index = getIndex(uri, localName);
        return index != -1 ? getReportableType(fAttributes[index].type) : null;
    } // getType(String,String):String

    /**
     * Returns the prefix of the attribute at the specified index.
     *
     * @param index The index of the attribute.
     */
    public String getPrefix(int index) {
        if (index < 0 || index >= fLength) {
            return null;
        }
        String prefix = fAttributes[index].name.prefix;
        // REVISIT: The empty string is not entered in the symbol table!
        return prefix != null ? prefix : "";
    } // getPrefix(int):String

    /**
     * Look up an attribute's Namespace URI by index.
     *
     * @param index The attribute index (zero-based).
     * @return The Namespace URI
     * @see #getLength
     */
    public String getURI(int index) {
        if (index < 0 || index >= fLength) {
            return null;
        }
        String uri = fAttributes[index].name.uri;
        return uri;                        
    } // getURI(int):String

    /**
     * Look up an attribute's value by Namespace name.
     *
     * <p>See {@link #getValue(int) getValue(int)} for a description
     * of the possible values.</p>
     *
     * @param uri The Namespace URI, or null if the
     * @param localName The local name of the attribute.
     * @return The attribute value as a string, or null if the
     *         attribute is not in the list.
     */
    public String getValue(String uri, String localName) {
        int index = getIndex(uri, localName);
        return index != -1 ? getValue(index) : null;
    } // getValue(String,String):String


    /**
     * Look up an augmentations by Namespace name.
     *
     * @param uri The Namespace URI, or null if the
     * @param localName The local name of the attribute.
     * @return Augmentations     
     */
    public Augmentations getAugmentations (String uri, String localName) {
        int index = getIndex(uri, localName);
        return index != -1 ? fAttributes[index].augs : null;
    }

    /**
     * Look up an augmentation by XML 1.0 qualified name.
     * <p>
     *
     * @param qName The XML 1.0 qualified name.
     *
     * @return Augmentations
     *
     */
    public Augmentations getAugmentations(String qName){
        int index = getIndex(qName);
        return index != -1 ? fAttributes[index].augs : null;
    }

    /**
     * Look up an augmentations by attributes index.
     * 
     * @param attributeIndex The attribute index.
     * @return Augmentations
     */
    public Augmentations getAugmentations (int attributeIndex){
        if (attributeIndex < 0 || attributeIndex >= fLength) {
            return null;
        }
        return fAttributes[attributeIndex].augs;
    }

    /**
     * Sets the augmentations of the attribute at the specified index.
     * 
     * @param attrIndex The attribute index.
     * @param augs      The augmentations.
     */
    public void setAugmentations(int attrIndex, Augmentations augs) {
        fAttributes[attrIndex].augs = augs;
    }

    /**
     * Sets the uri of the attribute at the specified index.
     * 
     * @param attrIndex The attribute index.
     * @param uri       Namespace uri
     */
    public void setURI(int attrIndex, String uri) {
        fAttributes[attrIndex].name.uri = uri;
    } // getURI(int,QName)

    // Implementation methods
    public void setSchemaId(int attrIndex, boolean schemaId) {
        fAttributes[attrIndex].schemaId = schemaId;
    }
    public boolean getSchemaId(int index) {
        if (index < 0 || index >= fLength) {
            return false;
        }
        return fAttributes[index].schemaId;
    }
    public boolean getSchemaId(String qname) {
        int index = getIndex(qname);
        return index != -1 ? fAttributes[index].schemaId : false; 
    } // getType(String):String
    public boolean getSchemaId(String uri, String localName) {
        if (!fNamespaces) {
            return false;
        }
        int index = getIndex(uri, localName);
        return index != -1 ? fAttributes[index].schemaId : false;
    } // getType(String,String):String
    
    /**
     * Look up the index of an attribute by XML 1.0 qualified name.
     * <p>
     * <strong>Note:</strong> 
     * This method uses reference comparison, and thus should
     * only be used internally. We cannot use this method in any
     * code exposed to users as they may not pass in unique strings.
     *
     * @param qName The qualified (prefixed) name.
     * @return The index of the attribute, or -1 if it does not
     *         appear in the list.
     */
    public int getIndexFast(String qName) {
        for (int i = 0; i < fLength; ++i) {
            Attribute attribute = fAttributes[i];
            if (attribute.name.rawname == qName) {
                return i;
            }
        }
        return -1;
    } // getIndexFast(String):int
    
    /**
     * Adds an attribute. The attribute's non-normalized value of the
     * attribute will have the same value as the attribute value until
     * set using the <code>setNonNormalizedValue</code> method. Also,
     * the added attribute will be marked as specified in the XML instance
     * document unless set otherwise using the <code>setSpecified</code>
     * method.
     * <p>
     * This method differs from <code>addAttribute</code> in that it
     * does not check if an attribute of the same name already exists
     * in the list before adding it. In order to improve performance
     * of namespace processing, this method allows uniqueness checks
     * to be deferred until all the namespace information is available
     * after the entire attribute specification has been read.
     * <p>
     * <strong>Caution:</strong> If this method is called it should
     * not be mixed with calls to <code>addAttribute</code> unless
     * it has been determined that all the attribute names are unique.
     * 
     * @param name the attribute name
     * @param type the attribute type
     * @param value the attribute value
     * 
     * @see #setNonNormalizedValue
     * @see #setSpecified
     * @see #checkDuplicatesNS
     */
    public void addAttributeNS(QName name, String type, String value) {
        int index = fLength;
        if (fLength++ == fAttributes.length) {
            Attribute[] attributes;
            if (fLength < SIZE_LIMIT) {
                attributes = new Attribute[fAttributes.length + 4];
            }
            else {
                attributes = new Attribute[fAttributes.length << 1];
            }
            System.arraycopy(fAttributes, 0, attributes, 0, fAttributes.length);
            for (int i = fAttributes.length; i < attributes.length; i++) {
                attributes[i] = new Attribute();
            }
            fAttributes = attributes;
        }
        
        // set values
        Attribute attribute = fAttributes[index];
        attribute.name.setValues(name);
        attribute.type = type;
        attribute.value = value;
        attribute.nonNormalizedValue = value;
        attribute.specified = false;
            
        // clear augmentations
        attribute.augs.removeAllItems();
    }
    
    /**
     * Checks for duplicate expanded names (local part and namespace name
     * pairs) in the attribute specification. If a duplicate is found its
     * name is returned.
     * <p>
     * This should be called once all the in-scope namespaces for the element
     * enclosing these attributes is known, and after all the attributes
     * have gone through namespace binding.
     * 
     * @return the name of a duplicate attribute found in the search,
     * otherwise null.
     */
    public QName checkDuplicatesNS() {
        // If the list is small check for duplicates using pairwise comparison.
        if (fLength <= SIZE_LIMIT) {
            for (int i = 0; i < fLength - 1; ++i) {
            	Attribute att1 = fAttributes[i];
                for (int j = i + 1; j < fLength; ++j) {
                    Attribute att2 = fAttributes[j];
                    if (att1.name.localpart == att2.name.localpart &&
                        att1.name.uri == att2.name.uri) {
                        return att2.name;    	
                    }
                }
            }
    	}
    	// If the list is large check duplicates using a hash table.
    	else {
            // We don't want this table view to be read if someone calls 
            // addAttribute so we invalidate it up front.
            fIsTableViewConsistent = false;

            prepareTableView();

            Attribute attr;
            int bucket;

            for (int i = fLength - 1; i >= 0; --i) {
                attr = fAttributes[i];
                bucket = getTableViewBucket(attr.name.localpart, attr.name.uri);
                
                // The chain is stale. 
                // This must be a unique attribute.
                if (fAttributeTableViewChainState[bucket] != fLargeCount) {
                    fAttributeTableViewChainState[bucket] = fLargeCount;
                    attr.next = null;
                    fAttributeTableView[bucket] = attr;
                } 
                // This chain is active. 
                // We need to check if any of the attributes has the same name.
                else {
                    // Search the table.
                    Attribute found = fAttributeTableView[bucket];
                    while (found != null) {
                        if (found.name.localpart == attr.name.localpart &&
                            found.name.uri == attr.name.uri) {
                            return attr.name;
                        }
                        found = found.next;
                    }
                    
                    // Update table view
                    attr.next = fAttributeTableView[bucket];
                    fAttributeTableView[bucket] = attr;
                }
            }
    	}
    	return null;
    }
    
    /**
     * Look up the index of an attribute by Namespace name.
     * <p>
     * <strong>Note:</strong> 
     * This method uses reference comparison, and thus should
     * only be used internally. We cannot use this method in any
     * code exposed to users as they may not pass in unique strings.
     *
     * @param uri The Namespace URI, or null if
     *        the name has no Namespace URI.
     * @param localPart The attribute's local name.
     * @return The index of the attribute, or -1 if it does not
     *         appear in the list.
     */
    public int getIndexFast(String uri, String localPart) {
        for (int i = 0; i < fLength; ++i) {
            Attribute attribute = fAttributes[i];
            if (attribute.name.localpart == localPart && 
                attribute.name.uri == uri) {
                return i;
            }
        }
        return -1;
    } // getIndexFast(String,String):int

    /**
     * Returns the value passed in or NMTOKEN if it's an enumerated type.
     * 
     * @param type attribute type
     * @return the value passed in or NMTOKEN if it's an enumerated type.
     */
    private String getReportableType(String type) {

        if (type.charAt(0) == '(') {
            return "NMTOKEN";
        }
        return type;
    }
	
    /**
     * Returns the position in the table view 
     * where the given attribute name would be hashed.
     * 
     * @param qname the attribute name
     * @return the position in the table view where the given attribute
     * would be hashed
     */
    protected int getTableViewBucket(String qname) {
        return (qname.hashCode() & 0x7FFFFFFF) % fTableViewBuckets;
    }
    
    /**
     * Returns the position in the table view
     * where the given attribute name would be hashed.
     * 
     * @param localpart the local part of the attribute
     * @param uri the namespace name of the attribute
     * @return the position in the table view where the given attribute
     * would be hashed
     */
    protected int getTableViewBucket(String localpart, String uri) {
        if (uri == null) {
            return (localpart.hashCode() & 0x7FFFFFFF) % fTableViewBuckets;
        }
        else {
            return ((localpart.hashCode() + uri.hashCode()) 
               & 0x7FFFFFFF) % fTableViewBuckets;
        }
    }
	
    /**
     * Purges all elements from the table view.
     */
    protected void cleanTableView() {
        if (++fLargeCount < 0) {
            // Overflow. We actually need to visit the chain state array.
            if (fAttributeTableViewChainState != null) {
                for (int i = fTableViewBuckets - 1; i >= 0; --i) {
                    fAttributeTableViewChainState[i] = 0;
                } 
            }
            fLargeCount = 1;
        }
    }
    
    /**
     * Prepares the table view of the attributes list for use.
     */
    protected void prepareTableView() {
        if (fAttributeTableView == null) {
            fAttributeTableView = new Attribute[fTableViewBuckets];
            fAttributeTableViewChainState = new int[fTableViewBuckets];
        }
        else {
            cleanTableView();
        }
    }
    
    /**
     * Prepares the table view of the attributes list for use,
     * and populates it with the attributes which have been
     * previously read.
     */
    protected void prepareAndPopulateTableView() {
        prepareTableView();
        // Need to populate the hash table with the attributes we've scanned so far.
        Attribute attr;
        int bucket;
        for (int i = 0; i < fLength; ++i) {
            attr = fAttributes[i];
            bucket = getTableViewBucket(attr.name.rawname);
            if (fAttributeTableViewChainState[bucket] != fLargeCount) {
                fAttributeTableViewChainState[bucket] = fLargeCount;
                attr.next = null;
                fAttributeTableView[bucket] = attr;
            } 
            else {
                // Update table view
                attr.next = fAttributeTableView[bucket];
                fAttributeTableView[bucket] = attr;
            }
        }
    }

    //
    // Classes
    //

    /**
     * Attribute information.
     *
     * @author Andy Clark, IBM
     */
    static class Attribute {
        
        //
        // Data
        //

        // basic info

        /** Name. */
        public QName name = new QName();

        /** Type. */
        public String type;

        /** Value. */
        public String value;

        /** Non-normalized value. */
        public String nonNormalizedValue;

        /** Specified. */
        public boolean specified;

        /** Schema ID type. */
        public boolean schemaId;
        
        /** 
         * Augmentations information for this attribute.
         * XMLAttributes has no knowledge if any augmentations
         * were attached to Augmentations.
         */
        public Augmentations augs = new AugmentationsImpl();
        
        // Additional data for attribute table view
        
        /** Pointer to the next attribute in the chain. **/
        public Attribute next;
        
    } // class Attribute

} // class XMLAttributesImpl
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9718.java