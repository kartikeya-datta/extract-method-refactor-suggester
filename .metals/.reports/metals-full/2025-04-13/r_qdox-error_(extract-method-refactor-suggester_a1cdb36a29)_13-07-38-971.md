error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9575.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9575.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 698
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9575.java
text:
```scala
public class ScenariosForSpringSecurity extends AbstractExpressionTests {

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

p@@ackage org.springframework.expression.spel;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.MethodExecutor;
import org.springframework.expression.MethodResolver;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypeConverter;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.ReflectionHelper;
import org.springframework.expression.spel.support.StandardEvaluationContext;

///CLOVER:OFF
/**
 * Spring Security scenarios from https://wiki.springsource.com/display/SECURITY/Spring+Security+Expression-based+Authorization
 *
 * @author Andy Clement
 */
public class ScenariosForSpringSecurity extends ExpressionTestCase {

	@Test
	public void testScenario01_Roles() throws Exception {
		try {
			SpelExpressionParser parser = new SpelExpressionParser();
			StandardEvaluationContext ctx = new StandardEvaluationContext();
			Expression expr = parser.parseRaw("hasAnyRole('MANAGER','TELLER')");

			ctx.setRootObject(new Person("Ben"));
			Boolean value = expr.getValue(ctx,Boolean.class);
			assertFalse(value);

			ctx.setRootObject(new Manager("Luke"));
			value = expr.getValue(ctx,Boolean.class);
			assertTrue(value);

		} catch (EvaluationException ee) {
			ee.printStackTrace();
			fail("Unexpected SpelException: " + ee.getMessage());
		}
	}

	@Test
	public void testScenario02_ComparingNames() throws Exception {
		SpelExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext ctx = new StandardEvaluationContext();

		ctx.addPropertyAccessor(new SecurityPrincipalAccessor());

		// Multiple options for supporting this expression: "p.name == principal.name"
		// (1) If the right person is the root context object then "name==principal.name" is good enough
		Expression expr = parser.parseRaw("name == principal.name");

		ctx.setRootObject(new Person("Andy"));
		Boolean value = expr.getValue(ctx,Boolean.class);
		assertTrue(value);

		ctx.setRootObject(new Person("Christian"));
		value = expr.getValue(ctx,Boolean.class);
		assertFalse(value);

		// (2) Or register an accessor that can understand 'p' and return the right person
		expr = parser.parseRaw("p.name == principal.name");

		PersonAccessor pAccessor = new PersonAccessor();
		ctx.addPropertyAccessor(pAccessor);
		ctx.setRootObject(null);

		pAccessor.setPerson(new Person("Andy"));
		value = expr.getValue(ctx,Boolean.class);
		assertTrue(value);

		pAccessor.setPerson(new Person("Christian"));
		value = expr.getValue(ctx,Boolean.class);
		assertFalse(value);
	}

	@Test
	public void testScenario03_Arithmetic() throws Exception {
		SpelExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext ctx = new StandardEvaluationContext();

		// Might be better with a as a variable although it would work as a property too...
		// Variable references using a '#'
		Expression expr = parser.parseRaw("(hasRole('SUPERVISOR') or (#a <  1.042)) and hasIpAddress('10.10.0.0/16')");

		Boolean value = null;

		ctx.setVariable("a",1.0d); // referenced as #a in the expression
		ctx.setRootObject(new Supervisor("Ben")); // so non-qualified references 'hasRole()' 'hasIpAddress()' are invoked against it
		value = expr.getValue(ctx,Boolean.class);
		assertTrue(value);

		ctx.setRootObject(new Manager("Luke"));
		ctx.setVariable("a",1.043d);
		value = expr.getValue(ctx,Boolean.class);
		assertFalse(value);
	}

	// Here i'm going to change which hasRole() executes and make it one of my own Java methods
	@Test
	public void testScenario04_ControllingWhichMethodsRun() throws Exception {
		SpelExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext ctx = new StandardEvaluationContext();

		ctx.setRootObject(new Supervisor("Ben")); // so non-qualified references 'hasRole()' 'hasIpAddress()' are invoked against it);

		ctx.addMethodResolver(new MyMethodResolver()); // NEEDS TO OVERRIDE THE REFLECTION ONE - SHOW REORDERING MECHANISM
		// Might be better with a as a variable although it would work as a property too...
		// Variable references using a '#'
//		SpelExpression expr = parser.parseExpression("(hasRole('SUPERVISOR') or (#a <  1.042)) and hasIpAddress('10.10.0.0/16')");
		Expression expr = parser.parseRaw("(hasRole(3) or (#a <  1.042)) and hasIpAddress('10.10.0.0/16')");

		Boolean value = null;

		ctx.setVariable("a",1.0d); // referenced as #a in the expression
		value = expr.getValue(ctx,Boolean.class);
		assertTrue(value);

//			ctx.setRootObject(new Manager("Luke"));
//			ctx.setVariable("a",1.043d);
//			value = (Boolean)expr.getValue(ctx,Boolean.class);
//			assertFalse(value);
	}


	static class Person {

		private String n;

		Person(String n) { this.n = n; }

		public String[] getRoles() { return new String[]{"NONE"}; }

		public boolean hasAnyRole(String... roles) {
			if (roles==null) return true;
			String[] myRoles = getRoles();
			for (int i=0;i<myRoles.length;i++) {
				for (int j=0;j<roles.length;j++) {
					if (myRoles[i].equals(roles[j])) return true;
				}
			}
			return false;
		}

		public boolean hasRole(String role) {
			return hasAnyRole(role);
		}

		public boolean hasIpAddress(String ipaddr) {
			return true;
		}

		public String getName() { return n; }
	}


	static class Manager extends Person {

		Manager(String n) {
			super(n);
		}

		@Override
		public String[] getRoles() { return new String[]{"MANAGER"};}
	}


	static class Teller extends Person {

		Teller(String n) {
			super(n);
		}

		@Override
		public String[] getRoles() { return new String[]{"TELLER"};}
	}


	static class Supervisor extends Person {

		Supervisor(String n) {
			super(n);
		}

		@Override
		public String[] getRoles() { return new String[]{"SUPERVISOR"};}
	}


	static class SecurityPrincipalAccessor implements PropertyAccessor {

		static class Principal {
			public String name = "Andy";
		}

		@Override
		public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
			return name.equals("principal");
		}

		@Override
		public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
			return new TypedValue(new Principal());
		}

		@Override
		public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException {
			return false;
		}

		@Override
		public void write(EvaluationContext context, Object target, String name, Object newValue)
				throws AccessException {
		}

		@Override
		public Class<?>[] getSpecificTargetClasses() {
			return null;
		}


	}


	static class PersonAccessor implements PropertyAccessor {

		Person activePerson;

		void setPerson(Person p) { this.activePerson = p; }

		@Override
		public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
			return name.equals("p");
		}

		@Override
		public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
			return new TypedValue(activePerson);
		}

		@Override
		public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException {
			return false;
		}

		@Override
		public void write(EvaluationContext context, Object target, String name, Object newValue)
				throws AccessException {
		}

		@Override
		public Class<?>[] getSpecificTargetClasses() {
			return null;
		}

	}


	static class MyMethodResolver implements MethodResolver {

		static class HasRoleExecutor implements MethodExecutor {

			TypeConverter tc;

			public HasRoleExecutor(TypeConverter typeConverter) {
				this.tc = typeConverter;
			}

			@Override
			public TypedValue execute(EvaluationContext context, Object target, Object... arguments)
					throws AccessException {
				try {
					Method m = HasRoleExecutor.class.getMethod("hasRole", String[].class);
					Object[] args = arguments;
					if (args != null) {
						ReflectionHelper.convertAllArguments(tc, args, m);
					}
					if (m.isVarArgs()) {
						args = ReflectionHelper.setupArgumentsForVarargsInvocation(m.getParameterTypes(), args);
					}
					return new TypedValue(m.invoke(null, args), new TypeDescriptor(new MethodParameter(m,-1)));
				}
				catch (Exception ex) {
					throw new AccessException("Problem invoking hasRole", ex);
				}
			}

			public static boolean hasRole(String... strings) {
				return true;
			}
		}

		@Override
		public MethodExecutor resolve(EvaluationContext context, Object targetObject, String name, List<TypeDescriptor> arguments)
				throws AccessException {
			if (name.equals("hasRole")) {
				return new HasRoleExecutor(context.getTypeConverter());
			}
			return null;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9575.java