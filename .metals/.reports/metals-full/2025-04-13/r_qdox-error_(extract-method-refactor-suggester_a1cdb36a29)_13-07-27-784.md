error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4167.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4167.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4167.java
text:
```scala
s@@b.append( len.toString() );

package bsh.commands;

import java.io.*;
import bsh.*;
import java.util.Date;
import java.util.Vector;
import java.util.GregorianCalendar;
import java.util.Calendar;

/**
	This is an example of a bsh command written in Java for speed.
*/
public class dir 
{
	static final String [] months = { "Jan", "Feb", "Mar", "Apr", 
		"May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

	public static String usage() {
		return "usage: dir( String dir )\n       dir()";
	}

	public static void invoke( Interpreter env, NameSpace namespace ) {
		//String dir = getCWD( namespace );
		String dir = ".";
		invoke( env, namespace, dir );
	}

	public static void invoke( 
		Interpreter env, NameSpace namespace, String dir ) 
	{
		File file;
		try {
			file =  env.pathToFile( dir );
		} catch (IOException e ) {
			env.println("error reading path: "+e);
			return;
		}

		if ( !file.exists() || !file.canRead() ) {
			env.println( "Can't read " + file );
			return;
		}
		if ( !file.isDirectory() )  {
			env.println("'"+dir+"' is not a directory");
		}

		String [] files = file.list();
		files = bubbleSort(files);

		for( int i=0; i< files.length; i++ ) {
			File f = new File( dir + File.separator + files[i] );
			StringBuffer sb = new StringBuffer();
			sb.append( f.canRead() ? "r": "-" );
			sb.append( f.canWrite() ? "w": "-" );
			sb.append( "_" );
			sb.append( " ");

			Date d = new Date(f.lastModified());
			GregorianCalendar c = new GregorianCalendar();
			c.setTime(d);
			int day	= c.get(Calendar.DAY_OF_MONTH);
			sb.append( months[ c.get(Calendar.MONTH) ] + " " + day );
			if ( day < 10 ) 
				sb.append(" ");

			sb.append(" ");

			// hack to get fixed length 'length' field
			int fieldlen = 8;
			StringBuffer len = new StringBuffer();
			for(int j=0; j<fieldlen; j++)
				len.append(" ");
			len.insert(0, f.length());
			len.setLength(fieldlen);
			// hack to move the spaces to the front
			int si = len.toString().indexOf(" ");
			if ( si != -1 ) {
				String pad = len.toString().substring(si);
				len.setLength(si);
				len.insert(0, pad);
			}
			
			sb.append( len );

			sb.append( " " + f.getName() );
			if ( f.isDirectory() ) 
				sb.append("/");

			env.println( sb.toString() );
		}
	}

	public static String [] bubbleSort( String [] in ) {
		Vector v = new Vector();
		for(int i=0; i<in.length; i++)
			v.addElement(in[i]);

		int n = v.size();
		boolean swap = true;
		while ( swap ) {
			swap = false;
			for(int i=0; i<(n-1); i++)
				if ( ((String)v.elementAt(i)).compareTo(
						((String)v.elementAt(i+1)) ) > 0 ) {
					String tmp = (String)v.elementAt(i+1);
					v.removeElementAt( i+1 );
					v.insertElementAt( tmp, i );
					swap = true;
				}
		}

		String [] out = new String [ n ];
		v.copyInto(out);
		return out;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4167.java