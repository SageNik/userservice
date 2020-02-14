package userservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import userservice.service.UserService;

@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    @Value("${oauth2.config.web.client}")
    private String webClient;
    @Value("${oauth2.config.web.secret}")
    private String webSecret;
    @Value("${oauth2.config.web.access_token_validity_seconds}")
    private Integer webAccessTokenValiditySeconds;
    @Value("${oauth2.config.web.refresh_token_validity_seconds}")
    private Integer webRefreshTokenValiditySeconds;

    @Value("${oauth2.config.server.client}")
    private String serverClient;
    @Value("${oauth2.config.server.secret}")
    private String serverSecret;
    @Value("${oauth2.config.server.access_token_validity_seconds}")
    private Integer serverAccessTokenValiditySeconds;
    @Value("${oauth2.config.server.refresh_token_validity_seconds}")
    private Integer serverRefreshTokenValiditySeconds;

//    @Value("${oauth2.config.redirect-url}")
//    private String redirectURL;

    private final UserService userService;
    private final TokenStore tokenStore;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()

                .withClient(serverClient)
                .secret(passwordEncoder.encode(serverSecret))
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read", "write")
//                .redirectUris(redirectURL)
                .accessTokenValiditySeconds(webAccessTokenValiditySeconds)
                .refreshTokenValiditySeconds(webRefreshTokenValiditySeconds)
                .resourceIds("api")

                .and()

                .withClient(webClient)
                .secret(passwordEncoder.encode(webSecret))
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read", "write")
//                .redirectUris(redirectURL)
                .accessTokenValiditySeconds(serverAccessTokenValiditySeconds)
                .refreshTokenValiditySeconds(serverRefreshTokenValiditySeconds)
                .resourceIds("api");

    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .accessTokenConverter(accessTokenConverter())
                .tokenStore(tokenStore)
                .userDetailsService(userService)
                .authenticationManager(authenticationManager);
    }

    @Bean
    JwtAccessTokenConverter accessTokenConverter() {
        return new JwtAccessTokenConverter();
    }

}
