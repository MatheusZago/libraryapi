package com.matheusluizago.libraryapi.config;

import com.matheusluizago.libraryapi.security.CustomAuthentication;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Configuration
@EnableWebSecurity
//Authenticates and generates the token
public class AuthorizationServerConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    @Order(1)
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception{

        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());  //Plugin to get the token infos

        //To validate token of apps made from here.
        http.oauth2ResourceServer(oauth2Rs -> oauth2Rs.jwt(Customizer.withDefaults()));

        http.formLogin(configurer -> configurer.loginPage("/login"));

        return http
                .build();
    }

    @Bean
    public TokenSettings tokenSettings(){
        return TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(Duration.ofMinutes(60))
                .refreshTokenTimeToLive(Duration.ofMinutes(90))
                .build();
    }

    @Bean
    public ClientSettings clientSettings(){
        return ClientSettings.builder()
                .requireAuthorizationConsent(false)
                .build();
    }

    //Generates JWK - Json Web Key | Key 'signs' the token
    @Bean
    public JWKSource<SecurityContext> jwkSource() throws Exception{
        RSAKey rsaKey = generateRSAKey(); //Cryptography with public and private keys
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);

    }

    private RSAKey generateRSAKey() throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        return new RSAKey
                .Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource){
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings(){
        return AuthorizationServerSettings.builder()
                .tokenEndpoint("/oauth2/token")
                .tokenIntrospectionEndpoint("/oauth2/introspect") //to get token info
                .tokenRevocationEndpoint("/oauth2/revoke")
                .authorizationEndpoint("/oauth2/authorize")
                .oidcUserInfoEndpoint("/oauth2/userinfo" ) //get exposed user info with OPEN ID CONNECT
                .jwkSetEndpoint("/oauth2/jwks") //get public key to validate
                .oidcLogoutEndpoint("/oauth2/logout")
                .build();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer(){
        return context ->{
            var principal = context.getPrincipal();

            if(principal instanceof CustomAuthentication authentication){
                OAuth2TokenType tokenType = context.getTokenType();

                if(OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)){
                    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

                    List<String> authoritiesList = authorities.stream().map(GrantedAuthority::getAuthority).toList();


                    context.getClaims()
                            //Defining the values of the token
                            .claim("authorities", authoritiesList)
                            .claim("email", authentication.getUser().get().getEmail());

                }
            }
        };
    }

}
