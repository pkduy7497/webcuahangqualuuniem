package Nhom03.WebBanQuaLuuNiem.service;

import Nhom03.WebBanQuaLuuNiem.Role;
import Nhom03.WebBanQuaLuuNiem.model.Employee;
import Nhom03.WebBanQuaLuuNiem.model.Product;
import Nhom03.WebBanQuaLuuNiem.repository.IRoleRepository;
import Nhom03.WebBanQuaLuuNiem.repository.EmployeeRepository;
import Nhom03.WebBanQuaLuuNiem.repository.ProductRepository;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class EmployeeService implements UserDetailsService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private IRoleRepository roleRepository;
    // Lưu người dùng mới vào cơ sở dữ liệu sau khi mã hóa mật khẩu.
    public void save(@NotNull Employee employee) {
        employee.setPassword(new BCryptPasswordEncoder().encode(employee.getPassword()));
        employeeRepository.save(employee);
    }
    // Gán vai trò mặc định cho người dùng dựa trên tên người dùng.
    public void setDefaultRole(String username) {
        employeeRepository.findByUsername(username).ifPresentOrElse(
                employee -> {
                    employee.getRoles().add(roleRepository.findRoleById(Role.USER.value));
                    employeeRepository.save(employee);
                },
                () -> { throw new UsernameNotFoundException("User not found"); }
        );
    }
    // Tải thông tin chi tiết người dùng để xác thực.
    @Override
    public UserDetails loadUserByUsername(String username) throws
            UsernameNotFoundException {
        var user = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .accountExpired(!user.isAccountNonExpired())
                .accountLocked(!user.isAccountNonLocked())
                .credentialsExpired(!user.isCredentialsNonExpired())
                .disabled(!user.isEnabled())
                .build();
    }
    // Tìm kiếm người dùng dựa trên tên đăng nhập.
    public Optional<Employee> findByUsername(String username) throws
            UsernameNotFoundException {
        return employeeRepository.findByUsername(username);
    }
    private final EmployeeRepository employeeRepository;
    public List<Employee> getAllEmployee() {
        return employeeRepository.findAll();
    }
    public Optional<Employee> getEmployeeByUsername(String username) {
        return employeeRepository.findByUsername(username);
    }

    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public void updateEmployee(@NotNull Employee employee) {
        Employee existingEmployee = employeeRepository.findById(employee.getUsername())
                .orElseThrow(() -> new IllegalStateException("Product with ID " +
                        employee.getId() + " does not exist."));
        existingEmployee.setUsername(employee.getUsername());
        existingEmployee.setPassword(employee.getPassword());
        employeeRepository.save(existingEmployee);
    }

    public void deleteEmployeeById(String username) {
        if (!employeeRepository.existsById(username)) {
            throw new IllegalStateException("Product with ID " + username + " does not exist.");
        }
        employeeRepository.deleteById(username);
    }
}

