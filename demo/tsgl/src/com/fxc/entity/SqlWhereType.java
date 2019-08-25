package com.fxc.entity;


public  enum SqlWhereType {
	/**
	 * 等于
	 */
	EQ(" = ? ", "等于", 1),
	/**
	 * 为空
	 */
	ISNULL(" IS NULL ", "为空", 2),
	/**
	 * 不为空
	 */
	ISNOTNULL(" IS NOT NULL ", "不为空", 3),
	/**
	 * 不等
	 */
	UNEQ(" != ? ", "不等", 4),
	/**
	 * 前后LIKE %?
	 */
	LIKE(" LIKE ? ", "like", 5),
	/**
	 * 前LIKE ?%
	 */
	BEFORELIKE(" LIKE ? ", "前like", 5),
	/**
	 * 后LIKE
	 */
	AFTERLIKE(" LIKE ? ", "后like", 6),
	/**
	 * 大于
	 */
	THAN(" > ? ", "大于", 7),
	/**
	 * 大于等于
	 */
	THANEQUAL(" >= ? ", "大于等于", 8),
	/**
	 * 小于
	 */
	LESSTHAN(" < ? ", "小于", 9),
	/**
	 * 小于等于
	 */
	LESSTHANEQUAL(" <= ? ", "小于等于", 10),
	/**
	 * IN语句
	 */
	IN(" in(?,...) ", "IN语句", 11),
	/**
	 * NOTIN语句
	 */
	NOTIN(" not in(?,...) ", "IN语句", 11);

	private String expression;
	private String name;
	private Integer sequence;

	SqlWhereType(String expression, String name, Integer sequence) {
		this.expression = expression;
		this.name = name;
		this.sequence = sequence;
	}
	
	public static SqlWhereType getKey(String key) {
		SqlWhereType s = Enum.valueOf(SqlWhereType.class,key);
		return s;
	}
	
//	public String getExpression(Object value) {
//		if (this.equals(SqlWhereType.IN) || this.equals(SqlWhereType.NOTIN)) {
//			if (Treat.isEmpty(value) || !(value instanceof Collection))
//				return "";
//			Collection o = (Collection) value;
//			StringBuffer in = new StringBuffer();
//			in.append(this.equals(SqlWhereType.IN) ? " in" : " not in");
//			in.append("(?");
//			for (int i = 1; i < o.size(); i++) {
//				in.append(",?");
//			}
//			in.append(")");
//			return in.toString();
//		} else {
//			return expression;
//		}
//	}

//	public int getValue(Query query, int i, Object value) {
//		if (this.equals(SqlWhereType.LIKE)) {
//			query.setParameter(i++, "%" + value + "%");
//		} else if (this.equals(SqlWhereType.BEFORELIKE)) {
//			query.setParameter(i++, "%" + value);
//		} else if (this.equals(SqlWhereType.AFTERLIKE)) {
//			query.setParameter(i++, value + "%");
//		} else if (this.equals(SqlWhereType.IN)
//				|| this.equals(SqlWhereType.NOTIN)) {
//			if (Treat.isEmpty(value) || !(value instanceof Collection))
//				return i;
//			Collection o = (Collection) value;
//			for (Object obj : o) {
//				query.setParameter(i++, obj);
//			}
//		} else {
//			if (value instanceof Date) {
//				query.setParameter(i++, (Date) value, TemporalType.TIMESTAMP);
//			} else {
//				query.setParameter(i++, value);
//			}
//		}
//		return i;
//	}

	public String getName() {
		return name;
	}

	public Integer getSequence() {
		return sequence;
	}

	public String getExpression() {
		return expression;
	}

}
