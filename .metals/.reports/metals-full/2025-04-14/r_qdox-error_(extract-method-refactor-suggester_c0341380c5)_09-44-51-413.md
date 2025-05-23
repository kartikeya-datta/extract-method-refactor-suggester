error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6414.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6414.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6414.java
text:
```scala
public static final S@@tring DEFER_NODE_EXPANSION_FEATURE = "dom/defer-node-expansion";

/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 2000-2002 The Apache Software Foundation.  All rights
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

package org.apache.xerces.impl;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * Commonly used constants.
 *
 * @author Andy Clark, IBM
 *
 * @version $Id$
 */
public final class Constants {

    //
    // Constants
    //

    // sax features

    /** SAX feature prefix ("http://xml.org/sax/features/"). */
    public static final String SAX_FEATURE_PREFIX = "http://xml.org/sax/features/";

    /** Namespaces feature ("namespaces"). */
    public static final String NAMESPACES_FEATURE = "namespaces";

    /** Namespace prefixes feature ("namespace-prefixes"). */
    public static final String NAMESPACE_PREFIXES_FEATURE = "namespace-prefixes";

    /** String interning feature ("string-interning"). */
    public static final String STRING_INTERNING_FEATURE = "string-interning";

    /** Validation feature ("validation"). */
    public static final String VALIDATION_FEATURE = "validation";

    /** External general entities feature ("external-general-entities "). */
    public static final String EXTERNAL_GENERAL_ENTITIES_FEATURE = "external-general-entities";

    /** External parameter entities feature ("external-parameter-entities "). */
    public static final String EXTERNAL_PARAMETER_ENTITIES_FEATURE = "external-parameter-entities";

    // sax properties

    /** SAX property prefix ("http://xml.org/sax/properties/"). */
    public static final String SAX_PROPERTY_PREFIX = "http://xml.org/sax/properties/";

    /** Declaration handler property ("declaration-handler"). */
    public static final String DECLARATION_HANDLER_PROPERTY = "declaration-handler";

    /** Lexical handler property ("lexical-handler"). */
    public static final String LEXICAL_HANDLER_PROPERTY = "lexical-handler";

    /** DOM node property ("dom-node"). */
    public static final String DOM_NODE_PROPERTY = "dom-node";

    /** XML string property ("xml-string"). */
    public static final String XML_STRING_PROPERTY = "xml-string";


    //
    // JAXP properties
    //

    /** JAXP property prefix ("http://xml.org/sax/properties/"). */
    public static final String JAXP_PROPERTY_PREFIX =
            "http://java.sun.com/xml/jaxp/properties/";

    /** JAXP schemaSource property */
    public static final String SCHEMA_SOURCE = "schemaSource";


    //
    // DOM features
    //

    /** Comments feature ("include-comments"). */
    public static final String INCLUDE_COMMENTS_FEATURE = "include-comments";

    /** Create cdata nodes feature ("create-cdata-nodes"). */
    public static final String CREATE_CDATA_NODES_FEATURE = "create-cdata-nodes";

    /** Feature id: load as infoset. */
    public static final String LOAD_AS_INFOSET = "load-as-infoset";

    // xerces features

    /** Xerces features prefix ("http://apache.org/xml/features/"). */
    public static final String XERCES_FEATURE_PREFIX = "http://apache.org/xml/features/";

    /** Schema validation feature ("validation/schema"). */
    public static final String SCHEMA_VALIDATION_FEATURE = "validation/schema";

    /** Expose schema normalized values */
    public static final String SCHEMA_NORMALIZED_VALUE = "validation/schema/normalized-value";

    /** Send schema default value via characters() */
    public static final String SCHEMA_ELEMENT_DEFAULT = "validation/schema/element-default";

    /** Schema full constraint checking ("validation/schema-full-checking"). */
    public static final String SCHEMA_FULL_CHECKING = "validation/schema-full-checking";

    /** Dynamic validation feature ("validation/dynamic"). */
    public static final String DYNAMIC_VALIDATION_FEATURE = "validation/dynamic";

    /** Warn on duplicate attribute declaration feature ("validation/warn-on-duplicate-attdef"). */
    public static final String WARN_ON_DUPLICATE_ATTDEF_FEATURE = "validation/warn-on-duplicate-attdef";

    /** Warn on undeclared element feature ("validation/warn-on-undeclared-elemdef"). */
    public static final String WARN_ON_UNDECLARED_ELEMDEF_FEATURE = "validation/warn-on-undeclared-elemdef";

    /** Allow Java encoding names feature ("allow-java-encodings"). */
    public static final String ALLOW_JAVA_ENCODINGS_FEATURE = "allow-java-encodings";

    /** Continue after fatal error feature ("continue-after-fatal-error"). */
    public static final String CONTINUE_AFTER_FATAL_ERROR_FEATURE = "continue-after-fatal-error";

    /** Load dtd grammar when nonvalidating feature ("nonvalidating/load-dtd-grammar"). */
    public static final String LOAD_DTD_GRAMMAR_FEATURE = "nonvalidating/load-dtd-grammar";

    /** Load external dtd when nonvalidating feature ("nonvalidating/load-external-dtd"). */
    public static final String LOAD_EXTERNAL_DTD_FEATURE = "nonvalidating/load-external-dtd";

    /** Defer node expansion feature ("dom/defer-node-expansion"). */
    //public static final String DEFER_NODE_EXPANSION_FEATURE = "dom/defer-node-expansion";

    /** Create entity reference nodes feature ("dom/create-entity-ref-nodes"). */
    public static final String CREATE_ENTITY_REF_NODES_FEATURE = "dom/create-entity-ref-nodes";

    /** Include ignorable whitespace feature ("dom/include-ignorable-whitespace"). */
    public static final String INCLUDE_IGNORABLE_WHITESPACE = "dom/include-ignorable-whitespace";

    /** Default attribute values feature ("validation/default-attribute-values"). */
    public static final String DEFAULT_ATTRIBUTE_VALUES_FEATURE = "validation/default-attribute-values";

    /** Validate content models feature ("validation/validate-content-models"). */
    public static final String VALIDATE_CONTENT_MODELS_FEATURE = "validation/validate-content-models";

    /** Validate datatypes feature ("validation/validate-datatypes"). */
    public static final String VALIDATE_DATATYPES_FEATURE = "validation/validate-datatypes";

    /** Notify character references feature (scanner/notify-char-refs"). */
    public static final String NOTIFY_CHAR_REFS_FEATURE = "scanner/notify-char-refs";

    /** Notify built-in (&amp;amp;, etc.) references feature (scanner/notify-builtin-refs"). */
    public static final String NOTIFY_BUILTIN_REFS_FEATURE = "scanner/notify-builtin-refs";

    // xerces properties

    /** Xerces properties prefix ("http://apache.org/xml/properties/"). */
    public static final String XERCES_PROPERTY_PREFIX = "http://apache.org/xml/properties/";

    /** Current element node property ("dom/current-element-node"). */
    public static final String CURRENT_ELEMENT_NODE_PROPERTY = "dom/current-element-node";

    /** Document class name property ("dom/document-class-name"). */
    public static final String DOCUMENT_CLASS_NAME_PROPERTY = "dom/document-class-name";

    /** Symbol table property ("internal/symbol-table"). */
    public static final String SYMBOL_TABLE_PROPERTY = "internal/symbol-table";

    /** Error reporter property ("internal/error-reporter"). */
    public static final String ERROR_REPORTER_PROPERTY = "internal/error-reporter";

    /** Error handler property ("internal/error-handler"). */
    public static final String ERROR_HANDLER_PROPERTY = "internal/error-handler";

    /** Entity manager property ("internal/entity-manager"). */
    public static final String ENTITY_MANAGER_PROPERTY = "internal/entity-manager";

    /** Entity resolver property ("internal/entity-resolver"). */
    public static final String ENTITY_RESOLVER_PROPERTY = "internal/entity-resolver";

    /** Grammar pool property ("internal/grammar-pool"). */
    public static final String XMLGRAMMAR_POOL_PROPERTY = "internal/grammar-pool";

    /** Datatype validator factory ("internal/datatype-validator-factory"). */
    public static final String DATATYPE_VALIDATOR_FACTORY_PROPERTY = "internal/datatype-validator-factory";

    /** Document scanner property ("internal/document-scanner"). */
    public static final String DOCUMENT_SCANNER_PROPERTY = "internal/document-scanner";

    /** DTD scanner property ("internal/dtd-scanner"). */
    public static final String DTD_SCANNER_PROPERTY = "internal/dtd-scanner";

    /** Validator property ("internal/validator"). */
    public static final String VALIDATOR_PROPERTY = "internal/validator";

    /** Validator property ("internal/validator/dtd"). */
    public static final String DTD_VALIDATOR_PROPERTY = "internal/validator/dtd";

    /** Validator property ("internal/validator/schema"). */
    public static final String SCHEMA_VALIDATOR_PROPERTY = "internal/validator/schema";

    /** No namespace schema location property ("schema/external-schemaLocation"). */
    public static final String SCHEMA_LOCATION = "schema/external-schemaLocation";

    /** Schema location property ("schema/external-noNamespaceSchemaLocation"). */
    public static final String SCHEMA_NONS_LOCATION = "schema/external-noNamespaceSchemaLocation";

    /** Namespace binder property ("internal/namespace-binder"). */
    public static final String NAMESPACE_BINDER_PROPERTY = "internal/namespace-binder";

    /** Validation manager property ("internal/validation-manager"). */
    public static final String VALIDATION_MANAGER_PROPERTY = "internal/validation-manager";


    // general constants
    
    /** Element PSVI is stored in augmentations using string "ELEMENT_PSVI" */    
    public final static String ELEMENT_PSVI = "ELEMENT_PSVI";

    /* Attribute PSVI is stored in augmentations using string "ATTRIBUTE_PSVI" */
    public final static String ATTRIBUTE_PSVI = "ATTRIBUTE_PSVI";


    // private

    /** SAX features. */
    private static final String[] fgSAXFeatures = {
        NAMESPACES_FEATURE,
        NAMESPACE_PREFIXES_FEATURE,
        STRING_INTERNING_FEATURE,
        VALIDATION_FEATURE,
        EXTERNAL_GENERAL_ENTITIES_FEATURE,
        EXTERNAL_PARAMETER_ENTITIES_FEATURE,
    };

    /** SAX properties. */
    private static final String[] fgSAXProperties = {
        DECLARATION_HANDLER_PROPERTY,
        LEXICAL_HANDLER_PROPERTY,
        DOM_NODE_PROPERTY,
        XML_STRING_PROPERTY,
    };

    /** Xerces features. */
    private static final String[] fgXercesFeatures = {
        SCHEMA_VALIDATION_FEATURE,
        SCHEMA_FULL_CHECKING,
        DYNAMIC_VALIDATION_FEATURE,
        WARN_ON_DUPLICATE_ATTDEF_FEATURE,
        WARN_ON_UNDECLARED_ELEMDEF_FEATURE,
        ALLOW_JAVA_ENCODINGS_FEATURE,
        CONTINUE_AFTER_FATAL_ERROR_FEATURE,
        LOAD_DTD_GRAMMAR_FEATURE,
        LOAD_EXTERNAL_DTD_FEATURE,
        //DEFER_NODE_EXPANSION_FEATURE,
        CREATE_ENTITY_REF_NODES_FEATURE,
        INCLUDE_IGNORABLE_WHITESPACE,
        //GRAMMAR_ACCESS_FEATURE,
        DEFAULT_ATTRIBUTE_VALUES_FEATURE,
        VALIDATE_CONTENT_MODELS_FEATURE,
        VALIDATE_DATATYPES_FEATURE,
        NOTIFY_CHAR_REFS_FEATURE
    };

    /** Xerces properties. */
    private static final String[] fgXercesProperties = {
        CURRENT_ELEMENT_NODE_PROPERTY,
        DOCUMENT_CLASS_NAME_PROPERTY,
        SYMBOL_TABLE_PROPERTY,
        ERROR_HANDLER_PROPERTY,
        ERROR_REPORTER_PROPERTY,
        ENTITY_MANAGER_PROPERTY,
        ENTITY_RESOLVER_PROPERTY,
        XMLGRAMMAR_POOL_PROPERTY,
        DATATYPE_VALIDATOR_FACTORY_PROPERTY,
        DOCUMENT_SCANNER_PROPERTY,
        DTD_SCANNER_PROPERTY,
        VALIDATOR_PROPERTY,
        SCHEMA_LOCATION,
        SCHEMA_NONS_LOCATION,
        VALIDATION_MANAGER_PROPERTY
    };

    /** Empty enumeration. */
    private static final Enumeration fgEmptyEnumeration = new ArrayEnumeration(new Object[] {});

    //
    // Constructors
    //

    /** This class cannot be instantiated. */
    private Constants() {}

    //
    // Public methods
    //

    // sax

    /** Returns an enumeration of the SAX features. */
    public static Enumeration getSAXFeatures() {
        return fgSAXFeatures.length > 0
               ? new ArrayEnumeration(fgSAXFeatures) : fgEmptyEnumeration;
    } // getSAXFeatures():Enumeration

    /** Returns an enumeration of the SAX properties. */
    public static Enumeration getSAXProperties() {
        return fgSAXProperties.length > 0
               ? new ArrayEnumeration(fgSAXProperties) : fgEmptyEnumeration;
    } // getSAXProperties():Enumeration

    // xerces

    /** Returns an enumeration of the Xerces features. */
    public static Enumeration getXercesFeatures() {
        return fgXercesFeatures.length > 0
               ? new ArrayEnumeration(fgXercesFeatures) : fgEmptyEnumeration;
    } // getXercesFeatures():Enumeration

    /** Returns an enumeration of the Xerces properties. */
    public static Enumeration getXercesProperties() {
        return fgXercesProperties.length > 0
               ? new ArrayEnumeration(fgXercesProperties) : fgEmptyEnumeration;
    } // getXercesProperties():Enumeration

    //
    // Classes
    //

    /**
     * An array enumeration.
     *
     * @author Andy Clark, IBM
     */
    static class ArrayEnumeration
        implements Enumeration {

        //
        // Data
        //

        /** Array. */
        private Object[] array;

        /** Index. */
        private int index;

        //
        // Constructors
        //

        /** Constructs an array enumeration. */
        public ArrayEnumeration(Object[] array) {
            this.array = array;
        } // <init>(Object[])

        //
        // Enumeration methods
        //

        /**
         * Tests if this enumeration contains more elements.
         *
         * @return  <code>true</code> if this enumeration contains more elements;
         *          <code>false</code> otherwise.
         * @since   JDK1.0
         */
        public boolean hasMoreElements() {
            return index < array.length;
        } // hasMoreElement():boolean

        /**
         * Returns the next element of this enumeration.
         *
         * @return     the next element of this enumeration.
         * @exception  NoSuchElementException  if no more elements exist.
         * @since      JDK1.0
         */
        public Object nextElement() {
            if (index < array.length) {
                return array[index++];
            }
            throw new NoSuchElementException();
        } // nextElement():Object

    } // class ArrayEnumeration

    //
    // MAIN
    //

    /** Prints all of the constants to standard output. */
    public static void main(String[] argv) {

        print("SAX features:", SAX_FEATURE_PREFIX, fgSAXFeatures);
        print("SAX properties:", SAX_PROPERTY_PREFIX, fgSAXProperties);
        print("Xerces features:", XERCES_FEATURE_PREFIX, fgXercesFeatures);
        print("Xerces properties:", XERCES_PROPERTY_PREFIX, fgXercesProperties);

    } // main(String[])

    /** Prints a list of features/properties. */
    private static void print(String header, String prefix, Object[] array) {
        System.out.print(header);
        if (array.length > 0) {
            System.out.println();
            for (int i = 0; i < array.length; i++) {
                System.out.print("  ");
                System.out.print(prefix);
                System.out.println(array[i]);
            }
        }
        else {
            System.out.println(" none.");
        }
    } // print(String,String,Object[])

} // class Constants
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6414.java