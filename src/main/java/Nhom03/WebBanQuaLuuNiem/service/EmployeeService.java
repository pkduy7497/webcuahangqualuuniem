package Nhom03.WebBanQuaLuuNiem.service;

import Nhom03.WebBanQuaLuuNiem.Role;
import Nhom03.WebBanQuaLuuNiem.model.Employee;
import Nhom03.WebBanQuaLuuNiem.repository.IRoleRepository;
import Nhom03.WebBanQuaLuuNiem.repository.EmployeeRepository;
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

    public void save(@NotNull Employee employee) {
        employee.setPassword(new BCryptPasswordEncoder().encode(employee.getPassword()));
        employeeRepository.save(employee);
    }

    public void setDefaultRole(String username) {
        employeeRepository.findByUsername(username).ifPresentOrElse(
                employee -> {
                    employee.getRoles().add(roleRepository.findRoleById(Role.ADMIN.value));
                    employeeRepository.save(employee);
                },
                () -> { throw new UsernameNotFoundException("User not found"); }
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var employee = employeeRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Employee not found"));
        return org.springframework.security.core.userdetails.User
            .withUsername(employee.getUsername())
            .password(employee.getPassword())
            .authorities(employee.getAuthorities())
            .accountExpired(!employee.isAccountNonExpired())
            .accountLocked(!employee.isAccountNonLocked())
            .credentialsExpired(!employee.isCredentialsNonExpired())
            .disabled(!employee.isEnabled())
            .build();
    }

    public Optional<Employee> findByUsername(String username) throws UsernameNotFoundException {
        return employeeRepository.findByUsername(username);
    }

    public List<Employee> getAllEmployee() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public void updateEmployee(@NotNull Employee employee) {
        Employee existingEmployee = employeeRepository.findById(employee.getId()).orElseThrow(() -> new IllegalStateException("Employee with ID " + employee.getId() + " does not exist."));
        existingEmployee.setUsername(employee.getUsername());
        existingEmployee.setPassword(employee.getPassword());
        employeeRepository.save(existingEmployee);
    }

    public void deleteEmployeeById(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new IllegalStateException("Employee with ID " + id + " does not exist.");
        }
        employeeRepository.deleteById(id);
    }
}