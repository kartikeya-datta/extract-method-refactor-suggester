error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6948.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6948.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6948.java
text:
```scala
public M@@yTextField(MarkupContainer parent,final String id, final String input)

/*
 * $Id$ $Revision:
 * 1.51 $ $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.properties;

import wicket.MarkupContainer;
import wicket.markup.html.WebPage;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextField;
import wicket.markup.html.panel.Panel;

/**
 * 
 * @author Juergen Donnerstag
 */
public class TestPage extends WebPage
{
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * Construct.
	 */
	public TestPage()
	{
		add(new Label(this,"label"));

		Form form1 = new Form(this,"form1");
		add(form1);
		form1.add(new MyTextField(form1,"text1", "input-1"));
		form1.add(new MyTextField(form1,"text2", "input-2"));
		form1.add(new MyTextField(form1,"text7", "input-3"));

		Form form2 = new TestForm(this,"form2");
		add(form2);

		Panel panel1 = new Panel(form2,"panel1");
		form2.add(panel1);
		panel1.add(new MyTextField(panel1,"text3", "input-3"));
		panel1.add(new MyTextField(panel1,"text4", "input-4"));

		Panel panel2 = new TestPanel(form2,"panel2");
		form2.add(panel2);
		panel2.add(new MyTextField(panel2,"text5", "input-5"));
		panel2.add(new MyTextField(panel2,"text6", "input-6"));
		panel2.add(new MyTextField(panel2,"text8", "input-8"));
		panel2.add(new MyTextField(panel2,"text9", "input-9"));
		panel2.add(new MyTextField(panel2,"text10", "input-10"));
		panel2.add(new MyTextField(panel2,"text11", "input-11"));
		panel2.add(new MyTextField(panel2,"text12", "input-12"));
	}

	/**
	 * 
	 * @return xxx
	 */
	public MyTextField getText1()
	{
		return (MyTextField)get("form1:text1");
	}

	/**
	 * 
	 * @return xxx
	 */
	public MyTextField getText2()
	{
		return (MyTextField)get("form1:text2");
	}

	/**
	 * 
	 * @return xxx
	 */
	public MyTextField getText3()
	{
		return (MyTextField)get("form2:panel1:text3");
	}

	/**
	 * 
	 * @return xxx
	 */
	public MyTextField getText4()
	{
		return (MyTextField)get("form2:panel1:text4");
	}

	/**
	 * 
	 * @return xxx
	 */
	public MyTextField getText5()
	{
		return (MyTextField)get("form2:panel2:text5");
	}

	/**
	 * 
	 * @return xxx
	 */
	public MyTextField getText6()
	{
		return (MyTextField)get("form2:panel2:text6");
	}

	/**
	 * 
	 * @return xxx
	 */
	public MyTextField getText7()
	{
		return (MyTextField)get("form1:text7");
	}

	/**
	 * 
	 * @return xxx
	 */
	public MyTextField getText8()
	{
		return (MyTextField)get("form2:panel2:text8");
	}

	/**
	 * 
	 * @return xxx
	 */
	public MyTextField getText9()
	{
		return (MyTextField)get("form2:panel2:text9");
	}

	/**
	 * 
	 * @return xxx
	 */
	public MyTextField getText10()
	{
		return (MyTextField)get("form2:panel2:text10");
	}

	/**
	 * 
	 * @return xxx
	 */
	public MyTextField getText11()
	{
		return (MyTextField)get("form2:panel2:text11");
	}

	/**
	 * 
	 * @return xxx
	 */
	public MyTextField getText12()
	{
		return (MyTextField)get("form2:panel2:text12");
	}

	/**
	 * 
	 */
	public static class MyTextField extends TextField
	{
		private static final long serialVersionUID = 1L;

		private String input;

		/**
		 * Construct.
		 * 
		 * @param id
		 * @param input
		 */
		public MyTextField(MarkupContainer<?> parent,final String id, final String input)
		{
			super(parent,id);
			this.input = input;

			setRequired(true);
		}

		/**
		 * @see wicket.markup.html.form.FormComponent#getInput()
		 */
		public String getInput()
		{
			return input;
		}

		/**
		 * @param input
		 */
		public void setInput(String input)
		{
			this.input = input;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6948.java