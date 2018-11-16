package com.company.action.portal;

import com.company.Commons.Const;
import com.company.Commons.ServerResponse;
import com.company.dao.pojo.User;
import com.company.service.iservice.IUserService;
import com.company.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserAction {
    @Autowired
    @Qualifier("userService")
    IUserService userService;

    /**
     * 登录
     * @param session
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(HttpSession session,String username,String password){
        ServerResponse responseResult = userService.login(username,password);
        if(responseResult.isSuccess()){
            session.setAttribute(Const.CURRENTUSER,responseResult.getData());
        }
        return  responseResult;
    }

    /**
     * 注销
     * @param session
     * @return
     */
    @RequestMapping(value = "logout.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse logout(HttpSession session){
        session.removeAttribute(Const.CURRENTUSER);
        return ServerResponse.createSuccessResponse();
    }

    /**
     * 对用户名或者邮箱进行校验
     * @param str
     * @param type
     * @return
     */
    @RequestMapping(value="check_valid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse checkVaild(String str,String type){
        return userService.checkVaild(str,type);
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @RequestMapping(value = "registry.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse registry(User user){
        return userService.registry(user);
    }

    /**
     * session获取当前用户信息
     * @param session
     * @return
     */
    @RequestMapping(value = "get_user_info.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if(user!=null){
            return ServerResponse.createSuccessResponse(user);
        }
        return ServerResponse.createErrorMsgResponse("用户未登录，无法获得当前信息");
    }

    /**
     * 忘记密码找到问题
     * @param username
     * @return
     */
    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return userService.forgetGetQuestion(username);
    }

    /**
     * 忘记密码回答预留问题 应用Token令牌和Guavv缓存 返回Token令牌
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping(value = "forget_check_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
        return userService.forgetCheckAnswer(username,question,answer);
    }

    /**
     * 根据用户名和token令牌修改密码
     * @param username
     * @param password
     * @param forgetToken
     * @return
     */
    @RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username,String password,String forgetToken){
        return userService.forgetResetPassword(username,password,forgetToken);
    }

    /**
     * 登录状态下修改密码
     * @param session
     * @param passwordOld
     * @param passwordNew
     * @return
     */

    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session,String passwordOld,String passwordNew){
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if(user==null){
            return ServerResponse.createErrorMsgResponse("没有用户登录");
        }
        return userService.resetPassword(passwordOld,passwordNew,user);
    }

    /**
     * 登录状态下修改用户信息
     * @param session
     * @param user
     * @return
     */
    @RequestMapping(value = "update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInformation(HttpSession session,User user){
        User currentUser = (User) session.getAttribute(Const.CURRENTUSER);
        if(currentUser==null){
            return ServerResponse.createErrorMsgResponse("没有用户登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        return userService.updateInformation(user);
    }


}
