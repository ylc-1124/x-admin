package edu.sust.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sust.sys.entity.Role;
import edu.sust.sys.entity.RoleMenu;
import edu.sust.sys.mapper.RoleMapper;
import edu.sust.sys.mapper.RoleMenuMapper;
import edu.sust.sys.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ylc
 * @since 2023-03-14
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Override
    @Transactional
    public void addRole(Role role) {
        //1、添加到角色表（插入完后 roleId会自动回填）
        this.baseMapper.insert(role);
        //2、添加角色菜单表内容（权限表）
        List<Integer> menuIdList = role.getMenuIdList();
        if (menuIdList != null) {
            for (Integer menuId : menuIdList) {
                roleMenuMapper.insert(new RoleMenu(null, role.getRoleId(), menuId));
            }
        }
    }

    @Override
    public Role getRoleById(Integer id) {
        Role role = this.baseMapper.selectById(id);
        //只查询出叶子结点的菜单的ID，因为前端勾选了子菜单内容，父菜单就会半勾选了
        List<Integer> menuIdList = roleMenuMapper.selectMenuIdListByRoleId(id);
        role.setMenuIdList(menuIdList);
        return role;
    }

    @Override
    @Transactional
    public void updateRole(Role role) {
        //1、更新角色表
        this.baseMapper.updateById(role);
        //2、删除角色的所有权限
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, role.getRoleId());
        roleMenuMapper.delete(wrapper);
        //3、给角色重新授予权限
        List<Integer> menuIdList = role.getMenuIdList();
        if (menuIdList != null) {
            for (Integer menuId : menuIdList) {
                roleMenuMapper.insert(new RoleMenu(null, role.getRoleId(), menuId));
            }
        }
    }

    @Override
    @Transactional
    public void deleteRoleById(Integer id) {
        //1、角色删除
        this.baseMapper.deleteById(id);
        //2、权限删除
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, id);
        roleMenuMapper.delete(wrapper);
    }
}
