error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10340.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10340.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10340.java
text:
```scala
i@@f ( fMinLength > fMaxLength ) {

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

import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;
import org.apache.xerces.validators.schema.SchemaSymbols;


/**
 * URIValidator validates that XML content is a W3C uri type,
 * according to RFC 2396
 * 
 * @author Ted Leung
 * @author Jeffrey Rodriguez
 * @see          RFC 2396 
 * @see Tim Berners-Lee, et. al. RFC 2396: Uniform Resource Identifiers (URI): Generic Syntax.. 1998 Available at: http://www.ietf.org/rfc/rfc2396.txt 
 * @version  $Id$
 */
public class URIReferenceDatatypeValidator extends AbstractDatatypeValidator {
    private DatatypeValidator fBaseValidator     = null;
    private boolean fDerivedByList               = false;

    private int       fLength          = 0;
    private int       fMaxLength       = Integer.MAX_VALUE;
    private int       fMinLength       = 0;
    private String    fPattern         = null;
    private Vector    fEnumeration     = null;
    private int       fFacetsDefined   = 0;

    
    public URIReferenceDatatypeValidator () throws InvalidDatatypeFacetException{
     this ( null, null, false ); // Native, No Facets defined, Restriction
    }

    public URIReferenceDatatypeValidator ( DatatypeValidator base, Hashtable facets, 
                     boolean derivedByList ) throws InvalidDatatypeFacetException {

         setBasetype( base ); // Set base type 

        // Set Facets if any defined
        if ( facets != null  ){
            if ( derivedByList == false) {
                for (Enumeration e = facets.keys(); e.hasMoreElements();) {
                    String key = (String) e.nextElement();

                    if ( key.equals(SchemaSymbols.ELT_LENGTH) ) {
                        fFacetsDefined += DatatypeValidator.FACET_LENGTH;
                        String lengthValue = (String)facets.get(key);
                        try {
                            fLength     = Integer.parseInt( lengthValue );
                        } catch (NumberFormatException nfe) {
                            throw new InvalidDatatypeFacetException("Length value '"+lengthValue+"' is invalid.");
                        }
                        if ( fLength < 0 )
                            throw new InvalidDatatypeFacetException("Length value '"+lengthValue+"'  must be a nonNegativeInteger.");

                    } else if (key.equals(SchemaSymbols.ELT_MINLENGTH) ) {
                        fFacetsDefined += DatatypeValidator.FACET_MINLENGTH;
                        String minLengthValue = (String)facets.get(key);
                        try {
                            fMinLength     = Integer.parseInt( minLengthValue );
                        } catch (NumberFormatException nfe) {
                            throw new InvalidDatatypeFacetException("maxLength value '"+minLengthValue+"' is invalid.");
                        }
                    } else if (key.equals(SchemaSymbols.ELT_MAXLENGTH) ) {
                        fFacetsDefined += DatatypeValidator.FACET_MAXLENGTH;
                        String maxLengthValue = (String)facets.get(key);
                        try {
                            fMaxLength     = Integer.parseInt( maxLengthValue );
                        } catch (NumberFormatException nfe) {
                            throw new InvalidDatatypeFacetException("maxLength value '"+maxLengthValue+"' is invalid.");
                        }
                    } else if (key.equals(SchemaSymbols.ELT_PATTERN)) {
                        fFacetsDefined += DatatypeValidator.FACET_PATTERN;
                        fPattern = (String)facets.get(key);
                    } else if (key.equals(SchemaSymbols.ELT_ENUMERATION)) {
                        fFacetsDefined += DatatypeValidator.FACET_ENUMERATION;
                        fEnumeration = (Vector)facets.get(key);
                    } else {
                        throw new InvalidDatatypeFacetException();
                    }
                }

                if (((fFacetsDefined & DatatypeValidator.FACET_LENGTH ) != 0 ) ) {
                    if (((fFacetsDefined & DatatypeValidator.FACET_MAXLENGTH ) != 0 ) ) {
                        throw new InvalidDatatypeFacetException(
                                                    "It is an error for both length and maxLength to be members of facets." );  
                    } else if (((fFacetsDefined & DatatypeValidator.FACET_MINLENGTH ) != 0 ) ) {
                        throw new InvalidDatatypeFacetException(
                                                    "It is an error for both length and minLength to be members of facets." );
                    }
                }

                if ( ( (fFacetsDefined & ( DatatypeValidator.FACET_MINLENGTH |
                                           DatatypeValidator.FACET_MAXLENGTH) ) != 0 ) ) {
                    if ( fMinLength < fMaxLength ) {
                        throw new InvalidDatatypeFacetException( "Value of maxLength = " + fMaxLength +
                                                      "must be greater that the value of minLength" + fMinLength );
                    }
                }
            } else { //derived by list
                fDerivedByList = true;
                for (Enumeration e = facets.keys(); e.hasMoreElements();) {
                    String key = (String) e.nextElement();
                    if ( key.equals(SchemaSymbols.ELT_LENGTH) ) {
                        fFacetsDefined += DatatypeValidator.FACET_LENGTH;
                        String lengthValue = (String)facets.get(key);
                        try {
                            fLength     = Integer.parseInt( lengthValue );
                        } catch (NumberFormatException nfe) {
                            throw new InvalidDatatypeFacetException("Length value '"+lengthValue+"' is invalid.");
                        }
                        if ( fLength < 0 )
                            throw new InvalidDatatypeFacetException("Length value '"+lengthValue+"'  must be a nonNegativeInteger.");

                    } else if (key.equals(SchemaSymbols.ELT_MINLENGTH) ) {
                        fFacetsDefined += DatatypeValidator.FACET_MINLENGTH;
                        String minLengthValue = (String)facets.get(key);
                        try {
                            fMinLength     = Integer.parseInt( minLengthValue );
                        } catch (NumberFormatException nfe) {
                            throw new InvalidDatatypeFacetException("maxLength value '"+minLengthValue+"' is invalid.");
                        }
                    } else if (key.equals(SchemaSymbols.ELT_MAXLENGTH) ) {
                        fFacetsDefined += DatatypeValidator.FACET_MAXLENGTH;
                        String maxLengthValue = (String)facets.get(key);
                        try {
                            fMaxLength     = Integer.parseInt( maxLengthValue );
                        } catch (NumberFormatException nfe) {
                            throw new InvalidDatatypeFacetException("maxLength value '"+maxLengthValue+"' is invalid.");
                        }
                    } else if (key.equals(SchemaSymbols.ELT_ENUMERATION)) {
                        fFacetsDefined += DatatypeValidator.FACET_ENUMERATION;
                        fEnumeration    = (Vector)facets.get(key);
                    } else {
                        throw new InvalidDatatypeFacetException();
                    }
                }
                if (((fFacetsDefined & DatatypeValidator.FACET_LENGTH ) != 0 ) ) {
                    if (((fFacetsDefined & DatatypeValidator.FACET_MAXLENGTH ) != 0 ) ) {
                        throw new InvalidDatatypeFacetException(
                                                    "It is an error for both length and maxLength to be members of facets." );  
                    } else if (((fFacetsDefined & DatatypeValidator.FACET_MINLENGTH ) != 0 ) ) {
                        throw new InvalidDatatypeFacetException(
                                                    "It is an error for both length and minLength to be members of facets." );
                    }
                }

                if ( ( (fFacetsDefined & ( DatatypeValidator.FACET_MINLENGTH |
                                           DatatypeValidator.FACET_MAXLENGTH) ) != 0 ) ) {
                    if ( fMinLength > fMaxLength ) {
                        throw new InvalidDatatypeFacetException( "Value of maxLength = " + fMinLength +
                                                      "must be greater that the value of minLength" + fMaxLength );
                    }
                }
            }
        }// End of Facets Setting

    }

    

    /**
     * Validates content to conform to a URIReference
     * definition and to conform to the facets allowed
     * for this datatype.
     * 
     * @param content
     * @param state
     * @return 
     * @exception InvalidDatatypeValueException
     */
    public Object validate(String content, Object state) 
                     throws InvalidDatatypeValueException
    {
        StringTokenizer parsedList = null;

        if ( fDerivedByList == true  ) { //derived by list
            parsedList = new StringTokenizer( content );
            try {
                while ( parsedList.hasMoreTokens() ) {
                    //checkContentList( parsedList.nextToken() ); TODO
                }
            } catch ( NoSuchElementException e ) {
                e.printStackTrace();
            }
        } else { //derived by constraint
            // 

            // checkContent( content ); TODO
        }
        return null;
    }



    /**
     * Compares two URIReferences for equality.
     * This is not really well defined.
     * 
     * @param content1
     * @param content2
     * @return 
     */
    public int compare( String content1, String content2){
        return 0;
    }
    /**
     * 
     * @return Returns a Hashtable containing the facet information
     *         for this datatype.
     */
    public Hashtable getFacets(){
        return null;
    }

      /**
     * Returns a copy of this object.
     */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("clone() is not supported in "+this.getClass().getName());
    }


    // Private methods starts here

    private void setBasetype(DatatypeValidator base) {
        fBaseValidator = base;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10340.java