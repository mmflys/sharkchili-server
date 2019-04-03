package com.shark.server.frame;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.shark.server.container.MainContainer;
import org.bson.Document;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: SuLiang
 * @Date: 2018/9/4 0004
 * @Description:
 */
public class DbTest {
	Connection connection;

	@Test
	public void connectionTest() throws SQLException {
		MainContainer mainContainer=MainContainer.get().init();
		connection= mainContainer.getBean(DataSource.class).getConnection();
		PreparedStatement statement1=connection.prepareStatement("select * from student");
		ResultSet resultSet=statement1.executeQuery();
		//Statement statement=connection.createStatement();
		//ResultSet resultSet=statement.executeQuery("selectColumns * from student");
		while (resultSet.next()){
			System.out.println(resultSet.getString(2));
			System.out.println(resultSet.getInt(3));
			System.out.println(resultSet.getString(4));
		}
	}

	@Test
	public void connectMongodb(){
		try {
			//连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址
			//ServerAddress()两个参数分别为 服务器地址 和 端口
			ServerAddress serverAddress = new ServerAddress("sharkchili",27017);
			List<ServerAddress> addrs = new ArrayList<>();
			addrs.add(serverAddress);

			//MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
			MongoCredential credential = MongoCredential.createScramSha1Credential("root", "test", "root".toCharArray());
			List<MongoCredential> credentials = new ArrayList<>();
			credentials.add(credential);

			//通过连接认证获取MongoDB连接
			MongoClient mongoClient = new MongoClient(addrs,credentials);

			//连接到数据库
			MongoDatabase mongoDatabase = mongoClient.getDatabase("test");
			//获取集合
			MongoCollection<Document> collection=mongoDatabase.getCollection("test");
			System.out.println("Connect to database successfully");
		} catch (Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
	}

	//@AfterMethod
	public void closeConnection() throws SQLException {
		connection.close();
		System.out.println("Connection is succeeded to be closed!");
	}
}
