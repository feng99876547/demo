package com.sjb.service.xxgl.impl;

import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fxc.admin.jdbc.service.PublicServiceImpl;
import com.fxc.admin.jdbc.service.entity.ServiceSection;
import com.fxc.entity.Result;
import com.fxc.exception.MyRollBackException;
import com.fxc.utils.Sequence;
import com.sjb.dao.xxgl.StudentDaoBean;
import com.sjb.model.xxgl.Student;
import com.sjb.service.xxgl.StudentService;

@Service
public class StudentSreviceImpl extends PublicServiceImpl<Student, Long, StudentDaoBean> implements StudentService{

	public StudentSreviceImpl() {
	}

	@Resource
	private Sequence sequence;
	
	private ConcurrentHashMap<Long,Integer> lock = new ConcurrentHashMap<Long,Integer>();
	
	@Override
	public ServiceSection<Student> getServiceAop() throws Exception {
		return null;
	}
	
	@Override
	public synchronized Result<Student>  updatess(Long id,Integer dormitoryId) throws Exception{
		Result<Student> result = new Result<Student>(false);
		Student s = getDao().get(id);
		if(s.getDormitoryId()!=null){
			if(s.getDormitoryId().intValue() == dormitoryId){//相等不需要修改
				lock.remove(id);
				result.setSuccess(true);
				result.setMsg("修改成功");
				return result;
			}else{//释放之前的宿舍位置（比如修改 12 23 31 同时执行到第二步时也会形成死锁）
				sequence.exSql("update xxgl_dormitory set yzrs = yzrs-1,syrs=zdrs-yzrs where id = ?  and yzrs > 0 ",s.getDormitoryId());
			}
		}
		Student stu = new Student();
		stu.setId(id);
		stu.setDormitoryId(dormitoryId);
		getDao().update(stu);
		//占用新的宿舍位置
		int count = sequence.exSql("update xxgl_dormitory set yzrs = yzrs+1,syrs=zdrs-yzrs where id = ?  and yzrs < zdrs",dormitoryId);
		if(count == 0){
			throw new MyRollBackException("该宿舍已经满了");
		}
		result.setSuccess(true);
		result.setMsg("修改成功");
		return result;
	}

}
