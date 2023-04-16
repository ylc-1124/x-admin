package edu.sust.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sust.sys.entity.Menu;
import edu.sust.sys.mapper.MenuMapper;
import edu.sust.sys.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Override
    public List<Menu> getAllMenu() {
        // 一级菜单
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, 0);
        List<Menu> menuList = this.list(wrapper);
        setMenuChildren(menuList);
        return menuList;
    }

    /**
     * 递归填充子菜单
     */
    private void setMenuChildren(List<Menu> menuList) {
        if (menuList != null) {
            for (Menu menu : menuList) {
                LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Menu::getParentId, menu.getMenuId());
                List<Menu> subMenuList = this.list(wrapper);
                menu.setChildren(subMenuList);
                //递归设置子菜单
                setMenuChildren(subMenuList);
            }
        }
    }


    @Override
    public List<Menu> getMenuListByUserId(Integer id) {
        //一级菜单
        List<Menu> menuList = this.baseMapper.queryMenuListByUserId(id, 0);
        //递归设置子菜单
        setMenuChildrenByUserId(id, menuList);
        return menuList;
    }

    private void setMenuChildrenByUserId(Integer id, List<Menu> menuList) {
        if (menuList != null) {
            for (Menu menu : menuList) {
                List<Menu> subMenuList = this.baseMapper.queryMenuListByUserId(id, menu.getMenuId());
                menu.setChildren(subMenuList);
                //递归
                setMenuChildrenByUserId(id, subMenuList);
            }
        }
    }
}
