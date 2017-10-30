package com.trs.gallery.mapper.generated;

import com.trs.gallery.entity.generated.UserAccessInfo;
import com.trs.gallery.entity.generated.UserAccessInfoExample;
import java.util.List;

public interface UserAccessInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gallery_user_access
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer accessId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gallery_user_access
     *
     * @mbggenerated
     */
    int insert(UserAccessInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gallery_user_access
     *
     * @mbggenerated
     */
    int insertSelective(UserAccessInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gallery_user_access
     *
     * @mbggenerated
     */
    List<UserAccessInfo> selectByExample(UserAccessInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gallery_user_access
     *
     * @mbggenerated
     */
    UserAccessInfo selectByPrimaryKey(Integer accessId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gallery_user_access
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(UserAccessInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gallery_user_access
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(UserAccessInfo record);
}