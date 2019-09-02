package com.learn.oa.controller;

import com.learn.oa.biz.DepartmentBiz;
import com.learn.oa.entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller("departmentController")
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    private DepartmentBiz departmentBiz;

    @RequestMapping("/list")
    public String list(Map<String, Object> map) {
        map.put("list", departmentBiz.getAll());
        return "department/department_list";
    }

    @RequestMapping("/to_add")
    public String toAdd(Map<String, Object> map) {
        map.put("department", new Department());
        return "department/department_add";
    }

    @RequestMapping("/add")
    public String add(Department department) {
        departmentBiz.add(department);
        return "redirect:list";
    }

    @RequestMapping(value = "/to_edit", params = "sn")
    public String toEdit(String sn, Map<String, Object> map) {
        map.put("department", departmentBiz.get(sn));
        return "department/department_edit";
    }

    @RequestMapping("/edit")
    public String edit(Department department) {
        departmentBiz.edit(department);
        return "redirect:list";
    }

    @RequestMapping(value = "/remove", params = "sn")
    public String toUpdate(String sn) {
        departmentBiz.remove(sn);
        return "redirect:list";
    }
}
