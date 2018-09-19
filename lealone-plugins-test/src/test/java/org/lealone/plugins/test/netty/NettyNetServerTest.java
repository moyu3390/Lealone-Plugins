/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lealone.plugins.test.netty;

import java.net.InetAddress;

import org.lealone.common.exceptions.ConfigException;
import org.lealone.db.Constants;
import org.lealone.p2p.config.Config;
import org.lealone.p2p.config.Config.PluggableEngineDef;
import org.lealone.plugins.mina.MinaNetFactory;
import org.lealone.test.start.TcpServerStart;

public class NettyNetServerTest extends TcpServerStart {

    public static void main(String[] args) {
        TcpServerStart.run(NettyNetServerTest.class, args);
    }

    @Override
    public void applyConfig(Config config) throws ConfigException {
        enableNettyNetServer(config);
        super.applyConfig(config);
    }

    public static void enableNettyNetServer(Config config) {
        for (PluggableEngineDef e : config.protocol_server_engines) {
            e.getParameters().put(Constants.NET_FACTORY_NAME_KEY, MinaNetFactory.NAME);
            // if (TcpServerEngine.NAME.equalsIgnoreCase(e.name)) {
            // e.enabled = true;
            // e.getParameters().put(Constants.NET_FACTORY_NAME_KEY, MinaNetFactory.NAME);
            // } else if (PgServerEngine.NAME.equalsIgnoreCase(e.name)) {
            // e.enabled = false;
            // }
        }
    }

    public static void optimizeNetty() {
        // 加了这一行反而慢100ms左右
        // InternalLoggerFactory.setDefaultFactory(JdkLoggerFactory.INSTANCE);
        try {
            // 如果不设置io.netty.machineId参数，会在io.netty.channel.DefaultChannelId类的static初始代码中
            // 调用io.netty.util.internal.MacAddressUtil.defaultMachineId()会多耗时三四百毫秒
            String machineIdHexString = io.netty.util.internal.MacAddressUtil
                    .formatAddress(InetAddress.getLocalHost().getAddress()) + ":00:01";
            System.setProperty("io.netty.machineId", machineIdHexString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
