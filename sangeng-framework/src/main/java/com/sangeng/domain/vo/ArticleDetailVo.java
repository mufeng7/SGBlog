package com.sangeng.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailVo {
    private Long id;
    //标题
    private String title;

    //文章摘要
    private String summary;
    //所属分类id
    private Long categoryId;

    private String categoryName;
    //缩略图
    private String thumbnail;

    private String content;
    //访问量
    private Long viewCount;



    private Date createTime;

}
