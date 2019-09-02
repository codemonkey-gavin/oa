package com.learn.oa.biz;

import com.learn.oa.entity.Employee;

public interface AccountBiz {
    Employee login(String sn,String password);
    void changePassword(Employee employee);
}
