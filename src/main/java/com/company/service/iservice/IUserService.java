package com.company.service.iservice;

import com.company.Commons.ServerResponse;
import com.company.dao.pojo.User;

public interface IUserService {
    ServerResponse login(String username, String password);

    ServerResponse checkVaild(String str, String type);

    ServerResponse registry(User user);

    ServerResponse<String> forgetGetQuestion(String username);

    ServerResponse<String> forgetCheckAnswer(String username, String question, String answer);

    ServerResponse<String> forgetResetPassword(String username, String password, String forgetToken);

    ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);

    ServerResponse<User> updateInformation(User user);
}
