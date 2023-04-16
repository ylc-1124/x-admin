package edu.sust.sys.mapper;

import edu.sust.sys.entity.RoleMenu;
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
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    List<Integer> selectMenuIdListByRoleId(Integer roleId);
}
