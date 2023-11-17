package com.mirandez.meetagora.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface LoginMapper {

    @Update({"update user set last_connection = now() , " +
            "  status_id = 1 " +
            " where institutional_email = #{email} and is_validate = true "})
    int loginAndUpdateUserLastConection(@Param( "email" ) String email);

    @Select({"SELECT password FROM user where institutional_email = #{email} limit 1"})
    String getPasswordByEmail(@Param("email") String email);

    @Update({"update user set last_connection = now() , " +
            "  status_id = 1 " +
            " where token = #{token} and is_validate = true "})
    int loginAndUpdateUserLastConectionByToken(@Param( "token" ) String token);
}
