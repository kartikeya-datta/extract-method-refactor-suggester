error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4942.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4942.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4942.java
text:
```scala
S@@tring resultData = new String(getResultBody(data)); // TODO - charset?

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.apache.jmeter.assertions;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;
// import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

// See Bug 34383

/**
 * XMLSchemaAssertion.java Validate response against an XML Schema author <a
 * href="mailto:d.maung@mdl.com">Dave Maung</a>
 * 
 */
public class XMLSchemaAssertion extends AbstractTestElement implements Serializable, Assertion {

    private static final long serialVersionUID = 233L;

    public static final String FILE_NAME_IS_REQUIRED = "FileName is required";

    public static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    public static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    public static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

    private static final Logger log = LoggingManager.getLoggerForClass();

    public static final String XSD_FILENAME_KEY = "xmlschema_assertion_filename";

    // private StringBuffer failureMessage = new StringBuffer();

    /**
     * getResult
     * 
     */
    public AssertionResult getResult(SampleResult response) {
        AssertionResult result = new AssertionResult(getName());
        // Note: initialised with error = failure = false

        byte data[] = response.getResponseData();
        if (data.length == 0) {
            return result.setResultForNull();
        }
        String resultData = new String(getResultBody(data));

        String xsdFileName = getXsdFileName();
        if (log.isDebugEnabled()) {
            log.debug("xmlString: " + resultData);
            log.debug("xsdFileName: " + xsdFileName);
        }
        if (xsdFileName == null || xsdFileName.length() == 0) {
            result.setResultForFailure(FILE_NAME_IS_REQUIRED);
        } else {
            setSchemaResult(result, resultData, xsdFileName);
        }
        return result;
    }

    /*
     * TODO move to SampleResult class? Return the body of the http return.
     */
    private byte[] getResultBody(byte[] resultData) {
        for (int i = 0; i < (resultData.length - 1); i++) {
            if (resultData[i] == '\n' && resultData[i + 1] == '\n') {
                return JOrphanUtils.getByteArraySlice(resultData, (i + 2), resultData.length - 1);
            }
        }
        return resultData;
    }

    public void setXsdFileName(String xmlSchemaFileName) throws IllegalArgumentException {
        setProperty(XSD_FILENAME_KEY, xmlSchemaFileName);
    }

    public String getXsdFileName() {
        return getPropertyAsString(XSD_FILENAME_KEY);
    }

    /**
     * set Schema result
     * 
     * @param result
     * @param xmlStr
     * @param xsdFileName
     */
    private void setSchemaResult(AssertionResult result, String xmlStr, String xsdFileName) {
        try {
            // boolean toReturn = true;

            // Document doc = null;
            DocumentBuilderFactory parserFactory = DocumentBuilderFactory.newInstance();
            parserFactory.setValidating(true);
            parserFactory.setNamespaceAware(true);
            parserFactory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            parserFactory.setAttribute(JAXP_SCHEMA_SOURCE, xsdFileName);

            // create a parser:
            DocumentBuilder parser = parserFactory.newDocumentBuilder();
            parser.setErrorHandler(new SAXErrorHandler(result));

            // doc =
            parser.parse(new InputSource(new StringReader(xmlStr)));
            // if everything went fine then xml schema validation is valid
        } catch (SAXParseException e) {

            // Only set message if error not yet flagged
            if (!result.isError() && !result.isFailure()) {
                result.setError(true);
                result.setFailureMessage(errorDetails(e));
            }

        } catch (SAXException e) {

            log.warn(e.toString());
            result.setResultForFailure(e.getMessage());

        } catch (IOException e) {

            log.warn("IO error", e);
            result.setResultForFailure(e.getMessage());

        } catch (ParserConfigurationException e) {

            log.warn("Problem with Parser Config", e);
            result.setResultForFailure(e.getMessage());

        }

    }

    // Helper method to construct SAX error details
    private static String errorDetails(SAXParseException spe) {
        StringBuffer str = new StringBuffer(80);
        int i;
        i = spe.getLineNumber();
        if (i != -1) {
            str.append("line=");
            str.append(i);
            str.append(" col=");
            str.append(spe.getColumnNumber());
            str.append(" ");
        }
        str.append(spe.getLocalizedMessage());
        return str.toString();
    }

    /**
     * SAXErrorHandler class
     */
    private static class SAXErrorHandler implements ErrorHandler {
        private AssertionResult result;

        public SAXErrorHandler(AssertionResult result) {
            this.result = result;
        }

        /*
         * Can be caused by: - failure to read XSD file - xml does not match XSD
         */
        public void error(SAXParseException exception) throws SAXParseException {

            String msg = "error: " + errorDetails(exception);
            log.debug(msg);
            result.setFailureMessage(msg);
            result.setError(true);
            throw exception;
        }

        /*
         * Can be caused by: - premature end of file - non-whitespace content
         * after trailer
         */
        public void fatalError(SAXParseException exception) throws SAXParseException {

            String msg = "fatal: " + errorDetails(exception);
            log.debug(msg);
            result.setFailureMessage(msg);
            result.setError(true);
            throw exception;
        }

        /*
         * Not clear what can cause this ? conflicting versions perhaps
         */
        public void warning(SAXParseException exception) throws SAXParseException {

            String msg = "warning: " + errorDetails(exception);
            log.debug(msg);
            result.setFailureMessage(msg);
            // result.setError(true); // TODO is this the correct strategy?
            // throw exception; // allow assertion to pass

        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4942.java