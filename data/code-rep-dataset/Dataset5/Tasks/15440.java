url = component.getPage().urlFor(((SharedResource)resource).getPath());

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
package wicket.markup.html.image.resource;

import java.io.Serializable;
import java.util.Locale;

import wicket.Application;
import wicket.Component;
import wicket.IResourceFactory;
import wicket.IResourceListener;
import wicket.SharedResource;
import wicket.Resource;
import wicket.WicketRuntimeException;
import wicket.markup.ComponentTag;
import wicket.util.parse.metapattern.Group;
import wicket.util.parse.metapattern.MetaPattern;
import wicket.util.parse.metapattern.OptionalMetaPattern;
import wicket.util.parse.metapattern.parsers.MetaPatternParser;
import wicket.util.string.Strings;

/**
 * This class contains the logic for extracting static image resources
 * referenced by the SRC attribute of component tags and keeping these static
 * image resources in sync with the component locale.
 * <p>
 * If no image is specified by the SRC attribute of an IMG tag, then any VALUE
 * attribute is inspected. If there is a VALUE attribute, it must be of the form
 * "[factoryName]:[sharedImageName]?:[specification]". [factoryName] is the name
 * of a resource factory that has been added to Application (for example,
 * DefaultButtonImageResourceFactory is installed by default under the name
 * "buttonFactory"). The [sharedImageName] value is optional and gives a name
 * under which a given generated image is shared. For example, a cancel button
 * image generated by the VALUE attribute "buttonFactory:cancelButton:Cancel" is
 * shared under the name "cancelButton" and this specification will cause a
 * component to reference the same image resource no matter what page it appears
 * on, which is a very convenient and efficient way to create and share images.
 * The [specification] string which follows the second colon is passed directly
 * to the image factory and its format is dependent on the specific image
 * factory. For details on the default buttonFactory, see
 * {@link wicket.markup.html.image.resource.DefaultButtonImageResourceFactory}.
 * <p>
 * Finally, if there is no SRC attribute and no VALUE attribute, the Image
 * component's model is converted to a String and that value is used as a path
 * to load the image.
 * 
 * @author Jonathan Locke
 */
public final class LocalizedImageResource implements Serializable, IResourceListener
{
	/** The component that is referencing this image resource */
	private Component component;

	/** True if the resource was generated by a factory */
	private boolean isFactoryResource;

	/** The locale of the image resource */
	private Locale locale;

	/** The image resource this image component references */
	private Resource resource;

	/** The style of the image resource */
	private String style;

	/**
	 * Parses image value specifications of the form "[factoryName]:
	 * [shared-image-name]?:[specification]"
	 * 
	 * @author Jonathan Locke
	 */
	private static final class ImageValueParser extends MetaPatternParser
	{
		/** Factory name */
		private static final Group factoryName = new Group(MetaPattern.VARIABLE_NAME);

		/** Image name */
		private static final Group imageName = new Group(MetaPattern.VARIABLE_NAME);

		/** Factory name */
		private static final Group specification = new Group(MetaPattern.ANYTHING_NON_EMPTY);

		/** Meta pattern. */
		private static final MetaPattern pattern = new MetaPattern(new MetaPattern[] { factoryName,
				MetaPattern.COLON, new OptionalMetaPattern(new MetaPattern[] { imageName }),
				MetaPattern.COLON, specification });

		/**
		 * Construct.
		 * 
		 * @param input
		 *            to parse
		 */
		private ImageValueParser(final CharSequence input)
		{
			super(pattern, input);
		}

		/**
		 * @return The factory name
		 */
		private String getFactoryName()
		{
			return factoryName.get(matcher());
		}

		/**
		 * @return Returns the imageName.
		 */
		private String getImageName()
		{
			return imageName.get(matcher());
		}

		/**
		 * @return Returns the specification.
		 */
		private String getSpecification()
		{
			return specification.get(matcher());
		}
	}

	/**
	 * Constructor
	 * 
	 * @param component
	 *            The component that owns this localized image resource
	 */
	public LocalizedImageResource(final Component component)
	{
		this.component = component;
	}
	
	/**
	 * Binds this resource if it is shared
	 */
	public final void bind()
	{
		// Rebind resource to application if it is a shared reference
		if (resource instanceof SharedResource)
		{
			((SharedResource)resource).bind(component.getApplication());
		}
	}

	/**
	 * @see wicket.IResourceListener#onResourceRequested()
	 */
	public final void onResourceRequested()
	{
		bind();
		resource.onResourceRequested();
	}
	
