package com.example.Group5.Controller;

import com.example.Group5.Entity.AppUser;
import com.example.Group5.Entity.UserRole;
import com.example.Group5.Repository.AppRoleRepo;
import com.example.Group5.Repository.AppUserRepo;
import com.example.Group5.Repository.RoleRepo;
import com.example.Group5.Utils.EncrytedPasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    private AppRoleRepo appRoleRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private AppUserRepo appUserRepo;

    //  Trả về trang danh sách nhân viên
    @RequestMapping(value = "/manage-employee", method = RequestMethod.GET)
    public String listEmployee(Model model) {
        long num = 2;
        List<UserRole> userRoles = roleRepo.findAllByAppRole(appRoleRepo.findById(num));
        List<AppUser> users = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            if (userRole.getAppUser().isEnabled()) {
                users.add(userRole.getAppUser());
            }
        }
        model.addAttribute("employeeInfo", users);
        return "ListEmployee";
    }

    //  Trả về trang tạo mới
    @RequestMapping(value = "/manage-employee/create", method = RequestMethod.GET)
    public String createEmployee(Model model) {
        model.addAttribute("appUser", new AppUser());
        return "CreateEmployee";
    }

    @RequestMapping(value = "/manage-employee/create", method = RequestMethod.POST)
    public String saveEmployee(@ModelAttribute @Valid AppUser appUser, BindingResult bindingResult,@RequestParam("role") long role,@RequestParam("status") long status) {
        if (bindingResult.hasErrors() && !appUser.isEnabled()) {
            return "createPage";
        } else {
            appUser.setEncrytedPassword(EncrytedPasswordUtils.encrytePassword(appUser.getEncrytedPassword()));
            if(status == 1) {
                appUser.setEnabled(true);
            }else{
                appUser.setEnabled(false);
            }
            appUserRepo.save(appUser);
            if (roleRepo.findByAppUser(appUser).size() == 0) {
                long num = role;
                UserRole newEmpRole = new UserRole();
                newEmpRole.setAppRole(appRoleRepo.findById(num).get());
                newEmpRole.setAppUser(appUser);
                roleRepo.save(newEmpRole);
            }
            return "redirect:/manage-employee";
        }
    }

    //Delete
    @RequestMapping(path = "/emp/delete/{id}", method = RequestMethod.GET)
    public String delProduct(@PathVariable long id) {
        EncrytedPasswordUtils newPass = new EncrytedPasswordUtils();
        AppUser appUser = appUserRepo.findById(id).get();
        if (appUser.isEnabled()) {
            AppUser updatedUser = new AppUser();
            updatedUser.setUserId(appUser.getUserId());
            updatedUser.setUserName(appUser.getUserName());
            updatedUser.setFullName(appUser.getFullName());
            updatedUser.setAddress(appUser.getAddress());
            updatedUser.setAge(appUser.getAge());
            updatedUser.setPhone(appUser.getPhone());
            updatedUser.setEncrytedPassword(appUser.getEncrytedPassword());
            updatedUser.setEnabled(false);
            appUserRepo.save(updatedUser);
            return "redirect:/manage-employee";
        } else {
            return "403Page";
        }
    }

    //Update
    @RequestMapping(path = "/emp/edit/{id}", method = RequestMethod.GET)
    public String editProduct(@PathVariable long id, Model model) {
        Optional<AppUser> optionalUser =appUserRepo.findById(id);
        if (optionalUser.isPresent()) {
            if(appUserRepo.findById(id).get().isEnabled()) {
                model.addAttribute("appUser", optionalUser.get());
                return "UpdateEmployee";
            }else {
                return "403Page";
            }
        } else {
            return "403Page";
        }
    }
    //update
    @RequestMapping(value = "/manage-employee/update", method = RequestMethod.POST)
    public String updateEmployee(@ModelAttribute @Valid AppUser appUser, BindingResult bindingResult,@RequestParam("role") long role,@RequestParam("status") long status) {
        if (bindingResult.hasErrors() && !appUser.isEnabled()) {
            return "createPage";
        } else {
            if(status == 1) {
                appUser.setEnabled(true);
            }else{
                appUser.setEnabled(false);
            }
            appUserRepo.save(appUser);
            List<UserRole> oldRole = new ArrayList<>();
            oldRole = roleRepo.findByAppUser(appUser);
            for (UserRole x:oldRole) {
                long num = role;
                x.setAppRole(appRoleRepo.findById(num).get());
                x.setAppUser(appUser);
                roleRepo.save(x);
            }
            return "redirect:/manage-employee";
        }
    }
}