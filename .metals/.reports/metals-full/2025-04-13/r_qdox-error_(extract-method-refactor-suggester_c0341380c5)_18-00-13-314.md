error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1865.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1865.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1865.java
text:
```scala
s@@ampler.getArguments().addArgument(new HTTPArgument(getArgumentName(), value, false));

/*
 * Copyright 2003-2004 The Apache Software Foundation.
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

package org.apache.jmeter.protocol.http.modifier;

import java.io.Serializable;

import org.apache.jmeter.processor.PreProcessor;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.protocol.http.util.HTTPArgument;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

//For unit tests, @see TestURLRewritingModifier

/**
 * @author mstover
 * @author <a href="mailto:jsalvata@apache.org">Jordi Salvat i Alabart</a>
 * @version $Revision$ updated on $Date$
 */

public class URLRewritingModifier extends AbstractTestElement implements Serializable, PreProcessor {

	private Pattern pathExtensionEqualsQuestionmarkRegexp;

	private Pattern pathExtensionEqualsNoQuestionmarkRegexp;

	private Pattern parameterRegexp;

	private Pattern pathExtensionNoEqualsQuestionmarkRegexp;

	private Pattern pathExtensionNoEqualsNoQuestionmarkRegexp;

	// transient Perl5Compiler compiler = new Perl5Compiler();
	private final static String ARGUMENT_NAME = "argument_name";

	private final static String PATH_EXTENSION = "path_extension";

	private final static String PATH_EXTENSION_NO_EQUALS = "path_extension_no_equals";

	private final static String PATH_EXTENSION_NO_QUESTIONMARK = "path_extension_no_questionmark";

	public void process() {
		JMeterContext ctx = getThreadContext();
		Sampler sampler = ctx.getCurrentSampler();
		SampleResult responseText = ctx.getPreviousResult();
		if (responseText == null) {
			return;
		}
		initRegex(getArgumentName());
		String text = new String(responseText.getResponseData());
		Perl5Matcher matcher = JMeterUtils.getMatcher();
		String value = "";
		if (isPathExtension() && isPathExtensionNoEquals() && isPathExtensionNoQuestionmark()) {
			if (matcher.contains(text, pathExtensionNoEqualsNoQuestionmarkRegexp)) {
				MatchResult result = matcher.getMatch();
				value = result.group(1);
			}
		} else if (isPathExtension() && isPathExtensionNoEquals()) // && !
																	// isPathExtensionNoQuestionmark
		{
			if (matcher.contains(text, pathExtensionNoEqualsQuestionmarkRegexp)) {
				MatchResult result = matcher.getMatch();
				value = result.group(1);
			}
		} else if (isPathExtension() && isPathExtensionNoQuestionmark()) // && !
																			// isPathExtensionNoEquals
		{
			if (matcher.contains(text, pathExtensionEqualsNoQuestionmarkRegexp)) {
				MatchResult result = matcher.getMatch();
				value = result.group(1);
			}
		} else if (isPathExtension()) // && ! isPathExtensionNoEquals && !
										// isPathExtensionNoQuestionmark
		{
			if (matcher.contains(text, pathExtensionEqualsQuestionmarkRegexp)) {
				MatchResult result = matcher.getMatch();
				value = result.group(1);
			}
		} else // if ! isPathExtension()
		{
			if (matcher.contains(text, parameterRegexp)) {
				MatchResult result = matcher.getMatch();
				for (int i = 1; i < result.groups(); i++) {
					value = result.group(i);
					if (value != null)
						break;
				}
			}
		}

		modify((HTTPSamplerBase) sampler, value);
	}

	private void modify(HTTPSamplerBase sampler, String value) {
		if (isPathExtension()) {
			if (isPathExtensionNoEquals()) {
				sampler.setPath(sampler.getPath() + ";" + getArgumentName() + value);
			} else {
				sampler.setPath(sampler.getPath() + ";" + getArgumentName() + "=" + value);
			}
		} else {
			sampler.getArguments().removeArgument(getArgumentName());
			sampler.getArguments().addArgument(new HTTPArgument(getArgumentName(), value, true));
		}
	}

	public void setArgumentName(String argName) {
		setProperty(ARGUMENT_NAME, argName);
	}

	private void initRegex(String argName) {
		pathExtensionEqualsQuestionmarkRegexp = JMeterUtils.getPatternCache().getPattern(
				";" + argName + "=([^\"'>&\\s;]*)[&\\s\"'>;]?$?",
				Perl5Compiler.MULTILINE_MASK | Perl5Compiler.READ_ONLY_MASK);

		pathExtensionEqualsNoQuestionmarkRegexp = JMeterUtils.getPatternCache().getPattern(
				";" + argName + "=([^\"'>&\\s;?]*)[&\\s\"'>;?]?$?",
				Perl5Compiler.MULTILINE_MASK | Perl5Compiler.READ_ONLY_MASK);

		pathExtensionNoEqualsQuestionmarkRegexp = JMeterUtils.getPatternCache().getPattern(
				";" + argName + "([^\"'>&\\s;]*)[&\\s\"'>;]?$?",
				Perl5Compiler.MULTILINE_MASK | Perl5Compiler.READ_ONLY_MASK);

		pathExtensionNoEqualsNoQuestionmarkRegexp = JMeterUtils.getPatternCache().getPattern(
				";" + argName + "([^\"'>&\\s;?]*)[&\\s\"'>;?]?$?",
				Perl5Compiler.MULTILINE_MASK | Perl5Compiler.READ_ONLY_MASK);

		parameterRegexp = JMeterUtils.getPatternCache().getPattern(
				"[;\\?&]" + argName + "=([^\"'>&\\s;]*)[&\\s\"'>;]?$?" + "|\\s[Nn][Aa][Mm][Ee]\\s*=\\s*[\"']" + argName
						+ "[\"']" + "[^>]*" + "\\s[vV][Aa][Ll][Uu][Ee]\\s*=\\s*[\"']" + "([^\"']*)" + "[\"']"
						+ "|\\s[vV][Aa][Ll][Uu][Ee]\\s*=\\s*[\"']" + "([^\"']*)" + "[\"']" + "[^>]*"
						+ "\\s[Nn][Aa][Mm][Ee]\\s*=\\s*[\"']" + argName + "[\"']",
				Perl5Compiler.MULTILINE_MASK | Perl5Compiler.READ_ONLY_MASK);
		// NOTE: the handling of simple- vs. double-quotes could be formally
		// more accurate, but I can't imagine a session id containing
		// either, so we should be OK. The whole set of expressions is a
		// quick hack anyway, so who cares.
	}

	public String getArgumentName() {
		return getPropertyAsString(ARGUMENT_NAME);
	}

	public void setPathExtension(boolean pathExt) {
		setProperty(new BooleanProperty(PATH_EXTENSION, pathExt));
	}

	public void setPathExtensionNoEquals(boolean pathExtNoEquals) {
		setProperty(new BooleanProperty(PATH_EXTENSION_NO_EQUALS, pathExtNoEquals));
	}

	public void setPathExtensionNoQuestionmark(boolean pathExtNoQuestionmark) {
		setProperty(new BooleanProperty(PATH_EXTENSION_NO_QUESTIONMARK, pathExtNoQuestionmark));
	}

	public boolean isPathExtension() {
		return getPropertyAsBoolean(PATH_EXTENSION);
	}

	public boolean isPathExtensionNoEquals() {
		return getPropertyAsBoolean(PATH_EXTENSION_NO_EQUALS);
	}

	public boolean isPathExtensionNoQuestionmark() {
		return getPropertyAsBoolean(PATH_EXTENSION_NO_QUESTIONMARK);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1865.java