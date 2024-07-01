package Nhom03.WebBanQuaLuuNiem.service;

import Nhom03.WebBanQuaLuuNiem.Role;
import Nhom03.WebBanQuaLuuNiem.model.Employee;
import Nhom03.WebBanQuaLuuNiem.repository.IRoleRepository;
import Nhom03.WebBanQuaLuuNiem.repository.EmployeeRepository;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private IRoleRepository roleRepository;

    public void setDefaultRole(String username) {
        employeeRepository.findByUsername(username).ifPresentOrElse(
            employee -> {
                employee.getRoles().add(roleRepository.findRoleById(Role.ADMIN.value));
                employeeRepository.save(employee);
            },
            () -> { throw new UsernameNotFoundException("User not found"); }
        );
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
        employee.setPassword(new BCryptPasswordEncoder().encode(employee.getPassword()));
        Nhom03.WebBanQuaLuuNiem.model.Role userRole = roleRepository.findRoleById(1L);
        employee.getRoles().add(userRole);
        employeeRepository.save(employee);
    }

    public void updateEmployee(@NotNull Employee employee) {
        Employee existingEmployee = employeeRepository.findById(employee.getId()).orElseThrow(() -> new IllegalStateException("Employee with ID " + employee.getId() + " does not exist."));
        existingEmployee.setUsername(employee.getUsername());
        existingEmployee.setPassword(new BCryptPasswordEncoder().encode(employee.getPassword()));
        employeeRepository.save(existingEmployee);
    }

    public void deleteEmployeeById(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new IllegalStateException("Employee with ID " + id + " does not exist.");
        }
        employeeRepository.deleteById(id);
    }
}