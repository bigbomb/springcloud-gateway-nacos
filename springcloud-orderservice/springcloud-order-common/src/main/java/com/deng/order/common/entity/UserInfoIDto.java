package com.deng.order.common.entity;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

public class UserInfoIDto {
    private Long id;

    @NotBlank
    @Length(min=3, max=10)
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp="^((13[0-9])|(15[^4,\\D])|(18[0,3-9]))\\d{8}$", message="手机号格式不正确")
    private String phone;

    @Min(value=18)
    @Max(value = 200)
    private int age;

    @NotBlank
    @Length(min=6, max=12, message="昵称长度为6到12位")
    private String nickname;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    // Getter & Setter
}
