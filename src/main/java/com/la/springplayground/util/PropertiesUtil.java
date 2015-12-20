/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.la.springplayground.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * Helper to fetch propeties from file
 * 
 * @author Luís Alves
 */
public class PropertiesUtil extends PropertyPlaceholderConfigurer {
   private static Map propertiesMap;

   @Override
   protected void processProperties(ConfigurableListableBeanFactory beanFactory,
             Properties props) throws BeansException {
        super.processProperties(beanFactory, props);

        propertiesMap = new HashMap<String, String>();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            propertiesMap.put(keyStr, resolvePlaceholder(props.getProperty(keyStr),
                props, SYSTEM_PROPERTIES_MODE_FALLBACK));
        }
    }

    public static String getProperty(String name) {
        return (String) propertiesMap.get(name);
    }
}
