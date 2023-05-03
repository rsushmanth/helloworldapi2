package com.example.helloworldapi;

import com.example.helloworldapi.exchanges.WishUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.regex.Pattern;

@RestController
public class RestAPIController {

    @Autowired
    UserService userService;
    @PutMapping("/hello/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addUser(@PathVariable String username,@Validated @RequestBody User user)
    {
        user.setUserName(username);
        LocalDate today= LocalDate.now();
        if(user.getDateOfBirth().isBefore(today) && Pattern.matches("[a-zA-Z]+",username)){
            userService.addOrUpdate(user);
        }
//        return "Hello Manan";
    }
    @GetMapping("/hello/{username}")
    public WishUser wishUser(@PathVariable String username){
        System.out.println("username->"+username);
        System.out.println(userService.getUser(username));
        User savedUser=userService.getUser(username);
        WishUser response=new WishUser();
        if(savedUser==null){
            response.setMessage("User Not Found!");
        }
        LocalDate today= LocalDate.now();

        if(savedUser.getDateOfBirth().getDayOfMonth()==today.getDayOfMonth() && savedUser.getDateOfBirth().getMonth()==today.getMonth()){
            response.setMessage("Hello,"+username+"! Happy Birthday!");

        }else{
            long daysUntilBirthday = ChronoUnit.DAYS.between(today, savedUser.getDateOfBirth().withYear(today.getYear()));
            response.setMessage("Hello,"+username+"! Your Birthday is in "+daysUntilBirthday+" day(s)");
        }
        return response;
    }
}
