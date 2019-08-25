package com.sjb.dao.xxgl.impl;

import org.springframework.stereotype.Repository;

import com.fxc.admin.jdbc.dao.PublicDaoImpl;
import com.sjb.dao.xxgl.StudentDaoBean;
import com.sjb.model.xxgl.Student;

@Repository
public class StudentDaoBeanImpl extends PublicDaoImpl<Student, Long> implements StudentDaoBean{

	public StudentDaoBeanImpl() {
	}

}
