error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9305.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9305.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 697
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9305.java
text:
```scala
protected class RecordExtractorImpl implements RecordExtractor<Object> {

/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

p@@ackage org.springframework.jca.cci.object;

import java.sql.SQLException;

import javax.resource.ResourceException;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;
import javax.resource.cci.RecordFactory;

import org.springframework.dao.DataAccessException;
import org.springframework.jca.cci.core.RecordCreator;
import org.springframework.jca.cci.core.RecordExtractor;

/**
 * EIS operation object that expects mapped input and output objects,
 * converting to and from CCI Records.
 *
 * <p>Concrete subclasses must implement the abstract
 * {@code createInputRecord(RecordFactory, Object)} and
 * {@code extractOutputData(Record)} methods, to create an input
 * Record from an object and to convert an output Record into an object,
 * respectively.
 *
 * @author Thierry Templier
 * @author Juergen Hoeller
 * @since 1.2
 * @see #createInputRecord(javax.resource.cci.RecordFactory, Object)
 * @see #extractOutputData(javax.resource.cci.Record)
 */
public abstract class MappingRecordOperation extends EisOperation {

	/**
	 * Constructor that allows use as a JavaBean.
	 */
	public MappingRecordOperation() {
	}

	/**
	 * Convenient constructor with ConnectionFactory and specifications
	 * (connection and interaction).
	 * @param connectionFactory ConnectionFactory to use to obtain connections
	 */
	public MappingRecordOperation(ConnectionFactory connectionFactory, InteractionSpec interactionSpec) {
		getCciTemplate().setConnectionFactory(connectionFactory);
		setInteractionSpec(interactionSpec);
	}

	/**
	 * Set a RecordCreator that should be used for creating default output Records.
	 * <p>Default is none: CCI's {@code Interaction.execute} variant
	 * that returns an output Record will be called.
	 * <p>Specify a RecordCreator here if you always need to call CCI's
	 * {@code Interaction.execute} variant with a passed-in output Record.
	 * This RecordCreator will then be invoked to create a default output Record instance.
	 * @see javax.resource.cci.Interaction#execute(javax.resource.cci.InteractionSpec, Record)
	 * @see javax.resource.cci.Interaction#execute(javax.resource.cci.InteractionSpec, Record, Record)
	 * @see org.springframework.jca.cci.core.CciTemplate#setOutputRecordCreator
	 */
	public void setOutputRecordCreator(RecordCreator creator) {
		getCciTemplate().setOutputRecordCreator(creator);
	}

	/**
	 * Execute the interaction encapsulated by this operation object.
	 * @param inputObject the input data, to be converted to a Record
	 * by the {@code createInputRecord} method
	 * @return the output data extracted with the {@code extractOutputData} method
	 * @throws DataAccessException if there is any problem
	 * @see #createInputRecord
	 * @see #extractOutputData
	 */
	public Object execute(Object inputObject) throws DataAccessException {
		return getCciTemplate().execute(
				getInteractionSpec(), new RecordCreatorImpl(inputObject), new RecordExtractorImpl());
	}


	/**
	 * Subclasses must implement this method to generate an input Record
	 * from an input object passed into the {@code execute} method.
	 * @param inputObject the passed-in input object
	 * @return the CCI input Record
	 * @throws ResourceException if thrown by a CCI method, to be auto-converted
	 * to a DataAccessException
	 * @see #execute(Object)
	 */
	protected abstract Record createInputRecord(RecordFactory recordFactory, Object inputObject)
			throws ResourceException, DataAccessException;

	/**
	 * Subclasses must implement this method to convert the Record returned
	 * by CCI execution into a result object for the {@code execute} method.
	 * @param outputRecord the Record returned by CCI execution
	 * @return the result object
	 * @throws ResourceException if thrown by a CCI method, to be auto-converted
	 * to a DataAccessException
	 * @see #execute(Object)
	 */
	protected abstract Object extractOutputData(Record outputRecord)
			throws ResourceException, SQLException, DataAccessException;


	/**
	 * Implementation of RecordCreator that calls the enclosing
	 * class's {@code createInputRecord} method.
	 */
	protected class RecordCreatorImpl implements RecordCreator {

		private final Object inputObject;

		public RecordCreatorImpl(Object inObject) {
			this.inputObject = inObject;
		}

		@Override
		public Record createRecord(RecordFactory recordFactory) throws ResourceException, DataAccessException {
			return createInputRecord(recordFactory, this.inputObject);
		}
	}


	/**
	 * Implementation of RecordExtractor that calls the enclosing
	 * class's {@code extractOutputData} method.
	 */
	protected class RecordExtractorImpl implements RecordExtractor {

		@Override
		public Object extractData(Record record) throws ResourceException, SQLException, DataAccessException {
			return extractOutputData(record);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9305.java