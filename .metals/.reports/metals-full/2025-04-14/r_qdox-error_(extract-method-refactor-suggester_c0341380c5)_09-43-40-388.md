error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17212.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17212.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[462,2]

error in qdox parser
file content:
```java
offset: 16319
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17212.java
text:
```scala
//private static XPathAPI xpath = null;

/*
 * Copyright 2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import junit.framework.TestCase;
import junit.textui.TestRunner;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.XPathUtil;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



/**
 * Checks if the result is a well-formed XML content and whether it matches an XPath 
 * 
 * author <a href="mailto:jspears@astrology.com">Justin Spears </a>
 */
public class XPathAssertion extends AbstractTestElement implements
		Serializable, Assertion {
	private static final Logger log = LoggingManager.getLoggerForClass();


	private static XPathAPI xpath = null;

	private static final String XPATH_KEY		= "XPath.xpath";
	private static final String WHITESPACE_KEY	= "XPath.whitespace";
	private static final String VALIDATE_KEY	= "XPath.validate";
	private static final String TOLERANT_KEY	= "XPath.tolerant";
	private static final String NEGATE_KEY		= "XPath.negate";
	private static final String NAMESPACE_KEY	= "XPath.namespace";

	public static final String DEFAULT_XPATH = "/";
	
	

	/**
	 * Returns the result of the Assertion. Checks if the result is well-formed
	 * XML, and that the XPath expression is matched (or not, as the case may
	 * be)
	 */
	public AssertionResult getResult(SampleResult response) {
		// no error as default
		AssertionResult result = new AssertionResult();
		if (response.getResponseData() == null) {
			return setResultForNull(result);
		}
		result.setFailure(false);
		result.setFailureMessage("");

		if (log.isDebugEnabled()){
			log.debug(new StringBuffer("Validation is set to ").append(isValidating()).toString());
			log.debug(new StringBuffer("Whitespace is set to ").append(isWhitespace()).toString());
			log.debug(new StringBuffer("Tolerant is set to ").append(isTolerant()).toString());
		}

		Document doc = null;

		try {
			doc = XPathUtil.makeDocument(
					new ByteArrayInputStream(response.getResponseData()),
					isValidating(), isWhitespace(), isNamespace(), isTolerant());
		}catch (SAXException e) {
			log.debug("Caught sax exception: "+e);
			result.setError(true);
			result.setFailureMessage(new StringBuffer("SAXException: ").append(e.getMessage()).toString());
			return result;
		} catch (IOException e) {
			log.warn("Cannot parse result content", e);
			result.setError(true);
			result.setFailureMessage(new StringBuffer("IOException: ").append(e.getMessage()).toString());
			return result;
		} catch (ParserConfigurationException e) {
			log.warn("Cannot parse result content", e);
			result.setError(true);
			result.setFailureMessage(new StringBuffer("ParserConfigurationException: ").append(e.getMessage()).toString());
			return result;
		}
		
		if ( doc == null || doc.getDocumentElement() == null){
			result.setError(true);
			result.setFailureMessage("Document is null, probably not parsable");
			return result;
		}

		NodeList nodeList = null;

		try {
			nodeList = XPathAPI.selectNodeList(doc, getXPathString());
		} catch (TransformerException e) {
			result.setError(true);
			result.setFailureMessage(new StringBuffer("TransformerException: ").append(e.getMessage()).toString());
			return result;
		}
			
		if ( nodeList == null || nodeList.getLength() == 0   ) {
				log.debug(new StringBuffer("nodeList null no match  ").append(getXPathString()).toString());
				result.setFailure(!isNegated());
				result.setFailureMessage("No Nodes Matched " + getXPathString());
				return result;
		}
		log.debug("nodeList length "+nodeList.getLength());
		if (log.isDebugEnabled() &! isNegated()){
			for (int i=0; i< nodeList.getLength(); i++)
				log.debug(new StringBuffer("nodeList[").append(i).append("] ").append(nodeList.item(i)).toString());
		}
		result.setFailure(isNegated());
		if (isNegated()) 
			result.setFailureMessage("Specified XPath was found... Turn off negate if this is not desired");
		return result;
	}

	private AssertionResult setResultForNull(AssertionResult result) {
		result.setError(false);
		result.setFailure(true);
		result.setFailureMessage("Response was null");
		return result;
	}

	/**
	 * Get The XPath String that will be used in matching the document
	 * 
	 * @return String xpath String
	 */
	public String getXPathString() {
		return  getPropertyAsString(XPATH_KEY, DEFAULT_XPATH);
	}

	/**
	 * Get a Property or return the default string
	 * @param key Property Key
	 * @param defaultValue Default Value
	 * @return String property
	 */
	private String getPropertyAsString(String key, String defaultValue){
		String str = getPropertyAsString(key);
		return (str == null || str.length() == 0) ? defaultValue : str;
	}

	/**
	 * Set the XPath String this will be used as an xpath
	 * @param xpath String
	 */
	public void setXPathString(String xpath) {
		setProperty(new StringProperty(XPATH_KEY, xpath));
	}

	/**
	 * Set whether to ignore element whitespace
	 * @param whitespace
	 */
	public void setWhitespace(boolean whitespace) {
		setProperty(new BooleanProperty(WHITESPACE_KEY, whitespace));
	}

	/**
	 * Set use validation 
	 * @param validate
	 */
	public void setValidating(boolean validate) {
		setProperty(new BooleanProperty(VALIDATE_KEY, validate));
	}

	/**
	 * Set whether this is namespace aware 
	 * @param namespace
	 */
	public void setNamespace(boolean namespace) {
		setProperty(new BooleanProperty(NAMESPACE_KEY, namespace));
	}

	/**
	 * Set tolerant mode if required
	 * @param tolerant true/false
	 */
	public void setTolerant(boolean tolerant) {
		setProperty(new BooleanProperty(TOLERANT_KEY, tolerant));
	}

	public void setNegated(boolean negate){
		setProperty(new BooleanProperty(NEGATE_KEY, negate));
	}

	/**
	 * Is this whitepsace ignored.
	 * @return boolean
	 */
	public boolean isWhitespace() {
		return getPropertyAsBoolean(WHITESPACE_KEY, false);
	}

	/**
	 * Is this validating 
	 * @return boolean
	 */
	public boolean isValidating() {
		return getPropertyAsBoolean(VALIDATE_KEY, false);
	}
	/**
	 * Is this namespace aware?
	 * @return boolean
	 */
	public boolean isNamespace() {
		return getPropertyAsBoolean( NAMESPACE_KEY, false);
	}

	/**
	 * Is this using tolerant mode?
	 * @return boolean
	 */
	public boolean isTolerant() {
		return getPropertyAsBoolean(TOLERANT_KEY, false);
	}

	/**
	 * Negate the XPath test, that is return true if something
	 * is not found.
	 * @return boolean negated
	 */
	public boolean isNegated() {
		return getPropertyAsBoolean(NEGATE_KEY, false);
	}
	
	////////////////////////////////// TEST CASES //////////////////////////////
	
	public static class XPathAssertionTest extends TestCase {

        XPathAssertion assertion;
        SampleResult result;
        JMeterVariables vars;
        JMeterContext jmctx;
        public XPathAssertionTest() {super();}
        public XPathAssertionTest(String name)
        {
            super(name);
        }
        public void setUp()  {
    	jmctx = JMeterContextService.getContext();
        assertion = new XPathAssertion();
        assertion.setThreadContext(jmctx);// This would be done by the run command
//        assertion.setRefName("regVal");
        
        result = new SampleResult();
        String data =
            "<company-xmlext-query-ret>" +
              "<row>" +
                "<value field=\"RetCode\">LIS_OK</value>" +
                "<value field=\"RetCodeExtension\"></value>" +
                "<value field=\"alias\"></value>" +
                "<value field=\"positioncount\"></value>" +
                "<value field=\"invalidpincount\">0</value>" +
                "<value field=\"pinposition1\">1</value>" +
                "<value field=\"pinpositionvalue1\"></value>" +
                "<value field=\"pinposition2\">5</value>" +
                "<value field=\"pinpositionvalue2\"></value>" +
                "<value field=\"pinposition3\">6</value>" +
                "<value field=\"pinpositionvalue3\"></value>" +
              "</row>" +
            "</company-xmlext-query-ret>";
        result.setResponseData(data.getBytes());
        vars = new JMeterVariables();
        jmctx.setVariables(vars);
        jmctx.setPreviousResult(result);
        }

		public void testAssertion() throws Exception {
        	assertion.setXPathString("//row/value[@field = 'alias']");
        	AssertionResult res =	assertion.getResult(jmctx.getPreviousResult());
        	log.debug(" res "+res.isError());
        	log.debug(" failure "+res.getFailureMessage());
        	assertFalse(res.isError());
        	assertFalse(res.isFailure());
        }
        public void testNegateAssertion() throws Exception {
        	assertion.setXPathString("//row/value[@field = 'noalias']");
        	assertion.setNegated(true);
        	
        	AssertionResult res =	assertion.getResult(jmctx.getPreviousResult());
        	log.debug(" res "+res.isError());
        	log.debug(" failure "+res.getFailureMessage());
        	assertFalse(res.isError());
        	assertFalse(res.isFailure());
        }
        public void testValidationFailure() throws Exception {
        	assertion.setXPathString("//row/value[@field = 'alias']");
        	assertion.setNegated(false);
        	assertion.setValidating(true);
        	AssertionResult res =	assertion.getResult(jmctx.getPreviousResult());
        	log.debug(res.getFailureMessage()+" error: "+res.isError()+" failure: "+res.isFailure());
        	assertTrue(res.isError());
        	assertFalse(res.isFailure());
        	
        }
        public void testValidationSuccess() throws Exception {
        	 String data ="<?xml version=\"1.0\"?>"
        	 			+"<!DOCTYPE BOOK ["
        	 			+"<!ELEMENT p (#PCDATA)>"
        	 			+"<!ELEMENT BOOK         (OPENER,SUBTITLE?,INTRODUCTION?,(SECTION | PART)+)>"
        	 			+"<!ELEMENT OPENER       (TITLE_TEXT)*>"
        	 			+"<!ELEMENT TITLE_TEXT   (#PCDATA)>"
        	 			+"<!ELEMENT SUBTITLE     (#PCDATA)>"
        	 			+"<!ELEMENT INTRODUCTION (HEADER, p+)+>"
        	 			+"<!ELEMENT PART         (HEADER, CHAPTER+)>"
        	 			+"<!ELEMENT SECTION      (HEADER, p+)>"
        	 			+"<!ELEMENT HEADER       (#PCDATA)>"
        	 			+"<!ELEMENT CHAPTER      (CHAPTER_NUMBER, CHAPTER_TEXT)>"
        	 			+"<!ELEMENT CHAPTER_NUMBER (#PCDATA)>"
        	 			+"<!ELEMENT CHAPTER_TEXT (p)+>"
        	 			+"]>"
        	 			+"<BOOK>"
        	 			+"<OPENER>"
        	 			+"<TITLE_TEXT>All About Me</TITLE_TEXT>"
        	 			+"</OPENER>"
        	 			+"<PART>"
        	 			+"<HEADER>Welcome To My Book</HEADER>"
        	 			+"<CHAPTER>"
        	 			+"<CHAPTER_NUMBER>CHAPTER 1</CHAPTER_NUMBER>"
        	 			+"<CHAPTER_TEXT>"
        	 			+"<p>Glad you want to hear about me.</p>"
        	 			+"<p>There's so much to say!</p>"
        	 			+"<p>Where should we start?</p>"
        	 			+"<p>How about more about me?</p>"
        	 			+"</CHAPTER_TEXT>"
        	 			+"</CHAPTER>"
        	 			+"</PART>"
        	 			+"</BOOK>";
             
            result.setResponseData(data.getBytes());
            vars = new JMeterVariables();
            jmctx.setVariables(vars);
            jmctx.setPreviousResult(result);
            assertion.setXPathString("/");
            assertion.setValidating(true);
            AssertionResult res = assertion.getResult(result);
            assertFalse(res.isError());
        	assertFalse(res.isFailure());
        }
        public void testValidationFailureWithDTD() throws Exception {
       	 String data ="<?xml version=\"1.0\"?>"
       	 			+"<!DOCTYPE BOOK ["
       	 			+"<!ELEMENT p (#PCDATA)>"
       	 			+"<!ELEMENT BOOK         (OPENER,SUBTITLE?,INTRODUCTION?,(SECTION | PART)+)>"
       	 			+"<!ELEMENT OPENER       (TITLE_TEXT)*>"
       	 			+"<!ELEMENT TITLE_TEXT   (#PCDATA)>"
       	 			+"<!ELEMENT SUBTITLE     (#PCDATA)>"
       	 			+"<!ELEMENT INTRODUCTION (HEADER, p+)+>"
       	 			+"<!ELEMENT PART         (HEADER, CHAPTER+)>"
       	 			+"<!ELEMENT SECTION      (HEADER, p+)>"
       	 			+"<!ELEMENT HEADER       (#PCDATA)>"
       	 			+"<!ELEMENT CHAPTER      (CHAPTER_NUMBER, CHAPTER_TEXT)>"
       	 			+"<!ELEMENT CHAPTER_NUMBER (#PCDATA)>"
       	 			+"<!ELEMENT CHAPTER_TEXT (p)+>"
       	 			+"]>"
       	 			+"<BOOK>"
       	 			+"<OPENER>"
       	 			+"<TITLE_TEXT>All About Me</TITLE_TEXT>"
       	 			+"</OPENER>"
       	 			+"<PART>"
       	 			+"<HEADER>Welcome To My Book</HEADER>"
       	 			+"<CHAPTER>"
       	 			+"<CHAPTER_NUMBER>CHAPTER 1</CHAPTER_NUMBER>"
       	 			+"<CHAPTER_TEXT>"
       	 			+"<p>Glad you want to hear about me.</p>"
       	 			+"<p>There's so much to say!</p>"
       	 			+"<p>Where should we start?</p>"
       	 			+"<p>How about more about me?</p>"
       	 			+"</CHAPTER_TEXT>"
       	 			+"</CHAPTER>"
					+"<illegal>not defined in dtd</illegal>"
       	 			+"</PART>"
       	 			+"</BOOK>";
            
           result.setResponseData(data.getBytes());
           vars = new JMeterVariables();
           jmctx.setVariables(vars);
           jmctx.setPreviousResult(result);
           assertion.setXPathString("/");
           assertion.setValidating(true);
           AssertionResult res = assertion.getResult(result);
           log.debug("failureMessage: "+res.getFailureMessage());
           assertTrue(res.isError());
       	   assertFalse(res.isFailure());
       }
        public void testTolerance() throws Exception {
          	 String data ="<html><head><title>testtitle</title></head>"
          	 	+"<body>"
				+"<p><i><b>invalid tag nesting</i></b><hr>"
				+"</body></html>";
               
              result.setResponseData(data.getBytes());
              vars = new JMeterVariables();
              jmctx.setVariables(vars);
              jmctx.setPreviousResult(result);
              assertion.setXPathString("/html/head/title");
              assertion.setValidating(true);
              assertion.setTolerant(true);
              AssertionResult res = assertion.getResult(result);
              log.debug("failureMessage: "+res.getFailureMessage());
          	  assertFalse(res.isFailure());
              assertFalse(res.isError());
        }

		public void testNoTolerance() throws Exception {
         	 String data ="<html><head><title>testtitle</title></head>"
         	 	+"<body>"
				+"<p><i><b>invalid tag nesting</i></b><hr>"
				+"</body></html>";
              
             result.setResponseData(data.getBytes());
             vars = new JMeterVariables();
             jmctx.setVariables(vars);
             jmctx.setPreviousResult(result);
             assertion.setXPathString("/html/head/title");
             assertion.setValidating(false);
             assertion.setTolerant(false);
             AssertionResult res = assertion.getResult(result);
             log.debug("failureMessage: "+res.getFailureMessage());
             assertTrue(res.isError());
         	 assertFalse(res.isFailure());
         }
        
        public static void main(String[] args) {
        	TestRunner.run(XPathAssertionTest.class);
        }
	}
}
 N@@o newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17212.java