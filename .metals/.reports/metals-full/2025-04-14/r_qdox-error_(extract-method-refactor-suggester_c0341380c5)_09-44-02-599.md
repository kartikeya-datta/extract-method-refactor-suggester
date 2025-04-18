error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14473.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14473.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14473.java
text:
```scala
S@@tringBuilder failString = new StringBuilder();

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
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.TestCase;

import org.apache.wicket.util.lang.Generics;
import org.apache.wicket.util.string.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Testcase used in the different wicket projects for testing for the correct ASL license headers.
 * Doesn't really make sense outside org.apache.wicket.
 * 
 * @author Frank Bille Jensen (frankbille)
 */
public abstract class ApacheLicenseHeaderTestCase extends TestCase
{
	/** Log. */
	private static final Logger log = LoggerFactory.getLogger(ApacheLicenseHeaderTestCase.class);

	private static final String LINE_ENDING = System.getProperty("line.separator");

	static interface FileVisitor
	{
		/**
		 * @param file
		 */
		void visitFile(File file);
	}

	private class SuffixAndIgnoreFileFilter implements FileFilter
	{
		private final List<String> suffixes;
		private final List<String> ignoreFiles;

		private SuffixAndIgnoreFileFilter(List<String> suffixes)
		{
			this(suffixes, null);
		}

		private SuffixAndIgnoreFileFilter(List<String> suffixes, List<String> ignoreFiles)
		{
			this.suffixes = suffixes;
			this.ignoreFiles = ignoreFiles;
		}

		public boolean accept(File pathname)
		{
			boolean accept = false;

			if (pathname.isFile())
			{
				if (ignoreFile(pathname) == false)
				{
					for (int i = 0; i < suffixes.size(); i++)
					{
						String suffix = suffixes.get(i);

						if (pathname.getName().endsWith("." + suffix))
						{
							accept = true;
							break;
						}
						else
						{
							log.info("File ignored: " + pathname.toString());
						}
					}
				}
				else
				{
					log.info("File ignored: " + pathname.toString());
				}
			}

			return accept;
		}

		private boolean ignoreFile(File pathname)
		{
			boolean ignore = false;

			if (ignoreFiles != null)
			{
				String relativePathname = pathname.getAbsolutePath();
				relativePathname = Strings.replaceAll(relativePathname,
					baseDirectory.getAbsolutePath() + System.getProperty("file.separator"), "")
					.toString();

				for (int i = 0; i < ignoreFiles.size(); i++)
				{
					String ignorePath = ignoreFiles.get(i);
					// Will convert '/'s to '\\'s on Windows
					ignorePath = Strings.replaceAll(ignorePath, "/",
						System.getProperty("file.separator")).toString();
					File ignoreFile = new File(baseDirectory, ignorePath);

					// Directory ignore
					if (ignoreFile.isDirectory())
					{
						if (pathname.getAbsolutePath().startsWith(ignoreFile.getAbsolutePath()))
						{
							ignore = true;
							break;
						}
					}
					// Absolute file
					else if (ignoreFile.isFile())
					{
						if (relativePathname.equals(ignorePath))
						{
							ignore = true;
							break;
						}
					}
					else if (pathname.getName().equals(ignorePath))
					{
						ignore = true;
						break;
					}
				}
			}

			return ignore;
		}
	}

	private class DirectoryFileFilter implements FileFilter
	{
		private final String[] ignoreDirectory = new String[] { ".svn" };

		public boolean accept(File pathname)
		{
			boolean accept = false;

			if (pathname.isDirectory())
			{
				String relativePathname = pathname.getAbsolutePath();
				relativePathname = Strings.replaceAll(relativePathname,
					baseDirectory.getAbsolutePath() + System.getProperty("file.separator"), "")
					.toString();
				if (relativePathname.equals("target") == false)
				{
					boolean found = false;
					for (int i = 0; i < ignoreDirectory.length; i++)
					{
						String ignore = ignoreDirectory[i];
						if (pathname.getName().equals(ignore))
						{
							found = true;
							break;
						}
					}
					if (found == false)
					{
						accept = true;
					}
				}
			}

			return accept;
		}
	}

	private ILicenseHeaderHandler[] licenseHeaderHandlers;

	private File baseDirectory = new File("").getAbsoluteFile();

	protected List<String> javaIgnore = Generics.newArrayList();
	protected List<String> htmlIgnore = Generics.newArrayList();
	protected List<String> xmlPrologIgnore = Generics.newArrayList();
	protected List<String> propertiesIgnore = Generics.newArrayList();
	protected List<String> xmlIgnore = Generics.newArrayList();
	protected List<String> cssIgnore = Generics.newArrayList();
	protected List<String> velocityIgnore = Generics.newArrayList();
	protected List<String> javaScriptIgnore = Generics.newArrayList();
	protected boolean addHeaders = false;

	/**
	 * Construct.
	 */
	public ApacheLicenseHeaderTestCase()
	{
		super("Test of the legal aspects of the Wicket source code is correct.");

		// -------------------------------
		// Configure defaults
		// -------------------------------

		// addHeaders = true;
		xmlIgnore.add(".settings");
		xmlIgnore.add("EclipseCodeFormat.xml");

		/*
		 * License header in test files lower the visibility of the test.
		 */
		htmlIgnore.add("src/test/java");

		/*
		 * Low level configuration files for logging. No license needed.
		 */
		propertiesIgnore.add("src/test/java");

		/*
		 * .html in test is very test specific and a license header would confuse and make it
		 * unclear what the test is about.
		 */
		xmlPrologIgnore.add("src/test/java");

		/*
		 * Ignore package.html
		 */
		xmlPrologIgnore.add("package.html");
	}

	/**
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	public final void setUp()
	{
		// setup the base directory for when running inside maven (building a release
		// comes to mind).
		String property = System.getProperty("basedir");
		if (!Strings.isEmpty(property))
		{
			baseDirectory = new File(property).getAbsoluteFile();
		}
	}

	/**
	 * Test all the files in the project which has an associated {@link ILicenseHeaderHandler}.
	 */
	public void testLicenseHeaders()
	{
		licenseHeaderHandlers = new ILicenseHeaderHandler[] {
				new JavaLicenseHeaderHandler(javaIgnore),
				new JavaScriptLicenseHeaderHandler(javaScriptIgnore),
				new XmlLicenseHeaderHandler(xmlIgnore),
				new PropertiesLicenseHeaderHandler(propertiesIgnore),
				new HtmlLicenseHeaderHandler(htmlIgnore),
				new VelocityLicenseHeaderHandler(velocityIgnore),
				new XmlPrologHeaderHandler(xmlPrologIgnore),
				new CssLicenseHeaderHandler(cssIgnore), };

		final Map<ILicenseHeaderHandler, List<File>> badFiles = new HashMap<ILicenseHeaderHandler, List<File>>();

		for (final ILicenseHeaderHandler licenseHeaderHandler : licenseHeaderHandlers)
		{
			visitFiles(licenseHeaderHandler.getSuffixes(), licenseHeaderHandler.getIgnoreFiles(),
				new FileVisitor()
				{
					public void visitFile(File file)
					{
						if (licenseHeaderHandler.checkLicenseHeader(file) == false)
						{
							if (addHeaders == false ||
								licenseHeaderHandler.addLicenseHeader(file) == false)
							{
								List<File> files = badFiles.get(licenseHeaderHandler);

								if (files == null)
								{
									files = new ArrayList<File>();
									badFiles.put(licenseHeaderHandler, files);
								}

								files.add(file);
							}
						}
					}
				});
		}

		failIncorrectLicenceHeaders(badFiles);
	}

	private void failIncorrectLicenceHeaders(Map<ILicenseHeaderHandler, List<File>> files)
	{
		if (files.size() > 0)
		{
			StringBuffer failString = new StringBuffer();

			for (Entry<ILicenseHeaderHandler, List<File>> entry : files.entrySet())
			{
				ILicenseHeaderHandler licenseHeaderHandler = entry.getKey();
				List<File> fileList = entry.getValue();

				failString.append("\n");
				failString.append(licenseHeaderHandler.getClass().getName());
				failString.append(" failed. The following files(");
				failString.append(fileList.size());
				failString.append(") didn't have correct license header:\n");

				for (File file : fileList)
				{
					String filename = file.getAbsolutePath();

					// Find the license type
					String licenseType = licenseHeaderHandler.getLicenseType(file);

					if (licenseType == null)
					{
						failString.append("NONE");
					}
					else
					{
						failString.append(licenseType);
					}

					failString.append(" ").append(filename).append(LINE_ENDING);
				}
			}

			System.out.println(failString);
			fail(failString.toString());
		}
	}

	private void visitFiles(List<String> suffixes, List<String> ignoreFiles, FileVisitor fileVisitor)
	{
		visitDirectory(suffixes, ignoreFiles, baseDirectory, fileVisitor);
	}

	private void visitDirectory(List<String> suffixes, List<String> ignoreFiles, File directory,
		FileVisitor fileVisitor)
	{
		File[] files = directory.listFiles(new SuffixAndIgnoreFileFilter(suffixes, ignoreFiles));

		if (files != null)
		{
			for (int i = 0; i < files.length; i++)
			{
				File file = files[i];
				fileVisitor.visitFile(file);
			}
		}

		// Find the directories in this directory on traverse deeper
		files = directory.listFiles(new DirectoryFileFilter());

		if (files != null)
		{
			for (int i = 0; i < files.length; i++)
			{
				File childDirectory = files[i];
				visitDirectory(suffixes, ignoreFiles, childDirectory, fileVisitor);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14473.java