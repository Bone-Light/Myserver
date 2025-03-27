package org.example.DAO.entity.VO.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public class RenameNodeVO {
    @NotNull
    int id;
    @Length(min = 1, max = 10)
    String node;
    @Pattern(regexp = "(cn|hk|jp|us|sg|kr|de)")
    String location;
}
