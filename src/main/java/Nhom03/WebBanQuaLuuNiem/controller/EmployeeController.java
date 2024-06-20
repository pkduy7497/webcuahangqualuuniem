package Nhom03.WebBanQuaLuuNiem.controller;

import Nhom03.WebBanQuaLuuNiem.model.Employee;
import Nhom03.WebBanQuaLuuNiem.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Employee")
@RequiredArgsConstructor
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public String showEmployeeList(Model model) {
        model.addAttribute("Employee", employeeService.getAllEmployee());
        return "/Employee/employee-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("Employee", new Employee());
        return "/Employee/add-employee";
    }

    @PostMapping("/add")
    public String addEmployee(@Valid Employee employee, BindingResult result) {
        if (result.hasErrors()) {
            return "/Employee/add-employee";
        }
        employeeService.addEmployee(employee);
        return "redirect:/Employee";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Employee Id:" + id));
        model.addAttribute("Employee", employee);
        return "/Employee/update-employee";
    }

    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable Long id, @Valid Employee employee, BindingResult result) {
        if (result.hasErrors()) {
            employee.setId(id);
            return "/Employee/update-employee";
        }
        employeeService.updateEmployee(employee);
        return "redirect:/Employee";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployeeById(id);
        return "redirect:/Employee";
    }
}