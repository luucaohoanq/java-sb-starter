package com.orchid.orchidbe.domain.account;

import com.orchid.orchidbe.components.JwtTokenUtils;
import com.orchid.orchidbe.domain.account.AccountDTO.UpdateAccountReq;
import com.orchid.orchidbe.domain.role.Role;
import com.orchid.orchidbe.domain.role.Role.RoleName;
import com.orchid.orchidbe.domain.role.RoleService;
import com.orchid.orchidbe.repositories.AccountRepository;
import com.orchid.orchidbe.repositories.TokenRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TokenRepository tokenRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    public List<AccountDTO.AccountResp> getAll() {
        return accountRepository.findAll()
            .stream()
            .map(AccountDTO.AccountResp::fromEntity)
            .toList();
    }

    @Override
    public Account getById(Long id) {
        return accountRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found"));
    }

    @Override
    public Account getByEmail(String email) {
        return accountRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found"));
    }

    @Override
    public void add(AccountDTO.CreateAccountReq account) {

        var defaultRole = roleService.getByName(RoleName.USER);

        if (accountRepository.existsByEmail(account.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        var newAccount = new Account();
        newAccount.setName(account.name());
        newAccount.setEmail(account.email());
        newAccount.setPassword(passwordEncoder.encode(account.password()));
        newAccount.setRole(defaultRole);

        log.info("New user registered successfully");
        accountRepository.save(newAccount);
    }

    @Override
    @Transactional
    public void addEmployee(AccountDTO.CreateStaffReq account) {

        if (accountRepository.existsByEmail(account.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        var newAccount = new Account();
        newAccount.setName(account.name());
        newAccount.setEmail(account.email());
        newAccount.setPassword(passwordEncoder.encode("123456"));
        newAccount.setRole(roleService.getByName(RoleName.STAFF));

        log.info("New staff registered successfully");
        accountRepository.save(newAccount);
    }

    @Override
    @Transactional
    public void update(Long id, UpdateAccountReq account) {

        var existingAccount = accountRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found"));

        if (accountRepository.existsByEmailAndIdNot(account.email(), id)) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (StringUtils.isNotBlank(account.password())) {
            existingAccount.setPassword(passwordEncoder.encode(account.password()));
        }
        if (StringUtils.isNotBlank(account.name())) {
            existingAccount.setName(account.name());
        }
        if (StringUtils.isNotBlank(account.email())) {
            existingAccount.setEmail(account.email());
        }
        if(account.roleName() != null){
            var role = roleService.getByName(account.roleName());
            if(role == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found");
            }
            existingAccount.setRole(role);
        }
        accountRepository.save(existingAccount);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        var existingAccount = getById(id);
        accountRepository.delete(existingAccount);
    }

    @Override
    public String login(String email, String password) throws Exception {
        try{
            Optional<Account> optionalUser = accountRepository.findByEmail(email);
            if (optionalUser.isEmpty()) {
                throw new BadCredentialsException("Wrong email or password");
            }

            Account existingUser = optionalUser.get();

            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password, existingUser.getAuthorities());
            authenticationManager.authenticate(authenticationToken);
            return jwtTokenUtils.generateToken(existingUser);
        }catch (Exception e){
            log.error("Login failed for user {}: {}", email, e.getMessage());
            throw new Exception("Login failed: " + e.getMessage(), e);
        }
    }

    private void ensureEmailNotExists(String email) {
        if (accountRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }
    }
}
