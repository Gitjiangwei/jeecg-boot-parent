package org.kunze.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.kunze.entity.Category;
import org.kunze.mapper.CategoryMapper;
import org.kunze.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements ICategoryService  {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> qryList(String pid) {
        return null;
    }
}
