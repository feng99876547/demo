CREATE TABLE xxgl_announcement (

	id bigint NOT NULL primary key auto_increment,
	
	title varchar(255) COMMENT'标题',
	
	content text COMMENT'内容',
	
	createUser bigint COMMENT '创建人',
	
	createTime datetime COMMENT '创建时间',
    
	effective int(11) COMMENT '有效时间',
	
	maturity datetime COMMENT '到期时间',
	
	top datetime COMMENT '置顶'
    
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4;

CREATE TABLE xxgl_report (

	id bigint NOT NULL primary key auto_increment,
	
	student bigint NOT NULL COMMENT '学生',
	
	createTime datetime COMMENT '创建时间',
    
    createUser bigint COMMENT '经办人',
    
    status tinyint(2) COMMENT  '办理流程'
    
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4;

CREATE TABLE xxgl_schoolcard (

	id bigint NOT NULL primary key auto_increment,
	
	student bigint NOT NULL COMMENT '学生',
	
	card VARCHAR(32)  COMMENT '校园卡号',
	
	je decimal(11,2) not null COMMENT '金额',
	
	pzh varchar(64) COMMENT'开通凭证',
	
	createTime datetime COMMENT '创建时间',
    
    createUser bigint COMMENT '经办人'
    
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4;
ALTER TABLE `xxgl_schoolcard`
ADD INDEX `index_student` (`student`) USING HASH ;
ALTER TABLE `xxgl_schoolcard`
ADD INDEX `index_createTime` (`createTime`) USING BTREE ;


CREATE TABLE xxgl_filemanagement (

	id bigint NOT NULL primary key auto_increment,
	
	student bigint NOT NULL COMMENT '学生',
	
	pzh varchar(64) COMMENT'开通凭证',
	
	createTime datetime COMMENT '创建时间',
    
    createUser bigint COMMENT '经办人'
    
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4;
ALTER TABLE `xxgl_filemanagement`
ADD INDEX `index_student` (`student`) USING HASH ;
ALTER TABLE `xxgl_filemanagement`
ADD INDEX `index_createTime` (`createTime`) USING BTREE ;

CREATE TABLE xxgl_healthinformation (

	id bigint NOT NULL primary key auto_increment,
	
	student bigint NOT NULL COMMENT '学生',
	
	tjbg varchar(64) COMMENT'体检报告',
	
	createTime datetime COMMENT '创建时间',
    
    createUser bigint COMMENT '经办人'
    
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4;
ALTER TABLE `xxgl_healthinformation`
ADD INDEX `index_student` (`student`) USING HASH ;
ALTER TABLE `xxgl_healthinformation`
ADD INDEX `index_createTime` (`createTime`) USING BTREE ;

ALTER TABLE `xxgl_report`
ADD INDEX `index_student` (`student`) USING HASH ;

CREATE TABLE xxgl_student (

	id bigint NOT NULL primary key auto_increment,
	
	number varchar(40) COMMENT '学号',
	
	name varchar(30) NOT NULL COMMENT '名称',
	
	sfz varchar(18) NOT NULL COMMENT '身份证',
	
	imgUrl varchar(60) COMMENT '头像',
	
	xb tinyint(2) COMMENT '性别',
	
	mz varchar(10)  COMMENT '名族',
	
	zzmm varchar(10)  COMMENT '政治面貌',
	
    faculty varchar(30)  COMMENT '学院',
    
    zy varchar(30)  COMMENT '专业',
    
    className varchar(20)  COMMENT '班级',
    
    cellPhone varchar(15)  COMMENT '电话',
    
    dz varchar(80)  COMMENT '地址',
    
    createTime datetime COMMENT '创建时间',
    
    createUser bigint COMMENT '创建人',
    
    status tinyint(2) COMMENT '状态',
    
    deletedState  tinyint(2) COMMENT '删除状态'
    
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4;
ALTER TABLE `xxgl_student`
ADD UNIQUE INDEX `index_number` (`number`) USING HASH ;
ALTER TABLE `xxgl_student`
ADD INDEX `index_createTime` (`createTime`) USING BTREE ;
ALTER TABLE `xxgl_student`
ADD INDEX `index_name` (`name`) USING HASH ;
ALTER TABLE `xxgl_student`
ADD UNIQUE INDEX `index_sfz` (`sfz`) USING HASH ;

ALTER TABLE `xxgl_student`
ADD COLUMN `dormitoryId`  int(11) NULL COMMENT '宿舍' AFTER `dz`;

CREATE TABLE system_dictionary (

	id int NOT NULL primary key auto_increment,
	
	name varchar(30) NOT NULL COMMENT '名称',
	
	code varchar(30) NOT NULL unique COMMENT '编码',
	
	sequence int  NOT NULL COMMENT '排序',
	
	remarks varchar(100) COMMENT '备注',
	
	state tinyint(11) NOT NULL COMMENT '状态',
	
	showPeanl tinyint(2)  COMMENT '是否要弹出新面板状态',
	
	level tinyint(11) COMMENT '层级',
	
	parentId int COMMENT '父节点',
	
	createUser bigint NOT NULL COMMENT '创建人',
	
	createTime datetime COMMENT '创建时间',
	
	updateUser bigint COMMENT '修改人',
	
    updateDate datetime COMMENT '修改时间'
  
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4;


CREATE TABLE tsgl_themeedit (

	id bigint NOT NULL primary key auto_increment,
	
	title varchar(30) COMMENT '标题',
	
	remarks varchar(30) COMMENT '备注',
	
	releaseStatus tinyint(11) COMMENT '发布状态',
	
    createTime datetime COMMENT '创建时间',
    
    content text COMMENT '编辑内容'
  
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4;

CREATE TABLE tsgl_onlinearticles (

	id bigint NOT NULL primary key auto_increment,
	
	themeEditId bigint not null unique,
	
	title varchar(30) COMMENT '标题',
	
	remarks varchar(30) COMMENT '备注',
	
    createTime datetime COMMENT '创建时间',
    
    updateTime datetime COMMENT '修改时间',
    
    content text COMMENT '编辑内容'
  
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4;

--宿舍管理
CREATE TABLE xxgl_dormitory (

	id int NOT NULL primary key auto_increment,
	
	lou tinyint(11) not null COMMENT '楼号',
	
	ceng tinyint(11) not null COMMENT '楼层',
	
	code varchar(30) NOT NULL unique COMMENT '宿舍编号',
	
    type tinyint(2) COMMENT '男 女',
    
    zdrs tinyint(3) COMMENT '最大人数',
    
    yzrs tinyint(3) COMMENT '已住人数',
    
    syrs tinyint(3) COMMENT '剩余人数'
  
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4;

--缴费管理	
CREATE TABLE xxgl_contributions (

	id bigint NOT NULL primary key auto_increment,
	
	user bigint not null COMMENT '缴费人',
	
	je decimal(11,2) not null COMMENT '金额',
	
	date datetime COMMENT '缴费时间',
	
	zfddh varchar(64) COMMENT '支付订单号'
  
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4;

--系统用户表
CREATE TABLE `system_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `loginName` varchar(30) NOT NULL,
  `password` varchar(64) NOT NULL COMMENT '密码',
  `number` varchar(20) DEFAULT NULL COMMENT '学号',
  `name` varchar(10) DEFAULT NULL COMMENT '名字',
  `imgUrl` varchar(255) DEFAULT NULL COMMENT '头像',
  `faculty` varchar(15) DEFAULT NULL COMMENT '院系',
  `cellPhone` varchar(11) DEFAULT NULL COMMENT '电话',
  `status` tinyint(11) DEFAULT NULL COMMENT '状态',
  `createTime` datetime DEFAULT NULL,
  `createUser` varchar(36) DEFAULT NULL,
  `updateUser` varchar(36) DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  `deletedState` tinyint(11) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `dormitoryId` int(11) DEFAULT NULL COMMENT '宿舍id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `loginName` (`loginName`) USING HASH,
  UNIQUE KEY `index_number` (`number`) USING BTREE,
  KEY `index_dormitoryId` (`dormitoryId`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4;


INSERT INTO system_user VALUES ('1', 'admin', 'E10ADC3949BA59ABBE56E057F20F883E', null, '超级管理员', null, '123456789011111111', '12345678911', null, null, null, '2017-10-31 15:47:43', null, null, null, null, null, null, null, null, null);


