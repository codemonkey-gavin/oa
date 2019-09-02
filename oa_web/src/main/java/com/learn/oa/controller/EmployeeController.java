package com.learn.oa.controller;

import com.learn.oa.biz.DepartmentBiz;
import com.learn.oa.biz.EmployeeBiz;
import com.learn.oa.entity.Employee;
import com.learn.oa.global.Contant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller("employeeController")
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private DepartmentBiz departmentBiz;
    @Autowired
    private EmployeeBiz employeeBiz;

    @RequestMapping("/list")
    public String list(Map<String, Object> map) {
        map.put("list", employeeBiz.getAll());
        return "employee/employee_list";
    }

    @RequestMapping("/to_add")
    public String toAdd(Map<String, Object> map) {
        map.put("employee", new Employee());
        map.put("dlist",departmentBiz.getAll());
        map.put("plist", Contant.getPosts());
        return "employee/employee_add";
    }

    @RequestMapping("/add")
    public String add(Employee employee) {
        employeeBiz.add(employee);
        return "redirect:list";
    }

    @RequestMapping(value = "/to_edit", params = "sn")
    public String toEdit(String sn, Map<String, Object> map) {
        map.put("employee", employeeBiz.get(sn));
        map.put("dlist",departmentBiz.getAll());
        map.put("plist", Contant.getPosts());
        return "employee/employee_edit";
    }

    @RequestMapping("/edit")
    public String edit(Employee employee) {
        employeeBiz.edit(employee);
        return "redirect:list";
    }

    @RequestMapping(value = "/remove", params = "sn")
    public String toUpdate(String sn) {
        employeeBiz.remove(sn);
        return "redirect:list";
    }
}
