package com.company.action.backend;

import com.company.Commons.Const;
import com.company.Commons.ServerResponse;
import com.company.dao.pojo.User;
import com.company.service.iservice.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/user/")
public class UserManageAction {

    @Autowired
    @Qualifier("userService")
    IUserService userService;

    /**
     * 后台登录
     * @param session
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(HttpSession session,String username, String password){
        ServerResponse responseResult = userService.login(username,password);
        if(responseResult.isSuccess()){
            User user = (User) responseResult.getData();
            if(user.getRole()== Const.Role.ROLE_ADMIN){
                session.setAttribute(Const.CURRENTUSER,user);
            }else{
                return ServerResponse.createErrorMsgResponse("该用户没有权限");
            }
        }
        return responseResult;
    }
}
