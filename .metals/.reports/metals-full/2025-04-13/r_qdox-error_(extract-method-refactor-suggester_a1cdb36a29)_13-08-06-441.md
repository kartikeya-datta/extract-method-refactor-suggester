error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9016.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9016.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9016.java
text:
```scala
f@@ailString.append("The following files("+files.size()+") didn't have a correct license header:\n");

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
package wicket.util.license;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import wicket.util.diff.Diff;
import wicket.util.diff.Revision;

/**
 * A silly try to create a testcase for running through all files in the project
 * and check if they have the correct license headers. Lets see if it holds.
 * 
 * @author Frank Bille Jensen (frankbille)
 */
public abstract class ApacheLicenseHeaderTestCase extends TestCase
{
	private static final String LINE_ENDING = System.getProperty("line.separator");

	private interface FileVisitor
	{
		/**
		 * @param file
		 */
		void visitFile(File file);
	}

	private class SuffixAndIgnoreFileFilter implements FileFilter
	{
		private String[] suffixes;
		private String[] ignoreFiles;

		private SuffixAndIgnoreFileFilter(String[] suffixes)
		{
			this(suffixes, null);
		}

		private SuffixAndIgnoreFileFilter(String[] suffixes, String[] ignoreFiles)
		{
			this.suffixes = suffixes;
			this.ignoreFiles = ignoreFiles;
		}

		public boolean accept(File pathname)
		{
			boolean accept = false;

			if (pathname.isFile())
			{
				boolean ignore = false;

				if (ignoreFiles != null)
				{
					String relativePathname = pathname.getAbsolutePath();
					relativePathname = relativePathname.replace(baseDirectory.getAbsolutePath()
							+ System.getProperty("file.separator"), "");

					for (String ignoreFile : ignoreFiles)
					{
						if (relativePathname.equals(ignoreFile))
						{
							ignore = true;
							break;
						}
					}
				}

				if (ignore == false)
				{
					for (String suffix : suffixes)
					{
						if (pathname.getName().endsWith("." + suffix))
						{
							accept = true;
							break;
						}
					}
				}
			}

			return accept;
		}
	}

	private class DirectoryFileFilter implements FileFilter
	{
		private String[] ignoreDirectory = new String[] { ".svn", "target" };

		public boolean accept(File pathname)
		{
			boolean accept = false;

			if (pathname.isDirectory())
			{
				boolean found = false;
				for (String ignore : ignoreDirectory)
				{
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

			return accept;
		}
	}

	private File baseDirectory = new File("").getAbsoluteFile();
	private String javaLicenseHeader;
	private String xmlLicenseHeader;
	private String propertiesLicenseHeader;
	private String cssLicenseHeader;
	private String velocityLicenseHeader;
	private String javaScriptLicenseHeader;
	private String x = "^(\\<\\?xml[^"+LINE_ENDING+"]+).*";
	private Pattern xmlHeader = Pattern.compile(x, Pattern.DOTALL);

	protected String[] javaIgnore;
	protected String[] htmlIgnore;
	protected String[] propertiesIgnore;
	protected String[] xmlIgnore;
	protected String[] cssIgnore;
	protected String[] velocityIgnore;
	protected String[] javaScriptIgnore;

	/**
	 * Construct.
	 */
	public ApacheLicenseHeaderTestCase()
	{
		super("Test of the legal aspects of the Wicket source code is correct.");

		// Load licenses
		javaLicenseHeader = loadFile("javaLicense.txt");
		xmlLicenseHeader = loadFile("xmlLicense.txt");
		propertiesLicenseHeader = loadFile("propertiesLicense.txt");
		cssLicenseHeader = loadFile("cssLicense.txt");
		velocityLicenseHeader = loadFile("velocityLicense.txt");
		javaScriptLicenseHeader = loadFile("javaScriptLicense.txt");
	}

	/**
	 * Test all java files.
	 */
	public void testJavaFiles()
	{
		final List<File> badFiles = new ArrayList<File>();

		visitFiles("java", javaIgnore, new FileVisitor()
		{
			public void visitFile(File file)
			{
				if (checkJavaHeader(file) == false)
				{
					badFiles.add(file);
				}
			}
		});

		failIncorrectLicenceHeaders(badFiles);
	}

	/**
	 * Test all html files.
	 */
	public void testHtmlFiles()
	{
		final List<File> badFiles = new ArrayList<File>();

		visitFiles("html", htmlIgnore, new FileVisitor()
		{
			public void visitFile(File file)
			{
				if (checkXmlHeader(file) == false)
				{
					badFiles.add(file);
				}
			}
		});

		failIncorrectLicenceHeaders(badFiles);
	}

	/**
	 * Test all properties files.
	 */
	public void testPropertiesFiles()
	{
		final List<File> badFiles = new ArrayList<File>();

		visitFiles("properties", propertiesIgnore, new FileVisitor()
		{
			public void visitFile(File file)
			{
				if (checkPropertiesHeader(file) == false)
				{
					badFiles.add(file);
				}
			}
		});

		failIncorrectLicenceHeaders(badFiles);
	}

	/**
	 * Test all xml files.
	 */
	public void testXmlFiles()
	{
		final List<File> badFiles = new ArrayList<File>();

		visitFiles(new String[] { "xml", "fml" }, xmlIgnore, new FileVisitor()
		{
			public void visitFile(File file)
			{
				if (checkXmlHeader(file) == false)
				{
					badFiles.add(file);
				}
			}
		});

		failIncorrectLicenceHeaders(badFiles);
	}

	/**
	 * Test all css files.
	 */
	public void testCssFiles()
	{
		final List<File> badFiles = new ArrayList<File>();

		visitFiles("css", cssIgnore, new FileVisitor()
		{
			public void visitFile(File file)
			{
				if (checkCssHeader(file) == false)
				{
					badFiles.add(file);
				}
			}
		});

		failIncorrectLicenceHeaders(badFiles);
	}

	/**
	 * Test all velocity files.
	 */
	public void testVelocityFiles()
	{
		final List<File> badFiles = new ArrayList<File>();

		visitFiles("vm", velocityIgnore, new FileVisitor()
		{
			public void visitFile(File file)
			{
				if (checkVelocityHeader(file) == false)
				{
					badFiles.add(file);
				}
			}
		});

		failIncorrectLicenceHeaders(badFiles);
	}

	/**
	 * Test all javascript files.
	 */
	public void testJavaScriptFiles()
	{
		final List<File> badFiles = new ArrayList<File>();

		visitFiles("js", javaScriptIgnore, new FileVisitor()
		{
			public void visitFile(File file)
			{
				if (checkJavaScriptHeader(file) == false)
				{
					badFiles.add(file);
				}
			}
		});

		failIncorrectLicenceHeaders(badFiles);
	}

	private void failIncorrectLicenceHeaders(List<File> files)
	{
		if (files.size() > 0)
		{
			StringBuffer failString = new StringBuffer();

			failString.append("The following files didn't have a correct license header:\n");

			for (File file : files)
			{
				failString.append(file.getAbsolutePath()).append(LINE_ENDING);
			}

			fail(failString.toString());
		}
	}

	private String extractLicenseHeader(File file, int start, int length)
	{
		String header = "";
		FileReader fileReader = null;

		try
		{
			fileReader = new FileReader(file);
			LineNumberReader lineNumberReader = new LineNumberReader(fileReader);

			for (int i = start; i < length; i++)
			{
				header += lineNumberReader.readLine() + LINE_ENDING;
			}
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
		finally
		{
			if (fileReader != null)
			{
				try
				{
					fileReader.close();
				}
				catch (IOException e)
				{
					fail(e.getMessage());
				}
			}
		}

		return header.trim();
	}

	private boolean checkJavaHeader(File file)
	{
		String header = extractLicenseHeader(file, 0, 16);

		return javaLicenseHeader.equals(header);
	}

	private boolean checkJavaScriptHeader(File file)
	{
		String header = extractLicenseHeader(file, 0, 16);

		return javaScriptLicenseHeader.equals(header);
	}

	private boolean checkXmlHeader(File file)
	{
		Revision revision = null;

		try
		{
			String header = extractLicenseHeader(file, 0, 17);

			if (header.startsWith("<?xml")) 
			{
				header = header.substring(header.indexOf(LINE_ENDING)+1);
			}
			else
			{
				// Then only take the first 16 lines
				String[] headers = header.split(LINE_ENDING);
				header = "";
				for (int i = 0; i < 16; i++)
				{
					if (header.length() > 0)
					{
						header += LINE_ENDING;
					}
					header += headers[i];
				}
			}

			revision = Diff.diff(xmlLicenseHeader.split(LINE_ENDING), header.split(LINE_ENDING));
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}

		return revision.size() == 0;
	}

	private boolean checkPropertiesHeader(File file)
	{
		Revision revision = null;

		try
		{
			String header = extractLicenseHeader(file, 0, 14);

			revision = Diff.diff(propertiesLicenseHeader.split(LINE_ENDING), header
					.split(LINE_ENDING));
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}

		return revision.size() == 0;
	}

	private boolean checkVelocityHeader(File file)
	{
		Revision revision = null;

		try
		{
			String header = extractLicenseHeader(file, 0, 16);

			revision = Diff.diff(velocityLicenseHeader.split(LINE_ENDING), header
					.split(LINE_ENDING));
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}

		return revision.size() == 0;
	}

	private boolean checkCssHeader(File file)
	{
		Revision revision = null;

		try
		{
			String header = extractLicenseHeader(file, 0, 16);

			revision = Diff.diff(cssLicenseHeader.split(LINE_ENDING), header.split(LINE_ENDING));
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}

		return revision.size() == 0;
	}

	private void visitFiles(String suffix, FileVisitor fileVisitor)
	{
		visitFiles(new String[] { suffix }, null, fileVisitor);
	}

	private void visitFiles(String suffix, String[] ignoreFiles, FileVisitor fileVisitor)
	{
		visitFiles(new String[] { suffix }, ignoreFiles, fileVisitor);
	}

	private void visitFiles(String[] suffixes, FileVisitor fileVisitor)
	{
		visitFiles(suffixes, null, fileVisitor);
	}

	private void visitFiles(String[] suffixes, String[] ignoreFiles, FileVisitor fileVisitor)
	{
		visitDirectory(suffixes, ignoreFiles, baseDirectory, fileVisitor);
	}

	private void visitDirectory(String[] suffixes, String[] ignoreFiles, File directory,
			FileVisitor fileVisitor)
	{
		File[] files = directory.listFiles(new SuffixAndIgnoreFileFilter(suffixes, ignoreFiles));

		if (files != null)
		{
			for (File file : files)
			{
				fileVisitor.visitFile(file);
			}
		}

		// Find the directories in this directory on traverse deeper
		files = directory.listFiles(new DirectoryFileFilter());

		if (files != null)
		{
			for (File childDirectory : files)
			{
				visitDirectory(suffixes, ignoreFiles, childDirectory, fileVisitor);
			}
		}
	}

	/**
	 * @param filename
	 * @return The contents of the file
	 */
	private String loadFile(String filename)
	{
		String contents = null;

		try
		{
			URL url = ApacheLicenseHeaderTestCase.class.getResource(".");
			File legalsDir = new File(url.toURI());
			String legalsDirString = legalsDir.getAbsolutePath();
			contents = new wicket.util.file.File(legalsDirString, filename).readString();
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}

		return contents.trim();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9016.java