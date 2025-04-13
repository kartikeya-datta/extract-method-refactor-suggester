error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14899.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14899.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14899.java
text:
```scala
m@@essage = sfsb.queryCacheCheckIfEmpty(id);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.test.integration.jpa.secondlevelcache;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * JPA Second level cache tests
 * 
 * @author Scott Marlow and Zbynek Roubalik
 */
@RunWith(Arquillian.class)
public class JPA2LCTestCase {

    private static final String ARCHIVE_NAME = "jpa_SecondLevelCacheTestCase";
    
    // cache region name prefix, use getCacheRegionName() method to get the value!
    private static String CACHE_REGION_NAME = null;

    @Deployment
    public static Archive<?> deploy() {

        JavaArchive jar = ShrinkWrap.create(JavaArchive.class, ARCHIVE_NAME + ".jar");
        jar.addClasses(JPA2LCTestCase.class,
            Employee.class,
            SFSB1.class,
            SFSB2LC.class
        );

        jar.addAsManifestResource(JPA2LCTestCase.class.getPackage(), "persistence.xml","persistence.xml");

        return jar;
    }

    @ArquillianResource
    private InitialContext iniCtx;

    protected <T> T lookup(String beanName, Class<T> interfaceType) throws NamingException {
        return interfaceType.cast(iniCtx.lookup("java:global/" + ARCHIVE_NAME + "/" + beanName + "!" + interfaceType.getName()));
    }

    protected <T> T rawLookup(String name, Class<T> interfaceType) throws NamingException {
        return interfaceType.cast(iniCtx.lookup(name));
    }
    
    // Cache region name depends on the internal entity cache naming convention:
    // "fully application scoped persistence unit name" + "the entity class full name"
    // first part could be rewritten by property "hibernate.cache.region_prefix"
    // This method returns prefix + package name, the entity name needs to be appended
    public String getCacheRegionName() throws Exception{
    	
    	if (CACHE_REGION_NAME == null){
    		SFSB2LC sfsb = lookup("SFSB2LC", SFSB2LC.class);
    		String prefix = sfsb.getCacheRegionName();
    		
    		assertNotNull("'hibernate.cache.region_prefix' is null.", prefix);
    		CACHE_REGION_NAME = prefix + '.' + this.getClass().getPackage().getName() + '.';
    	}

    	return CACHE_REGION_NAME; 	
    }

    
    @Test
    public void testMultipleNonTXTransactionalEntityManagerInvocations() throws Exception {
        SFSB1 sfsb1 = lookup("SFSB1", SFSB1.class);
        sfsb1.createEmployee("Kelly Smith", "Watford, England", 10);
        sfsb1.createEmployee("Alex Scott", "London, England", 20);
        sfsb1.getEmployeeNoTX(10);
        sfsb1.getEmployeeNoTX(20);

        DataSource ds = rawLookup("java:jboss/datasources/ExampleDS", DataSource.class);
        Connection conn = ds.getConnection();
        try {
            int deleted = conn.prepareStatement("delete from Employee").executeUpdate();
            // verify that delete worked (or test is invalid)
            assertTrue("was able to delete added rows.  delete count=" + deleted, deleted > 1);

        } finally {
            conn.close();
        }

        // read deleted data from second level cache
        Employee emp = sfsb1.getEmployeeNoTX(10);

        assertTrue("was able to read deleted database row from second level cache", emp != null);
    }

    // When caching is disabled, no extra action is done or exception happens
 	// even if the code marks an entity and/or a query as cacheable
 	@Test
 	public void testDisabledCache() throws Exception {

 		SFSB2LC sfsb = lookup("SFSB2LC", SFSB2LC.class);
 		String message = sfsb.disabled2LCCheck();

 		if (!message.equals("OK")){
 			fail(message);
 		}
 	}

 	// When entity caching is enabled, loading all entities at once
 	// will put all entities in the cache. During the SAME session,
 	// when looking up for the ID of an entity which was returned by
 	// the original query, no SQL queries should be executed.
 	@Test
 	public void testEntityCacheSameSession() throws Exception {

 		SFSB2LC sfsb = lookup("SFSB2LC", SFSB2LC.class);
 		String message = sfsb.sameSessionCheck(getCacheRegionName());

 		if (!message.equals("OK")){
 			fail(message);
 		}
 	}

 	// When entity caching is enabled, loading all entities at once
 	// will put all entities in the cache. During the SECOND session,
 	// when looking up for the ID of an entity which was returned by
 	// the original query, no SQL queries should be executed.
 	@Test
 	public void testEntityCacheSecondSession() throws Exception {

 		SFSB2LC sfsb = lookup("SFSB2LC", SFSB2LC.class);
 		String message = sfsb.secondSessionCheck(getCacheRegionName());

 		if (!message.equals("OK")){
 			fail(message);
 		}

 	}

 	// Check if evicting entity second level cache is working as expected
	@Test
	@Ignore  //FIXME AS7-3541, fixed in Hibernate 4.1.0
 	public void testEvictEntityCache() throws Exception {

 		SFSB2LC sfsb = lookup("SFSB2LC", SFSB2LC.class);
 		String message = sfsb.addEntitiesAndEvictAll(getCacheRegionName());

 		if (!message.equals("OK")){
 			fail(message);
 		}
 		
 		message = sfsb.evictedEntityCacheCheck(getCacheRegionName());

 		if (!message.equals("OK")){
 			fail(message);
 		}
 	}

 	// When query caching is enabled, running the same query twice 
 	// without any operations between them will perform SQL queries only once.
 	@Test
 	public void testSameQueryTwice() throws Exception {

 		SFSB2LC sfsb = lookup("SFSB2LC", SFSB2LC.class);
 	    String id = "1";

 	    String message = sfsb.queryCacheCheck(id);

 		if (!message.equals("OK")){
 			fail(message);
 		}
 	}
 	

    //When query caching is enabled, running a query to return all entities of a class
    // and then adding one entity of such class would invalidate the cache
    @Test
    public void testInvalidateQuery() throws Exception {

        SFSB2LC sfsb = lookup("SFSB2LC", SFSB2LC.class);
        String id = "2";

        String message = sfsb.queryCacheCheck(id);
        
        if (!message.equals("OK")){
            fail(message);
        }
    
        // invalidate the cache
        sfsb.createEmployee("Newman", "Paul", 400);
        
        message = sfsb.queryCacheCheck(id);

        if (!message.equals("OK")){
            fail(message);
        }

    }
 	
 	// Check if evicting query cache is working as expected
 	@Test
	@Ignore  //FIXME HHH-7127, fixed in Hibernate 4.1.1
 	public void testEvictQueryCache() throws Exception {

 		SFSB2LC sfsb = lookup("SFSB2LC", SFSB2LC.class);
 		String id = "3";
 		
 		String message = sfsb.queryCacheCheck(id);

 		if (!message.equals("OK")){
 			fail(message);
 		}
 		
 		// evict query cache
 		sfsb.evictQueryCache();
 		
 		message = sfsb.queryCacheCheck(id);

 		if (!message.equals("OK")){
 			fail(message);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14899.java