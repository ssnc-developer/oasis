package app.ssnc.io.oasis.security.config

import app.ssnc.io.oasis.config.ApiConfig.API_PATH
import app.ssnc.io.oasis.config.ApiConfig.API_VERSION
import app.ssnc.io.oasis.config.ApiConfig.AUTH_PATH
import app.ssnc.io.oasis.config.SecurityConfig.PASSWORD_STRENGTH
import app.ssnc.io.oasis.security.CustomUserDetails
import app.ssnc.io.oasis.security.jwt.JwtAuthenticationFilter
import app.ssnc.io.oasis.security.jwt.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
//@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    private lateinit var tokenProvider: JwtTokenProvider

    @Autowired
    private lateinit var userDetails: CustomUserDetails

//    @Throws(Exception::class)
//    override fun configure(http: HttpSecurity) {
////        cors()
//
////        sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//    http
//        .authorizeRequests()
//            .antMatchers(HttpMethod.POST,"/$API_VERSION/$AUTH_PATH/b2bLogin").permitAll()
//            .antMatchers("/test").permitAll()
//            .anyRequest().authenticated()
//        .and()
//        .cors()
////        exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint())
//        .and()
//        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//        .and()
//        .httpBasic()
//        .and()
//        .csrf().disable()
//
//        http.apply(JwtConfigurerAdapter(tokenProvider))
//    }

    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter = JwtAuthenticationFilter(tokenProvider, userDetails)

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity): Unit = with(http) {
        csrf().disable()

        sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        authorizeRequests()
                .antMatchers("/$API_PATH/$API_VERSION/$AUTH_PATH/**").permitAll()
//            .antMatchers("/$API_VERSION/$API_PATH/emp/**").permitAll()
//            .antMatchers("/$API_VERSION/$CORE_PATH/**").permitAll()
//			.antMatchers("/$API_VERSION/$SHOP_PATH/**").permitAll()
                .anyRequest().authenticated()

//		apply(JwtConfigurerAdapter(tokenProvider))
        addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder(PASSWORD_STRENGTH)

//    @Bean
//    fun authManager(): AuthenticationManager = authenticationManagerBean()

//    @Bean
//    open fun unauthorizedEntryPoint(): AuthenticationEntryPoint {
//        return AuthenticationEntryPoint { request, response, authException -> response.sendError(
//            HttpStatus.UNAUTHORIZED.value(),
//            HttpStatus.UNAUTHORIZED.getReasonPhrase()) }
//    }

//    @Bean
//    fun passwordEncoder(): PasswordEncoder {
//        return BCryptPasswordEncoder()
//    }
//
//    override
//    @Bean(name = arrayOf("authenticationManagerBean"))
//    @Throws(Exception::class)
//    fun authenticationManagerBean(): AuthenticationManager {
//        return super.authenticationManagerBean()
//    }
//
//    @Configuration
//    protected class AuthenticationConfiguration(
//        private val userDetailsService: CustomUserDetails
//    )  : GlobalAuthenticationConfigurerAdapter(){
//
//        @Throws(Exception::class)
//        override fun init(auth: AuthenticationManagerBuilder) {
//            auth.userDetailsService(userDetailsService)
//                .passwordEncoder(BCryptPasswordEncoder())
//        }
//    }
//
//    @Bean
//    fun corsConfigurationSource(): CorsConfigurationSource {
//        val configuration = CorsConfiguration()
//        configuration.allowedOrigins = Arrays.asList("http://dredear.nolit.net", "http://localhost:4080")
//        configuration.allowedMethods = Arrays.asList("GET", "POST", "PATCH", "PUT", "DELETE")
//        configuration.allowedHeaders = Arrays.asList("*")
//        configuration.allowCredentials = true
//        configuration.maxAge = 3600
//        val source = UrlBasedCorsConfigurationSource()
//        source.registerCorsConfiguration("/**", configuration)
//        return source
//    }
}