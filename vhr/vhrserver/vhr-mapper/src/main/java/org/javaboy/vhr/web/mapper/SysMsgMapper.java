package org.javaboy.vhr.web.mapper;

import org.javaboy.vhr.web.model.SysMsg;

public interface SysMsgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysMsg record);

    int insertSelective(SysMsg record);

    SysMsg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysMsg record);

    int updateByPrimaryKey(SysMsg record);

    int deleteByHrid(Integer hid);
}