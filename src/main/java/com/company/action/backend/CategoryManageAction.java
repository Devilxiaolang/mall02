package com.company.action.backend;

import com.company.Commons.Const;
import com.company.Commons.ServerResponse;
import com.company.dao.pojo.Category;
import com.company.dao.pojo.User;
import com.company.service.iservice.ICategoryService;
import com.company.service.iservice.IUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/manage/category/")
public class CategoryManageAction {
    @Autowired
    @Qualifier("categoryService")
    ICategoryService categoryService;

    @Autowired
    @Qualifier("userService")
    IUserService userService;

    /**
     * 获取该节点的子节点
     *
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse<List<Category>> getCategory(HttpSession session, @RequestParam(value = "category", defaultValue = "0") int categoryId) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if (user == null) {
            return ServerResponse.createErrorMsgResponse("用户没有登录");
        }
        if (user.getRole() == Const.Role.ROLE_ADMIN) {
            return categoryService.getChildrenParallelCategory(categoryId);
        }
        return ServerResponse.createErrorMsgResponse("用户没有权限");
    }

    /**
     * 新增子节点
     *
     * @param session
     * @param categoryName
     * @param parentId
     * @return
     */
    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse<String> addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if (user == null) {
            return ServerResponse.createErrorMsgResponse("用户没有登录");
        }
        if (user.getRole() == Const.Role.ROLE_ADMIN) {
            return categoryService.addCategory(categoryName, parentId);
        }
        return ServerResponse.createErrorMsgResponse("用户没有权限");
    }

    /**
     * 修改品类名称
     * @param session
     * @param categoryId
     * @param categoryName
     * @return
     */
    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerResponse<String> setCategoryName(HttpSession session,int categoryId,String categoryName){
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if (user == null) {
            return ServerResponse.createErrorMsgResponse("用户没有登录");
        }
        if (user.getRole() == Const.Role.ROLE_ADMIN) {
            return categoryService.setCategoryName(categoryId, categoryName);
        }
        return ServerResponse.createErrorMsgResponse("用户没有权限");
    }

    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse<List<Integer>> getDeepCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") int categoryId){
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if (user == null) {
            return ServerResponse.createErrorMsgResponse("用户没有登录");
        }
        if (user.getRole() == Const.Role.ROLE_ADMIN) {
            return categoryService.getDeepCategory(categoryId);
        }
        return ServerResponse.createErrorMsgResponse("用户没有权限");
    }
}
