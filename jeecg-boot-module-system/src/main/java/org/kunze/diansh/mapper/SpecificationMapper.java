package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.Specification;

import java.util.List;

public interface SpecificationMapper extends BaseMapper<Specification> {


    Specification qrySpecification(@Param("categoryId") String categoryId);


    int saveSpecification(Specification specification);


    int updateSpecification(Specification specification);

    int delSpecifications(@Param("updateName")String updateName,@Param("list")List<String> specIds);

}
