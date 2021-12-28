package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credentials;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialsMapper {

    @Select("SELECT * FROM CREDENTIALS")
    List<Credentials> getAllCredentials();

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userid}")
    List<Credentials> getUserCredentials(int userId);

    @Select("SELECT * FROM CREDENTIALS WHERE CredentialId = #{credentialId}")
    Credentials getCredentialsById(Integer credentialId);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) " + "VALUES(#{url}," +
            "#{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    void addCredentials(Credentials credentials);

    @Delete("DELETE FROM CREDENTIALS WHERE CredentialId = #{credentialId}")
    int deleteCredentials(Integer credentialId);

    @Update("UPDATE CREDENTIALS" +
            " SET url = #{url}," +
            " username = #{username}," +
            " password = #{password}," +
            " key = #{key}" +
            " WHERE credentialid = #{credentialId};")
    void updateCredentials(Credentials credentials);
}
