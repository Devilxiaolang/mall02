package com.company.dao;

import com.company.dao.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    User login(@Param("username") String username, @Param("password") String password);

    int checkEmail(String email);

    String getQuestionByUsername(String username);

    int checkAnswer(@Param("username") String username,@Param("question") String question,@Param("answer") String answer);

    int forgetResetPassword(@Param("username") String username, @Param("password") String password);

    int checkPassword(@Param("id") Integer id, @Param("password") String passoword);

    int checkEmailById(@Param("id") Integer id, @Param("email") String email);
}