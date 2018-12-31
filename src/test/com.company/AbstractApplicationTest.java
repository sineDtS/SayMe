package com.company;

import com.company.persist.domain.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.GregorianCalendar;

public abstract class AbstractApplicationTest {
    private final static String DEFAULT_MESSAGE_TEXT = "Lorem ipsum dolor sit amet...";

    protected static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private static User getDefaultPerson() {
        return User.builder()
                .id(1L)
                .firstName("Denis")
                .lastName("Streltsov")
                .email("den.strelts@gmail.com")
                .password("7913782o")
                .birthDate(new GregorianCalendar(1996, 6, 7).getTime())
                .phone("+375295839006")
                .build();
    }

    protected static Pageable getDefaultPageRequest() {
        return new PageRequest(0, 20);
//	    return new PageRequest(1,
//			    10,
//			    new Sort(Sort.Direction.DESC, "description")
//					    .and(new Sort(Sort.Direction.ASC, "title")));
    }

    protected static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

}
