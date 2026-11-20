package com.example.banque.utilisateur.out;

import com.example.banque.utilisateur.Utilisateur;
import com.example.banque.utilisateur.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.Optional;

/**
 * Faux utilisateur courant : tout le monde est « app.utilisateur-fake » (alice par défaut).
 * Il sert tant que l'appli n'a pas de sécurité. À remplacer par un UtilisateurCourant
 * qui lit le SecurityContext (supprimez alors cette classe).
 */
@Component
public class UtilisateurCourantNotFake implements UtilisateurCourant {

    private final UtilisateurRepository utilisateurs;

    public UtilisateurCourantNotFake(UtilisateurRepository utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    @Override
    public Optional<Utilisateur> get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            return utilisateurs.findByLogin(authentication.getName());
        }
        return Optional.empty();
    }
}
