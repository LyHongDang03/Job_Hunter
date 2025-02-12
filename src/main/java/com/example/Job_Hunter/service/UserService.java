package com.example.Job_Hunter.service;
import com.example.Job_Hunter.domain.Entity.Company;
import com.example.Job_Hunter.domain.Entity.Role;
import com.example.Job_Hunter.domain.Entity.User;
import com.example.Job_Hunter.domain.request.UpdateUserReq;
import com.example.Job_Hunter.domain.response.ResCreateUserDTO;
import com.example.Job_Hunter.domain.response.ResUpdateUserDTO;
import com.example.Job_Hunter.domain.response.ResUserDTO;
import com.example.Job_Hunter.domain.response.ResultPaginationDTO;
import com.example.Job_Hunter.repository.UserRepository;
import com.example.Job_Hunter.utill.exception.IdInvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyService companyService;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CompanyService companyService, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyService = companyService;
        this.roleService = roleService;
    }

    public User createUser(User user) throws IdInvalidException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getCompany() != null){
            Optional<Company> optionalCompany = companyService.findById(user.getCompany().getId());
            user.setCompany(optionalCompany.orElse(null));
        }
        if (user.getRole() != null){
            Role role = roleService.getRoleById(user.getRole().getId());
            user.setRole(role != null ? role : null);
        }

        return userRepository.save(user);
    }
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    public ResultPaginationDTO getAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> pageUsers = userRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageUsers.getTotalPages());
        meta.setTotal(pageUsers.getTotalElements());
        rs.setMeta(meta);
        List<ResUserDTO> users = pageUsers.getContent()
                .stream()
                .map(item -> new ResUserDTO(
                        item.getId(),
                        item.getUsername(),
                        item.getEmail(),
                        item.getAge(),
                        item.getGender(),
                        item.getAddress(),
                        item.getCreatedAt(),
                        item.getUpdatedAt(),
                        new ResUserDTO.CompanyUserDTO(
                                item.getCompany() != null ? item.getCompany().getId() : 0,
                                item.getCompany() != null ? item.getCompany().getName() : null
                        ), new ResUserDTO.RoleUserDTO(
                               item.getRole() != null ? item.getRole().getId() : 0,
                               item.getRole() != null ? item.getRole().getName() : null
                )
                ))
                        .collect(Collectors.toList());
        rs.setResult(users);
        return rs;
    }
    public User updateUser(UpdateUserReq req) throws IdInvalidException {
          User user = getUserById(req.getId());
          user.setUsername(req.getUsername());
          user.setPassword(passwordEncoder.encode(req.getPassword()));
          user.setEmail(req.getEmail());
          user.setAge(req.getAge());
          user.setGender(req.getGender());
          user.setAddress(req.getAddress());
          if (req.getCompany() != null){
              Optional<Company> optionalCompany = companyService.findById(req.getCompany().getId());
              user.setCompany(optionalCompany.orElse(null));
          }
        if (user.getRole() != null){
            Role role = roleService.getRoleById(user.getRole().getId());
            user.setRole(role != null ? role : null);
        }

          return userRepository.save(user);
    }

    public void deleteUser(Long id) throws IdInvalidException {
        if (id > 2000){
            throw new IdInvalidException("Abc");
        }
        userRepository.deleteById(id);
    }
    public User getUserByUsername(String username) {
        return userRepository.findByEmail(username);
    }
    public User getMyInFor(){
        var name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(name);
    }
    public boolean isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }
    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();
        ResCreateUserDTO.CompanyUserDTO companyUserDTO = new ResCreateUserDTO.CompanyUserDTO();
        res.setId(user.getId());
        res.setUsername(user.getUsername());
        res.setEmail(user.getEmail());
        res.setAge(user.getAge());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        if (user.getCompany() != null){
            companyUserDTO.setId(user.getCompany().getId());
            companyUserDTO.setName(user.getCompany().getName());
            res.setCompany(companyUserDTO);
        }
        return res;
    }
    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        ResUpdateUserDTO.CompanyUserDTO companyUserDTO = new ResUpdateUserDTO.CompanyUserDTO();
        res.setId(user.getId());
        res.setUsername(user.getUsername());
        res.setAge(user.getAge());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setUpdatedBy(user.getUpdatedBy());
        if (user.getCompany() != null){
            companyUserDTO.setId(user.getCompany().getId());
            companyUserDTO.setName(user.getCompany().getName());
            res.setCompany(companyUserDTO);
        }
        return res;
    }
    public void updateUserToken(String token, String email) {
        User user = getUserByUsername(email);
        if (user != null) {
            user.setRefreshToken(token);
            userRepository.save(user);
        }
    }
    public User getUserRefreshTokenAndEmail(String token, String email) {
        return userRepository.findByRefreshTokenAndEmail(token, email);
    }
}
