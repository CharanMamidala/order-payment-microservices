package com.example.products;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByAdminId(Long adminId);
    List<User> findByUserType(String userType);
    List<User> findByUserTypeAndIsActive(String userType, Boolean isActive);
    List<User> findByAdminIdAndIsActive(Long adminId, Boolean isActive);
    Optional<User> findByUsernameAndIsActive(String username, Boolean isActive);
    List<User> findByStoreId(Long storeId);
    List<User> findByStoreIdAndIsActive(Long storeId, Boolean isActive);
    List<User> findByStoreIdAndUserType(Long storeId, String userType);
    List<User> findByStoreIdAndUserTypeAndIsActive(Long storeId, String userType, Boolean isActive);
} 