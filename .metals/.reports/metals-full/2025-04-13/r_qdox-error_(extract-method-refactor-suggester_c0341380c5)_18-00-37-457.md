error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1808.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1808.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1808.java
text:
```scala
f@@eedSource = new QueryFeed(query,user1,scarabToolManager,scarabLink);

package org.tigris.scarab.screens;

/* ================================================================
 * Copyright (c) 2003 CollabNet.  All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if
 * any, must include the following acknowlegement: "This product includes
 * software developed by CollabNet <http://www.collab.net/>."
 * Alternately, this acknowlegement may appear in the software itself, if
 * and wherever such third-party acknowlegements normally appear.
 * 
 * 4. The hosted project names must not be used to endorse or promote
 * products derived from this software without prior written
 * permission. For written permission, please contact info@collab.net.
 * 
 * 5. Products derived from this software may not use the "Tigris" or 
 * "Scarab" names nor may "Tigris" or "Scarab" appear in their names without 
 * prior written permission of CollabNet.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL COLLABNET OR ITS CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ====================================================================
 * 
 * This software consists of voluntary contributions made by many
 * individuals on behalf of CollabNet.
 */

import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import org.apache.fulcrum.parser.ParameterParser;
import org.apache.torque.TorqueException;
import org.apache.torque.om.NumberKey;
import org.apache.turbine.RunData;
import org.apache.turbine.TemplateContext;
import org.apache.turbine.TemplateScreen;
import org.tigris.scarab.feeds.Feed;
import org.tigris.scarab.feeds.IssueFeed;
import org.tigris.scarab.feeds.QueryFeed;
import org.tigris.scarab.om.Issue;
import org.tigris.scarab.om.IssueManager;
import org.tigris.scarab.om.Query;
import org.tigris.scarab.om.QueryManager;
import org.tigris.scarab.om.ScarabUser;
import org.tigris.scarab.om.ScarabUserManager;
import org.tigris.scarab.tools.ScarabLocalizationTool;
import org.tigris.scarab.tools.ScarabRequestTool;
import org.tigris.scarab.tools.ScarabToolManager;
import org.tigris.scarab.util.Log;
import org.tigris.scarab.util.ScarabConstants;
import org.tigris.scarab.util.ScarabLink;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;

/**
 * <p>
 * Sends rss content directly to the output stream, setting the
 * <code>Content-Type</code> to application/xml; charset=UTF-8.
 * 
 * @author <a href="mailto:epugh@opensourceconnections.com">Eric Pugh </a>
 */
public class RSSDataExport extends TemplateScreen {
	private static final String DEFAULT_FEED_FORMAT = "atom_0.3";

	private static final String MIME_TYPE = "application/xml; charset=UTF-8";

	private static final String COULD_NOT_GENERATE_FEED_ERROR = "Could not generate feed";

	private static final String COULD_NOT_GENERATE_FEED_ERROR_DATABASE = "Could not retrive data successfully";

	public static final String QUERY_ID_KEY = "queryId";
	public static final String ISSUE_ID_KEY = "issueId";

	public static final String USER_ID_KEY = "userId";

	public static final String FEED_TYPE_KEY = "feedType";
	public static final String FEED_FORMAT_KEY = "type";

	/**
	 * Sets the <code>Content-Type</code> header for the response. Since this
	 * assumes we're writing the reponse ourself, indicates no target to render
	 * by setting it to <code>null</code>.
	 */
	public void doBuildTemplate(RunData data, TemplateContext context)
			throws Exception {
		super.doBuildTemplate(data, context);

		data.getResponse().setContentType(MIME_TYPE);

		// we will send the response, so no target to render
		data.setTarget(null);
		
		Writer writer = data.getResponse().getWriter();
		
		try {
            ParameterParser parser = data.getParameters();



            String feedType = parser.getString(FEED_TYPE_KEY);
            String feedFormat = parser.getString(FEED_FORMAT_KEY);

            ScarabLink scarabLink= getScarabLinkTool(context);
            ScarabRequestTool scarabRequestTool= getScarabRequestTool(context);
            

            
            Feed feedSource = null;
            ScarabToolManager scarabToolManager = new ScarabToolManager(getLocalizationTool(context));
            if (feedType.equals("QueryFeed")){
                
                long queryId = parser.getLong(QUERY_ID_KEY);            
                long userId = parser.getLong(USER_ID_KEY);                
                if(queryId==0){
                    throw new IllegalArgumentException("Query ID is missing.  Should be appended like: /queryId/xxx");
                }
                if(userId==0){
                    throw new IllegalArgumentException("User ID is missing.  Should be appended like: /userId/xxx");
                }            	
            	Query query = QueryManager.getInstance(new Long(queryId));
                ScarabUser user1 = ScarabUserManager.getInstance(new NumberKey(userId), false);
            	feedSource = new QueryFeed(query,user1,scarabRequestTool,scarabLink);
            }
            else if (feedType.equals("IssueFeed")){
                String issueId = parser.getString(ISSUE_ID_KEY);                
                if(issueId.equals("")){
                    throw new IllegalArgumentException("Issue ID is missing.  Should be appended like: /issueId/xxx");
                }            	
            	Issue issue = IssueManager.getIssueById(issueId);
            	feedSource = new IssueFeed(issue,scarabLink,scarabToolManager);
            }
            else {
            	throw new Exception("Couldn't find feed for type:" + feedType);
            }
            SyndFeed feed = feedSource.getFeed();

            feedFormat = (feedFormat!=null) ? feedFormat : DEFAULT_FEED_FORMAT;
            feed.setFeedType(feedFormat);

            
            SyndFeedOutput output = new SyndFeedOutput();
            output.output(feed,writer);
        }
        catch(IllegalArgumentException iae){
            String msg = COULD_NOT_GENERATE_FEED_ERROR + ": " + iae.getMessage();
            Log.get().error(msg,iae);
            data.getResponse().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,msg);
        }        
        catch(TorqueException te){
            String msg = COULD_NOT_GENERATE_FEED_ERROR_DATABASE;
            Log.get().error(msg,te);
            data.getResponse().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,msg);
        }
        catch (FeedException ex) {
            String msg = COULD_NOT_GENERATE_FEED_ERROR;
            Log.get().error(msg,ex);
            data.getResponse().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,msg);
        }
        catch (Exception e) {
            String msg = COULD_NOT_GENERATE_FEED_ERROR;
            Log.get().error(msg,e);
            data.getResponse().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,msg);
        }      		
	}
	
    /**
     * Helper method to retrieve the ScarabLocalizationTool from the Context
     */
    protected final ScarabLocalizationTool 
        getLocalizationTool(TemplateContext context)
    {
        return (ScarabLocalizationTool)
            context.get(ScarabConstants.LOCALIZATION_TOOL);
    }	
	
    /**
     * Helper method to retrieve the ScarabRequestTool from the Context
     */
    public ScarabRequestTool getScarabRequestTool(TemplateContext context)
    {
        return (ScarabRequestTool)context
            .get(ScarabConstants.SCARAB_REQUEST_TOOL);
    }
    
    /**
     * Helper method to retrieve the ScarabRequestTool from the Context
     */
    public ScarabLink getScarabLinkTool(TemplateContext context)
    {
        return (ScarabLink)context
            .get(ScarabConstants.SCARAB_LINK_TOOL);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1808.java