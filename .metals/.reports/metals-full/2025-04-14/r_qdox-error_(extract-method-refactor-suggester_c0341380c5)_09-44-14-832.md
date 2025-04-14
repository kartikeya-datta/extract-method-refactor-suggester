error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3177.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3177.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3177.java
text:
```scala
i@@f (clazz.isInstance(this)) {

package org.eclipse.ecf.provider.xmpp.identity;

import java.net.URI;
import java.net.URISyntaxException;
import org.eclipse.ecf.core.identity.BaseID;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.presence.IChatID;
import org.jivesoftware.smack.XMPPConnection;

public class XMPPRoomID extends BaseID implements IChatID {
	
	private static final long serialVersionUID = -4843967090539640622L;
	public static final String DOMAIN_DEFAULT = "conference";
	public static final String NICKNAME = "nickname";
	
	URI uri = null;
	String longName = null;
	
	protected String fixHostname(String host, String domain) {
		if (domain == null) domain = DOMAIN_DEFAULT;
		return domain+"."+host;
	}
	
	protected String fixUsername(String connUsername) {
		int atIndex = connUsername.indexOf('@');
		if (atIndex == -1) return connUsername;
		else return connUsername.substring(0,atIndex);
	}
	protected String[] getRoomAndHost(String roomatconfhost) {
		int atIndex = roomatconfhost.indexOf('@');
		if (atIndex == -1) return new String[] { "", ""};
		String room = roomatconfhost.substring(0,atIndex);
		String fullHost = roomatconfhost.substring(atIndex+1);
		int dotIndex = fullHost.indexOf('.');
		String domain = fullHost.substring(0,dotIndex);
		String host = fullHost.substring(dotIndex+1);
		return new String [] { room, host, domain };
	}
	public XMPPRoomID(Namespace namespace, String username, String host, String domain, String groupname, String nickname) throws URISyntaxException {
		super(namespace);
		String hostname = fixHostname(host,domain);
		String query = NICKNAME+"="+((nickname==null)?username:nickname);
		uri = new URI(namespace.getScheme(),username,hostname,-1,"/"+groupname,query,null);
	}
	public XMPPRoomID(Namespace namespace, XMPPID userid, String domain, String groupname, String nickname) throws URISyntaxException {
		this(namespace,userid.getUsername(),userid.getHostname(),domain,groupname,nickname);
	}
	
	public XMPPRoomID(Namespace namespace, XMPPConnection conn, String roomid, String longName) throws URISyntaxException {
		super(namespace);
		String username = fixUsername(conn.getUser());
		String [] roomandhost = getRoomAndHost(roomid);
		String room = roomandhost[0];
		String hostname = fixHostname(roomandhost[1],roomandhost[2]);
		String query = NICKNAME+"="+username;
		this.uri = new URI(namespace.getScheme(),username,hostname,-1,"/"+room,query,null);
		this.longName = longName;
	}
	public XMPPRoomID(Namespace namespace, XMPPConnection conn, String roomid) throws URISyntaxException {
		this(namespace,conn,roomid,null);
	}
	protected int namespaceCompareTo(BaseID o) {
        return getName().compareTo(o.getName());
	}
	protected boolean namespaceEquals(BaseID o) {
		if (!(o instanceof XMPPRoomID)) {
			return false;
		}
		XMPPRoomID other = (XMPPRoomID) o;
		return uri.equals(other.uri);
	}
	protected String fixPath(String path) {
		while (path.startsWith("/")) {
			path = path.substring(1);
		}
		return path;
	}
	protected String namespaceGetName() {
		String path = uri.getPath();
		return fixPath(path);
	}
	protected int namespaceHashCode() {
		return uri.hashCode();
	}
	protected URI namespaceToURI() throws URISyntaxException {
		return uri;
	}
	public String getMucString() {
		String host = uri.getHost();
		String group = fixPath(uri.getPath());
		String res = group + "@" + host;
		return res;
	}
	public String getNickname() {
		String query = uri.getQuery();
		if (query == null) {
			return uri.getUserInfo();
		} else {
			return query.substring(query.indexOf('=')+1);
		}
	}
	public String getLongName() {
		return longName;
	}
	public String toString() {
		StringBuffer sb = new StringBuffer("XMPPRoomID[");
		sb.append(uri).append("]");
		return sb.toString();
	}
	public Object getAdapter(Class clazz) {
		if (clazz.equals(IChatID.class)) {
			return this;
		} else return super.getAdapter(clazz);
	}

	public String getUsername() {
		return getNickname();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3177.java