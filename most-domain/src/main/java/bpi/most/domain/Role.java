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
package bpi.most.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Entity specifying a Role.
 *
 * @author Jakob Korherr
 */
@Entity
@Table(name = "role",
        uniqueConstraints = @UniqueConstraint(name = "name_UNIQUE", columnNames = {"name"}))
public class Role
{

    @Id
    @GeneratedValue
    private Integer id;

    @Basic(optional = false)
    @Column(name = "name", unique = true, nullable = false, length = 100)
    private String name;

    @Basic(optional = false)
    @Column(name = "description", nullable = false, length = 100)
    private String description;

}
