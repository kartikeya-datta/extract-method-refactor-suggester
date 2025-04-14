error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1458.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1458.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1458.java
text:
```scala
A@@ssert.notNull(sessionId, "sessionId must not be null");

/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.messaging.simp.handler;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.messaging.Message;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public class DefaultSubscriptionRegistry extends AbstractSubscriptionRegistry {

	private final DestinationCache destinationCache = new DestinationCache();

	private final SessionSubscriptionRegistry subscriptionRegistry = new SessionSubscriptionRegistry();

	private AntPathMatcher pathMatcher = new AntPathMatcher();


	/**
	 * @param pathMatcher the pathMatcher to set
	 */
	public void setPathMatcher(AntPathMatcher pathMatcher) {
		this.pathMatcher = pathMatcher;
	}

	public AntPathMatcher getPathMatcher() {
		return this.pathMatcher;
	}

	@Override
	protected void addSubscriptionInternal(String sessionId, String subsId, String destination, Message<?> message) {
		SessionSubscriptionInfo info = this.subscriptionRegistry.addSubscription(sessionId, subsId, destination);
		if (!this.pathMatcher.isPattern(destination)) {
			this.destinationCache.mapToDestination(destination, info);
		}
	}

	@Override
	protected void removeSubscriptionInternal(String sessionId, String subscriptionId, Message<?> message) {
		SessionSubscriptionInfo info = this.subscriptionRegistry.getSubscriptions(sessionId);
		if (info != null) {
			String destination = info.removeSubscription(subscriptionId);
			if (info.getSubscriptions(destination) == null) {
				this.destinationCache.unmapFromDestination(destination, info);
			}
		}
	}

	@Override
	public void unregisterAllSubscriptions(String sessionId) {
		SessionSubscriptionInfo info = this.subscriptionRegistry.removeSubscriptions(sessionId);
		if (info != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Unregistering subscriptions for sessionId=" + sessionId);
			}
			this.destinationCache.removeSessionSubscriptions(info);
		}
	}

	@Override
	protected MultiValueMap<String, String> findSubscriptionsInternal(String destination, Message<?> message) {
		MultiValueMap<String,String> result = this.destinationCache.getSubscriptions(destination);
		if (result.isEmpty()) {
			result = new LinkedMultiValueMap<String, String>();
			for (SessionSubscriptionInfo info : this.subscriptionRegistry.getAllSubscriptions()) {
				for (String destinationPattern : info.getDestinations()) {
					if (this.pathMatcher.match(destinationPattern, destination)) {
						for (String subscriptionId : info.getSubscriptions(destinationPattern)) {
							result.add(info.sessionId, subscriptionId);
						}
					}
				}
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return "[destinationCache=" + this.destinationCache + ", subscriptionRegistry="
				+ this.subscriptionRegistry + "]";
	}




	/**
	 * Provide direct lookup of session subscriptions by destination (for non-pattern destinations).
	 */
	private static class DestinationCache {

		// destination -> ..
		private final Map<String, Set<SessionSubscriptionInfo>> subscriptionsByDestination =
				new ConcurrentHashMap<String, Set<SessionSubscriptionInfo>>();

		private final Object monitor = new Object();


		public void mapToDestination(String destination, SessionSubscriptionInfo info) {
			synchronized(this.monitor) {
				Set<SessionSubscriptionInfo> registrations = this.subscriptionsByDestination.get(destination);
				if (registrations == null) {
					registrations = new CopyOnWriteArraySet<SessionSubscriptionInfo>();
					this.subscriptionsByDestination.put(destination, registrations);
				}
				registrations.add(info);
			}
		}

		public void unmapFromDestination(String destination, SessionSubscriptionInfo info) {
			synchronized(this.monitor) {
				Set<SessionSubscriptionInfo> infos = this.subscriptionsByDestination.get(destination);
				if (infos != null) {
					infos.remove(info);
					if (infos.isEmpty()) {
						this.subscriptionsByDestination.remove(destination);
					}
				}
			}
		}

		public void removeSessionSubscriptions(SessionSubscriptionInfo info) {
			for (String destination : info.getDestinations()) {
				unmapFromDestination(destination, info);
			}
		}

		public MultiValueMap<String, String> getSubscriptions(String destination) {
			MultiValueMap<String, String> result = new LinkedMultiValueMap<String, String>();
			Set<SessionSubscriptionInfo> infos = this.subscriptionsByDestination.get(destination);
			if (infos != null) {
				for (SessionSubscriptionInfo info : infos) {
					Set<String> subscriptions = info.getSubscriptions(destination);
					if (subscriptions != null) {
						for (String subscription : subscriptions) {
							result.add(info.getSessionId(), subscription);
						}
					}
				}
			}
			return result;
		}

		@Override
		public String toString() {
			return "[subscriptionsByDestination=" + this.subscriptionsByDestination + "]";
		}
	}

	/**
	 * Provide access to session subscriptions by sessionId.
	 */
	private static class SessionSubscriptionRegistry {

		private final ConcurrentMap<String, SessionSubscriptionInfo> sessions =
				new ConcurrentHashMap<String, SessionSubscriptionInfo>();


		public SessionSubscriptionInfo getSubscriptions(String sessionId) {
			return this.sessions.get(sessionId);
		}

		public Collection<SessionSubscriptionInfo> getAllSubscriptions() {
			return this.sessions.values();
		}

		public SessionSubscriptionInfo addSubscription(String sessionId, String subscriptionId, String destination) {
			SessionSubscriptionInfo info = this.sessions.get(sessionId);
			if (info == null) {
				info = new SessionSubscriptionInfo(sessionId);
				SessionSubscriptionInfo value = this.sessions.putIfAbsent(sessionId, info);
				if (value != null) {
					info = value;
				}
			}
			info.addSubscription(destination, subscriptionId);
			return info;
		}

		public SessionSubscriptionInfo removeSubscriptions(String sessionId) {
			return this.sessions.remove(sessionId);
		}

		@Override
		public String toString() {
			return "[sessions=" + sessions + "]";
		}
	}

	/**
	 * Hold subscriptions for a session.
	 */
	private static class SessionSubscriptionInfo {

		private final String sessionId;

		private final Map<String, Set<String>> subscriptions = new ConcurrentHashMap<String, Set<String>>(4);

		private final Object monitor = new Object();


		public SessionSubscriptionInfo(String sessionId) {
			Assert.notNull(sessionId, "sessionId is required");
			this.sessionId = sessionId;
		}

		public String getSessionId() {
			return this.sessionId;
		}

		public Set<String> getDestinations() {
			return this.subscriptions.keySet();
		}

		public Set<String> getSubscriptions(String destination) {
			return this.subscriptions.get(destination);
		}

		public void addSubscription(String destination, String subscriptionId) {
			Set<String> subs = this.subscriptions.get(destination);
			if (subs == null) {
				synchronized(this.monitor) {
					subs = this.subscriptions.get(destination);
					if (subs == null) {
						subs = new HashSet<String>(4);
						this.subscriptions.put(destination, subs);
					}
				}
			}
			subs.add(subscriptionId);
		}

		public String removeSubscription(String subscriptionId) {
			for (String destination : this.subscriptions.keySet()) {
				Set<String> subscriptionIds = this.subscriptions.get(destination);
				if (subscriptionIds.remove(subscriptionId)) {
					synchronized(this.monitor) {
						if (subscriptionIds.isEmpty()) {
							this.subscriptions.remove(destination);
						}
					}
					return destination;
				}
			}
			return null;
		}

		@Override
		public String toString() {
			return "[sessionId=" + this.sessionId + ", subscriptions=" + this.subscriptions + "]";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1458.java