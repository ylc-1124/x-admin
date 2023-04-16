package edu.sust.sys.mapper;

import edu.sust.sys.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ylc
 * @since 2023-03-14
 */
public interface MenuMapper extends BaseMapper<Menu> {
    List<Menu> queryMenuListByUserId(@Param("userId") Integer userId , @Param("pid") Integer pid);
}
