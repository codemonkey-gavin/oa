package com.learn.oa.biz.impl;

import com.learn.oa.biz.AccountBiz;
import com.learn.oa.dao.EmployeeDao;
import com.learn.oa.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("AccountBiz")
public class AccountBizImpl implements AccountBiz {

    @Autowired
    private EmployeeDao employeeDao;

    public Employee login(String sn, String password) {
        Employee employee = employeeDao.select(sn);
        if (employee != null && employee.getPassword().equals(password)) {
            return employee;
        }
        return null;
    }

    public void changePassword(Employee employee) {
        employeeDao.update(employee);
    }
}
