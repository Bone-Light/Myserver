package org.example.DAO.entity.VO.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RenameClientVO {
    @NotNull
    int id;
    @Length(min = 1, max = 10)
    String name;
}
