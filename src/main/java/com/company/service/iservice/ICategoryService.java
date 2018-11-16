package com.company.service.iservice;

import com.company.Commons.ServerResponse;
import com.company.dao.pojo.Category;

import java.util.List;

public interface ICategoryService {
    ServerResponse<List<Category>> getChildrenParallelCategory(int categoryId);

    ServerResponse<String> addCategory(String categoryName, int parentId);

    ServerResponse<String> setCategoryName(int categoryId, String categoryName);

    ServerResponse<List<Integer>> getDeepCategory(Integer categoryId);
}
