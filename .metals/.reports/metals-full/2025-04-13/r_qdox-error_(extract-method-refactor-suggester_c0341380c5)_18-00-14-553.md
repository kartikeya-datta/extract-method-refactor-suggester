error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9187.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9187.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9187.java
text:
```scala
s@@canner.startEntity(fXMLSymbol, fEntityManager.getCurrentResourceIdentifier(), fEncoding, null);

/*
 * Copyright 1999-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.xerces.impl;

import java.io.EOFException;
import java.io.IOException;

import org.apache.xerces.impl.msg.XMLMessageFormatter;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLInputSource;

/**
 * This class scans the version of the document to determine
 * which scanner to use: XML 1.1 or XML 1.0.
 * The version is scanned using XML 1.1. scanner.  
 * 
 * @author Neil Graham, IBM 
 * @author Elena Litani, IBM
 * @version $Id$
 */

public class XMLVersionDetector {

    //
    // Constants
    //

    private final static char[] XML11_VERSION = new char[]{'1', '.', '1'};


    // property identifiers

    /** Property identifier: symbol table. */
    protected static final String SYMBOL_TABLE = 
        Constants.XERCES_PROPERTY_PREFIX + Constants.SYMBOL_TABLE_PROPERTY;

    /** Property identifier: error reporter. */
    protected static final String ERROR_REPORTER = 
        Constants.XERCES_PROPERTY_PREFIX + Constants.ERROR_REPORTER_PROPERTY;

    /** Property identifier: entity manager. */
    protected static final String ENTITY_MANAGER = 
        Constants.XERCES_PROPERTY_PREFIX + Constants.ENTITY_MANAGER_PROPERTY;

    //
    // Data
    //

    /** Symbol: "version". */
    protected final static String fVersionSymbol = "version".intern();

    // symbol:  [xml]:
    protected static final String fXMLSymbol = "[xml]".intern();

    /** Symbol table. */
    protected SymbolTable fSymbolTable;

    /** Error reporter. */
    protected XMLErrorReporter fErrorReporter;

    /** Entity manager. */
    protected XMLEntityManager fEntityManager;

    protected String fEncoding = null;

    private XMLString fVersionNum = new XMLString();

    private final char [] fExpectedVersionString = {'<', '?', 'x', 'm', 'l', ' ', 'v', 'e', 'r', 's', 
                    'i', 'o', 'n', '=', ' ', ' ', ' ', ' ', ' '};

    /**
     * 
     * 
     * @param componentManager The component manager.
     *
     * @throws SAXException Throws exception if required features and
     *                      properties cannot be found.
     */
    public void reset(XMLComponentManager componentManager)
        throws XMLConfigurationException {

        // Xerces properties
        fSymbolTable = (SymbolTable)componentManager.getProperty(SYMBOL_TABLE);
        fErrorReporter = (XMLErrorReporter)componentManager.getProperty(ERROR_REPORTER);
        fEntityManager = (XMLEntityManager)componentManager.getProperty(ENTITY_MANAGER);
        for(int i=14; i<fExpectedVersionString.length; i++ )
            fExpectedVersionString[i] = ' ';
    } // reset(XMLComponentManager)

    /**
     * Reset the reference to the appropriate scanner given the version of the
     * document and start document scanning.
     * @param scanner - the scanner to use
     * @param version - the version of the document (XML 1.1 or XML 1.0).
     */
    public void startDocumentParsing(XMLEntityHandler scanner, short version){

        if (version == Constants.XML_VERSION_1_0){
            fEntityManager.setScannerVersion(Constants.XML_VERSION_1_0);
        }
        else {
            fEntityManager.setScannerVersion(Constants.XML_VERSION_1_1);
        }
        // Make sure the locator used by the error reporter is the current entity scanner.
        fErrorReporter.setDocumentLocator(fEntityManager.getEntityScanner());
        
        // Note: above we reset fEntityScanner in the entity manager, thus in startEntity
        // in each scanner fEntityScanner field must be reset to reflect the change.
        // 
        fEntityManager.setEntityHandler(scanner);
        
        scanner.startEntity(fXMLSymbol, fEntityManager.getCurrentResourceIdentifier(), fEncoding);        
    }


    /**
     * This methods scans the XML declaration to find out the version 
     * (and provisional encoding)  of the document.
     * The scanning is doing using XML 1.1 scanner.
     * @param inputSource
     * @return short - Constants.XML_VERSION_1_1 if document version 1.1, 
     *                  otherwise Constants.XML_VERSION_1_0 
     * @throws IOException
     */
    public short determineDocVersion(XMLInputSource inputSource) throws IOException {
        fEncoding = fEntityManager.setupCurrentEntity(fXMLSymbol, inputSource, false, true);

        // Must use XML 1.0 scanner to handle whitespace correctly
        // in the XML declaration.
        fEntityManager.setScannerVersion(Constants.XML_VERSION_1_0);
        XMLEntityScanner scanner = fEntityManager.getEntityScanner();
        try {
            if (!scanner.skipString("<?xml")) {
                // definitely not a well-formed 1.1 doc!
                return Constants.XML_VERSION_1_0;
            }
            if (!scanner.skipDeclSpaces()) {
                fixupCurrentEntity(fEntityManager, fExpectedVersionString, 5);
                return Constants.XML_VERSION_1_0;
            }
            if (!scanner.skipString("version")) {
                fixupCurrentEntity(fEntityManager, fExpectedVersionString, 6);
                return Constants.XML_VERSION_1_0;
            }
            scanner.skipDeclSpaces();
            // Check if the next character is '='. If it is then consume it.
            if (scanner.peekChar() != '=') {
                fixupCurrentEntity(fEntityManager, fExpectedVersionString, 13);
                return Constants.XML_VERSION_1_0;
            }
            scanner.scanChar();
            scanner.skipDeclSpaces();
            int quoteChar = scanner.scanChar();
            fExpectedVersionString[14] = (char) quoteChar;
            for (int versionPos = 0; versionPos < XML11_VERSION.length; versionPos++) {
                fExpectedVersionString[15 + versionPos] = (char) scanner.scanChar();
            }
            // REVISIT:  should we check whether this equals quoteChar? 
            fExpectedVersionString[18] = (char) scanner.scanChar();
            fixupCurrentEntity(fEntityManager, fExpectedVersionString, 19);
            int matched = 0;
            for (; matched < XML11_VERSION.length; matched++) {
                if (fExpectedVersionString[15 + matched] != XML11_VERSION[matched])
                    break;
            }
            if (matched == XML11_VERSION.length)
                return Constants.XML_VERSION_1_1;
            return Constants.XML_VERSION_1_0;
            // premature end of file
        }
        catch (EOFException e) {
            fErrorReporter.reportError(
                XMLMessageFormatter.XML_DOMAIN,
                "PrematureEOF",
                null,
                XMLErrorReporter.SEVERITY_FATAL_ERROR);
            return Constants.XML_VERSION_1_0;
			
        }

    }

    // This method prepends "length" chars from the char array,
    // from offset 0, to the manager's fCurrentEntity.ch.
    private void fixupCurrentEntity(XMLEntityManager manager, 
                char [] scannedChars, int length) {
        XMLEntityManager.ScannedEntity currentEntity = manager.getCurrentEntity();
        if(currentEntity.count-currentEntity.position+length > currentEntity.ch.length) {
            //resize array; this case is hard to imagine...
            char[] tempCh = currentEntity.ch;
            currentEntity.ch = new char[length+currentEntity.count-currentEntity.position+1];
            System.arraycopy(tempCh, 0, currentEntity.ch, 0, tempCh.length);
        }
        if(currentEntity.position < length) {
            // have to move sensitive stuff out of the way...
            System.arraycopy(currentEntity.ch, currentEntity.position, currentEntity.ch, length, currentEntity.count-currentEntity.position);
            currentEntity.count += length-currentEntity.position;
        } else {
            // have to reintroduce some whitespace so this parses:
            for(int i=length; i<currentEntity.position; i++) 
                currentEntity.ch[i]=' ';
        }
        // prepend contents...
        System.arraycopy(scannedChars, 0, currentEntity.ch, 0, length);
        currentEntity.position = 0;
        currentEntity.columnNumber = currentEntity.lineNumber = 1;
    }

} // class XMLVersionDetector

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9187.java