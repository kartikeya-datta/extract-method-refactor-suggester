error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9570.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9570.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 686
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9570.java
text:
```scala
public class MapAccessTests extends AbstractExpressionTests {

/*
 * Copyright 2002-2014 the original author or authors.
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

p@@ackage org.springframework.expression.spel;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import static org.junit.Assert.*;

/**
 * Testing variations on map access.
 *
 * @author Andy Clement
 */
public class MapAccessTests extends ExpressionTestCase {

	@Test
	public void testSimpleMapAccess01() {
		evaluate("testMap.get('monday')", "montag", String.class);
	}

	@Test
	public void testMapAccessThroughIndexer() {
		evaluate("testMap['monday']", "montag", String.class);
	}

	@Test
	public void testCustomMapAccessor() throws Exception {
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext ctx = TestScenarioCreator.getTestEvaluationContext();
		ctx.addPropertyAccessor(new MapAccessor());

		Expression expr = parser.parseExpression("testMap.monday");
		Object value = expr.getValue(ctx, String.class);
		assertEquals("montag", value);
	}

	@Test
	public void testVariableMapAccess() throws Exception {
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext ctx = TestScenarioCreator.getTestEvaluationContext();
		ctx.setVariable("day", "saturday");

		Expression expr = parser.parseExpression("testMap[#day]");
		Object value = expr.getValue(ctx, String.class);
		assertEquals("samstag", value);
	}

	@Test
	public void testGetValue(){
		Map<String,String> props1 = new HashMap<String,String>();
		props1.put("key1", "value1");
		props1.put("key2", "value2");
		props1.put("key3", "value3");

		Object bean = new TestBean("name1", new TestBean("name2", null, "Description 2", 15, props1), "description 1", 6, props1);

		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression("testBean.properties['key2']");
		String key = (String) exp.getValue(bean);
		assertNotNull(key);
	}


	public static class TestBean {

		private String name;
		private TestBean testBean;
		private String description;
		private Integer priority;
		private Map<String, String> properties;

		public TestBean(String name, TestBean testBean, String description, Integer priority, Map<String, String> props) {
			this.name = name;
			this.testBean = testBean;
			this.description = description;
			this.priority = priority;
			this.properties = props;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public TestBean getTestBean() {
			return testBean;
		}

		public void setTestBean(TestBean testBean) {
			this.testBean = testBean;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Integer getPriority() {
			return priority;
		}

		public void setPriority(Integer priority) {
			this.priority = priority;
		}

		public Map getProperties() {
			return properties;
		}

		public void setProperties(Map properties) {
			this.properties = properties;
		}
	}


	public static class MapAccessor implements PropertyAccessor {

		@Override
		public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
			return (((Map) target).containsKey(name));
		}

		@Override
		public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
			return new TypedValue(((Map) target).get(name));
		}

		@Override
		public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException {
			return true;
		}

		@Override
		@SuppressWarnings("unchecked")
		public void write(EvaluationContext context, Object target, String name, Object newValue) throws AccessException {
			((Map) target).put(name, newValue);
		}

		@Override
		public Class<?>[] getSpecificTargetClasses() {
			return new Class<?>[] {Map.class};
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9570.java