error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14674.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14674.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14674.java
text:
```scala
i@@nputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);

/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.http.converter.xml;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Result;
import javax.xml.transform.Source;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;

/**
 * An {@code HttpMessageConverter} that can read XML collections using JAXB2.
 *
 * <p>This converter can read {@linkplain Collection collections} that contain classes
 * annotated with {@link XmlRootElement} and {@link XmlType}. Note that this converter
 * does not support writing.
 *
 * @author Arjen Poutsma
 * @since 3.2
 */
@SuppressWarnings("rawtypes")
public class Jaxb2CollectionHttpMessageConverter<T extends Collection>
		extends AbstractJaxb2HttpMessageConverter<T> implements GenericHttpMessageConverter<T> {

	private final XMLInputFactory inputFactory = createXmlInputFactory();

	/**
	 * Always returns {@code false} since Jaxb2CollectionHttpMessageConverter
	 * required generic type information in order to read a Collection.
	 */
	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * <p>Jaxb2CollectionHttpMessageConverter can read a generic
	 * {@link Collection} where the generic type is a JAXB type annotated with
	 * {@link XmlRootElement} or {@link XmlType}.
	 */
	@Override
	public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
		if (!(type instanceof ParameterizedType)) {
			return false;
		}
		ParameterizedType parameterizedType = (ParameterizedType) type;
		if (!(parameterizedType.getRawType() instanceof Class)) {
			return false;
		}
		Class<?> rawType = (Class<?>) parameterizedType.getRawType();
		if (!(Collection.class.isAssignableFrom(rawType))) {
			return false;
		}
		if (parameterizedType.getActualTypeArguments().length != 1) {
			return false;
		}
		Type typeArgument = parameterizedType.getActualTypeArguments()[0];
		if (!(typeArgument instanceof Class)) {
			return false;
		}
		Class<?> typeArgumentClass = (Class<?>) typeArgument;
		return (typeArgumentClass.isAnnotationPresent(XmlRootElement.class) ||
				typeArgumentClass.isAnnotationPresent(XmlType.class)) && canRead(mediaType);
	}

	/**
	 * Always returns {@code false} since Jaxb2CollectionHttpMessageConverter
	 * does not convert collections to XML.
	 */
	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return false;
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		// should not be called, since we override canRead/Write
		throw new UnsupportedOperationException();
	}

	@Override
	protected T readFromSource(Class<? extends T> clazz, HttpHeaders headers, Source source) throws IOException {
		// should not be called, since we return false for canRead(Class)
		throw new UnsupportedOperationException();
	}

	@Override
	@SuppressWarnings("unchecked")
	public T read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {

		ParameterizedType parameterizedType = (ParameterizedType) type;
		T result = createCollection((Class<?>) parameterizedType.getRawType());
		Class<?> elementClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];

		try {
			Unmarshaller unmarshaller = createUnmarshaller(elementClass);
			XMLStreamReader streamReader = this.inputFactory.createXMLStreamReader(inputMessage.getBody());
			int event = moveToFirstChildOfRootElement(streamReader);

			while (event != XMLStreamReader.END_DOCUMENT) {
				if (elementClass.isAnnotationPresent(XmlRootElement.class)) {
					result.add(unmarshaller.unmarshal(streamReader));
				}
				else if (elementClass.isAnnotationPresent(XmlType.class)) {
					result.add(unmarshaller.unmarshal(streamReader, elementClass).getValue());
				}
				else {
					// should not happen, since we check in canRead(Type)
					throw new HttpMessageConversionException("Could not unmarshal to [" + elementClass + "]");
				}
				event = moveToNextElement(streamReader);
			}
			return result;
		}
		catch (UnmarshalException ex) {
			throw new HttpMessageNotReadableException("Could not unmarshal to [" + elementClass + "]: " + ex.getMessage(), ex);
		}
		catch (JAXBException ex) {
			throw new HttpMessageConversionException("Could not instantiate JAXBContext: " + ex.getMessage(), ex);
		}
		catch (XMLStreamException ex) {
			throw new HttpMessageConversionException(ex.getMessage(), ex);
		}
	}

	/**
	 * Create a Collection of the given type, with the given initial capacity
	 * (if supported by the Collection type).
	 *
	 * @param collectionClass the type of Collection to instantiate
	 * @return the created Collection instance
	 */
	@SuppressWarnings("unchecked")
	protected T createCollection(Class<?> collectionClass) {
		if (!collectionClass.isInterface()) {
			try {
				return (T) collectionClass.newInstance();
			}
			catch (Exception ex) {
				throw new IllegalArgumentException(
						"Could not instantiate collection class [" +
								collectionClass.getName() + "]: " + ex.getMessage());
			}
		}
		else if (List.class.equals(collectionClass)) {
			return (T) new ArrayList();
		}
		else if (SortedSet.class.equals(collectionClass)) {
			return (T) new TreeSet();
		}
		else {
			return (T) new LinkedHashSet();
		}
	}

	private int moveToFirstChildOfRootElement(XMLStreamReader streamReader) throws XMLStreamException {
		// root
		int event = streamReader.next();
		while (event != XMLStreamReader.START_ELEMENT) {
			event = streamReader.next();
		}

		// first child
		event = streamReader.next();
		while ((event != XMLStreamReader.START_ELEMENT) && (event != XMLStreamReader.END_DOCUMENT)) {
			event = streamReader.next();
		}
		return event;
	}

	private int moveToNextElement(XMLStreamReader streamReader) throws XMLStreamException {
		int event = streamReader.getEventType();
		while (event != XMLStreamReader.START_ELEMENT && event != XMLStreamReader.END_DOCUMENT) {
			event = streamReader.next();
		}
		return event;
	}

	@Override
	protected void writeToResult(T t, HttpHeaders headers, Result result) throws IOException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Create a {@code XMLInputFactory} that this converter will use to create {@link
	 * javax.xml.stream.XMLStreamReader} and {@link javax.xml.stream.XMLEventReader} objects.
	 * <p/> Can be overridden in subclasses, adding further initialization of the factory.
	 * The resulting factory is cached, so this method will only be called once.
	 *
	 * @return the created factory
	 */
	protected XMLInputFactory createXmlInputFactory() {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		inputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
		return inputFactory;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14674.java