package com.example.Group5.Repository;

import com.example.Group5.Entity.AppRole;
import com.example.Group5.Entity.AppUser;
import com.example.Group5.Entity.UserRole;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepo extends PagingAndSortingRepository<UserRole, Long> {
    List<UserRole> findAllByAppRole(Optional<AppRole> a);
    List<UserRole> findByAppUser(AppUser b);
    void deleteByAppUser(AppUser c);
}
