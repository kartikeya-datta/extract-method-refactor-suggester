error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15958.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15958.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15958.java
text:
```scala
private final P@@attern xmlHeader = Pattern.compile("^(\\<\\?xml[^"+LINE_ENDING+"]+?)"+LINE_ENDING+"(.*)$", Pattern.DOTALL | Pattern.MULTILINE);

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.util.license;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;

import org.apache.wicket.util.diff.Diff;
import org.apache.wicket.util.diff.Revision;
import org.apache.wicket.util.string.Strings;

class XmlLicenseHeaderHandler extends AbstractLicenseHeaderHandler
{
	private Pattern xmlHeader = Pattern.compile("^(\\<\\?xml[^"+LINE_ENDING+"]+?)"+LINE_ENDING+"(.*)$", Pattern.DOTALL | Pattern.MULTILINE);
	
	/**
	 * Construct.
	 * 
	 * @param ignoreFiles
	 */
	public XmlLicenseHeaderHandler(String[] ignoreFiles)
	{
		super(ignoreFiles);
	}

	protected String getLicenseHeaderFilename()
	{
		return "xmlLicense.txt";
	}

	public boolean checkLicenseHeader(File file)
	{
		Revision revision = null;

		try
		{
			String header = extractLicenseHeader(file, 0, 17);

			if (header.startsWith("<?xml"))
			{
				header = header.substring(header.indexOf(LINE_ENDING) + LINE_ENDING.length());
			}
			else
			{
				// Then only take the first 16 lines
				String[] headers = header.split(LINE_ENDING);
				header = "";
				for (int i = 0; i < 16 && i < headers.length; i++)
				{
					if (header.length() > 0)
					{
						header += LINE_ENDING;
					}
					header += headers[i];
				}
			}

			revision = Diff.diff(getLicenseHeader().split(LINE_ENDING), header.split(LINE_ENDING));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

		return revision.size() == 0;
	}

	public String[] getSuffixes()
	{
		return new String[] { "xml", "fml" };
	}

	public boolean addLicenseHeader(File file)
	{
		boolean added = false;
		
		try
		{
			String content = new org.apache.wicket.util.file.File(file).readString();
			String xml = "";
			StringBuffer newContent = new StringBuffer();
			
			Matcher mat = xmlHeader.matcher(content);
			if (mat.matches())
			{
				xml = mat.group(1);
				content = mat.group(2);
			}
			
			if (Strings.isEmpty(xml) == false)
			{
				newContent.append(xml).append(LINE_ENDING);
			}
			
			newContent.append(getLicenseHeader()).append(LINE_ENDING);
			newContent.append(content);
			
			new org.apache.wicket.util.file.File(file).write(newContent.toString());
			added = true;
		}
		catch (Exception e)
		{
			Assert.fail(e.getMessage());
		}
		
		return added;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15958.java