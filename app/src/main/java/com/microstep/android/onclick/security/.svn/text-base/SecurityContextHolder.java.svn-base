package com.microstep.android.onclick.security;

public class SecurityContextHolder {
	private static SecurityContext contextHolder = new SecurityContext();

	public static SecurityContext getSecurityContext() {
		if (contextHolder == null) {
			contextHolder = new SecurityContext();
		}
		return contextHolder;
	}

	public static void setSecurityContext(SecurityContext securityContext) {
		contextHolder = securityContext;
	}

}