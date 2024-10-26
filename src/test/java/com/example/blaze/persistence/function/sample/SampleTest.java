/*
 * Copyright 2014 - 2024 Blazebit.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.blaze.persistence.function.sample;

import com.blazebit.persistence.integration.view.spring.EnableEntityViews;
import com.blazebit.persistence.spring.data.repository.config.EnableBlazeRepositories;
import com.example.blaze.persistence.function.model.Cat;
import com.example.blaze.persistence.function.repository.CatRepository;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SampleTest.TestConfig.class)
public class SampleTest extends AbstractSampleTest {

    @Autowired
    private CatRepository catRepository;

    @Test
    public void function() {
        transactional(em -> {
            Session session = (Session) em.getDelegate();
            Integer number = (Integer) session.createSQLQuery("SELECT * FROM get_cat_id(?)")
                    .setParameter(1, 10)
                    .getSingleResult();
            Assert.assertNotNull(number);
            Assert.assertEquals((int) number, 10);
        });
    }

    @Test
    public void trivialSpecification() {
        transactional(em -> {
            Specification<Cat> spec = (root, query, cb) -> cb.ge(root.get("age"), 6);
            List<Cat> cats = catRepository.findAll(spec);
            Assert.assertEquals(3, cats.size());
        });
    }

    @Test
    public void functionSpecification() {
        transactional(em -> {
            Specification<Cat> spec = (root, query, cb) -> root.get("id").in(cb.function(
                    "SELECT * FROM get_cat_id",
                    List.class,
                    cb.literal(6)
            ));
            List<Cat> cats = catRepository.findAll(spec);
            Assert.assertEquals(1, cats.size());
        });
    }

    @Configuration
    @ComponentScan("com.example.blaze.persistence.function")
    @ImportResource("/META-INF/application-config.xml")
    @EnableEntityViews(basePackages = {"com.example.blaze.persistence.function.view"})
    @EnableBlazeRepositories(
            basePackages = "com.example.blaze.persistence.function.repository",
            entityManagerFactoryRef = "myEmf")
    static class TestConfig {
    }
}
