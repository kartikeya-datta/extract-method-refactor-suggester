error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1673.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1673.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1673.java
text:
```scala
protected M@@odule getInstanceImpl(String moduleDomain,



package org.tigris.scarab.om;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.Serializable;

import org.apache.torque.TorqueException;
import org.apache.torque.om.Persistent;
import org.apache.torque.om.ObjectKey;
import org.apache.torque.util.Criteria;
import org.apache.torque.manager.CacheListener;

/** 
 * This class manages Module objects.  
 * The skeleton for this class was autogenerated by Torque  
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class ModuleManager
    extends BaseModuleManager
    implements CacheListener
{
    /**
     * Creates a new <code>ModuleManager</code> instance.
     *
     * @exception TorqueException if an error occurs
     */
    public ModuleManager()
        throws TorqueException
    {
        super();
        setRegion(getClassName().replace('.', '_'));
    }

    protected Module getInstanceImpl()
    {
        return new ScarabModule();
    }

    /**
     * Get an instance of a Module by realName and code. If the result
     * != 1, then throw a TorqueException.
     *
     * FIXME: Use caching? John?
     */
    public static Module getInstance(String moduleDomain, 
                                     String moduleRealName, 
                                     String moduleCode)
        throws TorqueException
    {
        return getManager().getInstanceImpl(moduleDomain, moduleRealName, 
                                            moduleCode);
    }

    /**
     * Get an instance of a Module by realName and code. If the result
     * != 1, then throw a TorqueException.
     *
     * FIXME: Use caching? John?
     */
    public Module getInstanceImpl(String moduleDomain, 
                                  String moduleRealName, 
                                  String moduleCode)
        throws TorqueException
    {
        Criteria crit = new Criteria();
        crit.add(ScarabModulePeer.MODULE_NAME, moduleDomain);
        crit.add(ScarabModulePeer.MODULE_NAME, moduleRealName);
        crit.add(ScarabModulePeer.MODULE_CODE, moduleCode);
        List result = ScarabModulePeer.doSelect(crit);
        if (result.size() != 1)
        {
            throw new TorqueException ("Selected: " + result.size() + 
                " rows. Expected 1.");
        }
        return (Module) result.get(0);
    }

    /**
     * Create a list of Modules from the given list of issues.  Each
     * Module in the list of issues will only occur once in the list of 
     * Modules.
     *
     * @param issues a <code>List</code> value
     * @return a <code>List</code> value
     * @exception TorqueException if an error occurs
     */
    public static List getInstancesFromIssueList(List issues)
        throws TorqueException
    {
        if (issues == null) 
        {
            throw new IllegalArgumentException("Null issue list is not allowed.");
        }        

        List modules = new ArrayList();
        Iterator i = issues.iterator();
        if (i.hasNext()) 
        {
            Issue issue = (Issue)i.next();
            if (issue != null)
            {
                Module module = issue.getModule();
                if (module != null && !modules.contains(module)) 
                {
                    modules.add(module);
                }
            }
            else
            {
                throw new TorqueException("Null issue in list is not allowed.");
            }
        }
        return modules;
    }


    /**
     * Notify other managers with relevant CacheEvents.
     */
    protected void registerAsListener()
    {
        RModuleIssueTypeManager.addCacheListener(this);
        RModuleAttributeManager.addCacheListener(this);
        AttributeGroupManager.addCacheListener(this);
        RModuleOptionManager.addCacheListener(this);
        AttributeManager.addCacheListener(this);
        AttributeOptionManager.addCacheListener(this);
    }

    // -------------------------------------------------------------------
    // CacheListener implementation

    public void addedObject(Persistent om)
    {
        if (om instanceof RModuleAttribute)
        {
            RModuleAttribute castom = (RModuleAttribute)om;
            ObjectKey key = castom.getModuleId();
            Serializable obj = (Serializable)cacheGet(key);
            if (obj != null) 
            {
                getMethodResult().removeAll(obj, 
                    AbstractScarabModule.GET_R_MODULE_ATTRIBUTES);
            }
        }
        else if (om instanceof RModuleOption)
        {
            RModuleOption castom = (RModuleOption)om;
            ObjectKey key = castom.getModuleId();
            Serializable obj = (Serializable)cacheGet(key);
            if (obj != null) 
            {
                getMethodResult().removeAll(obj, 
                    AbstractScarabModule.GET_LEAF_R_MODULE_OPTIONS);
            }
        }
        else if (om instanceof RModuleIssueType) 
        {
            RModuleIssueType castom = (RModuleIssueType)om;
            ObjectKey key = castom.getModuleId();
            Serializable obj = (Serializable)cacheGet(key);
            if (obj != null) 
            {
                getMethodResult().remove(obj, 
                    AbstractScarabModule.GET_NAV_ISSUE_TYPES);
            }
        }
        else if (om instanceof AttributeGroup)
        {
            AttributeGroup castom = (AttributeGroup)om;
            ObjectKey key = castom.getModuleId();
            Serializable obj = (Serializable)cacheGet(key);
            if (obj != null) 
            {
                getMethodResult().removeAll(obj, 
                    AbstractScarabModule.GET_ATTRIBUTE_GROUPS);
            }
        }
        else if (om instanceof Attribute) 
        {
            getMethodResult().clear();
        }
        else if (om instanceof AttributeOption) 
        {
            getMethodResult().clear();
        }
    }

    public void refreshedObject(Persistent om)
    {
        addedObject(om);
    }

    /** fields which interest us with respect to cache events */
    public List getInterestedFields()
    {
        List interestedCacheFields = new LinkedList();
        interestedCacheFields.add(RModuleOptionPeer.MODULE_ID);
        interestedCacheFields.add(RModuleAttributePeer.MODULE_ID);
        interestedCacheFields.add(RModuleIssueTypePeer.MODULE_ID);
        interestedCacheFields.add(AttributeGroupPeer.MODULE_ID);
        interestedCacheFields.add(AttributePeer.ATTRIBUTE_ID);
        interestedCacheFields.add(AttributeOptionPeer.OPTION_ID);
        return interestedCacheFields;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1673.java