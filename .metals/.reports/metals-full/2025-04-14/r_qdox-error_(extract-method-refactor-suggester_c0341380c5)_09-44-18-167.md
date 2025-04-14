error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13617.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13617.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 858
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13617.java
text:
```scala
public final class PackageTest extends JMeterTestCase {

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
p@@ackage org.apache.jmeter.testbeans.gui;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.jmeter.gui.util.JMeterMenuBar;
import org.apache.jmeter.junit.JMeterTestCase;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.reflect.ClassFinder;
import org.apache.log.Logger;

import junit.framework.Test;
// import junit.framework.TestCase;
import junit.framework.TestSuite;

/*
 * Find all beans out there and check their resource property files: - Check
 * that non-default property files don't have any extra keys. - Check all
 * necessary properties are defined at least in the default property file,
 * except for beans whose name contains "Experimental" or "Alpha". 
 * 
 * TODO: - Check property files don't have duplicate keys (is this important)
 * 
 */
public class PackageTest extends JMeterTestCase {
    private static final Logger log = LoggingManager.getLoggerForClass();

    // ResourceBundle i18nEdit=
    // ResourceBundle.getBundle("org.apache.jmeter.resources.i18nedit");
    private static final Locale defaultLocale = new Locale("en",""); // i18nEdit.getString("locale.default");

    private final ResourceBundle defaultBundle;

    private final Class<?> testBeanClass;

    private final Locale testLocale;

    private PackageTest(Class<?> testBeanClass, Locale locale, ResourceBundle defaultBundle) {
        super(testBeanClass.getName() + " - " + locale.getLanguage() + " - " + locale.getCountry());
        this.testBeanClass = testBeanClass;
        this.testLocale = locale;
        this.defaultBundle = defaultBundle;
    }

    private PackageTest(String name){
        super(name);
        this.testBeanClass = null;
        this.testLocale = null;
        this.defaultBundle = null;
    }
    
    private BeanInfo beanInfo;

    private ResourceBundle bundle;

    @Override
    public void setUp() {
        if (testLocale == null) {
            return;// errorDetected()
        }
        JMeterUtils.setLocale(testLocale);
        Introspector.flushFromCaches(testBeanClass);
        try {
            beanInfo = Introspector.getBeanInfo(testBeanClass);
            bundle = (ResourceBundle) beanInfo.getBeanDescriptor().getValue(GenericTestBeanCustomizer.RESOURCE_BUNDLE);
        } catch (IntrospectionException e) {
            log.error("Can't get beanInfo for " + testBeanClass.getName(), e);
            throw new Error(e.toString()); // Programming error. Don't continue.
        }
        if (bundle == null) {
            throw new Error("This can't happen!");
        }
    }

    @Override
    public void tearDown() {
        JMeterUtils.setLocale(Locale.getDefault());
    }

    @Override
    public void runTest() throws Throwable {
        if (testLocale == null) {
            super.runTest();
            return;// errorDetected()
        }
        if (bundle == defaultBundle) {
            checkAllNecessaryKeysPresent();
        } else {
            checkNoInventedKeys();
        }
    }

    public void checkNoInventedKeys() {
        // Check that all keys in the bundle are also in the default bundle:
        for (Enumeration<String> keys = bundle.getKeys(); keys.hasMoreElements();) {
            String key = keys.nextElement();
            defaultBundle.getString(key);
            // Will throw MissingResourceException if key is not there.
        }
    }

    public void checkAllNecessaryKeysPresent() {
        // Check that all necessary keys are there:

        // displayName is always mandatory:
        String dn = defaultBundle.getString("displayName").toUpperCase(Locale.ENGLISH);

        // Skip the rest of this test for alpha/experimental beans:
        if (dn.indexOf("(ALPHA") != -1 || dn.indexOf("(EXPERIMENTAL") != -1) {
            return;
        }

        // Check for property- and group-related texts:
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < descriptors.length; i++) {
            // Skip non-editable properties, that is:
            // Ignore hidden, read-only, and write-only properties
            if (descriptors[i].isHidden() || descriptors[i].getReadMethod() == null
 descriptors[i].getWriteMethod() == null) {
                continue;
            }
            // Ignore TestElement properties which don't have an explicit
            // editor:
            if (TestElement.class.isAssignableFrom(descriptors[i].getPropertyType())
                    && descriptors[i].getPropertyEditorClass() == null) {
                continue;
            }
            // Done -- we're working with an editable property.

            String name = descriptors[i].getName();

            bundle.getString(name + ".displayName");
            // bundle.getString(name+".shortDescription"); NOT MANDATORY

            String group = (String) descriptors[i].getValue(GenericTestBeanCustomizer.GROUP);
            if (group != null) {
                bundle.getString( group + ".displayName");
            }
        }
    }

    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite("Bean Resource Test Suite");

        List<String> testBaeanclassNames = ClassFinder.findClassesThatExtend(JMeterUtils.getSearchPaths(), new Class[] { TestBean.class });

        boolean errorDetected = false;
        JMeterUtils.setLocale(defaultLocale);
        for (String className : testBaeanclassNames) {
            Class<?> testBeanClass = Class.forName(className);
            ResourceBundle defaultBundle = null;
            try {
                defaultBundle = (ResourceBundle) Introspector.getBeanInfo(testBeanClass).getBeanDescriptor().getValue(
                        GenericTestBeanCustomizer.RESOURCE_BUNDLE);
            } catch (IntrospectionException e) {
                log.error("Can't get beanInfo for " + testBeanClass.getName(), e);
                throw new Error(e.toString()); // Programming error. Don't
                                                // continue.
            }

            if (defaultBundle == null) {
                if (className.startsWith("org.apache.jmeter.examples.")) {
                    log.info("No default bundle found for " + className);
                    continue;
                }
                errorDetected=true;
                log.error("No default bundle found for " + className + " using " + defaultLocale.toString());
                //throw new Error("No default bundle for class " + className);
                continue;
            }

            suite.addTest(new PackageTest(testBeanClass, defaultLocale, defaultBundle));

            String [] languages = JMeterMenuBar.getLanguages();
            for (int i=0; i < languages.length; i++){
                final String[] language = languages[i].split("_");
                if (language.length == 1){
                    suite.addTest(new PackageTest(testBeanClass, new Locale(language[0]), defaultBundle));                                    
                } else if (language.length == 2){
                    suite.addTest(new PackageTest(testBeanClass, new Locale(language[0], language[1]), defaultBundle));                                                        
                }
            }
        }

        if (errorDetected)
        {
            suite.addTest(new PackageTest("errorDetected"));
        }
        return suite;
    }

    public void errorDetected(){
        fail("One or more errors detected - see log file");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13617.java