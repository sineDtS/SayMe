package com.company.web.controller;

import com.company.AbstractApplicationTest;
import com.company.config.DataConfig;
import com.company.config.SecurityConfig;
import com.company.config.WebAppConfig;
import com.company.model.PersonView;
import com.company.persist.domain.User;
import com.company.service.UserService;
import com.company.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DataConfig.class, SecurityConfig.class, WebAppConfig.class})
@WebAppConfiguration
@EnableSpringDataWebSupport //For pagination
public class PersonControllerTest extends AbstractApplicationTest {


    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    private UserService userService = org.mockito.Mockito.mock(UserServiceImpl.class);

    private final User person = getDefaultPerson();
    private final Pageable pageRequest = PageRequest.of(0, 1);

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .defaultRequest(get("/").with(user(person)))
                .build();
    }

    private void getPageablePersonList(Page<PersonView> peoplePage, String urlTemplate) throws Exception {
        final List<PersonView> people = Arrays.asList(new PersonView(person), new PersonView(person));
        final Pageable pageRequest = PageRequest.of(0, 1);
        final Page<PersonView> value = new PageImpl<>(people, pageRequest, people.size());

        given(peoplePage).willReturn(value);

        mvc.perform(
                get(urlTemplate)
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].fullName").value("Den Streltsov"));
    }

    @Test
    public void getPeopleShouldReturnPageableListOfPersons() throws Exception {
        getPageablePersonList(
                userService.getModelPeople("Den", pageRequest),
                "/api/people.json?size=1&searchTerm=Den");
    }

    @Test
    public void getByExistingIdShouldReturnPerson() throws Exception {
        given(userService.findById(person.getId())).willReturn(person);

        mvc.perform(
                get("/api/person/{personId}.json", person.getId())
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(person.getId()))
                .andExpect(jsonPath("$.fullName").value(person.getFullName()));
    }

    @Test
    public void getByMissingIdShouldReturnNotFoundStatus() throws Exception {
        given(userService.findById(Long.MAX_VALUE)).willReturn(null);

        mvc.perform(
                get("/api/person/{personId}.json", Long.MAX_VALUE)
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addOrRemoveMissingFriendShouldReturnNotFoundStatus() throws Exception {
        given(userService.findById(Long.MAX_VALUE)).willReturn(null);

        mvc.perform(
                put("/api/friends/add/{personId}.json", Long.MAX_VALUE)
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

        mvc.perform(
                put("/api/friends/remove/{personId}.json", Long.MAX_VALUE)
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

}

