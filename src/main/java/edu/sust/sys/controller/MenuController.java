package edu.sust.sys.controller;

import edu.sust.common.vo.Result;
import edu.sust.sys.entity.Menu;
import edu.sust.sys.service.IMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ylc
 * @since 2023-03-14
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @ApiOperation("查询所有菜单数据")
    @GetMapping
    public Result<List<Menu>> getAllMenu() {
        List<Menu> menuList = menuService.getAllMenu();
        return Result.success(menuList);
    }
}
