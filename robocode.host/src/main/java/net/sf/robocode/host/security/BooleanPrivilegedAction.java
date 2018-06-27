package net.sf.robocode.host.security;

import java.security.Permission;
import java.security.PrivilegedAction;

class BooleanPrivilegedAction implements PrivilegedAction<Boolean> {
    private RobocodeSecurityPolicy robocodeSecurityPolicy;
    private final Permission permission;

    public BooleanPrivilegedAction(RobocodeSecurityPolicy robocodeSecurityPolicy, Permission permission) {
        this.robocodeSecurityPolicy = robocodeSecurityPolicy;
        this.permission = permission;
    }

    public Boolean run() {
        return robocodeSecurityPolicy.impliesRobot(permission);
    }
}
