error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1939.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1939.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[137,2]

error in qdox parser
file content:
```java
offset: 2363
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1939.java
text:
```scala
package wicket.examples.compref;

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.examples.beanedit;

import java.io.Serializable;
import java.util.Date;

/**
 * A person.
 *
 * @author Eelco Hillenius
 */
public class Person implements Serializable
{
	private String name;
	private String lastName;
	private Date dateOfBirth;
	private Address address;
	
	/**
	 * Construct.
	 */
	public Person()
	{
	}

	/**
	 * Construct.
	 * @param name name
	 * @param lastName last name
	 */
	public Person(String name, String lastName)
	{
		this.name = name;
		this.lastName = lastName;
	}

	/**
	 * Gets the dateOfBirth.
	 * @return dateOfBirth
	 */
	public Date getDateOfBirth()
	{
		return dateOfBirth;
	}

	/**
	 * Sets the dateOfBirth.
	 * @param dateOfBirth dateOfBirth
	 */
	public void setDateOfBirth(Date dateOfBirth)
	{
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * Gets the lastName.
	 * @return lastName
	 */
	public String getLastName()
	{
		return lastName;
	}

	/**
	 * Sets the lastName.
	 * @param lastName lastName
	 */
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	/**
	 * Gets the name.
	 * @return name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the name.
	 * @param name name
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Gets the address.
	 * @return address
	 */
	public Address getAddress()
	{
		return address;
	}

	/**
	 * Sets the address.
	 * @param address address
	 */
	public void setAddress(Address address)
	{
		this.address = address;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return "Person{name=" + name + ",lastName=" + lastName +
			",dateOfBirth=" + dateOfBirth + "}";
	}
}
 N@@o newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1939.java