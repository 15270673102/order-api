package com.loushi.vo.order;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoTaskItemVO {

    private List<Object> comments;
    private List<Object> likes;
    private List<Object> forwards;

}
