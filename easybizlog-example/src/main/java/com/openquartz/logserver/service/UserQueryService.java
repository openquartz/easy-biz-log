package com.openquartz.logserver.service;

import org.apache.catalina.User;

import java.util.List;

/**
 * @author svnee
 */
public interface UserQueryService {
    List<User> getUserList(List<String> userIds);
}
