String url = getRequestCycle().urlFor(resourceReference);

/*
 * $Id$
 * $Revision$ $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.markup.resolver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.Page;
import wicket.PageParameters;
import wicket.ResourceReference;
import wicket.WicketRuntimeException;
import wicket.application.IClassResolver;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupResourceStream;
import wicket.markup.MarkupStream;
import wicket.markup.html.PackageResourceReference;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.link.ExternalLink;
import wicket.markup.parser.filter.WicketLinkTagHandler;
import wicket.util.lang.Packages;
import wicket.util.string.Strings;
import wicket.util.value.ValueMap;

/**
 * The AutoLinkResolver is responsible to handle automatic link resolution. Tags
 * are marked "autolink" by the MarkupParser for all tags with href attribute,
 * such as anchor and link tags with no explicit wicket id. E.g. &lt;a
 * href="Home.html"&gt;
 * <p>
 * If href points to a *.html file, a BookmarkablePageLink will automatically be
 * created, except for absolut paths, where an ExternalLink is created.
 * <p>
 * If href points to a *.html file, it resolves the given URL by searching for a
 * page class, either relative or absolute, specified by the href attribute of
 * the tag. If relative the href URL must be relative to the package containing
 * the associated page. An exception is thrown if no Page class was found.
 * <p>
 * If href is no *.html file a static reference to the resource is created.
 * 
 * @see wicket.markup.parser.filter.WicketLinkTagHandler
 * @author Juergen Donnerstag
 */
public final class AutoLinkResolver implements IComponentResolver
{
	/** Logging */
	private static final Log log = LogFactory.getLog(AutoLinkResolver.class);

	private static final long serialVersionUID = 1L;

	/** List of all file name extensions which are supported by autolink */
	private static final ValueMap supportedPageExtensions = new ValueMap();

	static
	{
		/**
		 * Initialize supported list of file name extension which'll create
		 * bookmarkable pages
		 */
		supportedPageExtensions.put("html", null);
		supportedPageExtensions.put("xml", null);
		supportedPageExtensions.put("wml", null);
		supportedPageExtensions.put("svg", null);
	}

	/**
	 * Automatically creates a BookmarkablePageLink component.
	 * 
	 * @see wicket.markup.resolver.IComponentResolver#resolve(MarkupContainer,
	 *      MarkupStream, ComponentTag)
	 * 
	 * @param markupStream
	 *            The current markupStream
	 * @param tag
	 *            The current component tag while parsing the markup
	 * @param container
	 *            The container parsing its markup
	 * @return true, if componentId was handle by the resolver. False, otherwise
	 */
	public final boolean resolve(final MarkupContainer container, final MarkupStream markupStream,
			final ComponentTag tag)
	{
		// Must be marked as autolink tag
		if (tag.isAutolinkEnabled())
		{
			// Try to find the Page matching the href
			// Note: to not use tag.getId() because it will be modified while
			// resolving the link and hence the 2nd render will fail.
			final Component link = resolveAutomaticLink(container,
					WicketLinkTagHandler.AUTOLINK_ID, tag);

			// Add the link to the container
			container.autoAdd(link);
			if (log.isDebugEnabled())
			{
				log.debug("Added autolink " + link);
			}

			// Tell the container, we resolved the id
			return true;
		}

		// We were not able to resolve the id
		return false;
	}

