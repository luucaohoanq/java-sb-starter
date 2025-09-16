package com.orchid.orchidbe.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orchid.orchidbe.IntegrationTest;
import com.orchid.orchidbe.domain.account.Account;
import com.orchid.orchidbe.domain.account.AccountDTO;
import com.orchid.orchidbe.repositories.AccountRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
@AutoConfigureMockMvc
@Transactional //rollback after each test
public class AccountControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        //clean up db before each test
        accountRepository.deleteAll();
    }

    @Test
    public void getAccounts_shouldReturnEmptyList_whenNoAccounts() throws Exception {
        //arrange
        //action
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        //assert
        var outputAccounts = objectMapper.readValue(result, AccountDTO.AccountResp[].class);
        assert(outputAccounts.length == 0);
    }

    @Test
    public void getAccounts_shouldReturnListOfAccounts_whenAccountsExist() throws Exception {
        //arrange
        var account1 = new Account();
        account1.setName("user1");
        account1.setEmail("user1@gmail.com");
        var account2 = new Account();
        account2.setName("user2");
        account2.setEmail("user2@gmail.com");

        accountRepository.saveAll(List.of(account1, account2));

        //action
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts").contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(List.of(account1, account2))))
                                            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        //assert
//        var outputAccounts = objectMapper.readValue(result, AccountDTO.AccountResp[].class);
        var outputAccounts = objectMapper.readValue(result, new TypeReference<List<AccountDTO.AccountResp>>() {});
        assert(outputAccounts.size() == 2);
    }

    @Disabled
    @Test
    public void createNewEmployee_shouldReturnEmployee_whenValid() throws Exception {
        //arrange
        var inputAccount = new AccountDTO.CreateStaffReq("hoang", "hoang@gmail.com");

        //act
        String result = mockMvc.perform(post("/api//create-new-employee").contentType(
            MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(inputAccount))).andExpect(
            status().isCreated()).andReturn().getResponse().getContentAsString();

        //assert
        var outputAccount = objectMapper.readValue(result, AccountDTO.AccountResp.class);
        assert(outputAccount.id() != null);
        assert(outputAccount.email().equals("hoang@gmail.com"));

        verify(accountRepository, times(1)).save(any(Account.class));
    }

}
