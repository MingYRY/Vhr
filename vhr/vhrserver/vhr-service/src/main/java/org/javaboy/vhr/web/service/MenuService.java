package org.javaboy.vhr.web.service;

import org.javaboy.vhr.web.mapper.MenuMapper;
import org.javaboy.vhr.web.mapper.MenuRoleMapper;
import org.javaboy.vhr.web.model.Menu;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @作者 江南一点雨
 * @公众号 江南一点雨
 * @微信号 a_java_boy
 * @GitHub https://github.com/lenve
 * @博客 http://wangsong.blog.csdn.net
 * @网站 http://www.javaboy.org
 * @时间 2019-09-27 7:13
 */
@Service
@CacheConfig(cacheNames = "menus_cache")
public class MenuService {
    @Resource
    MenuMapper menuMapper;
    @Resource
    MenuRoleMapper menuRoleMapper;

    @Cacheable(key = "#id")
    public List<Menu> getMenusByHrId(Integer id) {
        return menuMapper.getMenusByHrId(id);
    }

    // @Cacheable 这个注解一般加在查询方法上，表示将一个方法的返回值缓存起来，
    // 默认情况下，缓存的 key 就是方法的参数，缓存的 value 就是方法的返回值。
    @Cacheable(key = "#root.methodName")
    public List<Menu> getAllMenusWithRole() {
        return menuMapper.getAllMenusWithRole();
    }

    public List<Menu> getAllMenus() {
        return menuMapper.getAllMenus();
    }

    public List<Integer> getMidsByRid(Integer rid) {
        return menuMapper.getMidsByRid(rid);
    }

    @Transactional
    public boolean updateMenuRole(Integer rid, Integer[] mids) {
        menuRoleMapper.deleteByRid(rid);
        if (mids == null || mids.length == 0) {
            return true;
        }
        Integer result = menuRoleMapper.insertRecord(rid, mids);
        return result==mids.length;
    }
}
