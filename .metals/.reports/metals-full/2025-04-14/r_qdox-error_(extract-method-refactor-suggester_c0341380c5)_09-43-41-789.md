error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5552.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5552.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5552.java
text:
```scala
A@@ssertionResult result = new AssertionResult(getName());

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

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.IntegerProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;
import org.apache.oro.text.MalformedCachePatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

// @see org.apache.jmeter.assertions.PackageTest for unit tests

/**
 * 
 * @author Michael Stover
 * @author <a href="mailto:jacarlco@katun.com">Jonathan Carlson</a>
 */
public class ResponseAssertion extends AbstractTestElement implements Serializable, Assertion {
	private static final Logger log = LoggingManager.getLoggerForClass();

	private final static String TEST_FIELD = "Assertion.test_field";  // $NON-NLS-1$

	// Values for TEST_FIELD
	// N.B. we cannot change the text value as it is in test plans
	private final static String SAMPLE_URL = "Assertion.sample_label"; // $NON-NLS-1$

	private final static String RESPONSE_DATA = "Assertion.response_data"; // $NON-NLS-1$

	private final static String RESPONSE_CODE = "Assertion.response_code"; // $NON-NLS-1$

	private final static String RESPONSE_MESSAGE = "Assertion.response_message"; // $NON-NLS-1$

	private final static String ASSUME_SUCCESS = "Assertion.assume_success"; // $NON-NLS-1$

	private final static String TEST_STRINGS = "Asserion.test_strings"; // $NON-NLS-1$

	private final static String TEST_TYPE = "Assertion.test_type"; // $NON-NLS-1$

	/*
	 * Mask values for TEST_TYPE TODO: remove either MATCH or CONTAINS - they
	 * are mutually exckusive
	 */
	private final static int MATCH = 1 << 0;

	final static int CONTAINS = 1 << 1;

	private final static int NOT = 1 << 2;

	private final static int EQUALS = 1 << 3;

    private static final int  EQUALS_SECTION_DIFF_LEN
            = JMeterUtils.getPropDefault("assertion.equals_section_diff_len", 100);

    /** Signifies truncated text in diff display. */
    private static final String EQUALS_DIFF_TRUNC = "...";

    private static final String RECEIVED_STR = "****** received  : ";
    private static final String COMPARISON_STR = "****** comparison: ";
    private static final String DIFF_DELTA_START
            = JMeterUtils.getPropDefault("assertion.equals_diff_delta_start", "[[[");
    private static final String DIFF_DELTA_END
            = JMeterUtils.getPropDefault("assertion.equals_diff_delta_end", "]]]");

	public ResponseAssertion() {
		setProperty(new CollectionProperty(TEST_STRINGS, new ArrayList()));
	}

	public void clear() {
		super.clear();
		setProperty(new CollectionProperty(TEST_STRINGS, new ArrayList()));
	}

	private void setTestField(String testField) {
		setProperty(TEST_FIELD, testField);
	}

	public void setTestFieldURL(){
		setTestField(SAMPLE_URL);
	}

	public void setTestFieldResponseCode(){
		setTestField(RESPONSE_CODE);
	}

	public void setTestFieldResponseData(){
		setTestField(RESPONSE_DATA);
	}

	public void setTestFieldResponseMessage(){
		setTestField(RESPONSE_MESSAGE);
	}

	public boolean isTestFieldURL(){
		return SAMPLE_URL.equals(getTestField());
	}

	public boolean isTestFieldResponseCode(){
		return RESPONSE_CODE.equals(getTestField());
	}

	public boolean isTestFieldResponseData(){
		return RESPONSE_DATA.equals(getTestField());
	}

	public boolean isTestFieldResponseMessage(){
		return RESPONSE_MESSAGE.equals(getTestField());
	}

	private void setTestType(int testType) {
		setProperty(new IntegerProperty(TEST_TYPE, testType));
	}

	public void addTestString(String testString) {
		getTestStrings().addProperty(new StringProperty(String.valueOf(testString.hashCode()), testString));
	}

	public void clearTestStrings() {
		getTestStrings().clear();
	}

	public AssertionResult getResult(SampleResult response) {
		AssertionResult result;

		// None of the other Assertions check the response status, so remove
		// this check
		// for the time being, at least...
		// if (!response.isSuccessful())
		// {
		// result = new AssertionResult();
		// result.setError(true);
		// byte [] ba = response.getResponseData();
		// result.setFailureMessage(
		// ba == null ? "Unknown Error (responseData is empty)" : new String(ba)
		// );
		// return result;
		// }

		result = evaluateResponse(response);
		return result;
	}

	/***************************************************************************
	 * !ToDoo (Method description)
	 * 
	 * @return !ToDo (Return description)
	 **************************************************************************/
	public String getTestField() {
		return getPropertyAsString(TEST_FIELD);
	}

	/***************************************************************************
	 * !ToDoo (Method description)
	 * 
	 * @return !ToDo (Return description)
	 **************************************************************************/
	public int getTestType() {
		JMeterProperty type = getProperty(TEST_TYPE);
		if (type instanceof NullProperty) {
			return CONTAINS;
		}
		return type.getIntValue();
	}

	/***************************************************************************
	 * !ToDoo (Method description)
	 * 
	 * @return !ToDo (Return description)
	 **************************************************************************/
	public CollectionProperty getTestStrings() {
		return (CollectionProperty) getProperty(TEST_STRINGS);
	}

	public boolean isEqualsType() {
		return (getTestType() & EQUALS) > 0;
	}

	public boolean isContainsType() {
		return (getTestType() & CONTAINS) > 0;
	}

	public boolean isMatchType() {
		return (getTestType() & MATCH) > 0;
	}

	public boolean isNotType() {
		return (getTestType() & NOT) > 0;
	}

	public void setToContainsType() {
		setTestType((getTestType() | CONTAINS) & ~(MATCH | EQUALS));
	}

	public void setToMatchType() {
		setTestType((getTestType() | MATCH) & ~(CONTAINS | EQUALS));
	}

	public void setToEqualsType() {
		setTestType((getTestType() | EQUALS) & ~(MATCH | CONTAINS));
	}

	public void setToNotType() {
		setTestType((getTestType() | NOT));
	}

	public void unsetNotType() {
		setTestType(getTestType() & ~NOT);
	}

	public boolean getAssumeSuccess() {
		return getPropertyAsBoolean(ASSUME_SUCCESS, false);
	}

	public void setAssumeSuccess(boolean b) {
		setProperty(ASSUME_SUCCESS, JOrphanUtils.booleanToString(b));
	}

	/**
	 * Make sure the response satisfies the specified assertion requirements.
	 * 
	 * @param response
	 *            an instance of SampleResult
	 * @return an instance of AssertionResult
	 */
	AssertionResult evaluateResponse(SampleResult response) {
		boolean pass = true;
		boolean not = (NOT & getTestType()) > 0;
		AssertionResult result = new AssertionResult();
		String toCheck = ""; // The string to check (Url or data)

		if (getAssumeSuccess()) {
			response.setSuccessful(true);// Allow testing of failure codes
		}

		// What are we testing against?
		if (isTestFieldResponseData()) {
			// TODO treat header separately from response? (would not apply to
			// all samplers)
			String data = response.getResponseDataAsString(); // (bug25052)
			toCheck = new StringBuffer(response.getResponseHeaders()).append(data).toString();
		} else if (isTestFieldResponseCode()) {
			toCheck = response.getResponseCode();
		} else if (isTestFieldResponseMessage()) {
			toCheck = response.getResponseMessage();
		} else { // Assume it is the URL
			toCheck = response.getSamplerData(); // TODO - is this where the URL is stored?
			if (toCheck == null)
				toCheck = "";
		}

		if (toCheck.length() == 0) {
			return result.setResultForNull();
		}

		result.setFailure(false);
		result.setError(false);

		boolean contains = isContainsType(); // do it once outside loop
		boolean equals = isEqualsType();
		boolean debugEnabled = log.isDebugEnabled();
		if (debugEnabled){
			log.debug("Type:" + (contains?"Contains":"Match") + (not? "(not)": ""));
		}
		
		try {
			// Get the Matcher for this thread
			Perl5Matcher localMatcher = JMeterUtils.getMatcher();
			PropertyIterator iter = getTestStrings().iterator();
			while (iter.hasNext()) {
				String stringPattern = iter.next().getStringValue();
				Pattern pattern = JMeterUtils.getPatternCache().getPattern(stringPattern, Perl5Compiler.READ_ONLY_MASK);
				boolean found;
				if (contains) {
					found = localMatcher.contains(toCheck, pattern);
                } else if (equals) {
                    found = toCheck.equals(stringPattern);
				} else {
					found = localMatcher.matches(toCheck, pattern);
				}
				pass = not ? !found : found;
				if (!pass) {
					if (debugEnabled){log.debug("Failed: "+pattern);}
					result.setFailure(true);
					result.setFailureMessage(getFailText(stringPattern,toCheck));
					break;
				}
				if (debugEnabled){log.debug("Passed: "+pattern);}
			}
		} catch (MalformedCachePatternException e) {
			result.setError(true);
			result.setFailure(false);
			result.setFailureMessage("Bad test configuration " + e);
		}
		return result;
	}

	/**
	 * Generate the failure reason from the TestType
	 * 
	 * @param stringPattern
	 * @return the message for the assertion report 
	 */
	// TODO strings should be resources
	private String getFailText(String stringPattern, String toCheck) {
		
		StringBuffer sb = new StringBuffer(200);
		sb.append("Test failed, ");

		if (ResponseAssertion.RESPONSE_DATA.equals(getTestField())) {
			sb.append("text");
		} else if (ResponseAssertion.RESPONSE_CODE.equals(getTestField())) {
			sb.append("code");
		} else if (ResponseAssertion.RESPONSE_MESSAGE.equals(getTestField())) {
			sb.append("message");
		} else // Assume it is the URL
		{
			sb.append("URL");
		}

		switch (getTestType()) {
		case CONTAINS:
			sb.append(" expected to contain ");
			break;
		case NOT | CONTAINS:
			sb.append(" expected not to contain ");
			break;
		case MATCH:
			sb.append(" expected to match ");
			break;
		case NOT | MATCH:
			sb.append(" expected not to match ");
			break;
		case EQUALS:
			sb.append(" expected to equal ");
			break;
		case NOT | EQUALS:
			sb.append(" expected not to equal ");
			break;
		default:// should never happen...
			sb.append(" expected something using ");
		}

		sb.append("/");
		
		if (isEqualsType()){
			sb.append(equalsComparisonText(toCheck, stringPattern));
		} else {
			sb.append(stringPattern);
		}
		
		sb.append("/");
		
		return sb.toString();
	}


    private static String trunc(final boolean right, final String str)
    {
        if (str.length() <= EQUALS_SECTION_DIFF_LEN)
            return str;
        else if (right)
            return str.substring(0, EQUALS_SECTION_DIFF_LEN) + EQUALS_DIFF_TRUNC;
        else
            return EQUALS_DIFF_TRUNC + str.substring(str.length() - EQUALS_SECTION_DIFF_LEN, str.length());
    }

    /**
     *   Returns some helpful logging text to determine where equality between two strings
     * is broken, with one pointer working from the front of the strings and another working
     * backwards from the end.
     *
     * @param received      String received from sampler.
     * @param comparison    String specified for "equals" response assertion.
     * @return  Two lines of text separated by newlines, and then forward and backward pointers
     *      denoting first position of difference.
     */
    private static StringBuffer equalsComparisonText(final String received, final String comparison)
    {
        final StringBuffer      text;
        int                     firstDiff;
        int                     lastRecDiff = -1;
        int                     lastCompDiff = -1;
        final int               recLength = received.length();
        final int               compLength = comparison.length();
        final int               minLength = Math.min(recLength, compLength);
        final String            startingEqSeq;
        String                  recDeltaSeq = "";
        String                  compDeltaSeq = "";
        String                  endingEqSeq = "";
        final StringBuffer      pad;


        text = new StringBuffer(Math.max(recLength, compLength) * 2);
        for (firstDiff = 0; firstDiff < minLength; firstDiff++)
            if (received.charAt(firstDiff) != comparison.charAt(firstDiff))
                break;
        if (firstDiff == 0)
            startingEqSeq = "";
        else
            startingEqSeq = trunc(false, received.substring(0, firstDiff));

        lastRecDiff = recLength - 1;
        lastCompDiff = compLength - 1;

        while ((lastRecDiff > firstDiff) && (lastCompDiff > firstDiff)
                && received.charAt(lastRecDiff) == comparison.charAt(lastCompDiff))
        {
            lastRecDiff--;
            lastCompDiff--;
        }
        endingEqSeq = trunc(true, received.substring(lastRecDiff + 1, recLength));
        if (endingEqSeq.length() == 0)
        {
            recDeltaSeq = trunc(true, received.substring(firstDiff, recLength));
            compDeltaSeq = trunc(true, comparison.substring(firstDiff, compLength));
        }
        else
        {
            recDeltaSeq = trunc(true, received.substring(firstDiff, lastRecDiff + 1));
            compDeltaSeq = trunc(true, comparison.substring(firstDiff, lastCompDiff + 1));
        }
        pad = new StringBuffer(Math.abs(recDeltaSeq.length() - compDeltaSeq.length()));
        for (int i = 0; i < pad.capacity(); i++)
            pad.append(' ');
        if (recDeltaSeq.length() > compDeltaSeq.length())
            compDeltaSeq += pad.toString();
        else
            recDeltaSeq += pad.toString();

        text.append("\n\n");
        text.append(RECEIVED_STR);
        text.append(startingEqSeq);
        text.append(DIFF_DELTA_START);
        text.append(recDeltaSeq);
        text.append(DIFF_DELTA_END);
        text.append(endingEqSeq);
        text.append("\n\n");
        text.append(COMPARISON_STR);
        text.append(startingEqSeq);
        text.append(DIFF_DELTA_START);
        text.append(compDeltaSeq);
        text.append(DIFF_DELTA_END);
        text.append(endingEqSeq);
        text.append("\n\n");
        return text;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5552.java