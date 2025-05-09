error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3698.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3698.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3698.java
text:
```scala
r@@eturn rtn;

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
package org.apache.wicket.markup.html;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Pattern;

import org.apache.wicket.settings.IResourceSettings;
import org.apache.wicket.util.string.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is a resource guard which by default denies access to all resources and thus is more secure.
 * <p/>
 * All pattern are executed in the order they were provided. All pattern are executed to determine
 * if access can be granted or not.
 * <p/>
 * Note that access to the config data such as get/setPattern() and acceptXXX() is not synchronized.
 * It is assumed that configuration has finished before the first request gets executed.
 * <p/>
 * The rules are fairly simple. Each pattern must start with either "+" (include) or "-" (exclude).
 * "*" is a placeholder for zero, one or more characters within a file or directory name. "**" is a
 * placeholder for zero, one or more sub-directories.
 * <p/>
 * Examples:
 * <table border="0">
 * <tr>
 * <td>+*.gif</td>
 * <td>All gif files in all directories</td>
 * </tr>
 * <tr>
 * <td>+test*.*</td>
 * <td>All files in all directories starting with "test"</td>
 * </tr>
 * <tr>
 * <td>+mydir&#47;*&#47;*.gif</td>
 * <td>All gif files two levels below the mydir directory. E.g. mydir&#47;dir2&#47;test.gif</td>
 * </tr>
 * <tr>
 * <td>+mydir&#47;**&#47;*.gif</td>
 * <td>All gif files in all directories below mydir. E.g. mydir&#47;test.gif or
 * mydir&#47;dir2&#47;dir3&#47;test.gif</td>
 * </tr>
 * </table>
 * 
 * @see IPackageResourceGuard
 * @see IResourceSettings#getPackageResourceGuard
 * @see PackageResourceGuard
 * 
 * @author Juergen Donnerstag
 */
public class SecurePackageResourceGuard extends PackageResourceGuard
{
	/** Log. */
	private static final Logger log = LoggerFactory.getLogger(SecurePackageResourceGuard.class);

	/** The path separator used */
	private static final char PATH_SEPARATOR = '/';

	/** The list of pattern. Note that the order is important, hence a list */
	private List<SearchPattern> pattern = new ArrayList<SearchPattern>();

	/** A cache to speed up the checks */
	private final ConcurrentHashMap<String, Boolean> cache;

	/**
	 * Construct.
	 */
	public SecurePackageResourceGuard()
	{
		cache = newCache();
	}

	/**
	 * Get a new cache implementation. Subclasses may return null to disable caching. More advanced
	 * caches (e.h. ehcache) should be used in production environments to limit the size and remove
	 * "old" entries.
	 * 
	 * @return the cache implementation
	 */
	public ConcurrentHashMap<String, Boolean> newCache()
	{
		return new SimpleCache(100);
	}

	/**
	 * 
	 */
	public void clearCache()
	{
		if (cache != null)
		{
			cache.clear();
		}
	}

	/**
	 * Whether the provided absolute path is accepted.
	 * 
	 * @param path
	 *            The absolute path, starting from the class root (packages are separated with
	 *            forward slashes instead of dots).
	 * @return True if accepted, false otherwise.
	 */
	@Override
	protected boolean acceptAbsolutePath(String path)
	{
		// First check the cache
		if (cache != null)
		{
			Boolean rtn = cache.get(path);
			if (rtn != null)
			{
				return rtn.booleanValue();
			}
		}

		// Check typical files such as log4j.xml etc.
		if (super.acceptAbsolutePath(path) == false)
		{
			return false;
		}

		// Check against the pattern
		boolean hit = false;
		for (SearchPattern pattern : this.pattern)
		{
			if ((pattern != null) && pattern.isActive())
			{
				if (pattern.matches(path))
				{
					hit = pattern.isInclude();
				}
			}
		}

		if (cache != null)
		{
			// Do not use putIfAbsent(). See newCache()
			cache.put(path, (hit ? Boolean.TRUE : Boolean.FALSE));
		}

		if (hit == false)
		{
			log.warn("Access denied to shared (static) resource: " + path);
		}

		return hit;
	}

	/**
	 * Gets the current list of pattern. Please invoke clearCache() or setPattern(List) when
	 * finished in order to clear the cache of previous checks.
	 * 
	 * @return pattern
	 */
	public List<SearchPattern> getPattern()
	{
		clearCache();
		return pattern;
	}

	/**
	 * Sets pattern.
	 * 
	 * @param pattern
	 *            pattern
	 */
	public void setPattern(List<SearchPattern> pattern)
	{
		this.pattern = pattern;
		clearCache();
	}

	/**
	 * @param pattern
	 */
	public void addPattern(String pattern)
	{
		this.pattern.add(new SearchPattern(pattern));
		clearCache();
	}

	/**
	 * 
	 */
	public static class SearchPattern
	{
		private String pattern;

		private Pattern regex;

		private boolean include;

		private boolean active = true;

		private boolean fileOnly;

		/**
		 * Construct.
		 * 
		 * @param pattern
		 */
		public SearchPattern(final String pattern)
		{
			setPattern(pattern);
		}

		/**
		 * 
		 * @param pattern
		 * @return Regex pattern
		 */
		private Pattern convertToRegex(final String pattern)
		{
			String regex = Strings.replaceAll(pattern, ".", "#dot#").toString();

			// If path starts with "*/" or "**/"
			regex = regex.replaceAll("^\\*" + PATH_SEPARATOR, "[^" + PATH_SEPARATOR + "]+" +
				PATH_SEPARATOR);
			regex = regex.replaceAll("^[\\*]{2,}" + PATH_SEPARATOR, "([^" + PATH_SEPARATOR +
				"].#star#" + PATH_SEPARATOR + ")?");

			// Handle "/*/" and "/**/"
			regex = regex.replaceAll(PATH_SEPARATOR + "\\*" + PATH_SEPARATOR, PATH_SEPARATOR +
				"[^" + PATH_SEPARATOR + "]+" + PATH_SEPARATOR);
			regex = regex.replaceAll(PATH_SEPARATOR + "[\\*]{2,}" + PATH_SEPARATOR, "(" +
				PATH_SEPARATOR + "|" + PATH_SEPARATOR + ".+" + PATH_SEPARATOR + ")");

			// Handle "*" within dir or file names
			regex = regex.replaceAll("\\*+", "[^" + PATH_SEPARATOR + "]*");

			// replace placeholder
			regex = Strings.replaceAll(regex, "#dot#", "\\.").toString();
			regex = Strings.replaceAll(regex, "#star#", "*").toString();

			return Pattern.compile(regex);
		}

		/**
		 * Gets pattern.
		 * 
		 * @return pattern
		 */
		public String getPattern()
		{
			return pattern;
		}

		/**
		 * Gets regex.
		 * 
		 * @return regex
		 */
		public Pattern getRegex()
		{
			return regex;
		}

		/**
		 * Sets pattern.
		 * 
		 * @param pattern
		 *            pattern
		 */
		public void setPattern(String pattern)
		{
			if (Strings.isEmpty(pattern))
			{
				throw new IllegalArgumentException(
					"Parameter 'pattern' can not be null or an empty string");
			}

			if (pattern.charAt(0) == '+')
			{
				include = true;
			}
			else if (pattern.charAt(0) == '-')
			{
				include = false;
			}
			else
			{
				throw new IllegalArgumentException(
					"Parameter 'pattern' must start with either '+' or '-'. pattern='" + pattern +
						"'");
			}

			this.pattern = pattern;
			regex = convertToRegex(pattern.substring(1));

			fileOnly = (pattern.indexOf(PATH_SEPARATOR) == -1);
		}

		/**
		 * 
		 * @param path
		 * @return True if 'path' matches the pattern
		 */
		public boolean matches(String path)
		{
			if (fileOnly)
			{
				path = Strings.lastPathComponent(path, PATH_SEPARATOR);
			}
			return regex.matcher(path).matches();
		}

		/**
		 * Gets include.
		 * 
		 * @return include
		 */
		public boolean isInclude()
		{
			return include;
		}

		/**
		 * Sets include.
		 * 
		 * @param include
		 *            include
		 */
		public void setInclude(boolean include)
		{
			this.include = include;
		}

		/**
		 * Gets active.
		 * 
		 * @return active
		 */
		public boolean isActive()
		{
			return active;
		}

		/**
		 * Sets active.
		 * 
		 * @param active
		 *            active
		 */
		public void setActive(boolean active)
		{
			this.active = active;
		}

		@Override
		public String toString()
		{
			return "Pattern: " + pattern + ", Regex: " + regex + ", include:" + include +
				", fileOnly:" + fileOnly + ", active:" + active;
		}
	}

	/**
	 * A very simple cache
	 */
	public static class SimpleCache extends ConcurrentHashMap<String, Boolean>
	{
		private static final long serialVersionUID = 1L;

		private final ConcurrentLinkedQueue<String> fifo = new ConcurrentLinkedQueue<String>();

		private final int maxSize;

		/**
		 * Construct.
		 * 
		 * @param maxSize
		 */
		public SimpleCache(int maxSize)
		{
			this.maxSize = maxSize;
		}

		/**
		 * @see java.util.concurrent.ConcurrentHashMap#put(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Boolean put(String key, Boolean value)
		{
			// add the key to the hash map. Do not replace existing once
			Boolean rtn = super.putIfAbsent(key, value);

			// If found, than remove it from the fifo list and ...
			if (rtn != null)
			{
				fifo.remove(key);
			}

			// append it at the end of the list
			fifo.add(key);

			// remove all "outdated" cache entries
			while (fifo.size() > maxSize)
			{
				remove(fifo.poll());
			}
			return rtn;
		}
	};
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3698.java