	/**
	 * @param resource
	 *            The resource to set.
	 */
	public final void setResource(final Resource resource)
	{
		this.resource = resource;
		bind();
	}

	/**
	 * @param tag
	 *            The tag to inspect for an optional src attribute that might
	 *            reference an image.
	 * @throws WicketRuntimeException
	 *             Thrown if an image is required by the caller, but none can be
	 *             found.
	 */
	public final void setSrcAttribute(final ComponentTag tag)
	{
		// If locale has changed from the initial locale used to attach image
		// resource, then we need to reload the resource in the new locale
		if ((locale != null && locale != component.getLocale())
				|| (style != null && style.equals(component.getStyle())))
		{
			// If the image is a shared resource
			if (resource instanceof SharedResource)
			{
				// If the shared image resource was generated by a resource
				// factory
				if (isFactoryResource)
				{
					// force it to regenerate!
					this.resource = null;
				}
				else
				{
					// Change the locale and style for the named resource so it
					// will rebind when next accessed.
					final SharedResource namedResource = (SharedResource)resource;
					namedResource.setLocale(locale);
					namedResource.setStyle(style);
				}
			}
			else
			{
				// Resource is not shared, so it should simply be re-loaded
				this.resource = null;
			}
		}

		// Need to load image resource for this component?
		if (resource == null)
		{
			// Get SRC attribute of tag
			final String src = tag.getString("src");
			if (src != null)
			{
				// Try to load static image
				this.resource = loadStaticImage(src);
			}
			else
			{
				// Get VALUE attribute of tag
				final String value = tag.getString("value");
				if (value != null)
				{
					// Try to generate an image using an image factory
					this.resource = newImage(value);
					this.isFactoryResource = true;
				}
				else
				{
					// Load static image using model object as the path
					this.resource = loadStaticImage(component.getModelObjectAsString());
				}
			}
		}

		// Save component locale and style so we can detect changes
		this.locale = component.getLocale();
		this.style = component.getStyle();

		// Get URL for resource
		final String url;
		if (this.resource instanceof SharedResource)
		{
			// Create URL to shared resource
			url = component.getPage().urlFor("shared/" + ((SharedResource)resource).getPath());
		}
		else
		{
			// Create URL to component
			url = component.urlFor(IResourceListener.class);
		}

		// Set the SRC attribute to point to the component or shared resource
		tag.put("src", component.getResponse().encodeURL(url).replaceAll("&", "&amp;"));
	}

	/**
	 * Tries to load static image at the given path and throws an exception if
	 * the image cannot be located.
	 * 
	 * @param path
	 *            The path to the image
	 * @return The loaded image resource
	 * @throws WicketRuntimeException
	 *             Thrown if the image cannot be located
	 */
	private ImageResource loadStaticImage(final String path)
	{
		final Package basePackage = component.findParentWithAssociatedMarkup().getClass()
				.getPackage();
		final ImageResource resource = StaticImageResource.get(basePackage, path, component
				.getLocale(), component.getStyle());
		if (resource == null)
		{
			throw new WicketRuntimeException("Could not load static image resource using path '"
					+ path + "'");
		}
		return resource;
	}

	/**
	 * Generates an image resource based on the attribute values on tag
	 * 
	 * @param value
	 *            The value to parse
	 * @return The image resource
	 */
	private Resource newImage(final String value)
	{
		// Parse value
		final ImageValueParser valueParser = new ImageValueParser(value);

		// Does value match parser?
		if (valueParser.matches())
		{
			// Look up factory
			final IResourceFactory factory = component.getApplication().getResourceFactory(
					valueParser.getFactoryName());

			// Found factory?
			if (factory == null)
			{
				throw new WicketRuntimeException("Could not find image resource factory named "
						+ valueParser.getFactoryName());
			}

			// Have factory create new image resource
			final ImageResource resource = (ImageResource)factory.newResource(valueParser
					.getSpecification(), locale, style);

			// Stash resource in application for sharing
			final String name = valueParser.getImageName();
			if (!Strings.isEmpty(name))
			{
				component.getApplication().addResource(name, resource);
				return new SharedResource(Application.class, name);
			}
			return resource;
		}
		else
		{
			throw new WicketRuntimeException(
					"Could not generate image for value attribute '"
							+ value
							+ "'.  Was expecting a value attribute of the form \"[resourceFactoryName]:[sharedResourceName]?:[factorySpecification]\".");
		}
	}
}