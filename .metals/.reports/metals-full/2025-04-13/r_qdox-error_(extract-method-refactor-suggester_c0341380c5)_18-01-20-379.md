error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6030.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6030.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6030.java
text:
```scala
X@@SSimpleTypeDecl negativeDV = new XSSimpleTypeDecl(nonPositiveDV, NEGATIVEINTEGER, URI_SCHEMAFORSCHEMA, (short)0, false, null, XSConstants.NEGATIVEINTEGER_DT);

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

package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.SchemaDVFactory;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.dv.XSFacets;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.util.SymbolHash;

/**
 * the factory to create/return built-in schema DVs and create user-defined DVs
 *
 * @xerces.internal 
 *
 * @author Neeraj Bajaj, Sun Microsystems, inc.
 * @author Sandy Gao, IBM
 *
 * @version $Id$
 */
public class BaseDVFactory extends SchemaDVFactory {

    static final String URI_SCHEMAFORSCHEMA = "http://www.w3.org/2001/XMLSchema";

    // there are 27 types. 53 is the closest prime number to 27*2=54.
    static SymbolHash fBaseTypes = new SymbolHash(53);
    static {
        createBuiltInTypes(fBaseTypes);
    }

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
        return (XSSimpleType)fBaseTypes.get(name);
    }

    /**
     * get all built-in simple types, which are stored in a hashtable keyed by
     * the name
     *
     * @return      a hashtable which contains all built-in simple types
     */
    public SymbolHash getBuiltInTypes() {
        return (SymbolHash)fBaseTypes.makeClone();
    }

    /**
     * Create a new simple type which is derived by restriction from another
     * simple type.
     *
     * @param name              name of the new type, could be null
     * @param targetNamespace   target namespace of the new type, could be null
     * @param finalSet          value of "final"
     * @param base              base type of the new type
     * @param annotations       set of annotations
     * @return                  the newly created simple type
     */
    public XSSimpleType createTypeRestriction(String name, String targetNamespace,
                                              short finalSet, XSSimpleType base, XSObjectList annotations) {
        return new XSSimpleTypeDecl((XSSimpleTypeDecl)base, name, targetNamespace, finalSet, false, annotations);
    }

    /**
     * Create a new simple type which is derived by list from another simple
     * type.
     *
     * @param name              name of the new type, could be null
     * @param targetNamespace   target namespace of the new type, could be null
     * @param finalSet          value of "final"
     * @param itemType          item type of the list type
     * @param annotations       set of annotations
     * @return                  the newly created simple type
     */
    public XSSimpleType createTypeList(String name, String targetNamespace,
                                       short finalSet, XSSimpleType itemType,
                                       XSObjectList annotations) {
        return new XSSimpleTypeDecl(name, targetNamespace, finalSet, (XSSimpleTypeDecl)itemType, false, annotations);
    }

    /**
     * Create a new simple type which is derived by union from a list of other
     * simple types.
     *
     * @param name              name of the new type, could be null
     * @param targetNamespace   target namespace of the new type, could be null
     * @param finalSet          value of "final"
     * @param memberTypes       member types of the union type
     * @param annotations       set of annotations
     * @return                  the newly created simple type
     */
    public XSSimpleType createTypeUnion(String name, String targetNamespace,
                                        short finalSet, XSSimpleType[] memberTypes,
                                        XSObjectList annotations) {
        int typeNum = memberTypes.length;
        XSSimpleTypeDecl[] mtypes = new XSSimpleTypeDecl[typeNum];
        System.arraycopy(memberTypes, 0, mtypes, 0, typeNum);

        return new XSSimpleTypeDecl(name, targetNamespace, finalSet, mtypes, annotations);
    }

    // create all built-in types
    static void createBuiltInTypes(SymbolHash types) {
        // base schema simple type names
        final String ANYSIMPLETYPE     = "anySimpleType";
        final String ANYURI            = "anyURI";
        final String BASE64BINARY      = "base64Binary";
        final String BOOLEAN           = "boolean";
        final String BYTE              = "byte";
        final String DATE              = "date";
        final String DATETIME          = "dateTime";
        final String DAY               = "gDay";
        final String DECIMAL           = "decimal";
        final String INT               = "int";
        final String INTEGER           = "integer";
        final String LONG              = "long";
        final String NEGATIVEINTEGER   = "negativeInteger";
        final String MONTH             = "gMonth";
        final String MONTHDAY          = "gMonthDay";
        final String NONNEGATIVEINTEGER= "nonNegativeInteger";
        final String NONPOSITIVEINTEGER= "nonPositiveInteger";
        final String POSITIVEINTEGER   = "positiveInteger";
        final String SHORT             = "short";
        final String STRING            = "string";
        final String TIME              = "time";
        final String UNSIGNEDBYTE      = "unsignedByte";
        final String UNSIGNEDINT       = "unsignedInt";
        final String UNSIGNEDLONG      = "unsignedLong";
        final String UNSIGNEDSHORT     = "unsignedShort";
        final String YEAR              = "gYear";
        final String YEARMONTH         = "gYearMonth";

        final XSFacets facets = new XSFacets();

        XSSimpleTypeDecl anySimpleType = XSSimpleTypeDecl.fAnySimpleType;
        types.put(ANYSIMPLETYPE, anySimpleType);
        XSSimpleTypeDecl stringDV = new XSSimpleTypeDecl(anySimpleType, STRING, XSSimpleTypeDecl.DV_STRING, XSSimpleType.ORDERED_FALSE, false, false, false, true, XSConstants.STRING_DT);
        types.put(STRING, stringDV);
        types.put(BOOLEAN, new XSSimpleTypeDecl(anySimpleType, BOOLEAN, XSSimpleTypeDecl.DV_BOOLEAN, XSSimpleType.ORDERED_FALSE, false, true, false, true, XSConstants.BOOLEAN_DT));
        XSSimpleTypeDecl decimalDV = new XSSimpleTypeDecl(anySimpleType, DECIMAL, XSSimpleTypeDecl.DV_DECIMAL, XSSimpleType.ORDERED_TOTAL, false, false, true, true, XSConstants.DECIMAL_DT);
        types.put(DECIMAL, decimalDV);

        types.put(ANYURI, new XSSimpleTypeDecl(anySimpleType, ANYURI, XSSimpleTypeDecl.DV_ANYURI, XSSimpleType.ORDERED_FALSE, false, false, false, true, XSConstants.ANYURI_DT));
        types.put(BASE64BINARY, new XSSimpleTypeDecl(anySimpleType, BASE64BINARY, XSSimpleTypeDecl.DV_BASE64BINARY, XSSimpleType.ORDERED_FALSE, false, false, false, true, XSConstants.BASE64BINARY_DT));
        types.put(DATETIME, new XSSimpleTypeDecl(anySimpleType, DATETIME, XSSimpleTypeDecl.DV_DATETIME, XSSimpleType.ORDERED_PARTIAL, false, false, false, true, XSConstants.DATETIME_DT));
        types.put(TIME, new XSSimpleTypeDecl(anySimpleType, TIME, XSSimpleTypeDecl.DV_TIME, XSSimpleType.ORDERED_PARTIAL, false, false, false, true, XSConstants.TIME_DT));
        types.put(DATE, new XSSimpleTypeDecl(anySimpleType, DATE, XSSimpleTypeDecl.DV_DATE, XSSimpleType.ORDERED_PARTIAL, false, false, false, true, XSConstants.DATE_DT));
        types.put(YEARMONTH, new XSSimpleTypeDecl(anySimpleType, YEARMONTH, XSSimpleTypeDecl.DV_GYEARMONTH, XSSimpleType.ORDERED_PARTIAL, false, false, false, true, XSConstants.GYEARMONTH_DT));
        types.put(YEAR, new XSSimpleTypeDecl(anySimpleType, YEAR, XSSimpleTypeDecl.DV_GYEAR, XSSimpleType.ORDERED_PARTIAL, false, false, false, true, XSConstants.GYEAR_DT));
        types.put(MONTHDAY, new XSSimpleTypeDecl(anySimpleType, MONTHDAY, XSSimpleTypeDecl.DV_GMONTHDAY, XSSimpleType.ORDERED_PARTIAL, false, false, false, true, XSConstants.GMONTHDAY_DT));
        types.put(DAY, new XSSimpleTypeDecl(anySimpleType, DAY, XSSimpleTypeDecl.DV_GDAY, XSSimpleType.ORDERED_PARTIAL, false, false, false, true, XSConstants.GDAY_DT));
        types.put(MONTH, new XSSimpleTypeDecl(anySimpleType, MONTH, XSSimpleTypeDecl.DV_GMONTH, XSSimpleType.ORDERED_PARTIAL, false, false, false, true, XSConstants.GMONTH_DT));

        XSSimpleTypeDecl integerDV = new XSSimpleTypeDecl(decimalDV, INTEGER, XSSimpleTypeDecl.DV_INTEGER, XSSimpleType.ORDERED_TOTAL, false, false, true, true, XSConstants.INTEGER_DT);
        types.put(INTEGER, integerDV);

        facets.maxInclusive = "0";
        XSSimpleTypeDecl nonPositiveDV = new XSSimpleTypeDecl(integerDV, NONPOSITIVEINTEGER, URI_SCHEMAFORSCHEMA, (short)0, false, null, XSConstants.NONPOSITIVEINTEGER_DT);
        nonPositiveDV.applyFacets1(facets , XSSimpleType.FACET_MAXINCLUSIVE, (short)0);
        types.put(NONPOSITIVEINTEGER, nonPositiveDV);

        facets.maxInclusive = "-1";
        XSSimpleTypeDecl negativeDV = new XSSimpleTypeDecl(integerDV, NEGATIVEINTEGER, URI_SCHEMAFORSCHEMA, (short)0, false, null, XSConstants.NEGATIVEINTEGER_DT);
        negativeDV.applyFacets1(facets , XSSimpleType.FACET_MAXINCLUSIVE, (short)0);
        types.put(NEGATIVEINTEGER, negativeDV);

        facets.maxInclusive = "9223372036854775807";
        facets.minInclusive = "-9223372036854775808";
        XSSimpleTypeDecl longDV = new XSSimpleTypeDecl(integerDV, LONG, URI_SCHEMAFORSCHEMA, (short)0, false, null, XSConstants.LONG_DT);
        longDV.applyFacets1(facets , (short)(XSSimpleType.FACET_MAXINCLUSIVE | XSSimpleType.FACET_MININCLUSIVE), (short)0 );
        types.put(LONG, longDV);


        facets.maxInclusive = "2147483647";
        facets.minInclusive =  "-2147483648";
        XSSimpleTypeDecl intDV = new XSSimpleTypeDecl(longDV, INT, URI_SCHEMAFORSCHEMA, (short)0, false, null, XSConstants.INT_DT);
        intDV.applyFacets1(facets, (short)(XSSimpleType.FACET_MAXINCLUSIVE | XSSimpleType.FACET_MININCLUSIVE), (short)0 );
        types.put(INT, intDV);

        facets.maxInclusive = "32767";
        facets.minInclusive = "-32768";
        XSSimpleTypeDecl shortDV = new XSSimpleTypeDecl(intDV, SHORT , URI_SCHEMAFORSCHEMA, (short)0, false, null, XSConstants.SHORT_DT);
        shortDV.applyFacets1(facets, (short)(XSSimpleType.FACET_MAXINCLUSIVE | XSSimpleType.FACET_MININCLUSIVE), (short)0 );
        types.put(SHORT, shortDV);

        facets.maxInclusive = "127";
        facets.minInclusive = "-128";
        XSSimpleTypeDecl byteDV = new XSSimpleTypeDecl(shortDV, BYTE , URI_SCHEMAFORSCHEMA, (short)0, false, null, XSConstants.BYTE_DT);
        byteDV.applyFacets1(facets, (short)(XSSimpleType.FACET_MAXINCLUSIVE | XSSimpleType.FACET_MININCLUSIVE), (short)0 );
        types.put(BYTE, byteDV);

        facets.minInclusive =  "0" ;
        XSSimpleTypeDecl nonNegativeDV = new XSSimpleTypeDecl(integerDV, NONNEGATIVEINTEGER , URI_SCHEMAFORSCHEMA, (short)0, false, null, XSConstants.NONNEGATIVEINTEGER_DT);
        nonNegativeDV.applyFacets1(facets, XSSimpleType.FACET_MININCLUSIVE, (short)0 );
        types.put(NONNEGATIVEINTEGER, nonNegativeDV);

        facets.maxInclusive = "18446744073709551615" ;
        XSSimpleTypeDecl unsignedLongDV = new XSSimpleTypeDecl(nonNegativeDV, UNSIGNEDLONG , URI_SCHEMAFORSCHEMA, (short)0, false, null, XSConstants.UNSIGNEDLONG_DT);
        unsignedLongDV.applyFacets1(facets, XSSimpleType.FACET_MAXINCLUSIVE, (short)0 );
        types.put(UNSIGNEDLONG, unsignedLongDV);

        facets.maxInclusive = "4294967295" ;
        XSSimpleTypeDecl unsignedIntDV = new XSSimpleTypeDecl(unsignedLongDV, UNSIGNEDINT , URI_SCHEMAFORSCHEMA, (short)0, false, null, XSConstants.UNSIGNEDINT_DT);
        unsignedIntDV.applyFacets1(facets, XSSimpleType.FACET_MAXINCLUSIVE, (short)0 );
        types.put(UNSIGNEDINT, unsignedIntDV);

        facets.maxInclusive = "65535" ;
        XSSimpleTypeDecl unsignedShortDV = new XSSimpleTypeDecl(unsignedIntDV, UNSIGNEDSHORT , URI_SCHEMAFORSCHEMA, (short)0, false, null, XSConstants.UNSIGNEDSHORT_DT);
        unsignedShortDV.applyFacets1(facets, XSSimpleType.FACET_MAXINCLUSIVE, (short)0 );
        types.put(UNSIGNEDSHORT, unsignedShortDV);

        facets.maxInclusive = "255" ;
        XSSimpleTypeDecl unsignedByteDV = new XSSimpleTypeDecl(unsignedShortDV, UNSIGNEDBYTE , URI_SCHEMAFORSCHEMA, (short)0, false, null, XSConstants.UNSIGNEDBYTE_DT);
        unsignedByteDV.applyFacets1(facets, XSSimpleType.FACET_MAXINCLUSIVE, (short)0 );
        types.put(UNSIGNEDBYTE, unsignedByteDV);

        facets.minInclusive = "1" ;
        XSSimpleTypeDecl positiveIntegerDV = new XSSimpleTypeDecl(nonNegativeDV, POSITIVEINTEGER , URI_SCHEMAFORSCHEMA, (short)0, false, null, XSConstants.POSITIVEINTEGER_DT);
        positiveIntegerDV.applyFacets1(facets, XSSimpleType.FACET_MININCLUSIVE, (short)0 );
        types.put(POSITIVEINTEGER, positiveIntegerDV);
    }//createBuiltInTypes(SymbolHash)

}//BaseDVFactory
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6030.java