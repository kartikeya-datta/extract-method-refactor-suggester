error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12878.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12878.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12878.java
text:
```scala
p@@arams.put("bootstrap-context","someContext");

/*
* JBoss, Home of Professional Open Source.
* Copyright 2011, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.connector.subsystems.jca;

import java.util.Enumeration;
import java.util.Properties;

import junit.framework.Assert;
import org.jboss.dmr.ModelNode;

/**Common utility class for parsing operation tests
 *
 * @author <a href="vrastsel@redhat.com">Vladimir Rastseluev</a>
 */
public class ParseUtils {
    /**
     * Returns common properties for both XA and Non-XA datasource
     * @param indiName
     */
    public static  Properties commonDsProperties(String jndiName){
    	Properties params=new Properties();
    	//attributes
    	params.put("use-java-context","true");
        params.put("spy","false");
        params.put("use-ccm","true");
    	params.put("jndi-name", jndiName);
    	//common elements
        params.put("driver-name","h2");
        params.put("new-connection-sql","select 1");
        params.put("transaction-isolation","TRANSACTION_READ_COMMITTED");
        params.put("url-delimiter",":");
        params.put("url-selector-strategy-class-name","someClass");
        //pool
        params.put("min-pool-size","1");
        params.put("max-pool-size","5");
        params.put("pool-prefill","true");
        params.put("pool-use-strict-min","true");
        params.put("flush-strategy","EntirePool");
        //security
        params.put("user-name","sa");
        params.put("password","sa");
        params.put("security-domain","HsqlDbRealm");
        params.put("reauth-plugin-class-name","someClass1");
        //validation
        params.put("valid-connection-checker-class-name","someClass2");
        params.put("check-valid-connection-sql","select 1");
        params.put("validate-on-match","true");
        params.put("background-validation","true");
        params.put("background-validation-millis","2000");
        params.put("use-fast-fail","true");
        params.put("stale-connection-checker-class-name","someClass3");
        params.put("exception-sorter-class-name","someClass4");
        //time-out
        params.put("blocking-timeout-wait-millis","20000");
        params.put("idle-timeout-minutes","4");
        params.put("set-tx-query-timeout","true");
        params.put("query-timeout","120");
        params.put("use-try-lock","100");
        params.put("allocation-retry","2");
        params.put("allocation-retry-wait-millis","3000");
        //statement
        params.put("track-statements","nowarn");
        params.put("prepared-statements-cache-size","30");
        params.put("share-prepared-statements","true");

    	return params;
    }
    /**
     * Returns properties for complex XA datasource
     * @param indiName
     */
    public static  Properties xaDsProperties(String jndiName){
    	Properties params=commonDsProperties(jndiName);
    	//attributes

        //common
        params.put("xa-datasource-class","org.jboss.as.connector.subsystems.datasources.ModifiableXaDataSource");
        //xa-pool
        params.put("same-rm-override","true");
        params.put("interleaving","true");
        params.put("no-tx-separate-pool","true");
        params.put("pad-xid","true");
        params.put("wrap-xa-resource","true");
        //time-out
        params.put("xa-resource-timeout","120");
        //recovery
        params.put("no-recovery","false");
        params.put("recovery-plugin-class-name","someClass5");
        params.put("recovery-username","sa");
        params.put("recovery-password","sa");
        params.put("recovery-security-domain","HsqlDbRealm");


    	return params;
    }
    /**
     * Returns properties for non XA datasource
     * @param jndiName
     */
    public static Properties nonXaDsProperties(String jndiName){
    	Properties params=commonDsProperties(jndiName);    	//attributes
        params.put("jta","false");
        //common
        params.put("driver-class","org.hsqldb.jdbcDriver");
        params.put("datasource-class","org.jboss.as.connector.subsystems.datasources.ModifiableDataSource");
        params.put("connection-url","jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");

        return params;
    }
    /**
     * Returns common properties for resource-adapter element

     */
    public static  Properties raCommonProperties(){
    	Properties params=new Properties();
    	 params.put("archive","some.rar");
         params.put("transaction-support","XATransaction");
         params.put("bootstrapcontext","someContext");

    	return params;
    }
    /**
     * Returns properties for RA connection-definition element

     */
    public static  Properties raConnectionProperties(){
    	Properties params=new Properties();
    	//attributes
    	params.put("use-java-context","false");
        params.put("class-name","Class1");
        params.put("use-ccm","true");
    	params.put("jndi-name", "java:jboss/name1");
    	params.put("enabled","false");
        //pool
        params.put("min-pool-size","1");
        params.put("max-pool-size","5");
        params.put("pool-prefill","true");
        params.put("pool-use-strict-min","true");
        params.put("flush-strategy","IdleConnections");
        //xa-pool
        params.put("same-rm-override","true");
        params.put("interleaving","true");
        params.put("no-tx-separate-pool","true");
        params.put("pad-xid","true");
        params.put("wrap-xa-resource","true");
        //security
        params.put("security-application","true");
        //validation
        params.put("background-validation","true");
        params.put("background-validation-millis","5000");
        params.put("use-fast-fail","true");
        //time-out
        params.put("blocking-timeout-wait-millis","5000");
        params.put("idle-timeout-minutes","4");
        params.put("allocation-retry","2");
        params.put("allocation-retry-wait-millis","3000");
        params.put("xa-resource-timeout","300");
        //recovery
        params.put("no-recovery","false");
        params.put("recovery-plugin-class-name","someClass2");
        params.put("recovery-username","sa");
        params.put("recovery-password","sa-pass");
        params.put("recovery-security-domain","HsqlDbRealm");

    	return params;
    }
    /**
     * Returns properties for RA admin-object element

     */
    public static  Properties raAdminProperties(){
    	Properties params=new Properties();
    	//attributes
    	params.put("use-java-context","false");
        params.put("class-name","Class3");
    	params.put("jndi-name", "java:jboss/Name3");
    	params.put("enabled","true");

    	return params;
    }

    /**
     * Sets parameters for DMR operation
     * @param operation
     * @param params
     */
    public static void setOperationParams(ModelNode operation,Properties params){
    	String str;
    	Enumeration e = params.propertyNames();

        while (e.hasMoreElements()) {
        	str=(String)e.nextElement();
        	operation.get(str).set(params.getProperty(str));
        }
    }
    /**
     * Adds properties of Extension type to the operation
     * TODO: not implemented jet in DMR
     */
    public static void addExtensionProperties(ModelNode operation){
    	/*

        operation.get("reauth-plugin-properties","Property").set("A");
        operation.get("valid-connection-checker-properties","Property").set("B");
        operation.get("stale-connect,roperties","Property").set("C");
        operation.get("exception-sorter-properties","Property").set("D");
       */
        /*final ModelNode sourcePropertiesAddress = address.clone();
        sourcePropertiesAddress.add("reauth-plugin-properties", "Property");
        sourcePropertiesAddress.protect();
        final ModelNode sourcePropertyOperation = new ModelNode();
        sourcePropertyOperation.get(OP).set("add");
        sourcePropertyOperation.get(OP_ADDR).set(sourcePropertiesAddress);
        sourcePropertyOperation.get("value").set("A");

        execute(sourcePropertyOperation);*/

    }
    /**
     * Controls if result of reparsing contains certain parameters
     * @param model
     * @param params
     */
    public static void checkModelParams(ModelNode node,Properties params){
    	String str;

        StringBuffer sb = new StringBuffer();
        String par,child;
        Enumeration e = params.propertyNames();

        while (e.hasMoreElements()) {
        	str=(String)e.nextElement();
        	par=params.getProperty(str);
        	if (node.get(str)==null) sb.append("Parameter <"+str+"> is not set, but must be set to '"+par+"' \n");
        	else{
        		child= node.get(str).asString();
        		if (!child.equals(par)) sb.append("Parameter <"+str+"> is set to '"+child+"', but must be set to '"+par+"' \n");
        	}
        }
        if (sb.length()>0) Assert.fail("There are parsing errors:\n"+sb.toString()+"Parsed configuration:\n"+node);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12878.java