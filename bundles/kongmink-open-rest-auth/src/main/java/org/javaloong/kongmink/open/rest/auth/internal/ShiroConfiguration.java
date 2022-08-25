package org.javaloong.kongmink.open.rest.auth.internal;

import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import static org.javaloong.kongmink.open.rest.auth.internal.ShiroConfiguration.SHIRO_CONFIGURATION_PID;

@ObjectClassDefinition(name = "Shiro Configuration", pid = SHIRO_CONFIGURATION_PID)
public @interface ShiroConfiguration {

    String SHIRO_CONFIGURATION_PID = "org.javaloong.kongmink.open.rest.auth.shiro";

    String shiro_ini_file() default "etc/shiro.ini";
}
