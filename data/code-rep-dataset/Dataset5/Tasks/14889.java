numberFormat = newNumberFormat(locale);

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
package org.apache.wicket.util.convert.converters;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base class for all number converters.
 * 
 * @author Jonathan Locke
 * 
 */
public abstract class AbstractDecimalConverter extends AbstractNumberConverter
{
	private static final long serialVersionUID = 1L;

	/** The date format to use */
	private final Map<Locale, NumberFormat> numberFormats = new ConcurrentHashMap<Locale, NumberFormat>();

	/**
	 * @param locale
	 * @return Returns the numberFormat.
	 */
	@Override
	public NumberFormat getNumberFormat(Locale locale)
	{
		NumberFormat numberFormat = numberFormats.get(locale);
		if (numberFormat == null)
		{
			numberFormat = NumberFormat.getInstance(locale);
			if (numberFormat instanceof DecimalFormat)
				((DecimalFormat)numberFormat).setParseBigDecimal(true);

			numberFormats.put(locale, numberFormat);
		}
		return (NumberFormat)numberFormat.clone();
	}

	/**
	 * Creates a new {@link NumberFormat} for the given locale. The instance is later cached and is
	 * accessible through {@link #getNumberFormat(Locale)}
	 * 
	 * @param locale
	 * @return number format
	 */
	protected NumberFormat newNumberFormat(Locale locale)
	{
		return NumberFormat.getInstance(locale);
	}

	/**
	 * @param locale
	 *            The Locale that was used for this NumberFormat
	 * @param numberFormat
	 *            The numberFormat to set.
	 */
	public final void setNumberFormat(final Locale locale, final NumberFormat numberFormat)
	{
		numberFormats.put(locale, numberFormat);
	}
}