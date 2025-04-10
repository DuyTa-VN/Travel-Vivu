package vn.duyta.Travel_Vivu.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import vn.duyta.Travel_Vivu.model.User;
import vn.duyta.Travel_Vivu.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component("userDetailsService")
@RequiredArgsConstructor
public class UserDetailsCustom implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userService.handleGetUserByUsername(email);
        if (user == null) {
            throw new UsernameNotFoundException("Email/Password không hợp lệ!");
        }

        // lấy role từ user và tạo danh sách GrantedAuthority
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getRole() != null){
            // thêm tiền tố "ROLE_" theo chuẩn Spring Security
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        }else {
            authorities = Collections.emptyList();
        }
        //Trả về UserDetails với email, password và danh sách authorities
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}
