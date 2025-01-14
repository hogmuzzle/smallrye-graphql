package io.smallrye.graphql.client;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.json.JsonNumber;

import org.junit.jupiter.api.Test;

import io.smallrye.graphql.client.dynamic.ResponseImpl;

public class ResponseReaderTest {

    @Test
    public void verifyErrors() {
        String responseString = "{\"errors\":[{\"message\":\"blabla\"," +
                "\"path\": [1, 2, 3, \"asd\"]," +
                "\"locations\": [{\"line\":1,\"column\":30}]," +
                "\"somethingExtra\": 123456," +
                "\"extensions\": {\"code\":\"GRAPHQL_VALIDATION_FAILED\"}}]}";
        ResponseImpl response = ResponseReader.readFrom(responseString);

        Error theError = response.getErrors().get(0);
        assertEquals("blabla", theError.getMessage());
        assertEquals(123456, ((JsonNumber) theError.getOtherFields().get("somethingExtra")).intValue());
        assertEquals("GRAPHQL_VALIDATION_FAILED", theError.getExtensions().get("code"));
        assertEquals(1, theError.getLocations().get(0).get("line"));
        assertEquals(30, theError.getLocations().get(0).get("column"));
        assertArrayEquals(new Object[] { 1, 2, 3, "asd" }, theError.getPath());
    }
}
