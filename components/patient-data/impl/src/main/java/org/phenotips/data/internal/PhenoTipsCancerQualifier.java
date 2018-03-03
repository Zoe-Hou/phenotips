/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/
 */
package org.phenotips.data.internal;

import org.phenotips.data.CancerQualifier;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.objects.BaseObject;

/**
 * Implementation of patient data based on the XWiki data model, where cancer qualifier data is represented by
 * properties in objects of type {@code PhenoTips.CancerQualifierClass}.
 *
 * @version $Id$
 * @since 1.4
 */
public class PhenoTipsCancerQualifier extends AbstractPhenoTipsVocabularyProperty implements CancerQualifier
{
    /** All the permitted qualifier properties. */
    private static final CancerQualifierProperty[] PROPERTIES = CancerQualifierProperty.values();

    /** The qualifier data. */
    private final Map<String, Object> qualifier;

    /**
     * The constructor that takes the qualifier {@link BaseObject} as a parameter.
     *
     * @param qualifierObj the {@link BaseObject} that contains qualifier data
     * @throws IllegalArgumentException if no associated cancer is provided
     */
    PhenoTipsCancerQualifier(@Nonnull final BaseObject qualifierObj)
    {
        super((String) CancerQualifierProperty.CANCER.extractValue(qualifierObj));
        this.qualifier = Arrays.stream(PROPERTIES)
            .collect(LinkedHashMap::new, (m, p) -> extractValueFromBaseObj(qualifierObj, m, p), LinkedHashMap::putAll);
    }

    /**
     * The constructor that takes a {@link JSONObject} as a parameter.
     *
     * @param json the {@link JSONObject} that contains qualifier data
     * @throws IllegalArgumentException if no associated cancer is provided
     */
    PhenoTipsCancerQualifier(@Nonnull final JSONObject json)
    {
        super(json.optString(CancerQualifierProperty.CANCER.getProperty(), null));
        this.qualifier = Arrays.stream(PROPERTIES)
            .collect(LinkedHashMap::new, (m, p) -> extractValueFromJson(json, m, p), LinkedHashMap::putAll);
    }

    @Override
    public void write(@Nonnull final BaseObject baseObject, final @Nonnull XWikiContext context)
    {
        this.qualifier.forEach((property, value) -> writeProperty(baseObject, property, value, context));
    }

    @Nullable
    @Override
    public Object getProperty(@Nonnull final CancerQualifierProperty property)
    {
        return this.qualifier.get(property.getProperty());
    }

    @Override
    @Nonnull
    public JSONObject toJSON()
    {
        return new JSONObject(this.qualifier);
    }

    /**
     * Sets the {@code value} for {@code property} if the {@code value} is valid.
     *
     * @param property the {@link CancerQualifierProperty} of interest
     * @param value the value for the property
     */
    public void setProperty(@Nonnull final CancerQualifierProperty property, @Nullable final Object value)
    {
        // Cannot change the identifier.
        if (property != CancerQualifierProperty.CANCER && property.valueIsValid(value)) {
            this.qualifier.put(property.getProperty(), value);
        }
    }

    /**
     * Writes a {@code property} to {@code baseObject} if the {@code property} is defined.
     *
     * @param baseObject the {@link BaseObject} where data will be written
     * @param property the property of interest
     * @param value the property value
     * @param context the current {@link XWikiContext}
     */
    private void writeProperty(@Nonnull final BaseObject baseObject,
        @Nonnull final String property,
        @Nullable final Object value,
        @Nonnull final XWikiContext context)
    {
        if (value != null) {
            baseObject.set(property, value, context);
        }
    }

    /**
     * Extracts the property value from the provided {@code json}, and populates the {@code propertyMap} if there is a
     * value specified for the property.
     *
     * @param json the {@link JSONObject} that contains the values for qualifier properties
     * @param propertyMap the {@link Map} of qualifier properties to their values
     * @param property the {@link CancerQualifierProperty} property of interest
     */
    private void extractValueFromJson(@Nonnull final JSONObject json,
                                      @Nonnull final Map<String, Object> propertyMap,
                                      @Nonnull final CancerQualifierProperty property)
    {
        final String propertyStr = property.getProperty();
        final Object value = json.opt(propertyStr);
        if (property.valueIsValid(value)) {
            propertyMap.put(propertyStr, value);
        }
    }

    /**
     * Extracts the property value from the provided {@code qualifierObj}, and populates the {@code propertyMap} if
     * there is a value specified for the property.
     *
     * @param qualifierObj the {@link BaseObject} that contains the values for qualifier properties
     * @param propertyMap the {@link Map} of qualifier properties to their values
     * @param property the {@link CancerQualifierProperty} property of interest
     */
    private void extractValueFromBaseObj(@Nonnull final BaseObject qualifierObj,
                                         @Nonnull final LinkedHashMap<String, Object> propertyMap,
                                         @Nonnull final CancerQualifierProperty property)
    {
        final String propertyStr = property.getProperty();
        final Object value = property.extractValue(qualifierObj);
        if (property.valueIsValid(value)) {
            propertyMap.put(propertyStr, value);
        }
    }
}
