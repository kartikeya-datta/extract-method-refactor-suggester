error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3950.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3950.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3950.java
text:
```scala
r@@eturn name + "=" + getStringValue();

// $Header$
/*
 * Copyright 2001-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
*/

// $Header$
/*
 * Copyright 2003-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
*/

 package org.apache.jmeter.testelement.property;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * @version $Revision$
 */
public abstract class AbstractProperty implements JMeterProperty
{
    protected static final Logger log = LoggingManager.getLoggerForClass();
    private String name;
    private boolean runningVersion = false;

    public AbstractProperty(String name)
    {
        if (name == null) 
            throw new IllegalArgumentException("Name cannot be null");
        this.name = name;
    }

    public AbstractProperty()
    {
        this("");
    }

    protected boolean isEqualType(JMeterProperty prop)
    {
        if (this.getClass().equals(prop.getClass()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /* (non-Javadoc)
     * @see JMeterProperty#isRunningVersion()
     */
    public boolean isRunningVersion()
    {
        return runningVersion;
    }

    /* (non-Javadoc)
     * @see JMeterProperty#getName()
     */
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        if (name == null) 
            throw new IllegalArgumentException("Name cannot be null");
        this.name = name;
    }

    /* (non-Javadoc)
     * @see JMeterProperty#setRunningVersion(boolean)
     */
    public void setRunningVersion(boolean runningVersion)
    {
        this.runningVersion = runningVersion;
    }
    
    protected PropertyIterator getIterator(Collection values)
    {
        return new PropertyIteratorImpl(values);
    }

    /* (non-Javadoc)
     * @see Object#clone()
     */
    public Object clone()
    {
        try
        {
            AbstractProperty prop =
                (AbstractProperty) this.getClass().newInstance();
            prop.name = name;
            prop.runningVersion = runningVersion;
            return prop;
        }
        catch (InstantiationException e)
        {
            return null;
        }
        catch (IllegalAccessException e)
        {
            return null;
        }
    }

    /**
     * Returns 0 if string is invalid or null.
     * @see JMeterProperty#getIntValue()
     */
    public int getIntValue()
    {
        String val = getStringValue();
        if (val == null)
        {
            return 0;
        }
        try
        {
            return Integer.parseInt(val);
        }
        catch (NumberFormatException e)
        {
            return 0;
        }
    }

    /**
     * Returns 0 if string is invalid or null.
     * @see JMeterProperty#getLongValue()
     */
    public long getLongValue()
    {
        String val = getStringValue();
        if (val == null)
        {
            return 0;
        }
        try
        {
            return Long.parseLong(val);
        }
        catch (NumberFormatException e)
        {
            return 0;
        }
    }

    /**
     * Returns 0 if string is invalid or null.
     * @see JMeterProperty#getDoubleValue()
     */
    public double getDoubleValue()
    {
        String val = getStringValue();
        if (val == null)
        {
            return 0;
        }
        try
        {
            return Double.parseDouble(val);
        }
        catch (NumberFormatException e)
        {
            log.error("Tried to parse a non-number string to an integer", e);
            return 0;
        }
    }

    /**
     * Returns 0 if string is invalid or null.
     * @see JMeterProperty#getFloatValue()
     */
    public float getFloatValue()
    {
        String val = getStringValue();
        if (val == null)
        {
            return 0;
        }
        try
        {
            return Float.parseFloat(val);
        }
        catch (NumberFormatException e)
        {
            log.error("Tried to parse a non-number string to an integer", e);
            return 0;
        }
    }

    /**
     * Returns false if string is invalid or null.
     * @see JMeterProperty#getBooleanValue()
     */
    public boolean getBooleanValue()
    {
        String val = getStringValue();
        if (val == null)
        {
            return false;
        }
        return Boolean.valueOf(val).booleanValue();
    }

    /**
     * Determines if the two objects are equal by comparing names and values
     *
     * @return true if names are equal and values are equal (or both null)
     */
    public boolean equals(Object o)
    {
    	if (!(o instanceof JMeterProperty)) return false;
    	if (this == o) return true;
    	JMeterProperty jpo = (JMeterProperty) o;
    	if (!name.equals(jpo.getName())) return false; 
    	String s1 = getStringValue(); 
    	String s2 = jpo.getStringValue();
		return s1 == null ? s2 == null : s1.equals(s2);
    }
    
	public int hashCode()
	{
		int result = 17;
		result = result * 37 + name.hashCode();// name cannot be null 
		String s = getStringValue();
		result = result * 37 + (s == null ? 0 : s.hashCode()); 
		return result;
	}

    /**
     * Compares two JMeterProperty object values.
     * N.B. Does not compare names
     * 
     * @param arg0 JMeterProperty to compare against
     * @return 0 if equal values or both values null;
     * -1 otherwise
     * @seeComparable#compareTo(Object)
     */
    public int compareTo(Object arg0)
    {
        if (arg0 instanceof JMeterProperty)
        {
            // We don't expect the string values to ever be null.  But (as in
            // bug 19499) sometimes they are.  So have null compare less than
            // any other value.  Log a warning so we can try to find the root
            // cause of the null value.
            String val = getStringValue();
            String val2 = ((JMeterProperty)arg0).getStringValue();
            if (val == null)
            {
                log.warn(
                    "Warning: Unexpected null value for property: " + name);
                
                if (val2 == null)
                {
                    // Two null values -- return equal
                    return 0;
                }
                else
                {
                    return -1;
                }
            }
            return val.compareTo(val2);
        }
        else
        {
            return -1;
        }
    }

    /**
     * Get the property type for this property.  Used to convert raw values into
     * JMeterProperties.
     */
    protected Class getPropertyType()
    {
        return getClass();
    }

    protected JMeterProperty getBlankProperty()
    {
        try
        {
            JMeterProperty prop =
                (JMeterProperty) getPropertyType().newInstance();
            if (prop instanceof NullProperty)
            {
                return new StringProperty();
            }
            return prop;
        }
        catch (Exception e)
        {
            return new StringProperty();
        }
    }

    protected Collection normalizeList(Collection coll)
    {
        Iterator iter = coll.iterator();
        Collection newColl = null;
        while (iter.hasNext())
        {
            Object item = iter.next();
            if (newColl == null)
            {
                try
                {
                    newColl = (Collection) coll.getClass().newInstance();
                }
                catch (Exception e)
                {
                    log.error("Bad collection", e);
                }
            }
            newColl.add(convertObject(item));
        }
        if (newColl != null)
        {
            return newColl;
        }
        else
        {
            return coll;
        }
    }

    /**
     * Given a Map, it converts the Map into a collection of JMeterProperty
     * objects, appropriate for a MapProperty object.
     */
    protected Map normalizeMap(Map coll)
    {
        Iterator iter = coll.keySet().iterator();
        Map newColl = null;
        while (iter.hasNext())
        {
            Object item = iter.next();
            Object prop = coll.get(item);
            if (newColl == null)
            {
                try
                {
                    newColl = (Map) coll.getClass().newInstance();
                }
                catch (Exception e)
                {
                    log.error("Bad collection", e);
                }
            }
            newColl.put(item, convertObject(prop));
        }
        if (newColl != null)
        {
            return newColl;
        }
        else
        {
            return coll;
        }
    }

    protected JMeterProperty convertObject(Object item)
    {
        if (item instanceof JMeterProperty)
        {
            return (JMeterProperty) item;
        }
        else if (item instanceof TestElement)
        {
            return new TestElementProperty(
                ((TestElement) item).getPropertyAsString(TestElement.NAME),
                (TestElement) item);
        }
        else if (item instanceof Collection)
        {
            return new CollectionProperty(
                "" + item.hashCode(),
                (Collection) item);
        }
        else if (item instanceof Map)
        {
            return new MapProperty("" + item.hashCode(), (Map) item);
        }
        else
        {
            JMeterProperty prop = getBlankProperty();
            prop.setName(item.toString());
            prop.setObjectValue(item);
            return prop;
        }
    }

    /**
     * Provides a string representation of the property.
     * 
     * @return a string consisting of the name and its value
     */
    public String toString()
    {
        return getStringValue();
    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.testelement.property.JMeterProperty#mergeIn(org.apache.jmeter.testelement.property.JMeterProperty)
     */
    public void mergeIn(JMeterProperty prop)
    {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3950.java