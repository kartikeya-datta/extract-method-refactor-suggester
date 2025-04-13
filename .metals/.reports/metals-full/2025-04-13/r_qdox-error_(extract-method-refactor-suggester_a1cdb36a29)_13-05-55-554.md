error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1974.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1974.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1974.java
text:
```scala
.@@println("          <A href=\"http://aries.apache.org/\" title=\"Apache Aries \">");

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.aries.samples.blog.web.util;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Utility class to provide html headers, footers, dojo use and blogging
 * service.
 */
public class HTMLOutput {

	public static final void writeHTMLHeaderPartOne(PrintWriter out,
			String pageTitle) {
		out.println("<html>");
		out.println(" <head>");

		out
				.println("  <link type=\"text/css\" rel=\"stylesheet\" href=\"style/blog.css\"></link>");
		out.println("  <meta name=\"keywords\" content=\"...\">");
		out.println("  <meta name=\"description\" content=\"...\">");

		out.print("  <title>");
		out.print(pageTitle);
		out.println("  </title>");

		out
				.println("  <META http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">");
		out.println(" </head>");
		
	}

	public static final void writeDojoUses(PrintWriter out, String... modules) {
		out
				.println("<link rel=\"Stylesheet\" href=\"http://ajax.googleapis.com/ajax/libs/dojo/1.4.0/dijit/themes/tundra/tundra.css\" type=\"text/css\" media=\"screen\"/>");
		out
				.println("<link rel=\"Stylesheet\" href=\"http://ajax.googleapis.com/ajax/libs/dojo/1.4.0/dijit/themes/nihilo/nihilo.css\" type=\"text/css\" media=\"screen\"/>");
		out
				.println("<link rel=\"Stylesheet\" href=\"http://ajax.googleapis.com/ajax/libs/dojo/1.4.0/dijit/themes/soria/soria.css\" type=\"text/css\" media=\"screen\"/>");
	
		out
				.println("<script type=\"text/javascript\"  src=\"http://ajax.googleapis.com/ajax/libs/dojo/1.4.0/dojo/dojo.xd.js\" djConfig=\"parseOnLoad: true\"></script>");
		out.println("<script type=\"text/javascript\">");
		out.println("dojo.require(\"dojo.parser\");");

		for (String module : modules) {
			out.print("dojo.require(\"");
			out.print(module);
			out.println("\");");
		}

		out.println("</script>");
	}

	public static final void writeHTMLHeaderPartTwo(PrintWriter out) {
		writeHTMLHeaderPartTwo(out, new ArrayList<String>());
	}

	public static final void writeHTMLHeaderPartTwo(PrintWriter out,
			Collection<String> errorMessages) {

		out.println(" <body class=\"soria\">");

		out
				.println("  <TABLE width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">");
		out.println("   <TR width=\"100%\">");
		out.println("    <TD id=\"cell-0-0\" colspan=\"2\">&nbsp;</TD>");
		out.println("    <TD id=\"cell-0-1\">&nbsp;</TD>");
		out.println("    <TD id=\"cell-0-2\" colspan=\"2\">&nbsp;</TD>");
		out.println("   </TR>");

		out.println("   <TR width=\"100%\">");
		out.println("    <TD id=\"cell-1-0\">&nbsp;</TD>");
		out.println("    <TD id=\"cell-1-1\">&nbsp;</TD>");
		out.println("    <TD id=\"cell-1-2\">");

		out.println("     <DIV style=\"padding: 5px;\">");
		out.println("      <DIV id=\"banner\">");

		out
				.println("       <TABLE border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");
		out.println("        <TR>");
		out.println("         <TD align=\"left\" class=\"topbardiv\" nowrap=\"\">");
		out
				.println("          <A href=\"http://aries.apache.org/aries/\" title=\"Apache Aries \">");
		out
				.println("          <IMG border=\"0\" src=\"images/Arieslogo_Horizontal.gif\">");
		out.println("          </A>");
		out.println("         </TD>");
		out.println("         <TD align=\"right\" nowrap=\"\">");
		out
				.println("          <A href=\"http://www.apache.org/\" title=\"The Apache Software Foundation\">");
		out
				.println("          <IMG border=\"0\" src=\"images/feather.png\">");
		out.println("          </A>");
		out.println("         </TD>");
		out.println("        </TR> ");
		out.println("       </TABLE>");
		out.println("      </DIV>");
		out.println("     </DIV>");

		out.println("     <DIV id=\"top-menu\">");
		out
				.println("      <TABLE border=\"0\" cellpadding=\"1\" cellspacing=\"0\" width=\"100%\">");
		out.println("       <TR>");
		out.println("        <TD>");
		out.println("         <DIV align=\"left\">");
		out.println("          <!-- Breadcrumbs -->");
		out.println("          <!-- Breadcrumbs -->");
		out.println("         </DIV>");
		out.println("        </TD>");
		out.println("        <TD>");
		out.println("         <DIV align=\"right\">");
		out.println("          <!-- Quicklinks -->");
		out.println("           <p><a href=\"ViewBlog\" style=\"text-decoration: none; color: white\">Blog home</a></p>");
		out.println("          <!-- Quicklinks -->");
		out.println("         </DIV>");
		out.println("        </TD>");
		out.println("       </TR>");
		out.println("      </TABLE>");
		out.println("     </DIV>");
		out.println("    </TD>");
		out.println("    <TD id=\"cell-1-3\">&nbsp;</TD>");
		out.println("    <TD id=\"cell-1-4\">&nbsp;</TD>");
		out.println("   </TR>");

		out.println("   <TR width=\"100%\">");
		out.println("    <TD id=\"cell-2-0\" colspan=\"2\">&nbsp;</TD>");
		out.println("    <TD id=\"cell-2-1\">");
		out.println("     <TABLE>");
		out.println("      <TR height=\"100%\" valign=\"top\">");
		out.println("       <TD height=\"100%\"></td>");
		out.println("       <TD height=\"100%\" width=\"100%\">");
		out.println("        <H1>Apache Aries Sample Blog</H1><br>");

		if (!!!errorMessages.isEmpty()) {
			out.println("\t\t\t<div id=\"errorMessages\">");
			for (String msg : errorMessages) {
				out.println("\t\t\t\t<div class=\"errorMessage\">" + msg
						+ "</div>");
			}
			out.println("\t\t\t</div>");
		}

		out.println("        <div id=\"mainContent\" class=\"mainContent\">");
	}

	public static final void writeHTMLFooter(PrintWriter out) {
		out.println("         <BR>");
		out.println("        </DIV>");
		out.println("       </TD>");
		out.println("      </TR>");
		out.println("     </TABLE>");
		out.println("    </TD>");
		out.println("    <TD id=\"cell-2-2\" colspan=\"2\">&nbsp;</TD>");
		out.println("   </TR>");
		out.println("   <TR width=\"100%\">");

		out.println("    <TD id=\"cell-3-0\">&nbsp;</TD>");
		out.println("    <TD id=\"cell-3-1\">&nbsp;</TD>");
		out.println("    <TD id=\"cell-3-2\">");
		out.println("     <DIV id=\"footer\">");
		out.println("     <!-- Footer -->");
		out.println("     </DIV>");
		
		out.println("    </TD>");
		out.println("    <TD id=\"cell-3-3\">&nbsp;</TD>");
		out.println("    <TD id=\"cell-3-4\">&nbsp;</TD>");
		out.println("   </TR>");
		out.println("   <TR width=\"100%\">");
		out.println("    <TD id=\"cell-4-0\" colspan=\"2\">&nbsp;</TD>");
		out.println("    <TD id=\"cell-4-1\">&nbsp;</TD>");

		out.println("    <TD id=\"cell-4-2\" colspan=\"2\">&nbsp;</TD>");
		out.println("   </TR>");
		out.println("  </TABLE>");
		out.println(" </BODY>");
		out.println("</HTML> ");

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1974.java