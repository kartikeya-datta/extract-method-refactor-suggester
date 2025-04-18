error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5150.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5150.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5150.java
text:
```scala
g@@etHostConfiguration().setProxy(address.getHostName(), address.getPort());

/****************************************************************************
 * Copyright (c) 2008, 2009 Composent, Inc., IBM and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *    Henrich Kraemer - bug 263869, testHttpsReceiveFile fails using HTTP proxy
 *****************************************************************************/

package org.eclipse.ecf.provider.filetransfer.httpclient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Iterator;
import javax.security.auth.login.LoginException;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScheme;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.auth.CredentialsNotAvailableException;
import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.util.DateUtil;
import org.eclipse.core.runtime.Assert;
import org.eclipse.ecf.core.security.Callback;
import org.eclipse.ecf.core.security.CallbackHandler;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.ecf.core.security.NameCallback;
import org.eclipse.ecf.core.security.ObjectCallback;
import org.eclipse.ecf.core.security.UnsupportedCallbackException;
import org.eclipse.ecf.core.util.Proxy;
import org.eclipse.ecf.core.util.ProxyAddress;
import org.eclipse.ecf.core.util.Trace;
import org.eclipse.ecf.filetransfer.IRemoteFile;
import org.eclipse.ecf.filetransfer.IRemoteFileSystemListener;
import org.eclipse.ecf.filetransfer.IRemoteFileSystemRequest;
import org.eclipse.ecf.filetransfer.events.socket.ISocketEventSource;
import org.eclipse.ecf.filetransfer.identity.IFileID;
import org.eclipse.ecf.internal.provider.filetransfer.httpclient.Activator;
import org.eclipse.ecf.internal.provider.filetransfer.httpclient.ConnectingSocketMonitor;
import org.eclipse.ecf.internal.provider.filetransfer.httpclient.DebugOptions;
import org.eclipse.ecf.internal.provider.filetransfer.httpclient.Messages;
import org.eclipse.ecf.provider.filetransfer.browse.AbstractFileSystemBrowser;
import org.eclipse.ecf.provider.filetransfer.browse.URLRemoteFile;
import org.eclipse.ecf.provider.filetransfer.events.socket.SocketEventSource;
import org.eclipse.ecf.provider.filetransfer.httpclient.HttpClientRetrieveFileTransfer.HostConfigHelper;
import org.eclipse.ecf.provider.filetransfer.util.JREProxyHelper;
import org.eclipse.osgi.util.NLS;

/**
 *
 */
public class HttpClientFileSystemBrowser extends AbstractFileSystemBrowser {

	// changing to 2 minutes (120000) as per bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=266246
	protected static final int DEFAULT_CONNECTION_TIMEOUT = 120000;

	private static final String USERNAME_PREFIX = "Username:"; //$NON-NLS-1$

	private JREProxyHelper proxyHelper = null;

	private ConnectingSocketMonitor connectingSockets;

	protected String username = null;

	protected String password = null;

	protected HttpClient httpClient = null;

	protected volatile HeadMethod headMethod;

	protected HostConfigHelper hostConfigHelper;

	/**
	 * @param directoryOrFileID
	 * @param listener
	 */
	public HttpClientFileSystemBrowser(HttpClient httpClient, IFileID directoryOrFileID, IRemoteFileSystemListener listener, URL directoryOrFileURL, IConnectContext connectContext, Proxy proxy) {
		super(directoryOrFileID, listener, directoryOrFileURL, connectContext, proxy);
		Assert.isNotNull(httpClient);
		this.httpClient = httpClient;
		this.proxyHelper = new JREProxyHelper();
		this.connectingSockets = new ConnectingSocketMonitor(1);

	}

	class HttpClientRemoteFileSystemRequest extends RemoteFileSystemRequest {
		protected SocketEventSource socketEventSource;

		HttpClientRemoteFileSystemRequest() {
			this.socketEventSource = new SocketEventSource() {
				public Object getAdapter(Class adapter) {
					if (adapter == null) {
						return null;
					}
					if (adapter.isInstance(this)) {
						return this;
					}
					if (adapter.isInstance(HttpClientRemoteFileSystemRequest.this)) {
						return HttpClientRemoteFileSystemRequest.this;
					}
					return null;
				}
			};
		}

		public Object getAdapter(Class adapter) {
			if (adapter == null) {
				return null;
			}
			return socketEventSource.getAdapter(adapter);
		}

		public void cancel() {
			HttpClientFileSystemBrowser.this.cancel();
		}
	}

	protected IRemoteFileSystemRequest createRemoteFileSystemRequest() {
		return new HttpClientRemoteFileSystemRequest();
	}

	protected void cancel() {
		if (isCanceled()) {
			return; // break job cancel recursion
		}
		setCanceled(getException());
		super.cancel();
		if (headMethod != null) {
			if (!headMethod.isAborted()) {
				headMethod.abort();
			}
		}
		if (connectingSockets != null) {
			// this should unblock socket connect calls, if any
			for (Iterator iterator = connectingSockets.getConnectingSockets().iterator(); iterator.hasNext();) {
				Socket socket = (Socket) iterator.next();
				try {
					socket.close();
				} catch (IOException e) {
					Trace.catching(Activator.PLUGIN_ID, DebugOptions.EXCEPTIONS_CATCHING, this.getClass(), "cancel", e); //$NON-NLS-1$
				}
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.provider.filetransfer.browse.AbstractFileSystemBrowser#runRequest()
	 */
	protected void runRequest() throws Exception {
		Trace.entering(Activator.PLUGIN_ID, DebugOptions.METHODS_ENTERING, this.getClass(), "runRequest"); //$NON-NLS-1$
		setupProxies();
		// set timeout
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(DEFAULT_CONNECTION_TIMEOUT);
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
		String urlString = directoryOrFile.toString();
		// setup authentication
		setupAuthentication(urlString);
		// setup https host and port
		setupHostAndPort(urlString);

		headMethod = new HeadMethod(hostConfigHelper.getTargetRelativePath());
		headMethod.setFollowRedirects(true);
		// Define a CredentialsProvider - found that possibility while debugging in org.apache.commons.httpclient.HttpMethodDirector.processProxyAuthChallenge(HttpMethod)
		// Seems to be another way to select the credentials.
		headMethod.getParams().setParameter(CredentialsProvider.PROVIDER, new ECFCredentialsProvider());
		// set max-age for cache control to 0 for bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=249990
		headMethod.addRequestHeader("Cache-Control", "max-age=0"); //$NON-NLS-1$//$NON-NLS-2$

		long lastModified = 0;
		long fileLength = -1;
		connectingSockets.clear();
		try {
			Trace.trace(Activator.PLUGIN_ID, "browse=" + urlString); //$NON-NLS-1$

			int code = httpClient.executeMethod(getHostConfiguration(), headMethod);

			Trace.trace(Activator.PLUGIN_ID, "browse resp=" + code); //$NON-NLS-1$

			if (code == HttpURLConnection.HTTP_OK) {
				fileLength = headMethod.getResponseContentLength();
				lastModified = getLastModifiedTimeFromHeader();
			} else if (code == HttpURLConnection.HTTP_NOT_FOUND) {
				throw new FileNotFoundException(urlString);
			} else if (code == HttpURLConnection.HTTP_UNAUTHORIZED) {
				throw new LoginException(Messages.HttpClientRetrieveFileTransfer_Unauthorized);
			} else if (code == HttpURLConnection.HTTP_FORBIDDEN) {
				throw new LoginException("Forbidden"); //$NON-NLS-1$
			} else if (code == HttpURLConnection.HTTP_PROXY_AUTH) {
				throw new LoginException(Messages.HttpClientRetrieveFileTransfer_Proxy_Auth_Required);
			} else {
				throw new IOException(NLS.bind(Messages.HttpClientRetrieveFileTransfer_ERROR_GENERAL_RESPONSE_CODE, new Integer(code)));
			}
			remoteFiles = new IRemoteFile[1];
			remoteFiles[0] = new URLRemoteFile(lastModified, fileLength, fileID);
		} catch (Exception e) {
			Trace.throwing(Activator.PLUGIN_ID, DebugOptions.EXCEPTIONS_THROWING, this.getClass(), "runRequest", e); //$NON-NLS-1$
			throw e;
		} finally {
			headMethod.releaseConnection();
		}
	}

	private long getLastModifiedTimeFromHeader() throws IOException {
		Header lastModifiedHeader = headMethod.getResponseHeader("Last-Modified"); //$NON-NLS-1$
		if (lastModifiedHeader == null)
			return 0L;
		String lastModifiedString = lastModifiedHeader.getValue();
		long lastModified = 0;
		if (lastModifiedString != null) {
			try {
				lastModified = DateUtil.parseDate(lastModifiedString).getTime();
			} catch (Exception e) {
				throw new IOException(Messages.HttpClientRetrieveFileTransfer_EXCEPITION_INVALID_LAST_MODIFIED_FROM_SERVER);
			}
		}
		return lastModified;
	}

	final class ECFCredentialsProvider implements CredentialsProvider {

		/**
		 * @throws CredentialsNotAvailableException  
		 */
		public Credentials getCredentials(AuthScheme scheme, String host, int port, boolean isProxyAuthenticating) throws CredentialsNotAvailableException {
			if ("ntlm".equalsIgnoreCase(scheme.getSchemeName())) { //$NON-NLS-1$
				return HttpClientRetrieveFileTransfer.createNTLMCredentials(getProxy());
			}
			return null;
		}
	}

	Proxy getProxy() {
		return proxy;
	}

	protected void setupHostAndPort(String urlString) {
		getHostConfiguration(); // creates hostConfigHelper if needed
		hostConfigHelper.setTargetHostByURL(urlString);
	}

	protected Credentials getFileRequestCredentials() throws UnsupportedCallbackException, IOException {
		if (connectContext == null)
			return null;
		final CallbackHandler callbackHandler = connectContext.getCallbackHandler();
		if (callbackHandler == null)
			return null;
		final NameCallback usernameCallback = new NameCallback(USERNAME_PREFIX);
		final ObjectCallback passwordCallback = new ObjectCallback();
		callbackHandler.handle(new Callback[] {usernameCallback, passwordCallback});
		username = usernameCallback.getName();
		password = (String) passwordCallback.getObject();
		return new UsernamePasswordCredentials(username, password);
	}

	protected void setupAuthentication(String urlString) throws UnsupportedCallbackException, IOException {
		Credentials credentials = null;
		if (username == null) {
			credentials = getFileRequestCredentials();
		}

		if (credentials != null && username != null) {
			final AuthScope authScope = new AuthScope(HttpClientRetrieveFileTransfer.getHostFromURL(urlString), HttpClientRetrieveFileTransfer.getPortFromURL(urlString), AuthScope.ANY_REALM);
			Trace.trace(Activator.PLUGIN_ID, "browse credentials=" + credentials); //$NON-NLS-1$
			httpClient.getState().setCredentials(authScope, credentials);
		}
	}

	private HostConfiguration getHostConfiguration() {
		if (hostConfigHelper == null) {
			ISocketEventSource source = (ISocketEventSource) job.getRequest().getAdapter(ISocketEventSource.class);
			hostConfigHelper = new HostConfigHelper(source, connectingSockets);
		}
		return hostConfigHelper.getHostConfiguration();
	}

	protected void setupProxy(Proxy proxy) {
		if (proxy.getType().equals(Proxy.Type.HTTP)) {
			final ProxyAddress address = proxy.getAddress();
			getHostConfiguration().setProxy(HttpClientRetrieveFileTransfer.getHostFromURL(address.getHostName()), address.getPort());
			final String proxyUsername = proxy.getUsername();
			final String proxyPassword = proxy.getPassword();
			if (proxyUsername != null) {
				final Credentials credentials = new UsernamePasswordCredentials(proxyUsername, proxyPassword);
				final AuthScope proxyAuthScope = new AuthScope(address.getHostName(), address.getPort(), AuthScope.ANY_REALM);
				Trace.trace(Activator.PLUGIN_ID, "browse httpproxy=" + proxyAuthScope + ";credentials" + credentials); //$NON-NLS-1$ //$NON-NLS-2$
				httpClient.getState().setProxyCredentials(proxyAuthScope, credentials);
			}
		} else if (proxy.getType().equals(Proxy.Type.SOCKS)) {
			Trace.trace(Activator.PLUGIN_ID, "brows socksproxy=" + proxy.getAddress()); //$NON-NLS-1$
			proxyHelper.setupProxy(proxy);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5150.java