package com.edukg.open.model;

import lombok.Data;

import java.util.List;

/**
 * 操作信息
 *
 * @author tanzheng
 * @date 2023/12/21
 */

@Data
public class Operation {

    String type;

    List<List<String>> termdef;
}
