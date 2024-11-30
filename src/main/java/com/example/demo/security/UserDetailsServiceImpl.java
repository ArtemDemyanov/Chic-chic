package com.example.demo.security;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        User currentUser = repository.findByEmail(email);

        if (currentUser!=null) {
            System.out.println(currentUser.getEmail());

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(currentUser.getRole()));


            UserDetails user = new org.springframework.security.core
                    .userdetails.User(email, currentUser.getPassword()
                    , true, true, true, true,
                    authorities);
            return user;
        }else {
            throw new UsernameNotFoundException("User not authorized.");
        }
    }

}
