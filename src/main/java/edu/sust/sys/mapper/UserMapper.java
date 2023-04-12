package edu.sust.sys.mapper;

import edu.sust.sys.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ylc
 * @since 2023-03-14
 */
public interface UserMapper extends BaseMapper<User> {
    List<String> getRoleNameByUserId(Integer userId);
}
