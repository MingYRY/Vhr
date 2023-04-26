package org.javaboy.vhr.web.service;

import org.javaboy.vhr.web.mapper.PoliticsstatusMapper;
import org.javaboy.vhr.web.model.Politicsstatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @作者 江南一点雨
 * @公众号 江南一点雨
 * @微信号 a_java_boy
 * @GitHub https://github.com/lenve
 * @博客 http://wangsong.blog.csdn.net
 * @网站 http://www.javaboy.org
 * @时间 2019-11-03 23:20
 */
@Service
public class PoliticsstatusService {
    @Resource
    PoliticsstatusMapper politicsstatusMapper;
    public List<Politicsstatus> getAllPoliticsstatus() {
        return politicsstatusMapper.getAllPoliticsstatus();
    }
}
