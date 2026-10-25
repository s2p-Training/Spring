# Adding In-Memory Users Inside Spring Security Context

1. Create the PasswordEncoder Bean, inside JobPortalSecurityConfig
```java
 @Bean
    public PasswordEncoder passwordEncoder()
    {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder;
    }
```

2. Create the UserDetailsService Bean, Which Is Responsible For Fetching The Users From RAM, Database Any Other Locations.
```java

    @Bean
    public UserDetailsService userDetailsService()
    {
        // Fetching The Password Encoder
        PasswordEncoder encoder = passwordEncoder();

        // User-1 Details
        String username1 = "john";
        String password1 = "johnTest@123";
        String hashedPassword1 = encoder.encode(password1);

        // user-2 Details
        String username2 = "jane";
        String password2 = "janeTest@123";
        String hashedPassword2 = encoder.encode(password2);

        // user-2 Details
        String username3 = "kane";
        String password3 = "kaneTest@123";
        String hashedPassword3 = encoder.encode(password2);

        // Creating Two Dummy Users
        UserDetails u1 = User.builder().username(username1).authorities("ROLE_ADMIN").password(hashedPassword1).build();
        UserDetails u2 = User.builder().username(username2).authorities("ROLE_HR").password(hashedPassword2).build();
        UserDetails u3 = User.builder().username(username3).authorities("ROLE_JOB_SEEKER").password(hashedPassword3).build();

        UserDetailsService service = new InMemoryUserDetailsManager(u1,u2,u3);
        return service;

    }
```

3. Provide AuthenticationManager Bean Which Will Use The Custom UserDetailsService Bean Provided By The Programmer.
```java
   @Bean
    public AuthenticationManager authenticationManager()
    {
        AuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService());
        ProviderManager providerManager = new ProviderManager(provider);
        return providerManager;
    }
```