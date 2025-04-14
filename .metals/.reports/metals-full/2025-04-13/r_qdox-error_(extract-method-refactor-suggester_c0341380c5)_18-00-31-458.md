error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6993.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6993.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6993.java
text:
```scala
public A@@rrayList<Integer> scalarListTarget;

package org.springframework.core.convert.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.convert.ConverterNotFoundException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class CollectionToCollectionConverterTests {

	private GenericConversionService conversionService = new GenericConversionService();

	@Before
	public void setUp() {
		conversionService.addConverter(new CollectionToCollectionConverter(conversionService));
	}

	@Test
	public void scalarList() throws Exception {
		List<String> list = new ArrayList<String>();
		list.add("9");
		list.add("37");
		TypeDescriptor sourceType = TypeDescriptor.forObject(list);
		TypeDescriptor targetType = new TypeDescriptor(getClass().getField("scalarListTarget"));
		assertFalse(conversionService.canConvert(sourceType, targetType));
		conversionService.addConverterFactory(new StringToNumberConverterFactory());
		assertTrue(conversionService.canConvert(sourceType, targetType));
		@SuppressWarnings("unchecked")		
		List<String> result = (List<String>) conversionService.convert(list, sourceType, targetType);
		assertFalse(list.equals(result));
		assertEquals((Integer) 9, result.get(0));
		assertEquals((Integer) 37, result.get(1));
	}
	
	public List<Integer> scalarListTarget;

	@Test
	public void emptyListToList() throws Exception {
		conversionService.addConverter(new CollectionToCollectionConverter(conversionService));
		conversionService.addConverterFactory(new StringToNumberConverterFactory());
		List<String> list = new ArrayList<String>();
		TypeDescriptor sourceType = TypeDescriptor.forObject(list);
		TypeDescriptor targetType = new TypeDescriptor(getClass().getField("emptyListTarget"));
		assertTrue(conversionService.canConvert(sourceType, targetType));
		assertEquals(list, conversionService.convert(list, sourceType, targetType));
	}

	public List<Integer> emptyListTarget;

	@Test
	public void emptyListToListDifferentTargetType() throws Exception {
		conversionService.addConverter(new CollectionToCollectionConverter(conversionService));
		conversionService.addConverterFactory(new StringToNumberConverterFactory());
		List<String> list = new ArrayList<String>();
		TypeDescriptor sourceType = TypeDescriptor.forObject(list);
		TypeDescriptor targetType = new TypeDescriptor(getClass().getField("emptyListDifferentTarget"));
		assertTrue(conversionService.canConvert(sourceType, targetType));
		@SuppressWarnings("unchecked")
		LinkedList<Integer> result = (LinkedList<Integer>) conversionService.convert(list, sourceType, targetType);
		assertEquals(LinkedList.class, result.getClass());
		assertTrue(result.isEmpty());
	}

	public LinkedList<Integer> emptyListDifferentTarget;

	@Test
	public void collectionToObjectInteraction() throws Exception {
		List<List<String>> list = new ArrayList<List<String>>();
		list.add(Arrays.asList("9", "12"));
		list.add(Arrays.asList("37", "23"));
		conversionService.addConverter(new CollectionToObjectConverter(conversionService));
		assertTrue(conversionService.canConvert(List.class, List.class));
		assertEquals(list, conversionService.convert(list, List.class));
	}

	@Test
	public void arrayCollectionToObjectInteraction() throws Exception {
		List<String>[] array = new List[2];
		array[0] = Arrays.asList("9", "12");
		array[1] = Arrays.asList("37", "23");
		conversionService.addConverter(new ArrayToCollectionConverter(conversionService));
		conversionService.addConverter(new CollectionToObjectConverter(conversionService));
		assertTrue(conversionService.canConvert(String[].class, List.class));
		assertEquals(Arrays.asList(array), conversionService.convert(array, List.class));
	}

	@Test
	public void objectToCollection() throws Exception {
		List<List<String>> list = new ArrayList<List<String>>();
		list.add(Arrays.asList("9", "12"));
		list.add(Arrays.asList("37", "23"));
		conversionService.addConverterFactory(new StringToNumberConverterFactory());
		conversionService.addConverter(new ObjectToCollectionConverter(conversionService));
		conversionService.addConverter(new CollectionToObjectConverter(conversionService));
		TypeDescriptor sourceType = TypeDescriptor.forObject(list);
		TypeDescriptor targetType = new TypeDescriptor(getClass().getField("objectToCollection"));
		assertTrue(conversionService.canConvert(sourceType, targetType));
		List<List<List<Integer>>> result = (List<List<List<Integer>>>) conversionService.convert(list, sourceType, targetType);
		assertEquals((Integer)9, result.get(0).get(0).get(0));
		assertEquals((Integer)12, result.get(0).get(1).get(0));
		assertEquals((Integer)37, result.get(1).get(0).get(0));
		assertEquals((Integer)23, result.get(1).get(1).get(0));
	}

	public List<List<List<Integer>>> objectToCollection;
	
	@Test
	public void stringToCollection() throws Exception {
		List<List<String>> list = new ArrayList<List<String>>();
		list.add(Arrays.asList("9,12"));
		list.add(Arrays.asList("37,23"));
		conversionService.addConverterFactory(new StringToNumberConverterFactory());
		conversionService.addConverter(new StringToCollectionConverter(conversionService));
		conversionService.addConverter(new ObjectToCollectionConverter(conversionService));		
		conversionService.addConverter(new CollectionToObjectConverter(conversionService));
		TypeDescriptor sourceType = TypeDescriptor.forObject(list);
		TypeDescriptor targetType = new TypeDescriptor(getClass().getField("objectToCollection"));
		assertTrue(conversionService.canConvert(sourceType, targetType));
		List<List<List<Integer>>> result = (List<List<List<Integer>>>) conversionService.convert(list, sourceType, targetType);
		assertEquals((Integer)9, result.get(0).get(0).get(0));
		assertEquals((Integer)12, result.get(0).get(0).get(1));
		assertEquals((Integer)37, result.get(1).get(0).get(0));
		assertEquals((Integer)23, result.get(1).get(0).get(1));
	}

	@Test
	public void differentImpls() throws Exception {
		List<Resource> resources = new ArrayList<Resource>();
		resources.add(new ClassPathResource("test"));
		resources.add(new FileSystemResource("test"));
		resources.add(new TestResource());
		TypeDescriptor sourceType = TypeDescriptor.forObject(resources);
		assertEquals(resources, conversionService.convert(resources, sourceType, new TypeDescriptor(getClass().getField("resources"))));
	}

	@Test
	public void mixedInNulls() throws Exception {
		List<Resource> resources = new ArrayList<Resource>();
		resources.add(new ClassPathResource("test"));
		resources.add(null);
		resources.add(new FileSystemResource("test"));
		resources.add(new TestResource());
		TypeDescriptor sourceType = TypeDescriptor.forObject(resources);
		assertEquals(resources, conversionService.convert(resources, sourceType, new TypeDescriptor(getClass().getField("resources"))));
	}

	@Test
	public void allNulls() throws Exception {
		List<Resource> resources = new ArrayList<Resource>();
		resources.add(null);
		resources.add(null);
		TypeDescriptor sourceType = TypeDescriptor.forObject(resources);
		assertEquals(resources, conversionService.convert(resources, sourceType, new TypeDescriptor(getClass().getField("resources"))));
	}
	
	@Test(expected=ConverterNotFoundException.class)
	public void nothingInCommon() throws Exception {
		List<Object> resources = new ArrayList<Object>();
		resources.add(new ClassPathResource("test"));
		resources.add(3);
		TypeDescriptor sourceType = TypeDescriptor.forObject(resources);
		assertEquals(resources, conversionService.convert(resources, sourceType, new TypeDescriptor(getClass().getField("resources"))));
	}

	public List<Resource> resources;

	public static abstract class BaseResource implements Resource {

		public InputStream getInputStream() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean exists() {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isReadable() {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isOpen() {
			// TODO Auto-generated method stub
			return false;
		}

		public URL getURL() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		public URI getURI() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		public File getFile() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		public long contentLength() throws IOException {
			// TODO Auto-generated method stub
			return 0;
		}

		public long lastModified() throws IOException {
			// TODO Auto-generated method stub
			return 0;
		}

		public Resource createRelative(String relativePath) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		public String getFilename() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public static class TestResource extends BaseResource {
		
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6993.java