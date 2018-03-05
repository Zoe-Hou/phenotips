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

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.objects.BaseObject;
import com.xpn.xwiki.objects.BaseStringProperty;

/**
 * Information about a specific cancer recorded for a {@link Patient patient}.
 *
 * @version $Id$
 * @since 1.4
 */
@Unstable
public interface Cancer extends VocabularyProperty
{
    /** The XClass used for storing cancer data. */
    EntityReference CLASS_REFERENCE = new EntityReference("CancerClass", EntityType.DOCUMENT,
            Constants.CODE_SPACE_REFERENCE);

    /**
     * The supported cancer properties.
     */
    enum CancerProperty
    {
        /** The cancer identifier. */
        CANCER("cancer")
        {
            @Nullable
            @Override
            public String extractValue(@Nonnull final BaseObject cancer)
            {
                final BaseStringProperty field = (BaseStringProperty) cancer.getField(getProperty());
                return field == null ? null : field.getValue();
            }

            @Override
            public void writeValue(@Nonnull final BaseObject cancer,
                                   @Nullable final Object value,
                                   @Nonnull final XWikiContext context)
            {
                if (valueIsValid(value)) {
                    cancer.set(getProperty(), value, context);
                }
            }

            @Override
            public boolean valueIsValid(@Nullable final Object value)
            {
                return value != null && value instanceof String;
            }

            @Nonnull
            @Override
            public String getJsonProperty()
            {
                return "id";
            }
        },

        /** The field denoting if the individual is affected with the cancer. */
        AFFECTED("affected")
        {
            @Nullable
            @Override
            public Boolean extractValue(@Nonnull final BaseObject cancer)
            {
                final int value = cancer.getIntValue(getProperty(), -1);
                return value == -1 ? null : (value == 1);
            }

            @Override
            public void writeValue(@Nonnull final BaseObject cancer,
                                   @Nullable final Object value,
                                   @Nonnull final XWikiContext context)
            {
                if (valueIsValid(value)) {
                    cancer.set(getProperty(), (Boolean) value ? 1 : 0, context);
                }
            }

            @Override
            public boolean valueIsValid(@Nullable final Object value)
            {
                return value != null && value instanceof Boolean;
            }
        };

        /** @see #getProperty() */
        private final String property;

        /**
         * Constructor that initializes the property.
         *
         * @param property the name of the cancer property
         * @see #getName()
         */
        CancerProperty(@Nonnull final String property)
        {
            this.property = property;
        }

        /**
         * Extracts a value from {@code cancer} for {@link #getProperty()}.
         *
         * @param cancer the {@link BaseObject} cancer that contains various properties
         * @return a value for {@link #getProperty()} stored in {@code cancer}; {@code null} if no such value stored
         */
        @Nullable
        public abstract Object extractValue(@Nonnull BaseObject cancer);

        /**
         * Writes a value to {@code cancer} for {@link #getProperty()}.
         *
         * @param cancer the {@link BaseObject} cancer object where data will be written
         * @param value the value to write
         * @param context the current {@link XWikiContext}
         */
        public abstract void writeValue(@Nonnull BaseObject cancer,
                                        @Nullable Object value,
                                        @Nonnull XWikiContext context);

        /**
         * Checks if the value selected for the type of property is valid.
         *
         * @param value the potential value for {@link #getProperty()}
         * @return true iff the value is valid
         */
        public abstract boolean valueIsValid(@Nullable Object value);

        @Override
        public String toString()
        {
            return getJsonProperty();
        }

        /**
         * Get the type of property.
         *
         * @return the type of property
         */
        @Nonnull
        public String getProperty()
        {
            return this.property;
        }

        /**
         * Returns the name of this property for JSON.
         *
         * @return the name of the property
         */
        @Nonnull
        public String getJsonProperty()
        {
            return this.property;
        }
    }

    /**
     * Returns true iff the {@link Patient} is affected with the cancer.
     *
     * @return true iff the {@link Patient} is affected with the cancer, false otherwise
     */
    boolean isAffected();

    /**
     * A collection of {@link CancerQualifier} associated with the cancer. Cancer qualifier objects may contain data
     * such as cancer type, age at diagnosis, and laterality. Each cancer may have several {@link CancerQualifier}
     * objects associated with it, signifying multiple occurrences.
     *
     * @return a collection of {@link CancerQualifier} associated with the given {@link Cancer}
     */
    @Nonnull
    Collection<CancerQualifier> getQualifiers();

    /**
     * Merges data contained in {@code cancer} into this object iff the two objects have the same {@link #getId() id}.
     * If both objects contain colliding values for the same property, a choice will be made in favour of the value in
     * {@code cancer}.
     *
     * @param cancer the {@link Cancer} object containing data that will be merged in
     * @return the updated {@link Cancer} object
     * @throws IllegalArgumentException iff the two objects do not have the same {@link #getId()}
     */
    @Nonnull
    Cancer mergeData(@Nonnull Cancer cancer);

    /**
     * Writes cancer data to {@code baseObject}.
     *
     * @param baseObject the {@link BaseObject} that will store cancer data
     * @param context the current {@link XWikiContext}
     */
    void write(@Nonnull BaseObject baseObject, @Nonnull XWikiContext context);

    /**
     * Gets the value associated with the provided {@code property}, {@code null} if the {@code property} has no value
     * associated with it.
     *
     * @param property the {@link Cancer.CancerProperty} of interest
     * @return the {@link Object value} associated with the {@code property}, {@code null} if no such value exists
     */
    @Nullable
    Object getProperty(@Nonnull Cancer.CancerProperty property);
}
