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
package org.lealone.plugins.vertx.net;

import org.lealone.net.NetBufferFactory;
import org.lealone.net.WritableChannel;

import io.vertx.core.net.NetSocket;

public class VertxWritableChannel implements WritableChannel {

    private final NetSocket socket;

    public VertxWritableChannel(NetSocket socket) {
        this.socket = socket;
    }

    @Override
    public void write(Object data) {
        if (data instanceof VertxBuffer) {
            socket.write(((VertxBuffer) data).getBuffer());
        }
    }

    @Override
    public void close() {
        socket.close();
    }

    @Override
    public String getHost() {
        return socket.remoteAddress().host();
    }

    @Override
    public int getPort() {
        return socket.remoteAddress().port();
    }

    @Override
    public NetBufferFactory getBufferFactory() {
        return VertxBufferFactory.getInstance();
    }
}
