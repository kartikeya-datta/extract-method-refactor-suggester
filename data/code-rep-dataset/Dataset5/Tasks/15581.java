public AbstractTextComponent(final String id, final IModel<T> model)

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
package org.apache.wicket.markup.html.form;

import java.text.SimpleDateFormat;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.string.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Abstract base class for TextArea and TextField.
 * 
 * @author Jonathan Locke
 * 
 * @param <T>
 *            The model object type
 */
public abstract class AbstractTextComponent<T> extends FormComponent<T>
{
	// Flag for the type resolving. FLAG_RESERVED1-3 is taken by form component
	private static final int TYPE_RESOLVED = Component.FLAG_RESERVED4;

	/** Log for reporting. */
	private static final Logger log = LoggerFactory.getLogger(AbstractTextComponent.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Text components that implement this interface are know to be able to provide a pattern for
	 * formatting output and parsing input. This can be used by for instance date picker components
	 * which are based on Javascript and need some knowledge as to how to communicate properly via
	 * request parameters.
	 */
	public static interface ITextFormatProvider
	{
		/**
		 * Gets the pattern for printing output and parsing input.
		 * 
		 * @return The text pattern
		 * @see SimpleDateFormat
		 */
		String getTextFormat();
	}

	/**
	 * @see org.apache.wicket.Component#Component(String)
	 */
	public AbstractTextComponent(String id)
	{
		super(id);
		setConvertEmptyInputStringToNull(true);
	}

	/**
	 * @see org.apache.wicket.Component#Component(String, IModel)
	 */
	public AbstractTextComponent(final String id, final IModel<?> model)
	{
		super(id, model);
		setConvertEmptyInputStringToNull(true);
	}

	/**
	 * Should the bound object become <code>null</code> when the input is empty?
	 * 
	 * @return <code>true</code> when the value will be set to <code>null</code> when the input
	 *         is empty.
	 */
	public final boolean getConvertEmptyInputStringToNull()
	{
		return getFlag(FLAG_CONVERT_EMPTY_INPUT_STRING_TO_NULL);
	}

	/**
	 * TextFields return an empty string even if the user didn't type anything in them. To be able
	 * to work nicely with validation, this method returns false.
	 * 
	 * @see org.apache.wicket.markup.html.form.FormComponent#isInputNullable()
	 */
	@Override
	public boolean isInputNullable()
	{
		return false;
	}

	/**
	 * 
	 * @see org.apache.wicket.markup.html.form.FormComponent#convertInput()
	 */
	@Override
	protected void convertInput()
	{
		// Stateless forms don't have to be rendered first, convertInput could be called before
		// onBeforeRender calling resolve type here again to check if the type is correctly set.
		resolveType();
		super.convertInput();
	}

	/**
	 * If the type is not set try to guess it if the model supports it.
	 * 
	 * @see org.apache.wicket.Component#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();
		resolveType();
	}

	/**
	 * 
	 */
	private void resolveType()
	{
		if (!getFlag(TYPE_RESOLVED) && getType() == null)
		{
			// Set the type, but only if it's not a String (see WICKET-606).
			// Otherwise, getConvertEmptyInputStringToNull() won't work.
			Class<?> type = getModelType(getModel());
			if (!String.class.equals(type))
			{
				setType(type);
			}
			setFlag(TYPE_RESOLVED, true);
		}
	}

	/**
	 * 
	 * @param model
	 * @return the type of the model object or <code>null</code>
	 */
	private Class<?> getModelType(IModel<?> model)
	{
		if (model instanceof IObjectClassAwareModel)
		{
			Class<?> objectClass = ((IObjectClassAwareModel<?>)model).getObjectClass();
			if (objectClass == null)
			{
				log.warn("Couldn't resolve model type of " + model + " for " + this +
					", please set the type yourself.");
			}
			return objectClass;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Should the bound object become <code>null</code> when the input is empty?
	 * 
	 * @param flag
	 *            the value to set this flag.
	 * @return this
	 */
	public final FormComponent<?> setConvertEmptyInputStringToNull(boolean flag)
	{
		setFlag(FLAG_CONVERT_EMPTY_INPUT_STRING_TO_NULL, flag);
		return this;
	}

	/**
	 * @see org.apache.wicket.markup.html.form.FormComponent#convertValue(String[])
	 */
	@Override
	protected T convertValue(String[] value) throws ConversionException
	{
		String tmp = value != null && value.length > 0 ? value[0] : null;
		if (getConvertEmptyInputStringToNull() && Strings.isEmpty(tmp))
		{
			return null;
		}
		return super.convertValue(value);
	}

	/**
	 * @see FormComponent#supportsPersistence()
	 */
	@Override
	protected boolean supportsPersistence()
	{
		return true;
	}
}