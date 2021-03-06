/*
 * Copyright (C) 2013 Clarion Media, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clarionmedia.infinitum.aop.impl;

import com.clarionmedia.infinitum.aop.Pointcut;
import com.clarionmedia.infinitum.di.AbstractProxy;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class DelegatingAdvisedProxyFactoryTest {

    private DelegatingAdvisedProxyFactory proxyFactory;

    @Before
    public void setup() {
        proxyFactory = new DelegatingAdvisedProxyFactory();
    }

    @Test
    public void testCreateProxy_dexMakerProxy() {
        AbstractProxy proxy = proxyFactory.createProxy(Robolectric.application, new Object(),
                new Pointcut("someBean", Object.class));
        assertEquals(proxy.getClass(), AdvisedDexMakerProxy.class);
    }

    @Test
    public void testCreateProxy_jdkDynamicProxy() {
        AbstractProxy proxy = proxyFactory.createProxy(Robolectric.application, new ArrayList<String>(),
                new Pointcut("someBean", ArrayList.class));
        assertEquals(proxy.getClass(), AdvisedJdkDynamicProxy.class);
    }

    @Test
    public void test_CreateProxy_bytecodeInstrumented() {
        AbstractProxy proxy = proxyFactory.createProxy(Robolectric.application, new Object(),
                new Pointcut("someBean", ArrayList.class), true);
        assertEquals(proxy.getClass(), AdvisedDexMakerProxy.class);
    }

    @Test
    public void test_CreateProxy_notBytecodeInstrumented() {
        AbstractProxy proxy = proxyFactory.createProxy(Robolectric.application, new ArrayList<String>(),
                new Pointcut("someBean", ArrayList.class), false);
        assertEquals(proxy.getClass(), AdvisedJdkDynamicProxy.class);
    }

    @Test
    public void test_CreateProxy_notBytecodeInstrumented_fallback() {
        AbstractProxy proxy = proxyFactory.createProxy(Robolectric.application, new Object(),
                new Pointcut("someBean", ArrayList.class), false);
        assertEquals(proxy.getClass(), AdvisedDexMakerProxy.class);
    }

}
