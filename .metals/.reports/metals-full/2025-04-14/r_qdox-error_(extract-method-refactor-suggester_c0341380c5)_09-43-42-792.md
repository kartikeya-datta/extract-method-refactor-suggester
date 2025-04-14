error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10771.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10771.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10771.java
text:
```scala
M@@apper<NestedDto, Nested> nestedMapper = new Mapper<NestedDto, Nested>() {

package org.springframework.mapping.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.core.convert.converter.Converter;
import org.springframework.mapping.Mapper;
import org.springframework.mapping.MappingException;

public class SpelMapperTests {

	private SpelMapper mapper = new SpelMapper();

	@Test
	public void mapAutomatic() {
		Map<String, Object> source = new HashMap<String, Object>();
		source.put("name", "Keith");
		source.put("age", 31);

		Person target = new Person();

		mapper.map(source, target);

		assertEquals("Keith", target.name);
		assertEquals(31, target.age);
	}

	@Test
	public void mapExplicit() throws MappingException {
		Map<String, Object> source = new HashMap<String, Object>();
		source.put("name", "Keith");
		source.put("age", 31);

		Person target = new Person();

		mapper.setAutoMappingEnabled(false);
		mapper.addMapping("name");
		mapper.map(source, target);

		assertEquals("Keith", target.name);
		assertEquals(0, target.age);
	}

	@Test
	public void mapAutomaticWithExplictOverrides() {
		Map<String, Object> source = new HashMap<String, Object>();
		source.put("name", "Keith");
		source.put("test", "3");
		source.put("favoriteSport", "FOOTBALL");

		Person target = new Person();

		mapper.addMapping("test", "age");
		mapper.map(source, target);

		assertEquals("Keith", target.name);
		assertEquals(3, target.age);
		assertEquals(Sport.FOOTBALL, target.favoriteSport);
	}

	@Test
	public void mapAutomaticIgnoreUnknownField() {
		Map<String, Object> source = new HashMap<String, Object>();
		source.put("name", "Keith");
		source.put("age", 31);
		source.put("unknown", "foo");

		Person target = new Person();

		mapper.map(source, target);

		assertEquals("Keith", target.name);
		assertEquals(31, target.age);
	}

	@Test
	public void mapAutomaticWithExclusions() {
		Map<String, Object> source = new HashMap<String, Object>();
		source.put("name", "Keith");
		source.put("test", "3");
		source.put("favoriteSport", "FOOTBALL");

		Person target = new Person();

		mapper.addMapping("test").setExclude();
		mapper.map(source, target);

		assertEquals("Keith", target.name);
		assertEquals(0, target.age);
		assertEquals(Sport.FOOTBALL, target.favoriteSport);
	}

	@Test
	public void mapSameSourceFieldToMultipleTargets() {
		Map<String, Object> source = new HashMap<String, Object>();
		source.put("test", "FOOTBALL");

		Person target = new Person();

		mapper.addMapping("test", "name");
		mapper.addMapping("test", "favoriteSport");
		mapper.map(source, target);

		assertEquals("FOOTBALL", target.name);
		assertEquals(0, target.age);
		assertEquals(Sport.FOOTBALL, target.favoriteSport);
	}

	@Test
	public void mapBean() {
		PersonDto source = new PersonDto();
		source.setFullName("Keith Donald");
		source.setAge("31");
		source.setSport("FOOTBALL");

		Person target = new Person();

		mapper.addMapping("fullName", "name");
		mapper.addMapping("sport", "favoriteSport");
		mapper.map(source, target);

		assertEquals("Keith Donald", target.name);
		assertEquals(31, target.age);
		assertEquals(Sport.FOOTBALL, target.favoriteSport);
	}

	@Test
	public void mapBeanDeep() {
		PersonDto source = new PersonDto();
		source.age = "0";
		NestedDto nested = new NestedDto();
		nested.foo = "bar";
		source.setNested(nested);

		Person target = new Person();

		mapper.addMapping("nested.foo");
		mapper.map(source, target);

		assertEquals("bar", target.nested.foo);
	}

	@Test
	public void mapBeanNested() {
		PersonDto source = new PersonDto();
		NestedDto nested = new NestedDto();
		nested.foo = "bar";
		source.setNested(nested);

		Person target = new Person();

		mapper.setAutoMappingEnabled(false);
		mapper.addMapping("nested");
		mapper.map(source, target);

		assertEquals("bar", target.nested.foo);
	}

	@Test
	public void mapBeanNestedCustomNestedMapper() {
		PersonDto source = new PersonDto();
		NestedDto nested = new NestedDto();
		nested.foo = "bar";
		source.setNested(nested);

		Person target = new Person();

		SpelMapper nestedMapper = new SpelMapper();
		nestedMapper.setAutoMappingEnabled(false);
		nestedMapper.addMapping("foo").setConverter(new Converter<String, String>() {
			public String convert(String source) {
				return source + " and baz";
			}
		});
		mapper.addNestedMapper(NestedDto.class, Nested.class, nestedMapper);

		mapper.setAutoMappingEnabled(false);
		mapper.addMapping("nested");
		mapper.map(source, target);

		assertEquals("bar and baz", target.nested.foo);
	}

	@Test
	public void mapBeanNestedCustomNestedMapperHandCoded() {
		PersonDto source = new PersonDto();
		NestedDto nested = new NestedDto();
		nested.foo = "bar";
		source.setNested(nested);

		Person target = new Person();

		Mapper nestedMapper = new Mapper<NestedDto, Nested>() {
			public Object map(NestedDto source, Nested target) {
				target.foo = source.foo + " and baz";
				return target;
			}

		};
		mapper.addNestedMapper(nestedMapper);

		mapper.setAutoMappingEnabled(false);
		mapper.addMapping("nested");
		mapper.map(source, target);

		assertEquals("bar and baz", target.nested.foo);
	}

	@Test
	public void mapList() {
		PersonDto source = new PersonDto();
		List<String> sports = new ArrayList<String>();
		sports.add("FOOTBALL");
		sports.add("BASKETBALL");
		source.setSports(sports);

		Person target = new Person();

		mapper.setAutoMappingEnabled(false);
		mapper.addMapping("sports", "favoriteSports");
		mapper.map(source, target);

		assertEquals(Sport.FOOTBALL, target.favoriteSports.get(0));
		assertEquals(Sport.BASKETBALL, target.favoriteSports.get(1));
	}

	@Test
	public void mapListFlatten() {
		PersonDto source = new PersonDto();
		List<String> sports = new ArrayList<String>();
		sports.add("FOOTBALL");
		sports.add("BASKETBALL");
		source.setSports(sports);

		Person target = new Person();

		mapper.setAutoMappingEnabled(false);
		mapper.addMapping("sports[0]", "favoriteSport");
		mapper.map(source, target);

		assertEquals(Sport.FOOTBALL, target.favoriteSport);
		assertNull(target.favoriteSports);
	}

	@Test
	public void mapMap() {
		PersonDto source = new PersonDto();
		Map<String, String> friendRankings = new HashMap<String, String>();
		friendRankings.put("Keri", "1");
		friendRankings.put("Alf", "2");
		source.setFriendRankings(friendRankings);

		Person target = new Person();

		mapper.setAutoMappingEnabled(false);
		mapper.addMapping("friendRankings", "friendRankings");
		mapper.getConverterRegistry().addConverter(new Converter<String, Person>() {
			public Person convert(String source) {
				return new Person(source);
			}
		});
		mapper.map(source, target);

		assertEquals(new Integer(1), target.friendRankings.get(new Person("Keri")));
		assertEquals(new Integer(2), target.friendRankings.get(new Person("Alf")));
	}

	@Test
	public void mapFieldConverter() {
		Map<String, Object> source = new HashMap<String, Object>();
		source.put("name", "Keith Donald");
		source.put("age", 31);

		Person target = new Person();

		mapper.addMapping("name").setConverter(new Converter<String, String>() {
			public String convert(String source) {
				String[] names = source.split(" ");
				return names[0] + " P. " + names[1];
			}
		});
		mapper.map(source, target);

		assertEquals("Keith P. Donald", target.name);
		assertEquals(31, target.age);
	}

	@Test
	public void mapFailure() {
		Map<String, Object> source = new HashMap<String, Object>();
		source.put("name", "Keith");
		source.put("age", "invalid");
		Person target = new Person();
		try {
			mapper.map(source, target);
		} catch (MappingException e) {
			assertEquals(1, e.getMappingFailureCount());
		}
	}

	@Test
	public void mapCyclic() {
		Person source = new Person();
		source.setName("Keith");
		source.setAge(3);
		source.setFavoriteSport(Sport.FOOTBALL);
		source.cyclic = source;
		Person target = new Person();
		mapper.map(source, target);
		assertEquals("Keith", target.getName());
		assertEquals(3, target.getAge());
		assertEquals(Sport.FOOTBALL, target.getFavoriteSport());
		assertEquals(source.cyclic, target.cyclic);
	}

	@Test
	public void mapCyclicTypicalHibernateDomainModel() {
		Order source = new Order();
		source.setNumber(1);
		LineItem item = new LineItem();
		item.setAmount(new BigDecimal("30.00"));
		item.setOrder(source);
		source.setLineItem(item);

		Order target = new Order();

		mapper.map(source, target);
		assertEquals(1, target.getNumber());
		assertTrue(item != target.getLineItem());
		assertEquals(new BigDecimal("30.00"), target.getLineItem().getAmount());
		assertEquals(source, target.getLineItem().getOrder());
	}

	public static class Order {

		private int number;

		private LineItem lineItem;

		public int getNumber() {
			return number;
		}

		public void setNumber(int number) {
			this.number = number;
		}

		public LineItem getLineItem() {
			return lineItem;
		}

		public void setLineItem(LineItem lineItem) {
			this.lineItem = lineItem;
		}

	}

	public static class LineItem {

		private BigDecimal amount;

		private Order order;

		public BigDecimal getAmount() {
			return amount;
		}

		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}

		public Order getOrder() {
			return order;
		}

		public void setOrder(Order order) {
			this.order = order;
		}

	}

	public static class PersonDto {

		private String fullName;

		private String age;

		private String sport;

		private List<String> sports;

		private Map<String, String> friendRankings;

		private NestedDto nested;

		public String getFullName() {
			return fullName;
		}

		public void setFullName(String fullName) {
			this.fullName = fullName;
		}

		public String getAge() {
			return age;
		}

		public void setAge(String age) {
			this.age = age;
		}

		public String getSport() {
			return sport;
		}

		public void setSport(String sport) {
			this.sport = sport;
		}

		public List<String> getSports() {
			return sports;
		}

		public void setSports(List<String> sports) {
			this.sports = sports;
		}

		public Map<String, String> getFriendRankings() {
			return friendRankings;
		}

		public void setFriendRankings(Map<String, String> friendRankings) {
			this.friendRankings = friendRankings;
		}

		public NestedDto getNested() {
			return nested;
		}

		public void setNested(NestedDto nested) {
			this.nested = nested;
		}

	}

	public static class NestedDto {

		private String foo;

		public String getFoo() {
			return foo;
		}
	}

	public static class Person {

		private String name;

		private int age;

		private Sport favoriteSport;

		private Nested nested;

		private Person cyclic;

		private List<Sport> favoriteSports;

		private Map<Person, Integer> friendRankings;

		public Person() {

		}

		public Person(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public Sport getFavoriteSport() {
			return favoriteSport;
		}

		public void setFavoriteSport(Sport favoriteSport) {
			this.favoriteSport = favoriteSport;
		}

		public Nested getNested() {
			return nested;
		}

		public void setNested(Nested nested) {
			this.nested = nested;
		}

		public Person getCyclic() {
			return cyclic;
		}

		public void setCyclic(Person cyclic) {
			this.cyclic = cyclic;
		}

		public List<Sport> getFavoriteSports() {
			return favoriteSports;
		}

		public void setFavoriteSports(List<Sport> favoriteSports) {
			this.favoriteSports = favoriteSports;
		}

		public Map<Person, Integer> getFriendRankings() {
			return friendRankings;
		}

		public void setFriendRankings(Map<Person, Integer> friendRankings) {
			this.friendRankings = friendRankings;
		}

		public int hashCode() {
			return name.hashCode();
		}

		public boolean equals(Object o) {
			if (!(o instanceof Person)) {
				return false;
			}
			Person p = (Person) o;
			return name.equals(p.name);
		}
	}

	public static class Nested {

		private String foo;

		public String getFoo() {
			return foo;
		}

		public void setFoo(String foo) {
			this.foo = foo;
		}

	}

	public enum Sport {
		FOOTBALL, BASKETBALL
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10771.java