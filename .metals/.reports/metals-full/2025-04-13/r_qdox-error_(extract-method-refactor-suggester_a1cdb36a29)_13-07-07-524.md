error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13755.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13755.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,16]

error in qdox parser
file content:
```java
offset: 16
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13755.java
text:
```scala
private static L@@ogger log = LoggingManager.getLoggerForClass();

package org.apache.jmeter.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.jmeter.gui.JMeterGUIComponent;
import org.apache.jmeter.gui.UnsharedComponent;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.reflect.ClassFinder;
import org.apache.log.Logger;

/**
 * @author    Michael Stover
 * @version   $Revision$
 */
public class JMeterTest extends TestCase
{
    private static Logger log = LoggingManager.getLoggerFor(JMeterUtils.TEST);

    public JMeterTest(String name)
    {
        super(name);
    }

    public void testGUIComponents() throws Exception
    {
        Iterator iter = getObjects(JMeterGUIComponent.class).iterator();
        while (iter.hasNext())
        {
            JMeterGUIComponent item = (JMeterGUIComponent) iter.next();
            if (item instanceof JMeterTreeNode)
            {
                continue;
            }
            assertEquals(
                "Failed on " + item.getClass().getName(),
                item.getStaticLabel(),
                item.getName());
            TestElement el = item.createTestElement();
            assertEquals(
                "GUI-CLASS: Failed on " + item.getClass().getName(),
                item.getClass().getName(),
                el.getPropertyAsString(TestElement.GUI_CLASS));
            assertEquals(
                "NAME: Failed on " + item.getClass().getName(),
                item.getName(),
                el.getPropertyAsString(TestElement.NAME));
            assertEquals(
                "TEST-CLASS: Failed on " + item.getClass().getName(),
                el.getClass().getName(),
                el.getPropertyAsString(TestElement.TEST_CLASS));
            TestElement el2 = item.createTestElement();
            el.setProperty(TestElement.NAME, "hey, new name!:");
            el.setProperty("NOT", "Shouldn't be here");
            if (!(item instanceof UnsharedComponent))
            {
                assertEquals(
                    "GUI-CLASS: Failed on " + item.getClass().getName(),
                    "",
                    el2.getPropertyAsString("NOT"));
            }
            log.debug("Saving element: " + el.getClass());
            el =
                SaveService.createTestElement(
                    SaveService.getConfigForTestElement(null, el));
            log.debug("Successfully saved");
            item.configure(el);
            assertEquals(
                "CONFIGURE-TEST: Failed on " + item.getClass().getName(),
                el.getPropertyAsString(TestElement.NAME),
                item.getName());
            item.modifyTestElement(el2);
            assertEquals(
                "Modify Test: Failed on " + item.getClass().getName(),
                "hey, new name!:",
                el2.getPropertyAsString(TestElement.NAME));
        }
    }

    public void testSerializableElements() throws Exception
    {
        Iterator iter = getObjects(Serializable.class).iterator();
        while (iter.hasNext())
        {
            Serializable serObj = (Serializable) iter.next();
            if (serObj.getClass().getName().endsWith("_Stub"))
            {
                continue;
            }
            try
            {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bytes);
                out.writeObject(serObj);
                out.close();
                ObjectInputStream in =
                    new ObjectInputStream(
                        new ByteArrayInputStream(bytes.toByteArray()));
                Object readObject = in.readObject();
                in.close();
                assertEquals(
                    "deserializing class: " + serObj.getClass().getName(),
                    serObj.getClass(),
                    readObject.getClass());
            }
            catch (Exception e)
            {
                log.error(
                    "Trying to serialize object: "
                        + serObj.getClass().getName(),
                    e);
                throw e;
            }
        }
    }

    public void testTestElements() throws Exception
    {
        Iterator iter = getObjects(TestElement.class).iterator();
        while (iter.hasNext())
        {
            TestElement item = (TestElement) iter.next();
            checkElementCloning(item);
            assertTrue(
                item.getClass().getName() + " must implement Serializable",
                item instanceof Serializable);
        }
    }

    protected Collection getObjects(Class extendsClass) throws Exception
    {
        Iterator classes =
            ClassFinder
                .findClassesThatExtend(
                    JMeterUtils.getSearchPaths(),
                    new Class[] { extendsClass })
                .iterator();
        List objects = new LinkedList();
        while (classes.hasNext())
        {
            Class c = Class.forName((String) classes.next());
            try
            {
                try
                {
                    // Try with a parameter-less constructor first
                    objects.add(c.newInstance());
                }
                catch (InstantiationException e)
                {
                    try
                    {
                        // Events often have this constructor
                        objects.add(
                            c.getConstructor(
                                new Class[] { Object.class }).newInstance(
                                new Object[] { this }));
                    }
                    catch (NoSuchMethodException f)
                    {
                        // no luck. Ignore this class
                    }
                }
            }
            catch (IllegalAccessException e)
            {
                // We won't test serialization of restricted-access
                // classes.
            }
        }
        return objects;
    }

    private void cloneTesting(TestElement item, TestElement clonedItem)
    {
        assertTrue(item != clonedItem);
        assertEquals(
            "CLONE-SAME-CLASS: testing " + item.getClass().getName(),
            item.getClass().getName(),
            clonedItem.getClass().getName());
    }

    private void checkElementCloning(TestElement item)
    {
        TestElement clonedItem = (TestElement) item.clone();
        cloneTesting(item, clonedItem);
        PropertyIterator iter2 = item.propertyIterator();
        while (iter2.hasNext())
        {
            JMeterProperty item2 = iter2.next();
            assertEquals(item2, clonedItem.getProperty(item2.getName()));
            assertTrue(item2 != clonedItem.getProperty(item2.getName()));
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13755.java