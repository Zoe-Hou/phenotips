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
package org.phenotips.data;

import org.phenotips.Constants;
import org.xwiki.model.EntityType;
import org.xwiki.model.reference.EntityReference;
import org.xwiki.stability.Unstable;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.objects.BaseObject;
import com.xpn.xwiki.objects.BaseStringProperty;

/**
 * Information about the {@link Patient patient}'s {@link CancerQualifier qualifier} metadatum.
 *
 * @version $Id$
 * @since 1.4
 */
@Unstable
public interface CancerQualifier extends VocabularyProperty
{
    /** The XClass used for storing cancer metadata. */
    EntityReference CLASS_REFERENCE = new EntityReference("CancerQualifierClass", EntityType.DOCUMENT,
            Constants.CODE_SPACE_REFERENCE);

    /**
     * The supported qualifier properties.
     */
    enum CancerQualifierProperty
    {
        /** An age at which the cancer is diagnosed. */
        AGE_AT_DIAGNOSIS("ageAtDiagnosis"),

        /** The numeric age estimate at which the cancer is diagnosed. */
        NUMERIC_AGE_AT_DIAGNOSIS("numericAgeAtDiagnosis") {
            @Nullable
            @Override
            public Object extractValue(@Nonnull final BaseObject qualifier)
            {
                final int value = qualifier.getIntValue(this.property, -1);
                return value == -1 ? null : value;
            }

            @Override
            public boolean valueIsValid(@Nullable final Object value) {
                return value != null && value instanceof Integer;
            }
        },

        /** The type of cancer -- can be primary or metastasized (if primary is false). */
        PRIMARY("primary") {
            @Nullable
            @Override
            public Object extractValue(@Nonnull final BaseObject qualifier)
            {
                final int value = qualifier.getIntValue(this.property, -1);
                return value == -1 ? null : (value == 1);
            }

            @Override
            public boolean valueIsValid(@Nullable final Object value) {
                return value != null && value instanceof Boolean;
            }
        },

        /** The localization with respect to the side of the body of the specified cancer. */
        LATERALITY("laterality");

        /** @see #getProperty() */
        private final String property;

        /**
         * Constructor that initializes the property.
         *
         * @param property the name of the qualifier property
         * @see #getName()
         */
        CancerQualifierProperty(@Nonnull final String property)
        {
            this.property = property;
        }

        /**
         * Extracts a value from {@code qualifier} for {@link #getProperty()}.
         *
         * @param qualifier the {@link BaseObject} qualifier that contains various properties
         * @return a value for {@link #getProperty()} stored in {@code qualifier}; {@code null} if no such value stored
         */
        @Nullable
        public Object extractValue(@Nonnull final BaseObject qualifier)
        {
            final BaseStringProperty field = (BaseStringProperty) qualifier.getField(this.property);
            return field == null ? null : field.getValue();
        }

        /**
         * Checks if the value selected for the type of property is valid.
         *
         * @param value the potential value for {@link #getProperty()}
         * @return true iff the value is valid
         */
        public boolean valueIsValid(@Nullable final Object value)
        {
            return value != null && value instanceof String;
        }

        @Override
        public String toString()
        {
            return this.name().toLowerCase(Locale.ROOT);
        }

        /**
         * Get the name of this property.
         *
         * @return the name of the property
         */
        @Nonnull
        public String getProperty()
        {
            return this.property;
        }
    }

    /**
     * Writes cancer qualifier data to {@code baseObject}.
     *
     * @param baseObject the {@link BaseObject} that will store cancer qualifier data
     * @param context the current {@link XWikiContext}
     */
    void write(@Nonnull BaseObject baseObject, @Nonnull XWikiContext context);

    /**
     * Gets the value associated with the provided {@code property}, {@code null} if the {@code property} has no value
     * associated with it.
     *
     * @param property the {@link CancerQualifierProperty} of interest
     * @return the {@link Object value} associted with the {@code property}, {@code null} if no such value exists
     */
    @Nullable
    Object getProperty(@Nonnull CancerQualifierProperty property);
}
