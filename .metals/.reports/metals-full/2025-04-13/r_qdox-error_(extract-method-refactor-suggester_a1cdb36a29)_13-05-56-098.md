error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4710.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4710.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4710.java
text:
```scala
S@@carabUser user = (ScarabUser)TurbineSecurity.getUser(username);

package org.tigris.scarab.pipeline;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.fulcrum.security.TurbineSecurity;
import org.apache.turbine.RunData;
import org.apache.turbine.Turbine;
import org.apache.turbine.TurbineException;
import org.apache.turbine.ValveContext;
import org.apache.turbine.pipeline.AbstractValve;
import org.tigris.scarab.om.ScarabUser;
import org.tigris.scarab.util.Log;

/*
 * This valve will try to automatically login an Anonymous user if there is no user authenticated.
 * The user and password will be set in scarab.user.anonymous and scarab.anonymous.password
 * If scarab.anonymous.username does not exists, the valve will just pass control to the following
 * through the pipeline.
 * 
 */
public class AnonymousLoginValve extends AbstractValve
{
    private final static Set nonAnonymousTargets = new HashSet();
    private String username = null;
    private String password = null;

    /**
     * Initilizes the templates that will not make an automatical
     * anonymous login.
     */
    public void initialize() throws Exception
    {
        Configuration conf = Turbine.getConfiguration();
        username = (String)conf.getProperty("scarab.anonymous.username");
        if (username != null) {
	        password = (String)conf.getProperty("scarab.anonymous.password");
	        nonAnonymousTargets.add("Index.vm");
	        nonAnonymousTargets.add("Logout.vm");
	        nonAnonymousTargets.add(conf.getProperty("template.login"));
	        nonAnonymousTargets.add(conf.getProperty("template.homepage"));
	        nonAnonymousTargets.add("Register.vm");
	        nonAnonymousTargets.add("ForgotPassword.vm");
        }
    }
    
    /* 
     * Invoked by the Turbine's pipeline, as defined in scarab-pipeline.xml
     * @see org.apache.turbine.pipeline.AbstractValve#invoke(org.apache.turbine.RunData, org.apache.turbine.ValveContext)
     */
    public void invoke(RunData data, ValveContext context) throws IOException, TurbineException
    {
        String target = data.getTarget();
        if (null != username && !nonAnonymousTargets.contains(target) && target.indexOf("help,") == -1)
        {
	        // If there's no user, we will login as Anonymous.
	        ScarabUser user = (ScarabUser)data.getUserFromSession();
	        if (null == user || user.getUserId() == null)
	            anonymousLogin(data, context);
        }
        context.invokeNext(data);        
    }

    /**
     * Logs the user defined as anonymous in the system.
     * @param data
     * @param context
     */
    private void anonymousLogin(RunData data, ValveContext context)
    {
        try
        {
            ScarabUser user = (ScarabUser)TurbineSecurity.getAuthenticatedUser(username, password);
            data.setUser(user);
            user.setHasLoggedIn(Boolean.TRUE);
            user.updateLastLogin();
            data.save();            
        }
        catch (Exception e)
        {
            Log.get().error("anonymousLogin failed to login anonymously: " + e.getMessage());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4710.java