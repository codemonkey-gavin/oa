package com.learn.oa.controller;

import com.learn.oa.biz.AccountBiz;
import com.learn.oa.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller("accountController")
public class AccountController {
    @Autowired
    private AccountBiz accountBiz;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/login")
    public String login(HttpSession session, @RequestParam String sn, @RequestParam String password) {
        Employee employee = accountBiz.login(sn, password);
        if (employee == null) {
            return "redirect:to_login";
        }
        session.setAttribute("employee", employee);
        return "redirect:detail";
    }

    @RequestMapping("/detail")
    public String self() {
        return "employee/employee_detail";
    }
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.setAttribute("employee",null);
        return "redirect:to_login";
    }
    @RequestMapping("/to_change_password")
    public String toChangePassword() {
        return "employee/employee_change_password";
    }

    @RequestMapping("/change_password")
    public String changePassword(HttpSession session,@RequestParam String oldPassword, @RequestParam String newPassword1, @RequestParam String newPassword2) {
        Employee employee = (Employee)session.getAttribute("employee");
        if (employee.getPassword().equals(oldPassword)) {
            if(newPassword1.equals(newPassword2)){
                employee.setPassword(newPassword1);
                accountBiz.changePassword(employee);
                return "redirect:detail";
            }
        }
        return "redirect:to_login";
    }
}
