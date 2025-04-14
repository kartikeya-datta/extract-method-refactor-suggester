error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17343.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17343.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,18]

error in qdox parser
file content:
```java
offset: 18
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17343.java
text:
```scala
protected static L@@ogger log = Hierarchy.getDefaultHierarchy().getLoggerFor("jmeter.elements");

package org.apache.jmeter.testelement;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.jmeter.control.NextIsNullException;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.MapProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.testelement.property.PropertyIteratorImpl;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;

/****************************************
 * Title: JMeter Description: Copyright: Copyright (c) 2000 Company: Apache
 *
 *@author    Michael Stover
 *@created   $Date$
 *@version   1.0
 ***************************************/

public abstract class AbstractTestElement implements TestElement, Serializable
{
    transient private static Logger log = Hierarchy.getDefaultHierarchy().getLoggerFor("jmeter.elements");

    private Map propMap = Collections.synchronizedMap(new HashMap());

    private boolean runningVersion = false;

    /****************************************
     * !ToDo (Method description)
     *
     *@return   !ToDo (Return description)
     ***************************************/
    public Object clone()
    {
        TestElement clonedElement = null;
        try
        {
            clonedElement = (TestElement) this.getClass().newInstance();
        }
        catch (Exception e)
        {}

        PropertyIterator iter = propertyIterator();
        while (iter.hasNext())
        {
            clonedElement.setProperty((JMeterProperty) iter.next().clone());
        }
        clonedElement.setRunningVersion(runningVersion);
        return clonedElement;
    }

    public void clear()
    {
        propMap.clear();
    }

    public void removeProperty(String key)
    {
        propMap.remove(key);
    }

    public boolean equals(Object o)
    {
        if (o instanceof AbstractTestElement)
        {
            return ((AbstractTestElement) o).propMap.equals(propMap);
        }
        else
        {
            return false;
        }
    }

    /****************************************
     * !ToDo
     *
     *@param el  !ToDo
     ***************************************/
    public void addTestElement(TestElement el)
    {
        mergeIn(el);
    }

    /****************************************
     * !ToDo (Method description)
     *
     *@param name  !ToDo (Parameter description)
     ***************************************/
    public void setName(String name)
    {
        setProperty(new StringProperty(TestElement.NAME, name));
    }

    /****************************************
     * !ToDoo (Method description)
     *
     *@return   !ToDo (Return description)
     ***************************************/
    public String getName()
    {
        return getProperty(TestElement.NAME).getStringValue();
    }

    /****************************************
     * Get the named property.  If it doesn't
     * exist, a NullProperty object is
     * returned.
     * **************************************/
    public JMeterProperty getProperty(String key)
    {
        JMeterProperty prop = (JMeterProperty) propMap.get(key);
        if (prop == null)
        {
            prop = new NullProperty(key);
        }
        return prop;
    }

    public void traverse(TestElementTraverser traverser)
    {
        PropertyIterator iter = propertyIterator();
        traverser.startTestElement(this);
        while (iter.hasNext())
        {
            traverseProperty(traverser, iter.next());
        }
        traverser.endTestElement(this);
    }

    protected void traverseProperty(TestElementTraverser traverser, JMeterProperty value)
    {
        traverser.startProperty(value);
        if (value instanceof TestElementProperty)
        {
            ((TestElement) value.getObjectValue()).traverse(traverser);
        }
        else if (value instanceof CollectionProperty)
        {
            traverseCollection((CollectionProperty) value, traverser);
        }
        else if (value instanceof MapProperty)
        {
            traverseMap((MapProperty) value, traverser);
        }
        traverser.endProperty(value);
    }

    protected void traverseMap(MapProperty map, TestElementTraverser traverser)
    {
        PropertyIterator iter = map.valueIterator();
        while (iter.hasNext())
        {
            traverseProperty(traverser, iter.next());
        }
    }

    protected void traverseCollection(CollectionProperty col, TestElementTraverser traverser)
    {
        PropertyIterator iter = col.iterator();
        while (iter.hasNext())
        {
            traverseProperty(traverser, iter.next());
        }
    }

    public int getPropertyAsInt(String key)
    {
        return getProperty(key).getIntValue();
    }

    public boolean getPropertyAsBoolean(String key)
    {
        return getProperty(key).getBooleanValue();
    }

    public float getPropertyAsFloat(String key)
    {
        return getProperty(key).getFloatValue();
    }

    public long getPropertyAsLong(String key)
    {
        return getProperty(key).getLongValue();
    }

    public double getPropertyAsDouble(String key)
    {
        return getProperty(key).getDoubleValue();
    }

    public String getPropertyAsString(String key)
    {
        return getProperty(key).getStringValue();
    }

    protected void addProperty(JMeterProperty property)
    {
        if (isRunningVersion())
        {
            property.setTemporary(true, this);
        }
        else
        {
            property.clearTemporary(this);
        }
        JMeterProperty prop = getProperty(property.getName());
        if (prop instanceof NullProperty || (prop instanceof StringProperty && prop.getStringValue().equals("")))
        {
            propMap.put(property.getName(), property);
        }
        else
        {
            prop.mergeIn(property);
        }
    }

    /**
     * Log the properties of the test element
     * @see org.apache.jmeter.testelement.TestElement#setProperty(JMeterProperty)
     */
    protected void logProperties()
    {
        if (log.isDebugEnabled())
        {
            PropertyIterator iter = propertyIterator();
            while (iter.hasNext())
            {
                JMeterProperty prop = iter.next();
                log.debug("Property " + prop.getName() + " is temp? " + prop.isTemporary(this) + " and is a " + prop.getObjectValue());
            }
        }
    }

    public void setProperty(JMeterProperty property)
    {
        if (isRunningVersion())
        {
            if (getProperty(property.getName()) instanceof NullProperty)
            {
                addProperty(property);
            }
            else
            {
                getProperty(property.getName()).setObjectValue(property.getObjectValue());
            } 
        }
        else
        {
            propMap.put(property.getName(), property);
        }
    }

    public void setProperty(String name, String value)
    {
        setProperty(new StringProperty(name, value));
    }

    public PropertyIterator propertyIterator()
    {
        return new PropertyIteratorImpl(propMap.values());
    }

    /****************************************
     * !ToDo (Method description)
     *
     *@param element  !ToDo (Parameter description)
     ***************************************/
    protected void mergeIn(TestElement element)
    {
        PropertyIterator iter = element.propertyIterator();
        while (iter.hasNext())
        {
            JMeterProperty prop = iter.next();
            addProperty(prop);
        }
    }
    /**
     * Returns the runningVersion.
     * @return boolean
     */
    public boolean isRunningVersion()
    {
        return runningVersion;
    }

    /**
     * Sets the runningVersion.
     * @param runningVersion The runningVersion to set
     */
    public void setRunningVersion(boolean runningVersion)
    {
        this.runningVersion = runningVersion;
        PropertyIterator iter = propertyIterator();
        while (iter.hasNext())
        {
            iter.next().setRunningVersion(runningVersion);
        }
    }

    public void recoverRunningVersion()
    {
        PropertyIterator iter = propertyIterator();
        while (iter.hasNext())
        {
            JMeterProperty prop = iter.next();
            if (prop.isTemporary(this))
            {
                iter.remove();
                prop.clearTemporary(this);
            }
            else
            {
                prop.recoverRunningVersion(this);
            }
        }
    }

	protected Sampler nextIsNull() throws NextIsNullException {
		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17343.java