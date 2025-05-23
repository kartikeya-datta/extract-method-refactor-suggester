error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1245.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1245.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1245.java
text:
```scala
i@@f(lname.endsWith(".html")/*  || lname.endsWith(".txt") */)

/*
 * HelpIndex.java - Index for help searching feature
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 2002 Slava Pestov
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.gjt.sp.jedit.help;

//{{{ Imports
import java.io.*;
import java.net.*;
import java.util.zip.*;
import java.util.*;
import org.gjt.sp.jedit.io.*;
import org.gjt.sp.jedit.*;
import org.gjt.sp.util.Log;
//}}}

public class HelpIndex
{
	//{{{ HelpIndex constructor
	public HelpIndex()
	{
		words = new HashMap();
		files = new ArrayList();

		ignoreWord("a");
		ignoreWord("an");
		ignoreWord("and");
		ignoreWord("are");
		ignoreWord("as");
		ignoreWord("be");
		ignoreWord("by");
		ignoreWord("can");
		ignoreWord("do");
		ignoreWord("for");
		ignoreWord("from");
		ignoreWord("how");
		ignoreWord("i");
		ignoreWord("if");
		ignoreWord("in");
		ignoreWord("is");
		ignoreWord("it");
		ignoreWord("not");
		ignoreWord("of");
		ignoreWord("on");
		ignoreWord("or");
		ignoreWord("s");
		ignoreWord("that");
		ignoreWord("the");
		ignoreWord("this");
		ignoreWord("to");
		ignoreWord("will");
		ignoreWord("with");
		ignoreWord("you");
	} //}}}

	/* //{{{ HelpIndex constructor
	public HelpIndex(String fileListPath, String wordIndexPath)
	{
		this();
	} //}}} */

	//{{{ indexEditorHelp() method
	/**
	 * Indexes all available help, including the jEdit user's guide, FAQ, and
	 * plugin documentation.
	 */
	public void indexEditorHelp() throws Exception
	{
		String jEditHome = jEdit.getJEditHome();
		if(jEditHome != null)
		{
			indexDirectory(MiscUtilities.constructPath(jEditHome,"doc","users-guide"));
			indexDirectory(MiscUtilities.constructPath(jEditHome,"doc","FAQ"));
		}

		EditPlugin.JAR[] jars = jEdit.getPluginJARs();
		for(int i = 0; i < jars.length; i++)
		{
			indexJAR(jars[i].getZipFile());
		}

		Log.log(Log.DEBUG,this,"Indexed " + words.size() + " words");
	} //}}}

	//{{{ indexDirectory() method
	/**
	 * Indexes all HTML and text files in the specified directory.
	 * @param dir The directory
	 */
	public void indexDirectory(String dir) throws Exception
	{
		String[] files = VFSManager.getFileVFS()
			._listDirectory(null,dir,"*.{html,txt}",true,null);

		for(int i = 0; i < files.length; i++)
		{
			indexURL(files[i]);
		}
	} //}}}

	//{{{ indexJAR() method
	/**
	 * Indexes all HTML and text files in the specified JAR file.
	 * @param jar The JAR file
	 */
	public void indexJAR(ZipFile jar) throws Exception
	{
		Enumeration enum = jar.entries();
		while(enum.hasMoreElements())
		{
			ZipEntry entry = (ZipEntry)enum.nextElement();
			String name = entry.getName();
			String lname = name.toLowerCase();
			if(lname.endsWith(".html") || lname.endsWith(".txt"))
			{
				// only works for jEdit plugins
				String url = "jeditresource:/" +
					MiscUtilities.getFileName(jar.getName())
					+ "!/" + name;
				Log.log(Log.DEBUG,this,url);
				indexStream(jar.getInputStream(entry),url);
			}
		}
	} //}}}

	//{{{ indexURL() method
	/**
	 * Reads the specified HTML file and adds all words defined therein to the
	 * index.
	 * @param url The HTML file's URL
	 */
	public void indexURL(String url) throws Exception
	{
		InputStream _in;

		if(MiscUtilities.isURL(url))
			_in =  new URL(url).openStream();
		else
		{
			_in = new FileInputStream(url);
			// hack since HelpViewer needs a URL...
			url = "file:" + url;
		}

		indexStream(_in,url);
	} //}}}

	//{{{ lookupWord() method
	public Word lookupWord(String word)
	{
		Object o = words.get(word);
		if(o == IGNORE)
			return null;
		else
			return (Word)o;
	} //}}}

	//{{{ getFile() method
	public HelpFile getFile(int index)
	{
		return (HelpFile)files.get(index);
	} //}}}

	//{{{ Private members
	private static Word.Occurrence[] EMPTY_ARRAY = new Word.Occurrence[0];
	// used to mark words to ignore (see constructor for the list)
	private static Object IGNORE = new Object();
	private HashMap words;
	private ArrayList files;

	//{{{ ignoreWord() method
	private void ignoreWord(String word)
	{
		words.put(word,IGNORE);
	} //}}}

	//{{{ indexStream() method
	/**
	 * Reads the specified HTML file and adds all words defined therein to the
	 * index.
	 * @param _in The input stream
	 * @param file The file
	 */
	private void indexStream(InputStream _in, String fileName) throws Exception
	{
		HelpFile file = new HelpFile(fileName);
		files.add(file);
		int index = files.size() - 1;

		BufferedReader in = new BufferedReader(new InputStreamReader(_in));

		StringBuffer titleText = new StringBuffer();

		try
		{
			StringBuffer word = new StringBuffer();
			boolean insideTag = false;
			boolean insideEntity = false;

			boolean title = false;

			int c;
			while((c = in.read()) != -1)
			{
				char ch = (char)c;
				if(insideTag)
				{
					if(ch == '>')
					{
						if(word.toString().equals("title"))
							title = true;
						insideTag = false;
						word.setLength(0);
					}
					else
						word.append(ch);
				}
				else if(insideEntity)
				{
					if(ch == ';')
						insideEntity = false;
				}
				else if(ch == '<')
				{
					if(title)
						title = false;

					if(word.length() != 0)
					{
						addWord(word.toString(),index);
						word.setLength(0);
					}

					insideTag = true;
				}
				else if(ch == '&')
					insideEntity = true;
				else if(title)
					titleText.append(ch);
				else if(!Character.isLetterOrDigit(ch))
				{
					if(word.length() != 0)
					{
						addWord(word.toString(),index);
						word.setLength(0);
					}
				}
				else
					word.append(ch);
			}
		}
		finally
		{
			in.close();
		}

		if(titleText.length() == 0)
			file.title = fileName;
		else
			file.title = titleText.toString();
	} //}}}

	//{{{ addWord() method
	private void addWord(String word, int file)
	{
		word = word.toLowerCase();

		Object o = words.get(word);
		if(o == IGNORE)
			return;

		if(o == null)
			words.put(word,new Word(word,file));
		else
			((Word)o).addOccurrence(file);
	} //}}}

	//}}}

	//{{{ Word class
	public static class Word
	{
		// the word
		String word;

		// files it occurs in
		int occurCount = 0;
		Occurrence[] occurrences;

		Word(String word, int file)
		{
			this.word = word;
			occurrences = new Occurrence[5];
			addOccurrence(file);
		}

		void addOccurrence(int file)
		{
			for(int i = 0; i < occurCount; i++)
			{
				if(occurrences[i].file == file)
				{
					occurrences[i].count++;
					return;
				}
			}

			if(occurCount >= occurrences.length)
			{
				Occurrence[] newOccur = new Occurrence[occurrences.length * 2];
				System.arraycopy(occurrences,0,newOccur,0,occurCount);
				occurrences = newOccur;
			}

			occurrences[occurCount++] = new Occurrence(file);
		}

		static class Occurrence
		{
			int file;
			int count;

			Occurrence(int file)
			{
				this.file = file;
				this.count = 1;
			}
		}
	} //}}}

	//{{{ HelpFile class
	static class HelpFile
	{
		String file;
		String title;

		HelpFile(String file)
		{
			this.file = file;
			this.title = title;
		}

		public String toString()
		{
			return title;
		}

		public boolean equals(Object o)
		{
			if(o instanceof HelpFile)
				return ((HelpFile)o).file.equals(file);
			else
				return false;
		}
	} //}}}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1245.java