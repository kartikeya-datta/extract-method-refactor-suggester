package org.apache.wicket.request.resource;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.ng.resource;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.util.time.Time;

public abstract class DynamicImageResource extends AbstractResource
{
	/** The image type */
	private String format = "png";

	/** The last modified time of this resource */
	private Time lastModifiedTime;


	public DynamicImageResource()
	{
	}

	/**
	 * Creates a dynamic resource from for the given locale
	 * 
	 * @param format
	 *            The image format ("png", "jpeg", etc)
	 */
	public DynamicImageResource(String format)
	{
		setFormat(format);
	}

	/**
	 * @return Returns the image format.
	 */
	public synchronized final String getFormat()
	{
		return format;
	}

	/**
	 * Sets the format of this resource
	 * 
	 * @param format
	 *            The format (jpg, png or gif..)
	 */
	public synchronized final void setFormat(String format)
	{
		this.format = format;
	}

	/**
	 * set the last modified time for this resource.
	 * 
	 * @param time
	 */
	protected synchronized void setLastModifiedTime(Time time)
	{
		lastModifiedTime = time;
	}

	/**
	 * @param image
	 *            The image to turn into data
	 * @return The image data for this dynamic image
	 */
	protected byte[] toImageData(final BufferedImage image)
	{
		try
		{
			// Create output stream
			final ByteArrayOutputStream out = new ByteArrayOutputStream();

			// Write image using any matching ImageWriter
			ImageIO.write(image, format, out);

			// Return the image data
			return out.toByteArray();
		}
		catch (IOException e)
		{
			throw new WicketRuntimeException("Unable to convert dynamic image to stream", e);
		}
	}

	/**
	 * Get image data for our dynamic image resource. If the subclass regenerates the data, it
	 * should set the lastModifiedTime when it does so. This ensures that image caching works
	 * correctly.
	 * 
	 * @param attributes
	 * 
	 * @return The image data for this dynamic image
	 */
	protected abstract byte[] getImageData(Attributes attributes);


	protected void configureResponse(final ResourceResponse response, final Attributes attributes)
	{

	}

	@Override
	protected ResourceResponse newResourceResponse(final Attributes attributes)
	{
		final ResourceResponse response = new ResourceResponse();

		if (lastModifiedTime != null)
		{
			response.setLastModified(lastModifiedTime.toDate());
		}
		else
		{
			response.setLastModified(new Date());
		}

		if (response.dataNeedsToBeWritten(attributes))
		{
			response.setContentType("image/" + getFormat());

			response.setContentDisposition(ContentDisposition.INLINE);

			response.setWriteCallback(new WriteCallback()
			{
				@Override
				public void writeData(final Attributes attributes)
				{
					attributes.getResponse().write(getImageData(attributes));
				}
			});

			configureResponse(response, attributes);
		}

		return response;
	}
}