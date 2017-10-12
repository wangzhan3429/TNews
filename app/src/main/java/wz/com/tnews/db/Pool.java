/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package wz.com.tnews.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * a simple pool
 *
 * @author John Kenrinus Lee
 * @version 2014-06-01
 */
public abstract class Pool<T> {
    private List<T> lists;
    private Set<T> sets;
    private int maxSize;

    /**
     * @param maxSize indicate how many T object live
     */
    public Pool(int maxSize) {
        this.maxSize = maxSize;
        lists = new ArrayList<T>(maxSize);
        sets = new HashSet<T>(maxSize);
    }

    /**
     * get a object from pool
     */
    public final synchronized T use() {
        for (int i = 0; i != lists.size(); ++i) {
            T t = lists.get(i);
            if (!sets.contains(t) && t != null) {
                sets.add(t);
                return t;
            }
        }
        if (lists.size() <= maxSize) {
            T t = create();
            lists.add(t);
            sets.add(t);
            return t;
        } else {
            if (sets.size() >= maxSize) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (int i = 0; i != lists.size(); ++i) {
                T t = lists.get(i);
                if (!sets.contains(t) && t != null) {
                    sets.add(t);
                    return t;
                }
            }
        }
        return null;
    }

    /**
     * give back object to pool when after using
     */
    public final synchronized void unuse(T t) {
        sets.remove(t);
        notifyAll();
    }

    /**
     * you call this method just when you discard the pool
     */
    public final synchronized void destory() {
        sets.clear();
        for (T t : lists) {
            release(t);
        }
        lists.clear();
        sets = null;
        lists = null;
    }

    /**
     * indicate how the T object create
     */
    protected abstract T create();

    /**
     * indicate how the T object destruct
     */
    protected abstract void release(T t);
}