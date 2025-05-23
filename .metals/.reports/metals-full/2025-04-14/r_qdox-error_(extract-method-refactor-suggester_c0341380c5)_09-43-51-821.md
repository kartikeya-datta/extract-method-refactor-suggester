error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8828.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8828.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8828.java
text:
```scala
e@@xtractor = new JoddExtractor();

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

package org.apache.jmeter.extractor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.jmeter.processor.PostProcessor;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.AbstractScopedTestElement;
import org.apache.jmeter.testelement.property.IntegerProperty;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * 
 */
public class HtmlExtractor extends AbstractScopedTestElement implements PostProcessor, Serializable {

    public static final String EXTRACTOR_JSOUP = "JSOUP"; //$NON-NLS-1$

    public static final String EXTRACTOR_JODD = "JODD"; //$NON-NLS-1$

    public static String[] getImplementations(){
        return new String[]{EXTRACTOR_JSOUP,EXTRACTOR_JODD};
    }

    public static final String DEFAULT_EXTRACTOR = ""; // $NON-NLS-1$

    /**
     * 
     */
    private static final long serialVersionUID = 3978073849365558131L;

    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final String EXPRESSION = "HtmlExtractor.expr"; // $NON-NLS-1$

    private static final String ATTRIBUTE = "HtmlExtractor.attribute"; // $NON-NLS-1$

    private static final String REFNAME = "HtmlExtractor.refname"; // $NON-NLS-1$

    private static final String MATCH_NUMBER = "HtmlExtractor.match_number"; // $NON-NLS-1$

    private static final String DEFAULT = "HtmlExtractor.default"; // $NON-NLS-1$

    private static final String EXTRACTOR_IMPL = "HtmlExtractor.extractor_impl"; // $NON-NLS-1$

    private static final String REF_MATCH_NR = "_matchNr"; // $NON-NLS-1$
    
    private static final String UNDERSCORE = "_";  // $NON-NLS-1$
    
    private Extractor extractor;

    /**
     * Parses the response data using CSS/JQuery expressions and saving the results
     * into variables for use later in the test.
     *
     * @see org.apache.jmeter.processor.PostProcessor#process()
     */
    @Override
    public void process() {
        JMeterContext context = getThreadContext();
        SampleResult previousResult = context.getPreviousResult();
        if (previousResult == null) {
            return;
        }
        log.debug("HtmlExtractor processing result");

        // Fetch some variables
        JMeterVariables vars = context.getVariables();
        
        String refName = getRefName();
        String expression = getExpression();
        String attribute = getAttribute();
        int matchNumber = getMatchNumber();
        final String defaultValue = getDefaultValue();
        
        if (defaultValue.length() > 0){// Only replace default if it is provided
            vars.put(refName, defaultValue);
        }
        
        try {            
            List<String> matches = 
                    extractMatchingStrings(vars, expression, attribute, matchNumber, previousResult);
            int prevCount = 0;
            String prevString = vars.get(refName + REF_MATCH_NR);
            if (prevString != null) {
                vars.remove(refName + REF_MATCH_NR);// ensure old value is not left defined
                try {
                    prevCount = Integer.parseInt(prevString);
                } catch (NumberFormatException e1) {
                    log.warn("Could not parse "+prevString+" "+e1);
                }
            }
            int matchCount=0;// Number of refName_n variable sets to keep
            try {
                String match;
                if (matchNumber >= 0) {// Original match behaviour
                    match = getCorrectMatch(matches, matchNumber);
                    if (match != null) {
                        vars.put(refName, match);
                    } 
                } else // < 0 means we save all the matches
                {
                    matchCount = matches.size();
                    vars.put(refName + REF_MATCH_NR, Integer.toString(matchCount));// Save the count
                    for (int i = 1; i <= matchCount; i++) {
                        match = getCorrectMatch(matches, i);
                        if (match != null) {
                            final String refName_n = new StringBuilder(refName).append(UNDERSCORE).append(i).toString();
                            vars.put(refName_n, match);
                        }
                    }
                }
                // Remove any left-over variables
                for (int i = matchCount + 1; i <= prevCount; i++) {
                    final String refName_n = new StringBuilder(refName).append(UNDERSCORE).append(i).toString();
                    vars.remove(refName_n);
                }
            } catch (RuntimeException e) {
                log.warn("Error while generating result");
            }

        } catch (RuntimeException e) {
            log.warn("Error while generating result");
        }

    }

    /**
     * Grab the appropriate result from the list.
     *
     * @param matches
     *            list of matches
     * @param entry
     *            the entry number in the list
     * @return MatchResult
     */
    private String getCorrectMatch(List<String> matches, int entry) {
        int matchSize = matches.size();

        if (matchSize <= 0 || entry > matchSize){
            return null;
        }

        if (entry == 0) // Random match
        {
            return matches.get(JMeterUtils.getRandomInt(matchSize));
        }

        return matches.get(entry - 1);
    }

    private List<String> extractMatchingStrings(JMeterVariables vars,
            String expression, String attribute, int matchNumber,
            SampleResult previousResult) {
        int found = 0;
        List<String> result = new ArrayList<String>();
        if (isScopeVariable()){
            String inputString=vars.get(getVariableName());
            getExtractorImpl().extract(expression, attribute, matchNumber, inputString, result, found, "-1");
        } else {
            List<SampleResult> sampleList = getSampleList(previousResult);
            int i=0;
            for (SampleResult sr : sampleList) {
                String inputString = sr.getResponseDataAsString();
                found = getExtractorImpl().extract(expression, attribute, matchNumber, inputString, result, found,
                        i>0 ? null : Integer.toString(i));
                i++;
                if (matchNumber > 0 && found == matchNumber){// no need to process further
                    break;
                }
            }
        }
        return result;
    }
    
    /**
     * 
     * @return Extractor
     */
    private Extractor getExtractorImpl() {
        if (extractor == null) {
            boolean useDefaultExtractor = DEFAULT_EXTRACTOR.equals(getExtractor());
            if (useDefaultExtractor || EXTRACTOR_JSOUP.equals(getExtractor())) {
                extractor = new JSoupExtractor();
            } else if (EXTRACTOR_JODD.equals(getExtractor())) {
                extractor = new JSoupExtractor();
            } else {
                throw new IllegalArgumentException("Extractor implementation:"+ getExtractor()+" is unknown");
            }
        }
        return extractor;
    }
    

    public void setExtractor(String attribute) {
        setProperty(EXTRACTOR_IMPL, attribute);
    }

    public String getExtractor() {
        return getPropertyAsString(EXTRACTOR_IMPL); // $NON-NLS-1$
    }

    
    public void setAttribute(String attribute) {
        setProperty(ATTRIBUTE, attribute);
    }

    public String getAttribute() {
        return getPropertyAsString(ATTRIBUTE, ""); // $NON-NLS-1$
    }

    public void setExpression(String regex) {
        setProperty(EXPRESSION, regex);
    }

    public String getExpression() {
        return getPropertyAsString(EXPRESSION);
    }

    public void setRefName(String refName) {
        setProperty(REFNAME, refName);
    }

    public String getRefName() {
        return getPropertyAsString(REFNAME);
    }

    /**
     * Set which Match to use. This can be any positive number, indicating the
     * exact match to use, or 0, which is interpreted as meaning random.
     *
     * @param matchNumber
     */
    public void setMatchNumber(int matchNumber) {
        setProperty(new IntegerProperty(MATCH_NUMBER, matchNumber));
    }

    public void setMatchNumber(String matchNumber) {
        setProperty(MATCH_NUMBER, matchNumber);
    }

    public int getMatchNumber() {
        return getPropertyAsInt(MATCH_NUMBER);
    }

    public String getMatchNumberAsString() {
        return getPropertyAsString(MATCH_NUMBER);
    }

    /**
     * Sets the value of the variable if no matches are found
     *
     * @param defaultValue
     */
    public void setDefaultValue(String defaultValue) {
        setProperty(DEFAULT, defaultValue);
    }

    public String getDefaultValue() {
        return getPropertyAsString(DEFAULT);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8828.java