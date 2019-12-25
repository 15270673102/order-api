package com.loushi.vo.login;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenMapVO {

    private String token;
    private String phone;
    private Integer userRole;

}
