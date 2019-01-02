package com.company;

import com.company.persist.domain.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

import java.nio.charset.Charset;
import java.util.GregorianCalendar;

public abstract class AbstractApplicationTest {

    protected static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    protected static User getDefaultPerson() {
        return User.builder()
                .id(1L)
                .firstName("Den")
                .lastName("Streltsov")
                .email("den.strelts@gmail.com")
                .password("7913782o")
                .birthDate(new GregorianCalendar(1996, 5, 7).getTime())
                .phone("+375295839006")
                .build();
    }

    protected static Pageable getDefaultPageRequest() {
        return PageRequest.of(0, 20);
    }
}
