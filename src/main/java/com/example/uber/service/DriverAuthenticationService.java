package com.example.uber.service;

import com.example.uber.model.Drivers;
import com.example.uber.model.UserRole;
import com.example.uber.repository.DriverAuthenticationRepository;
import com.example.uber.utils.EmailValidator;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@AllArgsConstructor
@Service
public class DriverAuthenticationService implements UserDetailsService {


    private final DriverAuthenticationRepository driverAuthenticationRepository;
    private final EmailValidator emailValidator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return driverAuthenticationRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public Map<String, Object> driverInfo(String username, String token) throws UsernameNotFoundException {

        Drivers driver = driverAuthenticationRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Driver not found"));

        int id = driver.getId();
        String currentUsername = driver.getUsername();
        String firstname = driver.getFirstname();
        String lastname = driver.getLastname();
        String number = driver.getNumber();
        UserRole role = driver.getUserRole();
        String car = driver.getCar();
        String image = driver.getImage();

        Map<String, Object> driverInfo = new HashMap<>();
        driverInfo.put("id", id);
        driverInfo.put("username", currentUsername);
        driverInfo.put("firstname", firstname);
        driverInfo.put("lastname", lastname);
        driverInfo.put("role", role);
        driverInfo.put("number", number);
        driverInfo.put("car", car);
        driverInfo.put("image", image);
        driverInfo.put("token", token);

        return driverInfo;
    }

    public void signUp(Drivers driver){

        boolean isValidEmail = emailValidator.test(driver.getUsername());
        if(!isValidEmail){
            throw new IllegalStateException("Email not valid");
        }

        boolean userUsernameExists = driverAuthenticationRepository.findByUsername(driver.getUsername()).isPresent();
        if(userUsernameExists){
            throw new IllegalStateException("User already Exists.");
        }

        driver.setPassword(bCryptPasswordEncoder.encode(driver.getPassword()));
        driver.setUserRole(UserRole.USER);

        driverAuthenticationRepository.save(driver);

    }


}