	/**
	 * Resolves the given tag's page class and page parameters by parsing the
	 * tag component name and then searching for a page class at the absolute or
	 * relative URL specified by the href attribute of the tag.
	 * <p>
	 * None html references are treated similar.
	 * 
	 * @param container
	 *            The container where the link is
	 * @param id
	 *            the name of the component
	 * @param tag
	 *            the component tag
	 * @return A BookmarkablePageLink to handle the href
	 */
	private final Component resolveAutomaticLink(final MarkupContainer container, final String id,
			final ComponentTag tag)
	{
		final Page page = container.getPage();
		final String href = tag.getAttributes().getString("href");

		// If href contains URL query parameters ..
		final PageParameters pageParameters;
		String infoPath;

		// get the query string
		int pos = href.indexOf("?");
		if (pos != -1)
		{
			final String queryString = href.substring(pos + 1);
			pageParameters = new PageParameters(new ValueMap(queryString, "&"));
			infoPath = href.substring(0, pos);
		}
		else
		{
			pageParameters = null;
			infoPath = href;
		}

		// Make the id (page-)unique
		final String autoId = id + Integer.toString(page.getAutoIndex());

		// By setting the component name, the tag becomes a Wicket component
		// tag, which must have a associated Component.
		tag.setId(autoId);

		// remove file extension, but remember it
		String extension = null;
		pos = infoPath.lastIndexOf(".");
		if (pos != -1)
		{
			extension = infoPath.substring(pos + 1);
			infoPath = infoPath.substring(0, pos);

			// HTML hrefs are handled first
			if (supportedPageExtensions.containsKey(extension.toLowerCase()))
			{
				// Obviously a href like href="myPkg.MyLabel.html" will do as
				// well. Wicket will not throw an exception. It accepts it.
				infoPath = Strings.replaceAll(infoPath, "/", ".");

				final IClassResolver defaultClassResolver = page.getApplication()
						.getApplicationSettings().getClassResolver();

				final String className;
				if (!infoPath.startsWith("."))
				{
					// Href is relative. Resolve the url given relative to the
					// current page
					className = Packages.extractPackageName(page.getClass()) + "." + infoPath;
				}
				else
				{
					// Href is absolute. If class with the same absolute path
					// exists, use it. Else don't change the href.
					className = infoPath.substring(1);
				}

				try
				{
					final Class clazz = defaultClassResolver.resolveClass(className);
					return new AutolinkBookmarkablePageLink(autoId, clazz, pageParameters);
				}
				catch (WicketRuntimeException ex)
				{
					log.info("Did not find corresponding java class: " + className);
					// fall through
				}
			}
			// It is not "*.html". Create a static resource reference
			else
			{
				if (infoPath.startsWith("/") || infoPath.startsWith("\\"))
				{
					// href is absolute. Don't change it at all.
				}
				else
				{
					// Href is relative. Create a resource reference pointing at
					// this file

					// <wicket:head> components are handled differently. We can
					// not use the container, because it is the container the
					// header has been added to (e.g. the Page). What we need
					// however, is the component (e.g. a Panel) which
					// contributed it.
					Class clazz = ((MarkupResourceStream)container.getMarkupStream().getResource())
							.getMarkupClass();

					try
					{
						// Create the component implementing the link
						return new CssLink(autoId, clazz, href);
					}
					catch (WicketRuntimeException ex)
					{
						// Provided the resource does not exist, assume the user
						// did deliberately not point it to a page or resource.
						// The href might still point to a valid homepage
						// outside
						// of wicket.
						log.info("Did not find autolink resource: " + href
								+ "; Assume it is a valid external URL");
					}
				}
			}
		}

		// We have not been able to find the resource requested
		// Don't change the href.
		return new AutolinkExternalLink(autoId, href);
	}

	/**
	 * Autolink components delegate component resolution to their parent
	 * components. Reason: autolink tags don't have wicket:id and users wouldn't
	 * know where to add the component to.
	 * 
	 * @author Juergen Donnerstag
	 */
	private final static class AutolinkBookmarkablePageLink extends BookmarkablePageLink
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Construct
		 * 
		 * @see BookmarkablePageLink#BookmarkablePageLink(String, Class,
		 *      PageParameters)
		 * 
		 * @param id
		 * @param pageClass
		 * @param parameters
		 */
		public AutolinkBookmarkablePageLink(final String id, final Class pageClass,
				final PageParameters parameters)
		{
			super(id, pageClass, parameters);
			setAutoEnable(true);
		}

		/**
		 * @see wicket.MarkupContainer#isTransparentResolver()
		 */
		public boolean isTransparentResolver()
		{
			return true;
		}
	}

	/**
	 * Autolink components delegate component resolution to their parent
	 * components. Reason: autolink tags don't have wicket:id and users wouldn't
	 * know where to add the component to.
	 * 
	 * @author Juergen Donnerstag
	 */
	private final static class AutolinkExternalLink extends ExternalLink
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Construct
		 * 
		 * @param id
		 * @param href
		 */
		public AutolinkExternalLink(final String id, final String href)
		{
			super(id, href);
		}

		/**
		 * @see wicket.MarkupContainer#isTransparentResolver()
		 */
		public boolean isTransparentResolver()
		{
			return true;
		}
	}

	/**
	 * Autolink component delegate component resolution to their parent
	 * components. Reason: autolink tags don't have wicket:id and users wouldn't
	 * know where to add the component to.
	 */
	private final static class CssLink extends WebMarkupContainer
	{
		private static final long serialVersionUID = 1L;

		/** Resource reference */
		private final ResourceReference resourceReference;

		/**
		 * @param id
		 * @param clazz
		 * @param href
		 */
		public CssLink(final String id, final Class clazz, final String href)
		{
			super(id);

			// Create the component implementing the link
			resourceReference = new PackageResourceReference(getApplication(), clazz, href,
					getLocale(), getStyle());
		}

		/**
		 * Handles this link's tag.
		 * 
		 * @param tag
		 *            the component tag
		 * @see wicket.Component#onComponentTag(ComponentTag)
		 */
		protected final void onComponentTag(final ComponentTag tag)
		{
			// Default handling for tag
			super.onComponentTag(tag);

			// Set href to link to this link's linkClicked method
			String url = getPage().urlFor(resourceReference.getPath());

			// generate the href attribute
			tag.put("href", Strings.replaceAll(url, "&", "&amp;"));
		}

		/**
		 * @see wicket.MarkupContainer#isTransparentResolver()
		 */
		public boolean isTransparentResolver()
		{
			return true;
		}
	}
}