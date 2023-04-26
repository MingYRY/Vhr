package org.javaboy.vhr.web.service;

import org.javaboy.vhr.web.mapper.HrMapper;
import org.javaboy.vhr.web.mapper.HrRoleMapper;
import org.javaboy.vhr.web.mapper.SysMsgMapper;
import org.javaboy.vhr.web.model.Hr;
import org.javaboy.vhr.web.utils.HrUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Hr管理
 */
@Service
public class HrService implements UserDetailsService {
    @Resource
    HrMapper hrMapper;
    @Resource
    HrRoleMapper hrRoleMapper;
    @Resource
    SysMsgMapper sysMsgMapper;

    /**
     * 这个方法是，在登录验证器登录验证之后，开始进行一个无权限的认证，security中会帮助判断是否支持该登录方式
     * 实现该方法来获取用户是否存在，并且获取当前用户的角色信息，以便于后面做角色验证
     * 获取到当前用户后，回到AbstractUserDetailsAuthenticationProvider方法，他进行
     * preAuthenticationChecks，—预检查，检查三个布尔异常，密码是否为空等等
     * postAuthenticationChecks
     * additionalAuthenticationChecks将UsernamePasswordAuthenticationToken中的Credential和角色的密码相对比，如果相同则验证成功
     * 最后构建一个createSuccessAuthentication
     * Hr里面已经把账号的信息都给验证过了，都设置为了true
     * 返回到AbstractUserDetailsAuthenticationProvider类中调用**createSuccessAuthentication(principalToReturn, authentication, user)**方法
     * 这个时候，调用三个参数的UsernamePasswordAuthenticationToken构造器，因为已经给予了所赋予的角色权限信息
     * 这时候会检查角色权限集合，如果其中一个权限为空，则抛出异常
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Hr hr = hrMapper.loadUserByUsername(username);
        if (hr == null) {
            throw new UsernameNotFoundException("用户名不存在!");
        }
        hr.setRoles(hrMapper.getHrRolesById(hr.getId()));
        return hr;
    }

    public List<Hr> getAllHrs(String keywords) {
        return hrMapper.getAllHrs(HrUtils.getCurrentHr().getId(),keywords);
    }

    public Integer updateHr(Hr hr) {
        return hrMapper.updateByPrimaryKeySelective(hr);
    }

    @Transactional
    public boolean updateHrRole(Integer hrid, Integer[] rids) {
        hrRoleMapper.deleteByHrid(hrid);
        return hrRoleMapper.addRole(hrid, rids) == rids.length;
    }

    @Transactional
    public Integer deleteHrById(Integer id) {
        hrRoleMapper.deleteByHrid(id);
        int num = sysMsgMapper.deleteByHrid(id);
        System.out.println(num);
        return hrMapper.deleteByPrimaryKey(id);
    }

    public List<Hr> getAllHrsExceptCurrentHr() {
        return hrMapper.getAllHrsExceptCurrentHr(HrUtils.getCurrentHr().getId());
    }

    public Integer updateHyById(Hr hr) {
        return hrMapper.updateByPrimaryKeySelective(hr);
    }

    public boolean updateHrPasswd(String oldpass, String pass, Integer hrid) {
        Hr hr = hrMapper.selectByPrimaryKey(hrid);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oldpass, hr.getPassword())) {
            String encodePass = encoder.encode(pass);
            Integer result = hrMapper.updatePasswd(hrid, encodePass);
            if (result == 1) {
                return true;
            }
        }
        return false;
    }

    public Integer updateUserface(String url, Integer id) {
        return hrMapper.updateUserface(url, id);
    }
}
