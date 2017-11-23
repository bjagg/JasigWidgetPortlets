/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.portlet.widget.links;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ResourceLinkServiceTest {
    static final String TITLE = "My Title";
    static final String DESC = "My description.";
    static final String ICON_FA = "fa-disk";
    static final String ICON_URL = "/rs/234234.png";
    static final String URL = "http://localhost:8080/myapp";
    static final List<String> GROUPS = new ArrayList<>();
    static final String JSON = "{\"title\":\"My Title\",\"description\":\"My description.\",\"icon\":\"fa-disk\",\"url\":\"http://localhost:8080/myapp\",\"groups\":[\"everyone\",\"students\"]}";
    static final String JSON2 = "{\"title\":\"My Title 2\",\"description\":\"My 2nd description.\",\"icon\":\"fa-hand\",\"url\":\"http://localhost:8080/myapp2\",\"groups\":[\"everyone\"]}";
    static final String JSON3 = "{\"title\":\"My Title 3\",\"description\":\"My 3rd description.\",\"icon\":\"fa-foot\",\"url\":\"http://localhost:8080/myapp3\",\"groups\":[\"admin\"]}";
    static final String BAD_JSON = "{\"title\":\"My Title\",\"description\":\"My description.\",\"icon\":\"fa-disk\",\"url\":\"http://localhost:8080/myapp\",\"groups\":[\"everyone\",\"students\"],\"iconType\":\"fa\"}";
    final String[] JSON_STRINGS = {JSON, JSON2, JSON3};
    final String[] LINK_ORDER = {"My Title 2", "My Title 3", "My Title"};

    static {
        GROUPS.add("everyone");
        GROUPS.add("students");
    }

    public ResourceLink createLink() {
        final ResourceLink link = new ResourceLink();
        link.setTitle(TITLE);
        link.setDescription(DESC);
        link.setIcon(ICON_FA);
        link.setUrl(URL);
        link.setGroups(GROUPS);
        return link;
    }

    @Test
    public void testToJson() {
        final ResourceLink link = createLink();
        assertEquals(JSON, ResourceLinkService.linkToJson(link));
        assertNull(ResourceLinkService.linkToJson(null));
    }

    @Test(expected = NullPointerException.class)
    public void testFromJson() {
        final ResourceLink link = createLink();
        final ResourceLink jsonLink = ResourceLinkService.jsonToLink(JSON);
        assertEquals(link, jsonLink);
        assertEquals(null, ResourceLinkService.jsonToLink(BAD_JSON));
        assertEquals(null, ResourceLinkService.jsonToLink(null));
    }

    @Test
    public void testJsonStringsToMapByTitle() {
        Map<String, ResourceLink> map = ResourceLinkService.jsonStringArrayToMapByTitle(JSON_STRINGS);
        assertEquals(3, map.size());
        assertEquals(createLink(), map.get(TITLE));
    }

    @Test
    public void testOrderedList() {
        Map<String, ResourceLink> map = ResourceLinkService.jsonStringArrayToMapByTitle(JSON_STRINGS);
        List<ResourceLink> list = ResourceLinkService.createOrderedLinkList(LINK_ORDER, map);
        for (int i = 0; i < 3; i++) {
            assertEquals(map.get(LINK_ORDER[i]), list.get(i));
        }
    }
}