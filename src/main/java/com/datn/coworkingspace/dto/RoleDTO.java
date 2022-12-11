package com.datn.coworkingspace.dto;

import com.datn.coworkingspace.entity.User;

import java.util.ArrayList;
import java.util.List;

public class RoleDTO extends AbstractDTO{

    private String code;

    private String name;

    private List<User> employees = new ArrayList<>();

    private List<User> customers = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getEmployees() {
        return employees;
    }

    public void setEmployees(List<User> employees) {
        this.employees = employees;
    }

    public List<User> getCustomers() {
        return customers;
    }

    public void setCustomers(List<User> customers) {
        this.customers = customers;
    }
}
