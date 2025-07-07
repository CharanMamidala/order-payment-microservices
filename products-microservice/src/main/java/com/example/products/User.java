package com.example.products;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String userType; // "SUPER_ADMIN", "ADMIN", or "USER"

    @Column(unique = true)
    private String email;

    @Column
    private String phoneNumber;

    @Column
    private String businessName; // For admins (restaurant/store name)

    @Column
    private String businessType; // For admins (restaurant, grocery, etc.)

    @Column
    private String address; // For shipping orders

    @Column
    private Long adminId; // Links USER to their ADMIN (null for SUPER_ADMIN and ADMIN users)

    @Column
    private Long storeId; // Links user/admin to specific store (null for SUPER_ADMIN)

    @Column
    private Boolean isActive = true; // To deactivate users

    public User() {}

    public User(Long id, String username, String password, String userType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public User(Long id, String username, String password, String userType, String email, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public User(Long id, String username, String password, String userType, String email, String phoneNumber, 
                String businessName, String businessType, String address, Long adminId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.businessName = businessName;
        this.businessType = businessType;
        this.address = address;
        this.adminId = adminId;
    }

    public User(Long id, String username, String password, String userType, String email, String phoneNumber, 
                String businessName, String businessType, String address, Long adminId, Long storeId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.businessName = businessName;
        this.businessType = businessType;
        this.address = address;
        this.adminId = adminId;
        this.storeId = storeId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }
    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }
    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
} 