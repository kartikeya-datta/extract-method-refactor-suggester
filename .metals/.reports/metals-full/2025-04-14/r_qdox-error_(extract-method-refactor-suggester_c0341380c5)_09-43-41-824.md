error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7208.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7208.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7208.java
text:
```scala
.@@contains(RemoteConstants.DISCOVERY_SERVICE_TYPE))

/*******************************************************************************
 * Copyright (c) 2010-2011 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.osgi.services.remoteserviceadmin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ecf.discovery.IDiscoveryAdvertiser;
import org.eclipse.ecf.discovery.IDiscoveryLocator;
import org.eclipse.ecf.discovery.IServiceEvent;
import org.eclipse.ecf.discovery.IServiceInfo;
import org.eclipse.ecf.discovery.IServiceListener;
import org.eclipse.ecf.discovery.identity.IServiceID;
import org.eclipse.ecf.internal.osgi.services.remoteserviceadmin.Activator;
import org.eclipse.ecf.internal.osgi.services.remoteserviceadmin.DebugOptions;
import org.eclipse.ecf.internal.osgi.services.remoteserviceadmin.LogUtility;
import org.eclipse.ecf.internal.osgi.services.remoteserviceadmin.PropertiesUtil;
import org.eclipse.equinox.concurrent.future.IExecutor;
import org.eclipse.equinox.concurrent.future.IProgressRunnable;
import org.eclipse.equinox.concurrent.future.ThreadsExecutor;
import org.eclipse.osgi.framework.eventmgr.CopyOnWriteIdentityMap;
import org.eclipse.osgi.framework.eventmgr.EventDispatcher;
import org.eclipse.osgi.framework.eventmgr.EventManager;
import org.eclipse.osgi.framework.eventmgr.ListenerQueue;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.remoteserviceadmin.EndpointDescription;
import org.osgi.service.remoteserviceadmin.EndpointListener;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class EndpointDescriptionLocator {

	private BundleContext context;
	private IExecutor executor;

	// service info factory default
	private ServiceInfoFactory serviceInfoFactory;
	private ServiceRegistration defaultServiceInfoFactoryRegistration;
	// service info factory service tracker
	private Object serviceInfoFactoryTrackerLock = new Object();
	private ServiceTracker serviceInfoFactoryTracker;

	// endpoint description factory default
	private DiscoveredEndpointDescriptionFactory defaultEndpointDescriptionFactory;
	private ServiceRegistration defaultEndpointDescriptionFactoryRegistration;
	// endpoint description factory tracker
	private Object endpointDescriptionFactoryTrackerLock = new Object();
	private ServiceTracker endpointDescriptionFactoryTracker;
	// endpointDescriptionReader default
	private ServiceRegistration defaultEndpointDescriptionReaderRegistration;

	// For processing synchronous notifications asynchronously
	private EventManager eventManager;
	private ListenerQueue eventQueue;
	private LocatorServiceListener localLocatorServiceListener;

	// ECF IDiscoveryLocator tracker
	private ServiceTracker locatorServiceTracker;
	// Locator listeners
	private Map<IDiscoveryLocator, LocatorServiceListener> locatorListeners;

	private ServiceTracker endpointListenerTracker;

	private ServiceTracker advertiserTracker;
	private Object advertiserTrackerLock = new Object();

	private BundleTracker bundleTracker;
	private EndpointDescriptionBundleTrackerCustomizer bundleTrackerCustomizer;

	public EndpointDescriptionLocator(BundleContext context) {
		this.context = context;
		this.executor = new ThreadsExecutor();
	}

	public void start() {
		// For service info and endpoint description factories
		// set the service ranking to Integer.MIN_VALUE
		// so that any other registered factories will be preferred
		final Properties properties = new Properties();
		properties.put(Constants.SERVICE_RANKING,
				new Integer(Integer.MIN_VALUE));
		serviceInfoFactory = new ServiceInfoFactory();
		defaultServiceInfoFactoryRegistration = context.registerService(
				IServiceInfoFactory.class.getName(), serviceInfoFactory,
				(Dictionary) properties);
		defaultEndpointDescriptionFactory = new DiscoveredEndpointDescriptionFactory();
		defaultEndpointDescriptionFactoryRegistration = context
				.registerService(
						IDiscoveredEndpointDescriptionFactory.class.getName(),
						defaultEndpointDescriptionFactory,
						(Dictionary) properties);
		// setup/register default endpointDescriptionReader
		defaultEndpointDescriptionReaderRegistration = context.registerService(
				IEndpointDescriptionReader.class.getName(),
				new EndpointDescriptionReader(), (Dictionary) properties);

		// Create thread group, event manager, and eventQueue, and setup to
		// dispatch EndpointListenerEvents
		ThreadGroup eventGroup = new ThreadGroup(
				"RSA EndpointDescriptionLocator ThreadGroup"); //$NON-NLS-1$
		eventGroup.setDaemon(true);
		eventManager = new EventManager(
				"RSA EndpointDescriptionLocator Dispatcher", eventGroup); //$NON-NLS-1$
		eventQueue = new ListenerQueue(eventManager);
		CopyOnWriteIdentityMap listeners = new CopyOnWriteIdentityMap();
		listeners.put(this, this);
		eventQueue.queueListeners(listeners.entrySet(), new EventDispatcher() {
			public void dispatchEvent(Object eventListener,
					Object listenerObject, int eventAction, Object eventObject) {
				final String logMethodName = "dispatchEvent"; //$NON-NLS-1$
				final EndpointListenerEvent event = (EndpointListenerEvent) eventObject;
				final EndpointListener endpointListener = event
						.getEndpointListener();
				final EndpointDescription endpointDescription = event
						.getEndointDescription();
				final String matchingFilter = event.getMatchingFilter();

				try {
					if (event.isDiscovered())
						endpointListener.endpointAdded(endpointDescription,
								matchingFilter);
					else
						endpointListener.endpointRemoved(endpointDescription,
								matchingFilter);
				} catch (Exception e) {
					String message = "Exception in EndpointListener listener=" //$NON-NLS-1$
							+ endpointListener + " description=" //$NON-NLS-1$
							+ endpointDescription + " matchingFilter=" //$NON-NLS-1$
							+ matchingFilter;
					logError(logMethodName, message, e);
				} catch (LinkageError e) {
					String message = "LinkageError in EndpointListener listener=" //$NON-NLS-1$
							+ endpointListener + " description=" //$NON-NLS-1$
							+ endpointDescription + " matchingFilter=" //$NON-NLS-1$
							+ matchingFilter;
					logError(logMethodName, message, e);
				} catch (AssertionError e) {
					String message = "AssertionError in EndpointListener listener=" //$NON-NLS-1$
							+ endpointListener + " description=" //$NON-NLS-1$
							+ endpointDescription + " matchingFilter=" //$NON-NLS-1$
							+ matchingFilter;
					logError(logMethodName, message, e);
				}
			}
		});
		// Register the endpoint listener tracker, so that endpoint listeners
		// that are subsequently added
		// will then be notified of discovered endpoints
		endpointListenerTracker = new ServiceTracker(context,
				EndpointListener.class.getName(),
				new ServiceTrackerCustomizer() {
					public Object addingService(ServiceReference reference) {
						if (context == null)
							return null;
						EndpointListener listener = (EndpointListener) context
								.getService(reference);
						if (listener == null)
							return null;
						Collection<org.osgi.service.remoteserviceadmin.EndpointDescription> allDiscoveredEndpointDescriptions = getAllDiscoveredEndpointDescriptions();
						for (org.osgi.service.remoteserviceadmin.EndpointDescription ed : allDiscoveredEndpointDescriptions) {
							EndpointDescriptionLocator.EndpointListenerHolder[] endpointListenerHolders = getMatchingEndpointListenerHolders(
									new ServiceReference[] { reference }, ed);
							if (endpointListenerHolders != null) {
								for (int i = 0; i < endpointListenerHolders.length; i++) {
									queueEndpointDescription(
											endpointListenerHolders[i]
													.getListener(),
											endpointListenerHolders[i]
													.getDescription(),
											endpointListenerHolders[i]
													.getMatchingFilter(), true);
								}
							}
						}
						return listener;
					}

					public void modifiedService(ServiceReference reference,
							Object service) {
					}

					public void removedService(ServiceReference reference,
							Object service) {
					}
				});

		endpointListenerTracker.open();

		locatorListeners = new HashMap();
		localLocatorServiceListener = new LocatorServiceListener(null);
		// Create locator service tracker, so new IDiscoveryLocators can
		// be used to discover endpoint descriptions
		locatorServiceTracker = new ServiceTracker(context,
				IDiscoveryLocator.class.getName(),
				new LocatorTrackerCustomizer());
		locatorServiceTracker.open();
		// Create bundle tracker for reading local/xml-file endpoint
		// descriptions
		bundleTrackerCustomizer = new EndpointDescriptionBundleTrackerCustomizer();
		bundleTracker = new BundleTracker(context, Bundle.ACTIVE
 Bundle.STARTING, bundleTrackerCustomizer);
		// This may trigger local endpoint description discovery
		bundleTracker.open();
	}

	private void logError(String methodName, String message, Throwable e) {
		LogUtility.logError(methodName,
				DebugOptions.ENDPOINT_DESCRIPTION_LOCATOR, this.getClass(),
				message, e);
	}

	private void trace(String methodName, String message) {
		LogUtility.trace(methodName, DebugOptions.ENDPOINT_DESCRIPTION_LOCATOR,
				this.getClass(), message);
	}

	public void close() {
		if (bundleTracker != null) {
			bundleTracker.close();
			bundleTracker = null;
		}
		if (bundleTrackerCustomizer != null) {
			bundleTrackerCustomizer.close();
			bundleTrackerCustomizer = null;
		}

		// shutdown locatorListeners
		synchronized (locatorListeners) {
			for (IDiscoveryLocator l : locatorListeners.keySet()) {
				LocatorServiceListener locatorListener = locatorListeners
						.get(l);
				if (locatorListener != null) {
					l.removeServiceListener(locatorListener);
					locatorListener.close();
				}
			}
			locatorListeners.clear();
		}

		Object[] locators = locatorServiceTracker.getServices();
		if (locators != null) {
			for (int i = 0; i < locators.length; i++) {
				// Add service listener to locator
				shutdownLocator((IDiscoveryLocator) locators[i]);
			}
		}

		if (localLocatorServiceListener != null) {
			localLocatorServiceListener.close();
			localLocatorServiceListener = null;
		}

		if (endpointListenerTracker != null) {
			endpointListenerTracker.close();
			endpointListenerTracker = null;
		}
		// Shutdown asynchronous event manager
		if (eventManager != null) {
			eventManager.close();
			eventManager = null;
		}

		synchronized (endpointDescriptionFactoryTrackerLock) {
			if (endpointDescriptionFactoryTracker != null) {
				endpointDescriptionFactoryTracker.close();
				endpointDescriptionFactoryTracker = null;
			}
		}
		if (defaultEndpointDescriptionFactoryRegistration != null) {
			defaultEndpointDescriptionFactoryRegistration.unregister();
			defaultEndpointDescriptionFactoryRegistration = null;
		}
		if (defaultEndpointDescriptionFactory != null) {
			defaultEndpointDescriptionFactory.close();
			defaultEndpointDescriptionFactory = null;
		}

		synchronized (serviceInfoFactoryTrackerLock) {
			if (serviceInfoFactoryTracker != null) {
				serviceInfoFactoryTracker.close();
				serviceInfoFactoryTracker = null;
			}
		}
		if (defaultServiceInfoFactoryRegistration != null) {
			defaultServiceInfoFactoryRegistration.unregister();
			defaultServiceInfoFactoryRegistration = null;
		}
		if (serviceInfoFactory != null) {
			serviceInfoFactory.close();
			serviceInfoFactory = null;
		}
		if (defaultEndpointDescriptionReaderRegistration != null) {
			defaultEndpointDescriptionReaderRegistration.unregister();
			defaultEndpointDescriptionReaderRegistration = null;
		}
		if (locatorServiceTracker != null) {
			locatorServiceTracker.close();
			locatorServiceTracker = null;
		}
		synchronized (advertiserTrackerLock) {
			if (advertiserTracker != null) {
				advertiserTracker.close();
				advertiserTracker = null;
			}
		}
		this.executor = null;
		this.context = null;
	}

	public IDiscoveryAdvertiser[] getDiscoveryAdvertisers() {
		synchronized (advertiserTrackerLock) {
			if (advertiserTracker == null) {
				advertiserTracker = new ServiceTracker(context,
						IDiscoveryAdvertiser.class.getName(), null);
				advertiserTracker.open();
			}
		}
		ServiceReference[] advertiserRefs = advertiserTracker
				.getServiceReferences();
		if (advertiserRefs == null)
			return null;
		List<IDiscoveryAdvertiser> results = new ArrayList<IDiscoveryAdvertiser>();
		for (int i = 0; i < advertiserRefs.length; i++) {
			results.add((IDiscoveryAdvertiser) context
					.getService(advertiserRefs[i]));
		}
		return results.toArray(new IDiscoveryAdvertiser[results.size()]);
	}

	private void openLocator(IDiscoveryLocator locator) {
		if (context == null)
			return;
		synchronized (locatorListeners) {
			LocatorServiceListener locatorListener = new LocatorServiceListener(
					locator);
			locatorListeners.put(locator, locatorListener);
			processInitialLocatorServices(locator, locatorListener);
		}
	}

	private void shutdownLocator(IDiscoveryLocator locator) {
		if (locator == null || context == null)
			return;
		synchronized (locatorListeners) {
			LocatorServiceListener locatorListener = locatorListeners
					.remove(locator);
			if (locatorListener != null)
				locatorListener.close();
		}
	}

	void queueEndpointDescription(
			EndpointListener listener,
			org.osgi.service.remoteserviceadmin.EndpointDescription endpointDescription,
			String matchingFilters, boolean discovered) {
		if (eventQueue == null)
			return;
		trace("queueEndpointDescription", "endpointDescription="
				+ endpointDescription);
		synchronized (eventQueue) {
			eventQueue
					.dispatchEventAsynchronous(0, new EndpointListenerEvent(
							listener, endpointDescription, matchingFilters,
							discovered));
		}
	}

	Collection<org.osgi.service.remoteserviceadmin.EndpointDescription> getAllDiscoveredEndpointDescriptions() {
		Collection<org.osgi.service.remoteserviceadmin.EndpointDescription> result = new ArrayList();
		if (localLocatorServiceListener == null)
			return result;
		// Get local first
		result.addAll(localLocatorServiceListener.getEndpointDescriptions());
		synchronized (locatorListeners) {
			for (IDiscoveryLocator l : locatorListeners.keySet()) {
				LocatorServiceListener locatorListener = locatorListeners
						.get(l);
				result.addAll(locatorListener.getEndpointDescriptions());
			}
		}
		return result;
	}

	void queueEndpointDescription(
			org.osgi.service.remoteserviceadmin.EndpointDescription endpointDescription,
			boolean discovered) {
		EndpointListenerHolder[] endpointListenerHolders = getMatchingEndpointListenerHolders(endpointDescription);
		if (endpointListenerHolders != null) {
			for (int i = 0; i < endpointListenerHolders.length; i++) {
				queueEndpointDescription(
						endpointListenerHolders[i].getListener(),
						endpointListenerHolders[i].getDescription(),
						endpointListenerHolders[i].getMatchingFilter(),
						discovered);

			}
		} else {
			LogUtility.logWarning(
					"queueEndpointDescription", //$NON-NLS-1$
					DebugOptions.ENDPOINT_DESCRIPTION_LOCATOR, this.getClass(),
					"No matching EndpointListeners found for " //$NON-NLS-1$
							+ (discovered ? "discovered" : "undiscovered") //$NON-NLS-1$ //$NON-NLS-2$
							+ " endpointDescription=" + endpointDescription); //$NON-NLS-1$
		}

	}

	private void processInitialLocatorServices(final IDiscoveryLocator locator,
			final LocatorServiceListener locatorListener) {
		IProgressRunnable runnable = new IProgressRunnable() {
			public Object run(IProgressMonitor arg0) throws Exception {
				IServiceInfo[] serviceInfos = locator.getServices();
				for (int i = 0; i < serviceInfos.length; i++) {
					locatorListener.handleService(serviceInfos[i], true);
				}
				return null;
			}
		};
		executor.execute(runnable, null);
	}

	void shutdownLocators() {
		Object[] locators = locatorServiceTracker.getServices();
		if (locators != null) {
			for (int i = 0; i < locators.length; i++) {
				// Add service listener to locator
				shutdownLocator((IDiscoveryLocator) locators[i]);
			}
		}
	}

	private class EndpointListenerEvent {

		private EndpointListener endpointListener;
		private org.osgi.service.remoteserviceadmin.EndpointDescription endpointDescription;
		private String matchingFilter;
		private boolean discovered;

		public EndpointListenerEvent(
				EndpointListener endpointListener,
				org.osgi.service.remoteserviceadmin.EndpointDescription endpointDescription,
				String matchingFilter, boolean discovered) {
			this.endpointListener = endpointListener;
			this.endpointDescription = endpointDescription;
			this.matchingFilter = matchingFilter;
			this.discovered = discovered;
		}

		public EndpointListener getEndpointListener() {
			return endpointListener;
		}

		public org.osgi.service.remoteserviceadmin.EndpointDescription getEndointDescription() {
			return endpointDescription;
		}

		public String getMatchingFilter() {
			return matchingFilter;
		}

		public boolean isDiscovered() {
			return discovered;
		}
	}

	private class LocatorTrackerCustomizer implements ServiceTrackerCustomizer {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.osgi.util.tracker.ServiceTrackerCustomizer#addingService(org.
		 * osgi.framework.ServiceReference)
		 */
		public Object addingService(ServiceReference reference) {
			IDiscoveryLocator locator = (IDiscoveryLocator) context
					.getService(reference);
			if (locator != null)
				openLocator(locator);
			return locator;
		}

		public void modifiedService(ServiceReference reference, Object service) {
		}

		public void removedService(ServiceReference reference, Object service) {
			shutdownLocator((IDiscoveryLocator) service);
		}
	}

	public IServiceInfoFactory getServiceInfoFactory() {
		if (context == null)
			return null;
		synchronized (serviceInfoFactoryTrackerLock) {
			if (serviceInfoFactoryTracker == null) {
				serviceInfoFactoryTracker = new ServiceTracker(context,
						IServiceInfoFactory.class.getName(), null);
				serviceInfoFactoryTracker.open();
			}
		}
		return (IServiceInfoFactory) serviceInfoFactoryTracker.getService();
	}

	public IDiscoveredEndpointDescriptionFactory getDiscoveredEndpointDescriptionFactory() {
		synchronized (endpointDescriptionFactoryTrackerLock) {
			if (context == null)
				return null;
			if (endpointDescriptionFactoryTracker == null) {
				endpointDescriptionFactoryTracker = new ServiceTracker(context,
						IDiscoveredEndpointDescriptionFactory.class.getName(),
						null);
				endpointDescriptionFactoryTracker.open();
			}
			return (IDiscoveredEndpointDescriptionFactory) endpointDescriptionFactoryTracker
					.getService();
		}
	}

	private Object endpointListenerServiceTrackerLock = new Object();

	public EndpointListenerHolder[] getMatchingEndpointListenerHolders(
			EndpointDescription description) {
		synchronized (endpointListenerServiceTrackerLock) {
			if (context == null)
				return null;
			return getMatchingEndpointListenerHolders(
					endpointListenerTracker.getServiceReferences(), description);
		}
	}

	public class EndpointListenerHolder {

		private EndpointListener listener;
		private EndpointDescription description;
		private String matchingFilter;

		public EndpointListenerHolder(EndpointListener l,
				EndpointDescription d, String f) {
			this.listener = l;
			this.description = d;
			this.matchingFilter = f;
		}

		public EndpointListener getListener() {
			return listener;
		}

		public EndpointDescription getDescription() {
			return description;
		}

		public String getMatchingFilter() {
			return matchingFilter;
		}
	}

	public EndpointListenerHolder[] getMatchingEndpointListenerHolders(
			ServiceReference[] refs, EndpointDescription description) {
		if (refs == null)
			return null;
		List results = new ArrayList();
		for (int i = 0; i < refs.length; i++) {
			EndpointListener listener = (EndpointListener) context
					.getService(refs[i]);
			if (listener == null)
				continue;
			List<String> filters = PropertiesUtil.getStringPlusProperty(
					getMapFromProperties(refs[i]),
					EndpointListener.ENDPOINT_LISTENER_SCOPE);
			String matchingFilter = isMatch(description, filters);
			if (matchingFilter != null)
				results.add(new EndpointListenerHolder(listener, description,
						matchingFilter));
		}
		return (EndpointListenerHolder[]) results
				.toArray(new EndpointListenerHolder[results.size()]);
	}

	private String isMatch(EndpointDescription description, List<String> filters) {
		for (String filter : filters)
			if (description.matches(filter))
				return filter;
		return null;
	}

	private Map getMapFromProperties(ServiceReference ref) {
		Map<String, Object> results = new TreeMap<String, Object>(
				String.CASE_INSENSITIVE_ORDER);
		String[] keys = ref.getPropertyKeys();
		if (keys != null) {
			for (int i = 0; i < keys.length; i++) {
				results.put(keys[i], ref.getProperty(keys[i]));
			}
		}
		return results;
	}

	class EndpointDescriptionBundleTrackerCustomizer implements
			BundleTrackerCustomizer {

		private static final String REMOTESERVICE_MANIFESTHEADER = "Remote-Service"; //$NON-NLS-1$
		private static final String XML_FILE_PATTERN = "*.xml"; //$NON-NLS-1$

		private Map<Long, Collection<org.osgi.service.remoteserviceadmin.EndpointDescription>> bundleDescriptionMap = Collections
				.synchronizedMap(new HashMap<Long, Collection<org.osgi.service.remoteserviceadmin.EndpointDescription>>());

		private Object endpointDescriptionReaderTrackerLock = new Object();
		private ServiceTracker endpointDescriptionReaderTracker;

		private IEndpointDescriptionReader getEndpointDescriptionReader() {
			synchronized (endpointDescriptionReaderTrackerLock) {
				if (endpointDescriptionReaderTracker == null) {
					endpointDescriptionReaderTracker = new ServiceTracker(
							context,
							IEndpointDescriptionReader.class.getName(), null);
					endpointDescriptionReaderTracker.open();
				}
			}
			return (IEndpointDescriptionReader) endpointDescriptionReaderTracker
					.getService();
		}

		public Object addingBundle(Bundle bundle, BundleEvent event) {
			handleAddingBundle(bundle);
			return bundle;
		}

		private void handleAddingBundle(Bundle bundle) {
			if (context == null)
				return;
			String remoteServicesHeaderValue = (String) bundle.getHeaders()
					.get(REMOTESERVICE_MANIFESTHEADER);
			if (remoteServicesHeaderValue != null) {
				// First parse into comma-separated values
				String[] paths = remoteServicesHeaderValue.split(","); //$NON-NLS-1$
				if (paths != null)
					for (int i = 0; i < paths.length; i++)
						handleEndpointDescriptionPath(bundle, paths[i]);
			}
		}

		private void handleEndpointDescriptionPath(Bundle bundle,
				String remoteServicesHeaderValue) {
			// if it's empty, ignore
			if ("".equals(remoteServicesHeaderValue)) //$NON-NLS-1$
				return;
			Enumeration<URL> e = null;
			// if it endswith a '/', then scan for *.xml files
			if (remoteServicesHeaderValue.endsWith("/")) { //$NON-NLS-1$
				e = bundle.findEntries(remoteServicesHeaderValue,
						XML_FILE_PATTERN, false);
			} else {
				// Break into path and filename/pattern
				int lastSlashIndex = remoteServicesHeaderValue.lastIndexOf('/');
				if (lastSlashIndex == -1) {
					// no slash...might be a file name or pattern, assumed to be
					// at root of bundle
					e = bundle.findEntries(
							"/", remoteServicesHeaderValue, false); //$NON-NLS-1$
				} else {
					String path = remoteServicesHeaderValue.substring(0,
							lastSlashIndex);
					if ("".equals(path)) { //$NON-NLS-1$
						// path is empty so assume it's root
						path = "/"; //$NON-NLS-1$
					}
					String filePattern = remoteServicesHeaderValue
							.substring(lastSlashIndex + 1);
					e = bundle.findEntries(path, filePattern, false);
				}
			}
			// Now process any found
			Collection<org.osgi.service.remoteserviceadmin.EndpointDescription> endpointDescriptions = new ArrayList<org.osgi.service.remoteserviceadmin.EndpointDescription>();
			if (e != null) {
				while (e.hasMoreElements()) {
					org.osgi.service.remoteserviceadmin.EndpointDescription[] eps = handleEndpointDescriptionFile(
							bundle, e.nextElement());
					if (eps != null)
						for (int i = 0; i < eps.length; i++)
							endpointDescriptions.add(eps[i]);
				}
			}
			// finally, handle them
			if (endpointDescriptions.size() > 0) {
				bundleDescriptionMap.put(new Long(bundle.getBundleId()),
						endpointDescriptions);
				for (org.osgi.service.remoteserviceadmin.EndpointDescription ed : endpointDescriptions)
					localLocatorServiceListener.handleEndpointDescription(ed,
							true);
			}
		}

		private org.osgi.service.remoteserviceadmin.EndpointDescription[] handleEndpointDescriptionFile(
				Bundle bundle, URL fileURL) {
			InputStream ins = null;
			try {
				IEndpointDescriptionReader endpointDescriptionReader = getEndpointDescriptionReader();
				if (endpointDescriptionReader == null)
					throw new NullPointerException(
							"No endpointDescriptionReader available for handleEndpointDescriptionFile fileURL=" //$NON-NLS-1$
									+ fileURL);
				ins = fileURL.openStream();
				return endpointDescriptionReader.readEndpointDescriptions(ins);
			} catch (Exception e) {
				logError("handleEndpointDescriptionFile", //$NON-NLS-1$
						"Exception creating endpoint descriptions from fileURL=" //$NON-NLS-1$
								+ fileURL, e);
				return null;
			} finally {
				if (ins != null)
					try {
						ins.close();
					} catch (IOException e) {
						logError("handleEndpointDescriptionFile", //$NON-NLS-1$
								"Exception closing endpointDescription input fileURL=" //$NON-NLS-1$
										+ fileURL, e);
					}
			}
		}

		private void logError(String method, String message, Throwable t) {
			LogUtility.logError(method,
					DebugOptions.ENDPOINT_DESCRIPTION_LOCATOR, this.getClass(),
					new Status(IStatus.ERROR, Activator.PLUGIN_ID,
							IStatus.ERROR, message, t));
		}

		public void modifiedBundle(Bundle bundle, BundleEvent event,
				Object object) {
		}

		public void removedBundle(Bundle bundle, BundleEvent event,
				Object object) {
			handleRemovedBundle(bundle);
		}

		private void handleRemovedBundle(Bundle bundle) {
			Collection<org.osgi.service.remoteserviceadmin.EndpointDescription> endpointDescriptions = bundleDescriptionMap
					.remove(new Long(bundle.getBundleId()));
			if (endpointDescriptions != null)
				for (org.osgi.service.remoteserviceadmin.EndpointDescription ed : endpointDescriptions)
					localLocatorServiceListener.handleEndpointDescription(ed,
							false);
		}

		public void close() {
			synchronized (endpointDescriptionReaderTrackerLock) {
				if (endpointDescriptionReaderTracker != null) {
					endpointDescriptionReaderTracker.close();
					endpointDescriptionReaderTracker = null;
				}
			}
			bundleDescriptionMap.clear();
		}
	}

	class LocatorServiceListener implements IServiceListener {

		private Object listenerLock = new Object();
		private IDiscoveryLocator locator;

		private List<org.osgi.service.remoteserviceadmin.EndpointDescription> discoveredEndpointDescriptions = new ArrayList();

		public LocatorServiceListener(IDiscoveryLocator locator) {
			this.locator = locator;
			if (locator != null)
				this.locator.addServiceListener(this);
		}

		public void serviceDiscovered(IServiceEvent anEvent) {
			handleService(anEvent.getServiceInfo(), true);
		}

		public void serviceUndiscovered(IServiceEvent anEvent) {
			handleService(anEvent.getServiceInfo(), false);
		}

		private boolean matchServiceID(IServiceID serviceId) {
			if (Arrays.asList(serviceId.getServiceTypeID().getServices())
					.contains(RemoteConstants.SERVICE_TYPE))
				return true;
			return false;
		}

		void handleService(IServiceInfo serviceInfo, boolean discovered) {
			IServiceID serviceID = serviceInfo.getServiceID();
			if (matchServiceID(serviceID))
				handleOSGiServiceEndpoint(serviceID, serviceInfo, discovered);
		}

		private void handleOSGiServiceEndpoint(IServiceID serviceId,
				IServiceInfo serviceInfo, boolean discovered) {
			if (locator == null)
				return;
			DiscoveredEndpointDescription discoveredEndpointDescription = getDiscoveredEndpointDescription(
					serviceId, serviceInfo, discovered);
			if (discoveredEndpointDescription != null) {
				handleEndpointDescription(
						discoveredEndpointDescription.getEndpointDescription(),
						discovered);
			} else {
				logWarning("handleOSGiServiceEvent", //$NON-NLS-1$
						"discoveredEndpointDescription is null for service info=" //$NON-NLS-1$
								+ serviceInfo + ",discovered=" + discovered); //$NON-NLS-1$
			}
		}

		public void handleEndpointDescription(
				org.osgi.service.remoteserviceadmin.EndpointDescription endpointDescription,
				boolean discovered) {
			synchronized (listenerLock) {
				if (discovered)
					discoveredEndpointDescriptions.add(endpointDescription);
				else
					discoveredEndpointDescriptions.remove(endpointDescription);

				queueEndpointDescription(endpointDescription, discovered);
			}
		}

		public Collection<org.osgi.service.remoteserviceadmin.EndpointDescription> getEndpointDescriptions() {
			synchronized (listenerLock) {
				Collection<org.osgi.service.remoteserviceadmin.EndpointDescription> result = new ArrayList<org.osgi.service.remoteserviceadmin.EndpointDescription>();
				result.addAll(discoveredEndpointDescriptions);
				return result;
			}
		}

		private void logWarning(String methodName, String message) {
			LogUtility.logWarning(methodName,
					DebugOptions.ENDPOINT_DESCRIPTION_LOCATOR, this.getClass(),
					message);
		}

		private void logError(String methodName, String message) {
			logError(methodName, message, null);
		}

		private void logError(String methodName, String message, Throwable t) {
			LogUtility.logError(methodName,
					DebugOptions.ENDPOINT_DESCRIPTION_LOCATOR, this.getClass(),
					message, t);
		}

		private DiscoveredEndpointDescription getDiscoveredEndpointDescription(
				IServiceID serviceId, IServiceInfo serviceInfo,
				boolean discovered) {
			// Get IEndpointDescriptionFactory
			final String methodName = "getDiscoveredEndpointDescription"; //$NON-NLS-1$
			IDiscoveredEndpointDescriptionFactory factory = getDiscoveredEndpointDescriptionFactory();
			if (factory == null) {
				logError(
						methodName,
						"No IEndpointDescriptionFactory found, could not create EndpointDescription for " //$NON-NLS-1$
								+ (discovered ? "discovered" : "undiscovered") //$NON-NLS-1$ //$NON-NLS-2$
								+ " serviceInfo=" + serviceInfo); //$NON-NLS-1$
				return null;
			}
			try {
				// Else get endpoint description factory to create
				// EndpointDescription
				// for given serviceID and serviceInfo
				return (discovered) ? factory
						.createDiscoveredEndpointDescription(locator,
								serviceInfo) : factory
						.removeDiscoveredEndpointDescription(locator, serviceId);
			} catch (Exception e) {
				logError(
						methodName,
						"Exception calling IEndpointDescriptionFactory." //$NON-NLS-1$
								+ ((discovered) ? "createDiscoveredEndpointDescription" //$NON-NLS-1$
										: "getUndiscoveredEndpointDescription"), e); //$NON-NLS-1$
				return null;
			} catch (NoClassDefFoundError e) {
				logError(
						methodName,
						"NoClassDefFoundError calling IEndpointDescriptionFactory." //$NON-NLS-1$
								+ ((discovered) ? "createDiscoveredEndpointDescription" //$NON-NLS-1$
										: "getUndiscoveredEndpointDescription"), e); //$NON-NLS-1$
				return null;
			}
		}

		public synchronized void close() {
			if (locator != null) {
				locator.removeServiceListener(this);
				locator = null;
			}
			discoveredEndpointDescriptions.clear();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7208.java