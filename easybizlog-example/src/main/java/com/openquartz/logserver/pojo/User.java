package com.openquartz.logserver.pojo;

import com.openquartz.javaobjdiff.annotation.DiffAlias;
import com.openquartz.javaobjdiff.annotation.DiffIgnore;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * @author svnee
 **/
@Data
public class User {

    private Long id;
    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    @DiffIgnore
    private Integer age;

    /**
     * 性别
     */
    @DiffAlias(alias = "性别")
    private String sex;

    /**
     * 用户地址
     */
    private Address address;

    /**
     * 爱好
     */
    @DiffIgnore
    private List<String> likeList;

    /**
     * 不喜欢
     */
    private List<String> noLikeList;

    @DiffIgnore
    private List<Address> testList;

    @DiffIgnore
    private String[] likeStrings;

    private String[] noLikeStrings;


    private LocalDateTime localDateTime;

    private LocalDate localDate;

    @Data
    public static class Address {
        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 区/县名称
         */
        private String areaName;
    }
}
