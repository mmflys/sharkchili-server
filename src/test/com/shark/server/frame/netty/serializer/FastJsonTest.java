package com.shark.server.frame.netty.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.shark.server.pro.bean.Response;
import com.shark.server.pro.bean.Student;
import com.shark.server.socket.message.Message;
import org.testng.annotations.Test;
import org.testng.collections.Maps;

/**
 * @Author: SuLiang
 * @Date: 2018/9/14 0014
 * @Description:
 */
public class FastJsonTest {

	@Test
	public void t1(){
		Response response=new Response();
		Student student=new Student();
		student.setAge(15);
		student.setName("suliang");
		response.setValus(Maps.newHashMap());
		response.getValus().put("student",student);

		String json= JSON.toJSONString(response);
		Object object=JSON.parseObject(json,Response.class);
		System.out.println(object);
	}

	public void t2(){
		Message message= (Message) new Response();
		Student student=new Student();
		student.setAge(15);
		student.setName("suliang");
		message.setBody(student);

		//ParserConfig.getGlobalInstance().addAccept("pro.message.Student");
		ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
		String json= JSON.toJSONString(message, SerializerFeature.WriteClassName);
		System.out.println("json: "+json);
		Object object=JSON.parseObject(json, Response.class);
		System.out.println("object: "+object);
	}


}
