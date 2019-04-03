package com.shark.server.bean.producer;

import com.shark.server.bean.Bean;

public interface BeanProducer {
	/**
	 * produce a message that its name is`t initialized and need to be proxied.
	 * @param C
	 * @return
	 */
	Bean produce(Class<?> C);
	/**
	 * produce a message that its name is`t initialized.
	 * @param C
	 * @param proxy
	 * @return
	 */
	Bean produce(Class<?> C, boolean proxy);

	/**
	 * produce a message that default is proxied.
	 * @param C
	 * @param name
	 * @return
	 */
	public Bean produce(Class<?> C, String name);

	/**
	 * produce a message
	 * @param C
	 * @param name
	 * @param proxy whether or not is created by proxy
	 * @return
	 */
	public Bean produce(Class<?> C, String name, boolean proxy);
}
