package com.company.service.impl;

import com.company.Commons.ServerResponse;
import com.company.dao.CategoryMapper;
import com.company.dao.pojo.Category;
import com.company.service.iservice.ICategoryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

@Service("categoryService")
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    @Qualifier("categoryMapper")
    CategoryMapper categoryMapper;
    private final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(int categoryId) {
        List<Category> categoryList =categoryMapper.getChildrenParallelCategoryById(categoryId);
        if(CollectionUtils.isEmpty(categoryList)){
            logger.info("没有找到子分类");
        }
        return ServerResponse.createSuccessResponse(categoryList);
    }

    @Override
    public ServerResponse<String> addCategory(String categoryName, int parentId) {
        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        int resultCount = categoryMapper.insertSelective(category);
        if(resultCount>0){
            return ServerResponse.createSuccessMsgResponse("添加品类成功");
        }
        return ServerResponse.createErrorMsgResponse("添加品类失败");
    }

    @Override
    public ServerResponse<String> setCategoryName(int categoryId, String categoryName) {
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int resultCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(resultCount>0){
            return ServerResponse.createSuccessMsgResponse("修改品类名称成功");
        }
        return ServerResponse.createErrorMsgResponse("修改品类名称失败");
    }

    @Override
    public ServerResponse<List<Integer>> getDeepCategory(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        //业务需求根据id判断是否重复因此重构category equals方法
        findChildCategory(categorySet,categoryId);
        List<Integer> categoryIdList = Lists.newArrayList();
        if(categoryId!=null) {
            for (Category category : categorySet) {
                categoryIdList.add(category.getId());
            }
        }
        return ServerResponse.createSuccessResponse(categoryIdList);
    }

    private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
       Category category = categoryMapper.selectByPrimaryKey(categoryId);
       if(category!=null){
           categorySet.add(category);
       }
       List<Category> categoryList = categoryMapper.getChildrenParallelCategoryById(categoryId);
       for(Category categoryItem:categoryList){
           findChildCategory(categorySet,categoryItem.getId());
       }
        return categorySet;
    }
}
