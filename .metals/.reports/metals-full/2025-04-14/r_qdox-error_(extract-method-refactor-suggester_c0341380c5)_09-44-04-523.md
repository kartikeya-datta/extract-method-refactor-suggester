error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6285.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6285.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6285.java
text:
```scala
private static final S@@tring rendererPath = "org.columba.mail.composer.mimepartrenderers.";

// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Library General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

package org.columba.mail.composer;

import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Hashtable;

import org.columba.core.io.DiskIO;
import org.columba.mail.message.MimeHeader;
import org.columba.mail.message.MimePart;
import org.columba.mail.message.MimePartTree;

/**
 * @author timo
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class MimeTreeRenderer {
	
	private static final String rendererPath = "org.columba.modules.mail.composer.mimepartrenderers.";
	
	private static final String[] renderers = {
		"MultipartRenderer", "MultipartSignedRenderer" };
	
	private static MimeTreeRenderer myInstance;
	
	private Hashtable rendererTable;
	private MimePartRenderer defaultRenderer;
	
	protected MimeTreeRenderer() {
		rendererTable = new Hashtable();
		loadAllRenderer();
		
		defaultRenderer = new DefaultMimePartRenderer();
	}
	
	public static MimeTreeRenderer getInstance() {
		
		if( myInstance == null )
			myInstance = new MimeTreeRenderer();
		
		return myInstance;	
	}
	
	public String render( MimePartTree tree ) {
		return renderMimePart( tree.getRootMimeNode() );
	}
	
	public String renderMimePart( MimePart part ) {		
		MimePartRenderer renderer = getRenderer( part.getHeader() );
		
		return renderer.render(part);	
	}
	
	private MimePartRenderer getRenderer( MimeHeader input ) {
		// If no ContentType specified return StandardParser
		if (input.contentType == null)
			return defaultRenderer;

		MimePartRenderer renderer;

		// First try to find renderer for "type/subtype"

		renderer =
			(MimePartRenderer) rendererTable.get(
				input.contentType + "/" + input.contentSubtype);
		if (renderer != null) {
			return renderer;
		}

		// Next try to find renderer for "type"

		renderer = (MimePartRenderer) rendererTable.get(input.contentType);
		if (renderer != null) {
			return renderer;
		}

		// Nothing found -> return Standardrenderer
		return defaultRenderer;
		
	}

	private void loadAllRenderer() {
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		Class actClass = null;

		try {
			for (int i = 0; i < Array.getLength(renderers); i++) {
				actClass = loader.loadClass(rendererPath + renderers[i]);

				if (actClass
					.getSuperclass()
					.getName()
					.equals("org.columba.modules.mail.composer.MimePartRenderer")) {

					MimePartRenderer renderer =
						(MimePartRenderer) actClass.newInstance();
							
					rendererTable.put( renderer.getRegisterString(), renderer);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6285.java