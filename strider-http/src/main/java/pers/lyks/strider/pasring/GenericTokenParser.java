/**
 * Copyright 2009-2019 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pers.lyks.strider.pasring;

/**
 * @author lawyerance
 * @version 1.0 2019-09-11
 */
public class GenericTokenParser {

    private final String prefix;
    private final String suffix;
    private final TokenHandler handler;

    public GenericTokenParser(String prefix, String suffix, TokenHandler handler) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.handler = handler;
    }

    public String parse(String text) {
        if (null == text || text.isEmpty()) {
            return "";
        }
        // search open token
        int start = text.indexOf(prefix);
        if (start == -1) {
            return text;
        }
        char[] src = text.toCharArray();
        int offset = 0;
        final StringBuilder builder = new StringBuilder();
        StringBuilder expression = null;
        while (start > -1) {
            if (start > 0 && src[start - 1] == '\\') {
                // this open token is escaped. remove the backslash and continue.
                builder.append(src, offset, start - offset - 1).append(prefix);
                offset = start + prefix.length();
            } else {
                // found open token. let's search close token.
                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }
                builder.append(src, offset, start - offset);
                offset = start + prefix.length();
                int end = text.indexOf(suffix, offset);
                while (end > -1) {
                    if (end > offset && src[end - 1] == '\\') {
                        // this close token is escaped. remove the backslash and continue.
                        expression.append(src, offset, end - offset - 1).append(suffix);
                        offset = end + suffix.length();
                        end = text.indexOf(suffix, offset);
                    } else {
                        expression.append(src, offset, end - offset);
                        break;
                    }
                }
                if (end == -1) {
                    // close token was not found.
                    builder.append(src, start, src.length - start);
                    offset = src.length;
                } else {
                    builder.append(handler.handle(expression.toString()));
                    offset = end + suffix.length();
                }
            }
            start = text.indexOf(prefix, offset);
        }
        if (offset < src.length) {
            builder.append(src, offset, src.length - offset);
        }
        return builder.toString();
    }
}
