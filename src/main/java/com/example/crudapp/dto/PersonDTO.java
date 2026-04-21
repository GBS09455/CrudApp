package com.example.crudapp.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = false)
public class PersonDTO {


    private Long id;
    @NotNull
    private String name;
   // @Size(min = 2, max = 15)
    @Min(value = 0)
    private int age;
    private List<ItemDTO> items;
}
