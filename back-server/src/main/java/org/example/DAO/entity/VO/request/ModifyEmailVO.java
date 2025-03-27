package org.example.DAO.entity.VO.request;

import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;

public class ModifyEmailVO {
    @Email
    String email;
    @Length(max = 6, min = 6)
    String code;
}
