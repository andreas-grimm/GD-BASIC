package eu.gricom.basic.helper;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class EnvParam {
    private static final Logger LOGGER = Logger.getLogger(EnvParam.class.getName());
    private static final String CONFIG_FILE_PATH = "application.yaml";
    private static EnvParam _oInstance;
    private static String _strConfigGroup = "environment";
    private final Map<String, Object> _mConfig;

    private EnvParam() {
        _mConfig = loadConfig();
    }

    public static synchronized EnvParam getInstance() {
        if (_oInstance == null) {
            _oInstance = new EnvParam();
        }
        return _oInstance;
    }

    public static void setConfigGroup(String strGroup) {
        _strConfigGroup = strGroup;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> loadConfig() {
        Map<String, Object> mResult = new HashMap<>();
        try {
            ClassLoader oClassLoader = Thread.currentThread().getContextClassLoader();
            InputStream oInputStream = oClassLoader.getResourceAsStream(CONFIG_FILE_PATH);

            if (oInputStream != null) {
                Yaml oYaml = new Yaml();
                Map<String, Object> mYamlData = oYaml.load(oInputStream);
                if (mYamlData != null && mYamlData.containsKey(_strConfigGroup)) {
                    Object oEnvData = mYamlData.get(_strConfigGroup);
                    if (oEnvData instanceof Map) {
                        mResult.putAll((Map<String, Object>) oEnvData);
                    }
                }
            } else {
                LOGGER.warning("Config file not found: " + CONFIG_FILE_PATH);
            }
        } catch (Exception e) {
            LOGGER.warning("Failed to load config file: " + e.getMessage());
        }
        return mResult;
    }

    private String getValueOrEnv(String strKey) {
        String strEnvValue = System.getenv(strKey);
        if (strEnvValue != null && !strEnvValue.trim().isEmpty()) {
            return strEnvValue;
        }

        Object oConfigValue = _mConfig.get(strKey);
        if (oConfigValue != null) {
            return oConfigValue.toString();
        }

        return null;
    }

    public static String getString(String strKey) {
        String strValue = getInstance().getValueOrEnv(strKey);
        if (strValue == null) {
            LOGGER.warning("Configuration key not found: " + strKey);
            return "";
        }
        return strValue;
    }

    public static int getInt(String strKey) {
        String strValue = getInstance().getValueOrEnv(strKey);
        if (strValue == null) {
            LOGGER.warning("Configuration key not found: " + strKey);
            return 0;
        }
        try {
            return Integer.parseInt(strValue);
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid integer value for key '" + strKey + "': " + strValue);
            return 0;
        }
    }

    public static float getFloat(String strKey) {
        String strValue = getInstance().getValueOrEnv(strKey);
        if (strValue == null) {
            LOGGER.warning("Configuration key not found: " + strKey);
            return 0.0f;
        }
        try {
            return Float.parseFloat(strValue);
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid float value for key '" + strKey + "': " + strValue);
            return 0.0f;
        }
    }

    public static boolean getBoolean(String strKey) {
        String strValue = getInstance().getValueOrEnv(strKey);
        if (strValue == null) {
            LOGGER.warning("Configuration key not found: " + strKey);
            return false;
        }
        return Boolean.parseBoolean(strValue);
    }

    public static int getMaxBcdDigits() {
        return getInt("max_bcd_digits");
    }
}
