/**
 * Copyright 2010 Nicholas Blair, Eric Dalquist
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.googlecode.ehcache.annotations.key;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Eric Dalquist
 * @version $Revision$
 */
public class HashCodeCacheKeyGeneratorTest {

    @Test
    public void testCircularReference() {
        final HashCodeCacheKeyGenerator generator = new HashCodeCacheKeyGenerator(false, false);
        generator.setCheckforCycles(true);
        
        final Object[] arg = new Object[2];
        final Object[] childArg = new Object[2];
        arg[0] = childArg;
        arg[1] = "argString";
        childArg[0] = arg;
        childArg[1] = "childArgString";
        
        final MethodInvocation invocation = createMock(MethodInvocation.class);
        expect(invocation.getArguments()).andReturn(new Object[] { arg });
        
        replay(invocation);
        
        final Long key = generator.generateKey(invocation);
        Assert.assertEquals(Long.valueOf(55143651163l), key);
        
        verify(invocation);
    }

    @Test
    public void testForDocs() throws SecurityException, NoSuchMethodException {
        final HashCodeCacheKeyGenerator generator = new HashCodeCacheKeyGenerator();
        generator.setCheckforCycles(true);
        
        final Method testMethod = MethodInvocationHelper.class.getMethod("testMethod1", Object.class);
        
        final MethodInvocation invocation = createMock(MethodInvocation.class);
        expect(invocation.getMethod()).andReturn(testMethod);
        expect(invocation.getArguments()).andReturn(new Object[] { "49931" });
        
        replay(invocation);
        
        final Long key = generator.generateKey(invocation);
        Assert.assertEquals(Long.valueOf(-78777307802699l), key);
        
        verify(invocation);
    }
    
    
    @Test
    public void testNegativeOne() {
        final HashCodeCacheKeyGenerator generator = new HashCodeCacheKeyGenerator(false, false);
        
        final MethodInvocation negOneCall = createMock(MethodInvocation.class);
        expect(negOneCall.getArguments()).andReturn(new Object[] { -1 });
        
        replay(negOneCall);
        
        final Long key = generator.generateKey(negOneCall);
        Assert.assertEquals(Long.valueOf(30), key);
        
        verify(negOneCall);
    }
    
    @Test
    public void testMinimumLong() {
        final HashCodeCacheKeyGenerator generator = new HashCodeCacheKeyGenerator(false, false);
        
        final MethodInvocation negOneCall = createMock(MethodInvocation.class);
        expect(negOneCall.getArguments()).andReturn(new Object[] { Long.MIN_VALUE });
        
        replay(negOneCall);
        
        final Long key = generator.generateKey(negOneCall);
        Assert.assertEquals(Long.valueOf(-9223372036854775777l), key);
        
        verify(negOneCall);
    }
    
    @Test
    public void testMaximumLong() {
        final HashCodeCacheKeyGenerator generator = new HashCodeCacheKeyGenerator(false, false);
        
        final MethodInvocation negOneCall = createMock(MethodInvocation.class);
        expect(negOneCall.getArguments()).andReturn(new Object[] { Long.MAX_VALUE });
        
        replay(negOneCall);
        
        final Long key = generator.generateKey(negOneCall);
        Assert.assertEquals(Long.valueOf(-9223372036854775778l), key);
        
        verify(negOneCall);
    }
    
    @Test
    public void testEnumHashCode() {
        final HashCodeCacheKeyGenerator generator = new HashCodeCacheKeyGenerator(false, false);
        
        final MethodInvocation invocation = createMock(MethodInvocation.class);
        expect(invocation.getArguments()).andReturn(new Object[] { TimeUnit.DAYS });
        
        replay(invocation);
        
        final Long key = generator.generateKey(invocation);
        Assert.assertEquals(Long.valueOf(-53035962820l), key);
        
        verify(invocation);
    }
    
    @Test
    public void testComplexHashCode() throws SecurityException, NoSuchMethodException {
        final HashCodeCacheKeyGenerator generator = new HashCodeCacheKeyGenerator(true, true);
        
        final Method testMethod = MethodInvocationHelper.class.getMethod("testMethod2", int[].class, String.class, boolean[].class, Object.class);
        
        final MethodInvocation invocation = createMock(MethodInvocation.class);
        expect(invocation.getMethod()).andReturn(testMethod);
        expect(invocation.getArguments()).andReturn(new Object[] { 
                new int[] {1, 2, 3, 4}, 
                "foo", 
                new boolean[] {false, true},
                null
                });
        
        replay(invocation);
        
        final Long key = generator.generateKey(invocation);
        
        Assert.assertEquals(Long.valueOf(-43138117840462l), key);
        
        verify(invocation);
    }
    
    @Test
    public void testPrimitiveHashCode() throws SecurityException, NoSuchMethodException {
        final HashCodeCacheKeyGenerator generator = new HashCodeCacheKeyGenerator(true, true);
        
        final Method testMethod = MethodInvocationHelper.class.getMethod("testMethod3", int.class, long.class, boolean.class, Integer.class);
        
        final MethodInvocation invocation = createMock(MethodInvocation.class);
        expect(invocation.getMethod()).andReturn(testMethod);
        expect(invocation.getArguments()).andReturn(new Object[] { 
                1, 
                42, 
                false,
                1337
                });
        
        replay(invocation);
        
        final Long key = generator.generateKey(invocation);
        
        Assert.assertEquals(Long.valueOf(-78616304309326l), key);
        
        verify(invocation);
    }
    
    @Test
    public void testGeneratesDifferentKeysWithDifferentNonIntegerPartsOfFloatParameter() throws Exception {
        HashCodeCacheKeyGenerator keyGenerator = new HashCodeCacheKeyGenerator(false, true);
        float firstValue = 1.5f;
        Long firstKey = keyGenerator.generateKey(firstValue);
        float secondValue = 1.7f;
        Long secondKey = keyGenerator.generateKey(secondValue);
        assertFalse(firstKey.equals(secondKey));
    }

    @Test
    public void testGeneratesDifferentKeysWithDifferentNonIntegerPartsOfDoubleParameter() throws Exception {
        HashCodeCacheKeyGenerator keyGenerator = new HashCodeCacheKeyGenerator(false, true);
        double firstValue = 1.5;
        Long firstKey = keyGenerator.generateKey(firstValue);
        double secondValue = 1.7;
        Long secondKey = keyGenerator.generateKey(secondValue);
        Assert.assertFalse(firstKey.equals(secondKey));
    }
}
