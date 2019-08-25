package com.sjb.service.xxgl;

import com.fxc.admin.jdbc.service.PublicService;
import com.fxc.entity.Result;
import com.sjb.dao.xxgl.StudentDaoBean;
import com.sjb.model.xxgl.Student;

public interface StudentService extends PublicService<Student, Long, StudentDaoBean>{
	
	public  Result<Student> updatess(Long id,Integer dormitoryId)throws Exception;
}
