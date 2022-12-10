package io.jokerr.spring.security.providers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.TestingAuthenticationToken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@RunWith(MockitoJUnitRunner.class)
public class CustomReactiveAuthenticationManagerTest {

    private CustomReactiveAuthenticationManager manager;

    @Mock
    private CustomAuthenticationProvider provider;

    @Before
    public void setUp() throws Exception {
        manager = new CustomReactiveAuthenticationManager(provider);
    }

    @Test
    public void nullArgument() {
        provider = null;
        assertThatCode(() -> new CustomReactiveAuthenticationManager(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void badCreds() {
        assertThat(this.manager.authenticate(new TestingAuthenticationToken("princcipal", "creds")).block()).isNull();
    }
}