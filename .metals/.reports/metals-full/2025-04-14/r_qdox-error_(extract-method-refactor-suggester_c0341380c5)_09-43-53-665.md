error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12231.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12231.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12231.java
text:
```scala
f@@Enumeration[i] = dValueOf((String)enumeration.elementAt(i));

/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 1999, 2000 The Apache Software Foundation.  All rights
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

package org.apache.xerces.validators.datatype;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.xerces.validators.schema.SchemaSymbols;
import org.apache.xerces.utils.regex.RegularExpression;

/**
 * @author Elena Litani
 * @author Ted Leung
 * @author Jeffrey Rodriguez
 * @author Mark Swinkles - List Validation refactoring
 * @version $Id$
 */

public class DoubleDatatypeValidator extends AbstractNumericFacetValidator {

    public DoubleDatatypeValidator () throws InvalidDatatypeFacetException {
        this( null, null, false ); // Native, No Facets defined, Restriction
    }

    public DoubleDatatypeValidator ( DatatypeValidator base, Hashtable facets,
                                     boolean derivedByList ) throws InvalidDatatypeFacetException  {
        super(base, facets, derivedByList);
    }

    public int compare( String value1, String value2) {
        try {
            double d1 = dValueOf(value1).doubleValue();
            double d2 = dValueOf(value2).doubleValue();
            return compareDoubles(d1, d2);
        }
        catch ( NumberFormatException e ) {
            //REVISIT: should we throw exception??
            return -1;
        }

    }

    /**
     * Compares 2 double values.
     * 
     * @param value1 - Double
     * @param value2 - Double
     * @return value1<value2 return -1
     *         value1>value2 return 1
     *         value1==value2 return 0
     */
    protected int compareValues (Object value1, Object value2) {
        double d1 = ((Double)value1).doubleValue();
        double d2 = ((Double)value2).doubleValue();
        return compareDoubles(d1, d2);
    }

    protected void setMaxInclusive (String value) {
        fMaxInclusive = dValueOf(value);
    }
    protected void setMinInclusive (String value) {
        fMinInclusive = dValueOf(value);

    }
    protected void setMaxExclusive (String value) {
        fMaxExclusive = dValueOf(value);

    }
    protected void setMinExclusive (String value) {
        fMinExclusive = dValueOf(value);

    }
    protected void setEnumeration (Vector enumeration) throws InvalidDatatypeValueException{
        if ( enumeration != null ) {
            fEnumeration = new Double[enumeration.size()];
            Object baseEnum=null;
            try {

                for ( int i = 0; i < enumeration.size(); i++ ) {
                    fEnumeration[i] = new Double( (String)enumeration.elementAt(i));
                    ((DoubleDatatypeValidator)fBaseValidator).validate((String)enumeration.elementAt(i), null);

                }
            }
            catch ( Exception e ) {
                throw new InvalidDatatypeValueException(e.getMessage());
            }
        }
    }


    protected String getMaxInclusive (boolean isBase) {
        return(isBase)?(((DoubleDatatypeValidator)fBaseValidator).fMaxInclusive.toString())
        :((Double)fMaxInclusive).toString();
    }
    protected String getMinInclusive (boolean isBase) {
        return(isBase)?(((DoubleDatatypeValidator)fBaseValidator).fMinInclusive.toString())
        :((Double)fMinInclusive).toString();
    }
    protected String getMaxExclusive (boolean isBase) {
        return(isBase)?(((DoubleDatatypeValidator)fBaseValidator).fMaxExclusive.toString())
        :((Double)fMaxExclusive).toString();
    }
    protected String getMinExclusive (boolean isBase) {
        return(isBase)?(((DoubleDatatypeValidator)fBaseValidator).fMinExclusive.toString())
        :((Double)fMinExclusive).toString();
    }

    /**
    * validate if the content is valid against base datatype and facets (if any)
    * this function might be called directly from UnionDatatype or ListDatatype
    *
    * @param content A string containing the content to be validated
    * @param enumeration A vector with enumeration strings
    * @exception throws InvalidDatatypeException if the content is
    *  is not a W3C double type;
    * @exception throws InvalidDatatypeFacetException if enumeration is not double
    */

    protected void checkContentEnum(String content, Object state, Vector enumeration)
    throws InvalidDatatypeValueException {
        checkContent (content, state, enumeration, false);
    }

    protected void checkContent(String content, Object state, Vector enumeration, boolean asBase)
    throws InvalidDatatypeValueException {
        // validate against parent type if any
        if ( this.fBaseValidator != null ) {
            // validate content as a base type
            ((DoubleDatatypeValidator)fBaseValidator).checkContent(content, state, enumeration, true);
        }

        // we check pattern first
        if ( (fFacetsDefined & DatatypeValidator.FACET_PATTERN ) != 0 ) {
            if ( fRegex == null || fRegex.matches( content) == false )
                throw new InvalidDatatypeValueException("Value'"+content+
                                                        "does not match regular expression facet" + fPattern );
        }

        // if this is a base validator, we only need to check pattern facet
        // all other facet were inherited by the derived type
        if ( asBase )
            return;

        Double d = null;
        try {
            d = dValueOf(content);
        }
        catch ( NumberFormatException nfe ) {
            throw new InvalidDatatypeValueException( getErrorString(DatatypeMessageProvider.NotDouble,
                                                                    DatatypeMessageProvider.MSG_NONE,
                                                                    new Object [] { content}));
        }

        if ( enumeration != null ) { //the call was made from List or union
            int size = enumeration.size();
            Double[] enumDoubles = new Double[size];
            int i=0;
            try {
                for ( ; i < size; i++ )
                    enumDoubles[i] = dValueOf((String) enumeration.elementAt(i));
            }
            catch ( NumberFormatException nfe ) {
                throw new InvalidDatatypeValueException( getErrorString(DatatypeMessageProvider.InvalidEnumValue,
                                                                        DatatypeMessageProvider.MSG_NONE,
                                                                        new Object [] { enumeration.elementAt(i)}));
            }

            enumCheck(d.doubleValue(), enumDoubles);
        }

        boundsCheck(d);

        if ( ((fFacetsDefined & DatatypeValidator.FACET_ENUMERATION ) != 0 &&
              (fEnumeration != null) ) )
            enumCheck(d.doubleValue(), (Double[])fEnumeration);
    }

    //
    // private methods
    //
    private static Double dValueOf(String s) throws NumberFormatException {
        Double d;
        try {
            d = Double.valueOf(s);
        }
        catch ( NumberFormatException nfe ) {
            if ( s.equals("INF") ) {
                d = new Double(Double.POSITIVE_INFINITY);
            }
            else if ( s.equals("-INF") ) {
                d = new Double (Double.NEGATIVE_INFINITY);
            }
            else if ( s.equals("NaN" ) ) {
                d = new Double (Double.NaN);
            }
            else {
                throw nfe;
            }
        }
        return d;
    }

    private void enumCheck(double v, Double[] enumDoubles) throws InvalidDatatypeValueException {
        for ( int i = 0; i < enumDoubles.length; i++ ) {
            if ( v == enumDoubles[i].doubleValue() ) return;
        }
        throw new InvalidDatatypeValueException(
                                               getErrorString(DatatypeMessageProvider.NotAnEnumValue,
                                                              DatatypeMessageProvider.MSG_NONE,
                                                              new Object [] { new Double(v)}));
    }

    private int compareDoubles(double d1, double d2) {
        long d1V = Double.doubleToLongBits(d1);
        long d2V = Double.doubleToLongBits(d2);

        if ( d1 > d2 ) {
            return 1;
        }
        if ( d1 < d2 ) {
            return -1;
        }
        if ( d1V == d2V ) {
            return 0;
        }
        //REVISIT: NaN values.. revisit when new PR vs of Schema is available
        return(d1V < d2V) ? -1 : 1;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12231.java