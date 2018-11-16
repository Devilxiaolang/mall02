package com.company.service.impl;

import com.company.Commons.Const;
import com.company.Commons.ServerResponse;
import com.company.dao.UserMapper;
import com.company.dao.pojo.User;
import com.company.service.iservice.IUserService;
import com.company.util.MD5Util;
import com.company.util.TokenCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("userService")
public class UserServiceImpl implements IUserService {
    @Autowired
    @Qualifier("userMapper")
    UserMapper userMapper;

    /**
     * 用户登录 校验用户是否存在 MD5
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public ServerResponse login(String username, String password) {
        int currentResult = userMapper.checkUsername(username);
        if (currentResult == 0) {
            return ServerResponse.createErrorResponse("用户不存在");
        }
        User user = userMapper.login(username, MD5Util.MD5EncodeUtf8(password));
        if (user != null) {
            //清空密码
            user.setPassword(StringUtils.EMPTY);
            return ServerResponse.createSuccessResponse(user);
        }
        return ServerResponse.createErrorResponse("密码不正确");
    }

    /**
     * checkVaild 成功说明没有当前用户名
     *
     * @param str
     * @param type
     * @return
     */
    @Override
    public ServerResponse checkVaild(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            if (Const.VaildType.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createErrorMsgResponse("用户名已存在");
                }
            }
            if (Const.VaildType.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createErrorMsgResponse("邮箱已存在");
                }
            }
        } else {
            return ServerResponse.createErrorMsgResponse("参数错误");
        }
        return ServerResponse.createSuccessResponse();
    }

    @Override
    public ServerResponse registry(User user) {
        ServerResponse vaildResult = this.checkVaild(user.getUsername(), Const.VaildType.USERNAME);
        if (!vaildResult.isSuccess()) {
            return ServerResponse.createErrorMsgResponse("用户名已经被占用");
        }
        vaildResult = this.checkVaild(user.getEmail(), Const.VaildType.EMAIL);
        if (!vaildResult.isSuccess()) {
            return ServerResponse.createErrorMsgResponse("邮箱已经被占用");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        user.setRole(Const.Role.ROLE_USER);
        int resultCount = userMapper.insertSelective(user);
        if (resultCount > 0) {
            return ServerResponse.createSuccessMsgResponse("注册成功");
        }
        return ServerResponse.createErrorMsgResponse("注册失败");
    }

    @Override
    public ServerResponse<String> forgetGetQuestion(String username) {
        ServerResponse vaildResult = this.checkVaild(username, Const.VaildType.USERNAME);
        if (vaildResult.isSuccess()) {
            return ServerResponse.createErrorMsgResponse("没有当前用户");
        }
        String question = userMapper.getQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createSuccessResponse(question);
        }
        return ServerResponse.createErrorMsgResponse("当前用户没有设置问题");
    }

    @Override
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey("token_", forgetToken);
            return ServerResponse.createSuccessResponse(forgetToken);
        }
        return ServerResponse.createErrorMsgResponse("预留问题回答不正确");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String password, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createErrorMsgResponse("Token令牌失效");
        }
        if (this.checkVaild(username, Const.VaildType.USERNAME).isSuccess()) {
            return ServerResponse.createErrorMsgResponse("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX);
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createErrorMsgResponse("Token令牌失效");
        }
        if (StringUtils.equals(token, forgetToken)) {
            int resultCount = userMapper.forgetResetPassword(username, MD5Util.MD5EncodeUtf8(password));
            if (resultCount > 0) {
                return ServerResponse.createSuccessMsgResponse("修改密码成功");
            }
            return ServerResponse.createErrorMsgResponse("修改密码失败");
        }
        return ServerResponse.createErrorMsgResponse("Token令牌错误");
    }

    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        int resultCount = userMapper.checkPassword(user.getId(), MD5Util.MD5EncodeUtf8(passwordOld));
        if (resultCount == 0) {
            return ServerResponse.createErrorMsgResponse("密码错误");
        }
        int checkCount = userMapper.forgetResetPassword(user.getUsername(), MD5Util.MD5EncodeUtf8(passwordNew));
        if (checkCount == 0) {
            return ServerResponse.createErrorMsgResponse("修改密码失败");
        }
        return ServerResponse.createSuccessMsgResponse("修改密码成功");
    }

    @Override
    public ServerResponse<User> updateInformation(User user) {
        int resultCount = userMapper.checkEmailById(user.getId(),user.getEmail());
        if(resultCount>0){
            return ServerResponse.createErrorMsgResponse("邮箱已经被占用");
        }
        User userUpdate = new User();
        //动态sql 不改的需要为null(createtime)
        userUpdate.setId(user.getId());
        userUpdate.setUsername(user.getUsername());
        userUpdate.setEmail(user.getEmail());
        userUpdate.setPhone(user.getPhone());
        userUpdate.setQuestion(user.getQuestion());
        userUpdate.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(userUpdate);
        if(updateCount>0){
            return ServerResponse.createSuccessResponse("修改资料成功",userUpdate);
        }
        return ServerResponse.createErrorMsgResponse("修改资料失败");
    }

}
