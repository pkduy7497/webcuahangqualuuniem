package Nhom03.WebBanQuaLuuNiem.service;

import Nhom03.WebBanQuaLuuNiem.Role;
import Nhom03.WebBanQuaLuuNiem.model.Employee;
import Nhom03.WebBanQuaLuuNiem.model.User;
import Nhom03.WebBanQuaLuuNiem.repository.EmployeeRepository;
import Nhom03.WebBanQuaLuuNiem.repository.IRoleRepository;
import Nhom03.WebBanQuaLuuNiem.repository.IUserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public void save(@NotNull User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        Nhom03.WebBanQuaLuuNiem.model.Role userRole = roleRepository.findRoleById(2L);
        user.getRoles().add(userRole);
        userRepository.save(user);
    }

    public void setDefaultRole(String username) {
        userRepository.findByUsername(username).ifPresentOrElse(
            user -> {
                user.getRoles().add(roleRepository.findRoleById(Role.USER.value));
                userRepository.save(user);
            },
            () -> { throw new UsernameNotFoundException("User not found"); }
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return org.springframework.security.core.userdetails.User
                .withUsername(user.get().getUsername())
                .password(user.get().getPassword())
                .authorities(user.get().getAuthorities())
                .accountExpired(!user.get().isAccountNonExpired())
                .accountLocked(!user.get().isAccountNonLocked())
                .credentialsExpired(!user.get().isCredentialsNonExpired())
                .disabled(!user.get().isEnabled())
                .build();
        }
        Optional<Employee> employee = employeeRepository.findByUsername(username);
        if (employee.isPresent()) {
            return org.springframework.security.core.userdetails.User
                .withUsername(employee.get().getUsername())
                .password(employee.get().getPassword())
                .authorities(employee.get().getAuthorities())
                .accountExpired(!employee.get().isAccountNonExpired())
                .accountLocked(!employee.get().isAccountNonLocked())
                .credentialsExpired(!employee.get().isCredentialsNonExpired())
                .disabled(!employee.get().isEnabled())
                .build();
        }
        throw new UsernameNotFoundException("User not found");
    }

    public Optional<User> findByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
}