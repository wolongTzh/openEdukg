package com.edukg.open.util;

import java.util.UUID;

public class SmsValidateCodeGenerator {
	public static String generate(){
		return realGen();
	}
	
	public static String realGen(){
		UUID uuid = UUID.randomUUID();
		long id = uuid.hashCode();
		
		String time = System.currentTimeMillis()+""+Math.random();;
		
		id = id * time.hashCode();
		String u = id+"";
		u = u.substring(2, 8).toUpperCase();
		return u;
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 100000; i++) {
			System.out.println(generate());
		}
	}
	
}
