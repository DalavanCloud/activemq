/**
 *
 * Copyright 2005-2006 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq.transport.udp;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A default implementation of {@link BufferPool} which keeps a pool of direct
 * byte buffers.
 * 
 * @version $Revision$
 */
public class DefaultBufferPool implements ByteBufferPool {

    private int defaultSize;
    private List buffers = new ArrayList();
    private Object lock = new Object();

    public synchronized ByteBuffer borrowBuffer() {
        synchronized (lock) {
            int size = buffers.size();
            if (size > 0) {
                return (ByteBuffer) buffers.remove(size - 1);
            }
        }
        return ByteBuffer.allocateDirect(defaultSize);
    }

    public synchronized void returnBuffer(ByteBuffer buffer) {
        synchronized (lock) {
            buffers.add(buffer);
        }
    }

    public void setDefaultSize(int defaultSize) {
        this.defaultSize = defaultSize;
    }

    public synchronized void start() throws Exception {
    }

    public synchronized void stop() throws Exception {
        synchronized (lock) {
            /*
            for (Iterator iter = buffers.iterator(); iter.hasNext();) {
                ByteBuffer buffer = (ByteBuffer) iter.next();
            }
            */
            buffers.clear();
        }
    }

}
