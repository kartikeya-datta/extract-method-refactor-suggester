error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16652.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16652.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16652.java
text:
```scala
f@@acets.pattern  = "([a-zA-Z]{1,8})(-[a-zA-Z0-9]{1,8})*";

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

package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.SchemaDVFactory;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.dv.XSFacets;
import org.apache.xerces.impl.xs.XSDeclarationPool;
import org.apache.xerces.util.SymbolHash;

/**
 * the factory to create/return built-in schema DVs and create user-defined DVs
 *
 * @author Neeraj Bajaj, Sun Microsystems, inc.
 * @author Sandy Gao, IBM
 *
 * @version $Id$
 */
public class SchemaDVFactoryImpl extends SchemaDVFactory {

    static final String URI_SCHEMAFORSCHEMA = "http://www.w3.org/2001/XMLSchema";

    static SymbolHash fBuiltInTypes = new SymbolHash();
    static {
        createBuiltInTypes();
    }

    protected XSDeclarationPool fDeclPool = null;

    /**
     * Get a built-in simple type of the given name
     * REVISIT: its still not decided within the Schema WG how to define the
     *          ur-types and if all simple types should be derived from a
     *          complex type, so as of now we ignore the fact that anySimpleType
     *          is derived from anyType, and pass 'null' as the base of
     *          anySimpleType. It needs to be changed as per the decision taken.
     *
     * @param name  the name of the datatype
     * @return      the datatype validator of the given name
     */
    public XSSimpleType getBuiltInType(String name) {
        return (XSSimpleType)fBuiltInTypes.get(name);
    }

    /**
     * get all built-in simple types, which are stored in a hashtable keyed by
     * the name
     *
     * @return      a hashtable which contains all built-in simple types
     */
    public SymbolHash getBuiltInTypes() {
        return (SymbolHash)fBuiltInTypes.makeClone();
    }

    /**
     * Create a new simple type which is derived by restriction from another
     * simple type.
     *
     * @param name              name of the new type, could be null
     * @param targetNamespace   target namespace of the new type, could be null
     * @param finalSet          value of "final"
     * @param base              base type of the new type
     * @return                  the newly created simple type
     */
    public XSSimpleType createTypeRestriction(String name, String targetNamespace,
                                              short finalSet, XSSimpleType base) {
        
        if (fDeclPool != null) {
           XSSimpleTypeDecl st= fDeclPool.getSimpleTypeDecl();
           return st.setRestrictionValues((XSSimpleTypeDecl)base, name, targetNamespace, finalSet);
        }
        return new XSSimpleTypeDecl((XSSimpleTypeDecl)base, name, targetNamespace, finalSet, false);
    }

    /**
     * Create a new simple type which is derived by list from another simple
     * type.
     *
     * @param name              name of the new type, could be null
     * @param targetNamespace   target namespace of the new type, could be null
     * @param finalSet          value of "final"
     * @param itemType          item type of the list type
     * @return                  the newly created simple type
     */
    public XSSimpleType createTypeList(String name, String targetNamespace,
                                       short finalSet, XSSimpleType itemType) {
        if (fDeclPool != null) {
           XSSimpleTypeDecl st= fDeclPool.getSimpleTypeDecl();
           return st.setListValues(name, targetNamespace, finalSet, (XSSimpleTypeDecl)itemType);
        }
        return new XSSimpleTypeDecl(name, targetNamespace, finalSet, (XSSimpleTypeDecl)itemType, false);
    }

    /**
     * Create a new simple type which is derived by union from a list of other
     * simple types.
     *
     * @param name              name of the new type, could be null
     * @param targetNamespace   target namespace of the new type, could be null
     * @param finalSet          value of "final"
     * @param base              member types of the union type
     * @return                  the newly created simple type
     */
    public XSSimpleType createTypeUnion(String name, String targetNamespace,
                                        short finalSet, XSSimpleType[] memberTypes) {
        int typeNum = memberTypes.length;
        XSSimpleTypeDecl[] mtypes = new XSSimpleTypeDecl[typeNum];
        System.arraycopy(memberTypes, 0, mtypes, 0, typeNum);

        if (fDeclPool != null) {
           XSSimpleTypeDecl st= fDeclPool.getSimpleTypeDecl();
           return st.setUnionValues(name, targetNamespace, finalSet, mtypes);
        }
        return new XSSimpleTypeDecl(name, targetNamespace, finalSet, mtypes);
    }

    // create all built-in types
    static void createBuiltInTypes() {
        // all schema simple type names
        final String ANYSIMPLETYPE     = "anySimpleType";
        final String ANYURI            = "anyURI";
        final String BASE64BINARY      = "base64Binary";
        final String BOOLEAN           = "boolean";
        final String BYTE              = "byte";
        final String DATE              = "date";
        final String DATETIME          = "dateTime";
        final String DAY               = "gDay";
        final String DECIMAL           = "decimal";
        final String DOUBLE            = "double";
        final String DURATION          = "duration";
        final String ENTITY            = "ENTITY";
        final String ENTITIES          = "ENTITIES";
        final String FLOAT             = "float";
        final String HEXBINARY         = "hexBinary";
        final String ID                = "ID";
        final String IDREF             = "IDREF";
        final String IDREFS            = "IDREFS";
        final String INT               = "int";
        final String INTEGER           = "integer";
        final String LONG              = "long";
        final String NAME              = "Name";
        final String NEGATIVEINTEGER   = "negativeInteger";
        final String MONTH             = "gMonth";
        final String MONTHDAY          = "gMonthDay";
        final String NCNAME            = "NCName";
        final String NMTOKEN           = "NMTOKEN";
        final String NMTOKENS          = "NMTOKENS";
        final String LANGUAGE          = "language";
        final String NONNEGATIVEINTEGER= "nonNegativeInteger";
        final String NONPOSITIVEINTEGER= "nonPositiveInteger";
        final String NORMALIZEDSTRING  = "normalizedString";
        final String NOTATION          = "NOTATION";
        final String POSITIVEINTEGER   = "positiveInteger";
        final String QNAME             = "QName";
        final String SHORT             = "short";
        final String STRING            = "string";
        final String TIME              = "time";
        final String TOKEN             = "token";
        final String UNSIGNEDBYTE      = "unsignedByte";
        final String UNSIGNEDINT       = "unsignedInt";
        final String UNSIGNEDLONG      = "unsignedLong";
        final String UNSIGNEDSHORT     = "unsignedShort";
        final String YEAR              = "gYear";
        final String YEARMONTH         = "gYearMonth";

        final XSFacets facets = new XSFacets();

        //REVISIT: passing "anyType" here.
        XSSimpleTypeDecl anySimpleType = XSSimpleTypeDecl.fAnySimpleType;
        fBuiltInTypes.put(ANYSIMPLETYPE, anySimpleType);
        XSSimpleTypeDecl stringDV = new XSSimpleTypeDecl(anySimpleType, STRING, XSSimpleTypeDecl.DV_STRING, XSSimpleType.ORDERED_FALSE, false, false, false , true);
        fBuiltInTypes.put(STRING, stringDV);
        fBuiltInTypes.put(BOOLEAN, new XSSimpleTypeDecl(anySimpleType, BOOLEAN, XSSimpleTypeDecl.DV_BOOLEAN, XSSimpleType.ORDERED_FALSE, false, true, false, true));
        XSSimpleTypeDecl decimalDV = new XSSimpleTypeDecl(anySimpleType, DECIMAL, XSSimpleTypeDecl.DV_DECIMAL, XSSimpleType.ORDERED_TOTAL, false, false, true, true);
        fBuiltInTypes.put(DECIMAL, decimalDV);

        fBuiltInTypes.put(ANYURI, new XSSimpleTypeDecl(anySimpleType, ANYURI, XSSimpleTypeDecl.DV_ANYURI, XSSimpleType.ORDERED_FALSE, false, false, false, true));
        fBuiltInTypes.put(BASE64BINARY, new XSSimpleTypeDecl(anySimpleType, BASE64BINARY, XSSimpleTypeDecl.DV_BASE64BINARY, XSSimpleType.ORDERED_FALSE, false, false, false, true));
        fBuiltInTypes.put(DURATION, new XSSimpleTypeDecl(anySimpleType, DURATION, XSSimpleTypeDecl.DV_DURATION, XSSimpleType.ORDERED_PARTIAL, false, false, false, true));
        fBuiltInTypes.put(DATETIME, new XSSimpleTypeDecl(anySimpleType, DATETIME, XSSimpleTypeDecl.DV_DATETIME, XSSimpleType.ORDERED_PARTIAL, false, false, false, true));
        fBuiltInTypes.put(TIME, new XSSimpleTypeDecl(anySimpleType, TIME, XSSimpleTypeDecl.DV_TIME, XSSimpleType.ORDERED_PARTIAL, false, false, false, true));
        fBuiltInTypes.put(DATE, new XSSimpleTypeDecl(anySimpleType, DATE, XSSimpleTypeDecl.DV_DATE, XSSimpleType.ORDERED_PARTIAL, false, false, false, true));
        fBuiltInTypes.put(YEARMONTH, new XSSimpleTypeDecl(anySimpleType, YEARMONTH, XSSimpleTypeDecl.DV_GYEARMONTH, XSSimpleType.ORDERED_PARTIAL, false, false, false, true));
        fBuiltInTypes.put(YEAR, new XSSimpleTypeDecl(anySimpleType, YEAR, XSSimpleTypeDecl.DV_GYEAR, XSSimpleType.ORDERED_PARTIAL, false, false, false, true));
        fBuiltInTypes.put(MONTHDAY, new XSSimpleTypeDecl(anySimpleType, MONTHDAY, XSSimpleTypeDecl.DV_GMONTHDAY, XSSimpleType.ORDERED_PARTIAL, false, false, false, true));
        fBuiltInTypes.put(DAY, new XSSimpleTypeDecl(anySimpleType, DAY, XSSimpleTypeDecl.DV_GDAY, XSSimpleType.ORDERED_PARTIAL, false, false, false, true));
        fBuiltInTypes.put(MONTH, new XSSimpleTypeDecl(anySimpleType, MONTH, XSSimpleTypeDecl.DV_GMONTH, XSSimpleType.ORDERED_PARTIAL, false, false, false, true));

        facets.fractionDigits = 0;
        XSSimpleTypeDecl integerDV = new XSSimpleTypeDecl(decimalDV, INTEGER, URI_SCHEMAFORSCHEMA, (short)0, false);
        integerDV.applyFacets1(facets , XSSimpleType.FACET_FRACTIONDIGITS, (short)0, XSSimpleTypeDecl.SPECIAL_PATTERN_INTEGER);
        fBuiltInTypes.put(INTEGER, integerDV);

        facets.maxInclusive = "0";
        XSSimpleTypeDecl nonPositiveDV = new XSSimpleTypeDecl(integerDV, NONPOSITIVEINTEGER, URI_SCHEMAFORSCHEMA, (short)0, false);
        nonPositiveDV.applyFacets1(facets , XSSimpleType.FACET_MAXINCLUSIVE, (short)0);
        fBuiltInTypes.put(NONPOSITIVEINTEGER, nonPositiveDV);

        facets.maxInclusive = "-1";
        XSSimpleTypeDecl negativeDV = new XSSimpleTypeDecl(integerDV, NEGATIVEINTEGER, URI_SCHEMAFORSCHEMA, (short)0, false);
        negativeDV.applyFacets1(facets , XSSimpleType.FACET_MAXINCLUSIVE, (short)0);
        fBuiltInTypes.put(NEGATIVEINTEGER, negativeDV);

        facets.maxInclusive = "9223372036854775807";
        facets.minInclusive = "-9223372036854775808";
        XSSimpleTypeDecl longDV = new XSSimpleTypeDecl(integerDV, LONG, URI_SCHEMAFORSCHEMA, (short)0, false);
        longDV.applyFacets1(facets , (short)(XSSimpleType.FACET_MAXINCLUSIVE | XSSimpleType.FACET_MININCLUSIVE), (short)0 );
        fBuiltInTypes.put(LONG, longDV);


        facets.maxInclusive = "2147483647";
        facets.minInclusive =  "-2147483648";
        XSSimpleTypeDecl intDV = new XSSimpleTypeDecl(longDV, INT, URI_SCHEMAFORSCHEMA, (short)0, false);
        intDV.applyFacets1(facets, (short)(XSSimpleType.FACET_MAXINCLUSIVE | XSSimpleType.FACET_MININCLUSIVE), (short)0 );
        fBuiltInTypes.put(INT, intDV);

        facets.maxInclusive = "32767";
        facets.minInclusive = "-32768";
        XSSimpleTypeDecl shortDV = new XSSimpleTypeDecl(intDV, SHORT , URI_SCHEMAFORSCHEMA, (short)0, false);
        shortDV.applyFacets1(facets, (short)(XSSimpleType.FACET_MAXINCLUSIVE | XSSimpleType.FACET_MININCLUSIVE), (short)0 );
        fBuiltInTypes.put(SHORT, shortDV);

        facets.maxInclusive = "127";
        facets.minInclusive = "-128";
        XSSimpleTypeDecl byteDV = new XSSimpleTypeDecl(shortDV, BYTE , URI_SCHEMAFORSCHEMA, (short)0, false);
        byteDV.applyFacets1(facets, (short)(XSSimpleType.FACET_MAXINCLUSIVE | XSSimpleType.FACET_MININCLUSIVE), (short)0 );
        fBuiltInTypes.put(BYTE, byteDV);

        facets.minInclusive =  "0" ;
        XSSimpleTypeDecl nonNegativeDV = new XSSimpleTypeDecl(integerDV, NONNEGATIVEINTEGER , URI_SCHEMAFORSCHEMA, (short)0, false);
        nonNegativeDV.applyFacets1(facets, XSSimpleType.FACET_MININCLUSIVE, (short)0 );
        fBuiltInTypes.put(NONNEGATIVEINTEGER, nonNegativeDV);

        facets.maxInclusive = "18446744073709551615" ;
        XSSimpleTypeDecl unsignedLongDV = new XSSimpleTypeDecl(nonNegativeDV, UNSIGNEDLONG , URI_SCHEMAFORSCHEMA, (short)0, false);
        unsignedLongDV.applyFacets1(facets, XSSimpleType.FACET_MAXINCLUSIVE, (short)0 );
        fBuiltInTypes.put(UNSIGNEDLONG, unsignedLongDV);

        facets.maxInclusive = "4294967295" ;
        XSSimpleTypeDecl unsignedIntDV = new XSSimpleTypeDecl(unsignedLongDV, UNSIGNEDINT , URI_SCHEMAFORSCHEMA, (short)0, false);
        unsignedIntDV.applyFacets1(facets, XSSimpleType.FACET_MAXINCLUSIVE, (short)0 );
        fBuiltInTypes.put(UNSIGNEDINT, unsignedIntDV);

        facets.maxInclusive = "65535" ;
        XSSimpleTypeDecl unsignedShortDV = new XSSimpleTypeDecl(unsignedIntDV, UNSIGNEDSHORT , URI_SCHEMAFORSCHEMA, (short)0, false);
        unsignedShortDV.applyFacets1(facets, XSSimpleType.FACET_MAXINCLUSIVE, (short)0 );
        fBuiltInTypes.put(UNSIGNEDSHORT, unsignedShortDV);

        facets.maxInclusive = "255" ;
        XSSimpleTypeDecl unsignedByteDV = new XSSimpleTypeDecl(unsignedShortDV, UNSIGNEDBYTE , URI_SCHEMAFORSCHEMA, (short)0, false);
        unsignedByteDV.applyFacets1(facets, XSSimpleType.FACET_MAXINCLUSIVE, (short)0 );
        fBuiltInTypes.put(UNSIGNEDBYTE, unsignedByteDV);

        facets.minInclusive = "1" ;
        XSSimpleTypeDecl positiveIntegerDV = new XSSimpleTypeDecl(nonNegativeDV, POSITIVEINTEGER , URI_SCHEMAFORSCHEMA, (short)0, false);
        positiveIntegerDV.applyFacets1(facets, XSSimpleType.FACET_MININCLUSIVE, (short)0 );
        fBuiltInTypes.put(POSITIVEINTEGER, positiveIntegerDV);


        fBuiltInTypes.put(FLOAT, new XSSimpleTypeDecl(anySimpleType, FLOAT, XSSimpleTypeDecl.DV_FLOAT, XSSimpleType.ORDERED_TOTAL, true, true, true, true));
        fBuiltInTypes.put(DOUBLE, new XSSimpleTypeDecl(anySimpleType, DOUBLE, XSSimpleTypeDecl.DV_DOUBLE, XSSimpleType.ORDERED_TOTAL, true, true, true, true));
        fBuiltInTypes.put(HEXBINARY, new XSSimpleTypeDecl(anySimpleType, HEXBINARY, XSSimpleTypeDecl.DV_HEXBINARY, XSSimpleType.ORDERED_FALSE, false, false, false, true));
        fBuiltInTypes.put(NOTATION, new XSSimpleTypeDecl(anySimpleType, NOTATION, XSSimpleTypeDecl.DV_NOTATION, XSSimpleType.ORDERED_FALSE, false, false, false, true));


        facets.whiteSpace =  XSSimpleType.WS_REPLACE;
        XSSimpleTypeDecl normalizedDV = new XSSimpleTypeDecl(stringDV, NORMALIZEDSTRING , URI_SCHEMAFORSCHEMA, (short)0, false);
        normalizedDV.applyFacets1(facets, XSSimpleType.FACET_WHITESPACE, (short)0 );
        fBuiltInTypes.put(NORMALIZEDSTRING, normalizedDV);

        facets.whiteSpace = XSSimpleType.WS_COLLAPSE;
        XSSimpleTypeDecl tokenDV = new XSSimpleTypeDecl(normalizedDV, TOKEN , URI_SCHEMAFORSCHEMA, (short)0, false);
        tokenDV.applyFacets1(facets, XSSimpleType.FACET_WHITESPACE, (short)0 );
        fBuiltInTypes.put(TOKEN, tokenDV);

        facets.whiteSpace = XSSimpleType.WS_COLLAPSE;
        facets.pattern  = "([a-zA-Z]{2}|[iI]-[a-zA-Z]+|[xX]-[a-zA-Z]+)(-[a-zA-Z]+)*";
        XSSimpleTypeDecl languageDV = new XSSimpleTypeDecl(tokenDV, LANGUAGE , URI_SCHEMAFORSCHEMA, (short)0, false);
        languageDV.applyFacets1(facets, (short)(XSSimpleType.FACET_WHITESPACE | XSSimpleType.FACET_PATTERN) ,(short)0);
        fBuiltInTypes.put(LANGUAGE, languageDV);


        facets.whiteSpace =  XSSimpleType.WS_COLLAPSE;
        XSSimpleTypeDecl nameDV = new XSSimpleTypeDecl(tokenDV, NAME , URI_SCHEMAFORSCHEMA, (short)0, false);
        nameDV.applyFacets1(facets, XSSimpleType.FACET_WHITESPACE, (short)0, XSSimpleTypeDecl.SPECIAL_PATTERN_NAME);
        fBuiltInTypes.put(NAME, nameDV);

        facets.whiteSpace = XSSimpleType.WS_COLLAPSE;
        XSSimpleTypeDecl ncnameDV = new XSSimpleTypeDecl(nameDV, NCNAME , URI_SCHEMAFORSCHEMA, (short)0, false) ;
        ncnameDV.applyFacets1(facets, XSSimpleType.FACET_WHITESPACE, (short)0, XSSimpleTypeDecl.SPECIAL_PATTERN_NCNAME);
        fBuiltInTypes.put(NCNAME, ncnameDV);

        fBuiltInTypes.put(QNAME, new XSSimpleTypeDecl(anySimpleType, QNAME, XSSimpleTypeDecl.DV_QNAME, XSSimpleType.ORDERED_FALSE, false, false, false, true));

        fBuiltInTypes.put(ID, new XSSimpleTypeDecl(ncnameDV,  ID, XSSimpleTypeDecl.DV_ID, XSSimpleType.ORDERED_FALSE, false, false, false , true));
        XSSimpleTypeDecl idrefDV = new XSSimpleTypeDecl(ncnameDV,  IDREF , XSSimpleTypeDecl.DV_IDREF, XSSimpleType.ORDERED_FALSE, false, false, false, true);
        fBuiltInTypes.put(IDREF, idrefDV);

        facets.minLength = 1;
        XSSimpleTypeDecl tempDV = new XSSimpleTypeDecl(null, URI_SCHEMAFORSCHEMA, (short)0, idrefDV, true);
        XSSimpleTypeDecl idrefsDV = new XSSimpleTypeDecl(tempDV, IDREFS, URI_SCHEMAFORSCHEMA, (short)0, false);
        idrefsDV.applyFacets1(facets, XSSimpleType.FACET_MINLENGTH, (short)0);
        fBuiltInTypes.put(IDREFS, idrefsDV);

        XSSimpleTypeDecl entityDV = new XSSimpleTypeDecl(ncnameDV, ENTITY , XSSimpleTypeDecl.DV_ENTITY, XSSimpleType.ORDERED_FALSE, false, false, false, true);
        fBuiltInTypes.put(ENTITY, entityDV);

        facets.minLength = 1;
        tempDV = new XSSimpleTypeDecl(null, URI_SCHEMAFORSCHEMA, (short)0, entityDV, true);
        XSSimpleTypeDecl entitiesDV = new XSSimpleTypeDecl(tempDV, ENTITIES, URI_SCHEMAFORSCHEMA, (short)0, false);
        entitiesDV.applyFacets1(facets, XSSimpleType.FACET_MINLENGTH, (short)0);
        fBuiltInTypes.put(ENTITIES, entitiesDV);


        facets.whiteSpace  = XSSimpleType.WS_COLLAPSE;
        XSSimpleTypeDecl nmtokenDV = new XSSimpleTypeDecl(tokenDV, NMTOKEN, URI_SCHEMAFORSCHEMA, (short)0, false);
        nmtokenDV.applyFacets1(facets, XSSimpleType.FACET_WHITESPACE, (short)0, XSSimpleTypeDecl.SPECIAL_PATTERN_NMTOKEN);
        fBuiltInTypes.put(NMTOKEN, nmtokenDV);

        facets.minLength = 1;
        tempDV = new XSSimpleTypeDecl(null, URI_SCHEMAFORSCHEMA, (short)0, nmtokenDV, true);
        XSSimpleTypeDecl nmtokensDV = new XSSimpleTypeDecl(tempDV, NMTOKENS, URI_SCHEMAFORSCHEMA, (short)0, false);
        nmtokensDV.applyFacets1(facets, XSSimpleType.FACET_MINLENGTH, (short)0);
        fBuiltInTypes.put(NMTOKENS, nmtokensDV);
    }//createBuiltInTypes()

    public void setDeclPool (XSDeclarationPool declPool){
        fDeclPool = declPool;
    }

}//SchemaDVFactory
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16652.java