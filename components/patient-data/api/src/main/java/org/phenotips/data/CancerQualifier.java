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

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONObject;

/**
 * Information about the {@link Patient patient}'s {@link Cancer cancer} metadatum (qualifiers).
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
     * The supported qualifier types.
     */
    enum Meta
    {
        /** The age at which the cancer is diagnosed. */
        AGE_AT_DIAGNOSIS("ageAtDiagnosis"),
        /** The numeric age estimate at which the cancer is diagnosed. */
        NUMERIC_AGE_AT_DIAGNOSIS("numericAgeAtDiagnosis"),
        /** The type of cancer -- can be primary or metastasized. */
        PRIMARY("primary"),
        /** The localization with respect to the side of the body of the specified cancer. */
        LATERALITY("laterality");

        /** @see #getName() */
        private final String name;

        /**
         * Constructor that initializes the meta-datum.
         *
         * @param name the name of the meta-datum
         * @see #getName()
         */
        Meta(final String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return this.name().toLowerCase(Locale.ROOT);
        }

        /**
         * Get the name of this meta-datum.
         *
         * @return the name of the meta-datum
         */
        public String getName()
        {
            return this.name;
        }

        /**
         * Get all possible meta values.
         *
         * @return a {@link Set} of all possible meta values
         */
        public static Set<String> getNames()
        {
            return Arrays.stream(Meta.values()).map(Meta::getName).collect(Collectors.toSet());
        }
    }

    /**
     * Retrieve information about this metadata in a JSON format. For example:
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
