package com.tech.utils;

import com.tech.model.User;
import org.springframework.beans.BeanUtils;

public class UserUtils {

    public static User getWritableUser(User securityContextHolderUser){
        User user = new User();
        BeanUtils.copyProperties(securityContextHolderUser, user);
        return user;
    }
}
