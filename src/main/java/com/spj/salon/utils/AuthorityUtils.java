package com.spj.salon.utils;

import com.spj.salon.user.entities.Authorities;
import com.spj.salon.user.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Yogesh Sharma
 */
public class AuthorityUtils {

    public static Collection<GrantedAuthority> getUserAuthorities(User user) {
        Collection<GrantedAuthority> grantedAuthoritiesList = new ArrayList<>();

        Authorities authority = user.getAuthority();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getAuthority());
        grantedAuthoritiesList.add(grantedAuthority);

        return grantedAuthoritiesList;
    }
}
