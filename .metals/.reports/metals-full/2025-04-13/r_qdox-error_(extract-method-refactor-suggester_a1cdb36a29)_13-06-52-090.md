error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10341.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10341.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10341.java
text:
```scala
a@@ssertEquals("http://server.com/index.html?session_id=jfdkjdkf+jddkfdfjkdjfdf%22", sampler.toString());

package org.apache.jmeter.protocol.http.modifier;
import java.io.Serializable;

import junit.framework.TestCase;

import org.apache.jmeter.config.Argument;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.processor.PreProcessor;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.protocol.http.util.HTTPArgument;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * @author mstover
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class URLRewritingModifier extends AbstractTestElement implements Serializable, PreProcessor
{
    transient private static Logger log = Hierarchy.getDefaultHierarchy().getLoggerFor("jmeter.protocol.http");
    private Pattern case1, case2, case3, case4;
    transient Perl5Compiler compiler = new Perl5Compiler();
    private final static String ARGUMENT_NAME = "argument_name";
    private final static String PATH_EXTENSION = "path_extension";
    private final static String PATH_EXTENSION_NO_EQUALS = "path_extension_no_equals";

    public void process()
    {
        Sampler sampler = JMeterContextService.getContext().getCurrentSampler();
        SampleResult responseText = JMeterContextService.getContext().getPreviousResult();
        initRegex(getArgumentName());
        String text = new String(responseText.getResponseData());
        Perl5Matcher matcher = JMeterUtils.getMatcher();
        String value = "";
        if (matcher.contains(text, case1))
        {
            MatchResult result = matcher.getMatch();
            value = result.group(1);
        }
        else if (matcher.contains(text, case2))
        {
            MatchResult result = matcher.getMatch();
            value = result.group(1);
        }
        else if (matcher.contains(text, case3))
        {
            MatchResult result = matcher.getMatch();
            value = result.group(1);
        }
        else if (matcher.contains(text, case4))
        {
            MatchResult result = matcher.getMatch();
            value = result.group(1);
        }

        modify((HTTPSampler) sampler, value);
    }
    private void modify(HTTPSampler sampler, String value)
    {
        if (isPathExtension())
        {
            if (isPathExtensionNoEquals())
            {
                sampler.setPath(sampler.getPath() + ";" + getArgumentName() + value);
            }
            else
            {
                sampler.setPath(sampler.getPath() + ";" + getArgumentName() + "=" + value);
            }
        }
        else
        {
            sampler.getArguments().removeArgument(getArgumentName());
            sampler.getArguments().addArgument(new HTTPArgument(getArgumentName(), value, true));
        }
    }
    public void setArgumentName(String argName)
    {
        setProperty(ARGUMENT_NAME, argName);
    }
    private void initRegex(String argName)
    {
        case1 = JMeterUtils.getPatternCache().getPattern(argName + "=([^\"'>& \n\r;]*)[& \\n\\r\"'>;]?$?", Perl5Compiler.MULTILINE_MASK);
        case2 =
            JMeterUtils.getPatternCache().getPattern(
                "[Nn][Aa][Mm][Ee]=\"" + argName + "\"[^>]+[vV][Aa][Ll][Uu][Ee]=\"([^\"]*)\"",
                Perl5Compiler.MULTILINE_MASK);
        case3 =
            JMeterUtils.getPatternCache().getPattern(
                "[vV][Aa][Ll][Uu][Ee]=\"([^\"]*)\"[^>]+[Nn][Aa][Mm][Ee]=\"" + argName + "\"",
                Perl5Compiler.MULTILINE_MASK);
        // case1 could be re-written "=?([^..."  instead of creating a new pattern?
        case4 = JMeterUtils.getPatternCache().getPattern(argName + "([^\"'>& \n\r]*)[& \\n\\r\"'>]?$?", Perl5Compiler.MULTILINE_MASK);
    }
    public String getArgumentName()
    {
        return getPropertyAsString(ARGUMENT_NAME);
    }
    public void setPathExtension(boolean pathExt)
    {
        setProperty(new BooleanProperty(PATH_EXTENSION, pathExt));
    }
    public void setPathExtensionNoEquals(boolean pathExtNoEquals)
    {
        setProperty(new BooleanProperty(PATH_EXTENSION_NO_EQUALS, pathExtNoEquals));
    }
    public boolean isPathExtension()
    {
        return getPropertyAsBoolean(PATH_EXTENSION);
    }
    public boolean isPathExtensionNoEquals()
    {
        return getPropertyAsBoolean(PATH_EXTENSION_NO_EQUALS);
    }
    public static class Test extends TestCase
    {
        SampleResult response;
        JMeterContext context;
        public Test(String name)
        {
            super(name);
        }
        public void setUp()
        {
            context = JMeterContextService.getContext();
        }
        public void testGrabSessionId() throws Exception
        {
            String html = "location: http://server.com/index.html?session_id=jfdkjdkf%20jddkfdfjkdjfdf%22;";
            response = new SampleResult();
            response.setResponseData(html.getBytes());
            URLRewritingModifier mod = new URLRewritingModifier();
            mod.setArgumentName("session_id");
            HTTPSampler sampler = createSampler();
            sampler.addArgument("session_id", "adfasdfdsafasdfasd");
            context.setCurrentSampler(sampler);
            context.setPreviousResult(response);
            mod.process();
            Arguments args = sampler.getArguments();
            assertEquals("jfdkjdkf jddkfdfjkdjfdf\"", ((Argument) args.getArguments().get(0).getObjectValue()).getValue());
            assertEquals("http://server.com:80/index.html?session_id=jfdkjdkf+jddkfdfjkdjfdf%22", sampler.toString());
        }
        public void testGrabSessionId2() throws Exception
        {
            String html = "<a href=\"http://server.com/index.html?session_id=jfdkjdkfjddkfdfjkdjfdf\">";
            response = new SampleResult();
            response.setResponseData(html.getBytes());
            URLRewritingModifier mod = new URLRewritingModifier();
            mod.setArgumentName("session_id");
            HTTPSampler sampler = createSampler();
            context.setCurrentSampler(sampler);
            context.setPreviousResult(response);
            mod.process();
            Arguments args = sampler.getArguments();
            assertEquals("jfdkjdkfjddkfdfjkdjfdf", ((Argument) args.getArguments().get(0).getObjectValue()).getValue());
        }
        private HTTPSampler createSampler()
        {
            HTTPSampler sampler = new HTTPSampler();
            sampler.setDomain("server.com");
            sampler.setPath("index.html");
            sampler.setMethod(HTTPSampler.GET);
            sampler.setProtocol("http");
            return sampler;
        }

        public void testGrabSessionId3() throws Exception
        {
            String html = "href='index.html?session_id=jfdkjdkfjddkfdfjkdjfdf'";
            response = new SampleResult();
            response.setResponseData(html.getBytes());
            URLRewritingModifier mod = new URLRewritingModifier();
            mod.setArgumentName("session_id");
            HTTPSampler sampler = createSampler();
            context.setCurrentSampler(sampler);
            context.setPreviousResult(response);
            mod.process();
            Arguments args = sampler.getArguments();
            assertEquals("jfdkjdkfjddkfdfjkdjfdf", ((Argument) args.getArguments().get(0).getObjectValue()).getValue());
        }

        public void testGrabSessionId4() throws Exception
        {
            String html = "href='index.html;%24sid%24KQNq3AAADQZoEQAxlkX8uQV5bjqVBPbT'";
            response = new SampleResult();
            response.setResponseData(html.getBytes());
            URLRewritingModifier mod = new URLRewritingModifier();
            mod.setArgumentName("%24sid%24");
            mod.setPathExtension(true);
            mod.setPathExtensionNoEquals(true);
            HTTPSampler sampler = createSampler();
            context.setCurrentSampler(sampler);
            context.setPreviousResult(response);
            mod.process();
            Arguments args = sampler.getArguments();
            assertEquals("index.html;%24sid%24KQNq3AAADQZoEQAxlkX8uQV5bjqVBPbT", sampler.getPath());
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10341.java