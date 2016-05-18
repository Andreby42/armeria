/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.linecorp.armeria.server.http.file;

import static java.util.Objects.requireNonNull;

import java.net.URLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.google.common.net.MediaType;

final class MimeTypeUtil {

    /**
     * A map from extension to MIME types, which is queried before
     * {@link URLConnection#guessContentTypeFromName(String)}, so that
     * important extensions are always mapped to the right MIME types.
     */
    private static final Map<String, MediaType> EXTENSION_TO_MEDIA_TYPE;

    static {
        final Map<String, MediaType> map = new HashMap<>();

        // Text files
        add(map, MediaType.CSS_UTF_8, "css");
        add(map, MediaType.HTML_UTF_8, "html", "htm");
        add(map, MediaType.PLAIN_TEXT_UTF_8, "txt");

        // Image files
        add(map, MediaType.GIF, "gif");
        add(map, MediaType.JPEG, "jpeg", "jpg");
        add(map, MediaType.PNG, "png");
        add(map, MediaType.SVG_UTF_8, "svg", "svgz");
        add(map, MediaType.create("image", "x-icon"), "ico");

        // Font files
        add(map, MediaType.create("application", "x-font-ttf"), "ttc", "ttf");
        add(map, MediaType.WOFF, "woff");
        add(map, MediaType.create("application", "font-woff2"), "woff2");
        add(map, MediaType.EOT, "eot");
        add(map, MediaType.create("font", "opentype"), "otf");

        // JavaScript, XML, etc
        add(map, MediaType.JAVASCRIPT_UTF_8, "js");
        add(map, MediaType.JSON_UTF_8, "json");
        add(map, MediaType.PDF, "pdf");
        add(map, MediaType.XHTML_UTF_8, "xhtml", "xhtm");
        add(map, MediaType.APPLICATION_XML_UTF_8, "xml", "xsd");
        add(map, MediaType.create("application", "xml-dtd"), "dtd");

        EXTENSION_TO_MEDIA_TYPE = Collections.unmodifiableMap(map);
    }

    private static void add(Map<String, MediaType> extensionToMediaType,
                            MediaType mediaType, String... extensions) {

        for (String e : extensions) {
            assert e.toLowerCase(Locale.US).equals(e);
            extensionToMediaType.put(e, mediaType);
        }
    }

    static MediaType guessFromPath(String path) {
        requireNonNull(path, "path");
        final int dotIdx = path.lastIndexOf('.');
        final int slashIdx = path.lastIndexOf('/');
        if (dotIdx < 0 || slashIdx > dotIdx) {
            // No extension
            return null;
        }

        final String extension = path.substring(dotIdx + 1).toLowerCase(Locale.US);
        final MediaType mediaType = EXTENSION_TO_MEDIA_TYPE.get(extension);
        return mediaType != null ? mediaType : MediaType.parse(URLConnection.guessContentTypeFromName(path));
    }

    private MimeTypeUtil() {}
}
