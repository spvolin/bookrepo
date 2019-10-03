package com.volin.bookrepo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.volin.bookrepo.model.User;
import com.volin.bookrepo.repositories.UserRepository;

public final class SecurityUtils {

	@Autowired
	private static UserRepository userRepository;

	public static User getCurrentUser() {
		
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        
        System.out.println("SecurityUtils> currentPrincipalName: "+ currentPrincipalName);
        
        User user = userRepository.findByUsernameOrEmail(currentPrincipalName, currentPrincipalName)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email : " + currentPrincipalName)
                );
       
        System.out.println("SecurityUtils> User: " + user);
		return user;
	}

}
