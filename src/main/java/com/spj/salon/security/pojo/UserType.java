package com.spj.salon.security.pojo;

/**
 * 
 * @author Yogesh Sharma
 *
 */
public enum UserType {
	BARBER("BARBER"), CUSTOMER("CUSTOMER"), SUPERUSER("SUPERUSER");
    private final String value;

    public String getResponse() {
        return value;
    }

    UserType(String value){
        this.value = value;
    }
}
