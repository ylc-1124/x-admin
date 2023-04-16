package edu.sust.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.sust.common.vo.Result;
import edu.sust.sys.entity.Role;
import edu.sust.sys.entity.RoleMenu;
import edu.sust.sys.service.IRoleMenuService;
import edu.sust.sys.service.IRoleService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ylc
 * @since 2023-03-14
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @GetMapping("/all")
    public Result<List<Role>> getAllRole() {
        List<Role> list = roleService.list();
        return Result.success(list, "查询成功");
    }
    /**
     * 根据多条件查询角色信息
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> getRoleList(@RequestParam(value = "roleName", required = false) String roleName,
                                                   @RequestParam("pageNo") Long pageNo,
                                                   @RequestParam("pageSize") Long pageSize) {
        //这个类的好处是：写列名时不用写字符串
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(roleName), Role::getRoleName, roleName);
        wrapper.orderByDesc(Role::getRoleId); //按roleId进行降序排序
        Page<Role> page = new Page<>(pageNo, pageSize);  //baomidou的Page类
        //进行条件分页查询,结果在page对象中
        roleService.page(page, wrapper);
        Map<String, Object> data = new HashMap<>();
        data.put("rows", page.getRecords());
        data.put("total", page.getTotal());
        return Result.success(data);
    }

    /**
     * 新增角色
     */
    @PostMapping("/add")
    public Result<?> addRole(@RequestBody Role role) {
        roleService.addRole(role);
        return Result.success("角色添加成功");
    }

    /**
     * 修改角色
     */
    @PutMapping("/update")
    public Result<?> updateRole(@RequestBody Role role) {
        roleService.updateRole(role);
        return Result.success("修改角色成功");
    }

    /**
     * 根据id查询角色
     */
    @GetMapping("/{id}")
    public Result<Role> getRoleById(@PathVariable("id") Integer id) {
        Role role = roleService.getRoleById(id);
        return Result.success(role);
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/delete/{id}")
    public Result<?> deleteRoleById(@PathVariable("id") Integer id) {
        roleService.deleteRoleById(id); //逻辑删除
        return Result.success("删除角色成功");
    }
}
