setChoices(new AbstractReadOnlyModel<List< ? extends Locale>>()

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
package org.apache.wicket.examples.dates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Session;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.examples.WicketExamplePage;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Demonstrates components from the wicket-date project and a bunch of locale fiddling.
 */
public class DatesPage extends WicketExamplePage
{
	/**
	 * Choice for a locale.
	 */
	private final class LocaleChoiceRenderer extends ChoiceRenderer<Locale>
	{
		/**
		 * Constructor.
		 */
		public LocaleChoiceRenderer()
		{
		}

		/**
		 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(Object)
		 */
		@Override
		public Object getDisplayValue(Locale locale)
		{
			String enName = locale.getDisplayName(LOCALE_EN);
			String localizedName = locale.getDisplayName(selectedLocale);
			return localizedName + (!enName.equals(localizedName) ? (" (" + enName + ")") : "");
		}
	}

	/**
	 * Dropdown with Locales.
	 */
	private final class LocaleDropDownChoice extends DropDownChoice<Locale>
	{
		/**
		 * Construct.
		 * 
		 * @param id
		 *            component id
		 */
		public LocaleDropDownChoice(String id)
		{
			super(id);
			// sort locales on strings of selected locale
			setChoices(new AbstractReadOnlyModel<List<Locale>>()
			{
				@Override
				public List<Locale> getObject()
				{
					getSelectedLocale();
					List<Locale> locales = new ArrayList<Locale>(LOCALES);
					Collections.sort(locales, new Comparator<Locale>()
					{
						public int compare(Locale o1, Locale o2)
						{
							return o1.getDisplayName(selectedLocale).compareTo(
								o2.getDisplayName(selectedLocale));
						}
					});
					return locales;
				}
			});
			setChoiceRenderer(new LocaleChoiceRenderer());
			setModel(new PropertyModel<Locale>(DatesPage.this, "selectedLocale"));
		}

		/**
		 * @see org.apache.wicket.markup.html.form.DropDownChoice#onSelectionChanged(java.lang.Object)
		 */
		@Override
		public void onSelectionChanged(Locale newSelection)
		{
		}

		/**
		 * @see org.apache.wicket.markup.html.form.DropDownChoice#wantOnSelectionChangedNotifications()
		 */
		@Override
		protected boolean wantOnSelectionChangedNotifications()
		{
			return true;
		}
	}

	private static final Locale LOCALE_EN = new Locale("en");

	private static final List<Locale> LOCALES;
	static
	{
		LOCALES = Arrays.asList(Locale.getAvailableLocales());
	}

	private final Date date = new Date();

	private Locale selectedLocale = LOCALE_EN;

	/**
	 * Constructor
	 */
	public DatesPage()
	{
		selectedLocale = Session.get().getLocale();
		Form localeForm = new Form("localeForm");
		localeForm.add(new LocaleDropDownChoice("localeSelect"));
		localeForm.add(new Link("localeUSLink")
		{
			@Override
			public void onClick()
			{
				selectedLocale = LOCALE_EN;
			}
		});
		add(localeForm);
		DateTextField dateTextField = new DateTextField("dateTextField", new PropertyModel(this,
			"date"), new StyleDateConverter("S-", true))
		{
			@Override
			public Locale getLocale()
			{
				return selectedLocale;
			}
		};
		Form form = new Form("form")
		{
			@Override
			protected void onSubmit()
			{
				info("set date to " + date);
			}
		};
		add(form);
		form.add(dateTextField);
		dateTextField.add(new DatePicker());
		add(new FeedbackPanel("feedback"));
	}

	/**
	 * @return the selected locale
	 */
	public final Locale getSelectedLocale()
	{
		return selectedLocale;
	}

	/**
	 * @param selectedLocale
	 */
	public final void setSelectedLocale(Locale selectedLocale)
	{
		this.selectedLocale = selectedLocale;
	}
}
 No newline at end of file