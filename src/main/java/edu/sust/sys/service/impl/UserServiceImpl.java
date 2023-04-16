package edu.sust.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sust.common.utils.JwtUtil;
import edu.sust.sys.entity.Menu;
import edu.sust.sys.entity.User;
import edu.sust.sys.entity.UserRole;
import edu.sust.sys.mapper.MenuMapper;
import edu.sust.sys.mapper.UserMapper;
import edu.sust.sys.mapper.UserRoleMapper;
import edu.sust.sys.service.IMenuService;
import edu.sust.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ylc
 * @since 2023-03-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Map<String, Object> login(User user) {
        //1.根据用户名查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        User loginUser = baseMapper.selectOne(wrapper);
        //2.结果不为空，则登录成功
        if (loginUser != null && passwordEncoder.matches(user.getPassword(), loginUser.getPassword())) {
            //生成 Token
            String token = jwtUtil.createToken(loginUser);
            //返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            return data;
        }
        return null;
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        //从token中解析出用户对象
        User loginUser = null;
        try {
            loginUser = jwtUtil.parseToken(token, User.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("name", loginUser.getUsername());
        data.put("avatar", loginUser.getAvatar());
        //角色列表（内连接查询）
        List<String> roleList = baseMapper.getRoleNameByUserId(loginUser.getId());
        data.put("roles", roleList);

        //权限列表（查询用户所有的权限）
        List<Menu> menuList = menuService.getMenuListByUserId(loginUser.getId());
        data.put("menuList", menuList);
        return data;
    }

    @Override
    public void logout(String token) {
//        redisTemplate.delete(token);
    }

    @Override
    @Transactional
    public void addUser(User user) {
        //1、写入用户表
        this.baseMapper.insert(user);
        //2、写入用户角色表
        List<Integer> roleIdList = user.getRoleIdList();
        if (roleIdList != null) {
            for (Integer roleId : roleIdList) {
                userRoleMapper.insert(new UserRole(null, user.getId(), roleId));
            }
        }
    }

    @Override
    public User getUserById(Integer id) {
        //1、查出用户
        User user = this.baseMapper.selectById(id);
        //2、从用户角色表查出用户拥有的角色ID
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, id);
        List<UserRole> userRoleList = userRoleMapper.selectList(wrapper);
        List<Integer> roleIdList = new ArrayList<>();
        for (UserRole userRole : userRoleList) {
            roleIdList.add(userRole.getRoleId());
        }
        user.setRoleIdList(roleIdList);
        return user;
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        //1、更新用户表
        user.setPassword(null);
        this.baseMapper.updateById(user); //为空的字段不会更新
        //2、删除用户所有角色
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, user.getId());
        userRoleMapper.delete(wrapper);
        //3、重新给用户分配角色
        List<Integer> roleIdList = user.getRoleIdList();
        if (roleIdList != null) {
            for (Integer roleId : roleIdList) {
                userRoleMapper.insert(new UserRole(null, user.getId(), roleId));
            }
        }
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        //1、删除用户表
        this.baseMapper.deleteById(id);
        //2、删除用户角色表
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, id);
        userRoleMapper.delete(wrapper);
    }
}
