package com.edukg.open.model;

import lombok.Data;

import java.util.List;

/**
 * 教材编辑的参数
 *
 * @author tanzheng
 * @date 2023/12/20
 */
@Data
public class Properties {

    String title;

    String isbn;

    String author;

    String publishDate;

    String publisher;
}
