error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7950.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7950.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7950.java
text:
```scala
S@@tring INSTR_CL_NAME = GlassFishClassLoaderAdapter.INSTRUMENTABLE_CLASSLOADER_GLASSFISH_V2;

/*
 * Copyright 2002-2006 the original author or authors.
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

package org.springframework.instrument.classloading.glassfish;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.lang.instrument.ClassFileTransformer;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.SecureClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.spi.ClassTransformer;

import org.easymock.ArgumentsMatcher;
import org.easymock.MockControl;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.instrument.classloading.LoadTimeWeaver;

// converting away from old-style EasyMock APIs was problematic with this class
@SuppressWarnings("deprecation")
@Ignore
public class GlassFishLoadTimeWeaverTests {

	private MockControl loaderCtrl;
	private GlassFishClassLoaderAdapter loader;
	private LoadTimeWeaver ltw;

	private class DummyInstrumentableClassLoader extends SecureClassLoader {

		static String INSTR_CL_NAME = GlassFishClassLoaderAdapter.INSTRUMENTABLE_CLASSLOADER_GLASSFISH_V2;

		public DummyInstrumentableClassLoader() {
			super();
		}

		public DummyInstrumentableClassLoader(ClassLoader parent) {
			super(parent);
		}

		private List<ClassTransformer> v2Transformers = new ArrayList<ClassTransformer>();
		private List<ClassFileTransformer> v3Transformers = new ArrayList<ClassFileTransformer>();

		public void addTransformer(ClassTransformer transformer) {
			v2Transformers.add(transformer);
		}

		public void addTransformer(ClassFileTransformer transformer) {
			v3Transformers.add(transformer);
		}

		public ClassLoader copy() {
			return new DummyInstrumentableClassLoader();
		}

		@Override
		public Class<?> loadClass(String name) throws ClassNotFoundException {
			if (INSTR_CL_NAME.equals(name)) {
				return this.getClass();
			}

			return getClass().getClassLoader().loadClass(name);
		}
	}

	@Before
	public void setUp() throws Exception {
		ltw = new GlassFishLoadTimeWeaver(new DummyInstrumentableClassLoader());
	}

	@After
	public void tearDown() throws Exception {
		loaderCtrl.verify();
		ltw = null;
	}

	@Test
	public void testGlassFishLoadTimeWeaver() {
		try {
			ltw = new GlassFishLoadTimeWeaver();
			fail("expected exception");
		} catch (IllegalArgumentException ex) {
			// expected
		}

	}

	@Test
	public void testGlassFishLoadTimeWeaverClassLoader() {
		try {
			ltw = new GlassFishLoadTimeWeaver(null);
			fail("expected exception");
		} catch (RuntimeException e) {
			// expected
		}

		ClassLoader cl1 = new URLClassLoader(new URL[0]);
		ClassLoader cl2 = new URLClassLoader(new URL[0], cl1);
		ClassLoader cl3 = new DummyInstrumentableClassLoader(cl2);
		ClassLoader cl4 = new URLClassLoader(new URL[0], cl3);

		ltw = new GlassFishLoadTimeWeaver(cl4);
		assertSame(cl3, ltw.getInstrumentableClassLoader());

		cl1 = new URLClassLoader(new URL[0]);
		cl2 = new URLClassLoader(new URL[0], cl1);
		cl3 = new DummyInstrumentableClassLoader(cl2);
		cl4 = new DummyInstrumentableClassLoader(cl3);

		ltw = new GlassFishLoadTimeWeaver(cl4);
		assertSame(cl4, ltw.getInstrumentableClassLoader());
	}

	@Test
	public void testAddTransformer() {
		ClassFileTransformer transformer = MockControl.createNiceControl(ClassFileTransformer.class).getMock();
		loaderCtrl.reset();
		loader.addTransformer(transformer);
		loaderCtrl.setMatcher(new ArgumentsMatcher() {

			public boolean matches(Object[] arg0, Object[] arg1) {
				for (int i = 0; i < arg0.length; i++) {
					if (arg0 != null && arg0.getClass() != arg1.getClass())
						return false;
				}
				return true;
			}

			public String toString(Object[] arg0) {
				return Arrays.toString(arg0);
			}

		});

		loaderCtrl.replay();

		ltw.addTransformer(transformer);
	}

	@Test
	public void testGetThrowawayClassLoader() {
		loaderCtrl.reset();
		ClassLoader cl = new URLClassLoader(new URL[0]);
		loaderCtrl.expectAndReturn(loader.getClassLoader(), cl);
		loaderCtrl.replay();

		assertSame(ltw.getThrowawayClassLoader(), cl);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7950.java