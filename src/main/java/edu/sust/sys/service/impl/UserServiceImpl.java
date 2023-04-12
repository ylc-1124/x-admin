package edu.sust.sys.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sust.sys.entity.User;
import edu.sust.sys.mapper.UserMapper;
import edu.sust.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.tomcat.util.json.JSONFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    private RedisTemplate redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, Object> login(User user) {
        //1.根据用户名查询(这里只是最简单的实现，未进行参数校验)
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        User loginUser = baseMapper.selectOne(wrapper);
        //2.结果不为空，则登录成功
        if (loginUser != null && passwordEncoder.matches(user.getPassword(), loginUser.getPassword())) {
            //生成 token(暂时使用UUID,终极方案:jwt)
            String key = "user:" + UUID.randomUUID();
            //存入redis（表示用户处于登录状态,设置保存时间为30min）
            loginUser.setPassword(null);
            redisTemplate.opsForValue().set(key, loginUser, 30, TimeUnit.MINUTES);
            //返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("token", key);
            return data;
        }
        return null;
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        //1、根据token从redis获取用户信息
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
            //反序列化成 User对象
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            //返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("name", loginUser.getUsername());
            data.put("avatar", loginUser.getAvatar());
            //角色列表（内连接查询）
            List<String> roleList = baseMapper.getRoleNameByUserId(loginUser.getId());
            data.put("roles", roleList);
            return data;
        }
        return null;
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete(token);
    }
}
