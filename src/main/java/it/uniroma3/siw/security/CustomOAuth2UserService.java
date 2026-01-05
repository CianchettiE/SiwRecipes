package it.uniroma3.siw.security;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        // Get provider (google, github, etc.)
        String provider = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        // Extract email and name
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        // Find or create user
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);

                    // Handle name (might be null for some providers)
                    if (name != null) {
                        String[] nameParts = name.split(" ", 2);
                        newUser.setName(nameParts[0]);
                        newUser.setSurname(nameParts.length > 1 ? nameParts[1] : "");
                    } else {
                        newUser.setName(email.split("@")[0]);
                        newUser.setSurname("");
                    }

                    // Set dummy password (won't be used for OAuth login)
                    newUser.setPassword("oauth_user_" + System.currentTimeMillis());
                    newUser.setRole("USER");
                    newUser.setBanned(false);
                    newUser.setOauthProvider(provider);

                    return userRepository.save(newUser);
                });

        // Update provider if user was registered locally but now logging in with OAuth
        if (user.getOauthProvider() == null || !user.getOauthProvider().equals(provider)) {
            user.setOauthProvider(provider);
            userRepository.save(user);
        }

        // Return CustomUserDetails with OAuth2 attributes
        return new CustomUserDetails(user, oauth2User.getAttributes());
    }
}
