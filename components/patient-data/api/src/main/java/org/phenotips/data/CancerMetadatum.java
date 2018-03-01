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

import org.xwiki.stability.Unstable;

import java.util.Locale;

import org.json.JSONObject;

/**
 * Information about a {@link Patient patient}'s {@link Cancer cancer} properties (qualifiers).
 *
 * @version $Id$
 * @since 1.4
 */
@Unstable
public interface CancerMetadatum extends VocabularyProperty
{
    /**
     * The supported qualifier types.
     */
    enum Type
    {
        /** The age at which the cancer is diagnosed. */
        AGE_AT_DIAGNOSIS("ageAtDiagnosis"),
        /** The numeric age estimate at which the cancer is diagnosed. */
        NUMERIC_AGE_AT_DIAGNOSIS("numericAgeAtDiagnosis"),
        /** The type of cancer -- can be primary or metastasized. */
        PRIMARY("primary"),
        /** The localization with respect to the side of the body of the specified cancer. */
        LATERALITY("laterality");

        /** @see #getId() */
        private final String id;

        /**
         * Constructor that initializes the {@link #getId() vocabulary term identifier}.
         *
         * @param id an identifier, in the format {@code VOCABULARY:termId}
         * @see #getId()
         */
        Type(final String id)
        {
            this.id = id;
        }

        @Override
        public String toString()
        {
            return this.name().toLowerCase(Locale.ROOT);
        }

        /**
         * Get the vocabulary term identifier associated to this type of qualifier.
         *
         * @return an identifier, in the format {@code VOCABULARY:termId}
         */
        public String getId()
        {
            return this.id;
        }
    }

    /**
     * Retrieve information about these qualifiers in a JSON format. For example:
     *
     * <pre>
     * {
     *   "id": "HP:0100615",
     *   "ageAtDiagnosis": "before_40",
     *   "numericAgeAtDiagnosis": 31,
     *   "primary": true,
     *   "laterality": "l"
     * }
     * </pre>
     *
     * @return the meta-feature data, using the org.json classes
     */
    @Override
    JSONObject toJSON();
}
