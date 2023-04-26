package org.javaboy.vhr.web.service;

import org.javaboy.vhr.web.mapper.DepartmentMapper;
import org.javaboy.vhr.web.model.Department;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 部门管理
 */
@Service
public class DepartmentService {
    @Resource
    DepartmentMapper departmentMapper;
    public List<Department> getAllDepartments() {
        return departmentMapper.getAllDepartmentsByParentId(-1);
    }

    public void addDep(Department dep) {
        dep.setEnabled(true);
        departmentMapper.addDep(dep);
    }

    /**
     * 根据id删除部门，里面流程较长，使用数据库的存储过程来删除，在存储过程中，有in参数输入和out参数输出，我们可以使用实体类或者map来存储输入和输出
     * @param dep
     */
    public void deleteDepById(Department dep) {
        departmentMapper.deleteDepById(dep);
    }

    public List<Department> getAllDepartmentsWithOutChildren() {
        return departmentMapper.getAllDepartmentsWithOutChildren();
    }
}
