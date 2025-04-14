error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15893.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15893.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15893.java
text:
```scala
private static final S@@tring[] RECOGNIZED_PROPERTIES = {

/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 2001, 2002 The Apache Software Foundation.  
 * All rights reserved.
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

package org.apache.xerces.parsers;

import java.io.IOException;
import java.util.Locale;
import java.util.Hashtable;
import java.util.Enumeration;

import org.apache.xerces.impl.Constants;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarLoader;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.ObjectFactory;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLInputSource;

/**
 * <p> This class provides an easy way for a user to preparse grammars
 * of various types.  By default, it knows how to preparse external
 * DTD's and schemas; it provides an easy way for user applications to
 * register classes that know how to parse additional grammar types.
 * By default, it does no grammar caching; but it provides ways for
 * user applications to do so.  
 *
 * @author Neil Graham, IBM
 *
 * @version $Id$
 */
public class XMLGrammarPreparser { 

    //
    // Constants
    //

    /** Property identifier: symbol table. */
    protected static final String SYMBOL_TABLE = 
        Constants.XERCES_PROPERTY_PREFIX + Constants.SYMBOL_TABLE_PROPERTY;

    /** Property identifier: error reporter. */
    protected static final String ERROR_REPORTER =
        Constants.XERCES_PROPERTY_PREFIX + Constants.ERROR_REPORTER_PROPERTY;

    /** Property identifier: error handler. */
    protected static final String ERROR_HANDLER =
        Constants.XERCES_PROPERTY_PREFIX + Constants.ERROR_HANDLER_PROPERTY;

    /** Property identifier: entity resolver. */
    protected static final String ENTITY_RESOLVER =
        Constants.XERCES_PROPERTY_PREFIX + Constants.ENTITY_RESOLVER_PROPERTY;

    /** Property identifier: grammar pool . */
    protected static final String GRAMMAR_POOL =
        Constants.XERCES_PROPERTY_PREFIX + Constants.XMLGRAMMAR_POOL_PROPERTY;

    // the "built-in" grammar loaders
    private static final Hashtable KNOWN_LOADERS = new Hashtable();

    static {
        KNOWN_LOADERS.put(XMLGrammarDescription.XML_SCHEMA, 
            "org.apache.xerces.impl.xs.XMLSchemaLoader");
        KNOWN_LOADERS.put(XMLGrammarDescription.XML_DTD, 
            "org.apache.xerces.impl.dtd.XMLDTDLoader");
    }

    /** Recognized properties. */
    protected static final String[] RECOGNIZED_PROPERTIES = {
        SYMBOL_TABLE,       
        ERROR_REPORTER,
        ERROR_HANDLER,
        ENTITY_RESOLVER,
        GRAMMAR_POOL,       
    };

    // Data
    protected SymbolTable fSymbolTable;
    protected XMLErrorReporter fErrorReporter;
    protected XMLEntityResolver fEntityResolver;
    protected XMLGrammarPool fGrammarPool;

    protected Locale fLocale;

    // Hashtable holding our loaders
    private Hashtable fLoaders;

    //
    // Constructors
    //

    /** Default constructor. */
    public XMLGrammarPreparser() {
        this(new SymbolTable());
    } // <init>()

    /** 
     * Constructs a preparser using the specified symbol table. 
     *
     * @param symbolTable The symbol table to use.
     */
    public XMLGrammarPreparser (SymbolTable symbolTable) {
        fSymbolTable = symbolTable;

        fLoaders = new Hashtable();
        setLocale(Locale.getDefault());
        fErrorReporter = new XMLErrorReporter();
        fErrorReporter.setLocale(fLocale); 
        fEntityResolver = new XMLEntityManager();
        // those are all the basic properties...
    } // <init>(SymbolTable)

    //
    // Public methods
    //

    /* 
    * Register a type of grammar to make it preparsable.   If
    * the second parameter is null, the parser will use its  built-in
    * facilities for that grammar type.  
    * This should be called by the application immediately
    * after creating this object and before initializing any properties/features.
    * @param type   URI identifying the type of the grammar
    * @param loader an object capable of preparsing that type; null if the ppreparser should use built-in knowledge.
    * @return true if successful; false if no built-in knowledge of
    *       the type or if unable to instantiate the string we know about
    */
    public boolean registerPreparser(String grammarType, XMLGrammarLoader loader) { 
        if(loader == null) { // none specified!
            if(KNOWN_LOADERS.containsKey(grammarType)) {
                // got one; just instantiate it...
                String loaderName = (String)KNOWN_LOADERS.get(grammarType);
                try {
                    ClassLoader cl = ObjectFactory.findClassLoader();
                    XMLGrammarLoader gl = (XMLGrammarLoader)(ObjectFactory.newInstance(loaderName, cl));
                    fLoaders.put(grammarType, gl);
                } catch (Exception e) {
                    return false;
                }
                return true;
            }
            return false;
        }
        // were given one
        fLoaders.put(grammarType, loader);
        return true;
    } // registerPreparser(String, XMLGrammarLoader):  boolean

    /**
     * Parse a grammar from a location identified by an
     * XMLInputSource.  
     * This method also adds this grammar to the XMLGrammarPool
     *
     * @param type The type of the grammar to be constructed
     * @param source The XMLInputSource containing this grammar's
     * information
     * <strong>If a URI is included in the systemId field, the parser will not expand this URI or make it
     * available to the EntityResolver</strong>
     * @return The newly created <code>Grammar</code>.
     * @exception XNIException thrown on an error in grammar
     * construction
     * @exception IOException thrown if an error is encountered
     * in reading the file
     */
    public Grammar preparseGrammar(String type, XMLInputSource
                is) throws XNIException, IOException {
        if(fLoaders.containsKey(type)) {
            XMLGrammarLoader gl = (XMLGrammarLoader)fLoaders.get(type);
            // make sure gl's been set up with all the "basic" properties:
            gl.setProperty(SYMBOL_TABLE, fSymbolTable);
            gl.setProperty(ENTITY_RESOLVER, fEntityResolver);
            gl.setProperty(ERROR_REPORTER, fErrorReporter);
            // potentially, not all will support this one...
            if(fGrammarPool != null) {
                try {
                    gl.setProperty(GRAMMAR_POOL, fGrammarPool);
                } catch(Exception e) {
                    // too bad...
                } 
            }
            return gl.loadGrammar(is);
        }
        return null;
    } // preparseGrammar(String, XMLInputSource):  Grammar

    /**
     * Set the locale to use for messages.
     *
     * @param locale The locale object to use for localization of messages.
     *
     * @exception XNIException Thrown if the parser does not support the
     *                         specified locale.
     */
    public void setLocale(Locale locale) {
        fLocale = locale;
    } // setLocale(Locale)

    /** Return the Locale the XMLGrammarLoader is using. */
    public Locale getLocale() {
        return fLocale;
    } // getLocale():  Locale


    /**
     * Sets the error handler.
     *
     * @param errorHandler The error handler.
     */
    public void setErrorHandler(XMLErrorHandler errorHandler) {
        fErrorReporter.setProperty(ERROR_HANDLER, errorHandler);
    } // setErrorHandler(XMLErrorHandler)

    /** Returns the registered error handler.  */
    public XMLErrorHandler getErrorHandler() {
        return fErrorReporter.getErrorHandler();
    } // getErrorHandler():  XMLErrorHandler

    /**
     * Sets the entity resolver.
     *
     * @param entityResolver The new entity resolver.
     */
    public void setEntityResolver(XMLEntityResolver entityResolver) {
        fEntityResolver = entityResolver;
    } // setEntityResolver(XMLEntityResolver)

    /** Returns the registered entity resolver.  */
    public XMLEntityResolver getEntityResolver() {
        return fEntityResolver;
    } // getEntityResolver():  XMLEntityResolver

    /**
     * Sets the grammar pool.
     *
     * @param grammarPool The new grammar pool.
     */
    public void setGrammarPool(XMLGrammarPool grammarPool) {
        fGrammarPool = grammarPool;
    } // setGrammarPool(XMLGrammarPool)

    /** Returns the registered grammar pool.  */
    public XMLGrammarPool getGrammarPool() {
        return fGrammarPool;
    } // getGrammarPool():  XMLGrammarPool

    // it's possible the application may want access to a certain loader to do
    // some custom work.
    public XMLGrammarLoader getLoader(String type) {
        return (XMLGrammarLoader)fLoaders.get(type);
    } // getLoader(String):  XMLGrammarLoader

    // set a feature.  This method tries to set it on all
    // registered loaders; it eats any resulting exceptions.  If
    // an app needs to know if a particular feature is supported
    // by a grammar loader of a particular type, it will have
    // to retrieve that loader and use the loader's setFeature method.
    public void setFeature(String featureId, boolean value) {
        Enumeration keys = fLoaders.keys();
        while(keys.hasMoreElements()){
            Object key = keys.nextElement();
            XMLGrammarLoader gl = (XMLGrammarLoader)fLoaders.get(key);
            try {
                gl.setFeature(featureId, value);
            } catch(Exception e) {
                // eat it up...
            }
        }
    } //setFeature(String, boolean)

    // set a property.  This method tries to set it on all
    // registered loaders; it eats any resulting exceptions.  If
    // an app needs to know if a particular property is supported
    // by a grammar loader of a particular type, it will have
    // to retrieve that loader and use the loader's setProperty method.
    // <p> <strong>An application should use the explicit method
    // in this class to set "standard" properties like error handler etc.</strong>
    public void setProperty(String propId, Object value) {
        Enumeration keys = fLoaders.keys();
        while(keys.hasMoreElements()){
            Object key = keys.nextElement();
            XMLGrammarLoader gl = (XMLGrammarLoader)fLoaders.get(key);
            try {
                gl.setProperty(propId, value);
            } catch(Exception e) {
                // eat it up...
            }
        }
    } //setProperty(String, Object)
    
    // get status of feature in a particular loader.  This
    // catches no exceptions--including NPE's--so the application had
    // better make sure the loader exists and knows about this feature.
    // @param type type of grammar to look for the feature in.
    // @param featureId the feature string to query.
    // @return the value of the feature.
    public boolean getFeature(String type, String featureId) {
        XMLGrammarLoader gl = (XMLGrammarLoader)fLoaders.get(type);
        return gl.getFeature(featureId);
    } // getFeature (String, String):  boolean

    // get status of property in a particular loader.  This
    // catches no exceptions--including NPE's--so the application had
    // better make sure the loader exists and knows about this property.
    // <strong>For standard properties--that will be supported
    // by all loaders--the specific methods should be queried!</strong>
    // @param type type of grammar to look for the property in.
    // @param propertyId the property string to query.
    // @return the value of the property.
    public Object getProperty(String type, String propertyId) {
        XMLGrammarLoader gl = (XMLGrammarLoader)fLoaders.get(type);
        return gl.getProperty(propertyId);
    } // getProperty(String, String):  Object
} // class XMLGrammarPreparser
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15893.java