package com.trs.gallery.mapper.generated;

import com.trs.gallery.entity.generated.UserCollectInfo;
import com.trs.gallery.entity.generated.UserCollectInfoExample;
import com.trs.gallery.entity.generated.UserCollectInfoKey;
import java.util.List;

public interface UserCollectInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gallery_user_collect
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(UserCollectInfoKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gallery_user_collect
     *
     * @mbggenerated
     */
    int insert(UserCollectInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gallery_user_collect
     *
     * @mbggenerated
     */
    int insertSelective(UserCollectInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gallery_user_collect
     *
     * @mbggenerated
     */
    List<UserCollectInfo> selectByExample(UserCollectInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gallery_user_collect
     *
     * @mbggenerated
     */
    UserCollectInfo selectByPrimaryKey(UserCollectInfoKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gallery_user_collect
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(UserCollectInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gallery_user_collect
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(UserCollectInfo record);
}