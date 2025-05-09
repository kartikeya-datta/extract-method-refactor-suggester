error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13798.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13798.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13798.java
text:
```scala
i@@nt attrCount = attrUseS.getLength();

/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 2001, 2002 The Apache Software Foundation.  All rights
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
 * originally based on software copyright (c) 2001, International
 * Business Machines, Inc., http://www.apache.org.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.dv.SchemaDVFactory;
import org.apache.xerces.impl.dv.XSFacets;
import org.apache.xerces.impl.dv.InvalidDatatypeFacetException;
import org.apache.xerces.impl.xs.XSConstraints;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSTypeDecl;
import org.apache.xerces.impl.xs.XSAttributeGroupDecl;
import org.apache.xerces.impl.xs.XSAttributeUseImpl;
import org.apache.xerces.impl.xs.XSWildcardDecl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.XSModelGroupImpl;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.impl.xs.util.XIntPool;
import org.apache.xerces.impl.xs.psvi.XSConstants;
import org.apache.xerces.impl.xs.psvi.XSObjectList;
import org.apache.xerces.xni.QName;
import org.w3c.dom.Element;
import java.util.Hashtable;

/**
 * A complex type definition schema component traverser.
 *
 * <complexType
 *   abstract = boolean : false
 *   block = (#all | List of (extension | restriction))
 *   final = (#all | List of (extension | restriction))
 *   id = ID
 *   mixed = boolean : false
 *   name = NCName
 *   {any attributes with non-schema namespace . . .}>
 *   Content: (annotation?, (simpleContent | complexContent |
 *            ((group | all | choice | sequence)?,
 *            ((attribute | attributeGroup)*, anyAttribute?))))
 * </complexType>
 * @version $Id$
 */

class  XSDComplexTypeTraverser extends XSDAbstractParticleTraverser {


    XSDComplexTypeTraverser (XSDHandler handler,
                             XSAttributeChecker gAttrCheck) {
        super(handler, gAttrCheck);
    }


    private static final boolean DEBUG=false;

    private static XSParticleDecl fErrorContent=null;
    private static XSWildcardDecl fErrorWildcard=null;
    
    private SchemaDVFactory schemaFactory = SchemaDVFactory.getInstance();

    private class ComplexTypeRecoverableError extends Exception {

        Object[] errorSubstText=null;
        Element  errorElem = null;
        ComplexTypeRecoverableError() {
            super();
        }
        ComplexTypeRecoverableError(String msgKey, Object[] args, Element e) {
            super(msgKey);
            errorSubstText=args;
            errorElem = e;
        }

    }

    /**
     * Traverse local complexType declarations
     *
     * @param Element
     * @param XSDocumentInfo
     * @param SchemaGrammar
     * @return XSComplexTypeDecl
     */
    XSComplexTypeDecl traverseLocal(Element complexTypeNode,
                                    XSDocumentInfo schemaDoc,
                                    SchemaGrammar grammar) {


        Object[] attrValues = fAttrChecker.checkAttributes(complexTypeNode, false,
                                                           schemaDoc);
        String complexTypeName = genAnonTypeName(complexTypeNode);
        XSComplexTypeDecl type = traverseComplexTypeDecl (complexTypeNode,
                                                          complexTypeName, attrValues, schemaDoc, grammar);
        // need to add the type to the grammar for later constraint checking
        grammar.addComplexTypeDecl(type, fSchemaHandler.element2Locator(complexTypeNode));
        type.setIsAnonymous();
        fAttrChecker.returnAttrArray(attrValues, schemaDoc);

        return type;
    }

    /**
     * Traverse global complexType declarations
     *
     * @param Element
     * @param XSDocumentInfo
     * @param SchemaGrammar
     * @return XSComplexTypeDecXSComplexTypeDecl
     */
    XSComplexTypeDecl traverseGlobal (Element complexTypeNode,
                                      XSDocumentInfo schemaDoc,
                                      SchemaGrammar grammar) {

        Object[] attrValues = fAttrChecker.checkAttributes(complexTypeNode, true,
                                                           schemaDoc);
        String complexTypeName = (String)  attrValues[XSAttributeChecker.ATTIDX_NAME];
        XSComplexTypeDecl type = traverseComplexTypeDecl (complexTypeNode,
                                                          complexTypeName, attrValues, schemaDoc, grammar);
        if (complexTypeName == null) {
            reportSchemaError("s4s-att-must-appear", new Object[]{SchemaSymbols.ELT_COMPLEXTYPE, SchemaSymbols.ATT_NAME}, complexTypeNode);
        } else {
            grammar.addGlobalTypeDecl(type);
        }
        // need to add the type to the grammar for later constraint checking
        grammar.addComplexTypeDecl(type, fSchemaHandler.element2Locator(complexTypeNode));
        fAttrChecker.returnAttrArray(attrValues, schemaDoc);

        return type;
    }


    private XSComplexTypeDecl traverseComplexTypeDecl(Element complexTypeDecl,
                                                      String complexTypeName,
                                                      Object[] attrValues,
                                                      XSDocumentInfo schemaDoc,
                                                      SchemaGrammar grammar) {

        Boolean abstractAtt  = (Boolean) attrValues[XSAttributeChecker.ATTIDX_ABSTRACT];
        XInt    blockAtt     = (XInt)    attrValues[XSAttributeChecker.ATTIDX_BLOCK];
        Boolean mixedAtt     = (Boolean) attrValues[XSAttributeChecker.ATTIDX_MIXED];
        XInt    finalAtt     = (XInt)    attrValues[XSAttributeChecker.ATTIDX_FINAL];

        XSComplexTypeDecl complexType = new XSComplexTypeDecl();
        complexType.fName = complexTypeName;
        complexType.fTargetNamespace = schemaDoc.fTargetNamespace;
        complexType.fBlock = blockAtt == null ?
                             schemaDoc.fBlockDefault : blockAtt.shortValue();
        complexType.fFinal = finalAtt == null ?
                             schemaDoc.fFinalDefault : finalAtt.shortValue();
        if (abstractAtt != null && abstractAtt.booleanValue())
            complexType.setIsAbstractType();


        Element child = null;

        try {
            // ---------------------------------------------------------------
            // First, handle any ANNOTATION declaration and get next child
            // ---------------------------------------------------------------
            child = DOMUtil.getFirstChildElement(complexTypeDecl);

            if (child != null) {
                // traverse annotation if any
                if (DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION)) {
                    traverseAnnotationDecl(child, attrValues, false, schemaDoc);
                    child = DOMUtil.getNextSiblingElement(child);
                }
                if (child !=null && DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION)) {
                    throw new ComplexTypeRecoverableError("src-ct.0.1",
                           new Object[]{complexType.fName,SchemaSymbols.ELT_ANNOTATION},
                           child);
                }
            }
            // ---------------------------------------------------------------
            // Process the content of the complex type definition
            // ---------------------------------------------------------------
            if (child==null) {
                //
                // EMPTY complexType with complexContent
                //

                // set the base to the anyType
                complexType.fBaseType = SchemaGrammar.fAnyType;
                processComplexContent(child, complexType, mixedAtt.booleanValue(), false,
                                      schemaDoc, grammar);
            }
            else if (DOMUtil.getLocalName(child).equals
                     (SchemaSymbols.ELT_SIMPLECONTENT)) {
                //
                // SIMPLE CONTENT
                //
                traverseSimpleContent(child, complexType, schemaDoc, grammar);
                Element elemTmp = DOMUtil.getNextSiblingElement(child);
                if (elemTmp != null) {
                    String siblingName = DOMUtil.getLocalName(elemTmp);
                    throw new ComplexTypeRecoverableError("src-ct.0.1",
                                                          new Object[]{complexType.fName,siblingName},
                                                          elemTmp);
                }
            }
            else if (DOMUtil.getLocalName(child).equals
                     (SchemaSymbols.ELT_COMPLEXCONTENT)) {
                traverseComplexContent(child, complexType, mixedAtt.booleanValue(),
                                       schemaDoc, grammar);
                Element elemTmp = DOMUtil.getNextSiblingElement(child);
                if (elemTmp != null) {
                    String siblingName = DOMUtil.getLocalName(elemTmp);
                    throw new ComplexTypeRecoverableError("src-ct.0.1",
                                                          new Object[]{complexType.fName,siblingName},
                                                          elemTmp);
                }
            }
            else {
                //
                // We must have ....
                // GROUP, ALL, SEQUENCE or CHOICE, followed by optional attributes
                // Note that it's possible that only attributes are specified.
                //

                // set the base to the anyType
                complexType.fBaseType = SchemaGrammar.fAnyType;
                processComplexContent(child, complexType, mixedAtt.booleanValue(), false,
                                      schemaDoc, grammar);
            }

        }
        catch (ComplexTypeRecoverableError e) {
            handleComplexTypeError(e.getMessage(), e.errorSubstText,
                                   complexType, e.errorElem);
        }

        if (DEBUG) {
            System.out.println(complexType.toString());
        }
        return complexType;


    }


    private void traverseSimpleContent(Element simpleContentElement,
                                       XSComplexTypeDecl typeInfo,
                                       XSDocumentInfo schemaDoc,
                                       SchemaGrammar grammar)
    throws ComplexTypeRecoverableError {


        String typeName = typeInfo.fName;
        Object[] attrValues = fAttrChecker.checkAttributes(simpleContentElement, false,
                                                           schemaDoc);

        // -----------------------------------------------------------------------
        // Set content type
        // -----------------------------------------------------------------------
        typeInfo.fContentType = XSComplexTypeDecl.CONTENTTYPE_SIMPLE;
        typeInfo.fParticle = null;

        Element simpleContent = DOMUtil.getFirstChildElement(simpleContentElement);
        if (simpleContent != null) {
            // traverse annotation if any
            if (DOMUtil.getLocalName(simpleContent).equals(SchemaSymbols.ELT_ANNOTATION)) {
                traverseAnnotationDecl(simpleContent, attrValues, false, schemaDoc);
                simpleContent = DOMUtil.getNextSiblingElement(simpleContent);
            }
        }
        fAttrChecker.returnAttrArray(attrValues, schemaDoc);

        // If there are no children, return
        if (simpleContent==null) {
            throw new ComplexTypeRecoverableError("src-ct.0.2",
                            new Object[]{typeInfo.fName,SchemaSymbols.ELT_SIMPLECONTENT},
                            simpleContentElement);
        }

        // -----------------------------------------------------------------------
        // The content should be either "restriction" or "extension"
        // -----------------------------------------------------------------------
        String simpleContentName = DOMUtil.getLocalName(simpleContent);
        if (simpleContentName.equals(SchemaSymbols.ELT_RESTRICTION))
            typeInfo.fDerivedBy = XSConstants.DERIVATION_RESTRICTION;
        else if (simpleContentName.equals(SchemaSymbols.ELT_EXTENSION))
            typeInfo.fDerivedBy = XSConstants.DERIVATION_EXTENSION;
        else {
            throw new ComplexTypeRecoverableError("src-ct.0.1",
                            new Object[]{typeInfo.fName,simpleContentName},
                            simpleContent);
        }
        Element elemTmp = DOMUtil.getNextSiblingElement(simpleContent);
        if (elemTmp != null) {
            String siblingName = DOMUtil.getLocalName(elemTmp);
            throw new ComplexTypeRecoverableError("src-ct.0.1",
                            new Object[]{typeInfo.fName,siblingName},
                            elemTmp);
        }

        attrValues = fAttrChecker.checkAttributes(simpleContent, false,
                                                  schemaDoc);
        QName baseTypeName = (QName)  attrValues[XSAttributeChecker.ATTIDX_BASE];
        fAttrChecker.returnAttrArray(attrValues, schemaDoc);


        // -----------------------------------------------------------------------
        // Need a base type.
        // -----------------------------------------------------------------------
        if (baseTypeName==null) {
            throw new ComplexTypeRecoverableError("src-ct.0.3",
                            new Object[]{typeInfo.fName}, simpleContent);
        }

        XSTypeDecl type = (XSTypeDecl)fSchemaHandler.getGlobalDecl(schemaDoc,
                                      XSDHandler.TYPEDECL_TYPE, baseTypeName,
                                      simpleContent);
        if (type==null)
            throw new ComplexTypeRecoverableError();

        typeInfo.fBaseType = type;

        XSSimpleType baseValidator = null;
        XSComplexTypeDecl baseComplexType = null;
        int baseFinalSet = 0;

        // If the base type is complex, it must have simpleContent
        if ((type.getTypeCategory() == XSTypeDecl.COMPLEX_TYPE)) {

            baseComplexType = (XSComplexTypeDecl)type;
            if (baseComplexType.fContentType != XSComplexTypeDecl.CONTENTTYPE_SIMPLE) {
                throw new ComplexTypeRecoverableError("src-ct.2",
                                new Object[]{typeInfo.fName}, simpleContent);
            }
            baseFinalSet = baseComplexType.fFinal;
            baseValidator = baseComplexType.fXSSimpleType;
        }
        else {
            baseValidator = (XSSimpleType)type;
            if (typeInfo.fDerivedBy == XSConstants.DERIVATION_RESTRICTION) {
                throw new ComplexTypeRecoverableError("src-ct.2",
                                new Object[]{typeInfo.fName}, simpleContent);
            }
            baseFinalSet=baseValidator.getFinal();
        }

        // -----------------------------------------------------------------------
        // Check that the base permits the derivation
        // -----------------------------------------------------------------------
        if ((baseFinalSet & typeInfo.fDerivedBy)!=0) {
            String errorKey = (typeInfo.fDerivedBy==XSConstants.DERIVATION_EXTENSION) ?
                              "cos-ct-extends.1.1" : "derivation-ok-restriction.1";
            throw new ComplexTypeRecoverableError(errorKey,
                                new Object[]{typeInfo.fName}, simpleContent);
        }

        // -----------------------------------------------------------------------
        // Skip over any potential annotations
        // -----------------------------------------------------------------------
        simpleContent = DOMUtil.getFirstChildElement(simpleContent);
        if (simpleContent != null) {
            // traverse annotation if any

            if (DOMUtil.getLocalName(simpleContent).equals(SchemaSymbols.ELT_ANNOTATION)) {
                traverseAnnotationDecl(simpleContent, null, false, schemaDoc);
                simpleContent = DOMUtil.getNextSiblingElement(simpleContent);
            }

            if (simpleContent !=null &&
                DOMUtil.getLocalName(simpleContent).equals(SchemaSymbols.ELT_ANNOTATION)){
                throw new ComplexTypeRecoverableError("src-ct.0.1",
                       new Object[]{typeName,SchemaSymbols.ELT_ANNOTATION},
                       simpleContent);
            }
        }

        // -----------------------------------------------------------------------
        // Process a RESTRICTION
        // -----------------------------------------------------------------------
        if (typeInfo.fDerivedBy == XSConstants.DERIVATION_RESTRICTION) {

            // -----------------------------------------------------------------------
            // There may be a simple type definition in the restriction element
            // The data type validator will be based on it, if specified
            // -----------------------------------------------------------------------
            if (simpleContent !=null &&
            DOMUtil.getLocalName(simpleContent).equals(SchemaSymbols.ELT_SIMPLETYPE )) {

                XSSimpleType dv = fSchemaHandler.fSimpleTypeTraverser.traverseLocal(
                      simpleContent, schemaDoc, grammar);
                if (dv == null)
                    throw new ComplexTypeRecoverableError();

                //check that this datatype validator is validly derived from the base
                //according to derivation-ok-restriction 5.1.1

                if (!XSConstraints.checkSimpleDerivationOk(dv, baseValidator,
                                                           baseValidator.getFinal())) {
                    throw new ComplexTypeRecoverableError("derivation-ok-restriction.5.1.1",
                           new Object[]{typeName},
                           simpleContent);
                }
                baseValidator = dv;
                simpleContent = DOMUtil.getNextSiblingElement(simpleContent);
            }

            // -----------------------------------------------------------------------
            // Traverse any facets
            // -----------------------------------------------------------------------
            Element attrNode = null;
            XSFacets facetData = null;
            short presentFacets = 0 ;
            short fixedFacets = 0 ;

            if (simpleContent!=null) {
                FacetInfo fi = traverseFacets(simpleContent, baseValidator, schemaDoc);
                attrNode = fi.nodeAfterFacets;
                facetData = fi.facetdata;
                presentFacets = fi.fPresentFacets;
                fixedFacets = fi.fFixedFacets;
            }

            typeInfo.fXSSimpleType = schemaFactory.createTypeRestriction(null,schemaDoc.fTargetNamespace,(short)0,baseValidator);
            try{
                fValidationState.setNamespaceSupport(schemaDoc.fNamespaceSupport);
                typeInfo.fXSSimpleType.applyFacets(facetData, presentFacets, fixedFacets, fValidationState);
            }catch(InvalidDatatypeFacetException ex){
                reportSchemaError(ex.getKey(), ex.getArgs(), simpleContent);
            }

            // -----------------------------------------------------------------------
            // Traverse any attributes
            // -----------------------------------------------------------------------
            if (attrNode != null) {
                if (!isAttrOrAttrGroup(attrNode)) {
                    throw new ComplexTypeRecoverableError("src-ct.0.1",
                             new Object[]{typeInfo.fName,DOMUtil.getLocalName(attrNode)},
                             attrNode);
                }
                Element node=traverseAttrsAndAttrGrps(attrNode,typeInfo.fAttrGrp,
                                                      schemaDoc,grammar,typeInfo);
                if (node!=null) {
                    throw new ComplexTypeRecoverableError("src-ct.0.1",
                             new Object[]{typeInfo.fName,DOMUtil.getLocalName(node)},
                             node);
                }
            }

            mergeAttributes(baseComplexType.fAttrGrp, typeInfo.fAttrGrp, typeName, false, simpleContentElement);
            // Prohibited uses must be removed after merge for RESTRICTION
            typeInfo.fAttrGrp.removeProhibitedAttrs();

            String errorCode=typeInfo.fAttrGrp.validRestrictionOf(baseComplexType.fAttrGrp);
            if (errorCode != null) {
                throw new ComplexTypeRecoverableError(errorCode,
                             new Object[]{typeInfo.fName}, attrNode);
            }

        }
        // -----------------------------------------------------------------------
        // Process a EXTENSION
        // -----------------------------------------------------------------------
        else {
            typeInfo.fXSSimpleType = baseValidator;
            if (simpleContent != null) {
                // -----------------------------------------------------------------------
                // Traverse any attributes
                // -----------------------------------------------------------------------
                Element attrNode = simpleContent;
                if (!isAttrOrAttrGroup(attrNode)) {
                    throw new ComplexTypeRecoverableError("src-ct.0.1",
                                                          new Object[]{typeInfo.fName,DOMUtil.getLocalName(attrNode)},
                                                          attrNode);
                }
                Element node=traverseAttrsAndAttrGrps(attrNode,typeInfo.fAttrGrp,
                                                      schemaDoc,grammar,typeInfo);

                if (node!=null) {
                    throw new ComplexTypeRecoverableError("src-ct.0.1",
                                                          new Object[]{typeInfo.fName,DOMUtil.getLocalName(node)},
                                                          node);
                }
                // Remove prohibited uses.   Should be done prior to any merge.
                typeInfo.fAttrGrp.removeProhibitedAttrs();
            }

            if (baseComplexType != null) {
                mergeAttributes(baseComplexType.fAttrGrp, typeInfo.fAttrGrp, typeName, true, simpleContentElement);
            }
        }
    }

    private void traverseComplexContent(Element complexContentElement,
                                        XSComplexTypeDecl typeInfo,
                                        boolean mixedOnType, XSDocumentInfo schemaDoc,
                                        SchemaGrammar grammar)
    throws ComplexTypeRecoverableError {


        String typeName = typeInfo.fName;
        Object[] attrValues = fAttrChecker.checkAttributes(complexContentElement, false,
                                                           schemaDoc);


        // -----------------------------------------------------------------------
        // Determine if this is mixed content
        // -----------------------------------------------------------------------
        boolean mixedContent = mixedOnType;
        Boolean mixedAtt     = (Boolean) attrValues[XSAttributeChecker.ATTIDX_MIXED];
        if (mixedAtt != null) {
            mixedContent = mixedAtt.booleanValue();
        }


        // -----------------------------------------------------------------------
        // Since the type must have complex content, set the simple type validators
        // to null
        // -----------------------------------------------------------------------
        typeInfo.fXSSimpleType = null;

        Element complexContent = DOMUtil.getFirstChildElement(complexContentElement);
        if (complexContent != null) {
            // traverse annotation if any
            if (DOMUtil.getLocalName(complexContent).equals(SchemaSymbols.ELT_ANNOTATION)) {
                traverseAnnotationDecl(complexContent, attrValues, false, schemaDoc);
                complexContent = DOMUtil.getNextSiblingElement(complexContent);
            }
        }

        fAttrChecker.returnAttrArray(attrValues, schemaDoc);
        // If there are no children, return
        if (complexContent==null) {
            throw new ComplexTypeRecoverableError("src-ct.0.2",
                      new Object[]{typeName,SchemaSymbols.ELT_COMPLEXCONTENT},
                      complexContentElement);
        }

        // -----------------------------------------------------------------------
        // The content should be either "restriction" or "extension"
        // -----------------------------------------------------------------------
        String complexContentName = DOMUtil.getLocalName(complexContent);
        if (complexContentName.equals(SchemaSymbols.ELT_RESTRICTION))
            typeInfo.fDerivedBy = XSConstants.DERIVATION_RESTRICTION;
        else if (complexContentName.equals(SchemaSymbols.ELT_EXTENSION))
            typeInfo.fDerivedBy = XSConstants.DERIVATION_EXTENSION;
        else {
            throw new ComplexTypeRecoverableError("src-ct.0.1",
                      new Object[]{typeName, complexContentName}, complexContent);
        }
        Element elemTmp = DOMUtil.getNextSiblingElement(complexContent);
        if (elemTmp != null) {
            String siblingName = DOMUtil.getLocalName(elemTmp);
            throw new ComplexTypeRecoverableError("src-ct.0.1",
                      new Object[]{typeName, siblingName}, elemTmp);
        }

        attrValues = fAttrChecker.checkAttributes(complexContent, false,
                                                  schemaDoc);
        QName baseTypeName = (QName)  attrValues[XSAttributeChecker.ATTIDX_BASE];
        fAttrChecker.returnAttrArray(attrValues, schemaDoc);


        // -----------------------------------------------------------------------
        // Need a base type.  Check that it's a complex type
        // -----------------------------------------------------------------------
        if (baseTypeName==null) {
            throw new ComplexTypeRecoverableError("src-ct.0.3",
                      new Object[]{typeName}, complexContent);
        }

        XSTypeDecl type = (XSTypeDecl)fSchemaHandler.getGlobalDecl(schemaDoc,
                                                                   XSDHandler.TYPEDECL_TYPE,
                                                                   baseTypeName,
                                                                   complexContent);

        if (type==null)
            throw new ComplexTypeRecoverableError();

        if (! (type instanceof XSComplexTypeDecl)) {
            throw new ComplexTypeRecoverableError("src-ct.1",
                      new Object[]{typeName}, complexContent);
        }
        XSComplexTypeDecl baseType = (XSComplexTypeDecl)type;
        typeInfo.fBaseType = baseType;

        // -----------------------------------------------------------------------
        // Check that the base permits the derivation
        // -----------------------------------------------------------------------
        if ((baseType.fFinal & typeInfo.fDerivedBy)!=0) {
            String errorKey = (typeInfo.fDerivedBy==XSConstants.DERIVATION_EXTENSION) ?
                              "cos-ct-extends.1.1" : "derivation-ok-restriction.1";
            throw new ComplexTypeRecoverableError(errorKey,
                                new Object[]{typeInfo.fName}, complexContent);
        }

        // -----------------------------------------------------------------------
        // Skip over any potential annotations
        // -----------------------------------------------------------------------
        complexContent = DOMUtil.getFirstChildElement(complexContent);

        if (complexContent != null) {
            // traverse annotation if any
            if (DOMUtil.getLocalName(complexContent).equals(SchemaSymbols.ELT_ANNOTATION)) {
                traverseAnnotationDecl(complexContent, null, false, schemaDoc);
                complexContent = DOMUtil.getNextSiblingElement(complexContent);
            }
            if (complexContent !=null &&
               DOMUtil.getLocalName(complexContent).equals(SchemaSymbols.ELT_ANNOTATION)){
                throw new ComplexTypeRecoverableError("src-ct.0.1",
                       new Object[]{typeName,SchemaSymbols.ELT_ANNOTATION}, complexContent);
            }
        }
        // -----------------------------------------------------------------------
        // Process the content.  Note:  should I try to catch any complexType errors
        // here in order to return the attr array?
        // -----------------------------------------------------------------------
        processComplexContent(complexContent, typeInfo, mixedContent, true, schemaDoc,
                              grammar);

        // -----------------------------------------------------------------------
        // Compose the final content and attribute uses
        // -----------------------------------------------------------------------
        XSParticleDecl baseContent = baseType.fParticle;
        if (typeInfo.fDerivedBy==XSConstants.DERIVATION_RESTRICTION) {

            // This is an RESTRICTION

            if (typeInfo.fParticle==null && (!(baseContent==null ||
                                               baseContent.emptiable()))) {
                throw new ComplexTypeRecoverableError("derivation-ok-restriction.5.2",
                                          new Object[]{typeName}, complexContent);
            }
            if (typeInfo.fParticle!=null && baseContent==null) {
                //REVISIT - need better error msg
                throw new ComplexTypeRecoverableError("derivation-ok-restriction.5.3",
                                          new Object[]{typeName}, complexContent);
            }

            mergeAttributes(baseType.fAttrGrp, typeInfo.fAttrGrp, typeName, false, complexContent);
            String error = typeInfo.fAttrGrp.validRestrictionOf(baseType.fAttrGrp);
            if (error != null) {
                throw new ComplexTypeRecoverableError(error,
                          new Object[]{typeName}, complexContent);
            }

            // Remove prohibited uses.   Must be done after merge for RESTRICTION.
            typeInfo.fAttrGrp.removeProhibitedAttrs();

        }
        else {

            // This is an EXTENSION

            //
            // Check if the contentType of the base is consistent with the new type
            // cos-ct-extends.1.4.2.2
            if (baseType.fContentType != XSComplexTypeDecl.CONTENTTYPE_EMPTY) {
                if (((baseType.fContentType ==
                      XSComplexTypeDecl.CONTENTTYPE_ELEMENT) &&
                     mixedContent) ||
                    ((baseType.fContentType ==
                      XSComplexTypeDecl.CONTENTTYPE_MIXED) && !mixedContent)) {

                    throw new ComplexTypeRecoverableError("cos-ct-extends.1.4.2.2.2.2.1",
                          new Object[]{typeName}, complexContent);
                }

            }

            // Create the particle
            if (typeInfo.fParticle == null) {
                typeInfo.fParticle = baseContent;
            }
            else if (baseContent==null) {
            }
            else {
                // if the content of either type is an "all" model group, error.
                if (typeInfo.fParticle.fType == XSParticleDecl.PARTICLE_MODELGROUP &&
                    ((XSModelGroupImpl)typeInfo.fParticle.fValue).fCompositor == XSModelGroupImpl.MODELGROUP_ALL ||
                    baseType.fParticle.fType == XSParticleDecl.PARTICLE_MODELGROUP &&
                    ((XSModelGroupImpl)baseType.fParticle.fValue).fCompositor == XSModelGroupImpl.MODELGROUP_ALL) {
                    throw new ComplexTypeRecoverableError("cos-all-limited.1.2",
                          null, complexContent);
                }
                // the "sequence" model group to contain both particles
                XSModelGroupImpl group = new XSModelGroupImpl();
                group.fCompositor = XSModelGroupImpl.MODELGROUP_SEQUENCE;
                group.fParticleCount = 2;
                group.fParticles = new XSParticleDecl[2];
                group.fParticles[0] = baseType.fParticle;
                group.fParticles[1] = typeInfo.fParticle;
                // the particle to contain the above sequence
                XSParticleDecl particle = new XSParticleDecl();
                particle.fType = XSParticleDecl.PARTICLE_MODELGROUP;
                particle.fValue = group;
                
                typeInfo.fParticle = particle;
            }

            // Set the contentType
            if (mixedContent)
                typeInfo.fContentType = XSComplexTypeDecl.CONTENTTYPE_MIXED;
            else if (typeInfo.fParticle == null)
                typeInfo.fContentType = XSComplexTypeDecl.CONTENTTYPE_EMPTY;
            else
                typeInfo.fContentType = XSComplexTypeDecl.CONTENTTYPE_ELEMENT;

            // Remove prohibited uses.   Must be done before merge for EXTENSION.
            typeInfo.fAttrGrp.removeProhibitedAttrs();
            mergeAttributes(baseType.fAttrGrp, typeInfo.fAttrGrp, typeName, true, complexContent);

        }

    } // end of traverseComplexContent


    // This method merges attribute uses from the base, into the derived set.
    // The first duplicate attribute, if any, is returned.
    // LM: may want to merge with attributeGroup processing.
    private void mergeAttributes(XSAttributeGroupDecl fromAttrGrp,
                                 XSAttributeGroupDecl toAttrGrp,
                                 String typeName,
                                 boolean extension,
                                 Element elem)
    throws ComplexTypeRecoverableError {

        XSObjectList attrUseS = fromAttrGrp.getAttributeUses();
        XSAttributeUseImpl existingAttrUse, duplicateAttrUse =  null, oneAttrUse;
        int attrCount = attrUseS.getListLength();
        for (int i=0; i<attrCount; i++) {
            oneAttrUse = (XSAttributeUseImpl)attrUseS.getItem(i);
            existingAttrUse = toAttrGrp.getAttributeUse(oneAttrUse.fAttrDecl.fTargetNamespace,
                                                        oneAttrUse.fAttrDecl.fName);
            if (existingAttrUse == null) {

                String idName = toAttrGrp.addAttributeUse(oneAttrUse);
                if (idName != null) {
                    throw new ComplexTypeRecoverableError("ct-props-correct.5",
                          new Object[]{typeName, idName, oneAttrUse.fAttrDecl.fName},
                          elem);
                }
            }
            else {
                if (extension) {
                    throw new ComplexTypeRecoverableError("ct-props-correct.4",
                          new Object[]{typeName, existingAttrUse.fAttrDecl.fName},
                          elem);
                }
            }
        }
        // For extension, the wildcard must be formed by doing a union of the wildcards
        if (extension) {
            if (toAttrGrp.fAttributeWC==null) {
                toAttrGrp.fAttributeWC = fromAttrGrp.fAttributeWC;
            }
            else if (fromAttrGrp.fAttributeWC != null) {
                toAttrGrp.fAttributeWC = toAttrGrp.fAttributeWC.performUnionWith(fromAttrGrp.fAttributeWC, toAttrGrp.fAttributeWC.fProcessContents);
            }

        }
    }



    private void processComplexContent(Element complexContentChild,
                                       XSComplexTypeDecl typeInfo,
                                       boolean isMixed, boolean isDerivation,
                                       XSDocumentInfo schemaDoc, SchemaGrammar grammar)
    throws ComplexTypeRecoverableError {

        Element attrNode = null;
        XSParticleDecl particle = null;
        String typeName = typeInfo.fName;

        if (complexContentChild != null) {
            // -------------------------------------------------------------
            // GROUP, ALL, SEQUENCE or CHOICE, followed by attributes, if specified.
            // Note that it's possible that only attributes are specified.
            // -------------------------------------------------------------


            String childName = DOMUtil.getLocalName(complexContentChild);

            if (childName.equals(SchemaSymbols.ELT_GROUP)) {

                particle = fSchemaHandler.fGroupTraverser.traverseLocal(complexContentChild,
                                                                        schemaDoc, grammar);
                attrNode = DOMUtil.getNextSiblingElement(complexContentChild);
            }
            else if (childName.equals(SchemaSymbols.ELT_SEQUENCE)) {
                particle = traverseSequence(complexContentChild,schemaDoc,grammar,
                                            NOT_ALL_CONTEXT,typeInfo);
                attrNode = DOMUtil.getNextSiblingElement(complexContentChild);
            }
            else if (childName.equals(SchemaSymbols.ELT_CHOICE)) {
                particle = traverseChoice(complexContentChild,schemaDoc,grammar,
                                          NOT_ALL_CONTEXT,typeInfo);
                attrNode = DOMUtil.getNextSiblingElement(complexContentChild);
            }
            else if (childName.equals(SchemaSymbols.ELT_ALL)) {
                particle = traverseAll(complexContentChild,schemaDoc,grammar,
                                       PROCESSING_ALL_GP,typeInfo);
                attrNode = DOMUtil.getNextSiblingElement(complexContentChild);
            }
            else {
                // Should be attributes here - will check below...
                attrNode = complexContentChild;
            }
        }

        typeInfo.fParticle = particle;

        // -----------------------------------------------------------------------
        // Set the content type
        // -----------------------------------------------------------------------

        if (isMixed) {
            typeInfo.fContentType = XSComplexTypeDecl.CONTENTTYPE_MIXED;
        }
        else if (typeInfo.fParticle == null)
            typeInfo.fContentType = XSComplexTypeDecl.CONTENTTYPE_EMPTY;
        else
            typeInfo.fContentType = XSComplexTypeDecl.CONTENTTYPE_ELEMENT;


        // -------------------------------------------------------------
        // Now, process attributes
        // -------------------------------------------------------------
        if (attrNode != null) {
            if (!isAttrOrAttrGroup(attrNode)) {
                throw new ComplexTypeRecoverableError("src-ct.0.1",
                                                      new Object[]{typeInfo.fName,DOMUtil.getLocalName(attrNode)},
                                                      attrNode);
            }
            Element node =
            traverseAttrsAndAttrGrps(attrNode,typeInfo.fAttrGrp,schemaDoc,grammar,typeInfo);
            if (node!=null) {
                throw new ComplexTypeRecoverableError("src-ct.0.1",
                                                      new Object[]{typeInfo.fName,DOMUtil.getLocalName(node)},
                                                      node);
            }
            // Only remove prohibited attribute uses if this isn't a derived type
            // Derivation-specific code worries about this elsewhere
            if (!isDerivation) {
                typeInfo.fAttrGrp.removeProhibitedAttrs();
            }
        }



    } // end processComplexContent


    private boolean isAttrOrAttrGroup(Element e) {
        String elementName = DOMUtil.getLocalName(e);

        if (elementName.equals(SchemaSymbols.ELT_ATTRIBUTE) ||
            elementName.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP) ||
            elementName.equals(SchemaSymbols.ELT_ANYATTRIBUTE))
            return true;
        else
            return false;
    }

    private void traverseSimpleContentDecl(Element simpleContentDecl,
                                           XSComplexTypeDecl typeInfo) {
    }

    private void traverseComplexContentDecl(Element complexContentDecl,
                                            XSComplexTypeDecl typeInfo,
                                            boolean mixedOnComplexTypeDecl) {
    }

    /*
     * Generate a name for an anonymous type
     */
    private String genAnonTypeName(Element complexTypeDecl) {

        // Generate a unique name for the anonymous type by concatenating together the
        // names of parent nodes
        // The name is quite good for debugging/error purposes, but we may want to
        // revisit how this is done for performance reasons (LM).
        String typeName;
        Element node = DOMUtil.getParent(complexTypeDecl);
        typeName="#AnonType_";
        while (node != null && (node != DOMUtil.getRoot(DOMUtil.getDocument(node)))) {
            typeName = typeName+node.getAttribute(SchemaSymbols.ATT_NAME);
            node = DOMUtil.getParent(node);
        }
        return typeName;
    }


    private void handleComplexTypeError(String messageId,Object[] args,
                                        XSComplexTypeDecl typeInfo, Element e) {

        if (messageId!=null) {
            reportSchemaError(messageId, args, e);
        }

        //
        //  Mock up the typeInfo structure so that there won't be problems during
        //  validation
        //
        typeInfo.fContentType = XSComplexTypeDecl.CONTENTTYPE_MIXED;
        typeInfo.fParticle = getErrorContent();
        // REVISIT: do we need to remove all attribute uses already added into
        // the attribute group? maybe it's ok to leave them there. -SG
        typeInfo.fAttrGrp.fAttributeWC = fErrorWildcard;

        return;

    }

    private static synchronized XSParticleDecl getErrorContent() {
        if (fErrorContent==null) {
            fErrorWildcard = new XSWildcardDecl();
            fErrorWildcard.fProcessContents = XSWildcardDecl.PC_SKIP;
            XSParticleDecl particle = new XSParticleDecl();
            particle.fType = XSParticleDecl.PARTICLE_WILDCARD;
            particle.fValue = fErrorWildcard;
            particle.fMinOccurs = 0;
            particle.fMaxOccurs = SchemaSymbols.OCCURRENCE_UNBOUNDED;
            XSModelGroupImpl group = new XSModelGroupImpl();
            group.fCompositor = XSModelGroupImpl.MODELGROUP_SEQUENCE;
            group.fParticleCount = 1;
            group.fParticles = new XSParticleDecl[1];
            group.fParticles[0] = particle;
            fErrorContent = new XSParticleDecl();
            fErrorContent.fType = XSParticleDecl.PARTICLE_MODELGROUP;
            fErrorContent.fValue = group;
        }

        return fErrorContent;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13798.java