package com.trs.gallery.mapper.generated;

import com.trs.gallery.entity.generated.LayoutInfo;
import com.trs.gallery.entity.generated.LayoutInfoExample;
import java.util.List;

public interface LayoutInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gallery_channel_layout
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer channelId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gallery_channel_layout
     *
     * @mbggenerated
     */
    int insert(LayoutInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gallery_channel_layout
     *
     * @mbggenerated
     */
    int insertSelective(LayoutInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gallery_channel_layout
     *
     * @mbggenerated
     */
    List<LayoutInfo> selectByExample(LayoutInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gallery_channel_layout
     *
     * @mbggenerated
     */
    LayoutInfo selectByPrimaryKey(Integer channelId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gallery_channel_layout
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(LayoutInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gallery_channel_layout
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(LayoutInfo record);
}