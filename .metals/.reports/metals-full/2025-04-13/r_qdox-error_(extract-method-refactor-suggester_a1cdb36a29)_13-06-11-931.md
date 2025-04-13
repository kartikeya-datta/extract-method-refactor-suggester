error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3688.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3688.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3688.java
text:
```scala
public static T@@est attributesDatabase(

/*

   Derby - Class org.apache.derbyTesting.junit.Decorator

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to you under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package org.apache.derbyTesting.junit;

import java.util.Properties;
import java.util.Random;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Utility class that provides static methods to decorate tests.
 * Used as a central collection point for decorators than cannot
 * be simply expressed as a TestSetup class. Typically the
 * decorators will be collections of other decorators
 */
public class Decorator {

    private Decorator() {
        super();
    }

    /**
     * Decorate a set of tests to use an encrypted
     * single use database. This is to run tests
     * using encryption as a general test and
     * not specific tests of how encryption is handled.
     * E.g. tests of setting various URL attributes
     * would be handled in a specific test.
     * <BR>
     * The database will use the default encryption
     * algorithm.
     * <BR>
     * A boot password (phrase) is used with a random
     * set of characters and digits 16 characters long.
     * 
     * @param test test to decorate
     * @return decorated tests
     */
    public static Test encryptedDatabase(Test test)
    {
        return encryptedDatabaseBpw(test, getBootPhrase(16));
    }

    /**
     * Decorate a set of tests to use an encrypted
     * single use database. This is to run tests
     * using encryption as a general test and
     * not specific tests of how encryption is handled.
     * E.g. tests of setting various URL attributes
     * would be handled in a specific test.
     * <BR>
     * The database will use the default encryption
     * algorithm.
     * 
     * @param test test to decorate
     * @param bootPassword boot passphrase to use
     * @return decorated tests
     */
    public static Test encryptedDatabaseBpw(Test test, String bootPassword)
    {
        if (JDBC.vmSupportsJSR169())
            return new TestSuite("no encryption support");

        Properties attributes = new Properties();
        attributes.setProperty("dataEncryption", "true");
        attributes.setProperty("bootPassword", bootPassword);

        return attributesDatabase(attributes, test);
    }

    /**
     * Decorate a set of tests to use an encrypted
     * single use database. This is to run tests
     * using encryption as a general test and
     * not specific tests of how encryption is handled.
     * E.g. tests of setting various URL attributes
     * would be handled in a specific test.
     * <BR>
     * The database will use the specified encryption
     * algorithm.
     * <BR>
     * A boot password (phrase) is used with a random
     * set of characters and digits 64 characters long.
     * 
     * @param test test to decorate
     * @return decorated tests
     */
    public static Test encryptedDatabase(Test test, final String algorithm)
    {
        return encryptedDatabaseBpw(test, algorithm, getBootPhrase(16));
    }


    /**
     * Decorate a set of tests to use an encrypted
     * single use database. This is to run tests
     * using encryption as a general test and
     * not specific tests of how encryption is handled.
     * E.g. tests of setting various URL attributes
     * would be handled in a specific test.
     * <BR>
     * The database will use the specified encryption
     * algorithm.
     * 
     * @param test test to decorate
     * @param bootPassword boot passphrase to use
     * @return decorated tests
     */
    public static Test encryptedDatabaseBpw(Test test,
                                            final String algorithm,
                                            String bootPassword)
    {
        Properties attributes = new Properties();
        attributes.setProperty("dataEncryption", "true");
        attributes.setProperty("bootPassword", bootPassword);
        attributes.setProperty("encryptionAlgorithm", algorithm);

        return attributesDatabase(attributes, test);
    }

    private static String getBootPhrase(int length)
    {
        Random rand = new Random();
        
        char[] bp = new char[length];
        for (int i = 0; i < bp.length; ) {
            char c = (char) rand.nextInt();
            if (Character.isLetterOrDigit(c))
            {
                bp[i++] = c;
            }
        }
        
        return new String(bp);
    }
    
    /**
     * Decorate a set of tests to use an single
     * use database with TERRITORY_BASED collation
     * set to the passed in locale.
     * @param locale Locale used to set territory JDBC attribute. If null
     * then only collation=TERRITORY_BASED will be set.
     */
    public static Test territoryCollatedDatabase(Test test, final String locale)
    {
        Properties attributes = new Properties();
        attributes.setProperty("collation", "TERRITORY_BASED");
        
        if (locale != null)
            attributes.setProperty("territory", locale);
        
        return attributesDatabase(attributes, test);
    }

    /**
     * Decorate a set of tests to use a single use database with
     *  logDevice pointing a log directory to non-default location  
     */
    public static Test logDeviceAttributeDatabase(Test test, final String logDevice)
    {
        Properties attributes = new Properties();
        if (logDevice != null) {
            attributes.setProperty("logDevice",logDevice);
        }

        test = TestConfiguration.singleUseDatabaseDecorator(test);
        return attributesDatabase(attributes, test);
    }

    /**
     * Decorate a set of tests to use an single
     * use database with TERRITORY_BASED:SECONDARY collation
     * set to the passed in locale.
     * @param locale Locale used to set territory JDBC attribute. If null
     * then only collation=TERRITORY_BASED:SECONDARY will be set.
     */
    public static Test territoryCollatedCaseInsensitiveDatabase(Test test, final String locale)
    {
        Properties attributes = new Properties();
        attributes.setProperty("collation", "TERRITORY_BASED:SECONDARY");

        if (locale != null)
            attributes.setProperty("territory", locale);

        return attributesDatabase(attributes, test);
    }

    /**
     * Decorate a test (or suite of tests) to use a single use database
     * as the default database with a specified set connection attributes.
     * 
     * @param attributes properties to set in the connection URL or in the
     * connectionAttributes of a data source when connecting to the database
     * @param test Test to decorate
     * @return Decorated test
     */
    private static Test attributesDatabase(
            final Properties attributes, Test test)
    {
        test = new ChangeConfigurationSetup(test) {
            TestConfiguration getNewConfiguration(TestConfiguration old) {
                return old.addConnectionAttributes(attributes);
            }
        };
        
        return TestConfiguration.singleUseDatabaseDecorator(test);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3688.java