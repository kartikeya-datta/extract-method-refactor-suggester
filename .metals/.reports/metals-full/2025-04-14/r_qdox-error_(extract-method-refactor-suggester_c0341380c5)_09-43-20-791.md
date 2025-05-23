error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6686.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6686.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6686.java
text:
```scala
i@@nt index = responseText.indexOf('<'); // $NON-NLS-1$

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

package org.apache.jmeter.protocol.http.modifier;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.jmeter.config.Argument;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.ConfigElement;
import org.apache.jmeter.processor.PreProcessor;
import org.apache.jmeter.protocol.http.parser.HtmlParsingUtils;
import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.protocol.http.util.ConversionUtils;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// For Unit tests, @see TestAnchorModifier

public class AnchorModifier extends AbstractTestElement implements PreProcessor, Serializable {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final long serialVersionUID = 240L;

    private static final Random rand = new Random();

    public AnchorModifier() {
    }

    /**
     * Modifies an Entry object based on HTML response text.
     */
    public void process() {
        JMeterContext context = getThreadContext();
        Sampler sam = context.getCurrentSampler();
        SampleResult res = context.getPreviousResult();
        HTTPSamplerBase sampler = null;
        HTTPSampleResult result = null;
        if (res == null || !(sam instanceof HTTPSamplerBase) || !(res instanceof HTTPSampleResult)) {
            log.info("Can't apply HTML Link Parser when the previous" + " sampler run is not an HTTP Request.");
            return;
        } else {
            sampler = (HTTPSamplerBase) sam;
            result = (HTTPSampleResult) res;
        }
        List<HTTPSamplerBase> potentialLinks = new ArrayList<HTTPSamplerBase>();
        String responseText = ""; // $NON-NLS-1$
        responseText = result.getResponseDataAsString();
        Document html;
        int index = responseText.indexOf("<"); // $NON-NLS-1$
        if (index == -1) {
            index = 0;
        }
        if (log.isDebugEnabled()) {
            log.debug("Check for matches against: "+sampler.toString());
        }
        html = (Document) HtmlParsingUtils.getDOM(responseText.substring(index));
        addAnchorUrls(html, result, sampler, potentialLinks);
        addFormUrls(html, result, sampler, potentialLinks);
        addFramesetUrls(html, result, sampler, potentialLinks);
        if (potentialLinks.size() > 0) {
            HTTPSamplerBase url = potentialLinks.get(rand.nextInt(potentialLinks.size()));
            if (log.isDebugEnabled()) {
                log.debug("Selected: "+url.toString());
            }
            sampler.setDomain(url.getDomain());
            sampler.setPath(url.getPath());
            if (url.getMethod().equals(HTTPConstants.POST)) {
                PropertyIterator iter = sampler.getArguments().iterator();
                while (iter.hasNext()) {
                    Argument arg = (Argument) iter.next().getObjectValue();
                    modifyArgument(arg, url.getArguments());
                }
            } else {
                sampler.setArguments(url.getArguments());
                // config.parseArguments(url.getQueryString());
            }
            sampler.setProtocol(url.getProtocol());
            return;
        } else {
            log.debug("No matches found");
        }
        return;
    }

    private void modifyArgument(Argument arg, Arguments args) {
        if (log.isDebugEnabled()) {
            log.debug("Modifying argument: " + arg);
        }
        List<Argument> possibleReplacements = new ArrayList<Argument>();
        PropertyIterator iter = args.iterator();
        Argument replacementArg;
        while (iter.hasNext()) {
            replacementArg = (Argument) iter.next().getObjectValue();
            try {
                if (HtmlParsingUtils.isArgumentMatched(replacementArg, arg)) {
                    possibleReplacements.add(replacementArg);
                }
            } catch (Exception ex) {
                log.error("Problem adding Argument", ex);
            }
        }

        if (possibleReplacements.size() > 0) {
            replacementArg = possibleReplacements.get(rand.nextInt(possibleReplacements.size()));
            arg.setName(replacementArg.getName());
            arg.setValue(replacementArg.getValue());
            if (log.isDebugEnabled()) {
                log.debug("Just set argument to values: " + arg.getName() + " = " + arg.getValue());
            }
            args.removeArgument(replacementArg);
        }
    }

    public void addConfigElement(ConfigElement config) {
    }

    private void addFormUrls(Document html, HTTPSampleResult result, HTTPSamplerBase config, 
            List<HTTPSamplerBase> potentialLinks) {
        NodeList rootList = html.getChildNodes();
        List<HTTPSamplerBase> urls = new LinkedList<HTTPSamplerBase>();
        for (int x = 0; x < rootList.getLength(); x++) {
            urls.addAll(HtmlParsingUtils.createURLFromForm(rootList.item(x), result.getURL()));
        }
        for (HTTPSamplerBase newUrl : urls) {
            newUrl.setMethod(HTTPConstants.POST);
            if (log.isDebugEnabled()) {
                log.debug("Potential Form match: " + newUrl.toString());
            }
            if (HtmlParsingUtils.isAnchorMatched(newUrl, config)) {
                log.debug("Matched!");
                potentialLinks.add(newUrl);
            }
        }
    }

    private void addAnchorUrls(Document html, HTTPSampleResult result, HTTPSamplerBase config, 
            List<HTTPSamplerBase> potentialLinks) {
        String base = "";
        NodeList baseList = html.getElementsByTagName("base"); // $NON-NLS-1$
        if (baseList.getLength() > 0) {
            base = baseList.item(0).getAttributes().getNamedItem("href").getNodeValue(); // $NON-NLS-1$
        }
        NodeList nodeList = html.getElementsByTagName("a"); // $NON-NLS-1$
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node tempNode = nodeList.item(i);
            NamedNodeMap nnm = tempNode.getAttributes();
            Node namedItem = nnm.getNamedItem("href"); // $NON-NLS-1$
            if (namedItem == null) {
                continue;
            }
            String hrefStr = namedItem.getNodeValue();
            if (hrefStr.startsWith("javascript:")) { // $NON-NLS-1$
                continue; // No point trying these
            }
            try {
                HTTPSamplerBase newUrl = HtmlParsingUtils.createUrlFromAnchor(hrefStr, ConversionUtils.makeRelativeURL(result.getURL(), base));
                newUrl.setMethod(HTTPConstants.GET);
                if (log.isDebugEnabled()) {
                    log.debug("Potential <a href> match: " + newUrl);
                }
                if (HtmlParsingUtils.isAnchorMatched(newUrl, config)) {
                    log.debug("Matched!");
                    potentialLinks.add(newUrl);
                }
            } catch (MalformedURLException e) {
                log.warn("Bad URL "+e);
            }
        }
    }

    private void addFramesetUrls(Document html, HTTPSampleResult result,
       HTTPSamplerBase config, List<HTTPSamplerBase> potentialLinks) {
       String base = "";
       NodeList baseList = html.getElementsByTagName("base"); // $NON-NLS-1$
       if (baseList.getLength() > 0) {
           base = baseList.item(0).getAttributes().getNamedItem("href") // $NON-NLS-1$
                   .getNodeValue();
       }
       NodeList nodeList = html.getElementsByTagName("frame"); // $NON-NLS-1$
       for (int i = 0; i < nodeList.getLength(); i++) {
           Node tempNode = nodeList.item(i);
           NamedNodeMap nnm = tempNode.getAttributes();
           Node namedItem = nnm.getNamedItem("src"); // $NON-NLS-1$
           if (namedItem == null) {
               continue;
           }
           String hrefStr = namedItem.getNodeValue();
           try {
               HTTPSamplerBase newUrl = HtmlParsingUtils.createUrlFromAnchor(
                       hrefStr, ConversionUtils.makeRelativeURL(result.getURL(), base));
               newUrl.setMethod(HTTPConstants.GET);
               if (log.isDebugEnabled()) {
                   log.debug("Potential <frame src> match: " + newUrl);
               }
               if (HtmlParsingUtils.isAnchorMatched(newUrl, config)) {
                   log.debug("Matched!");
                   potentialLinks.add(newUrl);
               }
           } catch (MalformedURLException e) {
               log.warn("Bad URL "+e);
           }
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6686.java