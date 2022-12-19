package com.cognologix.bankingApplication.controllers;

import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.CreatedAccountResponse;
import com.cognologix.bankingApplication.entities.Account;
import com.cognologix.bankingApplication.entities.Customer;
import com.cognologix.bankingApplication.entities.transactions.BankTransaction;
import com.cognologix.bankingApplication.enums.responseMessages.ForAccount;
import com.cognologix.bankingApplication.services.BankOperationsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers = BankServiceMockitoTest.class)
public class BankServiceMockitoTest {

    @MockBean
    private BankOperationsService bankOperationsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private BankServiceController bankServiceController;


    AccountDto accountDto = new AccountDto(1L, "Savings", 1000.0, 1);

    Customer customer = new Customer(1, "Onkar", LocalDate.of(1998, 11, 11), "903998989010", "PAN36SURYA", "sury6awanshi@gmail.com", "Male");

    Account account = new Account(accountDto.getAccountNumber(), "Activate", accountDto.getAccountType(), accountDto.getBalance(), customer);

    Account accountForReceiveMoney = new Account(2L, "Activate", "Cuarrent", 10000.00, customer);

    List<Account> accounts = new ArrayList<>();
    BankTransaction transaction = new BankTransaction();
    Account deactivatedAccount;
    List<Account> deactivatedAccounts = new ArrayList<>();


//    @BeforeEach
//    void setUp() {
//    }

    //    @Test
//    void testCreateNewAccount() throws excep{
//        try {
//            ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/account/create")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(null)))
//                    .andExpect(status().isFound())
//                    .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                    .andExpect()
//
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//    }
    @Test
    public void testCreateNewAccount() throws Exception {
        String URI = "/account/create";
        CreatedAccountResponse expected = new CreatedAccountResponse(true, ForAccount.CreateAccount.name(), account);
        String inputInJson = this.mapToJson(expected);


        when(bankOperationsService.createAccount(accountDto)).thenReturn(expected);

//        CreatedAccountResponse actual=bankOperationsService.createAccount(accountDto);
//
//        assertEquals(expected,actual);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).accept(inputInJson);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect()
                .andReturn();
        MockHttpServletResponse response=mvcResult.getResponse();

        String outputJson=response.getContentAsString();
//        assertThat(outputJson).isEqualTo(inputInJson);
//        assertEquals(HttpStatus.CREATED.value(),response.getStatus());
    }

    //Maps an object into JSON string. Uses a Jackson ObjectMapper
    private String mapToJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
