package com.example.Group5.Repository;

import com.example.Group5.Entity.AppUser;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AppUserRepo extends PagingAndSortingRepository<AppUser, Long> {
    AppUser findByUserId(long a);
}
