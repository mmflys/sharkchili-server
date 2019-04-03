package com.shark.server.frame;

import org.testng.annotations.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class MochitoTest {
	@Test
	public void verify_behavior(){
		//模拟创建一个List对象
		List mockList=mock(List.class);
		//使用mock对象
		mockList.add(1);
		mockList.clear();
		//验证add(1)喝clear()是否发生
		verify(mockList).add(1);
		verify(mockList).clear();
	}

	@Test
	public void when_thenReturn(){
		//mock一个Iterator类
		Iterator iterator = mock(Iterator.class);
		//预设当iterator调用next()时第一次返回hello，第n次都返回world
		when(iterator.next()).thenReturn("hello").thenReturn("world");
		//使用mock的对象
		String result = iterator.next() + " " + iterator.next() + " " + iterator.next();
		//验证结果
		assertEquals("hello world world",result);
	}

	@Test(expectedExceptions = IOException.class)
	public void when_thenThrow() throws IOException {
		OutputStream outputStream = mock(OutputStream.class);
		OutputStreamWriter writer = new OutputStreamWriter(outputStream);
		//预设当流关闭时抛出异常
		doThrow(new IOException()).when(outputStream).close();
		outputStream.close();
	}
}
