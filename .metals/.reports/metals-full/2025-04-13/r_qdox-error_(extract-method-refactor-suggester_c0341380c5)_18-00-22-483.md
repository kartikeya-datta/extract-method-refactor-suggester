error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12255.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12255.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12255.java
text:
```scala
public R@@equestEntity generateRequestEntity(String uri, IRemoteCall call, IRemoteCallable callable, IRemoteCallParameter paramDefault, Object paramToSerialize) throws NotSerializableException {

/*******************************************************************************
* Copyright (c) 2009 Composent, Inc. and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   Composent, Inc. - initial API and implementation
******************************************************************************/
package org.eclipse.ecf.remoteservice.rest.client;

import java.io.*;
import java.util.Map;
import org.apache.commons.httpclient.methods.*;
import org.eclipse.ecf.remoteservice.IRemoteCall;
import org.eclipse.ecf.remoteservice.client.IRemoteCallParameter;
import org.eclipse.ecf.remoteservice.client.IRemoteCallable;

public abstract class AbstractEntityRequestType extends AbstractRequestType {

	public static final int NO_REQUEST_ENTITY = -1;
	public static final int INPUT_STREAM_REQUEST_ENTITY = 0;
	public static final int STRING_REQUEST_ENTITY = 1;
	public static final int BYTEARRAY_REQUEST_ENTITY = 2;
	public static final int FILE_REQUEST_ENTITY = 3;

	public static final String CHARSET_PARAM_NAME = "charset"; //$NON-NLS-1$
	public static final String CONTENT_TYPE_PARAM_NAME = "contentType"; //$NON-NLS-1$
	public static final String CONTENT_LENGTH_PARAM_NAME = "contentLength"; //$NON-NLS-1$

	protected int requestEntityType = NO_REQUEST_ENTITY;
	protected long defaultContentLength = InputStreamRequestEntity.CONTENT_LENGTH_AUTO;
	protected String defaultContentType = null;
	protected String defaultCharset = null;

	public AbstractEntityRequestType(int requestEntityType, String defaultContentType, long defaultContentLength, String defaultCharset, Map defaultRequestHeaders) {
		super(defaultRequestHeaders);
		this.requestEntityType = requestEntityType;
		this.defaultContentType = defaultContentType;
		this.defaultContentLength = defaultContentLength;
		this.defaultCharset = defaultCharset;
	}

	public AbstractEntityRequestType(int requestEntityType, String defaultContentType, long defaultContentLength, String defaultCharset) {
		this(requestEntityType, defaultContentType, defaultContentLength, defaultCharset, null);
	}

	public AbstractEntityRequestType(int requestEntityType, String defaultContentType, long defaultContentLength, Map defaultRequestHeaders) {
		this(requestEntityType, defaultContentType, defaultContentLength, null, defaultRequestHeaders);
	}

	public AbstractEntityRequestType(int requestEntityType, String defaultContentType, long defaultContentLength) {
		this(requestEntityType, defaultContentType, defaultContentLength, (String) null);
	}

	public AbstractEntityRequestType(int requestEntityType, String defaultContentType, Map defaultRequestHeaders) {
		this(requestEntityType, defaultContentType, InputStreamRequestEntity.CONTENT_LENGTH_AUTO, null, defaultRequestHeaders);
	}

	public AbstractEntityRequestType(int requestEntityType, String defaultContentType) {
		this(requestEntityType, defaultContentType, null);
	}

	public AbstractEntityRequestType(int requestEntityType, Map defaultRequestHeaders) {
		this(requestEntityType, null, defaultRequestHeaders);
	}

	public AbstractEntityRequestType(Map defaultRequestHeaders) {
		this(NO_REQUEST_ENTITY, defaultRequestHeaders);
	}

	public AbstractEntityRequestType() {
		this(null);
	}

	public boolean useRequestEntity() {
		return requestEntityType > -1;
	}

	protected RequestEntity generateRequestEntity(String uri, IRemoteCall call, IRemoteCallable callable, IRemoteCallParameter paramDefault, Object paramToSerialize) throws NotSerializableException {
		if (paramToSerialize instanceof RequestEntity)
			return (RequestEntity) paramToSerialize;
		switch (requestEntityType) {
			case INPUT_STREAM_REQUEST_ENTITY :
				if (paramToSerialize instanceof InputStream) {
					return new InputStreamRequestEntity((InputStream) paramToSerialize, getContentLength(call, callable, paramDefault), getContentType(call, callable, paramDefault));
				}
				throw new NotSerializableException("Cannot generate request entity.  Expecting InputStream and got class=" + paramToSerialize.getClass().getName()); //$NON-NLS-1$
			case STRING_REQUEST_ENTITY :
				if (paramToSerialize instanceof String) {
					try {
						return new StringRequestEntity((String) paramToSerialize, getContentType(call, callable, paramDefault), getCharset(call, callable, paramDefault));
					} catch (UnsupportedEncodingException e) {
						throw new NotSerializableException("Could not create request entity from call parameters: " + e.getMessage()); //$NON-NLS-1$
					}
				}
				throw new NotSerializableException("Cannot generate request entity.  Expecting String and got class=" + paramToSerialize.getClass().getName()); //$NON-NLS-1$
			case BYTEARRAY_REQUEST_ENTITY :
				if (paramToSerialize instanceof byte[]) {
					return new ByteArrayRequestEntity((byte[]) paramToSerialize, getContentType(call, callable, paramDefault));
				}
				throw new NotSerializableException("Cannot generate request entity.  Expecting byte[] and got class=" + paramToSerialize.getClass().getName()); //$NON-NLS-1$
			case FILE_REQUEST_ENTITY :
				if (paramToSerialize instanceof File) {
					return new FileRequestEntity((File) paramToSerialize, getContentType(call, callable, paramDefault));
				}
				throw new NotSerializableException("Remote call parameter with name=" + paramDefault.getName() + " is incorrect type for creating request entity."); //$NON-NLS-1$ //$NON-NLS-2$
			default :
				throw new NotSerializableException("Request entity generation not supported for this request type"); //$NON-NLS-1$
		}
	}

	protected String getCharset(IRemoteCall call, IRemoteCallable callable, IRemoteCallParameter paramDefault) {
		IRemoteCallParameter[] defaultParameters = callable.getDefaultParameters();
		Object[] parameters = call.getParameters();
		if (defaultParameters != null) {
			for (int i = 0; i < defaultParameters.length; i++) {
				if (CHARSET_PARAM_NAME.equals(defaultParameters[i].getName())) {
					Object o = (parameters != null && parameters.length > i) ? parameters[i] : defaultParameters[i];
					if (o instanceof String) {
						return (String) o;
					}
				}
			}
		}
		return defaultCharset;
	}

	protected long getContentLength(IRemoteCall call, IRemoteCallable callable, IRemoteCallParameter paramDefault) {
		IRemoteCallParameter[] defaultParameters = callable.getDefaultParameters();
		Object[] parameters = call.getParameters();
		if (defaultParameters != null) {
			for (int i = 0; i < defaultParameters.length; i++) {
				if (CONTENT_LENGTH_PARAM_NAME.equals(defaultParameters[i].getName())) {
					Object o = (parameters != null && parameters.length > i) ? parameters[i] : defaultParameters[i];
					if (o instanceof Number) {
						return ((Number) o).longValue();
					} else if (o instanceof String) {
						try {
							return Integer.parseInt((String) o);
						} catch (NumberFormatException e) {
							return InputStreamRequestEntity.CONTENT_LENGTH_AUTO;
						}
					}
				}
			}
		}
		return defaultContentLength;
	}

	protected String getContentType(IRemoteCall call, IRemoteCallable callable, IRemoteCallParameter paramDefault) {
		IRemoteCallParameter[] defaultParameters = callable.getDefaultParameters();
		Object[] parameters = call.getParameters();
		if (defaultParameters != null) {
			for (int i = 0; i < defaultParameters.length; i++) {
				if (CONTENT_TYPE_PARAM_NAME.equals(defaultParameters[i].getName())) {
					Object o = (parameters != null && parameters.length > i) ? parameters[i] : defaultParameters[i];
					if (o instanceof String) {
						return (String) o;
					}
				}
			}
		}
		return defaultContentType;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12255.java