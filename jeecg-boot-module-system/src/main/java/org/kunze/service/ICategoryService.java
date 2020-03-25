package org.kunze.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.kunze.entity.Category;

import java.util.List;

public interface ICategoryService{

    List<Category> qryList(String pid);
}
