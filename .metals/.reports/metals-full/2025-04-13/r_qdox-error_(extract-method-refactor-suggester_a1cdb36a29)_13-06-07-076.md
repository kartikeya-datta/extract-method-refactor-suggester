error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/484.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/484.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/484.java
text:
```scala
L@@OG.info("SASL ANONYMOUS client transport is being established");

package backtype.storm.security.auth;

import backtype.storm.utils.Utils;

import java.security.Principal;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.security.auth.Subject;
import javax.security.auth.kerberos.KerberosTicket;
import javax.security.auth.login.Configuration;
import javax.security.sasl.Sasl;

import org.apache.thrift7.protocol.TBinaryProtocol;
import org.apache.thrift7.protocol.TProtocol;
import org.apache.thrift7.transport.TSocket;
import org.apache.thrift7.transport.TTransport;
import org.apache.thrift7.transport.TSaslClientTransport;
import org.apache.zookeeper.Login;
import org.apache.zookeeper.server.auth.KerberosName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ThriftClient {	
	private static final Logger LOG = LoggerFactory.getLogger(ThriftClient.class);
	private TTransport _transport;
	protected TProtocol _protocol;

	static {
		java.security.Security.addProvider(new AnonymousAuthenticationProvider());
	}

	public ThriftClient(String host, int port, String default_service_name) {
		this(host, port, default_service_name, null);
	}

	public ThriftClient(String host, int port, String default_service_name, Integer timeout) {
		try {
			if(host==null) {
				throw new IllegalArgumentException("host is not set");
			}
			if(port<=0) {
				throw new IllegalArgumentException("invalid port: "+port);
			}

			TSocket socket = new TSocket(host, port);
			if(timeout!=null) {
				socket.setTimeout(timeout);
			}
			final TTransport underlyingTransport = socket;

			String loginConfigurationFile = System.getProperty("java.security.auth.login.config");
			if ((loginConfigurationFile==null) || (loginConfigurationFile.length()==0)) {
				//apply Storm configuration for JAAS login 
				Map conf = Utils.readStormConfig();
				loginConfigurationFile = (String)conf.get("java.security.auth.login.config");
			}
			if ((loginConfigurationFile==null) || (loginConfigurationFile.length()==0)) { //ANONYMOUS
				LOG.debug("SASL ANONYMOUS client transport is being established");
				_transport = new TSaslClientTransport(AuthUtils.ANONYMOUS, 
						null, 
						AuthUtils.SERVICE,
						host,
						null,
						null, 
						underlyingTransport);
				_transport.open();
			} else {
				LOG.debug("Use jaas login config:"+loginConfigurationFile);
				System.setProperty("java.security.auth.login.config", loginConfigurationFile);
				Configuration auth_conf = Configuration.getConfiguration();

				//login our user
				SaslClientCallbackHandler callback_handler = new SaslClientCallbackHandler(auth_conf);
				Login login = new Login(AuthUtils.LoginContextClient, callback_handler);

				final Subject subject = login.getSubject();
				if (subject.getPrivateCredentials(KerberosTicket.class).isEmpty()) { //DIGEST-MD5
					LOG.debug("SASL DIGEST-MD5 client transport is being established");
					_transport = new TSaslClientTransport(AuthUtils.DIGEST, 
							null, 
							AuthUtils.SERVICE, 
							host,
							null,
							callback_handler, 
							underlyingTransport);
					_transport.open();
				} else { //GSSAPI
					final String principal = getPrincipal(subject); 
					String serviceName = AuthUtils.get(auth_conf, AuthUtils.LoginContextClient, "serviceName");
					if (serviceName == null) {
						serviceName = default_service_name; 
					}
					Map<String, String> props = new TreeMap<String,String>();
					props.put(Sasl.QOP, "auth");
					props.put(Sasl.SERVER_AUTH, "false");
					LOG.debug("SASL GSSAPI client transport is being established");
					_transport = new TSaslClientTransport(AuthUtils.KERBEROS, 
							principal, 
							serviceName, 
							host,
							props,
							null, 
							underlyingTransport);

					//open Sasl transport with the login credential
					try {
						Subject.doAs(subject,
								new PrivilegedExceptionAction<Void>() {
							public Void run() {
								try {
									LOG.debug("do as:"+ principal);
									_transport.open();
								}
								catch (Exception e) {
									LOG.error("Nimbus client failed to open SaslClientTransport to interact with a server during session initiation: " + e);
									e.printStackTrace();
								}
								return null;
							}
						});
					} catch (PrivilegedActionException e) {
						LOG.error("Nimbus client experienced a PrivilegedActionException exception while creating a TSaslClientTransport using a JAAS principal context:" + e);
						e.printStackTrace();
					}
				} 

			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
			throw new RuntimeException(e);
		}

		_protocol = null;
		if (_transport != null)
			_protocol = new  TBinaryProtocol(_transport);
	}

	private String getPrincipal(Subject subject) {
		Set<Principal> principals = (Set<Principal>)subject.getPrincipals();
		if (principals==null || principals.size()<1) {
			LOG.info("No principal found in login subject");
			return null;
		}
		return ((Principal)(principals.toArray()[0])).getName();
	}

	public TTransport transport() {
		return _transport;
	}

	public void close() {
		_transport.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/484.java