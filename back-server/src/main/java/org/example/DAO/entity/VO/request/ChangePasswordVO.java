package org.example.DAO.entity.VO.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ChangePasswordVO {
    @Length(min = 6, max=20)
    String oldPassword;
    @Length(min = 6, max=20)
    String newPassword;
}
