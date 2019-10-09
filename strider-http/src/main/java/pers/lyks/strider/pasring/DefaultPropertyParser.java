package pers.lyks.strider.pasring;

import java.util.Properties;

/**
 * @author lawyerance
 * @version 1.0 2019-09-11
 */
public class DefaultPropertyParser implements PropertyParser {

    private static final String KEY_PREFIX = "pers.lyks.strider.pasring.PropertyParser.";

    public static final String KEY_ENABLE_DEFAULT_VALUE = KEY_PREFIX + "enable-default-value";

    public static final String KEY_DEFAULT_VALUE_SEPARATOR = KEY_PREFIX + "default-value-separator";

    private static final String ENABLE_DEFAULT_VALUE = "false";

    private static final String DEFAULT_VALUE_SEPARATOR = ":";

    private static final String DEFAULT_PREFIX = "{";

    private static final String DEFAULT_SUFFIX = "}";

    @Override
    public String parse(String content, Properties properties) {
        VariableTokenHandler handler = new VariableTokenHandler(properties);
        GenericTokenParser parser = new GenericTokenParser(DEFAULT_PREFIX, DEFAULT_SUFFIX, handler);
        return parser.parse(content);
    }

    private static class VariableTokenHandler implements TokenHandler {
        private final Properties variables;
        private final boolean enableDefaultValue;
        private final String defaultValueSeparator;

        private VariableTokenHandler(Properties variables) {
            this.variables = variables;
            this.enableDefaultValue = Boolean.parseBoolean(getPropertyValue(KEY_ENABLE_DEFAULT_VALUE, ENABLE_DEFAULT_VALUE));
            this.defaultValueSeparator = getPropertyValue(KEY_DEFAULT_VALUE_SEPARATOR, DEFAULT_VALUE_SEPARATOR);
        }

        private String getPropertyValue(String key, String defaultValue) {
            return (variables == null) ? defaultValue : variables.getProperty(key, defaultValue);
        }

        @Override
        public String prefix() {
            return DEFAULT_PREFIX;
        }

        @Override
        public String suffix() {
            return DEFAULT_SUFFIX;
        }

        @Override
        public String handle(String content) {
            if (variables != null) {
                String key = content;
                if (enableDefaultValue) {
                    final int separatorIndex = content.indexOf(defaultValueSeparator);
                    String defaultValue = null;
                    if (separatorIndex >= 0) {
                        key = content.substring(0, separatorIndex);
                        defaultValue = content.substring(separatorIndex + defaultValueSeparator.length());
                    }
                    if (defaultValue != null) {
                        return variables.getProperty(key, defaultValue);
                    }
                }
                if (variables.containsKey(key)) {
                    return variables.getProperty(key);
                }
            }
            return prefix() + content + suffix();
        }
    }
}
