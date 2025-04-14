package com.fpt_be.fpt_be.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fpt_be.fpt_be.Repository.UserRepository;
import com.fpt_be.fpt_be.Request.DangKyRequest;
import com.fpt_be.fpt_be.Entity.User;
import com.fpt_be.fpt_be.Dto.UserDto;

@Service
public class UserService {
    @Autowired  
    private UserRepository userRepository;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    public String dangKy(DangKyRequest dangKyRequest) {
        // Check if email already exists
        if (userRepository.existsByEmail(dangKyRequest.getEmail())) {
            throw new RuntimeException("Email đã tồn tại trong hệ thống");
        } else {
            User newUser = new User();
            newUser.setEmail(dangKyRequest.getEmail());
            String encodedPassword = passwordEncoder.encode(dangKyRequest.getPassword());
            newUser.setPassword(encodedPassword);
            newUser.setHoVaTen(dangKyRequest.getHoVaTen());
            newUser.setSdt(dangKyRequest.getSdt());
            newUser.setDiaChi(dangKyRequest.getDiaChi());
            newUser.setNgaySinh(dangKyRequest.getNgaySinh());
            newUser.setGioiTinh(dangKyRequest.getGioiTinh());
            userRepository.save(newUser);
            return "Đăng ký thành công";
        }
    }
    public User dangNhap(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Email không tồn tại");
        }
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            throw new RuntimeException("Mật khẩu không chính xác");
        }
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với id: " + id));
    }

    // Update user
    public User updateUser(Long id, UserDto userDto) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setEmail(userDto.getEmail());
            if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(userDto.getPassword());
                user.setPassword(encodedPassword);
            }
            user.setHoVaTen(userDto.getHoVaTen());
            user.setSdt(userDto.getSdt());
            user.setDiaChi(userDto.getDiaChi());
            user.setNgaySinh(userDto.getNgaySinh());
            user.setGioiTinh(userDto.getGioiTinh());
            return userRepository.save(user);
        }
        throw new RuntimeException("Không tìm thấy người dùng với id: " + id);
    }

    // Delete user
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy người dùng với id: " + id);
        }
        userRepository.deleteById(id);
    }

    public UserDto getUserProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với id: " + id));
        
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setHoVaTen(user.getHoVaTen());
        userDto.setSdt(user.getSdt());
        userDto.setDiaChi(user.getDiaChi());
        userDto.setNgaySinh(user.getNgaySinh());
        userDto.setGioiTinh(user.getGioiTinh());
        
        return userDto;
    }

    public UserDto updateUserProfile(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với id: " + id));
        
        if (userDto.getHoVaTen() != null) {
            user.setHoVaTen(userDto.getHoVaTen());
        }
        if (userDto.getSdt() != null) {
            user.setSdt(userDto.getSdt());
        }
        if (userDto.getDiaChi() != null) {
            user.setDiaChi(userDto.getDiaChi());
        }
        if (userDto.getNgaySinh() != null) {
            user.setNgaySinh(userDto.getNgaySinh());
        }
        if (userDto.getGioiTinh() != null) {
            user.setGioiTinh(userDto.getGioiTinh());
        }
        
        User updatedUser = userRepository.save(user);
        
        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setId(updatedUser.getId());
        updatedUserDto.setEmail(updatedUser.getEmail());
        updatedUserDto.setHoVaTen(updatedUser.getHoVaTen());
        updatedUserDto.setSdt(updatedUser.getSdt());
        updatedUserDto.setDiaChi(updatedUser.getDiaChi());
        updatedUserDto.setNgaySinh(updatedUser.getNgaySinh());
        updatedUserDto.setGioiTinh(updatedUser.getGioiTinh());
        
        return updatedUserDto;
    }

    public void changePassword(Long id, String currentPassword, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với id: " + id));
        
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Mật khẩu hiện tại không chính xác");
        }
        
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        userRepository.save(user);
    }
}