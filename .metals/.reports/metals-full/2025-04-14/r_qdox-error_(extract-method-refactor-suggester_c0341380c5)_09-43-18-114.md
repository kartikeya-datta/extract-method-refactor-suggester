error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16466.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16466.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16466.java
text:
```scala
r@@eportSchemaError("s4s-att-must-appear", new Object[]{SchemaSymbols.ELT_ELEMENT, SchemaSymbols.ATT_NAME});

/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
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
import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.validation.ValidationContext;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.xs.XSConstraints;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSTypeDecl;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.impl.xs.util.XIntPool;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.QName;
import  org.w3c.dom.Element;

/**
 * The element declaration schema component traverser.
 * <element
 *   abstract = boolean : false
 *   block = (#all | List of (extension | restriction | substitution))
 *   default = string
 *   final = (#all | List of (extension | restriction))
 *   fixed = string
 *   form = (qualified | unqualified)
 *   id = ID
 *   maxOccurs = (nonNegativeInteger | unbounded)  : 1
 *   minOccurs = nonNegativeInteger : 1
 *   name = NCName
 *   nillable = boolean : false
 *   ref = QName
 *   substitutionGroup = QName
 *   type = QName
 *   {any attributes with non-schema namespace . . .}>
 *   Content: (annotation?, ((simpleType | complexType)?, (unique | key | keyref)*))
 * </element>
 *
 * @author Sandy Gao, IBM
 *
 * @version $Id$
 */
class XSDElementTraverser extends XSDAbstractTraverser {

    protected XSElementDecl  fTempElementDecl  = new XSElementDecl();
    protected XSParticleDecl fTempParticleDecl = new XSParticleDecl();

    // this controls what happens when a local element is encountered.
    // We may not encounter all local elements when first parsing.
    boolean fDeferTraversingLocalElements;

    XSDElementTraverser (XSDHandler handler,
                         XSAttributeChecker gAttrCheck) {
        super(handler, gAttrCheck);
    }

    /**
     * Traverse a locally declared element (or an element reference).
     *
     * To handle the recursive cases efficiently, we delay the traversal
     * and return an empty particle node. We'll fill in this particle node
     * later after we've done with all the global declarations.
     * This method causes a number of data structures in the schema handler to be filled in.
     *
     * @param  elmDecl
     * @param  schemaDoc
     * @param  grammar
     * @return the particle
     */
    XSParticleDecl traverseLocal(Element elmDecl,
                                 XSDocumentInfo schemaDoc,
                                 SchemaGrammar grammar,
                                 int allContextFlags) {

        XSParticleDecl particle = new XSParticleDecl();

        if(fDeferTraversingLocalElements) {
            fSchemaHandler.fillInLocalElemInfo(elmDecl, schemaDoc, allContextFlags, particle);
        } else {
            traverseLocal(particle, elmDecl, schemaDoc, grammar, allContextFlags);
        }

        return particle;
    }

    /**
     * Traverse a locally declared element (or an element reference).
     *
     * This is the real traversal method. It's called after we've done with
     * all the global declarations.
     *
     * @param  index
     */
    protected void traverseLocal(XSParticleDecl particle, Element elmDecl,
                               XSDocumentInfo schemaDoc,
                               SchemaGrammar grammar,
                               int allContextFlags) {

        // General Attribute Checking
        Object[] attrValues = fAttrChecker.checkAttributes(elmDecl, false, schemaDoc);

        QName refAtt = (QName) attrValues[XSAttributeChecker.ATTIDX_REF];
        XInt  minAtt = (XInt)  attrValues[XSAttributeChecker.ATTIDX_MINOCCURS];
        XInt  maxAtt = (XInt)  attrValues[XSAttributeChecker.ATTIDX_MAXOCCURS];

        XSElementDecl element = null;
        if (elmDecl.getAttributeNode(SchemaSymbols.ATT_REF) != null) {
            if (refAtt != null) {
                element = (XSElementDecl)fSchemaHandler.getGlobalDecl(schemaDoc, XSDHandler.ELEMENT_TYPE, refAtt);

                Element child = DOMUtil.getFirstChildElement(elmDecl);
                if (child != null && DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION)) {
                    traverseAnnotationDecl(child, attrValues, false, schemaDoc);
                    child = DOMUtil.getNextSiblingElement(child);
                }

                // Element Declaration Representation OK
                // 2 If the item's parent is not <schema>, then all of the following must be true:
                // 2.1 One of ref or name must be present, but not both.
                // 2.2 If ref is present, then all of <complexType>, <simpleType>, <key>, <keyref>, <unique>, nillable, default, fixed, form, block and type must be absent, i.e. only minOccurs, maxOccurs, id are allowed in addition to ref, along with <annotation>.
                if (child != null) {
                    reportSchemaError("src-element.2.2", new Object[]{refAtt});
                }
            } else {
                element = null;
            }
        } else {
            element = traverseNamedElement(elmDecl, attrValues, schemaDoc, grammar, false);
        }

        particle.fMinOccurs = minAtt.intValue();
        particle.fMaxOccurs = maxAtt.intValue();
        if (element != null) {
            particle.fType = XSParticleDecl.PARTICLE_ELEMENT;
            particle.fValue = element;
        }
        Long defaultVals = (Long)attrValues[XSAttributeChecker.ATTIDX_FROMDEFAULT];
        checkOccurrences(particle, SchemaSymbols.ELT_ELEMENT,
                         (Element)elmDecl.getParentNode(), allContextFlags,
                         defaultVals.longValue());

        fAttrChecker.returnAttrArray(attrValues, schemaDoc);
    }

    /**
     * Traverse a globally declared element.
     *
     * @param  elmDecl
     * @param  schemaDoc
     * @param  grammar
     * @return the element declaration
     */
    XSElementDecl traverseGlobal(Element elmDecl,
                                 XSDocumentInfo schemaDoc,
                                 SchemaGrammar grammar) {

        // General Attribute Checking
        Object[] attrValues = fAttrChecker.checkAttributes(elmDecl, true, schemaDoc);
        XSElementDecl element = traverseNamedElement(elmDecl, attrValues, schemaDoc, grammar, true);
        fAttrChecker.returnAttrArray(attrValues, schemaDoc);

        // set the scope of the element as global
        if (element != null)
            element.setIsGlobal();

        return element;
    }

    /**
     * Traverse a globally declared element.
     *
     * @param  elmDecl
     * @param  attrValues
     * @param  schemaDoc
     * @param  grammar
     * @param  isGlobal
     * @return the element declaration
     */
    XSElementDecl traverseNamedElement(Element elmDecl,
                                       Object[] attrValues,
                                       XSDocumentInfo schemaDoc,
                                       SchemaGrammar grammar,
                                       boolean isGlobal) {

        Boolean abstractAtt  = (Boolean) attrValues[XSAttributeChecker.ATTIDX_ABSTRACT];
        XInt    blockAtt     = (XInt)    attrValues[XSAttributeChecker.ATTIDX_BLOCK];
        String  defaultAtt   = (String)  attrValues[XSAttributeChecker.ATTIDX_DEFAULT];
        XInt    finalAtt     = (XInt)    attrValues[XSAttributeChecker.ATTIDX_FINAL];
        String  fixedAtt     = (String)  attrValues[XSAttributeChecker.ATTIDX_FIXED];
        XInt    formAtt      = (XInt)    attrValues[XSAttributeChecker.ATTIDX_FORM];
        String  nameAtt      = (String)  attrValues[XSAttributeChecker.ATTIDX_NAME];
        Boolean nillableAtt  = (Boolean) attrValues[XSAttributeChecker.ATTIDX_NILLABLE];
        QName   subGroupAtt  = (QName)   attrValues[XSAttributeChecker.ATTIDX_SUBSGROUP];
        QName   typeAtt      = (QName)   attrValues[XSAttributeChecker.ATTIDX_TYPE];

        // Step 1: get declaration information
        XSElementDecl element = new XSElementDecl();

        // get 'name'
        if (nameAtt != null)
            element.fName = fSymbolTable.addSymbol(nameAtt);

        // get 'target namespace'
        if (isGlobal) {
            element.fTargetNamespace = schemaDoc.fTargetNamespace;
        }
        else if (formAtt != null) {
            if (formAtt.intValue() == SchemaSymbols.FORM_QUALIFIED)
                element.fTargetNamespace = schemaDoc.fTargetNamespace;
            else
                element.fTargetNamespace = null;
        } else if (schemaDoc.fAreLocalElementsQualified) {
            element.fTargetNamespace = schemaDoc.fTargetNamespace;
        } else {
            element.fTargetNamespace = null;
        }

        // get 'block', 'final', 'nillable', 'abstract'
        element.fBlock = blockAtt == null ? schemaDoc.fBlockDefault : blockAtt.shortValue();
        element.fFinal = finalAtt == null ? schemaDoc.fFinalDefault : finalAtt.shortValue();
        if (nillableAtt.booleanValue())
            element.setIsNillable();
        if (abstractAtt != null && abstractAtt.booleanValue())
            element.setIsAbstract();

        // get 'value constraint'
        if (fixedAtt != null) {
            element.fDefault = new ValidatedInfo();
            element.fDefault.normalizedValue = fixedAtt;
            element.setConstraintType(XSElementDecl.FIXED_VALUE);
        } else if (defaultAtt != null) {
            element.fDefault = new ValidatedInfo();
            element.fDefault.normalizedValue = defaultAtt;
            element.setConstraintType(XSElementDecl.DEFAULT_VALUE);
        } else {
            element.setConstraintType(XSElementDecl.NO_CONSTRAINT);
        }

        // get 'substitutionGroup affiliation'
        // REVISIT: allow mutual-substitution
        //          we need to back-patch both fSubGroup and fType
        //          if there is no type attribute and no anonymous type
        if (subGroupAtt != null) {
            element.fSubGroup = (XSElementDecl)fSchemaHandler.getGlobalDecl(schemaDoc, XSDHandler.ELEMENT_TYPE, subGroupAtt);
        }

        // get 'annotation'
        Element child = DOMUtil.getFirstChildElement(elmDecl);
        if(child != null && DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION)) {
            traverseAnnotationDecl(child, attrValues, false, schemaDoc);
            child = DOMUtil.getNextSiblingElement(child);
        }

        // get 'type definition'
        XSTypeDecl elementType = null;
        boolean haveAnonType = false;

        // Handle Anonymous type if there is one
        if (child != null) {
            String childName = DOMUtil.getLocalName(child);

            if (childName.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
                elementType = fSchemaHandler.fComplexTypeTraverser.traverseLocal(child, schemaDoc, grammar);
                haveAnonType = true;
                child = DOMUtil.getNextSiblingElement(child);
            }
            else if (childName.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                elementType = fSchemaHandler.fSimpleTypeTraverser.traverseLocal(child, schemaDoc, grammar);
                haveAnonType = true;
                child = DOMUtil.getNextSiblingElement(child);
            }
        }

        // Handler type attribute
        if (elementType == null && typeAtt != null) {
            elementType = (XSTypeDecl)fSchemaHandler.getGlobalDecl(schemaDoc, XSDHandler.TYPEDECL_TYPE, typeAtt);
        }

        // Get it from the substitutionGroup declaration
        if (elementType == null && element.fSubGroup != null) {
            elementType = element.fSubGroup.fType;
        }

        if (elementType == null) {
            elementType = SchemaGrammar.fAnyType;
        }

        element.fType = elementType;

        // get 'identity constraint'

        // see if there's something here; it had better be key, keyref or unique.
        if (child != null) {
            String childName = DOMUtil.getLocalName(child);
            while (child != null &&
                   (childName.equals(SchemaSymbols.ELT_KEY) ||
                    childName.equals(SchemaSymbols.ELT_KEYREF) ||
                    childName.equals(SchemaSymbols.ELT_UNIQUE))) {

                if (childName.equals(SchemaSymbols.ELT_KEY) ||
                        childName.equals(SchemaSymbols.ELT_UNIQUE)) {
                    fSchemaHandler.fUniqueOrKeyTraverser.traverse(child, element, schemaDoc, grammar);
                    if(DOMUtil.getAttrValue(child, SchemaSymbols.ATT_NAME).length() != 0 ) {
                        fSchemaHandler.checkForDuplicateNames(
                            (schemaDoc.fTargetNamespace == null) ? ","+DOMUtil.getAttrValue(child, SchemaSymbols.ATT_NAME)
                            : schemaDoc.fTargetNamespace+","+ DOMUtil.getAttrValue(child, SchemaSymbols.ATT_NAME),
                            fSchemaHandler.getIDRegistry(),
                            child, schemaDoc);
                    }
                } else if (childName.equals(SchemaSymbols.ELT_KEYREF)) {
                    fSchemaHandler.storeKeyRef(child, schemaDoc, element);
                }
                child = DOMUtil.getNextSiblingElement(child);
                if (child != null) {
                    childName = DOMUtil.getLocalName(child);
                }
            }
        }

        // Step 2: register the element decl to the grammar
        if (isGlobal && nameAtt != null)
            grammar.addGlobalElementDecl(element);

        // Step 3: check against schema for schemas

        // required attributes
        if (nameAtt == null) {
            if (isGlobal)
                reportSchemaError("s4s-att-must-appear", new Object[]{NO_NAME, SchemaSymbols.ATT_NAME});
            else
                reportSchemaError("src-element.2.1", null);
            nameAtt = NO_NAME;
        }

        // element
        if (child != null) {
            reportSchemaError("s4s-elt-must-match", new Object[]{nameAtt, "(annotation?, (simpleType | complexType)?, (unique | key | keyref)*))"});
        }

        // Step 4: check 3.3.3 constraints

        // src-element

        // 1 default and fixed must not both be present.
        if (defaultAtt != null && fixedAtt != null) {
            reportSchemaError("src-element.1", new Object[]{nameAtt});
        }

        // 2 If the item's parent is not <schema>, then all of the following must be true:
        // 2.1 One of ref or name must be present, but not both.
        // This is checked in XSAttributeChecker

        // 2.2 If ref is present, then all of <complexType>, <simpleType>, <key>, <keyref>, <unique>, nillable, default, fixed, form, block and type must be absent, i.e. only minOccurs, maxOccurs, id are allowed in addition to ref, along with <annotation>.
        // Attributes are checked in XSAttributeChecker, elements are checked in "traverse" method

        // 3 type and either <simpleType> or <complexType> are mutually exclusive.
        if (haveAnonType && (typeAtt != null)) {
            reportSchemaError("src-element.3", new Object[]{nameAtt});
        }

        // Step 5: check 3.3.6 constraints
        // check for NOTATION type
        checkNotationType(nameAtt, elementType);

        // e-props-correct

        // 2 If there is a {value constraint}, the canonical lexical representation of its value must be valid with respect to the {type definition} as defined in Element Default Valid (Immediate) (3.3.6).
        if (element.fDefault != null) {
            fValidationState.setNamespaceSupport(schemaDoc.fNamespaceSupport);
            XSConstraints.ElementDefaultValidImmediate(element.fType, element.fDefault.normalizedValue, fValidationState, element.fDefault);
            if (element.fDefault.actualValue == null) {
                reportSchemaError ("e-props-correct.2", new Object[]{nameAtt, element.fDefault.normalizedValue});
                element.setConstraintType(XSElementDecl.NO_CONSTRAINT);
            }
        }

        // 3 If there is an {substitution group affiliation}, the {type definition} of the element declaration must be validly derived from the {type definition} of the {substitution group affiliation}, given the value of the {substitution group exclusions} of the {substitution group affiliation}, as defined in Type Derivation OK (Complex) (3.4.6) (if the {type definition} is complex) or as defined in Type Derivation OK (Simple) (3.14.6) (if the {type definition} is simple).
        if (element.fSubGroup != null) {
           if (!XSConstraints.checkTypeDerivationOk(element.fType, element.fSubGroup.fType, element.fSubGroup.fFinal)) {
                reportSchemaError ("e-props-correct.3", new Object[]{nameAtt, subGroupAtt.prefix+":"+subGroupAtt.localpart});
           }
        }

        // 4 If the {type definition} or {type definition}'s {content type} is or is derived from ID then there must not be a {value constraint}.
        if (element.fDefault != null) {
            if ((elementType.getXSType() == XSTypeDecl.SIMPLE_TYPE &&
                 ((XSSimpleType)elementType).isIDType()) ||
                (elementType.getXSType() == XSTypeDecl.COMPLEX_TYPE &&
                 ((XSComplexTypeDecl)elementType).containsTypeID())) {
                reportSchemaError ("e-props-correct.4", new Object[]{element.fName});
            }
        }

        return element;
    }

    void reset(XMLErrorReporter reporter, SymbolTable symbolTable) {
        super.reset(reporter, symbolTable);
        fDeferTraversingLocalElements = true;
    } // reset()

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16466.java