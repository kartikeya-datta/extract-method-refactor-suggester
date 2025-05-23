error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15167.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15167.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,19]

error in qdox parser
file content:
```java
offset: 19
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15167.java
text:
```scala
transient private T@@estElement searchStart = null;

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

package org.apache.jmeter.control;

import java.io.Serializable;

import org.apache.jmeter.junit.JMeterTestCase;
import org.apache.jmeter.junit.stubs.TestSampler;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.IntegerProperty;

/**
 * @author    Michael Stover
 * Created     March 13, 2001
 * @version   $Revision$ Last updated: $Date$
 */
public class InterleaveControl extends GenericController implements Serializable
{
    private static final String STYLE = "InterleaveControl.style";
    public static final int IGNORE_SUB_CONTROLLERS = 0;
    public static final int USE_SUB_CONTROLLERS = 1;
    private boolean skipNext;
    private TestElement searchStart = null;
    private boolean currentReturnedAtLeastOne;
    private boolean stillSame = true;

    /****************************************
     * Constructor for the InterleaveControl object
     ***************************************/
    public InterleaveControl()
    {}

    /* (non-Javadoc)
     * @see org.apache.jmeter.control.GenericController#reInitialize()
     */
    public void reInitialize()
    {
        setFirst(true);
        currentReturnedAtLeastOne = false;
        searchStart = null;
        stillSame = true;
        skipNext = false;
        incrementIterCount();
    }

    public void setStyle(int style)
    {
        setProperty(new IntegerProperty(STYLE, style));
    }

    public int getStyle()
    {
        return getPropertyAsInt(STYLE);
    }
    
    /* (non-Javadoc)
     * @see org.apache.jmeter.control.Controller#next()
     */
    public Sampler next()
    {
        if(isSkipNext())
        {
            reInitialize();
            return null;
        }
        return super.next();
    }

