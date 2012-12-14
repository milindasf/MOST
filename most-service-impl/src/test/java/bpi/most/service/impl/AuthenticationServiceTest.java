/*
 * Copyright 2012, Jakob Korherr
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package bpi.most.service.impl;

import bpi.most.service.api.AuthenticationService;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Tests for {@link AuthenticationServiceImpl}.
 *
 * @author Jakob Korherr
 */
@ContextConfiguration(locations = "/META-INF/most-service.spring.xml")
public class AuthenticationServiceTest extends AbstractTransactionalJUnit4SpringContextTests
{

    @Inject
    private AuthenticationService authenticationService;

    @Test
    @Transactional
    public void test_isValidPassword_invalidUser_shouldReturnFalse() {
        Assert.assertFalse(authenticationService.isValidPassword("invaliduser", "invalidpassword"));
    }

    @Test
    @Transactional
    public void test_isValidPassword_invalidPassword_shouldReturnFalse() {
        Assert.assertFalse(authenticationService.isValidPassword("mostsoc", "invalidpassword"));
    }

    @Test
    @Transactional
    public void test_isValidPassword_correctData_shouldReturnTrue() {
        Assert.assertTrue(authenticationService.isValidPassword("mostsoc", "demo12"));
    }

}
