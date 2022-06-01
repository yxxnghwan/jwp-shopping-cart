package woowacourse.auth.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static woowacourse.fixture.Fixture.CUSTOMER_ID;
import static woowacourse.fixture.Fixture.TEST_EMAIL;
import static woowacourse.fixture.Fixture.TEST_PASSWORD;
import static woowacourse.fixture.Fixture.TEST_USERNAME;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import woowacourse.auth.application.AuthService;
import woowacourse.auth.dto.SignInDto;
import woowacourse.auth.dto.TokenResponseDto;
import woowacourse.shoppingcart.controller.ControllerTest;
import woowacourse.shoppingcart.dto.CustomerDto;

class AuthControllerTest extends ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("이메일과 패스워드를 받아 로그인한 후 accessToken과 유효시간을 반환한다.")
    void login() throws Exception {
        CustomerDto customerDto = new CustomerDto(CUSTOMER_ID, TEST_EMAIL, TEST_USERNAME);
        when(authService.login(any())).thenReturn(new TokenResponseDto("testAccessToken", 10800000L, customerDto));

        final SignInDto signInDto = new SignInDto(TEST_EMAIL, TEST_PASSWORD);

        final MockHttpServletResponse response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(signInDto)))
                .andDo(print())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}

