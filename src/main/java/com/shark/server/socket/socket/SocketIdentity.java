package com.shark.server.socket.socket;


import com.shark.util.util.StringUtil;

/**
 * @Author: SuLiang
 * @Date: 2018/9/20 0020
 * @Description:
 */
public class SocketIdentity {
	private String name;
	private String address;
	private String publicAddress;
	private int port;

	public SocketIdentity() {
		address="localhost";
	}

	public static SocketIdentity create(String address, int point){
		SocketIdentity socketIdentity=new SocketIdentity();
		socketIdentity.address =address;
		socketIdentity.port =point;
		socketIdentity.publicAddress=address;
		return socketIdentity;
	}

	public static SocketIdentity create(String name, String address, int point){
		SocketIdentity socketIdentity=new SocketIdentity();
		socketIdentity.name=name;
		socketIdentity.address =address;
		socketIdentity.port =point;
		socketIdentity.publicAddress=address;
		return socketIdentity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPublicAddress() {
		return publicAddress;
	}

	public void setPublicAddress(String publicAddress) {
		this.publicAddress = publicAddress;
	}

	@Override
	public String toString() {
		return "SocketIdentity{" +
				"name='" + name + '\'' +
				", address='" + address + '\'' +
				", publicAddress='" + publicAddress + '\'' +
				", port=" + port +
				'}';
	}

	@Override
	public boolean equals(Object obj) {
		SocketIdentity other= (SocketIdentity) obj;
		return StringUtil.compareIp(this.publicAddress,other.publicAddress)
				&&StringUtil.compareIp(this.address,other.address)
				&&this.name.equals(other.name)
				&&this.port==other.port;
	}
}