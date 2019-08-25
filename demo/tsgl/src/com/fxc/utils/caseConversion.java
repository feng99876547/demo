package com.fxc.utils;


public class caseConversion {
	
	public static String division =".";
	/**
	 * 下划线转驼峰法
	 * 
	 * @param line 源字符串
	 * @param smallCamel 大小驼峰,是否为小驼峰 true小驼峰小写开头   false大驼峰
	 * @return 转换后的字符串
	 */
	//1小驼峰式命名法(lower camel case):第一个单字以小写字母开始，第二个单字的首字母大写。例如:firstName、lastName。
	//2、大驼峰式命名法(upper camel case):每一个单字的首字母都采用大写字母，例如:FirstName、LastName、CamelCase，也被称为 Pascal 命名法。
//	public static String underline2Camel(String line, boolean smallCamel) {
//		if (line == null || "".equals(line)) {
//			return "";
//		}
//		StringBuffer sb = new StringBuffer();
//		Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
//		Matcher matcher = pattern.matcher(line);
//		while (matcher.find()) {
//			String word = matcher.group();
//			//false 首字母大写 true首字母小写
//			sb.append((smallCamel && matcher.start() == 0 )? Character.toLowerCase(word.charAt(0))
//					: Character.toUpperCase(word.charAt(0)));
//			int index = word.lastIndexOf('_');
//			if (index > 0) {
//				sb.append(word.substring(1, index).toLowerCase());
//			} else {
//				sb.append(word.substring(1).toLowerCase());
//			}
//		}
//		return sb.toString();
//	}
	public static String underline2Camel(String line, boolean smallCamel) {
		return line;
	}
	
//	public static String underline2Camel(String line, boolean smallCamel) {
//		return toUpperCaseFirstOne(line);
//	}

//	/**
//	 * 驼峰法转下划线
//	 * sb的hibenate转毛驼峰
//	 * @param line 源字符串
//	 * @return 转换后的字符串
//	 */
//	public static String camel2Underline(String line) {
//		if (line == null || "".equals(line)) {
//			return "";
//		}
//		line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
//		StringBuffer sb = new StringBuffer();
//		Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
//		Matcher matcher = pattern.matcher(line);
//		while (matcher.find()) {
//			String word = matcher.group();
//			sb.append(word.toUpperCase());//转大写
//			//转小写
////			sb.append(word.toLowerCase());
//			sb.append(matcher.end() == line.length() ? "" : "_");
//		}
//		return sb.toString();
//	}
	
	public static String camel2Underline(String line) {
		return line;
	}
	
	//首字母转小写
    public static String toLowerCaseFirstOne(String s)
    {
        if(Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }
    //首字母转大写
    public static String toUpperCaseFirstOne(String s)
    {
        if(Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }
	
}
