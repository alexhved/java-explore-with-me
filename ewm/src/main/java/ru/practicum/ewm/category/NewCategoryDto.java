package ru.practicum.ewm.category;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewCategoryDto {

    @NotBlank
    @Size(max = 20)
    private String name;
}
