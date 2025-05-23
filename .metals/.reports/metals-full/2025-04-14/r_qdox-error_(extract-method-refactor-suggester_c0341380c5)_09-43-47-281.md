error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6289.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6289.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6289.java
text:
```scala
g@@etErrorString(DatatypeMessageProvider.NotFloat,

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
import java.util.Locale;
import java.util.Vector;
import org.apache.xerces.validators.schema.SchemaSymbols;
import org.apache.xerces.utils.regex.RegularExpression;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;

/**
 *
 * @author Ted Leung
 * @author Jeffrey Rodriguez
 * @version  $Id$
 */

public class FloatDatatypeValidator extends AbstractDatatypeValidator {
    private Locale    fLocale               = null;
    private DatatypeValidator    fBaseValidator = null; // null means a native datatype
    private float[]   fEnumFloats           = null;
    private String    fPattern              = null;
    private boolean   fDerivedByList     = false; // Default is restriction
    private float     fMaxInclusive         = Float.MAX_VALUE;
    private float     fMaxExclusive         = Float.MAX_VALUE;
    private float     fMinInclusive         = Float.MIN_VALUE;
    private float     fMinExclusive         = Float.MIN_VALUE;
    private int       fFacetsDefined        = 0;

    private boolean   isMaxExclusiveDefined = false;
    private boolean   isMaxInclusiveDefined = false;
    private boolean   isMinExclusiveDefined = false;
    private boolean   isMinInclusiveDefined = false;
    private DatatypeMessageProvider fMessageProvider = new DatatypeMessageProvider();
    private RegularExpression      fRegex    = null;



    public FloatDatatypeValidator () throws InvalidDatatypeFacetException{
        this( null, null, false ); // Native, No Facets defined, Restriction
    }

    public FloatDatatypeValidator ( DatatypeValidator base, Hashtable facets, 
                                    boolean derivedByList ) throws InvalidDatatypeFacetException {
        if ( base != null )
            setBasetype( base ); // Set base type 

        fDerivedByList = derivedByList; 
        // Set Facets if any defined

        if ( facets != null  )  {
            if ( fDerivedByList == false ) {
                for (Enumeration e = facets.keys(); e.hasMoreElements();) {
                    String key = (String) e.nextElement();

                    if (key.equals(SchemaSymbols.ELT_PATTERN)) {
                        fFacetsDefined += DatatypeValidator.FACET_PATTERN;
                        fPattern = (String)facets.get(key);
                        if ( fPattern != null )
                            fRegex = new RegularExpression(fPattern, "X" );


                    } else if (key.equals(SchemaSymbols.ELT_ENUMERATION)) {
                        fFacetsDefined += DatatypeValidator.FACET_ENUMERATION;
                        continue; //Treat the enumaration after this for loop
                    } else if (key.equals(SchemaSymbols.ELT_MAXINCLUSIVE)) {
                        fFacetsDefined += DatatypeValidator.FACET_MAXINCLUSIVE;
                        String value = null;
                        try {
                            value  = ((String)facets.get(key));
                            fMaxInclusive = Float.valueOf(value).floatValue();
                        } catch (NumberFormatException ex ) {
                            throw new InvalidDatatypeFacetException( getErrorString(
                                                                                   DatatypeMessageProvider.IllegalFacetValue, 
                                                                                   DatatypeMessageProvider.MSG_NONE, new Object [] { value, key}));
                        }
                    } else if (key.equals(SchemaSymbols.ELT_MAXEXCLUSIVE)) {
                        fFacetsDefined += DatatypeValidator.FACET_MAXEXCLUSIVE;
                        String value = null;
                        try {
                            value  = ((String)facets.get(key));
                            fMaxExclusive = Float.valueOf(value).floatValue();
                        } catch (NumberFormatException ex ) {
                            throw new InvalidDatatypeFacetException( getErrorString(
                                                                                   DatatypeMessageProvider.IllegalFacetValue, 
                                                                                   DatatypeMessageProvider.MSG_NONE, new Object [] { value, key}));
                        }
                    } else if (key.equals(SchemaSymbols.ELT_MININCLUSIVE)) {
                        fFacetsDefined += DatatypeValidator.FACET_MININCLUSIVE;
                        String value = null;
                        try {
                            value  = ((String)facets.get(key));
                            fMinInclusive  = Float.valueOf(value).floatValue();
                        } catch (NumberFormatException ex ) {
                            throw new InvalidDatatypeFacetException( getErrorString(
                                                                                   DatatypeMessageProvider.IllegalFacetValue, 
                                                                                   DatatypeMessageProvider.MSG_NONE, new Object [] { value, key}));
                        }
                    } else if (key.equals(SchemaSymbols.ELT_MINEXCLUSIVE)) {
                        fFacetsDefined += DatatypeValidator.FACET_MININCLUSIVE;
                        String value = null;
                        try {
                            value  = ((String)facets.get(key));
                            fMinExclusive  = Float.valueOf(value).floatValue();
                        } catch (NumberFormatException ex ) {
                            throw new InvalidDatatypeFacetException( getErrorString(
                                                                                   DatatypeMessageProvider.IllegalFacetValue, 
                                                                                   DatatypeMessageProvider.MSG_NONE, new Object [] { value, key}));
                        }
                    } else {
                        throw new InvalidDatatypeFacetException( getErrorString(  DatatypeMessageProvider.MSG_FORMAT_FAILURE,
                                                                                  DatatypeMessageProvider.MSG_NONE,
                                                                                  null));
                    }
                }
                isMaxExclusiveDefined = ((fFacetsDefined & 
                                          DatatypeValidator.FACET_MAXEXCLUSIVE ) != 0 )?true:false;
                isMaxInclusiveDefined = ((fFacetsDefined & 
                                          DatatypeValidator.FACET_MAXINCLUSIVE ) != 0 )?true:false;
                isMinExclusiveDefined = ((fFacetsDefined &
                                          DatatypeValidator.FACET_MINEXCLUSIVE ) != 0 )?true:false;
                isMinInclusiveDefined = ((fFacetsDefined &
                                          DatatypeValidator.FACET_MININCLUSIVE ) != 0 )?true:false;


                if ( isMaxExclusiveDefined && isMaxInclusiveDefined ) {
                    throw new InvalidDatatypeFacetException(
                                                           "It is an error for both maxInclusive and maxExclusive to be specified for the same datatype." ); 
                }
                if ( isMinExclusiveDefined && isMinInclusiveDefined ) {
                    throw new InvalidDatatypeFacetException(
                                                           "It is an error for both minInclusive and minExclusive to be specified for the same datatype." ); 
                }



                if ( (fFacetsDefined & DatatypeValidator.FACET_ENUMERATION ) != 0 ) {
                    Vector v = (Vector) facets.get(SchemaSymbols.ELT_ENUMERATION);    
                    if (v != null) {
                        fEnumFloats = new float[v.size()];
                        for (int i = 0; i < v.size(); i++)
                            try {
                                fEnumFloats[i] = Float.valueOf((String) v.elementAt(i)).floatValue();
                                boundsCheck(fEnumFloats[i]); // Check against max,min Inclusive, Exclusives
                            } catch (InvalidDatatypeValueException idve) {
                                throw new InvalidDatatypeFacetException(
                                                                       getErrorString(DatatypeMessageProvider.InvalidEnumValue,
                                                                                      DatatypeMessageProvider.MSG_NONE,
                                                                                      new Object [] { v.elementAt(i)}));
                            } catch (NumberFormatException nfe) {
                                System.out.println("Internal Error parsing enumerated values for real type");
                            }
                    }
                }
            } else { //derivation by list  - WORK TO DO
                //System.out.println( "inside derive by list" );

            }
        }// End facet setup
    }


    /**
     * validate that a string matches the real datatype
     * @param content A string containing the content to be validated
     * @exception throws InvalidDatatypeException if the content is
     *  is not a W3C real type
     */

    public Object validate(String content, Object state) 
    throws InvalidDatatypeValueException {
        if ( fDerivedByList == false  ) {
            checkContent(  content );
        } else {
           StringTokenizer parsedList = new StringTokenizer( content );
           try {
               while ( parsedList.hasMoreTokens() ) {
                   checkContent( parsedList.nextToken() );
               }
           } catch ( NoSuchElementException e ) {
               e.printStackTrace();
           }
        }
        return null;
    }

    /*
     * check that a facet is in range, assumes that facets are compatible -- compatibility ensured by setFacets
     */
    private void boundsCheck(float f) throws InvalidDatatypeValueException {
        boolean inUpperBound = false;
        boolean inLowerBound = false;

        if ( isMaxInclusiveDefined ) {
            inUpperBound = ( f <= fMaxInclusive );
        } else if ( isMaxExclusiveDefined ) {
            inUpperBound = ( f <  fMaxExclusive );
        }

        if ( isMinInclusiveDefined ) {
            inLowerBound = ( f >= fMinInclusive );
        } else if ( isMinExclusiveDefined ) {
            inLowerBound = ( f >  fMinExclusive );
        }

        if ( inUpperBound == false  || inLowerBound == false ) { // within bounds ?
            getErrorString(DatatypeMessageProvider.OutOfBounds,
                           DatatypeMessageProvider.MSG_NONE,
                           new Object [] { new Float(f), "","","",""}); //REVISIT
        }
    }

    private void enumCheck(float v) throws InvalidDatatypeValueException {
        for (int i = 0; i < fEnumFloats.length; i++) {
            if (v == fEnumFloats[i]) return;
        }
        throw new InvalidDatatypeValueException(
                                               getErrorString(DatatypeMessageProvider.NotAnEnumValue,
                                                              DatatypeMessageProvider.MSG_NONE,
                                                              new Object [] { new Float(v)}));
    }

    /**
     * set the locate to be used for error messages
     */
    public void setLocale(Locale locale) {
        fLocale = locale;
    }

    public int compare( String content1, String content2){
        return 0;
    }


    



    public Hashtable getFacets(){
        return null;
    }
    /**
       * Returns a copy of this object.
       */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("clone() is not supported in "+this.getClass().getName());
    }


    private String getErrorString(int major, int minor, Object args[]) {
        try {
            return fMessageProvider.createMessage(fLocale, major, minor, args);
        } catch (Exception e) {
            return "Illegal Errorcode "+minor;
        }
    }


    private void setBasetype(DatatypeValidator base) {
        fBaseValidator =  base;
    }

    private  void checkContent( String content )throws InvalidDatatypeValueException {
        if ( (fFacetsDefined & DatatypeValidator.FACET_PATTERN ) != 0 ) {
                if ( fRegex == null || fRegex.matches( content) == false )
                    throw new InvalidDatatypeValueException("Value'"+content+
                                                            "does not match regular expression facet" + fPattern );
            }


            float f = 0;
            try {
                //System.out.println("content = " + content );
                f = Float.valueOf(content).floatValue();
                //System.out.println("f = " + f );
            } catch (NumberFormatException nfe) {
                if( content.equals("INF") ){
                    f=Float.POSITIVE_INFINITY;
                } else if( content.equals("-INF") ){
                    f=Float.NEGATIVE_INFINITY;
                } else if( content.equals("NaN" ) ) {
                    f=Float.NaN;
                } else {
                    throw new InvalidDatatypeValueException(
                                  getErrorString(DatatypeMessageProvider.NotReal,
                                                 DatatypeMessageProvider.MSG_NONE,
                                                           new Object [] { content}));
                }
            }
            boundsCheck(f);

            if (((fFacetsDefined & DatatypeValidator.FACET_ENUMERATION ) != 0 ) )
                enumCheck(f);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6289.java