    /* (non-Javadoc)
     * @see GenericController#nextIsAController(Controller)
     */
    protected Sampler nextIsAController(Controller controller)
        throws NextIsNullException
    {
        Sampler sampler = controller.next();
        if (sampler == null)
        {
            currentReturnedNull(controller);
            return next();
        }
        else
        {
            currentReturnedAtLeastOne = true;
            if (getStyle() == IGNORE_SUB_CONTROLLERS)
            {
                incrementCurrent();
                skipNext = true;
            }
            else
            {
                searchStart = null;
            }
            return sampler;
        }
    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.control.GenericController#nextIsASampler(Sampler)
     */
    protected Sampler nextIsASampler(Sampler element) throws NextIsNullException
    {
        skipNext = true;
        incrementCurrent();
        return element;
    }

    /**
     * If the current is null, reset and continue searching.  The 
     * searchStart attribute will break us off when we start a repeat.
     * 
     * @see org.apache.jmeter.testelement.AbstractTestElement#nextIsNull()
     */
    protected Sampler nextIsNull()
    {
        resetCurrent();
        return next();
    }

    /* (non-Javadoc)
     * @see GenericController#setCurrentElement(TestElement)
     */
    protected void setCurrentElement(TestElement currentElement)
        throws NextIsNullException
    {
        // Set the position when next is first called, and don't overwrite
        // until reInitialize is called.
        if (searchStart == null)
        {
            searchStart = currentElement;
        }
        else if (searchStart == currentElement && !stillSame)
        {
            // We've gone through the whole list and are now back at the start
            // point of our search.
            reInitialize();
            throw new NextIsNullException();
        }
    }
    
    /* (non-Javadoc)
     * @see GenericController#currentReturnedNull(Controller)
     */
    protected void currentReturnedNull(Controller c)
    {
        if (c.isDone())
        {
            removeCurrentElement();
        }
        else if(getStyle() == USE_SUB_CONTROLLERS)
        {
            incrementCurrent();
        }
    }

    /**
     * @return skipNext
     */
    protected boolean isSkipNext()
    {
        return skipNext;
    }

    /**
     * @param skipNext
     */
    protected void setSkipNext(boolean skipNext)
    {
        this.skipNext = skipNext;
    }
    
    
	/* (non-Javadoc)
	 * @see org.apache.jmeter.control.GenericController#incrementCurrent()
	 */
	protected void incrementCurrent()
	{
		if (currentReturnedAtLeastOne)
		{
			skipNext = true;
		}
		stillSame = false;
		super.incrementCurrent();
	}

/////////////// Start of Test Code ////////////////////////////////

    public static class Test extends JMeterTestCase
    {
        public Test(String name)
        {
            super(name);
        }

        public void testProcessing() throws Exception
        {
            testLog.debug("Testing Interleave Controller 1");
            GenericController controller = new GenericController();
            InterleaveControl sub_1 = new InterleaveControl();
            sub_1.setStyle(IGNORE_SUB_CONTROLLERS);
            sub_1.addTestElement(new TestSampler("one"));
            sub_1.addTestElement(new TestSampler("two"));
            controller.addTestElement(sub_1);
            controller.addTestElement(new TestSampler("three"));
            LoopController sub_2 = new LoopController();
            sub_2.setLoops(3);
            GenericController sub_3 = new GenericController();
            sub_2.addTestElement(new TestSampler("four"));
            sub_3.addTestElement(new TestSampler("five"));
            sub_3.addTestElement(new TestSampler("six"));
            sub_2.addTestElement(sub_3);
            sub_2.addTestElement(new TestSampler("seven"));
            controller.addTestElement(sub_2);
            String[] interleaveOrder = new String[] { "one", "two" };
            String[] order =
                new String[] {
                    "dummy",
                    "three",
                    "four",
                    "five",
                    "six",
                    "seven",
                    "four",
                    "five",
                    "six",
                    "seven",
                    "four",
                    "five",
                    "six",
                    "seven" };
            int counter = 14;
            controller.initialize();
            for (int i = 0; i < 4; i++)
            {
                assertEquals(14, counter);
                counter = 0;
                TestElement sampler = null;
                while ((sampler = controller.next()) != null)
                {
                    if (counter == 0)
                    {
                        assertEquals(
                            interleaveOrder[i % 2],
                            sampler.getPropertyAsString(TestElement.NAME));
                    }
                    else
                    {
                        assertEquals(
                            order[counter],
                            sampler.getPropertyAsString(TestElement.NAME));
                    }
                    counter++;
                }
            }
        }
        
        public void testProcessing6() throws Exception
        {
            testLog.debug("Testing Interleave Controller 6");
            GenericController controller = new GenericController();
            InterleaveControl sub_1 = new InterleaveControl();
            controller.addTestElement(new TestSampler("one"));
            sub_1.setStyle(IGNORE_SUB_CONTROLLERS);
            controller.addTestElement(sub_1);
            LoopController sub_2 = new LoopController();
            sub_1.addTestElement(sub_2);
            sub_2.setLoops(3);
            int counter = 1;
            controller.initialize();
            for (int i = 0; i < 4; i++)
            {
                assertEquals(1, counter);
                counter = 0;
                TestElement sampler = null;
                while ((sampler = controller.next()) != null)
                {
                    assertEquals(
                        "one",
                        sampler.getPropertyAsString(TestElement.NAME));
                    counter++;
                }
            }
        }

        public void testProcessing2() throws Exception
        {
            testLog.debug("Testing Interleave Controller 2");
            GenericController controller = new GenericController();
            InterleaveControl sub_1 = new InterleaveControl();
            sub_1.setStyle(IGNORE_SUB_CONTROLLERS);
            sub_1.addTestElement(new TestSampler("one"));
            sub_1.addTestElement(new TestSampler("two"));
            controller.addTestElement(sub_1);
            controller.addTestElement(new TestSampler("three"));
            LoopController sub_2 = new LoopController();
            sub_2.setLoops(3);
            GenericController sub_3 = new GenericController();
            sub_2.addTestElement(new TestSampler("four"));
            sub_3.addTestElement(new TestSampler("five"));
            sub_3.addTestElement(new TestSampler("six"));
            sub_2.addTestElement(sub_3);
            sub_2.addTestElement(new TestSampler("seven"));
            sub_1.addTestElement(sub_2);
            String[] order =
                new String[] {
                    "one",
                    "three",
                    "two",
                    "three",
                    "four",
                    "three",
                    "one",
                    "three",
                    "two",
                    "three",
                    "five",
                    "three",
                    "one",
                    "three",
                    "two",
                    "three",
                    "six",
                    "three",
                    "one",
                    "three" };
            int counter = 0;
            controller.initialize();
            while (counter < order.length)
            {
                TestElement sampler = null;
                while ((sampler = controller.next()) != null)
                {
                    assertEquals(
                        "failed on " + counter,
                        order[counter],
                        sampler.getPropertyAsString(TestElement.NAME));
                    counter++;
                }
            }
        }

        public void testProcessing3() throws Exception
        {
            testLog.debug("Testing Interleave Controller 3");
            GenericController controller = new GenericController();
            InterleaveControl sub_1 = new InterleaveControl();
            sub_1.setStyle(USE_SUB_CONTROLLERS);
            sub_1.addTestElement(new TestSampler("one"));
            sub_1.addTestElement(new TestSampler("two"));
            controller.addTestElement(sub_1);
            controller.addTestElement(new TestSampler("three"));
            LoopController sub_2 = new LoopController();
            sub_2.setLoops(3);
            GenericController sub_3 = new GenericController();
            sub_2.addTestElement(new TestSampler("four"));
            sub_3.addTestElement(new TestSampler("five"));
            sub_3.addTestElement(new TestSampler("six"));
            sub_2.addTestElement(sub_3);
            sub_2.addTestElement(new TestSampler("seven"));
            sub_1.addTestElement(sub_2);
            String[] order =
                new String[] {
                    "one",
                    "three",
                    "two",
                    "three",
                    "four",
                    "five",
                    "six",
                    "seven",
                    "four",
                    "five",
                    "six",
                    "seven",
                    "four",
                    "five",
                    "six",
                    "seven",
                    "three",
                    "one",
                    "three",
                    "two",
                    "three" };
            int counter = 0;
            controller.initialize();
            while (counter < order.length)
            {
                TestElement sampler = null;
                while ((sampler = controller.next()) != null)
                {
                    assertEquals(
                        "failed on" + counter,
                        order[counter],
                        sampler.getPropertyAsString(TestElement.NAME));
                    counter++;
                }
            }
        }

        public void testProcessing4() throws Exception
        {
            testLog.debug("Testing Interleave Controller 4");
            GenericController controller = new GenericController();
            InterleaveControl sub_1 = new InterleaveControl();
            sub_1.setStyle(IGNORE_SUB_CONTROLLERS);
            controller.addTestElement(sub_1);
            GenericController sub_2 = new GenericController();
            sub_2.addTestElement(new TestSampler("one"));
            sub_2.addTestElement(new TestSampler("two"));
            sub_1.addTestElement(sub_2);
            GenericController sub_3 = new GenericController();
            sub_3.addTestElement(new TestSampler("three"));
            sub_3.addTestElement(new TestSampler("four"));
            sub_1.addTestElement(sub_3);
            String[] order = new String[] { "one", "three", "two", "four" };
            int counter = 0;
            controller.initialize();
            while (counter < order.length)
            {
                TestElement sampler = null;
                while ((sampler = controller.next()) != null)
                {
                    assertEquals(
                        "failed on" + counter,
                        order[counter],
                        sampler.getPropertyAsString(TestElement.NAME));
                    counter++;
                }
            }
        }

        public void testProcessing5() throws Exception
        {
            testLog.debug("Testing Interleave Controller 5");
            GenericController controller = new GenericController();
            InterleaveControl sub_1 = new InterleaveControl();
            sub_1.setStyle(USE_SUB_CONTROLLERS);
            controller.addTestElement(sub_1);
            GenericController sub_2 = new GenericController();
            sub_2.addTestElement(new TestSampler("one"));
            sub_2.addTestElement(new TestSampler("two"));
            sub_1.addTestElement(sub_2);
            GenericController sub_3 = new GenericController();
            sub_3.addTestElement(new TestSampler("three"));
            sub_3.addTestElement(new TestSampler("four"));
            sub_1.addTestElement(sub_3);
            String[] order = new String[] { "one", "two", "three", "four" };
            int counter = 0;
            controller.initialize();
            while (counter < order.length)
            {
                TestElement sampler = null;
                while ((sampler = controller.next()) != null)
                {
                    assertEquals(
                        "failed on" + counter,
                        order[counter],
                        sampler.getPropertyAsString(TestElement.NAME));
                    counter++;
                }
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15167.java