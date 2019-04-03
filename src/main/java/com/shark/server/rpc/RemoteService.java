package com.shark.server.rpc;

import com.shark.server.annotation.Remote;

/**
 * @Author: SuLiang
 * @Date: 2018/9/20 0020
 * @Description: If a class implement {@link RemoteService},indicate it is a remote service.<p>
 * You must to add {@link Remote} to your remote service.
 */
@Remote(address = "localhost", port = 8080, name = "account",publicAddress = "localhost")
public interface RemoteService {

}
