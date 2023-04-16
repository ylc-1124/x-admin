package edu.sust.sys.service;

import edu.sust.sys.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ylc
 * @since 2023-03-14
 */
public interface IRoleService extends IService<Role> {

    void addRole(Role role);

    Role getRoleById(Integer id);

    void updateRole(Role role);

    void deleteRoleById(Integer id);
}
