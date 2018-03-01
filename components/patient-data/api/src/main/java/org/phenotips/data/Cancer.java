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

import java.util.Collection;

import org.json.JSONObject;

/**
 * Information about a specific cancer recorded for a {@link Patient patient}.
 *
 * @version $Id$
 * @since 1.4
 */
@Unstable
public interface Cancer extends VocabularyProperty
{
    /**
     * Returns true iff the {@link Patient} is affected with the cancer.
     *
     * @return true iff the {@link Patient} is affected with the cancer, false otherwise
     */
    boolean isAffected();

    /**
     * A collection of {@link CancerMetadatum} associated with the cancer. Cancer qualifiers include data such
     * as cancer type, age at diagnosis, and laterality. Each cancer may have several {@link CancerMetadatum}
     * associated with it.
     *
     * @return a collection of {@link CancerMetadatum} associated with the given {@link Cancer}
     */
    Collection<CancerMetadatum> getQualifiers();

    /**
     * Retrieve all information about this cancer and its associated qualifiers in a JSON format. For example:
     *
     * <pre>
     * {
     *   "id": "HP:0009726",
     *   "affected": true,
     *   "qualifiers": [
     *     // See the documentation for {@link CancerMetadatum#toJSON()}
     *   ]
     * }
     * </pre>
     *
     * @return the cancer data, using the org.json classes
     */
    @Override
    JSONObject toJSON();
}
