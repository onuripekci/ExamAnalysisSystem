package com.etstur.dsas.services;

import com.etstur.dsas.entities.AppUser;
import com.etstur.dsas.entities.Role;
import com.etstur.dsas.repositories.AppUserRepository;
import com.etstur.dsas.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByUsername(username);
        if(user == null) {
            log.error("User not found in the database {}", username);
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}", username);
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public AppUser saveUser(AppUser appUser) {
        log.info("Saving new user {} to the db", appUser.getNickname());
        try {
            AppUser a = appUserRepository.save(appUser);
            return appUserRepository.save(a);
        }catch (Exception e){
            System.out.println("HATA "+ e.getMessage() );
        }

        return null;
    }

    public AppUser getUser(String username) {
        return appUserRepository.findByUsername(username);
    }

    public List<AppUser> getUsers() {
        return appUserRepository.findAll();
    }

    public Role saveRole(Role role) {
        log.info("Saving new role {} to the db", role.getName());
        return roleRepository.save(role);
    }

    public void grantRoleToUser(String username, String roleName) {
        log.info("Grant new role {} to {}", roleName, username);
        AppUser appUser = appUserRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);

        appUser.getRoles().add(role);
    }
}
